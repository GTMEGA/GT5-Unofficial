package gregtech.loaders.preload.metatileentity;


import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Pump;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_QuantumTank;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_SuperTank;

import static gregtech.api.enums.ItemList.*;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.OrePrefixes.circuitPower;
import static gregtech.api.enums.OrePrefixes.pipeLarge;
import static gregtech.api.util.GT_ModHandler.addCraftingRecipe;
import static gregtech.loaders.preload.GT_Loader_MetaTileEntities.DISMANTLEABLE_RECIPE_MASK;


public final class FluidStuffLoader {

    private static boolean LOADED = false;

    public static void load() {
        if (LOADED) {
            throw new RuntimeException("Already loaded!");
        }
        Quantum_Tank_LV.set(new GT_MetaTileEntity_QuantumTank(120, "quantum.tank.tier.06", "Quantum Tank I", 6).getStackForm(1L));
        Quantum_Tank_MV.set(new GT_MetaTileEntity_QuantumTank(121, "quantum.tank.tier.07", "Quantum Tank II", 7).getStackForm(1L));
        Quantum_Tank_HV.set(new GT_MetaTileEntity_QuantumTank(122, "quantum.tank.tier.08", "Quantum Tank III", 8).getStackForm(1L));
        Quantum_Tank_EV.set(new GT_MetaTileEntity_QuantumTank(123, "quantum.tank.tier.09", "Quantum Tank IV", 9).getStackForm(1L));
        Quantum_Tank_IV.set(new GT_MetaTileEntity_QuantumTank(124, "quantum.tank.tier.10", "Quantum Tank V", 10).getStackForm(1L));

        Super_Tank_LV.set(new GT_MetaTileEntity_SuperTank(130, "super.tank.tier.01", "Super Tank I", 1).getStackForm(1L));
        Super_Tank_MV.set(new GT_MetaTileEntity_SuperTank(131, "super.tank.tier.02", "Super Tank II", 2).getStackForm(1L));
        Super_Tank_HV.set(new GT_MetaTileEntity_SuperTank(132, "super.tank.tier.03", "Super Tank III", 3).getStackForm(1L));
        Super_Tank_EV.set(new GT_MetaTileEntity_SuperTank(133, "super.tank.tier.04", "Super Tank IV", 4).getStackForm(1L));
        Super_Tank_IV.set(new GT_MetaTileEntity_SuperTank(134, "super.tank.tier.05", "Super Tank V", 5).getStackForm(1L));

        Pump_LV.set(new GT_MetaTileEntity_Pump(1140, "basicmachine.pump.tier.01", "Basic Pump", 1).getStackForm(1L));
        Pump_MV.set(new GT_MetaTileEntity_Pump(1141, "basicmachine.pump.tier.02", "Advanced Pump", 2).getStackForm(1L));
        Pump_HV.set(new GT_MetaTileEntity_Pump(1142, "basicmachine.pump.tier.03", "Advanced Pump II", 3).getStackForm(1L));
        Pump_EV.set(new GT_MetaTileEntity_Pump(1143, "basicmachine.pump.tier.04", "Advanced Pump III", 4).getStackForm(1L));
        Pump_IV.set(new GT_MetaTileEntity_Pump(1144, "basicmachine.pump.tier.05", "Advanced Pump IV", 5).getStackForm(1L));
        addCraftingRecipe(
                Pump_LV.get(1L), DISMANTLEABLE_RECIPE_MASK,
                new Object[]{"CPC", "PMP", "BPB", 'M', Hull_LV, 'B', pipeLarge.get(Brass), 'C', circuitPower.get(PWR_LV), 'P', Electric_Pump_LV}
                         );
        addCraftingRecipe(
                Pump_MV.get(1L), DISMANTLEABLE_RECIPE_MASK,
                new Object[]{"CPC", "PMP", "BPB", 'M', Hull_MV, 'B', pipeLarge.get(Steel), 'C', circuitPower.get(PWR_MV), 'P', Electric_Pump_MV}
                         );
        addCraftingRecipe(
                Pump_HV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "CPC", "PMP", "BPB", 'M', Hull_HV, 'B', pipeLarge.get(StainlessSteel), 'C', circuitPower.get(PWR_HV), 'P', Electric_Pump_HV
                });
        addCraftingRecipe(
                Pump_EV.get(1L), DISMANTLEABLE_RECIPE_MASK,
                new Object[]{"CPC", "PMP", "BPB", 'M', Hull_EV, 'B', pipeLarge.get(Titanium), 'C', circuitPower.get(PWR_EV), 'P', Electric_Pump_EV}
                         );
        addCraftingRecipe(
                Pump_IV.get(1L), DISMANTLEABLE_RECIPE_MASK, new Object[]{
                        "CPC", "PMP", "BPB", 'M', Hull_IV, 'B', pipeLarge.get(TungstenSteel), 'C', circuitPower.get(PWR_IV), 'P', Electric_Pump_IV
                });
        LOADED = true;
    }

}
