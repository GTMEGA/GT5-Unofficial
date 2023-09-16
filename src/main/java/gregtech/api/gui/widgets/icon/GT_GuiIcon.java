package gregtech.api.gui.widgets.icon;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Getter
public enum GT_GuiIcon implements IGT_GuiIcon {
    BUTTON_NORMAL(Atlas.BUTTONS, 0, 0),
    BUTTON_DOWN(Atlas.BUTTONS, 32, 0),
    BUTTON_HIGHLIGHT(Atlas.BUTTONS, 32 * 2, 0),
    BUTTON_HIGHLIGHT_DOWN(Atlas.BUTTONS, 32 * 3, 0),
    BUTTON_DISABLED(Atlas.BUTTONS, 32 * 4, 0),
    //
    DISABLE(Atlas.BUTTONS, 0, 32),
    REDSTONE_OFF(Atlas.BUTTONS, 32, 32),
    REDSTONE_ON(Atlas.BUTTONS, 32 * 2, 32),
    CHECKMARK(Atlas.BUTTONS, 32 * 3, 32),
    CROSS(Atlas.BUTTONS, 32 * 4, 32),
    WHITELIST(Atlas.BUTTONS, 32 * 5, 32),
    BLACKLIST(Atlas.BUTTONS, 32 * 6, 32),
    PROGRESS(Atlas.BUTTONS, 32 * 7, 32),
    //
    EXPORT(Atlas.BUTTONS, 0, 32 * 2),
    IMPORT(Atlas.BUTTONS, 32, 32 * 2),
    ALLOW_INPUT(Atlas.BUTTONS, 32 * 2, 32 * 2),
    BLOCK_INPUT(Atlas.BUTTONS, 32 * 3, 32 * 2),
    //
    BUTTON_ERROR_NORMAL(Atlas.BUTTONS, 0, 32 * 3),
    BUTTON_ERROR_DOWN(Atlas.BUTTONS, 32, 32 * 3),
    BUTTON_ERROR_HIGHLIGHT(Atlas.BUTTONS, 32 * 2, 32 * 3),
    BUTTON_ERROR_HIGHLIGHT_DOWN(Atlas.BUTTONS, 32 * 3, 32 * 3),

    SLOT_DARKGRAY(Atlas.COVERS, 176, 0, 18, 18),
    SLOT_GRAY(Atlas.COVERS, 176, 18, 18, 18),

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

    // Mega Buttons
    MEGA_EXPLOSION(Atlas.MEGA_BUTTONS, 0, 0, 32, 32),

    INVALID();


    @AllArgsConstructor
    @Getter
    public enum Atlas {
        BUTTONS("textures/gui/GuiButtons.png"),
        MEGA_BUTTONS("textures/gui/GuiMegaButtons.png"),
        COVERS("textures/gui/GuiCover.png"),
        TIERS("textures/gui/TierIcons.png"),
        MATH("textures/gui/Math.png");

        static {
            Arrays.stream(values()).forEach(atlas -> atlas.resourceLocations.put(atlas.ordinal(), new ResourceLocation(atlas.modID, atlas.resourcePath)));
        }

        public final String modID, resourcePath;

        public final int atlasWidth, atlasHeight;

        public final Map<Integer, ResourceLocation> resourceLocations = new HashMap<>();

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

    GT_GuiIcon(Atlas atlas, int x, int y, int width, int height) {
        this(atlas, x, y, width, height, null);
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
        this(atlas, x, y, 32, 32, null);
    }

    @Override
    public double getMinU() {
        return x / (double) atlas.atlasWidth;
    }

    @Override
    public double getMinV() {
        return y / (double) atlas.atlasHeight;
    }

    @Override
    public double getMaxU() {
        return (x + width) / (double) atlas.atlasWidth;
    }

    @Override
    public double getMaxV() {
        return (y + height) / (double) atlas.atlasHeight;
    }

    @Override
    public @NonNull ResourceLocation getResourceLocation() {
        return atlas.getResourceLocation();
    }
}
