package gregtech.api.interfaces.internal;

import gregtech.api.util.GT_Recipe;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public interface IGT_RecipeAdder {
    /**
     * Adds a FusionreactorRecipe
     * Does not work anymore!
     */
    @Deprecated
    GT_Recipe addFusionReactorRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aFusionDurationInTicks, int aFusionEnergyPerTick, int aEnergyNeededForStartingFusion);
    @Deprecated
    default boolean addFusionReactorRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aFusionDurationInTicks, int aFusionEnergyPerTick, int aEnergyNeededForStartingFusion) {
        return addFusionReactorRecipeRemovable(aInput1, aInput2, aOutput1, aFusionDurationInTicks, aFusionEnergyPerTick, aEnergyNeededForStartingFusion) != null;
    }

    /**
     * Adds a FusionreactorRecipe
     *
     * @param aInput1                        = first Input (not null, and respects StackSize)
     * @param aInput2                        = second Input (not null, and respects StackSize)
     * @param aOutput1                       = Output of the Fusion (can be null, and respects StackSize)
     * @param aFusionDurationInTicks         = How many ticks the Fusion lasts (must be > 0)
     * @param aFusionEnergyPerTick           = The EU generated per Tick (can even be negative!)
     * @param aEnergyNeededForStartingFusion = EU needed for heating the Reactor up (must be >= 0)
     * @return true if the Recipe got added, otherwise false.
     */
    GT_Recipe addFusionReactorRecipeRemovable(FluidStack aInput1, FluidStack aInput2, FluidStack aOutput1, int aFusionDurationInTicks, int aFusionEnergyPerTick, int aEnergyNeededForStartingFusion);
    @Deprecated
    default boolean addFusionReactorRecipe(FluidStack aInput1, FluidStack aInput2, FluidStack aOutput1, int aFusionDurationInTicks, int aFusionEnergyPerTick, int aEnergyNeededForStartingFusion) {
        return addFusionReactorRecipeRemovable(aInput1, aInput2, aOutput1, aFusionDurationInTicks, aFusionEnergyPerTick, aEnergyNeededForStartingFusion) != null;
    }

    /**
     * Adds a Centrifuge Recipe
     *
     * @param aInput1    must be != null
     * @param aOutput1   must be != null
     * @param aOutput2   can be null
     * @param aOutput3   can be null
     * @param aOutput4   can be null
     * @param aDuration  must be > 0
     */
    GT_Recipe addCentrifugeRecipeRemovable(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int aDuration);
    @Deprecated
    default boolean addCentrifugeRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int aDuration) {
        return addCentrifugeRecipeRemovable(aInput1, aInput2, aOutput1, aOutput2, aOutput3, aOutput4, aOutput5, aOutput6, aDuration) != null;
    }

    GT_Recipe addCentrifugeRecipeRemovable(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int aDuration, int aEUt);
    @Deprecated
    default boolean addCentrifugeRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int aDuration, int aEUt) {
        return addCentrifugeRecipeRemovable(aInput1, aInput2, aOutput1, aOutput2, aOutput3, aOutput4, aOutput5, aOutput6, aDuration, aEUt) != null;
    }

    /**
     * Adds a Centrifuge Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aOutput2  can be null
     * @param aOutput3  can be null
     * @param aOutput4  can be null
     * @param aDuration must be > 0
     */
    GT_Recipe addCentrifugeRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt);
    @Deprecated
    default boolean addCentrifugeRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt) {
        return addCentrifugeRecipeRemovable(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput1, aOutput2, aOutput3, aOutput4, aOutput5, aOutput6, aChances, aDuration, aEUt) != null;
    }

    GT_Recipe addCentrifugeRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt, boolean aCleanroom);
    @Deprecated
    default boolean addCentrifugeRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt, boolean aCleanroom) {
        return addCentrifugeRecipeRemovable(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput1, aOutput2, aOutput3, aOutput4, aOutput5, aOutput6, aChances, aDuration, aEUt, aCleanroom) != null;
    }

    /**
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @return
     */
    GT_Recipe addCompressorRecipeRemovable(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt);
    @Deprecated
    default boolean addCompressorRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
        return addCompressorRecipeRemovable(aInput1, aOutput1, aDuration, aEUt) != null;
    }

    /**
     * Adds a Electrolyzer Recipe
     *
     * @param aInput1    must be != null
     * @param aOutput1   must be != null
     * @param aOutput2   can be null
     * @param aOutput3   can be null
     * @param aOutput4   can be null
     * @param aDuration  must be > 0
     * @param aEUt       should be > 0
     */
    GT_Recipe addElectrolyzerRecipeRemovable(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int aDuration, int aEUt);
    @Deprecated
    default boolean addElectrolyzerRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int aDuration, int aEUt) {
        return addElectrolyzerRecipeRemovable(aInput1, aInput2, aOutput1, aOutput2, aOutput3, aOutput4, aOutput5, aOutput6, aDuration, aEUt) != null;
    }

    /**
     * Adds a Electrolyzer Recipe
     *
     * @param aInput1    must be != null
     * @param aOutput1   must be != null
     * @param aOutput2   can be null
     * @param aOutput3   can be null
     * @param aOutput4   can be null
     * @param aDuration  must be > 0
     * @param aEUt       should be > 0
     */
    GT_Recipe addElectrolyzerRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt);
    @Deprecated
    default boolean addElectrolyzerRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt) {
        return addElectrolyzerRecipeRemovable(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput1, aOutput2, aOutput3, aOutput4, aOutput5, aOutput6, aChances, aDuration, aEUt) != null;
    }

    /**
     * Adds a Chemical Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   must be != null
     * @param aOutput   must be != null
     * @param aDuration must be > 0
     */
    GT_Recipe[] addChemicalRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput, int aDuration);
    @Deprecated
    default boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput, int aDuration) {
        return addChemicalRecipeRemovable(aInput1, aInput2, aOutput, aDuration) != null;
    }

    GT_Recipe[] addChemicalRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput, int aDuration, int aEUt);
    @Deprecated
    default boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput, int aDuration, int aEUt) {
        return addChemicalRecipeRemovable(aInput1, aInput2, aOutput, aDuration, aEUt) != null;
    }

    /**
     * Adds a Chemical Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   must be != null
     * @param aOutput   must be != null
     * @param aDuration must be > 0
     */
    GT_Recipe[] addChemicalRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration);
    @Deprecated
    default boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration) {
        return addChemicalRecipeRemovable(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aDuration) != null;
    }

    /**
     * Adds a Chemical Recipe
     * Only use this when the recipe conflicts in MultiBlock!
     *
     * @param aInput1   must be != null
     * @param aInput2   must be != null
     * @param aOutput  must be != null
     * @param aOutput2  must be != null
     * @param aDuration must be > 0
     */
    GT_Recipe addChemicalRecipeForBasicMachineOnlyRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick);
    @Deprecated
    default boolean addChemicalRecipeForBasicMachineOnly(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick) {
        return addChemicalRecipeForBasicMachineOnlyRemovable(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aOutput2, aDuration, aEUtick) != null;
    }


    /**
     * Adds a Chemical Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   must be != null
     * @param aOutput   must be != null
     * @param aOutput2  must be != null
     * @param aDuration must be > 0
     */
    GT_Recipe[] addChemicalRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration);
    @Deprecated
    default boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration) {
        return addChemicalRecipeRemovable(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aOutput2, aDuration) != null;
    }

    /**
     * Adds Recipes for creating a radically polymerized polymer from a base Material (for example Ethylene -> Polyethylene)
     *
     * @param aBasicMaterial     The basic Material
     * @param aBasicMaterialCell The corresponding Cell basic Material
     * @param aPolymer           The polymer
     */
    void addDefaultPolymerizationRecipes(Fluid aBasicMaterial, ItemStack aBasicMaterialCell, Fluid aPolymer);

    /**
     * Adds a Chemical Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   must be != null
     * @param aOutput   must be != null
     * @param aDuration must be > 0
     * @param aEUtick   must be > 0
     */
    GT_Recipe[] addChemicalRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUtick);
    @Deprecated
    default boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUtick) {
        return addChemicalRecipeRemovable(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aDuration, aEUtick) != null;
    }

    GT_Recipe[] addChemicalRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick, boolean aCleanroom);
    @Deprecated
    default boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick, boolean aCleanroom) {
        return addChemicalRecipeRemovable(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aOutput2, aDuration, aEUtick, aCleanroom) != null;
    }

    /**
     * Adds a Chemical Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   must be != null
     * @param aOutput   must be != null
     * @param aOutput2  must be != null
     * @param aDuration must be > 0
     * @param aEUtick   must be > 0
     */
    GT_Recipe[] addChemicalRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick);
    @Deprecated
    default boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick) {
        return addChemicalRecipeRemovable(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aOutput2, aDuration, aEUtick) != null;
    }

    /**
     * Adds a Chemical Recipe that only exists in the Large Chemical Reactor
     * 
     * @param aInputs   item inputs
     * @param aFluidInputs fluid inputs
     * @param aFluidOutputs fluid outputs
     * @param aOutputs  item outputs
     * @param aDuration must be > 0
     * @param aEUtick   must be > 0
     * <br>aInputs and aFluidInputs must contain at least one valid input.
     * <br>aOutputs and aFluidOutputs must contain at least one valid output.
     * 
     */

    GT_Recipe addMultiblockChemicalRecipeRemovable(ItemStack[] aInputs, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, ItemStack[] aOutputs, int aDuration, int aEUtick);
    @Deprecated
    default boolean addMultiblockChemicalRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, ItemStack[] aOutputs, int aDuration, int aEUtick) {
        return addMultiblockChemicalRecipeRemovable(aInputs, aFluidInputs, aFluidOutputs, aOutputs, aDuration, aEUtick) != null;
    }


    /**
     * Adds a Blast Furnace Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   can be null
     * @param aOutput1  must be != null
     * @param aOutput2  can be null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     * @param aLevel    should be > 0 is the minimum Heat Level needed for this Recipe
     */
    @Deprecated
    GT_Recipe addBlastRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, int aLevel);
    @Deprecated
    default boolean addBlastRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, int aLevel) {
        return addBlastRecipeRemovable(aInput1, aInput2, aOutput1, aOutput2, aDuration, aEUt, aLevel) != null;
    }

    /**
     * Adds a Blast Furnace Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   can be null
     * @param aOutput1  must be != null
     * @param aOutput2  can be null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     * @param aLevel    should be > 0 is the minimum Heat Level needed for this Recipe
     */
    GT_Recipe addBlastRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, int aLevel);
    @Deprecated
    default boolean addBlastRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, int aLevel) {
        return addBlastRecipeRemovable(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput1, aOutput2, aDuration, aEUt, aLevel) != null;
    }

    /**
     * Adds a Blast Furnace Recipe
     *
     * @param aInput1     must be != null if aInput2 == null
     * @param aInput2     must be != null if aInput1 == null
     * @param aCoalAmount must be > 0
     * @param aOutput1    must be != null if aOutput2 == null
     * @param aOutput2    must be != null if aOutput1 == null
     * @param aDuration   must be > 0
     */

    GT_Recipe[] addPrimitiveBlastRecipeRemovable(ItemStack aInput1, ItemStack aInput2, int aCoalAmount, ItemStack aOutput1, ItemStack aOutput2, int aDuration);
    @Deprecated
    default boolean addPrimitiveBlastRecipe(ItemStack aInput1, ItemStack aInput2, int aCoalAmount, ItemStack aOutput1, ItemStack aOutput2, int aDuration) {
        return addPrimitiveBlastRecipeRemovable(aInput1, aInput2, aCoalAmount, aOutput1, aOutput2, aDuration) != null;
    }


    /**
     * Adds a Canning Machine Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0, 100 ticks is standard.
     * @param aEUt      should be > 0, 1 EU/t is standard.
     */
    GT_Recipe addCannerRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt);
    @Deprecated
    default boolean addCannerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
        return addCannerRecipeRemovable(aInput1, aInput2, aOutput1, aOutput2, aDuration, aEUt) != null;
    }

    /**
     * Adds an Alloy Smelter Recipe
     *
     * @param aInput1   must be != null
     * @param aInput2   can be null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    GT_Recipe addAlloySmelterRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt);
    @Deprecated
    default boolean addAlloySmelterRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt) {
        return addAlloySmelterRecipeRemovable(aInput1, aInput2, aOutput1, aDuration, aEUt) != null;
    }

    GT_Recipe addAlloySmelterRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt, boolean hidden);
    @Deprecated
    default boolean addAlloySmelterRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt, boolean hidden) {
        return addAlloySmelterRecipeRemovable(aInput1, aInput2, aOutput1, aDuration, aEUt, hidden) != null;
    }


    /**
     * Adds a CNC-Machine Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    GT_Recipe addCNCRecipeRemovable(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt);
    @Deprecated
    default boolean addCNCRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
        return addCNCRecipeRemovable(aInput1, aOutput1, aDuration, aEUt) != null;
    }

    /**
     * Adds an Assembler Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aInput2   must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    GT_Recipe[] addAssemblerRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt);
    @Deprecated
    default boolean addAssemblerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt) {
        return addAssemblerRecipeRemovable(aInput1, aInput2, aOutput1, aDuration, aEUt) != null;
    }

    /**
     * Adds an Assembler Recipe
     *
     * @param aInputs   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    GT_Recipe[] addAssemblerRecipeRemovable(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt);
    @Deprecated
    default boolean addAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt) {
        return addAssemblerRecipeRemovable(aInputs, aFluidInput, aOutput1, aDuration, aEUt) != null;
    }

    /**
     * Adds an Assembler Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    GT_Recipe[] addAssemblerRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt);
    @Deprecated
    default boolean addAssemblerRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt) {
        return addAssemblerRecipeRemovable(aInput1, aInput2, aFluidInput, aOutput1, aDuration, aEUt) != null;
    }

    GT_Recipe[] addAssemblerRecipeRemovable(ItemStack aInput1, Object aOreDict, int aAmount, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt);
    @Deprecated
    default boolean addAssemblerRecipe(ItemStack aInput1, Object aOreDict, int aAmount, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt) {
        return addAssemblerRecipeRemovable(aInput1, aOreDict, aAmount, aFluidInput, aOutput1, aDuration, aEUt) != null;
    }

    GT_Recipe[] addAssemblerRecipeRemovable(ItemStack[] aInputs, Object aOreDict, int aAmount, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt);
    @Deprecated
    default boolean addAssemblerRecipe(ItemStack[] aInputs, Object aOreDict, int aAmount, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt) {
        return addAssemblerRecipeRemovable(aInputs, aOreDict, aAmount, aFluidInput, aOutput1, aDuration, aEUt) != null;
    }

    GT_Recipe[] addAssemblerRecipeRemovable(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt, boolean aCleanroom);
    @Deprecated
    default boolean addAssemblerRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt, boolean aCleanroom) {
        return addAssemblerRecipeRemovable(aInput1, aInput2, aFluidInput, aOutput1, aDuration, aEUt, aCleanroom) != null;
    }

    GT_Recipe[] addAssemblerRecipeRemovable(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt, boolean aCleanroom);
    @Deprecated
    default boolean addAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt, boolean aCleanroom) {
        return addAssemblerRecipeRemovable(aInputs, aFluidInput, aOutput1, aDuration, aEUt, aCleanroom) != null;
    }


    /**
     * Adds an Circuit Assembler Recipe
     *
     * @param aInputs     must be 1-6 ItemStacks
     * @param aFluidInput 0-1 fluids
     * @param aOutput     must be != null
     * @param aDuration   must be > 0
     * @param aEUt        should be > 0
     */
    GT_Recipe[] addCircuitAssemblerRecipeRemovable(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput, int aDuration, int aEUt);
    @Deprecated
    default boolean addCircuitAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput, int aDuration, int aEUt) {
        return addCircuitAssemblerRecipeRemovable(aInputs, aFluidInput, aOutput, aDuration, aEUt) != null;
    }

    GT_Recipe[] addCircuitAssemblerRecipeRemovable(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput, int aDuration, int aEUt, boolean aCleanroom);
    @Deprecated
    default boolean addCircuitAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput, int aDuration, int aEUt, boolean aCleanroom) {
        return addCircuitAssemblerRecipeRemovable(aInputs, aFluidInput, aOutput, aDuration, aEUt, aCleanroom) != null;
    }

    /**
     * Adds an Assemblyline Recipe
     *
     * @param aInputs      must be != null, 4-16 inputs
     * @param aFluidInputs 0-4 fluids
     * @param aOutput1     must be != null
     * @param aDuration    must be > 0
     * @param aEUt         should be > 0
     */
    Pair<GT_Recipe[], GT_Recipe.GT_Recipe_AssemblyLine> addAssemblylineRecipeRemovable(ItemStack aResearchItem, int aResearchTime, ItemStack[] aInputs, FluidStack[] aFluidInputs, ItemStack aOutput1, int aDuration, int aEUt);
    @Deprecated
    default boolean addAssemblylineRecipe(ItemStack aResearchItem, int aResearchTime, ItemStack[] aInputs, FluidStack[] aFluidInputs, ItemStack aOutput1, int aDuration, int aEUt) {
        return addAssemblylineRecipeRemovable(aResearchItem, aResearchTime, aInputs, aFluidInputs, aOutput1, aDuration, aEUt) != null;
    }

    /**
     * Adds a Assemblyline Recipe
     *
     * @param aInputs elements should be: ItemStack for single item;
     *                ItemStack[] for multiple equivalent items;
     *                {OreDict, amount} for oredict.
     */
    Pair<GT_Recipe[], GT_Recipe.GT_Recipe_AssemblyLine> addAssemblylineRecipeRemovable(ItemStack aResearchItem, int aResearchTime, Object[] aInputs, FluidStack[] aFluidInputs, ItemStack aOutput1, int aDuration, int aEUt);
    @Deprecated
    default boolean addAssemblylineRecipe(ItemStack aResearchItem, int aResearchTime, Object[] aInputs, FluidStack[] aFluidInputs, ItemStack aOutput1, int aDuration, int aEUt) {
        return addAssemblylineRecipeRemovable(aResearchItem, aResearchTime, aInputs, aFluidInputs, aOutput1, aDuration, aEUt) != null;
    }

    /**
     * Adds a Forge Hammer Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    GT_Recipe addForgeHammerRecipeRemovable(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt);
    @Deprecated
    default boolean addForgeHammerRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
        return addForgeHammerRecipeRemovable(aInput1, aOutput1, aDuration, aEUt) != null;
    }

    /**
     * Adds a Wiremill Recipe
     *
     * @param aInput    must be != null
     * @param aOutput   must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    GT_Recipe addWiremillRecipeRemovable(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt);
    @Deprecated
    default boolean addWiremillRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        return addWiremillRecipeRemovable(aInput, aOutput, aDuration, aEUt) != null;
    }

    /**
     * Adds a Polariser Recipe
     *
     * @param aInput    must be != null
     * @param aOutput   must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    GT_Recipe addPolarizerRecipeRemovable(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt);
    @Deprecated
    default boolean addPolarizerRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        return addPolarizerRecipeRemovable(aInput, aOutput, aDuration, aEUt) != null;
    }

    /**
     * Adds a Plate Bending Machine Recipe
     *
     * @param aInput    must be != null
     * @param aOutput   must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    GT_Recipe addBenderRecipeRemovable(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt);
    @Deprecated
    default boolean addBenderRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        return addBenderRecipeRemovable(aInput, aOutput, aDuration, aEUt) != null;
    }

    GT_Recipe addBenderRecipeRemovable(ItemStack aInput, ItemStack aCircuit, ItemStack aOutput, int aDuration, int aEUt);
    @Deprecated
    default boolean addBenderRecipe(ItemStack aInput, ItemStack aCircuit, ItemStack aOutput, int aDuration, int aEUt) {
        return addBenderRecipeRemovable(aInput, aCircuit, aOutput, aDuration, aEUt) != null;
    }

    /**
     * Adds a Extruder Machine Recipe
     *
     * @param aInput    must be != null
     * @param aShape    must be != null, Set the stackSize to 0 if you don't want to let it consume this Item.
     * @param aOutput   must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    GT_Recipe addExtruderRecipeRemovable(ItemStack aInput, ItemStack aShape, ItemStack aOutput, int aDuration, int aEUt);
    @Deprecated
    default boolean addExtruderRecipe(ItemStack aInput, ItemStack aShape, ItemStack aOutput, int aDuration, int aEUt) {
        return addExtruderRecipeRemovable(aInput, aShape, aOutput, aDuration, aEUt) != null;
    }

    /**
     * Adds a Slicer Machine Recipe
     *
     * @param aInput    must be != null
     * @param aShape    must be != null, Set the stackSize to 0 if you don't want to let it consume this Item.
     * @param aOutput   must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     */
    GT_Recipe addSlicerRecipeRemovable(ItemStack aInput, ItemStack aShape, ItemStack aOutput, int aDuration, int aEUt);
    @Deprecated
    default boolean addSlicerRecipe(ItemStack aInput, ItemStack aShape, ItemStack aOutput, int aDuration, int aEUt) {
        return addSlicerRecipeRemovable(aInput, aShape, aOutput, aDuration, aEUt) != null;
    }

    /**
     * @param aInput      must be != null
     * @param aFluidInput must be != null
     * @param aOutput1    must be != null
     * @param aDuration   must be > 0
     * @param aEUt        should be > 0
     * @return
     */
    GT_Recipe addOreWasherRecipeRemovable(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, FluidStack aFluidInput, int aDuration, int aEUt);
    @Deprecated
    default boolean addOreWasherRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, FluidStack aFluidInput, int aDuration, int aEUt) {
        return addOreWasherRecipeRemovable(aInput, aOutput1, aOutput2, aOutput3, aFluidInput, aDuration, aEUt) != null;
    }

    GT_Recipe addOreWasherRecipeRemovable(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, FluidStack aFluidInput, int[] aChances, int aDuration, int aEUt);
    @Deprecated
    default boolean addOreWasherRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, FluidStack aFluidInput, int[] aChances, int aDuration, int aEUt) {
        return addOreWasherRecipeRemovable(aInput, aOutput1, aOutput2, aOutput3, aFluidInput, aChances, aDuration, aEUt) != null;
    }

    /**
     * Adds an Implosion Compressor Recipe
     *
     * @param aInput1  must be != null
     * @param aInput2  amount of ITNT, should be > 0
     * @param aOutput1 must be != null
     * @param aOutput2 can be null
     */
    GT_Recipe[] addImplosionRecipeRemovable(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2);
    @Deprecated
    default boolean addImplosionRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2) {
        return addImplosionRecipeRemovable(aInput1, aInput2, aOutput1, aOutput2) != null;
    }

    /**
     * Adds a Grinder Recipe
     *
     * @param aInput1  must be != null
     * @param aInput2  id for the Cell needed for this Recipe
     * @param aOutput1 must be != null
     * @param aOutput2 can be null
     * @param aOutput3 can be null
     * @param aOutput4 can be null
     */
    GT_Recipe addGrinderRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4);
    @Deprecated
    default boolean addGrinderRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4) {
        return addGrinderRecipeRemovable(aInput1, aInput2, aOutput1, aOutput2, aOutput3, aOutput4) != null;
    }

    /**
     * Adds a Distillation Tower Recipe
     *
     * @param aInput   must be != null
     * @param aOutputs must be != null 1-5 Fluids
     * @param aOutput2 can be null
     */
    GT_Recipe addDistillationTowerRecipeRemovable(FluidStack aInput, FluidStack[] aOutputs, ItemStack aOutput2, int aDuration, int aEUt);
    @Deprecated
    default boolean addDistillationTowerRecipe(FluidStack aInput, FluidStack[] aOutputs, ItemStack aOutput2, int aDuration, int aEUt) {
        return addDistillationTowerRecipeRemovable(aInput, aOutputs, aOutput2, aDuration, aEUt) != null;
    }


    GT_Recipe addSimpleArcFurnaceRecipeRemovable(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt);
    @Deprecated
    default boolean addSimpleArcFurnaceRecipe(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt) {
        return addSimpleArcFurnaceRecipeRemovable(aInput, aFluidInput, aOutputs, aChances, aDuration, aEUt) != null;
    }

    GT_Recipe addPlasmaArcFurnaceRecipeRemovable(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt);
    @Deprecated
    default boolean addPlasmaArcFurnaceRecipe(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt) {
        return addPlasmaArcFurnaceRecipeRemovable(aInput, aFluidInput, aOutputs, aChances, aDuration, aEUt) != null;
    }

    GT_Recipe addPlasmaArcFurnaceRecipeRemovable(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs, FluidStack aFluidPutput, int[] aChances, int aDuration, int aEUt);
    @Deprecated
    default boolean addPlasmaArcFurnaceRecipe(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs, FluidStack aFluidPutput, int[] aChances, int aDuration, int aEUt) {
        return addPlasmaArcFurnaceRecipeRemovable(aInput, aFluidInput, aOutputs, aFluidPutput, aChances, aDuration, aEUt) != null;
    }


    /**
     * Adds a Distillation Tower Recipe
     */
    GT_Recipe addDistillationRecipeRemovable(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt);
    @Deprecated
    default boolean addDistillationRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt) {
        return addDistillationRecipeRemovable(aInput1, aInput2, aOutput1, aOutput2, aOutput3, aOutput4, aDuration, aEUt) != null;
    }

    /**
     * Adds a Lathe Machine Recipe
     */
    GT_Recipe addLatheRecipeRemovable(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt);
    @Deprecated
    default boolean addLatheRecipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
        return addLatheRecipeRemovable(aInput1, aOutput1, aOutput2, aDuration, aEUt) != null;
    }

    /**
     * Adds a Cutter Recipe
     */
    GT_Recipe[] addCutterRecipeRemovable(ItemStack aInput, FluidStack aLubricant, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt);
    @Deprecated
    default boolean addCutterRecipe(ItemStack aInput, FluidStack aLubricant, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
        return addCutterRecipeRemovable(aInput, aLubricant, aOutput1, aOutput2, aDuration, aEUt) != null;
    }

    /**
     * Adds Cutter Recipes with default Lubricants
     */
    GT_Recipe[] addCutterRecipeRemovable(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt);
    @Deprecated
    default boolean addCutterRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
        return addCutterRecipeRemovable(aInput, aOutput1, aOutput2, aDuration, aEUt) != null;
    }

    GT_Recipe[] addCutterRecipeRemovable(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, boolean aCleanroom);
    @Deprecated
    default boolean addCutterRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, boolean aCleanroom) {
        return addCutterRecipeRemovable(aInput, aOutput1, aOutput2, aDuration, aEUt, aCleanroom) != null;
    }

    GT_Recipe[] addCutterRecipeRemovable(ItemStack aInput, ItemStack aCircuit, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt);
    @Deprecated
    default boolean addCutterRecipe(ItemStack aInput, ItemStack aCircuit, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
        return addCutterRecipeRemovable(aInput, aCircuit, aOutput1, aOutput2, aDuration, aEUt) != null;
    }

    GT_Recipe[] addCutterRecipeRemovable(ItemStack aInput, ItemStack aCircuit, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, boolean aCleanroom);
    @Deprecated
    default boolean addCutterRecipe(ItemStack aInput, ItemStack aCircuit, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, boolean aCleanroom) {
        return addCutterRecipeRemovable(aInput, aCircuit, aOutput1, aOutput2, aDuration, aEUt, aCleanroom) != null;
    }

    GT_Recipe[] addCutterRecipeRemovable(ItemStack[] aInputs, ItemStack[] aOutputs, int aDuration, int aEUt, int aSpecial);
    @Deprecated
    default boolean addCutterRecipe(ItemStack[] aInputs, ItemStack[] aOutputs, int aDuration, int aEUt, int aSpecial) {
        return addCutterRecipeRemovable(aInputs, aOutputs, aDuration, aEUt, aSpecial) != null;
    }

    /**
     * Adds a Boxing Recipe
     */
    GT_Recipe addBoxingRecipeRemovable(ItemStack aContainedItem, ItemStack aEmptyBox, ItemStack aFullBox, int aDuration, int aEUt);

    /**
     * @param aInput    must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     * @param aEUt      should be > 0
     * @return
     */
    GT_Recipe addThermalCentrifugeRecipeRemovable(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, int aDuration, int aEUt);
    @Deprecated
    default boolean addThermalCentrifugeRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, int aDuration, int aEUt) {
        return addThermalCentrifugeRecipeRemovable(aInput, aOutput1, aOutput2, aOutput3, aDuration, aEUt) != null;
    }

    GT_Recipe addThermalCentrifugeRecipeRemovable(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, int[] aChances, int aDuration, int aEUt);
    @Deprecated
    default boolean addThermalCentrifugeRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, int[] aChances, int aDuration, int aEUt) {
        return addThermalCentrifugeRecipeRemovable(aInput, aOutput1, aOutput2, aOutput3, aChances, aDuration, aEUt) != null;
    }

    /**
     * Adds a Vacuum Freezer Recipe
     *
     * @param aInput1   must be != null
     * @param aOutput1  must be != null
     * @param aDuration must be > 0
     */
    GT_Recipe[] addVacuumFreezerRecipeRemovable(ItemStack aInput1, ItemStack aOutput1, int aDuration);
    @Deprecated
    default boolean addVacuumFreezerRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration) {
        return addVacuumFreezerRecipeRemovable(aInput1, aOutput1, aDuration) != null;
    }

    GT_Recipe[] addVacuumFreezerRecipeRemovable(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt);
    @Deprecated
    default boolean addVacuumFreezerRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
        return addVacuumFreezerRecipeRemovable(aInput1, aOutput1, aDuration, aEUt) != null;
    }

    GT_Recipe[] addVacuumFreezerRecipeRemovable(FluidStack aInput1, FluidStack aOutput1, int aDuration, int aEUt);
    @Deprecated
    default boolean addVacuumFreezerRecipe(FluidStack aInput1, FluidStack aOutput1, int aDuration, int aEUt) {
        return addVacuumFreezerRecipeRemovable(aInput1, aOutput1, aDuration, aEUt) != null;
    }

    /**
     * Adds a Fuel for My Generators
     *
     * @param aInput1  must be != null
     * @param aOutput1 can be null
     * @param aEU      EU per MilliBucket. If no Liquid Form of this Container is available, then it will give you EU*1000 per Item.
     * @param aType    0 = Diesel; 1 = Gas Turbine; 2 = Thermal; 3 = Dense Fluid; 4 = Plasma; 5 = Magic; And if something is unclear or missing, then look at the GT_Recipe-Class
     */
    GT_Recipe[] addFuelRemovable(ItemStack aInput1, ItemStack aOutput1, int aEU, int aType);
    @Deprecated
    default boolean addFuel(ItemStack aInput1, ItemStack aOutput1, int aEU, int aType) {
        return addFuelRemovable(aInput1, aOutput1, aEU, aType) != null;
    }

    /**
     * Adds an Amplifier Recipe for the Amplifabricator
     */
    GT_Recipe addAmplifierRemovable(ItemStack aAmplifierItem, int aDuration, int aAmplifierAmountOutputted);
    @Deprecated
    default boolean addAmplifier(ItemStack aAmplifierItem, int aDuration, int aAmplifierAmountOutputted) {
        return addAmplifierRemovable(aAmplifierItem, aDuration, aAmplifierAmountOutputted) != null;
    }

    /**
     * Adds a Recipe for the Brewing Machine (intentionally limited to Fluid IDs)
     */
    GT_Recipe addBrewingRecipeRemovable(ItemStack aIngredient, Fluid aInput, Fluid aOutput, boolean aHidden);
    @Deprecated
    default boolean addBrewingRecipe(ItemStack aIngredient, Fluid aInput, Fluid aOutput, boolean aHidden) {
        return addBrewingRecipeRemovable(aIngredient, aInput, aOutput, aHidden) != null;
    }

    GT_Recipe addBrewingRecipeRemovable(ItemStack aIngredient, Fluid aInput, Fluid aOutput, int aDuration, int aEUt, boolean aHidden);
    @Deprecated
    default boolean addBrewingRecipe(ItemStack aIngredient, Fluid aInput, Fluid aOutput, int aDuration, int aEUt, boolean aHidden) {
        return addBrewingRecipeRemovable(aIngredient, aInput, aOutput, aDuration, aEUt, aHidden) != null;
    }

    GT_Recipe addBrewingRecipeCustomRemovable(ItemStack aIngredient, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt, boolean aHidden);
    @Deprecated
    default boolean addBrewingRecipeCustom(ItemStack aIngredient, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt, boolean aHidden) {
        return addBrewingRecipeCustomRemovable(aIngredient, aInput, aOutput, aDuration, aEUt, aHidden) != null;
    }

    /**
     * Adds a Recipe for the Fermenter
     */
    GT_Recipe addFermentingRecipeRemovable(FluidStack aInput, FluidStack aOutput, int aDuration, boolean aHidden);
    @Deprecated
    default boolean addFermentingRecipe(FluidStack aInput, FluidStack aOutput, int aDuration, boolean aHidden) {
        return addFermentingRecipeRemovable(aInput, aOutput, aDuration, aHidden) != null;
    }

    GT_Recipe addFermentingRecipeRemovable(FluidStack aInput, FluidStack aOutput, int aDuration, int aEUT, boolean aHidden);
    @Deprecated
    default boolean addFermentingRecipe(FluidStack aInput, FluidStack aOutput, int aDuration, int aEUT, boolean aHidden) {
        return addFermentingRecipeRemovable(aInput, aOutput, aDuration, aEUT, aHidden) != null;
    }

    /**
     * Adds a Recipe for the Fluid Heater
     */
    GT_Recipe addFluidHeaterRecipeRemovable(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt);
    @Deprecated
    default boolean addFluidHeaterRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt) {
        return addFluidHeaterRecipeRemovable(aCircuit, aInput, aOutput, aDuration, aEUt) != null;
    }

    /**
     * Adds a Recipe for the Distillery
     */

    GT_Recipe addDistilleryRecipeRemovable(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, ItemStack aSolidOutput, int aDuration, int aEUt, boolean aHidden);
    @Deprecated
    default boolean addDistilleryRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, ItemStack aSolidOutput, int aDuration, int aEUt, boolean aHidden) {
        return addDistilleryRecipeRemovable(aCircuit, aInput, aOutput, aSolidOutput, aDuration, aEUt, aHidden) != null;
    }

    GT_Recipe addDistilleryRecipeRemovable(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt, boolean aHidden);
    @Deprecated
    default boolean addDistilleryRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt, boolean aHidden) {
        return addDistilleryRecipeRemovable(aCircuit, aInput, aOutput, aDuration, aEUt, aHidden) != null;
    }

    GT_Recipe addDistilleryRecipeRemovable(int circuitConfig, FluidStack aInput, FluidStack aOutput, ItemStack aSolidOutput, int aDuration, int aEUt, boolean aHidden);
    @Deprecated
    default boolean addDistilleryRecipe(int circuitConfig, FluidStack aInput, FluidStack aOutput, ItemStack aSolidOutput, int aDuration, int aEUt, boolean aHidden) {
        return addDistilleryRecipeRemovable(circuitConfig, aInput, aOutput, aSolidOutput, aDuration, aEUt, aHidden) != null;
    }

    GT_Recipe addDistilleryRecipeRemovable(int aCircuit, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt, boolean aHidden);
    @Deprecated
    default boolean addDistilleryRecipe(int aCircuit, FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt, boolean aHidden) {
        return addDistilleryRecipeRemovable(aCircuit, aInput, aOutput, aDuration, aEUt, aHidden) != null;
    }

    /**
     * Adds a Recipe for the Fluid Solidifier
     */
    GT_Recipe addFluidSolidifierRecipeRemovable(ItemStack aMold, FluidStack aInput, ItemStack aOutput, int aDuration, int aEUt);
    @Deprecated
    default boolean addFluidSolidifierRecipe(ItemStack aMold, FluidStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        return addFluidSolidifierRecipeRemovable(aMold, aInput, aOutput, aDuration, aEUt) != null;
    }

    /**
     * Adds a Recipe for Fluid Smelting
     */
    GT_Recipe addFluidSmelterRecipeRemovable(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance, int aDuration, int aEUt);
    @Deprecated
    default boolean addFluidSmelterRecipe(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance, int aDuration, int aEUt) {
        return addFluidSmelterRecipeRemovable(aInput, aRemains, aOutput, aChance, aDuration, aEUt) != null;
    }

    /**
     * Adds a Recipe for Fluid Smelting
     */
    GT_Recipe addFluidSmelterRecipeRemovable(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance, int aDuration, int aEUt, boolean hidden);
    @Deprecated
    default boolean addFluidSmelterRecipe(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance, int aDuration, int aEUt, boolean hidden) {
        return addFluidSmelterRecipeRemovable(aInput, aRemains, aOutput, aChance, aDuration, aEUt, hidden) != null;
    }

    /**
     * Adds a Recipe for Fluid Extraction
     */
    GT_Recipe addFluidExtractionRecipeRemovable(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance, int aDuration, int aEUt);
    @Deprecated
    default boolean addFluidExtractionRecipe(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance, int aDuration, int aEUt) {
        return addFluidExtractionRecipeRemovable(aInput, aRemains, aOutput, aChance, aDuration, aEUt) != null;
    }

    /**
     * Adds a Recipe for the Fluid Canner
     */
    GT_Recipe addFluidCannerRecipeRemovable(ItemStack aInput, ItemStack aOutput, FluidStack aFluidInput, FluidStack aFluidOutput);
    @Deprecated
    default boolean addFluidCannerRecipe(ItemStack aInput, ItemStack aOutput, FluidStack aFluidInput, FluidStack aFluidOutput) {
        return addFluidCannerRecipeRemovable(aInput, aOutput, aFluidInput, aFluidOutput) != null;
    }

    GT_Recipe addFluidCannerRecipeRemovable(ItemStack aInput, ItemStack aOutput, FluidStack aFluidInput, FluidStack aFluidOutput, int aDuration, int aEUt);
    @Deprecated
    default boolean addFluidCannerRecipe(ItemStack aInput, ItemStack aOutput, FluidStack aFluidInput, FluidStack aFluidOutput, int aDuration, int aEUt) {
        return addFluidCannerRecipeRemovable(aInput, aOutput, aFluidInput, aFluidOutput, aDuration, aEUt) != null;
    }

    /**
     * Adds a Recipe for the Chemical Bath
     */
    GT_Recipe addChemicalBathRecipeRemovable(ItemStack aInput, FluidStack aBathingFluid, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, int[] aChances, int aDuration, int aEUt);
    @Deprecated
    default boolean addChemicalBathRecipe(ItemStack aInput, FluidStack aBathingFluid, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, int[] aChances, int aDuration, int aEUt) {
        return addChemicalBathRecipeRemovable(aInput, aBathingFluid, aOutput1, aOutput2, aOutput3, aChances, aDuration, aEUt) != null;
    }

    /**
     * Adds a Recipe for the Electromagnetic Separator
     */
    GT_Recipe addElectromagneticSeparatorRecipeRemovable(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, int[] aChances, int aDuration, int aEUt);
    @Deprecated
    default boolean addElectromagneticSeparatorRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, int[] aChances, int aDuration, int aEUt) {
        return addElectromagneticSeparatorRecipeRemovable(aInput, aOutput1, aOutput2, aOutput3, aChances, aDuration, aEUt) != null;
    }

    /**
     * Adds a Recipe for the Extractor
     */
    GT_Recipe addExtractorRecipeRemovable(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt);
    @Deprecated
    default boolean addExtractorRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        return addExtractorRecipeRemovable(aInput, aOutput, aDuration, aEUt) != null;
    }

    /**
     * Adds a Recipe for the Printer
     */
    GT_Recipe addPrinterRecipeRemovable(ItemStack aInput, FluidStack aFluid, ItemStack aSpecialSlot, ItemStack aOutput, int aDuration, int aEUt);
    @Deprecated
    default boolean addPrinterRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aSpecialSlot, ItemStack aOutput, int aDuration, int aEUt) {
        return addPrinterRecipeRemovable(aInput, aFluid, aSpecialSlot, aOutput, aDuration, aEUt) != null;
    }

    /**
     * Adds a Recipe for the Autoclave
     */
    GT_Recipe addAutoclaveRecipeRemovable(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration, int aEUt);
    @Deprecated
    default boolean addAutoclaveRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration, int aEUt) {
        return addAutoclaveRecipeRemovable(aInput, aFluid, aOutput, aChance, aDuration, aEUt) != null;
    }

    GT_Recipe addAutoclaveRecipeRemovable(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom);
    @Deprecated
    default boolean addAutoclaveRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom) {
        return addAutoclaveRecipeRemovable(aInput, aFluid, aOutput, aChance, aDuration, aEUt, aCleanroom) != null;
    }

    GT_Recipe addAutoclaveRecipeRemovable(ItemStack aInput, ItemStack aCircuit, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom);
    @Deprecated
    default boolean addAutoclaveRecipe(ItemStack aInput, ItemStack aCircuit, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom) {
        return addAutoclaveRecipeRemovable(aInput, aCircuit, aFluid, aOutput, aChance, aDuration, aEUt, aCleanroom) != null;
    }

    GT_Recipe addAutoclaveRecipeRemovable(ItemStack aInput, ItemStack aCircuit, FluidStack aFluidIn, FluidStack aFluidOut, ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom);
    @Deprecated
    default boolean addAutoclaveRecipe(ItemStack aInput, ItemStack aCircuit, FluidStack aFluidIn, FluidStack aFluidOut, ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom) {
        return addAutoclaveRecipeRemovable(aInput, aCircuit, aFluidIn, aFluidOut, aOutput, aChance, aDuration, aEUt, aCleanroom) != null;
    }

    GT_Recipe addAutoclaveSpaceRecipeRemovable(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom);
    @Deprecated
    default boolean addAutoclaveSpaceRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom) {
        return addAutoclaveSpaceRecipeRemovable(aInput, aFluid, aOutput, aChance, aDuration, aEUt, aCleanroom) != null;
    }

    GT_Recipe addAutoclaveSpaceRecipeRemovable(ItemStack aInput, ItemStack aCircuit, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom);
    @Deprecated
    default boolean addAutoclaveSpaceRecipe(ItemStack aInput, ItemStack aCircuit, FluidStack aFluid, ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom) {
        return addAutoclaveSpaceRecipeRemovable(aInput, aCircuit, aFluid, aOutput, aChance, aDuration, aEUt, aCleanroom) != null;
    }


    /**
     * Adds a Recipe for the Mixer
     */
    GT_Recipe addMixerRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt);
    @Deprecated
    default boolean addMixerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt) {
        return addMixerRecipeRemovable(aInput1, aInput2, aInput3, aInput4, aFluidInput, aFluidOutput, aOutput, aDuration, aEUt) != null;
    }

    GT_Recipe addMixerRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4, ItemStack aInput5, ItemStack aInput6, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt);
    @Deprecated
    default boolean addMixerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4, ItemStack aInput5, ItemStack aInput6, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt) {
        return addMixerRecipeRemovable(aInput1, aInput2, aInput3, aInput4, aInput5, aInput6, aFluidInput, aFluidOutput, aOutput, aDuration, aEUt) != null;
    }

    GT_Recipe addMixerRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4, ItemStack aInput5, ItemStack aInput6, ItemStack aInput7, ItemStack aInput8, ItemStack aInput9, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt);
    @Deprecated
    default boolean addMixerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4, ItemStack aInput5, ItemStack aInput6, ItemStack aInput7, ItemStack aInput8, ItemStack aInput9, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt) {
        return addMixerRecipeRemovable(aInput1, aInput2, aInput3, aInput4, aInput5, aInput6, aInput7, aInput8, aInput9, aFluidInput, aFluidOutput, aOutput, aDuration, aEUt) != null;
    }

    GT_Recipe addMixerRecipeRemovable(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4, ItemStack aInput5, ItemStack aInput6, ItemStack aInput7, ItemStack aInput8, ItemStack aInput9, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt);
    @Deprecated
    default boolean addMixerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4, ItemStack aInput5, ItemStack aInput6, ItemStack aInput7, ItemStack aInput8, ItemStack aInput9, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt) {
        return addMixerRecipeRemovable(aInput1, aInput2, aInput3, aInput4, aInput5, aInput6, aInput7, aInput8, aInput9, aFluidInput, aFluidOutput, aOutput1, aOutput2, aOutput3, aOutput4, aDuration, aEUt) != null;
    }

    /**
     * Adds a Recipe for the Laser Engraver
     */
    GT_Recipe addLaserEngraverRecipeRemovable(ItemStack aItemToEngrave, ItemStack aLens, ItemStack aEngravedItem, int aDuration, int aEUt);
    @Deprecated
    default boolean addLaserEngraverRecipe(ItemStack aItemToEngrave, ItemStack aLens, ItemStack aEngravedItem, int aDuration, int aEUt) {
        return addLaserEngraverRecipeRemovable(aItemToEngrave, aLens, aEngravedItem, aDuration, aEUt) != null;
    }

    /**
     * Adds a Recipe for the Laser Engraver
     */
    GT_Recipe addLaserEngraverRecipeRemovable(ItemStack aItemToEngrave, ItemStack aLens, ItemStack aEngravedItem, int aDuration, int aEUt, boolean aCleanroom);
    @Deprecated
    default boolean addLaserEngraverRecipe(ItemStack aItemToEngrave, ItemStack aLens, ItemStack aEngravedItem, int aDuration, int aEUt, boolean aCleanroom) {
        return addLaserEngraverRecipeRemovable(aItemToEngrave, aLens, aEngravedItem, aDuration, aEUt, aCleanroom) != null;
    }

    /**
     * Adds a Recipe for the Forming Press
     */
    GT_Recipe addFormingPressRecipeRemovable(ItemStack aItemToImprint, ItemStack aForm, ItemStack aImprintedItem, int aDuration, int aEUt);
    @Deprecated
    default boolean addFormingPressRecipe(ItemStack aItemToImprint, ItemStack aForm, ItemStack aImprintedItem, int aDuration, int aEUt) {
        return addFormingPressRecipeRemovable(aItemToImprint, aForm, aImprintedItem, aDuration, aEUt) != null;
    }

    /**
     * Adds a Recipe for the Sifter. (up to 9 Outputs)
     */
    GT_Recipe addSifterRecipeRemovable(ItemStack aItemToSift, ItemStack[] aSiftedItems, int[] aChances, int aDuration, int aEUt);
    @Deprecated
    default boolean addSifterRecipe(ItemStack aItemToSift, ItemStack[] aSiftedItems, int[] aChances, int aDuration, int aEUt) {
        return addSifterRecipeRemovable(aItemToSift, aSiftedItems, aChances, aDuration, aEUt) != null;
    }

    /**
     * Adds a Recipe for the Arc Furnace. (up to 4 Outputs)
     */
    GT_Recipe[][] addArcFurnaceRecipeRemovable(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt);
    @Deprecated
    default boolean addArcFurnaceRecipe(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt) {
        return addArcFurnaceRecipeRemovable(aInput, aOutputs, aChances, aDuration, aEUt) != null;
    }

    /**
     * Adds a Recipe for the Arc Furnace. (up to 4 Outputs)
     */
    GT_Recipe[][] addArcFurnaceRecipeRemovable(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt, boolean hidden);
    @Deprecated
    default boolean addArcFurnaceRecipe(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt, boolean hidden) {
        return addArcFurnaceRecipeRemovable(aInput, aOutputs, aChances, aDuration, aEUt, hidden) != null;
    }


    /**
     * Adds a Recipe for the GT Pulveriser. (up to 4 Outputs)
     */
    GT_Recipe addPulveriserRecipeRemovable(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt);
    @Deprecated
    default boolean addPulveriserRecipe(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt) {
        return addPulveriserRecipeRemovable(aInput, aOutputs, aChances, aDuration, aEUt) != null;
    }

    /**
     * Adds a Recipe for the GT Pulveriser. (up to 4 Outputs)
     */
    GT_Recipe addPulveriserRecipeRemovable(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt, boolean hidden);
    @Deprecated
    default boolean addPulveriserRecipe(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt, boolean hidden) {
        return addPulveriserRecipeRemovable(aInput, aOutputs, aChances, aDuration, aEUt, hidden) != null;
    }

    /**
     * Adds a Distillation Tower Recipe
     * Every Fluid also gets separate distillation recipes
     *
     * @param aInput   must be != null
     * @param aOutputs must be != null 1-5 Fluids
     * @param aOutput2 can be null
     */
    GT_Recipe[][] addUniversalDistillationRecipeRemovable(FluidStack aInput, FluidStack[] aOutputs, ItemStack aOutput2, int aDuration, int aEUt);
    @Deprecated
    default boolean addUniversalDistillationRecipe(FluidStack aInput, FluidStack[] aOutputs, ItemStack aOutput2, int aDuration, int aEUt) {
        return addUniversalDistillationRecipeRemovable(aInput, aOutputs, aOutput2, aDuration, aEUt) != null;
    }

    /**
     * Adds Pyrolyse Recipe
     *
     * @param aInput
     * @param intCircuit
     * @param aOutput
     * @param aFluidOutput
     * @param aDuration
     * @param aEUt
     */
    GT_Recipe addPyrolyseRecipeRemovable(ItemStack aInput, FluidStack aFluidInput, int intCircuit, ItemStack aOutput, FluidStack aFluidOutput, int aDuration, int aEUt);
    @Deprecated
    default boolean addPyrolyseRecipe(ItemStack aInput, FluidStack aFluidInput, int intCircuit, ItemStack aOutput, FluidStack aFluidOutput, int aDuration, int aEUt) {
        return addPyrolyseRecipeRemovable(aInput, aFluidInput, intCircuit, aOutput, aFluidOutput, aDuration, aEUt) != null;
    }

    /**
     * Adds Oil Cracking Recipe
     *
     * @param aInput
     * @param aOutput
     * @param aDuration
     * @param aEUt
     */

    @Deprecated
    GT_Recipe addCrackingRecipeRemovable(FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt);
    @Deprecated
    default boolean addCrackingRecipe(FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt) {
        return addCrackingRecipeRemovable(aInput, aOutput, aDuration, aEUt) != null;
    }

    /**
     * Adds Oil Cracking Recipe
     *
     * @param circuitConfig The circuit configuration to control cracking severity
     * @param aInput        The fluid to be cracked
     * @param aInput2       The fluid to catalyze the cracking (typically Hydrogen or Steam)
     * @param aOutput       The cracked fluid
     * @param aDuration
     * @param aEUt
     */

    GT_Recipe addCrackingRecipeRemovable(int circuitConfig, FluidStack aInput, FluidStack aInput2, FluidStack aOutput, int aDuration, int aEUt);
    @Deprecated
    default boolean addCrackingRecipe(int circuitConfig, FluidStack aInput, FluidStack aInput2, FluidStack aOutput, int aDuration, int aEUt) {
        return addCrackingRecipeRemovable(circuitConfig, aInput, aInput2, aOutput, aDuration, aEUt) != null;
    }

    /**
     * Adds a Sound to the Sonictron9001
     * you should NOT call this in the preInit-Phase!
     *
     * @param aItemStack = The Item you want to display for this Sound
     * @param aSoundName = The Name of the Sound in the resources/newsound-folder like Vanillasounds
     * @return true if the Sound got added, otherwise false.
     */
    int addSonictronSoundRemovable(ItemStack aItemStack, String aSoundName);
    @Deprecated
    default boolean addSonictronSound(ItemStack aItemStack, String aSoundName) {
        return addSonictronSoundRemovable(aItemStack, aSoundName) != -1;
    }
    void removeFusionReactorRecipe(GT_Recipe recipe);
    void removeCentrifugeRecipe(GT_Recipe recipe);
    void removeCompressorRecipe(GT_Recipe recipe);
    void removeElectrolyzerRecipe(GT_Recipe recipe);
    void removeChemicalRecipe(GT_Recipe[] recipe);
    void removeChemicalRecipeForBasicMachineOnly(GT_Recipe recipe);
    void removeMultiblockChemicalRecipe(GT_Recipe recipe);
    void removeBlastRecipe(GT_Recipe recipe);
    void removePrimitiveBlastRecipe(GT_Recipe[] recipe);
    void removeCannerRecipe(GT_Recipe recipe);
    void removeAlloySmelterRecipe(GT_Recipe recipe);
    void removeCNCRecipe(GT_Recipe recipe);
    void removeAssemblerRecipe(GT_Recipe[] recipe);
    void removeCircuitAssemblerRecipe(GT_Recipe[] recipe);
    void removeAssemblylineRecipe(Pair<GT_Recipe[], GT_Recipe.GT_Recipe_AssemblyLine> recipe);
    void removeForgeHammerRecipe(GT_Recipe recipe);
    void removeWiremillRecipe(GT_Recipe recipe);
    void removePolarizerRecipe(GT_Recipe recipe);
    void removeBenderRecipe(GT_Recipe recipe);
    void removeExtruderRecipe(GT_Recipe recipe);
    void removeSlicerRecipe(GT_Recipe recipe);
    void removeOreWasherRecipe(GT_Recipe recipe);
    void removeImplosionRecipe(GT_Recipe[] recipe);
    void removeGrinderRecipe(GT_Recipe recipe);
    void removeDistillationTowerRecipe(GT_Recipe recipe);
    void removeSimpleArcFurnaceRecipe(GT_Recipe recipe);
    void removePlasmaArcFurnaceRecipe(GT_Recipe recipe);
    void removeDistillationRecipe(GT_Recipe recipe);
    void removeLatheRecipe(GT_Recipe recipe);
    void removeCutterRecipe(GT_Recipe[] recipe);
    void removeBoxingRecipe(GT_Recipe recipe);
    void removeThermalCentrifugeRecipe(GT_Recipe recipe);
    void removeVacuumFreezerRecipe(GT_Recipe[] recipe);
    void removeFuel(GT_Recipe[] recipe, int aType);
    void removeAmplifier(GT_Recipe recipe);
    void removeBrewingRecipe(GT_Recipe recipe);
    void removeBrewingRecipeCustom(GT_Recipe recipe);
    void removeFermentingRecipe(GT_Recipe recipe);
    void removeFluidHeaterRecipe(GT_Recipe recipe);
    void removeDistilleryRecipe(GT_Recipe recipe);
    void removeFluidSolidifierRecipe(GT_Recipe recipe);
    void removeFluidSmelterRecipe(GT_Recipe recipe);
    void removeFluidExtractionRecipe(GT_Recipe recipe);
    void removeFluidCannerRecipe(GT_Recipe recipe);
    void removeChemicalBathRecipe(GT_Recipe recipe);
    void removeElectromagneticSeparatorRecipe(GT_Recipe recipe);
    void removeExtractorRecipe(GT_Recipe recipe);
    void removePrinterRecipe(GT_Recipe recipe);
    void removeAutoclaveRecipe(GT_Recipe recipe);
    void removeAutoclaveSpaceRecipe(GT_Recipe recipe);
    void removeMixerRecipe(GT_Recipe recipe);
    void removeLaserEngraverRecipe(GT_Recipe recipe);
    void removeFormingPressRecipe(GT_Recipe recipe);
    void removeSifterRecipe(GT_Recipe recipe);
    void removeArcFurnaceRecipe(GT_Recipe[][] recipe);
    void removePulveriserRecipe(GT_Recipe recipe);
    void removeUniversalDistillationRecipe(GT_Recipe[][] recipe);
    void removePyrolyseRecipe(GT_Recipe recipe);
    void removeCrackingRecipe(GT_Recipe recipe);
    void removeSonictronSound(int index);
}
