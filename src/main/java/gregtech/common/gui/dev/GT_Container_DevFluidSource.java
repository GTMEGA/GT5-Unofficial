package gregtech.common.gui.dev;


import gregtech.api.GregTech_API;
import gregtech.api.enums.RSControlMode;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.gui.GT_Slot_Wrapper_Icon;
import gregtech.api.gui.widgets.icon.GT_GuiIcon;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.dev.GT_MetaTileEntity_DevFluidSource;
import gregtech.api.util.GT_Utility;
import lombok.Getter;
import lombok.val;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;


@Getter
public class GT_Container_DevFluidSource extends GT_Container_Dev<GT_MetaTileEntity_DevFluidSource, GT_MetaTileEntity_DevFluidSource.GUIData> {

    public GT_Container_DevFluidSource(
            final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity
                                      ) {
        super(aInventoryPlayer, aTileEntity, GT_MetaTileEntity_DevFluidSource.class, GT_MetaTileEntity_DevFluidSource.GUIData.class);
    }

    /**
     * To add the Slots to your GUI
     *
     * @param aPlayerInventory the Inventory of the Player
     */
    @Override
    public void addSlots(final InventoryPlayer aPlayerInventory) {
        val slotX = getGuiWidth() / 2 - (GregTech_API.mDWS ? 96 : 64) - 20;
        val slotY = 25;
        addSlotToContainer(new GT_Slot_Wrapper_Icon(new GT_Slot_Holo(mMetaTileEntity, 0, slotX, slotY, false, true, 1), GT_GuiIcon.INV_SLOT_INPUT, GT_GuiIcon.PLAYER_INV_SLOT_HIGHLIGHT));
    }

    /**
     * @return
     */
    @Override
    public int baseWidth() {
        return 256;
    }

    /**
     * @return
     */
    @Override
    public int getDWSWidthBump() {
        return 82;
    }

    /**
     * Amount of regular Slots in the GUI (so, non-HoloSlots)
     */
    @Override
    public int getSlotCount() {
        return super.getSlotCount();
    }

    /**
     * @return
     */
    @Override
    public int getShiftClickStartIndex() {
        return super.getShiftClickStartIndex();
    }

    /**
     * Amount of Slots in the GUI the player can Shift-Click into. Uses also getSlotStartIndex
     */
    @Override
    public int getShiftClickSlotCount() {
        return super.getShiftClickSlotCount();
    }

    /**
     * @return
     */
    @Override
    protected int getGuiWidth() {
        return applyDWSBump(baseWidth());
    }

    /**
     * @param aSlotIndex
     * @param aMouseclick
     * @param aShifthold
     * @param aPlayer
     * @return
     */
    @Override
    public ItemStack slotClick(final int aSlotIndex, final int aMouseclick, final int aShifthold, final EntityPlayer aPlayer) {
        if (aSlotIndex < 0) {
            return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        }
        val slotClicked = (Slot) inventorySlots.get(aSlotIndex);
        if (slotClicked != null) {
            if (!(mTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_DevFluidSource)) {
                return null;
            }
            if (aSlotIndex == 0) {
                val stackInHand = aPlayer.inventory.getItemStack();
                val itemInHand = stackInHand == null ? null : stackInHand.getItem();
                if (stackInHand == null || itemInHand == null) {
                    machine.setDrainableStack(null);
                } else {
                    val fluidStackInHand = getFluidFromItem(stackInHand);
                    if (fluidStackInHand != null) {
                        val copy = fluidStackInHand.copy();
                        copy.amount = Integer.MAX_VALUE;
                        machine.setDrainableStack(copy);
                    }
                }

            }
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    private static FluidStack getFluidFromItem(final ItemStack stack) {
        // TODO: Figure out which of these is preferable or if both might be. Works for now but idk. API fucky.
        // return FluidContainerRegistry.getFluidForFilledItem(stackInHand);
        return GT_Utility.getFluidForFilledItem(stack, true);
    }

    protected void setMode(final RSControlMode mode) {
        data.setMode(mode);
        detectAndSendChanges();
    }

    protected boolean toggleActive() {
        data.setActive(!data.isActive());
        detectAndSendChanges();
        return data.isActive();
    }

    protected void zeroOut() {
        setRate(0);
        setFrequency(0);
        detectAndSendChanges();
    }

    public void setRate(final int rate) {
        data.setRate(rate);
    }

    public void setFrequency(final int frequency) {
        data.setFrequency(frequency);
    }

}
