package gregtech.loaders.preload.metatileentity;

import codechicken.nei.api.API;
import cpw.mods.fml.common.Loader;
import gregtech.common.tileentities.generators.GT_MetaTileEntity_LightningRod;
import gregtech.common.tileentities.machines.multi.*;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static gregtech.GT_Mod.gregtechproxy;
import static gregtech.api.enums.ItemList.*;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.OreDictNames.craftingIronFurnace;
import static gregtech.api.enums.OrePrefixes.*;
import static gregtech.api.util.GT_ModHandler.RecipeBits.BUFFERED;
import static gregtech.api.util.GT_ModHandler.RecipeBits.NOT_REMOVABLE;
import static gregtech.api.util.GT_ModHandler.addCraftingRecipe;
import static gregtech.loaders.postload.GT_ProcessingArrayRecipeLoader.registerDefaultGregtechMaps;
import static gregtech.loaders.preload.GT_Loader_MetaTileEntities.DISMANTLEABLE_RECIPE_MASK;
import static ic2.core.Ic2Items.reinforcedStone;

public final class MultiblockMachineLoader {
    private static boolean LOADED = false;

    public static void load() {
        if (LOADED)
            throw new RuntimeException("Already loaded!");
        Machine_Bricked_BlastFurnace.set(new GT_MetaTileEntity_BrickedBlastFurnace(140, "multimachine.brickedblastfurnace", "Bricked Blast Furnace").getStackForm(1L));
        addCraftingRecipe(Machine_Bricked_BlastFurnace.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"BFB", "FwF", "BFB", 'B', Casing_Firebricks, 'F', craftingIronFurnace});

        Machine_Multi_BlastFurnace.set(new GT_MetaTileEntity_ElectricBlastFurnace(1000, "multimachine.blastfurnace", "Electric Blast Furnace").getStackForm(1L));
        Machine_Multi_ImplosionCompressor.set(new GT_MetaTileEntity_ImplosionCompressor(1001, "multimachine.implosioncompressor", "Implosion Compressor").getStackForm(1L));
        Machine_Multi_VacuumFreezer.set(new GT_MetaTileEntity_VacuumFreezer(1002, "multimachine.vacuumfreezer", "Vacuum Freezer").getStackForm(1L));
        Machine_Multi_Furnace.set(new GT_MetaTileEntity_MultiFurnace(1003, "multimachine.multifurnace", "Multi Smelter").getStackForm(1L));
        addCraftingRecipe(Machine_Multi_BlastFurnace.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"FFF", "CMC", "WCW", 'M', Casing_HeatProof, 'F', craftingIronFurnace, 'C', circuit.get(Basic), 'W', cableGt01.get(Tin)});
        addCraftingRecipe(Machine_Multi_VacuumFreezer.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"PPP", "CMC", "WCW", 'M', Casing_FrostProof, 'P', Electric_Pump_HV, 'C', circuit.get(Data), 'W', cableGt01.get(Gold)});
        addCraftingRecipe(Machine_Multi_ImplosionCompressor.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"OOO", "CMC", "WCW", 'M', Casing_SolidSteel, 'O', reinforcedStone, 'C', circuit.get(Advanced), 'W', cableGt01.get(Gold)});
        addCraftingRecipe(Machine_Multi_Furnace.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"FFF", "CMC", "WCW", 'M', Casing_HeatProof, 'F', craftingIronFurnace, 'C', circuit.get(Advanced), 'W', cableGt01.get(AnnealedCopper)});

        Machine_Multi_LargeBoiler_Bronze.set(new GT_MetaTileEntity_LargeBoiler_Bronze(1020, "multimachine.boiler.bronze", "Large Bronze Boiler").getStackForm(1L));
        Machine_Multi_LargeBoiler_Steel.set(new GT_MetaTileEntity_LargeBoiler_Steel(1021, "multimachine.boiler.steel", "Large Steel Boiler").getStackForm(1L));
        Machine_Multi_LargeBoiler_Titanium.set(new GT_MetaTileEntity_LargeBoiler_Titanium(1022, "multimachine.boiler.titanium", "Large Titanium Boiler").getStackForm(1L));
        Machine_Multi_LargeBoiler_TungstenSteel.set(new GT_MetaTileEntity_LargeBoiler_TungstenSteel(1023, "multimachine.boiler.tungstensteel", "Large Tungstensteel Boiler").getStackForm(1L));
        addCraftingRecipe(Machine_Multi_LargeBoiler_Bronze.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WCW", "CMC", "WCW", 'M', Casing_Firebox_Bronze, 'C', circuit.get(Good), 'W', cableGt01.get(Tin)});
        addCraftingRecipe(Machine_Multi_LargeBoiler_Steel.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WCW", "CMC", "WCW", 'M', Casing_Firebox_Steel, 'C', circuit.get(Advanced), 'W', cableGt01.get(AnyCopper)});
        addCraftingRecipe(Machine_Multi_LargeBoiler_Titanium.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WCW", "CMC", "WCW", 'M', Casing_Firebox_Titanium, 'C', circuit.get(Data), 'W', cableGt01.get(Gold)});
        addCraftingRecipe(Machine_Multi_LargeBoiler_TungstenSteel.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WCW", "CMC", "WCW", 'M', Casing_Firebox_TungstenSteel, 'C', circuit.get(Elite), 'W', cableGt01.get(Aluminium)});

        FusionComputer_LuV.set(new GT_MetaTileEntity_FusionComputer1(1193, "fusioncomputer.tier.06", "Fusion Control Computer MKI").getStackForm(1L));
        FusionComputer_ZPMV.set(new GT_MetaTileEntity_FusionComputer2(1194, "fusioncomputer.tier.07", "Fusion Control Computer MKII").getStackForm(1L));
        FusionComputer_UV.set(new GT_MetaTileEntity_FusionComputer3(1195, "fusioncomputer.tier.08", "Fusion Control Computer MKIII").getStackForm(1L));

        addCraftingRecipe(Casing_Fusion_Coil.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CTC", "FMF", "CTC", 'M', Casing_Coil_Superconductor, 'C', circuit.get(Master), 'F', Field_Generator_MV, 'T', Neutron_Reflector});

        Processing_Array.set(new GT_MetaTileEntity_ProcessingArray(1199, "multimachine.processingarray", "Processing Array").getStackForm(1L));
        addCraftingRecipe(Processing_Array.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CTC", "FMF", "CBC", 'M', Hull_EV, 'B', pipeLarge.get(StainlessSteel), 'C', circuit.get(Elite), 'F', Robot_Arm_EV, 'T', Energy_LapotronicOrb});
        registerDefaultGregtechMaps();

        Distillation_Tower.set(new GT_MetaTileEntity_DistillationTower(1126, "multimachine.distillationtower", "Distillation Tower").getStackForm(1L));
        addCraftingRecipe(Distillation_Tower.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CBC", "FMF", "CBC", 'M', Hull_HV, 'B', pipeLarge.get(StainlessSteel), 'C', circuit.get(Data), 'F', Electric_Pump_HV});

        LargeSteamTurbine.set(new GT_MetaTileEntity_LargeTurbine_Steam(1131, "multimachine.largeturbine", "Large Steam Turbine").getStackForm(1L));
        LargeGasTurbine.set(new GT_MetaTileEntity_LargeTurbine_Gas(1151, "multimachine.largegasturbine", "Large Gas Turbine").getStackForm(1L));
        LargeHPSteamTurbine.set(new GT_MetaTileEntity_LargeTurbine_HPSteam(1152, "multimachine.largehpturbine", "Large HP Steam Turbine").getStackForm(1L));
        LargePlasmaTurbine.set(new GT_MetaTileEntity_LargeTurbine_Plasma(1153, "multimachine.largeplasmaturbine", "Large Plasma Generator").getStackForm(1L));

        addCraftingRecipe(LargeSteamTurbine.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CPC", "PMP", "BPB", 'M', Hull_HV, 'B', pipeLarge.get(Steel), 'C', circuit.get(Advanced), 'P', gearGt.get(Steel)});
        addCraftingRecipe(LargeGasTurbine.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CPC", "PMP", "BPB", 'M', Hull_EV, 'B', pipeLarge.get(StainlessSteel), 'C', circuit.get(Data), 'P', gearGt.get(StainlessSteel)});
        //GT_ModHandler.addCraftingRecipe(ItemList.LargeHPSteamTurbine.get(1L), bitsd, new Object[]{"CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_IV, 'B', OrePrefixes.pipeLarge.get(Materials.Titanium), 'C', OrePrefixes.circuit.get(Materials.Elite), 'P', OrePrefixes.gearGt.get(Materials.Titanium)});
        //GT_ModHandler.addCraftingRecipe(ItemList.LargePlasmaTurbine.get(1L), bitsd, new Object[]{"CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_UV, 'B', OrePrefixes.pipeHuge.get(Materials.TungstenSteel), 'C', OrePrefixes.circuit.get(Materials.Master), 'P', OrePrefixes.gearGt.get(Materials.TungstenSteel)});

        Machine_Multi_HeatExchanger.set(new GT_MetaTileEntity_HeatExchanger(1154, "multimachine.heatexchanger", "Large Heat Exchanger").getStackForm(1L));
        addCraftingRecipe(Machine_Multi_HeatExchanger.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WCW", "CMC", "WCW", 'M', Casing_Pipe_Titanium, 'C', pipeMedium.get(Titanium), 'W', Electric_Pump_EV});

        Charcoal_Pile.set(new GT_MetaTileEntity_Charcoal_Pit(1155, "multimachine.charcoalpile", "Charcoal Pile Igniter").getStackForm(1));
        addCraftingRecipe(Charcoal_Pile.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"EXE", "EME", " C ", 'M', Hull_Bronze_Bricks, 'E', plate.get(AnyBronze), 'C', new ItemStack(Items.flint_and_steel, 1), 'X', rotor.get(Bronze),});

        OilDrill1.set(new GT_MetaTileEntity_OilDrill1(1157, "multimachine.oildrill1", "Oil/Gas/Fluid Drilling Rig").getStackForm(1));
        OilDrill2.set(new GT_MetaTileEntity_OilDrill2(141, "multimachine.oildrill2", "Oil/Gas/Fluid Drilling Rig II").getStackForm(1));
        OilDrill3.set(new GT_MetaTileEntity_OilDrill3(142, "multimachine.oildrill3", "Oil/Gas/Fluid Drilling Rig III").getStackForm(1));

        ConcreteBackfiller1.set(new GT_MetaTileEntity_ConcreteBackfiller1(143, "multimachine.concretebackfiller1", "Concrete Backfiller").getStackForm(1));
        ConcreteBackfiller2.set(new GT_MetaTileEntity_ConcreteBackfiller2(144, "multimachine.concretebackfiller3", "Advanced Concrete Backfiller").getStackForm(1));
        addCraftingRecipe(ConcreteBackfiller1.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WPW", "EME", "CQC", 'M', Hull_MV, 'W', frameGt.get(Steel), 'E', circuit.get(Good), 'C', Electric_Motor_MV, 'P', pipeLarge.get(Steel), 'Q', Electric_Pump_MV});
        addCraftingRecipe(ConcreteBackfiller2.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WPW", "EME", "CQC", 'M', ConcreteBackfiller1, 'W', frameGt.get(Titanium), 'E', circuit.get(Data), 'C', Electric_Motor_EV, 'P', pipeLarge.get(Steel), 'Q', Electric_Pump_EV});

        OreDrill1.set(new GT_MetaTileEntity_OreDrillingPlant1(1158, "multimachine.oredrill1", "Ore Drilling Rig MKIV").getStackForm(1));
        OreDrill2.set(new GT_MetaTileEntity_OreDrillingPlant2(1177, "multimachine.oredrill2", "Ore Drilling Rig MKV").getStackForm(1));
        OreDrill3.set(new GT_MetaTileEntity_OreDrillingPlant3(1178, "multimachine.oredrill3", "Ore Drilling Rig MKVI").getStackForm(1));
        OreDrill4.set(new GT_MetaTileEntity_OreDrillingPlant4(1179, "multimachine.oredrill4", "Ore Drilling Rig MKVII").getStackForm(1));

        PyrolyseOven.set(new GT_MetaTileEntity_PyrolyseOven(1159, "multimachine.pyro", "Pyrolyse Oven").getStackForm(1));
        addCraftingRecipe(PyrolyseOven.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WEP", "EME", "WCP", 'M', Hull_LV, 'W', Electric_Piston_LV, 'P', wireGt04.get(Cupronickel), 'E', circuit.get(Basic), 'C', Electric_Pump_LV});

        OilCracker.set(new GT_MetaTileEntity_OilCracker(1160, "multimachine.cracker", "Oil Cracking Unit").getStackForm(1));
        addCraftingRecipe(OilCracker.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WCW", "EME", "WCW", 'M', Hull_HV, 'W', Casing_Coil_Cupronickel, 'E', circuit.get(Advanced), 'C', Electric_Pump_HV});

        Machine_Multi_Assemblyline.set(new GT_MetaTileEntity_AssemblyLine(1170, "multimachine.assemblyline", "Assembling Line").getStackForm(1L));
        addCraftingRecipe(Machine_Multi_Assemblyline.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WCW", "EME", "WCW", 'M', Hull_IV, 'W', Casing_Assembler, 'E', circuit.get(Elite), 'C', Robot_Arm_IV});

        Machine_Multi_DieselEngine.set(new GT_MetaTileEntity_DieselEngine(1171, "multimachine.dieselengine", "Combustion Engine").getStackForm(1L));
        addCraftingRecipe(Machine_Multi_DieselEngine.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"PCP", "EME", "GWG", 'M', Hull_EV, 'P', Electric_Piston_EV, 'E', Electric_Motor_EV, 'C', circuit.get(Elite), 'W', cableGt01.get(TungstenSteel), 'G', gearGt.get(Titanium)});
        addCraftingRecipe(Casing_EngineIntake.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"PhP", "RFR", "PwP", 'R', pipeMedium.get(Titanium), 'F', Casing_StableTitanium, 'P', rotor.get(Titanium)});

        Machine_Multi_ExtremeDieselEngine.set(new GT_MetaTileEntity_ExtremeDieselEngine(2105, "multimachine.extremedieselengine", "Extreme Combustion Engine").getStackForm(1L));
        addCraftingRecipe(Machine_Multi_ExtremeDieselEngine.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"PCP", "EME", "GWG", 'M', Hull_IV, 'P', Electric_Piston_IV, 'E', Electric_Motor_IV, 'C', circuit.get(Master), 'W', cableGt01.get(HSSG), 'G', gearGt.get(TungstenSteel)});
        addCraftingRecipe(Casing_ExtremeEngineIntake.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"PhP", "RFR", "PwP", 'R', pipeMedium.get(TungstenSteel), 'F', Casing_RobustTungstenSteel, 'P', rotor.get(TungstenSteel)});

        Machine_Multi_Cleanroom.set(new GT_MetaTileEntity_Cleanroom(1172, "multimachine.cleanroom", "Cleanroom Controller").getStackForm(1));
        //If Cleanroom is enabled, add a recipe, else hide from NEI.
        if (gregtechproxy.mEnableCleanroom) {
            addCraftingRecipe(Machine_Multi_Cleanroom.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"FFF", "RHR", "MCM", 'H', Hull_HV, 'F', Component_Filter, 'R', rotor.get(StainlessSteel), 'M', Electric_Motor_HV, 'C', circuit.get(Advanced)});
        } else {
            if (Loader.isModLoaded("NotEnoughItems"))
                API.hideItem(Machine_Multi_Cleanroom.get(1L));
        }

        Machine_HV_LightningRod.set(new GT_MetaTileEntity_LightningRod(1174, "basicgenerator.lightningrod.03", "Lightning Rod", 3).getStackForm(1));
        addCraftingRecipe(Machine_HV_LightningRod.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"LTL", "TMT", "LTL", 'M', Hull_LuV, 'L', Energy_LapotronicOrb, 'T', Transformer_ZPM_LuV});
        Machine_EV_LightningRod.set(new GT_MetaTileEntity_LightningRod(1175, "basicgenerator.lightningrod.04", "Lightning Rod II", 4).getStackForm(1));
        addCraftingRecipe(Machine_EV_LightningRod.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"LTL", "TMT", "LTL", 'M', Hull_ZPM, 'L', Energy_LapotronicOrb2, 'T', Transformer_UV_ZPM});
        Machine_IV_LightningRod.set(new GT_MetaTileEntity_LightningRod(1176, "basicgenerator.lightningrod.05", "Lightning Rod III", 5).getStackForm(1));
        addCraftingRecipe(Machine_IV_LightningRod.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"LTL", "TMT", "LTL", 'M', Hull_UV, 'L', ZPM2, 'T', Transformer_MAX_UV});

        Machine_Multi_LargeChemicalReactor.set(new GT_MetaTileEntity_LargeChemicalReactor(1169, "multimachine.chemicalreactor", "Large Chemical Reactor").getStackForm(1));
        addCraftingRecipe(Machine_Multi_LargeChemicalReactor.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CRC", "PMP", "CBC", 'B', Hull_HV});

        LOADED = true;
    }
}
