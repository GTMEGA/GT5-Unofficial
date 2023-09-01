package gregtech.common.blocks;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;


public class GT_Item_Casings5 extends GT_Item_Casings_Abstract {

    protected static final String mCoilHeatTooltip = GT_LanguageManager.addStringLocalization("gt.coilheattooltip", "Base Heating Capacity = ");

    protected static final String mCoilUnitTooltip = GT_LanguageManager.addStringLocalization("gt.coilunittooltip", " Kelvin");

    protected static final String mCoilTierTooltip = GT_LanguageManager.addStringLocalization("gt.coiltiertooltip", "Coil Tier = ");

    public GT_Item_Casings5(Block par1) {
        super(par1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
        super.addInformation(aStack, aPlayer, aList, aF3_H);
        HeatingCoilLevel coilLevel = GT_Block_Casings5.getCoilHeatFromDamage(aStack.getItemDamage());
        aList.add(mCoilHeatTooltip + coilLevel.getHeat() + mCoilUnitTooltip);
        aList.add(mCoilTierTooltip + coilLevel.getTierName());
    }

}
