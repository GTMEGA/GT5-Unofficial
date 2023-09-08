package gregtech.api.gui.widgets;


import gregtech.api.gui.GT_GUIContainer_Plus;
import gregtech.api.interfaces.IGuiScreen;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;
import lombok.var;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Setter
@Getter
public class GT_GuiSlider extends Gui implements IGT_GuiButton {

    public interface ITextHandler {

        String handle(final GT_GuiSlider slider);

    }


    public interface IOnChange {

        void hook(final GT_GuiSlider slider);

    }


    public enum SlideJustification {
        LEFT,
        RIGHT,
        CENTER
    }


    private final IGuiScreen gui;

    private final int subdivisions;

    private final int width;

    private final int height;

    private final List<GT_GuiSlider> dependents = new ArrayList<>();

    private final int id;

    private final int x;

    private final int y;

    private SlideJustification justification = SlideJustification.CENTER;

    private int guiX = 0;

    private int guiY = 0;

    private double barDiameter = 0.1;

    private double sliderWidthFuzzy = 0.1;

    private double sliderHeightFuzzy = 0.3;

    private double min;

    private double max;

    private double current;

    private GT_GuiTooltip tooltip = null;

    @Accessors(chain = true)
    @Getter
    @Setter
    private IGT_GuiHook onClickBehavior, onInitBehavior, onUpdateBehavior;

    private int updateCooldown = 0;

    private IOnChange onChange = null;

    private boolean isDragged = false;

    private int precision = 0;

    private ITextHandler textHandler = null;

    private boolean liveUpdate = false;

    private boolean inside = false;

    private boolean showNumbers = true;

    private boolean drawOnDrag = false;

    public GT_GuiSlider(
            final IGuiScreen gui, final int id, final int x, final int y, final int width, final int height, double min, double max, final double current, final int subdivisions
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
        this.subdivisions = subdivisions;
        setValue(current);
        updateSlider(false);
        this.gui.addElement(this);
    }

    public void setBarDiameter(final double newDiameter) {
        barDiameter = Math.max(0.0, Math.min(1.0, newDiameter));
    }

    public void updateBounds(double newMin, double newMax, final boolean propagate) {
        if (newMin > newMax) {
            val temp = newMin;
            newMin = newMax;
            newMax = temp;
        }
        val oldRange = max - min;
        val oldMin = min;
        this.min = newMin;
        this.max = newMax;
        val newRange = this.max - this.min;
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
     *
     */
    @Override
    public void onInit() {
        if (onChange != null) {
            onChange.hook(this);
        }
        this.guiX = this.x + this.gui.getGuiLeft();
        this.guiY = this.y + this.gui.getGuiTop();
    }

    /**
     * @param mouseX
     * @param mouseY
     * @param parTicks
     */
    @Override
    public void draw(final int mouseX, final int mouseY, final float parTicks) {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        GL11.glTranslatef(this.gui.getGuiLeft(), this.gui.getGuiTop(), 0);
        //
        onUpdate(this.gui, mouseX, mouseY, 0);
        drawBackground(mouseX, mouseY, parTicks);
        drawInfo(mouseX, mouseY, parTicks);
        onMouseDragged(mouseX - this.gui.getGuiLeft(), mouseY - this.gui.getGuiTop());
        drawSlide(mouseX, mouseY, parTicks);
        //
        GL11.glPopMatrix();
        GL11.glPopAttrib();
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

    /**
     * @param mouseX
     * @param mouseY
     * @param clickType
     * @return
     */
    @Override
    public boolean inBounds(final int mouseX, final int mouseY, final int clickType) {
        return this.inside = mouseX > getX() - width * sliderWidthFuzzy / 2 && mouseX < getX() + width + width * sliderWidthFuzzy / 2 && mouseY > getY() && mouseY < getY() + height;
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getX() + width, getY() + height);
    }

    public void onMouseDragged(final int mouseX, final int mouseY) {
        if (this.isDragged) {
            if (this.onChange != null && this.liveUpdate) {
                this.onChange.hook(this);
            }
            onMousePressed(mouseX, mouseY, 0);
        } else {
            if (this.onChange != null) {
                this.onChange.hook(this);
            }
        }
    }

    public double getValue() {
        var val = current * (max - min) + min;
        val = Math.max(val, min);
        val = Math.min(val, max);
        return val;
    }

    public void setValue(final double value) {
        val range = this.max - this.min;
        this.current = range != 0 ? (value - this.min) / range : this.min;
    }

    private double mouseRelative;

    public void onMousePressed(final int mouseX, final int mouseY, final int clickType) {
        if (clickType == 0 && mouseInBar(mouseX, mouseY, clickType)) {
            val pseudoLeft = x + width * barRadius();
            val pseudoWidth = width * inverseDiameter();
            val mousePos = (mouseX - pseudoLeft) / pseudoWidth;
            if (isDragged) {
                this.current = mousePos - mouseRelative;
            } else {
                mouseRelative = mousePos - this.current;
            }
            updateSlider(true);
            this.isDragged = true;
        } else {
            this.isDragged = false;
            updateSlider(true);
        }
        onClick(this.gui, mouseX, mouseY, clickType);
    }

    public void onMouseReleased(final int mouseX, final int mouseY, final int clickType) {
        if (clickType == 0) {
            this.isDragged = false;
            lockSliderToSubdivisions();
        }
    }

    public void lockSliderToSubdivisions() {
        if (!hasSubdivisions()) {
            return;
        }
        val range = max - min;
        val subRange = range / (subdivisions + 1);
        val notches = Math.floor(current / subRange);
        current = notches * subRange + min;
    }

    public boolean hasSubdivisions() {
        return subdivisions > 0;
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
        val hoverModifier = this.inBounds(mouseX, mouseY, 0) ? 0x00303000 : 0;
        val edgeColor = GT_GUIContainer_Plus.colorToARGB(new Color(0x30, 0x30, 0xFF, 0xFF)) ^ hoverModifier;
        val midColor = GT_GUIContainer_Plus.colorToARGB(new Color(0x30, 0x30, 0xFF, 0x3F)) ^ hoverModifier;
        val left = x;
        val right = x + width;
        final int barLeft;
        final int barRight;
        if (isDragged) {
            barLeft = getBarLeft();
            barRight = getBarRight();
        } else {
            barLeft = barRight = getBarCenter();
        }
        drawBarLower(left, barLeft, edgeColor, midColor);
        drawBarUpper(barRight, right, midColor, edgeColor);
    }

    protected void drawBarUpper(final int left, final int right, final int midColor, final int edgeColor) {
        drawGradientRect(left, y, right, y + height, midColor, edgeColor, false);
    }

    protected void drawBarLower(final int left, final int right, final int edgeColor, final int midColor) {
        drawGradientRect(left, y, right, y + height, edgeColor, midColor, false);
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

    public double barRadius() {
        return barDiameter / 2;
    }

    protected void drawInfo(final int mouseX, final int mouseY, final float parTicks) {
        val format = String.format("%%.%df", precision);
        if (showNumbers) {
            String aStr = String.format(format, min);
            String bStr = String.format(format, max);
            val aWidth = this.gui.getFontRenderer().getStringWidth(aStr);
            var bWidth = this.gui.getFontRenderer().getStringWidth(bStr);
            var firLoc = x;
            var secLoc = x + width - bWidth;
            int aY = y + height + Math.max(1, height / 10);
            if (aWidth + bWidth > width * 0.8) {
                val aAbs = Math.abs(min);
                val bAbs = Math.abs(max);
                if (aAbs > Math.pow(10, 3)) {
                    aStr = getCompressedString(min);
                }
                if (bAbs > Math.pow(10, 3)) {
                    bStr = getCompressedString(max);
                    bWidth = this.gui.getFontRenderer().getStringWidth(bStr);
                    secLoc = x + width - bWidth;
                }
            }
            this.gui.getFontRenderer().drawString(aStr, firLoc, aY, textColor());
            this.gui.getFontRenderer().drawString(bStr, secLoc, aY, textColor());
        }
        if (isDragged && isDrawOnDrag()) {
            final String str;
            if (textHandler == null) {
                str = String.format(format, getValue());
            } else {
                str = textHandler.handle(this);
            }
            val sWidth = this.gui.getFontRenderer().getStringWidth(str);
            // This will be fucking broken but it looked like shit anyhow
            this.gui.getFontRenderer().drawString(str, mouseX - sWidth / 2, Math.min(y - height * 2, mouseY - height), textColor());
        }
    }

    protected void drawSlide(final int mouseX, final int mouseY, final float parTicks) {
        final int color;
        if (isDragged) {
            color = GT_GUIContainer_Plus.colorToARGB(new Color(43, 43, 80, 0xFF));
        } else {
            color = GT_GUIContainer_Plus.colorToARGB(new Color(0x3F, 0x3F, 0xFF, 0x7F));
        }
        drawRect(getBarLeft(), y, getBarRight(), y + height, color);
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
        return GT_GUIContainer_Plus.colorToARGB(new Color(0x3F, 0x3F, 0xFF, 0xFF));
    }

    protected boolean mouseInBar(final int mouseX, final int mouseY, final int clickState) {
        return mouseX > getX() - width * sliderWidthFuzzy && mouseX < getX() + width + width * sliderWidthFuzzy && mouseY > getY() - height * sliderHeightFuzzy && mouseY < getY() + height + height * sliderHeightFuzzy;
    }

    private double getPseudoRight() {
        return getPseudoX() + barRadius();
    }

    private double getPseudoLeft() {
        return getPseudoX() - barRadius();
    }

    private double getPseudoX() {
        return current * inverseDiameter() + barRadius();
    }

    private double inverseDiameter() {
        return 1 - barDiameter;
    }

    private int getBarLeft() {
        return (int) (x + width * getPseudoLeft());
    }

    private int getBarRight() {
        return (int) (x + width * getPseudoRight());
    }

    private int getBarCenter() {
        return (int) (x + width * getPseudoX());
    }

    private String getCompressedString(final double amt) {
        final String fmt = String.format("%%.%df%%s", 1);
        final String[] negPrefix = {"", "m", "μ", "p", "f", "a", "z", "y", "q"};
        final String[] posPrefix = {"", "k", "M", "b", "t", "q", "Q", "s", "S", "o", "n", "d"};
        final int pow = getLogTen(amt);
        final String suffix;
        if (pow < 0) {
            suffix = negPrefix[-pow / 3];
        } else {
            suffix = posPrefix[pow / 3];
        }
        return String.format(fmt, amt * Math.pow(10, -((double) ((pow / 3) * 3))), suffix);
    }

}
