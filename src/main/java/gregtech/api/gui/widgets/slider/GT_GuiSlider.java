package gregtech.api.gui.widgets.slider;


import gregtech.api.gui.GT_RichGuiContainer;
import gregtech.api.gui.widgets.GT_GuiTooltip;
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


@Getter
public abstract class GT_GuiSlider extends Gui implements IGuiScreen.IGuiElement {

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


    protected final IGuiScreen gui;

    protected final int subdivisions;

    protected final int width;

    protected final int height;

    protected final int id;

    protected final int x;

    protected final int y;

    private final Color sliderDraggedColor = new Color(43, 43, 80, 0xFF);

    private final Color sliderNormalColor = new Color(0x3F, 0x3F, 0xFF, 0x7F);

    protected double min;

    protected double max;

    protected double current;

    @Accessors(chain = true)
    @Setter
    protected IOnChange onChange = null;

    protected boolean isDragged = false;

    @Setter
    protected int precision = 0;

    @Accessors(chain = true)
    @Setter
    protected ITextHandler textHandler = null;

    @Setter
    protected boolean liveUpdate = false;

    @Setter
    protected boolean showNumbers = true;

    protected boolean inside = false;

    @Setter
    private Color edgeColor = new Color(0x30, 0x30, 0xFF, 0xFF);

    @Setter
    private Color midColor = new Color(0x30, 0x30, 0xFF, 0x3F);

    @Setter
    private int hoverModifier = 0x00303000;

    @Setter
    private SlideJustification justification = SlideJustification.CENTER;

    private int guiX = 0;

    private int guiY = 0;

    private double barDiameter = 0.1;

    @Setter
    private double sliderWidthFuzzy;

    @Setter
    private double sliderHeightFuzzy = 0.3;

    private GT_GuiTooltip tooltip = null;

    @Accessors(chain = true)
    @Setter
    private IGT_GuiHook onClickBehavior;

    @Accessors(chain = true)
    @Setter
    private IGT_GuiHook onInitBehavior;

    @Accessors(chain = true)
    @Setter
    private IGT_GuiHook onUpdateBehavior;

    @Setter
    private int updateCooldown = 0;

    @Setter
    private boolean drawOnDrag = false;

    public GT_GuiSlider(final IGuiScreen gui, final int id, final int x, final int y, final int width, final int height, double min, double max, final double current, final int subdivisions) {
        this.gui = gui;
        this.subdivisions = subdivisions;
        this.width = width;
        this.height = height;
        this.id = id;
        this.x = x;
        this.y = y;
        if (min > max) {
            double temp = min;
            min = max;
            max = temp;
        }
        this.min = min;
        this.max = max;
        setValue(current);
        updateSlider();
        this.gui.addElement(this);
    }

    public void updateSlider() {
        if (current > 1.0) {
            current = 1.0;
        }
        if (current < 0.0) {
            current = 0.0;
        }
    }

    public void setBarDiameter(final double newDiameter) {
        barDiameter = Math.max(0.0, Math.min(1.0, newDiameter));
    }

    public void updateBounds(final double[] bounds) {
        if (bounds.length == 2) {
            updateBounds(bounds[0], bounds[1]);
        }
    }

    public void updateBounds(double newMin, double newMax) {
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
        this.updateSlider();
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
        onMouseDragged(mouseX, mouseY);
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
        /* val sliderLeft = getX() - width * sliderWidthFuzzy / 2;
        val sliderRight = sliderLeft + width + width * sliderWidthFuzzy;
        val sliderTop = getY() - height * sliderHeightFuzzy / 2;
        val sliderBottom = sliderTop + height + height * sliderHeightFuzzy;
        return this.inside = mouseX > sliderLeft && mouseX < sliderRight && mouseY > sliderTop && mouseY < sliderBottom; */
        val bounds = getBounds();
        val contained = bounds.contains(mouseX, mouseY);
        return contained;
    }

    /**
     * @param screen
     * @param mouseX
     * @param mouseY
     * @param clickType
     */
    @Override
    public void onInit(final IGuiScreen screen, final int mouseX, final int mouseY, final int clickType) {
        IGuiScreen.IGuiElement.super.onInit(screen, mouseX, mouseY, clickType);
    }

    /**
     * @param screen
     * @param mouseX
     * @param mouseY
     * @param clickType
     */
    @Override
    public void onUpdate(final IGuiScreen screen, final int mouseX, final int mouseY, final int clickType) {
        IGuiScreen.IGuiElement.super.onUpdate(screen, mouseX, mouseY, clickType);
    }

    /**
     * @param screen
     * @param mouseX
     * @param mouseY
     * @param clickType
     */
    @Override
    public void onClick(final IGuiScreen screen, final int mouseX, final int mouseY, final int clickType) {
        IGuiScreen.IGuiElement.super.onClick(screen, mouseX, mouseY, clickType);
    }

    public Rectangle getBounds() {
        val sliderLeft = (int) (getX() + gui.getGuiLeft() - width * sliderWidthFuzzy / 2);
        val sliderTop = (int) (getY() + gui.getGuiTop() - height * sliderHeightFuzzy / 2);
        return new Rectangle(sliderLeft, sliderTop, (int) (width * (1 + sliderWidthFuzzy)), (int) (height * (1 + sliderHeightFuzzy)));
    }

    public void onMouseDragged(final int mouseX, final int mouseY) {
        if (this.isDragged && inBounds(mouseX, mouseY, 0)) {
            if (this.onChange != null && this.liveUpdate) {
                this.onChange.hook(this);
            }
            onMousePressed(mouseX, mouseY, 0);
        } else {
            if (this.onChange != null) {
                this.onChange.hook(this);
            }
            this.isDragged = false;
        }
    }

    public void onMousePressed(final int mouseX, final int mouseY, final int clickType) {
        if (clickType == 0 && inBounds(mouseX, mouseY, clickType)) {
            handleMouse(mouseX, mouseY);
        } else {
            this.isDragged = false;
            updateSlider();
        }
        onClick(this.gui, mouseX, mouseY, clickType);
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

    public void onMouseReleased(final int mouseX, final int mouseY, final int clickState) {
        if (clickState == 1) {
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

    /**
     * Draws a gradient with directionality
     *
     * @param vertical 0-3, inclusive, 0 is L->R, 1 is T->B, and the remaining two are the inverses
     */
    public void drawGradientRect(
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
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        if (vertical) {
            tessellator.addVertex(right, top, this.zLevel);
            tessellator.addVertex(left, top, this.zLevel);
            tessellator.setColorRGBA_F(f5, f6, f7, f4);
            tessellator.addVertex(left, bottom, this.zLevel);
            tessellator.addVertex(right, bottom, this.zLevel);
        } else {
            tessellator.addVertex(left, top, this.zLevel);
            tessellator.addVertex(left, bottom, this.zLevel);
            tessellator.setColorRGBA_F(f5, f6, f7, f4);
            tessellator.addVertex(right, bottom, this.zLevel);
            tessellator.addVertex(right, top, this.zLevel);
        }
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    protected double getPseudoPos() {
        return current * inverseDiameter() + barRadius();
    }

    protected double inverseDiameter() {
        return 1 - barDiameter;
    }

    public double barRadius() {
        return barDiameter / 2;
    }

    protected void drawBackground(final int mouseX, final int mouseY, final float parTicks) {
        val hoverModifier = this.inBounds(mouseX, mouseY, 0) ? this.hoverModifier : 0;
        val edgeColorBase = GT_RichGuiContainer.colorToARGB(edgeColor);
        val edgeColor = edgeColorBase ^ hoverModifier;
        val midColorBase = GT_RichGuiContainer.colorToARGB(midColor);
        val midColor = midColorBase ^ hoverModifier;
        drawSliderBackground(edgeColor, midColor);
    }

    protected abstract void drawInfo(int mouseX, int mouseY, float parTicks);

    protected void drawSlide(final int mouseX, final int mouseY, final float parTicks) {
        final int color;
        if (isDragged) {
            color = GT_RichGuiContainer.colorToARGB(sliderDraggedColor);
        } else {
            color = GT_RichGuiContainer.colorToARGB(sliderNormalColor);
        }
        drawSlideInternal(color);
    }

    protected abstract void drawSlideInternal(int color);

    protected abstract void drawSliderBackground(int edgeColor, int midColor);

    protected abstract void handleMouse(int mouseX, int mouseY);

    protected int textColor() {
        return GT_RichGuiContainer.colorToARGB(new Color(0x3F, 0x3F, 0xFF, 0xFF));
    }

    protected abstract void drawBarUpper(int start, int end, int midColor, int edgeColor);

    protected abstract void drawBarLower(int start, int end, int edgeColor, int midColor);

}
