package gregtech.api.gui;


import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;


public abstract class GT_GUIContainer_Machine_Plus extends GT_GUIContainer_Plus {

    public final GT_ContainerMetaTile_Machine mContainer;

    public GT_GUIContainer_Machine_Plus(final GT_ContainerMetaTile_Machine aContainer, final String aGUIBackground, int width, int height) {
        super(aContainer, aGUIBackground, width, height);
        this.mContainer = aContainer;
    }

    public abstract void sendUpdateToServer();

}
