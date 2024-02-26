package gregtech.api.gui.widgets;

import lombok.Getter;
import net.minecraft.client.gui.GuiButton;

@Getter
public class GT_GuiButtonNumber extends GuiButton {
    int value;

    public GT_GuiButtonNumber(int id,int value ,int x, int y, int with, int height, String name) {
        super(id, x, y, with, height, name);
        this.value = value;
    }

}
