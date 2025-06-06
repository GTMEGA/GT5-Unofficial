package gregtech.loaders.oreprocessing;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;

public class ProcessingPipe implements gregtech.api.interfaces.IOreRecipeRegistrator {
    public ProcessingPipe() {
//        OrePrefixes.pipeHuge.add(this);
        OrePrefixes.pipeLarge.add(this);
        OrePrefixes.pipeMedium.add(this);
//        OrePrefixes.pipeSmall.add(this);
//        OrePrefixes.pipeTiny.add(this);
//        OrePrefixes.pipeRestrictiveHuge.add(this);
        OrePrefixes.pipeRestrictiveLarge.add(this);
        OrePrefixes.pipeRestrictiveMedium.add(this);
//        OrePrefixes.pipeRestrictiveSmall.add(this);
//        OrePrefixes.pipeRestrictiveTiny.add(this);
        OrePrefixes.pipeQuadruple.add(this);
        OrePrefixes.pipeNonuple.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        switch (aPrefix) {
//            case pipeHuge:
            case pipeLarge:
            case pipeMedium:
//            case pipeSmall:
//            case pipeTiny:
//                        GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial, 8L), GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PPP", "h w", "PPP", 'P', OrePrefixes.plate.get(aMaterial)});
//                        GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial, 6L), GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PWP", "P P", "PHP", 'P', aMaterial == Materials.Wood ? OrePrefixes.plank.get(aMaterial) : OrePrefixes.plate.get(aMaterial), 'H', aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSoftHammer : ToolDictNames.craftingToolHardHammer, 'W', aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSaw : ToolDictNames.craftingToolWrench});
                        GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 6L), GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PPP", "W H", "PPP", 'P', aMaterial == Materials.Wood ? OrePrefixes.plank.get(aMaterial) : OrePrefixes.plate.get(aMaterial), 'H', aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSoftHammer : ToolDictNames.craftingToolHardHammer, 'W', aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSaw : ToolDictNames.craftingToolWrench});
                        GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 6L), GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PPP", "H W", "PPP", 'P', aMaterial == Materials.Wood ? OrePrefixes.plank.get(aMaterial) : OrePrefixes.plate.get(aMaterial), 'H', aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSoftHammer : ToolDictNames.craftingToolHardHammer, 'W', aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSaw : ToolDictNames.craftingToolWrench});
                        GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial, 2L), GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PPP", "W H", "PPP", 'P', aMaterial == Materials.Wood ? OrePrefixes.plank.get(aMaterial) : OrePrefixes.plateDouble.get(aMaterial), 'H', aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSoftHammer : ToolDictNames.craftingToolHardHammer, 'W', aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSaw : ToolDictNames.craftingToolWrench});
                        GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial, 2L), GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PPP", "H W", "PPP", 'P', aMaterial == Materials.Wood ? OrePrefixes.plank.get(aMaterial) : OrePrefixes.plateDouble.get(aMaterial), 'H', aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSoftHammer : ToolDictNames.craftingToolHardHammer, 'W', aMaterial.contains(SubTag.WOOD) ? ToolDictNames.craftingToolSaw : ToolDictNames.craftingToolWrench});
//                        GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial, 1L), GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"DhD", "D D", "DwD", 'D', OrePrefixes.plateDouble.get(aMaterial)});
                break;
//            case pipeRestrictiveHuge:
            case pipeRestrictiveLarge:
            case pipeRestrictiveMedium:
//            case pipeRestrictiveSmall:
//            case pipeRestrictiveTiny:
                gregtech.api.enums.GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(aOreDictName.replaceFirst("Restrictive", ""), null, 1L, false, true), GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Steel, aPrefix.mSecondaryMaterial.mAmount / OrePrefixes.ring.mMaterialAmount), GT_Utility.copyAmount(1L, aStack), 20, 4);
                break;
            case pipeQuadruple:
            	GT_ModHandler.addCraftingRecipe(GT_Utility.copyAmount(1, aStack), GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PP ", "PP ", "   ", 'P', GT_OreDictUnificator.get(aOreDictName.replaceFirst("Quadruple", "Medium"), null, 1L, false, true)});
            	gregtech.api.enums.GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(aOreDictName.replaceFirst("Quadruple", "Medium"), null, 4L, false, true), ItemList.Circuit_Integrated.getWithDamage(0, 4), GT_Utility.copyAmount(1L, aStack), 40 , 8);
            	break;
            case pipeNonuple:
            	GT_ModHandler.addCraftingRecipe(GT_Utility.copyAmount(1, aStack), GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PPP", "PPP", "PPP", 'P', GT_OreDictUnificator.get(aOreDictName.replaceFirst("Nonuple", "Medium"), null, 1L, false, true)});
            	gregtech.api.enums.GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(aOreDictName.replaceFirst("Nonuple", "Medium"), null, 9L, false, true), ItemList.Circuit_Integrated.getWithDamage(0, 9), GT_Utility.copyAmount(1L, aStack), 60 , 8);
            	break;
		default:
			break;
        }
    }
}
