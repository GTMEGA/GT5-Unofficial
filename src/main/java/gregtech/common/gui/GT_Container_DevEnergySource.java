package gregtech.common.gui;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.dev.GT_MetaTileEntity_DevEnergySource;
import lombok.Getter;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;


@Getter
public class GT_Container_DevEnergySource extends GT_ContainerMetaTile_Machine {

    private int tier;

    private int amperage;

    private boolean enabled;

    public GT_Container_DevEnergySource(InventoryPlayer aPlayerInventory, IGregTechTileEntity aTileEntityInventory) {
        super(aPlayerInventory, aTileEntityInventory);
        if (aTileEntityInventory instanceof GT_MetaTileEntity_DevEnergySource) {
            tier = getEnergySource().getEnergyTier();
            amperage = getEnergySource().getAmperage();
            enabled = getEnergySource().isEnabled();
        }
    }

    public GT_MetaTileEntity_DevEnergySource getEnergySource() {
        return (GT_MetaTileEntity_DevEnergySource) this.mTileEntity.getMetaTileEntity();
    }

    @Override
    public boolean doesBindPlayerInventory() {
        return true;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) {
            return;
        }
        for (Object o : this.crafters) {
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

}
