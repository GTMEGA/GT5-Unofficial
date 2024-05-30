package gregtech.api.gui;

import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

@Getter
public abstract class GT_Slot_Wrapper extends Slot {

    protected final Slot wrappedSlot;

    public GT_Slot_Wrapper(final Slot wrapped) {
        super(wrapped.inventory, wrapped.getSlotIndex(), wrapped.xDisplayPosition, wrapped.yDisplayPosition);
        wrappedSlot = wrapped;
    }

    /**
     * @param p_75220_1_
     * @param p_75220_2_
     */
    @Override
    public void onSlotChange(final ItemStack p_75220_1_, final ItemStack p_75220_2_) {
        wrappedSlot.onSlotChange(p_75220_1_, p_75220_2_);
    }

    /**
     * @param p_75210_1_
     * @param p_75210_2_
     */
    @Override
    protected void onCrafting(final ItemStack p_75210_1_, final int p_75210_2_) {
//        wrappedSlot.onCrafting(p_75210_1_, p_75210_2_);
    }

    /**
     * @param p_75208_1_
     */
    @Override
    protected void onCrafting(final ItemStack p_75208_1_) {
//        wrappedSlot.onCrafting(p_75208_1_);
    }

    /**
     * @param p_82870_1_
     * @param p_82870_2_
     */
    @Override
    public void onPickupFromSlot(final EntityPlayer p_82870_1_, final ItemStack p_82870_2_) {
        wrappedSlot.onPickupFromSlot(p_82870_1_, p_82870_2_);
    }

    /**
     * @param p_75214_1_
     *
     * @return
     */
    @Override
    public boolean isItemValid(final ItemStack p_75214_1_) {
        return wrappedSlot.isItemValid(p_75214_1_);
    }

    /**
     * @return
     */
    @Override
    public ItemStack getStack() {
        return wrappedSlot.getStack();
    }

    /**
     * @return
     */
    @Override
    public boolean getHasStack() {
        return wrappedSlot.getHasStack();
    }

    /**
     * @param p_75215_1_
     */
    @Override
    public void putStack(final ItemStack p_75215_1_) {
        wrappedSlot.putStack(p_75215_1_);
    }

    /**
     *
     */
    @Override
    public void onSlotChanged() {
        wrappedSlot.onSlotChanged();
    }

    /**
     * @return
     */
    @Override
    public int getSlotStackLimit() {
        return wrappedSlot.getSlotStackLimit();
    }

    /**
     * @param p_75209_1_
     *
     * @return
     */
    @Override
    public ItemStack decrStackSize(final int p_75209_1_) {
        return wrappedSlot.decrStackSize(p_75209_1_);
    }

    /**
     * @param p_75217_1_
     * @param p_75217_2_
     *
     * @return
     */
    @Override
    public boolean isSlotInInventory(final IInventory p_75217_1_, final int p_75217_2_) {
        return wrappedSlot.isSlotInInventory(p_75217_1_, p_75217_2_);
    }

    /**
     * @param p_82869_1_
     *
     * @return
     */
    @Override
    public boolean canTakeStack(final EntityPlayer p_82869_1_) {
        return wrappedSlot.canTakeStack(p_82869_1_);
    }

    /**
     * @return
     */
    @Override
    public boolean func_111238_b() {
        return wrappedSlot.func_111238_b();
    }

    /**
     * @return
     */
    @Override
    public int getSlotIndex() {
        return wrappedSlot.getSlotIndex();
    }

}
