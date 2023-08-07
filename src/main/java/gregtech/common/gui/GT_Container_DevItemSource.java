package gregtech.common.gui;


import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;


public class GT_Container_DevItemSource extends GT_ContainerMetaTile_Machine {

    public GT_Container_DevItemSource(
            final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity
                                     ) {
        super(aInventoryPlayer, aTileEntity);
    }

    /**
     * @return
     */
    @Override
    protected int getGuiWidth() {
        return 256;
    }

    /**
     * Is Player-Inventory visible?
     */
    @Override
    public boolean doesBindPlayerInventory() {
        return super.doesBindPlayerInventory();
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
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

}
