package gregtech.loaders.oreprocessing;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.Objects;

public class ProcessingGear implements gregtech.api.interfaces.IOreRecipeRegistrator {
    public ProcessingGear() {
        OrePrefixes.gearGtSmall.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        if (Objects.requireNonNull(aPrefix) == OrePrefixes.gearGtSmall) {
            if (aMaterial.mStandardMoltenFluid != null)
                if (!(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron)) {
                    //GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Gear_Small.get(0L), aMaterial.getMolten(144L), GT_Utility.copyAmount(1L, aStack), 16, 8);
                    GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 2L), ItemList.Shape_Mold_Gear_Small.get(0), GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, aMaterial, 1L), 140, 24);
                }
            if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial) && !aMaterial.contains(SubTag.NO_WORKING)) {
                switch (aMaterial.mName) {
                    case "Wood":
                        GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, aMaterial, 1L), GT_Proxy.tBits, new Object[]{"P ", " s", 'P', OrePrefixes.plank.get(aMaterial)});
                        break;
                    case "Stone":
                        GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, aMaterial, 1L), GT_Proxy.tBits, new Object[]{"P ", " f", 'P', OrePrefixes.stoneSmooth});
                        break;
                    default:
                        GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, aMaterial, 1L), GT_Proxy.tBits, new Object[]{" S ", "hPx", " S ", 'S', OrePrefixes.stick.get(aMaterial), 'P', OrePrefixes.plate.get(aMaterial)});
                }
            }
        }
    }
}
