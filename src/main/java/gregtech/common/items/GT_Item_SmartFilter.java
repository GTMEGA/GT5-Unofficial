package gregtech.common.items;


import com.google.common.io.ByteArrayDataInput;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.IPacketReceivableItem;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.util.ISerializableObject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;


public class GT_Item_SmartFilter extends GT_Generic_Item implements IPacketReceivableItem {


    @RequiredArgsConstructor
    public enum SF_Type {
        ITEM("sf_item", "Smart Item Filter", "Filters for specific items"),
        ORE_DICT("sf_oredict", "Smart Ore Dictionary Filter", "Filters for items with specific ore dictionary entries"),
        MOD("sf_mod", "Smart Mod Filter", "Filters for items from specific mods"),
        MATERIAL("sf_material", "Smart Material Filter", "Filters for items with specific materials"),
        PART_TYPE("sf_part", "Smart Part Type Filter", "Filters for items of specific part types"),
        RECIPE("sf_recipe", "Smart Recipe Filter", "Filters for items that are used in specific machines"),
        COMBINATION("sf_combo", "Smart Combination Filter", "Combination of other filters"),
        INVALID("sf_invalid", "Invalid Filter", "Invalid Filter");

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
    }


    private static final IIcon[] icons = new IIcon[SF_Type.values().length];

    public GT_Item_SmartFilter() {
        super("smart_filter", "Smart Filter", "Allows for smart filtering of items in GT machines");
        setMaxDamage(0);
        setMaxStackSize(64);
        setHasSubtypes(true);
    }

    @Override
    public void registerIcons(final IIconRegister aIconRegister) {
        for (int i = 0; i < SF_Type.values().length; i++) {
            icons[i] = aIconRegister.registerIcon(GT_Values.MOD_ID + ":iconsets/smart_filter/" + SF_Type.values()[i].unlocalizedName);
        }
    }

    @Override
    public IIcon getIconFromDamage(final int index) {
        if (index < 0 || index >= SF_Type.values().length) {
            return icons[SF_Type.INVALID.ordinal()];
        }
        return icons[index];
    }

    @SuppressWarnings(
            {
                    "unchecked",
                    "rawtypes"
            }
    )
    @Override
    protected void addAdditionalToolTips(final List aList, final ItemStack aStack, final EntityPlayer aPlayer) {
        val meta = aStack.getItemDamage();
        if (meta < 0 || meta >= SF_Type.values().length) {
            aList.add(SF_Type.INVALID.tooltip);
            return;
        }
        aList.add(SF_Type.values()[meta].tooltip);
    }

    @Override
    public void onCreated(final ItemStack aStack, final World aWorld, final EntityPlayer aPlayer) {
        setNBT(aStack);
    }

    private ItemStack setNBT(final ItemStack stack) {
        return stack;
    }

    @Override
    public ItemStack onItemRightClick(final ItemStack stack, final World world, final EntityPlayer player) {
        itemUse(stack, player, world);
        return setNBT(stack);
    }

    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        return SF_Type.getDisplayNameFromStack(stack);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getSubItems(final Item item, final CreativeTabs tabs, final List list) {
        for (int i = 0; i < SF_Type.INVALID.ordinal(); i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    private void itemUse(final ItemStack stack, final EntityPlayer player, final World world) {
        if (world.isRemote) {
        }
    }

    @Override
    public void onPacketReceived(final World world, final EntityPlayer player, final ItemStack stack, final ISerializableObject data) {

    }

    @Nullable
    @Override
    public ISerializableObject readFromBytes(final ByteArrayDataInput aData) {
        return null;
    }

}
