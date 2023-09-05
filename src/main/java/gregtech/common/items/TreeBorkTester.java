package gregtech.common.items;


import gregtech.api.items.GT_Generic_Item;
import gregtech.api.util.GT_TreeBorker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public class TreeBorkTester extends GT_Generic_Item {


    public TreeBorkTester() {
        super("borker", "Borker", "The Lorax HATES this. Learn how one simple trick leads to environmental destruction", true);
    }

    /**
     * This is called when the item is used, before the block is activated.
     *
     * @param stack  The Item Stack
     * @param player The Player that used the item
     * @param world  The Current World
     * @param x      Target X Position
     * @param y      Target Y Position
     * @param z      Target Z Position
     * @param side   The side of the target hit
     * @param hitX
     * @param hitY
     * @param hitZ
     * @return Return true to prevent any further processing.
     */
    @Override
    public boolean onItemUseFirst(
            final ItemStack stack, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float hitX,
            final float hitY, final float hitZ
                                 ) {
        if (!world.isRemote) {
            final GT_TreeBorker borker = new GT_TreeBorker(world, x, y, z, 3, 64, 16, 1024, true);
            borker.borkTrees(x, y, z);
            borker.getPositions().forEach(coords -> {
                world.setBlockToAir(coords[0], coords[1], coords[2]);
            });
            return true;
        }
        return false;
    }

}
