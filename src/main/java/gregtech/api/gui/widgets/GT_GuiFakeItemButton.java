package gregtech.api.gui.widgets;

import codechicken.lib.gui.GuiDraw;
import gregtech.api.gui.widgets.icon.GT_GuiIcon;
import gregtech.api.interfaces.IGuiScreen;
import gregtech.api.util.GT_UtilityClient;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.util.List;

public class GT_GuiFakeItemButton implements IGT_GuiButton {

    private GT_GuiIcon bgIcon;
    private ItemStack item;
    private IGuiScreen gui;
    private int xPosition, yPosition;
    private java.util.List<String> itemTooltips;
    private final GT_GuiTooltip tooltip = new GT_GuiTooltip(null) {
        @Override
        public List<String> getToolTipText() {
            return itemTooltips;
        }

        @Override
        public boolean isDelayed() {
            return false;
        }

        @Override
        public Rectangle getBounds() {
            return GT_GuiFakeItemButton.this.getBounds();
        }
    };
    private final Rectangle rectangle;
    private boolean mimicSlot;

    @Accessors(chain = true)
    @Getter
    @Setter
    private IGT_GuiHook onInitBehavior, onClickBehavior, onUpdateBehavior;

    @Getter
    @Setter
    private int updateCooldown = 0;

    public GT_GuiFakeItemButton(IGuiScreen gui, int x, int y, GT_GuiIcon bgIcon) {
        this.gui = gui;
        this.bgIcon = bgIcon;
        item = null;
        rectangle  = new Rectangle(x, y, 18, 18);
        gui.addElement(this);
    }

    public GT_GuiFakeItemButton setItem(ItemStack i) {
        item = i;
        if (getMimicSlot())
            updateTooltip();
        return this;
    }

    private void updateTooltip() {
        itemTooltips = item == null ? null :  GT_UtilityClient.getTooltip(item, true);
    }

    public ItemStack getItem(){
        return item;
    }

    public GT_GuiFakeItemButton setMimicSlot(boolean mimicSlot) {
        if (mimicSlot != this.mimicSlot) {
            if (mimicSlot) {
                updateTooltip();
                gui.addToolTip(tooltip);
            } else {
                gui.removeToolTip(tooltip);
            }
            this.mimicSlot = mimicSlot;
        }
        return this;
    }

    public boolean getMimicSlot() {
        return mimicSlot;
    }

    public GT_GuiIcon getBgIcon() {
        return bgIcon;
    }

    public GT_GuiFakeItemButton setBgIcon(GT_GuiIcon bgIcon) {
        this.bgIcon = bgIcon;
        return this;
    }

    @Override
    public void onInit() {
        xPosition = rectangle.x + gui.getGuiLeft();
        yPosition = rectangle.y + gui.getGuiTop();
    }

    @Override
    public void onRemoved() {
        if (mimicSlot)
            gui.removeToolTip(tooltip);
    }

    @Override
    public void draw(int mouseX, int mouseY, float parTicks) {
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (bgIcon != null){
            bgIcon.render(xPosition-1, yPosition-1, 18, 18,0,true);
        }

        if (item != null) {
            if (item.getItem() instanceof ItemBlock) {
                GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            }
            gui.getItemRenderer().renderItemAndEffectIntoGUI(gui.getFontRenderer(), Minecraft.getMinecraft().getTextureManager(), item, xPosition, yPosition);
            GL11.glColor4f(1, 1, 1, 1);
            if (item.getItem() instanceof ItemBlock) {
                GL11.glPopAttrib();
            }
        }


        if (getMimicSlot())
            if (getBounds().contains(mouseX - gui.getGuiLeft(), mouseY - gui.getGuiTop())) {
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glColorMask(true, true, true, false);
                GuiDraw.drawGradientRect(xPosition, yPosition, 16, 16, 0x80ffffff, 0x80ffffff);
                GL11.glColorMask(true, true, true, true);
                // no glEnable, state will be recovered by glPopAttrib
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

    public Rectangle getBounds() {
        return rectangle;
    }

    public void setX(int x) {
        rectangle.x = x;
    }

    public void setY(int y) {
        rectangle.y = y;
    }

    public void setWidth(int width) {
        rectangle.width = width;
    }

    public void setHeight(int height) {
        rectangle.height = height;
    }
}
