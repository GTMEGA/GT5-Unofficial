package gregtech.loaders.preload.metatileentity;

import codechicken.nei.api.API;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import lombok.*;
import net.minecraft.item.ItemStack;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.OrePrefixes.*;
import static gregtech.api.util.GT_LanguageManager.i18nPlaceholder;
import static gregtech.api.util.GT_ModHandler.RecipeBits.BUFFERED;
import static gregtech.api.util.GT_ModHandler.RecipeBits.NOT_REMOVABLE;
import static gregtech.api.util.GT_ModHandler.addCraftingRecipe;
import static gregtech.api.util.GT_ModHandler.getIC2Item;
import static gregtech.api.util.GT_OreDictUnificator.registerOre;

public final class CableLoader {
    private static final String aTextWire1 = "wire.";
    private static final String aTextCable1 = "cable.";
    private static final String aTextWire2 = " Wire";
    private static final String aTextCable2 = " Cable";

    private static boolean LOADED = false;

    public static void load() {
        if (LOADED)
            throw new RuntimeException("Already loaded!");
//        val extraLoss = !GT_Mod.gregtechproxy.mHardcoreCables;

        makeWires(RedAlloy, 2000, 0L, 8L, 16L, V[0], true, false);

        makeWires(Cobalt, 1200, 32, 32, 1L, V[1], true, false);
        makeWires(Lead, 1220, 32,32, 1L, V[1], true, false);
        makeWires(Tin, 1240, 2, 32, 16L, V[1], true, false);
        makeWires(Silver, 1460, 1,32, 16L, V[1], true, false);
        makeWires(Zinc, 1260, 32,32, 1L, V[1], true, false);
        makeWires(SolderingAlloy, 1280, 32,32, 1L, V[1], true, false);

        makeWires(Iron, 1300, 128,128, 2L, V[2], true, false);
        makeWires(Nickel, 1320, 128,128, 3L, V[2], true, false);
        makeWires(Cupronickel, 1340, 2,128, 1L, V[2], true, false);
        makeWires(Copper, 1360, 2,128, 16L, V[2], true, false);
        makeWires(AnnealedCopper, 1380, 1,128, 16L, V[2], true, false);

        makeWires(Gold, 1420, 3,512, 32L, V[3], true, false);
        makeWires(Electrum, 1440, 2,512, 16L, V[3], true, false);
        makeWires(BlueAlloy, 1480, 1,512, 16L, V[3], true, false);
        makeWires(Nichrome, 1500, 4,512, 1L, V[3], true, false);
        makeWires(Aluminium, 1580, 4,512, 16L, V[3], true, false);

        makeWires(Kanthal, 1400, 3,2048, 1L, V[4], true, false);
        makeWires(Steel, 1520, 5,2048, 8L, V[4], true, false);
        makeWires(BlackSteel, 1540, 2048,2048, 3L, V[4], true, false);
        makeWires(Titanium, 1560, 3,2048, 32L, V[4], true, false);
        makeWires(Graphene, 1600, 1,2048, 16L, V[4], false, true);

        makeWires(Osmium, 1620, 8192, 8192, 14L, V[5], true, false);
        makeWires(Platinum, 1640, 4, 8, 14L, V[5], true, false);
        makeWires(TungstenSteel, 1660, 8192, 8192, 6L, V[5], true, false);
        makeWires(Tungsten, 1680, 8192, 8192, 4L, V[5], true, false);

        makeWires(HSSG, 1700, 8192, 8192, 4L, V[6], true, false);
        makeWires(NiobiumTitanium, 1720, 8192, 8192, 12L, V[6], true, false);
        makeWires(VanadiumGallium, 1740, 16, 32, 12L, V[6], true, false);
        makeWires(YttriumBariumCuprate, 1760, 8192, 8192, 8L, V[6], true, false);

        makeWires(Naquadah, 1780, 32, 64, 8L, V[7], true, false);

        makeWires(NaquadahAlloy, 1800, 128, 256, 6L, V[8], true, false);
        makeWires(Duranium, 1820, 8192, 8192, 1L, V[8], true, false);

        makeWires(SuperconductorUHV, 2020, 69420L, 69420L, 8L, V[9], false, true);
        makeWires(Pentacadmiummagnesiumhexaoxid, 2200, 69420L, 69420L, 8L, V[2], true, false);
        makeWires(Titaniumonabariumdecacoppereikosaoxid, 2220, 69420L, 69420L, 8L, V[3], true, false);
        makeWires(Uraniumtriplatinid, 2240, 69420L, 69420L, 8L, V[4], true, false);
        makeWires(Vanadiumtriindinid, 2260, 69420L, 69420L, 8L, V[5], true, false);
        makeWires(Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid, 2280, 2L, 2L, 8L, V[6], true, false);
        makeWires(Tetranaquadahdiindiumhexaplatiumosminid, 2300, 69420L, 69420L, 8L, V[7], true, false);
        makeWires(Longasssuperconductornameforuvwire, 2500, 69420L, 69420L, 8L, V[8], true, false);
        makeWires(Longasssuperconductornameforuhvwire, 2520, 69420L, 69420L, 8L, V[9], true, false);
        makeWires(SuperconductorMV, 2320, 69420L, 69420L, 8L, V[2], false, true);
        makeWires(SuperconductorHV, 2340, 69420L, 69420L, 8L, V[3], false, true);
        makeWires(SuperconductorEV, 2360, 69420L, 69420L, 8L, V[4], false, true);
        makeWires(SuperconductorIV, 2380, 69420L, 69420L, 8L, V[5], false, true);
        makeWires(SuperconductorLuV, 2400, 69420L, 69420L, 8L, V[6], false, true);
        makeWires(SuperconductorZPM, 2420, 69420L, 69420L, 8L, V[7], false, true);
        makeWires(SuperconductorUV, 2440, 69420L, 69420L, 8L, V[8], false, true);
        //makeWires(Materials.SuperconductorUHV, 2540, 0L, 0L, 24L, gregtech.api.enums.GT_Values.V[9], aBoolConst_0, true);

        makeWires(Ichorium, 2600, 2L, 2L, 12L, V[9], false, true);

        if (!GT_Mod.gregtechproxy.mDisableIC2Cables) {
            addCraftingRecipe(getIC2Item("copperCableItem", 2L), NOT_REMOVABLE | BUFFERED, new Object[]{"xP", 'P', plate.get(AnyCopper)});
            addCraftingRecipe(getIC2Item("goldCableItem", 4L), NOT_REMOVABLE | BUFFERED, new Object[]{"xP", 'P', plate.get(Gold)});
            addCraftingRecipe(getIC2Item("ironCableItem", 3L), NOT_REMOVABLE | BUFFERED, new Object[]{"xP", 'P', plate.get(AnyIron)});
            addCraftingRecipe(getIC2Item("tinCableItem", 3L), NOT_REMOVABLE | BUFFERED, new Object[]{"xP", 'P', plate.get(Tin)});
        }
        LOADED = true;
    }

    private static void makeWires(Materials aMaterial, int aStartID, long aLossInsulated, long aLoss, long aAmperage, long aVoltage, boolean aInsulatable, boolean aAutoInsulated) {
        val name = i18nPlaceholder ? "%material" : aMaterial.mDefaultLocalName;
        registerOre(wireGt01, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 0, aTextWire1 + aMaterial.mName.toLowerCase() + ".01", "" + name + aTextWire2,0.25F, aMaterial, aLoss, 1L * aAmperage, aVoltage, false, !aAutoInsulated).getStackForm(1L));
        hideRegister(wireGt02, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 1, aTextWire1 + aMaterial.mName.toLowerCase() + ".02", "Deprecated " + name + aTextWire2, 0.1F, aMaterial, aLoss, 1L * aAmperage, aVoltage, false, !aAutoInsulated).getStackForm(1L));
        hideRegister(wireGt04, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 2, aTextWire1 + aMaterial.mName.toLowerCase() + ".04", "Deprecated " + name + aTextWire2, 0.1F, aMaterial, aLoss, 1L * aAmperage, aVoltage, false, !aAutoInsulated).getStackForm(1L));
        registerOre(wireGt08, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 3, aTextWire1 + aMaterial.mName.toLowerCase() + ".08", "Heavy " + name + aTextWire2, 0.5F, aMaterial, aLoss, 8L * aAmperage, aVoltage, false, !aAutoInsulated).getStackForm(1L));
        hideRegister(wireGt12, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 4, aTextWire1 + aMaterial.mName.toLowerCase() + ".12", "Deprecated " + name + aTextWire2, 0.1F, aMaterial, aLoss, 8L * aAmperage, aVoltage, false, !aAutoInsulated).getStackForm(1L));
        hideRegister(wireGt16, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 5, aTextWire1 + aMaterial.mName.toLowerCase() + ".16", "Mega " + name + aTextWire2, 0.875F, aMaterial, aLoss, 32L * aAmperage, aVoltage, false, !aAutoInsulated).getStackForm(1L));

        var amperageMultiplier = 1;
        if  (aMaterial.contains(SubTag.GOOD_CABLES)){
            amperageMultiplier = 2;
        }
        if (aInsulatable) {
            registerOre(cableGt01, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 6, aTextCable1 + aMaterial.mName.toLowerCase() + ".01", "" + name + aTextCable2, 0.375F, aMaterial, aLossInsulated, 1L * aAmperage * amperageMultiplier, aVoltage, true, false).getStackForm(1L));
            hideRegister(cableGt02, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 7, aTextCable1 + aMaterial.mName.toLowerCase() + ".02", "Deprecated " + name + aTextCable2, 0.1F, aMaterial, aLossInsulated, 1L * aAmperage, aVoltage, true, false).getStackForm(1L));
            hideRegister(cableGt04, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 8, aTextCable1 + aMaterial.mName.toLowerCase() + ".04", "Deprecated " + name + aTextCable2, 0.1F, aMaterial, aLossInsulated, 1L * aAmperage, aVoltage, true, false).getStackForm(1L));
            registerOre(cableGt08, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 9, aTextCable1 + aMaterial.mName.toLowerCase() + ".08", "Heavy " + name + aTextCable2, 0.625F, aMaterial, aLossInsulated, 8L * aAmperage, aVoltage, true, false).getStackForm(1L));
            hideRegister(cableGt12, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 10, aTextCable1 + aMaterial.mName.toLowerCase() + ".12", "Deprecated " + name + aTextCable2, 0.1F, aMaterial, aLossInsulated, 8L * aAmperage, aVoltage, true, false).getStackForm(1L));
            hideRegister(cableGt16, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 11, aTextCable1 + aMaterial.mName.toLowerCase() + ".16", "Mega " + name + aTextCable2, 0.9375F, aMaterial, aLossInsulated, 32L * aAmperage, aVoltage, true, false).getStackForm(1L));
        }

    }
    private static void hideRegister(OrePrefixes aPrefix, Object aMaterial, ItemStack aStack) {
        registerOre(aPrefix,aMaterial,aStack);
        if (GregTech_API.mNEI) {
            API.hideItem(aStack);
        }
    }
}
