package gregtech.api.interfaces;


import com.google.common.io.ByteArrayDataInput;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.util.ISerializableObject;


/**
 * Allows the use of GUI packets for TEs
 */
public interface IAdvancedGUIEntity extends IMetaTileEntity {

    /**
     * Receive and accept the packet
     */
    void receiveGuiData(final ISerializableObject data);

    /**
     * Decodes the packet, machine type specific
     */
    ISerializableObject decodePacket(ByteArrayDataInput aData);

    /**
     * Dumps the relevant data from the TE
     */
    ISerializableObject getTEGUIData();

    default void onNeighborBlockChange(final int x, final int y, final int z) {
        onNeighborChange(x, y, z);
    }

    void onNeighborChange(final int x, final int y, final int z);

    default boolean shouldServerReceiveLiveUpdates() {
        return true;
    }

}
