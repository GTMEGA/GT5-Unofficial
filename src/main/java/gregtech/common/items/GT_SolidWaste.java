package gregtech.common.items;

import gregtech.api.items.GT_Generic_Item;
import gregtech.common.GT_OreVeinStats;
import lombok.val;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
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

        val chunkX = x >> 4;
        val chunkY = z >> 4;
        val stats = GT_OreVeinStats.getOreVeinStatsInChunk(world, chunkX, chunkY);

        val message = String.format("[%d, %d] -> %s (%d / %d)",
                                    chunkX,
                                    chunkY,
                                    stats.oreMix(),
                                    stats.oresCurrent(),
                                    stats.oresPlaced());

        player.addChatMessage(new ChatComponentText(message));

        return false;
    }
}
