package gregtech.api.util.interop.ic2;


import cofh.api.energy.IEnergyReceiver;
import com.google.common.base.Stopwatch;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.ProgressManager;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.*;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.reactor.IReactorChamber;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.GT_Mod.gregtechproxy;


public class IC2Interop extends IC2InteropBase {

    public static final IC2Interop INSTANCE = new IC2Interop();

    private IC2Interop() {
    }

    /**
     * @param tile
     * @return
     */
    @Optional.Method(modid = "IC2")
    @Override
    public TileEntity getIC2EnergyTile(final TileEntity tile) {
        TileEntity ic2Energy;
        if (tile instanceof IReactorChamber) {
            ic2Energy = (TileEntity) ((IReactorChamber)tile).getReactor();
        } else {
            if (tile instanceof IEnergyTile || EnergyNet.instance == null) {
                ic2Energy = tile;
            } else {
                ic2Energy = EnergyNet.instance.getTileEntity(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
            }
        }
        return ic2Energy;
    }

    /**
     * @param base
     * @param target
     * @param side
     * @return
     */
    @Optional.Method(modid = "IC2")
    @Override
    public boolean isValidIC2Tile(final TileEntity base, final TileEntity target, final ForgeDirection side) {
        if (target instanceof IEnergySink && ((IEnergySink) target).acceptsEnergyFrom(base, side)) {
            return true;
        }
        if (gregtechproxy.ic2EnergySourceCompat && target instanceof IEnergySource) {
            return true;
        }
        return GregTech_API.mInputRF && target instanceof IEnergyEmitter && ((IEnergyEmitter) target).emitsEnergyTo(base, side);
    }

    /**
     * @param tile
     * @return
     */
    @Optional.Method(modid = "IC2")
    @Override
    public boolean isEmitter(final TileEntity tile) {
       return tile instanceof IEnergyEmitter;
    }

    /**
     * @param tile
     * @return
     */
    @Optional.Method(modid = "IC2")
    @Override
    public boolean isReceiver(final TileEntity tile) {
        return tile instanceof IEnergyReceiver;
    }

    /**
     * @param tile
     * @return
     */
    @Optional.Method(modid = "IC2")
    @Override
    public boolean isEnergyTile(final TileEntity tile) {
        return tile instanceof IEnergyTile;
    }

    /**
     * @return
     */
    @Optional.Method(modid = "IC2")
    @Override
    public boolean isEnetNull() {
        return EnergyNet.instance == null;
    }

    /**
     * @return
     */
    @Optional.Method(modid = "IC2")
    @Override
    public boolean shouldJoinIC2Enet(final TileEntity target) {
        TileEntity temp = null;
        if (target == null || target instanceof IEnergyTile || isEnetNull()) {
            temp = target;
        } else {
            temp = EnergyNet.instance.getTileEntity(target.getWorldObj(), target.xCoord, target.yCoord, target.zCoord);
        }
        return temp instanceof IEnergyEmitter;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public void parseIC2Recipes(final Stopwatch stopwatch) {
        // Save a copy of these list before activateOreDictHandler(), then loop over them.
        Map<IRecipeInput, RecipeOutput> aMaceratorRecipeList = GT_ModHandler.getMaceratorRecipeList();
        Map<IRecipeInput, RecipeOutput> aCompressorRecipeList = GT_ModHandler.getCompressorRecipeList();
        Map<IRecipeInput, RecipeOutput> aExtractorRecipeList = GT_ModHandler.getExtractorRecipeList();
        Map<IRecipeInput, RecipeOutput> aOreWashingRecipeList = GT_ModHandler.getOreWashingRecipeList();
        Map<IRecipeInput, RecipeOutput> aThermalCentrifugeRecipeList = GT_ModHandler.getThermalCentrifugeRecipeList();

        GT_Log.out.println("GT_Mod: Activating OreDictionary Handler, this can take some time, as it scans the whole OreDictionary");
        GT_FML_LOGGER.info("If your Log stops here, you were too impatient. Wait a bit more next time, before killing Minecraft with the Task Manager.");


        gregtechproxy.activateOreDictHandler();
        GT_FML_LOGGER.info("Congratulations, you have been waiting long enough (" + stopwatch.stop() + "). Have a Cake.");
        GT_Log.out.println("GT_Mod: List of Lists of Tool Recipes: " + GT_ModHandler.sSingleNonBlockDamagableRecipeList_list.toString());
        GT_Log.out.println("GT_Mod: Vanilla Recipe List -> Outputs null or stackSize <=0: " + GT_ModHandler.sVanillaRecipeList_warntOutput.toString());
        GT_Log.out.println("GT_Mod: Single Non Block Damagable Recipe List -> Outputs null or stackSize <=0: " + GT_ModHandler.sSingleNonBlockDamagableRecipeList_warntOutput.toString());

        Set<Materials> replaceVanillaItemsSet = gregtechproxy.mUseGreatlyShrukenReplacementList ? Arrays.stream(Materials.values()).filter(
                GT_RecipeRegistrator::hasVanillaRecipes).collect(Collectors.toSet()) : new HashSet<>(Arrays.asList(Materials.values()));

        stopwatch.reset();
        stopwatch.start();
        GT_FML_LOGGER.info("Replacing Vanilla Materials in recipes, please wait.");

        ProgressManager.ProgressBar progressBar = ProgressManager.push("Register materials", replaceVanillaItemsSet.size());
        if (GT_Values.cls_enabled){
            try {
                GT_CLS_Compat.doActualRegistrationCLS(progressBar, replaceVanillaItemsSet);
                GT_CLS_Compat.pushToDisplayProgress();
            } catch (InvocationTargetException | IllegalAccessException e) {
                GT_FML_LOGGER.catching(e);
            }
        }
        else {
            replaceVanillaItemsSet.forEach(m -> {
                progressBar.step(m.mDefaultLocalName);
                GT_Mod.doActualRegistration(m);
            });
        }
        ProgressManager.pop(progressBar);
        GT_FML_LOGGER.info("Replaced Vanilla Materials (" + stopwatch.stop() + "). Have a Cake.");


        stopwatch.reset();
        stopwatch.start();
        // remove gemIridium exploit
        ItemStack iridiumOre = GT_ModHandler.getIC2Item("iridiumOre", 1);
        aCompressorRecipeList.entrySet().stream()
                             .filter(e -> e.getKey().getInputs().size() == 1 && e.getKey().getInputs().get(0).isItemEqual(iridiumOre))
                             .findAny()
                             .ifPresent(e -> aCompressorRecipeList.remove(e.getKey()));
        //Add default IC2 recipe to GT
        GT_ModHandler.addIC2RecipesToGT(aMaceratorRecipeList, GT_Recipe.GT_Recipe_Map.sMaceratorRecipes, true, true, true);
        GT_ModHandler.addIC2RecipesToGT(aCompressorRecipeList, GT_Recipe.GT_Recipe_Map.sCompressorRecipes, true, true, true);
        GT_ModHandler.addIC2RecipesToGT(aExtractorRecipeList, GT_Recipe.GT_Recipe_Map.sExtractorRecipes, true, true, true);
        GT_ModHandler.addIC2RecipesToGT(aOreWashingRecipeList, GT_Recipe.GT_Recipe_Map.sOreWasherRecipes, false, true, true);
        GT_ModHandler.addIC2RecipesToGT(aThermalCentrifugeRecipeList, GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes, true, true, true);
        GT_FML_LOGGER.info("IC2 Removal (" + stopwatch.stop() + "). Have a Cake.");
    }

}
