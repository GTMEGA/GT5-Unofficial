package gregtech.api.events;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import gregtech.common.GT_Worldgenerator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.val;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GT_OreVeinLocations {

    @NoArgsConstructor
    @AllArgsConstructor
    public static class VeinData {
        public String oreMix;
        public int oresPlaced;
        public int oresCurrent;
    }

    /**
     * (dimId, ChunkLocation) -> ore mix name
     */
    public static final ThreadLocal<Table<Integer, ChunkCoordIntPair, VeinData>> RecordedOreVeinInChunk = ThreadLocal.withInitial(() -> HashBasedTable.create(3, 1024));
    private static final Map<String, GT_Worldgen_GT_Ore_Layer> lookup = new HashMap<>();

    private static final String NBT_ORE_MIX_TAG = "gregtech:ORE_MIX_IN_CHUNK";
    private static final String NBT_ORE_COUNT_MAX = "gregtech:ORES_PLACED_IN_CHUNK";
    private static final String NBT_ORE_COUNT = "gregtech:ORES_IN_CHUNK";

    @SubscribeEvent
    public void onChunkSave(ChunkDataEvent.Save event) {
        if (event.world.isRemote) {
            return;
        }

        val chunk = event.getChunk();

        val data = RecordedOreVeinInChunk.get().get(chunk.worldObj.provider.dimensionId,
                                                    chunk.getChunkCoordIntPair());

        if (data != null) {
            val nbt = event.getData();

            nbt.setString(NBT_ORE_MIX_TAG, data.oreMix);
            nbt.setInteger(NBT_ORE_COUNT_MAX, data.oresPlaced);
            nbt.setInteger(NBT_ORE_COUNT, data.oresCurrent);
        }
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkDataEvent.Load event) {
        if (event.world.isRemote) {
            return;
        }

        val data = event.getData();

        val oreMixNames = data.getString(NBT_ORE_MIX_TAG);
        val oreCountMax = data.getInteger(NBT_ORE_COUNT_MAX);
        val oreCountCurrent = data.getInteger(NBT_ORE_COUNT);

        if (oreMixNames != null && !oreMixNames.isEmpty()) {
            val chunk = event.getChunk();

            RecordedOreVeinInChunk.get().put(chunk.worldObj.provider.dimensionId,
                                             chunk.getChunkCoordIntPair(),
                                             new VeinData(oreMixNames, oreCountMax, oreCountCurrent));
        }
    }

    public static GT_Worldgen_GT_Ore_Layer getOreVeinInChunk(int dimensionId, ChunkCoordIntPair location) {
        val veinData = RecordedOreVeinInChunk.get().get(dimensionId, location);

        if (veinData == null) {
            return null;
        }

        return lookup.get(veinData.oreMix);
    }

    public static void recordOreVeinInChunk(Chunk chunk, VeinData veinData) {
        if (veinData == null) {
            return;
        }

        GT_OreVeinLocations.RecordedOreVeinInChunk.get().put(chunk.worldObj.provider.dimensionId,
                                                             chunk.getChunkCoordIntPair(),
                                                             veinData);
    }

    public static void addOreVeins(List<GT_Worldgen_GT_Ore_Layer> oreLayers) {
        for (val oreLayer : oreLayers) {
            lookup.put(oreLayer.mWorldGenName, oreLayer);
        }
    }
}
