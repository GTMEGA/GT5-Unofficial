package gregtech.common.gui.smart_filter;


import gregtech.api.gui.GT_RichGuiContainer;


public class GT_SmartFilter_GUIContainer extends GT_RichGuiContainer {

    private final GT_SmartFilter_Container smartFilterContainer;

    public GT_SmartFilter_GUIContainer(final GT_SmartFilter_Container serverGUIFromPlayerHand) {
        super(serverGUIFromPlayerHand, "gregtech:textures/gui/SmartFilter_GUI.png", 338, 166);
        this.smartFilterContainer = serverGUIFromPlayerHand;
        addGUIElements();
    }

    private void addGUIElements() {

    }

    @Override
    public void drawExtras(final int mouseX, final int mouseY, final float parTicks) {

    }

}
