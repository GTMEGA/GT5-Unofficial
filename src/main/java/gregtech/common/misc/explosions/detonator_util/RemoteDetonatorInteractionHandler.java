package gregtech.common.misc.explosions.detonator_util;

import gregtech.api.enums.GT_Values;
import gregtech.api.net.GT_Packet_OpenGUI;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.remotedetonator.GT_RemoteDetonator_Container;
import gregtech.common.gui.remotedetonator.GT_RemoteDetonator_GuiContainer;
import gregtech.common.items.GT_Item_RemoteDetonator;
import lombok.val;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

public class RemoteDetonatorInteractionHandler {

    public static final RemoteDetonatorInteractionHandler INSTANCE = new RemoteDetonatorInteractionHandler();

    private RemoteDetonatorInteractionHandler() {

    }

    public void openGUI(EntityPlayer player) {
        GT_Values.NW.sendToServer(new GT_Packet_OpenGUI(player, -2));
    }

    public GT_RemoteDetonator_Container getServerGUI(final EntityPlayer player) {
        val slotIndex       = new MutableInt(-1);
        val bauble          = new MutableBoolean(false);
        val remoteDetonator = RemoteDetonatorInteractionHandler.INSTANCE.getPlayerRemoteDetonator(player, slotIndex, bauble);
        if (remoteDetonator != null) {
            return new GT_RemoteDetonator_Container(player, remoteDetonator, slotIndex.intValue(), bauble.booleanValue());
        }
        return null;
    }

    public ItemStack getPlayerRemoteDetonator(EntityPlayer player, MutableInt slot, MutableBoolean bauble) {
        return GT_Utility.getItemInPlayerInventory(player, stack -> stack.getItem() instanceof GT_Item_RemoteDetonator, slot, bauble);
    }

    public GT_RemoteDetonator_GuiContainer getClientGUI(final EntityPlayer player) {
        val slotIndex       = new MutableInt(-1);
        val bauble          = new MutableBoolean(false);
        val remoteDetonator = RemoteDetonatorInteractionHandler.INSTANCE.getPlayerRemoteDetonator(player, slotIndex, bauble);
        if (remoteDetonator != null) {
            return new GT_RemoteDetonator_GuiContainer(new GT_RemoteDetonator_Container(player, remoteDetonator, slotIndex.intValue(), bauble.booleanValue()));
        }
        return null;
    }

}
