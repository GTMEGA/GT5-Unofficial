package gregtech.common.items;


import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.IPacketReceivableItem;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.net.GT_Packet_InventoryUpdate;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.api.util.interop.BaublesInterop;
import lombok.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;
import java.util.List;


@Optional.Interface(iface = "baubles.api.IBauble", modid = "Baubles", striprefs = true)
public class GT_MEGAnet extends GT_Generic_Item implements IBauble, IPacketReceivableItem {

    public static class MEGANetHotkeyHandler {

        public static MEGANetHotkeyHandler INSTANCE = new MEGANetHotkeyHandler();

        private MEGANetHotkeyHandler() {
            FMLCommonHandler.instance().bus().register(this);
        }

        @SubscribeEvent
        public void onItemPickup(PlayerEvent.ItemPickupEvent event) {
            final EntityPlayer player = event.player;
            if (!player.worldObj.isRemote) {
                val stack = getPlayerMeganet(player, true);
                if (stack != null && stack.getItem() instanceof GT_MEGAnet) {
                    val item = (GT_MEGAnet) stack.getItem();
                    val stackPickedUp = event.pickedUp.getEntityItem();
                    if (stackPickedUp != null) {
                        item.incrementPickedUp(player, stack, stackPickedUp.stackSize);
                    }
                }
            }
        }

        public ItemStack getPlayerMeganet(final EntityPlayer player, final boolean requireEnabled) {
            return getPlayerMeganet(player, requireEnabled, new MutableInt(0), new MutableBoolean(false));
        }

        public ItemStack getPlayerMeganet(final EntityPlayer player, final boolean requireEnabled, final MutableInt slot, final MutableBoolean bauble) {
            if (player != null && player.worldObj.isRemote) {
                int idx = 0;
                if (BaublesInterop.INSTANCE.isBaublesLoaded()) {
                    for (val stack : BaublesInterop.INSTANCE.getBaubles(player)) {
                        if (stack != null && stack.getItem() instanceof GT_MEGAnet) {
                            if (!requireEnabled || ((GT_MEGAnet) stack.getItem()).isActive(stack)) {
                                slot.setValue(idx);
                                bauble.setValue(true);
                                return stack;
                            }
                        }
                        idx += 1;
                    }
                }
                idx = 0;
                for (ItemStack stack : player.inventory.mainInventory) {
                    if (stack != null && stack.getItem() instanceof GT_MEGAnet) {
                        slot.setValue(idx);
                        bauble.setValue(false);
                        return stack;
                    }
                    idx += 1;
                }
            }
            return null;
        }

        public void togglePlayerMeganet(final EntityPlayer player) {
            if (player != null && player.worldObj.isRemote) {
                MutableInt slotIdx = new MutableInt(0);
                MutableBoolean bauble = new MutableBoolean(false);
                val stack = getPlayerMeganet(player, false, slotIdx, bauble);
                if (stack != null && stack.getItem() instanceof GT_MEGAnet) {
                    doToggle(player, (GT_MEGAnet) stack.getItem(), bauble.booleanValue(), slotIdx.intValue());
                }
            }
        }

        private void doToggle(final EntityPlayer player, final GT_MEGAnet item, final boolean bauble, final int slot) {
            GT_Values.NW.sendToServer(new GT_Packet_InventoryUpdate(player, item, bauble, slot, null));
        }

        public ItemStack getPlayerMeganet(final EntityPlayer player) {
            return getPlayerMeganet(player, false);
        }

    }


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

            @NonNull
            public static ItemSetting readFromNBT(final @NonNull NBTTagCompound compound) {
                return compound.hasKey("setting") ? new ItemSetting(compound.getCompoundTag("setting")) : new ItemSetting();
            }

            @Builder.Default
            private ItemStack stack = null;

            @Builder.Default
            private boolean ignoreMetadata = false;

            @Builder.Default
            private boolean ignoreNBTData = false;

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

        @NonNull
        public static MEGAnetFilter readFromNBT(final @NonNull NBTTagCompound nbt) {
            return nbt.hasKey("filter") ? new MEGAnetFilter(nbt.getCompoundTag("filter")) : new MEGAnetFilter();
        }

        private final List<ItemSetting> filter = new ArrayList<>();

        @Builder.Default
        private boolean enabled = false;

        @Builder.Default
        private boolean whitelist = false;

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

    public static final int TIMER = 10, BASE_RANGE = 12, MAX_RANGE = 16;

    private static final boolean FILTER_WORKS = false;

    public static boolean defBool(final @NonNull NBTTagCompound comp, final @NonNull String tag, final boolean defVal) {
        if (comp.hasKey(tag)) {
            return comp.getBoolean(tag);
        }
        return defVal;
    }

    public GT_MEGAnet() {
        super("MEGAnet", "MEGA Magnet", "Attracts nearby items and inserts them into your inventory.");
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
        itemUse(stack, player, world);
        return setNBT(stack);
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and update it's contents.
     *
     * @param stack       Stack to check
     * @param world       World in which entity is
     * @param entity      Entity holding it
     * @param slotIndex   Index of slot
     * @param currentItem If the item is the entity's main item
     */
    @Override
    public void onUpdate(final ItemStack stack, final World world, final Entity entity, final int slotIndex, final boolean currentItem) {
        doMagnetStuff(stack, world, entity, currentItem);
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

    private boolean itemUse(final ItemStack stack, final EntityPlayer player, final World world) {
        // toggle(world, player, stack);
        return !world.isRemote && player.isSneaking();
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

    public NBTTagCompound validateNBT(final @NonNull ItemStack stack) {
        return validateNBT(stack.getTagCompound());
    }

    public NBTTagCompound validateNBT(final NBTTagCompound compound) {
        if (compound == null) {
            return new NBTTagCompound();
        }
        return compound;
    }

    @Override
    public void onPacketReceived(final World world, final EntityPlayer player, final ItemStack stack, final ISerializableObject data) {
        toggle(world, player, stack);
    }

    @Override
    public ISerializableObject readFromBytes(final ByteArrayDataInput aData) {
        val id = aData.readByte();
        return null;
    }

    @Optional.Method(modid = "Baubles")
    @Override
    public BaubleType getBaubleType(final ItemStack itemstack) {
        return BaubleType.RING;
    }

    @Optional.Method(modid = "Baubles")
    @Override
    public void onWornTick(final ItemStack itemstack, final EntityLivingBase player) {
        if (player.isSneaking()) {
            doMagnetStuff(itemstack, player.worldObj, player, false);
        }
    }

    @Optional.Method(modid = "Baubles")
    @Override
    public void onEquipped(final ItemStack itemstack, final EntityLivingBase player) {
    }

    @Optional.Method(modid = "Baubles")
    @Override
    public void onUnequipped(final ItemStack itemstack, final EntityLivingBase player) {

    }

    @Optional.Method(modid = "Baubles")
    @Override
    public boolean canEquip(final ItemStack itemstack, final EntityLivingBase player) {
        return true;
    }

    @Optional.Method(modid = "Baubles")
    @Override
    public boolean canUnequip(final ItemStack itemstack, final EntityLivingBase player) {
        return true;
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
        aList.add((isActive(aStack) ? EnumChatFormatting.GREEN + "Active" : EnumChatFormatting.RED + "Inactive"));
        aList.add("Shift + RMB to toggle.");
        final int range = getRange(aStack);
        aList.add((String.format("Range of (%d / %d)", range, heldRange(range))));
        final MEGAnetFilter filter = getFilter(aStack);
        if (FILTER_WORKS) {
            if (filter.isEnabled()) {
                aList.add("Filter mode: " + (filter.isWhitelist() ? EnumChatFormatting.WHITE + "Whitelist" : EnumChatFormatting.DARK_GRAY + "Blacklist"));
                final int filSize = filter.getFilter().size();
                if (filSize > 0) {
                    aList.add(String.format("Filtering %d items", filSize));
                }
            } else {
                aList.add("Filter disabled");
            }
        }
        aList.add(String.format("Magnetized" + EnumChatFormatting.GOLD + " %d " + EnumChatFormatting.GRAY + "items!", getPickedUp(aStack)));
        aList.add(EnumChatFormatting.DARK_BLUE + "" + EnumChatFormatting.BOLD + EnumChatFormatting.ITALIC + "The MEGAnet!");
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
        final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(entity.posX - range, entity.posY - range, entity.posZ - range, entity.posX + range,
                                                                       entity.posY + range, entity.posZ + range
                                                                      );
        world.getEntitiesWithinAABB(EntityItem.class, boundingBox).forEach(oEntity -> {
            if (oEntity instanceof EntityItem) {
                final EntityItem itemEntity = (EntityItem) oEntity;
                if (canPickup(stack, itemEntity)) {
                    pickup(world, stack, entity, itemEntity);
                }
            }
        });
        world.getEntitiesWithinAABB(EntityXPOrb.class, boundingBox).forEach(oEntity -> {
            if (oEntity instanceof EntityXPOrb) {
                final EntityXPOrb xpOrb = (EntityXPOrb) oEntity;
                world.playSoundEffect(entity.posX, entity.posY, entity.posZ, GregTech_API.sSoundList.get(215), 4.0f, world.rand.nextFloat() + 0f);
                xpOrb.setPosition(entity.posX, entity.posY, entity.posZ);
                if (entity instanceof EntityPlayer) {
                    xpOrb.onCollideWithPlayer((EntityPlayer) entity);
                    ((EntityPlayer) entity).xpCooldown = 0;
                }
            }
        });
    }

    protected void pickup(final @NonNull World world, final @NonNull ItemStack stack, final @NonNull Entity entity, @NonNull EntityItem itemEntity) {
        if (!(entity instanceof EntityPlayer) || itemEntity.isDead || itemEntity.getEntityItem() == null || itemEntity.getEntityItem().stackSize <= 0) {
            return;
        }
        final EntityPlayer player = (EntityPlayer) entity;
        final NBTTagCompound compound = validateNBT(stack);
        itemEntity.setPosition(player.posX, player.eyeHeight, player.posZ);
        itemEntity.motionX = 0;
        itemEntity.motionY = 0;
        itemEntity.motionZ = 0;
        int tempAmount = itemEntity.getEntityItem().stackSize;
        if (player.inventory.addItemStackToInventory(itemEntity.getEntityItem())) {
            incrementPickedUp(player, stack, tempAmount - itemEntity.getEntityItem().stackSize);
        }
        if (itemEntity.getEntityItem().stackSize <= 0) {
            itemEntity.setDead();
        } else {
            world.playSoundEffect(entity.posX, entity.posY, entity.posZ, GregTech_API.sSoundList.get(215), 4.0f, world.rand.nextFloat() + 0.5f);
        }
        itemEntity.delayBeforeCanPickup = 20;
        stack.setTagCompound(compound);
        setNBT(stack);
    }

    protected boolean canPickup(final @NonNull ItemStack stack, final @NonNull EntityItem itemEntity) {
        return itemEntity.delayBeforeCanPickup <= 0; // && getFilter(stack).matchesFilter(itemEntity.getEntityItem());
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
        return new MEGAnetFilter(); // MEGAnetFilter.readFromNBT(validateNBT(stack)); // <- Disabled for now
    }

    private void incrementPickedUp(final EntityPlayer player, final ItemStack stack, final int stackSize) {
        final NBTTagCompound compound = validateNBT(stack);
        long pickedUp = getPickedUp(stack);
        pickedUp += stackSize;
        if (pickedUp < 0) {
            pickedUp = Long.MAX_VALUE;
        }
        processAchievement(player, pickedUp);
        compound.setLong("pickedUp", pickedUp);
        stack.setTagCompound(compound);
        setNBT(stack);
    }

    private void processAchievement(final EntityPlayer player, final long pickupCount) {
        if (pickupCount >= Integer.MAX_VALUE) {
            GT_Mod.achievements.issueAchievement(player, "lottaItems");
        }
        if (pickupCount == Long.MAX_VALUE) {
            GT_Mod.achievements.issueAchievement(player, "wholeLottaItems");
        }
    }

    private void handlePickedUp(
            final World world,
            final ItemStack stack,
            final Entity entity,
            final EntityItem itemEntity,
            long pickupCount,
            final EntityPlayer player,
            final NBTTagCompound compound
                               ) {
        if (pickupCount < 0) {
            pickupCount = Long.MAX_VALUE;
        }
        compound.setLong("pickedUp", pickupCount);

    }

    private void toggle(final World world, final EntityPlayer player, final ItemStack stack) {
        final NBTTagCompound comp = validateNBT(stack);
        final boolean active = isActive(stack);
        final String sound = GregTech_API.sSoundList.get(active ? 217 : 216);
        GT_Utility.sendChatToPlayer(player, active ? "Deactivated" : "Activated");
        world.playSoundEffect(player.posX, player.posY, player.posZ, sound, 4.0f, world.rand.nextFloat() + 0f);
        comp.setBoolean("enabled", !active);
        stack.setTagCompound(comp);
        setNBT(stack);
    }

    private void doMagnetStuff(final ItemStack stack, final World world, final Entity entity, final boolean currentItem) {
        if (!world.isRemote) {
            if (defBool(validateNBT(stack), "enabled", true) && getTimer(stack) % TIMER == 0) {
                magnetize(stack, world, entity, currentItem);
            }
            adjustTimer(stack);
        }
    }

}
