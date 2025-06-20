package gregtech.api.events;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.net.GT_Packet_VeinDataUpdate;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import gregtech.common.blocks.GT_Block_Ore;
import gregtech.common.blocks.GT_Block_Ore_Abstract;
import gregtech.common.fluids.GT_OreSlurry;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

import java.util.HashMap;
import java.util.IdentityHashMap;
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

    @SubscribeEvent
    public void onPlayerWatchChunk(ChunkWatchEvent.Watch event) {
        if (event.player.worldObj.isRemote) {
            return;
        }

        val dimId = event.player.worldObj.provider.dimensionId;
        val chunkCoord = event.chunk;

        val veinData = RecordedOreVeinInChunk.get().get(dimId, chunkCoord);

        val packet = new GT_Packet_VeinDataUpdate(Tables.immutableCell(dimId, chunkCoord, veinData));

        GT_Values.NW.sendToPlayer(packet, event.player);
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

    public static void updateClients(int dimensionId, ChunkCoordIntPair chunkCoord, VeinData veinData) {
        val packet = new GT_Packet_VeinDataUpdate(Tables.immutableCell(dimensionId, chunkCoord, veinData));
        val targetPoint = new NetworkRegistry.TargetPoint(dimensionId, 0, 0, 0, Double.POSITIVE_INFINITY);

        GT_Values.NW.sendToAllAround(packet, targetPoint);
    }

    public static GT_OreSlurry scanSlurryInChunkAt(World world, int chunkX, int chunkZ) {
        val oreVeinLikelihood = new HashMap<GT_Worldgen_GT_Ore_Layer, Integer>();
        val oreTypeFrequency = new IdentityHashMap<GT_Block_Ore_Abstract, Integer>();

        val chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                val yMax = chunk.getHeightValue(x, z);

                for (int y = 0; y < yMax; y++) {
                    val block = chunk.getBlock(x, y, z);

                    if (!(block instanceof GT_Block_Ore ore)) {
                        continue;
                    }

                    val frequency = oreTypeFrequency.computeIfAbsent(ore, key -> 0);
                    oreTypeFrequency.put(ore, frequency + 1);
                }
            }
        }

        for (val ore : oreTypeFrequency.keySet()) {
            val material = ore.material();

            for (val oreMix : GT_Worldgen_GT_Ore_Layer.sList) {
                if (oreMix.containsMaterial(material)) {
                    val frequency = oreVeinLikelihood.computeIfAbsent(oreMix, key -> 0);

                    oreVeinLikelihood.put(oreMix, frequency + 1);
                }
            }
        }

        val oreVeinEntry = oreVeinLikelihood.entrySet()
                                            .stream()
                                            .max(Map.Entry.comparingByValue())
                                            .orElse(null);

        GT_OreSlurry slurry = null;

        if (oreVeinEntry != null) {
            slurry = GT_OreSlurry.slurries.get(oreVeinEntry.getKey());

            val currentVein = slurry.oreLayer;

            val a = GT_Block_Ore.getOre(currentVein.mPrimary, GT_Block_Ore_Abstract.OreSize.Normal);
            val b = GT_Block_Ore.getOre(currentVein.mSecondary, GT_Block_Ore_Abstract.OreSize.Normal);
            val c = GT_Block_Ore.getOre(currentVein.mSporadic, GT_Block_Ore_Abstract.OreSize.Normal);
            val d = GT_Block_Ore.getOre(currentVein.mBetween, GT_Block_Ore_Abstract.OreSize.Normal);

            val oreCount = oreTypeFrequency.getOrDefault(a, 0) +
                           oreTypeFrequency.getOrDefault(b, 0) +
                           oreTypeFrequency.getOrDefault(c, 0) +
                           oreTypeFrequency.getOrDefault(d, 0);

            val veinData = new VeinData(currentVein.mWorldGenName, oreCount, oreCount);

            RecordedOreVeinInChunk.get().put(world.provider.dimensionId, new ChunkCoordIntPair(chunkX, chunkZ), veinData);

            GT_Mod.GT_FML_LOGGER.info("Recalculated Ore Vein type at [{}, {}] to be {}", chunkX, chunkZ, currentVein.mWorldGenName);
        } else {
            GT_Mod.GT_FML_LOGGER.warn("Null ore slurry selected");
        }

        return slurry;
    }
}
