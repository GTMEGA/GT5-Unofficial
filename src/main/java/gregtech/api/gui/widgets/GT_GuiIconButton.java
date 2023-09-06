package gregtech.api.gui.widgets;

import gregtech.api.interfaces.IGuiScreen;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;
import java.awt.Rectangle;

public class GT_GuiIconButton extends GuiButton implements IGT_GuiButton {
    public static final int DEFAULT_WIDTH = 16;
    public static final int DEFAULT_HEIGHT = 16;

    public IGT_GuiHook onClick = null, onInit = null, onUpdate = null;

    @Getter
    protected GT_GuiIcon icon;
    private int x0, y0;
    protected IGuiScreen gui;
    private String[] tooltipText;

    private GT_GuiTooltip tooltip;

    private int updateCooldown = 0;

    /**
     * @return
     */
    @Override
    public IGT_GuiHook getOnUpdateBehavior() {
        return onUpdate;
    }

    /**
     * @param hook
     * @return
     */
    @Override
    public IGuiScreen.IGuiElement setOnUpdateBehavior(final IGT_GuiHook hook) {
        this.onUpdate = hook;
        return IGT_GuiButton.super.setOnUpdateBehavior(hook);
    }

    public GT_GuiIconButton(IGuiScreen gui, int id, int x, int y, GT_GuiIcon icon) {
        super(id, x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, "");
        this.gui = gui;
        this.icon = icon;
        this.x0 = x;
        this.y0 = y;
        gui.addElement(this);
    }

    @Override
    public void onInit() {
        if (tooltip != null) {
            gui.addToolTip(tooltip);
        }
        xPosition = x0 + gui.getGuiLeft();
        yPosition = y0 + gui.getGuiTop();
    }

    @Override
    public void draw(int mouseX, int mouseY, float parTicks) {
        drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.tooltip != null) {
            this.tooltip.enabled = true;
        }

        if (this.visible) {
            //moused over
            this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + width && mouseY < this.yPosition + height;

            mouseDragged(mc, mouseX, mouseY);

            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            int x = xPosition;
            int y = yPosition;
            if(!this.field_146123_n) {
            //    GL11.glColor4f(200F/255F, 210F/255F, 1, 1);
            }
            else
                GL11.glColor4f(1, 1, 1, 1);

            GT_GuiIcon.render(getButtonTexture(this.field_146123_n), x, y, width, height, 0, true);

            GL11.glColor4f(1, 1, 1, 1);
            if (icon != null) {
                GT_GuiIcon.render(icon, x, y, width, height , 0, true);
            }

            GL11.glPopAttrib();
        }
        onUpdate(this.gui, mouseX, mouseY, 0);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        this.gui.clearSelectedButton();
        if(mousePressed(Minecraft.getMinecraft(), mouseX, mouseY)) {
            this.gui.buttonClicked(this);
            this.onClick(this.gui, mouseX, mouseY, 1);
        }
    }

    public GT_GuiIcon getButtonTexture(boolean mouseOver) {
        if (!enabled)
            return GT_GuiIcon.BUTTON_DISABLED;
        if (this.equals(this.gui.getSelectedButton()))
            return mouseOver ? GT_GuiIcon.BUTTON_HIGHLIGHT_DOWN : GT_GuiIcon.BUTTON_DOWN;

        return mouseOver ? GT_GuiIcon.BUTTON_HIGHLIGHT : GT_GuiIcon.BUTTON_NORMAL;
    }

    public GT_GuiIconButton setIcon(GT_GuiIcon icon) {
        this.icon = icon;
        return this;
    }

    @Override
    public GT_GuiTooltip getTooltip() {
        return tooltip;
    }

    @Override
    public IGuiScreen.IGuiElement setTooltipText(String... text) {
        if (tooltip == null)
            tooltip = new GT_GuiTooltip(getBounds(), text);
        else
            tooltip.setToolTipText(text);
        this.tooltipText = text;
        return this;
    }

    public Rectangle getBounds() {
        return new Rectangle(x0, y0, width, height);
    }

    /**
     * @return
     */
    @Override
    public IGT_GuiHook getOnClickHook() {
        return onClick;
    }

    /**
     * @param hook
     * @return
     */
    @Override
    public IGuiScreen.IGuiElement setOnClickHook(final IGT_GuiHook hook) {
        this.onClick = hook;
        return IGT_GuiButton.super.setOnClickHook(hook);
    }

    /**
     * @return
     */
    @Override
    public int getUpdateCooldown() {
        return updateCooldown;
    }

    /**
     * @param val
     */
    @Override
    public void setUpdateCooldown(final int val) {
        this.updateCooldown = val;
    }

}
