package gregtech.api.gui.widgets;


import gregtech.api.interfaces.IGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;


public class GT_GuiFakeItemButton implements IGT_GuiButton {

    private final GT_GuiIcon bgIcon;

    private ItemStack item;

    private final IGuiScreen gui;

    private final int x0;

    private final int y0;

    private int xPosition;

    private int yPosition;

    private final int width;

    private final int height;

    private GT_GuiTooltip tooltip = null;

    public GT_GuiFakeItemButton(IGuiScreen gui, int x, int y, GT_GuiIcon bgIcon) {
        this.gui = gui;
        this.x0 = x;
        this.y0 = y;
        this.bgIcon = bgIcon;
        item = null;
        width = 18;
        height = 18;
        gui.addElement(this);
    }

    public ItemStack getItem() {
        return item;
    }

    public GT_GuiFakeItemButton setItem(ItemStack i) {
        item = i;
        return this;
    }

    @Override
    public void onInit() {
        xPosition = x0 + gui.getGuiLeft();
        yPosition = y0 + gui.getGuiTop();
    }

    @Override
    public void draw(int mouseX, int mouseY, float parTicks) {
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (bgIcon != null) {
            GT_GuiIcon.render(bgIcon, xPosition - 1, yPosition - 1, 18, 18, 0, true);
        }

        if (item != null) {
            gui.getItemRenderer().renderItemAndEffectIntoGUI(gui.getFontRenderer(), Minecraft.getMinecraft().getTextureManager(), item, xPosition, yPosition);
        }

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
        return this;
    }

    public Rectangle getBounds() {
        return new Rectangle(x0, y0, width, height);
    }

}
