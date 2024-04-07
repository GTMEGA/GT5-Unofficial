package gregtech.api.gui.widgets.icon;


import gregtech.common.GT_Compat;
import lombok.NonNull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;


public interface IGT_GuiIcon {

    default void render(double x, double y, double width, double height, double zLevel, boolean doDraw) {
        if (this == GT_GuiIcon.INVALID) {
            return;
        }
        Tessellator tess = GT_Compat.tessellator();
        if (doDraw) {
            Minecraft.getMinecraft().renderEngine.bindTexture(getResourceLocation());
            tess.startDrawingQuads();
        }
        double minU = getMinU();
        double maxU = getMaxU();
        double minV = getMinV();
        double maxV = getMaxV();
        tess.addVertexWithUV(x, y + height, zLevel, minU, maxV);
        tess.addVertexWithUV(x + width, y + height, zLevel, maxU, maxV);
        tess.addVertexWithUV(x + width, y + 0, zLevel, maxU, minV);
        tess.addVertexWithUV(x, y + 0, zLevel, minU, minV);

        if (getOverlay() != null) {
            getOverlay().render(x, y, width, height, zLevel, false);
        }

        if (doDraw) {
            tess.draw();
        }
    }

    double getMinU();

    double getMinV();

    double getMaxU();

    double getMaxV();

    IGT_GuiIcon getOverlay();

    @NonNull ResourceLocation getResourceLocation();

}
