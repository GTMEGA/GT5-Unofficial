package gregtech.common.gui.prosp_toolkit;


import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IDWSCompatible;
import gregtech.common.items.ItemProspectingToolkit;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;


@Getter
public class ProspectorsToolkitContainer extends Container implements IDWSCompatible {

    public static @NonNull final ItemProspectingToolkit PROSPECTING_TOOLKIT = GregTech_API.sItemProspectingToolkit;

    private @NonNull final EntityPlayer owner;

    private @NonNull final ItemStack stack;

    public ProspectorsToolkitContainer(final @NonNull EntityPlayer owner, final @NonNull ItemStack stack) {
        super();
        this.owner = owner;
        this.stack = stack;
        addSlots();
    }

    private void addSlots() {

    }

    /**
     * @return
     */
    @Override
    public int baseWidth() {
        return 338;
    }

    /**
     * @return
     */
    @Override
    public int getDWSWidthBump() {
        return 0;
    }

    /**
     * @param player
     * @return
     */
    @Override
    public boolean canInteractWith(final EntityPlayer player) {
        return true;
    }

}
