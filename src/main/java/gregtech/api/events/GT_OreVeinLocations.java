package gregtech.api.events;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
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
    /**
     * (dimId, ChunkLocation) -> ore mix name
     */
    public static final Table<Integer, ChunkCoordIntPair, String> RecordedOreVeinInChunk = HashBasedTable.create(3, 1024);
    private static final Map<String, GT_Worldgen_GT_Ore_Layer> lookup = new HashMap<>();

    private static final String NBT_ORE_MIX_TAG = "gregtech:ORE_MIX_IN_CHUNK";

    @SubscribeEvent
    public void onChunkSave(ChunkDataEvent.Save event) {
        if (event.world.isRemote) {
            return;
        }

        val chunk = event.getChunk();

        val data = RecordedOreVeinInChunk.get(chunk.worldObj.provider.dimensionId,
                                              chunk.getChunkCoordIntPair());

        if (data != null) {
            val nbt = event.getData();

            nbt.setString(NBT_ORE_MIX_TAG, data);
        }
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkDataEvent.Load event) {
        if (event.world.isRemote) {
            return;
        }

        val oreMixNames = event.getData().getString(NBT_ORE_MIX_TAG);

        if (oreMixNames != null && !oreMixNames.isEmpty()) {
            val chunk = event.getChunk();

            RecordedOreVeinInChunk.put(chunk.worldObj.provider.dimensionId,
                                       chunk.getChunkCoordIntPair(),
                                       oreMixNames);
        }
    }

//    @SubscribeEvent
//    public void onChunkUnload(ChunkEvent.Unload event) {
//        if (event.world.isRemote) {
//            return;
//        }
//
//        RecordedOreVeinInChunk.remove(event.world.provider.dimensionId,
//                                      event.getChunk().getChunkCoordIntPair());
//    }

    public static GT_Worldgen_GT_Ore_Layer getOreVeinInChunk(int dimensionId, ChunkCoordIntPair location) {
        val oreMixName = RecordedOreVeinInChunk.get(dimensionId, location);

        if (oreMixName == null) {
            return null;
        }

        return lookup.get(oreMixName);
    }

    public static void recordOreVeinInChunk(Chunk chunk, GT_Worldgen_GT_Ore_Layer oreMix) {
        if (oreMix == null) {
            return;
        }

        val entry = oreMix.mWorldGenName;

        GT_OreVeinLocations.RecordedOreVeinInChunk.put(chunk.worldObj.provider.dimensionId,
                                                       chunk.getChunkCoordIntPair(),
                                                       entry);
    }

    public static void addOreVeins(List<GT_Worldgen_GT_Ore_Layer> oreLayers) {
        for (val oreLayer : oreLayers) {
            lookup.put(oreLayer.mWorldGenName, oreLayer);
        }
    }
}
