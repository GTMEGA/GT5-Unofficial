package gregtech.common.items.explosives;


import gregtech.common.misc.explosions.IGT_ExplosiveTier;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;


public class GT_Item_FlatBomb extends GT_Item_Explosive<IGT_ExplosiveTier.GT_FlatBombTier> {

    public GT_Item_FlatBomb(final Block block) {
        super(block, "flat_bomb");
    }

    /**
     * @param stack
     * @param player
     * @param list
     * @param b0
     */
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer player, final List list, final boolean b0) {
        super.addInformation(stack, player, list, b0);
    }

}
