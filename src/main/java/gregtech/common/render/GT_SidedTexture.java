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
    private final boolean isTranslucent;

    GT_SidedTexture(IIconContainer aIcon0, IIconContainer aIcon1, IIconContainer aIcon2, IIconContainer aIcon3, IIconContainer aIcon4, IIconContainer aIcon5, short[] aRGBa, boolean isTranslucent) {
        if (aRGBa.length != 4) throw new IllegalArgumentException("RGBa doesn't have 4 Values @ GT_RenderedTexture");
        mTextures = new ITexture[]{
                TextureFactory.builder().addIcon(aIcon0).setRGBA(aRGBa).setTranslucent(isTranslucent).build(),
                TextureFactory.builder().addIcon(aIcon1).setRGBA(aRGBa).setTranslucent(isTranslucent).build(),
                TextureFactory.builder().addIcon(aIcon2).setRGBA(aRGBa).setTranslucent(isTranslucent).build(),
                TextureFactory.builder().addIcon(aIcon3).setRGBA(aRGBa).setTranslucent(isTranslucent).build(),
                TextureFactory.builder().addIcon(aIcon4).setRGBA(aRGBa).setTranslucent(isTranslucent).build(),
                TextureFactory.builder().addIcon(aIcon5).setRGBA(aRGBa).setTranslucent(isTranslucent).build(),
        };
        mRGBa = aRGBa;
        this.isTranslucent = isTranslucent;
    }

    @Override
    public boolean renderXPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, boolean isTranslucentPass) {
        return shouldRenderOnPass(isTranslucentPass) &&
               mTextures[5].renderXPos(aRenderer, aBlock, aX, aY, aZ, isTranslucentPass);
    }

    @Override
    public boolean renderXNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, boolean isTranslucentPass) {
        return shouldRenderOnPass(isTranslucentPass) &&
               mTextures[4].renderXNeg(aRenderer, aBlock, aX, aY, aZ, isTranslucentPass);
    }

    @Override
    public boolean renderYPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, boolean isTranslucentPass) {
        return shouldRenderOnPass(isTranslucentPass) &&
               mTextures[1].renderYPos(aRenderer, aBlock, aX, aY, aZ, isTranslucentPass);
    }

    @Override
    public boolean renderYNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, boolean isTranslucentPass) {
        return shouldRenderOnPass(isTranslucentPass) &&
               mTextures[0].renderYNeg(aRenderer, aBlock, aX, aY, aZ, isTranslucentPass);
    }

    @Override
    public boolean renderZPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, boolean isTranslucentPass) {
        return shouldRenderOnPass(isTranslucentPass) &&
               mTextures[3].renderZPos(aRenderer, aBlock, aX, aY, aZ, isTranslucentPass);
    }

    @Override
    public boolean renderZNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, boolean isTranslucentPass) {
        return shouldRenderOnPass(isTranslucentPass) &&
               mTextures[2].renderZNeg(aRenderer, aBlock, aX, aY, aZ, isTranslucentPass);
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

    @Override
    public boolean isTranslucent() {
        return isTranslucent;
    }
}
