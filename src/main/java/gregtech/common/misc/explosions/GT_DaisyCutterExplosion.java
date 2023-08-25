package gregtech.common.misc.explosions;


import gregtech.api.util.GT_TreeBorker;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

import java.util.Set;
import java.util.stream.Collectors;


public class GT_DaisyCutterExplosion extends GT_Explosion {

    private final GT_TreeBorker borker;

    public GT_DaisyCutterExplosion(
            final World world, final EntityTNTPrimed entity, final double x, final double y, final double z, final float power
                                  ) {
        super(world, entity, x, y, z, power);
        this.borker = new GT_TreeBorker(entity, world, (int) x, (int) y, (int) z, 8, 80, 2048);
    }

    /**
     *
     */
    @Override
    protected void explosionPost() {
        /* System.out.println("Starting output report");
        int i = 0;
        for (final Object oPosition: affectedBlockPositions) {
            final ChunkPosition position = (ChunkPosition) oPosition;
            System.out.printf("\t%d: %d %d %d%n", ++i, position.chunkPosX, position.chunkPosY, position.chunkPosZ);
        }
        System.out.printf("Total: %d%n", i); */
    }

    /**
     * @return
     */
    @Override
    protected int getMaxRays() {
        return super.getMaxRays() * 3;
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void explosionAPost() {
        affectedBlockPositions.addAll(borker.getPositions().stream().map(GT_TreeBorker::getChunkPosition).collect(Collectors.toSet()));
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

    /**
     * @param power
     * @param rayLength
     * @param posX
     * @param posY
     * @param posZ
     * @return
     */
    @Override
    protected boolean rayValid(final float power, final double rayLength, final double posX, final double posY, final double posZ) {
        return rayLength < 40;
    }

    /**
     * @return
     */
    @Override
    protected float getRayPowerDropRatio() {
        return super.getRayPowerDropRatio() / 16.0f;
    }

    /**
     * @return
     */
    @Override
    protected float getRayDropBump() {
        return 0.001f;
    }

    /**
     * @param chunkPositions
     * @param pos
     */
    @Override
    protected void handleChunkPosition(final Set<ChunkPosition> chunkPositions, final ChunkPosition pos) {
        super.handleChunkPosition(chunkPositions, pos);
        final int r = 1;
        final int bX = pos.chunkPosX, bY = pos.chunkPosY, bZ = pos.chunkPosZ;
        for (int x = bX - r; x <= bX + r; x++) {
            for (int y = bY - r; y <= bX + r; y++) {
                for (int z = bZ - r; z <= bX + r; z++) {
                    if (borker.hasSeen(x, y, z) || !borker.isValidBlock(x, y, z)) {
                        continue;
                    }
                    bork(x, y, z);
                }
            }
        }
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

    public boolean isPlant(final Block block, final int metadata, final int x, final int y, final int z) {
        return borker.isValidBlock(block, metadata, x, y, z) || borker.isPlant(block);
    }

    protected void bork(final int x, final int y, final int z) {
        if (borker.isValidBlock(x, y, z)) {
            borker.borkTrees(x, y, z);
        }
    }

}
