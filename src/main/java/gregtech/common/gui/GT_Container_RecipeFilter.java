package gregtech.common.gui;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.automation.GT_MetaTileEntity_RecipeFilter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Container_RecipeFilter extends GT_ContainerMetaTile_Machine {

    public GT_Container_RecipeFilter(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aPlayerInventory) {

        addSlotToContainer(new Slot(this.mTileEntity, 0, 98, 5));
        addSlotToContainer(new Slot(this.mTileEntity, 1, 116, 5));
        addSlotToContainer(new Slot(this.mTileEntity, 2, 134, 5));
        addSlotToContainer(new Slot(this.mTileEntity, 3, 98, 23));
        addSlotToContainer(new Slot(this.mTileEntity, 4, 116, 23));
        addSlotToContainer(new Slot(this.mTileEntity, 5, 134, 23));
        addSlotToContainer(new Slot(this.mTileEntity, 6, 98, 41));
        addSlotToContainer(new Slot(this.mTileEntity, 7, 116, 41));
        addSlotToContainer(new Slot(this.mTileEntity, 8, 134, 41));

        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 9, 35, 14, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 10, 35, 32, false, true, 1));

        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 11, 8, 63, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 11, 26, 63, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 11, 44, 63, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 11, 62, 63, false, true, 1));
    }

    @Override
    public int getSlotCount() {
        return 9;
    }

    @Override
    public int getShiftClickSlotCount() {
        return 9;
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (aSlotIndex < 9) {
            return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        }
        Slot tSlot = (Slot) this.inventorySlots.get(aSlotIndex);
        if (tSlot != null) {
            if (!(this.mTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_RecipeFilter)) {
                return null;
            }
            if (aSlotIndex == 9) {
                ItemStack stack = aPlayer.inventory.getItemStack();
                if (stack == null) {
                    ((GT_MetaTileEntity_RecipeFilter) this.mTileEntity.getMetaTileEntity()).clear();
                } else {
                    if (((GT_MetaTileEntity_RecipeFilter) this.mTileEntity.getMetaTileEntity()).setFilterFromStack(stack.copy())) {
                        tSlot.putStack(GT_Utility.copyAmount(1L, stack.copy()));
                    }
                }
                return null;
            } else if (aSlotIndex == 10) {
                ItemStack stack = aPlayer.inventory.getItemStack();
                if (((GT_MetaTileEntity_RecipeFilter) this.mTileEntity.getMetaTileEntity()).setCircuitFromStack(stack)) {
                    tSlot.putStack(GT_Utility.copyAmount(1L, stack.copy()));
                }
            } else if (aSlotIndex == 11) {
                ((GT_MetaTileEntity_RecipeFilter) this.mTileEntity.getMetaTileEntity()).bOutput = !((GT_MetaTileEntity_RecipeFilter) this.mTileEntity.getMetaTileEntity()).bOutput;
                if (((GT_MetaTileEntity_RecipeFilter) this.mTileEntity.getMetaTileEntity()).bOutput) {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("116", "Emit Energy to Outputside"));
                } else {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("117", "Don't emit Energy"));
                }
                return null;
            } else if (aSlotIndex == 12) {
                ((GT_MetaTileEntity_RecipeFilter) this.mTileEntity.getMetaTileEntity()).bRedstoneIfFull = (!((GT_MetaTileEntity_RecipeFilter) this.mTileEntity.getMetaTileEntity()).bRedstoneIfFull);
                if (((GT_MetaTileEntity_RecipeFilter) this.mTileEntity.getMetaTileEntity()).bRedstoneIfFull) {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("122", "Emit Redstone if slots contain something"));
                } else {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("123", "Don't emit Redstone"));
                }
                return null;
            } else if (aSlotIndex == 13) {
                ((GT_MetaTileEntity_RecipeFilter) this.mTileEntity.getMetaTileEntity()).bInvert = (!((GT_MetaTileEntity_RecipeFilter) this.mTileEntity.getMetaTileEntity()).bInvert);
                if (((GT_MetaTileEntity_RecipeFilter) this.mTileEntity.getMetaTileEntity()).bInvert) {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("120", "Invert Redstone"));
                } else {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("121", "Don't invert Redstone"));
                }
                return null;
            } else if (aSlotIndex == 14) {
                ((GT_MetaTileEntity_RecipeFilter) this.mTileEntity.getMetaTileEntity()).invertFilter = (!((GT_MetaTileEntity_RecipeFilter) this.mTileEntity.getMetaTileEntity()).invertFilter);
                if (((GT_MetaTileEntity_RecipeFilter) this.mTileEntity.getMetaTileEntity()).invertFilter) {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("124", "Invert Filter"));
                } else {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("125", "Don't invert Filter"));
                }
                return null;
            }
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }
}
