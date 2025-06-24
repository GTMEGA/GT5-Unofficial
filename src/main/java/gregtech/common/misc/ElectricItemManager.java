package gregtech.common.misc;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ElectricItemManager implements IElectricItemManager {
    @Override
    public double charge(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
        IElectricItem item = (IElectricItem)stack.getItem();

        assert item.getMaxCharge(stack) > 0.0;

        if (!(amount < 0.0) && stack.stackSize <= 1 && item.getTier(stack) <= tier) {
            if (!ignoreTransferLimit && amount > item.getTransferLimit(stack)) {
                amount = item.getTransferLimit(stack);
            }

            NBTTagCompound tNBT = StackUtil.getOrCreateNbtData(stack);
            double newCharge = tNBT.getDouble("charge");
            amount = Math.min(amount, item.getMaxCharge(stack) - newCharge);
            if (!simulate) {
                newCharge += amount;
                if (newCharge > 0.0) {
                    tNBT.setDouble("charge", newCharge);
                } else {
                    tNBT.removeTag("charge");
                    if (tNBT.hasNoTags()) {
                        stack.setTagCompound(null);
                    }
                }

                stack.func_150996_a(newCharge > 0.0 ? item.getChargedItem(stack) : item.getEmptyItem(stack));
                if (stack.getItem() instanceof IElectricItem) {
                    item = (IElectricItem)stack.getItem();
                    int maxDamage = DamageHandler.getMaxDamage(stack);
                    if (maxDamage > 2) {
                        DamageHandler.setDamage(stack, maxDamage - 1 - (int)Util.map(newCharge, item.getMaxCharge(stack), (double)(maxDamage - 2)));
                    } else {
                        DamageHandler.setDamage(stack, 0);
                    }
                } else {
                    DamageHandler.setDamage(stack, 0);
                }
            }

            return amount;
        } else {
            return 0.0;
        }
    }

    @Override
    public double discharge(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
        if (stack == null || !(stack.getItem() instanceof IElectricItem)) {
            return 0.0;
        }

        var item = (IElectricItem) stack.getItem();

        if (amount < 0.0 || stack.stackSize > 1 || item.getTier(stack) > tier) {
            return 0.0;
        }

        if (externally && !item.canProvideEnergy(stack)) {
            return 0.0;
        }

        if (!ignoreTransferLimit && amount > item.getTransferLimit(stack)) {
            amount = item.getTransferLimit(stack);
        }

        NBTTagCompound tNBT = StackUtil.getOrCreateNbtData(stack);
        double newCharge = tNBT.getDouble("charge");
        amount = Math.min(amount, newCharge);

        if (simulate) {
            return amount;
        }

        newCharge -= amount;
        if (newCharge > 0.0) {
            stack.func_150996_a(item.getChargedItem(stack));

            tNBT.setDouble("charge", newCharge);
        } else {
            stack.func_150996_a(item.getEmptyItem(stack));

            tNBT.removeTag("charge");
            if (tNBT.hasNoTags()) {
                stack.setTagCompound(null);
            }
        }

        if (stack.getItem() instanceof IElectricItem) {
            item = (IElectricItem)stack.getItem();
            int maxDamage = DamageHandler.getMaxDamage(stack);
            if (maxDamage > 2) {
                DamageHandler.setDamage(stack, maxDamage - 1 - (int)Util.map(newCharge, item.getMaxCharge(stack), (double)(maxDamage - 2)));
            } else {
                DamageHandler.setDamage(stack, 0);
            }
        } else {
            DamageHandler.setDamage(stack, 0);
        }

        return amount;

    }

    @Override
    public double getCharge(ItemStack itemStack) {
        return ElectricItem.manager.discharge(itemStack, Double.POSITIVE_INFINITY, Integer.MAX_VALUE, true, false, true);
    }

    @Override
    public boolean canUse(ItemStack itemStack, double amount) {
        return ElectricItem.manager.getCharge(itemStack) >= amount;
    }

    @Override
    public boolean use(ItemStack itemStack, double amount, EntityLivingBase entity) {
        ElectricItem.manager.chargeFromArmor(itemStack, entity);
        double transfer = ElectricItem.manager.discharge(itemStack, amount, Integer.MAX_VALUE, true, false, true);
        if (Util.isSimilar(transfer, amount)) {
            ElectricItem.manager.discharge(itemStack, amount, Integer.MAX_VALUE, true, false, false);
            ElectricItem.manager.chargeFromArmor(itemStack, entity);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity) {
        boolean transferred = false;

        for (int i = 0; i < 4; i++) {
            ItemStack armorItemStack = entity.getEquipmentInSlot(i + 1);
            if (armorItemStack != null) {
                int tier;
                if (armorItemStack.getItem() instanceof IElectricItem) {
                    tier = ((IElectricItem)armorItemStack.getItem()).getTier(itemStack);
                } else {
                    tier = Integer.MAX_VALUE;
                }

                double transfer = ElectricItem.manager.discharge(armorItemStack, Double.POSITIVE_INFINITY, Integer.MAX_VALUE, true, true, true);
                if (!(transfer <= 0.0)) {
                    transfer = ElectricItem.manager.charge(itemStack, transfer, tier, true, false);
                    if (!(transfer <= 0.0)) {
                        ElectricItem.manager.discharge(armorItemStack, transfer, Integer.MAX_VALUE, true, true, false);
                        transferred = true;
                    }
                }
            }
        }

        if (transferred && entity instanceof EntityPlayer && !entity.worldObj.isRemote) {
            ((EntityPlayer)entity).openContainer.detectAndSendChanges();
        }
    }

    @Override
    public String getToolTip(ItemStack itemStack) {
        double charge = ElectricItem.manager.getCharge(itemStack);
        double space = ElectricItem.manager.charge(itemStack, Double.POSITIVE_INFINITY, Integer.MAX_VALUE, true, true);
        return Util.toSiString(charge, 3) + "/" + Util.toSiString(charge + space, 3) + " EU";
    }
}
