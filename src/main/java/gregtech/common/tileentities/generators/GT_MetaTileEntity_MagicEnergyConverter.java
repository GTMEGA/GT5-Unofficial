package gregtech.common.tileentities.generators;


import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicGenerator;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Recipe;

import static gregtech.api.enums.Textures.BlockIcons.*;


public class GT_MetaTileEntity_MagicEnergyConverter extends GT_MetaTileEntity_BasicGenerator {

    public int mEfficiency;

    public GT_MetaTileEntity_MagicEnergyConverter(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "Put your strange stuff in here");
        onConfigLoad();
    }

    public void onConfigLoad() {
        this.mEfficiency = GregTech_API.sMachineFile.get(
                ConfigCategories.machineconfig, "MagicEnergyConverter.efficiency.tier." + this.mTier, 100 - this.mTier * 5);
    }

    public GT_MetaTileEntity_MagicEnergyConverter(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        onConfigLoad();
    }

    public GT_MetaTileEntity_MagicEnergyConverter(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        onConfigLoad();
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_MagicEnergyConverter(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public ITexture[] getFront(byte aColor) {
        return new ITexture[]{
                super.getFront(aColor)[0], TextureFactory.of(MACHINE_CASING_MAGIC), TextureFactory.builder().addIcon(MACHINE_CASING_MAGIC_GLOW).glow().build(),
                OVERLAYS_ENERGY_OUT[mTier]
        };
    }

    @Override
    public ITexture[] getBack(byte aColor) {
        return new ITexture[]{
                super.getBack(aColor)[0], TextureFactory.of(MACHINE_CASING_MAGIC_FRONT), TextureFactory.builder().addIcon(MACHINE_CASING_MAGIC_FRONT_GLOW)
                                                                                                       .glow().build()
        };
    }

    @Override
    public ITexture[] getBottom(byte aColor) {
        return new ITexture[]{
                super.getBottom(aColor)[0], TextureFactory.of(MACHINE_CASING_MAGIC), TextureFactory.builder().addIcon(MACHINE_CASING_MAGIC_GLOW).glow().build()
        };
    }

    @Override
    public ITexture[] getTop(byte aColor) {
        return new ITexture[]{
                super.getTop(aColor)[0], TextureFactory.of(MACHINE_CASING_MAGIC), TextureFactory.builder().addIcon(MACHINE_CASING_MAGIC_GLOW).glow().build()
        };
    }

    @Override
    public ITexture[] getSides(byte aColor) {
        return new ITexture[]{
                super.getSides(aColor)[0], TextureFactory.of(MACHINE_CASING_MAGIC), TextureFactory.builder().addIcon(MACHINE_CASING_MAGIC_GLOW).glow().build()
        };
    }

    @Override
    public ITexture[] getFrontActive(byte aColor) {
        return new ITexture[]{
                super.getFrontActive(aColor)[0], TextureFactory.of(MACHINE_CASING_MAGIC_ACTIVE), TextureFactory.builder().addIcon(
                MACHINE_CASING_MAGIC_ACTIVE_GLOW).glow().build(), OVERLAYS_ENERGY_OUT[mTier]
        };
    }

    @Override
    public ITexture[] getBackActive(byte aColor) {
        return new ITexture[]{
                super.getBackActive(aColor)[0], TextureFactory.of(MACHINE_CASING_MAGIC_FRONT_ACTIVE), TextureFactory.builder().addIcon(
                MACHINE_CASING_MAGIC_FRONT_ACTIVE_GLOW).glow().build()
        };
    }

    @Override
    public ITexture[] getBottomActive(byte aColor) {
        return new ITexture[]{
                super.getBottomActive(aColor)[0], TextureFactory.of(MACHINE_CASING_MAGIC_ACTIVE), TextureFactory.builder().addIcon(
                MACHINE_CASING_MAGIC_ACTIVE_GLOW).glow().build()
        };
    }

    @Override
    public ITexture[] getTopActive(byte aColor) {
        return new ITexture[]{
                super.getTopActive(aColor)[0], TextureFactory.of(MACHINE_CASING_MAGIC_ACTIVE), TextureFactory.builder().addIcon(
                MACHINE_CASING_MAGIC_ACTIVE_GLOW).glow().build()
        };
    }

    @Override
    public ITexture[] getSidesActive(byte aColor) {
        return new ITexture[]{
                super.getSidesActive(aColor)[0], TextureFactory.of(MACHINE_CASING_MAGIC_ACTIVE), TextureFactory.builder().addIcon(
                MACHINE_CASING_MAGIC_ACTIVE_GLOW).glow().build()
        };
    }

    @Override
    public int getEfficiency() {
        return this.mEfficiency;
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public int getCapacity() {
        return 16000;
    }

    @Override
    public int getPollution() {
        return 0;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipes() {
        return GT_Recipe.GT_Recipe_Map.sMagicFuels;
    }

}
