package gregtech.loaders.preload.metatileentity;

import gregtech.api.enums.GT_Values;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicBatteryBuffer;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Transformer;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Charger;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_MicrowaveEnergyTransmitter;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_Locker;

import static gregtech.api.GregTech_API.sOPStuff;
import static gregtech.api.enums.ConfigCategories.Recipes.gregtechrecipes;
import static gregtech.api.enums.ItemList.*;
import static gregtech.api.enums.ItemList.Battery_Buffer_4by4_MAX;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.OreDictNames.craftingChest;
import static gregtech.api.enums.OrePrefixes.*;
import static gregtech.api.util.GT_ModHandler.addCraftingRecipe;
import static gregtech.api.util.GT_ModHandler.getIC2Item;
import static gregtech.loaders.preload.GT_Loader_MetaTileEntities.DISMANTLEABLE_RECIPE_MASK;

public final class PowerStuffLoader {
    private static boolean LOADED = false;

    public static void load() {
        if (LOADED)
            throw new RuntimeException("Already loaded!");
        Transformer_LV_ULV.set(new GT_MetaTileEntity_Transformer(20, "transformer.tier.00", "ULV Transformer", 0, "LV -> ULV (Use Soft Mallet to invert)").getStackForm(1L));
        Transformer_MV_LV.set(new GT_MetaTileEntity_Transformer(21, "transformer.tier.01", "LV Transformer", 1, "MV -> LV (Use Soft Mallet to invert)").getStackForm(1L));
        Transformer_HV_MV.set(new GT_MetaTileEntity_Transformer(22, "transformer.tier.02", "MV Transformer", 2, "HV -> MV (Use Soft Mallet to invert)").getStackForm(1L));
        Transformer_EV_HV.set(new GT_MetaTileEntity_Transformer(23, "transformer.tier.03", "HV Transformer", 3, "EV -> HV (Use Soft Mallet to invert)").getStackForm(1L));
        Transformer_IV_EV.set(new GT_MetaTileEntity_Transformer(24, "transformer.tier.04", "EV Transformer", 4, "IV -> EV (Use Soft Mallet to invert)").getStackForm(1L));
        Transformer_LuV_IV.set(new GT_MetaTileEntity_Transformer(25, "transformer.tier.05", "IV Transformer", 5, "LuV -> IV (Use Soft Mallet to invert)").getStackForm(1L));
        Transformer_ZPM_LuV.set(new GT_MetaTileEntity_Transformer(26, "transformer.tier.06", "LuV Transformer", 6, "ZPM -> LuV (Use Soft Mallet to invert)").getStackForm(1L));
        Transformer_UV_ZPM.set(new GT_MetaTileEntity_Transformer(27, "transformer.tier.07", "ZPM Transformer", 7, "UV -> ZPM (Use Soft Mallet to invert)").getStackForm(1L));
        Transformer_MAX_UV.set(new GT_MetaTileEntity_Transformer(28, "transformer.tier.08", "UV Transformer", 8, "UHV -> UV (Use Soft Mallet to invert)").getStackForm(1L));
        addCraftingRecipe(Transformer_LV_ULV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{" BB", "CM ", " BB", 'M', Hull_ULV, 'C', cableGt01.get(Tin), 'B', cableGt01.get(RedAlloy)});
        addCraftingRecipe(Transformer_MV_LV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{" BB", "CM ", " BB", 'M', Hull_LV, 'C', cableGt01.get(AnyCopper), 'B', cableGt01.get(Tin)});
        addCraftingRecipe(Transformer_HV_MV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"KBB", "CM ", "KBB", 'M', Hull_MV, 'C', cableGt01.get(Gold), 'B', cableGt01.get(AnyCopper), 'K', circuitPower.get(PWR_LV)});
        addCraftingRecipe(Transformer_EV_HV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"KBB", "CM ", "KBB", 'M', Hull_HV, 'C', cableGt01.get(Aluminium), 'B', cableGt01.get(Gold), 'K', circuitPower.get(PWR_MV)});
        addCraftingRecipe(Transformer_IV_EV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"KBB", "CM ", "KBB", 'M', Hull_EV, 'C', cableGt01.get(Tungsten), 'B', cableGt01.get(Aluminium), 'K', circuitPower.get(PWR_HV)});
        addCraftingRecipe(Transformer_LuV_IV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"KBB", "CM ", "KBB", 'M', Hull_IV, 'C', cableGt01.get(VanadiumGallium), 'B', cableGt01.get(Tungsten), 'K', circuitPower.get(PWR_EV)});
        addCraftingRecipe(Transformer_ZPM_LuV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"KBB", "CM ", "KBB", 'M', Hull_LuV, 'C', cableGt01.get(Naquadah), 'B', cableGt01.get(VanadiumGallium), 'K', circuitPower.get(PWR_IV)});
        addCraftingRecipe(Transformer_UV_ZPM.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"KBB", "CM ", "KBB", 'M', Hull_ZPM, 'C', cableGt01.get(NaquadahAlloy), 'B', cableGt01.get(Naquadah), 'K', circuitPower.get(PWR_LUV)});
        addCraftingRecipe(Transformer_MAX_UV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"KBB", "CM ", "KBB", 'M', Hull_UV, 'C', wireGt01.get(Bedrockium), 'B', cableGt01.get(NaquadahAlloy), 'K', circuitPower.get(PWR_ZPM)});

//        Battery_Buffer_1by1_ULV.set(new GT_MetaTileEntity_BasicBatteryBuffer(160, "batterybuffer.01.tier.00", "ULV Battery Buffer", 0, "", 1).getStackForm(1L));
//        Battery_Buffer_1by1_LV.set(new GT_MetaTileEntity_BasicBatteryBuffer(161, "batterybuffer.01.tier.01", "LV Battery Buffer", 1, "", 1).getStackForm(1L));
//        Battery_Buffer_1by1_MV.set(new GT_MetaTileEntity_BasicBatteryBuffer(162, "batterybuffer.01.tier.02", "MV Battery Buffer", 2, "", 1).getStackForm(1L));
//        Battery_Buffer_1by1_HV.set(new GT_MetaTileEntity_BasicBatteryBuffer(163, "batterybuffer.01.tier.03", "HV Battery Buffer", 3, "", 1).getStackForm(1L));
//        Battery_Buffer_1by1_EV.set(new GT_MetaTileEntity_BasicBatteryBuffer(164, "batterybuffer.01.tier.04", "EV Battery Buffer", 4, "", 1).getStackForm(1L));
//        Battery_Buffer_1by1_IV.set(new GT_MetaTileEntity_BasicBatteryBuffer(165, "batterybuffer.01.tier.05", "IV Battery Buffer", 5, "", 1).getStackForm(1L));
//        Battery_Buffer_1by1_LuV.set(new GT_MetaTileEntity_BasicBatteryBuffer(166, "batterybuffer.01.tier.06", "LuV Battery Buffer", 6, "", 1).getStackForm(1L));
//        Battery_Buffer_1by1_ZPM.set(new GT_MetaTileEntity_BasicBatteryBuffer(167, "batterybuffer.01.tier.07", "ZPM Battery Buffer", 7, "", 1).getStackForm(1L));
//        Battery_Buffer_1by1_UV.set(new GT_MetaTileEntity_BasicBatteryBuffer(168, "batterybuffer.01.tier.08", "UV Battery Buffer", 8, "", 1).getStackForm(1L));
//        Battery_Buffer_1by1_MAX.set(new GT_MetaTileEntity_BasicBatteryBuffer(169, "batterybuffer.01.tier.09", "UHV Battery Buffer", 9, "", 1).getStackForm(1L));
//        addCraftingRecipe(Battery_Buffer_1by1_ULV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_ULV, 'W', wireGt01.get(RedAlloy), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_1by1_LV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_LV, 'W', wireGt01.get(Tin), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_1by1_MV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_MV, 'W', wireGt01.get(AnyCopper), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_1by1_HV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_HV, 'W', wireGt01.get(Gold), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_1by1_EV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_EV, 'W', wireGt01.get(Aluminium), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_1by1_IV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_IV, 'W', wireGt01.get(Tungsten), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_1by1_LuV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_LuV, 'W', wireGt01.get(VanadiumGallium), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_1by1_ZPM.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_ZPM, 'W', wireGt01.get(Naquadah), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_1by1_UV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_UV, 'W', wireGt01.get(NaquadahAlloy), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_1by1_MAX.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_MAX, 'W', wireGt01.get(SuperconductorUHV), 'T', craftingChest});

//        Battery_Buffer_2by2_ULV.set(new GT_MetaTileEntity_BasicBatteryBuffer(170, "batterybuffer.04.tier.00", "ULV Battery Buffer", 0, "", 4).getStackForm(1L));
//        Battery_Buffer_2by2_LV.set(new GT_MetaTileEntity_BasicBatteryBuffer(171, "batterybuffer.04.tier.01", "LV Battery Buffer", 1, "", 4).getStackForm(1L));
//        Battery_Buffer_2by2_MV.set(new GT_MetaTileEntity_BasicBatteryBuffer(172, "batterybuffer.04.tier.02", "MV Battery Buffer", 2, "", 4).getStackForm(1L));
//        Battery_Buffer_2by2_HV.set(new GT_MetaTileEntity_BasicBatteryBuffer(173, "batterybuffer.04.tier.03", "HV Battery Buffer", 3, "", 4).getStackForm(1L));
//        Battery_Buffer_2by2_EV.set(new GT_MetaTileEntity_BasicBatteryBuffer(174, "batterybuffer.04.tier.04", "EV Battery Buffer", 4, "", 4).getStackForm(1L));
//        Battery_Buffer_2by2_IV.set(new GT_MetaTileEntity_BasicBatteryBuffer(175, "batterybuffer.04.tier.05", "IV Battery Buffer", 5, "", 4).getStackForm(1L));
//        Battery_Buffer_2by2_LuV.set(new GT_MetaTileEntity_BasicBatteryBuffer(176, "batterybuffer.04.tier.06", "LuV Battery Buffer", 6, "", 4).getStackForm(1L));
//        Battery_Buffer_2by2_ZPM.set(new GT_MetaTileEntity_BasicBatteryBuffer(177, "batterybuffer.04.tier.07", "ZPM Battery Buffer", 7, "", 4).getStackForm(1L));
//        Battery_Buffer_2by2_UV.set(new GT_MetaTileEntity_BasicBatteryBuffer(178, "batterybuffer.04.tier.08", "UV Battery Buffer", 8, "", 4).getStackForm(1L));
//        Battery_Buffer_2by2_MAX.set(new GT_MetaTileEntity_BasicBatteryBuffer(179, "batterybuffer.04.tier.09", "UHV Battery Buffer", 9, "", 4).getStackForm(1L));
//        addCraftingRecipe(Battery_Buffer_2by2_ULV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_ULV, 'W', wireGt01.get(RedAlloy), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_2by2_LV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_LV, 'W', wireGt01.get(Tin), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_2by2_MV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_MV, 'W', wireGt01.get(AnyCopper), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_2by2_HV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_HV, 'W', wireGt01.get(Gold), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_2by2_EV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_EV, 'W', wireGt01.get(Aluminium), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_2by2_IV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_IV, 'W', wireGt01.get(Tungsten), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_2by2_LuV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_LuV, 'W', wireGt01.get(VanadiumGallium), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_2by2_ZPM.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_ZPM, 'W', wireGt01.get(Naquadah), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_2by2_UV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_UV, 'W', wireGt01.get(NaquadahAlloy), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_2by2_MAX.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_MAX, 'W', wireGt01.get(SuperconductorUHV), 'T', craftingChest});

//        Battery_Buffer_3by3_ULV.set(new GT_MetaTileEntity_BasicBatteryBuffer(180, "batterybuffer.09.tier.00", "ULV Battery Buffer", 0, "", 9).getStackForm(1L));
//        Battery_Buffer_3by3_LV.set(new GT_MetaTileEntity_BasicBatteryBuffer(181, "batterybuffer.09.tier.01", "LV Battery Buffer", 1, "", 9).getStackForm(1L));
//        Battery_Buffer_3by3_MV.set(new GT_MetaTileEntity_BasicBatteryBuffer(182, "batterybuffer.09.tier.02", "MV Battery Buffer", 2, "", 9).getStackForm(1L));
//        Battery_Buffer_3by3_HV.set(new GT_MetaTileEntity_BasicBatteryBuffer(183, "batterybuffer.09.tier.03", "HV Battery Buffer", 3, "", 9).getStackForm(1L));
//        Battery_Buffer_3by3_EV.set(new GT_MetaTileEntity_BasicBatteryBuffer(184, "batterybuffer.09.tier.04", "EV Battery Buffer", 4, "", 9).getStackForm(1L));
//        Battery_Buffer_3by3_IV.set(new GT_MetaTileEntity_BasicBatteryBuffer(185, "batterybuffer.09.tier.05", "IV Battery Buffer", 5, "", 9).getStackForm(1L));
//        Battery_Buffer_3by3_LuV.set(new GT_MetaTileEntity_BasicBatteryBuffer(186, "batterybuffer.09.tier.06", "LuV Battery Buffer", 6, "", 9).getStackForm(1L));
//        Battery_Buffer_3by3_ZPM.set(new GT_MetaTileEntity_BasicBatteryBuffer(187, "batterybuffer.09.tier.07", "ZPM Battery Buffer", 7, "", 9).getStackForm(1L));
//        Battery_Buffer_3by3_UV.set(new GT_MetaTileEntity_BasicBatteryBuffer(188, "batterybuffer.09.tier.08", "UV Battery Buffer", 8, "", 9).getStackForm(1L));
//        Battery_Buffer_3by3_MAX.set(new GT_MetaTileEntity_BasicBatteryBuffer(189, "batterybuffer.09.tier.09", "UHV Battery Buffer", 9, "", 9).getStackForm(1L));
//        addCraftingRecipe(Battery_Buffer_3by3_ULV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_ULV, 'W', wireGt08.get(Tin), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_3by3_LV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_LV, 'W', wireGt08.get(Tin), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_3by3_MV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_MV, 'W', wireGt08.get(AnyCopper), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_3by3_HV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_HV, 'W', wireGt08.get(Gold), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_3by3_EV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_EV, 'W', wireGt08.get(Aluminium), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_3by3_IV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_IV, 'W', wireGt08.get(Tungsten), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_3by3_LuV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_LuV, 'W', wireGt08.get(VanadiumGallium), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_3by3_ZPM.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_ZPM, 'W', wireGt08.get(Naquadah), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_3by3_UV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_UV, 'W', wireGt08.get(NaquadahAlloy), 'T', craftingChest});
//        addCraftingRecipe(Battery_Buffer_3by3_MAX.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_MAX, 'W', wireGt08.get(SuperconductorUHV), 'T', craftingChest});

        Battery_Buffer_4by4_ULV.set(new GT_MetaTileEntity_BasicBatteryBuffer(190, "batterybuffer.16.tier.00", "ULV Battery Buffer", 0, "", 16).getStackForm(1L));
        Battery_Buffer_4by4_LV.set(new GT_MetaTileEntity_BasicBatteryBuffer(191, "batterybuffer.16.tier.01", "LV Battery Buffer", 1, "", 16).getStackForm(1L));
        Battery_Buffer_4by4_MV.set(new GT_MetaTileEntity_BasicBatteryBuffer(192, "batterybuffer.16.tier.02", "MV Battery Buffer", 2, "", 16).getStackForm(1L));
        Battery_Buffer_4by4_HV.set(new GT_MetaTileEntity_BasicBatteryBuffer(193, "batterybuffer.16.tier.03", "HV Battery Buffer", 3, "", 16).getStackForm(1L));
        Battery_Buffer_4by4_EV.set(new GT_MetaTileEntity_BasicBatteryBuffer(194, "batterybuffer.16.tier.04", "EV Battery Buffer", 4, "", 16).getStackForm(1L));
        Battery_Buffer_4by4_IV.set(new GT_MetaTileEntity_BasicBatteryBuffer(195, "batterybuffer.16.tier.05", "IV Battery Buffer", 5, "", 16).getStackForm(1L));
        Battery_Buffer_4by4_LuV.set(new GT_MetaTileEntity_BasicBatteryBuffer(196, "batterybuffer.16.tier.06", "LuV Battery Buffer", 6, "", 16).getStackForm(1L));
        Battery_Buffer_4by4_ZPM.set(new GT_MetaTileEntity_BasicBatteryBuffer(197, "batterybuffer.16.tier.07", "ZPM Battery Buffer", 7, "", 16).getStackForm(1L));
        Battery_Buffer_4by4_UV.set(new GT_MetaTileEntity_BasicBatteryBuffer(198, "batterybuffer.16.tier.08", "UV Battery Buffer", 8, "", 16).getStackForm(1L));
        Battery_Buffer_4by4_MAX.set(new GT_MetaTileEntity_BasicBatteryBuffer(199, "batterybuffer.16.tier.09", "UHV Battery Buffer", 9, "", 16).getStackForm(1L));
        addCraftingRecipe(Battery_Buffer_4by4_ULV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_ULV, 'W', wireGt08.get(Tin), 'T', craftingChest});
        addCraftingRecipe(Battery_Buffer_4by4_LV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_LV, 'W', wireGt08.get(Tin), 'T', craftingChest});
        addCraftingRecipe(Battery_Buffer_4by4_MV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_MV, 'W', wireGt08.get(AnyCopper), 'T', craftingChest});
        addCraftingRecipe(Battery_Buffer_4by4_HV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_HV, 'W', wireGt08.get(Gold), 'T', craftingChest});
        addCraftingRecipe(Battery_Buffer_4by4_EV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_EV, 'W', wireGt08.get(Aluminium), 'T', craftingChest});
        addCraftingRecipe(Battery_Buffer_4by4_IV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_IV, 'W', wireGt08.get(Tungsten), 'T', craftingChest});
        addCraftingRecipe(Battery_Buffer_4by4_LuV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_LuV, 'W', wireGt08.get(VanadiumGallium), 'T', craftingChest});
        addCraftingRecipe(Battery_Buffer_4by4_ZPM.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_ZPM, 'W', wireGt08.get(Naquadah), 'T', craftingChest});
        addCraftingRecipe(Battery_Buffer_4by4_UV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_UV, 'W', wireGt08.get(NaquadahAlloy), 'T', craftingChest});
        addCraftingRecipe(Battery_Buffer_4by4_MAX.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", 'M', Hull_MAX, 'W', wireGt08.get(SuperconductorUHV), 'T', craftingChest});

        Battery_Charger_4by4_ULV.set(new GT_MetaTileEntity_Charger(690, "batterycharger.16.tier.00", "ULV Battery Charger", 0, "Each battery gives 8A in/4A out (min 4A/2A)", 4).getStackForm(1L));
        Battery_Charger_4by4_LV.set(new GT_MetaTileEntity_Charger(691, "batterycharger.16.tier.01", "LV Battery Charger", 1, "Each battery gives 8A in/4A out (min 4A/2A)", 4).getStackForm(1L));
        Battery_Charger_4by4_MV.set(new GT_MetaTileEntity_Charger(692, "batterycharger.16.tier.02", "MV Battery Charger", 2, "Each battery gives 8A in/4A out (min 4A/2A)", 4).getStackForm(1L));
        Battery_Charger_4by4_HV.set(new GT_MetaTileEntity_Charger(693, "batterycharger.16.tier.03", "HV Battery Charger", 3, "Each battery gives 8A in/4A out (min 4A/2A)", 4).getStackForm(1L));
        Battery_Charger_4by4_EV.set(new GT_MetaTileEntity_Charger(694, "batterycharger.16.tier.04", "EV Battery Charger", 4, "Each battery gives 8A in/4A out (min 4A/2A)", 4).getStackForm(1L));
        Battery_Charger_4by4_IV.set(new GT_MetaTileEntity_Charger(695, "batterycharger.16.tier.05", "IV Battery Charger", 5, "Each battery gives 8A in/4A out (min 4A/2A)", 4).getStackForm(1L));
        Battery_Charger_4by4_LuV.set(new GT_MetaTileEntity_Charger(696, "batterycharger.16.tier.06", "LuV Battery Charger", 6, "Each battery gives 8A in/4A out (min 4A/2A)", 4).getStackForm(1L));
        Battery_Charger_4by4_ZPM.set(new GT_MetaTileEntity_Charger(697, "batterycharger.16.tier.07", "ZPM Battery Charger", 7, "Each battery gives 8A in/4A out (min 4A/2A)", 4).getStackForm(1L));
        Battery_Charger_4by4_UV.set(new GT_MetaTileEntity_Charger(698, "batterycharger.16.tier.08", "UV Battery Charger", 8, "Each battery gives 8A in/4A out (min 4A/2A)", 4).getStackForm(1L));
        Battery_Charger_4by4_MAX.set(new GT_MetaTileEntity_Charger(699, "batterycharger.16.tier.09", "UHV Battery Charger", 9, "Each battery gives 8A in/4A out (min 4A/2A)", 4).getStackForm(1L));
        addCraftingRecipe(Battery_Charger_4by4_ULV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", "BCB", 'M', Hull_ULV, 'W', wireGt16.get(RedAlloy), 'T', craftingChest, 'B', Battery_RE_HV_Sodium, 'C', circuitPower.get(PWR_LV)});
        addCraftingRecipe(Battery_Charger_4by4_LV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", "BCB", 'M', Hull_LV, 'W', wireGt16.get(Tin), 'T', craftingChest, 'B', Battery_RE_LV_Lithium, 'C', circuitPower.get(PWR_LV)});
        addCraftingRecipe(Battery_Charger_4by4_MV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", "BCB", 'M', Hull_MV, 'W', wireGt16.get(AnyCopper), 'T', craftingChest, 'B', Battery_RE_MV_Lithium, 'C', circuitPower.get(PWR_MV)});
        addCraftingRecipe(Battery_Charger_4by4_HV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", "BCB", 'M', Hull_HV, 'W', wireGt16.get(Gold), 'T', craftingChest, 'B', Battery_RE_HV_Lithium, 'C', circuitPower.get(PWR_HV)});
        addCraftingRecipe(Battery_Charger_4by4_EV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", "BCB", 'M', Hull_EV, 'W', wireGt16.get(Aluminium), 'T', craftingChest, 'B', battery.get(Master), 'C', circuitPower.get(PWR_EV)});
        addCraftingRecipe(Battery_Charger_4by4_IV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", "BCB", 'M', Hull_IV, 'W', wireGt16.get(Tungsten), 'T', craftingChest, 'B', Energy_LapotronicOrb, 'C', circuitPower.get(PWR_IV)});
        addCraftingRecipe(Battery_Charger_4by4_LuV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", "BCB", 'M', Hull_LuV, 'W', wireGt16.get(VanadiumGallium), 'T', craftingChest, 'B', Energy_LapotronicOrb2, 'C', circuitPower.get(PWR_LUV)});
        addCraftingRecipe(Battery_Charger_4by4_ZPM.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", "BCB", 'M', Hull_ZPM, 'W', wireGt16.get(Naquadah), 'T', craftingChest, 'B', Energy_LapotronicOrb2, 'C', circuitPower.get(PWR_ZPM)});
        addCraftingRecipe(Battery_Charger_4by4_UV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", "BCB", 'M', Hull_UV, 'W', wireGt16.get(NaquadahAlloy), 'T', craftingChest, 'B', ZPM2, 'C', circuitPower.get(PWR_UV)});
        addCraftingRecipe(Battery_Charger_4by4_MAX.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"WTW", "WMW", "BCB", 'M', Hull_MAX, 'W', wireGt16.get(SuperconductorUHV), 'T', craftingChest, 'B', ZPM2, 'C', circuitPower.get(PWR_UHV)});

        Locker_ULV.set(new GT_MetaTileEntity_Locker(150, "locker.tier.00", "Locker I", 0).getStackForm(1L));
        Locker_LV.set(new GT_MetaTileEntity_Locker(151, "locker.tier.01", "Locker II", 1).getStackForm(1L));
        Locker_MV.set(new GT_MetaTileEntity_Locker(152, "locker.tier.02", "Locker III", 2).getStackForm(1L));
        Locker_HV.set(new GT_MetaTileEntity_Locker(153, "locker.tier.03", "Locker IV", 3).getStackForm(1L));
        Locker_EV.set(new GT_MetaTileEntity_Locker(154, "locker.tier.04", "Locker V", 4).getStackForm(1L));
        Locker_IV.set(new GT_MetaTileEntity_Locker(155, "locker.tier.05", "Locker VI", 5).getStackForm(1L));
        Locker_LuV.set(new GT_MetaTileEntity_Locker(156, "locker.tier.06", "Locker VII", 6).getStackForm(1L));
        Locker_ZPM.set(new GT_MetaTileEntity_Locker(157, "locker.tier.07", "Locker VIII", 7).getStackForm(1L));
        Locker_UV.set(new GT_MetaTileEntity_Locker(158, "locker.tier.08", "Locker IX", 8).getStackForm(1L));
        Locker_MAX.set(new GT_MetaTileEntity_Locker(159, "locker.tier.09", "Locker X", 9).getStackForm(1L));
        addCraftingRecipe(Locker_ULV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"T", "M", 'M', Battery_Buffer_4by4_ULV, 'T', craftingChest});
        addCraftingRecipe(Locker_LV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"T", "M", 'M', Battery_Buffer_4by4_LV, 'T', craftingChest});
        addCraftingRecipe(Locker_MV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"T", "M", 'M', Battery_Buffer_4by4_MV, 'T', craftingChest});
        addCraftingRecipe(Locker_HV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"T", "M", 'M', Battery_Buffer_4by4_HV, 'T', craftingChest});
        addCraftingRecipe(Locker_EV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"T", "M", 'M', Battery_Buffer_4by4_EV, 'T', craftingChest});
        addCraftingRecipe(Locker_IV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"T", "M", 'M', Battery_Buffer_4by4_IV, 'T', craftingChest});
        addCraftingRecipe(Locker_LuV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"T", "M", 'M', Battery_Buffer_4by4_LuV, 'T', craftingChest});
        addCraftingRecipe(Locker_ZPM.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"T", "M", 'M', Battery_Buffer_4by4_ZPM, 'T', craftingChest});
        addCraftingRecipe(Locker_UV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"T", "M", 'M', Battery_Buffer_4by4_UV, 'T', craftingChest});
        addCraftingRecipe(Locker_MAX.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"T", "M", 'M', Battery_Buffer_4by4_MAX, 'T', craftingChest});

        MicroTransmitter_HV.set(new GT_MetaTileEntity_MicrowaveEnergyTransmitter(1161, "basicmachine.microtransmitter.03", "HV Microwave Energy Transmitter", 3).getStackForm(1L));
        MicroTransmitter_EV.set(new GT_MetaTileEntity_MicrowaveEnergyTransmitter(1162, "basicmachine.microtransmitter.04", "EV Microwave Energy Transmitter", 4).getStackForm(1L));
        MicroTransmitter_IV.set(new GT_MetaTileEntity_MicrowaveEnergyTransmitter(1163, "basicmachine.microtransmitter.05", "IV Microwave Energy Transmitter", 5).getStackForm(1L));
        MicroTransmitter_LUV.set(new GT_MetaTileEntity_MicrowaveEnergyTransmitter(1164, "basicmachine.microtransmitter.06", "LuV Microwave Energy Transmitter", 6).getStackForm(1L));
        MicroTransmitter_ZPM.set(new GT_MetaTileEntity_MicrowaveEnergyTransmitter(1165, "basicmachine.microtransmitter.07", "ZPM Microwave Energy Transmitter", 7).getStackForm(1L));
        MicroTransmitter_UV.set(new GT_MetaTileEntity_MicrowaveEnergyTransmitter(1166, "basicmachine.microtransmitter.08", "UV Microwave Energy Transmitter", 8).getStackForm(1L));
        addCraftingRecipe(MicroTransmitter_HV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CPC", "CMC", "GBG", 'M', Hull_HV, 'B', Battery_RE_HV_Lithium, 'C', Emitter_HV, 'G', circuitPower.get(PWR_HV), 'P', Field_Generator_HV});
        addCraftingRecipe(MicroTransmitter_EV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CPC", "CMC", "GBG", 'M', Hull_EV, 'B', getIC2Item("lapotronCrystal", 1L, GT_Values.W), 'C', Emitter_EV, 'G', circuitPower.get(PWR_EV), 'P', Field_Generator_EV});
        addCraftingRecipe(MicroTransmitter_IV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CPC", "CMC", "GBG", 'M', Hull_IV, 'B', Energy_LapotronicOrb, 'C', Emitter_IV, 'G', circuitPower.get(PWR_IV), 'P', Field_Generator_IV});
        addCraftingRecipe(MicroTransmitter_LUV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CPC", "CMC", "GBG", 'M', Hull_LuV, 'B', Energy_LapotronicOrb2, 'C', Emitter_LuV, 'G', circuitPower.get(PWR_LUV), 'P', Field_Generator_LuV});
        addCraftingRecipe(MicroTransmitter_ZPM.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CPC", "CMC", "GBG", 'M', Hull_ZPM, 'B', sOPStuff.get(gregtechrecipes, "EnableZPMandUVBatteries", false) ? Energy_Module : ZPM2, 'C', Emitter_ZPM, 'G', circuitPower.get(PWR_ZPM), 'P', Field_Generator_ZPM});
        addCraftingRecipe(MicroTransmitter_UV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{"CPC", "CMC", "GBG", 'M', Hull_UV, 'B', sOPStuff.get(gregtechrecipes, "EnableZPMandUVBatteries", false) ? Energy_Module : ZPM3, 'C', Emitter_UV, 'G', circuitPower.get(PWR_UV), 'P', Field_Generator_UV});

        LOADED = true;
    }
}
