package gregtech.common;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.enums.GT_Values;
import gregtech.api.net.*;
import gregtech.common.net.MessageSetFlaskCapacity;
import gregtech.common.net.MessageUpdateFluidDisplayItem;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.*;

import static gregtech.GT_Mod.GT_FML_LOGGER;

@ChannelHandler.Sharable
@SuppressWarnings("deprecation")
public class GT_Network extends MessageToMessageCodec<FMLProxyPacket, GT_Packet> implements IGT_NetworkHandler {
    private final EnumMap<Side, FMLEmbeddedChannel> mChannel;

    @Getter
    public enum PacketEnum {
        TILE_ENTITY(new GT_Packet_TileEntity()),
        SOUND(new GT_Packet_Sound()),
        BLOCK_EVENT(new GT_Packet_Block_Event()),
        POLLUTION(new GT_Packet_Pollution()),
        MESSAGE_SET_FLASK_CAPACITY(new MessageSetFlaskCapacity()),
        TILE_ENTITY_COVER(new GT_Packet_TileEntityCover()),
        TILE_ENTITY_COVER_GUI(new GT_Packet_TileEntityCoverGUI()),
        MESSAGE_UPDATE_FLUID_DISPLAY_ITEM(new MessageUpdateFluidDisplayItem()),
        CLIENT_PREFERENCE(new GT_Packet_ClientPreference()),
        WIRELESS_REDSTONE_COVER(new GT_Packet_WirelessRedstoneCover()),
        TILE_ENTITY_COVER_NEW(new GT_Packet_TileEntityCoverNew()),
        TILE_ENTITY_GUI(new GT_Packet_TileEntityGUI()),
        INVENTORY_UPDATE(new GT_Packet_InventoryUpdate()),
        OPEN_GUI(new GT_Packet_OpenGUI()),
        GT_PACKET_SET_FILTER_SLOT(new GT_Packet_SetFilterSlot()),
        ;

        private final GT_Packet_New packet;

        @Getter
        private static final Map<Class<? extends GT_Packet>, PacketEnum> packetMap = new HashMap<>();

        static {
            Arrays.stream(values()).forEach(packetEnum -> packetMap.put(packetEnum.getPacket().getClass(), packetEnum));
        }

        PacketEnum(final @NonNull GT_Packet_New packet) {
            this.packet = packet;
        }

        public static @NonNull GT_Packet processData(final @NonNull ByteArrayDataInput aData) {
            return values()[aData.readByte()].packet.decode(aData);
        }

        public static byte getID(final @NonNull Class<? extends GT_Packet> packetClass) {
            return (byte) packetMap.get(packetClass).ordinal();
        }

    }

    public GT_Network() {
        this.mChannel = NetworkRegistry.INSTANCE.newChannel("GregTech", this, new HandlerShared());
    }

    @Override
    protected void encode(ChannelHandlerContext aContext, GT_Packet aPacket, List<Object> aOutput)
            throws Exception {
        ByteBuf tBuf = Unpooled.buffer().writeByte(aPacket.getPacketID());
        aPacket.encode(tBuf);
        aOutput.add(new FMLProxyPacket(tBuf, aContext.channel().attr(NetworkRegistry.FML_CHANNEL).get()));
    }

    @Override
    protected void decode(ChannelHandlerContext aContext, FMLProxyPacket aPacket, List<Object> aOutput)
            throws Exception {
        ByteArrayDataInput aData = ByteStreams.newDataInput(aPacket.payload().array());
        GT_Packet tPacket = PacketEnum.processData(aData);
        tPacket.setINetHandler(aPacket.handler());
        aOutput.add(tPacket);
    }

    @Override
    public void sendToPlayer(GT_Packet aPacket, EntityPlayerMP aPlayer) {
        if (aPacket == null) {
            GT_FML_LOGGER.info("packet null");
            return;
        }
        if (aPlayer == null) {
            GT_FML_LOGGER.info("player null");
            return;
        }
        this.mChannel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        this.mChannel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(aPlayer);
        this.mChannel.get(Side.SERVER).writeAndFlush(aPacket);
    }

    @Override
    public void sendToAllAround(GT_Packet aPacket, NetworkRegistry.TargetPoint aPosition) {
        this.mChannel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        this.mChannel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(aPosition);
        this.mChannel.get(Side.SERVER).writeAndFlush(aPacket);
    }

    @Override
    public void sendToServer(GT_Packet aPacket) {
        this.mChannel.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        this.mChannel.get(Side.CLIENT).writeAndFlush(aPacket);
    }

    @Override
    public void sendPacketToAllPlayersInRange(World aWorld, GT_Packet aPacket, int aX, int aZ) {
        if (!aWorld.isRemote) {
            for (Object tObject : aWorld.playerEntities) {
                if (!(tObject instanceof EntityPlayerMP)) {
                    break;
                }
                EntityPlayerMP tPlayer = (EntityPlayerMP) tObject;
                Chunk tChunk = aWorld.getChunkFromBlockCoords(aX, aZ);
                if (tPlayer.getServerForPlayer().getPlayerManager().isPlayerWatchingChunk(tPlayer, tChunk.xPosition, tChunk.zPosition)) {
                    sendToPlayer(aPacket, tPlayer);
                }
            }
        }
    }

    @ChannelHandler.Sharable
    static final class HandlerShared
            extends SimpleChannelInboundHandler<GT_Packet> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, GT_Packet aPacket) {
            EntityPlayer aPlayer = GT_Values.GT.getThePlayer();
            aPacket.process(aPlayer == null ? null : GT_Values.GT.getThePlayer().worldObj);
        }
    }
}
