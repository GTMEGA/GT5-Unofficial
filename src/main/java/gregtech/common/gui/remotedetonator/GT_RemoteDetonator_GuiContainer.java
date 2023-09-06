package gregtech.common.gui.remotedetonator;

import gregtech.api.gui.GT_GUIContainer_Plus;
import lombok.Getter;

@Getter
public class GT_RemoteDetonator_GuiContainer extends GT_GUIContainer_Plus {

    private final GT_RemoteDetonator_Container remoteDetonatorContainer;

    public GT_RemoteDetonator_GuiContainer(final GT_RemoteDetonator_Container aContainer) {
        super(aContainer, "textures/gui/remote_detonator_gui.png", 338, 166);
        this.remoteDetonatorContainer = aContainer;
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
