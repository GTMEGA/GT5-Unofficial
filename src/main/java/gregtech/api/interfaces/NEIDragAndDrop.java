package gregtech.api.interfaces;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

public interface NEIDragAndDrop {
    boolean handleDragNDrop(int mousex, int mousey, ItemStack draggedStack, int button);
}
