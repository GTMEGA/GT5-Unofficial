package gregtech.api.gui.widgets.slider;


import gregtech.api.gui.GT_RichGuiContainer;
import gregtech.api.interfaces.IGuiScreen;
import lombok.val;


public class GT_GuiSlider_Vertical extends GT_GuiSlider {

    public static final double BASE_FUZZ_WIDTH = 0.5;

    public static final double BASE_FUZZ_HEIGHT = 0.3;

    public GT_GuiSlider_Vertical(final IGuiScreen gui, final int id, final int x, final int y, final int width, final int height, final double min, final double max, final double current, final int subdivisions) {
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
        val pseudoTop = y + height * barRad;
        val pseudoHeight = height * (1 - barRad);
        val sliderVal = (int) getValue();
        for (int i = 0; i < numSubdivisions; i++) {
            val pseudoPos = pseudoTop + pseudoHeight * i / numSubdivisions;
            val barPos = (int) pseudoPos;
            val colorToUse = i == sliderVal ? getSubDivisionActiveColor() : getSubDivisionInactiveColor();
            drawRect(x, barPos, x + width, barPos + 1, GT_RichGuiContainer.colorToARGB(colorToUse));
        }
    }

    /**
     * @param mouseX
     * @param mouseY
     * @param parTicks
     */
    @Override
    protected void drawInfo(final int mouseX, final int mouseY, final float parTicks) {

    }

    /**
     * @param color
     */
    @Override
    protected void drawSlideInternal(final int color) {
        drawRect(x, getBarTop(), x + width, getBarBottom(), color);
    }

    private int getBarTop() {
        return (int) (y + height * getPseudoTop());
    }

    private int getBarBottom() {
        return (int) (y + height * getPseudoBottom());
    }

    private double getPseudoTop() {
        return getPseudoPos() - barRadius();
    }

    private double getPseudoBottom() {
        return getPseudoPos() + barRadius();
    }

    /**
     * @param edgeColor
     * @param midColor
     */
    @Override
    protected void drawSliderBackground(final int edgeColor, final int midColor) {
        final int barTop;
        final int barBottom;
        if (isDragged) {
            barTop = getBarTop();
            barBottom = getBarBottom();
        } else {
            barTop = barBottom = getBarCenter();
        }
        drawBarLower(y, barTop, edgeColor, midColor);
        drawBarUpper(barBottom, y + height, midColor, edgeColor);
    }

    private int getBarCenter() {
        return (int) (y + height * getPseudoPos());
    }

    /**
     * @param mouseX
     * @param mouseY
     */
    @Override
    protected void handleMouse(final int mouseX, final int mouseY) {
        val barRad = barRadius();
        val pseudoTop = gui.getGuiTop() + y + height * barRad;
        val pseudoHeight = height * (1 - barRad);
        if (barRad < 1.0) {
            this.current = (mouseY - pseudoTop) / pseudoHeight;
        }
        updateSlider();
        this.isDragged = true;
    }

    /**
     * @param start
     * @param end
     * @param midColor
     * @param edgeColor
     */
    @Override
    protected void drawBarUpper(final int start, final int end, final int midColor, final int edgeColor) {
        drawGradientRect(x, start, x + width, end, midColor, edgeColor, true);
    }

    /**
     * @param start
     * @param end
     * @param edgeColor
     * @param midColor
     */
    @Override
    protected void drawBarLower(final int start, final int end, final int edgeColor, final int midColor) {
        drawGradientRect(x, start, x + width, end, edgeColor, midColor, true);
    }

}
