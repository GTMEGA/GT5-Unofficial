package gregtech.loaders.oreprocessing;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraftforge.fluids.FluidRegistry;

public class ProcessingCrop implements gregtech.api.interfaces.IOreRecipeRegistrator {
    public ProcessingCrop() {
        OrePrefixes.crop.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, net.minecraft.item.ItemStack aStack) {
        GT_ModHandler.addCompressionRecipe(gregtech.api.util.GT_Utility.copyAmount(8L, aStack), ItemList.Plantball.get(1L));
        switch (aOreDictName) {
            case "cropTea":
                GT_Values.RA.addBrewingRecipe(aStack, FluidRegistry.WATER, FluidRegistry.getFluid("potion.tea"), false);
                GT_Values.RA.addBrewingRecipe(aStack, GT_ModHandler.getDistilledWater(1L).getFluid(), FluidRegistry.getFluid("potion.tea"), false);
                break;
            case "cropGrape":
                GT_Values.RA.addBrewingRecipe(aStack, FluidRegistry.WATER, FluidRegistry.getFluid("potion.grapejuice"), false);
                GT_Values.RA.addBrewingRecipe(aStack, GT_ModHandler.getDistilledWater(1L).getFluid(), FluidRegistry.getFluid("potion.grapejuice"), false);
                break;
            case "cropChilipepper":
                GT_ModHandler.addPulverisationRecipe(aStack, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L));
                break;
            case "cropCoffee":
                GT_ModHandler.addPulverisationRecipe(aStack, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coffee, 1L));
                break;
            case "cropPotato":
                GT_Values.RA.addSlicerRecipe(aStack, ItemList.Shape_Slicer_Flat.get(0L), ItemList.Food_Raw_PotatoChips.get(1L), 64, 4);
                GT_Values.RA.addSlicerRecipe(aStack, ItemList.Shape_Slicer_Stripes.get(0L), ItemList.Food_Raw_Fries.get(1L), 64, 4);
                GT_Values.RA.addBrewingRecipe(aStack, FluidRegistry.WATER, FluidRegistry.getFluid("potion.potatojuice"), true);
                GT_Values.RA.addBrewingRecipe(aStack, GT_ModHandler.getDistilledWater(1L).getFluid(), FluidRegistry.getFluid("potion.potatojuice"), true);
                break;
            case "cropLemon":
                GT_Values.RA.addSlicerRecipe(aStack, ItemList.Shape_Slicer_Flat.get(0L), ItemList.Food_Sliced_Lemon.get(4L), 64, 4);
                GT_Values.RA.addBrewingRecipe(aStack, FluidRegistry.WATER, FluidRegistry.getFluid("potion.lemonjuice"), false);
                GT_Values.RA.addBrewingRecipe(aStack, GT_ModHandler.getDistilledWater(1L).getFluid(), FluidRegistry.getFluid("potion.lemonjuice"), false);
                GT_Values.RA.addBrewingRecipe(aStack, FluidRegistry.getFluid("potion.vodka"), FluidRegistry.getFluid("potion.leninade"), true);
                break;
            case "cropTomato":
                GT_Values.RA.addSlicerRecipe(aStack, ItemList.Shape_Slicer_Flat.get(0L), ItemList.Food_Sliced_Tomato.get(4L), 64, 4);
                break;
            case "cropCucumber":
                GT_Values.RA.addSlicerRecipe(aStack, ItemList.Shape_Slicer_Flat.get(0L), ItemList.Food_Sliced_Cucumber.get(4L), 64, 4);
                break;
            case "cropOnion":
                GT_Values.RA.addSlicerRecipe(aStack, ItemList.Shape_Slicer_Flat.get(0L), ItemList.Food_Sliced_Onion.get(4L), 64, 4);
                break;
        }
    }
}
