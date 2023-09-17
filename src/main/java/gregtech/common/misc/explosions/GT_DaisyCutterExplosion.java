package gregtech.common.misc.explosions;


import gregtech.api.util.GT_TreeBorker;
import gregtech.common.entities.explosives.GT_Entity_Explosive;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

import java.util.stream.Collectors;


public class GT_DaisyCutterExplosion extends GT_Explosion {

    private final GT_TreeBorker borker;

    // private final Set<ChunkPosition> tempSet = new HashSet<>();

    public GT_DaisyCutterExplosion(
            final World world, final GT_Entity_Explosive entity, final double x, final double y, final double z, final float power
                                  ) {
        super(world, entity, x, y, z, power);
        this.borker = new GT_TreeBorker(world, MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z), 3, 64, 12, -1, true);
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
     * @return
     */
    @Override
    protected double getExpRadius() {
        return 24;
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
     *
     */
    @Override
    protected void explosionAPost() {
        // targeted.addAll(borker.getPositions().stream().map(borker::getChunkPosition).collect(Collectors.toSet()));
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
     * @param maxRadius
     * @return
     */
    @Override
    protected boolean rayValid(final float power, final double rayLength, final double posX, final double posY, final double posZ, final double maxRadius) {
        return rayLength < maxRadius;
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
