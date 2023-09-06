package gregtech.api.net;


import com.google.common.io.ByteArrayDataInput;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.UUIDWrapper;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;


@Getter
public class GT_Packet_OpenGUI extends GT_Packet_New {

    private EntityPlayer player;

    private int guiID;

    public GT_Packet_OpenGUI() {
        super(true);
    }

    public GT_Packet_OpenGUI(final EntityPlayer player, final int guiID) {
        super(false);
        this.player = player;
        this.guiID = guiID;
    }

    /**
     * Process the packet
     *
     * @param aWorld null if message is received on server side, the client world if message is received on client side
     */
    @Override
    public void process(final IBlockAccess aWorld) {
        if (player == null) {
            return;
        }
        player.openGui(GT_Values.GT, guiID, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
    }

    /**
     * @param aOut
     */
    @Override
    public void encode(final ByteBuf aOut) {
        new UUIDWrapper(player.getUniqueID()).writeToByteBuf(aOut);
        aOut.writeInt(guiID);
    }

    /**
     * @param aData
     * @return
     */
    @Override
    public GT_Packet_New decode(final ByteArrayDataInput aData) {
        return new GT_Packet_OpenGUI(GT_Utility.getPlayerFromUUID(((UUIDWrapper)new UUIDWrapper().readFromPacket(aData, null))), aData.readInt());
    }

}
