package gregtech.loaders.preload.metatileentity;

import gregtech.api.enums.Materials;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Item;

import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.OrePrefixes.*;
import static gregtech.api.util.GT_LanguageManager.i18nPlaceholder;
import static gregtech.api.util.GT_OreDictUnificator.registerOre;

public final class ItemPipeLoader {
    private static boolean LOADED = false;

    public static void load() {
        if (LOADED)
            throw new RuntimeException("Already loaded!");
        generateItemPipes(Brass, Brass.mName, 5602, 1);
        generateItemPipes(Electrum, Electrum.mName, 5612, 2);
        generateItemPipes(Platinum, Platinum.mName, 5622, 4);
        generateItemPipes(Osmium, Osmium.mName, 5632, 8);
        generateItemPipes(PolyvinylChloride, PolyvinylChloride.mName, "PVC", 5690, 4);
        generateItemPipes(Nickel, Nickel.mName, 5700, 1);
        generateItemPipes(Cobalt, Cobalt.mName, 5710, 2);
        generateItemPipes(Aluminium, Aluminium.mName, 5720, 2);
        LOADED = true;
    }

    private static void generateItemPipes(Materials aMaterial, String name, int startID, int baseInvSlots) {
        generateItemPipes(aMaterial, name, i18nPlaceholder ? "%material" : aMaterial.mDefaultLocalName, startID, baseInvSlots);
    }

    private static void generateItemPipes(Materials aMaterial, String name, String displayName, int startID, int baseInvSlots) {
        registerOre(pipeMedium.get(aMaterial), new GT_MetaPipeEntity_Item(startID, "GT_Pipe_" + name, displayName + " Item Pipe", 0.50F, aMaterial, baseInvSlots, 32768 / baseInvSlots, false).getStackForm(1L));
        registerOre(pipeLarge.get(aMaterial), new GT_MetaPipeEntity_Item(startID + 1, "GT_Pipe_" + name + "_Large", "Large " + displayName + " Item Pipe", 0.75F, aMaterial, baseInvSlots * 2, 16384 / baseInvSlots, false).getStackForm(1L));
        registerOre(pipeHuge.get(aMaterial), new GT_MetaPipeEntity_Item(startID + 2, "GT_Pipe_" + name + "_Huge", "Huge " + displayName + " Item Pipe", 1.00F, aMaterial, baseInvSlots * 4, 8192 / baseInvSlots, false).getStackForm(1L));
        registerOre(pipeRestrictiveMedium.get(aMaterial), new GT_MetaPipeEntity_Item(startID + 3, "GT_Pipe_Restrictive_" + name, "Restrictive " + displayName + " Item Pipe", 0.50F, aMaterial, baseInvSlots, 3276800 / baseInvSlots, true).getStackForm(1L));
        registerOre(pipeRestrictiveLarge.get(aMaterial), new GT_MetaPipeEntity_Item(startID + 4, "GT_Pipe_Restrictive_" + name + "_Large", "Large Restrictive " + displayName + " Item Pipe", 0.75F, aMaterial, baseInvSlots * 2, 1638400 / baseInvSlots, true).getStackForm(1L));
        registerOre(pipeRestrictiveHuge.get(aMaterial), new GT_MetaPipeEntity_Item(startID + 5, "GT_Pipe_Restrictive_" + name + "_Huge", "Huge Restrictive " + displayName + " Item Pipe", 0.875F, aMaterial, baseInvSlots * 4, 819200 / baseInvSlots, true).getStackForm(1L));
    }
}
