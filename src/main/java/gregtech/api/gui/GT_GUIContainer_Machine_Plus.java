package gregtech.api.gui;


import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;


public abstract class GT_GUIContainer_Machine_Plus extends GT_GUIContainer_Plus {

    public final GT_ContainerMetaTile_Machine mContainer;

    public GT_GUIContainer_Machine_Plus(final GT_ContainerMetaTile_Machine aContainer, String aGUIBackground) {
        super(aContainer, aGUIBackground);
        this.mContainer = aContainer;
    }

    public GT_GUIContainer_Machine_Plus(final InventoryPlayer player, final IGregTechTileEntity entity, final String aGUIbackground) {
        this(new GT_ContainerMetaTile_Machine(player, entity), aGUIbackground);
    }

}
