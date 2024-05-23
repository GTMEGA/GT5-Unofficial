package gregtech.common.gui;

import codechicken.nei.VisiblityData;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.TaggedInventoryArea;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GT_Packet_SetFilterSlot;
import lombok.val;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class GT_GUIContainer_Filter extends GT_GUIContainerMetaTile_Machine implements INEIGuiHandler {
    public GT_GUIContainer_Filter(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(new GT_Container_Filter(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/Filter.png");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public VisiblityData modifyVisiblity(GuiContainer gui, VisiblityData currentVisibility) {
        return currentVisibility;
    }

    @Override
    public Iterable<Integer> getItemSpawnSlots(GuiContainer gui, ItemStack item) {
        return Collections.emptyList();
    }

    @Override
    public List<TaggedInventoryArea> getInventoryAreas(GuiContainer gui) {
        return Collections.emptyList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean handleDragNDrop(GuiContainer gui, int mousex, int mousey, ItemStack draggedStack, int button) {
        Slot mouseOverSlot = null;

        for (val slot : (List<Slot>) gui.inventorySlots.inventorySlots) {
            if (gui.isMouseOverSlot(slot, mousex, mousey)) {
                mouseOverSlot = slot;
                break;
            }
        }

        if (!(mouseOverSlot instanceof GT_Slot_Holo)) {
            return false;
        }

        val slot = (GT_Slot_Holo) mouseOverSlot;

        val player = Minecraft.getMinecraft().thePlayer;

        val windowId = gui.inventorySlots.windowId;
        val transactionId = player.openContainer.getNextTransactionID(player.inventory);

        player.inventory.setItemStack(draggedStack.copy());
        this.mContainer.slotClick(slot.slotNumber, button, 1, player);

        val bmte = this.mContainer.getMetaTileEntity().getBaseMetaTileEntity();

        val packet = new GT_Packet_SetFilterSlot(bmte.getXCoord(),
                                                 bmte.getYCoord(),
                                                 bmte.getZCoord(),
                                                 slot.slotNumber,
                                                 draggedStack);

        GT_Values.NW.sendToServer(packet);

        return true;
    }

    @Override
    public boolean hideItemPanelSlot(GuiContainer gui, int x, int y, int w, int h) {
        return false;
    }
}
