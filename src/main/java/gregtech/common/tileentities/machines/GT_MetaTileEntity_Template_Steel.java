package gregtech.common.tileentities.machines;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_Steel;
import gregtech.api.render.TextureFactory;

public abstract class GT_MetaTileEntity_Template_Steel extends GT_MetaTileEntity_BasicMachine_Steel {
    public GT_MetaTileEntity_Template_Steel(int aID, String aName, String aNameRegional, String aDescription, int aInputSlotCount, int aOutputSlotCount, boolean aBricked) {
        super(aID, aName, aNameRegional, aDescription, aInputSlotCount, aOutputSlotCount, aBricked);
    }

    public GT_MetaTileEntity_Template_Steel(String aName, String aDescription, ITexture[][][] aTextures, int aInputSlotCount, int aOutputSlotCount, boolean aBricked) {
        super(aName, aDescription, aTextures, aInputSlotCount, aOutputSlotCount, aBricked);
    }

    public GT_MetaTileEntity_Template_Steel(String aName, String[] aDescription, ITexture[][][] aTextures, int aInputSlotCount, int aOutputSlotCount, boolean aBricked) {
        super(aName, aDescription, aTextures, aInputSlotCount, aOutputSlotCount, aBricked);
    }

    @Override
    public float getEUTModifier() {
        return 2;
    }

    protected abstract String getOverlay();

    @Override
    public ITexture[] getSideFacingActive(byte aColor) {
        return new ITexture[]{
                super.getSideFacingActive(aColor)[0],
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_SIDE_ACTIVE"))};
    }

    @Override
    public ITexture[] getSideFacingInactive(byte aColor) {
        return new ITexture[]{
                super.getSideFacingInactive(aColor)[0],
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_SIDE"))};
    }

    @Override
    public ITexture[] getFrontFacingActive(byte aColor) {
        return new ITexture[]{
                super.getFrontFacingActive(aColor)[0],
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_FRONT_ACTIVE"))};
    }

    @Override
    public ITexture[] getFrontFacingInactive(byte aColor) {
        return new ITexture[]{super.getFrontFacingInactive(aColor)[0],
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_FRONT"))};
    }

    @Override
    public ITexture[] getTopFacingActive(byte aColor) {
        return new ITexture[]{super.getTopFacingActive(aColor)[0],
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_TOP_ACTIVE"))};
    }

    @Override
    public ITexture[] getTopFacingInactive(byte aColor) {
        return new ITexture[]{super.getTopFacingInactive(aColor)[0],
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_TOP"))};
    }

    @Override
    public ITexture[] getBottomFacingActive(byte aColor) {
        return new ITexture[]{super.getBottomFacingActive(aColor)[0],
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_BOTTOM_ACTIVE"))};
    }

    @Override
    public ITexture[] getBottomFacingInactive(byte aColor) {
        return new ITexture[]{super.getBottomFacingInactive(aColor)[0],
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_BOTTOM"))};
    }
}
