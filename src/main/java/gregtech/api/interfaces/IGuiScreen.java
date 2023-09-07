package gregtech.api.interfaces;


import gregtech.api.gui.widgets.GT_GuiTooltip;
import lombok.val;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.RenderItem;

import java.awt.*;


public interface IGuiScreen {

    interface IGuiElement {

        interface IGT_GuiHook {

            void action(final IGuiScreen screen, final IGuiElement button, final int mouseX, final int mouseY, final int clickType);

        }

        void onInit();

        void draw(int mouseX, int mouseY, float parTicks);

        GT_GuiTooltip getTooltip();

        IGuiElement setTooltipText(String... text);

        int getUpdateCooldown();

        void setUpdateCooldown(final int val);

        boolean inBounds(final int mouseX, final int mouseY, final int clickType);

        default void onInit(IGuiScreen screen, int mouseX, int mouseY, final int clickType) {
            final IGT_GuiHook action = getOnInitBehavior();
            if (action != null) {
                action.action(screen, this, mouseX, mouseY, clickType);
            }
        }

        IGT_GuiHook getOnInitBehavior();

        IGuiElement setOnInitBehavior(final IGT_GuiHook hook);

        default void onUpdate(IGuiScreen screen, int mouseX, int mouseY, final int clickType) {
            val action = getOnUpdateBehavior();
            if (action != null && getUpdateCooldown() <= 0) {
                action.action(screen, this, mouseX, mouseY, clickType);
            }
            if (getUpdateCooldown() > 0) {
                setUpdateCooldown(getUpdateCooldown() - 1);
            } else {
                setUpdateCooldown(0);
            }
        }

        IGT_GuiHook getOnUpdateBehavior();

        IGuiElement setOnUpdateBehavior(IGT_GuiHook hook);

        default void onClick(IGuiScreen screen, int mouseX, int mouseY, final int clickType) {
            final IGT_GuiHook hook = getOnClickBehavior();
            if (hook != null) {
                hook.action(screen, this, mouseX, mouseY, clickType);
            }
        }

        IGT_GuiHook getOnClickBehavior();

        IGuiElement setOnClickBehavior(IGT_GuiHook hook);

        Rectangle getBounds();

    }

    void addToolTip(GT_GuiTooltip toolTip);

    boolean removeToolTip(GT_GuiTooltip toolTip);

    GuiButton getSelectedButton();

    void clearSelectedButton();

    void buttonClicked(GuiButton button);

    int getGuiLeft();

    int getGuiTop();

    int getXSize();

    int getYSize();

    void addElement(IGuiElement element);

    boolean removeElement(IGuiElement element);

    RenderItem getItemRenderer();

    FontRenderer getFontRenderer();

}
