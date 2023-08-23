package gregtech.api.util;


import com.gtnewhorizon.structurelib.util.Vec3Impl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWood;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;


@Getter
@RequiredArgsConstructor
public class GT_TreeBorker {

    private final @NonNull EntityPlayer player;

    private final @NonNull World world;

    private final int startX, startY, startZ;

    private final Set<Vec3Impl> positions = new HashSet<>();

    @NonNull
    public GT_TreeBorker borkTrees() {
        final Queue<Vec3Impl> queue = new ArrayDeque<>();
        final Set<Vec3Impl> seen = new HashSet<>();
        queue.add(new Vec3Impl(startX, startY, startZ));
        while (!queue.isEmpty()) {
            final Vec3Impl current = queue.poll();
            if (seen.contains(current)) {
                continue;
            }
            seen.add(current);
            final int x, y, z;
            x = current.get0();
            y = current.get1();
            z = current.get2();
            final Block block = world.getBlock(x, y, z);
            final int metadata = world.getBlockMetadata(x, y, z);
            if (!isValidBlock(block, x, y, z, metadata)) {
                continue;
            }
            positions.add(current);
            addNewPositions(queue, current);
        }
        return this;
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

    protected boolean isValidBlock(final Block block, final int x, final int y, final int z, final int metadata) {
        final ItemStack item = new ItemStack(block.getItem(world, x, y, z), 1, metadata);
        return block instanceof BlockWood || OreDictionary.getOres("logWood").stream().anyMatch(stack -> GT_Utility.areStacksEqual(stack, item));
    }

}
