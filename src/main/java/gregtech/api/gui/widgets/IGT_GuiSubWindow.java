package gregtech.api.gui.widgets;

import gregtech.api.interfaces.IGuiScreen;

public interface IGT_GuiSubWindow extends IGuiScreen, IGuiScreen.IGuiElement {

    void receiveClick(final int mouseX, final int mouseY, final int mouseButton);

}
