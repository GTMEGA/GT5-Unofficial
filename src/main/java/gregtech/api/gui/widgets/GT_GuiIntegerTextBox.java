package gregtech.api.gui.widgets;


import gregtech.api.interfaces.IGuiScreen;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

import java.awt.*;


public class GT_GuiIntegerTextBox extends GuiTextField implements IGuiScreen.IGuiElement {

    public final int id;

    private final int x0, y0;

    private final IGuiScreen gui;

    @Getter
    private boolean enabled;

    private GT_GuiTooltip tooltip = null;

    @Getter
    @Setter
    private int updateCooldown = 0;

    @Accessors(chain = true)
    @Getter
    @Setter
    private IGT_GuiHook onInitBehavior = null, onUpdateBehavior = null, onClickBehavior = null;

    public GT_GuiIntegerTextBox(IGuiScreen gui, int id, int x, int y, int width, int height) {
        super(Minecraft.getMinecraft().fontRenderer, x, y, width, height);
        super.setText("");
        this.id = id;
        x0 = x;
        y0 = y;
        this.gui = gui;
        enabled = true;
        gui.addElement(this);
    }

    @Override
    public void onInit() {
        xPosition = x0 + gui.getGuiLeft();
        yPosition = y0 + gui.getGuiTop();
        onInit(this.gui, 0, 0, 0);
    }

    @Override
    public void draw(int mouseX, int mouseY, float parTicks) {
        super.drawTextBox();
        onUpdate(this.gui, mouseX, mouseY, 0);
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
    public boolean textboxKeyTyped(char c, int key) {
        if (validChar(c, key) || c == 1 || c == 3 || c == 22 || c == 24 || key == 14 || key == 199 || key == 203 || key == 205 || key == 207 || key == 211) {
            return super.textboxKeyTyped(c, key);
        }
        return false;
    }

    /**
     * Draws the textbox
     */
    @Override
    public void drawTextBox() {
        super.drawTextBox();
    }

    @Override
    public void setEnabled(boolean p_146184_1_) {
        super.setEnabled(p_146184_1_);
        enabled = p_146184_1_;
    }

    public boolean validChar(char c, int key) {
        return Character.isDigit(c);
    }

}
