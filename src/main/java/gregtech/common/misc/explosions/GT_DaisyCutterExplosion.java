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
        this.borker = new GT_TreeBorker(entity, world);
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
        return power > 0.0f && rayLength < 64;
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
     * @param chunkPositions
     * @param pos
     */
    @Override
    protected void handleChunkPosition(final Set<ChunkPosition> chunkPositions, final ChunkPosition pos) {
        super.handleChunkPosition(chunkPositions, pos);
        borker.borkTrees(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
        chunkPositions.addAll(borker.getPositions().stream().map(GT_TreeBorker::getChunkPosition).collect(Collectors.toSet()));
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

}
