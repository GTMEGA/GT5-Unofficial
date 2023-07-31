package gregtech.common.gui;


import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUIContainer_Machine_Plus;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconButton;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GT_Packet_TileEntityGUI;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;


public class GT_GUIContainer_DevEnergySource extends GT_GUIContainer_Machine_Plus {

    public GT_GUIContainer_DevEnergySource(
            final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity
                                          ) {
        super(new GT_Container_DevEnergySource(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/DevEnergySource.png");
        addGUIElements();
    }

    private void addGUIElements() {
        new GT_GuiIconCheckButton(this, 0, 8, 8, GT_GuiIcon.CHECKMARK, GT_GuiIcon.CROSS).setChecked(getSource().isEnabled());
        new GT_GuiIconButton(this, 1, 8, 16, GT_GuiIcon.MATH_PLUS);
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
        }
        GT_Values.NW.sendToServer(GT_Packet_TileEntityGUI.createFromMachine(getSource().getEnergySource()));
    }

}
