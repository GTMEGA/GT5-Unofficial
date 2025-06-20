package gregtech.common.misc.commands;

import com.gtnewhorizon.structurelib.commands.SubCommand;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class MEGA_Commands extends SubCommand {
    public MEGA_Commands() {
        super("mega");
        this.setPermLevel(PermLevel.ADMIN);

        this.addChildCommand(new GT_MEGA_Command_ReScanOreVein());
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        try {
            this.processChildCommand(sender, args);
        } catch (Exception e) {
            if (args.length > 0) {
                sender.addChatMessage(new ChatComponentText(String.format("Failed to process command %s: %s", args[0], e.getMessage())));
            } else {
                this.printHelp(sender, "help");
            }
        }
    }

    @Override
    public void printHelp(ICommandSender sender, String command) {
        sender.addChatMessage(new ChatComponentText("valid options are: " + String.join(", ", this.children.keySet())));
    }
}
