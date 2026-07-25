package gregtech.common.misc;

import gregtech.common.GT_OreVeinStats;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ClientOreVeinStats {
    private static final Map<Integer, Map<ChunkCoordIntPair, GT_OreVeinStats.Stats>> CLIENT_MAP = new HashMap<>();

    public static void updateClientData(int dimId, ChunkCoordIntPair chunkPos, GT_OreVeinStats.Stats stats) {
        ClientOreVeinStats.CLIENT_MAP.computeIfAbsent(dimId, id -> new HashMap<>())
                                     .put(chunkPos, stats);
    }

    @NotNull
    public static GT_OreVeinStats.Stats getVeinStats(World world, int chunkX, int chunkY) {
        val chunkCoord = new ChunkCoordIntPair(chunkX, chunkY);

        return CLIENT_MAP.getOrDefault(world.provider.dimensionId, Collections.emptyMap())
                         .getOrDefault(chunkCoord, GT_OreVeinStats.Stats.DEFAULT);
    }

    public static class EventHandler {
        @SubscribeEvent
        public void onPlayerLeave(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
            // in the rare event someone is playing on multiple MEGA servers at once
            CLIENT_MAP.clear();
        }
    }
}
