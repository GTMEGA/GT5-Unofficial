package gregtech.common.tileentities.automation;


import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Buffer;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Item_Machines;
import gregtech.common.gui.GT_Container_RecipeFilter;
import gregtech.common.gui.GT_GUIContainer_RecipeFilter;
import gregtech.common.items.GT_IntegratedCircuit_Item;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;
import java.util.Collection;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_RECIPEFILTER;


public class GT_MetaTileEntity_RecipeFilter extends GT_MetaTileEntity_Buffer {

    public final static int numSlots = 11;

    public final static String TAG_RECIPE = "recipe";

    public static final String TAG_INVERT = "invert";

    public static final String TAG_FILTER = "filter";

    public static final String TAG_CIRC = "circuit";

    public boolean invertFilter = false;

    private String uniqueIdentifier = null;

    private GT_Recipe.GT_Recipe_Map target = null;

    private int circuitNumber = 0;

    public GT_MetaTileEntity_RecipeFilter(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, numSlots, new String[]{
                "Allows filtering of items by checking if they are usable in a given machine", "Use Screwdriver to regulate output stack size",
                "Does not consume energy to move Item"
        });
    }

    public GT_MetaTileEntity_RecipeFilter(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_RecipeFilter(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_RecipeFilter(this.mName, this.mTier, this.mInventory.length, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex < 9;
    }

    @Override
    public ITexture getOverlayIcon() {
        return TextureFactory.of(AUTOMATION_RECIPEFILTER);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        final NBTTagCompound filterComp = new NBTTagCompound();
        if (uniqueIdentifier != null) {
            filterComp.setString(TAG_RECIPE, uniqueIdentifier);
        }
        filterComp.setBoolean(TAG_INVERT, this.invertFilter);
        filterComp.setInteger(TAG_CIRC, this.circuitNumber);
        aNBT.setTag(TAG_FILTER, filterComp);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        final NBTTagCompound filterComp = aNBT.getCompoundTag(TAG_FILTER);
        this.invertFilter = filterComp.getBoolean(TAG_INVERT);
        if (filterComp.hasKey(TAG_RECIPE)) {
            this.uniqueIdentifier = filterComp.getString(TAG_RECIPE);
        } else {
            this.uniqueIdentifier = null;
        }
        this.circuitNumber = filterComp.getInteger(TAG_CIRC);
        loadMap();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        if (!super.allowPutStack(aBaseMetaTileEntity, aIndex, aSide, aStack) || this.target == null) {
            return false;
        }
        final ItemStack circuit = getCircuitItem();
        final boolean valid = validStack(aStack) && validCircuit(aStack, circuit);
        return valid != invertFilter;
    }

    private ItemStack getCircuitItem() {
        return circuitNumber > 0 && circuitNumber < 25 ? ItemList.Circuit_Integrated.getWithDamage(1, circuitNumber) : null;
    }

    private boolean validStack(final ItemStack aStack) {
        return this.target.containsInput(aStack);
    }

    private boolean validCircuit(final ItemStack itemStack, final ItemStack circuit) {
        if (circuit == null) {
            return true;
        }
        final Collection<GT_Recipe> recipes = target.mRecipeItemMap.get(new GT_ItemStack(itemStack));
        if (recipes == null || recipes.isEmpty()) {
            return false;
        }
        return recipes.stream().anyMatch(recipe -> Arrays.stream(recipe.mInputs).anyMatch(input -> {
            return GT_Utility.areStacksEqual(input, circuit);
        }));
    }

    private void loadMap() {
        if (this.uniqueIdentifier != null && GT_Recipe.GT_Recipe_Map.sIndexedMappings.containsKey(this.uniqueIdentifier)) {
            this.target = GT_Recipe.GT_Recipe_Map.sIndexedMappings.get(this.uniqueIdentifier);
        } else {
            this.uniqueIdentifier = null;
            this.target = null;
        }
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (!getBaseMetaTileEntity().isServerSide() || (aTick % 8L != 0L)) {
            return;
        }
        if (this.target == null) {
            this.clearFilter();
            this.mInventory[9] = null;
            this.mInventory[10] = null;
        } else if (this.mInventory[9] != null) {
            String name = GT_LanguageManager.getTranslation(this.target.mUnlocalizedName);
            this.mInventory[9].setStackDisplayName(name);
            if (this.mInventory[10] != null) {
                this.mInventory[10].setStackDisplayName(String.format("Circuit: %d", this.circuitNumber));
            }
        }
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_RecipeFilter(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_RecipeFilter(aPlayerInventory, aBaseMetaTileEntity);
    }

    private void clearFilter() {
        this.uniqueIdentifier = null;
        this.target = null;
        this.circuitNumber = 0;
        markDirty();
    }

    public void clear() {
        clearFilter();
        markDirty();
    }

    public boolean setFilterFromStack(ItemStack stack) {
        if (getBaseMetaTileEntity().isServerSide()) {
            GT_Recipe.GT_Recipe_Map tempMap = getMapFromStack(stack);
            if (tempMap != null) {
                this.target = tempMap;
                this.uniqueIdentifier = this.target.mUniqueIdentifier;
                markDirty();
                return true;
            }
            return false;
        }
        return false;
    }

    public GT_Recipe.GT_Recipe_Map getMapFromStack(ItemStack stack) {
        final Item item = stack.getItem();
        final int metadata = stack.getItemDamage();
        if (item instanceof GT_Item_Machines) {
            if (metadata <= 0 || metadata >= GregTech_API.METATILEENTITIES.length) {
                return null;
            }
            final IMetaTileEntity gtTile = GregTech_API.METATILEENTITIES[metadata];
            if (gtTile instanceof GT_MetaTileEntity_BasicMachine_GT_Recipe) {
                final GT_MetaTileEntity_BasicMachine_GT_Recipe recipeTile = (GT_MetaTileEntity_BasicMachine_GT_Recipe) gtTile;
                return recipeTile.getRecipeList();
            } else if (gtTile instanceof GT_MetaTileEntity_MultiBlockBase) {
                final GT_MetaTileEntity_MultiBlockBase multiBlockBase = (GT_MetaTileEntity_MultiBlockBase) gtTile;
                return multiBlockBase.getRecipeMap();
            }
        }
        return null;
    }

    public boolean setCircuitFromStack(ItemStack stack) {
        if (getBaseMetaTileEntity().isServerSide()) {
            final int result = getCircuitNumberFromStack(stack);
            if (stack == null || result != 0) {
                this.circuitNumber = result;
                markDirty();
                return true;
            }
            return false;
        }
        return false;
    }

    public int getCircuitNumberFromStack(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof GT_IntegratedCircuit_Item) {
            return stack.getItemDamage();
        }
        return 0;
    }


}
