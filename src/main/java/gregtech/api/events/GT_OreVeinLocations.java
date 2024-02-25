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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class GT_OreVeinLocations {
    /**
     * (dimId, ChunkLocation) -> ore mix name
     */
    public static final Table<Integer, ChunkCoordIntPair, String> RecordedOreVeinsInChunk = HashBasedTable.create(3, 1024);
    private static final Map<String, GT_Worldgen_GT_Ore_Layer> lookup = new HashMap<>();

    private static final String NBT_ORE_MIX_TAG = "gregtech:ORE_MIX_IN_CHUNK";

    @SubscribeEvent
    public void onChunkSave(ChunkDataEvent.Save event) {
        if (event.world.isRemote) {
            return;
        }

        val chunk = event.getChunk();

        val data = RecordedOreVeinsInChunk.get(chunk.worldObj.provider.dimensionId,
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

            RecordedOreVeinsInChunk.put(chunk.worldObj.provider.dimensionId,
                                        chunk.getChunkCoordIntPair(),
                                        oreMixNames);
        }
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        if (event.world.isRemote) {
            return;
        }

        RecordedOreVeinsInChunk.remove(event.world.provider.dimensionId,
                                       event.getChunk().getChunkCoordIntPair());
    }

    public static List<GT_Worldgen_GT_Ore_Layer> getOreVeinsInChunk(int dimensionId, ChunkCoordIntPair location) {
        val oreMixeNames = RecordedOreVeinsInChunk.get(dimensionId, location);

        if (oreMixeNames == null) {
            return Collections.emptyList();
        }

        val oreMixes = Arrays.stream(oreMixeNames.split("\\|"))
                             .map(lookup::get)
                             .collect(Collectors.toList());

        return oreMixes;
    }

    public static void recordOreVeinsInChunk(Chunk chunk, Set<GT_Worldgen_GT_Ore_Layer> oreMixes) {
        if (oreMixes.isEmpty()) {
            return;
        }

        val entry = oreMixes.stream()
                            .filter(Objects::nonNull)
                            .map(mix -> mix.mWorldGenName)
                            .collect(Collectors.joining("|"));

        GT_OreVeinLocations.RecordedOreVeinsInChunk.put(chunk.worldObj.provider.dimensionId,
                                                        chunk.getChunkCoordIntPair(),
                                                        entry);
    }

    public static void addOreVeins(List<GT_Worldgen_GT_Ore_Layer> oreLayers) {
        for (val oreLayer : oreLayers) {
            lookup.put(oreLayer.mWorldGenName, oreLayer);
        }
    }
}
