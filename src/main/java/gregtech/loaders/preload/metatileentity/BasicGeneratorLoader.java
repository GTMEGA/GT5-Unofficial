
package gregtech.loaders.preload.metatileentity;

import cpw.mods.fml.common.Loader;
import gregtech.common.tileentities.generators.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import static gregtech.api.enums.ItemList.*;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.OrePrefixes.*;
import static gregtech.api.util.GT_ModHandler.addCraftingRecipe;
import static gregtech.loaders.preload.GT_Loader_MetaTileEntities.DISMANTLEABLE_RECIPE_MASK;

public final class BasicGeneratorLoader {
    private static boolean LOADED = false;

    public static void load() {
        if (LOADED)
            throw new RuntimeException("Already loaded!");
        Generator_Diesel_LV.set(new GT_MetaTileEntity_DieselGenerator(1110, "basicgenerator.diesel.tier.01", "Basic Combustion Generator", 1).getStackForm(1L));
        Generator_Diesel_MV.set(new GT_MetaTileEntity_DieselGenerator(1111, "basicgenerator.diesel.tier.02", "Advanced Combustion Generator", 2).getStackForm(1L));
        Generator_Diesel_HV.set(new GT_MetaTileEntity_DieselGenerator(1112, "basicgenerator.diesel.tier.03", "Advanced Combustion Generator II", 3).getStackForm(1L));

        addCraftingRecipe(Generator_Diesel_LV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"PCP", "EME", "GWG", 'M', Hull_LV, 'P', Electric_Piston_LV, 'E', Electric_Motor_LV, 'C', circuitPower.get(PWR_LV), 'W', cableGt01.get(Tin), 'G', gearGt.get(Steel)});
        addCraftingRecipe(Generator_Diesel_MV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"PCP", "EME", "GWG", 'M', Hull_MV, 'P', Electric_Piston_MV, 'E', Electric_Motor_MV, 'C', circuitPower.get(PWR_MV), 'W', cableGt01.get(AnyCopper), 'G', gearGt.get(StainlessSteel)});
        addCraftingRecipe(Generator_Diesel_HV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"PCP", "EME", "GWG", 'M', Hull_HV, 'P', Electric_Piston_HV, 'E', Electric_Motor_HV, 'C', circuitPower.get(PWR_HV), 'W', cableGt01.get(Gold), 'G', gearGt.get(Aluminium)});

        Generator_Gas_Turbine_LV.set(new GT_MetaTileEntity_GasTurbine(1115, "basicgenerator.gasturbine.tier.01", "Basic Gas Turbine", 1).getStackForm(1L));
        Generator_Gas_Turbine_MV.set(new GT_MetaTileEntity_GasTurbine(1116, "basicgenerator.gasturbine.tier.02", "Advanced Gas Turbine", 2).getStackForm(1L));
        Generator_Gas_Turbine_HV.set(new GT_MetaTileEntity_GasTurbine(1117, "basicgenerator.gasturbine.tier.03", "Advanced Gas Turbine II", 3).getStackForm(1L));

        addCraftingRecipe(Generator_Gas_Turbine_LV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CRC", "RMR", "EWE", 'M', Hull_LV, 'E', Electric_Motor_LV, 'R', rotor.get(Tin), 'C', circuitPower.get(PWR_LV), 'W', cableGt01.get(Tin)});
        addCraftingRecipe(Generator_Gas_Turbine_MV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CRC", "RMR", "EWE", 'M', Hull_MV, 'E', Electric_Motor_MV, 'R', rotor.get(Brass), 'C', circuitPower.get(PWR_MV), 'W', cableGt01.get(AnyCopper)});
        addCraftingRecipe(Generator_Gas_Turbine_HV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CRC", "RMR", "EWE", 'M', Hull_HV, 'E', Electric_Motor_HV, 'R', rotor.get(Steel), 'C', circuitPower.get(PWR_HV), 'W', cableGt01.get(Gold)});

        Generator_Steam_Turbine_LV.set(new GT_MetaTileEntity_SteamTurbine(1120, "basicgenerator.steamturbine.tier.01", "Basic Steam Turbine", 1).getStackForm(1L));
        Generator_Steam_Turbine_MV.set(new GT_MetaTileEntity_SteamTurbine(1121, "basicgenerator.steamturbine.tier.02", "Advanced Steam Turbine", 2).getStackForm(1L));
        Generator_Steam_Turbine_HV.set(new GT_MetaTileEntity_SteamTurbine(1122, "basicgenerator.steamturbine.tier.03", "Advanced Steam Turbine II", 3).getStackForm(1L));

        addCraftingRecipe(Generator_Steam_Turbine_LV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"PCP", "RMR", "EWE", 'M', Hull_LV, 'E', Electric_Motor_LV, 'R', rotor.get(Tin), 'C', circuitPower.get(PWR_LV), 'W', cableGt01.get(Tin), 'P', pipeMedium.get(Brass)});
        addCraftingRecipe(Generator_Steam_Turbine_MV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"PCP", "RMR", "EWE", 'M', Hull_MV, 'E', Electric_Motor_MV, 'R', rotor.get(Brass), 'C', circuitPower.get(PWR_MV), 'W', cableGt01.get(AnyCopper), 'P', pipeMedium.get(Steel)});
        addCraftingRecipe(Generator_Steam_Turbine_HV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"PCP", "RMR", "EWE", 'M', Hull_HV, 'E', Electric_Motor_HV, 'R', rotor.get(Steel), 'C', circuitPower.get(PWR_HV), 'W', cableGt01.get(Gold), 'P', pipeMedium.get(StainlessSteel)});

        Generator_Naquadah_Mark_I.set(new GT_MetaTileEntity_NaquadahReactor(1190, "basicgenerator.naquadah.tier.04", new String[]{"Requires Enriched Naquadah Bolts"}, "Naquadah Reactor Mark I", 4).getStackForm(1L));
        Generator_Naquadah_Mark_II.set(new GT_MetaTileEntity_NaquadahReactor(1191, "basicgenerator.naquadah.tier.05", new String[]{"Requires Enriched Naquadah Rods"}, "Naquadah Reactor Mark II", 5).getStackForm(1L));
        Generator_Naquadah_Mark_III.set(new GT_MetaTileEntity_NaquadahReactor(1192, "basicgenerator.naquadah.tier.06", new String[]{"Requires Enriched Naquadah Long Rods"}, "Naquadah Reactor Mark III", 6).getStackForm(1L));
        Generator_Naquadah_Mark_IV.set(new GT_MetaTileEntity_NaquadahReactor(1188, "basicgenerator.naquadah.tier.07", new String[]{"Requires Naquadria Bolts"}, "Naquadah Reactor Mark IV", 7).getStackForm(1L));
        Generator_Naquadah_Mark_V.set(new GT_MetaTileEntity_NaquadahReactor(1189, "basicgenerator.naquadah.tier.08", new String[]{"Requires Naquadria Rods"}, "Naquadah Reactor Mark V", 8).getStackForm(1L));

        MagicEnergyConverter_LV.set(new GT_MetaTileEntity_MagicEnergyConverter(1123, "basicgenerator.magicenergyconverter.tier.01", "Novice Magic Energy Converter", 1).getStackForm(1L));
        MagicEnergyConverter_MV.set(new GT_MetaTileEntity_MagicEnergyConverter(1124, "basicgenerator.magicenergyconverter.tier.02", "Adept Magic Energy Converter", 2).getStackForm(1L));
        MagicEnergyConverter_HV.set(new GT_MetaTileEntity_MagicEnergyConverter(1125, "basicgenerator.magicenergyconverter.tier.03", "Master Magic Energy Converter", 3).getStackForm(1L));

        MagicEnergyAbsorber_LV.set(new GT_MetaTileEntity_MagicalEnergyAbsorber(1127, "basicgenerator.magicenergyabsorber.tier.01", "Novice Magic Energy Absorber", 1).getStackForm(1L));
        MagicEnergyAbsorber_MV.set(new GT_MetaTileEntity_MagicalEnergyAbsorber(1128, "basicgenerator.magicenergyabsorber.tier.02", "Adept Magic Energy Absorber", 2).getStackForm(1L));
        MagicEnergyAbsorber_HV.set(new GT_MetaTileEntity_MagicalEnergyAbsorber(1129, "basicgenerator.magicenergyabsorber.tier.03", "Master Magic Energy Absorber", 3).getStackForm(1L));
        MagicEnergyAbsorber_EV.set(new GT_MetaTileEntity_MagicalEnergyAbsorber(1130, "basicgenerator.magicenergyabsorber.tier.04", "Grandmaster Magic Energy Absorber", 4).getStackForm(1L));
        if (!Loader.isModLoaded("Thaumcraft")) {
            addCraftingRecipe(MagicEnergyConverter_LV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CTC", "FMF", "CBC", 'M', Hull_LV, 'B', new ItemStack(Blocks.beacon), 'C', circuitPower.get(PWR_MV), 'T', Field_Generator_LV, 'F', plate.get(Platinum)});
            addCraftingRecipe(MagicEnergyConverter_MV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CTC", "FMF", "CBC", 'M', Hull_MV, 'B', new ItemStack(Blocks.beacon), 'C', circuitPower.get(PWR_HV), 'T', Field_Generator_MV, 'F', plate.get(Iridium)});
            addCraftingRecipe(MagicEnergyConverter_HV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CTC", "FMF", "CBC", 'M', Hull_HV, 'B', new ItemStack(Blocks.beacon), 'C', circuitPower.get(PWR_EV), 'T', Field_Generator_HV, 'F', plate.get(Neutronium)});

            addCraftingRecipe(MagicEnergyAbsorber_LV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CTC", "FMF", "CBC", 'M', Hull_LV, 'B', MagicEnergyConverter_LV.get(1L), 'C', circuitPower.get(PWR_MV), 'T', Field_Generator_LV, 'F', plate.get(Platinum)});
            addCraftingRecipe(MagicEnergyAbsorber_MV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CTC", "FMF", "CBC", 'M', Hull_MV, 'B', MagicEnergyConverter_MV.get(1L), 'C', circuitPower.get(PWR_HV), 'T', Field_Generator_MV, 'F', plate.get(Iridium)});
            addCraftingRecipe(MagicEnergyAbsorber_HV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CTC", "FMF", "CBC", 'M', Hull_HV, 'B', MagicEnergyConverter_MV.get(1L), 'C', circuitPower.get(PWR_EV), 'T', Field_Generator_HV, 'F', plate.get(Europium)});
            addCraftingRecipe(MagicEnergyAbsorber_EV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CTC", "FMF", "CBC", 'M', Hull_HV, 'B', MagicEnergyConverter_HV.get(1L), 'C', circuitPower.get(PWR_IV), 'T', Field_Generator_EV, 'F', plate.get(Neutronium)});
        }
        Generator_Plasma_IV.set(new GT_MetaTileEntity_PlasmaGenerator(1196, "basicgenerator.plasmagenerator.tier.05", "Plasma Generator MKI", 4).getStackForm(1L));
        Generator_Plasma_LuV.set(new GT_MetaTileEntity_PlasmaGenerator(1197, "basicgenerator.plasmagenerator.tier.06", "Plasma Generator MKII", 5).getStackForm(1L));
        Generator_Plasma_ZPMV.set(new GT_MetaTileEntity_PlasmaGenerator(1198, "basicgenerator.plasmagenerator.tier.07", "Plasma Generator MKIII", 6).getStackForm(1L));

        addCraftingRecipe(Generator_Plasma_IV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"UCU", "FMF", "WCW", 'M', Hull_LuV, 'F', Field_Generator_HV, 'C', circuitPower.get(PWR_IV), 'W', cableGt04.get(Tungsten), 'U', stick.get(Plutonium241)});
        addCraftingRecipe(Generator_Plasma_LuV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"UCU", "FMF", "WCW", 'M', Hull_ZPM, 'F', Field_Generator_EV, 'C', circuitPower.get(PWR_LUV), 'W', wireGt04.get(VanadiumGallium), 'U', stick.get(Europium)});
        addCraftingRecipe(Generator_Plasma_ZPMV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"UCU", "FMF", "WCW", 'M', Hull_UV, 'F', Field_Generator_IV, 'C', circuitPower.get(PWR_ZPM), 'W', wireGt04.get(Naquadah), 'U', stick.get(Americium)});
        LOADED = true;
    }
}
