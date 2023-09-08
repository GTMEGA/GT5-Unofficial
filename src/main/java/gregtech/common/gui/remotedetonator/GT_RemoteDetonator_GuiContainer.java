package gregtech.common.gui.remotedetonator;

import gregtech.api.gui.GT_GUIContainer_Plus;
import gregtech.api.gui.widgets.GT_GuiScrollPanel;
import gregtech.api.gui.widgets.GT_GuiSlider;
import lombok.Getter;
import lombok.val;
import lombok.var;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.util.Random;


@Getter
public class GT_RemoteDetonator_GuiContainer extends GT_GUIContainer_Plus {

    private final GT_RemoteDetonator_Container remoteDetonatorContainer;

    private GT_GuiScrollPanel<GT_RemoteDetonator_GuiContainer> scrollPanel;

    private GT_GuiSlider scrollBar;

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
        val rand = new Random();
        val x = 13;
        val yOffset = 13;
        val height = 140;
        val barHeight = 10;
        val scrollHeight = height - 4;
        scrollPanel = new GT_GuiScrollPanel<>(this, 0, x, yOffset, 100, height, 0.0, 96, scrollHeight);
        scrollBar = new GT_GuiSlider(this, 1, 125, 13, 80, barHeight, 0.0, 1.0, 0.0, -1);
        //
        // scrollPanel.setOnUpdateBehavior((screen, element, mouseX, mouseY, clickType) -> scrollBar.setBarDiameter(scrollPanel.effectiveWindowHeight()));
        for (int i = 0; i < 10; i++) {
            val detonatorEntry = new RemoteDetonator_GuiEntry(scrollPanel);
        }
        scrollBar.setBarDiameter(0.5);
        //
        scrollBar.setOnChange((slider) -> scrollPanel.setCurrentScroll(scrollBar.getCurrent()));
        scrollBar.setOnUpdateBehavior((screen, element, mouseX, mouseY, clickType) -> {
            /* var delta = rand.nextDouble() * 0.02 - 0.01;
            scrollBar.setBarDiameter(Math.min(1.0, Math.max(0.0, scrollBar.getBarDiameter() + delta))); */
        });
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
        val textColor = new Color(0x00, 0xFF, 0xFF, 0xFF);
        drawString("Bar Diameter = " + scrollBar.getBarDiameter(), 125, 35, textColor);
    }

}
