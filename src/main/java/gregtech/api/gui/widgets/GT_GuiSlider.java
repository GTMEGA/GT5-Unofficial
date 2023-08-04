package gregtech.api.gui.widgets;


import gregtech.api.interfaces.IGuiScreen;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Getter
public class GT_GuiSlider extends Gui implements IGT_GuiButton {

    public interface ITextHandler {

        String handle(final GT_GuiSlider slider);

    }


    public interface IOnChange {

        void hook(final GT_GuiSlider slider);

    }


    private final IGuiScreen gui;

    private final int x;

    private final int y;

    private final int width;

    private final int height;

    private final List<GT_GuiSlider> dependents = new ArrayList<>();

    @Getter
    private final int id;

    private double min;

    private double max;

    private double current;

    private GT_GuiTooltip tooltip = null;

    private IGT_GuiButtonHook hook = null;

    private IOnChange onChange = null;

    private boolean isDragged = false;

    @Setter
    private int precision = 0;

    private ITextHandler textHandler = null;

    public GT_GuiSlider(
            final int id, final IGuiScreen gui, final int x, final int y, final int width, final int height, double min, double max, final double current
                       ) {
        this.id = id;
        this.gui = gui;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        if (min > max) {
            double temp = min;
            min = max;
            max = temp;
        }
        this.min = min;
        this.max = max;
        setValue(current);
        updateSlider(false);
        this.gui.addElement(this);
    }

    public void updateBounds(double newMin, double newMax, final boolean propagate) {
        if (newMin > newMax) {
            double temp = newMin;
            newMin = newMax;
            newMax = temp;
        }
        double oldRange = max - min;
        double oldMin = min;
        this.min = newMin;
        this.max = newMax;
        final double newRange = this.max - this.min;
        this.current = newRange != 0 ? (this.current * oldRange + oldMin - this.min) / newRange : newMin;
        this.updateSlider(propagate);
    }

    public void updateBounds(final double[] bounds, final boolean propagate) {
        if (bounds.length == 2) {
            updateBounds(bounds[0], bounds[1], propagate);
        }
    }

    public void updateSlider(final boolean propagate) {
        if (current > 1.0) {
            current = 1.0;
        }
        if (current < 0.0) {
            current = 0.0;
        }
        if (propagate) {
            for (final GT_GuiSlider slider : dependents) {
                slider.updateSlider(false);
            }
        }
    }

    public GT_GuiSlider addDependentSlider(final GT_GuiSlider slider) {
        dependents.add(slider);
        return this;
    }

    public GT_GuiSlider addDependentSliders(final GT_GuiSlider... sliders) {
        this.dependents.addAll(Arrays.asList(sliders));
        return this;
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
        if (onChange != null) {
            onChange.hook(this);
        }
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
        onMouseDragged(mouseX, mouseY, 0);
        drawSlide(mouseX, mouseY, parTicks);
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

    public int getX() {
        return x + this.gui.getGuiLeft();
    }

    public int getY() {
        return y + this.gui.getGuiTop();
    }

    /**
     * @param mouseX
     * @param mouseY
     * @param clickType
     * @return
     */
    @Override
    public boolean inBounds(final int mouseX, final int mouseY, final int clickType) {
        return mouseX > getX() - width / 20 && mouseX < getX() + width + width / 20 && mouseY > getY() && mouseY < getY() + height;
    }

    public void onMouseDragged(final int mouseX, final int mouseY, final int clickType) {
        if (this.isDragged) {
            onMousePressed(mouseX, mouseY, clickType);
        } else {
            if (this.onChange != null) {
                this.onChange.hook(this);
            }
        }
    }

    public double getValue() {
        double val = current * (max - min) + min;
        val = Math.max(val, min);
        val = Math.min(val, max);
        return val;
    }

    public void setValue(final double value) {
        final double range = this.max - this.min;
        this.current = range != 0 ? (value - this.min) / range : this.min;
    }

    public void onMousePressed(final int mouseX, final int mouseY, final int clickType) {
        if (clickType >= 0 && mouseInBar(mouseX, mouseY, clickType)) {
            this.current = (double) (mouseX - (getX() + width / 20)) / (double) (this.width - width / 10);
            updateSlider(true);
            this.isDragged = true;
        } else {
            this.isDragged = false;
            updateSlider(true);
        }
        onClick(this.gui, mouseX, mouseY);
    }

    public void onMouseReleased(final int mouseX, final int mouseY, final int clickState) {
        if (clickState == 0 || clickState == 1) {
            this.isDragged = false;
        }
    }

    public GT_GuiSlider setTextHandler(final ITextHandler textHandler) {
        this.textHandler = textHandler;
        return this;
    }

    public GT_GuiSlider setOnChange(final IOnChange onChange) {
        this.onChange = onChange;
        return this;
    }

    protected void drawBackground(final int mouseX, final int mouseY, final float parTicks) {
        final int edgeColor, midColor;
        edgeColor = 0xFF0000FF;
        midColor = 0x3F0000FF;
        final int left = getX();
        final int right = getX() + width;
        final int midPoint = (int) (getX() + width * current);
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

    protected int getLogTen(double val) {
        if (val == 0.0) {
            return 0;
        }
        val = val > 0.0 ? val : -val;
        int result = 0;
        if (val < 1.0) {
            while (val < 1.0) {
                val *= 10;
                result -= 1;
            }
        } else if (val > 10.0) {
            while (val > 10.0) {
                val /= 10.0;
                result += 1;
            }
        }
        return result;
    }

    protected double getMantissa(double amt) {
        if (amt > 10.0) {
            while (amt > 10.0) {
                amt /= 10.0;
            }
        } else if (amt < 10.0) {
            while (amt < 1.0) {
                amt *= 10.0;
            }
        }
        return amt;
    }

    protected void drawInfo(final int mouseX, final int mouseY, final float parTicks) {
        final String[] negPrefix = {"", "m", "μ", "p", "f", "a", "z", "y", "q"};
        final String[] posPrefix = {"", "k", "M", "b", "t", "q", "Q", "s", "S", "o", "n", "d"};
        final String format = String.format("%%.%df", precision);
        String aStr = String.format(format, min);
        String bStr = String.format(format, max);
        final int aWidth = this.gui.getFontRenderer().getStringWidth(aStr);
        int bWidth = this.gui.getFontRenderer().getStringWidth(bStr);
        int firLoc = getX();
        int secLoc = getX() + width - bWidth;
        int aY = getY() + height + Math.max(1, height / 10);
        int bY = aY;
        if (aWidth + bWidth > width * 0.8) {
            final double aAbs = Math.abs(min);
            final double bAbs = Math.abs(max);
            String fmt = String.format("%%.%df%%s", 1);
            if (aAbs > Math.pow(10, 3)) {
                final int pow = getLogTen(min);
                final String suffix;
                if (pow < 0) {
                    suffix = negPrefix[-pow / 3];
                } else {
                    suffix = posPrefix[pow / 3];
                }
                aStr = String.format(fmt, min * Math.pow(10, -pow), suffix);
            }
            if (bAbs > Math.pow(10, 3)) {
                final int pow = getLogTen(max);
                final String suffix;
                if (pow < 0) {
                    suffix = negPrefix[-pow / 3];
                } else {
                    suffix = posPrefix[pow / 3];
                }
                bStr = String.format(fmt, max * Math.pow(10, -pow), suffix);
                bWidth = this.gui.getFontRenderer().getStringWidth(bStr);
                secLoc = getX() + width - bWidth;
            }
        }
        this.gui.getFontRenderer().drawString(aStr, firLoc, aY, textColor());
        this.gui.getFontRenderer().drawString(bStr, secLoc, bY, textColor());
        if (isDragged) {
            final String str;
            if (textHandler == null) {
                str = String.format(format, getValue());
            } else {
                str = textHandler.handle(this);
            }
            final int sWidth = this.gui.getFontRenderer().getStringWidth(str);
            this.gui.getFontRenderer().drawString(str, mouseX - sWidth / 2, mouseY - height, textColor());
        }
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

    /**
     * Draws a gradient with directionality
     *
     * @param vertical 0-3, inclusive, 0 is L->R, 1 is T->B, and the remaining two are the inverses
     */
    protected void drawGradientRect(
            final int left, final int top, final int right, final int bottom, final int colorA, final int colorB, final boolean vertical
                                   ) {
        float f = (float) (colorA >> 24 & 255) / 255.0F;
        float f1 = (float) (colorA >> 16 & 255) / 255.0F;
        float f2 = (float) (colorA >> 8 & 255) / 255.0F;
        float f3 = (float) (colorA & 255) / 255.0F;
        float f4 = (float) (colorB >> 24 & 255) / 255.0F;
        float f5 = (float) (colorB >> 16 & 255) / 255.0F;
        float f6 = (float) (colorB >> 8 & 255) / 255.0F;
        float f7 = (float) (colorB & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        if (vertical) {
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(f1, f2, f3, f);
            tessellator.addVertex(left, top, this.zLevel);
            tessellator.addVertex(right, top, this.zLevel);
            tessellator.setColorRGBA_F(f5, f6, f7, f4);
            tessellator.addVertex(right, bottom, this.zLevel);
            tessellator.addVertex(left, bottom, this.zLevel);
            tessellator.draw();
        } else {
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(f1, f2, f3, f);
            tessellator.addVertex(left, top, this.zLevel);
            tessellator.addVertex(left, bottom, this.zLevel);
            tessellator.setColorRGBA_F(f5, f6, f7, f4);
            tessellator.addVertex(right, bottom, this.zLevel);
            tessellator.addVertex(right, top, this.zLevel);
            tessellator.draw();
        }
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    protected int textColor() {
        return 0xFF3F3FFF;
    }

    protected boolean mouseInBar(final int mouseX, final int mouseY, final int clickState) {
        return mouseX > getX() - width / 20 && mouseX < getX() + width + width / 20;
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

    protected int sliderBackgroundColor() {
        return 0xAF7F7FFF;
    }

}
