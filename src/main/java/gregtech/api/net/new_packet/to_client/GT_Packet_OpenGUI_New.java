package gregtech.api.net.new_packet.to_client;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import eu.usrv.yamcore.network.client.AbstractClientMessageHandler;
import gregtech.api.enums.GT_Values;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GT_Packet_OpenGUI_New implements IMessage {

    public static class Handler extends AbstractClientMessageHandler<GT_Packet_OpenGUI_New> {

        @Override
        public IMessage handleClientMessage(final EntityPlayer player, final GT_Packet_OpenGUI_New message, final MessageContext ctx) {
            if (player == null) {
                return null;
            }
            player.openGui(GT_Values.GT, message.getGuiID(), player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
            return null;
        }

    }

    private int guiID;

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        byteBuf.writeInt(guiID);
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        guiID = byteBuf.readInt();
    }

}
