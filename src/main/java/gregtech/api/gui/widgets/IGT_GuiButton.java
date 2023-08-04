package gregtech.api.gui.widgets;


import gregtech.api.interfaces.IGuiScreen;


public interface IGT_GuiButton extends IGuiScreen.IGuiElement {

    interface IGT_GuiButtonHook {

        void action(final IGuiScreen screen, final IGT_GuiButton button, final int mouseX, final int mouseY);

    }

    default void onUpdate(final IGuiScreen screen, final int mouseX, final int mouseY) {
        final IGT_GuiButtonHook action = getOnUpdateBehavior();
        if (action != null) {
            action.action(screen, this, mouseX, mouseY);
        }
    }

    default IGT_GuiButtonHook getOnUpdateBehavior() {
        return null;
    }

    default IGT_GuiButton setOnUpdateBehavior(final IGT_GuiButtonHook hook) {
        return this;
    }

    default void onClick(final IGuiScreen screen, final int mouseX, final int mouseY) {
        final IGT_GuiButtonHook hook = getOnClickBehavior();
        if (hook != null) {
            hook.action(screen, this, mouseX, mouseY);
        }
    }

    default IGT_GuiButtonHook getOnClickBehavior() {
        return null;
    }

    default IGT_GuiButton setOnClickBehavior(final IGT_GuiButtonHook hook) {
        return this;
    }

}
