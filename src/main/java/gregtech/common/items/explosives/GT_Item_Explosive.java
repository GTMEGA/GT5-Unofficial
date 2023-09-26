package gregtech.common.items.explosives;


import gregtech.api.GregTech_API;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.explosives.GT_Block_Explosive;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;


public class GT_Item_Explosive extends ItemBlock {

    private final String mName;

    public GT_Item_Explosive(final Block block, final String uName, final String eName) {
        super(block);
        this.mName = "gt." + uName;
        if (block instanceof GT_Block_Explosive) {
            ((GT_Block_Explosive) block).setItem(this);
        }
        setMaxDamage(0);
        setHasSubtypes(false);
        setCreativeTab(GregTech_API.TAB_GREGTECH);
        GT_LanguageManager.addStringLocalization(mName + ".name", eName, true);
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
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     *
     * @param stack
     */
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        return mName;
    }

}
