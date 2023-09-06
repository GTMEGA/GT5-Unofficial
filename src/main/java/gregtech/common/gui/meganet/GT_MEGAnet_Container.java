package gregtech.common.gui.meganet;


import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.IDWSCompatible;
import gregtech.api.net.GT_Packet_InventoryUpdate;
import gregtech.common.items.GT_MEGAnet;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import lombok.var;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


@Getter
public class GT_MEGAnet_Container extends Container implements IDWSCompatible {

    public static @NonNull
    final GT_MEGAnet MEGANET = GregTech_API.sMEGAnet;

    private @NonNull
    final EntityPlayer owner;

    private @NonNull
    final ItemStack stack;

    private @NonNull
    final GT_MEGAnet.MEGAnetFilter filter;

    private final int slotIndex;

    private final boolean isBauble;

    public GT_MEGAnet_Container(
            final @NonNull EntityPlayer owner,
            final @NonNull ItemStack stack,
            final GT_MEGAnet.@NonNull MEGAnetFilter filter,
            final int slotIndex,
            final boolean isBauble
                               ) {
        super();
        this.owner = owner;
        this.stack = stack;
        this.filter = filter;
        this.slotIndex = slotIndex;
        this.isBauble = isBauble;
        addSlots();
    }

    @Override
    public int getDWSWidthBump() {
        return 0;
    }

    @Override
    public int baseWidth() {
        return 338;
    }

    public void sendSettingUpdate() {
        if (!isServerSide()) {
            GT_Values.NW.sendToServer(new GT_Packet_InventoryUpdate(owner, stack.getItem(), isBauble, slotIndex,
                                                                    new GT_MEGAnet.MEGANetSettingChange(MEGANET.isEnabled(stack), MEGANET.getRange(stack))
            ));
        }
    }

    private boolean isServerSide() {
        return !owner.worldObj.isRemote;
    }

    public void sendFilterUpdate() {
        if (!isServerSide()) {
            GT_Values.NW.sendToServer(new GT_Packet_InventoryUpdate(owner, stack.getItem(), isBauble, slotIndex, filter));
        }
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {
        if (isServerSide()) {
            synchronize();
        }
        super.detectAndSendChanges();
    }

    /**
     * @param slotIndex
     * @param clickType
     * @param shift
     * @param player
     * @return
     */
    @Override
    public ItemStack slotClick(final int slotIndex, final int clickType, final int shift, final EntityPlayer player) {
        if (slotIndex < 0) {
            return super.slotClick(slotIndex, clickType, shift, player);
        }
        val slot = (Slot) inventorySlots.get(slotIndex);
        if (slot == null || slot.getStack() != null && slot.getStack().getItem() instanceof GT_MEGAnet) {
            return null;
        }
        if (slotIndex < GT_MEGAnet.MEGAnetFilter.MAX_FILTERED) {
            slot.putStack(player.inventory.getItemStack());
            synchronize();
            return null;
        }
        val result = super.slotClick(slotIndex, clickType, shift, player);
        synchronize();
        return result;
    }

    /**
     * @param id
     * @param val
     */
    @Override
    public void updateProgressBar(final int id, final int val) {
        super.updateProgressBar(id, val);
    }

    @Override
    public boolean canInteractWith(final EntityPlayer player) {
        return true;
    }

    public void synchronize() {
        if (!stackValid()) {
            return;
        }
        MEGANET.setMEGANetFilter(stack, filter);
    }

    public boolean stackValid() {
        return stack.getItem() instanceof GT_MEGAnet;
    }

    public int getSlotsPerRow() {
        return GregTech_API.mDWS ? 18 : 9;
    }

    public int getNumSlots() {
        return GT_MEGAnet.MEGAnetFilter.MAX_FILTERED;
    }

    protected boolean toggleMeganet() {
        setMEGAnetActive(!getMEGAnetActive());
        synchronize();
        sendSettingUpdate();
        return getMEGAnetActive();
    }

    protected boolean getMEGAnetActive() {
        if (!stackValid()) {
            return false;
        }
        return MEGANET.isEnabled(stack);
    }

    protected void setMEGAnetActive(final boolean active) {
        if (!stackValid()) {
            return;
        }
        MEGANET.setEnabled(stack, active);
        sendSettingUpdate();
    }

    private void addSlots() {
        addFilterSlots();
        bindPlayerInventory();
    }

    private void addFilterSlots() {
        val numCols = getNumSlots() / 3;
        val left = getSlotLeft() + 1;
        for (var rowIndex = 0; rowIndex < 3; rowIndex++) {
            val y = 10 + rowIndex * 18;
            for (var colIndex = 0; colIndex < numCols; colIndex++) {
                val slotIndex = colIndex + rowIndex * numCols;
                val x = left + colIndex * 18;
                addSlotToContainer(new GT_Slot_Holo(filter, slotIndex, x, y, true, false, 1));
            }
        }
    }

    private void bindPlayerInventory() {
        val nRows = getNumPlayerInventoryRows();
        val numSlots = getNumPlayerInventorySlots();
        var total = 0;
        val ySlotSpacing = 18;
        val xSlotSpacing = 18;
        val nSlotsInRow = getSlotsPerRow();
        val xOffsetBase = getSlotLeft();
        val hotBarSpacing = 4;
        for (var rowIndex = 0; rowIndex < nRows && total < numSlots; rowIndex++) {
            val isHotbar = rowIndex == 0;
            val y = 84 + (isHotbar ? hotBarSpacing + nRows * ySlotSpacing : 0) + (rowIndex - 1) * ySlotSpacing;
            val xOffset = xOffsetBase + 1;
            for (var colIndex = 0; colIndex < nSlotsInRow && total < numSlots; colIndex++) {
                val slotIndex = colIndex + rowIndex * nSlotsInRow;
                val x = xOffset + colIndex * xSlotSpacing;
                addSlotToContainer(new Slot(owner.inventory, slotIndex, x, y));
                total += 1;
            }
        }
    }

    private int getNumPlayerInventoryRows() {
        return 4;
    }

    private int getSlotLeft() {
        return (getGuiWidth() - getSlotsPerRow() * 18) / 2;
    }

    private int getNumPlayerInventorySlots() {
        return getNumPlayerInventoryRows() * getSlotsPerRow();
    }

    private int getGuiWidth() {
        return applyDWSBump(baseWidth());
    }

}
