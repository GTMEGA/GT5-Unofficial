package gregtech.common.gui.dev;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.dev.GT_MetaTileEntity_DevFluidSource;
import gregtech.api.net.GT_Packet_TileEntityGUI;
import net.minecraft.entity.player.InventoryPlayer;

public class GT_Container_DevFluidSource extends GT_ContainerMetaTile_Machine {

    private GT_MetaTileEntity_DevFluidSource.GUIData data;

    public GT_Container_DevFluidSource(final InventoryPlayer aInventoryPlayer,
                                       final IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    protected void sendPacket() {
        final int dimension = mPlayerInventory.player.dimension;
        GT_Values.NW.sendToServer(GT_Packet_TileEntityGUI.createFromMachine(getSource(), data, dimension));
    }

    public GT_MetaTileEntity_DevFluidSource getSource() {
        return (GT_MetaTileEntity_DevFluidSource) mTileEntity;
    }

}
