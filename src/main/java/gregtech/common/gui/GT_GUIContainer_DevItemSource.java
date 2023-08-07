package gregtech.common.gui;


import gregtech.api.gui.GT_GUIContainer_Machine_Plus;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;


public class GT_GUIContainer_DevItemSource extends GT_GUIContainer_Machine_Plus {

    public GT_GUIContainer_DevItemSource(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
        super(new GT_Container_DevItemSource(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/DevItemSource.png", 256, 166);
    }

    /**
     * @param mouseX
     * @param mouseY
     * @param parTicks
     */
    @Override
    public void drawExtras(final int mouseX, final int mouseY, final float parTicks) {

    }

    /**
     * @return Whether to draw slots
     */
    @Override
    protected boolean autoDrawSlots() {
        return true;
    }

}
