package gregtech.api.net.new_packet.to_client;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import eu.usrv.yamcore.network.client.AbstractClientMessageHandler;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import lombok.*;
import net.minecraft.entity.player.EntityPlayer;

import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GT_Packet_Sound_New implements IMessage {

    public static class Handler extends AbstractClientMessageHandler<GT_Packet_Sound_New> {

        @Override
        public IMessage handleClientMessage(final EntityPlayer player, final GT_Packet_Sound_New message, final MessageContext ctx) {
            GT_Utility.doSoundAtClient(message.getSoundName(), 1, message.getSoundStrength(), message.getSoundPitch(), message.getX(), message.getY(), message.getZ());
            return null;
        }

    }

    private int x, y, z;

    private String soundName;

    private float soundStrength, soundPitch;

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        try (val tIn = new ByteBufInputStream(byteBuf)) {
            soundName = tIn.readUTF();
            soundStrength = tIn.readFloat();
            soundPitch = tIn.readFloat();
            x = tIn.readInt();
            y = tIn.readShort();
            z = tIn.readInt();
        } catch (IOException e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        try (val tOut = new ByteBufOutputStream(byteBuf)) {
            tOut.writeUTF(soundName);
            tOut.writeFloat(soundStrength);
            tOut.writeFloat(soundPitch);
            tOut.writeInt(x);
            tOut.writeShort(y);
            tOut.writeInt(z);
        } catch (IOException e) {
            e.printStackTrace(GT_Log.err);
        }
    }

}
