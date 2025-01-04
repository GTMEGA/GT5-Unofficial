package gregtech.common.render;

import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

class GT_SidedTexture implements ITexture, IColorModulationContainer {
    protected final ITexture[] mTextures;
    /**
     * DO NOT MANIPULATE THE VALUES INSIDE THIS ARRAY!!!
     * <p/>
     * Just set this variable to another different Array instead.
     * Otherwise some colored things will get Problems.
     */
    private final short[] mRGBa;

    GT_SidedTexture(IIconContainer aIcon0, IIconContainer aIcon1, IIconContainer aIcon2, IIconContainer aIcon3, IIconContainer aIcon4, IIconContainer aIcon5, short[] aRGBa, boolean aAllowAlpha) {
        if (aRGBa.length != 4) throw new IllegalArgumentException("RGBa doesn't have 4 Values @ GT_RenderedTexture");
        mTextures = new ITexture[]{
            TextureFactory.of(aIcon0, aRGBa, aAllowAlpha),
            TextureFactory.of(aIcon1, aRGBa, aAllowAlpha),
            TextureFactory.of(aIcon2, aRGBa, aAllowAlpha),
            TextureFactory.of(aIcon3, aRGBa, aAllowAlpha),
            TextureFactory.of(aIcon4, aRGBa, aAllowAlpha),
            TextureFactory.of(aIcon5, aRGBa, aAllowAlpha)
        };
        mRGBa = aRGBa;
    }

    @Override
    public boolean renderXPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        return mTextures[5].renderXPos(aRenderer, aBlock, aX, aY, aZ);
    }

    @Override
    public boolean renderXNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        return mTextures[4].renderXNeg(aRenderer, aBlock, aX, aY, aZ);
    }

    @Override
    public boolean renderYPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        return mTextures[1].renderYPos(aRenderer, aBlock, aX, aY, aZ);
    }

    @Override
    public boolean renderYNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        return mTextures[0].renderYNeg(aRenderer, aBlock, aX, aY, aZ);
    }

    @Override
    public boolean renderZPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        return mTextures[3].renderZPos(aRenderer, aBlock, aX, aY, aZ);
    }

    @Override
    public boolean renderZNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        return mTextures[2].renderZNeg(aRenderer, aBlock, aX, aY, aZ);
    }

    @Override
    public short[] getRGBA() {
        return mRGBa;
    }

    @Override
    public boolean isValidTexture() {
        for (ITexture renderedTexture : mTextures) {
            if (!renderedTexture.isValidTexture()) return false;
        }
        return true;
    }
}
