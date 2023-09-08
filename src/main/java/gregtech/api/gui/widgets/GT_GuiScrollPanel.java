package gregtech.api.gui.widgets;


import gregtech.api.interfaces.IGuiScreen;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;
import lombok.var;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.entity.RenderItem;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.*;
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


    // TODO: Remove
    private static final Random TEST_REMOVE = new Random();


    private final ParentType parent;

    private final int id;

    private final int x;

    private final int y;

    @Setter
    private double fuzz = 0.1;

    private final Map<Integer, IScrollableElement> scrollableElements = new HashMap<>();

    private final List<IGuiElement> elements = new ArrayList<>();

    private final GT_GuiTooltipManager tooltipManager = new GT_GuiTooltipManager();

    private final int scrollWidth;

    private final int scrollHeight;

    private final int myHeight;

    private final int myWidth;

    @Setter
    private Color panelBackground = new Color(19, 19, 19, 0xFF);

    @Setter
    private Color panelInteriorBackground = new Color(65, 65, 65, 255);

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
        Gui.drawRect(x + getGuiLeft(), y + getGuiTop(), x + getGuiLeft() + myWidth, y + getGuiTop() + myHeight, panelBackground.getRGB());
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
        return parent.getGuiLeft() + x;
    }

    /**
     * @return
     */
    @Override
    public int getGuiTop() {
        return parent.getGuiTop() + y;
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
        val start = pseudoRenderStart() - fuzz;
        val end = pseudoRenderEnd() + fuzz;
        return /* true ||  */(pseudoY >= start && pseudoY <= end) || (elemEnd >= start && elemEnd <= end);
    }

    public int contentOffsetX() {
        return (myWidth - scrollWidth) / 2;
    }

    public int contentOffsetY() {
        return (myHeight - scrollHeight) / 2;
    }

    public double getResolutionScale() {
        int factor = 1;
        while (mc.displayWidth / (factor) >= 320 && mc.displayHeight / (factor) >= 240) {
            factor++;
        }
        return (double) 1 / factor;
    }

    protected void drawScrollableContent(final int mouseX, final int mouseY, final float parTicks) {
        val scaled = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight).getScaleFactor();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        //
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        //
        GL11.glScissor(getContentX() * scaled, mc.displayHeight - ((getContentY() + scrollHeight) * scaled), scrollWidth * scaled, scrollHeight * scaled);
        drawScrollPanelBackground(mouseX, mouseY, parTicks);
        //
        var currentHeightTotal = 0;
        var currentHeightRendered = 0;
        val keys = scrollableElements.keySet().stream().sorted().collect(Collectors.toList());
        for (final Integer key : keys) {
            val element = scrollableElements.get(key);
            val elementPseudoY = getRenderHeightStart(getYAsProportionOfScrollHeight(currentHeightTotal));
            val elementPseudoHeight = getYAsProportionOfScrollHeight(element.getScrollHeight());
            if (inRange(elementPseudoY, elementPseudoHeight)) {
                val verticalOffset = (int) (currentHeightTotal - totalHeight * pseudoRenderStart());
                element.setRenderX(getContentX());
                element.setRenderY(getContentY() + verticalOffset);
                element.setCanRender(true);
                element.draw(mouseX, mouseY, parTicks);
                currentHeightRendered += verticalOffset;
            }
            currentHeightTotal += element.getScrollHeight();
        }
        //
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        //
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    protected void drawScrollPanelBackground(final int mouseX, final int mouseY, final float parTicks) {
        Gui.drawRect(getContentX(), getContentY(), getContentX() + scrollWidth, getContentY() + scrollHeight, panelInteriorBackground.getRGB());
    }

    protected void drawRegularContent(final int mouseX, final int mouseY, final float parTicks) {
        for (final IGuiElement element : elements) {
            element.draw(mouseX, mouseY, parTicks);
        }
    }

    private int getScaledScrollHeight() {
        return (int) (scrollHeight / getScaledHeight());
    }

    private double getScaledHeight() {
        return (double) myHeight / height;
    }

    private int getScaledScrollWidth() {
        return (int) (scrollWidth / getScaledWidth());
    }

    private double getScaledWidth() {
        return (double) myWidth / width;
    }

    private int getContentY() {
        return y + contentOffsetY() + getGuiTop();
    }

    private int getContentX() {
        return x + contentOffsetX() + getGuiLeft();
    }

    private double getRenderHeightEnd(final double allElementHeight, final IScrollableElement element) {
        return getRenderHeightStart(allElementHeight) + element.getScrollHeight();
    }

    private double getRenderHeightStart(final double allElementHeight) {
        return allElementHeight - pseudoRenderStart();
    }

    private double pseudoRenderStart() {
        val scroll = effectiveScroll();
        val height = effectiveWindowHeight();
        return Math.min(height - scroll, scroll);
    }

    public double effectiveScroll() {
        return currentScroll * (effectiveTotalContentHeight() - effectiveWindowHeight());
    }

    public double effectiveWindowHeight() {
        if (totalHeight <= scrollHeight || totalHeight == 0 || scrollHeight == 0) {
            return 1.0;
        }
        return (double) scrollHeight / totalHeight;
    }

    public double effectiveTotalContentHeight() {
        return 1.0;
        /* if (totalHeight <= scrollHeight || totalHeight == 0 || scrollHeight == 0) {
            return 1.0;
        }
        return totalHeight / (double) (scrollHeight); */
    }

    private double getYAsProportionOfScrollHeight(final int y) {
        if (scrollHeight <= 0) {
            return 0.0;
        }
        return (double) y / (double) (scrollHeight);
    }

    private double getYAsProportionOfTotalHeight(final int y) {
        if (totalHeight <= 0) {
            return 0.0;
        }
        return (double) y / (double) (totalHeight);
    }

    private double pseudoRenderEnd() {
        val totalHeight = effectiveTotalContentHeight();
        val height = effectiveWindowHeight();
        val scroll = effectiveScroll();
        return Math.min(1.0, scroll + totalHeight);
    }

}
