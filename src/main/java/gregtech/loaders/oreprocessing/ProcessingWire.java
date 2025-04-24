package gregtech.loaders.oreprocessing;

import appeng.api.config.TunnelType;
import appeng.core.Api;
import cpw.mods.fml.common.Optional;
import gregtech.GT_Mod;
import gregtech.api.enums.*;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class ProcessingWire implements gregtech.api.interfaces.IOreRecipeRegistrator {

    private Materials[] dielectrics = {Materials.PolyvinylChloride, Materials.Polydimethylsiloxane};
    private Materials[] rubbers = {Materials.Rubber, Materials.StyreneButadieneRubber, Materials.Silicone};
    private Materials[] syntheticRubbers = {Materials.StyreneButadieneRubber, Materials.Silicone};

    private static Object tt;
    public ProcessingWire() {
        OrePrefixes.wireGt01.add(this);
        OrePrefixes.wireGt02.add(this);
        OrePrefixes.wireGt04.add(this);
        OrePrefixes.wireGt08.add(this);
        OrePrefixes.wireGt12.add(this);
        OrePrefixes.wireGt16.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        if (GT_Mod.gregtechproxy.mAE2Integration) {
            if (tt == TunnelType.ME) {
                try {
                    tt = TunnelType.valueOf("GT_POWER");
                } catch (IllegalArgumentException ignored) {
                    tt = TunnelType.IC2_POWER;
                }
            }
        }

        int cableWidth;
        OrePrefixes correspondingCable;
        OrePrefixes correspondingWire;
        if (!aMaterial.contains(SubTag.YES_CABLES)) {
        return;
        }
        switch (aPrefix) {
            case wireGt01:
                cableWidth = 1;
                correspondingCable = OrePrefixes.cableGt01;
                if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial) && !aMaterial.contains(SubTag.NO_WORKING)) {
                    GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 1L), GT_Proxy.tBits, new Object[]{"Xx", 'X', OrePrefixes.plate.get(aMaterial)});
                }
                break;
            case wireGt08:
                cableWidth = 8;
                correspondingCable = OrePrefixes.cableGt08;
                correspondingWire = OrePrefixes.wireGt08;
                GT_ModHandler.addShapelessCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 8L), new Object[]{aOreDictName});
                GT_ModHandler.addShapelessCraftingRecipe(GT_Utility.copyAmount(1L, aStack), new Object[]{OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial),OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial)});
                GT_Values.RA.addAssemblerRecipe(GT_Utility.getIntegratedCircuit(8), GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 8L),GT_OreDictUnificator.get(correspondingWire, aMaterial, 1L), 60, 8);
                break;

            case wireGt16 :
                cableWidth = 16;
                correspondingCable = OrePrefixes.cableGt16;
                correspondingWire = OrePrefixes.wireGt16;
                GT_Values.RA.addAssemblerRecipe(GT_Utility.getIntegratedCircuit(16), GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 64L),GT_OreDictUnificator.get(correspondingWire, aMaterial, 1L), 300, 24);
                if (GT_Mod.gregtechproxy.mAE2Integration) {
                    AE2addNewAttunement(aStack);
                }
                break;
            default:
                GT_Log.err.println("OrePrefix " + aPrefix.name() + " cannot be registered as a cable for Material " + aMaterial.mName);
                return;
        }



        int costMultiplier = cableWidth / 4 + 1;


        switch (aMaterial.mName){
            case "RedAlloy":case "Cobalt": case "Lead": case "Tin": case "Zinc":case "SolderingAlloy":
                ArrayList<Object> craftingListRubber = new ArrayList<>();
                craftingListRubber.add(aOreDictName);
                for (int i = 0; i < costMultiplier; i++) {
                    craftingListRubber.add(OrePrefixes.plate.get(Materials.Rubber));
                }
                GT_ModHandler.addShapelessCraftingRecipe(GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), craftingListRubber.toArray());
//                GT_Values.RA.addBoxingRecipe(GT_Utility.copyAmount(1L, aStack), GT_OreDictUnificator.get(OrePrefixes.plate.get(Materials.Rubber), costMultiplier), GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), 100, 8);
//                GT_Values.RA.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 2L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 1L), GT_OreDictUnificator.get(OrePrefixes.cableGt01, aMaterial, 1L), 100, 8);
                GT_Values.RA.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 3L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 2L), GT_OreDictUnificator.get(OrePrefixes.cableGt01, aMaterial, 2L), 140, 16);
                GT_Values.RA.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 4L), GT_OreDictUnificator.get(OrePrefixes.wireGt08, aMaterial, 1L), GT_OreDictUnificator.get(OrePrefixes.cableGt08, aMaterial, 1L), 200, 16);
//                GT_Values.RA.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 4L), GT_OreDictUnificator.get(OrePrefixes.wireGt04, aMaterial, 1L), GT_OreDictUnificator.get(OrePrefixes.cableGt04, aMaterial, 1L), 300, 24);
            case "Iron": case "Nickel": case "Cupronickel": case "Copper": case "AnnealedCopper":
            /*case "Kanthal":*/ case "Gold": case "Electrum": case "Silver": case "Blue Alloy":
            case "Nichrome": /*case "Steel":*/ /*case "BlackSteel":*/ /*case "Titanium":*/ /*case "Aluminium":*/ case "BlueAlloy" :

            case "RedstoneAlloy":
                GT_Values.RA.addAssemblerRecipe(aStack, GT_Utility.getIntegratedCircuit(24), Materials.Rubber.getMolten(144 * costMultiplier), GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), 120, 8);

                break;
        }
//        GT_Values.RA.addUnboxingRecipe(GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), GT_Utility.copyAmount(1L, aStack), null, 100, 8);
        if (GT_Mod.gregtechproxy.mAE2Integration) {
            AE2AddNetAttunementCable(aStack, correspondingCable, aMaterial);
        }
    }

    //region AE2 compat
    static {
        if (GT_Mod.gregtechproxy.mAE2Integration)
            setAE2Field();
    }

    @Optional.Method(modid = "appliedenergistics2")
    private static void setAE2Field(){
        tt = TunnelType.ME;
    }

    @Optional.Method(modid = "appliedenergistics2")
    private void AE2addNewAttunement(ItemStack aStack){
        Api.INSTANCE.registries().p2pTunnel().addNewAttunement(aStack, (TunnelType) tt);
    }

    @Optional.Method(modid = "appliedenergistics2")
    private void AE2AddNetAttunementCable(ItemStack aStack, OrePrefixes correspondingCable, Materials aMaterial){
        Api.INSTANCE.registries().p2pTunnel().addNewAttunement(aStack, (TunnelType) tt);
        Api.INSTANCE.registries().p2pTunnel().addNewAttunement(GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L),(TunnelType) tt);
    }
//end region
}
