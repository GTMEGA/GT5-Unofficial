package gregtech.api.gui.widgets;


import gregtech.api.interfaces.IGuiScreen;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;
import lombok.var;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


@Getter
public class GT_GuiScrollPanel<ParentType extends GuiScreen & IGuiScreen> extends GuiScreen implements IGuiScreen.IGuiElement, IGuiScreen {

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

        GT_GuiScrollPanel<?> getScrollPanel();

        int getScrollID();

        IScrollableElement setScrollID(int scrollID);

    }


    private final ParentType parent;

    private final int id;

    private final int x;

    private final int y;

    private final Map<Integer, IScrollableElement> scrollableElements = new HashMap<>();

    private final List<IGuiElement> elements = new ArrayList<>();

    private final GT_GuiTooltipManager tooltipManager = new GT_GuiTooltipManager();

    private final int scrollWidth;

    private final int scrollHeight;

    private final int myHeight;

    private final int myWidth;

    private int totalHeight = 0;

    private int lastID = 0;

    @Setter
    private RenderItem itemRenderer = null;

    @Setter
    private FontRenderer fontRenderer = null;

    @Setter
    private int updateCooldown = 0;

    private double currentScroll;

    private GT_GuiTooltip tooltip;

    @Accessors(chain = true)
    @Setter
    private IGT_GuiHook onUpdateBehavior, onInitBehavior, onClickBehavior;

    private String[] tooltipText;

    @Setter
    private GuiButton selectedButton = null;

    public GT_GuiScrollPanel(
            ParentType parent, int id, int x, int y, int width, int height, double currentScroll, final int scrollWidth, final int scrollHeight
                            ) {
        super();
        this.id = id;
        this.parent = parent;
        this.x = x + parent.getGuiLeft();
        this.y = y + parent.getGuiTop();
        setWorldAndResolution(parent.mc, parent.width, parent.height);
        this.myWidth = width;
        this.myHeight = height;
        this.currentScroll = currentScroll;
        this.scrollWidth = scrollWidth;
        this.scrollHeight = scrollHeight;
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
        val bg = new Color(19, 19, 19, 0xFF);
        val fg = new Color(0x00, 0x00, 0x00, 0x7F);
        Gui.drawRect(x, y, x + myWidth, y + myHeight, bg.getRGB());
        Gui.drawRect(x, y, x + contentOffsetX() + scrollWidth, y + contentOffsetY() + scrollHeight, fg.getRGB());
        drawString(getFontRenderer(), String.format("Scroll: %.2f",  currentScroll), x + contentOffsetX() + myWidth + 10, y + contentOffsetY(), 0xFFFFFF);
        drawString(getFontRenderer(), String.format("Window start: %.2f",  pseudoRenderStart()), x + contentOffsetX() + myWidth + 10, y + contentOffsetY() + 15, 0xFFFFFF);
        drawString(getFontRenderer(), String.format("Window end: %.2f",  pseudoRenderEnd()), x + contentOffsetX() + myWidth + 10, y + contentOffsetY() + 30, 0xFFFFFF);
    }

    public void setCurrentScroll(final double currentScroll) {
        this.currentScroll = currentScroll;
        if (this.currentScroll < 0) {
            this.currentScroll = 0;
        }
        if (this.currentScroll > 1) {
            this.currentScroll = 1;
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
        for (final IScrollableElement element : scrollableElements.values()) {
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
        GL11.glTranslatef(parent.getGuiLeft(), parent.getGuiTop(), 0);
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
        return x;
    }

    /**
     * @return
     */
    @Override
    public int getGuiTop() {
        return y;
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

    public void addScrollableElement(final IScrollableElement element) {
        val id = getNextID();
        totalHeight += element.getScrollHeight();
        scrollableElements.put(id, element.setScrollID(id));
    }

    private int getNextID() {
        var last = lastID;
        while (scrollableElements.containsKey(last)) {
            last += 1;
        }
        return lastID = last;
    }

    public IScrollableElement removeScrollableElement(final IScrollableElement element) {
        var last = lastID - 1;
        while (!scrollableElements.containsKey(last)) {
            last -= 1;
        }
        lastID = last;
        totalHeight -= element.getScrollHeight();
        return scrollableElements.remove(element.getScrollID());
    }

    public boolean inRange(final double pseudoY, final double elementPseudoHeight) {
        val elemEnd = pseudoY + elementPseudoHeight;
        return pseudoY > pseudoRenderStart() && pseudoY < pseudoRenderEnd() && elemEnd > pseudoRenderStart() && elemEnd < pseudoRenderEnd();
    }

    protected void drawScrollableContent(final int mouseX, final int mouseY, final float parTicks) {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        //
        /* GL11.glClearColor(0, 0, 0, 1);
        GL11.glClearStencil(0);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
        GL11.glStencilMask(0xFF);
        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF); */
        //
        var currentHeightTotal = 0;
        var currentHeightRendered = 0;
        val keys = scrollableElements.keySet().stream().sorted().collect(Collectors.toList());
        for (final Integer key: keys) {
            val element = scrollableElements.get(key);
            val elementPseudoY = getRenderHeightStart(pseudoY(currentHeightTotal));
            val elementPseudoHeight = pseudoY(element.getScrollHeight());
            if (inRange(elementPseudoY, elementPseudoHeight)) {
                val verticalOffset = (int)(currentHeightRendered /* + element.getScrollHeight() */ - totalHeight * pseudoRenderStart());
                element.setRenderX(x + getGuiLeft() + contentOffsetX());
                element.setRenderY(y + getGuiTop() + contentOffsetY() + verticalOffset);
                element.setCanRender(true);
                element.draw(mouseX, mouseY, parTicks);
                currentHeightRendered += verticalOffset;
            }
            currentHeightTotal += element.getScrollHeight();
        }
        //
        /* GL11.glDisable(GL11.GL_STENCIL_TEST); */
        //
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    protected void drawRegularContent(final int mouseX, final int mouseY, final float parTicks) {
        for (final IGuiElement element : elements) {
            element.draw(mouseX, mouseY, parTicks);
        }
    }

    private double getRenderHeightStart(final double allElementHeight) {
        return allElementHeight - pseudoRenderStart();
    }

    private double getRenderHeightEnd(final double allElementHeight, final IScrollableElement element) {
        return getRenderHeightStart(allElementHeight) + element.getScrollHeight();
    }

    private double pseudoY(final int y) {
        if (totalHeight <= 0) {
            return 0.0;
        }
        return (double) y / (double) (totalHeight);
    }

    public double renderWindowEffectiveHeight() {
        if (totalHeight <= scrollHeight || totalHeight == 0) {
            return 1.0;
        }
        return scrollHeight / (double) totalHeight;
    }

    private double pseudoRenderStart() {
        return Math.max(0.0, currentScroll);
    }

    private double pseudoRenderEnd() {
        return Math.min(1.0, currentScroll + renderWindowEffectiveHeight());
    }

    public int contentOffsetX() {
        return (myWidth - scrollWidth) / 2;
    }

    public int contentOffsetY() {
        return (myHeight - scrollHeight) / 2;
    }

}
