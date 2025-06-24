package gregtech.common.misc;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public final class StackUtil {
    public static NBTTagCompound getOrCreateNbtData(ItemStack itemStack) {
        NBTTagCompound ret = itemStack.getTagCompound();
        if (ret == null) {
            ret = new NBTTagCompound();
            itemStack.setTagCompound(ret);
        }

        return ret;
    }

    public static boolean isStackEqual(ItemStack stack1, ItemStack stack2) {
        return stack1 == null && stack2 == null
               || stack1 != null
                  && stack2 != null
                  && stack1.getItem() == stack2.getItem()
                  && (!stack1.getHasSubtypes() && !stack1.isItemStackDamageable() || stack1.getItemDamage() == stack2.getItemDamage());
    }

    public static boolean isStackEqualStrict(ItemStack stack1, ItemStack stack2) {
        return isStackEqual(stack1, stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }

    public static Block getBlock(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof ItemBlock ? ((ItemBlock)item).field_150939_a : null;
    }

    public static boolean equals(Block block, ItemStack stack) {
        return block == getBlock(stack);
    }

    public static boolean storeInventoryItem(ItemStack stack, EntityPlayer player, boolean simulate) {
        if (simulate) {
            for (int i = 0; i < player.inventory.mainInventory.length; i++) {
                ItemStack invStack = player.inventory.mainInventory[i];
                if (invStack == null
                    || isStackEqualStrict(stack, invStack)
                       && invStack.stackSize + stack.stackSize <= Math.min(player.inventory.getInventoryStackLimit(), invStack.getMaxStackSize())) {
                    return true;
                }
            }
        } else if (player.inventory.addItemStackToInventory(stack)) {
            if (!player.worldObj.isRemote) {
                player.openContainer.detectAndSendChanges();
            }

            return true;
        }

        return false;
    }

}
