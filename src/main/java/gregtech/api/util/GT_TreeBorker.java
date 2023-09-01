package gregtech.api.util;


import gregtech.api.enums.OrePrefixes;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

import java.util.*;


@Getter
@RequiredArgsConstructor
public class GT_TreeBorker {

    /**
     * World
     */
    private final /* @NonNull */ World world;

    /**
     * Initial position the borker starts from
     */
    private final int initialX, initialY, initialZ;

    /**
     * How far from a given plant block it will scan, negative means unlimited
     */
    private final int scanRadius;

    /**
     * Maximum radius of borking, negative means unlimited
     */
    private final int maxDistance;

    /**
     * Maximum spread from a given block, negative means unlimited
     */
    private final int maxSpread;

    /**
     * Maximum total blocks it will process, negative means unlimited
     */
    private final int maxScannable;

    private final Queue<int[]> positions = new ArrayDeque<>();

    private final Map<Block, Boolean> blocksValid = new HashMap<>();

    private final Map<Block, ItemStack> blockItemStackMap = new HashMap<>();

    private final Set<Long> seen = new HashSet<>();

    private final int i = 0;

    @NonNull
    public GT_TreeBorker borkTrees(final int sX, final int sY, final int sZ) {
        if (hasSeen(sX, sY, sZ) || !isValidBlock(sX, sY, sZ)) {
            seen.add(getLongFromCoords(sX, sY, sZ));
            return this;
        }
//        System.out.printf("Borking: %d at (%d %d %d) %n", i++, sX, sY, sZ);
        final Queue<Long> longQueue = new ArrayDeque<>();
        final Queue<int[]> posQueue = new ArrayDeque<>();
        int count = 0;
        longQueue.add(getLongFromCoords(sX, sY, sZ));
        posQueue.add(new int[]{sX, sY, sZ});
        while (!longQueue.isEmpty() && !posQueue.isEmpty()) {
            if (maxScannable > 0 && positions.size() > maxScannable) {
                break;
            }
            final long curCoord = longQueue.poll();
            final int[] coords = posQueue.poll();
            if (hasSeen(curCoord)) {
                continue;
            }
            if (outsideSearchRange(coords, sX, sY, sZ)) {
                break;
            }
            seen.add(curCoord);
            if (outsideMaxRange(coords)) {
                break;
            }
            //noinspection DataFlowIssue
            if (!isValidBlock(coords[0], coords[1], coords[2])) {
                continue;
            }
            count += 1;
            positions.add(coords);
            addNewPositions(longQueue, posQueue, curCoord, coords);
        }
//        System.out.printf("Borked %d positions%n", count);
        return this;
    }

    public Block getBlock(final int x, final int y, final int z) {
        return world.getBlock(x, y, z);
    }

    public int getMetadata(final int x, final int y, final int z) {
        return world.getBlockMetadata(x, y, z);
    }

    public boolean isValidBlock(final @NonNull Block block, final int metadata, final int x, final int y, final int z) {
        if (block == Blocks.air || block == Blocks.bedrock) {
            return false;
        }
        return blocksValid.computeIfAbsent(block,
                                           b -> isWood(b, blockItemStackMap.computeIfAbsent(b, b0 -> new ItemStack(b0, 1, metadata)), metadata, x, y, z) ||
                                                isPlant(b)
                                          );
    }

    public boolean isValidBlock(final int x, final int y, final int z) {
        return isValidBlock(getBlock(x, y, z), getMetadata(x, y, z), x, y, z);
    }

    public boolean isPlant(final Block block) {
        return (block.getMaterial() == Material.leaves) || (block.getMaterial() == Material.vine) || (block.getMaterial() == Material.plants) ||
               (block.getMaterial() == Material.gourd);
    }

    public ChunkPosition getChunkPosition(final int[] coords) {
        return new ChunkPosition(coords[0], coords[1], coords[2]);
    }

    public boolean hasSeen(final ChunkPosition chunkPosition) {
        return hasSeen(chunkPosition.chunkPosX, chunkPosition.chunkPosY, chunkPosition.chunkPosZ);
    }

    public boolean hasSeen(final int x, final int y, final int z) {
        return hasSeen(getLongFromCoords(x, y, z));
    }

    public boolean hasSeen(final long coord) {
        return seen.contains(coord);
    }

    public long getLongFromCoords(final int x, final int y, final int z) {
        final short rX, rY, rZ;
        rX = (short) (x - initialX);
        rY = (short) (y - initialY);
        rZ = (short) (z - initialZ);
        return (rX * (1L << 32L)) + (rY * (1L << 16L)) + rZ;
    }

    protected void addNewPositions(final Queue<Long> coordQueue, final Queue<int[]> posQueue, final long coord, final int[] coords) {
        final int iX, iY, iZ;
        iX = coords[0];
        iY = coords[1];
        iZ = coords[2];
        final int r = getScanRadius();
        for (int x = iX - r; x <= iX + r; x++) {
            for (int y = iY - r; y <= iY + r; y++) {
                for (int z = iZ - r; z <= iZ + r; z++) {
                    if ((x == iX && y == iY && z == iZ) || hasSeen(x, y, z) || !isValidBlock(x, y, z)) {
                        continue;
                    }
                    coordQueue.add(getLongFromCoords(x, y, z));
                    posQueue.add(new int[]{x, y, z});
                }
            }
        }
    }

    protected boolean isWood(final @NonNull Block block, final ItemStack stack, final int metadata, final int x, final int y, final int z) {
        if (block == Blocks.air || block == Blocks.bedrock) {
            return false;
        }
        return block.isWood(world, x, y, z) || OrePrefixes.log.contains(stack);
    }

    private boolean notWithinRange(final int[] coords, final int x, final int y, final int z) {
        return outsideMaxRange(coords) || outsideSearchRange(coords, x, y, z);
    }

    private boolean outsideMaxRange(final int[] coords) {
        return maxDistance > 0 && magnitude(coords[0] - initialX, coords[1] - initialY, coords[2] - initialZ) > maxDistance;
    }

    private boolean outsideSearchRange(final int[] coords, final int x, final int y, final int z) {
        return maxSpread > 0 && magnitude(coords[0] - x, coords[1] - y, coords[2] - z) > maxSpread;
    }

    public double magnitude(final int x, final int y, final int z) {
        return Math.sqrt(x * x + y * y + z * z);
    }

}
