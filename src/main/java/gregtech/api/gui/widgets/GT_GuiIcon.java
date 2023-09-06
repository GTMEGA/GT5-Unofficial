package gregtech.api.gui.widgets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum GT_GuiIcon {
    BUTTON_NORMAL           (Atlas.BUTTONS, 0,     0),
    BUTTON_DOWN             (Atlas.BUTTONS, 32,    0),
    BUTTON_HIGHLIGHT        (Atlas.BUTTONS, 32*2,  0),
    BUTTON_HIGHLIGHT_DOWN   (Atlas.BUTTONS, 32*3,  0),
    BUTTON_DISABLED         (Atlas.BUTTONS, 32*4,  0),

    DISABLE                 (Atlas.BUTTONS, 0,     32),
    REDSTONE_OFF            (Atlas.BUTTONS, 32,    32),
    REDSTONE_ON             (Atlas.BUTTONS, 32*2,  32),
    CHECKMARK               (Atlas.BUTTONS, 32*3,  32),
    CROSS                   (Atlas.BUTTONS, 32*4,  32),
    WHITELIST               (Atlas.BUTTONS, 32*5,  32),
    BLACKLIST               (Atlas.BUTTONS, 32*6,  32),
    PROGRESS                (Atlas.BUTTONS, 32*7,  32),

    EXPORT                  (Atlas.BUTTONS, 0,     32*2),
    IMPORT                  (Atlas.BUTTONS, 32,    32*2),
    ALLOW_INPUT             (Atlas.BUTTONS, 32*2,  32*2),
    BLOCK_INPUT             (Atlas.BUTTONS, 32*3,  32*2),

    SLOT_DARKGRAY           (Atlas.COVERS, 176,0,18,18),
    SLOT_GRAY               (Atlas.COVERS, 176,18,18,18),

    // Tier Icons
    TIER_ULV(Atlas.TIERS, 0, 0, 16, 16),
    TIER_LV(Atlas.TIERS, 0, 16, 16, 16),
    TIER_MV(Atlas.TIERS, 0, 32, 16, 16),
    TIER_HV(Atlas.TIERS, 0, 48, 16, 16),
    TIER_EV(Atlas.TIERS, 16, 0, 16, 16),
    TIER_IV(Atlas.TIERS, 16, 16, 16, 16),
    TIER_LuV(Atlas.TIERS, 16, 32, 16, 16),
    TIER_ZPM(Atlas.TIERS, 16, 48, 16, 16),
    TIER_UV(Atlas.TIERS, 32, 0, 16, 16),
    TIER_UHV(Atlas.TIERS, 32, 16, 16, 16),
    TIER_UEV(Atlas.TIERS, 32, 32, 16, 16),
    TIER_UIV(Atlas.TIERS, 32, 48, 16, 16),
    TIER_UMV(Atlas.TIERS, 48, 0, 16, 16),
    TIER_UXV(Atlas.TIERS, 48, 16, 16, 16),
    TIER_OpV(Atlas.TIERS, 48, 32, 16, 16),
    TIER_MAX(Atlas.TIERS, 48, 48, 16, 16),

    // Math Symbols
    MATH_PLUS(Atlas.MATH, 0, 0, 8, 8),
    MATH_MINUS(Atlas.MATH, 0, 8, 8, 8),
    MATH_TIMES(Atlas.MATH, 0, 16, 8, 8),
    MATH_DIVIDE(Atlas.MATH, 0, 24, 8, 8),
    MATH_FRAC(Atlas.MATH, 0, 32, 8, 8),
    MATH_DEC(Atlas.MATH, 0, 40, 8, 8),
    // Numbers
    MATH_ZERO(Atlas.MATH, 8, 0, 8, 8),
    MATH_ONE(Atlas.MATH, 8, 8, 8, 8),
    MATH_TWO(Atlas.MATH, 8, 16, 8, 8),
    MATH_THREE(Atlas.MATH, 8, 24, 8, 8),
    MATH_FOUR(Atlas.MATH, 8, 32, 8, 8),
    MATH_FIVE(Atlas.MATH, 8, 40, 8, 8),
    MATH_SIX(Atlas.MATH, 8, 48, 8, 8),
    MATH_SEVEN(Atlas.MATH, 8, 56, 8, 8),
    MATH_EIGHT(Atlas.MATH, 8, 64, 8, 8),
    MATH_NINE(Atlas.MATH, 8, 72, 8, 8),

    INVALID();

    private static final int T_SIZE = 256;

    @AllArgsConstructor
    @Getter
    public enum Atlas {
        BUTTONS("textures/gui/GuiButtons.png"),
        COVERS("textures/gui/GuiCover.png"),
        TIERS("textures/gui/TierIcons.png"),
        MATH("textures/gui/Math.png");

        public final String modID, resourcePath;

        public final int atlasWidth, atlasHeight;

        public final Map<Integer, ResourceLocation> resourceLocations = new HashMap<>();

        static {
            Arrays.stream(values()).forEach(atlas -> atlas.resourceLocations.put(atlas.ordinal(), new ResourceLocation(atlas.modID, atlas.resourcePath)));
        }

        Atlas(String resourcePath) {
            this("gregtech", resourcePath, 256, 256);
        }

        public ResourceLocation getResourceLocation() {
            return resourceLocations.get(ordinal());
        }

    }

    public final int x, y, width, height;
    public final GT_GuiIcon overlay;
    private final Atlas atlas;

    GT_GuiIcon() {
        this(Atlas.BUTTONS, 0, 0, 0, 0);
    }

    GT_GuiIcon(Atlas atlas, int x, int y, int width, int height, GT_GuiIcon overlay) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.overlay = overlay;
        this.atlas = atlas;
    }

    GT_GuiIcon(Atlas atlas, int x, int y) {
        this(atlas, x, y,32,32,null);
    }
    GT_GuiIcon(Atlas atlas, int x, int y, int width, int height) {
        this(atlas, x, y, width, height,null);
    }

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

    public static void renderFraction(int num, int den, double x, double y, final double width,
                                      final double height, final double zLevel, final boolean doDraw, boolean showSign) {
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
            Minecraft.getMinecraft().renderEngine.bindTexture(icon.atlas.getResourceLocation());
            tess.startDrawingQuads();
        }
        double minU = (double) icon.x / icon.atlas.atlasWidth;
        double maxU = (double) (icon.x + icon.width) / icon.atlas.atlasWidth;
        double minV = (double) icon.y / icon.atlas.atlasHeight;
        double maxV = (double) (icon.y + icon.height) / icon.atlas.atlasHeight;
        tess.addVertexWithUV(x, y + height, zLevel, minU, maxV);
        tess.addVertexWithUV(x + width, y + height, zLevel, maxU, maxV);
        tess.addVertexWithUV(x + width, y + 0, zLevel, maxU, minV);
        tess.addVertexWithUV(x, y + 0, zLevel, minU, minV);

        if (icon.overlay != null)
            render(icon.overlay, x, y, width, height, zLevel, false);

        if (doDraw)
            tess.draw();
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

}
