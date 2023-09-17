package gregtech.common.items.explosives;


import com.google.common.io.ByteArrayDataInput;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.IPacketReceivableItem;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.net.GT_Packet_OpenGUI;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.blocks.explosives.GT_Block_Explosive;
import gregtech.common.gui.remotedetonator.GT_RemoteDetonator_Container;
import gregtech.common.gui.remotedetonator.GT_RemoteDetonator_GuiContainer;
import io.netty.buffer.ByteBuf;
import lombok.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GT_RemoteDetonator extends GT_Generic_Item implements IPacketReceivableItem {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RemoteDetonatorDelayUpdate implements ISerializableObject {

        private int delay;

        /**
         * @return
         */
        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new RemoteDetonatorDelayUpdate(delay);
        }

        /**
         * @return
         */
        @Deprecated
        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            return new NBTTagCompound();
        }

        /**
         * Write data to given ByteBuf
         * The data saved this way is intended to be stored for short amount of time over network.
         * DO NOT store it to disks.
         *
         * @param aBuf
         */
        @Override
        public void writeToByteBuf(final ByteBuf aBuf) {
            aBuf.writeByte(1);
            aBuf.writeInt(delay);
        }

        /**
         * @param aNBT
         */
        @Deprecated
        @Override
        public void loadDataFromNBT(final NBTBase aNBT) {

        }

        /**
         * Read data from given parameter and return this.
         * The data read this way is intended to be stored for short amount of time over network.
         *
         * @param aBuf
         * @param aPlayer
         */
        @Nonnull
        @Override
        public ISerializableObject readFromPacket(final ByteArrayDataInput aBuf, final EntityPlayerMP aPlayer) {
            return new RemoteDetonatorDelayUpdate(aBuf.readInt());
        }

    }


    public static class RemoteDetonatorInteractionHandler {

        public static final RemoteDetonatorInteractionHandler INSTANCE = new RemoteDetonatorInteractionHandler();

        private RemoteDetonatorInteractionHandler() {

        }

        public void openGUI(EntityPlayer player) {
            GT_Values.NW.sendToServer(new GT_Packet_OpenGUI(player, -2));
        }

        public GT_RemoteDetonator_Container getServerGUI(final EntityPlayer player) {
            val slotIndex = new MutableInt(-1);
            val bauble = new MutableBoolean(false);
            val remoteDetonator = GT_RemoteDetonator.RemoteDetonatorInteractionHandler.INSTANCE.getPlayerRemoteDetonator(player, slotIndex, bauble);
            if (remoteDetonator != null) {
                return new GT_RemoteDetonator_Container(player, remoteDetonator, slotIndex.intValue(), bauble.booleanValue());
            }
            return null;
        }

        public ItemStack getPlayerRemoteDetonator(EntityPlayer player, MutableInt slot, MutableBoolean bauble) {
            return GT_Utility.getItemInPlayerInventory(player, stack -> stack.getItem() instanceof GT_RemoteDetonator, slot, bauble);
        }

        public GT_RemoteDetonator_GuiContainer getClientGUI(final EntityPlayer player) {
            val slotIndex = new MutableInt(-1);
            val bauble = new MutableBoolean(false);
            val remoteDetonator = GT_RemoteDetonator.RemoteDetonatorInteractionHandler.INSTANCE.getPlayerRemoteDetonator(player, slotIndex, bauble);
            if (remoteDetonator != null) {
                return new GT_RemoteDetonator_GuiContainer(new GT_RemoteDetonator_Container(player, remoteDetonator, slotIndex.intValue(), bauble.booleanValue()));
            }
            return null;
        }

    }


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class RemoteDetonationTargetList {

        @Getter
        @RequiredArgsConstructor
        public enum ExplosiveType {
            MINING(GregTech_API.sBlockMiningExplosive, new ExplosiveColorPalette(new Color(187, 102, 41, 0xFF), new Color(133, 69, 22, 0xFF), new Color(98, 98, 98, 0xFF), new Color(138, 137, 137, 0xFF))),
            TUNNEL(GregTech_API.sBlockTunEx, new ExplosiveColorPalette(new Color(133, 84, 10, 0xFF), new Color(80, 40, 4, 0xFF), new Color(124, 124, 124, 0xFF), new Color(89, 89, 89, 0xFF))),
            DAISY_CUTTER(GregTech_API.sBlockDaisyCutter, new ExplosiveColorPalette(new Color(22, 98, 22, 0xFF), new Color(15, 68, 15, 0xFF), new Color(84, 175, 84, 0xFF), new Color(107, 222, 107, 0xFF)));


            @RequiredArgsConstructor
            @Getter
            public static class ExplosiveColorPalette {

                private final Color backgroundColor;

                private final Color backgroundColorSelected;

                private final Color textColor;

                private final Color textColorSelected;


            }

            public static ExplosiveType getType(final GT_Block_Explosive explosive) {
                for (val type : values()) {
                    if (type.getExplosive().equals(explosive)) {
                        return type;
                    }
                }
                return null;
            }

            private final GT_Block_Explosive explosive;

            private final ExplosiveColorPalette palette;

        }


        @Getter
        @NoArgsConstructor
        @Setter
        @AllArgsConstructor
        @EqualsAndHashCode
        @ToString(onlyExplicitlyIncluded = true)
        public static class Target {

            @EqualsAndHashCode.Exclude
            private int index = -1;

            @ToString.Include
            @EqualsAndHashCode.Exclude
            private ExplosiveType explosiveType;

            @ToString.Include
            private int x = -1, y = -1, z = -1;

            @EqualsAndHashCode.Exclude
            private boolean triggered = false;

            @EqualsAndHashCode.Exclude
            private boolean valid = false;

            public Target(final int x, final int y, final int z) {
                this(-1, null, x, y, z, false, false);
            }

            public Target(final int index, final ExplosiveType explosive, final int x, final int y, final int z) {
                this(index, explosive, x, y, z, false, true);
            }

            @NonNull
            public Target addFromCompound(final @NonNull NBTTagCompound compound) {
                setValid(compound.hasKey("index") && compound.hasKey("x") && compound.hasKey("y") && compound.hasKey("z"));
                setIndex(compound.getInteger("index"));
                setExplosiveType(ExplosiveType.values()[compound.getInteger("type")]);
                setX(compound.getInteger("x"));
                setY(compound.getInteger("y"));
                setZ(compound.getInteger("z"));
                setTriggered(compound.getBoolean("t"));
                return this;
            }

            public double getDistance(final EntityPlayer player) {
                val dX = player.posX - x;
                val dY = player.posY - y;
                val dZ = player.posZ - z;
                return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
            }

            @NonNull
            protected NBTTagCompound writeToNBT(final @NonNull NBTTagCompound nbtTagCompound) {
                nbtTagCompound.setInteger("index", index);
                nbtTagCompound.setInteger("type", explosiveType.ordinal());
                nbtTagCompound.setInteger("x", x);
                nbtTagCompound.setInteger("y", y);
                nbtTagCompound.setInteger("z", z);
                nbtTagCompound.setBoolean("t", triggered);
                return nbtTagCompound;
            }

            protected void trigger(final @NonNull World world, final @NonNull EntityPlayer player) {
                val block = world.getBlock(x, y, z);
                val metadata = world.getBlockMetadata(x, y, z);
                if (block instanceof GT_Block_Explosive && !world.isRemote) {
                    val explosive = (GT_Block_Explosive) block;
                    if (explosive.isPrimed(metadata)) {
                        explosive.remoteTrigger(world, x, y, z, player);
                    }
                }
                triggered = true;
            }

            protected boolean canDetonate(final @NonNull World world, final int pX, final int pY, final int pZ) {
                boolean loaded = isLoaded(world), inRange = getDistanceFromTarget(pX, pY, pZ) <= GT_Values.MEMaxRemoteRange;
                return loaded && inRange && !triggered;
            }

            protected boolean isLoaded(final @NonNull World world) {
                return world.getChunkProvider().chunkExists(x >> 4, z >> 4);
            }

            public int getDistanceFromTarget(final int pX, final int pY, final int pZ) {
                return magnitude(pX - this.x, pY - this.y, pZ - this.z);
            }

            private int magnitude(final int x, final int y, final int z) {
                return MathHelper.floor_double(Math.sqrt(x * x + y * y + z * z));
            }

        }

        @NonNull
        public static RemoteDetonationTargetList readFromNBT(final @NonNull NBTTagCompound compound, final @NonNull EntityPlayer player) {
            return compound.hasKey("targets") && compound.hasKey("dim") ? new RemoteDetonationTargetList(compound).addFromList(compound.getTagList("targets", Constants.NBT.TAG_COMPOUND)) : new RemoteDetonationTargetList(player);
        }

        @NonNull
        public RemoteDetonationTargetList addFromList(final @NonNull NBTTagList listComp) {
            for (var i = 0; i < listComp.tagCount(); i++) {
                addTarget(new Target().addFromCompound(listComp.getCompoundTagAt(i)));
            }
            return this;
        }

        private void addTarget(final @NonNull Target target) {
            if (target.isValid() && !targets.containsValue(target) && !targetsReversed.containsKey(target)) {
                getNextIndex(target);
                targets.put(target.getIndex(), target);
                targetsReversed.put(target, target.getIndex());
                maxTarget = Math.max(target.getIndex(), maxTarget);
            }
        }

        private void getNextIndex(final @NonNull Target target) {
            target.setIndex(getNextIndex(target.getIndex()));
        }

        private int getNextIndex(final int initial) {
            var temp = initial;
            while (targets.containsKey(temp)) {
                temp += 1;
            }
            return temp;
        }

        private final Map<Integer, Target> targets = new HashMap<>();

        private final Map<Target, Integer> targetsReversed = new HashMap<>();

        private int dimension;

        @Builder.Default
        private int maxTarget = -1, timer = -1;

        @Builder.Default
        private int delay = GT_Values.MERemoteDelay;

        @Builder.Default
        private boolean triggered = false, done = true;

        public RemoteDetonationTargetList(final @NonNull EntityPlayer player) {
            this(player.dimension, -1, -1, GT_Values.MERemoteDelay, false, false);
        }

        public RemoteDetonationTargetList(final @NonNull NBTTagCompound compound) {
            setDimension(compound.getInteger("dim"));
            setTriggered(compound.getBoolean("active"));
            setDelay(compound.getInteger("delay"));
            setTimer(compound.getInteger("timer"));
            setDone(compound.getBoolean("done"));
        }

        public void clear() {
            maxTarget = -1;
            targets.clear();
            targetsReversed.clear();
        }

        public boolean containsTarget(final int x, final int y, final int z) {
            return targets.containsValue(new Target(x, y, z)) && targetsReversed.containsKey(new Target(x, y, z));
        }

        @NonNull
        public NBTTagCompound writeToNBT(final @NonNull NBTTagCompound compound) {
            if (isDone()) {
                val result = new NBTTagCompound();
                result.setInteger("delay", delay);
                return new NBTTagCompound();
            }
            val list = new NBTTagList();
            for (val target : targets.values()) {
                list.appendTag(target.writeToNBT(new NBTTagCompound()));
            }
            compound.setInteger("num", numTargets());
            compound.setBoolean("active", triggered);
            compound.setInteger("timer", timer);
            compound.setInteger("delay", delay);
            compound.setInteger("dim", dimension);
            compound.setBoolean("done", done);
            compound.setTag("targets", list);
            return compound;
        }

        public int numTargets() {
            return targets.size();
        }

        public void tick(final @NonNull World world, final @NonNull EntityPlayer player, final int x, final int y, final int z) {
            if (!triggered) {
                return;
            }
            if (this.timer++ % delay == 0) {
                targets.values().stream().filter(target -> target.canDetonate(world, x, y, z)).min(Comparator.comparingInt(target -> target.index)).ifPresent(firstValid -> firstValid.trigger(world, player));
            }
            if (!hasUntriggered(world, x, y, z)) {
                setDone(true);
            }
        }

        public boolean hasUntriggered(final @NonNull World world, final int x, final int y, final int z) {
            return targets.values().stream().anyMatch(target -> target.canDetonate(world, x, y, z));
        }

        public void addTarget(final ExplosiveType type, final int x, final int y, final int z) {
            val nextIndex = getNextIndex(maxTarget);
            addTarget(new Target(nextIndex, type, x, y, z));
        }

        public void removeTarget(final int x, final int y, final int z) {
            removeTarget(new Target(x, y, z));
        }

        private void removeTarget(final @NonNull Target target) {
            if (targetsReversed.containsKey(target)) {
                val key = targetsReversed.get(target);
                targetsReversed.remove(target);
                targets.remove(key);
            }
        }

        public void trigger(final @NonNull World world, final @NonNull EntityPlayer player, final int x, final int y, final int z) {
            if (world.isRemote || isEmpty() || !validDimension(player) || triggered || done) {
                return;
            }
            setTriggered(true);
            setTimer(0);
        }

        public boolean isEmpty() {
            return targets.isEmpty();
        }

        public boolean validDimension(final @NonNull EntityPlayer player) {
            return validDimension(player.dimension);
        }

        public boolean validDimension(final int dimension) {
            if (isEmpty()) {
                this.dimension = dimension;
            }
            return this.dimension == dimension;
        }

        private void triggerAll(final @NonNull World world, final @NonNull EntityPlayer player, final int x, final int y, final int z) {
            targets.values().stream().filter(target -> target.canDetonate(world, x, y, z)).sorted(Comparator.comparingInt(target -> target.index)).forEach(target -> {
                target.trigger(world, player);
            });
        }

    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RemoteDetonatorArmedUpdate implements ISerializableObject {

        private RemoteDetonationTargetList.Target target;

        private boolean armed;

        /**
         * @return
         */
        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new RemoteDetonatorArmedUpdate(target, armed);
        }

        /**
         * @return
         */
        @Deprecated
        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            return new NBTTagCompound();
        }

        /**
         * Write data to given ByteBuf
         * The data saved this way is intended to be stored for short amount of time over network.
         * DO NOT store it to disks.
         *
         * @param aBuf
         */
        @Override
        public void writeToByteBuf(final ByteBuf aBuf) {
            aBuf.writeByte(2);
            aBuf.writeInt(target.getExplosiveType().ordinal());
            aBuf.writeInt(target.getX());
            aBuf.writeInt(target.getY());
            aBuf.writeInt(target.getZ());
            aBuf.writeBoolean(armed);
        }

        /**
         * @param aNBT
         */
        @Override
        public void loadDataFromNBT(final NBTBase aNBT) {

        }

        /**
         * Read data from given parameter and return this.
         * The data read this way is intended to be stored for short amount of time over network.
         *
         * @param aBuf
         * @param aPlayer
         */
        @Nonnull
        @Override
        public ISerializableObject readFromPacket(final ByteArrayDataInput aBuf, final EntityPlayerMP aPlayer) {
            return new RemoteDetonatorArmedUpdate(new RemoteDetonationTargetList.Target(-1, RemoteDetonationTargetList.ExplosiveType.values()[aBuf.readInt()], aBuf.readInt(), aBuf.readInt(), aBuf.readInt()), aBuf.readBoolean());
        }

    }

    public GT_RemoteDetonator() {
        super("remote_detonator", "Remote Detonator", "Triggers gregtech explosives remotely.");
        setMaxStackSize(1);
        setMaxDamage(0);
        setHasSubtypes(false);
    }

    /**
     * @param aWorld  The world
     * @param aX      The X Position
     * @param aY      The X Position
     * @param aZ      The X Position
     * @param aPlayer The Player that is wielding the item
     * @return
     */
    @Override
    public boolean doesSneakBypassUse(final World aWorld, final int aX, final int aY, final int aZ, final EntityPlayer aPlayer) {
        return false;
    }

    /**
     * @param aList
     * @param aStack
     * @param aPlayer
     */
    @SuppressWarnings(
            {
                    "rawtypes",
                    "unchecked"
            }
    )
    @Override
    protected void addAdditionalToolTips(final List aList, final ItemStack aStack, final EntityPlayer aPlayer) {
        val compound = validateNBT(aStack);
        val remoteDetonationTargetList = RemoteDetonationTargetList.readFromNBT(compound, aPlayer);
        aList.add("Can prime and detonate multiple gregtech explosives sequentially.");
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

    /**
     * @param aStack
     * @param aWorld
     * @param aPlayer
     */
    @Override
    public void onCreated(final ItemStack aStack, final World aWorld, final EntityPlayer aPlayer) {
        val compound = validateNBT(aStack);
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
            remoteDetonationTargetList.setDelay(((RemoteDetonatorDelayUpdate) data).getDelay());
            stack.setTagCompound(remoteDetonationTargetList.writeToNBT(new NBTTagCompound()));
        } else if (data instanceof RemoteDetonatorArmedUpdate) {
            val remoteDetonationTargetList = getRemoteDetonationTargetList(stack, player);
            val target = ((RemoteDetonatorArmedUpdate) data).getTarget();
            val armed = ((RemoteDetonatorArmedUpdate) data).isArmed();
            if (armed) {
                remoteDetonationTargetList.addTarget(target.getExplosiveType(), target.getX(), target.getY(), target.getZ());
            } else {
                remoteDetonationTargetList.removeTarget(target.getX(), target.getY(), target.getZ());
            }
            stack.setTagCompound(remoteDetonationTargetList.writeToNBT(new NBTTagCompound()));
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
        stack.setTagCompound(targetList.writeToNBT(new NBTTagCompound()));
        return targetList;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     *
     * @param stack
     * @param world
     * @param player
     */
    @Override
    public ItemStack onItemRightClick(final ItemStack stack, final World world, final EntityPlayer player) {
        if (player.isSneaking()) {
            itemUse(stack, world, player, (int) player.posX, (int) player.posY, (int) player.posZ);
        }
        return stack;
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and update it's contents.
     *
     * @param stack
     * @param world
     * @param entity
     * @param slotIndex
     * @param current
     */
    @Override
    public void onUpdate(final ItemStack stack, final World world, final Entity entity, final int slotIndex, final boolean current) {
        if (entity instanceof EntityPlayer) {
            val compound = validateNBT(stack);
            val remoteDetonationTargetList = RemoteDetonationTargetList.readFromNBT(compound, (EntityPlayer) entity);
            remoteDetonationTargetList.tick(world, (EntityPlayer) entity, (int) entity.posX, (int) entity.posY, (int) entity.posZ);
            stack.setTagCompound(remoteDetonationTargetList.writeToNBT(compound));
        }
    }

    /**
     * This is called when the item is used, before the block is activated.
     *
     * @param stack  The Item Stack
     * @param player The Player that used the item
     * @param world  The Current World
     * @param x      Target X Position
     * @param y      Target Y Position
     * @param z      Target Z Position
     * @param side   The side of the target hit
     * @param hitX
     * @param hitY
     * @param hitZ
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
        stack.setTagCompound(remoteDetonationTargetList.writeToNBT(new NBTTagCompound()));
        return !world.isRemote;
    }

    public void trigger(final @NonNull World aWorld, final @NonNull EntityPlayer player, final @NonNull RemoteDetonationTargetList remoteDetonationTargetList, final int x, final int y, final int z) {
        if (remoteDetonationTargetList.validDimension(player)) {
            aWorld.playSoundEffect(x, y, z, GregTech_API.sSoundList.get(220), 8.0f, aWorld.rand.nextFloat() + 1.0f);
            remoteDetonationTargetList.trigger(aWorld, player, x, y, z);
        } else {
            sendChat(aWorld, player, "Nothing to detonate!");
        }
    }

    public void removeTarget(final @NonNull World aWorld, final @NonNull EntityPlayer player, final @NonNull RemoteDetonationTargetList remoteDetonationTargetList, final int x, final int y, final int z) {
        final boolean contains, validDim;
        val target = aWorld.getBlock(x, y, z);
        contains = remoteDetonationTargetList.containsTarget(x, y, z);
        validDim = remoteDetonationTargetList.validDimension(player);
        if (contains && validDim && target instanceof GT_Block_Explosive) {
            remoteDetonationTargetList.removeTarget(x, y, z);
            ((GT_Block_Explosive) target).setPrimed(aWorld, x, y, z, false);
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
                remoteDetonationTargetList.addTarget(RemoteDetonationTargetList.ExplosiveType.getType((GT_Block_Explosive) target), x, y, z);
                ((GT_Block_Explosive) target).setPrimed(aWorld, x, y, z, true);
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
