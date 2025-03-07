package gregtech.common.gui.dev;


import gregtech.api.enums.RSControlMode;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.dev.GT_MetaTileEntity_DevEnergySource;
import lombok.Getter;
import net.minecraft.entity.player.InventoryPlayer;


@Getter
public class GT_Container_DevEnergySource extends GT_Container_Dev<GT_MetaTileEntity_DevEnergySource, GT_MetaTileEntity_DevEnergySource.GUIData> {

    boolean receiveServerData = true;

    public GT_Container_DevEnergySource(InventoryPlayer aPlayerInventory, IGregTechTileEntity aTileEntityInventory) {
        super(aPlayerInventory, aTileEntityInventory, GT_MetaTileEntity_DevEnergySource.class, GT_MetaTileEntity_DevEnergySource.GUIData.class);
    }

    @Override
    public boolean doesBindPlayerInventory() {
        return false;
    }

    public String getDisabledStatus() {
        if (!data.isEnabled()) {
            return "Disabled by User";
        }
        if (!data.isRsActive()) {
            return "Disabled by Redstone";
        }
        return "";
    }

    /**
     * @return
     */
    @Override
    public int baseWidth() {
        return 0;
    }

    /**
     * @return
     */
    @Override
    public int getDWSWidthBump() {
        return 0;
    }

    protected void setMode(final RSControlMode mode) {
        data.setMode(mode);
        detectAndSendChanges();
    }

    protected void setEnergyTier(final int energyTier) {
        data.setTier(energyTier);
        data.setVoltage(8L * (2L << (2 * energyTier)));
        detectAndSendChanges();
    }

    protected boolean toggleEnabled() {
        data.setEnabled(!data.isEnabled());
        detectAndSendChanges();
        return data.isEnabled();
    }

    protected void zeroOut() {
        setAmperage(0);
        setVoltage(0);
        detectAndSendChanges();
    }

    protected void setAmperage(final int amperage) {
        data.setAmps(amperage);
        detectAndSendChanges();
    }

    protected void setVoltage(final long voltage) {
        data.setVoltage(voltage);
        detectAndSendChanges();
    }

}
