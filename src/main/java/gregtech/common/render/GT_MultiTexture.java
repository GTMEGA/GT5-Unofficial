package gregtech.common.render;

import gregtech.api.interfaces.ITexture;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * <p>Lets Multiple ITextures Render overlay over each other.<</p>
 * <p>I should have done this much earlier...</p>
 */
class GT_MultiTexture implements ITexture {
    protected final ITexture[] mTextures;
    protected final boolean isTranslucent;

    GT_MultiTexture(ITexture... aTextures) {
        mTextures = aTextures;
        isTranslucent = Arrays.stream(aTextures).anyMatch(ITexture::isTranslucent);
    }

    @Override
    public boolean renderXPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, boolean isTranslucentPass) {
        if (!shouldRenderOnPass(isTranslucentPass))
            return false;
        var didWork = false;
        for (val tTexture : mTextures)
            if (tTexture != null && tTexture.isValidTexture())
                didWork |= tTexture.renderXPos(aRenderer, aBlock, aX, aY, aZ, isTranslucentPass);
        return didWork;
    }

    @Override
    public boolean renderXNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, boolean isTranslucentPass) {
        if (!shouldRenderOnPass(isTranslucentPass))
            return false;
        var didWork = false;
        for (val tTexture : mTextures)
            if (tTexture != null && tTexture.isValidTexture())
                didWork |= tTexture.renderXNeg(aRenderer, aBlock, aX, aY, aZ, isTranslucentPass);
        return didWork;
    }

    @Override
    public boolean renderYPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, boolean isTranslucentPass) {
        if (!shouldRenderOnPass(isTranslucentPass))
            return false;
        var didWork = false;
        for (val tTexture : mTextures)
            if (tTexture != null && tTexture.isValidTexture())
                didWork |= tTexture.renderYPos(aRenderer, aBlock, aX, aY, aZ, isTranslucentPass);
        return didWork;
    }

    @Override
    public boolean renderYNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, boolean isTranslucentPass) {
        if (!shouldRenderOnPass(isTranslucentPass))
            return false;
        var didWork = false;
        for (val tTexture : mTextures)
            if (tTexture != null && tTexture.isValidTexture())
                didWork |= tTexture.renderYNeg(aRenderer, aBlock, aX, aY, aZ, isTranslucentPass);
        return didWork;
    }

    @Override
    public boolean renderZPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, boolean isTranslucentPass) {
        if (!shouldRenderOnPass(isTranslucentPass))
            return false;
        var didWork = false;
        for (val tTexture : mTextures)
            if (tTexture != null && tTexture.isValidTexture())
                didWork |= tTexture.renderZPos(aRenderer, aBlock, aX, aY, aZ, isTranslucentPass);
        return didWork;
    }

    @Override
    public boolean renderZNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, boolean isTranslucentPass) {
        if (!shouldRenderOnPass(isTranslucentPass))
            return false;
        var didWork = false;
        for (val tTexture : mTextures)
            if (tTexture != null && tTexture.isValidTexture())
                didWork |= tTexture.renderZNeg(aRenderer, aBlock, aX, aY, aZ, isTranslucentPass);
        return didWork;
    }

    @Override
    public boolean isValidTexture() {
        return true;
    }

    @Override
    public boolean isTranslucent() {
        return isTranslucent;
    }
}
