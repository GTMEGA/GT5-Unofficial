package gregtech.api.gui;


import net.minecraft.inventory.Slot;

public abstract class GT_RichGuiContainer_Machine extends GT_RichGuiContainer {

    public final GT_ContainerMetaTile_Machine mContainer;

    public GT_RichGuiContainer_Machine(final GT_ContainerMetaTile_Machine aContainer, final String aGUIBackground, int width, int height) {
        super(aContainer, aGUIBackground, width, height);
        this.mContainer = aContainer;
    }

    /**
     * @param slot
     */
    @Override
    protected void drawSlot(final Slot slot) {
        super.drawSlot(slot);
    }

    /**
     * @param slot
     */
    @Override
    protected void partialDrawSlot(final Slot slot) {
        super.partialDrawSlot(slot);
    }

    /**
     * @param slot
     */
    @Override
    protected void renderSlotHighlight(final Slot slot) {
        super.renderSlotHighlight(slot);
    }

    public abstract void sendUpdateToServer();

}
