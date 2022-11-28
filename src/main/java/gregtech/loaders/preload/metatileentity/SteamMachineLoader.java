package gregtech.loaders.preload.metatileentity;

import gregtech.common.tileentities.boilers.*;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_BronzeBlastFurnace;
import gregtech.common.tileentities.machines.steam.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import static gregtech.api.enums.ItemList.*;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.OreDictNames.*;
import static gregtech.api.enums.OrePrefixes.*;
import static gregtech.api.util.GT_ModHandler.addCraftingRecipe;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.loaders.preload.metatileentity.MetaTileEntityLoader.RECIPE_MASK;

public final class SteamMachineLoader {
    private static boolean LOADED = false;

    static void load() {
        if (LOADED)
            throw new RuntimeException("Already loaded!");
        Machine_Bronze_Boiler.set(new GT_MetaTileEntity_Boiler_Bronze(100, "boiler.bronze", "Small Coal Boiler").getStackForm(1L));
        Machine_Steel_Boiler.set(new GT_MetaTileEntity_Boiler_Steel(101, "boiler.steel", "High Pressure Coal Boiler").getStackForm(1L));
        Machine_Steel_Boiler_Lava.set(new GT_MetaTileEntity_Boiler_Lava(102, "boiler.lava", "High Pressure Lava Boiler").getStackForm(1L));
        Machine_Bronze_Boiler_Solar.set(new GT_MetaTileEntity_Boiler_Solar(105, "boiler.solar", "Simple Solar Boiler").getStackForm(1L));
        Machine_HP_Solar.set(new GT_MetaTileEntity_Boiler_Solar_Steel(114, "boiler.steel.solar", "High Pressure Solar Boiler").getStackForm(1L));
        Machine_Steel_Boiler_SemiFluid.set(new GT_MetaTileEntity_Boiler_Semi(9333, "boiler.semi", "High Pressure SemiFluid Boiler").getStackForm(1));
        addCraftingRecipe(Machine_Bronze_Boiler.get(1L), RECIPE_MASK, new Object[]{"PPP", "PwP", "BFB", 'F', craftingIronFurnace, 'P', plate.get(Bronze), 'B', new ItemStack(Blocks.brick_block, 1)});
        addCraftingRecipe(Machine_Steel_Boiler.get(1L), RECIPE_MASK, new Object[]{"PPP", "PwP", "BFB", 'F', craftingIronFurnace, 'P', plate.get(Steel), 'B', new ItemStack(Blocks.brick_block, 1)});
        addCraftingRecipe(Machine_Steel_Boiler_Lava.get(1L), RECIPE_MASK, new Object[]{"PPP", "PTP", "PMP", 'M', Hull_HP, 'P', plate.get(Steel), 'T', getModItem("BuildCraft|Factory", "tankBlock", 1L, 0)});
        addCraftingRecipe(Machine_Bronze_Boiler_Solar.get(1L), RECIPE_MASK, new Object[]{"GGG", "SSS", "PMP", 'M', Hull_Bronze_Bricks, 'P', pipeSmall.get(Bronze), 'S', plateDouble.get(Silver), 'G', new ItemStack(Blocks.glass, 1)});
        addCraftingRecipe(Machine_HP_Solar.get(1L), RECIPE_MASK, new Object[]{"GGG", "SSS", "PMP", 'M', Hull_HP_Bricks, 'P', pipeSmall.get(Steel), 'S', plateTriple.get(Silver), 'G', getModItem("IC2", "blockAlloyGlass", 1L)});

        Machine_Bronze_BlastFurnace.set(new GT_MetaTileEntity_BronzeBlastFurnace(108, "bronzemachine.blastfurnace", "Bronze Plated Blast Furnace").getStackForm(1L));
        Machine_Bronze_Furnace.set(new GT_MetaTileEntity_Furnace_Bronze(103, "bronzemachine.furnace", "Steam Furnace").getStackForm(1L));
        Machine_HP_Furnace.set(new GT_MetaTileEntity_Furnace_Steel(104, "hpmachine.furnace", "High Pressure Furnace").getStackForm(1L));
        Machine_Bronze_Macerator.set(new GT_MetaTileEntity_Macerator_Bronze(106, "bronzemachine.macerator", "Steam Macerator").getStackForm(1L));
        Machine_HP_Macerator.set(new GT_MetaTileEntity_Macerator_Steel(107, "hpmachine.macerator", "High Pressure Macerator").getStackForm(1L));
        Machine_Bronze_Extractor.set(new GT_MetaTileEntity_Extractor_Bronze(109, "bronzemachine.extractor", "Steam Extractor").getStackForm(1L));
        Machine_HP_Extractor.set(new GT_MetaTileEntity_Extractor_Steel(110, "hpmachine.extractor", "High Pressure Extractor").getStackForm(1L));
        Machine_Bronze_Hammer.set(new GT_MetaTileEntity_ForgeHammer_Bronze(112, "bronzemachine.hammer", "Steam Forge Hammer").getStackForm(1L));
        Machine_HP_Hammer.set(new GT_MetaTileEntity_ForgeHammer_Steel(113, "hpmachine.hammer", "High Pressure Forge Hammer").getStackForm(1L));
        Machine_Bronze_Compressor.set(new GT_MetaTileEntity_Compressor_Bronze(115, "bronzemachine.compressor", "Steam Compressor").getStackForm(1L));
        Machine_HP_Compressor.set(new GT_MetaTileEntity_Compressor_Steel(116, "hpmachine.compressor", "High Pressure Compressor").getStackForm(1L));
        Machine_Bronze_AlloySmelter.set(new GT_MetaTileEntity_AlloySmelter_Bronze(118, "bronzemachine.alloysmelter", "Steam Alloy Smelter").getStackForm(1L));
        Machine_HP_AlloySmelter.set(new GT_MetaTileEntity_AlloySmelter_Steel(119, "hpmachine.alloysmelter", "High Pressure Alloy Smelter").getStackForm(1L));
        Machine_HP_Sifter.set(new GT_MetaTileEntity_Sifter_Steel(9330, "hpmachine.sifter", "High Pressure Sifter", "Stay calm and keep sifting.").getStackForm(1L));
        Machine_HP_Sifter.set(new GT_MetaTileEntity_Mixer_Steel(9331, "hpmachine.mixer", "High Pressure Mixer", "Will it Blend?").getStackForm(1L));
        Machine_HP_Sifter.set(new GT_MetaTileEntity_Mixer_Bronze(9332, "bronzemachine.mixer", "Steam Mixer", "Will it Blend?").getStackForm(1L));
        addCraftingRecipe(Machine_Bronze_Furnace.get(1L), RECIPE_MASK, new Object[]{"XXX", "XMX", "XFX", 'M', Hull_Bronze_Bricks, 'X', pipeSmall.get(Bronze), 'F', craftingFurnace});
        addCraftingRecipe(Machine_HP_Furnace.get(1L), RECIPE_MASK, new Object[]{"XSX", "PMP", "XXX", 'M', Machine_Bronze_Furnace, 'X', pipeSmall.get(WroughtIron), 'P', plate.get(WroughtIron), 'S', plate.get(Steel)});
        addCraftingRecipe(Machine_Bronze_Macerator.get(1L), RECIPE_MASK, new Object[]{"DXD", "XMX", "PXP", 'M', Hull_Bronze, 'X', pipeSmall.get(Bronze), 'P', craftingPiston, 'D', gem.get(Diamond)});
        addCraftingRecipe(Machine_HP_Macerator.get(1L), RECIPE_MASK, new Object[]{"PSP", "XMX", "PPP", 'M', Machine_Bronze_Macerator, 'X', pipeSmall.get(WroughtIron), 'P', plate.get(WroughtIron), 'S', plate.get(Steel)});
        addCraftingRecipe(Machine_Bronze_Extractor.get(1L), RECIPE_MASK, new Object[]{"XXX", "PMG", "XXX", 'M', Hull_Bronze, 'X', pipeSmall.get(Bronze), 'P', craftingPiston, 'G', new ItemStack(Blocks.glass, 1)});
        addCraftingRecipe(Machine_HP_Extractor.get(1L), RECIPE_MASK, new Object[]{"XSX", "PMP", "XXX", 'M', Machine_Bronze_Extractor, 'X', pipeSmall.get(WroughtIron), 'P', plate.get(WroughtIron), 'S', plate.get(Steel)});
        addCraftingRecipe(Machine_Bronze_Hammer.get(1L), RECIPE_MASK, new Object[]{"XPX", "XMX", "XAX", 'M', Hull_Bronze, 'X', pipeSmall.get(Bronze), 'P', craftingPiston, 'A', craftingAnvil});
        addCraftingRecipe(Machine_HP_Hammer.get(1L), RECIPE_MASK, new Object[]{"PSP", "XMX", "PPP", 'M', Machine_Bronze_Hammer, 'X', pipeSmall.get(WroughtIron), 'P', plate.get(WroughtIron), 'S', plate.get(Steel)});
        addCraftingRecipe(Machine_Bronze_Compressor.get(1L), RECIPE_MASK, new Object[]{"XXX", "PMP", "XXX", 'M', Hull_Bronze, 'X', pipeSmall.get(Bronze), 'P', craftingPiston});
        addCraftingRecipe(Machine_HP_Compressor.get(1L), RECIPE_MASK, new Object[]{"XSX", "PMP", "XXX", 'M', Machine_Bronze_Compressor, 'X', pipeSmall.get(WroughtIron), 'P', plate.get(WroughtIron), 'S', plate.get(Steel)});
        addCraftingRecipe(Machine_Bronze_AlloySmelter.get(1L), RECIPE_MASK, new Object[]{"XXX", "FMF", "XXX", 'M', Hull_Bronze_Bricks, 'X', pipeSmall.get(Bronze), 'F', craftingFurnace});
        addCraftingRecipe(Machine_HP_AlloySmelter.get(1L), RECIPE_MASK, new Object[]{"PSP", "PMP", "PXP", 'M', Machine_Bronze_AlloySmelter, 'X', pipeSmall.get(WroughtIron), 'P', plate.get(WroughtIron), 'S', plate.get(Steel)});
        LOADED = true;
    }
}
