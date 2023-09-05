package gregtech.common.gui.meganet;

import eu.usrv.yamcore.network.PacketDispatcher;
import gregtech.api.GregTech_API;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.IDWSCompatible;
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

    private @NonNull final EntityPlayer owner;

    private @NonNull final ItemStack source;

    private @NonNull final GT_MEGAnet.MEGAnetFilter filter;

    public GT_MEGAnet_Container(final @NonNull EntityPlayer owner, final @NonNull ItemStack source, final GT_MEGAnet.@NonNull MEGAnetFilter filter) {
        super();
        this.owner = owner;
        this.source = source;
        this.filter = filter;
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

    private void addSlots() {
        addFilterSlots();
        bindPlayerInventory();
    }

    private void addFilterSlots() {
        val numCols = getNumSlots() / 3;
        for (var rowIndex = 0; rowIndex < 3; rowIndex++) {
            val y = 18 + rowIndex * 18;
            for (var colIndex = 0; colIndex < numCols; colIndex++) {
                val slotIndex = colIndex + rowIndex * numCols;
                val x = 18 + colIndex * 18;
                addSlotToContainer(new GT_Slot_Holo(filter, slotIndex, x, y, true, false, 1));
            }
        }
    }

    private void bindPlayerInventory() {
        val nRows = 4;
        val numSlots = getNumSlots();
        var total = 0;
        val ySlotSpacing = 18;
        val xSlotSpacing = 18;
        val nSlotsInRow = getSlotsPerRow();
        val guiWidth = getGuiWidth();
        val xOffsetBase = (guiWidth - nSlotsInRow * xSlotSpacing) / 2;
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

    private int getGuiWidth() {
        return applyDWSBump(baseWidth());
    }

    public int getSlotsPerRow() {
        return GregTech_API.mDWS ? 18 : 9;
    }

    public int getNumSlots() {
        return getSlotsPerRow() * 4;
    }

    @Override
    public boolean canInteractWith(final EntityPlayer player) {
        return true;
    }

    public void synchronize() {
        if (!(source.getItem() instanceof GT_MEGAnet)) {
            return;
        }
        ((GT_MEGAnet) source.getItem()).setMEGANetFilter(source, filter);
    }

}
