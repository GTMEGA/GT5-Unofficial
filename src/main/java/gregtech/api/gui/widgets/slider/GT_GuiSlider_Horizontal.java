package gregtech.api.gui.widgets.slider;


import gregtech.api.gui.GT_RichGuiContainer;
import gregtech.api.interfaces.IGuiScreen;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.awt.*;


@Setter
@Getter
public class GT_GuiSlider_Horizontal extends GT_GuiSlider {

    public static final double BASE_FUZZ_WIDTH = 0.3, BASE_FUZZ_HEIGHT = 0.5;


    public GT_GuiSlider_Horizontal(
            final IGuiScreen gui, final int id, final int x, final int y, final int width, final int height, double min, double max, final double current, final int subdivisions
                                  ) {
        super(gui, id, x, y, width, height, min, max, current, subdivisions);
        setSliderWidthFuzzy(BASE_FUZZ_WIDTH);
        setSliderHeightFuzzy(BASE_FUZZ_HEIGHT);
    }

    /**
     *
     */
    @Override
    protected void drawSliderSubdivisions() {
        val numSubdivisions = getSubdivisions();
        val barRad = barRadius();
        val pseudoLeft = x + width * barRad;
        val pseudoWidth = width * (1 - barRad);
        val sliderVal = (int) getValue();
        for (int i = 0; i < numSubdivisions; i++) {
            val pseudoPos = pseudoLeft + pseudoWidth * i / numSubdivisions;
            val barPos = (int) pseudoPos;
            val colorToUse = i == sliderVal ? getSubDivisionActiveColor() : getSubDivisionInactiveColor();
            drawRect(barPos, y, barPos + 1, y + height, GT_RichGuiContainer.colorToARGB(colorToUse));
        }
    }

    @Override
    protected void drawInfo(final int mouseX, final int mouseY, final float parTicks) {

    }

    @Override
    protected void drawSlideInternal(final int color) {
        drawRect(getBarLeft(), y, getBarRight(), y + height, color);
    }

    @Override
    protected void drawSliderBackground(final int edgeColor, final int midColor) {
        final int barLeft;
        final int barRight;
        if (isDragged) {
            barLeft = getBarLeft();
            barRight = getBarRight();
        } else {
            barLeft = barRight = getBarCenter();
        }
        drawBarLower(x, barLeft, edgeColor, midColor);
        drawBarUpper(barRight, x + width, midColor, edgeColor);
    }

    @Override
    protected void handleMouse(final int mouseX, final int mouseY) {
        val barRad = barRadius();
        val pseudoLeft = gui.getGuiLeft() + x + width * barRad;
        val pseudoWidth = width * (1 - barRad);
        if (barRad < 1.0) {
            this.current = (mouseX - pseudoLeft) / pseudoWidth;
        }
        updateSlider();
        this.isDragged = true;
    }

    @Override
    protected void drawBarUpper(final int start, final int end, final int midColor, final int edgeColor) {
        drawGradientRect(start, y, end, y + height, midColor, edgeColor, false);
    }

    @Override
    protected void drawBarLower(final int start, final int end, final int edgeColor, final int midColor) {
        drawGradientRect(start, y, end, y + height, edgeColor, midColor, false);
    }

    private int getBarCenter() {
        return (int) (x + width * getPseudoPos());
    }

    private int getBarLeft() {
        return (int) (x + width * getPseudoLeft());
    }

    private int getBarRight() {
        return (int) (x + width * getPseudoRight());
    }

    private double getPseudoLeft() {
        return getPseudoPos() - barRadius();
    }

    private double getPseudoRight() {
        return getPseudoPos() + barRadius();
    }

}
