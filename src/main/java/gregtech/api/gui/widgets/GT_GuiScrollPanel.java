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
public class GT_GuiScrollPanel<ParentType extends GuiScreen & IGuiScreen> extends GuiScreen implements IGT_GuiSubWindow {

    public interface IScrollableElement extends IGuiElement {

        int getScrollWidth();

        int getScrollHeight();

        boolean isCanRender();

        void setCanRender(final boolean canRender);

        int getRenderX();

        void setRenderX(final int renderX);

        int getRenderY();

        void setRenderY(final int renderY);

        GT_GuiScrollPanel<?> getScrollPanel();

        int getScrollID();

        IScrollableElement setScrollID(int scrollID);

        boolean isZebra();

        void setZebra(final boolean zebra);

        void receiveClick(final int mouseX, final int mouseY, final int mouseButton);

    }


    // TODO: Remove
    private static final Random TEST_REMOVE = new Random();


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

    @Setter
    private Color panelBackground = new Color(19, 19, 19, 0xFF);

    @Setter
    private Color panelInteriorBackground = new Color(65, 65, 65, 255);

    private int totalHeight = 0;

    private int lastID = 0, maxID = -1;

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
        Gui.drawRect(getScrollPanelLeft(), getScrollPanelTop(), getScrollPanelRight(), getScrollPanelBottom(), panelBackground.getRGB());
/*         drawString(getFontRenderer(), String.format("Start: %.2f", pseudoPanelStart()), x + getGuiLeft() + myWidth, y + getGuiTop(), Color.WHITE.getRGB());
        drawString(getFontRenderer(), String.format("End: %.2f", pseudoPanelEnd()), x + getGuiLeft() + myWidth, y + getGuiTop() + 15, Color.WHITE.getRGB());
        drawString(getFontRenderer(), String.format("Effective: %.2f", effectiveScroll()), x + getGuiLeft() + myWidth, y + getGuiTop() + 30, Color.WHITE.getRGB());
        drawString(getFontRenderer(), String.format("Current: %.2f", currentScroll), x + getGuiLeft() + myWidth, y + getGuiTop() + 45, Color.WHITE.getRGB());
        drawString(getFontRenderer(), String.format("Max: %.2f", getMaxScrollFactor()), x + getGuiLeft() + myWidth, y + getGuiTop() + 60, Color.WHITE.getRGB()); */
    }

    public int getScrollPanelBottom() {
        return getScrollPanelTop() + myHeight;
    }

    public int getScrollPanelRight() {
        return getScrollPanelLeft() + myWidth;
    }

    public int getScrollPanelTop() {
        return y + getGuiTop();
    }

    public int getScrollPanelLeft() {
        return x + getGuiLeft();
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
        if (last > maxID) {
            maxID = last;
        }
        return lastID = last;
    }

    @Override
    public void receiveClick(final int mouseX, final int mouseY, final int mouseButton) {
        val pseudoX = mouseX + parent.getGuiLeft();
        val pseudoY = mouseY + parent.getGuiTop();
        val keys = scrollableElements.keySet().stream().sorted().collect(Collectors.toList());
//            System.out.println("Click: " + mouseX + ", " + mouseY);
//            System.out.println("Adjusted: " + (mouseX - getContentX()) + ", " + (mouseY - getContentY()));
        for (val key : keys) {
            val element = scrollableElements.get(key);
//                System.out.println("Element X: " + element.getRenderX() + ", Y: " + element.getRenderY() + ", Height: " + element.getScrollHeight() + ", Width: " + element.getScrollWidth());
            if (element.inBounds(pseudoX, pseudoY, mouseButton)) {
//                    System.out.println("Got element: " + element);
                element.receiveClick(pseudoX, pseudoY, mouseButton);
                break;
            }
        }
    }

    public IScrollableElement removeScrollableElement(final IScrollableElement element) {
        var last = lastID - 1;
        while (!scrollableElements.containsKey(last)) {
            if (last <= 0) {
                last = maxID + 1;
                break;
            }
            last -= 1;
        }
        lastID = last;
        totalHeight -= element.getScrollHeight();
        return scrollableElements.remove(element.getScrollID());
    }

    public boolean inRange(final int elementHeight, final int netHeight) {
        val start = pseudoPanelStart();
        val end = pseudoPanelEnd();
        val elemStart = getAsProportionOfContent(netHeight);
        val pseudoHeight = getAsProportionOfContent(elementHeight);
        val elemEnd = elemStart + pseudoHeight;
        return (elemStart >= start || elemEnd >= start) && (elemStart <= end || elemEnd <= end);
    }

    public int contentOffsetX() {
        return (myWidth - scrollWidth) / 2;
    }

    public int contentOffsetY() {
        return (myHeight - scrollHeight) / 2;
    }

    public int getMCScaleFactor() {
        return new ScaledResolution(mc, mc.displayWidth, mc.displayHeight).getScaleFactor();
    }

    public double effectiveScroll() {
        return Math.max(0.0, Math.min(1.0, currentScroll)) * getMaxScrollFactor();
    }

    public double getMaxScrollFactor() {
        if (scrollHeight >= totalHeight || totalHeight == 0) {
            return 0.0;
        }
        return 1 - (double) scrollHeight / totalHeight;
    }

    public double effectiveWindowHeight() {
        double height = (double) scrollHeight / totalHeight;
        if (height > 1) {
            height = 1;
        }
        return height;
    }

    public int getContentY() {
        return y + contentOffsetY() + getGuiTop();
    }

    public int getContentX() {
        return x + contentOffsetX() + getGuiLeft();
    }

    protected void drawScrollableContent(final int mouseX, final int mouseY, final float parTicks) {
        val scaled = getMCScaleFactor();
        //
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(getContentX() * scaled, mc.displayHeight - ((getContentY() + scrollHeight) * scaled), scrollWidth * scaled, scrollHeight * scaled);
        //
        drawScrollPanelBackground(mouseX, mouseY, parTicks);
        var totalElementHeight = 0;
        val keys = scrollableElements.keySet().stream().sorted().collect(Collectors.toList());
        int index = 0;
        for (final Integer key : keys) {
            val element = scrollableElements.get(key);
            renderIndividualScrollableElement(index, mouseX, mouseY, parTicks, totalElementHeight, element);
            totalElementHeight += element.getScrollHeight();
            index += 1;
        }
        //
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    protected void renderIndividualScrollableElement(final int index, final int mouseX, final int mouseY, final float parTicks, final int totalElementHeight, final IScrollableElement element) {
        if (inRange(element.getScrollHeight(), totalElementHeight)) {
            val verticalOffset = (int) (totalElementHeight - totalHeight * pseudoPanelStart());
            val myX = getContentX();
            val myY = getContentY() + verticalOffset;
            element.setRenderX(myX);
            element.setRenderY(myY);
            element.setZebra(index % 2 == 0);
            element.setCanRender(true);
            element.draw(mouseX, mouseY, parTicks);
        } else {
            element.setCanRender(false);
        }
    }

    protected void drawScrollPanelBackground(final int mouseX, final int mouseY, final float parTicks) {
        Gui.drawRect(getContentX(), getContentY(), getContentX() + scrollWidth, getContentY() + scrollHeight, panelInteriorBackground.getRGB());
    }

    protected void drawRegularContent(final int mouseX, final int mouseY, final float parTicks) {
        for (final IGuiElement element : elements) {
            element.draw(mouseX, mouseY, parTicks);
        }
    }

    private double pseudoPanelStart() {
        return effectiveScroll();
    }

    private double getAsProportionOfContent(final int y) {
        if (totalHeight <= 0) {
            return 0.0;
        }
        return (double) y / (double) (totalHeight);
    }

    private double pseudoPanelEnd() {
        val height = effectiveWindowHeight();
        val scroll = effectiveScroll();
        return scroll + height;
    }

}
