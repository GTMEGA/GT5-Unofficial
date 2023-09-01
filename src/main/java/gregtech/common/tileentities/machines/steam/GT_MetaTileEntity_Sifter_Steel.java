package gregtech.common.tileentities.machines.steam;


import gregtech.api.gui.GT_GUIContainer_BasicMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Recipe;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Template_Steel;
import net.minecraft.entity.player.InventoryPlayer;


public class GT_MetaTileEntity_Sifter_Steel extends GT_MetaTileEntity_Template_Steel {

    public GT_MetaTileEntity_Sifter_Steel(int aID, String aName, String aNameRegional, String aDescription) {
        super(aID, aName, aNameRegional, aDescription, 1, 9, false);
    }

    public GT_MetaTileEntity_Sifter_Steel(String aName, String aDescription, ITexture[][][] aTextures) {
        super(aName, aDescription, aTextures, 1, 9, false);
    }

    public GT_MetaTileEntity_Sifter_Steel(String aName, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aDescription, aTextures, 1, 9, false);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Sifter_Steel(mName, mDescriptionArray, mTextures);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_BasicMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "Sifter.png", getRecipeList().mUnlocalizedName);
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeList() {
        return GT_Recipe.GT_Recipe_Map.sSifterRecipes;
    }

    @Override
    protected String getOverlay() {
        return "sifter";
    }

}
