package gregtech.common.items;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import lombok.val;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class GT_TreeTap_Electric extends GT_TreeTap implements IElectricItem {
    private static final int COST_PER_OPERATION = 250;

    public GT_TreeTap_Electric() {
        super("treetapElectric",
              "Electric Treetap",
              "Got resin on tap",
              true);

        this.setNoRepair();
        this.setMaxDamage(-1);
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float xOffset, float yOffset, float zOffset) {
        if (!ElectricItem.manager.canUse(itemstack, COST_PER_OPERATION)) {
            return false;
        }

        val wasUsed = super.onItemUse(itemstack, entityPlayer, world, x, y, z, side, xOffset, yOffset, zOffset);

        if (wasUsed) {
            ElectricItem.manager.use(itemstack, COST_PER_OPERATION, entityPlayer);
        }

        return wasUsed;
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Override
    public Item getChargedItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public Item getEmptyItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return 100;
    }

    @Override
    public int getTier(ItemStack aStack) {
        return 1;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1 - ElectricItem.manager.getCharge(stack) / this.getMaxCharge(stack);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    protected void addAdditionalToolTips(List aList, ItemStack aStack, EntityPlayer aPlayer) {
        val charge = ElectricItem.manager.getCharge(aStack);
        val maxCharge = this.getMaxCharge(aStack);
        val percentCharged = charge / maxCharge;

        var color = EnumChatFormatting.GREEN;

        if (percentCharged < 0.1) {
            color = EnumChatFormatting.RED;
        } else if (percentCharged < 0.5) {
            color = EnumChatFormatting.YELLOW;
        }

        val tooltip = String.format("Charge: %s%.0f%%", color, 100 * percentCharged);

        aList.add(tooltip);
    }
}
