package gregtech.api.net;


import com.google.common.io.ByteArrayDataInput;
import gregtech.common.GT_Network;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;
import net.minecraft.world.IBlockAccess;


/**
 * @deprecated Use {@link GT_Packet_New} instead
 */
@Deprecated
public abstract class GT_Packet {

    public GT_Packet(boolean aIsReference) {
        //
    }

    /**
     * I use constant IDs instead of Dynamic ones, since that is much more fail safe
     * ^ NOPE ^
     * This statement was BS, hence why this method is now implemented, and FINAL.
     *
     * @return a Packet ID for this Class
     */
    public final byte getPacketID() {
        return GT_Network.PacketEnum.getID(this.getClass());
    }

    /**
     * @return encoded byte Stream
     * @deprecated Use {@link #encode(ByteBuf)} instead
     */
    @Deprecated
    public abstract byte[] encode();

    /**
     * Encode the data into given byte buffer without creating an intermediate byte array.
     * Default implementation just throw {@link UnsupportedOperationException}.
     */
    public void encode(ByteBuf aOut) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return encoded byte Stream
     */
    public abstract GT_Packet decode(ByteArrayDataInput aData);

    /**
     * Process the packet
     *
     * @param aWorld null if message is received on server side, the client world if message is received on client side
     */
    public abstract void process(IBlockAccess aWorld);

    /**
     * This will be called just before {@link #process(IBlockAccess)} to inform the handler about the source and type of connection
     */
    public void setINetHandler(INetHandler aHandler) {
    }

}
