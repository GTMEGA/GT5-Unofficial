package gregtech.common.items;

import gregtech.api.events.GT_OreVeinLocations;
import gregtech.api.items.GT_Generic_Item;
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
              "Solid Waste Description",
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
        val oreMix = GT_OreVeinLocations.RecordedOreVeinInChunk.get(world.provider.dimensionId,
                                                                    chunkCoordinate);

        player.addChatMessage(new ChatComponentText(String.format("[%s, %s] -> %s",
                                                                  chunkCoordinate.chunkXPos,
                                                                  chunkCoordinate.chunkZPos,
                                                                  oreMix)));

        return false;
    }
}
