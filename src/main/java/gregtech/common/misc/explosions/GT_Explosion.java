package gregtech.common.misc.explosions;


import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.common.entities.explosives.GT_Entity_Explosive;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class GT_Explosion extends Explosion {

    protected final List<ItemStack> harvested = new ArrayList<>();

    protected final Set<ChunkPosition> seen = new HashSet<>(), targeted = new HashSet<>();

    private final GT_Entity_Explosive gtExplosive;

    protected int maxX, maxY, maxZ;

    protected World pubWorld;

    public GT_Explosion(
            final World world, final GT_Entity_Explosive entity, final double x, final double y, final double z, final float power
                       ) {
        super(world, entity, x, y, z, power);
        this.gtExplosive = entity;
        this.pubWorld = world;
        this.isSmoking = true;
    }

    public int getX() {
        return (int) explosionX;
    }

    public int getY() {
        return (int) explosionY;
    }

    public int getZ() {
        return (int) explosionZ;
    }

    public GT_Explosion perform() {
        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.exploder.worldObj, this)) {
            return this;
        }
        maxX = getMaxX();
        maxY = getMaxY();
        maxZ = getMaxZ();
        doExplosionA();
        doExplosionB(true);
        explosionPost();
        return this;
    }

    public int getMaxX() {
        return getMaxRays();
    }

    public int getMaxY() {
        return getMaxRays();
    }

    public int getMaxZ() {
        return getMaxRays();
    }

    /**
     * Does the first part of the explosion (destroy blocks)
     */
    @Override
    public void doExplosionA() {
        final float ogExplosionSize = explosionSize;
        // fireRays();
        explosionSize *= 2.0F;
        doEntityStuff();
        explosionSize = ogExplosionSize;
        explosionAPost();
    }

    /**
     * Does the second part of the explosion (sound, particles, drop spawn)
     *
     * @param b0
     */
    @Override
    public void doExplosionB(final boolean b0) {
        playSound();
        pubWorld.spawnParticle("hugeexplosion", explosionX, explosionY, explosionZ, 1.0, 0.0, 0.0);
        Set<ChunkPosition> blocksToDestroy;
        GT_Explosion_PreCalculation preCalc;
        if (gtExplosive != null && (preCalc = gtExplosive.getPreCalc()) != null) {
            // It was literally just the s ven
            blocksToDestroy = Stream.concat(targeted.stream(), preCalc.getTargetPositions().stream()).collect(Collectors.toSet());
        } else {
            blocksToDestroy = targeted;
        }
        for (ChunkPosition position : blocksToDestroy) {
            int i, j, k, meta;
            Block block;
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
                destroyBlock(block, i, j, k);
            }
        }
        processDrops();
    }

    protected void explosionPost() {

    }

    protected int getMaxRays() {
        return GT_Values.MERays;
    }

    @SuppressWarnings(
            {
                    "rawtypes",
                    "unchecked"
            }
    )
    protected void doEntityStuff() {
        final int i, j, k, i2, l, j2;
        i = MathHelper.floor_double(this.explosionX - (double) this.explosionSize - 1.0D);
        j = MathHelper.floor_double(this.explosionX + (double) this.explosionSize + 1.0D);
        k = MathHelper.floor_double(this.explosionY - (double) this.explosionSize - 1.0D);
        i2 = MathHelper.floor_double(this.explosionY + (double) this.explosionSize + 1.0D);
        l = MathHelper.floor_double(this.explosionZ - (double) this.explosionSize - 1.0D);
        j2 = MathHelper.floor_double(this.explosionZ + (double) this.explosionSize + 1.0D);
        final List entities = pubWorld.getEntitiesWithinAABBExcludingEntity(this.exploder, AxisAlignedBB.getBoundingBox(i, k, l, j, i2, j2));
        net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(pubWorld, this, entities, explosionSize);
        final Vec3 expVec = Vec3.createVectorHelper(explosionX, explosionY, explosionZ);
        entities.forEach(oEntity -> {
            if (!(oEntity instanceof Entity)) {
                return;
            }
            final Entity entity = (Entity) oEntity;
            final double distance = entity.getDistance(explosionX, explosionY, explosionZ) / ((double) explosionSize);
            if (distance <= 1.0) {
                double disX, disY, disZ, disMag;
                disX = entity.posX - explosionX;
                disY = entity.getEyeHeight() - explosionY;
                disZ = entity.posZ - explosionZ;
                disMag = magnitude(disX, disY, disZ);
                if (disMag != 0.0) {
                    double blockDensity, invDist;
                    disX /= disMag;
                    disY /= disMag;
                    disZ /= disMag;
                    blockDensity = pubWorld.getBlockDensity(expVec, entity.boundingBox);
                    invDist = (1.0 - distance) * blockDensity;
                    if (!(entity instanceof EntityItem)) {
                        entity.attackEntityFrom(DamageSource.setExplosionSource(this), (float) ((int) ((invDist * invDist + invDist) / 2.0 * 8.0 * (double) explosionSize + 1.0)));
                    }
                    final double enchantProtection = EnchantmentProtection.func_92092_a(entity, invDist) * 3.0;
                    entity.motionX += (disX * enchantProtection) * 20 * disMag;
                    entity.motionY += (disY * enchantProtection) * 30 * disMag;
                    entity.motionZ += (disZ * enchantProtection) * 20 * disMag;
                    if (entity instanceof EntityPlayer) {
                        func_77277_b().put(entity, Vec3.createVectorHelper(disX * invDist, disY * invDist, disZ * invDist));
                    }
                }
            }
        });
    }

    protected void explosionAPost() {

    }

    protected void playSound() {
        pubWorld.playSoundEffect(explosionX, explosionY, explosionZ, GregTech_API.sSoundList.get(213), 4.0f, soundVolume());
    }

    protected void doParticles(final boolean p_77279_1_, final float i, final float j, final float k) {
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
            ArrayList<ItemStack> items = block.getDrops(pubWorld, i, j, k, meta, getFortune());
            harvested.addAll(items.stream().filter(s -> pubWorld.rand.nextFloat() < chance).collect(Collectors.toList()));
        }
    }

    protected void destroyBlock(final Block block, final int i, final int j, final int k) {
        block.onBlockExploded(pubWorld, i, j, k, this);
    }

    protected void processDrops() {
        harvested.forEach(this::spawnItem);
    }

    public double magnitude(final double x, final double y, final double z) {
        return Math.sqrt(x * x + y * y + z * z);
    }

    protected float soundVolume() {
        return 1.0f + (pubWorld.rand.nextFloat() - pubWorld.rand.nextFloat() * 0.2f) * 0.7f;
    }

    protected abstract float getDropChance(final Block block);

    protected boolean doCleverDrop() {
        return GT_Values.MEFancyDrops;
    }

    protected int getFortune() {
        return GT_Values.MEFortune;
    }

    protected void spawnItem(final ItemStack stack) {
        spawnItem(stack, explosionX, explosionY, explosionZ);
    }

    protected void spawnItem(final ItemStack stack, final double x, final double y, final double z) {
        pubWorld.spawnEntityInWorld(new EntityItem(pubWorld, x, y, z, stack));
    }

    protected boolean handlePositionPre(final ChunkPosition pos) {
        return true;
    }

    protected boolean hasEncountered(final int x, final int y, final int z) {
        return hasEncountered(new ChunkPosition(x, y, z));
    }

    protected boolean hasEncountered(final ChunkPosition pos) {
        return seen.contains(pos) || targeted.contains(pos);
    }

    protected float getRayPower() {
        return explosionSize * (0.7f + pubWorld.rand.nextFloat() * 0.6f);
    }

    protected float getBaseRayDist() {
        return GT_Values.MERayBaseRayDist;
    }

    protected abstract boolean rayValid(final GT_Explosion_PreCalculation.Ray ray);

    protected void preprocessRay(final GT_Explosion_PreCalculation.Ray ray) {

    }

    protected abstract double precalcRayMaxLength(final GT_Explosion_PreCalculation.Ray ray);

    protected float getRayPowerDropRatio() {
        return GT_Values.MERayPowerDropRatio;
    }

    protected boolean canDamage(final Block block, final int metadata, final int x, final int y, final int z) {
        final float chance = getDamageChance(block, metadata, x, y, z);
        if (chance <= 0) {
            return false;
        } else if (chance >= 1) {
            return true;
        }
        return chance > pubWorld.rand.nextFloat();
    }

    protected float getDamageChance(final Block block, final int metadata, final int x, final int y, final int z) {
        return 1.0f;
    }

    protected float getRayDropBump() {
        return GT_Values.MERayDropBump;
    }

    protected void handleChunkPosition(final ChunkPosition pos) {
        targeted.add(pos);
    }

    protected void processRay(final GT_Explosion_PreCalculation.Ray ray) {
    }

}
