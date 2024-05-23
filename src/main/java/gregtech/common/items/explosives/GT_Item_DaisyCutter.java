package gregtech.common.items.explosives;


import gregtech.api.enums.GT_Values;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

import static gregtech.common.misc.explosions.IGT_ExplosiveTier.GT_DaisyCutterTier;


public class GT_Item_DaisyCutter extends GT_Item_Explosive<GT_DaisyCutterTier> {

    public GT_Item_DaisyCutter(final Block block) {
        super(block, "daisy_cutter");
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     *
     * @param stack
     * @param player
     * @param lore
     * @param b0
     */
    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer player, final List lore, final boolean b0) {
        val tier = getTier();
        val tParm = tier.getParameters();
        final String[] lines = {
                "The Daisy Cutter",
                "An explosive, developed by a bygone civilization, which blows away only foliage",
                "to make room for industrial progress. Destroys most items.",
                "Prime with a Remote Detonator",
                "Packs a pretty mean punch, so take",
                String.format("Has a range of %.1f blocks", tier.getRadius()),
                // String.format("Has a %.2f %% to drop items per block", tParm.getDropChance() * 100),
                String.format("cover or you'll be mist in %d seconds!", GT_Values.MEFuse / 20)
        };
        lore.addAll(Arrays.asList(lines));
    }

}
