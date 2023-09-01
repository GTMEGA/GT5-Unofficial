package gregtech.api.interfaces;


import gregtech.api.enums.HeatingCoilLevel;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;


public interface IHeatingCoil {

    default HeatingCoilLevel getCoilHeat(ItemStack stack) {
        return getCoilHeat(stack.getItemDamage());
    }

    HeatingCoilLevel getCoilHeat(int meta);

    Consumer<IHeatingCoil> getOnCoilCheck();

    void setOnCoilCheck(Consumer<IHeatingCoil> callback);

}
