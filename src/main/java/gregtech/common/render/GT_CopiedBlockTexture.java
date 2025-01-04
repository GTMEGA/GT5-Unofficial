package gregtech.common.render;

import gregtech.api.interfaces.IBlockContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.util.LightingHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

class GT_CopiedBlockTexture implements ITexture, IBlockContainer {
    private final Block mBlock;
    private final byte mSide, mMeta;

    GT_CopiedBlockTexture(Block aBlock, int aSide, int aMeta, short[] aRGBa, boolean allowAlpha) {
        if (aRGBa.length != 4) throw new IllegalArgumentException("RGBa doesn't have 4 Values @ GT_CopiedBlockTexture");
        mBlock = aBlock;
        mSide = (byte) aSide;
        mMeta = (byte) aMeta;
    }

    private IIcon getIcon(int aSide) {
        if (mSide == 6) return mBlock.getIcon(aSide, mMeta);
        return mBlock.getIcon(mSide, mMeta);
    }

    @Override
    public boolean renderXPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        IIcon aIcon = getIcon(ForgeDirection.EAST.ordinal());
        aRenderer.field_152631_f = true;
        startDrawingQuads(aRenderer, 1.0f, 0.0f, 0.0f);
        new LightingHelper(aRenderer)
                .setupLightingXPos(aBlock, aX, aY, aZ)
                .setupColor(ForgeDirection.EAST.ordinal(), 0xffffff);
        aRenderer.renderFaceXPos(aBlock, aX, aY, aZ, aIcon);
        draw(aRenderer);
        aRenderer.field_152631_f = false;
        return true;
    }

    @Override
    public boolean renderXNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, -1.0f, 0.0f, 0.0f);
        IIcon aIcon = getIcon(ForgeDirection.WEST.ordinal());
        new LightingHelper(aRenderer)
                .setupLightingXNeg(aBlock, aX, aY, aZ)
                .setupColor(ForgeDirection.WEST.ordinal(), 0xffffff);
        aRenderer.renderFaceXNeg(aBlock, aX, aY, aZ, aIcon);
        draw(aRenderer);
        return true;
    }

    @Override
    public boolean renderYPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, 1.0f, 0.0f);
        IIcon aIcon = getIcon(ForgeDirection.UP.ordinal());
            new LightingHelper(aRenderer)
                    .setupLightingYPos(aBlock, aX, aY, aZ)
                    .setupColor(ForgeDirection.UP.ordinal(), 0xffffff);
        aRenderer.renderFaceYPos(aBlock, aX, aY, aZ, aIcon);
        draw(aRenderer);
        return true;
    }

    @Override
    public boolean renderYNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, -1.0f, 0.0f);
        IIcon aIcon = getIcon(ForgeDirection.DOWN.ordinal());
        new LightingHelper(aRenderer)
                .setupLightingYNeg(aBlock, aX, aY, aZ)
                .setupColor(ForgeDirection.DOWN.ordinal(), 0xffffff);
        aRenderer.renderFaceYNeg(aBlock, aX, aY, aZ, aIcon);
        draw(aRenderer);
        return true;
    }

    @Override
    public boolean renderZPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, 0.0f, 1.0f);
        IIcon aIcon = getIcon(ForgeDirection.SOUTH.ordinal());
        new LightingHelper(aRenderer)
                .setupLightingZPos(aBlock, aX, aY, aZ)
                .setupColor(ForgeDirection.SOUTH.ordinal(), 0xffffff);
        aRenderer.renderFaceZPos(aBlock, aX, aY, aZ, aIcon);
        draw(aRenderer);
        return true;
    }

    @Override
    public boolean renderZNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, 0.0f, -1.0f);
        IIcon aIcon = getIcon(ForgeDirection.NORTH.ordinal());
        aRenderer.field_152631_f = true;
        new LightingHelper(aRenderer)
                .setupLightingZNeg(aBlock, aX, aY, aZ)
                .setupColor(ForgeDirection.NORTH.ordinal(), 0xffffff);
        aRenderer.renderFaceZNeg(aBlock, aX, aY, aZ, aIcon);
        draw(aRenderer);
        aRenderer.field_152631_f = false;
        return true;
    }

    @Override
    public boolean isValidTexture() {
        return mBlock != null;
    }

    @Override
    public Block getBlock() {
        return mBlock;
    }

    @Override
    public byte getMeta() {
        return mMeta;
    }
}
