package gregtech.common.items;


import gregtech.api.items.GT_Generic_Item;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_MiningExplosive;
import lombok.*;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
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

            private int index = -1;

            private int x = -1;

            private int y = -1;

            private int z = -1;

            @EqualsAndHashCode.Exclude
            private boolean valid = false;

            public Target(final int x, final int y, final int z) {
                this(-1, x, y, z, false);
            }

            public Target(final int index, final int x, final int y, final int z) {
                this(index, x, y, z, true);
            }

            @NonNull
            public Target addFromIntList(final int[] intArray) {
                this.valid = intArray.length == 4;
                if (valid) {
                    setIndex(intArray[0]);
                    setX(intArray[1]);
                    setY(intArray[2]);
                    setZ(intArray[3]);
                }
                return this;
            }

            public int[] getAsList() {
                return new int[]{index, x, y, z};
            }

            public int getDistanceFromTarget(final int pX, final int pY, final int pZ) {
                return magnitude(pX - this.x, pY - this.y, pZ - this.z);
            }

            private int magnitude(final int x, final int y, final int z) {
                return MathHelper.floor_double(Math.sqrt(x * x + y * y + z * z));
            }

            protected void trigger(final @NonNull World world, final @NonNull EntityPlayer player) {
                final Block block = world.getBlock(x, y, z);
                if (block instanceof GT_Block_MiningExplosive) {
                    ((GT_Block_MiningExplosive) block).remoteTrigger(world, x, y, z, player);
                }
            }

            protected boolean isLoaded(final @NonNull World world) {
                return world.getChunkProvider().chunkExists(x >> 4, y >> 4);
            }

        }


        private final Map<Integer, Target> targets = new HashMap<>();

        private final Map<Target, Integer> targetsReversed = new HashMap<>();

        private int dimension;

        @Builder.Default
        private int maxTarget = -1;

        @NonNull
        public static RemoteDetonationTargetList readFromNBT(final @NonNull NBTTagCompound compound, final @NonNull EntityPlayer player) {
            return compound.hasKey("targets") && compound.hasKey("dim") ? new RemoteDetonationTargetList(compound).addFromList(
                    compound.getTagList("targets", Constants.NBT.TAG_COMPOUND)) : new RemoteDetonationTargetList(player);
        }

        @NonNull
        public RemoteDetonationTargetList addFromList(final @NonNull NBTTagList listComp) {
            for (int i = 0; i < listComp.tagCount(); i++) {
                addTarget(new Target().addFromIntList(listComp.func_150306_c(i)));
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
            this(player.dimension, -1);
        }

        public RemoteDetonationTargetList(final NBTTagCompound compound) {
            this.dimension = compound.getInteger("dim");
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
            final NBTTagList list = new NBTTagList();
            for (final Target target : targets.values()) {
                list.appendTag(new NBTTagIntArray(target.getAsList()));
            }
            compound.setInteger("num", numTargets());
            compound.setInteger("dim", dimension);
            compound.setTag("targets", list);
            return compound;
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

        private void removeTarget(final Target target) {
            if (targetsReversed.containsKey(target)) {
                final int key = targetsReversed.get(target);
                targetsReversed.remove(target);
                targets.remove(key);
            }
        }

        public boolean trigger(final @NonNull World world, final @NonNull EntityPlayer player, final int x, final int y, final int z) {
            if (!hasTargets() || !validDimension(player)) {
                return false;
            }
            targets.values().stream().filter(target -> target.getDistanceFromTarget(x, y, z) <= 256 && target.isLoaded(world)).forEach(target -> {
                target.trigger(world, player);
            });
            return true;
        }

        public boolean hasTargets() {
            return !targets.isEmpty();
        }

        public boolean validDimension(final @NonNull EntityPlayer player) {
            return validDimension(player.dimension);
        }

        public boolean validDimension(final int dimension) {
            if (!hasTargets()) {
                this.dimension = dimension;
            }
            return this.dimension == dimension;
        }

    }

    public GT_RemoteDetonator() {
        super("remote_detonator", "Remote Detonator", "Triggers distant explosions");
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
    public boolean onItemUse(
            final ItemStack stack, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float hitX,
            final float hitY, final float hitZ
                                 ) {
        return itemUse(stack, world, player, x, y, z);
    }

    private boolean itemUse(
            final @NonNull ItemStack stack, final @NonNull World world, final @NonNull EntityPlayer player, final int x, final int y, final int z
                           ) {
        if (!world.isRemote) {
            if (player.isSneaking()) {
                return trigger(world, player, stack, x, y, z);
            } else {
                final NBTTagCompound compound = validateNBT(stack);
                final RemoteDetonationTargetList remoteDetonationTargetList = RemoteDetonationTargetList.readFromNBT(compound, player);
                boolean valid = remoteDetonationTargetList.validDimension(player);
                if (valid) {
                    if (remoteDetonationTargetList.containsTarget(x, y, z)) {
                        removeTarget(world, player, stack, x, y, z);
                    } else {
                        addTarget(world, player, stack, x, y, z);
                    }
                } else {
                    GT_Utility.sendChatToPlayer(player, "You're in the wrong dimension");
                }
                stack.setTagCompound(remoteDetonationTargetList.writeToNBT(compound));
                return true;
            }
        }
        return false;
    }

    public boolean trigger(
            final @NonNull World aWorld, final @NonNull EntityPlayer player, final @NonNull ItemStack stack, final int x, final int y, final int z
                          ) {
        if (!aWorld.isRemote) {
            final NBTTagCompound compound = validateNBT(stack);
            final RemoteDetonationTargetList remoteDetonationTargetList = RemoteDetonationTargetList.readFromNBT(compound, player);
            boolean valid = remoteDetonationTargetList.validDimension(player);
            if (valid) {
                valid = remoteDetonationTargetList.trigger(aWorld, player, x, y, z);
                GT_Utility.sendChatToPlayer(player, "Unable to trigger explosives, either you don't have any placed or you're in a different dimension");
            }
            stack.setTagCompound(remoteDetonationTargetList.writeToNBT(compound));
            return valid;
        }
        return true;
    }

    @NonNull
    public NBTTagCompound validateNBT(final @NonNull ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return new NBTTagCompound();
        }
        return stack.getTagCompound();
    }

    public boolean removeTarget(
            final @NonNull World aWorld, final @NonNull EntityPlayer player, final @NonNull ItemStack stack, final int x, final int y, final int z
                               ) {
        if (!aWorld.isRemote) {
            final NBTTagCompound compound = validateNBT(stack);
            final RemoteDetonationTargetList remoteDetonationTargetList = RemoteDetonationTargetList.readFromNBT(compound, player);
            boolean contains, validDim;
            contains = remoteDetonationTargetList.containsTarget(x, y, z);
            validDim = remoteDetonationTargetList.validDimension(player);
            boolean valid = contains && validDim;
            if (valid) {
                remoteDetonationTargetList.removeTarget(x, y, z);
                GT_Utility.sendChatToPlayer(player, String.format("Removed target (%d %d %d)", x, y, z));
            } else if (!validDim) {
                GT_Utility.sendChatToPlayer(player, "You're not in the same dimension");
            }
            stack.setTagCompound(remoteDetonationTargetList.writeToNBT(compound));
            return valid;
        }
        return true;
    }

    public boolean addTarget(
            final @NonNull World aWorld, final @NonNull EntityPlayer player, final @NonNull ItemStack stack, final int x, final int y, final int z
                            ) {
        if (!aWorld.isRemote) {
            final Block target = aWorld.getBlock(x, y, z);
            if (validTarget(target)) {
                final NBTTagCompound compound = validateNBT(stack);
                final RemoteDetonationTargetList remoteDetonationTargetList = RemoteDetonationTargetList.readFromNBT(compound, player);
                final boolean valid = remoteDetonationTargetList.validDimension(player);
                if (valid) {
                    remoteDetonationTargetList.addTarget(x, y, z);
                    GT_Utility.sendChatToPlayer(player, String.format("Added target (%d %d %d)", x, y, z));
                } else {
                    GT_Utility.sendChatToPlayer(player, "Unable to add target, do you have them in multiple dimensions?");
                }
                stack.setTagCompound(remoteDetonationTargetList.writeToNBT(compound));
                return valid;
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean validTarget(final @NonNull Block target) {
        return target instanceof GT_Block_MiningExplosive;
    }

}
