package gregtech.common.gui.meganet;

import gregtech.common.items.GT_MEGAnet;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

@Getter
public class GT_MEGAnet_Container extends Container {

    private @NonNull final EntityPlayer owner;

    private @NonNull final ItemStack source;

    private @NonNull final GT_MEGAnet.MEGAnetFilter filter;

    public GT_MEGAnet_Container(final @NonNull EntityPlayer owner, final @NonNull ItemStack source, final GT_MEGAnet.@NonNull MEGAnetFilter filter) {
        super();
        this.owner = owner;
        this.source = source;
        this.filter = filter;
        addSlots();
    }

    private void addSlots() {
        
    }

    @Override
    public boolean canInteractWith(final EntityPlayer player) {
        return true;
    }

    public void synchronize() {
        if (!(source.getItem() instanceof GT_MEGAnet)) {
            return;
        }
        ((GT_MEGAnet) source.getItem()).setMEGANetFilter(source, filter);
    }

}
