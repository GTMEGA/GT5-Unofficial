package gregtech.api.gui.widgets;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;


public enum GT_GuiIcon {
    BUTTON_NORMAL(0, 0, 0),
    BUTTON_DOWN(0, 32, 0),
    BUTTON_HIGHLIGHT(0, 32 * 2, 0),
    BUTTON_HIGHLIGHT_DOWN(0, 32 * 3, 0),
    BUTTON_DISABLED(0, 32 * 4, 0),

    DISABLE(0, 0, 32),
    REDSTONE_OFF(0, 32, 32),
    REDSTONE_ON(0, 32 * 2, 32),
    CHECKMARK(0, 32 * 3, 32),
    CROSS(0, 32 * 4, 32),
    WHITELIST(0, 32 * 5, 32),
    BLACKLIST(0, 32 * 6, 32),
    PROGRESS(0, 32 * 7, 32),

    EXPORT(0, 0, 32 * 2),
    IMPORT(0, 32, 32 * 2),
    ALLOW_INPUT(0, 32 * 2, 32 * 2),
    BLOCK_INPUT(0, 32 * 3, 32 * 2),

    SLOT_DARKGRAY(1, 176, 0, 18, 18),
    SLOT_GRAY(1, 176, 18, 18, 18),

    // Tier Icons
    TIER_ULV(2, 0, 0, 16, 16),
    TIER_LV(2, 0, 16, 16, 16),
    TIER_MV(2, 0, 32, 16, 16),
    TIER_HV(2, 0, 48, 16, 16),
    TIER_EV(2, 16, 0, 16, 16),
    TIER_IV(2, 16, 16, 16, 16),
    TIER_LuV(2, 16, 32, 16, 16),
    TIER_ZPM(2, 16, 48, 16, 16),
    TIER_UV(2, 32, 0, 16, 16),
    TIER_UHV(2, 32, 16, 16, 16),
    TIER_UEV(2, 32, 32, 16, 16),
    TIER_UIV(2, 32, 48, 16, 16),
    TIER_UMV(2, 48, 0, 16, 16),
    TIER_UXV(2, 48, 16, 16, 16),
    TIER_OpV(2, 48, 32, 16, 16),
    TIER_MAX(2, 48, 48, 16, 16),

    // Math Symbols
    MATH_PLUS(3, 0, 0, 8, 8),
    MATH_MINUS(3, 0, 8, 8, 8),
    MATH_TIMES(3, 0, 16, 8, 8),
    MATH_DIVIDE(3, 0, 24, 8, 8),
    MATH_FRAC(3, 0, 32, 8, 8),
    MATH_DEC(3, 0, 40, 8, 8),
    // Numbers
    MATH_ZERO(3, 8, 0, 8, 8),
    MATH_ONE(3, 8, 8, 8, 8),
    MATH_TWO(3, 8, 16, 8, 8),
    MATH_THREE(3, 8, 24, 8, 8),
    MATH_FOUR(3, 8, 32, 8, 8),
    MATH_FIVE(3, 8, 40, 8, 8),
    MATH_SIX(3, 8, 48, 8, 8),
    MATH_SEVEN(3, 8, 56, 8, 8),
    MATH_EIGHT(3, 8, 64, 8, 8),
    MATH_NINE(3, 8, 72, 8, 8),

    INVALID();

    private static final int T_SIZE = 256;

    private static final ResourceLocation[] TEXTURES = {
            new ResourceLocation("gregtech", "textures/gui/GuiButtons.png"), new ResourceLocation("gregtech", "textures/gui/GuiCover.png"),
            new ResourceLocation("gregtech", "textures/gui/TierIcons.png"), new ResourceLocation("gregtech", "textures/gui/Math.png")
    };

    /**
     * @param width  width of INDIVIDUAL character
     * @param height height of INDIVIDUAL character
     */
    public static void renderInteger(
            int value, final double x, final double y, final double width, final double height, final double zLevel, final boolean doDraw, boolean showSign
                                    ) {
        // Just send her bud
        if (value == 0) {
            render(MATH_ZERO, x, y, width, height, zLevel, doDraw);
            return;
        }
        // Negative or positive
        final boolean negative = value < 0;
        showSign = showSign || negative;
        final GT_GuiIcon sign = negative ? MATH_MINUS : MATH_PLUS;
        // Get absolute value
        value = negative ? -value : value;
        final int signShift = showSign ? 1 : 0;
        // Draw
        if (showSign) {
            render(sign, x, y, width, height, zLevel, doDraw);
        }
        if (value < 10) {
            render(getDigit(value), x + width * signShift, y, width, height, zLevel, doDraw);
            return;
        }
        // Get number of digits
        int numDigits = 0;
        int temp = value;
        while (temp > 0) {
            temp /= 10;
            numDigits += 1;
        }
        for (int i = 0; i < numDigits; i++) {
            final int j = (numDigits - 1) - i;
            final int tens = (int) Math.pow(10, j);
            final int pre = (value / (tens * 10)) * (tens * 10);
            final int digit = (value - pre) / tens;
            render(getDigit(digit), x + width * (i + signShift), y, width, height, zLevel, doDraw);
        }
    }

    public static void renderFraction(
            int num, int den, double x, double y, final double width, final double height, final double zLevel, final boolean doDraw, boolean showSign
                                     ) {
        final boolean numNeg, denNeg;
        numNeg = num < 0;
        denNeg = den < 0;
        num = numNeg ? -num : num;
        den = denNeg ? -den : den;
        final boolean overallNeg = (numNeg || denNeg) && !(numNeg && denNeg);
        double newSizeX = width / 2;
        final double newSizeY = height / 2;
        final GT_GuiIcon sign = overallNeg ? MATH_MINUS : MATH_PLUS;
        if (overallNeg || showSign) {
            render(sign, x, y, newSizeX, newSizeY, zLevel, doDraw);
            x += newSizeX;
            newSizeX /= 2;
        }
        final double denX = x + newSizeX;
        final double denY = y + newSizeY;
        renderInteger(num, x, y, newSizeX, newSizeY, zLevel, doDraw, false);
        renderInteger(den, denX, denY, newSizeX, newSizeY, zLevel, doDraw, false);
        render(MATH_FRAC, x + newSizeX / 2, y + newSizeY / 2, newSizeX, newSizeY, zLevel, doDraw);
    }

    public static void render(GT_GuiIcon icon, double x, double y, double width, double height, double zLevel, boolean doDraw) {
        if (icon == INVALID) {
            return;
        }
        Tessellator tess = Tessellator.instance;
        if (doDraw) {
            Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURES[icon.texID]);
            tess.startDrawingQuads();
        }
        double minU = (double) icon.x / T_SIZE;
        double maxU = (double) (icon.x + icon.width) / T_SIZE;
        double minV = (double) icon.y / T_SIZE;
        double maxV = (double) (icon.y + icon.height) / T_SIZE;
        tess.addVertexWithUV(x, y + height, zLevel, minU, maxV);
        tess.addVertexWithUV(x + width, y + height, zLevel, maxU, maxV);
        tess.addVertexWithUV(x + width, y + 0, zLevel, maxU, minV);
        tess.addVertexWithUV(x, y + 0, zLevel, minU, minV);

        if (icon.overlay != null) {
            render(icon.overlay, x, y, width, height, zLevel, false);
        }

        if (doDraw) {
            tess.draw();
        }
    }

    public static GT_GuiIcon getDigit(int num) {
        final int base = MATH_ZERO.ordinal();
        if (num > 9) {
            return INVALID;
        }
        return values()[base + num];
    }

    public static GT_GuiIcon getTierIcon(int tier) {
        final int base = TIER_ULV.ordinal(), max = TIER_MAX.ordinal() - base;
        if (tier <= max) {
            return values()[base + tier];
        }
        return INVALID;
    }

    public final int x, y, width, height;

    public final GT_GuiIcon overlay;

    private final int texID;

    GT_GuiIcon() {
        this(0, 0, 0, 0, 0);
    }

    GT_GuiIcon(int texID, int x, int y, int width, int height) {
        this(texID, x, y, width, height, null);
    }

    GT_GuiIcon(int texID, int x, int y, int width, int height, GT_GuiIcon overlay) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.overlay = overlay;
        this.texID = texID;
    }

    GT_GuiIcon(int texID, int x, int y) {
        this(texID, x, y, 32, 32, null);
    }

}
