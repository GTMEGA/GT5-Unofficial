package gregtech.common.items;


import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;


public class GT_Item_MiningExplosive extends ItemBlock {

    private final String mName;

    public GT_Item_MiningExplosive(final Block block) {
        super(block);
        this.mName = "gt." + "mining_explosives";
        setMaxDamage(0);
        setHasSubtypes(false);
        setCreativeTab(GregTech_API.TAB_GREGTECH);
        GT_LanguageManager.addStringLocalization(mName + ".name", "Mining Explosives", true);
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
        final String[] lines = {"An ideal explosive created for extracting ore from the world",
                                "Lacks compressive power",
                                String.format("Fortune bonus of %d", GT_Values.MEFortune),
                                "Only effective on rocks, soil, and ore",
                                "Right click the center of the block to trigger",
                                "Not affected by gravity",
                                "Packs a pretty mean punch",
                                String.format("I'd take cover if I were you, you've got a little less than %d seconds", GT_Values.MEFuse / 20)};
        lore.addAll(Arrays.asList(lines));
    }

}
