package gregtech.common.gui.remotedetonator;

import gregtech.api.GregTech_API;
import gregtech.common.items.explosives.GT_RemoteDetonator;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Getter
public class GT_RemoteDetonator_Container extends Container {

    public static final GT_RemoteDetonator REMOTE_DETONATOR = GregTech_API.sItemRemoteDetonator;

    private final EntityPlayer owner;

    private final ItemStack stack;

    private final GT_RemoteDetonator.RemoteDetonationTargetList targetList;

    private final int slotIndex;

    private final boolean bauble;

    public GT_RemoteDetonator_Container(final @NonNull EntityPlayer owner, final @NonNull ItemStack remoteDetonator, final int slotIndex, final boolean bauble) {
        this.owner = owner;
        this.stack = remoteDetonator;
        this.targetList = REMOTE_DETONATOR.getRemoteDetonationTargetList(remoteDetonator, owner);
        this.slotIndex = slotIndex;
        this.bauble = bauble;
    }

    @Override
    public boolean canInteractWith(final EntityPlayer entityPlayer) {
        return true;
    }

    public void synchronize() {

    }

    @NonNull
    public World getWorld() {
        return owner.worldObj;
    }

}
