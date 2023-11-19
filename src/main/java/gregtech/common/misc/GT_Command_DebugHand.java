package gregtech.common.misc;


import gregtech.api.util.GT_Utility;
import lombok.val;
import lombok.var;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;


public class GT_Command_DebugHand extends CommandBase {

    /**
     * @return
     */
    @Override
    public String getCommandName() {
        return "gt_debug_hand";
    }

    /**
     * @param sender
     * @return
     */
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "Usage: /gt_debug_hand [client|server (default)]";
    }

    /**
     * @param sender
     * @param args
     */
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) {
        var isClient = false;
        if (args.length == 0){

        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("client") || args[0].equalsIgnoreCase("server")) {
                isClient = args[0].equalsIgnoreCase("client");
            } else {
                doUsage(sender);
                return;
            }
        } else {
            doUsage(sender);
            return;
        }
        doAndSend(sender, isClient);
    }

    private void doUsage(final ICommandSender sender) {
        if (sender instanceof EntityPlayer) {
            GT_Utility.sendChatToPlayer((EntityPlayer) sender, getCommandUsage(sender));
        }
    }

    private void doAndSend(final ICommandSender sender, final boolean isClient) {
        if (sender instanceof EntityPlayer) {
            val player = (EntityPlayer) sender;
            GT_Utility.sendChatToPlayer(player, "DebugHand: " + (isClient ? "Client" : "Server"));
            val currentStack = player.getCurrentEquippedItem();
            if (currentStack != null) {
                GT_Utility.sendChatToPlayer(player, "Current Item: " + currentStack.getDisplayName());
                GT_Utility.sendChatToPlayer(player, "Current Item NBT: " + currentStack.getTagCompound());
            } else {
                GT_Utility.sendChatToPlayer(player, "Current Item: null");
            }

        }
    }

}
