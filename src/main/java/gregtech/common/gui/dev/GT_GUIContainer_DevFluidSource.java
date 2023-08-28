package gregtech.common.gui.dev;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_GUIContainer_Machine_Plus;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

public class GT_GUIContainer_DevFluidSource extends GT_GUIContainer_Machine_Plus {

    public GT_GUIContainer_DevFluidSource(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
        super(new GT_Container_DevFluidSource(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/DevFluidSource.png", 256, 166);
        addGUIElements();
    }

    private void addGUIElements() {

    }

    @Override
    public void sendUpdateToServer() {

    }

    @Override
    public void drawExtras(final int mouseX, final int mouseY, final float parTicks) {
        getSource().sendPacket();
    }

    public GT_Container_DevFluidSource getSource() {
        return (GT_Container_DevFluidSource) mContainer;
    }

    @Override
    public int getDWSWidthBump() {
        return 82;
    }

}
