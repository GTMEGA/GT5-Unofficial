package gregtech.common;

import cpw.mods.fml.common.Loader;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.internal.IGT_RecipeAdder;
import gregtech.api.objects.GT_FluidStack;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_AssemblyLine;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_IntegratedCircuit_Item;
import lombok.val;
import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;
import mods.railcraft.common.items.RailcraftToolItems;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static gregtech.GT_Mod.GT_FML_LOGGER;

public class GT_RecipeAdder implements IGT_RecipeAdder {


    @Override
    @Deprecated
    public GT_Recipe addFusionReactorRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt, int aStartEU) {
        return null;
    }

    @Override //Really?
    public GT_Recipe addFusionReactorRecipeRemovable(FluidStack aInput1, FluidStack aInput2, FluidStack aOutput1, int aDuration, int aEUt, int aStartEU) {
        if (aInput1 == null || aInput2 == null || aOutput1 == null || aDuration < 1 || aEUt < 1 || aStartEU < 1) {
            return null;
        }
        if ((aOutput1 != null) && ((aDuration = GregTech_API.sRecipeFile.get("fusion", aOutput1.getFluid().getName(), aDuration)) <= 0)) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sFusionRecipes.addRecipe(null, new FluidStack[]{aInput1, aInput2}, new FluidStack[]{aOutput1}, aDuration, aEUt, aStartEU);
    }

    @Override
    public void removeFusionReactorRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sFusionRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addCentrifugeRecipeRemovable(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int aDuration) {
        return addCentrifugeRecipeRemovable(aInput1, aInput2 < 0 ? ItemList.IC2_Fuel_Can_Empty.get(-aInput2, new Object[0]) : aInput2 > 0 ? ItemList.Cell_Empty.get(aInput2, new Object[0]) : null, null, null, aOutput1, aOutput2, aOutput3, aOutput4, aOutput5, aOutput6, null, aDuration, 5);
    }

    @Override
    public GT_Recipe addCentrifugeRecipeRemovable(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int aDuration, int aEUt) {
        return addCentrifugeRecipeRemovable(aInput1, aInput2 < 0 ? ItemList.IC2_Fuel_Can_Empty.get(-aInput2, new Object[0]) : aInput2 > 0 ? ItemList.Cell_Empty.get(aInput2, new Object[0]) : null, null, null, aOutput1, aOutput2, aOutput3, aOutput4, aOutput5, aOutput6, null, aDuration, aEUt);
    }

    @Override
    public GT_Recipe addCentrifugeRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt) {
        return addCentrifugeRecipeRemovable(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput1, aOutput2, aOutput3, aOutput4, aOutput5, aOutput6, aChances, aDuration, aEUt, false);
    }

    @Override
    public GT_Recipe addCentrifugeRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt, boolean aCleanroom) {
        if (((aInput1 == null) && (aFluidInput == null)) || ((aOutput1 == null) && (aFluidOutput == null))) {
            return null;
        }
        if ((aInput1 != null) && ((aDuration = GregTech_API.sRecipeFile.get("centrifuge", aInput1, aDuration)) <= 0)) {
            return null;
        }
        if ((aFluidInput != null) && ((aDuration = GregTech_API.sRecipeFile.get("centrifuge", aFluidInput.getFluid().getName(), aDuration)) <= 0)) {
            return null;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }
        return GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2}, new ItemStack[]{aOutput1, aOutput2, aOutput3, aOutput4, aOutput5, aOutput6,}, null, aChances, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUt, aCleanroom ? -100 : 0);
    }

    @Override
    public void removeCentrifugeRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addCompressorRecipeRemovable(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return null;
        }
        if ((aInput1 != null) && ((aDuration = GregTech_API.sRecipeFile.get("compressor", aInput1, aDuration)) <= 0)) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sCompressorRecipes.addRecipe(true, new ItemStack[]{aInput1}, new ItemStack[]{aOutput1}, null, null, null, aDuration, aEUt, 0);
    }

    @Override
    public void removeCompressorRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sCompressorRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addElectrolyzerRecipeRemovable(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int aDuration, int aEUt) {
        return addElectrolyzerRecipeRemovable(aInput1, aInput2 < 0 ? ItemList.IC2_Fuel_Can_Empty.get(-aInput2, new Object[0]) : aInput2 > 0 ? ItemList.Cell_Empty.get(aInput2, new Object[0]) : null, null, null, aOutput1, aOutput2, aOutput3, aOutput4, aOutput5, aOutput6, null, aDuration, aEUt);
    }

    @Override
    public GT_Recipe addElectrolyzerRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt) {
        if (((aInput1 == null) && (aFluidInput == null)) || ((aOutput1 == null) && (aFluidOutput == null))) {
            return null;
        }
        if ((aInput1 != null) && ((aDuration = GregTech_API.sRecipeFile.get("electrolyzer", aInput1, aDuration)) <= 0)) {
            return null;
        }
        if ((aFluidInput != null) && ((aDuration = GregTech_API.sRecipeFile.get("electrolyzer", aFluidInput.getFluid().getName(), aDuration)) <= 0)) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2}, new ItemStack[]{aOutput1, aOutput2, aOutput3, aOutput4, aOutput5, aOutput6}, null, aChances, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
    }

    @Override
    public void removeElectrolyzerRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe[] addChemicalRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput, int aDuration) {
        return addChemicalRecipeRemovable(aInput1, aInput2, null, null, aOutput, aDuration);
    }

    @Override
    public GT_Recipe[] addChemicalRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput, int aDuration, int aEUt) {
        return addChemicalRecipeRemovable(aInput1, aInput2, null, null, aOutput, aDuration, aEUt);
    }

    @Override
    public GT_Recipe[] addChemicalRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration) {
        return addChemicalRecipeRemovable(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aDuration, 30);
    }

    @Override
    public GT_Recipe[] addChemicalRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration) {
        return addChemicalRecipeRemovable(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aOutput2, aDuration, 30);
    }

    @Override
    public GT_Recipe[] addChemicalRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUTick) {
        return addChemicalRecipeRemovable(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, GT_Values.NI, aDuration, aEUTick);
    }

    @Override
    public GT_Recipe[] addChemicalRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick) {
        return addChemicalRecipeRemovable(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aOutput2, aDuration, aEUtick, false);
    }

    @Override
    public GT_Recipe[] addChemicalRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick, boolean aCleanroom) {
        if (((aInput1 == null) && (aFluidInput == null)) || ((aOutput == null) && (aOutput2 == null) && (aFluidOutput == null))) {
            return null;
        }
        if ((aOutput != null || aOutput2 != null) && ((aDuration = GregTech_API.sRecipeFile.get("chemicalreactor", aOutput, aDuration)) <= 0)) {
            return null;
        }
        if ((aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get("chemicalreactor", aFluidOutput.getFluid().getName(), aDuration)) <= 0)) {
            return null;
        }
        if (aEUtick <= 0) {
            return null;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }
        val recipes = new GT_Recipe[2];
        recipes[0] = GT_Recipe.GT_Recipe_Map.sChemicalRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2}, new ItemStack[]{aOutput, aOutput2}, null, null, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUtick, aCleanroom ? -200 : 0);
        if (!(aInput1 != null && aInput1.getItem() instanceof GT_IntegratedCircuit_Item && aInput1.getItemDamage() >= 10)
                && !(aInput2 != null && aInput2.getItem() instanceof GT_IntegratedCircuit_Item && aInput2.getItemDamage() >= 10)) {
            recipes[1] = GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.addRecipe(false, new ItemStack[]{aInput1, aInput2}, new ItemStack[]{aOutput, aOutput2}, null, null, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUtick, 0);
        }
        return recipes;
    }

    @Override
    public void removeChemicalRecipe(GT_Recipe[] recipe) {
        GT_Recipe.GT_Recipe_Map.sChemicalRecipes.remove(recipe[0]);
        if (recipe[1] != null) {
            GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.remove(recipe[1]);
        }
    }

    @Override
    public GT_Recipe addMultiblockChemicalRecipeRemovable(ItemStack[] aInputs, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, ItemStack[] aOutputs, int aDuration, int aEUtick) {
        if (areItemsAndFluidsBothNull(aInputs, aFluidInputs) || areItemsAndFluidsBothNull(aOutputs, aFluidOutputs)) {
            return null;
        }
        if (aEUtick <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.addRecipe(false, aInputs, aOutputs, null, null, aFluidInputs, aFluidOutputs, aDuration, aEUtick, 0);
    }

    @Override
    public void removeMultiblockChemicalRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addChemicalRecipeForBasicMachineOnlyRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick) {
        if (((aInput1 == null) && (aFluidInput == null)) || ((aOutput == null) && (aOutput2 == null) && (aFluidOutput == null))) {
            return null;
        }
        if ((aOutput != null || aOutput2 != null) && ((aDuration = GregTech_API.sRecipeFile.get("chemicalreactor", aOutput, aDuration)) <= 0)) {
            return null;
        }
        if ((aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get("chemicalreactor", aFluidOutput.getFluid().getName(), aDuration)) <= 0)) {
            return null;
        }
        if (aEUtick <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sChemicalRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2}, new ItemStack[]{aOutput, aOutput2}, null, null, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUtick, 0);
    }

    @Override
    public void removeChemicalRecipeForBasicMachineOnly(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sChemicalRecipes.remove(recipe);
    }

    @Override
    public void addDefaultPolymerizationRecipes(Fluid aBasicMaterial, ItemStack aBasicMaterialCell, Fluid aPolymer) {
        //Oxygen/Titaniumtetrafluoride -> +50% Output each
        addChemicalRecipe(ItemList.Cell_Air.get(1, new Object[0]), GT_Utility.getIntegratedCircuit(1), new GT_FluidStack(aBasicMaterial, 144), new GT_FluidStack(aPolymer, 144), Materials.Empty.getCells(1), 160);
        addChemicalRecipe(Materials.Oxygen.getCells(1), GT_Utility.getIntegratedCircuit(1), new GT_FluidStack(aBasicMaterial, 144), new GT_FluidStack(aPolymer, 216), Materials.Empty.getCells(1), 160);
        addChemicalRecipe(aBasicMaterialCell, GT_Utility.getIntegratedCircuit(1), Materials.Air.getGas(14000), new GT_FluidStack(aPolymer, 1000), Materials.Empty.getCells(1), 1120);
        addChemicalRecipe(aBasicMaterialCell, GT_Utility.getIntegratedCircuit(1), Materials.Oxygen.getGas(7000), new GT_FluidStack(aPolymer, 1500), Materials.Empty.getCells(1), 1120);
        addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(2)},
                new FluidStack[]{new GT_FluidStack(aBasicMaterial, 2160), Materials.Air.getGas(7500), Materials.Titaniumtetrachloride.getFluid(100)},
                new FluidStack[]{new GT_FluidStack(aPolymer, 3240)}, null, 800, 30);
        addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(2)},
                new FluidStack[]{new GT_FluidStack(aBasicMaterial, 2160), Materials.Oxygen.getGas(7500), Materials.Titaniumtetrachloride.getFluid(100)},
                new FluidStack[]{new GT_FluidStack(aPolymer, 4320)}, null, 800, 30);
    }


    @Override
    public GT_Recipe addBlastRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, int aLevel) {
        return addBlastRecipeRemovable(aInput1, aInput2, null, null, aOutput1, aOutput2, aDuration, aEUt, aLevel);
    }

    @Override
    public GT_Recipe addBlastRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, int aLevel) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("blastfurnace", aInput1, aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sBlastRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2}, new ItemStack[]{aOutput1, aOutput2}, null, null,
                new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUt, aLevel);
    }

    @Override
    public void removeBlastRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sBlastRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe[] addPrimitiveBlastRecipeRemovable(ItemStack aInput1, ItemStack aInput2, int aCoalAmount, ItemStack aOutput1, ItemStack aOutput2, int aDuration) {
        if ((aInput1 == null && aInput2 == null) || (aOutput1 == null && aOutput2 == null)) {
            return null;
        }
        if (aCoalAmount <= 0) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("primitiveblastfurnace", aInput1, aDuration)) <= 0) {
            return null;
        }
        Materials[] coals = new Materials[]{Materials.Coal, Materials.Charcoal};
        val recipes = new ArrayList<GT_Recipe>();
        for (Materials coal : coals) {
            val r1 = GT_Recipe.GT_Recipe_Map.sPrimitiveBlastRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2, coal.getGems(aCoalAmount)}, new ItemStack[]{aOutput1, aOutput2, Materials.DarkAsh.getDust(aCoalAmount)}, null, null, null, null, aDuration, 0, 0);
            val r2 = GT_Recipe.GT_Recipe_Map.sPrimitiveBlastRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2, coal.getDust(aCoalAmount)}, new ItemStack[]{aOutput1, aOutput2, Materials.DarkAsh.getDust(aCoalAmount)}, null, null, null, null, aDuration, 0, 0);
            if (r1 != null) {
                recipes.add(r1);
            }
            if (r2 != null) {
                recipes.add(r2);
            }
        }
        if (Loader.isModLoaded("Railcraft")) {
            val r = GT_Recipe.GT_Recipe_Map.sPrimitiveBlastRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2, RailcraftToolItems.getCoalCoke(aCoalAmount / 2)}, new ItemStack[]{aOutput1, aOutput2, Materials.Ash.getDust(aCoalAmount / 2)}, null, null, null, null, aDuration * 2 / 3, 0, 0);
            if (r != null) {
                recipes.add(r);
            }
        }
        if ((aInput1 == null || aInput1.stackSize <= 6) && (aInput2 == null || aInput2.stackSize <= 6) &&
                (aOutput1 == null || aOutput1.stackSize <= 6) && (aOutput2 == null || aOutput2.stackSize <= 6)) {
            aInput1 = aInput1 == null ? null : GT_Utility.copyAmount(aInput1.stackSize * 10, aInput1);
            aInput2 = aInput2 == null ? null : GT_Utility.copyAmount(aInput2.stackSize * 10, aInput2);
            aOutput1 = aOutput1 == null ? null : GT_Utility.copyAmount(aOutput1.stackSize * 10, aOutput1);
            aOutput2 = aOutput2 == null ? null : GT_Utility.copyAmount(aOutput2.stackSize * 10, aOutput2);
            for (Materials coal : coals) {
                val r1 = GT_Recipe.GT_Recipe_Map.sPrimitiveBlastRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2, coal.getBlocks(aCoalAmount)}, new ItemStack[]{aOutput1, aOutput2, Materials.DarkAsh.getDust(aCoalAmount)}, null, null, null, null, aDuration * 10, 0, 0);
                val r2 = GT_Recipe.GT_Recipe_Map.sPrimitiveBlastRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2, coal.getBlocks(aCoalAmount)}, new ItemStack[]{aOutput1, aOutput2, Materials.DarkAsh.getDust(aCoalAmount)}, null, null, null, null, aDuration * 10, 0, 0);
                if (r1 != null) {
                    recipes.add(r1);
                }
                if (r2 != null) {
                    recipes.add(r2);
                }
            }
            if (Loader.isModLoaded("Railcraft")) {
                val r = GT_Recipe.GT_Recipe_Map.sPrimitiveBlastRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2, EnumCube.COKE_BLOCK.getItem(aCoalAmount / 2)}, new ItemStack[]{aOutput1, aOutput2, Materials.Ash.getDust(aCoalAmount / 2)}, null, null, null, null, aDuration * 20 / 3, 0, 0);
                if (r != null) {
                    recipes.add(r);
                }
            }
        }
        return recipes.isEmpty() ? null : recipes.toArray(new GT_Recipe[0]);
    }

    @Override
    public void removePrimitiveBlastRecipe(GT_Recipe[] recipe) {
        for (val r: recipe) {
            GT_Recipe.GT_Recipe_Map.sPrimitiveBlastRecipes.remove(r);
        }
    }

    @Override
    public GT_Recipe addCannerRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("canning", aInput1, aDuration)) <= 0) {
            return null;
        }
        return new GT_Recipe(aInput1, aEUt, aInput2, aDuration, aOutput1, aOutput2);
    }

    @Override
    public void removeCannerRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sCannerRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addAlloySmelterRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt) {
        return addAlloySmelterRecipeRemovable(aInput1, aInput2, aOutput1, aDuration, aEUt, false);
    }

    @Override
    public GT_Recipe addAlloySmelterRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt, boolean hidden) {
        if ((aInput1 == null) || (aOutput1 == null || Materials.Graphite.contains(aInput1))) {
            return null;
        }
        if ((aInput2 == null) && ((OrePrefixes.ingot.contains(aInput1)) || (OrePrefixes.dust.contains(aInput1)) || (OrePrefixes.gem.contains(aInput1)))) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("alloysmelting", aInput2 == null ? aInput1 : aOutput1, aDuration)) <= 0) {
            return null;
        }
        GT_Recipe tRecipe = new GT_Recipe(aInput1, aInput2, aEUt, aDuration, aOutput1);
        if ((hidden) && (tRecipe != null)) {
            tRecipe.mHidden = true;
        }
        return tRecipe;
    }

    @Override
    public void removeAlloySmelterRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes.remove(recipe);
    }

    @Override
    @Deprecated
    public GT_Recipe addCNCRecipeRemovable(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("cnc", aOutput1, aDuration)) <= 0) {
            return null;
        }
        return null;
    }

    @Override
    public void removeCNCRecipe(GT_Recipe recipe) {

    }

    @Override
    public GT_Recipe addLatheRecipeRemovable(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("lathe", aInput1, aDuration)) <= 0) {
            return null;
        }
        return new GT_Recipe(aInput1, aOutput1, aOutput2, aDuration, aEUt);
    }

    @Override
    public void removeLatheRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sLatheRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe[] addCutterRecipeRemovable(ItemStack aInput, FluidStack aLubricant, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
        if ((aInput == null) || (aLubricant == null) || (aOutput1 == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("cutting", aInput, aDuration)) <= 0) {
            return null;
        }
        val r = GT_Recipe.GT_Recipe_Map.sCutterRecipes.addRecipe(true, new ItemStack[]{aInput}, new ItemStack[]{aOutput1, aOutput2}, null, new FluidStack[]{aLubricant}, null, aDuration, aEUt, 0);
        return r == null ? null : new GT_Recipe[]{r};
    }

    @Override
    public void removeCutterRecipe(GT_Recipe[] recipe) {
        for (val r: recipe) {
            GT_Recipe.GT_Recipe_Map.sCutterRecipes.remove(r);
        }
    }

    @Override
    public GT_Recipe[] addCutterRecipeRemovable(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, boolean aCleanroom) {
        return addCutterRecipeRemovable(aInput, null, aOutput1, aOutput2, aDuration, aEUt, aCleanroom);
    }

    public GT_Recipe[] addCutterRecipeRemovable(ItemStack aInput, int aCircuit, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
        return addCutterRecipeRemovable(aInput, aCircuit, aOutput1, aOutput2, aDuration, aEUt, false);
    }

    public GT_Recipe[] addCutterRecipeRemovable(ItemStack aInput, int aCircuit, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, boolean aCleanroom) {
        return addCutterRecipeRemovable(aInput, GT_Utility.getIntegratedCircuit(aCircuit), aOutput1, aOutput2, aDuration, aEUt, aCleanroom);
    }

    @Override
    public GT_Recipe[] addCutterRecipeRemovable(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
        return addCutterRecipeRemovable(aInput, aOutput1, aOutput2, aDuration, aEUt, false);
    }

    @Override
    public GT_Recipe[] addCutterRecipeRemovable(ItemStack aInput, ItemStack aCircuit, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
        return addCutterRecipeRemovable(aInput, aCircuit, aOutput1, aOutput2, aDuration, aEUt, false);
    }

    @Override
    public GT_Recipe[] addCutterRecipeRemovable(ItemStack aInput, ItemStack aCircuit, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, boolean aCleanroom) {
        if (aInput == null || (aOutput1 == null && aOutput2 == null)) return null;
        return addCutterRecipeRemovable(new ItemStack[]{aInput, aCircuit}, new ItemStack[]{aOutput1, aOutput2}, aDuration, aEUt, aCleanroom ? -200 : 0);
    }

    public GT_Recipe[] addCutterRecipeRemovable(ItemStack[] aInputs, ItemStack[] aOutputs, int aDuration, int aEUt, boolean aCleanroom) {
        return addCutterRecipeRemovable(aInputs, aOutputs, aDuration, aEUt, aCleanroom ? -200 : 0);
    }

    @Override
    public GT_Recipe[] addCutterRecipeRemovable(ItemStack[] aInputs, ItemStack[] aOutputs, int aDuration, int aEUt, int aSpecial) {
        if ((aInputs == null) || (aOutputs == null) || aInputs.length == 0 || aOutputs.length == 0) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("cutting", aInputs[0], aDuration)) <= 0) {
            return null;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom && aSpecial == -200) {
            aSpecial = 0;
        }
        val r1 = GT_Recipe.GT_Recipe_Map.sCutterRecipes.addRecipe(true, aInputs, aOutputs, null, new FluidStack[]{Materials.Water.getFluid(Math.max(4, Math.min(1000, aDuration * aEUt / 320)))}, null, aDuration * 4, aEUt, aSpecial);
//        GT_Recipe.GT_Recipe_Map.sCutterRecipes.addRecipe(true, aInputs, aOutputs, null, new FluidStack[]{GT_ModHandler.getDistilledWater(Math.max(2, Math.min(500, aDuration * aEUt / 512)))}, null, aDuration * 4, aEUt, aSpecial);
        val r2 = GT_Recipe.GT_Recipe_Map.sCutterRecipes.addRecipe(true, aInputs, aOutputs, null, new FluidStack[]{Materials.Lubricant.getFluid(Math.max(4, Math.min(250, aDuration * aEUt / 1792)))}, null, aDuration, aEUt, aSpecial);
        if (r1 == null && r2 == null) {
            return null;
        } else if (r1 != null && r2 != null) {
            return new GT_Recipe[]{r1, r2};
        } else if (r1 != null) {
            return new GT_Recipe[]{r1};
        } else {
            return new GT_Recipe[]{r2};
        }
    }


    @Override
    public GT_Recipe[] addAssemblerRecipeRemovable(ItemStack aInput1, Object aOreDict, int aAmount, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt) {
        val recipes = new ArrayList<GT_Recipe>();
        for (ItemStack tStack : GT_OreDictUnificator.getOresImmutable(aOreDict)) {
            if (GT_Utility.isStackValid(tStack)) {
                val r = addAssemblerRecipeRemovable(aInput1, GT_Utility.copyAmount(aAmount, tStack), aFluidInput, aOutput1, aDuration, aEUt);
                if (r != null) {
                    recipes.addAll(Arrays.asList(r));
                }
            }
        }
        return recipes.isEmpty() ? null : recipes.toArray(new GT_Recipe[0]);
    }

    @Override
    public GT_Recipe[] addAssemblerRecipeRemovable(ItemStack[] aInputs, Object aOreDict, int aAmount, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt) {
        val recipes = new ArrayList<GT_Recipe>();
        for (ItemStack tStack : GT_OreDictUnificator.getOresImmutable(aOreDict)) {
            if (GT_Utility.isStackValid(tStack)) {
                ItemStack[] extendedInputs = new ItemStack[aInputs.length + 1];
                System.arraycopy(aInputs, 0, extendedInputs, 0, aInputs.length);
                extendedInputs[aInputs.length] = GT_Utility.copyAmount(aAmount, tStack);
                val r = addAssemblerRecipeRemovable(extendedInputs, aFluidInput, aOutput1, aDuration, aEUt);
                if (r != null) {
                    recipes.addAll(Arrays.asList(r));
                }
            }
        }
        return recipes.isEmpty() ? null : recipes.toArray(new GT_Recipe[0]);
    }

    @Override
    public GT_Recipe[] addAssemblerRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt) {
        return addAssemblerRecipeRemovable(new ItemStack[]{aInput1, aInput2 == null ? aInput1 : aInput2}, null, aOutput1, aDuration, aEUt, false);
    }

    @Override
    public GT_Recipe[] addAssemblerRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt) {
        return addAssemblerRecipeRemovable(new ItemStack[]{aInput1, aInput2}, aFluidInput, aOutput1, aDuration, aEUt);
    }

    @Override
    public GT_Recipe[] addAssemblerRecipeRemovable(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt) {
        return addAssemblerRecipeRemovable(aInputs, aFluidInput, aOutput1, aDuration, aEUt, false);
    }

    @Override
    public GT_Recipe[] addAssemblerRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt, boolean aCleanroom) {
        if (aInput2 == null)
            return addAssemblerRecipeRemovable(new ItemStack[]{aInput1}, aFluidInput, aOutput1, aDuration, aEUt, aCleanroom);
        return addAssemblerRecipeRemovable(new ItemStack[]{aInput1, aInput2}, aFluidInput, aOutput1, aDuration, aEUt, aCleanroom);
    }

    @Override
    public GT_Recipe[] addAssemblerRecipeRemovable(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt, boolean aCleanroom) {

        if (areItemsAndFluidsBothNull(aInputs, new FluidStack[]{aFluidInput})) {
            return null;
        }

        if ((aDuration = GregTech_API.sRecipeFile.get("assembling", aOutput1, aDuration)) <= 0) {
            return null;
        }

        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }

        if (!GT_Utility.isStackValid(aOutput1)) {
            return null;
        }

        for (int oreID : OreDictionary.getOreIDs(aOutput1)) {
            if (OreDictionary.getOreName(oreID).startsWith("circuit")) {
                return this.addAssemblerRecipeNonODRemovable(aInputs, aFluidInput, aOutput1, aDuration, aEUt, aCleanroom);
            }
        }

        val ret = new ArrayList<GT_Recipe>();

        for (int i = 0; i < aInputs.length; ++i) {
            if (!GT_Utility.isStackValid(aInputs[i])) {
                GT_FML_LOGGER.debug("GT_RecipeAdder: Invalid input for (" + aOutput1.toString() + ")");
                continue;
            }
            for (int oreID : OreDictionary.getOreIDs(aInputs[i])) {
                String odName = OreDictionary.getOreName(oreID);
                if (odName.startsWith("circuit")) {
                    for (ItemStack tStack : GT_OreDictUnificator.getOresImmutable(odName)) {
                        if (!GT_Utility.isStackValid(tStack))
                            continue;
                        aInputs[i] = new ItemStack(tStack.getItem(), aInputs[i].stackSize, tStack.getItemDamage());
                        val r = GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.addRecipe(true, aInputs, new ItemStack[]{aOutput1}, null, new FluidStack[]{aFluidInput}, null, aDuration, aEUt, aCleanroom ? -200 : 0);
                        if (r != null) {
                            ret.add(r);
                        }
                    }
                }
            }
        }

        if (ret.isEmpty()) {
            val r = GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.addRecipe(true, aInputs, new ItemStack[]{aOutput1}, null, new FluidStack[]{aFluidInput}, null, aDuration, aEUt, aCleanroom ? -200 : 0);
            if (r != null) {
                ret.add(r);
            }
        }

        return ret.isEmpty() ? null : ret.toArray(new GT_Recipe[0]);
    }

    @Override
    public void removeAssemblerRecipe(GT_Recipe[] recipe) {
        for (val r: recipe) {
            GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.remove(r);
        }
    }

    public GT_Recipe[] addAssemblerRecipeNonODRemovable(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt, boolean aCleanroom) {
        if (areItemsAndFluidsBothNull(aInputs, new FluidStack[]{aFluidInput})) {
            return null;
        }

        if ((aDuration = GregTech_API.sRecipeFile.get("assembling", aOutput1, aDuration)) <= 0) {
            return null;
        }

        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }

        if (!GT_Utility.isStackValid(aOutput1)) {
            return null;
        }

        val r = GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.addRecipe(true, aInputs, new ItemStack[]{aOutput1}, null, new FluidStack[]{aFluidInput}, null, aDuration, aEUt, aCleanroom ? -200 : 0);
        return r == null ? null : new GT_Recipe[]{r};
    }


    @Override
    public GT_Recipe addWiremillRecipeRemovable(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("wiremill", aInput, aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sWiremillRecipes.addRecipe(true, new ItemStack[]{aInput}, new ItemStack[]{aOutput}, null, null, null, aDuration, aEUt, 0);
    }

    @Override
    public void removeWiremillRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sWiremillRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addPolarizerRecipeRemovable(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("polarizer", aInput, aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sPolarizerRecipes.addRecipe(true, new ItemStack[]{aInput}, new ItemStack[]{aOutput}, null, null, null, aDuration, aEUt, 0);
    }

    @Override
    public void removePolarizerRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sPolarizerRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addBenderRecipeRemovable(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("bender", aInput1, aDuration)) <= 0) {
            return null;
        }
        return new GT_Recipe(aEUt, aDuration, aInput1, aOutput1);
    }

    @Override
    public GT_Recipe addBenderRecipeRemovable(ItemStack aInput1, ItemStack aCircuit, ItemStack aOutput1, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("bender", aInput1, aDuration)) <= 0) {
            return null;
        }
        GT_Recipe tRecipe = new GT_Recipe(new ItemStack[]{aInput1, aCircuit}, new ItemStack[]{aOutput1}, null, null, null, null, aDuration, Math.max(aEUt, 1), 0);
        return GT_Recipe.GT_Recipe_Map.sBenderRecipes.addRecipe(tRecipe);
    }

    @Override
    public void removeBenderRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sBenderRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addExtruderRecipeRemovable(ItemStack aInput, ItemStack aShape, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aShape == null) || (aOutput == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("extruder", aOutput, aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sExtruderRecipes.addRecipe(true, new ItemStack[]{aInput, aShape}, new ItemStack[]{aOutput}, null, null, null, aDuration, aEUt, 0);
    }

    @Override
    public void removeExtruderRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sExtruderRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addSlicerRecipeRemovable(ItemStack aInput, ItemStack aShape, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aShape == null) || (aOutput == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("slicer", aOutput, aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sSlicerRecipes.addRecipe(true, new ItemStack[]{aInput, aShape}, new ItemStack[]{aOutput}, null, null, null, aDuration, aEUt, 0);
    }

    @Override
    public void removeSlicerRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sSlicerRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addOreWasherRecipeRemovable(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, FluidStack aFluidInput, int aDuration, int aEUt) {
        if ((aInput == null) || (aFluidInput == null) || ((aOutput1 == null) || (aOutput2 == null) || (aOutput3 == null))) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("orewasher", aInput, aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sOreWasherRecipes.addRecipe(true, new ItemStack[]{aInput}, new ItemStack[]{aOutput1, aOutput2, aOutput3}, null, new FluidStack[]{aFluidInput}, null, aDuration, aEUt, 0);
    }

    @Override
    public void removeOreWasherRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sOreWasherRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addOreWasherRecipeRemovable(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, FluidStack aFluidInput, int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aFluidInput == null) || ((aOutput1 == null) || (aOutput2 == null) || (aOutput3 == null))) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("orewasher", aInput, aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sOreWasherRecipes.addRecipe(true, new ItemStack[]{aInput}, new ItemStack[]{aOutput1, aOutput2, aOutput3}, null, aChances, new FluidStack[]{aFluidInput}, null, aDuration, aEUt, 0);
    }



    @Override
    public GT_Recipe[] addImplosionRecipeRemovable(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return null;
        }
        if ((aInput2 = GregTech_API.sRecipeFile.get("implosion", aInput1, aInput2)) <= 0) {
            return null;
        }
        int tExplosives = aInput2 > 0 ? aInput2 < 64 ? aInput2 : 64 : 1;
        int tGunpowder = tExplosives<<1;//Worst
        int tDynamite = Math.max(1, tExplosives>>1);//good
        int tTNT = tExplosives;//Slightly better
        int tITNT = Math.max(1, tExplosives>>2);//the best
        //new GT_Recipe(aInput1, aInput2, aOutput1, aOutput2);
        val recipes = new ArrayList<GT_Recipe>();
        if(tGunpowder<65){
            val r = GT_Recipe.GT_Recipe_Map.sImplosionRecipes.addRecipe(true, new ItemStack[]{aInput1, ItemList.Block_Powderbarrel.get(tGunpowder, new Object[0])}, new ItemStack[]{aOutput1, aOutput2}, null, null, null, null, 20, 30, 0);
            if (r != null) {
                recipes.add(r);
            }
        }
        if(tDynamite<17){
            val r = GT_Recipe.GT_Recipe_Map.sImplosionRecipes.addRecipe(true, new ItemStack[]{aInput1, GT_ModHandler.getIC2Item("dynamite", tDynamite, null)}, new ItemStack[]{aOutput1, aOutput2}, null, null, null, null, 20, 30, 0);
            if (r != null) {
                recipes.add(r);
            }
        }
        val r1 = GT_Recipe.GT_Recipe_Map.sImplosionRecipes.addRecipe(true, new ItemStack[]{aInput1, new ItemStack(Blocks.tnt,tTNT)}, new ItemStack[]{aOutput1, aOutput2}, null, null, null, null, 20, 30, 0);
        val r2 = GT_Recipe.GT_Recipe_Map.sImplosionRecipes.addRecipe(true, new ItemStack[]{aInput1, GT_ModHandler.getIC2Item("industrialTnt", tITNT, null)}, new ItemStack[]{aOutput1, aOutput2}, null, null, null, null, 20, 30, 0);
        if (r1 != null) {
            recipes.add(r1);
        }
        if (r2 != null) {
            recipes.add(r2);
        }

        return recipes.isEmpty() ? null : recipes.toArray(new GT_Recipe[0]);
    }

    @Override
    public void removeImplosionRecipe(GT_Recipe[] recipe) {
        for (val r: recipe) {
            GT_Recipe.GT_Recipe_Map.sImplosionRecipes.remove(r);
        }
    }

    @Override
    @Deprecated
    public GT_Recipe addDistillationRecipeRemovable(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt) {
        return null;
    }

    @Override
    public void removeDistillationRecipe(GT_Recipe recipe) {

    }

    @Override
    public GT_Recipe[][] addUniversalDistillationRecipeRemovable(FluidStack aInput, FluidStack[] aOutputs, ItemStack aOutput2, int aDuration, int aEUt) {
        val distillery = new ArrayList<GT_Recipe>();
        for (int i = 0; i < Math.min(aOutputs.length, 11); i++) {
            val r = addDistilleryRecipeRemovable(i + 1, aInput, aOutputs[i], aOutput2, aDuration * 2, aEUt / 4, false);
            if (r != null) {
                distillery.add(r);
            }
        }
        val tower = addDistillationTowerRecipeRemovable(aInput, aOutputs, aOutput2, aDuration, aEUt);
        val towerArr = tower == null ? null : new GT_Recipe[]{tower};
        val distilleryArr = distillery.isEmpty() ? null : distillery.toArray(new GT_Recipe[0]);
        return new GT_Recipe[][]{distilleryArr, towerArr};
    }

    @Override
    public void removeUniversalDistillationRecipe(GT_Recipe[][] recipe) {
        val distillery = recipe[0];
        val tower = recipe[1];
        if (distillery != null) {
            for (val r: distillery) {
                removeDistilleryRecipe(r);
            }
        }
        if (tower != null) {
            for (val r: tower) {
                removeDistillationTowerRecipe(r);
            }
        }
    }

    @Override
    public GT_Recipe addDistillationTowerRecipeRemovable(FluidStack aInput, FluidStack[] aOutputs, ItemStack aOutput2, int aDuration, int aEUt) {
        if (aInput == null || aOutputs == null || aOutputs.length < 1 || aOutputs.length > 11) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("distillation", aInput.getUnlocalizedName(), aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sDistillationRecipes.addRecipe(false, null, new ItemStack[]{aOutput2}, null, new FluidStack[]{aInput}, aOutputs, Math.max(1, aDuration), Math.max(1, aEUt), 0);
    }

    @Override
    public void removeDistillationTowerRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sDistillationRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe[] addVacuumFreezerRecipeRemovable(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("vacuumfreezer", aInput1, aDuration)) <= 0) {
            return null;
        }
        val result = new ArrayList<GT_Recipe>();
        val r1 = new GT_Recipe(aInput1, aOutput1, aDuration, aEUt, 0);//Since all other methods are taken
        result.add(r1);
        FluidStack tInputFluid = GT_Utility.getFluidForFilledItem(aInput1, true);
        FluidStack tOutputFluid = GT_Utility.getFluidForFilledItem(aOutput1, true);
        if (tInputFluid != null && tOutputFluid != null) {
            val r2 = addVacuumFreezerRecipeRemovable(tInputFluid, tOutputFluid, aDuration, aEUt);
            if (r2 != null) {
                result.addAll(Arrays.asList(r2));
            }
        }
        return result.toArray(new GT_Recipe[0]);
    }

    @Override
    public GT_Recipe[] addVacuumFreezerRecipeRemovable(ItemStack aInput1, ItemStack aOutput1, int aDuration) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("vacuumfreezer", aInput1, aDuration)) <= 0) {
            return null;
        }
        val result = new ArrayList<GT_Recipe>();
        val r1 = new GT_Recipe(aInput1, aOutput1, aDuration);
        result.add(r1);
        FluidStack tInputFluid = GT_Utility.getFluidForFilledItem(aInput1, true);
        FluidStack tOutputFluid = GT_Utility.getFluidForFilledItem(aOutput1, true);
        if (tInputFluid != null && tOutputFluid != null) {
            val r2 = addVacuumFreezerRecipeRemovable(tInputFluid, tOutputFluid, aDuration, 120);
            if (r2 != null) {
                result.addAll(Arrays.asList(r2));
            }
        }
        return result.toArray(new GT_Recipe[0]);
    }

    @Override
    public GT_Recipe[] addVacuumFreezerRecipeRemovable(FluidStack aInput1, FluidStack aOutput1, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return null;
        }
        val r = new GT_Recipe(aInput1,  aOutput1, aDuration, aEUt);
        return new GT_Recipe[]{r};
    }

    @Override
    public void removeVacuumFreezerRecipe(GT_Recipe[] recipe) {
        for (val r: recipe) {
            GT_Recipe.GT_Recipe_Map.sVacuumRecipes.remove(r);
        }
    }

    @Override
    @Deprecated
    public GT_Recipe addGrinderRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4) {
        return null;
    }

    @Override
    public void removeGrinderRecipe(GT_Recipe recipe) {

    }

    @Override
    public GT_Recipe[] addFuelRemovable(ItemStack aInput1, ItemStack aOutput1, int aEU, int aType) {
        if (aInput1 == null) {
            return null;
        }
        return GT_Recipe.createFuel(aInput1, aOutput1, GregTech_API.sRecipeFile.get("fuel_" + aType, aInput1, aEU), aType);
    }

    @Override
    public void removeFuel(GT_Recipe[] recipe, int aType) {
        switch (aType) {
            // Diesel Generator
            case 0:
                GT_Recipe.GT_Recipe_Map.sDieselFuels.remove(recipe[0]);
                GT_Recipe.GT_Recipe_Map.sLargeBoilerFakeFuels.remove(recipe[1]);
                break;
            // Gas Turbine
            case 1:
                GT_Recipe.GT_Recipe_Map.sTurbineFuels.remove(recipe[0]);
                break;
            // Thermal Generator
            case 2:
                GT_Recipe.GT_Recipe_Map.sHotFuels.remove(recipe[0]);
                break;
            // Plasma Generator
            case 4:
                GT_Recipe.GT_Recipe_Map.sPlasmaFuels.remove(recipe[0]);
                break;
            // Magic Generator
            case 5:
                GT_Recipe.GT_Recipe_Map.sMagicFuels.remove(recipe[0]);
                break;
            // Fluid Generator. Usually 3. Every wrong Type ends up in the Semifluid Generator
            default:
                GT_Recipe.GT_Recipe_Map.sDenseLiquidFuels.remove(recipe[0]);
                GT_Recipe.GT_Recipe_Map.sLargeBoilerFakeFuels.remove(recipe[1]);
                break;
        }
    }

    @Override
    public int addSonictronSoundRemovable(ItemStack aItemStack, String aSoundName) {
        if ((aItemStack == null) || (aSoundName == null) || (aSoundName.equals(""))) {
            return -1;
        }
        val index = GT_Mod.gregtechproxy.mSoundItems.size();
        GT_Mod.gregtechproxy.mSoundItems.add(aItemStack);
        GT_Mod.gregtechproxy.mSoundNames.add(aSoundName);
        if (aSoundName.startsWith("note.")) {
            GT_Mod.gregtechproxy.mSoundCounts.add(Integer.valueOf(25));
        } else {
            GT_Mod.gregtechproxy.mSoundCounts.add(Integer.valueOf(1));
        }
        return index;
    }

    @Override
    public void removeSonictronSound(int index) {
        if (index == -1)
            return;
        GT_Mod.gregtechproxy.mSoundItems.remove(index);
        GT_Mod.gregtechproxy.mSoundNames.remove(index);
        GT_Mod.gregtechproxy.mSoundCounts.remove(index);
    }

    @Override
    public GT_Recipe addForgeHammerRecipeRemovable(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return null;
        }
        if (!GregTech_API.sRecipeFile.get("forgehammer", aOutput1, true)) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sHammerRecipes.addRecipe(true, new ItemStack[]{aInput1}, new ItemStack[]{aOutput1}, null, null, null, aDuration, aEUt, 0);
    }

    @Override
    public void removeForgeHammerRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sHammerRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addBoxingRecipeRemovable(ItemStack aContainedItem, ItemStack aEmptyBox, ItemStack aFullBox, int aDuration, int aEUt) {
        if ((aContainedItem == null) || (aFullBox == null)) {
            return null;
        }
        if (!GregTech_API.sRecipeFile.get("boxing", aFullBox, true)) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sBoxinatorRecipes.addRecipe(true, new ItemStack[]{aContainedItem, aEmptyBox}, new ItemStack[]{aFullBox}, null, null, null, aDuration, aEUt, 0);
    }

    @Override
    public void removeBoxingRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sBoxinatorRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addUnboxingRecipeRemovable(ItemStack aFullBox, ItemStack aContainedItem, ItemStack aEmptyBox, int aDuration, int aEUt) {
        if ((aFullBox == null) || (aContainedItem == null)) {
            return null;
        }
        if (!GregTech_API.sRecipeFile.get("unboxing", aFullBox, true)) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes.addRecipe(true, new ItemStack[]{aFullBox}, new ItemStack[]{aContainedItem, aEmptyBox}, null, null, null, aDuration, aEUt, 0);
    }

    @Override
    public void removeUnboxingRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addThermalCentrifugeRecipeRemovable(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput1 == null)) {
            return null;
        }
        if (!GregTech_API.sRecipeFile.get("thermalcentrifuge", aInput, true)) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes.addRecipe(true, new ItemStack[]{aInput}, new ItemStack[]{aOutput1, aOutput2, aOutput3}, null, null, null, aDuration, aEUt, 0);
    }

    @Override
    public void removeThermalCentrifugeRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addThermalCentrifugeRecipeRemovable(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput1 == null)) {
            return null;
        }
        if (!GregTech_API.sRecipeFile.get("thermalcentrifuge", aInput, true)) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes.addRecipe(true, new ItemStack[]{aInput}, new ItemStack[]{aOutput1, aOutput2, aOutput3}, null, aChances, null, null, aDuration, aEUt, 0);
    }

    @Override
    public GT_Recipe addAmplifierRemovable(ItemStack aAmplifierItem, int aDuration, int aAmplifierAmountOutputted) {
        if ((aAmplifierItem == null) || (aAmplifierAmountOutputted <= 0)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("amplifier", aAmplifierItem, aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sAmplifiers.addRecipe(true, new ItemStack[]{aAmplifierItem}, null, null, null, new FluidStack[]{Materials.UUAmplifier.getFluid(aAmplifierAmountOutputted)}, aDuration, 30, 0);
    }

    @Override
    public void removeAmplifier(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sAmplifiers.remove(recipe);
    }

    @Override
    public GT_Recipe addBrewingRecipeRemovable(ItemStack aIngredient, Fluid aInput, Fluid aOutput, int aDuration, int aEUt, boolean aHidden) {
        if ((aIngredient == null) || (aInput == null) || (aOutput == null)) {
            return null;
        }
        if (!GregTech_API.sRecipeFile.get("brewing", aOutput.getUnlocalizedName(), true)) {
            return null;
        }
        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sBrewingRecipes.addRecipe(false, new ItemStack[]{aIngredient}, null, null, new FluidStack[]{new FluidStack(aInput, 750)}, new FluidStack[]{new FluidStack(aOutput, 750)}, aDuration, aEUt, 0);
        if ((aHidden) && (tRecipe != null)) {
            tRecipe.mHidden = true;
        }
        return tRecipe;
    }

    @Override
    public void removeBrewingRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sBrewingRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addBrewingRecipeRemovable(ItemStack aIngredient, Fluid aInput, Fluid aOutput, boolean aHidden) {
        return addBrewingRecipeRemovable(aIngredient, aInput, aOutput, 128, 4, aHidden);
    }

    @Override
    public GT_Recipe addBrewingRecipeCustomRemovable(ItemStack aIngredient, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt, boolean aHidden) {
        if ((aInput == null) || (aOutput == null)) {
            return null;
        }
        if (!GregTech_API.sRecipeFile.get("brewing", aOutput.getUnlocalizedName(), true)) {
            return null;
        }
        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sBrewingRecipes.addRecipe(false, new ItemStack[]{aIngredient}, null, null, new FluidStack[]{aInput}, new FluidStack[]{aOutput}, aDuration, aEUt, 0);
        if ((aHidden) && (tRecipe != null)) {
            tRecipe.mHidden = true;
        }
        return tRecipe;
    }

    @Override
    public void removeBrewingRecipeCustom(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sBrewingRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addFermentingRecipeRemovable(FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt, boolean aHidden) {
        if ((aInput == null) || (aOutput == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("fermenting", aOutput.getFluid().getUnlocalizedName(), aDuration)) <= 0) {
            return null;
        }
        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sFermentingRecipes.addRecipe(false, null, null, null, new FluidStack[]{aInput}, new FluidStack[]{aOutput}, aDuration, aEUt, 0);
        if ((aHidden) && (tRecipe != null)) {
            tRecipe.mHidden = true;
        }
        return tRecipe;
    }

    @Override
    public void removeFermentingRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sFermentingRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addFermentingRecipeRemovable(FluidStack aInput, FluidStack aOutput, int aDuration, boolean aHidden) {
        return addFermentingRecipeRemovable(aInput, aOutput, aDuration, 2, aHidden);
    }

    @Override
    public GT_Recipe addDistilleryRecipeRemovable(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, ItemStack aSolidOutput, int aDuration, int aEUt, boolean aHidden) {
        if ((aInput == null) || (aOutput == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("distillery", aOutput.getFluid().getUnlocalizedName(), aDuration)) <= 0) {
            return null;
        }
        //reduce the batch size if fluid amount is exceeding
        int tScale = (Math.max(aInput.amount, aOutput.amount) + 999) / 1000;
        if (tScale <= 0) tScale = 1;
        if (tScale > 1){
            //trying to find whether there is a better factor
            for (int i = tScale; i <= 5; i++) {
                if (aInput.amount % i == 0 && aDuration % i == 0) {
                    tScale = i;
                    break;
                }
            }
            for (int i = tScale; i <= 5; i++) {
                if (aInput.amount % i == 0 && aDuration % i == 0 && aOutput.amount % i == 0) {
                    tScale = i;
                    break;
                }
            }
            aInput = new FluidStack(aInput.getFluid(), (aInput.amount + tScale - 1) / tScale);
            aOutput = new FluidStack(aOutput.getFluid(), aOutput.amount / tScale);
            if (aSolidOutput != null) {
                ItemData tData = GT_OreDictUnificator.getItemData(aSolidOutput);
                if (tData != null && (tData.mPrefix == OrePrefixes.dust || OrePrefixes.dust.mFamiliarPrefixes.contains(tData.mPrefix)))
                    aSolidOutput = GT_OreDictUnificator.getDust(tData.mMaterial.mMaterial, tData.mMaterial.mAmount * aSolidOutput.stackSize / tScale);
                else {
                    if (aSolidOutput.stackSize / tScale == 0) aSolidOutput = GT_Values.NI;
                    else aSolidOutput = new ItemStack(aSolidOutput.getItem(), aSolidOutput.stackSize / tScale);
                }
            }
            aDuration = (aDuration + tScale - 1) / tScale;
        }

        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sDistilleryRecipes.addRecipe(true, new ItemStack[]{aCircuit}, new ItemStack[]{aSolidOutput}, null, new FluidStack[]{aInput}, new FluidStack[]{aOutput}, aDuration, aEUt, 0);
        if ((aHidden) && (tRecipe != null)) {
            tRecipe.mHidden = true;
        }
        return tRecipe;
    }

    @Override
    public void removeDistilleryRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sDistilleryRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addDistilleryRecipeRemovable(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt, boolean aHidden) {
        return addDistilleryRecipeRemovable(aCircuit, aInput, aOutput, null, aDuration, aEUt, aHidden);
    }

    @Override
    public GT_Recipe addDistilleryRecipeRemovable(int circuitConfig, FluidStack aInput, FluidStack aOutput, ItemStack aSolidOutput, int aDuration, int aEUt, boolean aHidden) {
        return addDistilleryRecipeRemovable(GT_Utility.getIntegratedCircuit(circuitConfig), aInput, aOutput, aSolidOutput, aDuration, aEUt, aHidden);
    }

    @Override
    public GT_Recipe addDistilleryRecipeRemovable(int circuitConfig, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt, boolean aHidden) {
        return addDistilleryRecipeRemovable(GT_Utility.getIntegratedCircuit(circuitConfig), aInput, aOutput, aDuration, aEUt, aHidden);
    }

    @Override
    public GT_Recipe addFluidSolidifierRecipeRemovable(ItemStack aMold, FluidStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aMold == null) || (aInput == null) || (aOutput == null)) {
            return null;
        }
        if (aInput.isFluidEqual(Materials.PhasedGold.getMolten(144))) {
            aInput = Materials.VibrantAlloy.getMolten(aInput.amount);
        }
        if (aInput.isFluidEqual(Materials.PhasedIron.getMolten(144))) {
            aInput = Materials.PulsatingIron.getMolten(aInput.amount);
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("fluidsolidifier", aOutput, aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes.addRecipe(true, new ItemStack[]{aMold}, new ItemStack[]{aOutput}, null, new FluidStack[]{aInput}, null, aDuration, aEUt, 0);
    }

    @Override
    public void removeFluidSolidifierRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addFluidSmelterRecipeRemovable(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance, int aDuration, int aEUt) {
        return addFluidSmelterRecipeRemovable(aInput, aRemains, aOutput, aChance, aDuration, aEUt, false);
    }

    @Override
    public GT_Recipe addFluidSmelterRecipeRemovable(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance, int aDuration, int aEUt, boolean hidden) {
        if ((aInput == null) || (aOutput == null)) {
            return null;
        }
        if (aOutput.isFluidEqual(Materials.PhasedGold.getMolten(1))) {
            aOutput = Materials.VibrantAlloy.getMolten(aOutput.amount);
        }
        if (aOutput.isFluidEqual(Materials.PhasedIron.getMolten(1))) {
            aOutput = Materials.PulsatingIron.getMolten(aOutput.amount);
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("fluidsmelter", aInput, aDuration)) <= 0) {
            return null;
        }
        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes.addRecipe(true, new ItemStack[]{aInput}, new ItemStack[]{aRemains}, null, new int[]{aChance}, null, new FluidStack[]{aOutput}, aDuration, aEUt, 0);
        if ((hidden) && (tRecipe != null)) {
            tRecipe.mHidden = true;
        }
        return tRecipe;
    }

    @Override
    public void removeFluidSmelterRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addFluidExtractionRecipeRemovable(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput == null)) {
            return null;
        }
        if (aOutput.isFluidEqual(Materials.PhasedGold.getMolten(1))) {
            aOutput = Materials.VibrantAlloy.getMolten(aOutput.amount);
        }
        if (aOutput.isFluidEqual(Materials.PhasedIron.getMolten(1))) {
            aOutput = Materials.PulsatingIron.getMolten(aOutput.amount);
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("fluidextractor", aInput, aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes.addRecipe(true, new ItemStack[]{aInput}, new ItemStack[]{aRemains}, null, new int[]{aChance}, null, new FluidStack[]{aOutput}, aDuration, aEUt, 0);
    }

    @Override
    public void removeFluidExtractionRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addFluidCannerRecipeRemovable(ItemStack aInput, ItemStack aOutput, FluidStack aFluidInput, FluidStack aFluidOutput) {
        int aDuration= aFluidOutput == null ? aFluidInput.amount / 62 : aFluidOutput.amount / 62;

        if (aInput == null || aOutput == null) {
            return null;
        }
        if (!GregTech_API.sRecipeFile.get("fluidcanner", aOutput, true)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("fluidcanner", aInput, aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes.addRecipe(true, new ItemStack[]{aInput}, new ItemStack[]{aOutput}, null, new FluidStack[]{aFluidInput == null ? null : aFluidInput}, new FluidStack[]{aFluidOutput == null ? null : aFluidOutput}, aDuration, 1, 0);
    }

    @Override
    public void removeFluidCannerRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addFluidCannerRecipeRemovable(ItemStack aInput, ItemStack aOutput, FluidStack aFluidInput, FluidStack aFluidOutput, int aDuration, int aEUt) {
        if (aInput == null || aOutput == null) {
            return null;
        }
        if (!GregTech_API.sRecipeFile.get("fluidcanner", aOutput, true)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("fluidcanner", aInput, aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes.addRecipe(true, new ItemStack[]{aInput}, new ItemStack[]{aOutput}, null, new FluidStack[]{aFluidInput == null ? null : aFluidInput}, new FluidStack[]{aFluidOutput == null ? null : aFluidOutput}, aDuration, aEUt, 0);
    }

    @Override
    public GT_Recipe addChemicalBathRecipeRemovable(ItemStack aInput, FluidStack aBathingFluid, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aBathingFluid == null) || (aOutput1 == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("chemicalbath", aInput, aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes.addRecipe(true, new ItemStack[]{aInput}, new ItemStack[]{aOutput1, aOutput2, aOutput3}, null, aChances, new FluidStack[]{aBathingFluid}, null, aDuration, aEUt, 0);
    }

    @Override
    public void removeChemicalBathRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addElectromagneticSeparatorRecipeRemovable(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput1 == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("electromagneticseparator", aInput, aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes.addRecipe(true, new ItemStack[]{aInput}, new ItemStack[]{aOutput1, aOutput2, aOutput3}, null, aChances, null, null, aDuration, aEUt, 0);
    }

    @Override
    public void removeElectromagneticSeparatorRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addExtractorRecipeRemovable(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("extractor", aInput, aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sExtractorRecipes.addRecipe(true, new ItemStack[]{aInput}, new ItemStack[]{aOutput}, null, null, null, aDuration, aEUt, 0);
    }

    @Override
    public void removeExtractorRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sExtractorRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addPrinterRecipeRemovable(ItemStack aInput, FluidStack aFluid, ItemStack aSpecialSlot, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aFluid == null) || (aOutput == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("printer", aInput, aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sPrinterRecipes.addRecipe(true, new ItemStack[]{aInput}, new ItemStack[]{aOutput}, aSpecialSlot, null, new FluidStack[]{aFluid}, null, aDuration, aEUt, 0);
    }

    @Override
    public void removePrinterRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sPrinterRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addAutoclaveRecipeRemovable(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom) {
        return addAutoclaveRecipeRemovable(aInput, aFluid, aOutput,aChance, aDuration, aEUt);
    }

    @Override
    public GT_Recipe addAutoclaveRecipeRemovable(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration, int aEUt) {
        return addAutoclaveRecipeRemovable(aInput, null, aFluid, aOutput, aChance, aDuration, aEUt, false);
    }

    public GT_Recipe addAutoclaveRecipeRemovable(ItemStack aInput, ItemStack aCircuit, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration, int aEUt) {
        return addAutoclaveRecipeRemovable(aInput, aCircuit, aFluid, aOutput, aChance, aDuration, aEUt, false);
    }

    @Override
    public GT_Recipe addAutoclaveRecipeRemovable(ItemStack aInput, ItemStack aCircuit, FluidStack aFluidIn, ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom) {
        return addAutoclaveRecipeRemovable(aInput, aCircuit, aFluidIn, null, aOutput, aChance, aDuration, aEUt,aCleanroom);
    }

    @Override
    public GT_Recipe addAutoclaveRecipeRemovable(ItemStack aInput, ItemStack aCircuit, FluidStack aFluidIn, FluidStack aFluidOut, ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom) {
        if ((aInput == null) || (aFluidIn == null) || (aOutput == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("autoclave", aInput, aDuration)) <= 0) {
            return null;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom){
            aCleanroom = false;
        }
        return GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes.addRecipe(true, new ItemStack[]{aInput, aCircuit}, new ItemStack[]{aOutput}, null, new int[]{aChance}, new FluidStack[]{aFluidIn}, new FluidStack[]{aFluidOut}, aDuration, aEUt, aCleanroom ? -200 : 0);
    }

    @Override
    public void removeAutoclaveRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addAutoclaveSpaceRecipeRemovable(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom) {
        return addAutoclaveRecipeRemovable(aInput, aFluid, aOutput, aChance, aDuration, aEUt, aCleanroom);
    }

    @Override
    public GT_Recipe addAutoclaveSpaceRecipeRemovable(ItemStack aInput, ItemStack aCircuit, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom) {
        if ((aInput == null) || (aFluid == null) || (aOutput == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("autoclave", aInput, aDuration)) <= 0) {
            return null;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom){
            aCleanroom = false;
        }
        return GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes.addRecipe(true, new ItemStack[]{aInput, aCircuit}, new ItemStack[]{aOutput}, null, new int[]{aChance}, new FluidStack[]{aFluid}, null, aDuration, aEUt, aCleanroom ? -100 : 0);
    }

    @Override
    public void removeAutoclaveSpaceRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addMixerRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt) {
        return addMixerRecipeRemovable(aInput1, aInput2, aInput3, aInput4,  null, null, null, null, null, aFluidInput, aFluidOutput, aOutput, aDuration, aEUt);

    }
    @Override
    public GT_Recipe addMixerRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4, ItemStack aInput5, ItemStack aInput6, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt) {
        return addMixerRecipeRemovable(aInput1, aInput2, aInput3, aInput4,  aInput5, aInput6, null, null, null, aFluidInput, aFluidOutput, aOutput, aDuration, aEUt);

    }
    @Override
    public GT_Recipe addMixerRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4, ItemStack aInput5, ItemStack aInput6, ItemStack aInput7, ItemStack aInput8, ItemStack aInput9, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt) {
        if (((aInput1 == null) && (aFluidInput == null)) || ((aOutput == null) && (aFluidOutput == null))) {
            return null;
        }
        if ((aOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get("mixer", aOutput, aDuration)) <= 0)) {
            return null;
        }
        if ((aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get("mixer", aFluidOutput.getFluid().getName(), aDuration)) <= 0)) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sMixerRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2, aInput3, aInput4, aInput5, aInput6, aInput7, aInput8, aInput9}, new ItemStack[]{aOutput}, null, null, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
    }

    @Override
    public void removeMixerRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sMixerRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addMixerRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4, ItemStack aInput5, ItemStack aInput6, ItemStack aInput7, ItemStack aInput8, ItemStack aInput9, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt) {
        if (((aInput1 == null) && (aFluidInput == null)) || ((aOutput1 == null) && (aFluidOutput == null))) {
            return null;
        }
        if ((aOutput1 != null) && ((aDuration = GregTech_API.sRecipeFile.get("mixer", aOutput1, aDuration)) <= 0)) {
            return null;
        }
        if ((aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get("mixer", aFluidOutput.getFluid().getName(), aDuration)) <= 0)) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sMixerRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2, aInput3, aInput4, aInput5, aInput6, aInput7, aInput8, aInput9}, new ItemStack[]{aOutput1, aOutput2, aOutput3, aOutput4}, null, null, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
    }

    @Override
    public GT_Recipe addLaserEngraverRecipeRemovable(ItemStack aItemToEngrave, ItemStack aLens, ItemStack aEngravedItem, int aDuration, int aEUt) {
        return addLaserEngraverRecipeRemovable( aItemToEngrave, aLens, aEngravedItem, aDuration, aEUt, false);
    }

    @Override
    public GT_Recipe addLaserEngraverRecipeRemovable(ItemStack aItemToEngrave, ItemStack aLens, ItemStack aEngravedItem, int aDuration, int aEUt, boolean aCleanroom) {
        if ((aItemToEngrave == null) || (aLens == null) || (aEngravedItem == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("laserengraving", aEngravedItem, aDuration)) <= 0) {
            return null;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom){
            aCleanroom = false;
        }
        return GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes.addRecipe(true, new ItemStack[]{aItemToEngrave, aLens}, new ItemStack[]{aEngravedItem}, null, null, null, aDuration, aEUt, aCleanroom ? -200 : 0);
    }

    @Override
    public void removeLaserEngraverRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addFormingPressRecipeRemovable(ItemStack aItemToImprint, ItemStack aForm, ItemStack aImprintedItem, int aDuration, int aEUt) {
        if ((aItemToImprint == null) || (aForm == null) || (aImprintedItem == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("press", aImprintedItem, aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sPressRecipes.addRecipe(true, new ItemStack[]{aItemToImprint, aForm}, new ItemStack[]{aImprintedItem}, null, null, null, aDuration, aEUt, 0);
    }

    @Override
    public void removeFormingPressRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sPressRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addFluidHeaterRecipeRemovable(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("fluidheater", aOutput.getFluid().getUnlocalizedName(), aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes.addRecipe(true, new ItemStack[]{aCircuit}, null, null, new FluidStack[]{aInput}, new FluidStack[]{aOutput}, aDuration, aEUt, 0);
    }

    @Override
    public void removeFluidHeaterRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addSifterRecipeRemovable(ItemStack aItemToSift, ItemStack[] aSiftedItems, int[] aChances, int aDuration, int aEUt) {
        if ((aItemToSift == null) || (aSiftedItems == null)) {
            return null;
        }
        for (ItemStack tStack : aSiftedItems) {
            if (tStack != null) {
                if ((aDuration = GregTech_API.sRecipeFile.get("sifter", aItemToSift, aDuration)) <= 0) {
                    return null;
                }
                return GT_Recipe.GT_Recipe_Map.sSifterRecipes.addRecipe(true, new ItemStack[]{aItemToSift}, aSiftedItems, null, aChances, null, null, aDuration, aEUt, 0);
            }
        }
        return null;
    }

    @Override
    public void removeSifterRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sSifterRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe[][] addArcFurnaceRecipeRemovable(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt) {
        return addArcFurnaceRecipeRemovable(aInput, aOutputs, aChances, aDuration, aEUt,	false);
    }

    @Override
    public GT_Recipe[][] addArcFurnaceRecipeRemovable(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt, boolean hidden) {
        if ((aInput == null) || (aOutputs == null)) {
            return null;
        }
        for (ItemStack tStack : aOutputs) {
            if (tStack != null) {
                if ((aDuration = GregTech_API.sRecipeFile.get("arcfurnace", aInput, aDuration)) <= 0) {
                    return null;
                }
                GT_Recipe sRecipe = GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes.addRecipe(true, new ItemStack[]{aInput}, aOutputs, null, aChances, new FluidStack[]{Materials.Oxygen.getGas(aDuration)}, null, Math.max(1, aDuration), Math.max(1, aEUt), 0);
                if ((hidden) && (sRecipe != null)) {
                    sRecipe.mHidden = true;
                }
                val tRecipes = new ArrayList<GT_Recipe>();
                for (Materials tMaterial : new Materials[]{Materials.Argon, Materials.Nitrogen}) {
                    if (tMaterial.mPlasma != null) {
                        int tPlasmaAmount = (int) Math.max(1L, aDuration / (tMaterial.getMass() * 16L));
                        GT_Recipe tRecipe =GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes.addRecipe(true, new ItemStack[]{aInput}, aOutputs, null, aChances, new FluidStack[]{tMaterial.getPlasma(tPlasmaAmount)}, new FluidStack[]{tMaterial.getGas(tPlasmaAmount)}, Math.max(1, aDuration / 16), Math.max(1, aEUt / 3), 0);
                        if ((hidden) && (tRecipe != null)) {
                            tRecipe.mHidden = true;
                        }
                        if (tRecipe != null) {
                            tRecipes.add(tRecipe);
                        }
                    }
                }
                val sRecipes = sRecipe == null ? null : new GT_Recipe[]{sRecipe};
                val tRecipesArr = tRecipes.isEmpty() ? null : tRecipes.toArray(new GT_Recipe[0]);
                if (sRecipes == null && tRecipesArr == null) {
                    return null;
                }
                return new GT_Recipe[][]{sRecipes, tRecipesArr};
            }
        }
        return null;
    }

    @Override
    public void removeArcFurnaceRecipe(GT_Recipe[][] recipe) {
        val s = recipe[0];
        val t = recipe[1];
        if (s != null) {
            for (val r: s) {
                GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes.remove(r);
            }
        }
        if (t != null) {
            for (val r: t) {
                GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes.remove(r);
            }
        }
    }

    @Override
    public GT_Recipe addSimpleArcFurnaceRecipeRemovable(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutputs == null) || aFluidInput == null) {
            return null;
        }
        for (ItemStack tStack : aOutputs) {
            if (tStack != null) {
                if ((aDuration = GregTech_API.sRecipeFile.get("arcfurnace", aInput, aDuration)) <= 0) {
                    return null;
                }
                return GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes.addRecipe(true, new ItemStack[]{aInput}, aOutputs, null, aChances, new FluidStack[]{aFluidInput}, null, Math.max(1, aDuration), Math.max(1, aEUt), 0);
            }
        }
        return null;
    }

    @Override
    public void removeSimpleArcFurnaceRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addPlasmaArcFurnaceRecipeRemovable(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutputs == null) || aFluidInput == null) {
            return null;
        }
        for (ItemStack tStack : aOutputs) {
            if (tStack != null) {
                if ((aDuration = GregTech_API.sRecipeFile.get("arcfurnace", aInput, aDuration)) <= 0) {
                    return null;
                }
                return GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes.addRecipe(true, new ItemStack[]{aInput}, aOutputs, null, aChances, new FluidStack[]{aFluidInput}, null, Math.max(1, aDuration), Math.max(1, aEUt), 0);
            }
        }
        return null;
    }

    @Override
    public void removePlasmaArcFurnaceRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addPlasmaArcFurnaceRecipeRemovable(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs, FluidStack aFluidOutput, int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutputs == null) || aFluidInput == null) {
            return null;
        }
        for (ItemStack tStack : aOutputs) {
            if (tStack != null) {
                if ((aDuration = GregTech_API.sRecipeFile.get("arcfurnace", aInput, aDuration)) <= 0) {
                    return null;
                }
                return GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes.addRecipe(true, new ItemStack[]{aInput}, aOutputs, null, aChances, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, Math.max(1, aDuration), Math.max(1, aEUt), 0);
            }
        }
        return null;
    }

    @Override
    public GT_Recipe addPulveriserRecipeRemovable(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt) {
        return addPulveriserRecipeRemovable(aInput, aOutputs, aChances, aDuration, aEUt, false);
    }

    @Override
    public GT_Recipe addPulveriserRecipeRemovable(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt, boolean hidden) {
        if ((aInput == null) || (aOutputs == null)) {
            return null;
        }
        for (ItemStack tStack : aOutputs) {
            if (tStack != null) {
                if ((aDuration = GregTech_API.sRecipeFile.get("pulveriser", aInput, aDuration)) <= 0) {
                    return null;
                }
                val recipe = new GT_Recipe(false,new ItemStack[]{aInput},aOutputs,null,aChances,null,null,aDuration,aEUt,0);
                return GT_Recipe.GT_Recipe_Map.sMaceratorRecipes.addRecipe(recipe,true,false,hidden);
            }
        }
        return null;
    }

    @Override
    public void removePulveriserRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sMaceratorRecipes.remove(recipe);
    }

    @Override
    public GT_Recipe addPyrolyseRecipeRemovable(ItemStack aInput, FluidStack aFluidInput, int intCircuit, ItemStack aOutput, FluidStack aFluidOutput, int aDuration, int aEUt) {
        if (aInput == null) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("pyrolyse", aInput, aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sPyrolyseRecipes.addRecipe(false, new ItemStack[]{aInput, ItemList.Circuit_Integrated.getWithDamage(0L, intCircuit, new Object[0])}, new ItemStack[]{aOutput}, null, null, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
    }

    @Override
    public void removePyrolyseRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sPyrolyseRecipes.remove(recipe);
    }

    @Override
    @Deprecated
    public GT_Recipe addCrackingRecipeRemovable(FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt) {
        return null;
    }

    @Override
    public GT_Recipe addCrackingRecipeRemovable(int circuitConfig, FluidStack aInput, FluidStack aInput2, FluidStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null && aInput2 == null) || (aOutput == null)) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("cracking", aInput.getUnlocalizedName(), aDuration)) <= 0) {
            return null;
        }
        return GT_Recipe.GT_Recipe_Map.sCrakingRecipes.addRecipe(false, new ItemStack[]{GT_Utility.getIntegratedCircuit(circuitConfig)}, null, null, null,
                new FluidStack[]{aInput, aInput2}, new FluidStack[]{aOutput}, aDuration, aEUt, 0);
    }

    @Override
    public void removeCrackingRecipe(GT_Recipe recipe) {
        GT_Recipe.GT_Recipe_Map.sCrakingRecipes.remove(recipe);
    }

    @Override
    public Pair<GT_Recipe[], GT_Recipe_AssemblyLine> addAssemblylineRecipeRemovable(ItemStack aResearchItem, int aResearchTime, ItemStack[] aInputs, FluidStack[] aFluidInputs, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aResearchItem==null)||(aResearchTime<=0)||(aInputs == null) || (aOutput == null) || aInputs.length>15 || aInputs.length<4) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("assemblingline", aOutput, aDuration)) <= 0) {
            return null;
        }
        for(ItemStack tItem : aInputs){
            if(tItem==null){
                GT_FML_LOGGER.info("addAssemblingLineRecipe "+aResearchItem.getDisplayName()+" --> "+aOutput.getUnlocalizedName()+" there is some null item in that recipe");
            }
        }
        val r1 = GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(false, new ItemStack[]{aResearchItem}, new ItemStack[]{aOutput}, new ItemStack[]{ItemList.Tool_DataStick.getWithName(1L, "Writes Research result", new Object[0])}, null, null, aResearchTime, 30, -201);
        val r2 = GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes.addFakeRecipe(false, aInputs, new ItemStack[]{aOutput}, new ItemStack[]{ItemList.Tool_DataStick.getWithName(1L, "Reads Research result", new Object[0])}, aFluidInputs, null, aDuration, aEUt, 0,true);
        val r3 = new GT_Recipe_AssemblyLine( aResearchItem, aResearchTime, aInputs, aFluidInputs, aOutput, aDuration, aEUt);
        GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes.add(r3);
        return Pair.of(new GT_Recipe[]{r1, r2}, r3);
    }

    @Override
    public void removeAssemblylineRecipe(Pair<GT_Recipe[], GT_Recipe_AssemblyLine> recipe) {
        val l = recipe.getLeft();
        val r = recipe.getRight();
        GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.remove(l[0]);
        GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes.remove(l[1]);
        GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes.remove(r);
    }

    @Override
	public Pair<GT_Recipe[], GT_Recipe_AssemblyLine> addAssemblylineRecipeRemovable(ItemStack aResearchItem, int aResearchTime, Object[] aInputs, FluidStack[] aFluidInputs, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aResearchItem==null)||(aResearchTime<=0)||(aInputs == null) || (aOutput == null) || aInputs.length>15 || aInputs.length<4) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("assemblingline", aOutput, aDuration)) <= 0) {
            return null;
        } 
        ItemStack[] tInputs = new ItemStack[aInputs.length];
        ItemStack[][] tAlts = new ItemStack[aInputs.length][];
        for(int i = 0; i < aInputs.length; i++){
        	Object obj = aInputs[i];
        	if (obj instanceof ItemStack) {
        		tInputs[i] = (ItemStack) obj;
        		tAlts[i] = null;
        		continue;
        	} else if (obj instanceof ItemStack[]) {
        		ItemStack[] aStacks = (ItemStack[]) obj;
        		if (aStacks.length > 0) {
        			tInputs[i] = aStacks[0];
        			tAlts[i] = (ItemStack[]) Arrays.copyOf(aStacks, aStacks.length);
        			continue;
        		}
        	} else if (obj instanceof Object[]) {
        		Object[] objs = (Object[]) obj;
        		List<ItemStack> tList;
        		if (objs.length >= 2 && !(tList = GT_OreDictUnificator.getOres(objs[0])).isEmpty()) {
        			try {
        				int tAmount = ((Number) objs[1]).intValue();
            			List<ItemStack> uList = new ArrayList<>();
            			for (ItemStack tStack : tList) {
            				ItemStack uStack = GT_Utility.copyAmount(tAmount, tStack); 
            				if (GT_Utility.isStackValid(uStack)) {
            					uList.add(uStack);
            					if (tInputs[i] == null)
            					    tInputs[i] = uStack;
            				}
            			}
            			tAlts[i] = uList.toArray(new ItemStack[uList.size()]);
            			continue;
        			} catch (Exception t) {}
        		}
        	}
        	GT_FML_LOGGER.info("addAssemblingLineRecipe "+aResearchItem.getDisplayName()+" --> "+aOutput.getUnlocalizedName()+" there is some null item in that recipe");
        }
        val r1 = GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(false, new ItemStack[]{aResearchItem}, new ItemStack[]{aOutput}, new ItemStack[]{ItemList.Tool_DataStick.getWithName(1L, "Writes Research result", new Object[0])}, null, null, aResearchTime, 30, -201);
        val r2 = GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes.addFakeRecipe(false,tInputs,new ItemStack[]{aOutput},new ItemStack[]{ItemList.Tool_DataStick.getWithName(1L, "Reads Research result", new Object[0])},aFluidInputs,null,aDuration,aEUt,0,tAlts,true);
        val r3 = new GT_Recipe_AssemblyLine( aResearchItem, aResearchTime, tInputs, aFluidInputs, aOutput, aDuration, aEUt, tAlts);
        GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes.add(r3);
        return Pair.of(new GT_Recipe[]{r1, r2}, r3);
	}

    @Override
    public GT_Recipe[] addCircuitAssemblerRecipeRemovable(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput, int aDuration, int aEUt) {
        return addCircuitAssemblerRecipeRemovable(aInputs, aFluidInput, aOutput,aDuration,aEUt, false);
    }

    @Override
    public GT_Recipe[] addCircuitAssemblerRecipeRemovable(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput, int aDuration, int aEUt, boolean aCleanroom) {

        if (this.areItemsAndFluidsBothNull(aInputs, new FluidStack[]{aFluidInput})) {
            return null;
        }

        if ((aDuration = GregTech_API.sRecipeFile.get("circuitassembler", aOutput, aDuration)) <= 0) {
            return null;
        }

        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }

        if (!GT_Utility.isStackValid(aOutput)) {
            return null;
        }

        for (int oreID : OreDictionary.getOreIDs(aOutput)) {
            if (OreDictionary.getOreName(oreID).startsWith("circuit")){
                val r = this.addCircuitAssemblerRecipeNonOredictedRemovable(aInputs, aFluidInput, aOutput, aDuration, aEUt, aCleanroom);
                return r == null ? null : new GT_Recipe[]{r};
            }
        }

        val ret = new ArrayList<GT_Recipe>();

        for (int i = 0; i < aInputs.length; ++i) {
            for (int oreID : OreDictionary.getOreIDs(aInputs[i])) {
                String odName = OreDictionary.getOreName(oreID);
                if (odName.startsWith("circuit")) {
                    for (ItemStack tStack : GT_OreDictUnificator.getOresImmutable(odName)) {
                        if (!GT_Utility.isStackValid(tStack))
                            continue;
                        aInputs[i] = new ItemStack(tStack.getItem(),aInputs[i].stackSize,tStack.getItemDamage());
                        val r = GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.addRecipe(true, aInputs, new ItemStack[]{aOutput}, null, new FluidStack[]{aFluidInput}, null, aDuration, aEUt, aCleanroom ? -200 : 0);
                        if (r != null) {
                            ret.add(r);
                        }
                    }
                }
            }
        }

        if (ret.isEmpty()) {
            val r = GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.addRecipe(true, aInputs, new ItemStack[]{aOutput}, null, new FluidStack[]{aFluidInput}, null, aDuration, aEUt, aCleanroom ? -200 : 0);
            if (r != null) {
                ret.add(r);
            }
        }

        return ret.isEmpty() ? null : ret.toArray(new GT_Recipe[0]);
    }

    @Override
    public void removeCircuitAssemblerRecipe(GT_Recipe[] recipe) {
        for (val r: recipe) {
            GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.remove(r);
        }
    }

    public GT_Recipe addCircuitAssemblerRecipeNonOredictedRemovable(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput, int aDuration, int aEUt, boolean aCleanroom) {
        if ((aInputs == null) || (aOutput == null) || aInputs.length>6 || aInputs.length<1) {
            return null;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("circuitassembler", aOutput, aDuration)) <= 0) {
            return null;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom){
            aCleanroom = false;
        }
        return GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.addRecipe(true, aInputs, new ItemStack[]{aOutput}, null, null, new FluidStack[]{aFluidInput}, null, aDuration, aEUt, aCleanroom ? -200 : 0);
    }
    private boolean areItemsAndFluidsBothNull(ItemStack[] items, FluidStack[] fluids){
        boolean itemsNull = true;
        if (items != null) {
            for (ItemStack itemStack : items) {
                if (itemStack != null) {
                    itemsNull = false;
                    break;
                }
            }
        }
        boolean fluidsNull = true;
        if (fluids != null) {
            for (FluidStack fluidStack : fluids) {
                if (fluidStack != null) {
                    fluidsNull = false;
                    break;
                }
            }
        }
        return itemsNull && fluidsNull;

    }
}
