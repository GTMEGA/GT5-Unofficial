package gregtech.api.gui.widgets;


import gregtech.api.interfaces.IGuiScreen;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
public class GT_CanvasPanel<ParentType extends GuiScreen & IGuiScreen> extends GuiScreen implements IGT_GuiSubWindow {

    public interface IDraggableElement extends IGuiElement {

        default void onPositionChange() {
            if (getOnPositionChangeBehavior() != null) {
                getOnPositionChangeBehavior().action(getCurrentCanvas(), this, getRenderX(), getRenderY(), 0);
            }
        }

        IGT_GuiHook getOnPositionChangeBehavior();

        IDraggableElement setOnPositionChangeBehavior(final IGT_GuiHook hook);

        GT_CanvasPanel<?> getCurrentCanvas();

        int getRenderX();

        void setRenderX(final int x);

        int getRenderY();

        void setRenderY(final int y);

        boolean canClip();

        void setCanClip(final boolean clip);

        int getElementWidth();

        void setElementWidth(final int width);

        int getElementHeight();

        void setElementHeight(final int height);

        default boolean isSelected() {
            return getCurrentCanvas().getCurrentDraggableElement() == this;
        }

        boolean isXUnlocked();

        void setXUnlocked(final boolean unlocked);

        boolean isYUnlocked();

        void setYUnlocked(final boolean unlocked);

        default boolean processDrag(final int mouseX, final int mouseY, final int lastClick) {
            val canvas = getCurrentCanvas();
            if (canvas != null) {
                val pX = mouseX + canvas.getGuiLeft();
                val pY = mouseY + canvas.getGuiTop();
                if (inMouseBounds(pX, pY, lastClick)) {
                    receiveDrag(pX, pY, lastClick);
                    return true;
                }
            }
            return false;
        }

        default boolean inMouseBounds(final int mouseX, final int mouseY, final int mouseButton) {
            return getMouseBounds().contains(mouseX, mouseY);
        }

        void receiveDrag(final int mouseX, final int mouseY, final int lastClick);

        Rectangle getMouseBounds();

        default boolean processClick(final int mouseX, final int mouseY, final int mouseButton) {
            val canvas = getCurrentCanvas();
            if (canvas != null) {
                val pX = mouseX + canvas.getGuiLeft();
                val pY = mouseY + canvas.getGuiTop();
                if (inMouseBounds(pX, pY, mouseButton)) {
                    receiveClick(pX, pY, mouseButton);
                    return true;
                }
            }
            return false;
        }

        void receiveClick(final int mouseX, final int mouseY, final int mouseButton);

        default boolean processMouseMovement(final int mouseX, final int mouseY, final int clickState) {
            val canvas = getCurrentCanvas();
            if (canvas != null) {
                val pX = mouseX + canvas.getGuiLeft();
                val pY = mouseY + canvas.getGuiTop();
                if (inMouseBounds(pX, pY, clickState)) {
                    receiveMouseMovement(pX, pY, clickState);
                    return true;
                }
            }
            return false;
        }

        void receiveMouseMovement(final int mouseX, final int mouseY, final int clickState);

    }


    private final ParentType parent;

    private final int id, x, y;

    private final int totalWidth, totalHeight, contentWidth, contentHeight;

    private final List<IGuiElement> elements = new ArrayList<>();

//    private final List<IDraggableElement> draggableElements = new ArrayList<>();

    private final Map<Rectangle, IDraggableElement> draggableElementMap = new HashMap<>();

    private final GT_GuiTooltipManager tooltipManager = new GT_GuiTooltipManager();

    @Accessors(chain = true)
    @Setter
    private IDraggableElement currentDraggableElement = null;

    @Setter
    private Color bgColor = new Color(19, 19, 19, 0xFF);

    @Setter
    private Color fgColor = new Color(65, 65, 65, 255);

    @Setter
    private RenderItem itemRenderer = null;

    @Setter
    private FontRenderer fontRenderer = null;

    @Setter
    private int updateCooldown = 0;

    private GT_GuiTooltip tooltip;

    @Accessors(chain = true)
    @Setter
    private IGT_GuiHook onUpdateBehavior, onInitBehavior, onClickBehavior;

    private String[] tooltipText;

    @Setter
    private GuiButton selectedButton = null;

    public GT_CanvasPanel(ParentType parent, int id, int x, int y, int totalWidth, int totalHeight, int contentWidth, int contentHeight) {
        super();
        this.parent = parent;
        this.id = id;
        this.x = x + parent.getGuiLeft();
        this.y = y + parent.getGuiTop();
        setWorldAndResolution(parent.mc, parent.width, parent.height);
        this.totalWidth = totalWidth;
        this.totalHeight = totalHeight;
        this.contentWidth = contentWidth;
        this.contentHeight = contentHeight;
        parent.addElement(this);
    }

    /**
     * @param mc
     * @param width
     * @param height
     */
    @Override
    public void setWorldAndResolution(final Minecraft mc, final int width, final int height) {
        this.mc = mc;
        this.width = width;
        this.height = height;
    }

    /**
     * @param alwaysZero
     */
    @Override
    public void drawBackground(final int alwaysZero) {
        Gui.drawRect(getContentLeft(), getContentTop(), getContentRight(), getContentBottom(), bgColor.getRGB());
    }

    public void addDraggableElement(final IDraggableElement element) {
        /* draggableElements.add(element); */
        draggableElementMap.put(element.getBounds(), element);
    }

    public boolean removeDraggableElement(final IDraggableElement element) {
        return /* draggableElements.remove(element) &&  */draggableElementMap.remove(element.getBounds()) != null;
    }

    /**
     * @param mouseX
     * @param mouseY
     * @param mouseButton
     */
    @Override
    public void receiveClick(final int mouseX, final int mouseY, final int mouseButton) {
        if (currentDraggableElement == null || !currentDraggableElement.processClick(mouseX, mouseY, mouseButton)) {
            boolean found = false;
            for (val element : draggableElementMap.values()) {
                if (element.processClick(mouseX, mouseY, mouseButton)) {
                    currentDraggableElement = element;
                    found = true;
                    break;
                }
            }
            if (!found) {
                currentDraggableElement = null;
            }
        }
    }

    /**
     * @param mouseX
     * @param mouseY
     * @param lastClick
     */
    @Override
    public void receiveDrag(final int mouseX, final int mouseY, final int lastClick) {
        if (currentDraggableElement == null || !currentDraggableElement.processDrag(mouseX, mouseY, lastClick)) {
            boolean found = false;
            for (val element : draggableElementMap.values()) {
                if (element.processDrag(mouseX, mouseY, lastClick)) {
                    currentDraggableElement = element;
                    found = true;
                    break;
                }
            }
            if (!found) {
                currentDraggableElement = null;
            }
        }
        dragElement(mouseX, mouseY, lastClick);
//        onDrag(mouseX, mouseY, lastClick);
    }

    private void dragElement(final int mouseX, final int mouseY, final int lastClick) {
        if (currentDraggableElement == null) {
            return;
        }
        val pX = mouseX + parent.getGuiLeft();
        val pY = mouseY + parent.getGuiTop();
        val deltaMX = pX - currentDraggableElement.getRenderX();
        val deltaMY = pY - currentDraggableElement.getRenderY();
        val newX = currentDraggableElement.isXUnlocked() ? (currentDraggableElement.getRenderX() + deltaMX) : currentDraggableElement.getRenderX();
        val newY = currentDraggableElement.isYUnlocked() ? (currentDraggableElement.getRenderY() + deltaMY) : currentDraggableElement.getRenderY();
        // TODO: Clipping with other elements
        val myRect = getBounds();
        val oldRect = currentDraggableElement.getBounds();
        val newRect = new Rectangle(newX, newY, oldRect.width, oldRect.height);
        if (currentDraggableElement.canClip() || myRect.contains(newRect)) {
            currentDraggableElement.setRenderX(newX);
            currentDraggableElement.setRenderY(newY);
        } else {
            if (newRect.getMaxX() > myRect.getMaxX()) {
                currentDraggableElement.setRenderX((int) (myRect.getMaxX() - oldRect.getWidth()));
            } else if (newRect.getMinX() < myRect.getMinX()) {
                currentDraggableElement.setRenderX((int) myRect.getMinX());
            }
            if (newRect.getMaxY() > myRect.getMaxY()) {
                currentDraggableElement.setRenderY((int) (myRect.getMaxY() - oldRect.getHeight()));
            } else if (newRect.getMinY() < myRect.getMinY()) {
                currentDraggableElement.setRenderY((int) myRect.getMinY());
            }
            currentDraggableElement.onPositionChange();
        }
    }

    /* public void onDrag(final int pX, final int pY, final int lastClick) {

    } */

    /**
     * @param mouseX
     * @param mouseY
     * @param clickState
     */
    @Override
    public void receiveMouseMovement(final int mouseX, final int mouseY, final int clickState) {
        if (currentDraggableElement == null || !currentDraggableElement.processMouseMovement(mouseX, mouseY, clickState)) {
            boolean found = false;
            for (val element : draggableElementMap.values()) {
                if (element.processMouseMovement(mouseX, mouseY, clickState)) {
                    currentDraggableElement = element;
                    found = true;
                    break;
                }
            }
            /* if (!found) {
                currentDraggableElement = null;
            } */
        }
    }

    /**
     * @return
     */
    @Override
    public Minecraft getMinecraftInstance() {
        return mc;
    }

    /**
     *
     */
    @Override
    public void onInit() {
        if (tooltip != null) {
            addToolTip(tooltip);
        }
        for (val element : elements) {
            element.onInit();
        }
        for (val element : draggableElementMap.values()) {
            element.onInit();
        }
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
        for (val element : elements) {
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
        /* if (element instanceof IDraggableElement) {
            draggableElements.add((IDraggableElement) element);
        } */
    }

    /**
     * @param element
     * @return
     */
    @Override
    public boolean removeElement(final IGuiElement element) {
        return elements.remove(element);
    }

    public RenderItem getItemRenderer() {
        return itemRenderer != null ? itemRenderer : parent.getItemRenderer();
    }

    public FontRenderer getFontRenderer() {
        return fontRenderer != null ? fontRenderer : parent.getFontRenderer();
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
        //
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    public int getContentLeft() {
        return x + getGuiLeft();
    }

    public int getContentTop() {
        return y + getGuiTop();
    }

    public int getContentRight() {
        return getContentLeft() + contentWidth;
    }

    public int getContentBottom() {
        return getContentTop() + contentHeight;
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

    /**
     * @return
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

}
