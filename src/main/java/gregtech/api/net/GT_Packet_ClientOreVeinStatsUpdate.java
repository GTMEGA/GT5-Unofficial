package gregtech.api.net;

import com.google.common.io.ByteArrayDataInput;
import gregtech.common.GT_OreVeinStats;
import gregtech.common.misc.ClientOreVeinStats;
import io.netty.buffer.ByteBuf;
import lombok.val;

import net.minecraft.client.Minecraft;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.common.network.ByteBufUtils;

public class GT_Packet_ClientOreVeinStatsUpdate extends GT_Packet_New {
    private GT_OreVeinStats.Stats stats;
    private int chunkX;
    private int chunkZ;

    public GT_Packet_ClientOreVeinStatsUpdate() {
        super(true);
    }

    public GT_Packet_ClientOreVeinStatsUpdate(GT_OreVeinStats.Stats stats, int chunkX, int chunkZ) {
        super(false);

        this.stats = stats;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(this.chunkX);
        aOut.writeInt(this.chunkZ);

        aOut.writeInt(this.stats.oresPlaced());
        aOut.writeInt(this.stats.oresCurrent());
        
        ByteBufUtils.writeUTF8String(aOut, this.stats.oreMix());
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        val chunkX = aData.readInt();
        val chunkZ = aData.readInt();

        val stats = GT_OreVeinStats.Stats.builder()
                                         .oresPlaced(aData.readInt())
                                         .oresCurrent(aData.readInt())
                                         .oreMix(aData.readLine())
                                         .build();

        return new GT_Packet_ClientOreVeinStatsUpdate(stats, chunkX, chunkZ);
    }

    @Override
    public void process(IBlockAccess aWorld) {
        val world = Minecraft.getMinecraft().theWorld;
        val dimId = world.provider.dimensionId;

        val chunkPos = new ChunkCoordIntPair(this.chunkX, this.chunkZ);

        ClientOreVeinStats.updateClientData(dimId, chunkPos, this.stats);
    }
}
