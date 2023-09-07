package gregtech.common.gui.remotedetonator;


import gregtech.api.gui.widgets.GT_GuiScrollPanel;
import gregtech.api.gui.widgets.GT_GuiTooltip;
import gregtech.api.interfaces.IGuiScreen;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;
import net.minecraft.client.gui.Gui;

import java.awt.*;


@Getter
public class RemoteDetonator_GuiEntry implements GT_GuiScrollPanel.IScrollableElement {

    private final GT_GuiScrollPanel<GT_RemoteDetonator_GuiContainer> scrollPanel;

    private GT_GuiTooltip tooltip;

    private String[] tooltipText;

    @Accessors(chain = true)
    @Setter
    int scrollID = 0;

    @Setter
    private int updateCooldown = 0;

    @Getter
    @Setter
    private boolean canRender = false;

    @Setter
    private int renderX, renderY;

    @Accessors(chain = true)
    @Setter
    private IGT_GuiHook onInitBehavior, onUpdateBehavior, onClickBehavior;

    public RemoteDetonator_GuiEntry(final GT_GuiScrollPanel<GT_RemoteDetonator_GuiContainer> scrollPanel) {
        this.scrollPanel = scrollPanel;
    }

    /**
     * @return
     */
    @Override
    public int getScrollWidth() {
        return scrollPanel.getScrollWidth();
    }

    /**
     * @return
     */
    @Override
    public int getScrollHeight() {
        return 12;
    }

    /**
     *
     */
    @Override
    public void onInit() {

    }

    /**
     * @param screen
     * @param mouseX
     * @param mouseY
     * @param clickType
     */
    @Override
    public void onUpdate(final IGuiScreen screen, final int mouseX, final int mouseY, final int clickType) {
        // GT_GuiScrollPanel.IScrollableElement.super.onUpdate(screen, mouseX, mouseY, clickType);
    }

    /**
     * @param mouseX
     * @param mouseY
     * @param parTicks
     */
    @Override
    public void draw(final int mouseX, final int mouseY, final float parTicks) {
        val bg = new Color(0xFF, 0xFF, 0xFF, 0xFF);
        val fg = new Color(0x00, 0x00, 0x00, 0x7F);
        Gui.drawRect(getRenderX(), getRenderY(), getRenderX() + getScrollWidth(), getRenderY() + getScrollHeight(), bg.getRGB());
        Gui.drawRect(getRenderX() + 1, getRenderY() + 1, getRenderX() + getScrollWidth() - 1, getRenderY() + getScrollHeight() - 1, fg.getRGB());
        if (queryCanRender()) {
            scrollPanel.drawString(scrollPanel.getFontRenderer(), String.format("Test %d", scrollID), renderX + 1, renderY + 1, Color.WHITE.getRGB());
        }
    }

    /**
     * @param text
     * @return
     */
    @Override
    public IGuiScreen.IGuiElement setTooltipText(final String... text) {
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
        return canRender && getBounds().contains(mouseX, mouseY);
    }

    /**
     * @return
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(renderX, renderY, getScrollWidth(), getScrollHeight());
    }

}
