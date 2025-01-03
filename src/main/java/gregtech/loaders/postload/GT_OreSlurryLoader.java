package gregtech.loaders.postload;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.GT_FluidStack;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import gregtech.common.fluids.GT_OreSlurry;
import lombok.val;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class GT_OreSlurryLoader implements Runnable {
    @Override
    public void run() {
        for (val oreMix : GT_Worldgen_GT_Ore_Layer.sList) {
            val slurry = new GT_OreSlurry(oreMix);
            this.registerSlurryRecipes(slurry);
            GT_OreSlurry.slurries.put(oreMix, slurry);
        }
    }

    private void addSet(ArrayList<Pair<Materials,Integer>> set,Materials mat,int chance) {
        if (mat == null) return;
        for (val material : set) {
            if (material.getLeft().equals(mat)) {
                material.setValue(material.getValue()+chance);
                return;
            }
        }
        set.add(new MutablePair<>(mat, chance));
    }

    private void registerSlurryRecipes(GT_OreSlurry slurry) {
        val primary = slurry.oreLayer.mPrimary;
        val secondary = slurry.oreLayer.mSecondary;
        val between = slurry.oreLayer.mBetween;
        val sporadic = slurry.oreLayer.mSporadic;

        ArrayList<Pair<Materials,Integer>> permutations = new ArrayList<>();
        addSet(permutations, primary,4000);
        addSet(permutations, secondary,3000);
        addSet(permutations, between,2000);
        addSet(permutations, sporadic,1000);

        for (int i = 0; i < (Math.pow(2,permutations.size()) - 1); i++) {

            val itemOutputsAndChances = new ArrayList<Pair<ItemStack, Integer>>();
            if (i%2 == 0 && !permutations.isEmpty()) {
                val pair = Pair.of(GT_OreDictUnificator.get(OrePrefixes.oreChunk, permutations.get(0).getKey(),1), permutations.get(0).getValue());
                itemOutputsAndChances.add(pair);
            }
            if (i%4 < 2 && permutations.size() > 1) {
                val pair = Pair.of(GT_OreDictUnificator.get(OrePrefixes.oreChunk, permutations.get(1).getKey(),1), permutations.get(1).getValue());
                itemOutputsAndChances.add(pair);
            }
            if (i%8 < 4 && permutations.size() > 2) {
                val pair = Pair.of(GT_OreDictUnificator.get(OrePrefixes.oreChunk, permutations.get(2).getKey(),1), permutations.get(2).getValue());
                itemOutputsAndChances.add(pair);
            }
            if (i%16 < 8 && permutations.size() > 3) {
                val pair = Pair.of(GT_OreDictUnificator.get(OrePrefixes.oreChunk, permutations.get(3).getKey(),1), permutations.get(3).getValue());
                itemOutputsAndChances.add(pair);
            }

            if (itemOutputsAndChances.isEmpty()) {
                continue;
            }

            val fluidIn = new GT_FluidStack(slurry, 100);
            val fluidOut = new GT_FluidStack(ItemList.sTailings, 50);

            val circuit = ItemList.Circuit_Integrated.getWithDamage(0L, i);

            val itemOutputs = new ItemStack[5];
            val itemChances = new int[] { 0, 0, 0, 0, 0 };

            var leftoverChance = 10000;
            int j = 0;
            for (; j < itemOutputsAndChances.size(); j++) {
                val pair = itemOutputsAndChances.get(j);
                itemOutputs[j] = pair.getLeft();
                itemChances[j] = pair.getRight();

                leftoverChance -= itemChances[j];
            }

            if (leftoverChance > 0) {
                itemOutputs[j] = ItemList.Solid_Waste.get(1L);
                itemChances[j] = leftoverChance;
            }

            GT_Values.RA.addCentrifugeRecipe(circuit,
                                             null,
                                             fluidIn,
                                             fluidOut,
                                             itemOutputs[0],
                                             itemOutputs[1],
                                             itemOutputs[2],
                                             itemOutputs[3],
                                             itemOutputs[4],
                                             null,
                                             itemChances,
                                             200,
                                             24);
        }
    }
}
