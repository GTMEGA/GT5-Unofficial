package gregtech.common.items;


import gregtech.api.GregTech_API;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;


public class GT_Item_Potentiometer extends ItemBlock {

    private final String mName;

    public GT_Item_Potentiometer(final Block block) {
        super(block);
        this.mName = "gt.potentiometer";
        setMaxDamage(0);
        setHasSubtypes(false);
        setCreativeTab(GregTech_API.TAB_GREGTECH);
        GT_LanguageManager.addStringLocalization(mName + ".name", "Potentiometer", true);
    }

    /**
     * Sets the unlocalized name of this item to the string passed as the parameter, prefixed by "item."
     *
     * @param p_77655_1_
     */
    @Override
    public ItemBlock setUnlocalizedName(final String p_77655_1_) {
        return this;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     *
     * @param p_77667_1_
     */
    @Override
    public String getUnlocalizedName(final ItemStack p_77667_1_) {
        return mName;
    }

}
