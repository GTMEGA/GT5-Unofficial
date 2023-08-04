package gregtech.api.gui.widgets;


import gregtech.api.interfaces.IGuiScreen;


public interface IGT_GuiButton extends IGuiScreen.IGuiElement {

    interface IGT_GuiButtonHook {

        void onClick(final IGuiScreen screen, final IGT_GuiButton button, final int mouseX, final int mouseY);

    }

    default void onClick(final IGuiScreen screen, final int mouseX, final int mouseY) {
        final IGT_GuiButtonHook hook = getOnClickBehavior();
        if (hook != null) {
            hook.onClick(screen, this, mouseX, mouseY);
        }
    }

    default IGT_GuiButtonHook getOnClickBehavior() {
        return null;
    }

    default IGT_GuiButton setOnClickBehavior(final IGT_GuiButtonHook hook) {
        return this;
    }

}
