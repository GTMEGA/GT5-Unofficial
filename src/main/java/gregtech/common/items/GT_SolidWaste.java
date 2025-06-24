package gregtech.common.items;

import gregtech.api.events.GT_OreVeinLocations;
import gregtech.api.items.GT_Generic_Item;
import gregtech.common.fluids.GT_OreSlurry;
import lombok.val;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

public class GT_SolidWaste extends GT_Generic_Item {
    public GT_SolidWaste() {
        super("solidwaste",
              "Solid Waste",
              "[Waste Material]",
              true);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world,
                             int x, int y, int z, int side,
                             float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return false;
        }

        if (player != null && player.capabilities != null && !player.capabilities.isCreativeMode) {
            return false;
        }

        val chunkCoordinate = new ChunkCoordIntPair(x >> 4, z >> 4);
        var oreMix = GT_OreVeinLocations.RecordedOreVeinInChunk.get().get(world.provider.dimensionId, chunkCoordinate);

        if (oreMix == null) {
            val scannedSlurry = GT_OreVeinLocations.scanSlurryInChunkAt(world, chunkCoordinate.chunkXPos, chunkCoordinate.chunkZPos);

            oreMix = GT_OreVeinLocations.RecordedOreVeinInChunk.get().get(world.provider.dimensionId, chunkCoordinate);
        }

        player.addChatMessage(new ChatComponentText(String.format("[%s, %s] -> %s (%d / %d)",
                                                                  chunkCoordinate.chunkXPos,
                                                                  chunkCoordinate.chunkZPos,
                                                                  oreMix.oreMix,
                                                                  oreMix.oresCurrent,
                                                                  oreMix.oresPlaced)));

        return false;
    }
}
