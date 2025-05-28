package gregtech.loaders.preload.metatileentity;

import gregtech.GT_Mod;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicHull;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_BasicHull_Bronze;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_BasicHull_BronzeBricks;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_BasicHull_Steel;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_BasicHull_SteelBricks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static gregtech.api.enums.ItemList.*;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.OrePrefixes.*;
import static gregtech.api.util.GT_ModHandler.RecipeBits.*;
import static gregtech.api.util.GT_ModHandler.addCraftingRecipe;
import static gregtech.api.util.GT_ModHandler.removeRecipeByOutput;
import static gregtech.loaders.preload.GT_Loader_MetaTileEntities.RECIPE_MASK;

public final class HullLoader {
    private static final String IMAGINATION = EnumChatFormatting.RESET + "Can be used with covers and also used to make machines.";

    private static boolean LOADED = false;

    public static void load() {
        if (LOADED)
            throw new RuntimeException("Already loaded!");
        Hull_Bronze.set(new GT_MetaTileEntity_BasicHull_Bronze(1, "hull.bronze", "Brass Hull", 0, "For your first Steam Machines").getStackForm(1L));
        Hull_Bronze_Bricks.set(new GT_MetaTileEntity_BasicHull_BronzeBricks(2, "hull.bronze_bricked", "Bricked Brass Hull", 0, "For your first Steam Machines").getStackForm(1L));
        Hull_HP.set(new GT_MetaTileEntity_BasicHull_Steel(3, "hull.steel", "High Pressure Steam Hull", 0, "For improved Steam Machines").getStackForm(1L));
        Hull_HP_Bricks.set(new GT_MetaTileEntity_BasicHull_SteelBricks(4, "hull.steel_bricked", "Bricked High Pressure Hull", 0, "For improved Steam Machines").getStackForm(1L));

        Hull_ULV.set(new GT_MetaTileEntity_BasicHull(10, "hull.tier.00", "ULV Machine Hull", 0, IMAGINATION).getStackForm(1L));
        Hull_LV.set(new GT_MetaTileEntity_BasicHull(11, "hull.tier.01", "LV Machine Hull", 1, IMAGINATION).getStackForm(1L));
        Hull_MV.set(new GT_MetaTileEntity_BasicHull(12, "hull.tier.02", "MV Machine Hull", 2, IMAGINATION).getStackForm(1L));
        Hull_HV.set(new GT_MetaTileEntity_BasicHull(13, "hull.tier.03", "HV Machine Hull", 3, IMAGINATION).getStackForm(1L));
        Hull_EV.set(new GT_MetaTileEntity_BasicHull(14, "hull.tier.04", "EV Machine Hull", 4, IMAGINATION).getStackForm(1L));
        Hull_IV.set(new GT_MetaTileEntity_BasicHull(15, "hull.tier.05", "IV Machine Hull", 5, IMAGINATION).getStackForm(1L));
        Hull_LuV.set(new GT_MetaTileEntity_BasicHull(16, "hull.tier.06", "LuV Machine Hull", 6, IMAGINATION).getStackForm(1L));
        Hull_ZPM.set(new GT_MetaTileEntity_BasicHull(17, "hull.tier.07", "ZPM Machine Hull", 7, IMAGINATION).getStackForm(1L));
        Hull_UV.set(new GT_MetaTileEntity_BasicHull(18, "hull.tier.08", "UV Machine Hull", 8, IMAGINATION).getStackForm(1L));
        Hull_MAX.set(new GT_MetaTileEntity_BasicHull(19, "hull.tier.09", "UHV Machine Hull", 9, IMAGINATION).getStackForm(1L));

//        addCraftingRecipe(Hull_ULV.get(1L), REVERSIBLE, new Object[]{"CMC", 'M', Casing_ULV, 'C', cableGt01.get(Tin), 'H', plate.get(Brass), 'P', plate.get(Wood)});
        addCraftingRecipe(Hull_LV.get(1L), REVERSIBLE, new Object[]{"CMC", 'M', Casing_LV, 'C', cableGt01.get(Tin), 'H', plate.get(Steel), 'P', plate.get(Brass)});
        addCraftingRecipe(Hull_MV.get(1L), REVERSIBLE, new Object[]{"CMC", 'M', Casing_MV, 'C', cableGt01.get(Copper), 'H', plate.get(StainlessSteel), 'P', plate.get(Cobalt)});
        addCraftingRecipe(Hull_HV.get(1L), REVERSIBLE, new Object[]{"CMC", 'M', Casing_HV, 'C', cableGt01.get(Gold), 'H', plate.get(Aluminium), 'P', plate.get(Plastic)});
       //addCraftingRecipe(Hull_EV.get(1L), REVERSIBLE, new Object[]{"CMC", 'M', Casing_EV, 'C', cableGt01.get(Aluminium), 'H', plate.get(Titanium), 'P', plate.get(Plastic)});
       //addCraftingRecipe(Hull_IV.get(1L), REVERSIBLE, new Object[]{"CMC", 'M', Casing_IV, 'C', cableGt01.get(Tungsten), 'H', plate.get(TungstenSteel), 'P', plate.get(Polytetrafluoroethylene)});
       //addCraftingRecipe(Hull_LuV.get(1L), REVERSIBLE, new Object[]{"CMC", 'M', Casing_LuV, 'C', cableGt01.get(VanadiumGallium), 'H', plate.get(Chrome), 'P', plate.get(Polytetrafluoroethylene)});
       //addCraftingRecipe(Hull_ZPM.get(1L), REVERSIBLE, new Object[]{"CMC", 'M', Casing_ZPM, 'C', cableGt02.get(Naquadah), 'H', plate.get(Iridium), 'P', plate.get(Polybenzimidazole)});
       //addCraftingRecipe(Hull_UV.get(1L), REVERSIBLE, new Object[]{"CMC", 'M', Casing_UV, 'C', cableGt04.get(NaquadahAlloy), 'H', plate.get(Osmium), 'P', plate.get(Polybenzimidazole)});
       //addCraftingRecipe(Hull_MAX.get(1L), REVERSIBLE, new Object[]{"CMC", 'M', Casing_MAX, 'C', wireGt04.get(SuperconductorUV), 'H', plate.get(Neutronium), 'P', plate.get(Polybenzimidazole)});

        removeRecipeByOutput(Hull_ULV.get(1L));
        removeRecipeByOutput(Hull_LV.get(1L));
        removeRecipeByOutput(Hull_MV.get(1L));
        removeRecipeByOutput(Hull_HV.get(1L));
        removeRecipeByOutput(Hull_EV.get(1L));
        removeRecipeByOutput(Hull_IV.get(1L));
        removeRecipeByOutput(Hull_LuV.get(1L));
        removeRecipeByOutput(Hull_ZPM.get(1L));
        removeRecipeByOutput(Hull_UV.get(1L));
        removeRecipeByOutput(Hull_MAX.get(1L));

        if (GT_Mod.gregtechproxy.mHardMachineCasings) {
            addCraftingRecipe(Hull_ULV.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"PHP", "CMC", 'M', Casing_ULV, 'C', cableGt01.get(Tin), 'H', plate.get(Brass), 'P', plate.get(Wood)});
            addCraftingRecipe(Hull_LV.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"PHP", "CMC", 'M', Casing_LV, 'C', cableGt01.get(Tin), 'H', plate.get(Steel), 'P', plate.get(WroughtIron)});
            addCraftingRecipe(Hull_MV.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"PHP", "CMC", 'M', Casing_MV, 'C', cableGt01.get(Copper), 'H', plate.get(StainlessSteel), 'P', plate.get(Cobalt)});
            addCraftingRecipe(Hull_HV.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"PHP", "CMC", 'M', Casing_HV, 'C', cableGt01.get(Gold), 'H', plate.get(Aluminium), 'P', plate.get(Plastic)});
            addCraftingRecipe(Hull_EV.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"PHP", "CMC", 'M', Casing_EV, 'C', cableGt01.get(Aluminium), 'H', plate.get(Titanium), 'P', plate.get(Plastic)});
            addCraftingRecipe(Hull_IV.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"PHP", "CMC", 'M', Casing_IV, 'C', cableGt01.get(Tungsten), 'H', plate.get(TungstenSteel), 'P', plate.get(Polytetrafluoroethylene)});
            //addCraftingRecipe(Hull_LuV.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"PHP", "CMC", 'M', Casing_LuV, 'C', cableGt01.get(VanadiumGallium), 'H', plate.get(Chrome), 'P', plate.get(Polytetrafluoroethylene)});
            //addCraftingRecipe(Hull_ZPM.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"PHP", "CMC", 'M', Casing_ZPM, 'C', cableGt01.get(Naquadah), 'H', plate.get(Iridium), 'P', plate.get(Polybenzimidazole)});
            //addCraftingRecipe(Hull_UV.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"PHP", "CMC", 'M', Casing_UV, 'C', wireGt04.get(NaquadahAlloy), 'H', plate.get(Osmium), 'P', plate.get(Polybenzimidazole)});
            //addCraftingRecipe(Hull_MAX.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"PHP", "CMC", 'M', Casing_MAX, 'C', wireGt04.get(SuperconductorUV), 'H', plate.get(Neutronium), 'P', plate.get(Polybenzimidazole)});
        } else {
            addCraftingRecipe(Hull_ULV.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"CMC", 'M', Casing_ULV, 'C', cableGt01.get(Tin)});
            addCraftingRecipe(Hull_LV.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"CMC", 'M', Casing_LV, 'C', cableGt01.get(Tin)});
            addCraftingRecipe(Hull_MV.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"CMC", 'M', Casing_MV, 'C', cableGt01.get(Copper)});
            addCraftingRecipe(Hull_HV.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"CMC", 'M', Casing_HV, 'C', cableGt01.get(Gold)});
            addCraftingRecipe(Hull_EV.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"CMC", 'M', Casing_EV, 'C', cableGt01.get(Aluminium)});
            addCraftingRecipe(Hull_IV.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"CMC", 'M', Casing_IV, 'C', cableGt01.get(Tungsten)});
           //addCraftingRecipe(Hull_LuV.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"CMC", 'M', Casing_LuV, 'C', cableGt01.get(VanadiumGallium)});
           //addCraftingRecipe(Hull_ZPM.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"CMC", 'M', Casing_ZPM, 'C', cableGt01.get(Naquadah)});
           //addCraftingRecipe(Hull_UV.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"CMC", 'M', Casing_UV, 'C', wireGt04.get(NaquadahAlloy)});
           //addCraftingRecipe(Hull_MAX.get(1L), NOT_REMOVABLE | BUFFERED, new Object[]{"CMC", 'M', Casing_MAX, 'C', wireGt04.get(SuperconductorUV)});
        }
        LOADED = true;
    }
}
