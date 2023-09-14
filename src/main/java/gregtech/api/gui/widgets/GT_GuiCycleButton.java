package gregtech.api.gui.widgets;


import gregtech.api.gui.widgets.icon.IGT_GuiIcon;
import gregtech.api.interfaces.IGuiScreen;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


public class GT_GuiCycleButton extends GT_GuiIconButton {

    @Getter
    @RequiredArgsConstructor
    public static class IconToolTipPair {

        private final IGT_GuiIcon icon;

        private final String tooltip;

    }


    private final IconToolTipPair[] iconToolTipPairs;

    @Getter
    private int state;

    @Setter
    private boolean doCycle = true;

    public GT_GuiCycleButton(final IGuiScreen gui, final int id, final int x, final int y, final int state, final IconToolTipPair[] iconToolTipPairs) {
        super(gui, id, x, y, null);
        this.iconToolTipPairs = iconToolTipPairs;
        this.setState(state);
    }

    public GT_GuiCycleButton setState(final int newState) {
        this.state = newState % iconToolTipPairs.length;
        if (this.state < 0) {
            this.state += iconToolTipPairs.length;
        }
        final IconToolTipPair pair = getPair(this.state);
        this.setIcon(pair.getIcon());
        this.setTooltipText(pair.tooltip, String.format("Next: %s", getPair(this.state + 1).tooltip));
        return this;
    }

    public IconToolTipPair getPair(int state) {
        state = state % iconToolTipPairs.length;
        if (state < 0) {
            state += iconToolTipPairs.length;
        }
        return iconToolTipPairs[state];
    }

    /**
     * @param screen
     * @param mouseX
     * @param mouseY
     * @param clickType
     */
    @Override
    public void onClick(final IGuiScreen screen, final int mouseX, final int mouseY, final int clickType) {
        if (doCycle) {
            cycle();
        }
        super.onClick(screen, mouseX, mouseY, clickType);
    }

    public GT_GuiCycleButton cycle() {
        return this.setState(getState() + 1);
    }

}
