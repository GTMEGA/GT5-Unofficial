package gregtech.common.gui.dev;


import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Wrapper_Icon;
import gregtech.api.gui.widgets.icon.GT_GuiIcon;
import gregtech.api.interfaces.IAdvancedGUIEntity;
import gregtech.api.interfaces.IDWSCompatible;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.net.GT_Packet_TileEntityGUI;
import gregtech.api.util.IAdvancedTEData;
import lombok.Getter;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import org.apache.logging.log4j.Level;

import static gregtech.GT_Mod.GT_FML_LOGGER;


@Getter
public abstract class GT_Container_Dev<MachineType extends MetaTileEntity & IAdvancedGUIEntity, DataType extends IAdvancedTEData> extends GT_ContainerMetaTile_Machine implements IDWSCompatible {

    protected final DataType data;

    protected final MachineType machine;

    protected boolean valid = false;

    public GT_Container_Dev(
            final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity, Class<MachineType> machineKlass, Class<DataType> dataKlass
                           ) {
        super(aInventoryPlayer, aTileEntity);
        MachineType tempMachine = null;
        DataType    tempData    = null;
        try {
            tempMachine = machineKlass.cast(getMetaTileEntity());
            tempData    = dataKlass.cast(tempMachine.getTEGUIData());
            valid       = true;
        } catch (ClassCastException e) {
            GT_FML_LOGGER.printf(Level.ERROR, "Could not cast container properly, please scream at me");
            // throw new RuntimeException(e);
        }
        data    = tempData;
        machine = tempMachine;
        detectAndSendChanges();
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (mTileEntity == null || mTileEntity.getMetaTileEntity() == null || mTileEntity.isClientSide() || data == null) {
            return;
        }
        data.detectAndSendChanges(this, crafters);
    }

    /**
     * @param changeID
     * @param data
     */
    @Override
    public void updateProgressBar(final int changeID, final int data) {
        super.updateProgressBar(changeID, data);
        if (mTileEntity == null || this.data == null) {
            return;
        }
        if (shouldReceiveLiveServerUpdates()) {
            this.data.receiveChange(changeID, data);
        }
    }

    public boolean shouldReceiveLiveServerUpdates() {
        return true;
    }

    public MachineType getSource() {
        return machine;
    }

    /**
     * @param aInventoryPlayer
     * @param slotIndex
     * @param x
     * @param y
     *
     * @return
     */
    @Override
    protected Slot createPlayerSlot(final InventoryPlayer aInventoryPlayer, final int slotIndex, final int x, final int y) {
        return new GT_Slot_Wrapper_Icon(super.createPlayerSlot(aInventoryPlayer, slotIndex, x, y), GT_GuiIcon.PLAYER_INV_SLOT, GT_GuiIcon.PLAYER_INV_SLOT_HIGHLIGHT);
    }

    protected void sendPacket() {
        // TODO Figure this out
        GT_Values.NW.sendToServer(GT_Packet_TileEntityGUI.createFromMachine(machine, data, mPlayerInventory.player.dimension));
    }

}
