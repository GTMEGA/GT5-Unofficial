package gregtech.loaders.preload.metatileentity;

import gregtech.api.metatileentity.implementations.*;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_OutputBus_ME;

import static gregtech.api.enums.ItemList.*;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.OrePrefixes.*;
import static gregtech.api.util.GT_ModHandler.*;
import static gregtech.loaders.preload.GT_Loader_MetaTileEntities.DISMANTLEABLE_RECIPE_MASK;

public final class HatchLoader {
    private static boolean LOADED = false;

    public static void load() {
        if (LOADED)
            throw new RuntimeException("Already loaded!");
        Hatch_Dynamo_ULV.set(new GT_MetaTileEntity_Hatch_Dynamo(30, "hatch.dynamo.tier.00", "ULV Energy Output Hatch 1A", 0).getStackForm(1L));
        Hatch_Dynamo_LV.set(new GT_MetaTileEntity_Hatch_Dynamo(31, "hatch.dynamo.tier.01", "LV Energy Output Hatch 1A", 1).getStackForm(1L));
        Hatch_Dynamo_MV.set(new GT_MetaTileEntity_Hatch_Dynamo(32, "hatch.dynamo.tier.02", "MV Energy Output Hatch 1A", 2).getStackForm(1L));
        Hatch_Dynamo_HV.set(new GT_MetaTileEntity_Hatch_Dynamo(33, "hatch.dynamo.tier.03", "HV Energy Output Hatch 1A", 3).getStackForm(1L));
        Hatch_Dynamo_EV.set(new GT_MetaTileEntity_Hatch_Dynamo(34, "hatch.dynamo.tier.04", "EV Energy Output Hatch 1A", 4).getStackForm(1L));
        Hatch_Dynamo_IV.set(new GT_MetaTileEntity_Hatch_Dynamo(35, "hatch.dynamo.tier.05", "IV Energy Output Hatch 1A", 5).getStackForm(1L));
        Hatch_Dynamo_LuV.set(new GT_MetaTileEntity_Hatch_Dynamo(36, "hatch.dynamo.tier.06", "LuV Energy Output Hatch 1A", 6).getStackForm(1L));
        Hatch_Dynamo_ZPM.set(new GT_MetaTileEntity_Hatch_Dynamo(37, "hatch.dynamo.tier.07", "ZPM Energy Output Hatch 1A", 7).getStackForm(1L));
        Hatch_Dynamo_UV.set(new GT_MetaTileEntity_Hatch_Dynamo(38, "hatch.dynamo.tier.08", "UV Energy Output Hatch 1A", 8).getStackForm(1L));
        Hatch_Dynamo_MAX.set(new GT_MetaTileEntity_Hatch_Dynamo(39, "hatch.dynamo.tier.09", "UHV Energy Output Hatch 1A", 9).getStackForm(1L));

        Hatch_Energy_ULV.set(new GT_MetaTileEntity_Hatch_Energy(40, "hatch.energy.tier.00", "ULV Energy Input Hatch 2A", 0).getStackForm(1L));
        Hatch_Energy_LV.set(new GT_MetaTileEntity_Hatch_Energy(41, "hatch.energy.tier.01", "LV Energy Input Hatch 2A", 1).getStackForm(1L));
        Hatch_Energy_MV.set(new GT_MetaTileEntity_Hatch_Energy(42, "hatch.energy.tier.02", "MV Energy Input Hatch 2A", 2).getStackForm(1L));
        Hatch_Energy_HV.set(new GT_MetaTileEntity_Hatch_Energy(43, "hatch.energy.tier.03", "HV Energy Input Hatch 2A", 3).getStackForm(1L));
        Hatch_Energy_EV.set(new GT_MetaTileEntity_Hatch_Energy(44, "hatch.energy.tier.04", "EV Energy Input Hatch 2A", 4).getStackForm(1L));
        Hatch_Energy_IV.set(new GT_MetaTileEntity_Hatch_Energy(45, "hatch.energy.tier.05", "IV Energy Input Hatch 2A", 5).getStackForm(1L));
        Hatch_Energy_LuV.set(new GT_MetaTileEntity_Hatch_Energy(46, "hatch.energy.tier.06", "LuV Energy Input Hatch 2A", 6).getStackForm(1L));
        Hatch_Energy_ZPM.set(new GT_MetaTileEntity_Hatch_Energy(47, "hatch.energy.tier.07", "ZPM Energy Input Hatch 2A", 7).getStackForm(1L));
        Hatch_Energy_UV.set(new GT_MetaTileEntity_Hatch_Energy(48, "hatch.energy.tier.08", "UV Energy Input Hatch 2A", 8).getStackForm(1L));
        Hatch_Energy_MAX.set(new GT_MetaTileEntity_Hatch_Energy(49, "hatch.energy.tier.09", "UHV Energy Input Hatch 2A", 9).getStackForm(1L));

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

        Hatch_AutoMaintenance.set(new GT_MetaTileEntity_Hatch_Maintenance(111, "hatch.maintenance.auto", "Auto Maintenance Hatch", 2, true).getStackForm(1L));
        Hatch_DataAccess_EV.set(new GT_MetaTileEntity_Hatch_DataAccess(145, "hatch.dataaccess", "Data Access Hatch", 4).getStackForm(1L));
        Hatch_DataAccess_LuV.set(new GT_MetaTileEntity_Hatch_DataAccess(146, "hatch.dataaccess.adv", "Advanced Data Access Hatch", 6).getStackForm(1L));
        Hatch_DataAccess_UV.set(new GT_MetaTileEntity_Hatch_DataAccess(147, "hatch.dataaccess.auto", "Automatable Data Access Hatch", 8).getStackForm(1L));

        Hatch_Muffler_LV.set(new GT_MetaTileEntity_Hatch_Muffler(91, "hatch.muffler.tier.01", "LV Muffler Hatch", 1).getStackForm(1L));
        Hatch_Muffler_MV.set(new GT_MetaTileEntity_Hatch_Muffler(92, "hatch.muffler.tier.02", "MV Muffler Hatch", 2).getStackForm(1L));
        Hatch_Muffler_HV.set(new GT_MetaTileEntity_Hatch_Muffler(93, "hatch.muffler.tier.03", "HV Muffler Hatch", 3).getStackForm(1L));
        Hatch_Muffler_EV.set(new GT_MetaTileEntity_Hatch_Muffler(94, "hatch.muffler.tier.04", "EV Muffler Hatch", 4).getStackForm(1L));
        Hatch_Muffler_IV.set(new GT_MetaTileEntity_Hatch_Muffler(95, "hatch.muffler.tier.05", "IV Muffler Hatch", 5).getStackForm(1L));
        Hatch_Muffler_LuV.set(new GT_MetaTileEntity_Hatch_Muffler(96, "hatch.muffler.tier.06", "LuV Muffler Hatch", 6).getStackForm(1L));
        Hatch_Muffler_ZPM.set(new GT_MetaTileEntity_Hatch_Muffler(97, "hatch.muffler.tier.07", "ZPM Muffler Hatch", 7).getStackForm(1L));
        Hatch_Muffler_UV.set(new GT_MetaTileEntity_Hatch_Muffler(98, "hatch.muffler.tier.08", "UV Muffler Hatch", 8).getStackForm(1L));
        Hatch_Muffler_MAX.set(new GT_MetaTileEntity_Hatch_Muffler(99, "hatch.muffler.tier.09", "UHV Muffler Hatch", 9).getStackForm(1L));

        DynamoPowerHatch_16A_ULV.set(new GT_MetaTileEntity_Hatch_Dynamo_Power(9000, "hatch.dynamo.power_16.tier.00", "ULV Energy Output Hatch 16A", 0, 16L).getStackForm(1L));
        DynamoPowerHatch_16A_LV.set(new GT_MetaTileEntity_Hatch_Dynamo_Power(9001, "hatch.dynamo.power_16.tier.01", "LV Energy Output Hatch 16A", 1, 16L).getStackForm(1L));
        DynamoPowerHatch_16A_MV.set(new GT_MetaTileEntity_Hatch_Dynamo_Power(9002, "hatch.dynamo.power_16.tier.02", "MV Energy Output Hatch 16A", 2, 16L).getStackForm(1L));
        DynamoPowerHatch_16A_HV.set(new GT_MetaTileEntity_Hatch_Dynamo_Power(9003, "hatch.dynamo.power_16.tier.03", "HV Energy Output Hatch 16A", 3, 16L).getStackForm(1L));
        DynamoPowerHatch_16A_EV.set(new GT_MetaTileEntity_Hatch_Dynamo_Power(9004, "hatch.dynamo.power_16.tier.04", "EV Energy Output Hatch 16A", 4, 16L).getStackForm(1L));
        DynamoPowerHatch_16A_IV.set(new GT_MetaTileEntity_Hatch_Dynamo_Power(9005, "hatch.dynamo.power_16.tier.05", "IV Energy Output Hatch 16A", 5, 16L).getStackForm(1L));
        DynamoPowerHatch_16A_LuV.set(new GT_MetaTileEntity_Hatch_Dynamo_Power(9006, "hatch.dynamo.power_16.tier.06", "LuV Energy Output Hatch 16A", 6, 16L).getStackForm(1L));
        DynamoPowerHatch_16A_ZPM.set(new GT_MetaTileEntity_Hatch_Dynamo_Power(9007, "hatch.dynamo.power_16.tier.07", "ZPM Energy Output Hatch 16A", 7, 16L).getStackForm(1L));
        DynamoPowerHatch_16A_UV.set(new GT_MetaTileEntity_Hatch_Dynamo_Power(9008, "hatch.dynamo.power_16.tier.08", "UV Energy Output Hatch 16A", 8, 16L).getStackForm(1L));

        DynamoPowerHatch_64A_ULV.set(new GT_MetaTileEntity_Hatch_Dynamo_Power(9009, "hatch.dynamo.power_64.tier.00", "ULV Energy Output Hatch 64A", 0, 64L).getStackForm(1L));
        DynamoPowerHatch_64A_LV.set(new GT_MetaTileEntity_Hatch_Dynamo_Power(9010, "hatch.dynamo.power_64.tier.01", "LV Energy Output Hatch 64A", 1, 64L).getStackForm(1L));
        DynamoPowerHatch_64A_MV.set(new GT_MetaTileEntity_Hatch_Dynamo_Power(9011, "hatch.dynamo.power_64.tier.02", "MV Energy Output Hatch 64A", 2, 64L).getStackForm(1L));
        DynamoPowerHatch_64A_HV.set(new GT_MetaTileEntity_Hatch_Dynamo_Power(9012, "hatch.dynamo.power_64.tier.03", "HV Energy Output Hatch 64A", 3, 64L).getStackForm(1L));
        DynamoPowerHatch_64A_EV.set(new GT_MetaTileEntity_Hatch_Dynamo_Power(9013, "hatch.dynamo.power_64.tier.04", "EV Energy Output Hatch 64A", 4, 64L).getStackForm(1L));
        DynamoPowerHatch_64A_IV.set(new GT_MetaTileEntity_Hatch_Dynamo_Power(9014, "hatch.dynamo.power_64.tier.05", "IV Energy Output Hatch 64A", 5, 64L).getStackForm(1L));
        DynamoPowerHatch_64A_LuV.set(new GT_MetaTileEntity_Hatch_Dynamo_Power(9015, "hatch.dynamo.power_64.tier.06", "LuV Energy Output Hatch 64A", 6, 64L).getStackForm(1L));
        DynamoPowerHatch_64A_ZPM.set(new GT_MetaTileEntity_Hatch_Dynamo_Power(9016, "hatch.dynamo.power_64.tier.07", "ZPM Energy Output Hatch 64A", 7, 64L).getStackForm(1L));
        DynamoPowerHatch_64A_UV.set(new GT_MetaTileEntity_Hatch_Dynamo_Power(9017, "hatch.dynamo.power_64.tier.08", "UV Energy Output Hatch 64A", 8, 64L).getStackForm(1L));
        DynamoPowerHatch_16A_UHV.set(new GT_MetaTileEntity_Hatch_Dynamo_Power(9018, "hatch.dynamo.power_64.tier.09", "UHV Energy Output Hatch 64A", 9, 64L).getStackForm(1L));

        EnergyPowerHatch_16A_ULV.set(new GT_MetaTileEntity_Hatch_Energy_Power(9019, "hatch.energy.power_16.tier.00", "ULV Energy Input Hatch 16A", 0, 16L).getStackForm(1L));
        EnergyPowerHatch_16A_LV.set(new GT_MetaTileEntity_Hatch_Energy_Power(9020, "hatch.energy.power_16.tier.01", "LV Energy Input Hatch 16A", 1, 16L).getStackForm(1L));
        EnergyPowerHatch_16A_MV.set(new GT_MetaTileEntity_Hatch_Energy_Power(9021, "hatch.energy.power_16.tier.02", "MV Energy Input Hatch 16A", 2, 16L).getStackForm(1L));
        EnergyPowerHatch_16A_HV.set(new GT_MetaTileEntity_Hatch_Energy_Power(9022, "hatch.energy.power_16.tier.03", "HV Energy Input Hatch 16A", 3, 16L).getStackForm(1L));
        EnergyPowerHatch_16A_EV.set(new GT_MetaTileEntity_Hatch_Energy_Power(9023, "hatch.energy.power_16.tier.04", "EV Energy Input Hatch 16A", 4, 16L).getStackForm(1L));
        EnergyPowerHatch_16A_IV.set(new GT_MetaTileEntity_Hatch_Energy_Power(9024, "hatch.energy.power_16.tier.05", "IV Energy Input Hatch 16A", 5, 16L).getStackForm(1L));
        EnergyPowerHatch_16A_LuV.set(new GT_MetaTileEntity_Hatch_Energy_Power(9025, "hatch.energy.power_16.tier.06", "LuV Energy Input Hatch 16A", 6, 16L).getStackForm(1L));
        EnergyPowerHatch_16A_ZPM.set(new GT_MetaTileEntity_Hatch_Energy_Power(9026, "hatch.energy.power_16.tier.07", "ZPM Energy Input Hatch 16A", 7, 16L).getStackForm(1L));
        EnergyPowerHatch_16A_UV.set(new GT_MetaTileEntity_Hatch_Energy_Power(9027, "hatch.energy.power_16.tier.08", "UV Energy Input Hatch 16A", 8, 16L).getStackForm(1L));
        EnergyPowerHatch_16A_UHV.set(new GT_MetaTileEntity_Hatch_Energy_Power(9028, "hatch.energy.power_16.tier.09", "UHV Energy Input Hatch 16A", 9, 16L).getStackForm(1L));

        EnergyPowerHatch_64A_ULV.set(new GT_MetaTileEntity_Hatch_Energy_Power(9029, "hatch.energy.power_64.tier.00", "ULV Energy Input Hatch 64A", 0, 64L).getStackForm(1L));
        EnergyPowerHatch_64A_LV.set(new GT_MetaTileEntity_Hatch_Energy_Power(9030, "hatch.energy.power_64.tier.01", "LV Energy Input Hatch 64A", 1, 64L).getStackForm(1L));
        EnergyPowerHatch_64A_MV.set(new GT_MetaTileEntity_Hatch_Energy_Power(9031, "hatch.energy.power_64.tier.02", "MV Energy Input Hatch 64A", 2, 64L).getStackForm(1L));
        EnergyPowerHatch_64A_HV.set(new GT_MetaTileEntity_Hatch_Energy_Power(9032, "hatch.energy.power_64.tier.03", "HV Energy Input Hatch 64A", 3, 64L).getStackForm(1L));
        EnergyPowerHatch_64A_EV.set(new GT_MetaTileEntity_Hatch_Energy_Power(9033, "hatch.energy.power_64.tier.04", "EV Energy Input Hatch 64A", 4, 64L).getStackForm(1L));
        EnergyPowerHatch_64A_IV.set(new GT_MetaTileEntity_Hatch_Energy_Power(9034, "hatch.energy.power_64.tier.05", "IV Energy Input Hatch 64A", 5, 64L).getStackForm(1L));
        EnergyPowerHatch_64A_LuV.set(new GT_MetaTileEntity_Hatch_Energy_Power(9035, "hatch.energy.power_64.tier.06", "LuV Energy Input Hatch 64A", 6, 64L).getStackForm(1L));
        EnergyPowerHatch_64A_ZPM.set(new GT_MetaTileEntity_Hatch_Energy_Power(9036, "hatch.energy.power_64.tier.07", "ZPM Energy Input Hatch 64A", 7, 64L).getStackForm(1L));
        EnergyPowerHatch_64A_UV.set(new GT_MetaTileEntity_Hatch_Energy_Power(9037, "hatch.energy.power_64.tier.08", "UV Energy Input Hatch 64A", 8, 64L).getStackForm(1L));
        EnergyPowerHatch_64A_UHV.set(new GT_MetaTileEntity_Hatch_Energy_Power(9038, "hatch.energy.power_64.tier.09", "UHV Energy Input Hatch 64A", 9, 64L).getStackForm(1L));


        LOADED = true;
    }
}
