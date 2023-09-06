package gregtech.common.gui.meganet;


import gregtech.api.gui.GT_GUIContainer_Plus;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import lombok.Getter;
import lombok.val;


@Getter
public class GT_MEGAnet_GuiContainer extends GT_GUIContainer_Plus {

    private final GT_MEGAnet_Container meganetContainer;

    public GT_MEGAnet_GuiContainer(final GT_MEGAnet_Container aContainer) {
        super(aContainer, "gregtech:textures/gui/MEGAnet_GUI.png", 338, 166);
        this.meganetContainer = aContainer;
        addGUIElements();
    }

    private void addGUIElements() {
        val check = new GT_GuiIconCheckButton(this, 0, 256, 9, GT_GuiIcon.CHECKMARK, GT_GuiIcon.CROSS);
        check.setChecked(meganetContainer.getMEGAnetActive());
    }

    /**
     * True by default specifically so that anyone implementing this wrong gets a nice fat purple and black checkerboard
     */
    @Override
    public boolean hasDWSAlternativeBackground() {
        return false;
    }

    @Override
    public void drawExtras(final int mouseX, final int mouseY, final float parTicks) {

    }

    @Override
    protected boolean autoDrawSlots() {
        return true;
    }

}
