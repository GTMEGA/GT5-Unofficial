package gregtech.common;

import eu.usrv.yamcore.network.PacketDispatcher;
import gregtech.api.enums.GT_Values;
import gregtech.api.net.GT_Packet_Block_Event;
import gregtech.api.net.new_packet.to_client.GT_Packet_Block_Event_New;
import gregtech.api.net.new_packet.to_client.GT_Packet_OpenGUI_New;
import gregtech.api.net.new_packet.to_client.GT_Packet_Sound_New;
import gregtech.api.net.new_packet.to_client.GT_Packet_TileEntity_New;
import gregtech.api.net.new_packet.to_server.GT_Packet_ClientPreference_New;
import gregtech.api.net.new_packet.to_server.GT_Packet_InventoryUpdate_New;

public class GT_Network_New extends PacketDispatcher {

    public GT_Network_New() {
        super(GT_Values.MOD_ID);
    }

    @Override
    public void registerPackets() {
        //
        // Sent to server
        //
        registerMessage(GT_Packet_InventoryUpdate_New.Handler.class, GT_Packet_InventoryUpdate_New.class);
        registerMessage(GT_Packet_ClientPreference_New.Handler.class, GT_Packet_ClientPreference_New.class);
        //
        // Sent to client
        //
        registerMessage(GT_Packet_Block_Event_New.Handler.class, GT_Packet_Block_Event_New.class);
        registerMessage(GT_Packet_OpenGUI_New.Handler.class, GT_Packet_OpenGUI_New.class);
        registerMessage(GT_Packet_Sound_New.Handler.class, GT_Packet_Sound_New.class);
        registerMessage(GT_Packet_TileEntity_New.Handler.class, GT_Packet_TileEntity_New.class);
    }

}
