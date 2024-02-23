package gregtech.loaders.postload;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.GT_FluidStack;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import gregtech.common.fluids.GT_OreSlurry;
import lombok.val;
import lombok.var;
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

    private void registerSlurryRecipes(GT_OreSlurry slurry) {
        val primary = slurry.oreLayer.mPrimary;
        val secondary = slurry.oreLayer.mSecondary;
        val between = slurry.oreLayer.mBetween;
        val sporadic = slurry.oreLayer.mSporadic;

        for (int i = 1; i < 16; i++) {
            val usePrimary   = primary   != null && (i & 0b00000001) > 0;
            val useSecondary = secondary != null && (i & 0b00000010) > 0;
            val useBetween   = between   != null && (i & 0b00000100) > 0;
            val useSporadic  = sporadic  != null && (i & 0b00001000) > 0;

            val itemOutputsAndChances = new ArrayList<Pair<ItemStack, Integer>>();

            if (usePrimary) {
                val pair = Pair.of(GT_OreDictUnificator.get(OrePrefixes.oreChunk, primary,1), 4000);
                itemOutputsAndChances.add(pair);
            }

            if (useSecondary) {
                val pair = Pair.of(GT_OreDictUnificator.get(OrePrefixes.oreChunk, secondary, 1), 3000);
                itemOutputsAndChances.add(pair);
            }

            if (useBetween) {
                val pair = Pair.of(GT_OreDictUnificator.get(OrePrefixes.oreChunk, between, 1), 2000);
                itemOutputsAndChances.add(pair);
            }

            if (useSporadic) {
                val pair = Pair.of(GT_OreDictUnificator.get(OrePrefixes.oreChunk, sporadic, 1), 1000);
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


            itemOutputs[j] = ItemList.Solid_Waste.get(1L);
            itemChances[j] = leftoverChance;

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
