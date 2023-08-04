package gregtech.api.gui.widgets;


import gregtech.api.interfaces.IGuiScreen;
import lombok.Setter;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import java.awt.*;


public class GT_GuiSlider extends Gui implements IGT_GuiButton {


    private final IGuiScreen gui;

    private final int x;

    private final int y;

    private final int width;

    private final int height;

    private final double min;

    private final double max;

    private double current;

    private GT_GuiTooltip tooltip = null;

    private IGT_GuiButtonHook hook = null;

    private boolean isDragged = false;

    @Setter
    private int precision = 0;

    public GT_GuiSlider(
            final IGuiScreen gui, final int x, final int y, final int width, final int height, final double min, final double max, final double current
                       ) {
        this.gui = gui;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.min = min;
        this.max = max;
        this.current = (current - min) / (max - min);
    }

    public double getValue() {
        return current * (max - min) + min;
    }

    /**
     * @return
     */
    @Override
    public IGT_GuiButtonHook getOnClickBehavior() {
        return hook;
    }

    /**
     * @param hook
     * @return
     */
    @Override
    public IGT_GuiButton setOnClickBehavior(final IGT_GuiButtonHook hook) {
        this.hook = hook;
        return this;
    }

    /**
     *
     */
    @Override
    public void onInit() {

    }

    /**
     * @param mouseX
     * @param mouseY
     * @param parTicks
     */
    @Override
    public void draw(final int mouseX, final int mouseY, final float parTicks) {
        drawBackground(mouseX, mouseY, parTicks);
        drawInfo(mouseX, mouseY, parTicks);
        drawSlide(mouseX, mouseY, parTicks);
        onMouseDragged(mouseX, mouseY, 0);
    }

    protected void drawSlide(final int mouseX, final int mouseY, final float parTicks) {
        final double barWidth = 0.2;
        final int positionLeft = (int) (getX() + width * (current - barWidth / 2));
        final int positionRight = (int) (getX() + width * (current + barWidth / 2));
        final int color;
        if (isDragged) {
            color = 0xFF3F3F7F;
        } else {
            color = 0x7F3F3F7F;
        }
        drawRect(positionLeft, getY(), positionRight, getY() + height, color);
    }

    protected void drawBackground(final int mouseX, final int mouseY, final float parTicks) {
        final int edgeColor, midColor;
        edgeColor = 0xFF0000FF;
        midColor = 0x3F0000FF;
        final int left = getX();
        final int midPoint = (int) (getX() + width * current);
        final int right = getX() + width;
        final int mpLeft;
        final int mpRight;
        if (isDragged) {
             mpLeft = midPoint - width / 10;
             mpRight = midPoint + width / 10;
        } else {
            mpLeft = mpRight = midPoint;
        }
        drawGradientRect(left, getY(), mpLeft, getY() + height, edgeColor, midColor, false);
        drawGradientRect(mpRight, getY(), right, getY() + height, midColor, edgeColor, false);
    }

    protected void drawInfo(final int mouseX, final int mouseY, final float parTicks) {
        final String format = String.format("%%.%df", precision);
        final String aStr = String.format(format, min);
        final String bStr = String.format(format, max);
        final int bWidth;
        bWidth = this.gui.getFontRenderer().getStringWidth(bStr);
        final int firLoc = getX();
        final int secLoc = getX() + width - bWidth;
        this.gui.getFontRenderer().drawString(aStr, firLoc, getY() + height, textColor());
        this.gui.getFontRenderer().drawString(bStr, secLoc, getY() + height, textColor());
        if (isDragged) {
            final String str = String.format("%.1f", getValue());
            final int sWidth = this.gui.getFontRenderer().getStringWidth(str);
            this.gui.getFontRenderer().drawString(str, mouseX - sWidth / 2, getY() - height, textColor());
        }
    }

    public void onMouseDragged(final int mouseX, final int mouseY, final int clickType) {
        if (this.isDragged) {
            onMousePressed(mouseX, mouseY, clickType);
        }
    }

    public int getX() {
        return x + this.gui.getGuiLeft();
    }

    /**
     * Draws a rectangle with a vertical gradient between the specified colors.
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param colorA
     * @param colorB
     */
    @Override
    protected void drawGradientRect(
            final int left, final int top, final int right, final int bottom, final int colorA, final int colorB
                                   ) {
        super.drawGradientRect(left, top, right, bottom, colorA, colorB);
    }

    public int getY() {
        return y + this.gui.getGuiTop();
    }

    protected int textColor() {
        return 0xFF3F3FFF;
    }

    public void onMousePressed(final int mouseX, final int mouseY, final int clickType) {
        this.current = (double) (mouseX - (getX() + 2)) / (double) (this.width - 4);
        updateSlider();
        this.isDragged = true;
    }

    public void updateSlider() {
        if (current > max) {
            current = max;
        }
        if (current < min) {
            current = min;
        }
    }

    /**
     * @return
     */
    @Override
    public GT_GuiTooltip getTooltip() {
        return tooltip;
    }

    /**
     * @param text
     * @return
     */
    @Override
    public IGuiScreen.IGuiElement setTooltipText(final String... text) {
        if (tooltip == null) {
            this.tooltip = new GT_GuiTooltip(getBounds(), text);
        } else {
            this.tooltip.setToolTipText(text);
        }
        return null;
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getX() + width, getY() + height);
    }

    /**
     * @param mouseX
     * @param mouseY
     * @param clickType
     * @return
     */
    @Override
    public boolean inBounds(final int mouseX, final int mouseY, final int clickType) {
        return mouseX > getX() && mouseX < getX() + width && mouseY > getY() && mouseY < getY() + height;
    }

    public void onMouseReleased(final int mouseX, final int mouseY, final int clickState) {
        if (clickState == 0 || clickState == 1) {
            this.isDragged = false;
        }
    }

    /**
     * Draws a gradient with directionality
     *
     * @param vertical 0-3, inclusive, 0 is L->R, 1 is T->B, and the remaining two are the inverses
     */
    protected void drawGradientRect(final int left, final int top, final int right, final int bottom, final int colorA, final int colorB, final boolean vertical) {
        float f = (float)(colorA >> 24 & 255) / 255.0F;
        float f1 = (float)(colorA >> 16 & 255) / 255.0F;
        float f2 = (float)(colorA >> 8 & 255) / 255.0F;
        float f3 = (float)(colorA & 255) / 255.0F;
        float f4 = (float)(colorB >> 24 & 255) / 255.0F;
        float f5 = (float)(colorB >> 16 & 255) / 255.0F;
        float f6 = (float)(colorB >> 8 & 255) / 255.0F;
        float f7 = (float)(colorB & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        if (vertical) {
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(f1, f2, f3, f);
            tessellator.addVertex((double)left, (double)top, (double)this.zLevel);
            tessellator.addVertex((double)right, (double)top, (double)this.zLevel);
            tessellator.setColorRGBA_F(f5, f6, f7, f4);
            tessellator.addVertex((double)right, (double)bottom, (double)this.zLevel);
            tessellator.addVertex((double)left, (double)bottom, (double)this.zLevel);
            tessellator.draw();
        } else {
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(f1, f2, f3, f);
            tessellator.addVertex((double)left, (double)top, (double)this.zLevel);
            tessellator.addVertex((double)left, (double)bottom, (double)this.zLevel);
            tessellator.setColorRGBA_F(f5, f6, f7, f4);
            tessellator.addVertex((double)right, (double)bottom, (double)this.zLevel);
            tessellator.addVertex((double)right, (double)top, (double)this.zLevel);
            tessellator.draw();
        }
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    protected int sliderBackgroundColor() {
        return 0xAF7F7FFF;
    }

}
