package gregtech.api.net;

import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.google.common.io.ByteArrayDataInput;
import gregtech.api.events.GT_OreVeinLocations;
import io.netty.buffer.ByteBuf;
import lombok.val;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.IBlockAccess;

import java.nio.charset.StandardCharsets;

public class GT_Packet_VeinDataUpdate extends GT_Packet_New {
    Table.Cell<Integer, ChunkCoordIntPair, GT_OreVeinLocations.VeinData> veinUpdate;

    public GT_Packet_VeinDataUpdate() {
        super(true);
    }

    public GT_Packet_VeinDataUpdate(Table.Cell<Integer, ChunkCoordIntPair, GT_OreVeinLocations.VeinData> veinUpdate) {
        super(false);

        this.veinUpdate = veinUpdate;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(veinUpdate.getRowKey());

        aOut.writeInt(veinUpdate.getColumnKey().chunkXPos);
        aOut.writeInt(veinUpdate.getColumnKey().chunkZPos);

        aOut.writeInt(veinUpdate.getValue().oresPlaced);
        aOut.writeInt(veinUpdate.getValue().oresCurrent);
        aOut.writeBytes(veinUpdate.getValue().oreMix.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        val dimId = aData.readInt();

        val chunkCoord = new ChunkCoordIntPair(aData.readInt(), aData.readInt());

        val oresPlaced = aData.readInt();
        val oresCurrent = aData.readInt();
        val oreMix = aData.readLine();

        val veinData = new GT_OreVeinLocations.VeinData(oreMix, oresPlaced, oresCurrent);

        val veinUpdate = Tables.immutableCell(dimId, chunkCoord, veinData);

        return new GT_Packet_VeinDataUpdate(veinUpdate);
    }

    @Override
    public void process(IBlockAccess aWorld) {
        GT_OreVeinLocations.RecordedOreVeinInChunk.get().put(this.veinUpdate.getRowKey(),
                                                             this.veinUpdate.getColumnKey(),
                                                             this.veinUpdate.getValue());
    }
}
