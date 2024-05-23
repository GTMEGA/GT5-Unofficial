package gregtech.loaders.preload;

import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Frame;
import gregtech.api.util.GT_Log;
import gregtech.common.tileentities.machines.basic.*;
import gregtech.common.tileentities.machines.long_distance.GT_MetaTileEntity_LongDistancePipelineFluid;
import gregtech.common.tileentities.machines.long_distance.GT_MetaTileEntity_LongDistancePipelineItem;
import gregtech.loaders.preload.metatileentity.*;

import net.minecraft.util.EnumChatFormatting;

import static gregtech.api.GregTech_API.sGeneratedMaterials;
import static gregtech.api.enums.ItemList.*;
import static gregtech.api.GregTech_API.sSoundList;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.OrePrefixes.*;
import static gregtech.api.util.GT_LanguageManager.i18nPlaceholder;
import static gregtech.api.util.GT_ModHandler.*;

//TODO move this back after we fix coremod
public final class GT_Loader_MetaTileEntities implements Runnable {
    //TODO CHECK CIRCUIT RECIPES AND USAGES
    public static final long RECIPE_MASK = RecipeBits.NOT_REMOVABLE | RecipeBits.REVERSIBLE | RecipeBits.BUFFERED;
    public static final long DISMANTLEABLE_RECIPE_MASK = RecipeBits.DISMANTLEABLE | RECIPE_MASK; //TODO: Remove Any Dismantleable stuff

    //TODO remove this after we fix coremod
    public static final String imagination = EnumChatFormatting.RESET + "You just need "
                                             + EnumChatFormatting.DARK_PURPLE + "I" + EnumChatFormatting.LIGHT_PURPLE + "m" + EnumChatFormatting.DARK_RED
                                             + "a" + EnumChatFormatting.RED + "g" + EnumChatFormatting.YELLOW + "i" + EnumChatFormatting.GREEN + "n"
                                             + EnumChatFormatting.AQUA + "a" + EnumChatFormatting.DARK_AQUA + "t" + EnumChatFormatting.BLUE + "i"
                                             + EnumChatFormatting.DARK_BLUE + "o" + EnumChatFormatting.DARK_PURPLE + "n" + EnumChatFormatting.RESET
                                             + " to use this.";
    @Override
    public void run() {
        GT_Log.out.println("GT_Mod: Registering MetaTileEntities.");
        CasingRecipeLoader.load();
        HullLoader.load();
        PowerStuffLoader.load();
        HatchLoader.load();
        FluidStuffLoader.load();
        SteamMachineLoader.load();
        loadLongDistancePipelines();
        BasicMachineLoader.load();
        BasicGeneratorLoader.load();
        MultiblockMachineLoader.load();
        loadTeleporter();
        loadMiner();
        loadMobRepelator();
        loadSesmicProspector();
        loadFrames();
        CableLoader.load();
        FluidPipeLoader.load();
        ItemPipeLoader.load();
        ItemStuffLoader.load();
        DevLoader.load();
    }

    private static void loadLongDistancePipelines() {
        Long_Distance_Pipeline_Fluid.set(new GT_MetaTileEntity_LongDistancePipelineFluid(2700, "long.distance.pipeline.fluid", "Long Distance Fluid Pipeline", 1).getStackForm(1L));
        Long_Distance_Pipeline_Item.set(new GT_MetaTileEntity_LongDistancePipelineItem(2701, "long.distance.pipeline.item", "Long Distance Item Pipeline", 1).getStackForm(1L));
    }

    private static void loadMiner() {
        Machine_LV_Miner.set(new GT_MetaTileEntity_Miner(679, "basicmachine.miner.tier.01", "Depracated Miner 1", 1).getStackForm(1L));
        Machine_MV_Miner.set(new GT_MetaTileEntity_Miner(680, "basicmachine.miner.tier.02", "Depracated Miner 2", 2).getStackForm(1L));
        Machine_HV_Miner.set(new GT_MetaTileEntity_Miner(681, "basicmachine.miner.tier.03", "Depracated Miner 3", 3).getStackForm(1L));

//        addCraftingRecipe(Machine_LV_Miner.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"EEE", "WMW", "CSC", 'M', Hull_LV, 'E', Electric_Motor_LV, 'C', circuitLogic.get(LOGIC_LV), 'W', cableGt01.get(Tin), 'S', Sensor_LV});
//        addCraftingRecipe(Machine_MV_Miner.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"PEP", "WMW", "CSC", 'M', Hull_MV, 'E', Electric_Motor_MV, 'P', Electric_Piston_MV, 'C',circuitLogic.get(LOGIC_MV), 'W', cableGt02.get(Copper), 'S', Sensor_MV});
//        addCraftingRecipe(Machine_HV_Miner.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"RPR", "WMW", "CSC", 'M', Hull_HV, 'E', Electric_Motor_HV, 'P', Electric_Piston_HV, 'R', Robot_Arm_HV, 'C', circuitLogic.get(LOGIC_HV), 'W', cableGt04.get(Gold), 'S', Sensor_HV});
    }

    private static void loadTeleporter() {
        Teleporter.set(new GT_MetaTileEntity_Teleporter(1145, "basicmachine.teleporter", "Teleporter", 9).getStackForm(1L));
    }

    private static void loadMobRepelator() {
        MobRep_LV.set(new GT_MetaTileEntity_MonsterRepellent(1146, "basicmachine.mobrep.tier.01", "Basic Monster Repellator", 1).getStackForm(1L));
        MobRep_MV.set(new GT_MetaTileEntity_MonsterRepellent(1147, "basicmachine.mobrep.tier.02", "Advanced Monster Repellator", 2).getStackForm(1L));
        MobRep_HV.set(new GT_MetaTileEntity_MonsterRepellent(1148, "basicmachine.mobrep.tier.03", "Advanced Monster Repellator II", 3).getStackForm(1L));
        MobRep_EV.set(new GT_MetaTileEntity_MonsterRepellent(1149, "basicmachine.mobrep.tier.04", "Advanced Monster Repellator III", 4).getStackForm(1L));
        MobRep_IV.set(new GT_MetaTileEntity_MonsterRepellent(1150, "basicmachine.mobrep.tier.05", "Advanced Monster Repellator IV", 5).getStackForm(1L));
        MobRep_LuV.set(new GT_MetaTileEntity_MonsterRepellent(1135, "basicmachine.mobrep.tier.06", "Advanced Monster Repellator V", 6).getStackForm(1L));
        MobRep_ZPM.set(new GT_MetaTileEntity_MonsterRepellent(1136, "basicmachine.mobrep.tier.07", "Advanced Monster Repellator VI", 7).getStackForm(1L));
        MobRep_UV.set(new GT_MetaTileEntity_MonsterRepellent(1137, "basicmachine.mobrep.tier.08", "Advanced Monster Repellator VII", 8).getStackForm(1L));
        addCraftingRecipe(MobRep_LV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"EEE", " M ", "CCC", 'M', Hull_LV, 'E', Emitter_LV.get(1L), 'C', circuitLogic.get(LOGIC_LV)});
        addCraftingRecipe(MobRep_MV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"EEE", " M ", "CCC", 'M', Hull_MV, 'E', Emitter_MV.get(1L), 'C', circuitLogic.get(LOGIC_MV)});
        addCraftingRecipe(MobRep_HV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"EEE", " M ", "CCC", 'M', Hull_HV, 'E', Emitter_HV.get(1L), 'C', circuitLogic.get(LOGIC_HV)});
        addCraftingRecipe(MobRep_EV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"EEE", " M ", "CCC", 'M', Hull_EV, 'E', Emitter_EV.get(1L), 'C', circuitLogic.get(LOGIC_EV)});
        addCraftingRecipe(MobRep_IV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"EEE", " M ", "CCC", 'M', Hull_IV, 'E', Emitter_IV.get(1L), 'C', circuitLogic.get(LOGIC_IV)});
        addCraftingRecipe(MobRep_LuV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"EEE", " M ", "CCC", 'M', Hull_LuV, 'E', Emitter_LuV.get(1L), 'C', circuitLogic.get(LOGIC_LUV)});
        addCraftingRecipe(MobRep_ZPM.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"EEE", " M ", "CCC", 'M', Hull_ZPM, 'E', Emitter_ZPM.get(1L), 'C', circuitLogic.get(LOGIC_ZPM)});
        addCraftingRecipe(MobRep_UV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"EEE", " M ", "CCC", 'M', Hull_UV, 'E', Emitter_UV.get(1L), 'C', circuitLogic.get(LOGIC_UV)});
    }

    private static void loadSesmicProspector() {
        Seismic_Prospector_LV.set(new GT_MetaTileEntity_SeismicProspector(1156, "basicmachine.seismicprospector.01", "Seismic Prospector LV", 1).getStackForm(1));
        Seismic_Prospector_MV.set(new GT_MetaTileEntity_SeismicProspector(2100, "basicmachine.seismicprospector.02", "Seismic Prospector MV", 2).getStackForm(1));
        Seismic_Prospector_HV.set(new GT_MetaTileEntity_SeismicProspector(2101, "basicmachine.seismicprospector.03", "Seismic Prospector HV", 3).getStackForm(1));

        Seismic_Prospector_Adv_LV.set(new GT_MetaTileEntity_AdvSeismicProspector(2102, "basicmachine.seismicprospector.07", "Advanced Seismic Prospector LV", 1, sSoundList.get(Integer.valueOf(228)), 9 * 16 / 2, 2).getStackForm(1));
        Seismic_Prospector_Adv_MV.set(new GT_MetaTileEntity_AdvSeismicProspector(2103, "basicmachine.seismicprospector.06", "Advanced Seismic Prospector MV", 2, sSoundList.get(Integer.valueOf(228)), 17 * 16 / 2, 2).getStackForm(1));
        Seismic_Prospector_Adv_HV.set(new GT_MetaTileEntity_AdvSeismicProspector(2104, "basicmachine.seismicprospector.05", "Advanced Seismic Prospector HV", 3, sSoundList.get(Integer.valueOf(228)), 25 * 16 / 2, 2).getStackForm(1));
        Seismic_Prospector_Adv_EV.set(new GT_MetaTileEntity_AdvSeismicProspector(1173, "basicmachine.seismicprospector.04", "Advanced Seismic Prospector EV", 4, sSoundList.get(Integer.valueOf(228)), 27 * 16 / 2, 2).getStackForm(1));
        addShapelessCraftingRecipe(Seismic_Prospector_Adv_LV.get(1L), RECIPE_MASK, new Object[]{Seismic_Prospector_LV});
        addShapelessCraftingRecipe(Seismic_Prospector_Adv_MV.get(1L), RECIPE_MASK, new Object[]{Seismic_Prospector_MV});
        addShapelessCraftingRecipe(Seismic_Prospector_Adv_HV.get(1L), RECIPE_MASK, new Object[]{Seismic_Prospector_HV});
        addCraftingRecipe(Seismic_Prospector_Adv_LV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WWW", "EME", "CXC", 'M', Hull_LV, 'W', plate.get(Steel), 'E', circuitLogic.get(LOGIC_LV), 'C', Sensor_LV, 'X', cableGt02.get(Tin)});
        addCraftingRecipe(Seismic_Prospector_Adv_MV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WWW", "EME", "CXC", 'M', Hull_MV, 'W', plate.get(StainlessSteel), 'E', circuitLogic.get(LOGIC_MV), 'C', Sensor_MV, 'X', cableGt02.get(Copper)});
        addCraftingRecipe(Seismic_Prospector_Adv_HV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WWW", "EME", "CXC", 'M', Hull_HV, 'W', plate.get(Aluminium), 'E', circuitLogic.get(LOGIC_HV), 'C', Sensor_HV, 'X', cableGt04.get(Gold)});
        addCraftingRecipe(Seismic_Prospector_Adv_EV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WWW", "EME", "CXC", 'M', Hull_EV, 'W', plate.get(VanadiumSteel), 'E', circuitLogic.get(LOGIC_EV), 'C', Sensor_EV, 'X', cableGt04.get(Aluminium)});
    }

    private static void loadFrames() {
        for (int i = 0; i < sGeneratedMaterials.length; i++) {
            if (((sGeneratedMaterials[i] != null) && ((sGeneratedMaterials[i].mTypes & 0x2) != 0)) || (sGeneratedMaterials[i] == Wood)) {
                new GT_MetaPipeEntity_Frame(4096 + i, "GT_Frame_" + sGeneratedMaterials[i], (i18nPlaceholder ? "%material" : sGeneratedMaterials[i].mDefaultLocalName) + " Frame Box", sGeneratedMaterials[i]);
            }
        }
    }
}
