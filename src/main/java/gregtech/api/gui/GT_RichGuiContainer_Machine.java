package gregtech.api.gui;


public abstract class GT_RichGuiContainer_Machine extends GT_RichGuiContainer {

    public final GT_ContainerMetaTile_Machine mContainer;

    public GT_RichGuiContainer_Machine(final GT_ContainerMetaTile_Machine aContainer, final String aGUIBackground, int width, int height) {
        super(aContainer, aGUIBackground, width, height);
        this.mContainer = aContainer;
    }

    public abstract void sendUpdateToServer();

}
