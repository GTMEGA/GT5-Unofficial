package gregtech.api.metatileentity.implementations;


import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;

import static gregtech.api.enums.Textures.BlockIcons.*;


/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the main construct for my Basic Machines such as the Automatic Extractor
 * Extend this class to make a simple Machine
 */
public abstract class GT_MetaTileEntity_BasicMachine_Steel extends GT_MetaTileEntity_BasicMachine_Bronze {

    public GT_MetaTileEntity_BasicMachine_Steel(
            int aID,
            String aName,
            String aNameRegional,
            String aDescription,
            int aInputSlotCount,
            int aOutputSlotCount,
            boolean aBricked
                                               ) {
        super(aID, aName, aNameRegional, aDescription, aInputSlotCount, aOutputSlotCount, aBricked);
    }

    public GT_MetaTileEntity_BasicMachine_Steel(
            String aName,
            String aDescription,
            ITexture[][][] aTextures,
            int aInputSlotCount,
            int aOutputSlotCount,
            boolean aBricked
                                               ) {
        super(aName, aDescription, aTextures, aInputSlotCount, aOutputSlotCount, aBricked);
    }

    public GT_MetaTileEntity_BasicMachine_Steel(
            String aName,
            String[] aDescription,
            ITexture[][][] aTextures,
            int aInputSlotCount,
            int aOutputSlotCount,
            boolean aBricked
                                               ) {
        super(aName, aDescription, aTextures, aInputSlotCount, aOutputSlotCount, aBricked);
    }

    @Override
    public float getSteamDamage() {
        return 12.0F;
    }

    @Override
    public ITexture[] getSideFacingActive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_STEELBRICKS_SIDE : MACHINE_STEEL_SIDE, Dyes.getModulation(aColor, Dyes._NULL.mRGBa))};
    }

    @Override
    public ITexture[] getSideFacingInactive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_STEELBRICKS_SIDE : MACHINE_STEEL_SIDE, Dyes.getModulation(aColor, Dyes._NULL.mRGBa))};
    }

    @Override
    public ITexture[] getFrontFacingActive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_STEELBRICKS_SIDE : MACHINE_STEEL_SIDE, Dyes.getModulation(aColor, Dyes._NULL.mRGBa))};
    }

    @Override
    public ITexture[] getFrontFacingInactive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_STEELBRICKS_SIDE : MACHINE_STEEL_SIDE, Dyes.getModulation(aColor, Dyes._NULL.mRGBa))};
    }

    @Override
    public ITexture[] getTopFacingActive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_STEELBRICKS_TOP : MACHINE_STEEL_TOP, Dyes.getModulation(aColor, Dyes._NULL.mRGBa))};
    }

    @Override
    public ITexture[] getTopFacingInactive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_STEELBRICKS_TOP : MACHINE_STEEL_TOP, Dyes.getModulation(aColor, Dyes._NULL.mRGBa))};
    }

    @Override
    public ITexture[] getBottomFacingActive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_STEELBRICKS_BOTTOM : MACHINE_STEEL_BOTTOM, Dyes.getModulation(aColor, Dyes._NULL.mRGBa))};
    }

    @Override
    public ITexture[] getBottomFacingInactive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_STEELBRICKS_BOTTOM : MACHINE_STEEL_BOTTOM, Dyes.getModulation(aColor, Dyes._NULL.mRGBa))};
    }

    @Override
    public ITexture[] getBottomFacingPipeActive(byte aColor) {
        return new ITexture[]{
                TextureFactory.of(mTier == 1 ? MACHINE_STEELBRICKS_BOTTOM : MACHINE_STEEL_BOTTOM, Dyes.getModulation(aColor, Dyes._NULL.mRGBa)),
                TextureFactory.of(OVERLAY_PIPE_OUT)
        };
    }

    @Override
    public ITexture[] getBottomFacingPipeInactive(byte aColor) {
        return new ITexture[]{
                TextureFactory.of(mTier == 1 ? MACHINE_STEELBRICKS_BOTTOM : MACHINE_STEEL_BOTTOM, Dyes.getModulation(aColor, Dyes._NULL.mRGBa)),
                TextureFactory.of(OVERLAY_PIPE_OUT)
        };
    }

    @Override
    public ITexture[] getTopFacingPipeActive(byte aColor) {
        return new ITexture[]{
                TextureFactory.of(mTier == 1 ? MACHINE_STEELBRICKS_TOP : MACHINE_STEEL_TOP, Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(
                OVERLAY_PIPE_OUT)
        };
    }

    @Override
    public ITexture[] getTopFacingPipeInactive(byte aColor) {
        return new ITexture[]{
                TextureFactory.of(mTier == 1 ? MACHINE_STEELBRICKS_TOP : MACHINE_STEEL_TOP, Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(
                OVERLAY_PIPE_OUT)
        };
    }

    @Override
    public ITexture[] getSideFacingPipeActive(byte aColor) {
        return new ITexture[]{
                TextureFactory.of(mTier == 1 ? MACHINE_STEELBRICKS_SIDE : MACHINE_STEEL_SIDE, Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(
                OVERLAY_PIPE_OUT)
        };
    }

    @Override
    public ITexture[] getSideFacingPipeInactive(byte aColor) {
        return new ITexture[]{
                TextureFactory.of(mTier == 1 ? MACHINE_STEELBRICKS_SIDE : MACHINE_STEEL_SIDE, Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(
                OVERLAY_PIPE_OUT)
        };
    }

    @Override
    public int checkRecipe() {
        return checkRecipe(true);
    }

}
