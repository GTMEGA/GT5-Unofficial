package gregtech.api.gui.widgets;

import gregtech.api.gui.widgets.icon.GT_GuiIcon;
import gregtech.api.gui.widgets.icon.IGT_GuiIcon;
import gregtech.api.interfaces.IGuiScreen;
import lombok.Getter;


public class GT_GuiIconCheckButton extends GT_GuiIconButton {
    private final IGT_GuiIcon checkedIcon;
    private final IGT_GuiIcon normalIcon;
    private final String checkedTooltip;
    private final String normalTooltip;
    @Getter
    private boolean checked = false;

    public GT_GuiIconCheckButton(IGuiScreen gui, int id, int x, int y, IGT_GuiIcon checkedIcon, IGT_GuiIcon normalIcon) {
        this(gui, id, x, y, checkedIcon, normalIcon, null, null);
    }

    public GT_GuiIconCheckButton(IGuiScreen gui, int id, int x, int y, IGT_GuiIcon checkedIcon, IGT_GuiIcon normalIcon, String checkedTooltip, String normalTooltip) {
        super(gui, id, x, y, normalIcon);
        this.checkedIcon = checkedIcon;
        this.normalIcon = normalIcon;
        this.checkedTooltip = checkedTooltip;
        this.normalTooltip = normalTooltip;
    }

    public GT_GuiIconCheckButton setChecked(boolean checked) {
        super.setIcon(checked ? checkedIcon : normalIcon);
        super.setTooltipText(checked ? checkedTooltip : normalTooltip);
        this.checked = checked;
        return this;
    }

    public boolean toggle() {
        setChecked(!isChecked());
        return isChecked();
    }
}
