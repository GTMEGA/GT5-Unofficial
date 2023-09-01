package gregtech.api.util.interop;


import codechicken.nei.guihook.GuiContainerManager;
import cpw.mods.fml.common.Optional;
import gregtech.api.enums.GT_Values;
import net.minecraft.client.gui.inventory.GuiContainer;


public final class NEIInterop extends NEIInteropBase {

    public static final NEIInterop INSTANCE = new NEIInterop();

    private NEIInterop() {

    }

    /**
     * @param container
     */
    @Override
    @Optional.Method(modid = GT_Values.MOD_ID_NEI)
    public void preDraw(final GuiContainer container) {
        final GuiContainerManager manager = GuiContainerManager.getManager(container);
        // Mixin fuckery
        //noinspection ConstantValue
        if (manager != null) {
            manager.preDraw();
        }
    }

    /**
     * @param container
     * @param mouseX
     * @param mouseY
     */
    @Override
    @Optional.Method(modid = GT_Values.MOD_ID_NEI)
    public void renderObjects(final GuiContainer container, final int mouseX, final int mouseY) {
        final GuiContainerManager manager = GuiContainerManager.getManager(container);
        //noinspection ConstantValue
        if (manager != null) {
            manager.renderObjects(mouseX, mouseY);
        }
    }

    /**
     * @param container
     * @param mouseX
     * @param mouseY
     */
    @Override
    @Optional.Method(modid = GT_Values.MOD_ID_NEI)
    public void renderTooltips(final GuiContainer container, final int mouseX, final int mouseY) {
        final GuiContainerManager manager = GuiContainerManager.getManager(container);
        //noinspection ConstantValue
        if (manager != null) {
            manager.renderToolTips(mouseX, mouseY);
        }
    }

    /**
     * @param container
     * @param mouseX
     * @param mouseY
     * @return
     */
    @Override
    public boolean checkSlotUnderMouse(final GuiContainer container, final int mouseX, final int mouseY) {
        final GuiContainerManager manager = GuiContainerManager.getManager(container);
        //noinspection ConstantValue
        if (manager != null) {
            return manager.objectUnderMouse(mouseX, mouseY);
        }
        return false;
    }

}
