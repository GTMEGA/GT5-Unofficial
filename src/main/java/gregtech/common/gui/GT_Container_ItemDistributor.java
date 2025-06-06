package gregtech.common.gui;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.automation.GT_MetaTileEntity_ItemDistributor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Container_ItemDistributor extends GT_ContainerMetaTile_Machine {
    public GT_Container_ItemDistributor(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlotToContainer(new Slot(this.mTileEntity, x + y * 9, 8 + x * 18, 5 + y * 18));
            }
        }
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 27, 8, 63, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 27, 26, 63, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 27, 44, 63, false, true, 1));
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (aSlotIndex < 27) {
            return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        }
        Slot tSlot = (Slot) this.inventorySlots.get(aSlotIndex);
        if (tSlot != null) {
            if (this.mTileEntity.getMetaTileEntity() == null) {
                return null;
            }
            if (aSlotIndex == 27) {
                ((GT_MetaTileEntity_ItemDistributor) this.mTileEntity.getMetaTileEntity()).bOutput = (!((GT_MetaTileEntity_ItemDistributor) this.mTileEntity.getMetaTileEntity()).bOutput);
                if (((GT_MetaTileEntity_ItemDistributor) this.mTileEntity.getMetaTileEntity()).bOutput) {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("116","Passing energy to outputs."));
                } else {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("117","Energy pass-through disabled."));
                }
                return null;
            }
            if (aSlotIndex == 28) {
                ((GT_MetaTileEntity_ItemDistributor) this.mTileEntity.getMetaTileEntity()).bRedstoneIfFull = (!((GT_MetaTileEntity_ItemDistributor) this.mTileEntity.getMetaTileEntity()).bRedstoneIfFull);
                if (((GT_MetaTileEntity_ItemDistributor) this.mTileEntity.getMetaTileEntity()).bRedstoneIfFull) {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("118","Emit Redstone if no Slot is free"));
                } else {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("119","Don't emit Redstone"));
                }
                return null;
            }
            if (aSlotIndex == 29) {
                ((GT_MetaTileEntity_ItemDistributor) this.mTileEntity.getMetaTileEntity()).bInvert = (!((GT_MetaTileEntity_ItemDistributor) this.mTileEntity.getMetaTileEntity()).bInvert);
                if (((GT_MetaTileEntity_ItemDistributor) this.mTileEntity.getMetaTileEntity()).bInvert) {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("120","Invert Redstone"));
                } else {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("121","Don't invert Redstone"));
                }
                return null;
            }
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    @Override
    public int getSlotCount() {
        return 27;
    }

    @Override
    public int getShiftClickSlotCount() {
        return 27;
    }
}
