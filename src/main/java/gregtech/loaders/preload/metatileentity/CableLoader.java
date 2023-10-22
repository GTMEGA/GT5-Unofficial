package gregtech.loaders.preload.metatileentity;

import gregtech.GT_Mod;
import gregtech.api.enums.Materials;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import lombok.*;

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
        val extraLoss = !GT_Mod.gregtechproxy.mHardcoreCables;

        makeWires(RedAlloy, 2000, 0L, 1L, 4L, V[0], true, false);

        makeWires(Cobalt, 1200, extraLoss ? 2L : 2L, extraLoss ? 4L : 4L, 4L, V[1], true, false);
        makeWires(Lead, 1220, extraLoss ? 2L : 2L, extraLoss ? 4L : 4L, 2L, V[1], true, false);
        makeWires(Tin, 1240, extraLoss ? 1L : 1L, extraLoss ? 2L : 2L, 4L, V[1], true, false);

        makeWires(Zinc, 1260, extraLoss ? 1L : 1L, extraLoss ? 2L : 2L, 1L, V[1], true, false);
        makeWires(SolderingAlloy, 1280, extraLoss ? 1L : 1L, extraLoss ? 2L : 2L, 1L, V[1], true, false);

        makeWires(Iron, 1300, extraLoss ? 3L : 4L, extraLoss ? 6L : 8L, 2L, V[2], true, false);
        makeWires(Nickel, 1320, extraLoss ? 3L : 5L, extraLoss ? 6L : 10L, 3L, V[2], true, false);
        makeWires(Cupronickel, 1340, extraLoss ? 3L : 4L, extraLoss ? 6L : 8L, 2L, V[2], true, false);
        makeWires(Copper, 1360, extraLoss ? 3L : 4L, extraLoss ? 4L : 6L, 6L, V[2], true, false);
        makeWires(AnnealedCopper, 1380, extraLoss ? 1L : 2L, extraLoss ? 2L : 4L, 8L, V[2], true, false);

        makeWires(Kanthal, 1400, extraLoss ? 3L : 8L, extraLoss ? 6L : 16L, 4L, V[3], true, false);
        makeWires(Gold, 1420, extraLoss ? 2L : 6L, extraLoss ? 4L : 12L, 6L, V[3], true, false);
        makeWires(Electrum, 1440, extraLoss ? 2L : 5L, extraLoss ? 4L : 10L, 6L, V[3], true, false);
        makeWires(Silver, 1460, extraLoss ? 1L : 4L, extraLoss ? 2L : 8L, 8L, V[3], true, false);
        makeWires(BlueAlloy, 1480, extraLoss ? 1L : 4L, extraLoss ? 2L : 8L, 10L, V[3], true, false);

        makeWires(Nichrome, 1500, extraLoss ? 4L : 32L, extraLoss ? 8L : 64L, 6L, V[4], true, false);
        makeWires(Steel, 1520, extraLoss ? 2L : 16L, extraLoss ? 4L : 32L, 2L, V[4], true, false);
        makeWires(BlackSteel, 1540, extraLoss ? 2L : 14L, extraLoss ? 4L : 28L, 3L, V[4], true, false);
        makeWires(Titanium, 1560, extraLoss ? 2L : 12L, extraLoss ? 4L : 24L, 6L, V[4], true, false);
        makeWires(Aluminium, 1580, extraLoss ? 1L : 8L, extraLoss ? 2L : 16L, 12L, V[4], true, false);

        makeWires(Graphene, 1600, extraLoss ? 1L : 16L, extraLoss ? 2L : 32L, 16L, V[5], false, true);
        makeWires(Osmium, 1620, extraLoss ? 2L : 32L, extraLoss ? 4L : 64L, 4L, V[5], true, false);
        makeWires(Platinum, 1640, extraLoss ? 1L : 16L, extraLoss ? 2L : 32L, 14L, V[5], true, false);
        makeWires(TungstenSteel, 1660, extraLoss ? 2L : 14L, extraLoss ? 4L : 28L, 6L, V[5], true, false);
        makeWires(Tungsten, 1680, extraLoss ? 2L : 12L, extraLoss ? 4L : 24L, 4L, V[5], true, false);

        makeWires(HSSG, 1700, extraLoss ? 2L : 128L, extraLoss ? 4L : 256L, 4L, V[6], true, false);
        makeWires(NiobiumTitanium, 1720, extraLoss ? 2L : 128L, extraLoss ? 4L : 256L, 12L, V[6], true, false);
        makeWires(VanadiumGallium, 1740, extraLoss ? 2L : 128L, extraLoss ? 4L : 256L, 12L, V[6], true, false);
        makeWires(YttriumBariumCuprate, 1760, extraLoss ? 4L : 256L, extraLoss ? 8L : 512L, 8L, V[6], true, false);

        makeWires(Naquadah, 1780, extraLoss ? 2L : 64L, extraLoss ? 4L : 128L, 8L, V[7], true, false);

        makeWires(NaquadahAlloy, 1800, extraLoss ? 4L : 64L, extraLoss ? 8L : 128L, 6L, V[8], true, false);
        makeWires(Duranium, 1820, extraLoss ? 8L : 64L, extraLoss ? 16L : 128L, 1L, V[8], true, false);

        makeWires(SuperconductorUHV, 2020, 0L, 0L, 8L, V[9], false, true);
        makeWires(Pentacadmiummagnesiumhexaoxid, 2200, extraLoss ? 1L : 128L, extraLoss ? 2L : 256L, 8L, V[2], true, false);
        makeWires(Titaniumonabariumdecacoppereikosaoxid, 2220, extraLoss ? 1L : 128L, extraLoss ? 2L : 256L, 8L, V[3], true, false);
        makeWires(Uraniumtriplatinid, 2240, extraLoss ? 1L : 128L, extraLoss ? 2L : 256L, 8L, V[4], true, false);
        makeWires(Vanadiumtriindinid, 2260, extraLoss ? 1L : 128L, extraLoss ? 2L : 256L, 8L, V[5], true, false);
        makeWires(Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid, 2280, 2L, 2L, 8L, V[6], true, false);
        makeWires(Tetranaquadahdiindiumhexaplatiumosminid, 2300, 2L, 2L, 8L, V[7], true, false);
        makeWires(Longasssuperconductornameforuvwire, 2500, 2L, 2L, 8L, V[8], true, false);
        makeWires(Longasssuperconductornameforuhvwire, 2520, 2L, 2L, 8L, V[9], true, false);
        makeWires(SuperconductorMV, 2320, 0L, 0L, 8L, V[2], false, true);
        makeWires(SuperconductorHV, 2340, 0L, 0L, 8L, V[3], false, true);
        makeWires(SuperconductorEV, 2360, 0L, 0L, 8L, V[4], false, true);
        makeWires(SuperconductorIV, 2380, 0L, 0L, 8L, V[5], false, true);
        makeWires(SuperconductorLuV, 2400, 0L, 0L, 8L, V[6], false, true);
        makeWires(SuperconductorZPM, 2420, 0L, 0L, 8L, V[7], false, true);
        makeWires(SuperconductorUV, 2440, 0L, 0L, 8L, V[8], false, true);
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
        registerOre(wireGt01, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 0, aTextWire1 + aMaterial.mName.toLowerCase() + ".01", "1x " + name + aTextWire2, 0.125F, aMaterial, aLoss, 1L * aAmperage, aVoltage, false, !aAutoInsulated).getStackForm(1L));
        registerOre(wireGt02, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 1, aTextWire1 + aMaterial.mName.toLowerCase() + ".02", "2x " + name + aTextWire2, 0.25F, aMaterial, aLoss, 2L * aAmperage, aVoltage, false, !aAutoInsulated).getStackForm(1L));
        registerOre(wireGt04, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 2, aTextWire1 + aMaterial.mName.toLowerCase() + ".04", "4x " + name + aTextWire2, 0.375F, aMaterial, aLoss, 4L * aAmperage, aVoltage, false, !aAutoInsulated).getStackForm(1L));
        registerOre(wireGt08, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 3, aTextWire1 + aMaterial.mName.toLowerCase() + ".08", "8x " + name + aTextWire2, 0.5F, aMaterial, aLoss, 8L * aAmperage, aVoltage, false, !aAutoInsulated).getStackForm(1L));
        registerOre(wireGt12, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 4, aTextWire1 + aMaterial.mName.toLowerCase() + ".12", "12x " + name + aTextWire2, 0.625F, aMaterial, aLoss, 12L * aAmperage, aVoltage, false, !aAutoInsulated).getStackForm(1L));
        registerOre(wireGt16, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 5, aTextWire1 + aMaterial.mName.toLowerCase() + ".16", "16x " + name + aTextWire2, 0.75F, aMaterial, aLoss, 16L * aAmperage, aVoltage, false, !aAutoInsulated).getStackForm(1L));
        if (aInsulatable) {
            registerOre(cableGt01, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 6, aTextCable1 + aMaterial.mName.toLowerCase() + ".01", "1x " + name + aTextCable2, 0.25F, aMaterial, aLossInsulated, 1L * aAmperage, aVoltage, true, false).getStackForm(1L));
            registerOre(cableGt02, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 7, aTextCable1 + aMaterial.mName.toLowerCase() + ".02", "2x " + name + aTextCable2, 0.375F, aMaterial, aLossInsulated, 2L * aAmperage, aVoltage, true, false).getStackForm(1L));
            registerOre(cableGt04, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 8, aTextCable1 + aMaterial.mName.toLowerCase() + ".04", "4x " + name + aTextCable2, 0.5F, aMaterial, aLossInsulated, 4L * aAmperage, aVoltage, true, false).getStackForm(1L));
            registerOre(cableGt08, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 9, aTextCable1 + aMaterial.mName.toLowerCase() + ".08", "8x " + name + aTextCable2, 0.625F, aMaterial, aLossInsulated, 8L * aAmperage, aVoltage, true, false).getStackForm(1L));
            registerOre(cableGt12, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 10, aTextCable1 + aMaterial.mName.toLowerCase() + ".12", "12x " + name + aTextCable2, 0.75F, aMaterial, aLossInsulated, 12L * aAmperage, aVoltage, true, false).getStackForm(1L));
            registerOre(cableGt16, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 11, aTextCable1 + aMaterial.mName.toLowerCase() + ".16", "16x " + name + aTextCable2, 0.875F, aMaterial, aLossInsulated, 16L * aAmperage, aVoltage, true, false).getStackForm(1L));
        }
    }
}
