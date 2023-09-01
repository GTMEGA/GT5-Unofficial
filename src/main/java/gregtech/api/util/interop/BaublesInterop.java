package gregtech.api.util.interop;


import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.Optional;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Arrays;


public class BaublesInterop extends BaublesInteropBase {

    public static final BaublesInterop INSTANCE = new BaublesInterop();

    private BaublesInterop() {
    }

    @Optional.Method(modid = "Baubles")
    @Override
    public Iterable<ItemStack> getBaubles(EntityPlayer player) {
        return Arrays.asList(PlayerHandler.getPlayerBaubles(player).stackList);
    }

    @Optional.Method(modid = "Baubles")
    @Override
    public ItemStack getBauble(EntityPlayer player, int slot) {
        return PlayerHandler.getPlayerBaubles(player).getStackInSlot(slot);
    }

    @Optional.Method(modid = "Baubles")
    @Override
    public boolean isBaublesLoaded() {
        return true;
    }

}
