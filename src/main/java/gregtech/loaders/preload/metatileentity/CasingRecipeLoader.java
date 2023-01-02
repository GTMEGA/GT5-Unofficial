package gregtech.loaders.preload.metatileentity;

import net.minecraft.item.ItemStack;

import static gregtech.api.enums.Dyes.dyeBlack;
import static gregtech.api.enums.Dyes.dyeYellow;
import static gregtech.api.enums.ItemList.*;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.OrePrefixes.*;
import static gregtech.api.util.GT_ModHandler.addCraftingRecipe;
import static gregtech.api.util.GT_ModHandler.addShapelessCraftingRecipe;
import static gregtech.loaders.preload.GT_Loader_MetaTileEntities.RECIPE_MASK;
import static net.minecraft.init.Blocks.brick_block;
import static net.minecraft.init.Blocks.iron_bars;

public final class CasingRecipeLoader {
    private static boolean LOADED = false;

    public static void load() {
        if (LOADED)
            throw new RuntimeException("Already loaded!");
        addCraftingRecipe(Casing_ULV.getOne(), RECIPE_MASK, new Object[]{"PPP", "PwP", "PPP", 'P', plate.get(Tin)});
        addCraftingRecipe(Casing_LV.getOne(), RECIPE_MASK, new Object[]{"PPP", "PwP", "PPP", 'P', plate.get(Steel)});
        addCraftingRecipe(Casing_MV.getOne(), RECIPE_MASK, new Object[]{"PPP", "PwP", "PPP", 'P', plate.get(Aluminium)});
        addCraftingRecipe(Casing_HV.getOne(), RECIPE_MASK, new Object[]{"PPP", "PwP", "PPP", 'P', plate.get(StainlessSteel)});
        addCraftingRecipe(Casing_EV.getOne(), RECIPE_MASK, new Object[]{"PPP", "PwP", "PPP", 'P', plate.get(Titanium)});
        addCraftingRecipe(Casing_IV.getOne(), RECIPE_MASK, new Object[]{"PPP", "PwP", "PPP", 'P', plate.get(TungstenSteel)});
        addCraftingRecipe(Casing_LuV.getOne(), RECIPE_MASK, new Object[]{"PPP", "PwP", "PPP", 'P', plate.get(Chrome)});
        addCraftingRecipe(Casing_ZPM.getOne(), RECIPE_MASK, new Object[]{"PPP", "PwP", "PPP", 'P', plate.get(Iridium)});
        addCraftingRecipe(Casing_UV.getOne(), RECIPE_MASK, new Object[]{"PPP", "PwP", "PPP", 'P', plate.get(Osmium)});
        addCraftingRecipe(Casing_MAX.getOne(), RECIPE_MASK, new Object[]{"PPP", "PwP", "PPP", 'P', plate.get(Neutronium)});

        addCraftingRecipe(Casing_Pipe_Polytetrafluoroethylene.getOne(), RECIPE_MASK, new Object[]{"PIP", "IFI", "PIP", 'P', plate.get(Polytetrafluoroethylene), 'F', frameGt.get(Polytetrafluoroethylene), 'I', pipeMedium.get(Polytetrafluoroethylene)});
        addCraftingRecipe(Casing_BronzePlatedBricks.getOne(), RECIPE_MASK, new Object[]{"PhP", "PBP", "PwP", 'P', plate.get(Bronze), 'B', new ItemStack(brick_block, 1)});
        addCraftingRecipe(Casing_SolidSteel.getOne(), RECIPE_MASK, new Object[]{"PhP", "PFP", "PwP", 'P', plate.get(Steel), 'F', frameGt.get(Steel)});
        addCraftingRecipe(Casing_StableTitanium.getOne(), RECIPE_MASK, new Object[]{"PhP", "PFP", "PwP", 'P', plate.get(Titanium), 'F', frameGt.get(Titanium)});
        addCraftingRecipe(Casing_HeatProof.getOne(), RECIPE_MASK, new Object[]{"PhP", "PFP", "PwP", 'P', plate.get(Invar), 'F', frameGt.get(Invar)});
        addCraftingRecipe(Casing_FrostProof.getOne(), RECIPE_MASK, new Object[]{"PhP", "PFP", "PwP", 'P', plate.get(Aluminium), 'F', frameGt.get(Aluminium)});
        addCraftingRecipe(Casing_CleanStainlessSteel.getOne(), RECIPE_MASK, new Object[]{"PhP", "PFP", "PwP", 'P', plate.get(StainlessSteel), 'F', frameGt.get(StainlessSteel)});
        addCraftingRecipe(Casing_RobustTungstenSteel.getOne(), RECIPE_MASK, new Object[]{"PhP", "PFP", "PwP", 'P', plate.get(TungstenSteel), 'F', frameGt.get(TungstenSteel)});
        addCraftingRecipe(Casing_MiningOsmiridium.getOne(), RECIPE_MASK, new Object[]{"PhP", "PFP", "PwP", 'P', plate.get(Osmiridium), 'F', frameGt.get(Osmiridium)});
        addCraftingRecipe(Casing_MiningNeutronium.getOne(), RECIPE_MASK, new Object[]{"PhP", "PFP", "PwP", 'P', plate.get(Neutronium), 'F', frameGt.get(Neutronium)});
        addCraftingRecipe(Casing_MiningBlackPlutonium.getOne(), RECIPE_MASK, new Object[]{"PhP", "PFP", "PwP", 'P', plate.get(BlackPlutonium), 'F', frameGt.get(BlackPlutonium)});
        addCraftingRecipe(Casing_Turbine.getOne(), RECIPE_MASK, new Object[]{"PhP", "PFP", "PwP", 'P', plate.get(Magnalium), 'F', frameGt.get(BlueSteel)});
        addCraftingRecipe(Casing_Turbine1.getOne(), RECIPE_MASK, new Object[]{"PhP", "PFP", "PwP", 'P', plate.get(StainlessSteel), 'F', Casing_Turbine});
        addCraftingRecipe(Casing_Turbine2.getOne(), RECIPE_MASK, new Object[]{"PhP", "PFP", "PwP", 'P', plate.get(Titanium), 'F', Casing_Turbine});
        addCraftingRecipe(Casing_Turbine3.getOne(), RECIPE_MASK, new Object[]{"PhP", "PFP", "PwP", 'P', plate.get(TungstenSteel), 'F', Casing_Turbine});
        addCraftingRecipe(Casing_Pipe_Bronze.getOne(), RECIPE_MASK, new Object[]{"PIP", "IFI", "PIP", 'P', plate.get(Bronze), 'F', frameGt.get(Bronze), 'I', pipeMedium.get(Bronze)});
        addCraftingRecipe(Casing_Pipe_Steel.getOne(), RECIPE_MASK, new Object[]{"PIP", "IFI", "PIP", 'P', plate.get(Steel), 'F', frameGt.get(Steel), 'I', pipeMedium.get(Steel)});
        addCraftingRecipe(Casing_Pipe_Titanium.getOne(), RECIPE_MASK, new Object[]{"PIP", "IFI", "PIP", 'P', plate.get(Titanium), 'F', frameGt.get(Titanium), 'I', pipeMedium.get(Titanium)});
        addCraftingRecipe(Casing_Pipe_TungstenSteel.getOne(), RECIPE_MASK, new Object[]{"PIP", "IFI", "PIP", 'P', plate.get(TungstenSteel), 'F', frameGt.get(TungstenSteel), 'I', pipeMedium.get(TungstenSteel)});
        addCraftingRecipe(Casing_Gearbox_Bronze.getOne(), RECIPE_MASK, new Object[]{"PhP", "GFG", "PwP", 'P', plate.get(Bronze), 'F', frameGt.get(Bronze), 'G', gearGt.get(Bronze)});
        addCraftingRecipe(Casing_Gearbox_Steel.getOne(), RECIPE_MASK, new Object[]{"PhP", "GFG", "PwP", 'P', plate.get(Steel), 'F', frameGt.get(Steel), 'G', gearGt.get(Steel)});
        addCraftingRecipe(Casing_Gearbox_Titanium.getOne(), RECIPE_MASK, new Object[]{"PhP", "GFG", "PwP", 'P', plate.get(Steel), 'F', frameGt.get(Titanium), 'G', gearGt.get(Titanium)});
        addCraftingRecipe(Casing_Gearbox_TungstenSteel.getOne(), RECIPE_MASK, new Object[]{"PhP", "GFG", "PwP", 'P', plate.get(Steel), 'F', frameGt.get(TungstenSteel), 'G', Robot_Arm_IV});
        addCraftingRecipe(Casing_Grate.getOne(), RECIPE_MASK, new Object[]{"PVP", "PFP", "PMP", 'P', new ItemStack(iron_bars, 1), 'F', frameGt.get(Steel), 'M', Electric_Motor_MV, 'V', rotor.get(Steel)});
        addCraftingRecipe(Casing_Assembler.getOne(), RECIPE_MASK, new Object[]{"PVP", "PFP", "PMP", 'P', circuitLogic.get(LOGIC_IV), 'F', frameGt.get(TungstenSteel), 'M', Electric_Motor_IV, 'V', circuitPower.get(PWR_LUV)});
        addCraftingRecipe(Casing_Firebox_Bronze.getOne(), RECIPE_MASK, new Object[]{"PSP", "SFS", "PSP", 'P', plate.get(Bronze), 'F', frameGt.get(Bronze), 'S', stick.get(Bronze)});
        addCraftingRecipe(Casing_Firebox_Steel.getOne(), RECIPE_MASK, new Object[]{"PSP", "SFS", "PSP", 'P', plate.get(Steel), 'F', frameGt.get(Steel), 'S', stick.get(Steel)});
        addCraftingRecipe(Casing_Firebox_Titanium.getOne(), RECIPE_MASK, new Object[]{"PSP", "SFS", "PSP", 'P', plate.get(Titanium), 'F', frameGt.get(Titanium), 'S', stick.get(Titanium)});
        addCraftingRecipe(Casing_Firebox_TungstenSteel.getOne(), RECIPE_MASK, new Object[]{"PSP", "SFS", "PSP", 'P', plate.get(TungstenSteel), 'F', frameGt.get(TungstenSteel), 'S', stick.get(TungstenSteel)});

        addCraftingRecipe(Casing_Advanced_Iridium.getOne(), RECIPE_MASK, new Object[]{"PhP", "PFP", "PwP", 'P', plate.get(Iridium), 'F', frameGt.get(Iridium)});

        addCraftingRecipe(Casing_Stripes_A.getOne(), RECIPE_MASK, new Object[]{"Y  ", " M ", "  B", 'M', Casing_SolidSteel, 'Y', dyeYellow, 'B', dyeBlack});
        addCraftingRecipe(Casing_Stripes_B.getOne(), RECIPE_MASK, new Object[]{"  Y", " M ", "B  ", 'M', Casing_SolidSteel, 'Y', dyeYellow, 'B', dyeBlack});
        addCraftingRecipe(Casing_RadioactiveHazard.getOne(), RECIPE_MASK, new Object[]{" YB", " M ", "   ", 'M', Casing_SolidSteel, 'Y', dyeYellow, 'B', dyeBlack});
        addCraftingRecipe(Casing_BioHazard.getOne(), RECIPE_MASK, new Object[]{" Y ", " MB", "   ", 'M', Casing_SolidSteel, 'Y', dyeYellow, 'B', dyeBlack});
        addCraftingRecipe(Casing_ExplosionHazard.getOne(), RECIPE_MASK, new Object[]{" Y ", " M ", "  B", 'M', Casing_SolidSteel, 'Y', dyeYellow, 'B', dyeBlack});
        addCraftingRecipe(Casing_FireHazard.getOne(), RECIPE_MASK, new Object[]{" Y ", " M ", " B ", 'M', Casing_SolidSteel, 'Y', dyeYellow, 'B', dyeBlack});
        addCraftingRecipe(Casing_AcidHazard.getOne(), RECIPE_MASK, new Object[]{" Y ", " M ", "B  ", 'M', Casing_SolidSteel, 'Y', dyeYellow, 'B', dyeBlack});
        addCraftingRecipe(Casing_MagicHazard.getOne(), RECIPE_MASK, new Object[]{" Y ", "BM ", "   ", 'M', Casing_SolidSteel, 'Y', dyeYellow, 'B', dyeBlack});
        addCraftingRecipe(Casing_FrostHazard.getOne(), RECIPE_MASK, new Object[]{"BY ", " M ", "   ", 'M', Casing_SolidSteel, 'Y', dyeYellow, 'B', dyeBlack});
        addCraftingRecipe(Casing_NoiseHazard.getOne(), RECIPE_MASK, new Object[]{"   ", " M ", "BY ", 'M', Casing_SolidSteel, 'Y', dyeYellow, 'B', dyeBlack});

        addShapelessCraftingRecipe(Casing_SolidSteel.getOne(), RECIPE_MASK, new Object[]{Casing_Stripes_A});
        addShapelessCraftingRecipe(Casing_SolidSteel.getOne(), RECIPE_MASK, new Object[]{Casing_Stripes_B});
        addShapelessCraftingRecipe(Casing_SolidSteel.getOne(), RECIPE_MASK, new Object[]{Casing_RadioactiveHazard});
        addShapelessCraftingRecipe(Casing_SolidSteel.getOne(), RECIPE_MASK, new Object[]{Casing_BioHazard});
        addShapelessCraftingRecipe(Casing_SolidSteel.getOne(), RECIPE_MASK, new Object[]{Casing_ExplosionHazard});
        addShapelessCraftingRecipe(Casing_SolidSteel.getOne(), RECIPE_MASK, new Object[]{Casing_FireHazard});
        addShapelessCraftingRecipe(Casing_SolidSteel.getOne(), RECIPE_MASK, new Object[]{Casing_AcidHazard});
        addShapelessCraftingRecipe(Casing_SolidSteel.getOne(), RECIPE_MASK, new Object[]{Casing_MagicHazard});
        addShapelessCraftingRecipe(Casing_SolidSteel.getOne(), RECIPE_MASK, new Object[]{Casing_FrostHazard});
        addShapelessCraftingRecipe(Casing_SolidSteel.getOne(), RECIPE_MASK, new Object[]{Casing_NoiseHazard});
        LOADED = true;
    }
}
