package gregtech.common.gui.remotedetonator;


import gregtech.api.gui.widgets.GT_GuiScrollPanel;
import gregtech.api.gui.widgets.GT_GuiTooltip;
import gregtech.api.gui.widgets.icon.GT_GuiIcon;
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

    @ToString.Include
    private final GT_RemoteDetonator.RemoteDetonationTargetList.Target target;

    private final Color baseTextColor = new Color(119, 119, 119, 255);

    @Accessors(chain = true)
    @Setter
    int scrollID = 0;

    private GT_GuiTooltip tooltip;

    private String[] tooltipText;

    @Setter
    private int updateCooldown = 0;

    @Setter
    private boolean canRender = false;

    @Setter
    private int renderX, renderY;

    @Setter
    private boolean validBlock = true;

    @Setter
    private boolean blockPrimed = true;

    @Accessors(chain = true)
    @Setter
    private IGT_GuiHook onInitBehavior, onUpdateBehavior, onClickBehavior;

    @Setter
    private double factor;

    public RemoteDetonator_GuiEntry(final GT_GuiScrollPanel<GT_RemoteDetonator_GuiContainer> scrollPanel, final GT_RemoteDetonator.RemoteDetonationTargetList targetList, final GT_RemoteDetonator.RemoteDetonationTargetList.Target target) {
        this.scrollPanel = scrollPanel;
        this.targetList = targetList;
        this.target = target;
        scrollPanel.addScrollableElement(this);
        setFactor((double) Math.min(getScrollWidth(), getScrollHeight()) / (itemSize() + 2));
    }

    /**
     * @return
     */
    @Override
    public int getScrollWidth() {
        return scrollPanel.getScrollWidth();
    }

    private static int itemSize() {
        return 18;
    }

    /**
     * @return
     */
    @Override
    public int getScrollHeight() {
        return 30;
    }

    @Override
    public void receiveClick(final int mouseX, final int mouseY, final int mouseButton) {
        onClick(scrollPanel, mouseX, mouseY, mouseButton);
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
            val bump = isHoveredEntry() ? 2 : 1;
            val rX = getRenderX();
            val rY = getRenderY();
            val sW = getScrollWidth();
            val sH = getScrollHeight();
            val rectXStart = rX + bump;
            val rectXEnd = rX + sW - bump;
            val rectYStart = rY + bump;
            val rectYEnd = rY + sH - bump;
            val textY = rY + (sH - 10) / 2;
            final Color color;
            if (isValidBlock()) {
                if (!isSelectedEntry()) {
                    color = target.getExplosiveType().getBackgroundColor();
                } else {
                    color = target.getExplosiveType().getBackgroundColor().darker();
                }
            } else {
                color = new Color(128, 0, 0, 255);
            }
            if (isHoveredEntry()) {
                Gui.drawRect(rX, rY, rX + sW, rY + sH, lighter(color).getRGB());
            } else {
                Gui.drawRect(rX, rY, rX + sW, rY + sH, color.darker().getRGB());
            }
            val textColor = inverse(color).darker();
            Gui.drawRect(rectXStart, rectYStart, rectXEnd, rectYEnd, color.getRGB());
            val idString = String.format("#%d", scrollID + 1);
            val idStringWidth = scrollPanel.getFontRenderer().getStringWidth(idString);
            val idStringX = rX + 5;
            val posString = String.format("(%d, %d, %d)", target.getX(), target.getY(), target.getZ());
            val posStringWidth = scrollPanel.getFontRenderer().getStringWidth(posString);
            val posStringX = rX + sW - (posStringWidth + 5);
            val itemIconX = (getScrollWidth() - (idStringWidth + posStringWidth + itemSize() + 2)) / 2 + idStringWidth;
            renderBlock(itemIconX, rY + 3);
            drawString(idString, idStringX, textY, textColor);
            drawString(posString, posStringX, textY, textColor);
            onUpdate(scrollPanel, mouseX, mouseY, 0);
        } else {
            if (isHoveredEntry()) {
                scrollPanel.getParent().setHoveredEntry(null);
            }
        }
    }

    public Color lighter(final Color other) {
        return new Color((int) Math.min(other.getRed() / 0.7, 255), (int) Math.min(other.getGreen() / 0.7, 255), (int) Math.min(other.getBlue() / 0.7, 255), other.getAlpha());
    }

    public void drawString(final String text, final int x, final int y, final Color color) {
        scrollPanel.getFontRenderer().drawString(text, x, y, color.getRGB());
    }

    public Color inverse(final Color color) {
        return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue(), color.getAlpha());
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
        val inside = getBounds().contains(mouseX, mouseY);
        return canRender && inside;
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

    private boolean isSelectedEntry() {
        return getSelectedEntry() == this;
    }

    private void renderBlock(int rX, int rY) {
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        //
        /* rX = (int) (rX / factor);
        rY = (int) (rY / factor); */
        GL11.glTranslatef((float)(scrollPanel.getScrollPanelLeft() + rX), (float) rY, 0.0F);
        GL11.glScalef((float) factor, (float) factor, 1.0F);
        if (isBlockPrimed()) {
            GL11.glDisable(GL11.GL_LIGHTING);
        }
        if (isValidBlock()) {
            scrollPanel.getParent().drawItemStack(stackCache.computeIfAbsent(target.getExplosiveType().getExplosive(), ItemStack::new), 0, 0, "");
        } else {
            GT_GuiIcon.CROSS.render(0, 0, 16, 16, 0, true);
        }
        //
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    private RemoteDetonator_GuiEntry getHoveredEntry() {
        return scrollPanel.getParent().getHoveredEntry();
    }

    private RemoteDetonator_GuiEntry getSelectedEntry() {
        return scrollPanel.getParent().getSelectedEntry();
    }

}
