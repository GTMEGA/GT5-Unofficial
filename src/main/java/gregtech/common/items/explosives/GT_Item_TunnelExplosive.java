package gregtech.common.items.explosives;


import gregtech.api.enums.GT_Values;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

import static gregtech.common.misc.explosions.IGT_ExplosiveTier.GT_TunnelExplosiveTier;


public class GT_Item_TunnelExplosive extends GT_Item_Explosive<GT_TunnelExplosiveTier> {

    public GT_Item_TunnelExplosive(final Block block) {
        super(block, "tunex");
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
        val tier  = getTier();
        val tParm = tier.getParameters();
        final String[] lines = new String[]{
                "Produces a powerful blast in the placed direction,",
                String.format("Has a maximum length of %.1f blocks and a radius of %.1f blocks", tParm.getMaxLength(), tParm.getMaxRadius()),
                "creating a small tunnel. Destroys most items, with clay being an exception.",
                "Prime with a Remote Detonator",
                "Packs a pretty mean punch, so take",
                String.format("cover or you'll be mist in %d seconds!", GT_Values.MEFuse / 20)
        };
        lore.addAll(Arrays.asList(lines));
    }

}
