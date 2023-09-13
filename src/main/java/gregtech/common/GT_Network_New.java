package gregtech.common;

import eu.usrv.yamcore.network.PacketDispatcher;
import gregtech.api.enums.GT_Values;

public class GT_Network_New extends PacketDispatcher {

    public GT_Network_New() {
        super(GT_Values.MOD_ID);
    }

    @Override
    public void registerPackets() {

    }

}
