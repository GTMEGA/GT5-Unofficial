package gregtech.api.net.new_packet.to_server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import eu.usrv.yamcore.network.server.AbstractServerMessageHandler;
import gregtech.GT_Mod;
import gregtech.api.util.GT_ClientPreference;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GT_Packet_ClientPreference_New implements IMessage {

    public static class Handler extends AbstractServerMessageHandler<GT_Packet_ClientPreference_New> {

        @Override
        public IMessage handleServerMessage(final EntityPlayer player, final GT_Packet_ClientPreference_New message, final MessageContext ctx) {
            GT_Mod.gregtechproxy.setClientPreference(player.getUniqueID(), message.getPreference());
            return null;
        }

    }

    private GT_ClientPreference preference;

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        preference = new GT_ClientPreference(byteBuf.readBoolean(), byteBuf.readBoolean(), byteBuf.readBoolean());
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        byteBuf.writeBoolean(preference.isSingleBlockInitialFilterEnabled());
        byteBuf.writeBoolean(preference.isSingleBlockInitialMultiStackEnabled());
        byteBuf.writeBoolean(preference.isInputBusInitialFilterEnabled());
    }

}
