package gregtech.common.misc;


import gregtech.api.enums.GT_Values;
import gregtech.common.blocks.GT_Block_Ores_Abstract;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


public class GT_MiningExplosion extends Explosion {

    protected final List<ItemStack> harvested = new ArrayList<>();

    protected World pubWorld;

    public GT_MiningExplosion(
            final World world, final Entity entity, final double x, final double y, final double z, final float power
                             ) {
        super(world, entity, x, y, z, power);
        this.pubWorld = world;
        this.isSmoking = true;
    }

    public GT_MiningExplosion perform() {
        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.exploder.worldObj, this)) {
            return this;
        }
        doExplosionA();
        doExplosionB(true);
        return this;
    }

    /**
     * Does the first part of the explosion (destroy blocks)
     */
    @Override
    public void doExplosionA() {
        final float ogExplosionSize = explosionSize;
        HashSet<ChunkPosition> hashSet = new HashSet<>();
        int i, j, k;
        double expX, expY, expZ;
        for (i = 0; i < getMaxRays(); ++i) {
            for (j = 0; j < getMaxRays(); ++j) {
                for (k = 0; k < getMaxRays(); ++k) {
                    if (atEdge(i, j, k)) {
                        double rayX, rayY, rayZ, length;
                        rayX = getRayValue(i);
                        rayY = getRayValue(j);
                        rayZ = getRayValue(k);
                        length = magnitude(rayX, rayY, rayZ);
                        rayX /= length;
                        rayY /= length;
                        rayZ /= length;
                        float power = getRayPower();
                        expX = explosionX;
                        expY = explosionY;
                        expZ = explosionZ;
                        for (float rayDist = getBaseRayDist(); power > 0.0f; power -= rayDist * getRayPowerDropRatio()) {
                            double rayLength = magnitude(expX - explosionX, expY - explosionY, expZ - explosionZ);
                            if (rayLength > GT_Values.MEMaxRange) {
                                break;
                            }
                            int posX, posY, posZ;
                            posX = MathHelper.floor_double(expX);
                            posY = MathHelper.floor_double(expY);
                            posZ = MathHelper.floor_double(expZ);
                            Block block = pubWorld.getBlock(posX, posY, posZ);
                            if (block.getMaterial() != Material.air) {
                                float expDrop = exploder != null ? exploder.func_145772_a(this, pubWorld, posX, posY, posZ, block)
                                                                 : block.getExplosionResistance(
                                                                         null, pubWorld, posX, posY, posZ, explosionX, explosionY, explosionZ);
                                power -= (expDrop + getRayDropBump()) * rayDist;
                            }

                            if (power > 0.0f && (exploder == null || exploder.func_145774_a(this, pubWorld, posX, posY, posZ, block, power))) {
                                hashSet.add(new ChunkPosition(posX, posY, posZ));
                            }

                            expX += rayX * rayDist;
                            expY += rayY * rayDist;
                            expZ += rayZ * rayDist;
                        }
                    }
                }
            }
        }
        //noinspection unchecked
        affectedBlockPositions.addAll(hashSet);
        explosionSize = ogExplosionSize;
    }

    public double magnitude(final double x, final double y, final double z) {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Does the second part of the explosion (sound, particles, drop spawn)
     *
     * @param b0
     */
    @Override
    public void doExplosionB(final boolean b0) {
        pubWorld.playSoundEffect(explosionX, explosionY, explosionZ, "random.explode", 4.0f, soundVolume());
        pubWorld.spawnParticle("hugeexplosion", explosionX, explosionY, explosionZ, 1.0, 0.0, 0.0);
        for (Object oPosition : affectedBlockPositions) {
            if (!(oPosition instanceof ChunkPosition)) {
                continue;
            }
            int i, j, k, meta;
            Block block;
            ChunkPosition position = (ChunkPosition) oPosition;
            i = position.chunkPosX;
            j = position.chunkPosY;
            k = position.chunkPosZ;
            block = pubWorld.getBlock(i, j, k);
            meta = pubWorld.getBlockMetadata(i, j, k);
            //
            doParticles(b0, (float) i, (float) j, (float) k);
            if (block.getMaterial() != Material.air) {
                if (block.canDropFromExplosion(this)) {
                    getDrops(block, i, j, k, meta);
                }
                block.onBlockExploded(pubWorld, i, j, k, this);
            }
        }
        processDrops();
    }

    protected int getMaxRays() {
        return GT_Values.MERays;
    }

    private boolean atEdge(final int i, final int j, final int k) {
        return atEdge(i) || atEdge(j) || atEdge(k);
    }

    private double getRayValue(final int val) {
        return ((float) val / (float) (getMaxRays() - 1)) * 2.0f - 1.0f;
    }

    private float getRayPower() {
        return explosionSize * (0.7f + pubWorld.rand.nextFloat() * 0.6f);
    }

    private static float getBaseRayDist() {
        return GT_Values.MERayBaseRayDist;
    }

    private static float getRayPowerDropRatio() {
        return GT_Values.MERayPowerDropRatio;
    }

    private static float getRayDropBump() {
        return GT_Values.MERayDropBump;
    }

    private float soundVolume() {
        return 1.0f + (pubWorld.rand.nextFloat() - pubWorld.rand.nextFloat() * 0.2f) * 0.7f;
    }

    private void doParticles(final boolean p_77279_1_, final float i, final float j, final float k) {
        if (p_77279_1_) {
            double d0 = i + pubWorld.rand.nextFloat();
            double d1 = j + pubWorld.rand.nextFloat();
            double d2 = k + pubWorld.rand.nextFloat();
            double d3 = d0 - this.explosionX;
            double d4 = d1 - this.explosionY;
            double d5 = d2 - this.explosionZ;
            double d6 = MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
            d3 /= d6;
            d4 /= d6;
            d5 /= d6;
            double d7 = 0.5D / (d6 / (double) this.explosionSize + 0.1D);
            d7 *= pubWorld.rand.nextFloat() * pubWorld.rand.nextFloat() + 0.3F;
            d3 *= d7;
            d4 *= d7;
            d5 *= d7;
            pubWorld.spawnParticle("explode", (d0 + this.explosionX) / 2.0D, (d1 + this.explosionY) / 2.0D, (d2 + this.explosionZ) / 2.0D, d3, d4, d5);
            pubWorld.spawnParticle("smoke", d0, d1, d2, d3, d4, d5);
        }
    }

    protected void getDrops(final Block block, final int i, final int j, final int k, final int meta) {
        if (!pubWorld.isRemote) {
            final float chance = getDropChance(block);
            if (doCleverDrop()) {
                ArrayList<ItemStack> items = block.getDrops(pubWorld, i, j, k, meta, getFortune());
                harvested.addAll(items.stream().filter(s -> pubWorld.rand.nextFloat() < chance).collect(Collectors.toList()));
            } else {
                block.dropBlockAsItemWithChance(pubWorld, i, j, k, meta, chance, getFortune());
            }
        }
    }

    private void processDrops() {
        EntityLivingBase entity = getExplosivePlacedBy();
        if (entity instanceof EntityPlayer) {
            harvested.forEach(stack -> {
                final EntityItem item = ((EntityPlayer) entity).dropPlayerItemWithRandomChoice(stack, false);
                item.moveEntity(0.0, 0.0, 0.0);
                item.setPosition(entity.posX, entity.posY + 0.5f, entity.posZ);
                item.delayBeforeCanPickup = 5;
            });
        } else if (entity != null) {
            harvested.forEach(stack -> spawnItem(stack, entity));
        } else {
            harvested.forEach(this::spawnItem);
        }
    }

    private boolean atEdge(final int val) {
        return val == 0 || val == getMaxRays() - 1;
    }

    protected float getDropChance(final Block block) {
        if (block instanceof BlockOre || block instanceof GT_Block_Ores_Abstract) {
            return GT_Values.MEOreChance;
        } else {
            final Material material = block.getMaterial();
            if (material == Material.ground || material == Material.sand || material == Material.clay || block instanceof BlockGrass) {
                return GT_Values.MESoilChance;
            } else if (material == Material.rock) {
                return GT_Values.MERockChance;
            }
        }
        return GT_Values.MEOtherChance;
    }

    protected boolean doCleverDrop() {
        return GT_Values.MEFancyDrops;
    }

    protected int getFortune() {
        return GT_Values.MEFortune;
    }

    protected void spawnItem(final ItemStack stack, final Entity entity) {
        spawnItem(stack, entity.posX, entity.posY, entity.posZ);
    }

    protected void spawnItem(final ItemStack stack) {
        spawnItem(stack, explosionX, explosionY, explosionZ);
    }

    protected void spawnItem(final ItemStack stack, final double x, final double y, final double z) {
        pubWorld.spawnEntityInWorld(new EntityItem(pubWorld, x, y, z, stack));
    }

}
