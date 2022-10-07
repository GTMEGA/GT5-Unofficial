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
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_SIDE_ACTIVE")),
                TextureFactory.builder().addIcon((new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_SIDE_ACTIVE_GLOW"))).glow().build()};
    }

    @Override
    public ITexture[] getSideFacingInactive(byte aColor) {
        return new ITexture[]{
                super.getSideFacingInactive(aColor)[0],
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_SIDE")),
                TextureFactory.builder().addIcon((new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_SIDE_GLOW"))).glow().build()};
    }

    @Override
    public ITexture[] getFrontFacingActive(byte aColor) {
        return new ITexture[]{
                super.getFrontFacingActive(aColor)[0],
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_FRONT_ACTIVE")),
                TextureFactory.builder().addIcon((new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_FRONT_ACTIVE_GLOW"))).glow().build()};
    }

    @Override
    public ITexture[] getFrontFacingInactive(byte aColor) {
        return new ITexture[]{super.getFrontFacingInactive(aColor)[0],
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_FRONT")),
                TextureFactory.builder().addIcon((new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_FRONT_GLOW"))).glow().build()};
    }

    @Override
    public ITexture[] getTopFacingActive(byte aColor) {
        return new ITexture[]{super.getTopFacingActive(aColor)[0],
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_TOP_ACTIVE")),
                TextureFactory.builder().addIcon((new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_TOP_ACTIVE_GLOW"))).glow().build()};
    }

    @Override
    public ITexture[] getTopFacingInactive(byte aColor) {
        return new ITexture[]{super.getTopFacingInactive(aColor)[0],
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_TOP")),
                TextureFactory.builder().addIcon((new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_TOP_GLOW"))).glow().build()};
    }

    @Override
    public ITexture[] getBottomFacingActive(byte aColor) {
        return new ITexture[]{super.getBottomFacingActive(aColor)[0],
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_BOTTOM_ACTIVE")),
                TextureFactory.builder().addIcon((new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_BOTTOM_ACTIVE_GLOW"))).glow().build()};
    }

    @Override
    public ITexture[] getBottomFacingInactive(byte aColor) {
        return new ITexture[]{super.getBottomFacingInactive(aColor)[0],
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_BOTTOM")),
                TextureFactory.builder().addIcon((new Textures.BlockIcons.CustomIcon("basicmachines/" + getOverlay() + "/OVERLAY_BOTTOM_GLOW"))).glow().build()};
    }
}
