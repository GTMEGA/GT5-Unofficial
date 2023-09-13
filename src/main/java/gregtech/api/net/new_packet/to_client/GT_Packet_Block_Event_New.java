package gregtech.api.net.new_packet.to_client;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import eu.usrv.yamcore.network.client.AbstractClientMessageHandler;
import io.netty.buffer.ByteBuf;
import lombok.*;
import net.minecraft.entity.player.EntityPlayer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GT_Packet_Block_Event_New implements IMessage {

    public static class Handler extends AbstractClientMessageHandler<GT_Packet_Block_Event_New> {

        @Override
        public IMessage handleClientMessage(EntityPlayer player, GT_Packet_Block_Event_New message, MessageContext ctx) {
            if (player.worldObj != null) {
                val world = player.worldObj;
                val te = world.getTileEntity(message.mX, message.mY, message.mZ);
                if (te != null) {
                    te.receiveClientEvent(message.mID, message.mValue);
                }
            }
            return null;
        }

    }

    private int mX, mY, mZ;

    private byte mID, mValue;


    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        mX = byteBuf.readInt();
        mY = byteBuf.readInt();
        mZ = byteBuf.readInt();
        mID = byteBuf.readByte();
        mValue = byteBuf.readByte();
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        byteBuf.writeInt(mX);
        byteBuf.writeInt(mY);
        byteBuf.writeInt(mZ);
        byteBuf.writeByte(mID);
        byteBuf.writeByte(mValue);
    }

}
