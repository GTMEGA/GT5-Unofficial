package gregtech.common.items;


import gregtech.GT_Mod;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.util.GT_Utility;
import lombok.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;


public class GT_MEGAnet extends GT_Generic_Item {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MEGAnetFilter {

        @NoArgsConstructor
        @Builder
        @Getter
        @Setter
        @AllArgsConstructor
        public static class ItemSetting {

            @Builder.Default
            private ItemStack stack = null;

            @Builder.Default
            private boolean ignoreMetadata = false;

            @Builder.Default
            private boolean ignoreNBTData = false;

            @NonNull
            public static ItemSetting readFromNBT(final @NonNull NBTTagCompound compound) {
                return compound.hasKey("setting") ? new ItemSetting(compound.getCompoundTag("setting")) : new ItemSetting();
            }

            public ItemSetting(final @NonNull NBTTagCompound compound) {
                this(ItemStack.loadItemStackFromNBT(compound.getCompoundTag("item")), defBool(compound, "iMeta", false), defBool(compound, "iNBT", false));
            }

            @NonNull
            public NBTTagCompound writeToNBT(final @NonNull NBTTagCompound compound) {
                final NBTTagCompound setting = new NBTTagCompound();
                setting.setTag("item", stack.writeToNBT(new NBTTagCompound()));
                setting.setBoolean("iMeta", ignoreMetadata);
                setting.setBoolean("iNBT", ignoreNBTData);
                compound.setTag("setting", setting);
                return compound;
            }

            public boolean isValid() {
                return stack != null;
            }

        }


        public static final int MAX_FILTERED = 15;

        private final List<ItemSetting> filter = new ArrayList<>();

        @Builder.Default
        private boolean enabled = false;

        @Builder.Default
        private boolean whitelist = false;

        @NonNull
        public static MEGAnetFilter readFromNBT(final @NonNull NBTTagCompound nbt) {
            return nbt.hasKey("filter") ? new MEGAnetFilter(nbt.getCompoundTag("filter")) : new MEGAnetFilter();
        }

        public MEGAnetFilter(final @NonNull NBTTagCompound filterNBT) {
            this(defBool(filterNBT, "enabled", false), defBool(filterNBT, "whitelist", false));
            if (filterNBT.hasKey("itemFilter", Constants.NBT.TAG_LIST)) {
                final NBTTagList itemList = filterNBT.getTagList("itemFilter", Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < itemList.tagCount() && i < MAX_FILTERED; i++) {
                    filter.add(ItemSetting.readFromNBT(itemList.getCompoundTagAt(i)));
                }
            }
        }

        @NonNull
        public NBTTagCompound writeToNBT(final @NonNull NBTTagCompound nbt) {
            final NBTTagCompound filterNBT = new NBTTagCompound();
            filterNBT.setBoolean("enabled", enabled);
            filterNBT.setBoolean("whitelist", whitelist);
            final NBTTagList filteredItems = new NBTTagList();
            for (final ItemSetting setting : filter) {
                if (setting != null && setting.isValid()) {
                    filteredItems.appendTag(setting.writeToNBT(new NBTTagCompound()));
                }
            }
            filterNBT.setTag("itemFilter", filteredItems);
            nbt.setTag("filter", filterNBT);
            return nbt;
        }

        public boolean matchesFilter(final @NonNull ItemStack stack) {
            return whitelist == filter.stream().anyMatch(fStack -> GT_Utility.areStacksEqual(fStack.getStack(), stack));
        }

    }


    public static final int TIMER = 2, BASE_RANGE = 12, MAX_RANGE = 16;

    public static boolean defBool(final @NonNull NBTTagCompound comp, final @NonNull String tag, final boolean defVal) {
        if (comp.hasKey(tag)) {
            return comp.getBoolean(tag);
        }
        return defVal;
    }

    public GT_MEGAnet() {
        super("MEGAnet", "MEGAnet", "Puts adjacent items into your inventory");
        setMaxDamage(0);
        setMaxStackSize(1);
        setHasSubtypes(false);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     *
     * @param stack  Stack being used
     * @param world  World
     * @param player Player using
     */
    @Override
    public ItemStack onItemRightClick(final ItemStack stack, final World world, final EntityPlayer player) {
        if (getTimer(stack) % TIMER == 0) {
            itemUse(stack, player, world);
        }
        return setNBT(stack);
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     *
     * @param stack       Stack to check
     * @param world       World in which entity is
     * @param entity      Entity holding it
     * @param slotIndex   Index of slot
     * @param currentItem If the item is the entity's main item
     */
    @Override
    public void onUpdate(final ItemStack stack, final World world, final Entity entity, final int slotIndex, final boolean currentItem) {
        if (!world.isRemote) {
            if (defBool(validateNBT(stack), "enabled", true) && getTimer(stack) % TIMER == 0) {
                magnetize(stack, world, entity, currentItem);
            }
            adjustTimer(stack);
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
     * @param hitX   Hit location X
     * @param hitY   Hit location y
     * @param hitZ   Hit location z
     * @return Return true to prevent any further processing.
     */
    @Override
    public boolean onItemUseFirst(
            final ItemStack stack, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float hitX,
            final float hitY, final float hitZ
                                 ) {
        return itemUse(stack, player, world) || super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    /**
     * Render Pass sensitive version of hasEffect()
     *
     * @param stack Stack
     * @param pass  Pass, not used
     */
    @Override
    public boolean hasEffect(final ItemStack stack, final int pass) {
        return isActive(stack);
    }

    protected boolean isActive(final @NonNull ItemStack stack) {
        final NBTTagCompound comp = validateNBT(stack);
        return !comp.hasKey("enabled") || comp.getBoolean("enabled");
    }

    public NBTTagCompound validateNBT(final @NonNull ItemStack stack) {
        return validateNBT(stack.getTagCompound());
    }

    public NBTTagCompound validateNBT(final NBTTagCompound compound) {
        if (compound == null) {
            return new NBTTagCompound();
        }
        return compound;
    }

    /**
     * @param aWorld  The world
     * @param aX      The X Position
     * @param aY      The X Position
     * @param aZ      The X Position
     * @param aPlayer The Player that is wielding the item
     * @return False
     */
    @Override
    public boolean doesSneakBypassUse(final World aWorld, final int aX, final int aY, final int aZ, final EntityPlayer aPlayer) {
        return false;
    }

    /**
     * @param aList   List of tooltips
     * @param aStack  Stack
     * @param aPlayer Player holding it
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected void addAdditionalToolTips(final List aList, final ItemStack aStack, final EntityPlayer aPlayer) {
        aList.add("Shift+RClick to " + (isActive(aStack) ? "Deactivate" : "Activate"));
        final int range = getRange(aStack);
        aList.add((String.format("Range of (%d / %d)", range, heldRange(range))));
        final MEGAnetFilter filter = getFilter(aStack);
        if (filter.isEnabled()) {
            aList.add("Filter mode: " + (filter.isWhitelist() ? "Whitelist" : "Blacklist"));
            final int filSize = filter.getFilter().size();
            if (filSize > 0) {
                aList.add(String.format("Filtering %d items", filSize));
            }
        } else {
            aList.add("Filter disabled");
        }
        aList.add(String.format("Magnetized %d items", getPickedUp(aStack)));
    }

    /**
     * @param aStack  Stack
     * @param aWorld  World
     * @param aPlayer Player crafting it
     */
    @Override
    public void onCreated(final ItemStack aStack, final World aWorld, final EntityPlayer aPlayer) {
        setNBT(aStack);
    }

    public ItemStack setNBT(final @NonNull ItemStack stack) {
        final NBTTagCompound compound = validateNBT(stack);
        //
        if (!compound.hasKey("timer")) {
            compound.setInteger("timer", 0);
        }
        if (!compound.hasKey("range")) {
            compound.setInteger("range", 0);
        }
        if (!compound.hasKey("pickedUp")) {
            compound.setLong("pickedUp", 0L);
        }
        if (!compound.hasKey("enabled")) {
            compound.setBoolean("enabled", true);
        }
        if (!compound.hasKey("filter")) {
            new MEGAnetFilter().writeToNBT(compound);
        }
        //
        stack.setTagCompound(compound);
        return stack;
    }

    public void adjustTimer(final ItemStack stack) {
        final NBTTagCompound compound = validateNBT(stack);
        int timer = getTimer(stack);
        if (timer < 0) {
            timer = 0;
        }
        compound.setInteger("timer", (timer + 1) % TIMER);
        stack.setTagCompound(compound);
    }

    @SuppressWarnings("unchecked")
    protected void magnetize(final ItemStack stack, final World world, final Entity entity, final boolean currentItem) {
        final int baseRange = getRange(stack);
        final int range = currentItem ? heldRange(baseRange) : baseRange;
        final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(
                entity.posX - range, entity.posY - range, entity.posZ - range, entity.posX + range, entity.posY + range, entity.posZ + range);
        world.getEntitiesWithinAABB(EntityItem.class, boundingBox).forEach(oEntity -> {
            if (oEntity instanceof EntityItem) {
                final EntityItem itemEntity = (EntityItem) oEntity;
                if (canPickup(stack, itemEntity)) {
                    pickup(stack, entity, itemEntity);
                }
            }
        });
    }

    protected void pickup(final @NonNull ItemStack stack, final @NonNull Entity entity, @NonNull EntityItem itemEntity) {
        if (!(entity instanceof EntityPlayer) || itemEntity.isDead) {
            return;
        }
        final EntityPlayer player = (EntityPlayer) entity;
        final NBTTagCompound compound = validateNBT(stack);
        itemEntity.delayBeforeCanPickup = 0;
        //
        long pickupCount = getPickedUp(stack);
        itemEntity.setPosition(player.posX, player.eyeHeight, player.posZ);
        final int originalAmount = itemEntity.getEntityItem().stackSize;
        itemEntity.onCollideWithPlayer(player);
        pickupCount += originalAmount - itemEntity.getEntityItem().stackSize;
        if (pickupCount < 0) {
            pickupCount = Long.MAX_VALUE;
        }
        if (pickupCount >= Integer.MAX_VALUE) {
            GT_Mod.achievements.issueAchievement(player, "lottaItems");
        }
        if (pickupCount == Long.MAX_VALUE) {
            GT_Mod.achievements.issueAchievement(player, "wholeLottaItems");
        }
        if (player instanceof EntityPlayerMP) {
            ((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
        }
        compound.setLong("pickedUp", pickupCount);
        itemEntity.delayBeforeCanPickup = 20;
        stack.setTagCompound(compound);
        setNBT(stack);
    }

    protected boolean canPickup(final @NonNull ItemStack stack, final @NonNull EntityItem itemEntity) {
        return itemEntity.delayBeforeCanPickup <= 0 && getFilter(stack).matchesFilter(itemEntity.getEntityItem());
    }

    protected int heldRange(final int baseRange) {
        return baseRange * 2;
    }

    protected int getTimer(final @NonNull ItemStack stack) {
        final NBTTagCompound comp = validateNBT(stack);
        return comp.hasKey("timer") ? Math.max(0, comp.getInteger("timer")) : 0;
    }

    protected long getPickedUp(final @NonNull ItemStack stack) {
        final NBTTagCompound comp = validateNBT(stack);
        if (comp.hasKey("pickedUp")) {
            long picked = comp.getLong("pickedUp");
            if (picked < 0) {
                picked = Long.MAX_VALUE;
            }
            return picked;
        }
        return 0L;
    }

    protected int getRange(final @NonNull ItemStack stack) {
        final NBTTagCompound comp = validateNBT(stack);
        int range = comp.getInteger("range");
        if (range > 0) {
            return Math.min(range, MAX_RANGE);
        }
        return BASE_RANGE;
    }

    protected MEGAnetFilter getFilter(final @NonNull ItemStack stack) {
        return MEGAnetFilter.readFromNBT(validateNBT(stack));
    }

    private boolean itemUse(final ItemStack stack, final EntityPlayer player, final World world) {
        if (!world.isRemote && player.isSneaking()) {
            final NBTTagCompound comp = validateNBT(stack);
            final boolean active = isActive(stack);
            GT_Utility.sendChatToPlayer(player, active ? "Deactivated" : "Activated");
            comp.setBoolean("enabled", !active);
            stack.setTagCompound(comp);
            setNBT(stack);
            return true;
        }
        return false;
    }

}
