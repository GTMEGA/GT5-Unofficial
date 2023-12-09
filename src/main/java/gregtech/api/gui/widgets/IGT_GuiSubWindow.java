package gregtech.api.gui.widgets;


import gregtech.api.interfaces.IGuiScreen;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;


public interface IGT_GuiSubWindow extends IGuiScreen, IGuiScreen.IGuiElement {

    void receiveClick(final int mouseX, final int mouseY, final int mouseButton);

    void receiveDrag(int mouseX, int mouseY, int lastClick);

    void receiveMouseMovement(int mouseX, int mouseY, int clickState);

    default int getMCScaleFactor() {
        val mc = getMinecraftInstance();
        return new ScaledResolution(mc, mc.displayWidth, mc.displayHeight).getScaleFactor();
    }

    Minecraft getMinecraftInstance();

}
