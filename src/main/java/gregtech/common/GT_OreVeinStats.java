package gregtech.common;

import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.net.GT_Packet_ClientOreVeinStatsUpdate;
import gregtech.api.util.GT_ChunkAssociatedData;
import gregtech.common.blocks.GT_Block_Ore;
import gregtech.common.blocks.GT_Block_Ore_Abstract;
import gregtech.common.fluids.GT_OreSlurry;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class GT_OreVeinStats {
    private static final byte VERSION = 0;
    public static final Map<String, GT_Worldgen_GT_Ore_Layer> ORE_MIX_LOOKUP = new HashMap<>();
    private static final Storage STORAGE = new Storage();

    public static GT_OreVeinStats.Stats getOreVeinStatsInChunk(World world, int chunkX, int chunkZ) {
        return STORAGE.get(world, chunkX, chunkZ);
    }

    public static void recordOreVeinStats(World world, int chunkX, int chunkZ, Stats stats) {
        if (world.isRemote) {
            return;
        }

        STORAGE.get(world, chunkX, chunkZ)
               .oreMix(stats.oreMix())
               .oresCurrent(stats.oresCurrent())
               .oresPlaced(stats.oresPlaced());
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
                                         .oreMix(currentVein.mWorldGenName)
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

    public static class GT_OreSlurryEventHandler {
        @SubscribeEvent
        public void chunkWatch(ChunkWatchEvent.Watch event) {
            val world = event.player.worldObj;

            if (!GT_OreVeinStats.STORAGE.isCreated(world, event.chunk)) {
                return;
            }

            val stats = GT_OreVeinStats.STORAGE.get(world, event.chunk);

            if (stats.isSameAsDefault()) {
                return;
            }

            val chunk = event.chunk;
            val packet = new GT_Packet_ClientOreVeinStatsUpdate(stats, chunk.chunkXPos, chunk.chunkZPos);

            GT_Values.NW.sendToPlayer(packet, event.player);
        }

        @SubscribeEvent
        public void onWorldLoad(WorldEvent.Load e) {
            // super class loads everything lazily. We force it to load them all.
            if (!e.world.isRemote) {
                GT_OreVeinStats.STORAGE.loadAll(e.world);
            }
        }
    }

    @ParametersAreNonnullByDefault
    private static final class Storage extends GT_ChunkAssociatedData<Stats> {
        private Storage() {
            super("ore_slurry", Stats.class, 1, VERSION, false);
        }

        public boolean isCreated(World world, ChunkCoordIntPair chunk) {
            return super.isCreated(world.provider.dimensionId, chunk.chunkXPos, chunk.chunkZPos);
        }

        @Override
        public void loadAll(World world) {
            super.loadAll(world);
        }

        @Override
        protected void writeElement(DataOutput output, Stats element, World world, int chunkX, int chunkZ)
                throws IOException {
            output.writeUTF(element.oreMix());
            output.writeInt(element.oresCurrent());
            output.writeInt(element.oresPlaced());
        }

        @Override
        protected Stats readElement(DataInput input, int version, World world, int chunkX, int chunkZ)
                throws IOException {
            if (version != VERSION) {
                throw new IOException("Region file corrupted");
            }

            return Stats.builder()
                        .oreMix(input.readUTF())
                        .oresCurrent(input.readInt())
                        .oresPlaced(input.readInt())
                        .build();
        }

        @Override
        protected Stats createElement(World world, int chunkX, int chunkZ) {
            return Stats.DEFAULT.toBuilder()
                                .build();
        }
    }

    @Getter
    @Setter
    @Accessors(fluent = true)
    @Builder(toBuilder = true)
    public static final class Stats implements GT_ChunkAssociatedData.IData {
        public static final GT_OreVeinStats.Stats DEFAULT = GT_OreVeinStats.Stats.builder()
                                                                                 .oreMix(GT_Worldgen_GT_Ore_Layer.EMPTY_VEIN.mWorldGenName)
                                                                                 .oresPlaced(0)
                                                                                 .oresCurrent(0)
                                                                                 .build();
        private String oreMix;
        private int oresPlaced;
        private int oresCurrent;

        @Override
        public boolean isSameAsDefault() {
            return DEFAULT.oreMix().equals(this.oreMix)
                   &&
                   DEFAULT.oresPlaced() == this.oresPlaced
                   &&
                   DEFAULT.oresCurrent() == this.oresCurrent;
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

        if (chunkData.isSameAsDefault()) {
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

            chunkData.oreMix(oreMix)
                     .oresPlaced(oresPlaced)
                     .oresCurrent(oresCurrent);
        }
    }
}
