package gregtech.common.gui.remotedetonator;

import gregtech.api.gui.GT_GUIContainer_Plus;
import gregtech.api.gui.widgets.GT_GuiScrollPanel;
import gregtech.api.gui.widgets.GT_GuiSlider;
import lombok.Getter;
import lombok.val;
import net.minecraft.client.Minecraft;


@Getter
public class GT_RemoteDetonator_GuiContainer extends GT_GUIContainer_Plus {

    private final GT_RemoteDetonator_Container remoteDetonatorContainer;

    public GT_RemoteDetonator_GuiContainer(final GT_RemoteDetonator_Container aContainer) {
        super(aContainer, "gregtech:textures/gui/remote_detonator_gui.png", 338, 166);
        this.remoteDetonatorContainer = aContainer;
        addGUIElements();
    }

    /**
     * Causes the screen to lay out its subcomponents again. This is the equivalent of the Java call
     * Container.validate()
     *
     * @param minecraft
     * @param width
     * @param height
     */
    @Override
    public void setWorldAndResolution(final Minecraft minecraft, final int width, final int height) {
        super.setWorldAndResolution(minecraft, width, height);
    }

    private void addGUIElements() {
        val x = 5;
        val yOffset = 5;
        val height = 100;
        val barSpace = 6;
        val barHeight = 10;
        val scrollHeight = height - 4;
        // val temp = (166 - (barHeight * 2 + barSpace + height + yOffset)) / 2;
        val barY = yOffset + height + barSpace;
        val scrollPanel = new GT_GuiScrollPanel<>(this, 0, x, yOffset, 80, height, 0.0, 76, scrollHeight);
        val scrollBar = new GT_GuiSlider(this, 1, x, barY, 80, barHeight, 0.0, 1.0, 0.0, -1);
        //
        // scrollPanel.setOnUpdateBehavior((screen, element, mouseX, mouseY, clickType) -> scrollBar.setBarDiameter(scrollPanel.effectiveWindowHeight()));
        for (int i = 0; i < 10; i++) {
            val scroll = new RemoteDetonator_GuiEntry(scrollPanel);
            scrollPanel.addScrollableElement(scroll);
        }
        scrollBar.setBarDiameter(0.82);
        //
        scrollBar.setOnChange((slider) -> scrollPanel.setCurrentScroll(scrollBar.getCurrent()));
        scrollBar.setLiveUpdate(true);
    }

    @Override
    public boolean hasDWSAlternativeBackground() {
        return false;
    }

    @Override
    protected boolean isNEIEnabled() {
        return false;
    }

    @Override
    public void drawExtras(final int mouseX, final int mouseY, final float parTicks) {

    }

}
