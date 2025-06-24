package gregtech.common.misc;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GatewayElectricItemManager implements IElectricItemManager {
    @Override
    public double charge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
        IElectricItemManager manager = this.getManager(itemStack);
        return manager == null ? 0.0 : manager.charge(itemStack, amount, tier, ignoreTransferLimit, simulate);
    }

    @Override
    public double discharge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
        IElectricItemManager manager = this.getManager(itemStack);

        if (manager == null) {
            return 0.0;
        }

        return manager.discharge(itemStack, amount, tier, ignoreTransferLimit, externally, simulate);
    }

    @Override
    public double getCharge(ItemStack itemStack) {
        IElectricItemManager manager = this.getManager(itemStack);

        if (manager == null) {
            return 0.0;
        }

        return manager.getCharge(itemStack);
    }

    @Override
    public boolean canUse(ItemStack itemStack, double amount) {
        IElectricItemManager manager = this.getManager(itemStack);

        if (manager == null) {
            return false;
        }

        return manager.canUse(itemStack, amount);
    }

    @Override
    public boolean use(ItemStack itemStack, double amount, EntityLivingBase entity) {
        IElectricItemManager manager = this.getManager(itemStack);

        if (manager == null) {
            return false;
        }

        return manager.use(itemStack, amount, entity);
    }

    @Override
    public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity) {
        if (entity != null) {
            IElectricItemManager manager = this.getManager(itemStack);
            if (manager != null) {
                manager.chargeFromArmor(itemStack, entity);
            }
        }
    }

    @Override
    public String getToolTip(ItemStack itemStack) {
        IElectricItemManager manager = this.getManager(itemStack);
        return manager == null ? null : manager.getToolTip(itemStack);
    }

    private IElectricItemManager getManager(ItemStack stack) {
        Item item = stack.getItem();
        if (item == null) {
            return null;
        } else if (item instanceof ISpecialElectricItem) {
            return ((ISpecialElectricItem)item).getManager(stack);
        } else {
            if (item instanceof IElectricItem) {
                return ElectricItem.rawManager;
            }
        }

        return null;
    }
}
