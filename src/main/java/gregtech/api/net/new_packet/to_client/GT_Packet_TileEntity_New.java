package gregtech.api.net.new_packet.to_client;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import eu.usrv.yamcore.network.client.AbstractClientMessageHandler;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import io.netty.buffer.ByteBuf;
import lombok.*;
import net.minecraft.entity.player.EntityPlayer;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GT_Packet_TileEntity_New implements IMessage {

    public static class Handler extends AbstractClientMessageHandler<GT_Packet_TileEntity_New> {

        @Override
        public IMessage handleClientMessage(final EntityPlayer player, final GT_Packet_TileEntity_New message, final MessageContext ctx) {
            if (player != null && player.worldObj != null) {
                val world = player.worldObj;
                val tileEntity = world.getTileEntity(message.x, message.y, message.z);
                if (tileEntity instanceof BaseMetaTileEntity) {
                    val metaTileEntity = (BaseMetaTileEntity) tileEntity;
                    metaTileEntity.receiveMetaTileEntityData(message.mID, message.c0, message.c1, message.c2, message.c3, message.c4, message.c5, message.texture, message.texturePage, message.update, message.redstone, message.color);
                } else if (tileEntity instanceof BaseMetaPipeEntity) {
                    val metaPipeEntity = (BaseMetaPipeEntity) tileEntity;
                    metaPipeEntity.receiveMetaTileEntityData(message.mID, message.c0, message.c1, message.c2, message.c3, message.c4, message.c5, message.texture, message.update, message.redstone, message.color);
                }
            }
            return null;
        }

    }

    private int x, y, z;

    private int c0, c1, c2, c3, c4, c5;

    private short mID;

    private byte texture, texturePage, update, redstone, color;

    public GT_Packet_TileEntity_New(final int x, final int y, final int z, final short mID, final int c0, final int c1, final int c2, final int c3, final int c4, final int c5, final byte texture, final byte update, final byte redstone,
                                    final byte color) {
        this(x, y, z, c0, c1, c2, c3, c4, c5, mID, texture, (byte) 0, update, redstone, color);
    }

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        x = byteBuf.readInt();
        y = byteBuf.readInt();
        z = byteBuf.readInt();
        c0 = byteBuf.readInt();
        c1 = byteBuf.readInt();
        c2 = byteBuf.readInt();
        c3 = byteBuf.readInt();
        c4 = byteBuf.readInt();
        c5 = byteBuf.readInt();
        mID = byteBuf.readShort();
        texture = byteBuf.readByte();
        texturePage = byteBuf.readByte();
        update = byteBuf.readByte();
        redstone = byteBuf.readByte();
        color = byteBuf.readByte();
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        byteBuf.writeInt(x);
        byteBuf.writeInt(y);
        byteBuf.writeInt(z);
        byteBuf.writeInt(c0);
        byteBuf.writeInt(c1);
        byteBuf.writeInt(c2);
        byteBuf.writeInt(c3);
        byteBuf.writeInt(c4);
        byteBuf.writeInt(c5);
        byteBuf.writeShort(mID);
        byteBuf.writeByte(texture);
        byteBuf.writeByte(texturePage);
        byteBuf.writeByte(update);
        byteBuf.writeByte(redstone);
        byteBuf.writeByte(color);
    }

}
