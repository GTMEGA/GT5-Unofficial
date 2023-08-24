package gregtech.api.util;


import com.gtnewhorizon.structurelib.util.Vec3Impl;
import gregtech.api.enums.OrePrefixes;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

import java.util.*;


@Getter
@RequiredArgsConstructor
public class GT_TreeBorker {

    public static Vec3 getVec3(final Vec3Impl impl) {
        return Vec3.createVectorHelper(impl.get0(), impl.get1(), impl.get2());
    }

    public static ChunkPosition getChunkPosition(final Vec3Impl impl) {
        return new ChunkPosition(impl.get0(), impl.get1(), impl.get2());
    }

    private final @NonNull Entity entity;

    private final @NonNull World world;

    private final int initialX, initialY, initialZ;

    private final int scanRadius, maxDistance, maxScannable;

    private final Queue<Vec3Impl> positions = new ArrayDeque<>();

    private final Map<Block, Boolean> blocksValid = new HashMap<>();

    private final Set<Vec3Impl> seen = new HashSet<>();

    @NonNull
    public GT_TreeBorker borkTrees(final int sX, final int sY, final int sZ) {
        if (hasSeen(sX, sY, sZ) || !isValidBlock(sX, sY, sZ)) {
            seen.add(new Vec3Impl(sX, sY, sZ));
            return this;
        }
        final Queue<Vec3Impl> queue = new ArrayDeque<>();
        queue.add(new Vec3Impl(sX, sY, sZ));
        while (!queue.isEmpty()) {
            if (maxScannable > 0 && positions.size() > maxScannable) {
                break;
            }
            final Vec3Impl current = queue.poll();
            if (hasSeen(current)) {
                continue;
            }
            seen.add(current);
            final int x, y, z;
            x = current.get0();
            y = current.get1();
            z = current.get2();
            if (maxDistance > 0 && magnitude(x - initialX, y - initialY, z - initialZ) > maxDistance) {
                continue;
            }
            final Block block = world.getBlock(x, y, z);
            final int metadata = world.getBlockMetadata(x, y, z);
            if (block == Blocks.air || !isValidBlock(block, metadata, x, y, z)) {
                continue;
            }
            positions.add(current);
            addNewPositions(queue, current);
        }
        return this;
    }

    public int numScanned() {
        return positions.size();
    }

    public double magnitude(final int x, final int y, final int z) {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public boolean isValidBlock(final @NonNull Block block, final int metadata, final int x, final int y, final int z) {
        if (block == Blocks.air || block == Blocks.bedrock) {
            return false;
        }
        final ItemStack item = new ItemStack(block, 1, metadata);
        return blocksValid.computeIfAbsent(block, b -> isWood(b, item, metadata, x, y, z) || isPlant(b));
    }

    public boolean isValidBlock(final int x, final int y, final int z) {
        return isValidBlock(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z), x, y, z);
    }

    protected void addNewPositions(final Queue<Vec3Impl> queue, final Vec3Impl current) {
        final int r = getScanRadius();
        final int iX, iY, iZ;
        iX = current.get0();
        iY = current.get1();
        iZ = current.get2();
        for (int x = iX - r; x <= iX + r; x++) {
            for (int y = iY - r; y <= iY + r; y++) {
                for (int z = iZ - r; z <= iZ + r; z++) {
                    final Vec3Impl pos = new Vec3Impl(x, y, z);
                    if (hasSeen(pos)) {
                        continue;
                    }
                    final Block block = world.getBlock(x, y, z);
                    final int metadata = world.getBlockMetadata(x, y, z);
                    if (!isValidBlock(block, metadata, x, y, z)) {
                        continue;
                    }
                    queue.add(pos);
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

    public boolean isPlant(final Block block) {
        return (block.getMaterial() == Material.leaves) || (block.getMaterial() == Material.vine) || (block.getMaterial() == Material.plants) ||
               (block.getMaterial() == Material.gourd);
    }

    public boolean hasSeen(final ChunkPosition position) {
        return hasSeen(new Vec3Impl(position.chunkPosX, position.chunkPosY, position.chunkPosZ));
    }

    public boolean hasSeen(final int x, final int y, final int z) {
        return hasSeen(new Vec3Impl(x, y, z));
    }

    public boolean hasSeen(final Vec3Impl vec3) {
        return seen.contains(vec3) || positions.contains(vec3);
    }

}
