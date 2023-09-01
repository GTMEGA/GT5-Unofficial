package gregtech.api.util.interop;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;


public abstract class BaublesInteropBase {

    public Iterable<ItemStack> getBaubles(EntityPlayer player) {
        return new ArrayList<>();
    }

    public ItemStack getBauble(EntityPlayer player, int slot) {
        return null;
    }

    public boolean isBaublesLoaded() {
        return false;
    }

}
