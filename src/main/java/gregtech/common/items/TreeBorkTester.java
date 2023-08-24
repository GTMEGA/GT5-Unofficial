package gregtech.common.items;


import gregtech.api.items.GT_Generic_Item;
import gregtech.api.util.GT_TreeBorker;
import net.minecraft.block.Block;
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
            final GT_TreeBorker borker = new GT_TreeBorker(player, world, x, y, z, 3, 64, 1024);
            borker.borkTrees(x, y, z);
            borker.getPositions().forEach(vec3 -> world.setBlockToAir(vec3.get0(), vec3.get1(), vec3.get2()));
            return true;
        }
        return false;
    }

}
