package gregtech.loaders.postload;

import codechicken.nei.api.API;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.*;
import gregtech.common.GT_DummyWorld;
import gregtech.common.items.GT_MetaGenerated_Item_03;
import ic2.api.item.IC2Items;
import ic2.api.recipe.ILiquidHeatExchangerManager;
import ic2.api.recipe.Recipes;
import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;
import mods.railcraft.common.items.RailcraftToolItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static gregtech.api.enums.GT_Values.*;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.OrePrefixes.*;

public class GT_MachineRecipeLoader implements Runnable {
    private final MaterialStack[][] mAlloySmelterList = {
            // {new MaterialStack(Materials.Tetrahedrite, 3L), new MaterialStack(Materials.Tin, 1L), new MaterialStack(Materials.Bronze, 3L)},
            // {new MaterialStack(Materials.Tetrahedrite, 3L), new MaterialStack(Materials.Zinc, 1L), new MaterialStack(Materials.Brass, 3L)},
            {new MaterialStack(Materials.Copper, 3L), new MaterialStack(Materials.Tin, 1L), new MaterialStack(Materials.Bronze, 4L)},
            {new MaterialStack(Materials.Copper, 3L), new MaterialStack(Materials.Zinc, 1L), new MaterialStack(Materials.Brass, 4L)},
            {new MaterialStack(Materials.Copper, 1L), new MaterialStack(Materials.Nickel, 1L), new MaterialStack(Materials.Cupronickel, 2L)},
            {new MaterialStack(Materials.Copper, 1L), new MaterialStack(Materials.Redstone, 4L), new MaterialStack(Materials.RedAlloy, 1L)},
            // {new MaterialStack(Materials.AnnealedCopper, 3L), new MaterialStack(Materials.Tin, 1L), new MaterialStack(Materials.Bronze, 4L)},
            // {new MaterialStack(Materials.AnnealedCopper, 3L), new MaterialStack(Materials.Zinc, 1L), new MaterialStack(Materials.Brass, 4L)},
            // {new MaterialStack(Materials.AnnealedCopper, 1L), new MaterialStack(Materials.Nickel, 1L), new MaterialStack(Materials.Cupronickel, 2L)},
            // {new MaterialStack(Materials.AnnealedCopper, 1L), new MaterialStack(Materials.Redstone, 4L), new MaterialStack(Materials.RedAlloy, 1L)},
//            {new MaterialStack(Materials.Iron, 1L), new MaterialStack(Materials.Tin, 1L), new MaterialStack(Materials.TinAlloy, 2L)},
//            {new MaterialStack(Materials.WroughtIron, 1L), new MaterialStack(Materials.Tin, 1L), new MaterialStack(Materials.TinAlloy, 2L)},
            {new MaterialStack(Materials.Steel, 2L), new MaterialStack(Materials.Nickel, 1L), new MaterialStack(Materials.Invar, 3L)},
//            {new MaterialStack(Materials.WroughtIron, 2L), new MaterialStack(Materials.Nickel, 1L), new MaterialStack(Materials.Invar, 3L)},
            {new MaterialStack(Materials.Tin, 6L), new MaterialStack(Materials.Lead, 4L), new MaterialStack(Materials.SolderingAlloy, 10L)},
//            {new MaterialStack(Materials.Lead, 4L), new MaterialStack(Materials.Antimony, 1L), new MaterialStack(Materials.BatteryAlloy, 5L)},
            {new MaterialStack(Materials.Gold, 1L), new MaterialStack(Materials.Silver, 1L), new MaterialStack(Materials.Electrum, 2L)},
//            {new MaterialStack(Materials.Silver, 1L), new MaterialStack(Materials.Electrotine, 4L), new MaterialStack(Materials.BlueAlloy, 1L)},
            {new MaterialStack(Materials.Boron, 1L), new MaterialStack(Materials.Glass, 7L), new MaterialStack(Materials.BorosilicateGlass, 8L)}};
    private static final String aTextAE = "appliedenergistics2"; private static final String aTextAEMM = "item.ItemMultiMaterial"; private static final String aTextForestry = "Forestry";
    private static final String aTextEBXL = "ExtrabiomesXL"; private static final String aTextTCGTPage = "gt.research.page.1.";
    private static final Boolean isNEILoaded = Loader.isModLoaded("NotEnoughItems");

    @Override
    public void run() {
        GT_Log.out.println("GT_Mod: Adding non-OreDict Machine Recipes.");
        try {
            GT_Utility.removeSimpleIC2MachineRecipe(GT_Values.NI, ic2.api.recipe.Recipes.metalformerExtruding.getRecipes(), ItemList.Cell_Empty.get(3L));
            GT_Utility.removeSimpleIC2MachineRecipe(ItemList.IC2_Energium_Dust.get(1L), ic2.api.recipe.Recipes.compressor.getRecipes(), GT_Values.NI);
            GT_Utility.removeSimpleIC2MachineRecipe(new ItemStack(Items.gunpowder), ic2.api.recipe.Recipes.extractor.getRecipes(), GT_Values.NI);
            GT_Utility.removeSimpleIC2MachineRecipe(new ItemStack(Blocks.wool, 1, 32767), ic2.api.recipe.Recipes.extractor.getRecipes(), GT_Values.NI);
			GT_Utility.removeSimpleIC2MachineRecipe(new ItemStack(Blocks.gravel), ic2.api.recipe.Recipes.oreWashing.getRecipes(), GT_Values.NI);
            } catch (Throwable ignored) {
        }
        GT_Utility.removeIC2BottleRecipe(GT_ModHandler.getIC2Item("fuelRod", 1), GT_ModHandler.getIC2Item("UranFuel", 1), ic2.api.recipe.Recipes.cannerBottle.getRecipes(), GT_ModHandler.getIC2Item("reactorUraniumSimple", 1, 1));
        GT_Utility.removeIC2BottleRecipe(GT_ModHandler.getIC2Item("fuelRod", 1), GT_ModHandler.getIC2Item("MOXFuel", 1), ic2.api.recipe.Recipes.cannerBottle.getRecipes(), GT_ModHandler.getIC2Item("reactorMOXSimple", 1, 1));
        GT_Values.RA.addFluidExtractionRecipe(new ItemStack(Items.wheat_seeds, 1, 32767), GT_Values.NI, Materials.SeedOil.getFluid(10), 10000, 32, 2);
        GT_Values.RA.addFluidExtractionRecipe(new ItemStack(Items.melon_seeds, 1, 32767), GT_Values.NI, Materials.SeedOil.getFluid(10), 10000, 32, 2);
        GT_Values.RA.addFluidExtractionRecipe(new ItemStack(Items.pumpkin_seeds, 1, 32767), GT_Values.NI, Materials.SeedOil.getFluid(10), 10000, 32, 2);
        GT_Values.RA.addFluidExtractionRecipe(ItemList.Crop_Drop_Rape.get(1), null, Materials.SeedOil.getFluid(125), 10000, 32, 2);

        try {
            GT_DummyWorld tWorld = (GT_DummyWorld) GT_Values.DW;
            while (tWorld.mRandom.mIterationStep > 0) {
                GT_Values.RA.addFluidExtractionRecipe(GT_Utility.copyAmount(1L, ForgeHooks.getGrassSeed(tWorld)), GT_Values.NI, Materials.SeedOil.getFluid(5L), 10000, 64, 2);
            }
        } catch (Throwable e) {
            GT_Log.out.println("GT_Mod: failed to iterate somehow, maybe it's your Forge Version causing it. But it's not that important\n");
            e.printStackTrace(GT_Log.err);
        }

        {
            ItemStack ironRod = GT_ModHandler.getModItem("IC2","itemRecipePart",1,11);
            GT_Values.RA.addExtruderRecipe(Materials.Iron.getBlocks(1),ItemList.Shape_Extruder_Shaft.get(0),ironRod,640,120);
            GT_Values.RA.addExtruderRecipe(Materials.Iron.getIngots(9),ItemList.Shape_Extruder_Shaft.get(0),ironRod,640,120);
            ItemStack steelRod = GT_ModHandler.getModItem("IC2","itemRecipePart",1,12);
            GT_Values.RA.addExtruderRecipe(Materials.Steel.getBlocks(1),ItemList.Shape_Extruder_Shaft.get(0),steelRod,1280,120);
        }
        GT_Values.RA.addArcFurnaceRecipe(ItemList.Block_TungstenSteelReinforced.get(1), new ItemStack[]{ GT_OreDictUnificator.get(OrePrefixes.ingot,Materials.TungstenSteel,2), GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Concrete,1)}, null, 160, 96);

        GT_Values.RA.addPrinterRecipe(GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Paper, 1L), FluidRegistry.getFluidStack("squidink", 36), GT_Values.NI, ItemList.Paper_Punch_Card_Empty.get(1L), 100, 2);
        GT_Values.RA.addPrinterRecipe(ItemList.Paper_Punch_Card_Empty.get(1L), FluidRegistry.getFluidStack("squidink", 36), ItemList.Tool_DataStick.getWithName(0L, "With Punch Card Data"), ItemList.Paper_Punch_Card_Encoded.get(1L), 100, 2);
        GT_Values.RA.addPrinterRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 3L), FluidRegistry.getFluidStack("squidink", 144), ItemList.Tool_DataStick.getWithName(0L, "With Scanned Book Data"), ItemList.Paper_Printed_Pages.get(1L), 400, 2);
        GT_Values.RA.addPrinterRecipe(new ItemStack(Items.map, 1, 32767), FluidRegistry.getFluidStack("squidink", 144), ItemList.Tool_DataStick.getWithName(0L, "With Scanned Map Data"), new ItemStack(Items.filled_map, 1, 0), 400, 2);
        GT_Values.RA.addPrinterRecipe(new ItemStack(Items.book, 1, 32767), FluidRegistry.getFluidStack("squidink", 144), GT_Values.NI, GT_Utility.getWrittenBook("Manual_Printer", ItemList.Book_Written_01.get(1L)), 400, 2);

        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.EnderPearl, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.EnderEye, OrePrefixes.dust.mMaterialAmount), (int) (100L * OrePrefixes.dust.mMaterialAmount / 3628800L), 48);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Electrum, 2L * OrePrefixes.dust.mMaterialAmount), (int) (200L * OrePrefixes.dust.mMaterialAmount / 3628800L), 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 2L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Invar, 3L * OrePrefixes.dust.mMaterialAmount), (int) (300L * OrePrefixes.dust.mMaterialAmount / 3628800L), 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Kanthal, 3L * OrePrefixes.dust.mMaterialAmount), (int) (300L * OrePrefixes.dust.mMaterialAmount / 3628800L), 24);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Brass, 4L * OrePrefixes.dust.mMaterialAmount), (int) (400L * OrePrefixes.dust.mMaterialAmount / 3628800L), 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Bronze, 4L * OrePrefixes.dust.mMaterialAmount), (int) (400L * OrePrefixes.dust.mMaterialAmount / 3628800L), 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Cupronickel, 2L * OrePrefixes.dust.mMaterialAmount), (int) (200L * OrePrefixes.dust.mMaterialAmount / 3628800L), 24);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 4L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.SterlingSilver, 5L * OrePrefixes.dust.mMaterialAmount), (int) (500L * OrePrefixes.dust.mMaterialAmount / 3628800L), 120);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Electrum, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.BlackBronze, 4L * OrePrefixes.dust.mMaterialAmount), (int) (500L * OrePrefixes.dust.mMaterialAmount / 3628800L), 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bismuth, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Brass, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.BismuthBronze, 2L * OrePrefixes.dust.mMaterialAmount), (int) (500L * OrePrefixes.dust.mMaterialAmount / 3628800L), 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BlackBronze, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 3L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.BlackSteel, 5L * OrePrefixes.dust.mMaterialAmount), (int) (500L * OrePrefixes.dust.mMaterialAmount / 3628800L), 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SterlingSilver, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BismuthBronze, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 4L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 2L), GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.RedSteel, 8L * OrePrefixes.dust.mMaterialAmount), (int) (800L * OrePrefixes.dust.mMaterialAmount / 3628800L), 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RoseGold, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Brass, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 4L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 2L), GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.BlueSteel, 8L* OrePrefixes.dust.mMaterialAmount), (int) (800L * OrePrefixes.dust.mMaterialAmount / 3628800L), 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 5L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 15L), GT_Utility.getIntegratedCircuit(14), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.BlackSteel, 25L* OrePrefixes.dust.mMaterialAmount), (int) (800L * OrePrefixes.dust.mMaterialAmount / 3628800L), 480);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 4L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 4L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bismuth, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 20L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 10L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(15), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.RedSteel, 40L* OrePrefixes.dust.mMaterialAmount), (int) (1200L * OrePrefixes.dust.mMaterialAmount / 3628800L), 480);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 31L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 4L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 5L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 64L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 16L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 40L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(16), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.BlueSteel, 64L* OrePrefixes.dust.mMaterialAmount), GT_OreDictUnificator.getDust(Materials.BlueSteel, 64L* OrePrefixes.dust.mMaterialAmount), GT_OreDictUnificator.getDust(Materials.BlueSteel, 32L* OrePrefixes.dust.mMaterialAmount), GT_Values.NI, (int) (3600L * OrePrefixes.dust.mMaterialAmount / 3628800L), 480);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 5L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 2L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Molybdenum, 1L), GT_Values.NI, GT_Utility.getIntegratedCircuit(2),GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Ultimet, 9L * OrePrefixes.dust.mMaterialAmount), (int) (900L * OrePrefixes.dust.mMaterialAmount / 3628800L), 500);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Brass, 7L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.CobaltBrass, 9L * OrePrefixes.dust.mMaterialAmount), (int) (900L * OrePrefixes.dust.mMaterialAmount / 3628800L), 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 2L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 3L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Gunpowder, 8L * OrePrefixes.dust.mMaterialAmount), 120, 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 2L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Charcoal, 3L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Gunpowder, 8L * OrePrefixes.dust.mMaterialAmount), 120, 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 2L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 3L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Gunpowder, 8L * OrePrefixes.dust.mMaterialAmount), 120, 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Indium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gallium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.IndiumGalliumPhosphide, 3L * OrePrefixes.dust.mMaterialAmount), (int) (200L * OrePrefixes.dust.mMaterialAmount / 3628800L), 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 4L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Nichrome, 5L * OrePrefixes.dust.mMaterialAmount), (int) (500L * OrePrefixes.dust.mMaterialAmount / 3628800L), 120);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Osmium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iridium, 3L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1),GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Osmiridium, 4L * OrePrefixes.dust.mMaterialAmount), (int) (400L * OrePrefixes.dust.mMaterialAmount / 3628800L), 2000);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Niobium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1),GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.NiobiumTitanium, 2L * OrePrefixes.dust.mMaterialAmount), (int) (200L * OrePrefixes.dust.mMaterialAmount / 3628800L), 2000);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gallium, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1),GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.VanadiumGallium, 4L * OrePrefixes.dust.mMaterialAmount), (int) (400L * OrePrefixes.dust.mMaterialAmount / 3628800L), 2000);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1),GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.TungstenCarbide, 2L * OrePrefixes.dust.mMaterialAmount), (int) (200L * OrePrefixes.dust.mMaterialAmount / 3628800L), 500);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1),GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.TungstenSteel, 2L * OrePrefixes.dust.mMaterialAmount), (int) (200L * OrePrefixes.dust.mMaterialAmount / 3628800L), 500);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TungstenSteel, 5L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Molybdenum, 2L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 1L), GT_Values.NI, GT_Utility.getIntegratedCircuit(1),GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.HSSG, 9L * OrePrefixes.dust.mMaterialAmount), (int) (600L * OrePrefixes.dust.mMaterialAmount / 3628800L), 3500);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HSSG, 6L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Manganese, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L), GT_Values.NI, GT_Utility.getIntegratedCircuit(1),GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.HSSE, 9L * OrePrefixes.dust.mMaterialAmount), (int) (700L * OrePrefixes.dust.mMaterialAmount / 3628800L), 4000);
//        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 4L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.FerriteMixture, 6L * OrePrefixes.dust.mMaterialAmount), (int) (200L * OrePrefixes.dust.mMaterialAmount / 3628800L), 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 7L), GT_Values.NI, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.BorosilicateGlass, 8L * OrePrefixes.dust.mMaterialAmount), (int) (200L * OrePrefixes.dust.mMaterialAmount / 3628800L), 8);

        GT_Values.RA.addMixerRecipe(new ItemStack(Items.rotten_flesh, 1, 0), new ItemStack(Items.fermented_spider_eye, 1, 0), ItemList.IC2_Scrap.get(1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.MeatRaw, 1L), FluidRegistry.getFluidStack("potion.purpledrink", 750), FluidRegistry.getFluidStack("sludge", 1000), ItemList.Food_Chum.get(4L), 128, 24);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(1000L), GT_Values.NF, ItemList.Food_Dough.get(2L), 32, 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L), ItemList.Food_PotatoChips.get(1L), GT_Values.NI, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.Food_ChiliChips.get(1L), 32, 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Redstone, 5L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Ruby, 4L), GT_Values.NI, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.IC2_Energium_Dust.get(1L), 300, 120);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 5L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ruby, 4L), GT_Values.NI, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.IC2_Energium_Dust.get(9L), 600, 120);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), new ItemStack(Blocks.brown_mushroom, 1), new ItemStack(Items.spider_eye, 1), GT_Values.NI, GT_Values.NF, GT_Values.NF, new ItemStack(Items.fermented_spider_eye, 1), 100, 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Gold, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.LiveRoot, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.IronWood, 2L), 100, 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 9L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.LiveRoot, 9L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.IronWood, 18L), 900, 8);
        GT_Values.RA.addMixerRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 1L), GT_Values.NI, Materials.Water.getFluid(500L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Fluix, 2L), 20, 16);
        GT_Values.RA.addMixerRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 1L), GT_Values.NI, GT_ModHandler.getDistilledWater(500L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Fluix, 2L), 20, 16);
        GT_Values.RA.addMixerRecipe(ItemList.IC2_Fertilizer.get(1L), new ItemStack(Blocks.dirt, 8, 32767), GT_Utility.getIntegratedCircuit(1), GT_Values.NI, Materials.Water.getFluid(1000L), GT_Values.NF, GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "soil", 8L, 0), 64, 16);
        GT_Values.RA.addMixerRecipe(ItemList.FR_Fertilizer.get(1L), new ItemStack(Blocks.dirt, 8, 32767),  GT_Utility.getIntegratedCircuit(1), GT_Values.NI, Materials.Water.getFluid(1000L), GT_Values.NF, GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "soil", 8L, 0), 64, 16);
        GT_Values.RA.addMixerRecipe(ItemList.FR_Compost.get(1L), new ItemStack(Blocks.dirt, 8, 32767),  GT_Utility.getIntegratedCircuit(1), GT_Values.NI, Materials.Water.getFluid(1000L), GT_Values.NF, GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "soil", 8L, 0), 64, 16);
        GT_Values.RA.addMixerRecipe(ItemList.FR_Mulch.get(8L), new ItemStack(Blocks.dirt, 8, 32767),  GT_Utility.getIntegratedCircuit(1), GT_Values.NI, Materials.Water.getFluid(1000L), GT_Values.NF, GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "soil", 8L, 0), 64, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.sand, 1, 32767), new ItemStack(Blocks.dirt, 1, 32767),  GT_Utility.getIntegratedCircuit(1), GT_Values.NI, Materials.Water.getFluid(250L), GT_Values.NF, GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "soil", 2L, 1), 16, 16);
         GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 5L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L), GT_Values.NI, GT_Values.NI, Materials.Lubricant.getFluid(25), new FluidStack(ItemList.sDrillingFluid, 250), Materials.Empty.getCells(5), 200, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 7L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GarnetSand, 1L), GT_Values.NI, GT_Values.NI, Materials.Lubricant.getFluid(40), new FluidStack(ItemList.sDrillingFluid, 500), Materials.Empty.getCells(7), 200, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 10L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GarnetYellow, 1L), GT_Values.NI, GT_Values.NI, Materials.Lubricant.getFluid(60), new FluidStack(ItemList.sDrillingFluid, 750), Materials.Empty.getCells(10), 200, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 10L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GarnetRed, 1L), GT_Values.NI, GT_Values.NI, Materials.Lubricant.getFluid(60), new FluidStack(ItemList.sDrillingFluid, 750), Materials.Empty.getCells(10), 200, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lapis, 1L), GT_Utility.getIntegratedCircuit(2), GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(125), FluidRegistry.getFluidStack("ic2coolant", 125), GT_Values.NI, 256, 48);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lapis, 1L), GT_Utility.getIntegratedCircuit(2), GT_Values.NI, GT_Values.NI, GT_ModHandler.getDistilledWater(1000), FluidRegistry.getFluidStack("ic2coolant", 1000), GT_Values.NI, 256, 48);

        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Creosote.getFluid(1000), null, ItemList.SFMixture.get(8), 1600, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Creosote.getFluid(1000), null, ItemList.SFMixture.get(8), 1600, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Creosote.getFluid(1000), null, ItemList.SFMixture.get(12), 1600, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Lubricant.getFluid(300), null, ItemList.SFMixture.get(8), 1200, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Lubricant.getFluid(300), null, ItemList.SFMixture.get(8), 1200, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Lubricant.getFluid(300), null, ItemList.SFMixture.get(12), 1200, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Glue.getFluid(333), null, ItemList.SFMixture.get(8), 800, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Glue.getFluid(333), null, ItemList.SFMixture.get(8), 800, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Glue.getFluid(333), null, ItemList.SFMixture.get(12), 800, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.McGuffium239.getFluid(10), null, ItemList.SFMixture.get(64), 400, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.McGuffium239.getFluid(10), null, ItemList.SFMixture.get(64), 400, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.McGuffium239.getFluid(10), null, ItemList.SFMixture.get(64), 400, 16);


        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(4), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.EnderEye, 1L), null, null, Materials.Mercury.getFluid(200), null, ItemList.MSFMixture.get(4), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(4), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L), null, null, Materials.Mercury.getFluid(200), null, ItemList.MSFMixture.get(4), 100, 64);

        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.MSFMixture.get(6),  GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(1000), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.MSFMixture.get(4), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(800), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.MSFMixture.get(2),     GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(500), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.MSFMixture.get(6),  GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Emerald, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(1000), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.MSFMixture.get(4), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Emerald, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(800), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.MSFMixture.get(2),     GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Emerald, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(500), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.MSFMixture.get(6),  GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sapphire, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(1000), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.MSFMixture.get(4), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sapphire, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(800), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.MSFMixture.get(2),     GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sapphire, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(500), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.MSFMixture.get(6),  GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GreenSapphire, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(1000), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.MSFMixture.get(4), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GreenSapphire, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(800), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.MSFMixture.get(2),     GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GreenSapphire, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(500), null, ItemList.Block_MSSFUEL.get(1), 120, 96);


        if(Loader.isModLoaded("Thaumcraft")){
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(4), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedAir, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.FierySteel.getFluid(10), null, ItemList.MSFMixture.get(4), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(4), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedEarth, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.FierySteel.getFluid(10), null, ItemList.MSFMixture.get(4), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(4), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedEntropy, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.FierySteel.getFluid(10), null, ItemList.MSFMixture.get(4), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(4), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedFire, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.FierySteel.getFluid(10), null, ItemList.MSFMixture.get(4), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(4), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedOrder, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.FierySteel.getFluid(10), null, ItemList.MSFMixture.get(4), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(4), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedWater, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.FierySteel.getFluid(10), null, ItemList.MSFMixture.get(4), 100, 64);

        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(2), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedAir, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Mercury.getFluid(50), null, ItemList.MSFMixture.get(2), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(2), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedEarth, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Mercury.getFluid(50), null, ItemList.MSFMixture.get(2), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(2), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedEntropy, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Mercury.getFluid(50), null, ItemList.MSFMixture.get(2), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(2), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedFire, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Mercury.getFluid(50), null, ItemList.MSFMixture.get(2), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(2), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedOrder, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Mercury.getFluid(50), null, ItemList.MSFMixture.get(2), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(2), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedWater, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Mercury.getFluid(50), null, ItemList.MSFMixture.get(2), 100, 64);

            FluidStack tFD = FluidRegistry.getFluidStack("fluiddeath", 10);
            if (tFD != null && tFD.getFluid() != null && tFD.amount > 0) {
                GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(8), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedAir, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), tFD, null, ItemList.MSFMixture.get(8), 100, 64);
                GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(8), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedEarth, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), tFD, null, ItemList.MSFMixture.get(8), 100, 64);
                GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(8), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedEntropy, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), tFD, null, ItemList.MSFMixture.get(8), 100, 64);
                GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(8), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedFire, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), tFD, null, ItemList.MSFMixture.get(8), 100, 64);
                GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(8), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedOrder, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), tFD, null, ItemList.MSFMixture.get(8), 100, 64);
                GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(8), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedWater, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), tFD, null, ItemList.MSFMixture.get(8), 100, 64);

        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.MSFMixture.get(6), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(1000), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.MSFMixture.get(4), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(800), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.MSFMixture.get(2), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(500), null, ItemList.Block_MSSFUEL.get(1), 120, 96);

                GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1), ItemList.MSFMixture.get(6), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.HeavyFuel.getFluid(1500), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
                GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1), ItemList.MSFMixture.get(4), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.HeavyFuel.getFluid(1200), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
                GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1), ItemList.MSFMixture.get(2), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.HeavyFuel.getFluid(750), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
				//todo CHECK ADDED X3
				GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.SFMixture.get(6), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.LPG.getFluid(1500), null, ItemList.Block_SSFUEL.get(1), 120, 96);
        		GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.SFMixture.get(4), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.LPG.getFluid(1200), null, ItemList.Block_SSFUEL.get(1), 120, 96);
        		GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.SFMixture.get(2), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.LPG.getFluid(750), null, ItemList.Block_SSFUEL.get(1), 120, 96);

        		GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.MSFMixture.get(6), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), null, Materials.LPG.getFluid(1500), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
                GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.MSFMixture.get(4), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), null, Materials.LPG.getFluid(1200), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
                GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.MSFMixture.get(2), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), null, Materials.LPG.getFluid(750), null, ItemList.Block_MSSFUEL.get(1), 120, 96);


            }
        }

        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.SFMixture.get(6), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(1000), null, ItemList.Block_SSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.SFMixture.get(4), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(800), null, ItemList.Block_SSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.SFMixture.get(2), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(500), null, ItemList.Block_SSFUEL.get(1), 120, 96);

        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1), ItemList.SFMixture.get(6), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.HeavyFuel.getFluid(1500), null, ItemList.Block_SSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1), ItemList.SFMixture.get(4), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.HeavyFuel.getFluid(1200), null, ItemList.Block_SSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1), ItemList.SFMixture.get(2), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.HeavyFuel.getFluid(750), null, ItemList.Block_SSFUEL.get(1), 120, 96);

		//todo CHECK ADDED X3
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.SFMixture.get(6), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.LPG.getFluid(1500), null, ItemList.Block_SSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.SFMixture.get(4), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.LPG.getFluid(1200), null, ItemList.Block_SSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.SFMixture.get(2), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.LPG.getFluid(750), null, ItemList.Block_SSFUEL.get(1), 120, 96);

        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.SFMixture.get(6), null, null, Materials.LPG.getFluid(1500), null, ItemList.Block_SSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.SFMixture.get(4), null, null, Materials.LPG.getFluid(1200), null, ItemList.Block_SSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.SFMixture.get(2), null, null, Materials.LPG.getFluid(750), null, ItemList.Block_SSFUEL.get(1), 120, 96);

        GT_Values.RA.addExtruderRecipe(ItemList.FR_Wax.get(1L), ItemList.Shape_Extruder_Cell.get(0L), ItemList.FR_WaxCapsule.get(1L), 64, 16);
        GT_Values.RA.addExtruderRecipe(ItemList.FR_RefractoryWax.get(1L), ItemList.Shape_Extruder_Cell.get(0L), ItemList.FR_RefractoryCapsule.get(1L), 128, 16);

        GT_Values.RA.addFluidCannerRecipe(ItemList.Battery_Hull_LV.get(1L), ItemList.IC2_ReBattery.get(1L), Materials.Redstone.getMolten(288L), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(ItemList.Battery_Hull_LV.get(1L), ItemList.Battery_SU_LV_Mercury.getWithCharge(1L, Integer.MAX_VALUE), Materials.Mercury.getFluid(1000L), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(ItemList.Battery_Hull_MV.get(1L), ItemList.Battery_SU_MV_Mercury.getWithCharge(1L, Integer.MAX_VALUE), Materials.Mercury.getFluid(4000L), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(ItemList.Battery_Hull_HV.get(1L), ItemList.Battery_SU_HV_Mercury.getWithCharge(1L, Integer.MAX_VALUE), Materials.Mercury.getFluid(16000L), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(ItemList.Battery_Hull_LV.get(1L), ItemList.Battery_SU_LV_SulfuricAcid.getWithCharge(1L, Integer.MAX_VALUE), Materials.SulfuricAcid.getFluid(1000L), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(ItemList.Battery_Hull_MV.get(1L), ItemList.Battery_SU_MV_SulfuricAcid.getWithCharge(1L, Integer.MAX_VALUE), Materials.SulfuricAcid.getFluid(4000L), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(ItemList.Battery_Hull_HV.get(1L), ItemList.Battery_SU_HV_SulfuricAcid.getWithCharge(1L, Integer.MAX_VALUE), Materials.SulfuricAcid.getFluid(16000L), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(ItemList.TF_Vial_FieryTears.get(1L), ItemList.Bottle_Empty.get(1L), GT_Values.NF, Materials.FierySteel.getFluid(250L));

//        Materials tMaterial = Materials.Iron;
//        if (tMaterial.mStandardMoltenFluid != null) {
//            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Casing.get(0L), tMaterial.getMolten(72L), ItemList.IC2_Item_Casing_Iron.get(1L), 16, 8);
//        }
        /*tMaterial = Materials.WroughtIron;
        if (tMaterial.mStandardMoltenFluid != null) {
            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Casing.get(0L), tMaterial.getMolten(72L), ItemList.IC2_Item_Casing_Iron.get(1L), 16, 8);
        }*/
//        tMaterial = Materials.Gold;
//        if (tMaterial.mStandardMoltenFluid != null) {
//            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Casing.get(0L), tMaterial.getMolten(72L), ItemList.IC2_Item_Casing_Gold.get(1L), 16, 8);
//        }
//        tMaterial = Materials.Bronze;
//        if (tMaterial.mStandardMoltenFluid != null) {
//            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Casing.get(0L), tMaterial.getMolten(72L), ItemList.IC2_Item_Casing_Bronze.get(1L), 16, 8);
//        }
//        tMaterial = Materials.Copper;
//        if (tMaterial.mStandardMoltenFluid != null) {
//            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Casing.get(0L), tMaterial.getMolten(72L), ItemList.IC2_Item_Casing_Copper.get(1L), 16, 8);
//        }
//        tMaterial = Materials.AnnealedCopper;
//        if (tMaterial.mStandardMoltenFluid != null) {
//            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Casing.get(0L), tMaterial.getMolten(72L), ItemList.IC2_Item_Casing_Copper.get(1L), 16, 8);
//        }
//        tMaterial = Materials.Tin;
//        if (tMaterial.mStandardMoltenFluid != null) {
//            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Casing.get(0L), tMaterial.getMolten(72L), ItemList.IC2_Item_Casing_Tin.get(1L), 16, 8);
//        }
//        tMaterial = Materials.Lead;
//        if (tMaterial.mStandardMoltenFluid != null) {
//            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Casing.get(0L), tMaterial.getMolten(72L), ItemList.IC2_Item_Casing_Lead.get(1L), 16, 8);
//        }
//        tMaterial = Materials.Steel;
//        if (tMaterial.mStandardMoltenFluid != null) {
//            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Casing.get(0L), tMaterial.getMolten(72L), ItemList.IC2_Item_Casing_Steel.get(1L), 16, 8);
//        }
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ball.get(0L), Materials.Mercury.getFluid(1000L), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 1, 3), 128, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ball.get(0L), Materials.Mercury.getFluid(1000L), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Mercury, 1L), 128, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ball.get(0L), Materials.Water.getFluid(250L), new ItemStack(Items.snowball, 1, 0), 128, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ball.get(0L), GT_ModHandler.getDistilledWater(250L), new ItemStack(Items.snowball, 1, 0), 128, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0L), Materials.Water.getFluid(1000L), new ItemStack(Blocks.snow, 1, 0), 512, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0L), GT_ModHandler.getDistilledWater(1000L), new ItemStack(Blocks.snow, 1, 0), 512, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0L), Materials.Lava.getFluid(1000L), new ItemStack(Blocks.obsidian, 1, 0), 1024, 16);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0L), Materials.Concrete.getMolten(144L), new ItemStack(GregTech_API.sBlockConcretes, 1, 8), 12, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0L), Materials.Glowstone.getMolten(576L), new ItemStack(Blocks.glowstone, 1, 0), 12, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0L), Materials.Glass.getMolten(144L), new ItemStack(Blocks.glass, 1, 0), 12, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Plate.get(0L), Materials.Glass.getMolten(144L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Glass, 1L), 12, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Bottle.get(0L), Materials.Glass.getMolten(144L), ItemList.Bottle_Empty.get(1L), 12, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Cylinder.get(0L), Materials.Milk.getFluid(250L), ItemList.Food_Cheese.get(1L), 1024, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Cylinder.get(0L), Materials.Cheese.getMolten(144L), ItemList.Food_Cheese.get(1L), 64, 8);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Anvil.get(0L), Materials.Iron.getMolten(4464L), new ItemStack(Blocks.anvil, 1, 0), 128, 16);
//        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Anvil.get(0L), Materials.WroughtIron.getMolten(4464L), new ItemStack(Blocks.anvil, 1, 0), 128, 16);

        GT_Values.RA.addChemicalBathRecipe(ItemList.Food_Raw_Fries.get(1L), Materials.FryingOilHot.getFluid(10L), ItemList.Food_Fries.get(1L), GT_Values.NI, GT_Values.NI, null, 16, 4);
        GT_Values.RA.addChemicalBathRecipe(GT_ModHandler.getIC2Item("dynamite", 1L), Materials.Glue.getFluid(10L), GT_ModHandler.getIC2Item("stickyDynamite", 1L), GT_Values.NI, GT_Values.NI, null, 16, 4);
        GT_Values.RA.addChemicalRecipe(new ItemStack(Items.paper, 1), new ItemStack(Items.string, 1), Materials.Glyceryl.getFluid(500), GT_Values.NF, GT_ModHandler.getIC2Item("dynamite", 1L), 160, 4);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1L), Materials.Concrete.getMolten(144L), GT_ModHandler.getIC2Item("reinforcedStone", 1L), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L), Materials.Water.getFluid(125L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HydratedCoal, 1L), GT_Values.NI, GT_Values.NI, null, 12, 4);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L), Materials.Water.getFluid(100L), new ItemStack(Items.paper, 1, 0), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Paper, 1L), Materials.Water.getFluid(100L), new ItemStack(Items.paper, 1, 0), GT_Values.NI, GT_Values.NI, null, 100, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Items.reeds, 1, 32767), Materials.Water.getFluid(100L), new ItemStack(Items.paper, 1, 0), GT_Values.NI, GT_Values.NI, null, 100, 8);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L), GT_ModHandler.getDistilledWater(125L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HydratedCoal, 1L), GT_Values.NI, GT_Values.NI, null, 12, 4);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L), GT_ModHandler.getDistilledWater(100L), new ItemStack(Items.paper, 1, 0), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Paper, 1L), GT_ModHandler.getDistilledWater(100L), new ItemStack(Items.paper, 1, 0), GT_Values.NI, GT_Values.NI, null, 100, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Items.reeds, 1, 32767), GT_ModHandler.getDistilledWater(100L), new ItemStack(Items.paper, 1, 0), GT_Values.NI, GT_Values.NI, null, 100, 8);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 1), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 2), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 3), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 4), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 5), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 6), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 7), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 8), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 9), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 10), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 11), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 12), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 13), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 14), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 15), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 1), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 2), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 3), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 4), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 5), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 6), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 7), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 8), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 9), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 10), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 11), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 12), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 13), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 14), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 15), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.stained_hardened_clay, 1, 32767), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.hardened_clay, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.stained_glass, 1, 32767), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.glass, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.stained_glass_pane, 1, 32767), Materials.Chlorine.getGas(20L), new ItemStack(Blocks.glass_pane, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 8), Materials.Water.getFluid(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 0), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 9), Materials.Water.getFluid(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 1), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 10), Materials.Water.getFluid(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 2), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 11), Materials.Water.getFluid(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 3), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 12), Materials.Water.getFluid(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 4), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 13), Materials.Water.getFluid(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 5), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 14), Materials.Water.getFluid(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 6), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 15), Materials.Water.getFluid(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 7), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 8), GT_ModHandler.getDistilledWater(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 0), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 9), GT_ModHandler.getDistilledWater(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 1), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 10), GT_ModHandler.getDistilledWater(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 2), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 11), GT_ModHandler.getDistilledWater(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 3), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 12), GT_ModHandler.getDistilledWater(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 4), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 13), GT_ModHandler.getDistilledWater(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 5), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 14), GT_ModHandler.getDistilledWater(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 6), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 15), GT_ModHandler.getDistilledWater(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 7), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plastic, 1L), Materials.Concrete.getMolten(144L), ItemList.Block_Plascrete.get(1L), 200, 48);

        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Bronze, 1L), Materials.Concrete.getMolten(144L), ItemList.Block_BronzePlate.get(1L), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1L), Materials.Steel.getMolten(288L), ItemList.Block_SteelPlate.get(1L), GT_Values.NI, GT_Values.NI, null, 250, 16);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Titanium, 1L), Materials.Titanium.getMolten(144L), ItemList.Block_TitaniumPlate.get(1L), GT_Values.NI, GT_Values.NI, null, 300, 30);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1L), Materials.TungstenSteel.getMolten(144L), ItemList.Block_TungstenSteelReinforced.get(1L), GT_Values.NI, GT_Values.NI, null, 350, 64);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1L), Materials.Iridium.getMolten(144L), ItemList.Block_IridiumTungstensteel.get(1L), GT_Values.NI, GT_Values.NI, null, 400, 120);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Naquadah, 1L), Materials.Osmium.getMolten(144L), ItemList.Block_NaquadahPlate.get(1L), GT_Values.NI, GT_Values.NI, null, 450, 256);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1L), Materials.Naquadria.getMolten(144L), ItemList.Block_NeutroniumPlate.get(1L), GT_Values.NI, GT_Values.NI, null, 500, 480);

        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1L), Materials.Concrete.getMolten(144L), ItemList.Block_TungstenSteelReinforced.get(1L), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedGold, 1L), GT_Values.NI, Materials.Mercury.getFluid(200L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1L), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 2L, 14), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{10000, 10000, 9000}, 400, 120);

        for (int j = 0; j < Dyes.dyeRed.getSizeOfFluidList(); j++) {
            GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1L), Dyes.dyeRed.getFluidDye(j, 72L), GT_ModHandler.getModItem("BuildCraft|Transport", "pipeWire", 4L, 0), GT_Values.NI, GT_Values.NI, null, 32, 16);
        }
        for (int j = 0; j < Dyes.dyeBlue.getSizeOfFluidList(); j++) {
            GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1L), Dyes.dyeBlue.getFluidDye(j, 72L), GT_ModHandler.getModItem("BuildCraft|Transport", "pipeWire", 4L, 1), GT_Values.NI, GT_Values.NI, null, 32, 16);
        }
        for (int j = 0; j < Dyes.dyeGreen.getSizeOfFluidList(); j++) {
            GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1L), Dyes.dyeGreen.getFluidDye(j, 72L), GT_ModHandler.getModItem("BuildCraft|Transport", "pipeWire", 4L, 2), GT_Values.NI, GT_Values.NI, null, 32, 16);
        }
        for (int j = 0; j < Dyes.dyeYellow.getSizeOfFluidList(); j++) {
            GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1L), Dyes.dyeYellow.getFluidDye(j, 72L), GT_ModHandler.getModItem("BuildCraft|Transport", "pipeWire", 4L, 3), GT_Values.NI, GT_Values.NI, null, 32, 16);
        }
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            for (int j = 0; j < Dyes.VALUES[i].getSizeOfFluidList(); j++) {
                if (i != 15) {
                    GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 0), Dyes.VALUES[i].getFluidDye(j, 72L), new ItemStack(Blocks.wool, 1, 15 - i), GT_Values.NI, GT_Values.NI, null, 64, 2);
                }
                GT_Values.RA.addAssemblerRecipe(new ItemStack(Items.string, 3), GT_Utility.getIntegratedCircuit(3), Dyes.VALUES[i].getFluidDye(j, 24L), new ItemStack(Blocks.carpet, 2, 15 - i), 128, 5);
                GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.glass, 1, 0), Dyes.VALUES[i].getFluidDye(j, 18L), new ItemStack(Blocks.stained_glass, 1, 15 - i), GT_Values.NI, GT_Values.NI, null, 64, 2);
                GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.hardened_clay, 1, 0), Dyes.VALUES[i].getFluidDye(j, 18L), new ItemStack(Blocks.stained_hardened_clay, 1, 15 - i), GT_Values.NI, GT_Values.NI, null, 64, 2);
            }
        }
        GT_Values.RA.addFluidExtractionRecipe(ItemList.Dye_SquidInk.get(1L), GT_Values.NI, FluidRegistry.getFluidStack("squidink", 144), 10000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(ItemList.Dye_Indigo.get(1L), GT_Values.NI, FluidRegistry.getFluidStack("indigo", 144), 10000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(ItemList.Crop_Drop_Indigo.get(1L), GT_Values.NI, FluidRegistry.getFluidStack("indigo", 144), 10000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(ItemList.Crop_Drop_MilkWart.get(1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Milk, 1L), GT_ModHandler.getMilk(150L), 1000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(ItemList.Crop_Drop_OilBerry.get(1L), GT_Values.NI, Materials.Oil.getFluid(100L), 10000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(ItemList.Crop_Drop_UUMBerry.get(1L), GT_Values.NI, Materials.UUMatter.getFluid(4L), 10000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(ItemList.Crop_Drop_UUABerry.get(1L), GT_Values.NI, Materials.UUAmplifier.getFluid(4L), 10000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(new ItemStack(Items.fish, 1, 0), GT_Values.NI, Materials.FishOil.getFluid(40L), 10000, 16, 4);
        GT_Values.RA.addFluidExtractionRecipe(new ItemStack(Items.fish, 1, 1), GT_Values.NI, Materials.FishOil.getFluid(60L), 10000, 16, 4);
        GT_Values.RA.addFluidExtractionRecipe(new ItemStack(Items.fish, 1, 2), GT_Values.NI, Materials.FishOil.getFluid(70L), 10000, 16, 4);
        GT_Values.RA.addFluidExtractionRecipe(new ItemStack(Items.fish, 1, 3), GT_Values.NI, Materials.FishOil.getFluid(30L), 10000, 16, 4);

        GT_Values.RA.addFluidExtractionRecipe(new ItemStack(Items.coal, 1, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 1L), Materials.WoodTar.getFluid(100L), 1000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L), ItemList.IC2_Plantball.get(1L), Materials.Creosote.getFluid(5L), 100, 16, 4);
        GT_Values.RA.addFluidExtractionRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HydratedCoal, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L), Materials.Water.getFluid(100L), 10000, 32, 4);
        GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 1, 3), GT_Values.NI, Materials.Mercury.getFluid(1000L), 10000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Mercury, 1L), GT_Values.NI, Materials.Mercury.getFluid(1000L), 10000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Monazite, 1L), GT_Values.NI, Materials.Helium.getGas(200L), 10000, 64, 64);

        GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("IC2","blockAlloyGlass", 1L, 0), GT_Values.NI, Materials.ReinforceGlass.getFluid(144), 10000, 100, 1920);
        GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem(MOD_ID_DC,"item.ReinforcedGlassPLate", 2L, 0), GT_Values.NI, Materials.ReinforceGlass.getFluid(144), 10000, 75, 1920);
        GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem(MOD_ID_DC,"item.ReinforcedGlassLense", 2L, 0), GT_Values.NI, Materials.ReinforceGlass.getFluid(144), 10000, 50, 1920);

        GT_Values.RA.addFluidSmelterRecipe(new ItemStack(Items.snowball, 1, 0), GT_Values.NI, Materials.Water.getFluid(250L), 10000, 32, 4);
        GT_Values.RA.addFluidSmelterRecipe(new ItemStack(Blocks.snow, 1, 0), GT_Values.NI, Materials.Water.getFluid(1000L), 10000, 128, 4);
        GT_Values.RA.addFluidSmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1L), GT_Values.NI, Materials.Ice.getSolid(1000L), 10000, 128, 4);
        GT_Values.RA.addFluidSmelterRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "phosphor", 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 1L), Materials.Lava.getFluid(800L), 1000, 256, 128);

        GT_Values.RA.addAutoclaveRecipe(ItemList.IC2_Energium_Dust.get(9L), Materials.EnergeticAlloy.getMolten(288), ItemList.IC2_EnergyCrystal.get(1L), 10000, 600, 256);
        GT_Values.RA.addAutoclaveRecipe(ItemList.IC2_Energium_Dust.get(9L), Materials.ConductiveIron.getMolten(576), ItemList.IC2_EnergyCrystal.get(1L), 10000, 1200, 256);
        GT_Values.RA.addAutoclaveRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 1L, 0), Materials.Water.getFluid(200L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 10), 8000, 2000, 24);
        GT_Values.RA.addAutoclaveRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 1L, 600), Materials.Water.getFluid(200L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 11), 8000, 2000, 24);
        GT_Values.RA.addAutoclaveRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 1L, 1200), Materials.Water.getFluid(200L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 12), 8000, 2000, 24);
        GT_Values.RA.addAutoclaveRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 1L, 0), GT_ModHandler.getDistilledWater(100L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 10), 9000, 1000, 24);
        GT_Values.RA.addAutoclaveRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 1L, 600), GT_ModHandler.getDistilledWater(100L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 11), 9000, 1000, 24);
        GT_Values.RA.addAutoclaveRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 1L, 1200), GT_ModHandler.getDistilledWater(100L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 12), 9000, 1000, 24);
        GT_Values.RA.addAutoclaveRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 1L, 0), Materials.Void.getMolten(36L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 10), 10000, 500, 24);
        GT_Values.RA.addAutoclaveRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 1L, 600), Materials.Void.getMolten(36L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 11), 10000, 500, 24);
        GT_Values.RA.addAutoclaveRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 1L, 1200), Materials.Void.getMolten(36L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 12), 10000, 500, 24);
        GT_Values.RA.addAutoclaveRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 32), Materials.Polybenzimidazole.getMolten(36L), GT_ModHandler.getIC2Item("carbonFiber", 64L), 10000, 150, 1920);
        GT_Values.RA.addAutoclaveRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64), Materials.Epoxid.getMolten(144L), GT_ModHandler.getIC2Item("carbonFiber", 64L), 10000, 300, 480);
        //GT_Values.RA.addAutoclaveRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64), Materials.Polytetrafluoroethylene.getMolten(288L), GT_ModHandler.getIC2Item("carbonFiber", 32L), 10000, 400, 120);
        GT_Values.RA.addAutoclaveRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64), Materials.Plastic.getMolten(576L), GT_ModHandler.getIC2Item("carbonFiber", 16L), 10000, 600, 30);
        GT_Values.RA.addAutoclaveRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherStar, 1), Materials.UUMatter.getFluid(576L), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 1), 3333, 72000, 480);

        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.PolyvinylChloride,1),ItemList.Paper_Printed_Pages.get(1L),Materials.Glue.getFluid(20L), new ItemStack(Items.written_book,1,0), 32, 8);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.PolyvinylChloride,1), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 3), Materials.Glue.getFluid(20L), new ItemStack(Items.book,1,0),20,16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 8), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicon, 1), Materials.Glue.getFluid(250L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphene, 1), 480, 240);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Electric_Pump_LV.get(1L), GT_OreDictUnificator.get(circuitLogic.get(LOGIC_LV), 1L),  GT_Utility.getIntegratedCircuit(1)}, GT_Values.NF, ItemList.FluidRegulator_LV.get(1L), 400, 30);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Electric_Pump_MV.get(1L),  GT_OreDictUnificator.get(circuitLogic.get(LOGIC_MV), 1L), GT_Utility.getIntegratedCircuit(1)}, GT_Values.NF, ItemList.FluidRegulator_MV.get(1L), 350, 120);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Electric_Pump_HV.get(1L),  GT_OreDictUnificator.get(circuitLogic.get(LOGIC_HV), 1L), GT_Utility.getIntegratedCircuit(1)}, GT_Values.NF, ItemList.FluidRegulator_HV.get(1L), 300, 480);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Electric_Pump_EV.get(1L),  GT_OreDictUnificator.get(circuitLogic.get(LOGIC_EV), 1L), GT_Utility.getIntegratedCircuit(1)}, GT_Values.NF, ItemList.FluidRegulator_EV.get(1L), 250, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Electric_Pump_IV.get(1L),  GT_OreDictUnificator.get(circuitLogic.get(LOGIC_IV), 1L), GT_Utility.getIntegratedCircuit(1)}, GT_Values.NF, ItemList.FluidRegulator_IV.get(1L), 200, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Electric_Pump_LuV.get(1L),  GT_OreDictUnificator.get(circuitLogic.get(LOGIC_LUV), 1L), GT_Utility.getIntegratedCircuit(1)}, GT_Values.NF, ItemList.FluidRegulator_LuV.get(1L), 150, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Electric_Pump_ZPM.get(1L),  GT_OreDictUnificator.get(circuitLogic.get(LOGIC_ZPM), 1L), GT_Utility.getIntegratedCircuit(1)}, GT_Values.NF, ItemList.FluidRegulator_ZPM.get(1L), 100, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Electric_Pump_UV.get(1L),  GT_OreDictUnificator.get(circuitLogic.get(LOGIC_UV), 1L), GT_Utility.getIntegratedCircuit(1)}, GT_Values.NF, ItemList.FluidRegulator_UV.get(1L), 50, 500000);

//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Electric_Pump_LV.get(1L), ItemList.Electric_Motor_LV.get(1L), GT_OreDictUnificator.get(OrePrefixes.gear.get(Materials.Steel), 2L), GT_Utility.getIntegratedCircuit(2)}, GT_Values.NF, ItemList.Steam_Valve_LV.get(1L), 400, 30);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Electric_Pump_MV.get(1L), ItemList.Electric_Motor_MV.get(1L), GT_OreDictUnificator.get(OrePrefixes.gear.get(Materials.Aluminium), 2L), GT_Utility.getIntegratedCircuit(2)}, GT_Values.NF, ItemList.Steam_Valve_MV.get(1L), 350, 120);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Electric_Pump_HV.get(1L), ItemList.Electric_Motor_HV.get(1L), GT_OreDictUnificator.get(OrePrefixes.gear.get(Materials.StainlessSteel), 2L), GT_Utility.getIntegratedCircuit(2)}, GT_Values.NF, ItemList.Steam_Valve_HV.get(1L), 300, 480);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Electric_Pump_EV.get(1L), ItemList.Electric_Motor_EV.get(1L), GT_OreDictUnificator.get(OrePrefixes.gear.get(Materials.Titanium), 2L), GT_Utility.getIntegratedCircuit(2)}, GT_Values.NF, ItemList.Steam_Valve_EV.get(1L), 250, 1920);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Electric_Pump_IV.get(1L), ItemList.Electric_Motor_IV.get(1L), GT_OreDictUnificator.get(OrePrefixes.gear.get(Materials.TungstenSteel), 2L), GT_Utility.getIntegratedCircuit(2)}, GT_Values.NF, ItemList.Steam_Valve_IV.get(1L), 200, 7680);

//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 4L), GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Basic), 4), GT_Utility.getIntegratedCircuit(3)}, GT_Values.NF, ItemList.Schematic.get(1L), 600, 16);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 3L), GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Good), 2), GT_Utility.getIntegratedCircuit(3)}, GT_Values.NF, ItemList.Schematic.get(1L), 300, 30);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 2L), GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Advanced), 1), GT_Utility.getIntegratedCircuit(3)}, GT_Values.NF, ItemList.Schematic.get(1L), 150, 48);

//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_HV.get(1L), GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 1L), ItemList.Circuit_Chip_LPIC.get(2L), ItemList.HV_Coil.get(2L), ItemList.Reactor_Coolant_He_1.get(1L), ItemList.Electric_Pump_HV.get(1L)}, GT_Values.NF, ItemList.Hatch_Energy_HV.get(1L), 200, 480);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_EV.get(1L), GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 1L), ItemList.Circuit_Chip_PIC.get(2L), ItemList.EV_Coil.get(2L), ItemList.Reactor_Coolant_He_1.get(1L), ItemList.Electric_Pump_EV.get(1L)}, GT_Values.NF, ItemList.Hatch_Energy_EV.get(1L), 200, 1920);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 1L), ItemList.Circuit_Chip_HPIC.get(2L), ItemList.IV_Coil.get(2L), ItemList.Reactor_Coolant_He_3.get(1L), ItemList.Electric_Pump_IV.get(1L)}, GT_Values.NF, ItemList.Hatch_Energy_IV.get(1L), 200, 7680);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_HV.get(1L), GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 1L), ItemList.Circuit_Chip_LPIC.get(2L), ItemList.HV_Coil.get(2L), ItemList.Reactor_Coolant_NaK_1.get(1L), ItemList.Electric_Pump_HV.get(1L)}, GT_Values.NF, ItemList.Hatch_Energy_HV.get(1L), 200, 480);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_EV.get(1L), GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 1L), ItemList.Circuit_Chip_PIC.get(2L), ItemList.EV_Coil.get(2L), ItemList.Reactor_Coolant_NaK_1.get(1L), ItemList.Electric_Pump_EV.get(1L)}, GT_Values.NF, ItemList.Hatch_Energy_EV.get(1L), 200, 1920);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 1L), ItemList.Circuit_Chip_HPIC.get(2L), ItemList.IV_Coil.get(2L), ItemList.Reactor_Coolant_NaK_3.get(1L), ItemList.Electric_Pump_IV.get(1L)}, GT_Values.NF, ItemList.Hatch_Energy_IV.get(1L), 200, 7680);

//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_HV.get(1L), GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Gold, 1L), ItemList.Circuit_Chip_LPIC.get(2L), ItemList.HV_Coil.get(2L), ItemList.Reactor_Coolant_He_1.get(1L), ItemList.Electric_Pump_HV.get(1L)}, GT_Values.NF, ItemList.Hatch_Dynamo_HV.get(1L), 200, 480);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_EV.get(1L), GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Aluminium, 1L), ItemList.Circuit_Chip_PIC.get(2L), ItemList.EV_Coil.get(2L), ItemList.Reactor_Coolant_He_1.get(1L), ItemList.Electric_Pump_EV.get(1L)}, GT_Values.NF, ItemList.Hatch_Dynamo_EV.get(1L), 200, 1920);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1L), GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Vanadiumtriindinid, 1L), ItemList.Circuit_Chip_HPIC.get(2L), ItemList.IV_Coil.get(2L), ItemList.Reactor_Coolant_He_3.get(1L), ItemList.Electric_Pump_IV.get(1L)}, GT_Values.NF, ItemList.Hatch_Dynamo_IV.get(1L), 200, 7680);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_HV.get(1L), GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Gold, 1L), ItemList.Circuit_Chip_LPIC.get(2L), ItemList.HV_Coil.get(2L), ItemList.Reactor_Coolant_NaK_1.get(1L), ItemList.Electric_Pump_HV.get(1L)}, GT_Values.NF, ItemList.Hatch_Dynamo_HV.get(1L), 200, 480);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_EV.get(1L), GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Aluminium, 1L), ItemList.Circuit_Chip_PIC.get(2L), ItemList.EV_Coil.get(2L), ItemList.Reactor_Coolant_NaK_1.get(1L), ItemList.Electric_Pump_EV.get(1L)}, GT_Values.NF, ItemList.Hatch_Dynamo_EV.get(1L), 200, 1920);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1L), GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Vanadiumtriindinid, 1L), ItemList.Circuit_Chip_HPIC.get(2L), ItemList.IV_Coil.get(2L), ItemList.Reactor_Coolant_NaK_3.get(1L), ItemList.Electric_Pump_IV.get(1L)}, GT_Values.NF, ItemList.Hatch_Dynamo_IV.get(1L), 200, 7680);

//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Steel, 2L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 6L), GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Steel, 2L), GT_Utility.getIntegratedCircuit(2)}, Materials.Tin.getMolten(144L), ItemList.Long_Distance_Pipeline_Fluid.get(2L), 300, 16);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Tin, 2L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 6L), GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Steel, 2L), GT_Utility.getIntegratedCircuit(2)}, Materials.Tin.getMolten(144L), ItemList.Long_Distance_Pipeline_Item.get(2L), 300, 16);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Steel, 2L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 9L), GT_Utility.getIntegratedCircuit(24)}, Materials.Tin.getMolten(144L), ItemList.Long_Distance_Pipeline_Fluid_Pipe.get(64L), 600, 24);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Tin, 2L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 9L), GT_Utility.getIntegratedCircuit(24)}, Materials.Tin.getMolten(144L), ItemList.Long_Distance_Pipeline_Item_Pipe.get(64L), 600, 24);

	GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 4L), GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1L), ItemList.Robot_Arm_IV.get(2L), GT_Utility.getIntegratedCircuit(3)}, GT_Values.NF, ItemList.Casing_Gearbox_TungstenSteel.get(1L), 200, 30);
	    
        {//limiting life time of the variables
            ItemStack flask = ItemList.VOLUMETRIC_FLASK.get(1);
            NBTTagCompound nbtFlask = new NBTTagCompound();
            nbtFlask.setInteger("Capacity", 144);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(ItemList.VOLUMETRIC_FLASK.get(1), GT_Utility.getIntegratedCircuit(1), flask, 10, 30);
            nbtFlask.setInteger("Capacity", 288);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(ItemList.VOLUMETRIC_FLASK.get(1), GT_Utility.getIntegratedCircuit(2), flask, 10, 30);
            nbtFlask.setInteger("Capacity", 576);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(ItemList.VOLUMETRIC_FLASK.get(1), GT_Utility.getIntegratedCircuit(3), flask, 10, 30);
            nbtFlask.setInteger("Capacity", 720);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(ItemList.VOLUMETRIC_FLASK.get(1), GT_Utility.getIntegratedCircuit(4), flask, 10, 30);
            nbtFlask.setInteger("Capacity", 864);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(ItemList.VOLUMETRIC_FLASK.get(1), GT_Utility.getIntegratedCircuit(5), flask, 10, 30);      
            nbtFlask.setInteger("Capacity", 72);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(ItemList.VOLUMETRIC_FLASK.get(1), GT_Utility.getIntegratedCircuit(6), flask, 10, 30);
            nbtFlask.setInteger("Capacity", 648);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(ItemList.VOLUMETRIC_FLASK.get(1), GT_Utility.getIntegratedCircuit(7), flask, 10, 30);
            nbtFlask.setInteger("Capacity", 936);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(ItemList.VOLUMETRIC_FLASK.get(1), GT_Utility.getIntegratedCircuit(8), flask, 10, 30);
            nbtFlask.setInteger("Capacity", 250);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(ItemList.VOLUMETRIC_FLASK.get(1), GT_Utility.getIntegratedCircuit(10), flask, 10, 30);
            nbtFlask.setInteger("Capacity", 500);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(ItemList.VOLUMETRIC_FLASK.get(1), GT_Utility.getIntegratedCircuit(11), flask, 10, 30);
            // make the 1000L recipe actualy in
            ItemStack flask500 = flask.copy();
            nbtFlask.setInteger("Capacity", 1000);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(flask500, GT_Utility.getIntegratedCircuit(24), flask, 10, 30);
        }

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_HV.get(1L), GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.StainlessSteel, 1L), GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.StainlessSteel, 1L), ItemList.Electric_Motor_HV.get(1L), GT_Utility.getIntegratedCircuit(3)}, GT_Values.NF, ItemList.Hatch_Muffler_HV.get(1L), 200, 480);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_EV.get(1L), GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Titanium, 1L), ItemList.Electric_Motor_EV.get(1L), GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Titanium, 1L), GT_Utility.getIntegratedCircuit(3)}, GT_Values.NF, ItemList.Hatch_Muffler_EV.get(1L), 200, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1L), GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.TungstenSteel, 1L), ItemList.Electric_Motor_IV.get(1L), GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.TungstenSteel, 1L), GT_Utility.getIntegratedCircuit(3)}, GT_Values.NF, ItemList.Hatch_Muffler_IV.get(1L), 200, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1L), GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Enderium, 1L), ItemList.Electric_Motor_LuV.get(1L), GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Enderium, 1L), GT_Utility.getIntegratedCircuit(3)}, GT_Values.NF, ItemList.Hatch_Muffler_LuV.get(1L), 200, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1L), GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Naquadah, 1L), ItemList.Electric_Motor_ZPM.get(1L), GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.NaquadahAlloy, 1L), GT_Utility.getIntegratedCircuit(3)}, GT_Values.NF, ItemList.Hatch_Muffler_ZPM.get(1L), 200, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1L), GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 1L), ItemList.Electric_Motor_UV.get(1L), GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Neutronium, 1L), GT_Utility.getIntegratedCircuit(3)}, GT_Values.NF, ItemList.Hatch_Muffler_UV.get(1L), 200, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_MAX.get(1L), GT_OreDictUnificator.get(OrePrefixes.pipeLarge, DraconiumAwakened, 1L), ItemList.Electric_Motor_UHV.get(1L), GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.CosmicNeutronium, 1L), GT_Utility.getIntegratedCircuit(3)}, GT_Values.NF, ItemList.Hatch_Muffler_MAX.get(1L), 200, 2000000);

        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Galena, 3), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Sphalerite, 1), Materials.SulfuricAcid.getFluid(4000), new FluidStack(ItemList.sIndiumConcentrate, 8000), null, 60, 150);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 4), GT_Utility.getIntegratedCircuit(1), new FluidStack(ItemList.sIndiumConcentrate, 8000), new FluidStack(ItemList.sLeadZincSolution, 8000), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Indium, 1), 50, 600);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 36), GT_Utility.getIntegratedCircuit(9), new FluidStack(ItemList.sIndiumConcentrate, 72000), new FluidStack(ItemList.sLeadZincSolution, 72000), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Indium, 1), 450, 600);
        GT_Values.RA.addElectrolyzerRecipe(GT_Values.NI, GT_Values.NI, new FluidStack(ItemList.sLeadZincSolution, 8000), Materials.Water.getFluid(2000), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 3), null, null, null, 300, 192);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Pentlandite, 1), GT_Utility.getIntegratedCircuit(1), new FluidStack(ItemList.sNitricAcid, 1000), new FluidStack(ItemList.sNickelSulfate, 2000), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.PlatinumGroupSludge, 1), 50, 30);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Chalcopyrite, 1), GT_Utility.getIntegratedCircuit(1), new FluidStack(ItemList.sNitricAcid, 1000), new FluidStack(ItemList.sBlueVitriol, 2000), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.PlatinumGroupSludge, 1), 50, 30);
        GT_Values.RA.addElectrolyzerRecipe(ItemList.Cell_Empty.get(1), null, new FluidStack(ItemList.sBlueVitriol, 2000), Materials.SulfuricAcid.getFluid(1000), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 900, 30);
        GT_Values.RA.addElectrolyzerRecipe(ItemList.Cell_Empty.get(1), null, new FluidStack(ItemList.sNickelSulfate, 2000), Materials.SulfuricAcid.getFluid(1000), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 900, 30);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.PlatinumGroupSludge, 1), null, null, null, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Platinum, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Palladium, 3), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Iridium, 3), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Osmium, 3), new int[]{10000, 10000, 10000, 9500, 9000, 8500}, 900, 30);

        GT_Values.RA.addElectrolyzerRecipe(ItemList.Cell_Empty.get(11), GT_Utility.getIntegratedCircuit(1), Materials.Glycerol.getFluid(14000), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 3), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 8), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 3), GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 300, 90);

        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1), ItemList.Cell_Empty.get(3),  Materials.HydrochloricAcid.getFluid(3000), Materials.IronIIIChloride.getFluid(1000),GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 3), 400, 30);
        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Salt, 1), null, Materials.SulfuricAcid.getFluid(1000), new FluidStack(ItemList.sHydrochloricAcid, 1000), null, 200, 30);

        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedGold, 8L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 8L), new FluidStack(FluidRegistry.getFluid("ic2coolant"), 1000), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Thaumium, 16L), 400, 480);

        GT_Values.RA.addSlicerRecipe(ItemList.Food_Dough_Chocolate.get(1L), ItemList.Shape_Slicer_Flat.get(0L), ItemList.Food_Raw_Cookie.get(4L), 128, 4);
        GT_Values.RA.addSlicerRecipe(ItemList.Food_Baked_Bun.get(1L), ItemList.Shape_Slicer_Flat.get(0L), ItemList.Food_Sliced_Bun.get(2L), 128, 4);
        GT_Values.RA.addSlicerRecipe(ItemList.Food_Baked_Bread.get(1L), ItemList.Shape_Slicer_Flat.get(0L), ItemList.Food_Sliced_Bread.get(2L), 128, 4);
        GT_Values.RA.addSlicerRecipe(ItemList.Food_Baked_Baguette.get(1L), ItemList.Shape_Slicer_Flat.get(0L), ItemList.Food_Sliced_Baguette.get(2L), 128, 4);

        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Plate.get(0L), Materials.ReinforceGlass.getMolten(72), GT_ModHandler.getModItem(MOD_ID_DC, "item.ReinforcedGlassPLate", 1L, 0),160, 1920);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0L), Materials.ReinforceGlass.getMolten(144), GT_ModHandler.getModItem("IC2", "blockAlloyGlass", 1L), 160, 1920);

//        GT_Values.RA.addChemicalRecipe(GT_ModHandler.getModItem("GalaxySpace","item.UnknowCrystal",4L), Materials.Osmiridium.getDust(2),  Materials.GrowthMediumSterilized.getFluid(1000L), FluidRegistry.getFluidStack("bacterialsludge", 1000), ItemList.Circuit_Chip_Stemcell.get(64L), GT_Values.NI,600, 30720);
//        GT_Values.RA.addChemicalRecipe(ItemList.Circuit_Chip_Stemcell.get(32L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CosmicNeutronium, 4), Materials.BioMediumSterilized.getFluid(2000L), FluidRegistry.getFluidStack("mutagen", 2000), ItemList.Circuit_Chip_Biocell.get(32L), GT_Values.NI,1200, 500000);
        GT_Values.RA.addFluidHeaterRecipe(GT_Utility.getIntegratedCircuit(1), Materials.GrowthMediumRaw.getFluid(1000L), Materials.GrowthMediumSterilized.getFluid(1000L), 200, 7680);
        GT_Values.RA.addFluidHeaterRecipe(GT_Utility.getIntegratedCircuit(1), Materials.BioMediumRaw.getFluid(1000L), Materials.BioMediumSterilized.getFluid(1000L), 200, 30720);

        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 1), 100, 120);
//        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 1), 100, 120);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 1L), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 2), 200, 120);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1L), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 3), 100, 480);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1L), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 2L, 4), 200, 120);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NetherQuartz, 1L), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 5), 300, 120);
        GT_Values.RA.addFormingPressRecipe(new ItemStack(Items.comparator, 1, 32767), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 6), 300, 120);
        GT_Values.RA.addFormingPressRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 10), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 0L, 13), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 16), 200, 16);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CertusQuartz, 1L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 0L, 13), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 16), 200, 16);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 0L, 14), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 17), 200, 16);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 1L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 0L, 15), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 18), 200, 16);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 1L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 0L, 19), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 20), 200, 16);

        this.run2();

        GT_Values.RA.addFormingPressRecipe(ItemList.Food_Dough_Sugar.get(4L), ItemList.Shape_Mold_Cylinder.get(0L), ItemList.Food_Raw_Cake.get(1L), 384, 4);
        GT_Values.RA.addFormingPressRecipe(new ItemStack(Blocks.glass, 1, 32767), ItemList.Shape_Mold_Arrow.get(0L), ItemList.Arrow_Head_Glass_Emtpy.get(1L), 64, 4);
//        for (Materials tMat : Materials.values()) {
//            if (tMat.isProperSolderingFluid()) {
//                int tMultiplier = tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1 : tMat.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : 2;
//                GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Coated_Basic.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Primitive, 2), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Iron, 2), GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Iron, 4), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Iron, 1),GT_Utility.getIntegratedCircuit(1)}, tMat.getMolten(1152L * tMultiplier / 2L), GT_ModHandler.getModItem("Forestry", "chipsets", 1L, 0), 200, 30);
//                GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Coated_Basic.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Basic, 2), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Bronze, 2), GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Bronze, 4), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Bronze, 1),GT_Utility.getIntegratedCircuit(1)}, tMat.getMolten(1152L * tMultiplier / 2L), GT_ModHandler.getModItem("Forestry", "chipsets", 1L, 1), 200, 30);
//                GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Phenolic_Good.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 2), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Steel, 2), GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 4), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Steel, 1),GT_Utility.getIntegratedCircuit(1)}, tMat.getMolten(1152L * tMultiplier / 2L), GT_ModHandler.getModItem("Forestry", "chipsets", 1L, 2), 200, 30);
//                GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Phenolic_Good.get(1L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 2), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, 2), GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Electrum, 4), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 1),GT_Utility.getIntegratedCircuit(1)}, tMat.getMolten(1152L * tMultiplier / 2L), GT_ModHandler.getModItem("Forestry", "chipsets", 1L, 3), 200, 30);
//                Circuit soldering
//                Integraded Circuits

//                for (ItemStack tPlate : new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 1L)}) {
//                    GT_Values.RA.addAssemblerRecipe(new ItemStack[]{new ItemStack(Blocks.lever, 1, 32767), tPlate, GT_Utility.getIntegratedCircuit(1)}, tMat.getMolten(144L * tMultiplier / 2L), ItemList.Cover_Controller.get(1L), 800, 16);
//                    GT_Values.RA.addAssemblerRecipe(new ItemStack[]{new ItemStack(Blocks.redstone_torch, 1, 32767), tPlate, GT_Utility.getIntegratedCircuit(1)}, tMat.getMolten(144L * tMultiplier / 2L), ItemList.Cover_ActivityDetector.get(1L), 800, 16);
//                    GT_Values.RA.addAssemblerRecipe(new ItemStack[]{new ItemStack(Blocks.heavy_weighted_pressure_plate, 1, 32767), tPlate, GT_Utility.getIntegratedCircuit(1)}, tMat.getMolten(144L * tMultiplier / 2L), ItemList.Cover_FluidDetector.get(1L), 800, 16);
//                    GT_Values.RA.addAssemblerRecipe(new ItemStack[]{new ItemStack(Blocks.light_weighted_pressure_plate, 1, 32767), tPlate, GT_Utility.getIntegratedCircuit(1)}, tMat.getMolten(144L * tMultiplier / 2L), ItemList.Cover_ItemDetector.get(1L), 800, 16);
//                    GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_ModHandler.getIC2Item("ecMeter", 1L), tPlate, GT_Utility.getIntegratedCircuit(1)}, tMat.getMolten(144L * tMultiplier / 2L), ItemList.Cover_EnergyDetector.get(1L), 800, 16);
//                }
//            }
//        }
//        GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 32), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GalliumArsenide, 1), null, null, ItemList.Circuit_Silicon_Ingot.get(1), null, 9000, 120, 1784);
//        GT_Values.RA.addCutterRecipe(ItemList.Circuit_Silicon_Ingot.get(1), ItemList.Circuit_Silicon_Wafer.get(16), null, 400, 30);
//        GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 64), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 8), Materials.Nitrogen.getGas(8000), null, ItemList.Circuit_Silicon_Ingot2.get(1), null, 12000, 480, 2484);
//        GT_Values.RA.addCutterRecipe(ItemList.Circuit_Silicon_Ingot2.get(1), GT_Values.NI, ItemList.Circuit_Silicon_Wafer2.get(32), null, 800, 120, true);
//        GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Silicon, 16), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadah, 1), Materials.Argon.getGas(8000), null, ItemList.Circuit_Silicon_Ingot3.get(1), null, 15000, 1920, 4484);
//        GT_Values.RA.addCutterRecipe(ItemList.Circuit_Silicon_Ingot3.get(1), GT_Values.NI, ItemList.Circuit_Silicon_Wafer3.get(64), null, 1600, 480, true);
//        GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Silicon, 32), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Europium, 2), Materials.Radon.getGas(8000), null, ItemList.Circuit_Silicon_Ingot4.get(1), null, 18000, 7680, 6484);
//        GT_Values.RA.addCutterRecipe(ItemList.Circuit_Silicon_Ingot4.get(1), GT_Values.NI, ItemList.Circuit_Silicon_Wafer4.get(64), ItemList.Circuit_Silicon_Wafer4.get(32), 2400, 1920, true);
//        GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Silicon, 64), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Americium, 4), Materials.Radon.getGas(16000), null, ItemList.Circuit_Silicon_Ingot5.get(1), null, 21000, 30720, 9000);
//        GT_Values.RA.addCutterRecipe(ItemList.Circuit_Silicon_Ingot5.get(1), GT_Values.NI, ItemList.Circuit_Silicon_Wafer5.get(64), ItemList.Circuit_Silicon_Wafer5.get(64), 3200, 7680, true);

        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.redstone_torch, 2, 32767), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), Materials.Concrete.getMolten(144L), new ItemStack(Items.repeater, 1, 0), 10, 10);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Items.leather, 1, 32767), new ItemStack(Items.lead, 1, 32767), Materials.Glue.getFluid(72L), new ItemStack(Items.name_tag, 1, 0), 100, 8);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem(MOD_ID_DC, "item.ArtificialLeather", 1L, 0), new ItemStack(Items.lead, 1, 32767), Materials.Glue.getFluid(72L), new ItemStack(Items.name_tag, 1, 0), 100, 8);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 8L), new ItemStack(Items.compass, 1, 32767), GT_Values.NF, new ItemStack(Items.map, 1, 0), 100, 8);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tantalum, 1L), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Manganese, 1L), Materials.Plastic.getMolten(144L), ItemList.Battery_RE_ULV_Tantalum.get(8L), 100, 4);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("TwilightForest", "item.charmOfLife1", 4L, 0), GT_Utility.getIntegratedCircuit(4), GT_Values.NF, GT_ModHandler.getModItem("TwilightForest", "item.charmOfLife2", 1L, 0), 100, 8);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("TwilightForest", "item.charmOfKeeping1", 4L, 0), GT_Utility.getIntegratedCircuit(4), GT_Values.NF, GT_ModHandler.getModItem("TwilightForest", "item.charmOfKeeping2", 1L, 0), 100, 8);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("TwilightForest", "item.charmOfKeeping2", 4L, 0), GT_Utility.getIntegratedCircuit(4), GT_Values.NF, GT_ModHandler.getModItem("TwilightForest", "item.charmOfKeeping3", 1L, 0), 100, 8);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("TwilightForest", "item.charmOfLife2", 1L, 0), GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_ModHandler.getModItem("TwilightForest", "item.charmOfLife1", 4L, 0), 100, 8);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("TwilightForest", "item.charmOfKeeping2", 1L, 0), GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_ModHandler.getModItem("TwilightForest", "item.charmOfKeeping1", 4L, 0), 100, 8);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("TwilightForest", "item.charmOfKeeping3", 1L, 0), GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_ModHandler.getModItem("TwilightForest", "item.charmOfKeeping2", 4L, 0), 100, 8);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 16), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 20), Materials.Redstone.getMolten(144L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 23), 64, 30);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 17), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 20), Materials.Redstone.getMolten(144L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 24), 64, 30);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 18), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 20), Materials.Redstone.getMolten(144L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 22), 64, 30);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CertusQuartz, 1L), new ItemStack(Blocks.sand, 1, 32767), GT_Values.NF, GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 2L, 0), 64, 8);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherQuartz, 1L), new ItemStack(Blocks.sand, 1, 32767), GT_Values.NF, GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 2L, 600), 64, 8);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Fluix, 1L), new ItemStack(Blocks.sand, 1, 32767), GT_Values.NF, GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 2L, 1200), 64, 8);
        GT_Values.RA.addAssemblerRecipe(ItemList.FR_Wax.get(6L), new ItemStack(Items.string, 1, 32767), Materials.Water.getFluid(600L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "candle", 24L, 0), 64, 8);
        GT_Values.RA.addAssemblerRecipe(ItemList.FR_Wax.get(2L), ItemList.FR_Silk.get(1L), Materials.Water.getFluid(200L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "candle", 8L, 0), 16, 8);
        GT_Values.RA.addAssemblerRecipe(ItemList.FR_Silk.get(9L), GT_Utility.getIntegratedCircuit(9), Materials.Water.getFluid(500L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "craftingMaterial", 1L, 3), 64, 8);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "propolis", 5L, 2), GT_Utility.getIntegratedCircuit(5), GT_Values.NF, GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "craftingMaterial", 1L, 1), 16, 8);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "sturdyMachine", 1L, 0), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Diamond, 4L), Materials.Water.getFluid(5000L), ItemList.FR_Casing_Hardened.get(1L), 64, 32);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Bronze, 8L), GT_Utility.getIntegratedCircuit(8), GT_Values.NF, ItemList.FR_Casing_Sturdy.get(1L), 32, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), new ItemStack(Blocks.wool, 1, 32767), Materials.Creosote.getFluid(1000L), new ItemStack(Blocks.torch, 6, 0), 400, 1);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "craftingMaterial", 5L, 1), GT_Utility.getIntegratedCircuit(5), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1L), 64, 8);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.piston, 1, 32767), new ItemStack(Items.slime_ball, 1, 32767), GT_Values.NF, new ItemStack(Blocks.sticky_piston, 1, 0), 10, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.piston, 1, 32767), ItemList.IC2_Resin.get(1L), GT_Values.NF, new ItemStack(Blocks.sticky_piston, 1, 0), 10, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.piston, 1, 32767), GT_Utility.getIntegratedCircuit(1), Materials.Glue.getFluid(100L), new ItemStack(Blocks.sticky_piston, 1, 0), 10, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Rubber, 3L), GT_ModHandler.getIC2Item("carbonMesh", 3L), GT_Utility.getIntegratedCircuit(1)}, Materials.Glue.getFluid(300L), ItemList.Duct_Tape.get(1L), 100, 120);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StyreneButadieneRubber, 2L), GT_ModHandler.getIC2Item("carbonMesh", 2L), GT_Utility.getIntegratedCircuit(2)}, Materials.Glue.getFluid(200L), ItemList.Duct_Tape.get(1L), 100, 480);
//        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicone, 1L), GT_ModHandler.getIC2Item("carbonMesh", 1L), GT_Utility.getIntegratedCircuit(3)}, Materials.Glue.getFluid(100L), ItemList.Duct_Tape.get(1L), 100, 1920);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 3L), new ItemStack(Items.leather, 1, 32767), Materials.Glue.getFluid(20L), new ItemStack(Items.book, 1, 0), 32, 8);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 3L), GT_ModHandler.getModItem(MOD_ID_DC, "item.ArtificialLeather", 1L, 0), Materials.Glue.getFluid(20L), new ItemStack(Items.book, 1, 0), 32, 8);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 3L), GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Paper, 1L), Materials.Glue.getFluid(20L), new ItemStack(Items.book, 1, 0), 32, 8);
        GT_Values.RA.addAssemblerRecipe(ItemList.Paper_Printed_Pages.get(1L), new ItemStack(Items.leather, 1, 32767), Materials.Glue.getFluid(20L), new ItemStack(Items.written_book, 1, 0), 32, 8);
        GT_Values.RA.addAssemblerRecipe(ItemList.Paper_Printed_Pages.get(1L), GT_ModHandler.getModItem(MOD_ID_DC, "item.ArtificialLeather", 1L, 0), Materials.Glue.getFluid(20L), new ItemStack(Items.written_book, 1, 0), 32, 8);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Tin, 4L)}, GT_Values.NF, ItemList.Cell_Universal_Fluid.get(1L), 128, 8);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Baked_Cake.get(1L), new ItemStack(Items.egg, 1, 0), Materials.Milk.getFluid(3000L), new ItemStack(Items.cake, 1, 0), 100, 8);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Bun.get(2L), GT_Utility.getIntegratedCircuit(2), GT_Values.NF, ItemList.Food_Sliced_Buns.get(1L), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Bread.get(2L), GT_Utility.getIntegratedCircuit(2), GT_Values.NF, ItemList.Food_Sliced_Breads.get(1L), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Baguette.get(2L), GT_Utility.getIntegratedCircuit(2), GT_Values.NF, ItemList.Food_Sliced_Baguettes.get(1L), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Buns.get(1L), GT_Utility.getIntegratedCircuit(1), GT_Values.NF, ItemList.Food_Sliced_Bun.get(2L), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Breads.get(1L), GT_Utility.getIntegratedCircuit(1), GT_Values.NF, ItemList.Food_Sliced_Bread.get(2L), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Baguettes.get(1L), GT_Utility.getIntegratedCircuit(1), GT_Values.NF, ItemList.Food_Sliced_Baguette.get(2L), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Bun.get(2L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.MeatCooked, 1L), GT_Values.NF, ItemList.Food_Burger_Meat.get(1L), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Buns.get(1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.MeatCooked, 1L), GT_Values.NF, ItemList.Food_Burger_Meat.get(1L), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Bun.get(2L), ItemList.Food_Chum.get(1L), GT_Values.NF, ItemList.Food_Burger_Chum.get(1L), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Buns.get(1L), ItemList.Food_Chum.get(1L), GT_Values.NF, ItemList.Food_Burger_Chum.get(1L), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Bun.get(2L), ItemList.Food_Sliced_Cheese.get(3L), GT_Values.NF, ItemList.Food_Burger_Cheese.get(1L), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Buns.get(1L), ItemList.Food_Sliced_Cheese.get(3L), GT_Values.NF, ItemList.Food_Burger_Cheese.get(1L), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Flat_Dough.get(1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.MeatCooked, 1L), GT_Values.NF, ItemList.Food_Raw_Pizza_Meat.get(1L), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Flat_Dough.get(1L), ItemList.Food_Sliced_Cheese.get(3L), GT_Values.NF, ItemList.Food_Raw_Pizza_Cheese.get(1L), 100, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ball.get(0L), new FluidStack(ItemList.sToluene, 100), ItemList.GelledToluene.get(1), 100, 16);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Nugget.get(0L), Materials.AnnealedCopper.getMolten(16), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Copper, 1L), 16, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ingot.get(0L), Materials.AnnealedCopper.getMolten(144), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Copper, 1L), 32, 8);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0L), Materials.AnnealedCopper.getMolten(1296), GT_OreDictUnificator.get(OrePrefixes.block, Materials.Copper, 1L), 288, 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.cell,Materials.SulfuricAcid, 1),  GT_Values.NI,  GT_Values.NI,  GT_Values.NI,  GT_Values.NI, GT_Utility.getIntegratedCircuit(1), new FluidStack(ItemList.sNitricAcid,1000), new FluidStack(ItemList.sNitrationMixture, 2000), ItemList.Cell_Empty.get(1), 480, 2);
        
        GT_Values.RA.addChemicalRecipe(new ItemStack(Items.sugar), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plastic, 1), new FluidStack(ItemList.sToluene, 133), GT_Values.NF, ItemList.GelledToluene.get(2), 140, 192);
        GT_Values.RA.addChemicalRecipe(new ItemStack(Items.sugar, 9), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plastic, 1), new FluidStack(ItemList.sToluene, 1197), GT_Values.NF, ItemList.GelledToluene.get(18), 1260, 192);
        GT_Values.RA.addChemicalRecipe(ItemList.GelledToluene.get(4), GT_Utility.getIntegratedCircuit(1), Materials.SulfuricAcid.getFluid(250), GT_Values.NF, new ItemStack(Blocks.tnt, 1), 200, 24);
        GT_Values.RA.addChemicalRecipe(ItemList.GelledToluene.get(4), GT_Utility.getIntegratedCircuit(1), new FluidStack(ItemList.sNitrationMixture,200), Materials.DilutedSulfuricAcid.getFluid(150), GT_ModHandler.getIC2Item("industrialTnt", 1L), 80, 480);
         GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Polycaprolactam, 1L), new ItemStack(Items.string, 32), 80, 48);

        GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.EnergeticAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.EnergeticAlloy, 4L), 200, 16);
        GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.EnergeticAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.EnergeticAlloy, 8L), 400, 30);
        GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 1L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Iridium, 4L), 200, 16);
        GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iridium, 1L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Iridium, 8L), 400, 30);
        GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmiridium, 1L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 4L), 200, 16);
        GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Osmiridium, 1L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 8L), 400, 30);
        GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Europium, 1L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 4L), 200, 16);
        GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Europium, 1L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 8L), 400, 30);
        GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Neutronium, 1L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 4L), 200, 16);
        GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Neutronium, 1L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 8L), 400, 30);
        GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Americium, 1L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 4L), 200, 16);
	    GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Americium, 1L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 8L), 400, 30);
        
	    GT_Values.RA.addDistilleryRecipe(GT_Utility.getIntegratedCircuit(4), Materials.Creosote.getFluid(100L), Materials.Lubricant.getFluid(32L), 240, 30, false);
        GT_Values.RA.addDistilleryRecipe(GT_Utility.getIntegratedCircuit(4), Materials.SeedOil.getFluid(32L), Materials.Lubricant.getFluid(8L), 80, 30, false);
        GT_Values.RA.addDistilleryRecipe(GT_Utility.getIntegratedCircuit(4), Materials.FishOil.getFluid(32L), Materials.Lubricant.getFluid(8L), 80, 30, false);
        GT_Values.RA.addDistilleryRecipe(GT_Utility.getIntegratedCircuit(4), Materials.Oil.getFluid(120L), Materials.Lubricant.getFluid(60L), 160, 30, false);
        GT_Values.RA.addDistilleryRecipe(GT_Utility.getIntegratedCircuit(5), Materials.Biomass.getFluid(40L), Materials.Water.getFluid(12L), 16, 24, false);
        GT_Values.RA.addDistilleryRecipe(GT_Utility.getIntegratedCircuit(5), Materials.Water.getFluid(25L), GT_ModHandler.getDistilledWater(25L), 16, 10, false);
        GT_Values.RA.addDistilleryRecipe(GT_Utility.getIntegratedCircuit(1), FluidRegistry.getFluidStack("potion.potatojuice", 2), FluidRegistry.getFluidStack("potion.vodka", 1), 16, 16, true);
        GT_Values.RA.addDistilleryRecipe(GT_Utility.getIntegratedCircuit(1), FluidRegistry.getFluidStack("potion.lemonade", 2), FluidRegistry.getFluidStack("potion.alcopops", 1), 16, 16, true);

        GT_Values.RA.addFluidHeaterRecipe(GT_Utility.getIntegratedCircuit(1), Materials.Water.getFluid(6L), Materials.Water.getGas((480/EU_PER_STEAM)), 30, 30);
        GT_Values.RA.addFluidHeaterRecipe(GT_Utility.getIntegratedCircuit(1), GT_ModHandler.getDistilledWater(6L), Materials.Water.getGas((480/EU_PER_STEAM)), 30, 30);
        GT_Values.RA.addFluidHeaterRecipe(GT_Utility.getIntegratedCircuit(1), Materials.SeedOil.getFluid(16L), Materials.FryingOilHot.getFluid(16L), 16, 30);
        GT_Values.RA.addFluidHeaterRecipe(GT_Utility.getIntegratedCircuit(1), Materials.FishOil.getFluid(16L), Materials.FryingOilHot.getFluid(16L), 16, 30);

        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L), FluidRegistry.getFluid("oil"), FluidRegistry.getFluid("lubricant"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L), FluidRegistry.getFluid("oil"), FluidRegistry.getFluid("lubricant"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), FluidRegistry.getFluid("oil"), FluidRegistry.getFluid("lubricant"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L), FluidRegistry.getFluid("creosote"), FluidRegistry.getFluid("lubricant"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L), FluidRegistry.getFluid("creosote"), FluidRegistry.getFluid("lubricant"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), FluidRegistry.getFluid("creosote"), FluidRegistry.getFluid("lubricant"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L), FluidRegistry.getFluid("seedoil"), FluidRegistry.getFluid("lubricant"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L), FluidRegistry.getFluid("seedoil"), FluidRegistry.getFluid("lubricant"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), FluidRegistry.getFluid("seedoil"), FluidRegistry.getFluid("lubricant"), false);
        for (Fluid tFluid : new Fluid[]{FluidRegistry.WATER, GT_ModHandler.getDistilledWater(1L).getFluid()}) {
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Milk, 1L), tFluid, FluidRegistry.getFluid("milk"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L), tFluid, FluidRegistry.getFluid("potion.wheatyjuice"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Potassium, 1L), tFluid, FluidRegistry.getFluid("potion.mineralwater"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), tFluid, FluidRegistry.getFluid("potion.mineralwater"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L), tFluid, FluidRegistry.getFluid("potion.mineralwater"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 1L), tFluid, FluidRegistry.getFluid("potion.mineralwater"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L), tFluid, FluidRegistry.getFluid("potion.thick"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.magma_cream, 1, 0), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.fermented_spider_eye, 1, 0), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.spider_eye, 1, 0), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.speckled_melon, 1, 0), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.ghast_tear, 1, 0), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.nether_wart, 1, 0), tFluid, FluidRegistry.getFluid("potion.awkward"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Blocks.red_mushroom, 1, 0), tFluid, FluidRegistry.getFluid("potion.poison"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.fish, 1, 3), tFluid, FluidRegistry.getFluid("potion.poison.strong"), true);
            GT_Values.RA.addBrewingRecipe(ItemList.IC2_Grin_Powder.get(1L), tFluid, FluidRegistry.getFluid("potion.poison.strong"), false);
            //GT_Values.RA.addBrewingRecipe(new ItemStack(Items.reeds, 1, 0), tFluid, FluidRegistry.getFluid("potion.reedwater"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.apple, 1, 0), tFluid, FluidRegistry.getFluid("potion.applejuice"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.golden_apple, 1, 0), tFluid, FluidRegistry.getFluid("potion.goldenapplejuice"), true);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.golden_apple, 1, 1), tFluid, FluidRegistry.getFluid("potion.idunsapplejuice"), true);
            GT_Values.RA.addBrewingRecipe(ItemList.IC2_Hops.get(1L), tFluid, FluidRegistry.getFluid("potion.hopsjuice"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coffee, 1L), tFluid, FluidRegistry.getFluid("potion.darkcoffee"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L), tFluid, FluidRegistry.getFluid("potion.chillysauce"), false);

            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L), 200);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(3L), 300);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L), 200);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(1L), 100);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(1L), 100);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(3L), 300);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(4L), 400);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(3L), 300);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L), 200);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L), 200);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(3L), 300);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(4L), 400);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(3L), 300);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L), 200);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L), 200);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(3L), 300);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(4L), 400);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(3L), 300);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L), 200);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L), 200);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(3L), 300);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(4L), 400);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(3L), 300);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L), 200);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L), 200);
        }
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L), FluidRegistry.getFluid("potion.chillysauce"), FluidRegistry.getFluid("potion.hotsauce"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L), FluidRegistry.getFluid("potion.hotsauce"), FluidRegistry.getFluid("potion.diabolosauce"), true);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L), FluidRegistry.getFluid("potion.diabolosauce"), FluidRegistry.getFluid("potion.diablosauce"), true);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coffee, 1L), FluidRegistry.getFluid("milk"), FluidRegistry.getFluid("potion.coffee"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cocoa, 1L), FluidRegistry.getFluid("milk"), FluidRegistry.getFluid("potion.darkchocolatemilk"), false);
        GT_Values.RA.addBrewingRecipe(ItemList.IC2_Hops.get(1L), FluidRegistry.getFluid("potion.wheatyjuice"), FluidRegistry.getFluid("potion.wheatyhopsjuice"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L), FluidRegistry.getFluid("potion.hopsjuice"), FluidRegistry.getFluid("potion.wheatyhopsjuice"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), FluidRegistry.getFluid("potion.tea"), FluidRegistry.getFluid("potion.sweettea"), true);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), FluidRegistry.getFluid("potion.coffee"), FluidRegistry.getFluid("potion.cafeaulait"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), FluidRegistry.getFluid("potion.cafeaulait"), FluidRegistry.getFluid("potion.laitaucafe"), true);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), FluidRegistry.getFluid("potion.lemonjuice"), FluidRegistry.getFluid("potion.lemonade"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), FluidRegistry.getFluid("potion.darkcoffee"), FluidRegistry.getFluid("potion.darkcafeaulait"), true);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), FluidRegistry.getFluid("potion.darkchocolatemilk"), FluidRegistry.getFluid("potion.chocolatemilk"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1L), FluidRegistry.getFluid("potion.tea"), FluidRegistry.getFluid("potion.icetea"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1L), FluidRegistry.getFluid("potion.lemonade"), FluidRegistry.getFluid("potion.cavejohnsonsgrenadejuice"), true);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), FluidRegistry.getFluid("potion.mundane"), FluidRegistry.getFluid("potion.purpledrink"), true);
        GT_Values.RA.addBrewingRecipe(new ItemStack(Items.fermented_spider_eye, 1, 0), FluidRegistry.getFluid("potion.mundane"), FluidRegistry.getFluid("potion.weakness"), false);
        GT_Values.RA.addBrewingRecipe(new ItemStack(Items.fermented_spider_eye, 1, 0), FluidRegistry.getFluid("potion.thick"), FluidRegistry.getFluid("potion.weakness"), false);

        GT_Values.RA.addBrewingRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "fertilizerBio", 4L, 0), FluidRegistry.WATER, FluidRegistry.getFluid("biomass"), false);
        GT_Values.RA.addBrewingRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "mulch", 16L, 0), GT_ModHandler.getDistilledWater(750L).getFluid(), FluidRegistry.getFluid("biomass"), false);
        GT_Values.RA.addBrewingRecipeCustom(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "mulch", 8L, 0), FluidRegistry.getFluidStack("juice", 500), FluidRegistry.getFluidStack("biomass", 750), 128, 4, false);

        GT_Values.RA.addBrewingRecipe(GT_ModHandler.getIC2Item("biochaff", 1), FluidRegistry.WATER, FluidRegistry.getFluid("ic2biomass"), false);
        GT_Values.RA.addBrewingRecipe(GT_ModHandler.getIC2Item("biochaff", 1), GT_ModHandler.getDistilledWater(750L).getFluid(), FluidRegistry.getFluid("ic2biomass"), false);

        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 32767), new ItemStack(Items.wheat, 4, 32767), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), null, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 2), new ItemStack(Items.wheat, 4, 32767), GT_Utility.getIntegratedCircuit(2), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 32767), GT_ModHandler.getModItem("BiomesOPlenty", "plants", 4, 6), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 2), GT_ModHandler.getModItem("BiomesOPlenty", "plants", 4, 6), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 32767), GT_ModHandler.getModItem("harvestcraft", "oatsItem", 4), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 2), GT_ModHandler.getModItem("harvestcraft", "oatsItem", 4), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 32767), GT_ModHandler.getModItem("harvestcraft", "ryeItem", 4), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 2), GT_ModHandler.getModItem("harvestcraft", "ryeItem", 4), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 32767), GT_ModHandler.getModItem("harvestcraft", "barleyItem", 4), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 2), GT_ModHandler.getModItem("harvestcraft", "barleyItem", 4, 6), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 32767), GT_ModHandler.getModItem("Natura", "barleyFood", 4), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 2), GT_ModHandler.getModItem("Natura", "barleyFood", 4), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 32767), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 4L), GT_Utility.getIntegratedCircuit(3),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 2), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 4L), GT_Utility.getIntegratedCircuit(3),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);

        this.addPotionRecipes("waterbreathing", new ItemStack(Items.fish, 1, 3));
        this.addPotionRecipes("fireresistance", new ItemStack(Items.magma_cream, 1, 0));
        this.addPotionRecipes("nightvision", new ItemStack(Items.golden_carrot, 1, 0));
        this.addPotionRecipes("weakness", new ItemStack(Items.fermented_spider_eye, 1, 0));
        this.addPotionRecipes("poison", new ItemStack(Items.spider_eye, 1, 0));
        this.addPotionRecipes("health", new ItemStack(Items.speckled_melon, 1, 0));
        this.addPotionRecipes("regen", new ItemStack(Items.ghast_tear, 1, 0));
        this.addPotionRecipes("speed", GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L));
        this.addPotionRecipes("strength", GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L));

        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("milk", 50), FluidRegistry.getFluidStack("potion.mundane", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.lemonjuice", 50), FluidRegistry.getFluidStack("potion.limoncello", 25), 1024, true);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.applejuice", 50), FluidRegistry.getFluidStack("potion.cider", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.goldenapplejuice", 50), FluidRegistry.getFluidStack("potion.goldencider", 25), 1024, true);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.idunsapplejuice", 50), FluidRegistry.getFluidStack("potion.notchesbrew", 25), 1024, true);
        //GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.reedwater", 50), FluidRegistry.getFluidStack("potion.rum", 25), 1024, true);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.rum", 50), FluidRegistry.getFluidStack("potion.piratebrew", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.grapejuice", 50), FluidRegistry.getFluidStack("potion.wine", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.wine", 50), FluidRegistry.getFluidStack("potion.vinegar", 10), 2048, true);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.wheatyjuice", 50), FluidRegistry.getFluidStack("potion.scotch", 25), 1024, true);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.scotch", 50), FluidRegistry.getFluidStack("potion.glenmckenner", 10), 2048, true);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.wheatyhopsjuice", 50), FluidRegistry.getFluidStack("potion.beer", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.hopsjuice", 50), FluidRegistry.getFluidStack("potion.darkbeer", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.darkbeer", 50), FluidRegistry.getFluidStack("potion.dragonblood", 10), 2048, true);

        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.beer", 75), FluidRegistry.getFluidStack("potion.vinegar", 50), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.cider", 75), FluidRegistry.getFluidStack("potion.vinegar", 50), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.goldencider", 75), FluidRegistry.getFluidStack("potion.vinegar", 50), 2048, true);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.rum", 75), FluidRegistry.getFluidStack("potion.vinegar", 50), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.wine", 75), FluidRegistry.getFluidStack("potion.vinegar", 50), 2048, false);

        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.awkward", 50), FluidRegistry.getFluidStack("potion.weakness", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.mundane", 50), FluidRegistry.getFluidStack("potion.weakness", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.thick", 50), FluidRegistry.getFluidStack("potion.weakness", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.poison", 50), FluidRegistry.getFluidStack("potion.damage", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.health", 50), FluidRegistry.getFluidStack("potion.damage", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.waterbreathing", 50), FluidRegistry.getFluidStack("potion.damage", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.nightvision", 50), FluidRegistry.getFluidStack("potion.invisibility", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.fireresistance", 50), FluidRegistry.getFluidStack("potion.slowness", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.speed", 50), FluidRegistry.getFluidStack("potion.slowness", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.strength", 50), FluidRegistry.getFluidStack("potion.weakness", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.regen", 50), FluidRegistry.getFluidStack("potion.poison", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.poison.strong", 50), FluidRegistry.getFluidStack("potion.damage.strong", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.health.strong", 50), FluidRegistry.getFluidStack("potion.damage.strong", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.speed.strong", 50), FluidRegistry.getFluidStack("potion.slowness.strong", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.strength.strong", 50), FluidRegistry.getFluidStack("potion.weakness.strong", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.nightvision.long", 50), FluidRegistry.getFluidStack("potion.invisibility.long", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.regen.strong", 50), FluidRegistry.getFluidStack("potion.poison.strong", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.poison.long", 50), FluidRegistry.getFluidStack("potion.damage.long", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.waterbreathing.long", 50), FluidRegistry.getFluidStack("potion.damage.long", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.fireresistance.long", 50), FluidRegistry.getFluidStack("potion.slowness.long", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.speed.long", 50), FluidRegistry.getFluidStack("potion.slowness.long", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.strength.long", 50), FluidRegistry.getFluidStack("potion.weakness.long", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.regen.long", 50), FluidRegistry.getFluidStack("potion.poison.long", 10), 2048, false);

        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_PotatoChips.get(1L), ItemList.Food_PotatoChips.get(1L));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Potato_On_Stick.get(1L), ItemList.Food_Potato_On_Stick_Roasted.get(1L));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Bun.get(1L), ItemList.Food_Baked_Bun.get(1L));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Bread.get(1L), ItemList.Food_Baked_Bread.get(1L));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Baguette.get(1L), ItemList.Food_Baked_Baguette.get(1L));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Pizza_Veggie.get(1L), ItemList.Food_Baked_Pizza_Veggie.get(1L));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Pizza_Cheese.get(1L), ItemList.Food_Baked_Pizza_Cheese.get(1L));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Pizza_Meat.get(1L), ItemList.Food_Baked_Pizza_Meat.get(1L));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Baguette.get(1L), ItemList.Food_Baked_Baguette.get(1L));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Cake.get(1L), ItemList.Food_Baked_Cake.get(1L));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Cookie.get(1L), new ItemStack(Items.cookie, 1));
        GT_ModHandler.addSmeltingRecipe(new ItemStack(Items.slime_ball, 1), ItemList.IC2_Resin.get(1L));

        GT_ModHandler.addExtractionRecipe(new ItemStack(Blocks.bookshelf, 1, 32767), new ItemStack(Items.book, 3, 0));
        GT_ModHandler.addExtractionRecipe(new ItemStack(Items.slime_ball, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 2L));
        GT_ModHandler.addExtractionRecipe(ItemList.IC2_Resin.get(1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 3L));
        GT_ModHandler.addExtractionRecipe(GT_ModHandler.getIC2Item("rubberSapling", 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 1L));
        GT_ModHandler.addExtractionRecipe(GT_ModHandler.getIC2Item("rubberLeaves", 16L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Cell_Air.get(1L), ItemList.Cell_Empty.get(1L));

        GT_ModHandler.addCompressionRecipe(ItemList.IC2_Compressed_Coal_Chunk.get(1L), ItemList.IC2_Industrial_Diamond.get(1L));
        GT_ModHandler.addCompressionRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1L), GT_ModHandler.getIC2Item("Uran238", 1L));
        GT_ModHandler.addCompressionRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uranium235, 1L), GT_ModHandler.getIC2Item("Uran235", 1L));
        GT_ModHandler.addCompressionRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 1L), GT_ModHandler.getIC2Item("Plutonium", 1L));
        GT_ModHandler.addCompressionRecipe(GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Uranium235, 1L), GT_ModHandler.getIC2Item("smallUran235", 1L));
        GT_ModHandler.addCompressionRecipe(GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium, 1L), GT_ModHandler.getIC2Item("smallPlutonium", 1L));
        GT_ModHandler.addCompressionRecipe(new ItemStack(Blocks.ice, 2, 32767), new ItemStack(Blocks.packed_ice, 1, 0));
        GT_ModHandler.addCompressionRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1L), new ItemStack(Blocks.ice, 1, 0));
        GT_ModHandler.addCompressionRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.CertusQuartz, 4L), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, "tile.BlockQuartz", 1L));
        GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 8L, 10), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, "tile.BlockQuartz", 1L));
        GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 8L, 11), new ItemStack(Blocks.quartz_block, 1, 0));
        GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 8L, 12), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, "tile.BlockFluix", 1L));
        GT_ModHandler.addCompressionRecipe(new ItemStack(Items.quartz, 4, 0), new ItemStack(Blocks.quartz_block, 1, 0));
        GT_ModHandler.addCompressionRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 4L), new ItemStack(Blocks.glowstone, 1));

        GameRegistry.addSmelting(ItemList.CompressedFireclay.get(1), ItemList.Firebrick.get(1), 0);

        GT_ModHandler.removeFurnaceSmelting(GT_OreDictUnificator.get(OrePrefixes.ore, Materials.Graphite, 1L));
        GT_ModHandler.addSmeltingRecipe(GT_OreDictUnificator.get(OrePrefixes.ore, Materials.Graphite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));
        GT_ModHandler.removeFurnaceSmelting(GT_OreDictUnificator.get(OrePrefixes.oreBlackgranite, Materials.Graphite, 1L));
        GT_ModHandler.addSmeltingRecipe(GT_OreDictUnificator.get(OrePrefixes.oreBlackgranite, Materials.Graphite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));
        GT_ModHandler.removeFurnaceSmelting(GT_OreDictUnificator.get(OrePrefixes.oreEndstone, Materials.Graphite, 1L));
        GT_ModHandler.addSmeltingRecipe(GT_OreDictUnificator.get(OrePrefixes.oreEndstone, Materials.Graphite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));
        GT_ModHandler.removeFurnaceSmelting(GT_OreDictUnificator.get(OrePrefixes.oreNetherrack, Materials.Graphite, 1L));
        GT_ModHandler.addSmeltingRecipe(GT_OreDictUnificator.get(OrePrefixes.oreNetherrack, Materials.Graphite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));
        GT_ModHandler.removeFurnaceSmelting(GT_OreDictUnificator.get(OrePrefixes.oreRedgranite, Materials.Graphite, 1L));
        GT_ModHandler.addSmeltingRecipe(GT_OreDictUnificator.get(OrePrefixes.oreRedgranite, Materials.Graphite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));

        GT_ModHandler.addPulverisationRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, "tile.BlockSkyStone", 1L, 32767), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 45), GT_Values.NI, 0, false);
        GT_ModHandler.addPulverisationRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, "tile.BlockSkyChest", 1L, 32767), GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 8L, 45), GT_Values.NI, 0, false);
        GT_ModHandler.addPulverisationRecipe(new ItemStack(Items.blaze_rod, 1), new ItemStack(Items.blaze_powder, 3), new ItemStack(Items.blaze_powder, 1), 50, false);
        GT_ModHandler.addPulverisationRecipe(new ItemStack(Blocks.red_mushroom, 1, 32767), ItemList.IC2_Grin_Powder.get(1L));
        GT_ModHandler.addPulverisationRecipe(new ItemStack(Items.item_frame, 1, 32767), new ItemStack(Items.leather, 1), GT_OreDictUnificator.getDust(Materials.Wood, OrePrefixes.stick.mMaterialAmount * 4L), 95, false);
        GT_ModHandler.addPulverisationRecipe(new ItemStack(Items.bow, 1, 0), new ItemStack(Items.string, 3), GT_OreDictUnificator.getDust(Materials.Wood, OrePrefixes.stick.mMaterialAmount * 3L), 95, false);
        GT_ModHandler.addPulverisationRecipe(Materials.Brick.getIngots(2), Materials.Brick.getDustSmall(1));
        GT_ModHandler.addPulverisationRecipe(new ItemStack(Blocks.brick_stairs, 1, 0), Materials.Brick.getDustSmall(6));
        GT_ModHandler.addPulverisationRecipe(ItemList.CompressedFireclay.get(1), Materials.Fireclay.getDustSmall(1));
        GT_ModHandler.addPulverisationRecipe(ItemList.Firebrick.get(1), Materials.Brick.getDust(1));
        GT_ModHandler.addPulverisationRecipe(ItemList.Casing_Firebricks.get(1), Materials.Brick.getDust(4));
        GT_Values.RA.addPulveriserRecipe(ItemList.Conveyor_Module_LV.get(1), new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Rubber, 5L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 4L),GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 2L)}, null, 1328, 4);
        GT_Values.RA.addSifterRecipe(new ItemStack(Blocks.gravel, 1, 0), new ItemStack[] {new ItemStack(Items.flint, 1, 0), new ItemStack(Items.flint, 1, 0), new ItemStack(Items.flint, 1, 0), new ItemStack(Items.flint, 1, 0), new ItemStack(Items.flint, 1, 0), new ItemStack(Items.flint, 1, 0)}, new int[] {10000, 9000, 8000, 6000, 3300, 2500}, 600, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.stonebrick, 1, 0), new ItemStack(Blocks.stonebrick, 1, 2), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.stone, 1, 0), new ItemStack(Blocks.cobblestone, 1, 0), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.cobblestone, 1, 0), new ItemStack(Blocks.gravel, 1, 0), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.gravel, 1, 0), new ItemStack(Blocks.sand, 1, 0), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.sandstone, 1, 32767), new ItemStack(Blocks.sand, 1, 0), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.ice, 1, 0), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1L), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.packed_ice, 1, 0), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 2L), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.brick_block, 1, 0), new ItemStack(Items.brick, 3, 0), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.nether_brick, 1, 0), new ItemStack(Items.netherbrick, 3, 0), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.stained_glass, 1, 32767), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 1L), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.glass, 1, 32767), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 1L), 10, 10);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.stained_glass_pane, 1, 32767), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3L), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.glass_pane, 1, 32767), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3L), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(ItemList.Firebrick.get(1), Materials.Brick.getDust(1), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(ItemList.Casing_Firebricks.get(1), ItemList.Firebrick.get(3), 10, 16);
        
        if (Loader.isModLoaded("HardcoreEnderExpansion")) {
        	GT_Values.RA.addForgeHammerRecipe(GT_ModHandler.getModItem("HardcoreEnderExpansion", "endium_ore", 1), GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.HeeEndium, 1), 16, 10);
            GT_ModHandler.addPulverisationRecipe(GT_ModHandler.getModItem("HardcoreEnderExpansion", "endium_ore", 1), GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.HeeEndium, 2), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Endstone, 1), 50, GT_Values.NI, 0, true);
            GT_OreDictUnificator.set(OrePrefixes.ingot, Materials.HeeEndium, GT_ModHandler.getModItem("HardcoreEnderExpansion", "endium_ingot", 1), true, true);
        }

        GT_Values.RA.addAmplifier(ItemList.IC2_Scrap.get(9L), 180, 1);
        GT_Values.RA.addAmplifier(ItemList.IC2_Scrapbox.get(1L), 180, 1);

        GT_Values.RA.addBoxingRecipe(ItemList.IC2_Scrap.get(9L), ItemList.Schematic_3by3.get(0L), ItemList.IC2_Scrapbox.get(1L), 16, 1);
        GT_Values.RA.addBoxingRecipe(ItemList.Food_Fries.get(1L), GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Paper, 1L), ItemList.Food_Packaged_Fries.get(1L), 64, 16);
        GT_Values.RA.addBoxingRecipe(ItemList.Food_PotatoChips.get(1L), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 1L), ItemList.Food_Packaged_PotatoChips.get(1L), 64, 16);
        GT_Values.RA.addBoxingRecipe(ItemList.Food_ChiliChips.get(1L), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 1L), ItemList.Food_Packaged_ChiliChips.get(1L), 64, 16);

        //fuel rod canner recipes
        GT_Values.RA.addCannerRecipe(GT_ModHandler.getIC2Item("fuelRod", 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Lithium, 1L), GT_ModHandler.getIC2Item("reactorLithiumCell", 1, 1), null, 16, 64);
        GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getIC2Item("TritiumCell", 1), GT_ModHandler.getIC2Item("fuelRod", 1), Materials.Tritium.getGas(32), 10000, 16, 64);
        GT_Values.RA.addCannerRecipe(GT_ModHandler.getIC2Item("fuelRod", 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 3), ItemList.ThoriumCell_1.get(1L), null, 30, 16);
        GT_Values.RA.addCannerRecipe(GT_ModHandler.getIC2Item("fuelRod", 1), GT_ModHandler.getIC2Item("UranFuel", 1), ItemList.Uraniumcell_1.get(1), null, 30, 16);
        GT_Values.RA.addCannerRecipe(GT_ModHandler.getIC2Item("fuelRod", 1), GT_ModHandler.getIC2Item("MOXFuel", 1), ItemList.Moxcell_1.get(1), null, 30, 16);

        //Fusion tiering -T1 32768EU/t -T2 65536EU/t - T3 131073EU/t
        //Fusion with margin 32700         65450          131000
        //Startup  max       160M EU       320M EU        640M EU
        //Fluid input,Fluid input,Fluid output,ticks,EU/t,Startup
        //FT1, FT2, FT3 - fusion tier required, + - requires different startup recipe (startup cost bigger than available on the tier)
        GT_Values.RA.addFusionReactorRecipe(Materials.Lithium.getMolten(16), Materials.Tungsten.getMolten(16), Materials.Iridium.getMolten(16), 64, 32700, 300000000); //FT1+ - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Deuterium.getGas(125), Materials.Tritium.getGas(125), Materials.Helium.getPlasma(125), 16, 4096, 40000000);  //FT1 Cheap - farmable
        GT_Values.RA.addFusionReactorRecipe(Materials.Deuterium.getGas(125), Materials.Helium_3.getGas(125), Materials.Helium.getPlasma(125), 16, 2048, 60000000); //FT1 Expensive //
        GT_Values.RA.addFusionReactorRecipe(Materials.Carbon.getMolten(125), Materials.Helium_3.getGas(125), Materials.Oxygen.getPlasma(125), 32, 4096, 80000000); //FT1 Expensive //
        GT_Values.RA.addFusionReactorRecipe(Materials.Aluminium.getMolten(16), Materials.Lithium.getMolten(16), Materials.Sulfur.getPlasma(144), 32, 10240, 240000000); //FT1+ Cheap
        GT_Values.RA.addFusionReactorRecipe(Materials.Beryllium.getMolten(16), Materials.Deuterium.getGas(375), Materials.Nitrogen.getPlasma(125), 16, 16384, 180000000); //FT1+ Expensive //
        GT_Values.RA.addFusionReactorRecipe(Materials.Silicon.getMolten(16), Materials.Magnesium.getMolten(16), Materials.Iron.getPlasma(144), 32, 8192, 360000000); //FT1++ Cheap //
        GT_Values.RA.addFusionReactorRecipe(Materials.Potassium.getMolten(16), Materials.Fluorine.getGas(144), Materials.Nickel.getPlasma(144), 16, 32700, 480000000); //FT1++ Expensive //
        GT_Values.RA.addFusionReactorRecipe(Materials.Beryllium.getMolten(16), Materials.Tungsten.getMolten(16), Materials.Platinum.getMolten(16), 32, 32700, 150000000); //FT1 - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Neodymium.getMolten(16), Materials.Hydrogen.getGas(48), Materials.Europium.getMolten(16), 32, 24576, 150000000); //FT1 - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Lutetium.getMolten(16), Materials.Chrome.getMolten(16), Materials.Americium.getMolten(16), 96, 49152, 200000000); //FT2 - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Plutonium.getMolten(16), Materials.Thorium.getMolten(16), Materials.Naquadah.getMolten(16), 64, 32700, 300000000); //FT1+ - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Americium.getMolten(96), Materials.Naquadria.getMolten(96), Materials.Neutronium.getMolten(24), 60, 98304, 600000000); //FT3 - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Glowstone.getMolten(16), Materials.Helium.getPlasma(4), Materials.Sunnarium.getMolten(16), 32, 7680, 40000000); //Mark 1 Expensive //

        GT_Values.RA.addFusionReactorRecipe(Materials.Tungsten.getMolten(16), Materials.Helium.getGas(16), Materials.Osmium.getMolten(16), 256, 24578, 150000000); //FT1 - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Manganese.getMolten(16), Materials.Hydrogen.getGas(16), Materials.Iron.getMolten(16), 64, 8192, 120000000); //FT1 - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Magnesium.getMolten(128), Materials.Oxygen.getGas(128), Materials.Calcium.getPlasma(16), 128, 8192, 120000000); //
        GT_Values.RA.addFusionReactorRecipe(Materials.Mercury.getFluid(16), Materials.Magnesium.getMolten(16), Materials.Uranium.getMolten(16), 64, 49152, 240000000); //FT2 - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Gold.getMolten(16), Materials.Aluminium.getMolten(16), Materials.Uranium.getMolten(16), 64, 49152, 240000000); //FT2 - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Uranium.getMolten(16), Materials.Helium.getGas(16), Materials.Plutonium.getMolten(16), 128, 49152, 480000000); //FT2+ - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Vanadium.getMolten(16), Materials.Hydrogen.getGas(125), Materials.Chrome.getMolten(16), 64, 24576, 140000000); //FT1 - utility

        GT_Values.RA.addFusionReactorRecipe(Materials.Gallium.getMolten(16), Materials.Radon.getGas(125), Materials.Duranium.getMolten(16), 64, 16384, 140000000);
        GT_Values.RA.addFusionReactorRecipe(Materials.Titanium.getMolten(48), Materials.Duranium.getMolten(32), Materials.Tritanium.getMolten(16), 64, 32700, 200000000);
        GT_Values.RA.addFusionReactorRecipe(Materials.Tantalum.getMolten(16), Materials.Tritium.getGas(16), Materials.Tungsten.getMolten(16), 16, 24576, 200000000); //
        GT_Values.RA.addFusionReactorRecipe(Materials.Silver.getMolten(16), Materials.Lithium.getMolten(16), Materials.Indium.getMolten(16), 32, 24576, 380000000); //

        //NEW RECIPES FOR FUSION
        GT_Values.RA.addFusionReactorRecipe(Materials.Magnesium.getMolten(144), Materials.Carbon.getMolten(144), Materials.Argon.getPlasma(125), 32, 24576, 180000000);//FT1+ - utility

        GT_Values.RA.addFusionReactorRecipe(Materials.Copper.getMolten(72), Materials.Tritium.getGas(250), Materials.Zinc.getPlasma(72), 16, 49152, 180000000);//FT2 - farmable
        GT_Values.RA.addFusionReactorRecipe(Materials.Cobalt.getMolten(144), Materials.Silicon.getMolten(144), Materials.Niobium.getPlasma(144), 16, 49152, 200000000);//FT2 - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Gold.getMolten(144), Materials.Arsenic.getMolten(144), Materials.Silver.getPlasma(144), 16, 49152, 350000000);//FT2+
        GT_Values.RA.addFusionReactorRecipe(Materials.Silver.getMolten(144), Materials.Helium_3.getGas(375), Materials.Tin.getPlasma(144), 16, 49152, 280000000);//FT2
        GT_Values.RA.addFusionReactorRecipe(Materials.Tungsten.getMolten(144), Materials.Carbon.getMolten(144), Materials.Mercury.getPlasma(144), 16, 49152, 300000000);//FT2

        GT_Values.RA.addFusionReactorRecipe(Materials.Tantalum.getMolten(144), Materials.Zinc.getPlasma(72), Materials.Bismuth.getPlasma(144), 16, 98304, 350000000);//FT3 - farmable
	    GT_Values.RA.addFusionReactorRecipe(Materials.Caesium.getMolten(144), Materials.Carbon.getMolten(144), Materials.Promethium.getMolten(144), 64, 49152, 400000000);//FT3
        GT_Values.RA.addFusionReactorRecipe(Materials.Iridium.getMolten(144), Materials.Fluorine.getGas(500), Materials.Radon.getPlasma(144), 32, 98304, 450000000);//FT3 - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Plutonium241.getMolten(144), Materials.Hydrogen.getGas(2000), Materials.Americium.getPlasma(144), 64, 98304, 500000000);//FT3

        GT_ModHandler.removeRecipeByOutput(ItemList.IC2_Fertilizer.get(1L));
        GT_Values.RA.addImplosionRecipe(ItemList.IC2_Compressed_Coal_Chunk.get(1L), 8, ItemList.IC2_Industrial_Diamond.get(1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 4L));
        GT_Values.RA.addImplosionRecipe(ItemList.Ingot_IridiumAlloy.get(1L), 8, GT_OreDictUnificator.get(OrePrefixes.plateAlloy, Materials.Iridium, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 4L));

        GT_Values.RA.addFluidExtractionRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quartzite, 1L), null, Materials.Glass.getMolten(72), 10000, 600, 28);//(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SiliconDioxide,1L), GT_OreDictUnificator.get(OrePrefixes.dust,Materials.SiliconDioxide,2L),GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Glass,1L)/** GT_Utility.fillFluidContainer(Materials.Glass.getMolten(1000), ItemList.Cell_Empty.get(1), true, true)**/, 600, 16);

        GT_Values.RA.addDistillationTowerRecipe(Materials.Creosote.getFluid(1000L), new FluidStack[]{Materials.Lubricant.getFluid(500L)}, null, 400, 120);
        GT_Values.RA.addDistillationTowerRecipe(Materials.SeedOil.getFluid(1400L), new FluidStack[]{Materials.Lubricant.getFluid(500L)}, null, 400, 120);
        GT_Values.RA.addDistillationTowerRecipe(Materials.FishOil.getFluid(1200L), new FluidStack[]{Materials.Lubricant.getFluid(500L)}, null, 400, 120);
        GT_Values.RA.addDistillationTowerRecipe(Materials.Biomass.getFluid(1000L), new FluidStack[]{Materials.Ethanol.getFluid(600L), Materials.Water.getFluid(300L)}, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L), 32, 400);
        GT_Values.RA.addDistillationTowerRecipe(Materials.Water.getFluid(288L), new FluidStack[]{GT_ModHandler.getDistilledWater(260L)}, null, 16, 16);

        if(!GregTech_API.mIC2Classic){
            GT_Values.RA.addDistillationTowerRecipe(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 3000), new FluidStack[]{new FluidStack(FluidRegistry.getFluid("ic2biogas"), 8000), Materials.Water.getFluid(125L)}, ItemList.IC2_Fertilizer.get(1), 450, 120);
            GT_Values.RA.addFuel(GT_ModHandler.getIC2Item("biogasCell", 1L), null, 80, 1);

            GT_Values.RA.addDistilleryRecipe(GT_Utility.getIntegratedCircuit(1), new FluidStack(FluidRegistry.getFluid("ic2biomass"), 20), new FluidStack(FluidRegistry.getFluid("ic2biogas"), 32), 40, 16, false);
            GT_Values.RA.addDistilleryRecipe(GT_Utility.getIntegratedCircuit(2), new FluidStack(FluidRegistry.getFluid("ic2biomass"), 4), Materials.Water.getFluid(2), 80, 30, false);
        }

        GT_Values.RA.addFuel(new ItemStack(Items.golden_apple, 1, 1), new ItemStack(Items.apple, 1), 6400, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("Thaumcraft", "ItemShard", 1L, 6), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ForbiddenMagic", "GluttonyShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ForbiddenMagic", "FMResource", 1L, 3), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ForbiddenMagic", "NetherShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ForbiddenMagic", "NetherShard", 1L, 1), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ForbiddenMagic", "NetherShard", 1L, 2), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ForbiddenMagic", "NetherShard", 1L, 3), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ForbiddenMagic", "NetherShard", 1L, 4), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ForbiddenMagic", "NetherShard", 1L, 5), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ForbiddenMagic", "NetherShard", 1L, 6), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("TaintedMagic", "WarpedShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("TaintedMagic", "FluxShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("TaintedMagic", "EldritchShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ThaumicTinkerer", "kamiResource", 1L, 6), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ThaumicTinkerer", "kamiResource", 1L, 7), null, 720, 5);        
        
        GT_Values.RA.addElectrolyzerRecipe(GT_Utility.getIntegratedCircuit(1), ItemList.Cell_Empty.get(1L), Materials.Water.getFluid(3000L), Materials.Hydrogen.getGas(2000L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 2000, 30);
        GT_Values.RA.addElectrolyzerRecipe(GT_Utility.getIntegratedCircuit(2), ItemList.Cell_Empty.get(1L), GT_ModHandler.getDistilledWater(3000L), Materials.Hydrogen.getGas(2000L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 2000, 30);
        GT_Values.RA.addElectrolyzerRecipe(GT_Utility.getIntegratedCircuit(3), ItemList.Cell_Empty.get(2L), Materials.Water.getFluid(3000L), Materials.Oxygen.getGas(1000L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 2000, 30);
        GT_Values.RA.addElectrolyzerRecipe(GT_Utility.getIntegratedCircuit(4), ItemList.Cell_Empty.get(2L), GT_ModHandler.getDistilledWater(3000L), Materials.Oxygen.getGas(1000L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 2000, 30);
        GT_Values.RA.addElectrolyzerRecipe(GT_ModHandler.getIC2Item("electrolyzedWaterCell", 3L), 0, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 30, 30);
        GT_Values.RA.addElectrolyzerRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L), 0, GT_ModHandler.getIC2Item("electrolyzedWaterCell", 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, 490, 30);
        GT_Values.RA.addElectrolyzerRecipe(ItemList.Dye_Bonemeal.get(3L), 0, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, 98, 26);
        GT_Values.RA.addElectrolyzerRecipe(new ItemStack(Blocks.sand, 8, 0), 0, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, 500, 25);
        GT_Values.RA.addElectrolyzerRecipe(new ItemStack(Blocks.sand, 8, 1), 0, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, 500, 25);
        GT_Values.RA.addElectrolyzerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tungstate, 7L),  GT_Utility.getIntegratedCircuit(1), Materials.Hydrogen.getGas(7000L), Materials.Oxygen.getGas(4000L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 2L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{10000, 10000, 0, 0, 0, 0}, 120, 1920);
        GT_Values.RA.addElectrolyzerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Scheelite, 7L),  GT_Utility.getIntegratedCircuit(1), Materials.Hydrogen.getGas(7000L), Materials.Oxygen.getGas(4000L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 2L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{10000, 10000, 0, 0, 0, 0}, 120, 1920);
        GT_Values.RA.addElectrolyzerRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.CarbonDioxide, 4),  GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 3), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1), ItemList.Cell_Empty.get(3), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{10000, 10000, 10000, 0, 0, 0}, 180, 60);
        GT_Values.RA.addElectrolyzerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1), 0, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 4), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, 100, 64);

        GT_Values.RA.addElectrolyzerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sphalerite, 2), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gallium, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{8000, 8000, 2000, 0, 0, 0},200, 24);
        GT_Values.RA.addElectrolyzerRecipe(ItemList.IC2_Fertilizer.get(1L), GT_Values.NI, GT_Values.NF, Materials.Water.getFluid(1000L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 100, 30);

        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherQuartz, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), Materials.Water.getFluid(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 3L), 500);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CertusQuartz, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), Materials.Water.getFluid(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.gem, Materials.CertusQuartz, 3L), 500);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quartzite, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), Materials.Water.getFluid(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Quartzite, 3L), 500);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherQuartz, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), GT_ModHandler.getDistilledWater(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 3L), 500);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CertusQuartz, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), GT_ModHandler.getDistilledWater(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.gem, Materials.CertusQuartz, 3L), 500);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quartzite, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), GT_ModHandler.getDistilledWater(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Quartzite, 3L), 500);

        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uraninite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1L), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1L), 1000);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uraninite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 1L), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1L), 1000);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L), Materials.Oxygen.getGas(3000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 5L), 500);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L), GT_Values.NI, Materials.Hydrogen.getGas(2000L), GT_ModHandler.getDistilledWater(1000L), ItemList.Cell_Empty.get(1L), GT_Values.NI, 10, 30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 1L), GT_Values.NI, Materials.Oxygen.getGas(500L), GT_ModHandler.getDistilledWater(500L), ItemList.Cell_Empty.get(1L), GT_Values.NI, 5, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(1)}, new FluidStack[]{Materials.Hydrogen.getGas(2000), Materials.Oxygen.getGas(1000)}, new FluidStack[]{GT_ModHandler.getDistilledWater(1000)}, new ItemStack[]{}, 10, 30);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Rutile, 1L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Carbon, 2L), Materials.Chlorine.getGas(4000L), Materials.Titaniumtetrachloride.getFluid(1000L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.CarbonMonoxide, 2L), 500, 480);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Rutile, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 2L), Materials.Chlorine.getGas(4000L), Materials.Titaniumtetrachloride.getFluid(1000L), GT_Values.NI, 500, 480);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesiumchloride, 2L), GT_Values.NF, Materials.Chlorine.getGas(3000L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2L), 300, 240);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 9L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_Values.NF, Materials.Rubber.getMolten(1296L), GT_Values.NI, 600, 16);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Sulfur, 1L), GT_Values.NF, Materials.Rubber.getMolten(144L), GT_Values.NI, 100, 16);

        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Gold, 8L), new ItemStack(Items.melon, 1, 32767), new ItemStack(Items.speckled_melon, 1, 0), 50);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Gold, 8L), new ItemStack(Items.carrot, 1, 32767), new ItemStack(Items.golden_carrot, 1, 0), 50);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Gold, 8L), new ItemStack(Items.apple, 1, 32767), new ItemStack(Items.golden_apple, 1, 0), 50);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Gold, 8L), new ItemStack(Items.apple, 1, 32767), new ItemStack(Items.golden_apple, 1, 1), 50);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1L), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderEye, 1L), 200, 480);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L), new ItemStack(Items.slime_ball, 1, 32767), new ItemStack(Items.magma_cream, 1, 0), 50);

        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 12L), ItemList.Cell_Empty.get(6L), 1200, 8);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 12L), ItemList.Cell_Empty.get(12L), 1200, 8);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 12L), ItemList.Cell_Empty.get(48L), 1200, 8);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 12L), new ItemStack(Items.bucket, 4, 0), 800, 4);
        GT_Values.RA.addPulveriserRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Marble, 1L), new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Marble, 1L)}, null, 160, 4);

        GT_Values.RA.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Obsidian, 2L), ItemList.TE_Hardened_Glass.get(2L), 200, 16);
        GT_Values.RA.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Lead, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Obsidian, 2L), ItemList.TE_Hardened_Glass.get(2L), 200, 16);
        GT_Values.RA.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 1L), 200, 8);//We use rubber

        GT_Values.RA.addCutterRecipe(GT_ModHandler.getModItem("BuildCraft|Transport", "item.buildcraftPipe.pipestructurecobblestone", 1L, 0), GT_ModHandler.getModItem("BuildCraft|Transport", "pipePlug", 8L, 0), GT_Values.NI, 32, 16);
        for (int i = 0; i < 16; i++) {
            GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.stained_glass, 3, i), new ItemStack(Blocks.stained_glass_pane, 8, i), GT_Values.NI, 50, 8);
        }
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.glass, 3, 0), new ItemStack(Blocks.glass_pane, 8, 0), GT_Values.NI, 50, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.stone, 1, 0), new ItemStack(Blocks.stone_slab, 2, 0), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.sandstone, 1, 0), new ItemStack(Blocks.stone_slab, 2, 1), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.cobblestone, 1, 0), new ItemStack(Blocks.stone_slab, 2, 3), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.brick_block, 1, 0), new ItemStack(Blocks.stone_slab, 2, 4), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.stonebrick, 1, 0), new ItemStack(Blocks.stone_slab, 2, 5), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.nether_brick, 1, 0), new ItemStack(Blocks.stone_slab, 2, 6), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.quartz_block, 1, 32767), new ItemStack(Blocks.stone_slab, 2, 7), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.glowstone, 1, 0), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Glowstone, 4L), GT_Values.NI, 100, 16);

        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.wool, 1, i), new ItemStack(Blocks.carpet, 2, i), GT_Values.NI, 50, 8);
        }
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.wooden_slab, 1, 0), ItemList.Plank_Oak.get(2L), GT_Values.NI, 50, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.wooden_slab, 1, 1), ItemList.Plank_Spruce.get(2L), GT_Values.NI, 50, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.wooden_slab, 1, 2), ItemList.Plank_Birch.get(2L), GT_Values.NI, 50, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.wooden_slab, 1, 3), ItemList.Plank_Jungle.get(2L), GT_Values.NI, 50, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.wooden_slab, 1, 4), ItemList.Plank_Acacia.get(2L), GT_Values.NI, 50, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.wooden_slab, 1, 5), ItemList.Plank_DarkOak.get(2L), GT_Values.NI, 50, 8);
        boolean loaded = Loader.isModLoaded(GT_MachineRecipeLoader.aTextForestry);//TODO OW YEAH NEW PLANK GEN CODE!!!
        ItemStack[] coverIDs = {
        		ItemList.Plank_Larch.get(2L),
        		ItemList.Plank_Teak.get(2L),
        		ItemList.Plank_Acacia_Green.get(2L),
        		ItemList.Plank_Lime.get(2L),
        		ItemList.Plank_Chestnut.get(2L),
        		ItemList.Plank_Wenge.get(2L),
        		ItemList.Plank_Baobab.get(2L),
        		ItemList.Plank_Sequoia.get(2L),
        		ItemList.Plank_Kapok.get(2L),
        		ItemList.Plank_Ebony.get(2L),
        		ItemList.Plank_Mahagony.get(2L),
        		ItemList.Plank_Balsa.get(2L),
        		ItemList.Plank_Willow.get(2L),
        		ItemList.Plank_Walnut.get(2L),
        		ItemList.Plank_Greenheart.get(2L),
        		ItemList.Plank_Cherry.get(2L),
        		ItemList.Plank_Mahoe.get(2L),
        		ItemList.Plank_Poplar.get(2L),
        		ItemList.Plank_Palm.get(2L),
        		ItemList.Plank_Papaya.get(2L),
        		ItemList.Plank_Pine.get(2L),
        		ItemList.Plank_Plum.get(2L),
        		ItemList.Plank_Maple.get(2L),
        		ItemList.Plank_Citrus.get(2L)};
        int i = 0;
        for (ItemStack cover : coverIDs) {
            if (loaded) {
                ItemStack slabWood = GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "slabs", 1, i);
                ItemStack slabWoodFireproof = GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "slabsFireproof", 1, i);
                GT_ModHandler.addCraftingRecipe(cover, GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"s ", " P", 'P', slabWood});
                GT_ModHandler.addCraftingRecipe(cover, GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"s ", " P", 'P', slabWoodFireproof});
                GT_Values.RA.addCutterRecipe(slabWood, cover, null, 40, 8);
                GT_Values.RA.addCutterRecipe(slabWoodFireproof, cover, null, 40, 8);
            } else if (GT_MachineRecipeLoader.isNEILoaded) {
                API.hideItem(cover);
            }
            i++;
        }
        for (int g = 0; g < 16; g++) {
            if (!GT_MachineRecipeLoader.isNEILoaded) {
                break;
            }
            API.hideItem(new ItemStack(GT_MetaGenerated_Item_03.INSTANCE, 1, g));
        }
    }
    public void run2() {
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Cupronickel, 1L), ItemList.Shape_Mold_Credit.get(0L), ItemList.Credit_Greg_Cupronickel.get(4L), 100, 16);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L), ItemList.Shape_Mold_Credit.get(0L), ItemList.Credit_Iron.get(4L), 100, 16);

        if (!GT_Mod.gregtechproxy.mDisableIC2Cables) {
            GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Copper, 1L), GT_ModHandler.getIC2Item("copperCableItem", 3L), 100, 2);
            GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.AnnealedCopper, 1L), GT_ModHandler.getIC2Item("copperCableItem", 3L), 100, 2);
            GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 1L), GT_ModHandler.getIC2Item("tinCableItem", 4L), 150, 1);
            GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L), GT_ModHandler.getIC2Item("ironCableItem", 6L), 200, 2);
            GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 1L), GT_ModHandler.getIC2Item("goldCableItem", 6L), 200, 1);
        }
        GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphene, 1L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Graphene, 1L), 400, 2);
        if (!GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.disabledrecipes, "torchesFromCoal", false)) {
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), new ItemStack(Items.coal, 1, 32767), new ItemStack(Blocks.torch, 4), 400, 1);
        }
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 6L), GT_Utility.getIntegratedCircuit(6), new ItemStack(Items.iron_door, 1), 600, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 7L), GT_Utility.getIntegratedCircuit(7), new ItemStack(Items.cauldron, 1), 700, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 1L), GT_Utility.getIntegratedCircuit(1), GT_ModHandler.getIC2Item("ironFence", 1L), 100, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 3L), GT_Utility.getIntegratedCircuit(3), new ItemStack(Blocks.iron_bars, 4), 300, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 3L), GT_Utility.getIntegratedCircuit(3), new ItemStack(Blocks.fence, 1), 300, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Iron, 2L), new ItemStack(Blocks.tripwire_hook, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 3L), new ItemStack(Items.string, 3, 32767), new ItemStack(Items.bow, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 3L), ItemList.Component_Minecart_Wheels_Iron.get(2L), new ItemStack(Items.minecart, 1), 500, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 3L), ItemList.Component_Minecart_Wheels_Steel.get(2L), new ItemStack(Items.minecart, 1), 300, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 1L), GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Iron, 2L), ItemList.Component_Minecart_Wheels_Iron.get(1L), 500, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 1L), GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Steel, 2L), ItemList.Component_Minecart_Wheels_Steel.get(1L), 300, 2);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Items.minecart, 1), new ItemStack(Blocks.hopper, 1, 32767), new ItemStack(Items.hopper_minecart, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Items.minecart, 1), new ItemStack(Blocks.tnt, 1, 32767), new ItemStack(Items.tnt_minecart, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Items.minecart, 1), new ItemStack(Blocks.chest, 1, 32767), new ItemStack(Items.chest_minecart, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Items.minecart, 1), new ItemStack(Blocks.trapped_chest, 1, 32767), new ItemStack(Items.chest_minecart, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Items.minecart, 1), new ItemStack(Blocks.furnace, 1, 32767), new ItemStack(Items.furnace_minecart, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.tripwire_hook, 1), new ItemStack(Blocks.chest, 1, 32767), new ItemStack(Blocks.trapped_chest, 1), 200, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.stone, 1, 0), GT_Utility.getIntegratedCircuit(4), new ItemStack(Blocks.stonebrick, 1, 0), 50, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.sandstone, 1, 0), GT_Utility.getIntegratedCircuit(1), new ItemStack(Blocks.sandstone, 1, 2), 50, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.sandstone, 1, 1), GT_Utility.getIntegratedCircuit(1), new ItemStack(Blocks.sandstone, 1, 0), 50, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.sandstone, 1, 2), GT_Utility.getIntegratedCircuit(1), new ItemStack(Blocks.sandstone, 1, 0), 50, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 8L), GT_Utility.getIntegratedCircuit(8), GT_ModHandler.getIC2Item("machine", 1L), 25, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 6L), GT_Utility.getIntegratedCircuit(6), ItemList.Casing_ULV.get(1L), 25, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 6L), GT_Utility.getIntegratedCircuit(6), ItemList.Casing_LV.get(1L), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, StainlessSteel, 16L), GT_Utility.getIntegratedCircuit(8), ItemList.Casing_MV.get(1L), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Aluminium, 32L), GT_Utility.getIntegratedCircuit(8), ItemList.Casing_HV.get(1L), 50, 16);

        if (GT_Mod.gregtechproxy.mHardMachineCasings) {
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Lead, 2L), ItemList.Casing_ULV.get(1L), Materials.Plastic.getMolten(288), ItemList.Hull_ULV.get(1L), 25, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 2L), ItemList.Casing_LV.get(1L), Materials.Plastic.getMolten(288), ItemList.Hull_LV.get(1L), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Copper, 2L), ItemList.Casing_MV.get(1L), Materials.Plastic.getMolten(288), ItemList.Hull_MV.get(1L), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 2L), ItemList.Casing_HV.get(1L), Materials.Plastic.getMolten(288), ItemList.Hull_HV.get(1L), 50, 16);
        } else {
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Lead, 2L), ItemList.Casing_ULV.get(1L),ItemList.Hull_ULV.get(1L), 25, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 2L), ItemList.Casing_LV.get(1L),ItemList.Hull_LV.get(1L), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Copper, 2L), ItemList.Casing_MV.get(1L),ItemList.Hull_MV.get(1L), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 2L), ItemList.Casing_HV.get(1L), ItemList.Hull_HV.get(1L), 50, 16);
        }
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 1L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 1L), Materials.Plastic.getMolten(144), ItemList.Battery_Hull_LV.get(1L), 800, 1);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Copper, 2L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 3L), Materials.Plastic.getMolten(432), ItemList.Battery_Hull_MV.get(1L), 1600, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 4L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 9L), Materials.Plastic.getMolten(1296), ItemList.Battery_Hull_HV.get(1L), 3200, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Items.string, 4, 32767), new ItemStack(Items.slime_ball, 1, 32767), new ItemStack(Items.lead, 2), 200, 2);
        GT_Values.RA.addAssemblerRecipe(ItemList.IC2_Compressed_Coal_Ball.get(8L), new ItemStack(Blocks.brick_block, 1), ItemList.IC2_Compressed_Coal_Chunk.get(1L), 400, 4);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getIC2Item("carbonFiber", 2L), GT_Utility.getIntegratedCircuit(2), GT_ModHandler.getIC2Item("carbonMesh", 1L), 800, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1L), new ItemStack(Items.blaze_powder, 1, 0), new ItemStack(Items.ender_eye, 1, 0), 400, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 6L), new ItemStack(Items.blaze_rod, 1, 0), new ItemStack(Items.ender_eye, 6, 0), 2500, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.gear, Materials.CobaltBrass, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L), ItemList.Component_Sawblade_Diamond.get(1L), 1600, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 4L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 4L), new ItemStack(Blocks.redstone_lamp, 1), 400, 1);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), new ItemStack(Blocks.redstone_torch, 1), 400, 1);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 4L), new ItemStack(Items.compass, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 4L), new ItemStack(Items.clock, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), new ItemStack(Blocks.torch, 2), 400, 1);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1L), new ItemStack(Blocks.torch, 6), 400, 1);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), ItemList.IC2_Resin.get(1L), new ItemStack(Blocks.torch, 6), 400, 1);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 8L), new ItemStack(Items.flint, 1), ItemList.IC2_Compressed_Coal_Ball.get(1L), 400, 4);

        if (!GT_Mod.gregtechproxy.mDisableIC2Cables) {
            GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getIC2Item("tinCableItem", 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 1L), GT_ModHandler.getIC2Item("insulatedTinCableItem", 1L), 100, 2);
            GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getIC2Item("copperCableItem", 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 1L), GT_ModHandler.getIC2Item("insulatedCopperCableItem", 1L), 100, 2);
            GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getIC2Item("goldCableItem", 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 2L), GT_ModHandler.getIC2Item("insulatedGoldCableItem", 1L), 200, 2);
            GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getIC2Item("ironCableItem", 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 3L), GT_ModHandler.getIC2Item("insulatedIronCableItem", 1L), 300, 2);
        }

        GT_ModHandler.removeRecipe(new ItemStack(Items.lava_bucket), ItemList.Cell_Empty.get(1L));
        GT_ModHandler.removeRecipe(new ItemStack(Items.water_bucket), ItemList.Cell_Empty.get(1L));

        GT_ModHandler.removeFurnaceSmelting(ItemList.IC2_Resin.get(1L));
        if(!GregTech_API.mIC2Classic)
        GT_Values.RA.addPyrolyseRecipe(GT_ModHandler.getIC2Item("biochaff", 4L), Materials.Water.getFluid(4000), 1, GT_Values.NI, new FluidStack(FluidRegistry.getFluid("ic2biomass"), 5000), 900, 10);
        if(Loader.isModLoaded(MOD_ID_FR)) {
            GT_Values.RA.addPyrolyseRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "fertilizerBio", 4L), Materials.Water.getFluid(4000), 1, GT_Values.NI, Materials.Biomass.getFluid(5000), 900, 10);
            GT_Values.RA.addPyrolyseRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextForestry, "mulch", 32L), Materials.Water.getFluid(4000), 1, GT_Values.NI, Materials.Biomass.getFluid(5000), 900, 10);
        }

        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Blocks.sand, 1, 1), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Diamond, 1L), new ItemStack(Blocks.sand, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 100, 5000}, 600, 120);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Blocks.dirt, 1, 32767), GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.IC2_Plantball.get(1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Clay, 1L), new ItemStack(Blocks.sand, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{1250, 5000, 5000}, 250, 30);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Blocks.grass, 1, 32767), GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.IC2_Plantball.get(1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Clay, 1L), new ItemStack(Blocks.sand, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{2500, 5000, 5000}, 250, 30);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Blocks.mycelium, 1, 32767), GT_Values.NI, GT_Values.NF, GT_Values.NF, new ItemStack(Blocks.brown_mushroom, 1), new ItemStack(Blocks.red_mushroom, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Clay, 1L), new ItemStack(Blocks.sand, 1), GT_Values.NI, GT_Values.NI, new int[]{2500, 2500, 5000, 5000}, 650, 30);
        GT_Values.RA.addCentrifugeRecipe(ItemList.IC2_Resin.get(1L), GT_Values.NI, GT_Values.NF, Materials.Glue.getFluid(100L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 3L), ItemList.IC2_Plantball.get(1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{10000, 1000}, 300, 5);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1), 0, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 1L), ItemList.TE_Slag.get(1L, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L)), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, 250);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.magma_cream, 1), 0, new ItemStack(Items.blaze_powder, 1), new ItemStack(Items.slime_ball, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, 500);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Uranium235, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{2000, 200}, 800, 320);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium241, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Uranium, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{2000, 3000}, 1600, 320);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.NaquadahEnriched, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Naquadria, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 1000}, 3200, 320);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahEnriched, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Naquadria, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Naquadah, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{2000, 3000}, 6400, 640);
        GT_Values.RA.addCentrifugeRecipe(GT_Values.NI, GT_Values.NI, Materials.Hydrogen.getGas(160L), Materials.Deuterium.getGas(40L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 160, 20);
        GT_Values.RA.addCentrifugeRecipe(GT_Values.NI, GT_Values.NI, Materials.Deuterium.getGas(160L), Materials.Tritium.getGas(40L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 160, 80);
        GT_Values.RA.addCentrifugeRecipe(GT_Values.NI, GT_Values.NI, Materials.Helium.getGas(80L), Materials.Helium_3.getGas(5L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 160, 80);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 2L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 600, 24);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Endstone, 9L), GT_Values.NI, GT_Values.NF, Materials.Helium.getGas(50L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tungstate, 2L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Platinum, 1L), new ItemStack(Blocks.sand, 6), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{2000, 2000, 9000, 0, 0, 0}, 600, 112);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Netherrack, 9L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Glowstone, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1L), GT_Values.NI, GT_Values.NI, new int[]{2000, 2000, 2000, 1000, 0, 0}, 400, 24);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Blocks.soul_sand, 4), GT_Values.NI, GT_Values.NF, Materials.Oil.getFluid(80L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L), new ItemStack(Blocks.sand, 4), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{8000, 2000, 9000, 0, 0, 0}, 200, 80);
        GT_Values.RA.addCentrifugeRecipe(GT_Utility.getIntegratedCircuit(10),GT_Values.NI,Materials.Lava.getFluid(100L),GT_Values.NF,Materials.SiliconDioxide.getDustSmall(1),Materials.Magnesia.getDustSmall(1),Materials.Quicklime.getDustSmall(1),Materials.Gold.getNuggets(1),Materials.Sapphire.getDustSmall(1),Materials.Tantalite.getDustSmall(1),new int[]{5000,1000,1000,250,1250,500},80,80);
        GT_Values.RA.addCentrifugeRecipe(GT_Utility.getIntegratedCircuit(10), GT_Values.NI, FluidRegistry.getFluidStack("ic2pahoehoelava", 100), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Copper, 1L), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Tin, 1L), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Silver, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Phosphorus, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Scheelite, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Bauxite, 1L), new int[]{2000, 1000, 250, 50, 250, 500}, 40, 1024);

        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RareEarth, 4L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Neodymium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Yttrium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lanthanum, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cerium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cadmium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 1L), new int[]{2500, 2500, 2500, 2500, 2500, 2500}, 64, 20);
        GT_Values.RA.addCentrifugeRecipe(GT_ModHandler.getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 4L, 45), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BasalticMineralSand, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Olivine, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Obsidian, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Basalt, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Flint, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RareEarth, 1L), new int[]{2000, 2000, 2000, 2000, 2000, 2000}, 64, 20);

        //Stone Dust and Metal Mixture centrifuge recipes
        GT_Values.RA.addCentrifugeRecipe(Materials.Stone.getDust(36), GT_Values.NI, GT_Values.NF, GT_Values.NF,
                Materials.Quartzite.getDust(9), Materials.PotassiumFeldspar.getDust(9), Materials.Marble.getDust(8),
                Materials.Biotite.getDust(4), Materials.MetalMixture.getDust(4), Materials.Sodalite.getDust(4),
                new int[]{10000, 10000, 10000, 10000, 7500, 5000}, 3600, 24);
        GT_Values.RA.addCentrifugeRecipe(Materials.MetalMixture.getDust(11), GT_Values.NI, GT_Values.NF, GT_Values.NF,
                Pyrite.getDust(4), Tetrahedrite.getDust(2), Materials.Pyrolusite.getDust(2),
                Materials.Barite.getDust(1), Materials.Chromite.getDust(1), Materials.Ilmenite.getDust(1),
                new int[]{10000, 10000, 10000, 10000, 7500, 5000}, 1200, 112);

        this.run3();

        GT_Utility.removeSimpleIC2MachineRecipe(new ItemStack(Blocks.cobblestone), GT_ModHandler.getMaceratorRecipeList(), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L));
        GT_Utility.removeSimpleIC2MachineRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Lapis, 1L), GT_ModHandler.getMaceratorRecipeList(), ItemList.IC2_Plantball.get(1L));
        GT_Utility.removeSimpleIC2MachineRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), GT_ModHandler.getMaceratorRecipeList(), ItemList.IC2_Plantball.get(1L));
        GT_Utility.removeSimpleIC2MachineRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L), GT_ModHandler.getMaceratorRecipeList(), ItemList.IC2_Plantball.get(1L));
		GT_Utility.removeSimpleIC2MachineRecipe(GT_Values.NI, GT_ModHandler.getMaceratorRecipeList(), GT_ModHandler.getModItem("IC2", "itemBiochaff", 1L));
		
        GT_Utility.removeSimpleIC2MachineRecipe(new ItemStack(Blocks.cactus, 8, 0), GT_ModHandler.getCompressorRecipeList(), GT_ModHandler.getModItem("IC2", "itemFuelPlantBall", 1L));
        GT_Utility.removeSimpleIC2MachineRecipe(GT_ModHandler.getModItem("ExtraTrees", "food", 8L, 24), GT_ModHandler.getCompressorRecipeList(), GT_ModHandler.getModItem("IC2", "itemFuelPlantBall", 1L));
        {
            ItemStack alu = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Aluminium, 2);
            GT_ModHandler.addAlloySmelterRecipe(Materials.Magnesium.getDust(1),alu,Materials.Magnalium.getIngots(3) ,100,120,false);
            GT_ModHandler.addAlloySmelterRecipe(Materials.Magnesium.getIngots(1),alu,Materials.Magnalium.getIngots(3) ,100,120,false);
        }


        for (MaterialStack[] tMats : mAlloySmelterList) {
            ItemStack tDust1 = GT_OreDictUnificator.get(OrePrefixes.dust, tMats[0].mMaterial, tMats[0].mAmount);
            ItemStack tDust2 = GT_OreDictUnificator.get(OrePrefixes.dust, tMats[1].mMaterial, tMats[1].mAmount);
            ItemStack tIngot1 = GT_OreDictUnificator.get(OrePrefixes.ingot, tMats[0].mMaterial, tMats[0].mAmount);
            ItemStack tIngot2 = GT_OreDictUnificator.get(OrePrefixes.ingot, tMats[1].mMaterial, tMats[1].mAmount);
            ItemStack tOutputIngot = GT_OreDictUnificator.get(OrePrefixes.ingot, tMats[2].mMaterial, tMats[2].mAmount);
            if (tOutputIngot != GT_Values.NI) {
                GT_ModHandler.addAlloySmelterRecipe(tIngot1, tDust2, tOutputIngot, (int) tMats[2].mAmount * 50, 16, false);
                GT_ModHandler.addAlloySmelterRecipe(tIngot1, tIngot2, tOutputIngot, (int) tMats[2].mAmount * 50, 16, false);
                GT_ModHandler.addAlloySmelterRecipe(tDust1, tIngot2, tOutputIngot, (int) tMats[2].mAmount * 50, 16, false);
                GT_ModHandler.addAlloySmelterRecipe(tDust1, tDust2, tOutputIngot, (int) tMats[2].mAmount * 50, 16, false);
            }
        }

        if(!GregTech_API.mIC2Classic){
            try {
                Map<String, ILiquidHeatExchangerManager.HeatExchangeProperty> tLiqExchange = ic2.api.recipe.Recipes.liquidCooldownManager.getHeatExchangeProperties();
                Iterator<Map.Entry<String, ILiquidHeatExchangerManager.HeatExchangeProperty>> tIterator = tLiqExchange.entrySet().iterator();
                while (tIterator.hasNext()) {
                    Map.Entry<String, ILiquidHeatExchangerManager.HeatExchangeProperty> tEntry = tIterator.next();
                    if(tEntry.getKey().equals("ic2hotcoolant")){
                    	tIterator.remove();
                    	Recipes.liquidCooldownManager.addFluid("ic2hotcoolant", "ic2coolant", 100);
                    }
                }
            } catch (Throwable e) {/*Do nothing*/}

            try {
                Map<String, ILiquidHeatExchangerManager.HeatExchangeProperty> tLiqExchange = ic2.api.recipe.Recipes.liquidHeatupManager.getHeatExchangeProperties();
                Iterator<Map.Entry<String, ILiquidHeatExchangerManager.HeatExchangeProperty>> tIterator = tLiqExchange.entrySet().iterator();
                while (tIterator.hasNext()) {
                    Map.Entry<String, ILiquidHeatExchangerManager.HeatExchangeProperty> tEntry = tIterator.next();
                    if(tEntry.getKey().equals("ic2coolant")){
                    	tIterator.remove();
                    	Recipes.liquidHeatupManager.addFluid("ic2coolant", "ic2hotcoolant", 100);
                    }
                }
            } catch (Throwable e) {/*Do nothing*/}
        }
        GT_Utility.removeSimpleIC2MachineRecipe(ItemList.Crop_Drop_BobsYerUncleRanks.get(1L), GT_ModHandler.getExtractorRecipeList(), null);
        GT_Utility.removeSimpleIC2MachineRecipe(ItemList.Crop_Drop_Ferru.get(1L), GT_ModHandler.getExtractorRecipeList(), null);
        GT_Utility.removeSimpleIC2MachineRecipe(ItemList.Crop_Drop_Aurelia.get(1L), GT_ModHandler.getExtractorRecipeList(), null);

		ItemStack tCrop;
	    // Metals Line
		tCrop = ItemList.Crop_Drop_Coppon.get(1);
        this.addProcess(tCrop, Materials.Copper, 100, true);
        this.addProcess(tCrop, Materials.Tetrahedrite, 100, false);
        this.addProcess(tCrop, Materials.Chalcopyrite, 100, false);
        this.addProcess(tCrop, Materials.Malachite, 100, false);
        this.addProcess(tCrop, Materials.Pyrite, 100, false);
        this.addProcess(tCrop, Materials.Stibnite, 100, false);
		tCrop = ItemList.Crop_Drop_Tine.get(1);
        this.addProcess(tCrop, Materials.Tin, 100, true);
        this.addProcess(tCrop, Materials.Cassiterite, 100, false);
        this.addProcess(tCrop, Materials.CassiteriteSand, 100, true);
		tCrop = ItemList.Crop_Drop_Plumbilia.get(1);
        this.addProcess(tCrop, Materials.Lead, 100, true);
        this.addProcess(tCrop, Materials.Galena, 100, false);//
        tCrop = ItemList.Crop_Drop_Ferru.get(1);
        this.addProcess(tCrop, Materials.Iron, 100, true);
        this.addProcess(tCrop, Materials.Magnetite, 100, false);
        this.addProcess(tCrop, Materials.BrownLimonite, 100, false);
        this.addProcess(tCrop, Materials.YellowLimonite, 100, false);
        this.addProcess(tCrop, Materials.VanadiumMagnetite, 100, false);
        this.addProcess(tCrop, Materials.BandedIron, 100, false);
        this.addProcess(tCrop, Materials.Pyrite, 100, false);
		tCrop = ItemList.Crop_Drop_Nickel.get(1);
        this.addProcess(tCrop, Materials.Nickel, 100, true);
        this.addProcess(tCrop, Materials.Garnierite, 100, false);
        this.addProcess(tCrop, Materials.Pentlandite, 100, false);
        this.addProcess(tCrop, Materials.Cobaltite, 100, false);
        this.addProcess(tCrop, Materials.Wulfenite, 100, false);
        this.addProcess(tCrop, Materials.Powellite, 100, false);
		tCrop = ItemList.Crop_Drop_Zinc.get(1);
        this.addProcess(tCrop, Materials.Zinc, 100, true);
        this.addProcess(tCrop, Materials.Sphalerite, 100, false);
        this.addProcess(tCrop, Materials.Sulfur, 100, false);
		tCrop = ItemList.Crop_Drop_Argentia.get(1);
        this.addProcess(tCrop, Materials.Silver, 100, true);
        this.addProcess(tCrop, Materials.Galena, 100, false);
		tCrop = ItemList.Crop_Drop_Aurelia.get(1);
        this.addProcess(tCrop, Materials.Gold, 100, true);
        this.addProcess(tCrop, Materials.Magnetite, Materials.Gold, 100, false);
        tCrop = ItemList.Crop_Drop_Mica.get(1);
        this.addProcess(tCrop,Materials.Mica,75, true);

	    // Rare Metals Line
		tCrop = ItemList.Crop_Drop_Bauxite.get(1);
        this.addProcess(tCrop,Materials.Aluminium,60, true);
        this.addProcess(tCrop,Materials.Bauxite,100, false);
		tCrop = ItemList.Crop_Drop_Manganese.get(1);
        this.addProcess(tCrop,Materials.Manganese,30, true);
        this.addProcess(tCrop,Materials.Grossular,100, false);
        this.addProcess(tCrop,Materials.Spessartine,100, false);
        this.addProcess(tCrop,Materials.Pyrolusite,100, false);
        this.addProcess(tCrop,Materials.Tantalite,100, false);
		tCrop = ItemList.Crop_Drop_Ilmenite.get(1);
        this.addProcess(tCrop,Materials.Titanium,100, true);
        this.addProcess(tCrop,Materials.Ilmenite,100, false);
        this.addProcess(tCrop,Materials.Bauxite,100, false);
        this.addProcess(tCrop,Materials.Rutile,100, false);
		tCrop = ItemList.Crop_Drop_Scheelite.get(1);
        this.addProcess(tCrop,Materials.Scheelite,100, true);
        this.addProcess(tCrop,Materials.Tungstate,100, false);
        this.addProcess(tCrop,Materials.Lithium,100, false);
        this.addProcess(tCrop,Materials.Tungsten,75, false);
		tCrop = ItemList.Crop_Drop_Platinum.get(1);
        this.addProcess(tCrop,Materials.Platinum,40, true);
        this.addProcess(tCrop,Materials.Cooperite,40, false);
        this.addProcess(tCrop,Materials.Palladium,40, false);
        this.addProcess(tCrop, Materials.Neodymium, 100, false);
        this.addProcess(tCrop, Materials.Bastnasite, 100, false);
		tCrop = ItemList.Crop_Drop_Iridium.get(1);
        this.addProcess(tCrop,Materials.Iridium,20, true);
		tCrop = ItemList.Crop_Drop_Osmium.get(1);
        this.addProcess(tCrop,Materials.Osmium,20, true);

	    // Radioactive Line
        tCrop = ItemList.Crop_Drop_Pitchblende.get(1);
        this.addProcess(tCrop,Materials.Pitchblende,50, true);
		tCrop = ItemList.Crop_Drop_Uraninite.get(1);
        this.addProcess(tCrop,Materials.Uraninite,50, false);
        this.addProcess(tCrop,Materials.Uranium,50, true);
        this.addProcess(tCrop,Materials.Pitchblende,50, false);
        this.addProcess(tCrop,Materials.Uranium235,50, false);
		tCrop = ItemList.Crop_Drop_Thorium.get(1);
        this.addProcess(tCrop,Materials.Thorium,50, true);
		tCrop = ItemList.Crop_Drop_Naquadah.get(1);
        this.addProcess(tCrop,Materials.Naquadah,10, true);
        this.addProcess(tCrop,Materials.NaquadahEnriched,10, false);
        this.addProcess(tCrop,Materials.Naquadria,10, false);

		//Gem Line
		tCrop = ItemList.Crop_Drop_BobsYerUncleRanks.get(1);
        this.addProcess(tCrop, Materials.Emerald, 100, true);
        this.addProcess(tCrop, Materials.Beryllium, 100, false);

        this.addRecipesApril2017ChemistryUpdate();
        GT_Values.RA.addChemicalRecipe(Materials.Sodium.getDust(2), Materials.Sulfur.getDust(1), Materials.SodiumSulfide.getDust(1), 60);
        GT_Values.RA.addChemicalRecipe(Materials.HydricSulfide.getCells(1), GT_Values.NI, Materials.Water.getFluid(1000), Materials.DilutedSulfuricAcid.getFluid(750), Materials.Empty.getCells(1), 60);
        GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(1), GT_Values.NI, Materials.HydricSulfide.getGas(1000), Materials.DilutedSulfuricAcid.getFluid(750), Materials.Empty.getCells(1), 60);
        GT_Values.RA.addAutoclaveRecipe(Materials.SiliconDioxide.getDust(1), Materials.Water.getFluid(200L), Materials.Quartzite.getGems(1), 750,  2000, 24);
        GT_Values.RA.addAutoclaveRecipe(Materials.SiliconDioxide.getDust(1), GT_ModHandler.getDistilledWater(100L), Materials.Quartzite.getGems(1), 1000, 1500, 24);
        GT_Values.RA.addAutoclaveRecipe(Materials.SiliconDioxide.getDust(1), Materials.Void.getMolten(36L), Materials.Quartzite.getGems(1), 10000, 1000, 24);

        this.addRecipesMay2017OilRefining();
//        this.addPyrometallurgicalRecipes();
        this.addPolybenzimidazoleRecipes();

        loadRailcraftRecipes();
    }

    public void addProcess(ItemStack tCrop, Materials aMaterial, int chance, boolean aMainOutput) {
    	if(tCrop==null||aMaterial==null||GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial,1)==null)return;
        if (GT_Mod.gregtechproxy.mNerfedCrops) {
            GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tCrop), GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1), Materials.Water.getFluid(1000), aMaterial.mOreByProducts.isEmpty() ? null : aMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4), 96, 24);
            GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tCrop), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass()+9)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 384);
        } else {
            if (aMainOutput) GT_ModHandler.addExtractionRecipe(tCrop, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1));
        }
    }

    public void addProcess(ItemStack tCrop, Materials aMaterial, int chance){
    	if(tCrop==null||aMaterial==null||GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial,1)==null)return;
        if (GT_Mod.gregtechproxy.mNerfedCrops) {
            GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tCrop), GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1), Materials.Water.getFluid(1000), aMaterial.mOreByProducts.isEmpty() ? null : aMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4), 96, 24);
            GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tCrop), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass()+9)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 384);
        } else {
            GT_ModHandler.addExtractionRecipe(tCrop, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1));
        }
    }

    public void addProcess(ItemStack tCrop, Materials aMaterial, Materials aMaterialOut, int chance, boolean aMainOutput){
    	if(tCrop==null||aMaterial==null||GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial,1)==null)return;
        if (GT_Mod.gregtechproxy.mNerfedCrops) {
            GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tCrop), GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1), Materials.Water.getFluid(1000), aMaterialOut.mOreByProducts.isEmpty() ? null : aMaterialOut.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterialOut, 4), 96, 24);
            GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tCrop), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass()+9)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 384);
        } else {
            if (aMainOutput) GT_ModHandler.addExtractionRecipe(tCrop, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1));
        }
    }

    public void addProcess(ItemStack tCrop, Materials aMaterial, Materials aMaterialOut, int chance){
    	if(tCrop==null||aMaterial==null||GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial,1)==null)return;
        if (GT_Mod.gregtechproxy.mNerfedCrops) {
            GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tCrop), GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1), Materials.Water.getFluid(1000), aMaterialOut.mOreByProducts.isEmpty() ? null : aMaterialOut.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterialOut, 4), 96, 24);
            GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tCrop), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass()+9)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 384);
        } else {
            GT_ModHandler.addExtractionRecipe(tCrop, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1));
        }
    }

	private void run3(){
        if (GregTech_API.sThaumcraftCompat != null) {
                String tKey = "GT_WOOD_TO_CHARCOAL";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey, "You have discovered a way of making charcoal magically instead of using regular ovens for this purpose.<BR><BR>To create charcoal from wood you first need an air-free environment, some vacuus essentia is needed for that, then you need to incinerate the wood using ignis essentia and wait until all the water inside the wood is burned away.<BR><BR>This method however doesn't create creosote oil as byproduct.");

                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Charcoal Transmutation", "Turning wood into charcoal", new String[]{"ALUMENTUM"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 1L), 2, 0, 13, 5, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 10L), new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 8L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 8L)), null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey, 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.log.get(Materials.Wood), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L)))});

                tKey = "GT_FILL_WATER_BUCKET";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey, "You have discovered a way of filling a bucket with aqua essentia in order to simply get water.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Water Transmutation", "Filling buckets with water", null, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Water, 1L), 2, 0, 16, 5, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 4L), new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L)), null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey, 
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Empty, 1L), GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Water, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L))),
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, GT_OreDictUnificator.get(OrePrefixes.bucketClay, Materials.Empty, 1L), GT_OreDictUnificator.get(OrePrefixes.bucketClay, Materials.Water, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L))),
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, GT_OreDictUnificator.get(OrePrefixes.capsule, Materials.Empty, 1L), GT_OreDictUnificator.get(OrePrefixes.capsule, Materials.Water, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L))),
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L)))});

                tKey = "GT_TRANSZINC";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey, "You have discovered a way to multiply zinc by steeping zinc nuggets in metallum harvested from other metals.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Zinc Transmutation", "Transformation of metals into zinc", new String[]{"TRANSTIN"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Zinc, 1L), 2, 1, 9, 13, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.SANO, 3L)), null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey, 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Zinc), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Zinc, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.SANO, 1L)))});

                tKey = "GT_TRANSANTIMONY";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey, "You have discovered a way to multiply antimony by steeping antimony nuggets in metallum harvested from other metals.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Antimony Transmutation", "Transformation of metals into antimony", new String[]{"GT_TRANSZINC", "TRANSLEAD"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Antimony, 1L), 2, 1, 9, 14, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 3L)), null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey, 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Antimony), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Antimony, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L)))});

                tKey = "GT_TRANSNICKEL";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey, "You have discovered a way to multiply nickel by steeping nickel nuggets in metallum harvested from other metals.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Nickel Transmutation", "Transformation of metals into nickel", new String[]{"TRANSLEAD"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Nickel, 1L), 2, 1, 9, 15, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)), null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey, 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Nickel), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Nickel, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L)))});

                tKey = "GT_TRANSCOBALT";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey, "You have discovered a way to multiply cobalt by steeping cobalt nuggets in metallum harvested from other metals.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Cobalt Transmutation", "Transformation of metals into cobalt", new String[]{"GT_TRANSNICKEL"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Cobalt, 1L), 2, 1, 9, 16, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 3L)), null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey, 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Cobalt), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Cobalt, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1L)))});

                tKey = "GT_TRANSBISMUTH";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey, "You have discovered a way to multiply bismuth by steeping bismuth nuggets in metallum harvested from other metals.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Bismuth Transmutation", "Transformation of metals into bismuth", new String[]{"GT_TRANSCOBALT"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Bismuth, 1L), 2, 1, 11, 17, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 3L)), null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey, 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Bismuth), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Bismuth, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1L)))});

                tKey = "GT_IRON_TO_STEEL";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey, "You have discovered a way of making Iron harder by just re-ordering its components.<BR><BR>This Method can be used to create a Material called Steel, which is used in many non-Thaumaturgic applications.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Steel Transmutation", "Transforming iron to steel", new String[]{"TRANSIRON", "GT_WOOD_TO_CHARCOAL"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Steel, 1L), 3, 0, 13, 8, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3L)), null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey, 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Iron), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Steel, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1L)))});

                tKey = "GT_TRANSBRONZE";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey, "You have discovered a way of creating Alloys using the already known transmutations of Copper and Tin.<BR><BR>This Method can be used to create a Bronze directly without having to go through an alloying process.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Bronze Transmutation", "Transformation of metals into bronze", new String[]{"TRANSTIN", "TRANSCOPPER"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Bronze, 1L), 2, 0, 13, 11, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 3L)), null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey, 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Bronze), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Bronze, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1L)))});

                tKey = "GT_TRANSELECTRUM";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey, "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Electrum as well.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Electrum Transmutation", "Transformation of metals into electrum", new String[]{"GT_TRANSBRONZE", "TRANSGOLD", "TRANSSILVER"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Electrum, 1L), 2, 1, 11, 11, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 3L)), null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey, 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Electrum), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Electrum, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 1L)))});

                tKey = "GT_TRANSBRASS";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey, "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Brass as well.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Brass Transmutation", "Transformation of metals into brass", new String[]{"GT_TRANSBRONZE", "GT_TRANSZINC"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Brass, 1L), 2, 1, 11, 12, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 3L)), null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey, 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Brass), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Brass, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1L)))});

                tKey = "GT_TRANSINVAR";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey, "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Invar as well.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Invar Transmutation", "Transformation of metals into invar", new String[]{"GT_TRANSBRONZE", "GT_TRANSNICKEL"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Invar, 1L), 2, 1, 11, 15, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.GELUM, 3L)), null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey, 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Invar), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Invar, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.GELUM, 1L)))});

                tKey = "GT_TRANSCUPRONICKEL";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey, "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Cupronickel as well.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Cupronickel Transmutation", "Transformation of metals into cupronickel", new String[]{"GT_TRANSBRONZE", "GT_TRANSNICKEL"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Cupronickel, 1L), 2, 1, 11, 16, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)), null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey, 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Cupronickel), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Cupronickel, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 1L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L)))});

                tKey = "GT_TRANSBATTERYALLOY";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey, "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Battery Alloy as well.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Battery Alloy Transmutation", "Transformation of metals into battery alloy", new String[]{"GT_TRANSBRONZE", "GT_TRANSANTIMONY"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.BatteryAlloy, 1L), 2, 1, 11, 13, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)), null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey, 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.BatteryAlloy), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.BatteryAlloy, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L), new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1L)))});

                tKey = "GT_TRANSSOLDERINGALLOY";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey, "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Soldering Alloy as well.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Soldering Alloy Transmutation", "Transformation of metals into soldering alloy", new String[]{"GT_TRANSBRONZE", "GT_TRANSANTIMONY"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.SolderingAlloy, 1L), 2, 1, 11, 14, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)), null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey, 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.SolderingAlloy), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.SolderingAlloy, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L), new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 1L)))});

                tKey = "GT_ADVANCEDMETALLURGY";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey, "Now that you have discovered all the basic metals, you can finally move on to the next Level of magic metallurgy and create more advanced metals");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Advanced Metallurgic Transmutation", "Mastering the basic metals", new String[]{"GT_TRANSBISMUTH", "GT_IRON_TO_STEEL", "GT_TRANSSOLDERINGALLOY", "GT_TRANSBATTERYALLOY", "GT_TRANSBRASS", "GT_TRANSELECTRUM", "GT_TRANSCUPRONICKEL", "GT_TRANSINVAR"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L), 3, 0, 16, 14, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 50L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 20L), new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 20L), new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 20L), new TC_Aspects.TC_AspectStack(TC_Aspects.NEBRISUM, 20L), new TC_Aspects.TC_AspectStack(TC_Aspects.MAGNETO, 20L)), null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey});

                tKey = "GT_TRANSALUMINIUM";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey, "You have discovered a way to multiply aluminium by steeping aluminium nuggets in metallum harvested from other metals.<BR><BR>This transmutation is slightly harder to achieve, because aluminium has special properties, which require more order to achieve the desired result.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Aluminium Transmutation", "Transformation of metals into aluminium", new String[]{"GT_ADVANCEDMETALLURGY"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 1L), 4, 0, 19, 14, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.VOLATUS, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)), null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey, 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Aluminium), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.VOLATUS, 1L), new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L)))});

                tKey = "GT_CRYSTALLISATION";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey, "Sometimes when processing your Crystal Shards they become a pile of Dust instead of the mostly required Shard.<BR><BR>You have finally found a way to reverse this Process by using Vitreus Essentia for recrystallising the Shards.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Shard Recrystallisation", "Fixing your precious crystals", new String[]{"ALCHEMICALMANUFACTURE"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedOrder, 1L), 3, 0, -11, -3, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3L)), null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey, 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.dust.get(Materials.Amber), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Amber, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L))), 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.dust.get(Materials.InfusedOrder), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedOrder, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L))), 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.dust.get(Materials.InfusedEntropy), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedEntropy, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L))), 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.dust.get(Materials.InfusedAir), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedAir, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L))), 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.dust.get(Materials.InfusedEarth), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedEarth, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L))), 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.dust.get(Materials.InfusedFire), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedFire, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L))), 
                		GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.dust.get(Materials.InfusedWater), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedWater, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L)))});

                tKey = "GT_MAGICENERGY";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "While trying to find new ways to integrate magic into your industrial factories, you have discovered a way to convert magical energy into electrical power.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey,
                        "Magic Energy Conversion",
                        "Magic to Power",
                        new String[]{"ARCANEBORE"},
                        "ARTIFICE",
                        ItemList.MagicEnergyConverter_LV.get(1L),
                        3, 0, -3, 10,
                        Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                        null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addInfusionRecipe(tKey,
                                        ItemList.Hull_LV.get(1L),
                                        new ItemStack[]{
                                                new ItemStack(Blocks.beacon),
                                                GT_OreDictUnificator.get(circuitPower, PWR_MV, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 1L),
                                                ItemList.Sensor_MV.get(2L),
                                                GT_OreDictUnificator.get(circuitPower, PWR_MV, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L),
                                                ItemList.Sensor_MV.get(2L)
                                        },
                                        ItemList.MagicEnergyConverter_LV.get(1L),
                                        5,
                                        Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L)))});

                tKey = "GT_MAGICENERGY2";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "Attempts to increase the output of your Magic Energy generators have resulted in significant improvements.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey,
                        "Adept Magic Energy Conversion",
                        "Magic to Power",
                        new String[]{"GT_MAGICENERGY"},
                        "ARTIFICE",
                        ItemList.MagicEnergyConverter_MV.get(1L),
                        1, 1, -4, 12,
                        Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                        null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addInfusionRecipe(tKey,
                                        ItemList.Hull_MV.get(1L),
                                        new ItemStack[]{
                                                new ItemStack(Blocks.beacon),
                                                GT_OreDictUnificator.get(circuitPower, PWR_HV, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Thaumium, 1L),
                                                ItemList.Sensor_HV.get(2L),
                                                GT_OreDictUnificator.get(circuitPower, PWR_HV, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.StainlessSteel, 1L),
                                                ItemList.Sensor_HV.get(2L)
                                        },
                                        ItemList.MagicEnergyConverter_MV.get(1L),
                                        6,
                                        Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L)))});

                tKey = "GT_MAGICENERGY3";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "Attempts to further increase the output of your Magic Energy generators have resulted in great improvements.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey,
                        "Master Magic Energy Conversion",
                        "Magic to Power",
                        new String[]{"GT_MAGICENERGY2"},
                        "ARTIFICE",
                        ItemList.MagicEnergyConverter_HV.get(1L),
                        1, 1, -4, 14,
                        Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 40L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 20L)),
                        null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addInfusionRecipe(tKey,
                                        ItemList.Hull_HV.get(1L),
                                        new ItemStack[]{
                                                new ItemStack(Blocks.beacon),
                                                GT_OreDictUnificator.get(circuitPower, PWR_MV, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Thaumium, 1L),
                                                ItemList.Field_Generator_MV.get(1L),
                                                GT_OreDictUnificator.get(circuitPower, PWR_MV, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Titanium, 1L),
                                                ItemList.Field_Generator_MV.get(1L)
                                        },
                                        ItemList.MagicEnergyConverter_HV.get(1L),
                                        8,
                                        Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 128L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L)))});


                tKey = "GT_MAGICABSORB";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "Research into magical energy conversion methods has identified a way to convert surrounding energies into electrical power.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey,
                        "Magic Energy Absorption",
                        "Harvesting Magic",
                        new String[]{"GT_MAGICENERGY"},
                        "ARTIFICE",
                        ItemList.MagicEnergyAbsorber_LV.get(1L),
                        3, 0, -2, 12,
                        Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                        null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addInfusionRecipe(tKey,
                                        ItemList.Hull_LV.get(1L),
                                        new ItemStack[]{
                                                ItemList.MagicEnergyConverter_LV.get(1L),
                                                GT_OreDictUnificator.get(circuitPower, PWR_LV, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L),
                                                ItemList.Sensor_MV.get(2L)
                                        },
                                        ItemList.MagicEnergyAbsorber_LV.get(1L),
                                        6,
                                        Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 16L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 4L)))});

                tKey = "GT_MAGICABSORB2";
                GT_LanguageManager.addStringLocalization(GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "Moar output! Drain all the Magic!");
                GregTech_API.sThaumcraftCompat.addResearch(tKey,
                        "Improved Magic Energy Absorption",
                        "Harvesting Magic",
                        new String[]{"GT_MAGICABSORB"},
                        "ARTIFICE",
                        ItemList.MagicEnergyAbsorber_EV.get(1L),
                        3, 1, -2, 14,
                        Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                        null, new Object[]{GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addInfusionRecipe(tKey,
                                        ItemList.Hull_MV.get(1L),
                                        new ItemStack[]{
                                                ItemList.MagicEnergyConverter_MV.get(1L),
                                                GT_OreDictUnificator.get(circuitPower, PWR_MV, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L),
                                                ItemList.Sensor_HV.get(2L),
                                                GT_OreDictUnificator.get(circuitPower, PWR_MV, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L)
                                        },
                                        ItemList.MagicEnergyAbsorber_MV.get(1L),
                                        6,
                                        Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 64L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 8L)))


                                , GregTech_API.sThaumcraftCompat.addInfusionRecipe(tKey,
                                ItemList.Hull_HV.get(1L),
                                new ItemStack[]{
                                        ItemList.MagicEnergyConverter_MV.get(1L),
                                        GT_OreDictUnificator.get(circuitLogic, LOGIC_MV, 1L),
                                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Void, 1),
                                        ItemList.Field_Generator_MV.get(1L),
                                        GT_OreDictUnificator.get(circuitLogic, LOGIC_MV, 1L),
                                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Void, 1),
                                },
                                ItemList.MagicEnergyAbsorber_HV.get(1L),
                                8,
                                Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 128L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 64L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 128L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 16L)))


                                , GregTech_API.sThaumcraftCompat.addInfusionRecipe(tKey,
                                ItemList.Hull_EV.get(1L),
                                new ItemStack[]{
                                        ItemList.MagicEnergyConverter_HV.get(1L),
                                        GT_OreDictUnificator.get(circuitLogic, LOGIC_HV, 1L),
                                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Void, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 1),
                                        ItemList.Field_Generator_HV.get(1L),
                                        GT_OreDictUnificator.get(circuitLogic, LOGIC_HV, 1L),
                                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Void, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 1),
                                },
                                ItemList.MagicEnergyAbsorber_EV.get(1L),
                                10,
                                Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 256L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 128L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 256L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 64L)))
                        });
            }
	}

	/**
	 * Adds recipes related to the processing of Charcoal Byproducts, Fermented Biomass
	 * Adds recipes related to the production of Glue, Gunpowder, Polyvinyl Chloride
	 * Adds replacement recipes for Epoxy Resin, Nitric Acid, Polyethylene, Polydimethylsiloxane (Silicone), Polytetrafluoroethylene, Rocket Fuel, Sulfuric Acid
	 * Instrumental materials are not mentioned here.
	 */
	private void addRecipesApril2017ChemistryUpdate(){
        GT_Values.RA.addElectrolyzerRecipe(GT_Utility.getIntegratedCircuit(1),  GT_Values.NI,                Materials.CarbonDioxide.getGas(1000), Materials.Oxygen.getGas(2000), Materials.Carbon.getDust(1), GT_Values.NI,                 GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 300, 120);
        GT_Values.RA.addElectrolyzerRecipe(GT_Utility.getIntegratedCircuit(11), Materials.Empty.getCells(2), Materials.CarbonDioxide.getGas(1000), GT_Values.NF,                  Materials.Carbon.getDust(1), Materials.Oxygen.getCells(2), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 300, 120);
        GT_Values.RA.addElectrolyzerRecipe(GT_Utility.getIntegratedCircuit(1),  GT_Values.NI,                Materials.SulfurDioxide.getGas(1000), Materials.Oxygen.getGas(2000), Materials.Sulfur.getDust(1), GT_Values.NI,                 GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 300, 120);
        GT_Values.RA.addElectrolyzerRecipe(GT_Utility.getIntegratedCircuit(11), Materials.Empty.getCells(2), Materials.SulfurDioxide.getGas(1000), GT_Values.NF,                  Materials.Sulfur.getDust(1), Materials.Oxygen.getCells(2), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 300, 120);
        GT_Values.RA.addElectrolyzerRecipe(Materials.Salt.getDust(2),   GT_Values.NI,                        GT_Values.NF,                                     Materials.Chlorine.getGas(1000), Materials.Sodium.getDust(1),          GT_Values.NI,                   GT_Values.NI, GT_Values.NI,GT_Values.NI, GT_Values.NI, null, 320, 30);
		GT_Values.RA.addElectrolyzerRecipe(Materials.Empty.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.SaltWater.getFluid(2000),               Materials.Chlorine.getGas(1000), Materials.SodiumHydroxide.getDust(1), Materials.Hydrogen.getCells(1), GT_Values.NI, GT_Values.NI,GT_Values.NI, GT_Values.NI, null, 300, 24);
		GT_Values.RA.addElectrolyzerRecipe(Materials.Empty.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.SaltWater.getFluid(2000),               Materials.Hydrogen.getGas(1000), Materials.SodiumHydroxide.getDust(1), Materials.Chlorine.getCells(1), GT_Values.NI, GT_Values.NI,GT_Values.NI, GT_Values.NI, null, 300, 24);
		GT_Values.RA.addElectrolyzerRecipe(Materials.Empty.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.HydrochloricAcid.getFluid(1000),        Materials.Chlorine.getGas(1000), Materials.Hydrogen.getCells(1),       GT_Values.NI,                   GT_Values.NI, GT_Values.NI,GT_Values.NI, GT_Values.NI, null, 720, 30);
		GT_Values.RA.addElectrolyzerRecipe(Materials.Empty.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.HydrochloricAcid.getFluid(1000),        Materials.Hydrogen.getGas(1000), Materials.Chlorine.getCells(1),       GT_Values.NI,                   GT_Values.NI, GT_Values.NI,GT_Values.NI, GT_Values.NI, null, 720, 30);
        GT_Values.RA.addElectrolyzerRecipe(Materials.HydrochloricAcid.getCells(1), GT_Utility.getIntegratedCircuit(1),  GT_Values.NF,        Materials.Chlorine.getGas(1000), Materials.Hydrogen.getCells(1),       GT_Values.NI,                   GT_Values.NI, GT_Values.NI,GT_Values.NI, GT_Values.NI, null, 720, 30);
        GT_Values.RA.addElectrolyzerRecipe(Materials.HydrochloricAcid.getCells(1), GT_Utility.getIntegratedCircuit(11), GT_Values.NF,        Materials.Hydrogen.getGas(1000), Materials.Chlorine.getCells(1),       GT_Values.NI,                   GT_Values.NI, GT_Values.NI,GT_Values.NI, GT_Values.NI, null, 720, 30);

        GT_Values.RA.addUniversalDistillationRecipe(Materials.DilutedHydrochloricAcid.getFluid(2000), new FluidStack[]{Materials.Water.getFluid(1000), Materials.HydrochloricAcid.getFluid(1000)}, GT_Values.NI, 600, 64);

        GT_Values.RA.addChemicalRecipe(Materials.Potassium.getDust(1), GT_Utility.getIntegratedCircuit(2), Materials.NitricAcid.getFluid(1000), GT_Values.NF, Materials.Saltpeter.getDust(1), 100, 30);

		GT_Values.RA.addMixerRecipe(Materials.Salt.getDust(2), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(1000), Materials.SaltWater.getFluid(2000), GT_Values.NI, 40, 8);
		GT_Values.RA.addDistilleryRecipe(1, Materials.SaltWater.getFluid(2000), GT_ModHandler.getDistilledWater(1000), Materials.Salt.getDust(1), 3200, 16, false);

        GT_Values.RA.addUniversalDistillationRecipe(FluidRegistry.getFluidStack("potion.vinegar", 40), new FluidStack[]{Materials.AceticAcid.getFluid(5), Materials.Water.getFluid(35)}, GT_Values.NI, 20, 64);
        GT_Values.RA.addMixerRecipe(Materials.Calcite.getDust(1),        GT_Utility.getIntegratedCircuit(1),  GT_Values.NI, GT_Values.NI, Materials.AceticAcid.getFluid(2000), Materials.CalciumAcetateSolution.getFluid(1000), GT_Values.NI, 240, 16);
        GT_Values.RA.addMixerRecipe(Materials.Calcium.getDust(1),        GT_Utility.getIntegratedCircuit(1),  GT_Values.NI, GT_Values.NI, Materials.AceticAcid.getFluid(2000), Materials.CalciumAcetateSolution.getFluid(1000), GT_Values.NI, 80, 16);
        GT_Values.RA.addMixerRecipe(Materials.Quicklime.getDust(1),      GT_Utility.getIntegratedCircuit(1),  GT_Values.NI, GT_Values.NI, Materials.AceticAcid.getFluid(2000), Materials.CalciumAcetateSolution.getFluid(1000), GT_Values.NI, 80, 16);
        GT_Values.RA.addMixerRecipe(Materials.Calcite.getDust(1),        Materials.Empty.getCells(1), GT_Utility.getIntegratedCircuit(11), GT_Values.NI, Materials.AceticAcid.getFluid(2000), GT_Values.NF, Materials.CalciumAcetateSolution.getCells(1), 240, 16);
        GT_Values.RA.addMixerRecipe(Materials.Calcium.getDust(1),        Materials.Empty.getCells(1), GT_Utility.getIntegratedCircuit(11), GT_Values.NI, Materials.AceticAcid.getFluid(2000), GT_Values.NF, Materials.CalciumAcetateSolution.getCells(1), 80, 16);
        GT_Values.RA.addMixerRecipe(Materials.Quicklime.getDust(1),      Materials.Empty.getCells(1), GT_Utility.getIntegratedCircuit(11), GT_Values.NI, Materials.AceticAcid.getFluid(2000), GT_Values.NF, Materials.CalciumAcetateSolution.getCells(1), 80, 16);
        //GameRegistry.addSmelting(Materials.CalciumAcetateSolution.getCells(1), Materials.Acetone.getCells(1), 0);
        GT_Values.RA.addFluidHeaterRecipe(GT_Utility.getIntegratedCircuit(1), Materials.CalciumAcetateSolution.getFluid(1000), Materials.Acetone.getFluid(1000), 80, 30);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.CalciumAcetateSolution.getFluid(1000), new FluidStack[]{Materials.Acetone.getFluid(1000), Materials.CarbonDioxide.getGas(1000)}, Materials.Quicklime.getDustSmall(3), 80, 480);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Calcite.getDust(1),   GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.AceticAcid.getFluid(4000)}, new FluidStack[]{Materials.Acetone.getFluid(4000), Materials.CarbonDioxide.getGas(4000)}, null, 400, 480);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Calcium.getDust(1),   GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.AceticAcid.getFluid(4000)}, new FluidStack[]{Materials.Acetone.getFluid(4000), Materials.CarbonDioxide.getGas(4000)}, null, 400, 480);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Quicklime.getDust(1), GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.AceticAcid.getFluid(4000)}, new FluidStack[]{Materials.Acetone.getFluid(4000), Materials.CarbonDioxide.getGas(4000)}, null, 400, 480);

        GT_Values.RA.addChemicalRecipe(Materials.AceticAcid.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Methanol.getFluid(1000),   Materials.MethylAcetate.getFluid(1000), Materials.Water.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1),   GT_Utility.getIntegratedCircuit(1),  Materials.AceticAcid.getFluid(1000), Materials.MethylAcetate.getFluid(1000), Materials.Water.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.AceticAcid.getCells(1), GT_Utility.getIntegratedCircuit(2),  Materials.Methanol.getFluid(1000),   Materials.MethylAcetate.getFluid(1000), Materials.Empty.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1),   GT_Utility.getIntegratedCircuit(2),  Materials.AceticAcid.getFluid(1000), Materials.MethylAcetate.getFluid(1000), Materials.Empty.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.AceticAcid.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Methanol.getFluid(1000),   Materials.Water.getFluid(1000),         Materials.MethylAcetate.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1),   GT_Utility.getIntegratedCircuit(11), Materials.AceticAcid.getFluid(1000), Materials.Water.getFluid(1000),         Materials.MethylAcetate.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.AceticAcid.getCells(1), GT_Utility.getIntegratedCircuit(12), Materials.Methanol.getFluid(1000),   GT_Values.NF,                           Materials.MethylAcetate.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1),   GT_Utility.getIntegratedCircuit(12), Materials.AceticAcid.getFluid(1000), GT_Values.NF,                           Materials.MethylAcetate.getCells(1), 240);

        GT_Values.RA.addMixerRecipe(Materials.Acetone.getCells(3), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.PolyvinylAcetate.getFluid(2000), Materials.Glue.getFluid(5000), Materials.Empty.getCells(3), 100, 8);
        GT_Values.RA.addMixerRecipe(Materials.PolyvinylAcetate.getCells(2), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Acetone.getFluid(3000), Materials.Glue.getFluid(5000), Materials.Empty.getCells(2), 100, 8);
        GT_Values.RA.addMixerRecipe(Materials.MethylAcetate.getCells(3), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.PolyvinylAcetate.getFluid(2000), Materials.Glue.getFluid(5000), Materials.Empty.getCells(3), 100, 8);
        GT_Values.RA.addMixerRecipe(Materials.PolyvinylAcetate.getCells(2), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.MethylAcetate.getFluid(3000), Materials.Glue.getFluid(5000), Materials.Empty.getCells(2), 100, 8);

        //CO and CO2 recipes 
        GT_Values.RA.addChemicalRecipe(Materials.Carbon.getDust(1),   GT_Utility.getIntegratedCircuit(1), Materials.Oxygen.getGas(1000), Materials.CarbonMonoxide.getGas(1000), GT_Values.NI, 40, 8);
//        GT_Values.RA.addChemicalRecipe(Materials.Coal.getGems(1),     GT_Utility.getIntegratedCircuit(1), Materials.Oxygen.getGas(1000), Materials.CarbonMonoxide.getGas(1000), Materials.Ash.getDust(1), 80, 8);
//        GT_Values.RA.addChemicalRecipe(Materials.Coal.getDust(1),     GT_Utility.getIntegratedCircuit(1), Materials.Oxygen.getGas(1000), Materials.CarbonMonoxide.getGas(1000), Materials.Ash.getDust(1), 80, 8);
//        GT_Values.RA.addChemicalRecipe(Materials.Charcoal.getGems(1), GT_Utility.getIntegratedCircuit(1), Materials.Oxygen.getGas(1000), Materials.CarbonMonoxide.getGas(1000), Materials.Ash.getDust(1), 80, 8);
//        GT_Values.RA.addChemicalRecipe(Materials.Charcoal.getDust(1), GT_Utility.getIntegratedCircuit(1), Materials.Oxygen.getGas(1000), Materials.CarbonMonoxide.getGas(1000), Materials.Ash.getDust(1), 80, 8);
//        GT_Values.RA.addChemicalRecipe(Materials.Carbon.getDust(1),   GT_Utility.getIntegratedCircuit(2), Materials.Oxygen.getGas(2000), Materials.CarbonDioxide.getGas(1000), GT_Values.NI, 40, 8);
//        GT_Values.RA.addChemicalRecipe(Materials.Coal.getGems(1),     GT_Utility.getIntegratedCircuit(2), Materials.Oxygen.getGas(2000), Materials.CarbonDioxide.getGas(1000), Materials.Ash.getDust(1), 40, 8);
//        GT_Values.RA.addChemicalRecipe(Materials.Coal.getDust(1),     GT_Utility.getIntegratedCircuit(2), Materials.Oxygen.getGas(2000), Materials.CarbonDioxide.getGas(1000), Materials.Ash.getDust(1), 40, 8);
//        GT_Values.RA.addChemicalRecipe(Materials.Charcoal.getGems(1), GT_Utility.getIntegratedCircuit(2), Materials.Oxygen.getGas(2000), Materials.CarbonDioxide.getGas(1000), Materials.Ash.getDust(1), 40, 8);
//        GT_Values.RA.addChemicalRecipe(Materials.Charcoal.getDust(1), GT_Utility.getIntegratedCircuit(2), Materials.Oxygen.getGas(2000), Materials.CarbonDioxide.getGas(1000), Materials.Ash.getDust(1), 40, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Carbon.getDust(1), GT_Values.NI, Materials.CarbonDioxide.getGas(1000), Materials.CarbonMonoxide.getGas(2000), GT_Values.NI, 800);

        GT_Values.RA.addChemicalRecipe(Materials.Coal.getGems(1),     GT_Utility.getIntegratedCircuit(9), Materials.Oxygen.getGas(9000), Materials.CarbonMonoxide.getGas(9000), Materials.Ash.getDust(1), 720, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Coal.getDust(1),     GT_Utility.getIntegratedCircuit(9), Materials.Oxygen.getGas(9000), Materials.CarbonMonoxide.getGas(9000), Materials.Ash.getDust(1), 720, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Charcoal.getGems(1), GT_Utility.getIntegratedCircuit(9), Materials.Oxygen.getGas(9000), Materials.CarbonMonoxide.getGas(9000), Materials.Ash.getDust(1), 720, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Charcoal.getDust(1), GT_Utility.getIntegratedCircuit(9), Materials.Oxygen.getGas(9000), Materials.CarbonMonoxide.getGas(9000), Materials.Ash.getDust(1), 720, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Coal.getGems(1),     GT_Utility.getIntegratedCircuit(8), Materials.Oxygen.getGas(18000), Materials.CarbonDioxide.getGas(9000), Materials.Ash.getDust(1), 360, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Coal.getDust(1),     GT_Utility.getIntegratedCircuit(8), Materials.Oxygen.getGas(18000), Materials.CarbonDioxide.getGas(9000), Materials.Ash.getDust(1), 360, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Charcoal.getGems(1), GT_Utility.getIntegratedCircuit(8), Materials.Oxygen.getGas(18000), Materials.CarbonDioxide.getGas(9000), Materials.Ash.getDust(1), 360, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Charcoal.getDust(1), GT_Utility.getIntegratedCircuit(8), Materials.Oxygen.getGas(18000), Materials.CarbonDioxide.getGas(9000), Materials.Ash.getDust(1), 360, 8);
        
        GT_Values.RA.addChemicalRecipe(Materials.CarbonMonoxide.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Hydrogen.getGas(4000),       Materials.Methanol.getFluid(1000), Materials.Empty.getCells(1), 120, 96);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(4),       GT_Utility.getIntegratedCircuit(1),  Materials.CarbonMonoxide.getGas(1000), Materials.Methanol.getFluid(1000), Materials.Empty.getCells(4), 120, 96);
        GT_Values.RA.addChemicalRecipe(Materials.CarbonMonoxide.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Hydrogen.getGas(4000),       GT_Values.NF, Materials.Methanol.getCells(1), 120, 96);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(4),       GT_Utility.getIntegratedCircuit(11), Materials.CarbonMonoxide.getGas(1000), GT_Values.NF, Materials.Methanol.getCells(1), Materials.Empty.getCells(3), 120, 96);
        GT_Values.RA.addChemicalRecipe(Materials.CarbonDioxide.getCells(1),  GT_Utility.getIntegratedCircuit(1),  Materials.Hydrogen.getGas(6000),       Materials.Methanol.getFluid(1000), Materials.Water.getCells(1), 120, 96);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(6),       GT_Utility.getIntegratedCircuit(1),  Materials.CarbonDioxide.getGas(1000),  Materials.Methanol.getFluid(1000), Materials.Water.getCells(1), Materials.Empty.getCells(3), 120, 96);
        GT_Values.RA.addChemicalRecipe(Materials.CarbonDioxide.getCells(1),  GT_Utility.getIntegratedCircuit(2),  Materials.Hydrogen.getGas(6000),       Materials.Methanol.getFluid(1000), Materials.Empty.getCells(1), 120, 96);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(6),       GT_Utility.getIntegratedCircuit(2),  Materials.CarbonDioxide.getGas(1000),  Materials.Methanol.getFluid(1000), Materials.Empty.getCells(6), 120, 96);
        GT_Values.RA.addChemicalRecipe(Materials.CarbonDioxide.getCells(1),  GT_Utility.getIntegratedCircuit(12), Materials.Hydrogen.getGas(6000),       GT_Values.NF, Materials.Methanol.getCells(1), 120, 96);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(6),       GT_Utility.getIntegratedCircuit(12), Materials.CarbonDioxide.getGas(1000),  GT_Values.NF, Materials.Methanol.getCells(1), Materials.Empty.getCells(5), 120, 96);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Carbon.getDust(1), GT_Utility.getIntegratedCircuit(23)}, new FluidStack[]{Materials.Hydrogen.getGas(4000), Materials.Oxygen.getGas(1000)}, new FluidStack[]{Materials.Methanol.getFluid(1000)}, null, 320, 96);

        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1), GT_Utility.getIntegratedCircuit(1), Materials.CarbonMonoxide.getGas(1000), Materials.AceticAcid.getFluid(1000), Materials.Empty.getCells(1), 300);
        GT_Values.RA.addChemicalRecipe(Materials.CarbonMonoxide.getCells(1), GT_Utility.getIntegratedCircuit(1), Materials.Methanol.getFluid(1000), Materials.AceticAcid.getFluid(1000), Materials.Empty.getCells(1), 300);
        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.CarbonMonoxide.getGas(1000), GT_Values.NF, Materials.AceticAcid.getCells(1), 300);
        GT_Values.RA.addChemicalRecipe(Materials.CarbonMonoxide.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Methanol.getFluid(1000), GT_Values.NF, Materials.AceticAcid.getCells(1), 300);
//        GT_Values.RA.addChemicalRecipe(Materials.Ethylene.getCells(1), GT_Utility.getIntegratedCircuit(9), Materials.Oxygen.getGas(2000), Materials.AceticAcid.getFluid(1000), Materials.Empty.getCells(1), 100);
//        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(2), GT_Utility.getIntegratedCircuit(9), Materials.Ethylene.getGas(1000), Materials.AceticAcid.getFluid(1000), Materials.Empty.getCells(2), 100);
//        GT_Values.RA.addChemicalRecipe(Materials.Ethylene.getCells(1), GT_Utility.getIntegratedCircuit(19), Materials.Oxygen.getGas(2000), GT_Values.NF, Materials.AceticAcid.getCells(1), 100);
//        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(2), GT_Utility.getIntegratedCircuit(19), Materials.Ethylene.getGas(1000), GT_Values.NF, Materials.AceticAcid.getCells(1), Materials.Empty.getCells(1), 100);
        //This recipe collides with one for Vinyl Chloride
        //GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Oxygen.getCells(2), Materials.Ethylene.getCells(1), GT_Values.NF, Materials.AceticAcid.getFluid(1000), Materials.Empty.getCells(3), GT_Values.NI, 100, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Carbon.getDust(2), GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Hydrogen.getGas(4000), Materials.Oxygen.getGas(2000)}, new FluidStack[]{Materials.AceticAcid.getFluid(1000)}, null, 480, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.CarbonMonoxide.getGas(2000), Materials.Hydrogen.getGas(4000)}, new FluidStack[]{Materials.AceticAcid.getFluid(1000)}, null, 320, 30);

        GT_Values.RA.addFermentingRecipe(Materials.Biomass.getFluid(100), Materials.FermentedBiomass.getFluid(100), 150, false);
        GT_Values.RA.addFermentingRecipe(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 100), Materials.FermentedBiomass.getFluid(100), 150, false);

        GT_Values.RA.addPyrolyseRecipe(GT_ModHandler.getIC2Item("biochaff", 1), Materials.Water.getFluid(1500), 2, GT_Values.NI, Materials.FermentedBiomass.getFluid(1500), 200, 10);
        GT_Values.RA.addPyrolyseRecipe(GT_Values.NI, new FluidStack(FluidRegistry.getFluid("ic2biomass"), 1000), 2, GT_Values.NI, Materials.FermentedBiomass.getFluid(1000), 100, 10);
        GT_Values.RA.addPyrolyseRecipe(GT_Values.NI, Materials.Biomass.getFluid(1000), 2, GT_Values.NI, Materials.FermentedBiomass.getFluid(1000), 100, 10);

        GT_Values.RA.addDistillationTowerRecipe(Materials.FermentedBiomass.getFluid(1000), new FluidStack[]{
                Materials.AceticAcid.getFluid(25), Materials.Water.getFluid(375), Materials.Ethanol.getFluid(150),
                Materials.Methanol.getFluid(150),Materials.Ammonia.getGas(100), Materials.CarbonDioxide.getGas(400)}, ItemList.IC2_Fertilizer.get(1), 75, 180);
        GT_Values.RA.addDistilleryRecipe(1, Materials.FermentedBiomass.getFluid(1000), Materials.AceticAcid.getFluid(25),   ItemList.IC2_Fertilizer.get(1), 500, 16, false);
        GT_Values.RA.addDistilleryRecipe(2, Materials.FermentedBiomass.getFluid(1000), Materials.Water.getFluid(375),       ItemList.IC2_Fertilizer.get(1), 500, 16, false);
        GT_Values.RA.addDistilleryRecipe(3, Materials.FermentedBiomass.getFluid(1000), Materials.Ethanol.getFluid(150),     ItemList.IC2_Fertilizer.get(1), 500, 16, false);
        GT_Values.RA.addDistilleryRecipe(4, Materials.FermentedBiomass.getFluid(1000), Materials.Methanol.getFluid(150),    ItemList.IC2_Fertilizer.get(1), 500, 16, false);
        GT_Values.RA.addDistilleryRecipe(5, Materials.FermentedBiomass.getFluid(1000), Materials.Ammonia.getGas(100),       ItemList.IC2_Fertilizer.get(1), 500, 16, false);
        GT_Values.RA.addDistilleryRecipe(6, Materials.FermentedBiomass.getFluid(1000), Materials.CarbonDioxide.getGas(400), ItemList.IC2_Fertilizer.get(1), 500, 16, false);
//        GT_Values.RA.addDistilleryRecipe(7, Materials.FermentedBiomass.getFluid(1000), Materials.Methane.getGas(600),       ItemList.IC2_Fertilizer.get(1), 1500, 8, false);

        GT_Values.RA.addDistilleryRecipe(17, Materials.FermentedBiomass.getFluid(1000), new FluidStack(FluidRegistry.getFluid("ic2biogas"), 1800), ItemList.IC2_Fertilizer.get(1), 1600, 8, false);
//        GT_Values.RA.addDistilleryRecipe(1, Materials.Methane.getGas(1000), new FluidStack(FluidRegistry.getFluid("ic2biogas"), 3000), GT_Values.NI, 160, 8, false);

        GT_Values.RA.addPyrolyseRecipe(Materials.Sugar.getDust(23), 								  GT_Values.NF, 					1, Materials.Charcoal.getDust(12),   Materials.Water.getFluid(1500), 				320, 64);
        GT_Values.RA.addPyrolyseRecipe(Materials.Sugar.getDust(23), 								  Materials.Nitrogen.getGas(500),   2, Materials.Charcoal.getDust(12),   Materials.Water.getFluid(1500), 				160, 96);

//        GT_Values.RA.addUniversalDistillationRecipe(Materials.CharcoalByproducts.getGas(1000),
//                new FluidStack[]{Materials.WoodTar.getFluid(250), Materials.WoodVinegar.getFluid(400), Materials.WoodGas.getGas(250), Materials.Dimethylbenzene.getFluid(100)},
//                Materials.Charcoal.getDustSmall(1),  40, 256);
//        GT_Values.RA.addUniversalDistillationRecipe(Materials.WoodGas.getGas(1000),
//                new FluidStack[]{Materials.CarbonDioxide.getGas(490), Materials.Ethylene.getGas(20), Materials.Methane.getGas(130), Materials.CarbonMonoxide.getGas(340), Materials.Hydrogen.getGas(20)},
//                GT_Values.NI,  40, 256);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.WoodVinegar.getFluid(1000),
                new FluidStack[]{Materials.AceticAcid.getFluid(100), Materials.Water.getFluid(500), Materials.Ethanol.getFluid(10), Materials.Methanol.getFluid(300), Materials.Acetone.getFluid(50), Materials.MethylAcetate.getFluid(10)},
                GT_Values.NI,  40, 256);
//        GT_Values.RA.addUniversalDistillationRecipe(Materials.WoodTar.getFluid(1000),
//                new FluidStack[]{Materials.Creosote.getFluid(300), Materials.Phenol.getFluid(75), Materials.Benzene.getFluid(350), Materials.Toluene.getFluid(75), Materials.Dimethylbenzene.getFluid(200)},
//                GT_Values.NI,  40, 256);

//        GT_Values.RA.addChemicalRecipe(Materials.Ethylene.getCells(1), 	Materials.AceticAcid.getCells(1), 	Materials.Oxygen.getGas(1000), 			Materials.VinylAcetate.getFluid(1000),Materials.Water.getCells(1), Materials.Empty.getCells(1), 180);
//        GT_Values.RA.addChemicalRecipe(Materials.AceticAcid.getCells(1),Materials.Oxygen.getCells(1), 		Materials.Ethylene.getGas(1000), 		Materials.VinylAcetate.getFluid(1000),Materials.Water.getCells(1), Materials.Empty.getCells(1), 180);
//        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(1), 	Materials.Ethylene.getCells(1), 	Materials.AceticAcid.getFluid(1000), 	Materials.VinylAcetate.getFluid(1000),Materials.Water.getCells(1), Materials.Empty.getCells(1), 180);

    	GT_Values.RA.addDefaultPolymerizationRecipes(Materials.VinylAcetate.mFluid, Materials.VinylAcetate.getCells(1), Materials.PolyvinylAcetate.mFluid);

//        GT_Values.RA.addChemicalRecipe(Materials.Ethanol.getCells(1), GT_Utility.getIntegratedCircuit(1), Materials.SulfuricAcid.getFluid(1000), Materials.DilutedSulfuricAcid.getFluid(1000), Materials.Ethylene.getCells(1), 1200, 120);
//        GT_Values.RA.addChemicalRecipe(Materials.SulfuricAcid.getCells(1), GT_Utility.getIntegratedCircuit(1), Materials.Ethanol.getFluid(1000), Materials.DilutedSulfuricAcid.getFluid(1000), Materials.Ethylene.getCells(1), 1200, 120);
//        GT_Values.RA.addChemicalRecipe(Materials.Ethanol.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.SulfuricAcid.getFluid(1000), Materials.Ethylene.getGas(1000), Materials.DilutedSulfuricAcid.getCells(1), 1200, 120);
//        GT_Values.RA.addChemicalRecipe(Materials.SulfuricAcid.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Ethanol.getFluid(1000), Materials.Ethylene.getGas(1000), Materials.DilutedSulfuricAcid.getCells(1), 1200, 120);

//    	GT_Values.RA.addDefaultPolymerizationRecipes(Materials.Ethylene.mGas, Materials.Ethylene.getCells(1), Materials.Plastic.mStandardMoltenFluid);

        GT_Values.RA.addChemicalRecipe(Materials.Sodium.getDust(1), GT_Utility.getIntegratedCircuit(1), Materials.Water.getFluid(1000), Materials.Hydrogen.getGas(500), Materials.SodiumHydroxide.getDust(1), 200, 30);

        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Hydrogen.getGas(1000), Materials.HydrochloricAcid.getFluid(1000), Materials.Empty.getCells(1), 60, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Chlorine.getGas(1000), Materials.HydrochloricAcid.getFluid(1000), Materials.Empty.getCells(1), 60, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Hydrogen.getGas(1000), GT_Values.NF, Materials.HydrochloricAcid.getCells(1), 60, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Chlorine.getGas(1000), GT_Values.NF, Materials.HydrochloricAcid.getCells(1), 60, 8);

//        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(1),  Materials.Propene.getGas(1000),  Materials.AllylChloride.getFluid(1000),    Materials.HydrochloricAcid.getCells(1), Materials.Empty.getCells(1), 160);
//        GT_Values.RA.addChemicalRecipe(Materials.Propene.getCells(1),  GT_Utility.getIntegratedCircuit(1),  Materials.Chlorine.getGas(2000), Materials.AllylChloride.getFluid(1000),    Materials.HydrochloricAcid.getCells(1), 160);
//        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(11), Materials.Propene.getGas(1000),  Materials.HydrochloricAcid.getFluid(1000), Materials.AllylChloride.getCells(1), Materials.Empty.getCells(1), 160);
//        GT_Values.RA.addChemicalRecipe(Materials.Propene.getCells(1),  GT_Utility.getIntegratedCircuit(11), Materials.Chlorine.getGas(2000), Materials.HydrochloricAcid.getFluid(1000), Materials.AllylChloride.getCells(1), 160);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Chlorine.getCells(10),  Materials.Mercury.getCells(1), 	Materials.Water.getFluid(10000), 	Materials.HypochlorousAcid.getFluid(10000), Materials.Empty.getCells(11), GT_Values.NI, 600, 8);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Water.getCells(10), 	Materials.Mercury.getCells(1), 	Materials.Chlorine.getGas(10000), 	Materials.HypochlorousAcid.getFluid(10000), Materials.Empty.getCells(11), GT_Values.NI, 600, 8);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Chlorine.getCells(1),	Materials.Water.getCells(1), 	Materials.Mercury.getFluid(100), 	Materials.HypochlorousAcid.getFluid(1000),  Materials.Empty.getCells(2),  GT_Values.NI,  60, 8);
    	GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(2)}, new FluidStack[]{Materials.Chlorine.getGas(10000), Materials.Water.getFluid(10000), Materials.Mercury.getFluid(1000)}, new FluidStack[]{Materials.HypochlorousAcid.getFluid(10000)}, null, 600, 8);

        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(1),  Materials.Water.getFluid(1000),  Materials.HypochlorousAcid.getFluid(1000),        Materials.DilutedHydrochloricAcid.getCells(1), Materials.Empty.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(1),    GT_Utility.getIntegratedCircuit(1),  Materials.Chlorine.getGas(2000), Materials.HypochlorousAcid.getFluid(1000),        Materials.DilutedHydrochloricAcid.getCells(1), GT_Values.NI, 120);
        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(11), Materials.Water.getFluid(1000),  Materials.DilutedHydrochloricAcid.getFluid(1000), Materials.HypochlorousAcid.getCells(1),        Materials.Empty.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(1),    GT_Utility.getIntegratedCircuit(11), Materials.Chlorine.getGas(2000), Materials.DilutedHydrochloricAcid.getFluid(1000), Materials.HypochlorousAcid.getCells(1),        GT_Values.NI, 120);

        GT_Values.RA.addChemicalRecipe(                   Materials.HypochlorousAcid.getCells(1), 	Materials.SodiumHydroxide.getDust(1), 	Materials.AllylChloride.getFluid(1000), 	Materials.Epichlorohydrin.getFluid(1000), Materials.SaltWater.getCells(1), 480);
        GT_Values.RA.addChemicalRecipe(                   Materials.SodiumHydroxide.getDust(1), 	Materials.AllylChloride.getCells(1), 	Materials.HypochlorousAcid.getFluid(1000), 	Materials.Epichlorohydrin.getFluid(1000), Materials.SaltWater.getCells(1), 480);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.HydrochloricAcid.getCells(1), 	Materials.Glycerol.getCells(1), 		GT_Values.NF, 								Materials.Epichlorohydrin.getFluid(1000), Materials.Water.getCells(2), GT_Values.NI, 480, 30);
        //GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.SodiumHydroxide.getDust(1), GT_Utility.getIntegratedCircuit(23)}, new FluidStack[]{Materials.Propene.getGas(1000), Materials.Chlorine.getGas(4000), Materials.Water.getFluid(1000)},                                  new FluidStack[]{Materials.Epichlorohydrin.getFluid(1000), Materials.SaltWater.getFluid(1000), Materials.HydrochloricAcid.getFluid(2000)}, null, 640, 30);
        //GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.SodiumHydroxide.getDust(1), GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Propene.getGas(1000), Materials.Chlorine.getGas(3000), Materials.Water.getFluid(1000), Materials.Mercury.getFluid(100)}, new FluidStack[]{Materials.Epichlorohydrin.getFluid(1000), Materials.SaltWater.getFluid(1000), Materials.HydrochloricAcid.getFluid(1000)}, null, 640, 30);
        //GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.SodiumHydroxide.getDust(1), GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Propene.getGas(1000), Materials.Chlorine.getGas(2000), Materials.HypochlorousAcid.getFluid(1000)},                       new FluidStack[]{Materials.Epichlorohydrin.getFluid(1000), Materials.SaltWater.getFluid(1000), Materials.HydrochloricAcid.getFluid(1000)}, null, 640, 30);

        GT_Values.RA.addChemicalRecipe(                   Materials.HydrochloricAcid.getCells(1), 	Materials.Empty.getCells(1), 			Materials.Glycerol.getFluid(1000), 			Materials.Epichlorohydrin.getFluid(1000), Materials.Water.getCells(2), 480);
        //GT_Values.RA.addChemicalRecipe(                   Materials.Glycerol.getCells(1), 			Materials.Empty.getCells(1), 			Materials.HydrochloricAcid.getFluid(1000), 	Materials.Epichlorohydrin.getFluid(1000), Materials.Water.getCells(2), 480);
        GT_Values.RA.addChemicalRecipe(                   Materials.HydrochloricAcid.getCells(1), 	GT_Utility.getIntegratedCircuit(11), 	Materials.Glycerol.getFluid(1000), 			Materials.Water.getFluid(2000), 		  Materials.Epichlorohydrin.getCells(1), 480);
        //GT_Values.RA.addChemicalRecipe(                   Materials.Glycerol.getCells(1), 			GT_Utility.getIntegratedCircuit(11), 	Materials.HydrochloricAcid.getFluid(1000), 	Materials.Water.getFluid(2000), 		  Materials.Epichlorohydrin.getCells(1), 480);
        GT_Values.RA.addChemicalRecipe(                   Materials.HydrochloricAcid.getCells(1), 	GT_Utility.getIntegratedCircuit(2), 	Materials.Glycerol.getFluid(1000), 			Materials.Epichlorohydrin.getFluid(1000), Materials.Empty.getCells(1), 480);
        //GT_Values.RA.addChemicalRecipe(                   Materials.Glycerol.getCells(1), 			GT_Utility.getIntegratedCircuit(2), 	Materials.HydrochloricAcid.getFluid(1000), 	Materials.Epichlorohydrin.getFluid(1000), Materials.Empty.getCells(1), 480);
        GT_Values.RA.addChemicalRecipe(                   Materials.HydrochloricAcid.getCells(1), 	GT_Utility.getIntegratedCircuit(12), 	Materials.Glycerol.getFluid(1000), 			GT_Values.NF, 		  					  Materials.Epichlorohydrin.getCells(1), 480);
        //GT_Values.RA.addChemicalRecipe(                   Materials.Glycerol.getCells(1), 			GT_Utility.getIntegratedCircuit(12), 	Materials.HydrochloricAcid.getFluid(1000), 	GT_Values.NF, 		  					  Materials.Epichlorohydrin.getCells(1), 480);

//        GT_Values.RA.addDistilleryRecipe(2, Materials.HeavyFuel.getFluid(100), Materials.Benzene.getFluid(40), 160, 24, false);
//        GT_Values.RA.addDistilleryRecipe(3, Materials.HeavyFuel.getFluid(100), Materials.Phenol.getFluid(25), 160, 24, false);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Apatite.getDust(1), Materials.SulfuricAcid.getCells(5), Materials.Water.getFluid(10000), Materials.PhosphoricAcid.getFluid(3000), Materials.HydrochloricAcid.getCells(1), Materials.Empty.getCells(4), 320, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Apatite.getDust(1)}, new FluidStack[]{Materials.SulfuricAcid.getFluid(5000), Materials.Water.getFluid(10000)}, new FluidStack[]{Materials.PhosphoricAcid.getFluid(3000), Materials.HydrochloricAcid.getFluid(1000)}, new ItemStack[]{Materials.Gypsum.getDust(5)}, 320, 30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Phosphorus.getDust(4), GT_Values.NI, Materials.Oxygen.getGas(10000), GT_Values.NF, Materials.PhosphorousPentoxide.getDust(1), GT_Values.NI, 40, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Phosphorus.getDust(4), GT_Utility.getIntegratedCircuit(1)}, new FluidStack[]{Materials.Oxygen.getGas(10000)}, null, new ItemStack[]{Materials.PhosphorousPentoxide.getDust(1)}, 40, 30);
        GT_Values.RA.addChemicalRecipe(Materials.PhosphorousPentoxide.getDust(1), GT_Values.NI, Materials.Water.getFluid(6000), Materials.PhosphoricAcid.getFluid(4000), GT_Values.NI, 40);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Phosphorus.getDust(1), GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Oxygen.getGas(2500), Materials.Water.getFluid(1500)}, new FluidStack[]{Materials.PhosphoricAcid.getFluid(1000)}, null, 320, 30);

        //GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Propene.getCells(8),        Materials.PhosphoricAcid.getCells(1), Materials.Benzene.getFluid(8000),       Materials.Cumene.getFluid(8000), Materials.Empty.getCells(9), GT_Values.NI, 1920, 30);
        //GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.PhosphoricAcid.getCells(1), Materials.Benzene.getCells(8),        Materials.Propene.getGas(8000),         Materials.Cumene.getFluid(8000), Materials.Empty.getCells(9), GT_Values.NI, 1920, 30);
        //GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Benzene.getCells(1),        Materials.Propene.getCells(1),        Materials.PhosphoricAcid.getFluid(125), Materials.Cumene.getFluid(1000), Materials.Empty.getCells(2), GT_Values.NI, 240 , 30);
        //GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(1)}, new FluidStack[]{Materials.Propene.getGas(8000), Materials.Benzene.getFluid(8000), Materials.PhosphoricAcid.getFluid(1000)}, new FluidStack[]{Materials.Cumene.getFluid(8000)}, null, 1920, 30);

        GT_Values.RA.addChemicalRecipe(Materials.Cumene.getCells(1), GT_Utility.getIntegratedCircuit(1), 	Materials.Oxygen.getGas(1000), 	Materials.Acetone.getFluid(1000), 	Materials.Phenol.getCells(1), 	160);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(1), GT_Utility.getIntegratedCircuit(1), 	Materials.Cumene.getFluid(1000),Materials.Acetone.getFluid(1000), 	Materials.Phenol.getCells(1), 	160);
        GT_Values.RA.addChemicalRecipe(Materials.Cumene.getCells(1), GT_Utility.getIntegratedCircuit(11), 	Materials.Oxygen.getGas(1000), 	Materials.Phenol.getFluid(1000), 	Materials.Acetone.getCells(1), 	160);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(1), GT_Utility.getIntegratedCircuit(11), 	Materials.Cumene.getFluid(1000),Materials.Phenol.getFluid(1000), 	Materials.Acetone.getCells(1), 	160);
        //GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Propene.getGas(1000), Materials.Benzene.getFluid(1000), Materials.PhosphoricAcid.getFluid(100), Materials.Oxygen.getGas(1000)}, new FluidStack[]{Materials.Phenol.getFluid(1000), Materials.Acetone.getFluid(1000)}, null, 480, 30);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Acetone.getCells(1), 			Materials.Phenol.getCells(2), 			Materials.HydrochloricAcid.getFluid(1000),  Materials.BisphenolA.getFluid(1000), Materials.Water.getCells(1), Materials.Empty.getCells(2), 160, 30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.HydrochloricAcid.getCells(1), 	Materials.Acetone.getCells(1), 			Materials.Phenol.getFluid(2000), 			Materials.BisphenolA.getFluid(1000), Materials.Water.getCells(1), Materials.Empty.getCells(1), 160, 30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Phenol.getCells(2), 			Materials.HydrochloricAcid.getCells(1), Materials.Acetone.getFluid(1000), 			Materials.BisphenolA.getFluid(1000), Materials.Water.getCells(1), Materials.Empty.getCells(2), 160, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(1)}, new FluidStack[]{Materials.Acetone.getFluid(1000), Materials.Phenol.getFluid(2000), Materials.HydrochloricAcid.getFluid(1000)}, new FluidStack[]{Materials.BisphenolA.getFluid(1000), Materials.Water.getFluid(1000)}, null, 160, 30);

        GT_Values.RA.addChemicalRecipe(Materials.BisphenolA.getCells(1), 		Materials.SodiumHydroxide.getDust(1), 	Materials.Epichlorohydrin.getFluid(1000), 	Materials.Epoxid.getMolten(1000), Materials.SaltWater.getCells(1), 200);
        GT_Values.RA.addChemicalRecipe(Materials.SodiumHydroxide.getDust(1), 	Materials.Epichlorohydrin.getCells(1), 	Materials.BisphenolA.getFluid(1000), 		Materials.Epoxid.getMolten(1000), Materials.SaltWater.getCells(1), 200);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.SodiumHydroxide.getDust(1), GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Acetone.getFluid(1000), Materials.Phenol.getFluid(2000), Materials.HydrochloricAcid.getFluid(1000), Materials.Epichlorohydrin.getFluid(1000)}, new FluidStack[]{Materials.Epoxid.getMolten(1000), Materials.SaltWater.getFluid(1000)}, null, 480, 30);

        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1),         GT_Utility.getIntegratedCircuit(1),  Materials.HydrochloricAcid.getFluid(1000), Materials.Chloromethane.getGas(1000), Materials.Water.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.HydrochloricAcid.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Methanol.getFluid(1000),         Materials.Chloromethane.getGas(1000), Materials.Water.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1),         GT_Utility.getIntegratedCircuit(11), Materials.HydrochloricAcid.getFluid(1000), Materials.Water.getFluid(1000), Materials.Chloromethane.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.HydrochloricAcid.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Methanol.getFluid(1000),         Materials.Water.getFluid(1000), Materials.Chloromethane.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1),         GT_Utility.getIntegratedCircuit(2),  Materials.HydrochloricAcid.getFluid(1000), Materials.Chloromethane.getGas(1000), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.HydrochloricAcid.getCells(1), GT_Utility.getIntegratedCircuit(2),  Materials.Methanol.getFluid(1000),         Materials.Chloromethane.getGas(1000), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1),         GT_Utility.getIntegratedCircuit(12), Materials.HydrochloricAcid.getFluid(1000), GT_Values.NF,                   Materials.Chloromethane.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.HydrochloricAcid.getCells(1), GT_Utility.getIntegratedCircuit(12), Materials.Methanol.getFluid(1000),         GT_Values.NF,                   Materials.Chloromethane.getCells(1), 160);

//        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(1),  Materials.Methane.getGas(1000),  Materials.Chloromethane.getGas(1000), 		Materials.HydrochloricAcid.getCells(1), Materials.Empty.getCells(1), 80);
//        GT_Values.RA.addChemicalRecipe(Materials.Methane.getCells(1),  GT_Utility.getIntegratedCircuit(1),  Materials.Chlorine.getGas(2000), Materials.Chloromethane.getGas(1000), 		Materials.HydrochloricAcid.getCells(1), 80);
//        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(11), Materials.Methane.getGas(1000),  Materials.HydrochloricAcid.getFluid(1000), Materials.Chloromethane.getCells(1), Materials.Empty.getCells(1), 80);
//        GT_Values.RA.addChemicalRecipe(Materials.Methane.getCells(1),  GT_Utility.getIntegratedCircuit(11), Materials.Chlorine.getGas(2000), Materials.HydrochloricAcid.getFluid(1000), Materials.Chloromethane.getCells(1), 80);

//        GT_Values.RA.addChemicalRecipe(                   Materials.Chlorine.getCells(6), GT_Utility.getIntegratedCircuit(3),  Materials.Methane.getGas(1000),  Materials.Chloroform.getFluid(1000),       Materials.HydrochloricAcid.getCells(3), Materials.Empty.getCells(3), 80);
//        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Methane.getCells(1),  Materials.Empty.getCells(2),         Materials.Chlorine.getGas(6000), Materials.Chloroform.getFluid(1000),       Materials.HydrochloricAcid.getCells(3), GT_Values.NI,  80, 30);
//        GT_Values.RA.addChemicalRecipe(                   Materials.Chlorine.getCells(6), GT_Utility.getIntegratedCircuit(13), Materials.Methane.getGas(1000),  Materials.HydrochloricAcid.getFluid(3000), Materials.Chloroform.getCells(1),       Materials.Empty.getCells(5), 80);
//        GT_Values.RA.addChemicalRecipe(                   Materials.Methane.getCells(1),  GT_Utility.getIntegratedCircuit(13), Materials.Chlorine.getGas(6000), Materials.HydrochloricAcid.getFluid(3000), Materials.Chloroform.getCells(1),       80);

        GT_Values.RA.addChemicalRecipe(Materials.Fluorine.getCells(1), 	GT_Utility.getIntegratedCircuit(1),  Materials.Hydrogen.getGas(1000), Materials.HydrofluoricAcid.getFluid(1000), Materials.Empty.getCells(1), 60, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(1), 	GT_Utility.getIntegratedCircuit(1),  Materials.Fluorine.getGas(1000), Materials.HydrofluoricAcid.getFluid(1000), Materials.Empty.getCells(1), 60, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Fluorine.getCells(1), 	GT_Utility.getIntegratedCircuit(11), Materials.Hydrogen.getGas(1000), GT_Values.NF, Materials.HydrofluoricAcid.getCells(1), 60, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(1), 	GT_Utility.getIntegratedCircuit(11), Materials.Fluorine.getGas(1000), GT_Values.NF, Materials.HydrofluoricAcid.getCells(1), 60, 8);

        GT_Values.RA.addChemicalRecipe(Materials.Chloroform.getCells(2), 		Materials.HydrofluoricAcid.getCells(4), GT_Values.NF,                              Materials.Tetrafluoroethylene.getGas(1000),       Materials.DilutedHydrochloricAcid.getCells(6), 480, 120);
        GT_Values.RA.addChemicalRecipe(Materials.Chloroform.getCells(2), 		Materials.Empty.getCells(4),            Materials.HydrofluoricAcid.getFluid(4000), Materials.Tetrafluoroethylene.getGas(1000),       Materials.DilutedHydrochloricAcid.getCells(6), 480, 120);
        GT_Values.RA.addChemicalRecipe(Materials.HydrofluoricAcid.getCells(4),  Materials.Empty.getCells(2),            Materials.Chloroform.getFluid(2000),       Materials.Tetrafluoroethylene.getGas(1000),       Materials.DilutedHydrochloricAcid.getCells(6), 480, 120);
        GT_Values.RA.addChemicalRecipe(Materials.HydrofluoricAcid.getCells(4),  GT_Utility.getIntegratedCircuit(11),    Materials.Chloroform.getFluid(2000),       Materials.DilutedHydrochloricAcid.getFluid(6000), Materials.Tetrafluoroethylene.getCells(1),     Materials.Empty.getCells(3), 480, 120);
        GT_Values.RA.addChemicalRecipe(Materials.Chloroform.getCells(2),        GT_Utility.getIntegratedCircuit(11),    Materials.HydrofluoricAcid.getFluid(4000), Materials.DilutedHydrochloricAcid.getFluid(6000), Materials.Tetrafluoroethylene.getCells(1),     Materials.Empty.getCells(1), 480, 120);
//        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.HydrofluoricAcid.getFluid(4000), Materials.Methane.getGas(2000), Materials.Chlorine.getGas(12000)}, new FluidStack[]{Materials.Tetrafluoroethylene.getGas(1000), Materials.HydrochloricAcid.getFluid(6000), Materials.DilutedHydrochloricAcid.getFluid(6000)}, null, 540, 240);

        GT_Values.RA.addDefaultPolymerizationRecipes(Materials.Tetrafluoroethylene.mGas, Materials.Tetrafluoroethylene.getCells(1), Materials.Polytetrafluoroethylene.mStandardMoltenFluid);

        GT_Values.RA.addChemicalRecipe(                   Materials.Silicon.getDust(1), GT_Utility.getIntegratedCircuit(1), Materials.Chloromethane.getGas(2000), Materials.Dimethyldichlorosilane.getFluid(1000), GT_Values.NI, 240, 96);
        //This recipe is redundant:
        GT_Values.RA.addChemicalRecipe(                   Materials.Silicon.getDust(1), GT_Utility.getIntegratedCircuit(11), Materials.Chloromethane.getGas(2000), GT_Values.NF, Materials.Dimethyldichlorosilane.getCells(1), 240, 96);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Silicon.getDust(1), Materials.Chloromethane.getCells(2), GT_Values.NF, Materials.Dimethyldichlorosilane.getFluid(1000), Materials.Empty.getCells(2), GT_Values.NI, 240, 96);

        GT_Values.RA.addChemicalRecipe(Materials.Dimethyldichlorosilane.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Water.getFluid(1000),                   Materials.DilutedHydrochloricAcid.getFluid(1000), Materials.Polydimethylsiloxane.getDust(3), Materials.Empty.getCells(1), 240, 96);
        GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(1),                  GT_Utility.getIntegratedCircuit(1),  Materials.Dimethyldichlorosilane.getFluid(1000),  Materials.DilutedHydrochloricAcid.getFluid(1000), Materials.Polydimethylsiloxane.getDust(3), Materials.Empty.getCells(1), 240, 96);
        GT_Values.RA.addChemicalRecipe(Materials.Dimethyldichlorosilane.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Water.getFluid(1000),                   GT_Values.NF,                                     Materials.Polydimethylsiloxane.getDust(3), Materials.DilutedHydrochloricAcid.getCells(1), 240, 96);
        GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(1),                  GT_Utility.getIntegratedCircuit(11), Materials.Dimethyldichlorosilane.getFluid(1000),  GT_Values.NF,                                     Materials.Polydimethylsiloxane.getDust(3), Materials.DilutedHydrochloricAcid.getCells(1), 240, 96);
        GT_Values.RA.addChemicalRecipe(Materials.Dimethyldichlorosilane.getCells(1), Materials.Water.getCells(1),         GT_Values.NF,                                     Materials.DilutedHydrochloricAcid.getFluid(1000), Materials.Polydimethylsiloxane.getDust(3), Materials.Empty.getCells(2), 240, 96);
//        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Silicon.getDust(1), GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Methane.getGas(2000), Materials.Chlorine.getGas(4000), Materials.Water.getFluid(1000)}, new FluidStack[]{Materials.HydrochloricAcid.getFluid(2000), Materials.DilutedHydrochloricAcid.getFluid(2000)}, new ItemStack[]{Materials.Polydimethylsiloxane.getDust(3)}, 480, 96);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Silicon.getDust(1), GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Methanol.getFluid(2000), Materials.HydrochloricAcid.getFluid(2000)},                    new FluidStack[]{Materials.DilutedHydrochloricAcid.getFluid(2000)}, new ItemStack[]{Materials.Polydimethylsiloxane.getDust(3)}, 480, 96);

        GT_Values.RA.addChemicalRecipe(Materials.Polydimethylsiloxane.getDust(9), Materials.Sulfur.getDust(1), GT_Values.NF, Materials.Silicone.getMolten(1296), GT_Values.NI, 600);

        GT_Values.RA.addChemicalRecipe(                   Materials.Methanol.getCells(2), GT_Utility.getIntegratedCircuit(1),  Materials.Ammonia.getGas(1000),    Materials.Dimethylamine.getGas(1000), Materials.Water.getCells(2), 240, 120);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Ammonia.getCells(1),  Materials.Empty.getCells(1),         Materials.Methanol.getFluid(2000), Materials.Dimethylamine.getGas(1000), Materials.Water.getCells(2), GT_Values.NI, 240, 120);
        GT_Values.RA.addChemicalRecipe(                   Materials.Methanol.getCells(2), GT_Utility.getIntegratedCircuit(11), Materials.Ammonia.getGas(1000),    Materials.Water.getFluid(1000),       Materials.Dimethylamine.getCells(1), Materials.Empty.getCells(1), 240, 120);
        GT_Values.RA.addChemicalRecipe(                   Materials.Ammonia.getCells(1),  GT_Utility.getIntegratedCircuit(11), Materials.Methanol.getFluid(2000), Materials.Water.getFluid(1000),       Materials.Dimethylamine.getCells(1), 240, 120);
        GT_Values.RA.addChemicalRecipe(                   Materials.Methanol.getCells(2), GT_Utility.getIntegratedCircuit(2),  Materials.Ammonia.getGas(1000),    Materials.Dimethylamine.getGas(1000), Materials.Empty.getCells(2), 240, 120);
        GT_Values.RA.addChemicalRecipe(                   Materials.Methanol.getCells(2), GT_Utility.getIntegratedCircuit(12), Materials.Ammonia.getGas(1000),    GT_Values.NF,                         Materials.Dimethylamine.getCells(1), Materials.Empty.getCells(1), 240, 120);
        GT_Values.RA.addChemicalRecipe(                   Materials.Ammonia.getCells(1),  GT_Utility.getIntegratedCircuit(12), Materials.Methanol.getFluid(2000), GT_Values.NF,                         Materials.Dimethylamine.getCells(1), 240, 120);

        GT_Values.RA.addChemicalRecipe(Materials.Ammonia.getCells(1),          GT_Utility.getIntegratedCircuit(1),  Materials.HypochlorousAcid.getFluid(1000), Materials.Chloramine.getFluid(1000), Materials.Water.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.HypochlorousAcid.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Ammonia.getGas(1000),            Materials.Chloramine.getFluid(1000), Materials.Water.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Ammonia.getCells(1),          GT_Utility.getIntegratedCircuit(11), Materials.HypochlorousAcid.getFluid(1000), Materials.Water.getFluid(1000), Materials.Chloramine.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.HypochlorousAcid.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Ammonia.getGas(1000),            Materials.Water.getFluid(1000), Materials.Chloramine.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Ammonia.getCells(1),          GT_Utility.getIntegratedCircuit(2),  Materials.HypochlorousAcid.getFluid(1000), Materials.Chloramine.getFluid(1000), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.HypochlorousAcid.getCells(1), GT_Utility.getIntegratedCircuit(2),  Materials.Ammonia.getGas(1000),            Materials.Chloramine.getFluid(1000), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Ammonia.getCells(1),          GT_Utility.getIntegratedCircuit(12), Materials.HypochlorousAcid.getFluid(1000), GT_Values.NF,                   Materials.Chloramine.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.HypochlorousAcid.getCells(1), GT_Utility.getIntegratedCircuit(12), Materials.Ammonia.getGas(1000),            GT_Values.NF,                   Materials.Chloramine.getCells(1), 160);

        GT_Values.RA.addChemicalRecipe(GT_Utility.getIntegratedCircuit(2), GT_Values.NI, Materials.NitrogenDioxide.getGas(2000), Materials.DinitrogenTetroxide.getGas(1000), GT_Values.NI, 640);
        GT_Values.RA.addChemicalRecipe(Materials.NitrogenDioxide.getCells(2), GT_Utility.getIntegratedCircuit(2), GT_Values.NF, Materials.DinitrogenTetroxide.getGas(1000), Materials.Empty.getCells(2), 640);
        GT_Values.RA.addChemicalRecipe(Materials.NitrogenDioxide.getCells(2), GT_Utility.getIntegratedCircuit(12), GT_Values.NF, GT_Values.NF, Materials.DinitrogenTetroxide.getCells(1), Materials.Empty.getCells(1), 640);
    	GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(23)}, new FluidStack[]{Materials.Ammonia.getGas(2000), Materials.Oxygen.getGas(7000)},                                   new FluidStack[]{Materials.DinitrogenTetroxide.getGas(1000), Materials.Water.getFluid(3000)}, null, 480, 30);
    	GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(23)}, new FluidStack[]{Materials.Nitrogen.getGas(2000), Materials.Hydrogen.getGas(6000), Materials.Oxygen.getGas(7000)}, new FluidStack[]{Materials.DinitrogenTetroxide.getGas(1000), Materials.Water.getFluid(3000)}, null, 1100, 480);

        GT_Values.RA.addChemicalRecipe(                   Materials.Ammonia.getCells(4), GT_Utility.getIntegratedCircuit(1),  Materials.Oxygen.getGas(10000), Materials.Water.getFluid(6000), Materials.NitricOxide.getCells(4), 320);
        GT_Values.RA.addChemicalRecipe(                   Materials.Oxygen.getCells(10), GT_Utility.getIntegratedCircuit(1),  Materials.Ammonia.getGas(4000), Materials.Water.getFluid(6000), Materials.NitricOxide.getCells(4), Materials.Empty.getCells(6), 320);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Ammonia.getCells(4), Materials.Empty.getCells(2),         Materials.Oxygen.getGas(10000), Materials.NitricOxide.getGas(4000), Materials.Water.getCells(6), GT_Values.NI, 320, 30);
        GT_Values.RA.addChemicalRecipe(                   Materials.Oxygen.getCells(10), GT_Utility.getIntegratedCircuit(11), Materials.Ammonia.getGas(4000), Materials.NitricOxide.getGas(4000), Materials.Water.getCells(6), Materials.Empty.getCells(4), 320);
        GT_Values.RA.addChemicalRecipe(                   Materials.Ammonia.getCells(4), GT_Utility.getIntegratedCircuit(2),  Materials.Oxygen.getGas(10000), GT_Values.NF, Materials.NitricOxide.getCells(4), 320);
        GT_Values.RA.addChemicalRecipe(                   Materials.Oxygen.getCells(10), GT_Utility.getIntegratedCircuit(2),  Materials.Ammonia.getGas(4000), GT_Values.NF, Materials.NitricOxide.getCells(4), Materials.Empty.getCells(6), 320);
        GT_Values.RA.addChemicalRecipe(                   Materials.Oxygen.getCells(10), GT_Utility.getIntegratedCircuit(12), Materials.Ammonia.getGas(4000), Materials.NitricOxide.getGas(4000), Materials.Empty.getCells(10), 320);


        GT_Values.RA.addChemicalRecipe(Materials.NitricOxide.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Oxygen.getGas(1000),      Materials.NitrogenDioxide.getGas(1000), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(1),      GT_Utility.getIntegratedCircuit(1),  Materials.NitricOxide.getGas(1000), Materials.NitrogenDioxide.getGas(1000), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.NitricOxide.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Oxygen.getGas(1000),      GT_Values.NF,                           Materials.NitrogenDioxide.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(1),      GT_Utility.getIntegratedCircuit(11), Materials.NitricOxide.getGas(1000), GT_Values.NF,                           Materials.NitrogenDioxide.getCells(1), 160);

        GT_Values.RA.addChemicalRecipe(                   Materials.Water.getCells(1),           GT_Utility.getIntegratedCircuit(1),  Materials.NitrogenDioxide.getGas(3000), Materials.NitricAcid.getFluid(2000), Materials.NitricOxide.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(                   Materials.NitrogenDioxide.getCells(3), GT_Utility.getIntegratedCircuit(1),  Materials.Water.getFluid(1000),         Materials.NitricAcid.getFluid(2000), Materials.NitricOxide.getCells(1), Materials.Empty.getCells(2), 240);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Water.getCells(1),           Materials.Empty.getCells(1),         Materials.NitrogenDioxide.getGas(3000), Materials.NitricOxide.getGas(1000),  Materials.NitricAcid.getCells(2), GT_Values.NI, 240, 30);
        GT_Values.RA.addChemicalRecipe(                   Materials.NitrogenDioxide.getCells(3), GT_Utility.getIntegratedCircuit(11), Materials.Water.getFluid(1000),         Materials.NitricOxide.getGas(1000),  Materials.NitricAcid.getCells(2), Materials.Empty.getCells(1), 240);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.NitrogenDioxide.getCells(2), 	Materials.Oxygen.getCells(1), 			Materials.Water.getFluid(1000), 		Materials.NitricAcid.getFluid(2000), Materials.Empty.getCells(3), GT_Values.NI,240, 30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Oxygen.getCells(1), 			Materials.Water.getCells(1), 			Materials.NitrogenDioxide.getGas(2000), Materials.NitricAcid.getFluid(2000), Materials.Empty.getCells(2), GT_Values.NI,240, 30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Water.getCells(1), 			Materials.NitrogenDioxide.getCells(2), 	Materials.Oxygen.getGas(1000), 			Materials.NitricAcid.getFluid(2000), Materials.Empty.getCells(3), GT_Values.NI,240, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Hydrogen.getGas(3000), Materials.Nitrogen.getGas(1000), Materials.Oxygen.getGas(4000)}, new FluidStack[]{Materials.NitricAcid.getFluid(1000), Materials.Water.getFluid(1000)}, null, 320, 480);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Ammonia.getGas(1000), Materials.Oxygen.getGas(4000)}, new FluidStack[]{Materials.NitricAcid.getFluid(1000), Materials.Water.getFluid(1000)}, null, 320, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.NitrogenDioxide.getGas(2000), Materials.Oxygen.getGas(1000), Materials.Water.getFluid(1000)}, new FluidStack[]{Materials.NitricAcid.getFluid(2000)}, null, 320, 30);

        GT_Values.RA.addChemicalRecipe(Materials.Sulfur.getDust(1), GT_Utility.getIntegratedCircuit(1), Materials.Hydrogen.getGas(2000), Materials.HydricSulfide.getGas(1000), GT_Values.NI, 60, 8);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Sulfur.getDust(1), Materials.Empty.getCells(1), Materials.Hydrogen.getGas(2000), GT_Values.NF, Materials.HydricSulfide.getCells(1), GT_Values.NI, 60, 8);

        GT_Values.RA.addChemicalRecipe(Materials.Sulfur.getDust(1),         GT_Utility.getIntegratedCircuit(3), Materials.Oxygen.getGas(2000), Materials.SulfurDioxide.getGas(1000),  GT_Values.NI, 60, 8);

        GT_Values.RA.addChemicalRecipe(Materials.HydricSulfide.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Oxygen.getGas(3000),        Materials.SulfurDioxide.getGas(1000), Materials.Water.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(3),        GT_Utility.getIntegratedCircuit(1),  Materials.HydricSulfide.getGas(1000), Materials.SulfurDioxide.getGas(1000), Materials.Water.getCells(1), Materials.Empty.getCells(2), 120);
        GT_Values.RA.addChemicalRecipe(Materials.HydricSulfide.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Oxygen.getGas(3000),        Materials.Water.getFluid(1000),       Materials.SulfurDioxide.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(3),        GT_Utility.getIntegratedCircuit(11), Materials.HydricSulfide.getGas(1000), Materials.Water.getFluid(1000),       Materials.SulfurDioxide.getCells(1), Materials.Empty.getCells(2), 120);
        GT_Values.RA.addChemicalRecipe(Materials.HydricSulfide.getCells(1), GT_Utility.getIntegratedCircuit(2),  Materials.Oxygen.getGas(3000),        Materials.SulfurDioxide.getGas(1000), Materials.Empty.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(3),        GT_Utility.getIntegratedCircuit(2),  Materials.HydricSulfide.getGas(1000), Materials.SulfurDioxide.getGas(1000), Materials.Empty.getCells(3), 120);
        GT_Values.RA.addChemicalRecipe(Materials.HydricSulfide.getCells(1), GT_Utility.getIntegratedCircuit(12), Materials.Oxygen.getGas(3000),        GT_Values.NF,                         Materials.SulfurDioxide.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(3),        GT_Utility.getIntegratedCircuit(12), Materials.HydricSulfide.getGas(1000), GT_Values.NF,                         Materials.SulfurDioxide.getCells(1), Materials.Empty.getCells(2), 120);

        GT_Values.RA.addChemicalRecipe(Materials.SulfurDioxide.getCells(1), GT_Utility.getIntegratedCircuit(1), Materials.HydricSulfide.getGas(2000), Materials.Water.getFluid(2000), Materials.Sulfur.getDust(3), Materials.Empty.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.HydricSulfide.getCells(2), GT_Utility.getIntegratedCircuit(1), Materials.SulfurDioxide.getGas(1000), Materials.Water.getFluid(2000), Materials.Sulfur.getDust(3), Materials.Empty.getCells(2), 120);
        GT_Values.RA.addChemicalRecipe(Materials.SulfurDioxide.getCells(1), GT_Utility.getIntegratedCircuit(2), Materials.HydricSulfide.getGas(2000), GT_Values.NF,                   Materials.Sulfur.getDust(3), Materials.Empty.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.HydricSulfide.getCells(2), GT_Utility.getIntegratedCircuit(2), Materials.SulfurDioxide.getGas(1000), GT_Values.NF,                   Materials.Sulfur.getDust(3), Materials.Empty.getCells(2), 120);

        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(1),        GT_Utility.getIntegratedCircuit(1),  Materials.SulfurDioxide.getGas(1000), Materials.SulfurTrioxide.getGas(1000), Materials.Empty.getCells(1), 200);
        GT_Values.RA.addChemicalRecipe(Materials.SulfurDioxide.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Oxygen.getGas(1000),        Materials.SulfurTrioxide.getGas(1000), Materials.Empty.getCells(1), 200);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(1),        GT_Utility.getIntegratedCircuit(11), Materials.SulfurDioxide.getGas(1000), GT_Values.NF,                          Materials.SulfurTrioxide.getCells(1), 200);
        GT_Values.RA.addChemicalRecipe(Materials.SulfurDioxide.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Oxygen.getGas(1000),        GT_Values.NF,                          Materials.SulfurTrioxide.getCells(1), 200);

        GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(1),          GT_Utility.getIntegratedCircuit(1),  Materials.SulfurTrioxide.getGas(1000), Materials.SulfuricAcid.getFluid(2000), Materials.Empty.getCells(1), 320, 16);
        GT_Values.RA.addChemicalRecipe(Materials.SulfurTrioxide.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Water.getFluid(1000),        Materials.SulfuricAcid.getFluid(2000), Materials.Empty.getCells(1), 320, 16);
        //GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(1),          GT_Utility.getIntegratedCircuit(11), Materials.SulfurTrioxide.getGas(1000), GT_Values.NF,                          Materials.SulfuricAcid.getCells(1), 320, 8);
        //GT_Values.RA.addChemicalRecipe(Materials.SulfurTrioxide.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Water.getFluid(1000),        GT_Values.NF,                          Materials.SulfuricAcid.getCells(1), 320, 8);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Sulfur.getDust(1), GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Oxygen.getGas(3000),        Materials.Water.getFluid(1000)},                                new FluidStack[]{Materials.SulfuricAcid.getFluid(1000)}, null, 480, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)},                              new FluidStack[]{Materials.HydricSulfide.getGas(1000), Materials.Oxygen.getGas(3000)},                                 new FluidStack[]{Materials.SulfuricAcid.getFluid(1000)}, null, 480, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)},                              new FluidStack[]{Materials.SulfurDioxide.getGas(1000), Materials.Oxygen.getGas(1000), Materials.Water.getFluid(1000)}, new FluidStack[]{Materials.SulfuricAcid.getFluid(1000)}, null, 480, 30);

        GT_Values.RA.addUniversalDistillationRecipe(Materials.DilutedSulfuricAcid.getFluid(3000), new FluidStack[]{Materials.SulfuricAcid.getFluid(2000), Materials.Water.getFluid(1000)}, GT_Values.NI, 600, 120);

//        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(1),  Materials.Ethylene.getGas(1000), Materials.VinylChloride.getGas(1000),      Materials.HydrochloricAcid.getCells(1), Materials.Empty.getCells(1), 160);
//        GT_Values.RA.addChemicalRecipe(Materials.Ethylene.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Chlorine.getGas(2000), Materials.VinylChloride.getGas(1000),      Materials.HydrochloricAcid.getCells(1), 160);
//        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(11), Materials.Ethylene.getGas(1000), Materials.HydrochloricAcid.getFluid(1000), Materials.VinylChloride.getCells(1), Materials.Empty.getCells(1), 160);
//        GT_Values.RA.addChemicalRecipe(Materials.Ethylene.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Chlorine.getGas(2000), Materials.HydrochloricAcid.getFluid(1000), Materials.VinylChloride.getCells(1), 160);

//        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Ethylene.getCells(1), 			Materials.HydrochloricAcid.getCells(1), Materials.Oxygen.getGas(1000), 				Materials.VinylChloride.getGas(1000), Materials.Water.getCells(1), Materials.Empty.getCells(1), 160, 30);
//        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.HydrochloricAcid.getCells(1), 	Materials.Oxygen.getCells(1), 			Materials.Ethylene.getGas(1000), 			Materials.VinylChloride.getGas(1000), Materials.Water.getCells(1), Materials.Empty.getCells(1), 160, 30);
//        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Oxygen.getCells(1), 			Materials.Ethylene.getCells(1), 		Materials.HydrochloricAcid.getFluid(1000),  Materials.VinylChloride.getGas(1000), Materials.Water.getCells(1), Materials.Empty.getCells(1), 160, 30);

//        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(2)}, new FluidStack[]{Materials.HydrochloricAcid.getFluid(1000), Materials.Ethylene.getGas(1000), Materials.Oxygen.getGas(1000)}, new FluidStack[]{Materials.VinylChloride.getGas(1000), Materials.Water.getFluid(1000)}, null, 160, 30);
//        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Chlorine.getGas(2000), Materials.Ethylene.getGas(2000), Materials.Oxygen.getGas(1000)}, new FluidStack[]{Materials.VinylChloride.getGas(2000), Materials.Water.getFluid(1000)}, null, 240, 30);

        GT_Values.RA.addDefaultPolymerizationRecipes(Materials.VinylChloride.mGas, Materials.VinylChloride.getCells(1), Materials.PolyvinylChloride.mStandardMoltenFluid);

        GT_Values.RA.addMixerRecipe(Materials.Sugar.getDust(4), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.SulfuricAcid.getFluid(1000), Materials.DilutedSulfuricAcid.getFluid(1000), Materials.Charcoal.getGems(1), 1200, 2);
        GT_Values.RA.addMixerRecipe(Materials.Wood.getDust(4), 	GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.SulfuricAcid.getFluid(1000), Materials.DilutedSulfuricAcid.getFluid(1000), Materials.Charcoal.getGems(1), 1200, 2);

//        GT_Values.RA.addUniversalDistillationRecipe(Materials.Acetone.getFluid(1000), new FluidStack[]{Materials.Ethenone.getGas(1000), Materials.Methane.getGas(1000)}, GT_Values.NI, 80, 640);
        GT_Values.RA.addFluidHeaterRecipe(GT_Utility.getIntegratedCircuit(1), Materials.Acetone.getFluid(1000), Materials.Ethenone.getGas(1000), 160, 160);
        //GameRegistry.addSmelting(Materials.Acetone.getCells(1), Materials.Ethenone.getCells(1), 0);
        GT_Values.RA.addChemicalRecipe(Materials.AceticAcid.getCells(1),   GT_Utility.getIntegratedCircuit(1),  Materials.SulfuricAcid.getFluid(1000), Materials.DilutedSulfuricAcid.getFluid(1000), Materials.Ethenone.getCells(1), 160, 120);
        GT_Values.RA.addChemicalRecipe(Materials.SulfuricAcid.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.AceticAcid.getFluid(1000),   Materials.DilutedSulfuricAcid.getFluid(1000), Materials.Ethenone.getCells(1), 160, 120);
        GT_Values.RA.addChemicalRecipe(Materials.AceticAcid.getCells(1),   GT_Utility.getIntegratedCircuit(11), Materials.SulfuricAcid.getFluid(1000), Materials.Ethenone.getGas(1000), Materials.DilutedSulfuricAcid.getCells(1), 160, 120);
        GT_Values.RA.addChemicalRecipe(Materials.SulfuricAcid.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.AceticAcid.getFluid(1000),   Materials.Ethenone.getGas(1000), Materials.DilutedSulfuricAcid.getCells(1), 160, 120);

        GT_Values.RA.addChemicalRecipe(Materials.Ethenone.getCells(1),   Materials.Empty.getCells(1),         Materials.NitricAcid.getFluid(8000), Materials.Water.getFluid(9000),             Materials.Tetranitromethane.getCells(2), 480, 120);
        GT_Values.RA.addChemicalRecipe(Materials.Ethenone.getCells(1),   GT_Utility.getIntegratedCircuit(12), Materials.NitricAcid.getFluid(8000), Materials.Tetranitromethane.getFluid(2000), Materials.Empty.getCells(1), 480, 120);
        GT_Values.RA.addChemicalRecipe(Materials.NitricAcid.getCells(8), GT_Utility.getIntegratedCircuit(1),  Materials.Ethenone.getGas(1000),     Materials.Water.getFluid(9000),             Materials.Tetranitromethane.getCells(2), Materials.Empty.getCells(6), 480, 120);
        GT_Values.RA.addChemicalRecipe(Materials.NitricAcid.getCells(8), GT_Utility.getIntegratedCircuit(2),  Materials.Ethenone.getGas(1000),     GT_Values.NF,                               Materials.Tetranitromethane.getCells(2), Materials.Empty.getCells(6), 480, 120);
        GT_Values.RA.addChemicalRecipe(Materials.NitricAcid.getCells(8), GT_Utility.getIntegratedCircuit(12), Materials.Ethenone.getGas(1000),     Materials.Tetranitromethane.getFluid(2000), Materials.Empty.getCells(8), 480, 120);
        GT_Values.RA.addChemicalRecipe(Materials.NitricAcid.getCells(8), Materials.Empty.getCells(1),         Materials.Ethenone.getGas(1000),     Materials.Tetranitromethane.getFluid(2000), Materials.Water.getCells(9), 480, 120);
        GT_Values.RA.addChemicalRecipe(Materials.Ethenone.getCells(1),   Materials.NitricAcid.getCells(8),    GT_Values.NF,                        Materials.Tetranitromethane.getFluid(2000), Materials.Water.getCells(9), 480, 120);

        GT_Values.RA.addMixerRecipe(Materials.Fuel.getCells(1),      GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Tetranitromethane.getFluid(20), Materials.NitroFuel.getFluid(1000), Materials.Empty.getCells(1), 20, 480);
        GT_Values.RA.addMixerRecipe(Materials.BioDiesel.getCells(2), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Tetranitromethane.getFluid(40), Materials.NitroFuel.getFluid(750), Materials.Empty.getCells(2), 20, 480);
        GT_Values.RA.addChemicalRecipe(ItemList.Cell_Air.get(1), GT_Utility.getIntegratedCircuit(1), Materials.Isoprene.getFluid(144),  GT_Values.NF, Materials.RawRubber.getDust(1),  Materials.Empty.getCells(1),  160);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(2),            GT_Utility.getIntegratedCircuit(1), Materials.Isoprene.getFluid(288),  GT_Values.NF, Materials.RawRubber.getDust(3),  Materials.Empty.getCells(2),  320);
        GT_Values.RA.addChemicalRecipe(Materials.Isoprene.getCells(1),          GT_Utility.getIntegratedCircuit(1), Materials.Air.getGas(14000),       GT_Values.NF, Materials.RawRubber.getDust(7),  Materials.Empty.getCells(1),  1120);
        GT_Values.RA.addChemicalRecipe(Materials.Isoprene.getCells(2),          GT_Utility.getIntegratedCircuit(1), Materials.Oxygen.getGas(14000),    GT_Values.NF, Materials.RawRubber.getDust(21),  Materials.Empty.getCells(2), 2240);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(2)}, new FluidStack[]{Materials.Isoprene.getFluid(1728), Materials.Air.getGas(6000), Materials.Titaniumtetrachloride.getFluid(80)}, null, new ItemStack[]{Materials.RawRubber.getDust(18)}, 640, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(2)}, new FluidStack[]{Materials.Isoprene.getFluid(1728), Materials.Oxygen.getGas(6000), Materials.Titaniumtetrachloride.getFluid(80)}, null, new ItemStack[]{Materials.RawRubber.getDust(24)}, 640, 30);

        GT_Values.RA.addDefaultPolymerizationRecipes(Materials.Styrene.mFluid, Materials.Styrene.getCells(1), Materials.Polystyrene.mStandardMoltenFluid);

        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(3)}, new FluidStack[]{Materials.Styrene.getFluid(36),  Materials.Butadiene.getGas(108), Materials.Air.getGas(2000)},                                                     null, new ItemStack[]{Materials.RawStyreneButadieneRubber.getDust(1)}, 160, 240);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(3)}, new FluidStack[]{Materials.Styrene.getFluid(72),  Materials.Butadiene.getGas(216), Materials.Oxygen.getGas(2000)},                                                  null, new ItemStack[]{Materials.RawStyreneButadieneRubber.getDust(3)}, 160, 240);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(4)}, new FluidStack[]{Materials.Styrene.getFluid(540), Materials.Butadiene.getGas(1620), Materials.Titaniumtetrachloride.getFluid(100), Materials.Air.getGas(15000)},    null, new ItemStack[]{Materials.RawStyreneButadieneRubber.getDust(22), Materials.RawStyreneButadieneRubber.getDustSmall(2)}, 640, 240);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(4)}, new FluidStack[]{Materials.Styrene.getFluid(540), Materials.Butadiene.getGas(1620), Materials.Titaniumtetrachloride.getFluid(100), Materials.Oxygen.getGas(7500)},  null, new ItemStack[]{Materials.RawStyreneButadieneRubber.getDust(30)}, 640, 240);

        GT_Values.RA.addChemicalRecipe(Materials.RawStyreneButadieneRubber.getDust(9), Materials.Sulfur.getDust(1), GT_Values.NF, Materials.StyreneButadieneRubber.getMolten(1296), GT_Values.NI, 600);

        GT_Values.RA.addChemicalRecipe(Materials.SodiumSulfide.getDust(1), ItemList.Cell_Air.get(8), Materials.Dichlorobenzene.getFluid(1000), Materials.PolyphenyleneSulfide.getMolten(1000), Materials.Salt.getDust(2), Materials.Empty.getCells(8), 240, 360);
        GT_Values.RA.addChemicalRecipe(Materials.SodiumSulfide.getDust(1), Materials.Oxygen.getCells(8),            Materials.Dichlorobenzene.getFluid(1000), Materials.PolyphenyleneSulfide.getMolten(1500), Materials.Salt.getDust(2), Materials.Empty.getCells(8), 240, 360);

        GT_Values.RA.addChemicalRecipe(Materials.Salt.getDust(2), GT_Utility.getIntegratedCircuit(1), Materials.SulfuricAcid.getFluid(1000), Materials.HydrochloricAcid.getFluid(1000), Materials.SodiumBisulfate.getDust(1), 60);
        GT_Values.RA.addElectrolyzerRecipe(Materials.SodiumBisulfate.getDust(2), Materials.Empty.getCells(2), null, Materials.SodiumPersulfate.getFluid(1000), Materials.Hydrogen.getCells(2), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 600, 30);

        GT_Values.RA.addChemicalRecipe(Materials.SodiumHydroxide.getDust(1), Materials.Methanol.getCells(9), Materials.SeedOil.getFluid(54000), Materials.BioDiesel.getFluid(54000), Materials.Glycerol.getCells(9), 5400);
        GT_Values.RA.addChemicalRecipe(Materials.SodiumHydroxide.getDust(1), Materials.SeedOil.getCells(54), Materials.Methanol.getFluid(9000), Materials.Glycerol.getFluid(9000), Materials.BioDiesel.getCells(54), 5400);
        GT_Values.RA.addChemicalRecipe(Materials.SodiumHydroxide.getDust(1), Materials.Methanol.getCells(9), Materials.FishOil.getFluid(54000), Materials.BioDiesel.getFluid(54000), Materials.Glycerol.getCells(9), 5400);
        GT_Values.RA.addChemicalRecipe(Materials.SodiumHydroxide.getDust(1), Materials.FishOil.getCells(54), Materials.Methanol.getFluid(9000), Materials.Glycerol.getFluid(9000), Materials.BioDiesel.getCells(54), 5400);
        GT_Values.RA.addChemicalRecipe(Materials.SodiumHydroxide.getDust(1), Materials.Ethanol.getCells(9), Materials.SeedOil.getFluid(54000), Materials.BioDiesel.getFluid(54000), Materials.Glycerol.getCells(9), 5400);
        GT_Values.RA.addChemicalRecipe(Materials.SodiumHydroxide.getDust(1), Materials.SeedOil.getCells(54), Materials.Ethanol.getFluid(9000), Materials.Glycerol.getFluid(9000), Materials.BioDiesel.getCells(54), 5400);
        GT_Values.RA.addChemicalRecipe(Materials.SodiumHydroxide.getDust(1), Materials.Ethanol.getCells(9), Materials.FishOil.getFluid(54000), Materials.BioDiesel.getFluid(54000), Materials.Glycerol.getCells(9), 5400);
        GT_Values.RA.addChemicalRecipe(Materials.SodiumHydroxide.getDust(1), Materials.FishOil.getCells(54), Materials.Ethanol.getFluid(9000), Materials.Glycerol.getFluid(9000), Materials.BioDiesel.getCells(54), 5400);
        
        

        GT_Values.RA.addChemicalRecipe(                   Materials.Glycerol.getCells(1),         GT_Utility.getIntegratedCircuit(1),  Materials.NitrationMixture.getFluid(3000), Materials.DilutedSulfuricAcid.getFluid(3000), Materials.Glyceryl.getCells(1), 180);
        GT_Values.RA.addChemicalRecipe(                   Materials.NitrationMixture.getCells(3), GT_Utility.getIntegratedCircuit(1),  Materials.Glycerol.getFluid(1000),         Materials.DilutedSulfuricAcid.getFluid(3000), Materials.Glyceryl.getCells(1), Materials.Empty.getCells(2), 180);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Glycerol.getCells(1),         Materials.Empty.getCells(2),         Materials.NitrationMixture.getFluid(3000), Materials.Glyceryl.getFluid(1000),            Materials.DilutedSulfuricAcid.getCells(3), GT_Values.NI, 180, 30);
        GT_Values.RA.addChemicalRecipe(                   Materials.NitrationMixture.getCells(3), GT_Utility.getIntegratedCircuit(11), Materials.Glycerol.getFluid(1000),         Materials.Glyceryl.getFluid(1000),            Materials.DilutedSulfuricAcid.getCells(3), 180);

        GT_Values.RA.addChemicalRecipe(Materials.Quicklime.getDust(2), GT_Values.NI,                       Materials.CarbonDioxide.getGas(1000), GT_Values.NF,                         Materials.Calcite.getDust(5), 80);
        GT_Values.RA.addChemicalRecipe(Materials.Calcite.getDust(5),   GT_Utility.getIntegratedCircuit(1), GT_Values.NF,                         Materials.CarbonDioxide.getGas(1000), Materials.Quicklime.getDust(2), 240);
        GT_Values.RA.addChemicalRecipe(Materials.Magnesia.getDust(1),  GT_Values.NI,                       Materials.CarbonDioxide.getGas(1000), GT_Values.NF,                         Materials.Magnesite.getDust(1), 80);
        GT_Values.RA.addChemicalRecipe(Materials.Magnesite.getDust(1), GT_Utility.getIntegratedCircuit(1), GT_Values.NF,                         Materials.CarbonDioxide.getGas(1000), Materials.Magnesia.getDust(1), 240);

        GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(1),         GT_Utility.getIntegratedCircuit(1),  Materials.Chlorobenzene.getFluid(1000), Materials.Phenol.getFluid(1000),                  Materials.DilutedHydrochloricAcid.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.Chlorobenzene.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Water.getFluid(1000),         Materials.Phenol.getFluid(1000),                  Materials.DilutedHydrochloricAcid.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(1),         GT_Utility.getIntegratedCircuit(11), Materials.Chlorobenzene.getFluid(1000), Materials.DilutedHydrochloricAcid.getFluid(1000), Materials.Phenol.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.Chlorobenzene.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Water.getFluid(1000),         Materials.DilutedHydrochloricAcid.getFluid(1000), Materials.Phenol.getCells(1), 240);

        GT_Values.RA.addChemicalRecipe(                   Materials.SodiumHydroxide.getDust(4), GT_Utility.getIntegratedCircuit(1),  Materials.Chlorobenzene.getFluid(4000), Materials.Phenol.getFluid(4000), Materials.Salt.getDust(6), 960);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.SodiumHydroxide.getDust(4), Materials.Empty.getCells(4),         Materials.Chlorobenzene.getFluid(4000), GT_Values.NF,                    Materials.Salt.getDust(6), Materials.Phenol.getCells(4), 960, 30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.SodiumHydroxide.getDust(4), Materials.Chlorobenzene.getCells(4), GT_Values.NF,                           GT_Values.NF,                    Materials.Salt.getDust(6), Materials.Phenol.getCells(4), 960, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Fuel.getFluid(10000), Materials.Tetranitromethane.getFluid(200)}, new FluidStack[]{Materials.NitroFuel.getFluid(10000)}, null, 120, 480);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.BioDiesel.getFluid(20000), Materials.Tetranitromethane.getFluid(400)}, new FluidStack[]{Materials.NitroFuel.getFluid(7500)}, null, 120, 480);
    }

    private void addRecipesMay2017OilRefining() {

        //Recipes for gasoline
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Nitrogen.getCells(2), Materials.Oxygen.getCells(1), GT_Values.NF, GT_Values.NF, Materials.NitrousOxide.getCells(3), GT_Values.NI,200, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Nitrogen.getGas(20000), Materials.Oxygen.getGas(10000)}, new FluidStack[]{Materials.NitrousOxide.getGas(30000)}, new ItemStack[]{null}, 50, 480);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Ethanol.getCells(1), Materials.Butene.getCells(1), GT_Values.NF, GT_Values.NF, Materials.AntiKnock.getCells(2), GT_Values.NI, 400, 480);
        //GT_Values.RA.addMixerRecipe(Materials.Naphtha.getCells(16), Materials.Gas.getCells(2), Materials.Methanol.getCells(1), Materials.Acetone.getCells(1), GT_Values.NF, GT_Values.NF, Materials.GasolineRaw.getCells(20), 100, 480);
        //GT_Values.RA.addChemicalRecipe(Materials.GasolineRaw.getCells(10), Materials.Toluene.getCells(1), GT_Values.NF, GT_Values.NF, Materials.GasolineRegular.getCells(11), 10, 480);
        //GT_Values.RA.addMixerRecipe(Materials.GasolineRegular.getCells(20), Materials.Octane.getCells(2), Materials.NitrousOxide.getCells(6), Materials.Toluene.getCells(1), Materials.AntiKnock.getFluid(3000L), Materials.GasolinePremium.getFluid(32000L), Materials.Empty.getCells(29), 50, 1920);

        //GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Naphtha.getFluid(16000), Materials.Gas.getGas(2000), Materials.Methanol.getFluid(1000), Materials.Acetone.getFluid(1000)}, new FluidStack[]{ Materials.GasolineRaw.getFluid(20000)}, null, 100, 480);
        //GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.GasolineRaw.getFluid(10000), Materials.Toluene.getFluid(1000)}, new FluidStack[]{ Materials.GasolineRegular.getFluid(11000)}, null, 10, 480);
        //GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.GasolineRegular.getFluid(20000), Materials.Octane.getFluid(2000), Materials.NitrousOxide.getGas(6000), Materials.Toluene.getFluid(1000), Materials.AntiKnock.getFluid(3000L)},   new FluidStack[]{Materials.GasolinePremium.getFluid(32000L)}, null, 50, 1920);
        //GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Ethanol.getFluid(1000), Materials.Butene.getGas(1000)}, new FluidStack[]{Materials.AntiKnock.getFluid(2000)}, null,400, 480);

    }

	public void addPotionRecipes(String aName,ItemStack aItem){
		//normal
		GT_Values.RA.addBrewingRecipe(aItem, FluidRegistry.getFluid("potion.awkward"), FluidRegistry.getFluid("potion."+aName), false);
		//strong
		 GT_Values.RA.addBrewingRecipe(aItem, FluidRegistry.getFluid("potion.thick"), FluidRegistry.getFluid("potion."+aName+".strong"), false);
		//long
		 GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), FluidRegistry.getFluid("potion."+aName), FluidRegistry.getFluid("potion."+aName+".long"), false);
		//splash
		 if(!(FluidRegistry.getFluid("potion."+aName)==null||FluidRegistry.getFluid("potion."+aName+".splash")==null))
		 GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1L), null, null, null, new FluidStack(FluidRegistry.getFluid("potion."+aName),750), new FluidStack(FluidRegistry.getFluid("potion."+aName+".splash"),750), null, 200, 24);
		//splash strong
		 if(!(FluidRegistry.getFluid("potion."+aName+".strong")==null||FluidRegistry.getFluid("potion."+aName+".strong.splash")==null))
		 GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1L), null, null, null, new FluidStack(FluidRegistry.getFluid("potion."+aName+".strong"),750), new FluidStack(FluidRegistry.getFluid("potion."+aName+".strong.splash"),750), null, 200, 24);
		//splash long
		 if(!(FluidRegistry.getFluid("potion."+aName+".long")==null||FluidRegistry.getFluid("potion."+aName+".long.splash")==null))
		 GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1L), null, null, null, new FluidStack(FluidRegistry.getFluid("potion."+aName+".long"),750), new FluidStack(FluidRegistry.getFluid("potion."+aName+".long.splash"),750), null, 200, 24);
	}

        /**
         * Adds recipes related to producing Polybenzimidazole.
     	 */
            private void addPolybenzimidazoleRecipes() {

            	// Chrome Trioxide
            	GT_Values.RA.addChemicalRecipe(Materials.ChromiumDioxide.getDust(1), GT_Utility.getIntegratedCircuit(1), Materials.Oxygen.getGas(1000), GT_Values.NF, Materials.ChromiumTrioxide.getDust(1), GT_Values.NI,100, 60);

            	//Potassium Dichromate
            	GT_Values.RA.addChemicalRecipe(Materials.Saltpeter.getDust(2), Materials.ChromiumTrioxide.getDust(2), Materials.Potassiumdichromate.getDust(1), 100, 480);

            	//Nitrochlorobenzene
            	GT_Values.RA.addChemicalRecipe(Materials.Chlorobenzene.getCells(1), GT_Utility.getIntegratedCircuit(1), Materials.NitrationMixture.getFluid(1000), Materials.DilutedSulfuricAcid.getFluid(1000), Materials.Nitrochlorobenzene.getCells(1), 100, 480);
            	GT_Values.RA.addChemicalRecipe(Materials.Chlorobenzene.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.NitrationMixture.getFluid(1000), Materials.Nitrochlorobenzene.getFluid(1000), Materials.DilutedSulfuricAcid.getCells(1), 100, 480);
            	GT_Values.RA.addChemicalRecipe(Materials.NitrationMixture.getCells(1), GT_Utility.getIntegratedCircuit(1), Materials.Chlorobenzene.getFluid(1000), Materials.DilutedSulfuricAcid.getFluid(1000), Materials.Nitrochlorobenzene.getCells(1), 100, 480);
            	GT_Values.RA.addChemicalRecipe(Materials.NitrationMixture.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Chlorobenzene.getFluid(1000), Materials.Nitrochlorobenzene.getFluid(1000), Materials.DilutedSulfuricAcid.getCells(1), 100, 480);

            	//Dimethylbenzene
            	GT_Values.RA.addDistilleryRecipe(5, Materials.WoodTar.getFluid(200), Materials.Dimethylbenzene.getFluid(40), 100, 120, false);
            	GT_Values.RA.addDistilleryRecipe(5, Materials.CharcoalByproducts.getGas(200), Materials.Dimethylbenzene.getFluid(20), 100, 120, false);

            	GT_Values.RA.addChemicalRecipe(Materials.Dimethylbenzene.getCells(9), Materials.Potassiumdichromate.getDust(1), Materials.Oxygen.getGas(18000), Materials.Water.getFluid(18000), Materials.PhthalicAcid.getCells(9), 900, 1920);
            	GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(18), Materials.Potassiumdichromate.getDust(1), Materials.Dimethylbenzene.getFluid(9000), Materials.Water.getFluid(18000), Materials.PhthalicAcid.getCells(9), ItemList.Cell_Empty.get(9L), 900, 1920);
            	
            	//Dichlorobenzidine
            	GT_Values.RA.addChemicalRecipe(Materials.Copper.getDust(1), GT_Utility.getIntegratedCircuit(9), Materials.Nitrochlorobenzene.getFluid(9000), Materials.Dichlorobenzidine.getFluid(9000), null, 1800, 1920);

            	//Diphenyl Isophthalate
            	GT_Values.RA.addChemicalRecipe(Materials.PhthalicAcid.getCells(1),Materials.SulfuricAcid.getCells(1),Materials.Phenol.getFluid(2000), Materials.DilutedSulfuricAcid.getFluid(1000),Materials.Diphenylisophthalate.getCells(1), ItemList.Cell_Empty.get(1L),100, 7680);
            	GT_Values.RA.addChemicalRecipe(Materials.PhthalicAcid.getCells(1),Materials.Phenol.getCells(2),Materials.SulfuricAcid.getFluid(1000), Materials.DilutedSulfuricAcid.getFluid(1000),Materials.Diphenylisophthalate.getCells(1), ItemList.Cell_Empty.get(2L), 100, 7680);
            	GT_Values.RA.addChemicalRecipe(Materials.SulfuricAcid.getCells(1),Materials.Phenol.getCells(2),Materials.PhthalicAcid.getFluid(1000), Materials.DilutedSulfuricAcid.getFluid(1000),Materials.Diphenylisophthalate.getCells(1), ItemList.Cell_Empty.get(2L), 100, 7680);

            	//Diaminobenzidin
            	GT_Values.RA.addChemicalRecipe(Materials.Ammonia.getCells(2), Materials.Zinc.getDust(1), Materials.Dichlorobenzidine.getFluid(1000), Materials.HydrochloricAcid.getFluid(2000), Materials.Diaminobenzidin.getCells(1), ItemList.Cell_Empty.get(1L),100, 7680);

            	//Polybenzimidazole
            	GT_Values.RA.addChemicalRecipe(Materials.Diphenylisophthalate.getCells(1), GT_Utility.getIntegratedCircuit(1), Materials.Diaminobenzidin.getFluid(1000), Materials.Polybenzimidazole.getMolten(1000), Materials.Phenol.getCells(1), 100, 7680);
            	GT_Values.RA.addChemicalRecipe(Materials.Diaminobenzidin.getCells(1), GT_Utility.getIntegratedCircuit(1), Materials.Diphenylisophthalate.getFluid(1000), Materials.Polybenzimidazole.getMolten(1000), Materials.Phenol.getCells(1), 100, 7680);
            }

    /**
     * Load all Railcraft recipes for GT Machines
     */
    private void loadRailcraftRecipes() {
        if (!Loader.isModLoaded(MOD_ID_RC)) return;
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{new ItemStack(Blocks.stone_slab, 1, 0), ItemList.RC_Rebar.get(1L), GT_Utility.getIntegratedCircuit(1)}, GT_Values.NF, ItemList.RC_Tie_Stone.get(1L), 128, 8);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{new ItemStack(Blocks.stone_slab, 1, 7), ItemList.RC_Rebar.get(1L), GT_Utility.getIntegratedCircuit(1)}, GT_Values.NF, ItemList.RC_Tie_Stone.get(1L), 128, 8);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 3L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3L), GT_Utility.getIntegratedCircuit(8)}, Materials.Blaze.getMolten(216L), ItemList.RC_Rail_HS.get(16L), 100, 16);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 3L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3L), GT_Utility.getIntegratedCircuit(9)}, Materials.ConductiveIron.getMolten(432L), ItemList.RC_Rail_HS.get(8L), 100, 16);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 3L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3L), GT_Utility.getIntegratedCircuit(9)}, Materials.VibrantAlloy.getMolten(216L), ItemList.RC_Rail_HS.get(32L), 100, 30);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 3L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3L), GT_Utility.getIntegratedCircuit(9)}, Materials.CrystallineAlloy.getMolten(216L), ItemList.RC_Rail_HS.get(64L), 100, 48);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.RC_Rail_Standard.get(3L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3L), GT_Utility.getIntegratedCircuit(8)}, Materials.Redstone.getMolten(216L), ItemList.RC_Rail_Adv.get(8L), 100, 16);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.RC_Rail_Standard.get(3L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3L), GT_Utility.getIntegratedCircuit(8)}, Materials.RedAlloy.getMolten(216L), ItemList.RC_Rail_Adv.get(16L), 100, 16);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.RC_Rail_Standard.get(3L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3L), GT_Utility.getIntegratedCircuit(8)}, Materials.ConductiveIron.getMolten(216L), ItemList.RC_Rail_Adv.get(32L), 100, 30);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.RC_Rail_Standard.get(3L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3L), GT_Utility.getIntegratedCircuit(8)}, Materials.VibrantAlloy.getMolten(216L), ItemList.RC_Rail_Adv.get(64L), 100, 48);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.RC_Rail_Standard.get(1L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Copper, 1L), GT_Utility.getIntegratedCircuit(1)}, GT_Values.NF, ItemList.RC_Rail_Electric.get(1L), 50, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.RC_Rail_Standard.get(1L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.AnnealedCopper, 1L), GT_Utility.getIntegratedCircuit(1)}, GT_Values.NF, ItemList.RC_Rail_Electric.get(1L), 50, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.RC_Rail_Standard.get(1L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Gold, 1L), GT_Utility.getIntegratedCircuit(1)}, GT_Values.NF, ItemList.RC_Rail_Electric.get(2L), 50, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.RC_Rail_Standard.get(1L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Electrum, 1L), GT_Utility.getIntegratedCircuit(1)}, GT_Values.NF, ItemList.RC_Rail_Electric.get(4L), 50, 16);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.RC_Rail_Standard.get(1L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Titanium, 1L), GT_Utility.getIntegratedCircuit(1)}, GT_Values.NF, ItemList.RC_Rail_Electric.get(8L), 50, 30);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.RC_Rail_Standard.get(1L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.TungstenSteel, 1L), GT_Utility.getIntegratedCircuit(1)}, GT_Values.NF, ItemList.RC_Rail_Electric.get(16L), 50, 48);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.RC_Rail_Standard.get(1L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.HSSG, 1L), GT_Utility.getIntegratedCircuit(1)}, GT_Values.NF, ItemList.RC_Rail_Electric.get(32L), 50, 64);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.RC_Rail_Standard.get(1L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Naquadah, 1L), GT_Utility.getIntegratedCircuit(1)}, GT_Values.NF, ItemList.RC_Rail_Electric.get(64L), 50, 96);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.RC_Tie_Wood.get(1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 1L), GT_Utility.getIntegratedCircuit(10)}, GT_Values.NF, ItemList.RC_Rail_Wooden.get(8L), 133, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.RC_Tie_Wood.get(1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 1L), GT_Utility.getIntegratedCircuit(11)}, GT_Values.NF, ItemList.RC_Rail_Wooden.get(8L), 133, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.RC_Tie_Wood.get(1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 1L), GT_Utility.getIntegratedCircuit(11)}, GT_Values.NF, ItemList.RC_Rail_Wooden.get(16L), 133, 16);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.RC_Tie_Wood.get(1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 1L), GT_Utility.getIntegratedCircuit(11)}, GT_Values.NF, ItemList.RC_Rail_Wooden.get(32L), 133, 30);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.RC_Tie_Wood.get(1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 1L), GT_Utility.getIntegratedCircuit(11)}, GT_Values.NF, ItemList.RC_Rail_Wooden.get(64L), 133, 48);
        GT_Values.RA.addAssemblerRecipe(ItemList.RC_Tie_Wood.get(32L), GT_Utility.getIntegratedCircuit(20), ItemList.RC_Bed_Wood.get(24L), 200, 30);
        GT_Values.RA.addAssemblerRecipe(ItemList.RC_Tie_Wood.get(64L), GT_Utility.getIntegratedCircuit(24), ItemList.RC_Bed_Wood.get(48L), 200, 48);
        GT_Values.RA.addAssemblerRecipe(ItemList.RC_Tie_Stone.get(32L), GT_Utility.getIntegratedCircuit(20), ItemList.RC_Bed_Stone.get(24L), 200, 30);
        GT_Values.RA.addAssemblerRecipe(ItemList.RC_Tie_Stone.get(64L), GT_Utility.getIntegratedCircuit(24), ItemList.RC_Bed_Stone.get(48L), 200, 48);

        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 20L), (GT_Utility.getIntegratedCircuit(10)), ItemList.RC_Rail_Standard.get(64L), 300, 24);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 48L), (GT_Utility.getIntegratedCircuit(10)), ItemList.RC_Rail_Standard.get(64L), 300, 24);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 32L), (GT_Utility.getIntegratedCircuit(10)), ItemList.RC_Rail_Standard.get(64L), 300, 24);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Bronze, 32L), (GT_Utility.getIntegratedCircuit(10)), ItemList.RC_Rail_Standard.get(64L), 300, 24);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 24L), (GT_Utility.getIntegratedCircuit(10)), ItemList.RC_Rail_Standard.get(64L), 300, 24);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Aluminium, 16L), (GT_Utility.getIntegratedCircuit(10)), ItemList.RC_Rail_Standard.get(64L), 300, 24);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 12L), (GT_Utility.getIntegratedCircuit(10)), ItemList.RC_Rail_Standard.get(64L), 300, 24);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 9L), (GT_Utility.getIntegratedCircuit(10)), ItemList.RC_Rail_Standard.get(64L), 300, 24);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 6L), (GT_Utility.getIntegratedCircuit(10)), ItemList.RC_Rail_Standard.get(64L), 300, 24);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmium, 3L), (GT_Utility.getIntegratedCircuit(10)), ItemList.RC_Rail_Standard.get(64L), 300, 24);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmiridium, 2L), (GT_Utility.getIntegratedCircuit(10)), ItemList.RC_Rail_Standard.get(64L), 300, 24);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Obsidian, 24L), (GT_Utility.getIntegratedCircuit(11)), ItemList.RC_Rail_Reinforced.get(64L), 600, 24);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Aluminium, 12L), (GT_Utility.getIntegratedCircuit(11)), ItemList.RC_Rail_Reinforced.get(64L), 600, 24);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 6L), (GT_Utility.getIntegratedCircuit(11)), ItemList.RC_Rail_Reinforced.get(64L), 600, 24);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 3L), (GT_Utility.getIntegratedCircuit(11)), ItemList.RC_Rail_Reinforced.get(64L), 600, 24);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmium, 1L), (GT_Utility.getIntegratedCircuit(11)), ItemList.RC_Rail_Reinforced.get(64L), 600, 24);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 20L), (GT_Utility.getIntegratedCircuit(12)), ItemList.RC_Rebar.get(64L), 200, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 48L), (GT_Utility.getIntegratedCircuit(12)), ItemList.RC_Rebar.get(64L), 200, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 24L), (GT_Utility.getIntegratedCircuit(12)), ItemList.RC_Rebar.get(64L), 200, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Bronze, 32L), (GT_Utility.getIntegratedCircuit(12)), ItemList.RC_Rebar.get(64L), 200, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 16L), (GT_Utility.getIntegratedCircuit(12)), ItemList.RC_Rebar.get(64L), 200, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Aluminium, 12L), (GT_Utility.getIntegratedCircuit(12)), ItemList.RC_Rebar.get(64L), 200, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 8), (GT_Utility.getIntegratedCircuit(12)), ItemList.RC_Rebar.get(64L), 200, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 6L), (GT_Utility.getIntegratedCircuit(12)), ItemList.RC_Rebar.get(64L), 200, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 4L), (GT_Utility.getIntegratedCircuit(12)), ItemList.RC_Rebar.get(64L), 200, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmium, 2L), (GT_Utility.getIntegratedCircuit(12)), ItemList.RC_Rebar.get(64L), 200, 15);

        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 1L), Materials.Glass.getMolten(864L), GT_Values.NF, GT_ModHandler.getModItem(MOD_ID_RC, "tile.railcraft.glass", 6L), 50);

        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.log, Materials.Wood, 1L), Materials.Creosote.getFluid(100L), GT_ModHandler.getModItem(MOD_ID_RC, "cube", 1L, 8), GT_Values.NI, GT_Values.NI, null, 100, 4);

        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Anvil.get(0L), Materials.Steel.getMolten(4464L), GT_ModHandler.getModItem(MOD_ID_RC, "tile.railcraft.anvil", 1L, 0), 128, 16);

        GT_ModHandler.addPulverisationRecipe(GT_ModHandler.getModItem(MOD_ID_RC, "cube.crushed.obsidian", 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Obsidian, 1L), GT_Values.NI, 0, true);

        GT_Values.RA.addMixerRecipe(EnumCube.COKE_BLOCK.getItem(), ItemList.SFMixture.get(1), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(250), null, ItemList.Block_SSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(EnumCube.COKE_BLOCK.getItem(), ItemList.SFMixture.get(1), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.HeavyFuel.getFluid(375), null, ItemList.Block_SSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(EnumCube.COKE_BLOCK.getItem(), ItemList.SFMixture.get(1), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.LPG.getFluid(375), null, ItemList.Block_SSFUEL.get(1), 120, 96);
        GT_Values.RA.addMixerRecipe(EnumCube.COKE_BLOCK.getItem(), ItemList.SFMixture.get(1), null, null, Materials.LPG.getFluid(375), null, ItemList.Block_SSFUEL.get(1), 120, 96);

        if (Loader.isModLoaded(MOD_ID_TC)) {
            GT_Values.RA.addMixerRecipe(EnumCube.COKE_BLOCK.getItem(), ItemList.MSFMixture.get(1), GT_ModHandler.getModItem(MOD_ID_TC, "ItemResource", 4), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(250), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
            GT_Values.RA.addMixerRecipe(EnumCube.COKE_BLOCK.getItem(), ItemList.MSFMixture.get(1), GT_ModHandler.getModItem(MOD_ID_TC, "ItemResource", 4), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.HeavyFuel.getFluid(375), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
            GT_Values.RA.addMixerRecipe(EnumCube.COKE_BLOCK.getItem(), ItemList.MSFMixture.get(1), GT_ModHandler.getModItem(MOD_ID_TC, "ItemResource", 4), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.LPG.getFluid(375), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
            GT_Values.RA.addMixerRecipe(EnumCube.COKE_BLOCK.getItem(), ItemList.MSFMixture.get(1), GT_ModHandler.getModItem(MOD_ID_TC, "ItemResource", 4), null, Materials.LPG.getFluid(375), null, ItemList.Block_MSSFUEL.get(1), 120, 96);
        }
    }
}
