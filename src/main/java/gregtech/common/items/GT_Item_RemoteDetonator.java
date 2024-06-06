package gregtech.common.items;


import com.google.common.io.ByteArrayDataInput;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.IPacketReceivableItem;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.blocks.GT_Block_Explosive;
import gregtech.common.misc.explosions.detonator_util.RemoteDetonationTargetList;
import gregtech.common.misc.explosions.detonator_util.RemoteDetonatorArmedUpdate;
import gregtech.common.misc.explosions.detonator_util.RemoteDetonatorDelayUpdate;
import lombok.NonNull;
import lombok.val;
import lombok.var;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.List;


public class GT_Item_RemoteDetonator extends GT_Generic_Item implements IPacketReceivableItem {


    public GT_Item_RemoteDetonator() {
        super("remote_detonator", "Remote Detonator", "Triggers GregTech explosives remotely.");
        setMaxStackSize(1);
        setMaxDamage(0);
        setHasSubtypes(false);
    }

    /**
     * @param aWorld
     *         The world
     * @param aX
     *         The X Position
     * @param aY
     *         The X Position
     * @param aZ
     *         The X Position
     * @param aPlayer
     *         The Player that is wielding the item
     *
     * @return True to prevent any further processing
     */
    @Override
    public boolean doesSneakBypassUse(final World aWorld, final int aX, final int aY, final int aZ, final EntityPlayer aPlayer) {
        return false;
    }

    @SuppressWarnings(
            {
                    "rawtypes",
                    "unchecked"
            }
    )
    @Override
    protected void addAdditionalToolTips(final List aList, final ItemStack aStack, final EntityPlayer aPlayer) {
        val compound                   = validateNBT(aStack);
        val remoteDetonationTargetList = RemoteDetonationTargetList.readFromNBT(compound, aPlayer);
        aList.add("Can prime and detonate multiple GregTech explosives sequentially.");
        aList.add("Has a transmission range of 256 blocks.");
        val numTargets = compound.getInteger("num");
        if (numTargets > 0) {
            aList.add(String.format("Explosives armed: %d", numTargets));
        }
        aList.add(String.format("Delay: %d ticks", remoteDetonationTargetList.getDelay()));
        aList.add("Press Shift+RMB to trigger");
        val temp = GT_Values.KB.getKeyBindings().get("key.remote_detonator.gui");
        if (temp.getKeyCode() > 0) {
            aList.add(String.format("Press %s to open GUI", Keyboard.getKeyName(temp.getKeyCode())));
        } else {
            aList.add("Press [unbound] to open GUI");
        }
        if (remoteDetonationTargetList.isTriggered()) {
            aList.add(EnumChatFormatting.DARK_RED + "DETONATING!" + EnumChatFormatting.GRAY + " Stand Clear!");
        }
        aStack.setTagCompound(remoteDetonationTargetList.writeToNBT(compound));
    }

    @Override
    public void onCreated(final ItemStack aStack, final World aWorld, final EntityPlayer aPlayer) {
        val compound                   = validateNBT(aStack);
        val remoteDetonationTargetList = RemoteDetonationTargetList.readFromNBT(compound, aPlayer);
        aStack.setTagCompound(remoteDetonationTargetList.writeToNBT(compound));
    }

    @NonNull
    public NBTTagCompound validateNBT(final @NonNull ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return new NBTTagCompound();
        }
        return stack.getTagCompound();
    }

    @Override
    public void onPacketReceived(final World world, final EntityPlayer player, final ItemStack stack, final ISerializableObject data) {
        if (data instanceof RemoteDetonatorDelayUpdate) {
            val remoteDetonationTargetList = getRemoteDetonationTargetList(stack, player);
            val delayUpdate                = (RemoteDetonatorDelayUpdate) data;
            remoteDetonationTargetList.setDelay(delayUpdate.getDelay());
            stack.setTagCompound(remoteDetonationTargetList.writeToNBT());
        } else if (data instanceof RemoteDetonatorArmedUpdate) {
            val remoteDetonationTargetList = getRemoteDetonationTargetList(stack, player);
            val target                     = ((RemoteDetonatorArmedUpdate) data).getTarget();
            val armed                      = ((RemoteDetonatorArmedUpdate) data).isArmed();
            if (armed) {
                remoteDetonationTargetList.addTarget(target.getExplosiveType(), target.getTier(), target.getX(), target.getY(), target.getZ());
            } else {
                remoteDetonationTargetList.removeTarget(target.getX(), target.getY(), target.getZ());
            }
            stack.setTagCompound(remoteDetonationTargetList.writeToNBT());
        }
    }

    @Nullable
    @Override
    public ISerializableObject readFromBytes(final ByteArrayDataInput aData) {
        switch (aData.readByte()) {
            case 1:
                return new RemoteDetonatorDelayUpdate().readFromPacket(aData, null);
            case 2:
                return new RemoteDetonatorArmedUpdate().readFromPacket(aData, null);
        }
        return null;
    }

    public RemoteDetonationTargetList getRemoteDetonationTargetList(final ItemStack stack, final EntityPlayer player) {
        val targetList = RemoteDetonationTargetList.readFromNBT(validateNBT(stack), player);
        stack.setTagCompound(targetList.writeToNBT());
        return targetList;
    }

    @Override
    public ItemStack onItemRightClick(final ItemStack stack, final World world, final EntityPlayer player) {
        if (player.isSneaking()) {
            itemUse(stack, world, player, (int) player.posX, (int) player.posY, (int) player.posZ);
        }
        return stack;
    }

    @Override
    public void onUpdate(final ItemStack stack, final World world, final Entity entity, final int slotIndex, final boolean current) {
        if (entity instanceof EntityPlayer) {
            val compound                   = validateNBT(stack);
            val remoteDetonationTargetList = RemoteDetonationTargetList.readFromNBT(compound, (EntityPlayer) entity);
            remoteDetonationTargetList.tick(world, (EntityPlayer) entity, (int) entity.posX, (int) entity.posY, (int) entity.posZ);
            stack.setTagCompound(remoteDetonationTargetList.writeToNBT(compound));
        }
    }

    /**
     * This is called when the item is used, before the block is activated.
     *
     * @param stack
     *         The Item Stack
     * @param player
     *         The Player that used the item
     * @param world
     *         The Current World
     * @param x
     *         Target X Position
     * @param y
     *         Target Y Position
     * @param z
     *         Target Z Position
     * @param side
     *         The side of the target hit
     * @param hitX
     *         The X Position of the target hit
     * @param hitY
     *         The Y Position of the target hit
     * @param hitZ
     *         The Z Position of the target hit
     *
     * @return Return true to prevent any further processing.
     */
    @Override
    public boolean onItemUseFirst(final ItemStack stack, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ) {
        return itemUse(stack, world, player, x, y, z);
    }

    private boolean itemUse(final @NonNull ItemStack stack, final @NonNull World world, final @NonNull EntityPlayer player, final int x, final int y, final int z) {
        val remoteDetonationTargetList = getRemoteDetonationTargetList(stack, player);
        if (player.isSneaking()) {
            trigger(world, player, remoteDetonationTargetList, x, y, z);
        } else {
            val target = world.getBlock(x, y, z);
            if (target instanceof GT_Block_Explosive) {
                var valid = remoteDetonationTargetList.validDimension(player);
                if (valid) {
                    if (remoteDetonationTargetList.containsTarget(x, y, z)) {
                        removeTarget(world, player, remoteDetonationTargetList, x, y, z);
                    } else {
                        addTarget(world, player, remoteDetonationTargetList, x, y, z);
                    }
                } else {
                    sendChat(world, player, "Cannot trigger THAT remotely...");
                }
            }
        }
        stack.setTagCompound(remoteDetonationTargetList.writeToNBT());
        return !world.isRemote;
    }

    public void trigger(final @NonNull World aWorld, final @NonNull EntityPlayer player, final @NonNull RemoteDetonationTargetList remoteDetonationTargetList, final int x, final int y, final int z) {
        if (remoteDetonationTargetList.validDimension(player)) {
            aWorld.playSoundEffect(x, y, z, GregTech_API.sSoundList.get(220), 8.0f, aWorld.rand.nextFloat() + 1.0f);
            remoteDetonationTargetList.trigger(aWorld, player);
        } else {
            sendChat(aWorld, player, "Nothing to detonate!");
        }
    }

    public void removeTarget(final @NonNull World aWorld, final @NonNull EntityPlayer player, final @NonNull RemoteDetonationTargetList remoteDetonationTargetList, final int x, final int y, final int z) {
        final boolean contains, validDim;
        val           target = aWorld.getBlock(x, y, z);
        contains = remoteDetonationTargetList.containsTarget(x, y, z);
        validDim = remoteDetonationTargetList.validDimension(player);
        if (contains && validDim && target instanceof GT_Block_Explosive) {
            remoteDetonationTargetList.removeTarget(x, y, z);
            ((GT_Block_Explosive<?>) target).setPrimed(aWorld, x, y, z, false);
            aWorld.playSoundEffect(x, y, z, GregTech_API.sSoundList.get(219), 4.0f, aWorld.rand.nextFloat() + 1.0f);
            sendChat(aWorld, player, String.format("Removed target (%d %d %d)", x, y, z));
        } else if (!validDim) {
            sendChat(aWorld, player, "Out of range!");
        }
    }

    public void addTarget(final @NonNull World aWorld, final @NonNull EntityPlayer player, final @NonNull RemoteDetonationTargetList remoteDetonationTargetList, final int x, final int y, final int z) {
        val target = aWorld.getBlock(x, y, z);
        if (target instanceof GT_Block_Explosive) {
            if (remoteDetonationTargetList.validDimension(player)) {
                val expTarget = (GT_Block_Explosive<?>) target;
                val tierInfo  = expTarget.getTier();
                remoteDetonationTargetList.addTarget(RemoteDetonationTargetList.ExplosiveType.getType(expTarget), tierInfo, x, y, z);
                expTarget.setPrimed(aWorld, x, y, z, true);
                aWorld.playSoundEffect(x, y, z, GregTech_API.sSoundList.get(218), 4.0f, aWorld.rand.nextFloat() + 1.0f);
                sendChat(aWorld, player, String.format("Added target (%d %d %d)", x, y, z));
            } else {
                sendChat(aWorld, player, "Unable to add target, do you have them in multiple dimensions?");
            }
        }
    }

    private void sendChat(final @NonNull World world, final @NonNull EntityPlayer player, final @NonNull String msg) {
        if (!world.isRemote) {
            GT_Utility.sendChatToPlayer(player, msg);
        }
    }

}
