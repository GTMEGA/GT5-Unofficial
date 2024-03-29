package gregtech.loaders.preload.metatileentity;


import gregtech.api.metatileentity.implementations.dev.GT_MetaTileEntity_DevEnergySource;
import gregtech.api.metatileentity.implementations.dev.GT_MetaTileEntity_DevFluidSource;
import gregtech.api.metatileentity.implementations.dev.GT_MetaTileEntity_DevItemSource;

import static gregtech.api.enums.ItemList.*;


public final class DevLoader {

    /**
     * I added this specifically so that it runs last
     */

    private static boolean LOADED = false;

    public static void load() {
        if (LOADED) {
            throw new RuntimeException("Already loaded!");
        }
        DEV_ENERGY_SOURCE.set(new GT_MetaTileEntity_DevEnergySource(700, "dev.energy_source", "Debug Energy Source").getStackForm(1L));
        DEV_ITEM_SOURCE.set(new GT_MetaTileEntity_DevItemSource(701, "dev.item_source", "Debug Item Source").getStackForm(1L));
        DEV_FLUID_SOURCE.set(new GT_MetaTileEntity_DevFluidSource(702, "dev.fluid_source", "Debug Fluid Source").getStackForm(1));
        LOADED = true;
    }

}
