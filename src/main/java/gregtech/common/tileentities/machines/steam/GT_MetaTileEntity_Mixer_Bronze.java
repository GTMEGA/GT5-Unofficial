package gregtech.common.tileentities.machines.steam;

import gregtech.api.gui.GT_GUIContainer_BasicMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Recipe;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Template_Bronze;
import net.minecraft.entity.player.InventoryPlayer;

public class GT_MetaTileEntity_Mixer_Bronze extends GT_MetaTileEntity_Template_Bronze {
    public GT_MetaTileEntity_Mixer_Bronze(int aID, String aName, String aNameRegional, String aDescription) {
        super(aID, aName, aNameRegional, aDescription, 6, 1, false);
    }

    public GT_MetaTileEntity_Mixer_Bronze(String aName, String aDescription, ITexture[][][] aTextures) {
        super(aName, aDescription, aTextures, 6, 1, false);
    }

    public GT_MetaTileEntity_Mixer_Bronze(String aName, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aDescription, aTextures, 6, 1, false);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Mixer_Bronze(mName,mDescriptionArray,mTextures);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_BasicMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "Mixer.png", getRecipeList().mUnlocalizedName);
    }

    @Override
    public int getCapacity() {
        return 16_000;
    }

    @Override
    protected String getOverlay() {
        return "mixer";
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeList() {
        return GT_Recipe.GT_Recipe_Map.sMixerRecipes;
    }

    @Override
    public boolean allowSelectCircuit() {
        return true;
    }
}
