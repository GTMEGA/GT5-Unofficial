package gregtech.common.items.explosives;


import gregtech.api.enums.GT_Values;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;


public class GT_Item_MiningExplosive extends GT_Item_Explosive {

    public GT_Item_MiningExplosive(final Block block) {
        super(block, "mining_explosives", "Mining Charge");
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
        final String[] lines = {"An extraordinary explosive for extracting ore from the world.",
                                String.format("Fortune bonus of %d", GT_Values.MEFortune),
                                "Mainly effective on ore, but will destroy most terrain.",
                                "Prime with a Remote Detonator",
                                "Packs a pretty mean punch, so take",
                                String.format("cover or you'll be mist in %d seconds!", GT_Values.MEFuse / 20)};
        lore.addAll(Arrays.asList(lines));
    }
}
