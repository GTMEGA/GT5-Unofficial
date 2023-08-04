package gregtech.common.gui;


import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUIContainer_Machine_Plus;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconButton;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.gui.widgets.GT_GuiSlider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GT_Packet_TileEntityGUI;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;

import static gregtech.api.enums.GT_Values.VN;


public class GT_GUIContainer_DevEnergySource extends GT_GUIContainer_Machine_Plus {

    public GT_GUIContainer_DevEnergySource(
            final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity
                                          ) {
        super(new GT_Container_DevEnergySource(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/DevEnergySource.png", 256, 256);
        addGUIElements();
    }

    private void addGUIElements() {
        new GT_GuiIconCheckButton(this, 0, 8, 8, GT_GuiIcon.CHECKMARK, GT_GuiIcon.CROSS, "Enable Activity", "Disable Activity").setChecked(!getSource().isEnabled());
        new GT_GuiIconButton(this, 1, 8, 24, GT_GuiIcon.MATH_PLUS).setOnClickBehavior((screen, button, mouseX, mouseY) -> {
            if (screen instanceof GT_GUIContainer_DevEnergySource) {
                final GT_GUIContainer_DevEnergySource source = (GT_GUIContainer_DevEnergySource) screen;
                button.setTooltipText("Increases Amperage by 1", String.format("Will increase to %d", source.getSource().getAmperage() + 1));
            }
        }).setTooltipText("Increases Amperage by 1", String.format("Will increase to %d", getSource().getAmperage() + 1));
        new GT_GuiIconButton(this, 2, 8, 40, GT_GuiIcon.MATH_MINUS).setOnClickBehavior((screen, button, mouseX, mouseY) -> {
            if (screen instanceof GT_GUIContainer_DevEnergySource) {
                final GT_GUIContainer_DevEnergySource source = (GT_GUIContainer_DevEnergySource) screen;
                button.setTooltipText("Decreases Amperage by 1", String.format("Will decrease to %d", Math.max(source.getSource().getAmperage() - 1, 0)));
            }
        }).setTooltipText("Decreases Amperage by 1", String.format("Will decrease to %d", Math.max(getSource().getAmperage() - 1, 0)));
        new GT_GuiIconButton(this, 3, 8, 56, GT_GuiIcon.MATH_ZERO).setTooltipText("Sets amperage and voltage to 0");
        addElement(new GT_GuiSlider(this, 32, 56, 32, 8, 0.0, 32.0, 8.0));
    }

    public GT_Container_DevEnergySource getSource() {
        return (GT_Container_DevEnergySource) mContainer;
    }

    /**
     * @param guiLeft
     * @param guiTop
     * @param width
     * @param height
     */
    @Override
    protected void onInitGui(final int guiLeft, final int guiTop, final int width, final int height) {
        super.onInitGui(guiLeft, guiTop, width, height);
    }

    /**
     * @param mouseX
     * @param mouseY
     * @param parTicks
     */
    @Override
    public void drawExtras(final int mouseX, final int mouseY, final float parTicks) {
        this.fontRendererObj.drawString(String.format("Voltage: %s", VN[getSource().getTier()]), 32, 8, 0xFF555555);
        this.fontRendererObj.drawString(String.format("Amperage: %d", getSource().getAmperage()), 32, 24, 0xFF555555);
        if (!getSource().isEnabled()) {
            this.fontRendererObj.drawString("Disabled", 32, 40, rgbaToInt(0xFF, 0x55, 0x55, 0xFF));
        }
    }

    /**
     * @param button Handler for a button click
     */
    @Override
    public void buttonClicked(final GuiButton button) {
        super.buttonClicked(button);
        if (button.id == 0) {
            GT_GuiIconCheckButton checkButton = (GT_GuiIconCheckButton) button;
            getSource().getEnergySource().toggleEnabled();
            checkButton.toggle();
        } else if (button.id == 1) {
            getSource().getEnergySource().bumpAmperage(1);
        } else if (button.id == 2) {
            getSource().getEnergySource().bumpAmperage(-1);
        } else if (button.id == 3) {
            getSource().getEnergySource().zeroOut();
        }
        GT_Values.NW.sendToServer(GT_Packet_TileEntityGUI.createFromMachine(getSource().getEnergySource(), getDimension()));
        updateButtons();
        clearSelectedButton();

    }

    private void updateButtons() {
        GuiButton button;
        for (Object o: buttonList) {
            button = (GuiButton) o;
            button.enabled = true;
        }
    }

}
