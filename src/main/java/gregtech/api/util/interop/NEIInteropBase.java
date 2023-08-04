package gregtech.api.util.interop;


import net.minecraft.client.gui.inventory.GuiContainer;


public abstract class NEIInteropBase {

    public void preDraw(final GuiContainer container) {
        // Do nothing by design
    }

    public void renderObjects(final GuiContainer container, final int mouseX, final int mouseY) {

    }

    public void renderTooltips(final GuiContainer container, final int mouseX, final int mouseY) {

    }

    public boolean checkSlotUnderMouse(final GuiContainer container, final int mouseX, final int mouseY) {
        return false;
    }

}
