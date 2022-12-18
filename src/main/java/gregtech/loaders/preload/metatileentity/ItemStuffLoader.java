package gregtech.loaders.preload.metatileentity;

import gregtech.common.tileentities.automation.*;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_QuantumChest;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_SuperChest;

import static gregtech.api.enums.ItemList.*;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.OreDictNames.craftingChest;
import static gregtech.api.enums.OreDictNames.craftingFilter;
import static gregtech.api.enums.OrePrefixes.circuit;
import static gregtech.api.util.GT_ModHandler.RecipeBits;
import static gregtech.api.util.GT_ModHandler.addCraftingRecipe;

public final class ItemStuffLoader {
    private static boolean LOADED = false;

    static void load() {
        if (LOADED)
            throw new RuntimeException("Already loaded!");
        Super_Chest_LV.set(new GT_MetaTileEntity_SuperChest(135, "super.chest.tier.01", "Super Chest I", 1).getStackForm(1L));
        Super_Chest_MV.set(new GT_MetaTileEntity_SuperChest(136, "super.chest.tier.02", "Super Chest II", 2).getStackForm(1L));
        Super_Chest_HV.set(new GT_MetaTileEntity_SuperChest(137, "super.chest.tier.03", "Super Chest III", 3).getStackForm(1L));
        Super_Chest_EV.set(new GT_MetaTileEntity_SuperChest(138, "super.chest.tier.04", "Super Chest IV", 4).getStackForm(1L));
        Super_Chest_IV.set(new GT_MetaTileEntity_SuperChest(139, "super.chest.tier.05", "Super Chest V", 5).getStackForm(1L));

        long bits = RecipeBits.NOT_REMOVABLE | RecipeBits.REVERSIBLE | RecipeBits.BUFFERED;
        long bitsd = RecipeBits.DISMANTLEABLE | RecipeBits.NOT_REMOVABLE | RecipeBits.REVERSIBLE | RecipeBits.BUFFERED;

        Automation_ChestBuffer_ULV.set(new GT_MetaTileEntity_ChestBuffer(9230, "automation.chestbuffer.tier.00", "ULV Chest Buffer", 0).getStackForm(1L));
        Automation_ChestBuffer_LV.set(new GT_MetaTileEntity_ChestBuffer(9231, "automation.chestbuffer.tier.01", "LV Chest Buffer", 1).getStackForm(1L));
        Automation_ChestBuffer_MV.set(new GT_MetaTileEntity_ChestBuffer(9232, "automation.chestbuffer.tier.02", "MV Chest Buffer", 2).getStackForm(1L));
        Automation_ChestBuffer_HV.set(new GT_MetaTileEntity_ChestBuffer(9233, "automation.chestbuffer.tier.03", "HV Chest Buffer", 3).getStackForm(1L));
        Automation_ChestBuffer_EV.set(new GT_MetaTileEntity_ChestBuffer(9234, "automation.chestbuffer.tier.04", "EV Chest Buffer", 4).getStackForm(1L));
        Automation_ChestBuffer_IV.set(new GT_MetaTileEntity_ChestBuffer(9235, "automation.chestbuffer.tier.05", "IV Chest Buffer", 5).getStackForm(1L));
        Automation_ChestBuffer_LuV.set(new GT_MetaTileEntity_ChestBuffer(9236, "automation.chestbuffer.tier.06", "LuV Chest Buffer", 6).getStackForm(1L));
        Automation_ChestBuffer_ZPM.set(new GT_MetaTileEntity_ChestBuffer(9237, "automation.chestbuffer.tier.07", "ZPM Chest Buffer", 7).getStackForm(1L));
        Automation_ChestBuffer_UV.set(new GT_MetaTileEntity_ChestBuffer(9238, "automation.chestbuffer.tier.08", "UV Chest Buffer", 8).getStackForm(1L));
        Automation_ChestBuffer_MAX.set(new GT_MetaTileEntity_ChestBuffer(9239, "automation.chestbuffer.tier.09", "UHV Chest Buffer", 9).getStackForm(1L));

        addCraftingRecipe(Automation_ChestBuffer_ULV.get(1L), bitsd, new Object[]{"CMV", " X ", 'M', Hull_ULV, 'V', Conveyor_Module_LV, 'C', craftingChest, 'X', circuit.get(Primitive)});
        addCraftingRecipe(Automation_ChestBuffer_LV.get(1L), bitsd, new Object[]{"CMV", " X ", 'M', Hull_LV, 'V', Conveyor_Module_LV, 'C', craftingChest, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_ChestBuffer_MV.get(1L), bitsd, new Object[]{"CMV", " X ", 'M', Hull_MV, 'V', Conveyor_Module_MV, 'C', craftingChest, 'X', circuit.get(Good)});
        addCraftingRecipe(Automation_ChestBuffer_HV.get(1L), bitsd, new Object[]{"CMV", " X ", 'M', Hull_HV, 'V', Conveyor_Module_HV, 'C', craftingChest, 'X', circuit.get(Advanced)});
        addCraftingRecipe(Automation_ChestBuffer_EV.get(1L), bitsd, new Object[]{"CMV", " X ", 'M', Hull_EV, 'V', Conveyor_Module_EV, 'C', craftingChest, 'X', circuit.get(Data)});
        addCraftingRecipe(Automation_ChestBuffer_IV.get(1L), bitsd, new Object[]{"CMV", " X ", 'M', Hull_IV, 'V', Conveyor_Module_IV, 'C', craftingChest, 'X', circuit.get(Elite)});
        addCraftingRecipe(Automation_ChestBuffer_LuV.get(1L), bitsd, new Object[]{"CMV", " X ", 'M', Hull_LuV, 'V', Conveyor_Module_LuV, 'C', craftingChest, 'X', circuit.get(Master)});
        addCraftingRecipe(Automation_ChestBuffer_ZPM.get(1L), bitsd, new Object[]{"CMV", " X ", 'M', Hull_ZPM, 'V', Conveyor_Module_ZPM, 'C', craftingChest, 'X', circuit.get(Ultimate)});
        addCraftingRecipe(Automation_ChestBuffer_UV.get(1L), bitsd, new Object[]{"CMV", " X ", 'M', Hull_UV, 'V', Conveyor_Module_UV, 'C', craftingChest, 'X', circuit.get(Superconductor)});
        addCraftingRecipe(Automation_ChestBuffer_MAX.get(1L), bitsd, new Object[]{"CMV", " X ", 'M', Hull_MAX, 'V', Conveyor_Module_UHV, 'C', craftingChest, 'X', circuit.get(Infinite)});

        Automation_Filter_ULV.set(new GT_MetaTileEntity_Filter(9240, "automation.filter.tier.00", "ULV Item Filter", 0).getStackForm(1L));
        Automation_Filter_LV.set(new GT_MetaTileEntity_Filter(9241, "automation.filter.tier.01", "LV Item Filter", 1).getStackForm(1L));
        Automation_Filter_MV.set(new GT_MetaTileEntity_Filter(9242, "automation.filter.tier.02", "MV Item Filter", 2).getStackForm(1L));
        Automation_Filter_HV.set(new GT_MetaTileEntity_Filter(9243, "automation.filter.tier.03", "HV Item Filter", 3).getStackForm(1L));
        Automation_Filter_EV.set(new GT_MetaTileEntity_Filter(9244, "automation.filter.tier.04", "EV Item Filter", 4).getStackForm(1L));
        Automation_Filter_IV.set(new GT_MetaTileEntity_Filter(9245, "automation.filter.tier.05", "IV Item Filter", 5).getStackForm(1L));
        Automation_Filter_LuV.set(new GT_MetaTileEntity_Filter(9246, "automation.filter.tier.06", "LuV Item Filter", 6).getStackForm(1L));
        Automation_Filter_ZPM.set(new GT_MetaTileEntity_Filter(9247, "automation.filter.tier.07", "ZPM Item Filter", 7).getStackForm(1L));
        Automation_Filter_UV.set(new GT_MetaTileEntity_Filter(9248, "automation.filter.tier.08", "UV Item Filter", 8).getStackForm(1L));
        Automation_Filter_MAX.set(new GT_MetaTileEntity_Filter(9249, "automation.filter.tier.09", "UHV Item Filter", 9).getStackForm(1L));

        addCraftingRecipe(Automation_Filter_ULV.get(1L), bitsd, new Object[]{" F ", "CMV", " X ", 'M', Hull_ULV, 'V', Electric_Motor_LV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Primitive)});
        addCraftingRecipe(Automation_Filter_LV.get(1L), bitsd, new Object[]{" F ", "CMV", " X ", 'M', Hull_LV, 'V', Conveyor_Module_LV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_Filter_MV.get(1L), bitsd, new Object[]{" F ", "CMV", " X ", 'M', Hull_MV, 'V', Conveyor_Module_MV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_Filter_HV.get(1L), bitsd, new Object[]{" F ", "CMV", " X ", 'M', Hull_HV, 'V', Conveyor_Module_HV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_Filter_EV.get(1L), bitsd, new Object[]{" F ", "CMV", " X ", 'M', Hull_EV, 'V', Conveyor_Module_EV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_Filter_IV.get(1L), bitsd, new Object[]{" F ", "CMV", " X ", 'M', Hull_IV, 'V', Conveyor_Module_IV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_Filter_LuV.get(1L), bitsd, new Object[]{" F ", "CMV", " X ", 'M', Hull_LuV, 'V', Conveyor_Module_LuV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_Filter_ZPM.get(1L), bitsd, new Object[]{" F ", "CMV", " X ", 'M', Hull_ZPM, 'V', Conveyor_Module_ZPM, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_Filter_UV.get(1L), bitsd, new Object[]{" F ", "CMV", " X ", 'M', Hull_UV, 'V', Conveyor_Module_UV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_Filter_MAX.get(1L), bitsd, new Object[]{" F ", "CMV", " X ", 'M', Hull_MAX, 'V', Conveyor_Module_UHV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});

        Automation_TypeFilter_ULV.set(new GT_MetaTileEntity_TypeFilter(9250, "automation.typefilter.tier.00", "ULV Type Filter", 0).getStackForm(1L));
        Automation_TypeFilter_LV.set(new GT_MetaTileEntity_TypeFilter(9251, "automation.typefilter.tier.01", "LV Type Filter", 1).getStackForm(1L));
        Automation_TypeFilter_MV.set(new GT_MetaTileEntity_TypeFilter(9252, "automation.typefilter.tier.02", "MV Type Filter", 2).getStackForm(1L));
        Automation_TypeFilter_HV.set(new GT_MetaTileEntity_TypeFilter(9253, "automation.typefilter.tier.03", "HV Type Filter", 3).getStackForm(1L));
        Automation_TypeFilter_EV.set(new GT_MetaTileEntity_TypeFilter(9254, "automation.typefilter.tier.04", "EV Type Filter", 4).getStackForm(1L));
        Automation_TypeFilter_IV.set(new GT_MetaTileEntity_TypeFilter(9255, "automation.typefilter.tier.05", "IV Type Filter", 5).getStackForm(1L));
        Automation_TypeFilter_LuV.set(new GT_MetaTileEntity_TypeFilter(9256, "automation.typefilter.tier.06", "LuV Type Filter", 6).getStackForm(1L));
        Automation_TypeFilter_ZPM.set(new GT_MetaTileEntity_TypeFilter(9257, "automation.typefilter.tier.07", "ZPM Type Filter", 7).getStackForm(1L));
        Automation_TypeFilter_UV.set(new GT_MetaTileEntity_TypeFilter(9258, "automation.typefilter.tier.08", "UV Type Filter", 8).getStackForm(1L));
        Automation_TypeFilter_MAX.set(new GT_MetaTileEntity_TypeFilter(9259, "automation.typefilter.tier.09", "UHV Type Filter", 9).getStackForm(1L));

        addCraftingRecipe(Automation_TypeFilter_ULV.get(1L), bitsd, new Object[]{" F ", "VMC", " X ", 'M', Hull_ULV, 'V', Conveyor_Module_LV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_TypeFilter_LV.get(1L), bitsd, new Object[]{" F ", "VMC", " X ", 'M', Hull_LV, 'V', Conveyor_Module_LV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_TypeFilter_MV.get(1L), bitsd, new Object[]{" F ", "VMC", " X ", 'M', Hull_MV, 'V', Conveyor_Module_MV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_TypeFilter_HV.get(1L), bitsd, new Object[]{" F ", "VMC", " X ", 'M', Hull_HV, 'V', Conveyor_Module_HV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_TypeFilter_EV.get(1L), bitsd, new Object[]{" F ", "VMC", " X ", 'M', Hull_EV, 'V', Conveyor_Module_EV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_TypeFilter_IV.get(1L), bitsd, new Object[]{" F ", "VMC", " X ", 'M', Hull_IV, 'V', Conveyor_Module_IV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_TypeFilter_LuV.get(1L), bitsd, new Object[]{" F ", "VMC", " X ", 'M', Hull_LuV, 'V', Conveyor_Module_LuV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_TypeFilter_ZPM.get(1L), bitsd, new Object[]{" F ", "VMC", " X ", 'M', Hull_ZPM, 'V', Conveyor_Module_ZPM, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_TypeFilter_UV.get(1L), bitsd, new Object[]{" F ", "VMC", " X ", 'M', Hull_UV, 'V', Conveyor_Module_UV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_TypeFilter_MAX.get(1L), bitsd, new Object[]{" F ", "VMC", " X ", 'M', Hull_MAX, 'V', Conveyor_Module_UHV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});

        Automation_Regulator_ULV.set(new GT_MetaTileEntity_Regulator(9270, "automation.regulator.tier.00", "ULV Regulator", 0).getStackForm(1L));
        Automation_Regulator_LV.set(new GT_MetaTileEntity_Regulator(9271, "automation.regulator.tier.01", "LV Regulator", 1).getStackForm(1L));
        Automation_Regulator_MV.set(new GT_MetaTileEntity_Regulator(9272, "automation.regulator.tier.02", "MV Regulator", 2).getStackForm(1L));
        Automation_Regulator_HV.set(new GT_MetaTileEntity_Regulator(9273, "automation.regulator.tier.03", "HV Regulator", 3).getStackForm(1L));
        Automation_Regulator_EV.set(new GT_MetaTileEntity_Regulator(9274, "automation.regulator.tier.04", "EV Regulator", 4).getStackForm(1L));
        Automation_Regulator_IV.set(new GT_MetaTileEntity_Regulator(9275, "automation.regulator.tier.05", "IV Regulator", 5).getStackForm(1L));
        Automation_Regulator_LuV.set(new GT_MetaTileEntity_Regulator(9276, "automation.regulator.tier.06", "LuV Regulator", 6).getStackForm(1L));
        Automation_Regulator_ZPM.set(new GT_MetaTileEntity_Regulator(9277, "automation.regulator.tier.07", "ZPM Regulator", 7).getStackForm(1L));
        Automation_Regulator_UV.set(new GT_MetaTileEntity_Regulator(9278, "automation.regulator.tier.08", "UV Regulator", 8).getStackForm(1L));
        Automation_Regulator_MAX.set(new GT_MetaTileEntity_Regulator(9279, "automation.regulator.tier.09", "UHV Regulator", 9).getStackForm(1L));

        addCraftingRecipe(Automation_Regulator_ULV.get(1L), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', Hull_ULV, 'V', Robot_Arm_LV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_Regulator_LV.get(1L), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', Hull_LV, 'V', Robot_Arm_LV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_Regulator_MV.get(1L), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', Hull_MV, 'V', Robot_Arm_MV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_Regulator_HV.get(1L), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', Hull_HV, 'V', Robot_Arm_HV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_Regulator_EV.get(1L), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', Hull_EV, 'V', Robot_Arm_EV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_Regulator_IV.get(1L), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', Hull_IV, 'V', Robot_Arm_IV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_Regulator_LuV.get(1L), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', Hull_LuV, 'V', Robot_Arm_LuV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_Regulator_ZPM.get(1L), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', Hull_ZPM, 'V', Robot_Arm_ZPM, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_Regulator_UV.get(1L), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', Hull_UV, 'V', Robot_Arm_UV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_Regulator_MAX.get(1L), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', Hull_MAX, 'V', Robot_Arm_UHV, 'C', craftingChest, 'F', craftingFilter, 'X', circuit.get(Basic)});

        Automation_SuperBuffer_ULV.set(new GT_MetaTileEntity_SuperBuffer(9300, "automation.superbuffer.tier.00", "ULV Super Buffer", 0).getStackForm(1L));
        Automation_SuperBuffer_LV.set(new GT_MetaTileEntity_SuperBuffer(9301, "automation.superbuffer.tier.01", "LV Super Buffer", 1).getStackForm(1L));
        Automation_SuperBuffer_MV.set(new GT_MetaTileEntity_SuperBuffer(9302, "automation.superbuffer.tier.02", "MV Super Buffer", 2).getStackForm(1L));
        Automation_SuperBuffer_HV.set(new GT_MetaTileEntity_SuperBuffer(9303, "automation.superbuffer.tier.03", "HV Super Buffer", 3).getStackForm(1L));
        Automation_SuperBuffer_EV.set(new GT_MetaTileEntity_SuperBuffer(9304, "automation.superbuffer.tier.04", "EV Super Buffer", 4).getStackForm(1L));
        Automation_SuperBuffer_IV.set(new GT_MetaTileEntity_SuperBuffer(9305, "automation.superbuffer.tier.05", "IV Super Buffer", 5).getStackForm(1L));
        Automation_SuperBuffer_LuV.set(new GT_MetaTileEntity_SuperBuffer(9306, "automation.superbuffer.tier.06", "LuV Super Buffer", 6).getStackForm(1L));
        Automation_SuperBuffer_ZPM.set(new GT_MetaTileEntity_SuperBuffer(9307, "automation.superbuffer.tier.07", "ZPM Super Buffer", 7).getStackForm(1L));
        Automation_SuperBuffer_UV.set(new GT_MetaTileEntity_SuperBuffer(9308, "automation.superbuffer.tier.08", "UV Super Buffer", 8).getStackForm(1L));
        Automation_SuperBuffer_MAX.set(new GT_MetaTileEntity_SuperBuffer(9309, "automation.superbuffer.tier.09", "UHV Super Buffer", 9).getStackForm(1L));

        addCraftingRecipe(Automation_SuperBuffer_ULV.get(1L), bitsd, new Object[]{"DMV", 'M', Automation_ChestBuffer_ULV, 'V', Conveyor_Module_LV, 'D', Tool_DataOrb});
        addCraftingRecipe(Automation_SuperBuffer_LV.get(1L), bitsd, new Object[]{"DMV", 'M', Automation_ChestBuffer_LV, 'V', Conveyor_Module_LV, 'D', Tool_DataOrb});
        addCraftingRecipe(Automation_SuperBuffer_MV.get(1L), bitsd, new Object[]{"DMV", 'M', Automation_ChestBuffer_MV, 'V', Conveyor_Module_MV, 'D', Tool_DataOrb});
        addCraftingRecipe(Automation_SuperBuffer_HV.get(1L), bitsd, new Object[]{"DMV", 'M', Automation_ChestBuffer_HV, 'V', Conveyor_Module_HV, 'D', Tool_DataOrb});
        addCraftingRecipe(Automation_SuperBuffer_EV.get(1L), bitsd, new Object[]{"DMV", 'M', Automation_ChestBuffer_EV, 'V', Conveyor_Module_EV, 'D', Tool_DataOrb});
        addCraftingRecipe(Automation_SuperBuffer_IV.get(1L), bitsd, new Object[]{"DMV", 'M', Automation_ChestBuffer_IV, 'V', Conveyor_Module_IV, 'D', Tool_DataOrb});
        addCraftingRecipe(Automation_SuperBuffer_LuV.get(1L), bitsd, new Object[]{"DMV", 'M', Automation_ChestBuffer_LuV, 'V', Conveyor_Module_LuV, 'D', Tool_DataOrb});
        addCraftingRecipe(Automation_SuperBuffer_ZPM.get(1L), bitsd, new Object[]{"DMV", 'M', Automation_ChestBuffer_ZPM, 'V', Conveyor_Module_ZPM, 'D', Tool_DataOrb});
        addCraftingRecipe(Automation_SuperBuffer_UV.get(1L), bitsd, new Object[]{"DMV", 'M', Automation_ChestBuffer_UV, 'V', Conveyor_Module_UV, 'D', Tool_DataOrb});
        addCraftingRecipe(Automation_SuperBuffer_MAX.get(1L), bitsd, new Object[]{"DMV", 'M', Automation_ChestBuffer_MAX, 'V', Conveyor_Module_UHV, 'D', Tool_DataOrb});

        addCraftingRecipe(Automation_SuperBuffer_ULV.get(1L), bitsd, new Object[]{"DMV", "DDD", 'M', Hull_ULV, 'V', Conveyor_Module_LV, 'D', Tool_DataStick});
        addCraftingRecipe(Automation_SuperBuffer_LV.get(1L), bitsd, new Object[]{"DMV", "DDD", 'M', Hull_LV, 'V', Conveyor_Module_LV, 'D', Tool_DataStick});
        addCraftingRecipe(Automation_SuperBuffer_MV.get(1L), bitsd, new Object[]{"DMV", "DDD", 'M', Hull_MV, 'V', Conveyor_Module_MV, 'D', Tool_DataStick});
        addCraftingRecipe(Automation_SuperBuffer_HV.get(1L), bitsd, new Object[]{"DMV", "DDD", 'M', Hull_HV, 'V', Conveyor_Module_HV, 'D', Tool_DataStick});
        addCraftingRecipe(Automation_SuperBuffer_EV.get(1L), bitsd, new Object[]{"DMV", "DDD", 'M', Hull_EV, 'V', Conveyor_Module_EV, 'D', Tool_DataStick});
        addCraftingRecipe(Automation_SuperBuffer_IV.get(1L), bitsd, new Object[]{"DMV", "DDD", 'M', Hull_IV, 'V', Conveyor_Module_IV, 'D', Tool_DataStick});
        addCraftingRecipe(Automation_SuperBuffer_LuV.get(1L), bitsd, new Object[]{"DMV", "DDD", 'M', Hull_LuV, 'V', Conveyor_Module_LuV, 'D', Tool_DataStick});
        addCraftingRecipe(Automation_SuperBuffer_ZPM.get(1L), bitsd, new Object[]{"DMV", "DDD", 'M', Hull_ZPM, 'V', Conveyor_Module_ZPM, 'D', Tool_DataStick});
        addCraftingRecipe(Automation_SuperBuffer_UV.get(1L), bitsd, new Object[]{"DMV", "DDD", 'M', Hull_UV, 'V', Conveyor_Module_UV, 'D', Tool_DataStick});
        addCraftingRecipe(Automation_SuperBuffer_MAX.get(1L), bitsd, new Object[]{"DMV", "DDD", 'M', Hull_MAX, 'V', Conveyor_Module_UHV, 'D', Tool_DataStick});

        Automation_ItemDistributor_ULV.set(new GT_MetaTileEntity_ItemDistributor(9320, "automation.itemdistributor.tier.00", "ULV Item Distributor", 0).getStackForm(1L));
        Automation_ItemDistributor_LV.set(new GT_MetaTileEntity_ItemDistributor(9321, "automation.itemdistributor.tier.01", "LV Item Distributor", 1).getStackForm(1L));
        Automation_ItemDistributor_MV.set(new GT_MetaTileEntity_ItemDistributor(9322, "automation.itemdistributor.tier.02", "MV Item Distributor", 2).getStackForm(1L));
        Automation_ItemDistributor_HV.set(new GT_MetaTileEntity_ItemDistributor(9323, "automation.itemdistributor.tier.03", "HV Item Distributor", 3).getStackForm(1L));
        Automation_ItemDistributor_EV.set(new GT_MetaTileEntity_ItemDistributor(9324, "automation.itemdistributor.tier.04", "EV Item Distributor", 4).getStackForm(1L));
        Automation_ItemDistributor_IV.set(new GT_MetaTileEntity_ItemDistributor(9325, "automation.itemdistributor.tier.05", "IV Item Distributor", 5).getStackForm(1L));
        Automation_ItemDistributor_LuV.set(new GT_MetaTileEntity_ItemDistributor(9326, "automation.itemdistributor.tier.06", "LuV Item Distributor", 6).getStackForm(1L));
        Automation_ItemDistributor_ZPM.set(new GT_MetaTileEntity_ItemDistributor(9327, "automation.itemdistributor.tier.07", "ZPM Item Distributor", 7).getStackForm(1L));
        Automation_ItemDistributor_UV.set(new GT_MetaTileEntity_ItemDistributor(9328, "automation.itemdistributor.tier.08", "UV Item Distributor", 8).getStackForm(1L));
        Automation_ItemDistributor_MAX.set(new GT_MetaTileEntity_ItemDistributor(9329, "automation.itemdistributor.tier.09", "UHV Item Distributor", 9).getStackForm(1L));

        addCraftingRecipe(Automation_ItemDistributor_ULV.get(1L), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', Hull_ULV, 'V', Conveyor_Module_LV, 'C', craftingChest, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_ItemDistributor_LV.get(1L), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', Hull_LV, 'V', Conveyor_Module_LV, 'C', craftingChest, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_ItemDistributor_MV.get(1L), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', Hull_MV, 'V', Conveyor_Module_MV, 'C', craftingChest, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_ItemDistributor_HV.get(1L), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', Hull_HV, 'V', Conveyor_Module_HV, 'C', craftingChest, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_ItemDistributor_EV.get(1L), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', Hull_EV, 'V', Conveyor_Module_EV, 'C', craftingChest, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_ItemDistributor_IV.get(1L), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', Hull_IV, 'V', Conveyor_Module_IV, 'C', craftingChest, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_ItemDistributor_LuV.get(1L), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', Hull_LuV, 'V', Conveyor_Module_LuV, 'C', craftingChest, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_ItemDistributor_ZPM.get(1L), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', Hull_ZPM, 'V', Conveyor_Module_ZPM, 'C', craftingChest, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_ItemDistributor_UV.get(1L), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', Hull_UV, 'V', Conveyor_Module_UV, 'C', craftingChest, 'X', circuit.get(Basic)});
        addCraftingRecipe(Automation_ItemDistributor_MAX.get(1L), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', Hull_MAX, 'V', Conveyor_Module_UHV, 'C', craftingChest, 'X', circuit.get(Basic)});

        Quantum_Chest_LV.set(new GT_MetaTileEntity_QuantumChest(125, "quantum.chest.tier.06", "Quantum Chest I", 6).getStackForm(1L));
        Quantum_Chest_MV.set(new GT_MetaTileEntity_QuantumChest(126, "quantum.chest.tier.07", "Quantum Chest II", 7).getStackForm(1L));
        Quantum_Chest_HV.set(new GT_MetaTileEntity_QuantumChest(127, "quantum.chest.tier.08", "Quantum Chest III", 8).getStackForm(1L));
        Quantum_Chest_EV.set(new GT_MetaTileEntity_QuantumChest(128, "quantum.chest.tier.09", "Quantum Chest IV", 9).getStackForm(1L));
        Quantum_Chest_IV.set(new GT_MetaTileEntity_QuantumChest(129, "quantum.chest.tier.10", "Quantum Chest V", 10).getStackForm(1L));
        LOADED = true;
    }
}
