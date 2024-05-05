package gregtech.common.misc.explosions;


import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_TreeBorker;
import gregtech.common.entities.explosives.GT_Entity_DaisyCutterExplosive;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

import java.util.stream.Collectors;

import static gregtech.common.misc.explosions.IGT_ExplosiveTier.GT_DaisyCutterTier;


public class GT_DaisyCutterExplosion extends GT_Explosion<GT_DaisyCutterTier> {

    private final GT_TreeBorker borker;

    // private final Set<ChunkPosition> tempSet = new HashSet<>();

    public GT_DaisyCutterExplosion(
            final World world, final GT_Entity_DaisyCutterExplosive entity, final double x, final double y, final double z, final float power
                                  ) {
        super(world, entity, x, y, z, power);
        int iX, iY, iZ;
        iX = MathHelper.floor_double(x);
        iY = MathHelper.floor_double(y);
        iZ = MathHelper.floor_double(z);
        this.borker = new GT_TreeBorker(world, iX, iY, iZ, getMaxScanDepth(), getMaxDistance(), -1, -1, true);
    }

    public int getMaxScanDepth() {
        return 3;
    }

    public int getMaxDistance() {
        return (int) getTier().getRadius();
    }

    /**
     *
     */
    @Override
    protected void explosionPost() {
        /* tempSet.addAll(targeted);
        for (ChunkPosition position: tempSet) {
            pubWorld.setBlock(position.chunkPosX, position.chunkPosY, position.chunkPosZ, Blocks.glass, 0, 3);
        } */
    }

    /**
     * @return
     */
    @Override
    protected int getMaxRays() {
        return super.getMaxRays() / 8;
    }

    /**
     *
     */
    @Override
    protected void explosionAPost() {
        // targeted.addAll(borker.getPositions().stream().map(borker::getChunkPosition).collect(Collectors.toSet()));
    }

    /**
     * @param block
     * @param i
     * @param j
     * @param k
     */
    @Override
    protected void destroyBlock(final Block block, final int i, final int j, final int k) {
        super.destroyBlock(block, i, j, k);
        // pubWorld.setBlock(i, j, k, Blocks.glass, 0, 3);
    }

    /**
     * @param block
     * @return
     */
    @Override
    protected float getDropChance(final Block block) {
        return 0.01f;
    }

    /**
     * @param pos
     * @return
     */
    @Override
    protected boolean handlePositionPre(final ChunkPosition pos) {
        // tempSet.add(pos);
        return true;
    }

    /**
     * @param pos
     * @return
     */
    @Override
    protected boolean hasEncountered(final ChunkPosition pos) {
        return super.hasEncountered(pos) || borker.hasSeen(pos);
    }

    /**
     * @return
     */
    @Override
    protected float getRayPower() {
        return super.getRayPower() * 16.0f;
    }

    /**
     * @return
     */
    @Override
    protected float getBaseRayDist() {
        return super.getBaseRayDist() / 4.0f;
    }

    @Override
    protected boolean isRayValid(GT_Explosion_PreCalculation.Ray ray) {
        return ray.myLength < ray.maxLength;
    }

    @Override
    protected double preCalculateRayMaximumLength(GT_Explosion_PreCalculation.Ray ray) {
        return GT_Values.MEMaxRange;
    }

    /**
     * @return
     */
    @Override
    protected float getRayPowerDropRatio() {
        return super.getRayPowerDropRatio() / 16.0f;
    }

    /**
     * @param block
     * @param metadata
     * @param x
     * @param y
     * @param z
     * @return
     */
    @Override
    protected float getDamageChance(final Block block, final int metadata, final int x, final int y, final int z) {
        if (!isPlant(block, metadata, x, y, z)) {
            return 0.0f;
        }
        return 1.0f;
    }

    /**
     * @return
     */
    @Override
    protected float getRayDropBump() {
        return 0.001f;
    }

    /**
     * @param pos
     */
    @Override
    protected void handleChunkPosition(final ChunkPosition pos) {
        final int r = 8;
        final int bX = pos.chunkPosX, bY = pos.chunkPosY, bZ = pos.chunkPosZ;
        for (int x = bX - r; x <= bX + r; x++) {
            for (int y = bY - r; y <= bY + r; y++) {
                for (int z = bZ - r; z <= bZ + r; z++) {
                    // tempSet.add(new ChunkPosition(x, y, z));
                    if (!borker.isValidBlock(x, y, z) || hasEncountered(x, y, z)) {
                        continue;
                    }
                    bork(x, y, z);
                }
            }
        }
        super.handleChunkPosition(pos);
        targeted.addAll(borker.getPositions().stream().map(borker::getChunkPosition).collect(Collectors.toSet()));
    }

    protected void bork(final int x, final int y, final int z) {
        if (borker.isValidBlock(x, y, z)) {
            borker.borkTrees(x, y, z);
        }
    }

    public boolean isPlant(final Block block, final int metadata, final int x, final int y, final int z) {
        return borker.isValidBlock(block, metadata, x, y, z) || borker.isPlant(block);
    }

}
