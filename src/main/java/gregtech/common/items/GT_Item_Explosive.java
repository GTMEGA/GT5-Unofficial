package gregtech.common.items;


import gregtech.api.GregTech_API;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Block_Explosive;
import gregtech.common.misc.explosions.IGT_ExplosiveTier;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;


@Getter
public class GT_Item_Explosive extends ItemBlock {

    protected final IGT_ExplosiveTier<?> tier;

    private final String mName;

    /**
     * @param block
     *         The block this item is associated with
     */
    public GT_Item_Explosive(final Block block) {
        this((GT_Block_Explosive<?>) block, block != null ? ((GT_Block_Explosive<?>) block).getTier().asInterface() : null);
    }

    /**
     * @param block
     *         The block this item is associated with
     * @param tier
     *         The tier of this explosive, note that 0 will be represented as 1 in the name
     */
    public GT_Item_Explosive(final GT_Block_Explosive<?> block, final IGT_ExplosiveTier<?> tier) {
        super(block);
        this.tier  = tier;
        this.mName = tier != null ? tier.getULName(tier.getBlockName()) : block.getUnlocalizedName();
        if (block != null) {
            block.setItem(this);
        }
        setMaxDamage(0);
        setHasSubtypes(false);
        setCreativeTab(GregTech_API.TAB_GREGTECH);
        GT_LanguageManager.addStringLocalization(mName + ".name", tier != null ? tier.getELName() : "[Invalid]", true);
    }

    /**
     * Sets the unlocalized name of this item to the string passed as the parameter, prefixed by "item."
     *
     * @param newName
     */
    @Override
    public ItemBlock setUnlocalizedName(final String newName) {
        return this;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have different names based on their damage or NBT.
     *
     * @param stack
     */
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        return mName;
    }

    /**
     * @param stack
     * @param player
     * @param lore
     * @param b0
     */
    @SuppressWarnings("unchecked")
    @Override
    public final void addInformation(final ItemStack stack, final EntityPlayer player, final List lore, final boolean b0) {
        tier.addInformation(lore);
    }

    /**
     * @param stack
     * @param pass
     *
     * @return
     */
    @Override
    public boolean hasEffect(final ItemStack stack, final int pass) {
        return tier.isMagic();
    }

}
