package gregtech.common.items;


import com.google.common.io.ByteArrayDataInput;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.IPacketReceivableItem;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.smart_filter.GT_SmartFilter_Container;
import gregtech.common.gui.smart_filter.GT_SmartFilter_GUIContainer;
import gregtech.common.sf_logic.SF_Type;
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


    private static final IIcon[] icons = new IIcon[SF_Type.values().length];

    public static GT_SmartFilter_GUIContainer getClientGUIFromPlayerHand(final EntityPlayer aPlayer) {
        val currentItem = aPlayer.getCurrentEquippedItem();
        if (currentItem == null || !(currentItem.getItem() instanceof GT_Item_SmartFilter)) {
            return null;
        }
        return new GT_SmartFilter_GUIContainer(getServerGUIFromPlayerHand(aPlayer));
    }

    public static GT_SmartFilter_Container getServerGUIFromPlayerHand(final EntityPlayer player) {
        val currentItem = player.getCurrentEquippedItem();
        if (currentItem == null || !(currentItem.getItem() instanceof GT_Item_SmartFilter)) {
            return null;
        }
        return new GT_SmartFilter_Container(player, currentItem);
    }

    public GT_Item_SmartFilter() {
        super("smart_filter", "Smart Filter", "Allows for smart filtering of items in GT machines");
        setMaxDamage(0);
        setMaxStackSize(64);
        setHasSubtypes(true);
    }

    @Override
    public void registerIcons(final IIconRegister aIconRegister) {
        for (int i = 0; i < SF_Type.values().length; i++) {
            icons[i] = aIconRegister.registerIcon(GT_Values.MOD_ID + ":iconsets/smart_filter/" + SF_Type.values()[i].getUnlocalizedName());
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
            aList.add(SF_Type.INVALID.getTooltip());
            return;
        }
        aList.add(SF_Type.values()[meta].getTooltip());
    }

    @Override
    public void onCreated(final ItemStack aStack, final World aWorld, final EntityPlayer aPlayer) {
        validateAndSetNBT(aStack);
    }

    private ItemStack validateAndSetNBT(final ItemStack stack) {
        if (stack == null) {
            return null;
        }
        if (stack.stackTagCompound == null) {
            stack.stackTagCompound = new net.minecraft.nbt.NBTTagCompound();
        }
        val nbt = stack.stackTagCompound;
        return stack;
    }

    @Override
    public ItemStack onItemRightClick(final ItemStack stack, final World world, final EntityPlayer player) {
        itemUse(stack, player, world);
        return validateAndSetNBT(stack);
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
        if (!world.isRemote) {
            if (player.isSneaking()) {
                GT_Utility.sendChatToPlayer(player, "Opening GUI");
                player.openGui(GT_Values.GT, -3, world, (int) player.posX, (int) player.posY, (int) player.posZ);
            }
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
