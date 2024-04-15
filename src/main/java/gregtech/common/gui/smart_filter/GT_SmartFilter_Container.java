package gregtech.common.gui.smart_filter;


import gregtech.api.interfaces.IDWSCompatible;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;


@RequiredArgsConstructor
@Getter
public class GT_SmartFilter_Container extends Container implements IDWSCompatible {

    private final EntityPlayer owner;
    private final ItemStack stack;

    @Override
    public int baseWidth() {
        return 338;
    }

    @Override
    public int getDWSWidthBump() {
        return 0;
    }

    @Override
    public boolean canInteractWith(final EntityPlayer entityPlayer) {
        return true;
    }

}
