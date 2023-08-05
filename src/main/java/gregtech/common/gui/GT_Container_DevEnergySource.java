package gregtech.common.gui;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.RSControlMode;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.dev.GT_MetaTileEntity_DevEnergySource;
import gregtech.api.net.GT_Packet_TileEntityGUI;
import lombok.Getter;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;


@Getter
public class GT_Container_DevEnergySource extends GT_ContainerMetaTile_Machine {

    private final boolean serverSide;

    private int energyTier;

    private long voltage;

    private int amperage;

    private RSControlMode mode;

    private boolean enabled;

    private boolean rsEnabled;

    public GT_Container_DevEnergySource(InventoryPlayer aPlayerInventory, IGregTechTileEntity aTileEntityInventory) {
        super(aPlayerInventory, aTileEntityInventory);
        if (aTileEntityInventory instanceof GT_MetaTileEntity_DevEnergySource) {
            energyTier = getEnergySource().getEnergyTier();
            voltage = getEnergySource().getVoltage();
            mode = getEnergySource().getMode();
            amperage = getEnergySource().getAmperage();
            enabled = getEnergySource().isEnabled();
        }
        serverSide = aTileEntityInventory.isServerSide();
        detectAndSendChanges();
    }

    public void sendPacket() {
        final int dimension = this.mPlayerInventory.player.dimension;
        final GT_MetaTileEntity_DevEnergySource.GUIData data = new GT_MetaTileEntity_DevEnergySource.GUIData(energyTier, voltage, amperage, mode, enabled);
        GT_Values.NW.sendToServer(GT_Packet_TileEntityGUI.createFromMachine(getEnergySource(), data, dimension));
    }

    @Override
    public boolean doesBindPlayerInventory() {
        return false;
    }

    protected void setMode(final RSControlMode mode) {
        this.mode = mode;
        detectAndSendChanges();
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
            crafter.sendProgressBarUpdate(this, 203, (int) (getEnergySource().getVoltage()));
            crafter.sendProgressBarUpdate(this, 204, (int) ((getEnergySource().getVoltage() & 0xFFFFFFFF00000000L) >> 32));
            crafter.sendProgressBarUpdate(this, 205, getEnergySource().getRsMode().ordinal());
            crafter.sendProgressBarUpdate(this, 206, getEnergySource().isRsEnabled() ? 1 : 0);
        }
    }

    public GT_MetaTileEntity_DevEnergySource getEnergySource() {
        return (GT_MetaTileEntity_DevEnergySource) this.mTileEntity.getMetaTileEntity();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(final int par1, final int par2) {
        super.updateProgressBar(par1, par2);
        switch (par1) {
            case 200: {
                energyTier = par2;
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
            case 203: {
                voltage = ((voltage & 0xFFFFFFFF00000000L) | par2);
                break;
            }
            case 204: {
                voltage = ((voltage & 0xFFFFFFFFL) | (long) par2 << 32);
                break;
            }
            case 205: {
                mode = RSControlMode.getMode(par2);
                break;
            }
            case 206: {
                rsEnabled = par2 != 0;
                break;
            }
        }
    }

    protected void setEnergyTier(final int energyTier) {
        this.energyTier = energyTier;
        this.setVoltage(Math.min(GT_Values.V[this.energyTier], getVoltage()));
        detectAndSendChanges();
    }

    protected void setVoltage(final long voltage) {
        this.voltage = voltage;
        detectAndSendChanges();
    }

    protected void setAmperage(final int amperage) {
        this.amperage = amperage;
        detectAndSendChanges();
    }

    protected boolean toggleEnabled() {
        this.enabled = !this.enabled;
        detectAndSendChanges();
        return this.enabled;
    }

    protected void zeroOut() {
        setAmperage(0);
        setVoltage(0);
        detectAndSendChanges();
    }

    public boolean isActive() {
        return enabled && rsEnabled;
    }

    public String getDisabledStatus() {
        if (!isEnabled()) {
            return "Disabled by User";
        }
        if (!isRsEnabled()) {
            return "Disabled by Redstone";
        }
        return "";
    }

}
