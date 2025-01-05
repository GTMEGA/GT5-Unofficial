package gregtech.common.tileentities.generators;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicGenerator;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Recipe;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAYS_ENERGY_OUT;

public class GT_MetaTileEntity_PlasmaGenerator extends GT_MetaTileEntity_BasicGenerator {

    public int mEfficiency;

    public GT_MetaTileEntity_PlasmaGenerator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "Plasma into energy");
        onConfigLoad();
    }

    public GT_MetaTileEntity_PlasmaGenerator(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        onConfigLoad();
    }

    public GT_MetaTileEntity_PlasmaGenerator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        onConfigLoad();
    }

    @Override
    public ITexture[] getFront(byte aColor) {
        return new ITexture[]{
                super.getFront(aColor)[0],
                TextureFactory.of(MACHINE_CASING_FUSION_GLASS),
                OVERLAYS_ENERGY_OUT[mTier]};
    }

    @Override
    public ITexture[] getBack(byte aColor) {
        return new ITexture[]{super.getBack(aColor)[0], TextureFactory.of(MACHINE_CASING_FUSION_GLASS)};
    }

    @Override
    public ITexture[] getBottom(byte aColor) {
        return new ITexture[]{super.getBottom(aColor)[0], TextureFactory.of(MACHINE_CASING_FUSION_GLASS)};
    }

    @Override
    public ITexture[] getTop(byte aColor) {
        return new ITexture[]{super.getTop(aColor)[0], TextureFactory.of(MACHINE_CASING_FUSION_GLASS)};
    }

    @Override
    public ITexture[] getSides(byte aColor) {
        return new ITexture[]{super.getSides(aColor)[0], TextureFactory.of(MACHINE_CASING_FUSION_GLASS)};
    }

    @Override
    public ITexture[] getFrontActive(byte aColor) {
        return new ITexture[]{
                super.getFrontActive(aColor)[0],
                TextureFactory.of(MACHINE_CASING_FUSION_GLASS_YELLOW),
                OVERLAYS_ENERGY_OUT[mTier]};
    }

    @Override
    public ITexture[] getBackActive(byte aColor) {
        return new ITexture[]{
                super.getBackActive(aColor)[0],
                TextureFactory.of(MACHINE_CASING_FUSION_GLASS_YELLOW)};
    }

    @Override
    public ITexture[] getBottomActive(byte aColor) {
        return new ITexture[]{
                super.getBottomActive(aColor)[0],
                TextureFactory.of(MACHINE_CASING_FUSION_GLASS_YELLOW)};
    }

    @Override
    public ITexture[] getTopActive(byte aColor) {
        return new ITexture[]{
                super.getTopActive(aColor)[0],
                TextureFactory.of(MACHINE_CASING_FUSION_GLASS_YELLOW)};
    }

    @Override
    public ITexture[] getSidesActive(byte aColor) {
        return new ITexture[]{
                super.getSidesActive(aColor)[0],
                TextureFactory.of(MACHINE_CASING_FUSION_GLASS_YELLOW)};
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipes() {
        return GT_Recipe.GT_Recipe_Map.sPlasmaFuels;
    }

    @Override
    public int getEfficiency() {
        return this.mEfficiency;
    }

    @Override
    public int getCapacity() {
        return 16000;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PlasmaGenerator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    public void onConfigLoad() {
        this.mEfficiency = GregTech_API.sMachineFile.get(ConfigCategories.machineconfig, "PlasmaGenerator.efficiency.tier." + this.mTier, Math.max(10, 10 + Math.min(90, this.mTier * 10)));
    }

    @Override
	public int getPollution() {
		return 0;
	}
}
