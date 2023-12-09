package gregtech.api.gui.widgets;


import gregtech.api.interfaces.IGuiScreen;
import ic2.core.util.Vector2;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.entity.RenderItem;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
public class GT_GuiScrollPanel2D<ParentType extends GuiScreen & IGuiScreen> extends GuiScreen implements IGT_GuiSubWindow {

    public interface IScrollableElement extends IGuiElement {

        int getScrollWidth();

        int getScrollHeight();

        void setRenderX(final int renderX);

        void setRenderY(final int renderY);

        default boolean queryCanRender() {
            val result = isCanRender();
            setCanRender(false);
            return result;
        }

        boolean isCanRender();

        void setCanRender(final boolean canRender);

        GT_GuiScrollPanel2D<?> getScrollPanel();

    }


    private final ParentType parent;

    private final int id;

    private final int x;

    private final int y;

    private final Map<IScrollableElement, Point> scrollableElements = new HashMap<>();

    private final List<IGuiElement> elements = new ArrayList<>();

    private final GT_GuiTooltipManager tooltipManager = new GT_GuiTooltipManager();

    private final int windowWidth;

    private final int windowHeight;

    private final int myHeight;

    private final int myWidth;

    @Setter
    private double fuzz = 0.1;

    @Setter
    private Color panelBackground = new Color(19, 19, 19, 0xFF);

    @Setter
    private Color panelInteriorBackground = new Color(65, 65, 65, 255);

    private Rectangle totalBounds = new Rectangle();

    @Setter
    private RenderItem itemRenderer = null;

    @Setter
    private FontRenderer fontRenderer = null;

    @Setter
    private int updateCooldown = 0;

    private final Vector2 currentScroll = new Vector2();

    private GT_GuiTooltip tooltip;

    @Accessors(chain = true)
    @Setter
    private IGT_GuiHook onUpdateBehavior, onInitBehavior, onClickBehavior;

    private String[] tooltipText;

    @Setter
    private GuiButton selectedButton = null;

    public GT_GuiScrollPanel2D(
            ParentType parent, int id, int x, int y, int width, int height, final int windowWidth, final int windowHeight, Vector2 currentScroll
                              ) {
        super();
        this.id = id;
        this.parent = parent;
        this.x = x + parent.getGuiLeft();
        this.y = y + parent.getGuiTop();
        setWorldAndResolution(parent.mc, parent.width, parent.height);
        this.myWidth = width;
        this.myHeight = height;
        if (currentScroll != null) {
            this.currentScroll.set(currentScroll);
        }
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        parent.addElement(this);
    }

    /**
     * Causes the screen to lay out its subcomponents again. This is the equivalent of the Java call
     * Container.validate()
     *
     * @param minecraft
     * @param newWidth
     * @param newHeight
     */
    @Override
    public void setWorldAndResolution(final Minecraft minecraft, final int newWidth, final int newHeight) {
        this.mc = minecraft;
        this.width = newWidth;
        this.height = newHeight;
    }

    /**
     * Draws the background (i is always 0 as of 1.2.2)
     *
     * @param alwaysZero
     */
    @Override
    public void drawBackground(final int alwaysZero) {
        Gui.drawRect(x + getGuiLeft(), y + getGuiTop(), x + getGuiLeft() + myWidth, y + getGuiTop() + myHeight, panelBackground.getRGB());
/*         drawString(getFontRenderer(), String.format("Start: %.2f", pseudoPanelStart()), x + getGuiLeft() + myWidth, y + getGuiTop(), Color.WHITE.getRGB());
        drawString(getFontRenderer(), String.format("End: %.2f", pseudoPanelEnd()), x + getGuiLeft() + myWidth, y + getGuiTop() + 15, Color.WHITE.getRGB());
        drawString(getFontRenderer(), String.format("Effective: %.2f", effectiveScroll()), x + getGuiLeft() + myWidth, y + getGuiTop() + 30, Color.WHITE.getRGB());
        drawString(getFontRenderer(), String.format("Current: %.2f", currentScroll), x + getGuiLeft() + myWidth, y + getGuiTop() + 45, Color.WHITE.getRGB());
        drawString(getFontRenderer(), String.format("Max: %.2f", getMaxScrollFactor()), x + getGuiLeft() + myWidth, y + getGuiTop() + 60, Color.WHITE.getRGB()); */
    }

    public void setCurrentScroll(final double X, final double y) {
        setCurrentScrollX(X);
        setCurrentScrollY(y);
    }

    public void setCurrentScrollX(double value) {
        if (value < 0) {
            currentScroll.x = 0;
        } else if (value > 1) {
            currentScroll.x = 1;
        } else {
            currentScroll.x = value;
        }
    }

    public void setCurrentScrollY(double value) {
        if (value < 0) {
            currentScroll.y = 0;
        } else if (value > 1) {
            currentScroll.y = 1;
        } else {
            currentScroll.y = value;
        }
    }

    /**
     *
     */
    @Override
    public void onInit() {
        if (tooltip != null) {
            addToolTip(tooltip);
        }
        for (final IGuiElement element : elements) {
            element.onInit();
        }
        for (final IScrollableElement element : scrollableElements.keySet()) {
            element.onInit();
        }
    }

    /**
     * @param mouseX
     * @param mouseY
     * @param parTicks
     */
    @Override
    public void draw(final int mouseX, final int mouseY, final float parTicks) {
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        //
        drawBackground(0);
        drawScrollableContent(mouseX, mouseY, parTicks);
        drawRegularContent(mouseX, mouseY, parTicks);
        onUpdate(parent, mouseX, mouseY, 0);
        //
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    /**
     * @param text
     * @return
     */
    @Override
    public IGuiElement setTooltipText(final String... text) {
        if (tooltip == null) {
            tooltip = new GT_GuiTooltip(getBounds(), text);
        } else {
            tooltip.setToolTipText(text);
        }
        this.tooltipText = text;
        return this;
    }

    /**
     * @param mouseX
     * @param mouseY
     * @param clickType
     * @return
     */
    @Override
    public boolean inBounds(final int mouseX, final int mouseY, final int clickType) {
        return getBounds().contains(mouseX, mouseY);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * @param toolTip
     */
    @Override
    public void addToolTip(final GT_GuiTooltip toolTip) {
        tooltipManager.addToolTip(toolTip);
    }

    /**
     * @param toolTip
     * @return
     */
    @Override
    public boolean removeToolTip(final GT_GuiTooltip toolTip) {
        return tooltipManager.removeToolTip(toolTip);
    }

    /**
     *
     */
    @Override
    public void clearSelectedButton() {
        setSelectedButton(null);
    }

    /**
     * @param button
     */
    @Override
    public void buttonClicked(final GuiButton button) {
        for (final IGuiElement element : elements) {
            if (element instanceof GuiButton) {
                ((GuiButton) element).enabled = true;
            }
        }
        clearSelectedButton();
    }

    /**
     * @return
     */
    @Override
    public int getGuiLeft() {
        return parent.getGuiLeft();
    }

    /**
     * @return
     */
    @Override
    public int getGuiTop() {
        return parent.getGuiTop();
    }

    /**
     * @return
     */
    @Override
    public int getXSize() {
        return width;
    }

    /**
     * @return
     */
    @Override
    public int getYSize() {
        return height;
    }

    /**
     * @param element
     */
    @Override
    public void addElement(final IGuiElement element) {
        elements.add(element);
    }

    /**
     * @param element
     * @return
     */
    @Override
    public boolean removeElement(final IGuiElement element) {
        return elements.remove(element);
    }

    /**
     * @return
     */
    @Override
    public RenderItem getItemRenderer() {
        return this.itemRenderer != null ? this.itemRenderer : parent.getItemRenderer();
    }

    /**
     * @return
     */
    @Override
    public FontRenderer getFontRenderer() {
        return this.fontRenderer != null ? this.fontRenderer : parent.getFontRenderer();
    }

    public void addScrollableElement(int x, int y, IScrollableElement element) {
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (scrollableElements.size() == 0) {
            totalBounds = new Rectangle(x, y, element.getScrollWidth(), element.getScrollHeight());
        } else {
            totalBounds = totalBounds.union(new Rectangle(x, y, element.getScrollWidth(), element.getScrollHeight()));
        }
        scrollableElements.put(element, new Point(x, y));
    }

    public IScrollableElement removeScrollableElement(IScrollableElement element) {
        Point removed = scrollableElements.remove(element);
        if (removed == null) {
            return null;
        }
        if (scrollableElements.size() == 0) {
            totalBounds = new Rectangle(0, 0, 0, 0);
        } else {
            boolean first = true;
            for (val elemPair : scrollableElements.entrySet()) {
                val point = elemPair.getValue();
                val elem = elemPair.getKey();
                if (first) {
                    first = false;
                    totalBounds = new Rectangle(point.x, point.y, elem.getScrollWidth(), elem.getScrollHeight());
                } else {
                    totalBounds = totalBounds.union(new Rectangle(point.x, point.y, elem.getScrollWidth(), elem.getScrollHeight()));
                }
            }
        }
        return element;
    }

    public boolean inRange(int x, int y, int w, int h) {
        val start = pseudoPanelStart();
        val end = pseudoPanelEnd();
        val elemStart = getAsProportionOfContent(x, y);
        val elemEnd = getAsProportionOfContent(w, h);
        start.sub(fuzz, fuzz);
        end.add(fuzz, fuzz);
        val panelBox = new Rectangle2D.Double(start.x, start.y, end.x, end.y);
        val elemBox = new Rectangle2D.Double(elemStart.x, elemStart.y, elemEnd.x, elemEnd.y);
        return panelBox.intersects(elemBox) || panelBox.contains(elemBox);
    }

    public int contentOffsetX() {
        return (myWidth - windowWidth) / 2;
    }

    public int contentOffsetY() {
        return (myHeight - windowHeight) / 2;
    }

    @Override
    public void receiveClick(final int mouseX, final int mouseY, final int mouseButton) {

    }

    /**
     * @param mouseX
     * @param mouseY
     * @param lastClick
     */
    @Override
    public void receiveDrag(final int mouseX, final int mouseY, final int lastClick) {

    }

    /**
     * @param mouseX
     * @param mouseY
     * @param clickState
     */
    @Override
    public void receiveMouseMovement(final int mouseX, final int mouseY, final int clickState) {

    }

    /**
     * @return
     */
    @Override
    public Minecraft getMinecraftInstance() {
        return mc;
    }

    public Vector2 effectiveScroll() {
        double x = Math.max(0.0, Math.min(1.0, currentScroll.x));
        double y = Math.max(0.0, Math.min(1.0, currentScroll.y));
        val scroll = getMaxScrollFactor();
        scroll.x *= x;
        scroll.y *= y;
        return scroll;
    }

    public Vector2 getMaxScrollFactor() {
        return new Vector2(getMaxScrollFactor(windowWidth, totalBounds.width), getMaxScrollFactor(windowHeight, totalBounds.height));
    }

    public Vector2 effectiveWindowSize() {
        double w = (double) windowWidth / totalBounds.width;
        double h = (double) windowHeight / totalBounds.height;
        if (w > 1) {
            w = 1;
        }
        if (h > 1) {
            h = 1;
        }
        return new Vector2(w, h);
    }

    protected void drawScrollableContent(final int mouseX, final int mouseY, final float parTicks) {
        val scaled = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight).getScaleFactor();
        //
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(getContentX() * scaled, mc.displayHeight - ((getContentY() + windowHeight) * scaled), windowWidth * scaled, windowHeight * scaled);
        //
        drawScrollPanelBackground(mouseX, mouseY, parTicks);
        for (val entry : scrollableElements.entrySet()) {
            val element = entry.getKey();
            val point = entry.getValue();
            renderIndividualScrollableElement(mouseX, mouseY, parTicks, point, element);
        }
        //
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    protected void renderIndividualScrollableElement(final int mouseX, final int mouseY, final float parTicks, Point elementPosition, final IScrollableElement element) {
        if (inRange(elementPosition.x, elementPosition.y, element.getScrollWidth(), element.getScrollHeight())) {
            val pseudo = pseudoPanelStart();
            val horizontalOffset = (int) (elementPosition.x - totalBounds.width * pseudo.x);
            val verticalOffset = (int) (elementPosition.y - totalBounds.height * pseudo.y);
            val myX = getContentX() + horizontalOffset;
            val myY = getContentY() + verticalOffset;
            element.setRenderX(myX);
            element.setRenderY(myY);
            element.setCanRender(true);
            element.draw(mouseX, mouseY, parTicks);
        }
    }

    protected void drawScrollPanelBackground(final int mouseX, final int mouseY, final float parTicks) {
        Gui.drawRect(getContentX(), getContentY(), getContentX() + windowWidth, getContentY() + windowHeight, panelInteriorBackground.getRGB());
    }

    protected void drawRegularContent(final int mouseX, final int mouseY, final float parTicks) {
        for (final IGuiElement element : elements) {
            element.draw(mouseX, mouseY, parTicks);
        }
    }

    private int getContentY() {
        return y + contentOffsetY() + getGuiTop();
    }

    private int getContentX() {
        return x + contentOffsetX() + getGuiLeft();
    }

    private Vector2 pseudoPanelStart() {
        return effectiveScroll();
    }

    private double getMaxScrollFactor(int scrollSize, int totalSize) {
        if (scrollSize >= totalSize || totalSize == 0) {
            return 0.0;
        }
        return 1 - (double) scrollSize / totalSize;
    }

    private Vector2 getAsProportionOfContent(int x, int y) {
        val result = new Vector2(0.0, 0.0);
        if (totalBounds.width > 0) {
            result.x = (double) x / (double) totalBounds.width;
        }
        if (totalBounds.height > 0) {
            result.y = (double) y / (double) totalBounds.height;
        }
        return result;
    }

    private Vector2 pseudoPanelEnd() {
        val scroll = effectiveScroll();
        val size = effectiveWindowSize();
        return scroll.add(size);
    }

}
