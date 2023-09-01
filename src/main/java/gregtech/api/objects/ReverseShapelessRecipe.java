package gregtech.api.objects;


import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Disassembler;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sDisassemblerRecipes;


public class ReverseShapelessRecipe {

    private static final Queue<ReverseShapelessRecipe> reverseRecipes = new LinkedList<>();

    public static Queue<ReverseShapelessRecipe> getReverseRecipes() {
        return reverseRecipes;
    }

    public static void runReverseRecipes() {
        for (ReverseShapelessRecipe x : reverseRecipes) {
            Optional<GT_Recipe> recipeOptional = GT_Utility.reverseShapelessRecipe(x.aResult, x.aRecipe);
            if (!recipeOptional.isPresent()) {
                continue;
            }
            GT_Recipe recipe = recipeOptional.get();
            ItemStack[] replacement = new ItemStack[recipe.mOutputs.length];
            GT_MetaTileEntity_Disassembler.handleRecipeTransformation(recipe.mOutputs, replacement, Collections.singleton(recipe.mOutputs));

            recipe.mOutputs = replacement;
            sDisassemblerRecipes.add(recipe);
        }
    }

    private final ItemStack aResult;

    private final Object[] aRecipe;

    public ReverseShapelessRecipe(ItemStack output, Object[] aRecipe) {
        this.aResult = output;
        this.aRecipe = aRecipe;
        reverseRecipes.add(this);
    }

}
