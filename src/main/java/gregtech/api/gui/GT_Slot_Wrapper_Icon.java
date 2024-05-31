package gregtech.api.gui;

import gregtech.api.gui.widgets.icon.IGT_GuiIcon;
import lombok.Getter;
import net.minecraft.inventory.Slot;
import org.lwjgl.opengl.GL11;

@Getter
public class GT_Slot_Wrapper_Icon extends GT_Slot_Wrapper {

    private final IGT_GuiIcon bgIcon, hlIcon;

    public GT_Slot_Wrapper_Icon(final Slot wrapped, final IGT_GuiIcon bgIcon, final IGT_GuiIcon fgIcon) {
        super(wrapped);
        this.bgIcon = bgIcon;
        this.hlIcon = fgIcon;
    }

    public void renderBackgroundIcon() {
        if (bgIcon == null) {
            return;
        }
//        System.out.println("Coordinates bg: " + xDisplayPosition + ", " + yDisplayPosition);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        bgIcon.render(xDisplayPosition-1, yDisplayPosition-1, 18, 18, 0, true);
        GL11.glPopMatrix();
    }

    public void renderHighlightIcon() {
        if (hlIcon == null) {
            return;
        }
//        System.out.println("Coordinates hl: " + xDisplayPosition + ", " + yDisplayPosition);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        hlIcon.render(xDisplayPosition, yDisplayPosition, 16, 16, 0, true);
        GL11.glPopMatrix();
    }

}
