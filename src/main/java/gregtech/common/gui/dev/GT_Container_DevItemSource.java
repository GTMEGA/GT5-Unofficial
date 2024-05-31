package gregtech.common.gui.dev;


import gregtech.api.enums.RSControlMode;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.gui.GT_Slot_Output;
import gregtech.api.gui.GT_Slot_Wrapper_Icon;
import gregtech.api.gui.widgets.icon.GT_GuiIcon;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.dev.GT_MetaTileEntity_DevItemSource;
import gregtech.api.util.GT_Utility;
import lombok.Getter;
import lombok.val;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


@Getter
public class GT_Container_DevItemSource extends GT_Container_Dev<GT_MetaTileEntity_DevItemSource, GT_MetaTileEntity_DevItemSource.GUIData> {

    public GT_Container_DevItemSource(
            final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity
                                     ) {
        super(aInventoryPlayer, aTileEntity, GT_MetaTileEntity_DevItemSource.class, GT_MetaTileEntity_DevItemSource.GUIData.class);
    }

    /**
     * To add the Slots to your GUI
     *
     * @param aPlayerInventory
     */
    @Override
    public void addSlots(final InventoryPlayer aPlayerInventory) {
        addSlotToContainer(new GT_Slot_Wrapper_Icon(new GT_Slot_Output(this.mTileEntity, 0, slotX(), 54), GT_GuiIcon.INV_SLOT_INPUT, GT_GuiIcon.PLAYER_INV_SLOT_HIGHLIGHT));
        addSlotToContainer(new GT_Slot_Wrapper_Icon(new GT_Slot_Holo(this.mTileEntity, 1, slotX(), 24, false, false, 1), GT_GuiIcon.INV_SLOT_OUTPUT, GT_GuiIcon.PLAYER_INV_SLOT_HIGHLIGHT));
    }

    public int slotX() {
        return 40;
    }

    /**
     * @param aSlotIndex
     * @param aMouseclick
     * @param aShifthold
     * @param aPlayer
     *
     * @return
     */
    @Override
    public ItemStack slotClick(final int aSlotIndex, final int aMouseclick, final int aShifthold, final EntityPlayer aPlayer) {
        if (aSlotIndex < 1) {
            return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        }
        val slotClicked = (Slot) inventorySlots.get(aSlotIndex);
        if (slotClicked != null) {
            if (!(mTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_DevItemSource)) {
                return null;
            }
            if (aSlotIndex == 1) {
                val currentStack = aPlayer.inventory.getItemStack();
                ((GT_MetaTileEntity_DevItemSource) mTileEntity.getMetaTileEntity()).clearItem();
                if (currentStack != null) {
                    ((GT_MetaTileEntity_DevItemSource) mTileEntity.getMetaTileEntity()).setStored(currentStack);
                    slotClicked.putStack(GT_Utility.copyAmount(1, currentStack.copy()));
                } else {
                    slotClicked.putStack(null);
                }
            }
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    /**
     * Amount of regular Slots in the GUI (so, non-HoloSlots)
     */
    @Override
    public int getSlotCount() {
        return 2;
    }

    /**
     * @return
     */
    @Override
    public int getShiftClickStartIndex() {
        return 0;
    }

    /**
     * Amount of Slots in the GUI the player can Shift-Click into. Uses also getSlotStartIndex
     */
    @Override
    public int getShiftClickSlotCount() {
        return 2;
    }

    /**
     * @return
     */
    @Override
    protected int getGuiWidth() {
        return applyDWSBump(baseWidth());
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

    public boolean toggleActive() {
        data.setActive(!data.isActive());
        detectAndSendChanges();
        return data.isActive();
    }

    /**
     *
     */
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

    /**
     * @param changeID
     * @param data
     */
    @Override
    public void updateProgressBar(final int changeID, final int data) {
        super.updateProgressBar(changeID, data);
    }

    /**
     * @return
     */
    @Override
    public boolean shouldReceiveLiveServerUpdates() {
        return false;
    }

    protected void setRedstoneMode(final RSControlMode redstoneMode) {
        data.setRedstoneMode(redstoneMode);
        detectAndSendChanges();
    }

    protected void setActive(final boolean active) {
        data.setActive(active);
        detectAndSendChanges();
    }

    protected void zeroOut() {
        setRate(0);
        setFrequency(20);
        detectAndSendChanges();
    }

    void setRate(final int rate) {
        data.setRate(rate);
        detectAndSendChanges();
    }

    void setFrequency(final int frequency) {
        data.setFrequency(frequency);
        detectAndSendChanges();
    }

}
