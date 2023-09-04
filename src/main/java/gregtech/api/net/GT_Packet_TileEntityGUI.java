package gregtech.api.net;


import com.google.common.io.ByteArrayDataInput;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IAdvancedGUIEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Log;
import gregtech.api.util.ISerializableObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;


public class GT_Packet_TileEntityGUI extends GT_Packet_New {

    protected int mID;

    protected int dimID;

    protected int mX;

    protected short mY;

    protected int mZ;

    protected ISerializableObject data;

    public static GT_Packet_TileEntityGUI createFromMachine(final IAdvancedGUIEntity entity, final ISerializableObject data, final int dimension) {
        IGregTechTileEntity gtEntity = entity.getBaseMetaTileEntity();
        return new GT_Packet_TileEntityGUI(gtEntity.getMetaTileID(), dimension, gtEntity.getXCoord(), gtEntity.getYCoord(), gtEntity.getZCoord(),
                                           data);
    }

    public GT_Packet_TileEntityGUI() {
        super(true);
    }

    public GT_Packet_TileEntityGUI(
            final int mID, final int dimID, final int x, final short y, final int z, final ISerializableObject data
                                  ) {
        super(false);
        this.mID = mID;
        this.dimID = dimID;
        this.mX = x;
        this.mY = y;
        this.mZ = z;
        this.data = data;
    }

    /**
     * Process the packet
     *
     * @param aWorld null if message is received on server side, the client world if message is received on client side
     */
    @Override
    public void process(final IBlockAccess aWorld) {
        final World world = DimensionManager.getWorld(dimID);
        TileEntity tile = world.getTileEntity(mX, mY, mZ);
        if (tile instanceof IGregTechTileEntity && ((IGregTechTileEntity) tile).getMetaTileEntity() instanceof IAdvancedGUIEntity) {
            if (data != null) {
                ((IAdvancedGUIEntity) (((IGregTechTileEntity) tile).getMetaTileEntity())).receiveGuiData(this.data);
            } else {
                GT_Log.err.println("Got bad gui packet :/");
            }
        }
    }

    /**
     * @param aOut
     */
    @Override
    public void encode(final ByteBuf aOut) {
        aOut.writeInt(mID);
        aOut.writeInt(dimID);
        aOut.writeInt(mX);
        aOut.writeShort(mY);
        aOut.writeInt(mZ);
        data.writeToByteBuf(aOut);
    }

    /**
     * @param aData
     * @return
     */
    @Override
    public GT_Packet_New decode(final ByteArrayDataInput aData) {
        int mID = aData.readInt();
        IAdvancedGUIEntity gui = getGUI(mID);
        return new GT_Packet_TileEntityGUI(mID, aData.readInt(), aData.readInt(), aData.readShort(), aData.readInt(),
                                           gui != null ? gui.decodePacket(aData) : null);
    }

    public static IAdvancedGUIEntity getGUI(final int mID) {
        IMetaTileEntity entity;
        if (mID >= GregTech_API.METATILEENTITIES.length) {
            entity = null;
        } else {
            entity = GregTech_API.METATILEENTITIES[mID];
        }
        IAdvancedGUIEntity gui = null;
        if (entity instanceof IAdvancedGUIEntity) {
            gui = (IAdvancedGUIEntity) entity;
        }
        return gui;
    }

}
