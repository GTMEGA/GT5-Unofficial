package gregtech.common.gui.remotedetonator;


import gregtech.api.gui.widgets.GT_GuiScrollPanel;
import gregtech.api.gui.widgets.GT_GuiTooltip;
import gregtech.api.interfaces.IGuiScreen;
import gregtech.common.blocks.explosives.GT_Block_Explosive;
import gregtech.common.items.explosives.GT_RemoteDetonator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.val;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;


@ToString(onlyExplicitlyIncluded = true)
@Getter
public class RemoteDetonator_GuiEntry implements GT_GuiScrollPanel.IScrollableElement {

    private static final Map<GT_Block_Explosive, ItemStack> stackCache = new HashMap<>();

    private final GT_GuiScrollPanel<GT_RemoteDetonator_GuiContainer> scrollPanel;

    private final GT_RemoteDetonator.RemoteDetonationTargetList targetList;

    @ToString.Include private final GT_RemoteDetonator.RemoteDetonationTargetList.Target target;

    @Accessors(chain = true)
    @Setter
    int scrollID = 0;

    private GT_GuiTooltip tooltip;

    private String[] tooltipText;

    @Setter private int updateCooldown = 0;

    @Setter private boolean canRender = false;

    @Setter private int renderX, renderY;

    @Setter private boolean valid = true;

    @Accessors(chain = true)
    @Setter
    private IGT_GuiHook onInitBehavior, onUpdateBehavior, onClickBehavior;

    public RemoteDetonator_GuiEntry(final GT_GuiScrollPanel<GT_RemoteDetonator_GuiContainer> scrollPanel, final GT_RemoteDetonator.RemoteDetonationTargetList targetList, final GT_RemoteDetonator.RemoteDetonationTargetList.Target target) {
        this.scrollPanel = scrollPanel;
        this.targetList = targetList;
        this.target = target;
        scrollPanel.addScrollableElement(this);
    }

    /**
     *
     */
    @Override
    public void onInit() {
        onInit(scrollPanel, 0, 0, 0);
    }

    /**
     * Go to {@link GT_RemoteDetonator.RemoteDetonationTargetList.ExplosiveType} To change colors
     *
     * @param mouseX
     * @param mouseY
     * @param parTicks
     */
    @Override
    public void draw(final int mouseX, final int mouseY, final float parTicks) {
        if (canRender) {
            val bump = isHoveredEntry() ? 1 : 0;
            val rX = getRenderX();
            val rY = getRenderY();
            val sW = getScrollWidth();
            val sH = getScrollHeight();
            val rectXStart = rX + bump;
            val rectXEnd = rX + sW - bump;
            val rectYStart = rY + bump;
            val rectYEnd = rY + sH - bump;
            final Color color;
            if (isValid()) {
                if (!isSelectedEntry()) {
                    color = target.getExplosiveType().getBackgroundColorNormal();
                } else {
                    color = target.getExplosiveType().getBackgroundColorSelected();
                }
            } else {
                color = new Color(255, 0, 0, 255);
            }
            if (isHoveredEntry()) {
                Gui.drawRect(rX, rY, rX + sW, rY + sH, new Color(255, 255, 255, 255).getRGB());
            }
            Gui.drawRect(rectXStart, rectYStart, rectXEnd, rectYEnd, color.getRGB());
            val string = String.format("(%d, %d, %d)", target.getX(), target.getY(), target.getZ());
            val stringWidth = scrollPanel.getFontRenderer().getStringWidth(string);
            GL11.glPushMatrix();
            scrollPanel.getParent().drawItemStack(stackCache.computeIfAbsent(target.getExplosiveType().getExplosive(), ItemStack::new), rX + 20, rY + 3, "");
            GL11.glPopMatrix();
            scrollPanel.drawString(scrollPanel.getFontRenderer(), String.format("#%d", scrollID + 1), rX + 5, rY + 5, Color.white.getRGB());
            scrollPanel.drawString(scrollPanel.getFontRenderer(), string, rX + sW - (stringWidth + 5), rY + 5, target.getExplosiveType().getTextColor().getRGB());
            onUpdate(scrollPanel, mouseX, mouseY, 0);
        } else {
            if (isHoveredEntry()) {
                scrollPanel.getParent().setHoveredEntry(null);
            }
        }
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
        return 20;
    }

    @Override
    public void receiveClick(final int mouseX, final int mouseY, final int mouseButton) {
        onClick(scrollPanel, mouseX, mouseY, mouseButton);
    }

    private boolean isSelectedEntry() {
        return getSelectedEntry() == this;
    }

    private RemoteDetonator_GuiEntry getSelectedEntry() {
        return scrollPanel.getParent().getSelectedEntry();
    }

    /**
     * @param text
     *
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
     *
     * @return
     */
    @Override
    public boolean inBounds(final int mouseX, final int mouseY, final int clickType) {
        return canRender && getBounds().contains(mouseX, mouseY);
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
     * @return
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(renderX, renderY, getScrollWidth(), getScrollHeight());
    }

    private boolean isHoveredEntry() {
        return getHoveredEntry() == this;
    }

    private RemoteDetonator_GuiEntry getHoveredEntry() {
        return scrollPanel.getParent().getHoveredEntry();
    }

}
