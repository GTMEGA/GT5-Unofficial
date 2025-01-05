package gregtech.api.interfaces;

import gregtech.common.GT_Compat;
import lombok.val;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;

public interface ITexture {
    boolean renderXPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    boolean renderXNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    boolean renderYPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    boolean renderYNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    boolean renderZPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    boolean renderZNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ);

    boolean isValidTexture();

    boolean isTranslucent();

    default boolean shouldRenderOnPass(int pass) {
        return switch (pass) {
            case 0 -> !isTranslucent();
            case 1 -> isTranslucent();
            default -> throw new IllegalStateException("Unknown render pass: " + pass);
        };
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
