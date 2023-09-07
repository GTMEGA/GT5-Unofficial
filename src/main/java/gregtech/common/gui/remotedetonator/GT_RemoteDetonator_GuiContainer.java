package gregtech.common.gui.remotedetonator;

import gregtech.api.gui.GT_GUIContainer_Plus;
import gregtech.api.gui.widgets.GT_GuiScrollPanel;
import gregtech.api.gui.widgets.GT_GuiSlider;
import lombok.Getter;
import lombok.val;

import java.util.Random;


@Getter
public class GT_RemoteDetonator_GuiContainer extends GT_GUIContainer_Plus {

    private final GT_RemoteDetonator_Container remoteDetonatorContainer;

    public GT_RemoteDetonator_GuiContainer(final GT_RemoteDetonator_Container aContainer) {
        super(aContainer, "gregtech:textures/gui/remote_detonator_gui.png", 338, 166);
        this.remoteDetonatorContainer = aContainer;
        addGUIElements();
    }

    private void addGUIElements() {
        val scrollPanel = new GT_GuiScrollPanel<>(this, 0, 0, 0, 100, 100, 0.0, 98, 98);
        val scrollBar = new GT_GuiSlider(1, this, 0, 110, 50, 10, 0.0, 1.0, 0.0, -1);
        //
        scrollPanel.setOnUpdateBehavior((screen, element, mouseX, mouseY, clickType) -> scrollBar.setBarRadius(scrollPanel.renderWindowEffectiveHeight()));
        for (int i = 0; i < 10; i++) {
            val scroll = new RemoteDetonator_GuiEntry(scrollPanel);
            scrollPanel.addScrollableElement(scroll);
        }
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
