package gregtech.common.sf_logic;


import gregtech.common.items.GT_Item_SmartFilter;
import gregtech.common.sf_logic.filter_data.SF_ItemData;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;


@Getter
@RequiredArgsConstructor
public enum SF_Type {
    ITEM("sf_item", "Smart Item Filter", "Filters for specific items", new ISF_Logic<SF_ItemData>() {
        @Override
        public boolean doesStackMatch(final SF_ItemData filterData, final ItemStack stack) {
            return true;
        }
    }),
    ORE_DICT("sf_oredict", "Smart Ore Dictionary Filter", "Filters for items with specific ore dictionary entries"),
    MOD("sf_mod", "Smart Mod Filter", "Filters for items from specific mods"),
    MATERIAL("sf_material", "Smart Material Filter", "Filters for items with specific materials"),
    PART_TYPE("sf_part", "Smart Part Type Filter", "Filters for items of specific part types"),
    RECIPE("sf_recipe", "Smart Recipe Filter", "Filters for items that are used in specific machines"),
    COMBINATION("sf_combo", "Smart Combination Filter", "Combination of other filters"),
    INVALID("sf_invalid", "Invalid Filter", "Invalid Filter");

    SF_Type(final @NonNull String unlocalizedName, final @NonNull String displayName, final @NonNull String tooltip) {
        this(unlocalizedName, displayName, tooltip, null);
    }

    public static String getDisplayNameFromStack(final ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return INVALID.displayName;
        }
        val meta = stack.getItemDamage();
        if (meta < 0 || meta >= SF_Type.values().length) {
            return INVALID.displayName;
        }
        return SF_Type.values()[meta].displayName;
    }

    private final @NonNull String unlocalizedName, displayName, tooltip;

    private final @Nullable ISF_Logic<? extends ISF_Data<?>> logic;

}
