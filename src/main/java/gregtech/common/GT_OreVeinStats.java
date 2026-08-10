package gregtech.common;

import com.github.matt159.mcqlite.api.Database;
import com.github.matt159.mcqlite.api.events.DatabaseLoadEvent;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.OreVein;
import gregtech.api.net.GT_Packet_ClientOreVeinStatsUpdate;
import gregtech.api.util.GT_ChunkAssociatedData;
import gregtech.common.blocks.GT_Block_Ore;
import gregtech.common.blocks.GT_Block_Ore_Abstract;
import gregtech.common.fluids.GT_OreSlurry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.val;
import org.apache.commons.lang3.time.StopWatch;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GT_OreVeinStats {
    public static final Map<String, GT_Worldgen_GT_Ore_Layer> ORE_MIX_LOOKUP = new HashMap<>();
    private static final Storage STORAGE = new Storage();

    public static GT_OreVeinStats.Stats getOreVeinStatsInChunk(World world, int chunkX, int chunkZ) {
        return STORAGE.get(world, chunkX, chunkZ);
    }

    public static void recordOreVeinStats(World world, int chunkX, int chunkZ, Stats stats) {
        if (world.isRemote) {
            return;
        }

        val location = GT_ChunkAssociatedData.makeKey(world.provider.dimensionId, chunkX, chunkZ);

        STORAGE.get(world, chunkX, chunkZ)
               .oreMix(stats.oreMix())
               .oresCurrent(stats.oresCurrent())
               .oresPlaced(stats.oresPlaced())
               .location(location)
               .isDirty(true);
    }

    public static void decrementOreVeinCount(World world, int chunkX, int chunkZ) {
        if (world.isRemote) {
            return;
        }

        val stats = STORAGE.get(world, chunkX, chunkZ);
        val updatedCount = Math.max(0, stats.oresCurrent() - 1);

        stats.oresCurrent(updatedCount);

        val packet = new GT_Packet_ClientOreVeinStatsUpdate(stats, chunkX, chunkZ);
        GT_Values.NW.sendPacketToAllPlayersInRange(world, packet, chunkX << 4, chunkZ << 4);
    }

    public static void addOreVeins(List<GT_Worldgen_GT_Ore_Layer> oreLayers) {
        for (val oreLayer : oreLayers) {
            ORE_MIX_LOOKUP.put(oreLayer.mWorldGenName, oreLayer);
        }
    }

    public static GT_OreVeinStats.Stats rescanVeinAt(World world, int chunkX, int chunkZ) {
        val chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
        val oreVeinLikelihood = new HashMap<GT_Worldgen_GT_Ore_Layer, Integer>();
        val oreTypeFrequency = new IdentityHashMap<GT_Block_Ore_Abstract, Integer>();

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

        for (val oreEntry : oreTypeFrequency.entrySet()) {
            val ore = oreEntry.getKey();
            val material = ore.material();

            for (val oreMix : GT_Worldgen_GT_Ore_Layer.sList) {
                if (oreMix.containsMaterial(material)) {
                    val frequency = oreVeinLikelihood.computeIfAbsent(oreMix, key -> 0);

                    oreVeinLikelihood.put(oreMix, frequency + oreEntry.getValue());
                }
            }
        }

        val fallback = new AbstractMap.SimpleEntry<>(GT_Worldgen_GT_Ore_Layer.EMPTY_VEIN, 0);

        val oreVeinEntry = oreVeinLikelihood.entrySet()
                                            .stream()
                                            .max(Map.Entry.comparingByValue())
                                            .orElse(fallback);

        GT_OreSlurry slurry;

        GT_OreVeinStats.Stats stats;

        if (oreVeinEntry != fallback) {
            slurry = GT_OreSlurry.ORE_SLURRY_LOOKUP.get(oreVeinEntry.getKey());

            val currentVein = slurry.oreLayer;

            val a = GT_Block_Ore.getOre(currentVein.mPrimary, GT_Block_Ore_Abstract.OreSize.Normal);
            val b = GT_Block_Ore.getOre(currentVein.mSecondary, GT_Block_Ore_Abstract.OreSize.Normal);
            val c = GT_Block_Ore.getOre(currentVein.mSporadic, GT_Block_Ore_Abstract.OreSize.Normal);
            val d = GT_Block_Ore.getOre(currentVein.mBetween, GT_Block_Ore_Abstract.OreSize.Normal);

            val oreCount = oreTypeFrequency.getOrDefault(a, 0) +
                           oreTypeFrequency.getOrDefault(b, 0) +
                           oreTypeFrequency.getOrDefault(c, 0) +
                           oreTypeFrequency.getOrDefault(d, 0);

            stats = GT_OreVeinStats.Stats.builder()
                                         .oreMix(OreVein.LOOKUP.get(currentVein.mWorldGenName))
                                         .oresPlaced(oreCount)
                                         .oresCurrent(oreCount)
                                         .build();

            GT_Mod.GT_FML_LOGGER.debug("Recalculated Ore Vein type at [{}, {}] to be {}", chunk.xPosition, chunk.zPosition, currentVein.mWorldGenName);
        } else {
//            GT_Mod.GT_FML_LOGGER.warn("Null ore slurry selected");

            stats = Stats.DEFAULT.toBuilder()
                                 .build();
        }

        GT_OreVeinStats.recordOreVeinStats(chunk.worldObj, chunk.xPosition, chunk.zPosition, stats);

        return stats;
    }

    public static class EventHandler {
        @SubscribeEvent
        public void onDatabaseLoad(DatabaseLoadEvent event) {
            if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
                return;
            }

            try (val connection = Database.getConnection()){
                val statement = connection.prepareStatement(Queries.CREATE_ORE_VEIN_STATS_TABLE);

                statement.execute();
            } catch (SQLException e) {
                GT_Mod.GT_FML_LOGGER.error("Failed to create ore_vein_stats table", e);
            }
        }

        @SubscribeEvent
        public void chunkWatch(ChunkWatchEvent.Watch event) {
            val world = event.player.worldObj;

            if (world.isRemote) {
                return;
            }

            if (!GT_OreVeinStats.STORAGE.isCreated(world, event.chunk)) {
                return;
            }

            val stats = GT_OreVeinStats.STORAGE.get(world, event.chunk);

//            if (stats.isDirty()) {
//                return;
//            }

            val chunk = event.chunk;
            val packet = new GT_Packet_ClientOreVeinStatsUpdate(stats, chunk.chunkXPos, chunk.chunkZPos);

            GT_Values.NW.sendToPlayer(packet, event.player);
        }

        @SubscribeEvent
        public void onWorldLoad(WorldEvent.Load e) {
            // super class loads everything lazily. We force it to load them all.
            if (!e.world.isRemote) {
                GT_Mod.GT_FML_LOGGER.info("Loading all chunk info for dimension: {}", e.world.provider.dimensionId);

                GT_OreVeinStats.STORAGE.loadAll(e.world);

                GT_Mod.GT_FML_LOGGER.info("Finished loading all chunk info for dimension: {}", e.world.provider.dimensionId);
            }
        }
    }

    @ParametersAreNonnullByDefault
    private static final class Storage extends GT_ChunkAssociatedData<Stats> {
        private Storage() {
            super("ore_vein_stats", Stats.class);
        }

        public boolean isCreated(World world, ChunkCoordIntPair chunk) {
            return super.isCreated(world.provider.dimensionId, chunk.chunkXPos, chunk.chunkZPos);
        }

        @Override
        protected void writeElements(Connection connection, Queue<Stats> writeQueue) throws SQLException {
            val values = writeQueue.stream()
                                   .map(entry -> Queries.UPSERT_ORE_VEIN_STATS_VALUES_TEMPLATE
                                                             .formatted(entry.location(),
                                                                        entry.oreMix().ordinal(),
                                                                        entry.oresPlaced(),
                                                                        entry.oresCurrent()))
                                   .collect(Collectors.joining(","));

            val query = Queries.UPSERT_ORE_VEIN_STATS.formatted(values);

            val stopWatch = StopWatch.createStarted();

            val upsert = connection.prepareStatement(query);

            upsert.executeUpdate();

            stopWatch.stop();

            GT_Mod.GT_FML_LOGGER.info("Recorded {} chunks in {} ms", writeQueue.size(), stopWatch.getNanoTime() / 1e6);

            while (!writeQueue.isEmpty()) {
                val stats = writeQueue.poll();

                stats.isDirty(false);
            }
        }

        @Override
        protected void readAllElementsInWorld(Connection connection, int dimId) throws SQLException {
            val stopWatch = StopWatch.createStarted();

            val countQuery = connection.prepareStatement(Queries.QUERY_ORE_VEIN_STATS_COUNT);
            countQuery.setInt(1, dimId);

            val countQueryResult = countQuery.executeQuery();
            countQueryResult.next();

            val count = countQueryResult.getInt(1);

            for (int offset = 0; offset < count; offset += PAGE_SIZE) {
                val pagedQuery = connection.prepareStatement(Queries.QUERY_ORE_VEIN_STATS_BY_DIMENSION_PAGED);

                pagedQuery.setInt(1, dimId);
                pagedQuery.setInt(2, PAGE_SIZE);
                pagedQuery.setInt(3, offset);

                val resultSet = pagedQuery.executeQuery();

                val map = this.masterMap.computeIfAbsent(dimId, key -> new ConcurrentHashMap<>());

                while (resultSet.next()) {
                    val stats = Stats.builder()
                                     .location(resultSet.getInt(1))
                                     .oreMix(OreVein.values()[resultSet.getInt(2)])
                                     .oresPlaced(resultSet.getInt(3))
                                     .oresCurrent(resultSet.getInt(4))
                                     .isDirty(false)
                                     .build();

                    map.put(keyToChunkCoord(stats.location()), stats);
                }

                pagedQuery.close();
            }

            countQuery.close();

            stopWatch.stop();

            GT_Mod.GT_FML_LOGGER.info("Read {} rows in {} ms", masterMap.get(dimId).size(), stopWatch.getNanoTime() / 1e6);
        }

        @Override
        protected void readElements(Connection connection, Queue<Long> keys) throws SQLException {

        }

        @Override
        protected Stats createElement(World world, int chunkX, int chunkZ) {
            val dimId = world.provider.dimensionId;
            val location = makeKey(dimId, chunkX, chunkZ);

            return Stats.DEFAULT.toBuilder()
                                .location(location)
                                .build();
        }
    }

    @Getter
    @SuperBuilder(toBuilder = true)
    @Accessors(fluent = true, chain = true)
    public static final class Stats extends GT_ChunkAssociatedData.IData {
        public static final GT_OreVeinStats.Stats DEFAULT = Stats.builder()
                                                                 .location(0L)
                                                                 .oreMix(OreVein.LOOKUP.get(GT_Worldgen_GT_Ore_Layer.EMPTY_VEIN.mWorldGenName))
                                                                 .oresPlaced(0)
                                                                 .oresCurrent(0)
                                                                 .isDirty(true)
                                                                 .build();

        private OreVein oreMix;
        private int oresPlaced;
        private int oresCurrent;

        public Stats oreMix(OreVein oreMix) {
            this.oreMix = oreMix;

            this.markDirty();
            return this;
        }

        public Stats oresPlaced(int oresPlaced) {
            this.oresPlaced = oresPlaced;

            this.markDirty();
            return this;
        }

        public Stats oresCurrent(int oresCurrent) {
            this.oresCurrent = oresCurrent;

            this.markDirty();
            return this;
        }
    }

    private static final String NBT_ORE_MIX_TAG = "gregtech:ORE_MIX_IN_CHUNK";
    private static final String NBT_ORE_COUNT_MAX = "gregtech:ORES_PLACED_IN_CHUNK";
    private static final String NBT_ORE_COUNT = "gregtech:ORES_IN_CHUNK";

    public static void migrate(ChunkDataEvent.Load e) {
        val data = e.getData();

        val oreMix = data.getString(NBT_ORE_MIX_TAG);
        val oresPlaced = data.getInteger(NBT_ORE_COUNT_MAX);
        val oresCurrent = data.getInteger(NBT_ORE_COUNT);

        if (oreMix == null || oreMix.isBlank()) {
            return;
        }

        var chunkData = STORAGE.get(e.getChunk());

        if (chunkData.isDirty()) {
            GT_Mod.GT_FML_LOGGER.info("Migrating chunk: [{}, {}] in dim: {} to new system",
                                      e.getChunk().xPosition,
                                      e.getChunk().zPosition,
                                      e.world.provider.dimensionId);

            GT_Mod.GT_FML_LOGGER.info("Chunk stats for: [{}, {}] | Vein: {} | Ores Placed: {} | Ores Left: {}",
                                      e.getChunk().xPosition,
                                      e.getChunk().zPosition,
                                      oreMix,
                                      oresPlaced,
                                      oresCurrent);

            val location = GT_ChunkAssociatedData.makeKey(e.world.provider.dimensionId,
                                                          e.getChunk().xPosition,
                                                          e.getChunk().zPosition);

            chunkData.oreMix(OreVein.LOOKUP.get(oreMix))
                     .oresPlaced(oresPlaced)
                     .oresCurrent(oresCurrent)
                     .location(location)
                     .markDirty();
        }
    }
}
