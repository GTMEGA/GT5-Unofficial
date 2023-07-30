package gregtech.common.gui;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.dev.GT_MetaTileEntity_DevEnergySource;
import gregtech.api.util.GT_Utility;
import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;


@Getter
public class GT_Container_DevEnergySource extends GT_ContainerMetaTile_Machine {

    private static final int VOLT_BUTTONS = GT_Values.V.length;

    private static final int AMP_BUTTONS = VOLT_BUTTONS + 8;

    private static final int MISC_BUTTONS = AMP_BUTTONS + 2;

    private int tier;

    private int amperage;

    private boolean enabled;

    private final boolean valid;

    public GT_Container_DevEnergySource(InventoryPlayer aPlayerInventory, IGregTechTileEntity aTileEntityInventory) {
        super(aPlayerInventory, aTileEntityInventory);
        if (aTileEntityInventory instanceof GT_MetaTileEntity_DevEnergySource) {
            tier = getEnergySource().getEnergyTier();
            amperage = getEnergySource().getAmperage();
            enabled = getEnergySource().isEnabled();
            valid = true;
        } else {
            valid = false;
        }
    }

    @Override
    public boolean doesBindPlayerInventory() {
        return false;
    }

    @Override
    public void addSlots(final InventoryPlayer aPlayerInventory) {
        for (int i = 0; i < GT_Values.V.length; i++) {
            final int x = slotX(i, 128, 8);
            final int y = slotY(i, 28, 8);
            addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 0, x, y, false, true, 1));
        }
        for (int i = 0; i < 8; i++) {
            final int x = slotX(i, 8, 4);
            final int y = slotY(i, 28, 4);
            addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 0, x, y, false, true, 1));
        }
        for (int i = 0; i < 2; i++) {
            final int x = slotX(i, 8, 1);
            final int y = slotY(i, 104, 1);
            addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 0, x, y, false, true, 1));
        }
    }

    public static int slotX(int index, int offset, int perColumn) {
        return offset + 16 * (index / perColumn);
    }

    public static int slotY(int index, int offset, int perColumn) {
        return offset + 16 * (index % perColumn);
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShiftHeld, EntityPlayer aPlayer) {
        if (aSlotIndex < 0) {
            return super.slotClick(aSlotIndex, aMouseclick, aShiftHeld, aPlayer);
        }
        mTileEntity.markDirty();
        if (aSlotIndex < VOLT_BUTTONS) {
            getEnergySource().setEnergyTier(aSlotIndex);
            GT_Utility.sendChatToPlayer(aPlayer, String.format("Changed voltage to %s", GT_Values.VN[aSlotIndex]));
            return null;
        } else if (aSlotIndex < AMP_BUTTONS) {
            aSlotIndex -= VOLT_BUTTONS;
            boolean inc = aSlotIndex >= 4;
            if (inc) {
                aSlotIndex -= 4;
            }
            if (aSlotIndex == 0) {
                int amt = inc ? 1 : -1;
                getEnergySource().bumpAmperage(amt);
                GT_Utility.sendChatToPlayer(aPlayer, String.format("Changed amperage by %d", amt));
                return null;
            } else if (aSlotIndex >= 1 && aSlotIndex < 4) {
                float fac = aSlotIndex + 1;
                fac = inc ? fac : 1 / fac;
                getEnergySource().scaleAmperage(fac);
                GT_Utility.sendChatToPlayer(aPlayer, String.format("Scaled amperage by %.2f", fac));
                return null;
            }
        } else if (aSlotIndex < MISC_BUTTONS) {
            aSlotIndex -= AMP_BUTTONS;
            if (aSlotIndex == 0) {
                getEnergySource().zeroOut();
                GT_Utility.sendChatToPlayer(aPlayer, "Set all values to 0");
                return null;
            } else if (aSlotIndex == 1) {
                GT_Utility.sendChatToPlayer(aPlayer, String.format("Source %s", getEnergySource().toggleEnabled() ? "Enabled" : "Disabled"));
                return null;
            }
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShiftHeld, aPlayer);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) {
            return;
        }
        for (Object o: this.crafters) {
            ICrafting crafter = (ICrafting) o;
            crafter.sendProgressBarUpdate(this, 200, getEnergySource().getEnergyTier());
            crafter.sendProgressBarUpdate(this, 201, getEnergySource().getAmperage());
            crafter.sendProgressBarUpdate(this, 202, getEnergySource().isEnabled() ? 1 : 0);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(final int par1, final int par2) {
        super.updateProgressBar(par1, par2);
        switch (par1) {
            case 200: {
                tier = par2;
                break;
            }
            case 201: {
                amperage = par2;
                break;
            }
            case 202: {
                enabled = par2 != 0;
                break;
            }
        }
    }

    public GT_MetaTileEntity_DevEnergySource getEnergySource() {
        return (GT_MetaTileEntity_DevEnergySource) this.mTileEntity.getMetaTileEntity();
    }

}
