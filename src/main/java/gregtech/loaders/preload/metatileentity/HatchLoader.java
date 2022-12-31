package gregtech.loaders.preload.metatileentity;

import gregtech.api.metatileentity.implementations.*;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_OutputBus_ME;

import static gregtech.api.enums.ItemList.*;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.OrePrefixes.*;
import static gregtech.api.util.GT_ModHandler.addCraftingRecipe;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.loaders.preload.GT_Loader_MetaTileEntities.DISMANTLEABLE_RECIPE_MASK;

public final class HatchLoader {
    private static boolean LOADED = false;

    public static void load() {
        if (LOADED)
            throw new RuntimeException("Already loaded!");
        Hatch_Dynamo_ULV.set(new GT_MetaTileEntity_Hatch_Dynamo(30, "hatch.dynamo.tier.00", "ULV Energy Output Hatch", 0).getStackForm(1L));
        Hatch_Dynamo_LV.set(new GT_MetaTileEntity_Hatch_Dynamo(31, "hatch.dynamo.tier.01", "LV Energy Output Hatch", 1).getStackForm(1L));
        Hatch_Dynamo_MV.set(new GT_MetaTileEntity_Hatch_Dynamo(32, "hatch.dynamo.tier.02", "MV Energy Output Hatch", 2).getStackForm(1L));
        Hatch_Dynamo_HV.set(new GT_MetaTileEntity_Hatch_Dynamo(33, "hatch.dynamo.tier.03", "HV Energy Output Hatch", 3).getStackForm(1L));
        Hatch_Dynamo_EV.set(new GT_MetaTileEntity_Hatch_Dynamo(34, "hatch.dynamo.tier.04", "EV Energy Output Hatch", 4).getStackForm(1L));
        Hatch_Dynamo_IV.set(new GT_MetaTileEntity_Hatch_Dynamo(35, "hatch.dynamo.tier.05", "IV Energy Output Hatch", 5).getStackForm(1L));
        Hatch_Dynamo_LuV.set(new GT_MetaTileEntity_Hatch_Dynamo(36, "hatch.dynamo.tier.06", "LuV Energy Output Hatch", 6).getStackForm(1L));
        Hatch_Dynamo_ZPM.set(new GT_MetaTileEntity_Hatch_Dynamo(37, "hatch.dynamo.tier.07", "ZPM Energy Output Hatch", 7).getStackForm(1L));
        Hatch_Dynamo_UV.set(new GT_MetaTileEntity_Hatch_Dynamo(38, "hatch.dynamo.tier.08", "UV Energy Output Hatch", 8).getStackForm(1L));
        Hatch_Dynamo_MAX.set(new GT_MetaTileEntity_Hatch_Dynamo(39, "hatch.dynamo.tier.09", "UHV Energy Output Hatch", 9).getStackForm(1L));

        addCraftingRecipe(Hatch_Dynamo_ULV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"XOL", "SMP", "XOL", 'M', Hull_ULV, 'S', spring.get(Tin), 'X',pwrcircuit.get(PWR_LV), 'O', ULV_Coil, 'L', cell.get(Lubricant), 'P', rotor.get(Tin)});
        addCraftingRecipe(Hatch_Dynamo_LV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"XOL", "SMP", "XOL", 'M', Hull_LV, 'S', spring.get(Tin), 'X', pwrcircuit.get(PWR_LV), 'O', LV_Coil, 'L', cell.get(Lubricant), 'P', Electric_Pump_LV});
        addCraftingRecipe(Hatch_Dynamo_MV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"XOL", "SMP", "XOL", 'M', Hull_MV, 'S', spring.get(Copper), 'X', pwrcircuit.get(PWR_MV), 'O', MV_Coil, 'L', cell.get(Lubricant), 'P', Electric_Pump_MV});

        Hatch_Energy_ULV.set(new GT_MetaTileEntity_Hatch_Energy(40, "hatch.energy.tier.00", "ULV Energy Input Hatch", 0).getStackForm(1L));
        Hatch_Energy_LV.set(new GT_MetaTileEntity_Hatch_Energy(41, "hatch.energy.tier.01", "LV Energy Input Hatch", 1).getStackForm(1L));
        Hatch_Energy_MV.set(new GT_MetaTileEntity_Hatch_Energy(42, "hatch.energy.tier.02", "MV Energy Input Hatch", 2).getStackForm(1L));
        Hatch_Energy_HV.set(new GT_MetaTileEntity_Hatch_Energy(43, "hatch.energy.tier.03", "HV Energy Input Hatch", 3).getStackForm(1L));
        Hatch_Energy_EV.set(new GT_MetaTileEntity_Hatch_Energy(44, "hatch.energy.tier.04", "EV Energy Input Hatch", 4).getStackForm(1L));
        Hatch_Energy_IV.set(new GT_MetaTileEntity_Hatch_Energy(45, "hatch.energy.tier.05", "IV Energy Input Hatch", 5).getStackForm(1L));
        Hatch_Energy_LuV.set(new GT_MetaTileEntity_Hatch_Energy(46, "hatch.energy.tier.06", "LuV Energy Input Hatch", 6).getStackForm(1L));
        Hatch_Energy_ZPM.set(new GT_MetaTileEntity_Hatch_Energy(47, "hatch.energy.tier.07", "ZPM Energy Input Hatch", 7).getStackForm(1L));
        Hatch_Energy_UV.set(new GT_MetaTileEntity_Hatch_Energy(48, "hatch.energy.tier.08", "UV Energy Input Hatch", 8).getStackForm(1L));
        Hatch_Energy_MAX.set(new GT_MetaTileEntity_Hatch_Energy(49, "hatch.energy.tier.09", "UHV Energy Input Hatch", 9).getStackForm(1L));

        addCraftingRecipe(Hatch_Energy_ULV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"COL", "XMP", "COL", 'M', Hull_ULV, 'C', cableGt01.get(RedAlloy), 'X',pwrcircuit.get(PWR_LV), 'O', ULV_Coil, 'L', cell.get(Lubricant), 'P', rotor.get(Tin)});
        addCraftingRecipe(Hatch_Energy_LV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"COL", "XMP", "COL", 'M', Hull_LV, 'C', cableGt01.get(Tin), 'X', pwrcircuit.get(PWR_LV), 'O', LV_Coil, 'L', cell.get(Lubricant), 'P', Electric_Pump_LV});
        addCraftingRecipe(Hatch_Energy_MV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"XOL", "CMP", "XOL", 'M', Hull_MV, 'C', cableGt01.get(Copper), 'X', pwrcircuit.get(PWR_MV), 'O', MV_Coil, 'L', cell.get(Lubricant), 'P', Electric_Pump_MV});

        Hatch_Input_ULV.set(new GT_MetaTileEntity_Hatch_Input(50, "hatch.input.tier.00", "ULV Fluid Input Hatch", 0).getStackForm(1L));
        Hatch_Input_LV.set(new GT_MetaTileEntity_Hatch_Input(51, "hatch.input.tier.01", "LV Fluid Input Hatch", 1).getStackForm(1L));
        Hatch_Input_MV.set(new GT_MetaTileEntity_Hatch_Input(52, "hatch.input.tier.02", "MV Fluid Input Hatch", 2).getStackForm(1L));
        Hatch_Input_HV.set(new GT_MetaTileEntity_Hatch_Input(53, "hatch.input.tier.03", "HV Fluid Input Hatch", 3).getStackForm(1L));
        Hatch_Input_EV.set(new GT_MetaTileEntity_Hatch_Input(54, "hatch.input.tier.04", "EV Fluid Input Hatch", 4).getStackForm(1L));
        Hatch_Input_IV.set(new GT_MetaTileEntity_Hatch_Input(55, "hatch.input.tier.05", "IV Fluid Input Hatch", 5).getStackForm(1L));
        Hatch_Input_LuV.set(new GT_MetaTileEntity_Hatch_Input(56, "hatch.input.tier.06", "LuV Fluid Input Hatch", 6).getStackForm(1L));
        Hatch_Input_ZPM.set(new GT_MetaTileEntity_Hatch_Input(57, "hatch.input.tier.07", "ZPM Fluid Input Hatch", 7).getStackForm(1L));
        Hatch_Input_UV.set(new GT_MetaTileEntity_Hatch_Input(58, "hatch.input.tier.08", "UV Fluid Input Hatch", 8).getStackForm(1L));
        Hatch_Input_MAX.set(new GT_MetaTileEntity_Hatch_Input(59, "hatch.input.tier.09", "UHV Fluid Input Hatch", 9).getStackForm(1L));

        Hatch_Output_ULV.set(new GT_MetaTileEntity_Hatch_Output(60, "hatch.output.tier.00", "ULV Fluid Output Hatch", 0).getStackForm(1L));
        Hatch_Output_LV.set(new GT_MetaTileEntity_Hatch_Output(61, "hatch.output.tier.01", "LV Fluid Output Hatch", 1).getStackForm(1L));
        Hatch_Output_MV.set(new GT_MetaTileEntity_Hatch_Output(62, "hatch.output.tier.02", "MV Fluid Output Hatch", 2).getStackForm(1L));
        Hatch_Output_HV.set(new GT_MetaTileEntity_Hatch_Output(63, "hatch.output.tier.03", "HV Fluid Output Hatch", 3).getStackForm(1L));
        Hatch_Output_EV.set(new GT_MetaTileEntity_Hatch_Output(64, "hatch.output.tier.04", "EV Fluid Output Hatch", 4).getStackForm(1L));
        Hatch_Output_IV.set(new GT_MetaTileEntity_Hatch_Output(65, "hatch.output.tier.05", "IV Fluid Output Hatch", 5).getStackForm(1L));
        Hatch_Output_LuV.set(new GT_MetaTileEntity_Hatch_Output(66, "hatch.output.tier.06", "LuV Fluid Output Hatch", 6).getStackForm(1L));
        Hatch_Output_ZPM.set(new GT_MetaTileEntity_Hatch_Output(67, "hatch.output.tier.07", "ZPM Fluid Output Hatch", 7).getStackForm(1L));
        Hatch_Output_UV.set(new GT_MetaTileEntity_Hatch_Output(68, "hatch.output.tier.08", "UV Fluid Output Hatch", 8).getStackForm(1L));
        Hatch_Output_MAX.set(new GT_MetaTileEntity_Hatch_Output(69, "hatch.output.tier.09", "UHV Fluid Output Hatch", 9).getStackForm(1L));

        Hatch_Output_Bus_ME.set(new GT_MetaTileEntity_Hatch_OutputBus_ME(2710, "hatch.output_bus.me", "ME Output Interface").getStackForm(1L));

        Hatch_Input_Bus_ULV.set(new GT_MetaTileEntity_Hatch_InputBus(70, "hatch.input_bus.tier.00", "ULV Item Input Hatch", 0).getStackForm(1L));
        Hatch_Input_Bus_LV.set(new GT_MetaTileEntity_Hatch_InputBus(71, "hatch.input_bus.tier.01", "LV Item Input Hatch", 1).getStackForm(1L));
        Hatch_Input_Bus_MV.set(new GT_MetaTileEntity_Hatch_InputBus(72, "hatch.input_bus.tier.02", "MV Item Input Hatch", 2).getStackForm(1L));
        Hatch_Input_Bus_HV.set(new GT_MetaTileEntity_Hatch_InputBus(73, "hatch.input_bus.tier.03", "HV Item Input Hatch)", 3).getStackForm(1L));
        Hatch_Input_Bus_EV.set(new GT_MetaTileEntity_Hatch_InputBus(74, "hatch.input_bus.tier.04", "EV Item Input Hatch", 4).getStackForm(1L));
        Hatch_Input_Bus_IV.set(new GT_MetaTileEntity_Hatch_InputBus(75, "hatch.input_bus.tier.05", "IV Item Input Hatch", 5).getStackForm(1L));
        Hatch_Input_Bus_LuV.set(new GT_MetaTileEntity_Hatch_InputBus(76, "hatch.input_bus.tier.06", "LuV Item Input Hatch", 6).getStackForm(1L));
        Hatch_Input_Bus_ZPM.set(new GT_MetaTileEntity_Hatch_InputBus(77, "hatch.input_bus.tier.07", "ZPM Item Input Hatch", 7).getStackForm(1L));
        Hatch_Input_Bus_UV.set(new GT_MetaTileEntity_Hatch_InputBus(78, "hatch.input_bus.tier.08", "UV Item Input Hatch", 8).getStackForm(1L));
        Hatch_Input_Bus_MAX.set(new GT_MetaTileEntity_Hatch_InputBus(79, "hatch.input_bus.tier.09", "UHV Item Input Hatch", 9).getStackForm(1L));

        Hatch_Output_Bus_ULV.set(new GT_MetaTileEntity_Hatch_OutputBus(80, "hatch.output_bus.tier.00", "ULV Item Output Hatch", 0).getStackForm(1L));
        Hatch_Output_Bus_LV.set(new GT_MetaTileEntity_Hatch_OutputBus(81, "hatch.output_bus.tier.01", "LV Item Output Hatch", 1).getStackForm(1L));
        Hatch_Output_Bus_MV.set(new GT_MetaTileEntity_Hatch_OutputBus(82, "hatch.output_bus.tier.02", "MV Item Output Hatch", 2).getStackForm(1L));
        Hatch_Output_Bus_HV.set(new GT_MetaTileEntity_Hatch_OutputBus(83, "hatch.output_bus.tier.03", "HV Item Output Hatch", 3).getStackForm(1L));
        Hatch_Output_Bus_EV.set(new GT_MetaTileEntity_Hatch_OutputBus(84, "hatch.output_bus.tier.04", "EV Item Output Hatch", 4).getStackForm(1L));
        Hatch_Output_Bus_IV.set(new GT_MetaTileEntity_Hatch_OutputBus(85, "hatch.output_bus.tier.05", "IV Item Output Hatch", 5).getStackForm(1L));
        Hatch_Output_Bus_LuV.set(new GT_MetaTileEntity_Hatch_OutputBus(86, "hatch.output_bus.tier.06", "LuV Item Output Hatch", 6).getStackForm(1L));
        Hatch_Output_Bus_ZPM.set(new GT_MetaTileEntity_Hatch_OutputBus(87, "hatch.output_bus.tier.07", "ZPM Item Output Hatch", 7).getStackForm(1L));
        Hatch_Output_Bus_UV.set(new GT_MetaTileEntity_Hatch_OutputBus(88, "hatch.output_bus.tier.08", "UV Item Output Hatch", 8).getStackForm(1L));
        Hatch_Output_Bus_MAX.set(new GT_MetaTileEntity_Hatch_OutputBus(89, "hatch.output_bus.tier.09", "UHV Item Output Hatch", 9).getStackForm(1L));

        Hatch_Maintenance.set(new GT_MetaTileEntity_Hatch_Maintenance(90, "hatch.maintenance", "Manual Maintenance Hatch", 1).getStackForm(1L));

        addCraftingRecipe(Hatch_Maintenance.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"dwx", "hMc", "fsr", 'M', Hull_LV});
        addCraftingRecipe(Hatch_Maintenance.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"dwx", "hMC", "fsr", 'M', Hull_LV, 'C', getModItem("Railcraft", "tool.crowbar", 1L, 0)});
        addCraftingRecipe(Hatch_Maintenance.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"dwx", "hMC", "fsr", 'M', Hull_LV, 'C', getModItem("Railcraft", "tool.crowbar.reinforced", 1L, 0)});
        addCraftingRecipe(Hatch_Maintenance.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"dwx", "hMC", "fsr", 'M', Hull_LV, 'C', getModItem("Railcraft", "tool.crowbar.magic", 1L, 0)});
        addCraftingRecipe(Hatch_Maintenance.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"dwx", "hMC", "fsr", 'M', Hull_LV, 'C', getModItem("Railcraft", "tool.crowbar.void", 1L, 0)});

        Hatch_AutoMaintenance.set(new GT_MetaTileEntity_Hatch_Maintenance(111, "hatch.maintenance.auto", "Auto Maintenance Hatch", 6, true).getStackForm(1L));
        Hatch_DataAccess_EV.set(new GT_MetaTileEntity_Hatch_DataAccess(145, "hatch.dataaccess", "Data Access Hatch", 4).getStackForm(1L));
        Hatch_DataAccess_LuV.set(new GT_MetaTileEntity_Hatch_DataAccess(146, "hatch.dataaccess.adv", "Advanced Data Access Hatch", 6).getStackForm(1L));
        Hatch_DataAccess_UV.set(new GT_MetaTileEntity_Hatch_DataAccess(147, "hatch.dataaccess.auto", "Automatable Data Access Hatch", 8).getStackForm(1L));

        addCraftingRecipe(Hatch_DataAccess_EV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"COC", "OMO", "COC", 'M', Hull_EV, 'O', Tool_DataStick, 'C', logiccircuit.get(LOGIC_EV)});
        addCraftingRecipe(Hatch_DataAccess_LuV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"COC", "OMO", "COC", 'M', Hull_LuV, 'O', Tool_DataOrb, 'C', logiccircuit.get(LOGIC_LUV)});
        addCraftingRecipe(Hatch_DataAccess_UV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CRC", "OMO", "CRC", 'M', Hull_UV, 'O', Tool_DataOrb, 'C', logiccircuit.get(LOGIC_UV), 'R', Robot_Arm_UV});

        addCraftingRecipe(Hatch_AutoMaintenance.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CHC", "AMA", "CHC", 'M', Hull_LuV, 'H', Hatch_Maintenance, 'A', Robot_Arm_LuV, 'C', logiccircuit.get(LOGIC_LUV)});

        Hatch_Muffler_LV.set(new GT_MetaTileEntity_Hatch_Muffler(91, "hatch.muffler.tier.01", "LV Muffler Hatch", 1).getStackForm(1L));
        Hatch_Muffler_MV.set(new GT_MetaTileEntity_Hatch_Muffler(92, "hatch.muffler.tier.02", "MV Muffler Hatch", 2).getStackForm(1L));
        Hatch_Muffler_HV.set(new GT_MetaTileEntity_Hatch_Muffler(93, "hatch.muffler.tier.03", "HV Muffler Hatch", 3).getStackForm(1L));
        Hatch_Muffler_EV.set(new GT_MetaTileEntity_Hatch_Muffler(94, "hatch.muffler.tier.04", "EV Muffler Hatch", 4).getStackForm(1L));
        Hatch_Muffler_IV.set(new GT_MetaTileEntity_Hatch_Muffler(95, "hatch.muffler.tier.05", "IV Muffler Hatch", 5).getStackForm(1L));
        Hatch_Muffler_LuV.set(new GT_MetaTileEntity_Hatch_Muffler(96, "hatch.muffler.tier.06", "LuV Muffler Hatch", 6).getStackForm(1L));
        Hatch_Muffler_ZPM.set(new GT_MetaTileEntity_Hatch_Muffler(97, "hatch.muffler.tier.07", "ZPM Muffler Hatch", 7).getStackForm(1L));
        Hatch_Muffler_UV.set(new GT_MetaTileEntity_Hatch_Muffler(98, "hatch.muffler.tier.08", "UV Muffler Hatch", 8).getStackForm(1L));
        Hatch_Muffler_MAX.set(new GT_MetaTileEntity_Hatch_Muffler(99, "hatch.muffler.tier.09", "UHV Muffler Hatch", 9).getStackForm(1L));

        addCraftingRecipe(Hatch_Muffler_LV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"MX ", "PR ", 'M', Hull_LV, 'P', pipeMedium.get(Bronze), 'R', rotor.get(Bronze), 'X', Electric_Motor_LV});
        addCraftingRecipe(Hatch_Muffler_MV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"MX ", "PR ", 'M', Hull_MV, 'P', pipeMedium.get(Steel), 'R', rotor.get(Steel), 'X', Electric_Motor_MV});

        LOADED = true;
    }
}
