package gregtech.common.misc.explosions;


import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.common.entities.GT_Entity_Explosive;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
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


@Getter
public abstract class GT_Explosion<TierType extends Enum<TierType> & IGT_ExplosiveTier<TierType>> extends Explosion {

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
        this.pubWorld    = world;
        this.isSmoking   = true;
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

    @SuppressWarnings("UnusedReturnValue")
    public GT_Explosion<TierType> perform() {
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
     * @param shouldDoParticles
     *         whether or not to spawn particles
     */
    @Override
    public void doExplosionB(final boolean shouldDoParticles) {
        playSound();
        val expParticleName = getTier().getFlavorInfo().getParticleName();
        pubWorld.spawnParticle(expParticleName, explosionX, explosionY, explosionZ, 1.0, 0.0, 0.0);
        Set<ChunkPosition>          blocksToDestroy;
        GT_Explosion_PreCalculation preCalc;
        if (gtExplosive != null && (preCalc = gtExplosive.getPreCalc()) != null) {
            // It was literally just the s ven
            blocksToDestroy = Stream.concat(targeted.stream(), preCalc.getTargetPositions().stream()).collect(Collectors.toSet());
        } else {
            blocksToDestroy = targeted;
        }
        for (ChunkPosition position : blocksToDestroy) {
            int   i, j, k, meta;
            Block block;
            i     = position.chunkPosX;
            j     = position.chunkPosY;
            k     = position.chunkPosZ;
            block = pubWorld.getBlock(i, j, k);
            meta  = pubWorld.getBlockMetadata(i, j, k);
            //
            if (shouldDoParticles) {
                doParticles(i, j, k);
            }
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
        final int minXCoord, maxXCoord, minYCoord, maxYCoord, minZCoord, maxZCoord;
        minXCoord = MathHelper.floor_double(this.explosionX - (double) this.explosionSize - 1.0D);
        maxXCoord = MathHelper.floor_double(this.explosionX + (double) this.explosionSize + 1.0D);
        minYCoord = MathHelper.floor_double(this.explosionY - (double) this.explosionSize - 1.0D);
        maxYCoord = MathHelper.floor_double(this.explosionY + (double) this.explosionSize + 1.0D);
        minZCoord = MathHelper.floor_double(this.explosionZ - (double) this.explosionSize - 1.0D);
        maxZCoord = MathHelper.floor_double(this.explosionZ + (double) this.explosionSize + 1.0D);
        final List entities = pubWorld.getEntitiesWithinAABBExcludingEntity(this.exploder, AxisAlignedBB.getBoundingBox(minXCoord, minYCoord, minZCoord, maxXCoord, maxYCoord, maxZCoord));
        net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(pubWorld, this, entities, explosionSize);
//        final Vec3 expVec = Vec3.createVectorHelper(explosionX, explosionY, explosionZ);
        entities.forEach(oEntity -> {
            if (!(oEntity instanceof Entity)) {
                return;
            }
            final Entity entity   = (Entity) oEntity;
            final double distance = entity.getDistance(explosionX, explosionY, explosionZ) / ((double) explosionSize);
            if (distance <= 1.0) {
                double disX, disY, disZ, disMag;
                disX   = entity.posX - explosionX;
                disY   = entity.getEyeHeight() - explosionY;
                disZ   = entity.posZ - explosionZ;
                disMag = magnitude(disX, disY, disZ);
                if (disMag != 0.0) {
                    double blockDensity, invDist;
                    disX /= disMag;
                    disY /= disMag;
                    disZ /= disMag;
                    // This line started causing unfathomable lag, so I had to disable it.
                    // The block density function sort of adjusted the explosion based upon the terrain so this is a bit goofy without it.
//                    blockDensity = pubWorld.getBlockDensity(expVec, entity.boundingBox);
                    blockDensity = 1.0 / 2.0;
                    invDist      = (1.0 - distance) * blockDensity;
                    if (!(entity instanceof EntityItem)) {
                        entity.attackEntityFrom(DamageSource.setExplosionSource(this), (float) ((int) ((invDist * invDist + invDist) / 2.0 * 8.0 * (double) explosionSize + 1.0)));
                    }
                    final double enchantProtection = EnchantmentProtection.func_92092_a(entity, invDist) * 3.0;
                    entity.motionX += (disX * enchantProtection) * 20 * disMag;
                    entity.motionY += (disY * enchantProtection) * 30 * disMag;
                    entity.motionZ += (disZ * enchantProtection) * 20 * disMag;
                    if (entity instanceof EntityPlayer) {
                        func_77277_b().put((EntityPlayer) entity, Vec3.createVectorHelper(disX * invDist, disY * invDist, disZ * invDist));
                    }
                }
            }
        });
    }

    protected void explosionAPost() {

    }

    protected void playSound() {
        val expSoundID = getTier().getFlavorInfo().getExplosionSoundID();
        pubWorld.playSoundEffect(explosionX, explosionY, explosionZ, GregTech_API.sSoundList.get(expSoundID), 4.0f, soundVolume());
    }

    @SuppressWarnings("unchecked")
    public @NonNull TierType getTier() {
        return (TierType) gtExplosive.getTier();
    }

    protected void doParticles(final float x, final float y, final float z) {
        double d0 = x + pubWorld.rand.nextFloat();
        double d1 = y + pubWorld.rand.nextFloat();
        double d2 = z + pubWorld.rand.nextFloat();
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

    protected void getDrops(final Block block, final int x, final int y, final int z, final int metadata) {
        if (!pubWorld.isRemote) {
            final float          chance = getDropChance(block);
            ArrayList<ItemStack> items  = block.getDrops(pubWorld, x, y, z, metadata, getFortune());
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

    protected int getFortune() {
        return getTier().getFortuneTier();
    }

    protected void spawnItem(final ItemStack stack) {
        spawnItem(stack, explosionX, explosionY, explosionZ);
    }

    protected void spawnItem(final ItemStack stack, final double x, final double y, final double z) {
        pubWorld.spawnEntityInWorld(new EntityItem(pubWorld, x, y, z, stack));
    }

    public float getBlockResistance(final int x, final int y, final int z, final Block block) {
        return getGtExplosive().defaultBlockResistance(this, pubWorld, x, y, z, block);
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

    /**
     * @return The distance a ray ought to march per iteration
     */
    protected float getBaseRayDist() {
        return GT_Values.MERayBaseRayDist;
    }

    protected abstract boolean isRayValid(final GT_Explosion_PreCalculation.Ray ray);

    protected void preprocessRay(final GT_Explosion_PreCalculation.Ray ray) {

    }

    protected abstract double preCalculateRayMaximumLength(final GT_Explosion_PreCalculation.Ray ray);

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
