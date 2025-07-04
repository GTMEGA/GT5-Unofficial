package gregtech.common.misc.commands;

import com.gtnewhorizon.structurelib.commands.SubCommand;
import gregtech.api.events.GT_OreVeinLocations;
import lombok.val;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

public class GT_MEGA_Command_ReScanOreVein extends SubCommand {
    public GT_MEGA_Command_ReScanOreVein() {
        super("rescan");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 1 || "help".equals(args[0])) {
            this.printHelp(sender, "rescan");

            return;
        }


        val start = System.nanoTime();
        int radius;

        if ("Server".equals(sender.getCommandSenderName())) {
            if (args.length != 3) {
                this.printHelp(sender, "rescan");

                return;
            }

            val x = Integer.parseInt(args[0]);
            val z = Integer.parseInt(args[1]);
            radius = Integer.parseInt(args[2]);

            this.regenAroundPosition(sender.getEntityWorld(), x, z, radius);
        } else {
            if (args.length != 1) {
                this.printHelp(sender, "rescan");

                return;
            }

            val position = sender.getPlayerCoordinates();
            radius = Integer.parseInt(args[0]);

            this.regenAroundPosition(sender.getEntityWorld(), position.posX, position.posZ, radius);
        }

        val end = System.nanoTime();

        sender.addChatMessage(new ChatComponentText(String.format("Scanned %d chunks in %.02fms", 4 * radius * radius, (end - start) / 1_000_000D)));
    }

    @Override
    public void printHelp(ICommandSender sender, String command) {
        if ("Server".equals(sender.getCommandSenderName())) {
            val message = """
            Scans chunks in a square area centered on the chunk at [x, z]
            usage: /mega rescan <x> <z> <radius>
            """.trim();

            sender.addChatMessage(new ChatComponentText(message));
        } else {
            val message = """
            Scans chunks in a square area centered on the player invoking this command
            usage: /mega rescan <radius>
            """.trim();

            sender.addChatMessage(new ChatComponentText(message));
        }
    }

    private void regenAroundPosition(World world, int x, int z, int radius) {
        val centerChunkX = x >> 4;
        val centerChunkZ = z >> 4;

        for (var chunkX = centerChunkX - radius; chunkX < centerChunkX + radius; chunkX++) {
            for (var chunkZ = centerChunkZ - radius; chunkZ < centerChunkZ + radius; chunkZ++) {
                GT_OreVeinLocations.scanSlurryInChunkAt(world, chunkX, chunkZ);
            }
        }

        val chunkCoord = new ChunkCoordIntPair(centerChunkX, centerChunkZ);
        val veinData = GT_OreVeinLocations.RecordedOreVeinInChunk.get()
                                          .get(world.provider.dimensionId, chunkCoord);

        GT_OreVeinLocations.updateClients(world.provider.dimensionId, chunkCoord, veinData);
    }
}
