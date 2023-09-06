package gregtech.common.gui.remotedetonator;

import gregtech.api.GregTech_API;
import gregtech.common.items.explosives.GT_RemoteDetonator;
import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

@Getter
public class GT_RemoteDetonator_Container extends Container {

    public static final GT_RemoteDetonator REMOTE_DETONATOR = GregTech_API.sItemRemoteDetonator;

    private final EntityPlayer player;

    private final ItemStack stack;

    private final GT_RemoteDetonator.RemoteDetonationTargetList targetList;

    private final int slotIndex;

    private final boolean bauble;

    public GT_RemoteDetonator_Container(final EntityPlayer player, final ItemStack remoteDetonator, final int slotIndex, final boolean bauble) {
        this.player = player;
        this.stack = remoteDetonator;
        this.targetList = REMOTE_DETONATOR.getRemoteDetonationTargetList(remoteDetonator, player);
        this.slotIndex = slotIndex;
        this.bauble = bauble;
    }

    @Override
    public boolean canInteractWith(final EntityPlayer entityPlayer) {
        return true;
    }

}
