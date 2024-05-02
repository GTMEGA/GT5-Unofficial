package gregtech.api.interfaces;

import gregtech.common.GT_Compat;
import lombok.val;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;

public interface ITexture {
    void renderXPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    void renderXNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    void renderYPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    void renderYNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    void renderZPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    void renderZNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    default void renderXPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, float offset) {
        renderXPos(aRenderer,aBlock,aX,aY,aZ);
    }

    default void renderXNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, float offset) {
        renderXNeg(aRenderer,aBlock,aX,aY,aZ);
    }

    default void renderYPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, float offset) {
        renderYPos(aRenderer,aBlock,aX,aY,aZ);
    }

    default void renderYNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, float offset) {
        renderYNeg(aRenderer,aBlock,aX,aY,aZ);
    }

    default void renderZPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, float offset) {
        renderZPos(aRenderer,aBlock,aX,aY,aZ);
    }

    default void renderZNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, float offset) {
        renderZNeg(aRenderer,aBlock,aX,aY,aZ);
    }

    boolean isValidTexture();

    /**
     * @return {@code true} if this texture is from the old package
     */
    default boolean isOldTexture() {
        return getClass().toString().startsWith("gregtech.api.objects");
    }

    /**
     * Will initialize the {@link Tessellator} if rendering off-world (Inventory)
     * @param aRenderer The {@link RenderBlocks} Renderer
     * @param aNormalX The X Normal for current Quad Face
     * @param aNormalY The Y Normal for current Quad Face
     * @param aNormalZ The Z Normal for current Quad Face
     */
    default void startDrawingQuads(RenderBlocks aRenderer, float aNormalX, float aNormalY, float aNormalZ) {
        if (aRenderer.useInventoryTint) {
            val tess = GT_Compat.tessellator();
            tess.startDrawingQuads();
            tess.setNormal(aNormalX, aNormalY, aNormalZ);
        }
    }

    /**
     * Will run the {@link Tessellator} to draw Quads if rendering off-world (Inventory)
     * @param aRenderer The {@link RenderBlocks} Renderer
     */
    default void draw(RenderBlocks aRenderer) {
        if (aRenderer.useInventoryTint) {
            GT_Compat.tessellator().draw();
        }
    }
}
