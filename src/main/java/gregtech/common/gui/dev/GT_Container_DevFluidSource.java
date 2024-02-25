package gregtech.common.gui.dev;


import gregtech.api.GregTech_API;
import gregtech.api.enums.RSControlMode;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.dev.GT_MetaTileEntity_DevFluidSource;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Item_98;
import ic2.core.item.resources.ItemCell;
import lombok.Getter;
import lombok.val;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;


@Getter
public class GT_Container_DevFluidSource extends GT_Container_Dev<GT_MetaTileEntity_DevFluidSource, GT_MetaTileEntity_DevFluidSource.GUIData> {

    public GT_Container_DevFluidSource(
            final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity
                                      ) {
        super(aInventoryPlayer, aTileEntity, GT_MetaTileEntity_DevFluidSource.class, GT_MetaTileEntity_DevFluidSource.GUIData.class);
    }

    /**
     * @return
     */
    @Override
    public int getDWSWidthBump() {
        return 82;
    }

    /**
     * @return
     */
    @Override
    public int baseWidth() {
        return 256;
    }

    /**
     * To add the Slots to your GUI
     *
     * @param aPlayerInventory
     */
    @Override
    public void addSlots(final InventoryPlayer aPlayerInventory) {
        val slotX = getGuiWidth()  / 2 - (GregTech_API.mDWS ? 96 : 64) - 20;
        val slotY = 25;
        addSlotToContainer(new GT_Slot_Holo(mMetaTileEntity, 0, slotX, slotY, false, true, 1));
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
                } else if (itemInHand instanceof IFluidContainerItem) {
                    FluidStack fluidStackInHand = GT_Utility.getFluidForFilledItem(stackInHand, true);
                    if (fluidStackInHand != null) {
                        fluidStackInHand.amount = Integer.MAX_VALUE;
                    }
                    machine.setDrainableStack(fluidStackInHand);
                } else {
                    val fluidStackInHand = getFluidFromItem(stackInHand);
                    if (fluidStackInHand != null) {
                        val copy = fluidStackInHand.copy();
                        copy.amount = Integer.MAX_VALUE;
                        machine.setDrainableStack(copy);
                    }/*  else {
                        System.out.println("Item in hand: " + itemInHand);
                    } */
                }

            }
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    private static FluidStack getFluidFromItem(final ItemStack stack) {
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
        setRPT(0);
        setRPS(0);
        setPerTick(true);
        detectAndSendChanges();
    }

    protected void setRPT(final int value) {
        data.setRPT(value);
        detectAndSendChanges();
    }

    protected void setRPS(final int value) {
        data.setRPS(value);
        detectAndSendChanges();
    }

    protected void setPerTick(final boolean b) {
        data.setPerTick(b);
        detectAndSendChanges();
    }

    protected void syncRates() {
        if (data.isPerTick()) {
            setRPS(data.getRPT() * 20);
        } else {
            setRPT(data.getRPS() / 20);
        }
        detectAndSendChanges();
    }

}
