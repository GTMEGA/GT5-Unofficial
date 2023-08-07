package gregtech.api.util.interop;


import net.minecraft.client.gui.inventory.GuiContainer;


/**
 * This serves as a method to allow access to NEI functionality safely, without requiring it to be present
 * */
public abstract class NEIInteropBase {

    public void preDraw(final GuiContainer container) {

    }

    public void renderObjects(final GuiContainer container, final int mouseX, final int mouseY) {

    }

    public void renderTooltips(final GuiContainer container, final int mouseX, final int mouseY) {

    }

    public boolean checkSlotUnderMouse(final GuiContainer container, final int mouseX, final int mouseY) {
        return false;
    }

}
