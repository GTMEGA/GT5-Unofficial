package gregtech.common.gui;

import gregtech.api.gui.widgets.GT_GuiButtonNumber;
import gregtech.common.net.MessageSetIntegratedCircuit;
import gregtech.loaders.misc.NetworkDispatcher;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GT_GUIContainer_IntegratedCircuit extends GuiContainer {

    private static final ResourceLocation BACKGROUND = new ResourceLocation("gregtech:textures/gui/Botworks_Circuitprogrammer.png");
    //the gui seems to be a few pixels to much to the left
    private static int guiOffset = 24;

    private GT_Container_IntegratedCircuit container;

    public GT_GUIContainer_IntegratedCircuit(GT_Container_IntegratedCircuit container) {
        super(container);
        this.container = container;
    }

    @Override
    public void initGui() {
        super.initGui();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int value = j * 5 + i;
                buttonList.add(new GT_GuiButtonNumber(0, value, guiLeft + 16 + i * 20 + guiOffset, guiTop + 16 + j * 22, 16, 20, "" + value));
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        mc.getTextureManager().bindTexture(BACKGROUND);
        drawTexturedModalRect(guiLeft + 24, guiTop, 0, 0, xSize, ySize);
    }


    @Override
    protected void actionPerformed(GuiButton btn) {
        if (btn instanceof GT_GuiButtonNumber) {
            int newValue = ((GT_GuiButtonNumber) btn).getValue();
            container.circuit.setItemDamage(newValue);
            NetworkDispatcher.INSTANCE.sendToServer(new MessageSetIntegratedCircuit((byte) newValue,true));
            mc.thePlayer.closeScreen();
        }
    }
}
