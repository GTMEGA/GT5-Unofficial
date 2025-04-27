package gregtech.api.gui;

import gregtech.api.interfaces.tileentity.ICoverable;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public abstract class GT_GUICover extends GT_GUIScreen {

    public final ICoverable tile;

    public GT_GUICover(ICoverable tile, int width, int height, ItemStack cover) {
        super(width, height, cover == null ? "" : cover.getDisplayName());
        this.tile = tile;
        headerIcon.setItem(cover);
    }

    public GT_GUICover(Container container, ICoverable tile, int width, int height, ItemStack cover) {
        super(container,width, height, cover == null ? "" : cover.getDisplayName());
        this.tile = tile;
        headerIcon.setItem(cover);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (!tile.isUseableByPlayer(mc.thePlayer)) {
            closeScreen();
        }
    }
}