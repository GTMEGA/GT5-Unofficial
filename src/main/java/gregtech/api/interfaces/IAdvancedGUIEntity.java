package gregtech.api.interfaces;


import com.google.common.io.ByteArrayDataInput;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.net.GT_Packet_TileEntityGUI;
import gregtech.api.util.ISerializableObject;
import net.minecraft.client.entity.EntityClientPlayerMP;


/**
 * Allows the use of GUI packets for TEs
 * */
public interface IAdvancedGUIEntity extends IMetaTileEntity {

    /**
     * Receive and accept the packet
     * */
    void receiveGuiData(final ISerializableObject data, final EntityClientPlayerMP player);

    /**
     * Decodes the packet, machine type specific
     * */
    ISerializableObject decodePacket(ByteArrayDataInput aData);

}
