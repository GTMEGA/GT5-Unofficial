package gregtech.loaders.oreprocessing;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ProcessingShaping implements gregtech.api.interfaces.IOreRecipeRegistrator {
    public ProcessingShaping() {
        OrePrefixes.ingot.add(this);
        OrePrefixes.dust.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        if (((aMaterial == Materials.Glass) || (GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L) != null)) && (!aMaterial.contains(SubTag.NO_SMELTING))) {
            long aMaterialMass = aMaterial.getMass();
            int tAmount = (int) (aPrefix.mMaterialAmount / 3628800L);
            if ((tAmount > 0) && (tAmount <= 64) && (aPrefix.mMaterialAmount % 3628800L == 0L)) {
                int tVoltageMultiplier = aMaterial.mBlastFurnaceTemp >= 2800 ? 48 : 12;

                if (aMaterial.contains(SubTag.NO_SMASHING)) {
                    tVoltageMultiplier /= 4;
                } else if (aPrefix.name().startsWith(OrePrefixes.dust.name())) {
                    return;
                }

                if (!OrePrefixes.block.isIgnored(aMaterial.mSmeltInto)) {
//                    GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(9L, aStack), ItemList.Shape_Extruder_Block.get(0L), GT_OreDictUnificator.get(OrePrefixes.block, aMaterial.mSmeltInto, tAmount), 10 * tAmount, 8 * tVoltageMultiplier);
                    GT_Values.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(9L, aStack), ItemList.Shape_Mold_Block.get(0L), GT_OreDictUnificator.get(OrePrefixes.block, aMaterial.mSmeltInto, tAmount), 5 * tAmount, 4 * tVoltageMultiplier);
                }
                if (((aPrefix != OrePrefixes.ingot) || (aMaterial != aMaterial.mSmeltInto))) {
                    GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Ingot.get(0L), GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, tAmount), 10, 4 * tVoltageMultiplier);
                }
                if (!(aMaterial == Materials.Redstone || aMaterial == Materials.Glowstone)) {
                    GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Pipe_Medium.get(0L), GT_OreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial.mSmeltInto, tAmount), 20, 2 * tVoltageMultiplier);
                    GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(4L, aStack), ItemList.Shape_Extruder_Pipe_Large.get(0L), GT_OreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial.mSmeltInto, tAmount), 40 * tAmount, 2 * tVoltageMultiplier);
                }

//                    GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(12L, aStack), ItemList.Shape_Extruder_Pipe_Huge.get(0L), GT_OreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial.mSmeltInto, tAmount), 96 * tAmount, 2 * tVoltageMultiplier);
//                    GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Plate.get(0L), GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, tAmount), (int) Math.max(aMaterialMass * 1L * tAmount, tAmount), 8 * tVoltageMultiplier);
//                    GT_Values.RA.addExtruderRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L), ItemList.Shape_Extruder_Small_Gear.get(0L), GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, aMaterial.mSmeltInto, tAmount), (int) Math.max(aMaterialMass * 1L * tAmount, tAmount), 8 * tVoltageMultiplier);
                    GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(6L, aStack), ItemList.Shape_Extruder_Turbine_Blade.get(0L), GT_OreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial.mSmeltInto, tAmount), (int) Math.max(aMaterialMass * 1L * tAmount, tAmount), 8 * tVoltageMultiplier);
                if (!(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron)) {
                        //GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ring.get(0L), aMaterial.getMolten(36L), GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial, 1L), 100, 2 * tVoltageMultiplier);
                        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L), ItemList.Shape_Mold_Ring.get(0), GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial, 2L), 100, 24);
                       // GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Screw.get(0L), aMaterial.getMolten(18L), GT_OreDictUnificator.get(OrePrefixes.screw, aMaterial, 1L), 50, 2 * tVoltageMultiplier);
                       // GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Rod.get(0L), aMaterial.getMolten(72L), GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L), 150, 2 * tVoltageMultiplier);
                       // GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Bolt.get(0L), aMaterial.getMolten(18L), GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L), 50, 2 * tVoltageMultiplier);
                        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Round.get(0L), aMaterial.getMolten(18L), GT_OreDictUnificator.get(OrePrefixes.round, aMaterial, 1L), 50, 2 * tVoltageMultiplier);
                        //GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Rod_Long.get(0L), aMaterial.getMolten(144L), GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L), 300, 2 * tVoltageMultiplier);
                        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Turbine_Blade.get(0L), aMaterial.getMolten(864L), GT_OreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 1L), 400, 2 * tVoltageMultiplier);
//                        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Pipe_Tiny.get(0L), aMaterial.getMolten(72L), GT_OreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial, 1L), 20, 2 * tVoltageMultiplier);
//                        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Pipe_Small.get(0L), aMaterial.getMolten(144L), GT_OreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial, 1L), 40, 2 * tVoltageMultiplier);
//                        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Pipe_Medium.get(0L), aMaterial.getMolten(432L), GT_OreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 1L), 80, 2 * tVoltageMultiplier);
//                        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Pipe_Large.get(0L), aMaterial.getMolten(864L), GT_OreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial, 1L), 160, 2 * tVoltageMultiplier);
//                        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Pipe_Huge.get(0L), aMaterial.getMolten(1728L), GT_OreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial, 1L), 320, 2 * tVoltageMultiplier);
                }
//                if (tAmount * 2 <= 64)
//                    GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Rod.get(0L), GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial.mSmeltInto, tAmount * 1), (int) Math.max(aMaterialMass * tAmount * 1.25, tAmount), 6 * tVoltageMultiplier);
//                if (tAmount * 2 <= 64)
//                    GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Wire.get(0L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial.mSmeltInto, tAmount * 2), (int) Math.max(aMaterialMass * 2L * tAmount, tAmount), 6 * tVoltageMultiplier);
//                if (tAmount * 8 <= 64)
//                    GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Bolt.get(0L), GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial.mSmeltInto, tAmount * 8), (int) Math.max(aMaterialMass * 2L * tAmount, tAmount), 8 * tVoltageMultiplier);
                if (tAmount * 4 <= 64) {
//                    GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Ring.get(0L), GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial.mSmeltInto, tAmount * 4), (int) Math.max(aMaterialMass * 2L * tAmount, tAmount), 6 * tVoltageMultiplier);
                    if ((aMaterial.mUnificatable) && (aMaterial.mMaterialInto == aMaterial) && !aMaterial.contains(SubTag.NO_SMASHING))
                     GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial, 1L), GT_Proxy.tBits, new Object[]{"h ", "fX", 'X', OrePrefixes.stick.get(aMaterial)});
                }
                //GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(2L, new Object[]{aStack}), ItemList.Shape_Extruder_Sword.get(0L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, aMaterial.mSmeltInto, tAmount), (int) Math.max(aMaterialMass * 2L * tAmount, tAmount), 8 * tVoltageMultiplier);
                //GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(3L, new Object[]{aStack}), ItemList.Shape_Extruder_Pickaxe.get(0L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, aMaterial.mSmeltInto, tAmount), (int) Math.max(aMaterialMass * 3L * tAmount, tAmount), 8 * tVoltageMultiplier);
                //GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), ItemList.Shape_Extruder_Shovel.get(0L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, aMaterial.mSmeltInto, tAmount), (int) Math.max(aMaterialMass * 1L * tAmount, tAmount), 8 * tVoltageMultiplier);
                //GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(3L, new Object[]{aStack}), ItemList.Shape_Extruder_Axe.get(0L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, aMaterial.mSmeltInto, tAmount), (int) Math.max(aMaterialMass * 3L * tAmount, tAmount), 8 * tVoltageMultiplier);
                //GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(2L, new Object[]{aStack}), ItemList.Shape_Extruder_Hoe.get(0L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, aMaterial.mSmeltInto, tAmount), (int) Math.max(aMaterialMass * 2L * tAmount, tAmount), 8 * tVoltageMultiplier);
                //GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(6L, new Object[]{aStack}), ItemList.Shape_Extruder_Hammer.get(0L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.toolHeadHammer, aMaterial.mSmeltInto, tAmount), (int) Math.max(aMaterialMass * 6L * tAmount, tAmount), 8 * tVoltageMultiplier);
                //GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(2L, new Object[]{aStack}), ItemList.Shape_Extruder_File.get(0L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.toolHeadFile, aMaterial.mSmeltInto, tAmount), (int) Math.max(aMaterialMass * 2L * tAmount, tAmount), 8 * tVoltageMultiplier);
                //GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(2L, new Object[]{aStack}), ItemList.Shape_Extruder_Saw.get(0L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.toolHeadSaw, aMaterial.mSmeltInto, tAmount), (int) Math.max(aMaterialMass * 2L * tAmount, tAmount), 8 * tVoltageMultiplier);
                //GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(4L, new Object[]{aStack}), ItemList.Shape_Extruder_Gear.get(0L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.gearGt, aMaterial.mSmeltInto, tAmount), (int) Math.max(aMaterialMass * 5L * tAmount, tAmount), 8 * tVoltageMultiplier);

                GT_Values.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Mold_Plate.get(0L), GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, tAmount), (int) Math.max(aMaterialMass * 2L * tAmount, tAmount), 2 * tVoltageMultiplier);
                GT_Values.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(3L, aStack), ItemList.Shape_Mold_Gear_Small.get(0L), GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, aMaterial.mSmeltInto, tAmount), (int) Math.max(aMaterialMass * 10L * tAmount, tAmount), 2 * tVoltageMultiplier);
                switch (aMaterial.mSmeltInto.mName) {
                    case "Glass":
                        GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Bottle.get(0L), new ItemStack(Items.glass_bottle, 1), tAmount * 32, 16);
                        GT_Values.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Mold_Bottle.get(0L), new ItemStack(Items.glass_bottle, 1), tAmount * 64, 4);
                        break;
                    case "Steel":
                        GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Cell.get(0L), ItemList.Cell_Empty.get(tAmount), tAmount * 128, 30);
                        if (tAmount * 2 <= 64)
                            GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Casing.get(0L), GT_ModHandler.getIC2Item("casingadviron", tAmount * 2), tAmount * 32, 3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64)
                            GT_Values.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Mold_Casing.get(0L), GT_ModHandler.getIC2Item("casingadviron", tAmount * 3), tAmount * 128, 1 * tVoltageMultiplier);
                        break;
                    case "Iron":
                    case "WroughtIron":
                        GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Cell.get(0L), GT_ModHandler.getIC2Item("fuelRod", tAmount), tAmount * 128, 30);
                        if (tAmount * 2 <= 64)
                            GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Casing.get(0L), GT_ModHandler.getIC2Item("casingiron", tAmount * 2), tAmount * 32, 3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64)
                            GT_Values.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Mold_Casing.get(0L), GT_ModHandler.getIC2Item("casingiron", tAmount * 3), tAmount * 128, 1 * tVoltageMultiplier);
                        if (tAmount * 31 <= 64)
                            GT_Values.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(31L, aStack), ItemList.Shape_Mold_Anvil.get(0L), new ItemStack(Blocks.anvil, 1, 0), tAmount * 512, 4 * tVoltageMultiplier);
                        break;
                    case "Tin":
                        GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Extruder_Cell.get(0L), ItemList.Cell_Empty.get(tAmount), tAmount * 128, 30);
                        if (tAmount * 2 <= 64)
                            GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Casing.get(0L), GT_ModHandler.getIC2Item("casingtin", tAmount * 2), tAmount * 32, 3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64)
                            GT_Values.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Mold_Casing.get(0L), GT_ModHandler.getIC2Item("casingtin", tAmount * 3), tAmount * 128, 1 * tVoltageMultiplier);
                        break;
                    case "Lead":
                        if (tAmount * 2 <= 64)
                            GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Casing.get(0L), GT_ModHandler.getIC2Item("casinglead", tAmount * 2), tAmount * 32, 3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64)
                            GT_Values.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Mold_Casing.get(0L), GT_ModHandler.getIC2Item("casinglead", tAmount * 3), tAmount * 128, 1 * tVoltageMultiplier);
                        break;
                    case "Copper":
                    case "AnnealedCopper":
                        if (tAmount * 2 <= 64)
                            GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Casing.get(0L), GT_ModHandler.getIC2Item("casingcopper", tAmount * 2), tAmount * 32, 3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64)
                            GT_Values.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Mold_Casing.get(0L), GT_ModHandler.getIC2Item("casingcopper", tAmount * 3), tAmount * 128, 1 * tVoltageMultiplier);
                        break;
                    case "Bronze":
                        if (tAmount * 2 <= 64)
                            GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Casing.get(0L), GT_ModHandler.getIC2Item("casingbronze", tAmount * 2), tAmount * 32, 3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64)
                            GT_Values.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Mold_Casing.get(0L), GT_ModHandler.getIC2Item("casingbronze", tAmount * 3), tAmount * 128, 1 * tVoltageMultiplier);
                        break;
                    case "Gold":
                        if (tAmount * 2 <= 64)
                            GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Casing.get(0L), GT_ModHandler.getIC2Item("casinggold", tAmount * 2), tAmount * 32, 3 * tVoltageMultiplier);
                        if (tAmount * 2 <= 64)
                            GT_Values.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Mold_Casing.get(0L), GT_ModHandler.getIC2Item("casinggold", tAmount * 3), tAmount * 128, 1 * tVoltageMultiplier);
                        break;
                    case "Polytetrafluoroethylene":
                        GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Cell.get(0L), ItemList.Cell_Empty.get(tAmount * 4), tAmount * 128, 30);
                    	break;
                }
            }
        }
    }
}
