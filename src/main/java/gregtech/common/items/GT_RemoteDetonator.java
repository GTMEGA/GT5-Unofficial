package gregtech.common.items;


import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_MiningExplosive;
import lombok.*;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GT_RemoteDetonator extends GT_Generic_Item {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class RemoteDetonationTargetList {

        @Getter
        @NoArgsConstructor
        @Setter
        @AllArgsConstructor
        @EqualsAndHashCode
        public static class Target {

            @EqualsAndHashCode.Exclude
            private int index = -1;

            private int x = -1;

            private int y = -1;

            private int z = -1;

            @EqualsAndHashCode.Exclude
            private boolean triggered = false;

            @EqualsAndHashCode.Exclude
            private boolean valid = false;

            public Target(final int x, final int y, final int z) {
                this(-1, x, y, z, false, false);
            }

            public Target(final int index, final int x, final int y, final int z) {
                this(index, x, y, z, false, true);
            }

            @NonNull
            public Target addFromCompound(final @NonNull NBTTagCompound compound) {
                setValid(compound.hasKey("index") && compound.hasKey("x") && compound.hasKey("y") && compound.hasKey("z"));
                setIndex(compound.getInteger("index"));
                setX(compound.getInteger("x"));
                setY(compound.getInteger("y"));
                setZ(compound.getInteger("z"));
                setTriggered(compound.getBoolean("t"));
                return this;
            }

            public int getDistanceFromTarget(final int pX, final int pY, final int pZ) {
                return magnitude(pX - this.x, pY - this.y, pZ - this.z);
            }

            private int magnitude(final int x, final int y, final int z) {
                return MathHelper.floor_double(Math.sqrt(x * x + y * y + z * z));
            }

            @NonNull
            protected NBTTagCompound writeToNBT(final @NonNull NBTTagCompound nbtTagCompound) {
                nbtTagCompound.setInteger("index", index);
                nbtTagCompound.setInteger("x", x);
                nbtTagCompound.setInteger("y", y);
                nbtTagCompound.setInteger("z", z);
                nbtTagCompound.setBoolean("t", triggered);
                return nbtTagCompound;
            }

            protected void trigger(final @NonNull World world, final @NonNull EntityPlayer player) {
                final Block block = world.getBlock(x, y, z);
                if (block instanceof GT_Block_MiningExplosive && !world.isRemote) {
                    ((GT_Block_MiningExplosive) block).remoteTrigger(world, x, y, z, player);
                }
                triggered = true;
            }

            protected boolean isLoaded(final @NonNull World world) {
                return world.getChunkProvider().chunkExists(x >> 4, z >> 4);
            }

            protected boolean canDetonate(final @NonNull World world, final int pX, final int pY, final int pZ) {
                boolean loaded = isLoaded(world), inRange = getDistanceFromTarget(pX, pY, pZ) <= GT_Values.MEMaxRemoteRange;
                return loaded && inRange && !triggered;
            }

        }


        private final Map<Integer, Target> targets = new HashMap<>();

        private final Map<Target, Integer> targetsReversed = new HashMap<>();

        private int dimension;

        @Builder.Default
        private int maxTarget = -1, timer = -1;

        @Builder.Default
        private boolean triggered = false, done = true;

        @NonNull
        public static RemoteDetonationTargetList readFromNBT(final @NonNull NBTTagCompound compound, final @NonNull EntityPlayer player) {
            return compound.hasKey("targets") && compound.hasKey("dim") ? new RemoteDetonationTargetList(compound).addFromList(
                    compound.getTagList("targets", Constants.NBT.TAG_COMPOUND)) : new RemoteDetonationTargetList(player);
        }

        @NonNull
        public RemoteDetonationTargetList addFromList(final @NonNull NBTTagList listComp) {
            for (int i = 0; i < listComp.tagCount(); i++) {
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
            int temp = initial;
            while (targets.containsKey(temp)) {
                temp += 1;
            }
            return temp;
        }

        public RemoteDetonationTargetList(final @NonNull EntityPlayer player) {
            this(player.dimension, -1, -1, false, false);
        }

        public RemoteDetonationTargetList(final @NonNull NBTTagCompound compound) {
            setDimension(compound.getInteger("dim"));
            setTriggered(compound.getBoolean("active"));
            setTimer(compound.getInteger("timer"));
            setDone(compound.getBoolean("done"));
        }

        public void clear() {
            maxTarget = -1;
            targets.clear();
            targetsReversed.clear();
        }

        public boolean hasUntriggered(final @NonNull World world, final int x, final int y, final int z) {
            return targets.values().stream().anyMatch(target -> target.canDetonate(world, x, y, z));
        }

        public boolean containsTarget(final int x, final int y, final int z) {
            return targets.containsValue(new Target(x, y, z)) && targetsReversed.containsKey(new Target(x, y, z));
        }

        @NonNull
        public NBTTagCompound writeToNBT(final @NonNull NBTTagCompound compound) {
            if (isDone()) {
                return new NBTTagCompound();
            }
            final NBTTagList list = new NBTTagList();
            for (final Target target : targets.values()) {
                list.appendTag(target.writeToNBT(new NBTTagCompound()));
            }
            compound.setInteger("num", numTargets());
            compound.setBoolean("active", triggered);
            compound.setInteger("timer", timer);
            compound.setInteger("dim", dimension);
            compound.setBoolean("done", done);
            compound.setTag("targets", list);
            return compound;
        }

        public void tick(final @NonNull World world, final @NonNull EntityPlayer player, final int x, final int y, final int z) {
            if (!triggered) {
                return;
            }
            if (this.timer++ % GT_Values.MERemoteDelay == 0) {
                targets.values().stream().filter(target -> target.canDetonate(world, x, y, z)).min(Comparator.comparingInt(target -> target.index)).ifPresent(
                        firstValid -> firstValid.trigger(world, player));
            }
            if (!hasUntriggered(world, x, y, z)) {
                setDone(true);
            }
        }

        public int numTargets() {
            return targets.size();
        }

        public void addTarget(final int x, final int y, final int z) {
            final int nextIndex = getNextIndex(maxTarget);
            addTarget(new Target(nextIndex, x, y, z));
        }

        public void removeTarget(final int x, final int y, final int z) {
            removeTarget(new Target(x, y, z));
        }

        private void removeTarget(final @NonNull Target target) {
            if (targetsReversed.containsKey(target)) {
                final int key = targetsReversed.get(target);
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

        private void triggerAll(final @NonNull World world, final @NonNull EntityPlayer player, final int x, final int y, final int z) {
            targets.values().stream().filter(target -> target.canDetonate(world, x, y, z)).sorted(
                    Comparator.comparingInt(target -> target.index)).forEach(target -> {
                target.trigger(world, player);
            });
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

    }

    public GT_RemoteDetonator() {
        super("remote_detonator", "Remote Detonator", "Triggers mining explosives remotely.");
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
     * @param aStack
     * @param aWorld
     * @param aPlayer
     */
    @Override
    public void onCreated(final ItemStack aStack, final World aWorld, final EntityPlayer aPlayer) {
        final NBTTagCompound compound = validateNBT(aStack);
        final RemoteDetonationTargetList remoteDetonationTargetList = RemoteDetonationTargetList.readFromNBT(compound, aPlayer);
        aStack.setTagCompound(remoteDetonationTargetList.writeToNBT(compound));
    }

    @NonNull
    public NBTTagCompound validateNBT(final @NonNull ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return new NBTTagCompound();
        }
        return stack.getTagCompound();
    }
    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
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
            final NBTTagCompound compound = validateNBT(stack);
            final RemoteDetonationTargetList remoteDetonationTargetList = RemoteDetonationTargetList.readFromNBT(compound, (EntityPlayer) entity);
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
    public boolean onItemUseFirst(
            final ItemStack stack, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float hitX,
            final float hitY, final float hitZ
                                 ) {
        return itemUse(stack, world, player, x, y, z);
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

    private boolean itemUse(
            final @NonNull ItemStack stack, final @NonNull World world, final @NonNull EntityPlayer player, final int x, final int y, final int z
                           ) {
        final NBTTagCompound compound = validateNBT(stack);
        final RemoteDetonationTargetList remoteDetonationTargetList = RemoteDetonationTargetList.readFromNBT(compound, player);
        if (player.isSneaking()) {
            trigger(world, player, remoteDetonationTargetList, x, y, z);
        } else {
            final Block target = world.getBlock(x, y, z);
            if (target instanceof GT_Block_MiningExplosive) {
                boolean valid = remoteDetonationTargetList.validDimension(player);
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
        stack.setTagCompound(remoteDetonationTargetList.writeToNBT(compound));
        return !world.isRemote;
    }

    public void trigger(
            final @NonNull World aWorld, final @NonNull EntityPlayer player, final @NonNull RemoteDetonationTargetList remoteDetonationTargetList, final int x,
            final int y, final int z
                       ) {
        if (remoteDetonationTargetList.validDimension(player)) {
            aWorld.playSoundEffect(x, y, z, GregTech_API.sSoundList.get(220), 8.0f, aWorld.rand.nextFloat() + 1.0f);
            remoteDetonationTargetList.trigger(aWorld, player, x, y, z);
        } else {
            sendChat(aWorld, player, "Nothing to detonate!");
        }
    }

    public void removeTarget(
            final @NonNull World aWorld, final @NonNull EntityPlayer player, final @NonNull RemoteDetonationTargetList remoteDetonationTargetList, final int x,
            final int y, final int z
                            ) {
        final boolean contains, validDim;
        final Block target = aWorld.getBlock(x, y, z);
        contains = remoteDetonationTargetList.containsTarget(x, y, z);
        validDim = remoteDetonationTargetList.validDimension(player);
        if (contains && validDim && target instanceof GT_Block_MiningExplosive) {
            remoteDetonationTargetList.removeTarget(x, y, z);
            ((GT_Block_MiningExplosive) target).setPrimed(aWorld, x, y, z, false);
            aWorld.playSoundEffect(x, y, z, GregTech_API.sSoundList.get(219), 4.0f, aWorld.rand.nextFloat() + 1.0f);
            sendChat(aWorld, player, String.format("Removed target (%d %d %d)", x, y, z));
        } else if (!validDim) {
            sendChat(aWorld, player, "Out of range!");
        }
    }

    public void addTarget(
            final @NonNull World aWorld, final @NonNull EntityPlayer player, final @NonNull RemoteDetonationTargetList remoteDetonationTargetList, final int x,
            final int y, final int z
                         ) {
        final Block target = aWorld.getBlock(x, y, z);
        if (target instanceof GT_Block_MiningExplosive) {
            if (remoteDetonationTargetList.validDimension(player)) {
                remoteDetonationTargetList.addTarget(x, y, z);
                ((GT_Block_MiningExplosive) target).setPrimed(aWorld, x, y, z, true);
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

    /**
     * @param aList
     * @param aStack
     * @param aPlayer
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void addAdditionalToolTips(final List aList, final ItemStack aStack, final EntityPlayer aPlayer) {
        final NBTTagCompound compound = validateNBT(aStack);
        final RemoteDetonationTargetList remoteDetonationTargetList = RemoteDetonationTargetList.readFromNBT(compound, aPlayer);
        aList.add("Can prime and detonate multiple mining explosives sequentially.");
        aList.add("Has a transmission range of 256 blocks.");
        final int numTargets = compound.getInteger("num");
        if (numTargets > 0) {
            aList.add(String.format("Explosives armed: %d", numTargets));
        }
        if (remoteDetonationTargetList.isTriggered()) {
            aList.add(EnumChatFormatting.DARK_RED+"DETONATING!"+EnumChatFormatting.GRAY+" Stand Clear!");
        }
        aStack.setTagCompound(remoteDetonationTargetList.writeToNBT(compound));
    }

}
