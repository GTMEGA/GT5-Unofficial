package gregtech.common.misc;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DamageHandler {

    public static void setDamage(ItemStack stack, int damage) {
        Item item = stack.getItem();

        if (item != null) {
            stack.setItemDamage(damage);
        }
    }

    public static int getMaxDamage(ItemStack stack) {
        Item item = stack.getItem();
        if (item == null) {
            return 0;
        } else {
            return stack.getMaxDamage();
        }
    }
}
