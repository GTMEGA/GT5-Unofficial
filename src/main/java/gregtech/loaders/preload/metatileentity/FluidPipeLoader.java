package gregtech.loaders.preload.metatileentity;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Fluid;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;

public final class FluidPipeLoader {
    private static boolean LOADED = false;

    public static void load() {
        if (LOADED)
            throw new RuntimeException("Already loaded!");
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeSmall.get(Materials.Wood), new GT_MetaPipeEntity_Fluid(5101, "GT_Pipe_Wood_Small", "Small Wooden Fluid Pipe", 0.375F, Materials.Wood, 10, 350, false).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(Materials.Wood), new GT_MetaPipeEntity_Fluid(5102, "GT_Pipe_Wood", "Wooden Fluid Pipe", 0.5F, Materials.Wood, 30, 350, false).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(Materials.Wood), new GT_MetaPipeEntity_Fluid(5103, "GT_Pipe_Wood_Large", "Large Wooden Fluid Pipe", 0.75F, Materials.Wood, 60, 350, false).getStackForm(1L));

        generateFluidPipes(Materials.Copper, Materials.Copper.mName, 5110, 24, 1000, true);
        generateFluidMultiPipes(Materials.Copper, Materials.Copper.mName, 5115, 24, 1000, true);
        //
        generateFluidPipes(Materials.Lead, Materials.Lead.mName, 5093, 60, 500, true);
        generateFluidMultiPipes(Materials.Lead, Materials.Lead.mName, 5099, 60, 500, true);
        //
        generateFluidPipes(Materials.Brass, Materials.Brass.mName, 5120, 120, 2000, true);
        generateFluidMultiPipes(Materials.Brass, Materials.Brass.mName, 5125, 120, 2000, true);
        //
        generateFluidPipes(Materials.Steel, Materials.Steel.mName, 5130, 240, 2500, true);
        generateFluidMultiPipes(Materials.Steel, Materials.Steel.mName, 5135, 240, 2500, true);
        //
        generateFluidPipes(Materials.StainlessSteel, Materials.StainlessSteel.mName, 5140, 480, 3000, true);
        generateFluidMultiPipes(Materials.StainlessSteel, Materials.StainlessSteel.mName, 5145, 480, 3000, true);
        //
        generateFluidPipes(Materials.Aluminium, Materials.Aluminium.mName, 5720, 960, 4000, true);
        //
        generateFluidPipes(Materials.Titanium, Materials.Titanium.mName, 5150, 1920, 5000, true);
        generateFluidMultiPipes(Materials.Titanium, Materials.Titanium.mName, 5155, 1920, 5000, true);
        //
        generateFluidPipes(Materials.TungstenSteel, Materials.TungstenSteel.mName, 5160, 3840, 7500, true);
        generateFluidMultiPipes(Materials.TungstenSteel, Materials.TungstenSteel.mName, 5270, 3840, 7500, true);
        //
        generateFluidPipes(Materials.Polybenzimidazole, Materials.Polybenzimidazole.mName, "PBI", 5280, 960, 1000, true);
        generateFluidMultiPipes(Materials.Polybenzimidazole, Materials.Polybenzimidazole.mName, "PBI", 5290, 960, 1000, true);
        //
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeSmall.get(Materials.Ultimate), new GT_MetaPipeEntity_Fluid(5165, "GT_Pipe_HighPressure_Small", "Small High Pressure Fluid Pipe", 0.375F, Materials.Redstone, 4800, 1500, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(Materials.Ultimate), new GT_MetaPipeEntity_Fluid(5166, "GT_Pipe_HighPressure", "High Pressure Fluid Pipe", 0.5F, Materials.Redstone, 7200, 1500, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(Materials.Ultimate), new GT_MetaPipeEntity_Fluid(5167, "GT_Pipe_HighPressure_Large", "Large High Pressure Fluid Pipe", 0.75F, Materials.Redstone, 9600, 1500, true).getStackForm(1L));
        generateFluidPipes(Materials.Plastic, Materials.Plastic.mName, "Plastic", 5170, 480, 350, true);
        generateFluidMultiPipes(Materials.Plastic, Materials.Plastic.mName, "Plastic", 5175, 480, 350, true);
        generateFluidPipes(Materials.Polytetrafluoroethylene, Materials.Polytetrafluoroethylene.mName, "PTFE", 5680, 640, 600, true);
        generateFluidMultiPipes(Materials.Polytetrafluoroethylene, Materials.Polytetrafluoroethylene.mName, "PTFE", 5685, 640, 600, true);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.TungstenSteel, 1L), ItemList.Electric_Pump_EV.get(1L), GT_Utility.getIntegratedCircuit(5)}, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.Ultimate, 1L), 300, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.TungstenSteel, 1L), ItemList.Electric_Pump_IV.get(1L), GT_Utility.getIntegratedCircuit(5)}, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Ultimate, 1L), 400, 4096);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.TungstenSteel, 1L), ItemList.Electric_Pump_IV.get(2L), GT_Utility.getIntegratedCircuit(5)}, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Ultimate, 1L), 600, 7680);

        LOADED = true;
    }

    private static void generateFluidPipes(Materials aMaterial, String name, int startID, int baseCapacity, int heatCapacity, boolean gasProof) {
        generateFluidPipes(aMaterial, name, GT_LanguageManager.i18nPlaceholder ? "%material" : aMaterial.mDefaultLocalName, startID, baseCapacity, heatCapacity, gasProof);
    }

    private static void generateFluidPipes(Materials aMaterial, String name, String displayName, int startID, int baseCapacity, int heatCapacity, boolean gasProof) {
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeTiny.get(aMaterial), new GT_MetaPipeEntity_Fluid(startID, "GT_Pipe_" + name + "_Tiny", "Tiny " + displayName + " Fluid Pipe", 0.25F, aMaterial, baseCapacity / 8, heatCapacity,
                                                                                                          gasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeSmall.get(aMaterial), new GT_MetaPipeEntity_Fluid(startID + 1, "GT_Pipe_" + name + "_Small", "Small " + displayName + " Fluid Pipe", 0.375F, aMaterial, (baseCapacity * 3) / 8,
                                                                                                           heatCapacity, gasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(aMaterial), new GT_MetaPipeEntity_Fluid(startID + 2, "GT_Pipe_" + name, displayName + " Fluid Pipe", 0.5F, aMaterial, baseCapacity, heatCapacity, gasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(aMaterial), new GT_MetaPipeEntity_Fluid(startID + 3, "GT_Pipe_" + name + "_Large", "Large " + displayName + " Fluid Pipe", 0.75F, aMaterial, baseCapacity * 3,
                                                                                                           heatCapacity, gasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeHuge.get(aMaterial), new GT_MetaPipeEntity_Fluid(startID + 4, "GT_Pipe_" + name + "_Huge", "Huge " + displayName + " Fluid Pipe", 0.875F, aMaterial, baseCapacity * 8, heatCapacity,
                                                                                                          gasProof).getStackForm(1L));
    }

    private static void generateFluidMultiPipes(Materials aMaterial, String name, int startID, int baseCapacity, int heatCapacity, boolean gasProof) {
        generateFluidMultiPipes(aMaterial, name, "%material", startID, baseCapacity, heatCapacity, gasProof);
    }

    private static void generateFluidMultiPipes(Materials aMaterial, String name, String displayName, int startID, int baseCapacity, int heatCapacity, boolean gasProof) {
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeQuadruple.get(aMaterial), new GT_MetaPipeEntity_Fluid(startID, "GT_Pipe_" + name + "_Quadruple", "Quadruple " + displayName + " Fluid Pipe", 1.0F, aMaterial, baseCapacity, heatCapacity, gasProof, 4).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeNonuple.get(aMaterial), new GT_MetaPipeEntity_Fluid(startID + 1, "GT_Pipe_" + name + "_Nonuple", "Nonuple " + displayName + " Fluid Pipe", 1.0F, aMaterial, baseCapacity / 3, heatCapacity, gasProof, 9).getStackForm(1L));
    }
}
