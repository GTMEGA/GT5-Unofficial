package gregtech.api.gui;


import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import lombok.val;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;


/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * Main Container-Class, used for all my GUIs
 */
public class GT_Container extends Container {

    public IGregTechTileEntity mTileEntity;

    public InventoryPlayer mPlayerInventory;

    public GT_Container(InventoryPlayer aPlayerInventory, IGregTechTileEntity aTileEntityInventory) {

        mTileEntity = aTileEntityInventory;
        mPlayerInventory = aPlayerInventory;
        mTileEntity.openInventory();
    }

    /**
     * To add the Slots to your GUI
     */
    public void addSlots(InventoryPlayer aPlayerInventory) {
        //
    }

    /**
     * Amount of regular Slots in the GUI (so, non-HoloSlots)
     */
    public int getSlotCount() {
        return 0;
    }

    /**
     * Start-Index of the usable Slots (the first non-HoloSlot)
     */
    public int getSlotStartIndex() {
        return 0;
    }

    public int getShiftClickStartIndex() {
        return getSlotStartIndex();
    }

    /**
     * Amount of Slots in the GUI the player can Shift-Click into. Uses also getSlotStartIndex
     */
    public int getShiftClickSlotCount() {
        return 0;
    }

    /**
     * Is Player-Inventory visible?
     */
    public boolean doesBindPlayerInventory() {
        return true;
    }

    /**
     * Amount of ALL Slots in the GUI including HoloSlots and ArmorSlots, but excluding regular Player Slots
     */
    protected final int getAllSlotCount() {
        if (inventorySlots != null) {
            if (doesBindPlayerInventory()) {
                return inventorySlots.size() - 36;
            }
            return inventorySlots.size();
        }
        return getSlotCount();
    }

    protected void bindPlayerInventory(final InventoryPlayer aInventoryPlayer) {
        val nRows = getNumRows();
        val numSlots = 36;
        var total = 0;
        val ySlotSpacing = getYSlotSpacing();
        val xSlotSpacing = getXSlotSpacing();
        val nSlotsInRow = 9;
        val guiWidth = getGuiWidth();
        val xOffsetBase = (guiWidth - nSlotsInRow * xSlotSpacing) / 2;
        val hotBarSpacing = getHotBarOffset();
        for (var rowIndex = 0; rowIndex < nRows && total < numSlots; rowIndex++) {
            val isHotbar = rowIndex == 0;
            val y = getYSlotOffset() + (isHotbar ? hotBarSpacing + nRows * ySlotSpacing : 0) + (rowIndex - 1) * ySlotSpacing;
            val xOffset = xOffsetBase + xBump();
            for (var colIndex = 0; colIndex < nSlotsInRow && total < numSlots; colIndex++) {
                val slotIndex = colIndex + rowIndex * nSlotsInRow;
                val x = xOffset + colIndex * xSlotSpacing;
                addSlotToContainer(createPlayerSlot(aInventoryPlayer, slotIndex, x, y));
                total += 1;
            }
        }
    }

    protected Slot createPlayerSlot(final InventoryPlayer aInventoryPlayer, final int slotIndex, final int x, final int y) {
        return new Slot(aInventoryPlayer, slotIndex, x, y);
    }

    protected int getNumRows() {
        return 4;
    }

    protected int getYSlotSpacing() {
        return 18;
    }

    protected int getXSlotSpacing() {
        return 18;
    }

    protected int getHotBarOffset() {
        return 4;
    }

    protected int getYSlotOffset() {
        return getGuiHeight() - (getHotBarOffset() + getNumRows() * getYSlotSpacing()) + yBump();
    }

    @Override
    protected Slot addSlotToContainer(Slot par1Slot) {
        try {
            return super.addSlotToContainer(par1Slot);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        return par1Slot;
    }

    protected int getGuiHeight() {
        return 166;
    }

    protected int yBump() {
        return -6;
    }

    protected int getGuiWidth() {
        return 176 + (GregTech_API.mDWS ? 162 : 0);
    }

    protected int xBump() {
        return 1;
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        try {
            super.addCraftingToCrafters(par1ICrafting);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public List getInventory() {
        try {
            return super.getInventory();
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        return null;
    }

    @Override
    public void removeCraftingFromCrafters(ICrafting par1ICrafting) {
        try {
            super.removeCraftingFromCrafters(par1ICrafting);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public void detectAndSendChanges() {
        try {
            super.detectAndSendChanges();
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public boolean enchantItem(EntityPlayer par1EntityPlayer, int par2) {
        try {
            return super.enchantItem(par1EntityPlayer, par2);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        return false;
    }

    @Override
    public Slot getSlotFromInventory(IInventory par1IInventory, int par2) {
        try {
            return super.getSlotFromInventory(par1IInventory, par2);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        return null;
    }

    @Override
    public Slot getSlot(int par1) {
        try {
            if (this.inventorySlots.size() > par1) {
                return super.getSlot(par1);
            }
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        return null;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer aPlayer, int aSlotIndex) {
        ItemStack stack = null;
        Slot slotObject = (Slot) inventorySlots.get(aSlotIndex);

        mTileEntity.markDirty();

        //null checks and checks if the item can be stacked (maxStackSize > 1)
        if (getSlotCount() > 0 && slotObject != null && slotObject.getHasStack() && !(slotObject instanceof GT_Slot_Holo)) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = GT_Utility.copyOrNull(stackInSlot);

            //TileEntity -> Player
            if (aSlotIndex < getAllSlotCount()) {
                if (doesBindPlayerInventory()) {
                    if (!mergeItemStack(stackInSlot, getAllSlotCount(), getAllSlotCount() + 36, true)) {
                        return null;
                    }
                }
                //Player -> TileEntity
            } else if (!mergeItemStack(stackInSlot, getShiftClickStartIndex(), getShiftClickStartIndex() + getShiftClickSlotCount(), false)) {
                return null;
            }

            if (stackInSlot.stackSize == 0) {
                slotObject.putStack(null);
            } else {
                slotObject.onSlotChanged();
            }
        }
        return stack;
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        // System.out.printf("%s %s %s %s%n", aPlayer, this, aPlayer.openContainer, aPlayer.worldObj.isRemote);
        mTileEntity.markDirty();

        if (aSlotIndex >= 0) {
            if (inventorySlots.get(aSlotIndex) == null || inventorySlots.get(aSlotIndex) instanceof GT_Slot_Holo) {
                return null;
            }
            if (!(inventorySlots.get(aSlotIndex) instanceof GT_Slot_Armor)) {
                if (aSlotIndex < getAllSlotCount()) {
                    if (aSlotIndex < getSlotStartIndex() || aSlotIndex >= getSlotStartIndex() + getSlotCount()) {
                        return null;
                    }
                }
            }
        }

        try {
            return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }

        ItemStack rStack = null;
        InventoryPlayer aPlayerInventory = aPlayer.inventory;
        Slot aSlot;
        ItemStack tTempStack;
        int tTempStackSize;
        ItemStack aHoldStack;

        if ((aShifthold == 0 || aShifthold == 1) && (aMouseclick == 0 || aMouseclick == 1)) {
            if (aSlotIndex == -999) {
                if (aPlayerInventory.getItemStack() != null) {
                    if (aMouseclick == 0) {
                        aPlayer.dropPlayerItemWithRandomChoice(aPlayerInventory.getItemStack(), true);
                        aPlayerInventory.setItemStack(null);
                    }
                    if (aMouseclick == 1) {
                        aPlayer.dropPlayerItemWithRandomChoice(aPlayerInventory.getItemStack().splitStack(1), true);
                        if (aPlayerInventory.getItemStack().stackSize == 0) {
                            aPlayerInventory.setItemStack(null);
                        }
                    }
                }
            } else if (aShifthold == 1) {
                aSlot = (Slot) this.inventorySlots.get(aSlotIndex);
                if (aSlot != null && aSlot.canTakeStack(aPlayer)) {
                    tTempStack = this.transferStackInSlot(aPlayer, aSlotIndex);
                    if (tTempStack != null) {
                        rStack = GT_Utility.copyOrNull(tTempStack);
                        if (aSlot.getStack() != null && aSlot.getStack().getItem() == tTempStack.getItem()) {
                            slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
                        }
                    }
                }
            } else {
                if (aSlotIndex < 0) {
                    return null;
                }
                aSlot = (Slot) this.inventorySlots.get(aSlotIndex);
                if (aSlot != null) {
                    tTempStack = aSlot.getStack();
                    ItemStack var13 = aPlayerInventory.getItemStack();
                    if (tTempStack != null) {
                        rStack = GT_Utility.copyOrNull(tTempStack);
                    }
                    if (tTempStack == null) {
                        if (var13 != null && aSlot.isItemValid(var13)) {
                            tTempStackSize = aMouseclick == 0 ? var13.stackSize : 1;
                            if (tTempStackSize > aSlot.getSlotStackLimit()) {
                                tTempStackSize = aSlot.getSlotStackLimit();
                            }
                            aSlot.putStack(var13.splitStack(tTempStackSize));

                            if (var13.stackSize == 0) {
                                aPlayerInventory.setItemStack(null);
                            }
                        }
                    } else if (aSlot.canTakeStack(aPlayer)) {
                        if (var13 == null) {
                            tTempStackSize = aMouseclick == 0 ? tTempStack.stackSize : (tTempStack.stackSize + 1) / 2;
                            aHoldStack = aSlot.decrStackSize(tTempStackSize);
                            aPlayerInventory.setItemStack(aHoldStack);
                            if (tTempStack.stackSize == 0) {
                                aSlot.putStack(null);
                            }
                            aSlot.onPickupFromSlot(aPlayer, aPlayerInventory.getItemStack());
                        } else if (aSlot.isItemValid(var13)) {
                            if (tTempStack.getItem() == var13.getItem() && tTempStack.getItemDamage() == var13.getItemDamage() &&
                                ItemStack.areItemStackTagsEqual(tTempStack, var13)) {
                                tTempStackSize = aMouseclick == 0 ? var13.stackSize : 1;
                                if (tTempStackSize > aSlot.getSlotStackLimit() - tTempStack.stackSize) {
                                    tTempStackSize = aSlot.getSlotStackLimit() - tTempStack.stackSize;
                                }
                                if (tTempStackSize > var13.getMaxStackSize() - tTempStack.stackSize) {
                                    tTempStackSize = var13.getMaxStackSize() - tTempStack.stackSize;
                                }
                                var13.splitStack(tTempStackSize);
                                if (var13.stackSize == 0) {
                                    aPlayerInventory.setItemStack(null);
                                }
                                tTempStack.stackSize += tTempStackSize;
                            } else if (var13.stackSize <= aSlot.getSlotStackLimit()) {
                                aSlot.putStack(var13);
                                aPlayerInventory.setItemStack(tTempStack);
                            }
                        } else if (tTempStack.getItem() == var13.getItem() && var13.getMaxStackSize() > 1 &&
                                   (!tTempStack.getHasSubtypes() || tTempStack.getItemDamage() == var13.getItemDamage()) && ItemStack.areItemStackTagsEqual(
                                tTempStack, var13)) {
                            tTempStackSize = tTempStack.stackSize;

                            if (tTempStackSize > 0 && tTempStackSize + var13.stackSize <= var13.getMaxStackSize()) {
                                var13.stackSize += tTempStackSize;
                                tTempStack = aSlot.decrStackSize(tTempStackSize);

                                if (tTempStack.stackSize == 0) {
                                    aSlot.putStack(null);
                                }

                                aSlot.onPickupFromSlot(aPlayer, aPlayerInventory.getItemStack());
                            }
                        }
                    }
                    aSlot.onSlotChanged();
                }
            }
        } else if (aShifthold == 2 && aMouseclick >= 0 && aMouseclick < 9) {
            aSlot = (Slot) this.inventorySlots.get(aSlotIndex);

            if (aSlot.canTakeStack(aPlayer)) {
                tTempStack = aPlayerInventory.getStackInSlot(aMouseclick);
                boolean var9 = tTempStack == null || aSlot.inventory == aPlayerInventory && aSlot.isItemValid(tTempStack);
                tTempStackSize = -1;

                if (!var9) {
                    tTempStackSize = aPlayerInventory.getFirstEmptyStack();
                    var9 = tTempStackSize > -1;
                }

                if (var9 && aSlot.getHasStack()) {
                    aHoldStack = aSlot.getStack();
                    aPlayerInventory.setInventorySlotContents(aMouseclick, aHoldStack);

                    if (tTempStack != null && (aSlot.inventory != aPlayerInventory || !aSlot.isItemValid(tTempStack))) {
                        if (tTempStackSize > -1) {
                            aPlayerInventory.addItemStackToInventory(tTempStack);
                            aSlot.decrStackSize(aHoldStack.stackSize);
                            aSlot.putStack(null);
                            aSlot.onPickupFromSlot(aPlayer, aHoldStack);
                        }
                    } else {
                        aSlot.decrStackSize(aHoldStack.stackSize);
                        aSlot.putStack(tTempStack);
                        aSlot.onPickupFromSlot(aPlayer, aHoldStack);
                    }
                } else if (tTempStack != null && !aSlot.getHasStack() && aSlot.isItemValid(tTempStack)) {
                    aPlayerInventory.setInventorySlotContents(aMouseclick, null);
                    aSlot.putStack(tTempStack);
                }
            }
        } else if (aShifthold == 3 && aPlayer.capabilities.isCreativeMode && aPlayerInventory.getItemStack() == null && aSlotIndex >= 0) {
            aSlot = (Slot) this.inventorySlots.get(aSlotIndex);
            if (aSlot != null && aSlot.getHasStack()) {
                tTempStack = GT_Utility.copyOrNull(aSlot.getStack());
                tTempStack.stackSize = tTempStack.getMaxStackSize();
                aPlayerInventory.setItemStack(tTempStack);
            }
        }
        return rStack;
    }

    @Override
    public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot) {
        try {
            return super.func_94530_a(par1ItemStack, par2Slot);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        return true;
    }

    @Override
    protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer) {
        try {
            super.retrySlotClick(par1, par2, par3, par4EntityPlayer);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer) {
        try {
            super.onContainerClosed(par1EntityPlayer);
            mTileEntity.closeInventory();
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory) {
        try {
            super.onCraftMatrixChanged(par1IInventory);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public void putStackInSlot(int par1, ItemStack par2ItemStack) {
        try {
            super.putStackInSlot(par1, par2ItemStack);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public void putStacksInSlots(ItemStack[] par1ArrayOfItemStack) {
        try {
            super.putStacksInSlots(par1ArrayOfItemStack);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public void updateProgressBar(int par1, int par2) {
        try {
            super.updateProgressBar(par1, par2);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public short getNextTransactionID(InventoryPlayer par1InventoryPlayer) {
        try {
            return super.getNextTransactionID(par1InventoryPlayer);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        return 0;
    }

    @Override
    public boolean isPlayerNotUsingContainer(EntityPlayer par1EntityPlayer) {
        try {
            return super.isPlayerNotUsingContainer(par1EntityPlayer);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        return true;
    }

    @Override
    public void setPlayerIsPresent(EntityPlayer par1EntityPlayer, boolean par2) {
        try {
            super.setPlayerIsPresent(par1EntityPlayer, par2);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    /**
     * Override this Function with something like "return mTileEntity.isUseableByPlayer(aPlayer);"
     */
    @Override
    public boolean canInteractWith(EntityPlayer aPlayer) {
        return false;
    }

    /**
     * merges provided ItemStack with the first avaliable one in the container/player inventory
     */
    @Override
    protected boolean mergeItemStack(ItemStack aStack, int aStartIndex, int aSlotCount, boolean par4) {
        boolean var5 = false;
        int var6 = aStartIndex;

        mTileEntity.markDirty();

        if (par4) {
            var6 = aSlotCount - 1;
        }

        Slot var7;
        ItemStack var8;

        if (aStack.isStackable()) {
            while (aStack.stackSize > 0 && (!par4 && var6 < aSlotCount || par4 && var6 >= aStartIndex)) {
                var7 = (Slot) this.inventorySlots.get(var6);
                var8 = var7.getStack();
                if (!(var7 instanceof GT_Slot_Holo) && !(var7 instanceof GT_Slot_Output) && var8 != null && var8.getItem() == aStack.getItem() &&
                    (!aStack.getHasSubtypes() || aStack.getItemDamage() == var8.getItemDamage()) && ItemStack.areItemStackTagsEqual(aStack, var8)) {
                    int var9 = var8.stackSize + aStack.stackSize;
                    if (var8.stackSize < mTileEntity.getInventoryStackLimit()) {
                        if (var9 <= aStack.getMaxStackSize()) {
                            aStack.stackSize = 0;
                            var8.stackSize = var9;
                            var7.onSlotChanged();
                            var5 = true;
                        } else if (var8.stackSize < aStack.getMaxStackSize()) {
                            aStack.stackSize -= aStack.getMaxStackSize() - var8.stackSize;
                            var8.stackSize = aStack.getMaxStackSize();
                            var7.onSlotChanged();
                            var5 = true;
                        }
                    }
                }

                if (par4) {
                    --var6;
                } else {
                    ++var6;
                }
            }
        }
        if (aStack.stackSize > 0) {
            if (par4) {
                var6 = aSlotCount - 1;
            } else {
                var6 = aStartIndex;
            }

            while (!par4 && var6 < aSlotCount || par4 && var6 >= aStartIndex) {
                var7 = (Slot) this.inventorySlots.get(var6);
                var8 = var7.getStack();

                if (var8 == null) {
                    int var10 = Math.min(aStack.stackSize, mTileEntity.getInventoryStackLimit());
                    var7.putStack(GT_Utility.copyAmount(var10, aStack));
                    var7.onSlotChanged();
                    aStack.stackSize -= var10;
                    var5 = true;
                    break;
                }

                if (par4) {
                    --var6;
                } else {
                    ++var6;
                }
            }
        }

        return var5;
    }

    @Override
    protected void func_94533_d() {
        try {
            super.func_94533_d();
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public boolean canDragIntoSlot(Slot par1Slot) {
        try {
            return super.canDragIntoSlot(par1Slot);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        return true;
    }

}
