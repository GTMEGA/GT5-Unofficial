package gregtech.common.gui.meganet;

import gregtech.api.gui.GT_GUIContainer_Plus;
import lombok.Getter;

@Getter
public class GT_MEGAnet_GuiContainer extends GT_GUIContainer_Plus {

    private final GT_MEGAnet_Container meganetContainer;

    public GT_MEGAnet_GuiContainer(final GT_MEGAnet_Container aContainer) {
        super(aContainer, "gregtech:textures/gui/MEGAnet_GUI.png", 256, 166);
        this.meganetContainer = aContainer;
    }

    @Override
    public void drawExtras(final int mouseX, final int mouseY, final float parTicks) {

    }

    @Override
    public int getDWSWidthBump() {
        return 82;
    }

}
