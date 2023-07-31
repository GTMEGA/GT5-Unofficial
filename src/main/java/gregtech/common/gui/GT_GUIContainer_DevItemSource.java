package gregtech.common.gui;


import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;


public class GT_GUIContainer_DevItemSource extends GT_GUIContainerMetaTile_Machine {

    public GT_GUIContainer_DevItemSource(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity, "gregtech:textures/gui/DevItemSource.png");
    }

}
