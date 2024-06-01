package gregtech.common.items.explosives;


import gregtech.api.enums.GT_Values;
import gregtech.common.misc.explosions.IGT_ExplosiveTier;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
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
    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer player, final List list, final boolean b0) {
        super.addInformation(stack, player, list, b0);
        val tier  = getTier();
        val tParm = tier.getParameters();
        val lines = new String[]{
                "Flattens an area along the placed direction",
                String.format("Has a maximum radius of %.1f blocks and a depth of %.1f blocks", tParm.getMaxRadius(), tParm.getMaxDepth()),
                "creating a flat area. Destroys most items, with clay being an exception.",
                "Prime with a Remote Detonator",
                "Packs a pretty mean punch, so take",
                String.format("cover or you'll be mist in %d seconds!", GT_Values.MEFuse / 20)
        };
        list.addAll(Arrays.asList(lines));
    }

}
