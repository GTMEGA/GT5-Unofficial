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

    private final Set<Vec3Impl> positions = new HashSet<>();

    private final Map<Block, Boolean> blocksValid = new HashMap<>();

    private final Set<Vec3Impl> seen = new HashSet<>();

    @NonNull
    public GT_TreeBorker borkTrees(final int sX, final int sY, final int sZ) {
        final Queue<Vec3Impl> queue = new ArrayDeque<>();
        queue.add(new Vec3Impl(sX, sY, sZ));
        while (!queue.isEmpty()) {
            final Vec3Impl current = queue.poll();
            if (seen.contains(current) || positions.contains(current)) {
                continue;
            }
            seen.add(current);
            final int x, y, z;
            x = current.get0();
            y = current.get1();
            z = current.get2();
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

    public boolean isValidBlock(final @NonNull Block block, final int metadata, final int x, final int y, final int z) {
        final ItemStack item = new ItemStack(block, 1, metadata);
        return blocksValid.computeIfAbsent(block, b -> isWood(b, item, metadata, x, y, z));
    }

    private static void addNewPositions(final Queue<Vec3Impl> queue, final Vec3Impl current) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    queue.add(current.add(x, y, z));
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

}
