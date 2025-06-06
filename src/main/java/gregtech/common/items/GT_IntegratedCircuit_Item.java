package gregtech.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.GT_Values.RES_PATH_ITEM;

public class GT_IntegratedCircuit_Item extends GT_Generic_Item {
    private static final String aTextEmptyRow = "   ";
    protected IIcon[] mIconDamage = new IIcon[25];
    public GT_IntegratedCircuit_Item() {
        super("integrated_circuit", "Programmed Circuit", "");
        setHasSubtypes(true);
        setMaxDamage(0);

        ItemList.Circuit_Integrated.set(this);

        for (int i = 0; i <= 24; i++) {
            GregTech_API.registerConfigurationCircuit(new ItemStack(this, 0, i));
        }


//        GT_ModHandler.addShapelessCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 0L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[]{OrePrefixes.circuit.get(Materials.Basic)});
//        long bits = GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE;
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 1L, new Object[0]), bits, new Object[]{"d  ", " P ", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 2L, new Object[0]), bits, new Object[]{" d ", " P ", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 3L, new Object[0]), bits, new Object[]{"  d", " P ", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 4L, new Object[0]), bits, new Object[]{aTextEmptyRow, " Pd", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 5L, new Object[0]), bits, new Object[]{aTextEmptyRow, " P ", "  d", 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 6L, new Object[0]), bits, new Object[]{aTextEmptyRow, " P ", " d ", 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 7L, new Object[0]), bits, new Object[]{aTextEmptyRow, " P ", "d  ", 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 8L, new Object[0]), bits, new Object[]{aTextEmptyRow, "dP ", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 9L, new Object[0]), bits, new Object[]{"P d", aTextEmptyRow, aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 10L, new Object[0]), bits, new Object[]{"P  ", "  d", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 11L, new Object[0]), bits, new Object[]{"P  ", aTextEmptyRow, "  d", 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 12L, new Object[0]), bits, new Object[]{"P  ", aTextEmptyRow, " d ", 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 13L, new Object[0]), bits, new Object[]{"  P", aTextEmptyRow, "  d", 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 14L, new Object[0]), bits, new Object[]{"  P", aTextEmptyRow, " d ", 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 15L, new Object[0]), bits, new Object[]{"  P", aTextEmptyRow, "d  ", 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 16L, new Object[0]), bits, new Object[]{"  P", "d  ", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 17L, new Object[0]), bits, new Object[]{aTextEmptyRow, aTextEmptyRow, "d P", 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 18L, new Object[0]), bits, new Object[]{aTextEmptyRow, "d  ", "  P", 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 19L, new Object[0]), bits, new Object[]{"d  ", aTextEmptyRow, "  P", 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 20L, new Object[0]), bits, new Object[]{" d ", aTextEmptyRow, "  P", 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 21L, new Object[0]), bits, new Object[]{"d  ", aTextEmptyRow, "P  ", 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 22L, new Object[0]), bits, new Object[]{" d ", aTextEmptyRow, "P  ", 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 23L, new Object[0]), bits, new Object[]{"  d", aTextEmptyRow, "P  ", 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
//        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 24L, new Object[0]), bits, new Object[]{aTextEmptyRow, "  d", "P  ", 'P', ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
    }

    private static String getModeString(int aMetaData) {
        switch ((byte) (aMetaData >>> 8)) {
            case 0:
                return "==";
            case 1:
                return "<=";
            case 2:
                return ">=";
            case 3:
                return "<";
            case 4:
                return ">";
        }
        return "";
    }

    private static String getConfigurationString(int aMetaData) {
        return getModeString(aMetaData) + " " + (byte) (aMetaData & 0xFF);
    }

    @Override
    public void addAdditionalToolTips(List aList, ItemStack aStack, EntityPlayer aPlayer) {
        super.addAdditionalToolTips(aList, aStack, aPlayer);
        aList.add(getConfigurationTooltip(aStack));
        aList.add(GT_LanguageManager.addStringLocalization("integrated.circuit.tooltip","Right click or SHIFT + scroll to change configuration"));
    }

    public String getConfigurationTooltip(ItemStack aStack) {
        return GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".configuration", "Configuration: ") + getConfigurationString(getDamage(aStack));
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void getSubItems(Item var1, CreativeTabs aCreativeTab, List aList) {
        aList.add(new ItemStack(this, 1, 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aIconRegister) {
        super.registerIcons(aIconRegister);
        for (int i = 0; i < mIconDamage.length; i++) {
            mIconDamage[i] = aIconRegister.registerIcon(RES_PATH_ITEM + (GT_Config.troll ? "troll" : getUnlocalizedName() + "/" + i));
        }
        GT_Log.out.println("GT_Mod: Starting Item Icon Load Phase");
        GT_FML_LOGGER.info("GT_Mod: Starting Item Icon Load Phase");
        GregTech_API.sItemIcons = aIconRegister;
        try {
            for (Runnable tRunnable : GregTech_API.sGTItemIconload) {
                tRunnable.run();
            }
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        GT_Log.out.println("GT_Mod: Finished Item Icon Load Phase");
        GT_FML_LOGGER.info("GT_Mod: Finished Item Icon Load Phase");
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        byte circuitMode = ((byte) (damage & 0xFF)); // Mask out the MSB Comparison Mode Bits. See: getModeString
        return mIconDamage[circuitMode < mIconDamage.length ? circuitMode : 0];
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote)
            player.openGui(GT_Values.GT,1011,world,0,0,0);
        return super.onItemRightClick(stack, world, player);
    }
}
