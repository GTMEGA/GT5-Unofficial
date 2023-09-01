package gregtech.api.gui.widgets;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class GT_GuiTooltip {

    public boolean enabled = true;

    protected Rectangle bounds;

    private List<String> text;

    public GT_GuiTooltip(Rectangle bounds, String... text) {
        this.bounds = bounds;
        setToolTipText(text);
    }

    public List<String> getToolTipText() {
        return text;
    }

    public void setToolTipText(String... text) {
        if (text != null) {
            this.text = new ArrayList<>(text.length);
            for (int i = 0; i < text.length; i++) {
                if (i == 0) {
                    this.text.add("\u00a7f" + text[i]);
                } else {
                    this.text.add("\u00a77" + text[i]);
                }
            }
        } else {
            this.text = new ArrayList<>();
        }
    }

    protected void updateText() {
    }

}
