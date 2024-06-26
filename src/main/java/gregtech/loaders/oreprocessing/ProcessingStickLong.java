package gregtech.loaders.oreprocessing;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;
import net.minecraft.item.ItemStack;

public class ProcessingStickLong implements gregtech.api.interfaces.IOreRecipeRegistrator {
    public ProcessingStickLong() {
        OrePrefixes.stickLong.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.spring, aMaterial, 1L), GT_ModHandler.RecipeBits.BUFFERED, new Object[]{" s ", "fSx", " S ", 'S', OrePrefixes.stickLong.get(aMaterial)});
        if (!aMaterial.contains(SubTag.NO_WORKING)) {
            GT_Values.RA.addCutterRecipe(GT_Utility.copyAmount(1L, aStack), GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 2L), null, (int) Math.max(aMaterial.getMass(), 1L), 2);
            if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L), GT_Proxy.tBits, new Object[]{"sf", "G ", 'G', OrePrefixes.gemFlawless.get(aMaterial)});
                GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 2L), GT_Proxy.tBits, new Object[]{"sf", "G ", 'G', OrePrefixes.gemExquisite.get(aMaterial)});
            }
        }
        if (!aMaterial.contains(SubTag.NO_SMASHING)) {
            GT_Values.RA.addBenderRecipe(GT_Utility.copyAmount(1L, aStack), GT_OreDictUnificator.get(OrePrefixes.spring, aMaterial, 1L), 200, 16);
            if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial))
                GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L), GT_Proxy.tBits, new Object[]{"ShS", 'S', OrePrefixes.stick.get(aMaterial)});
        }
    }
}
