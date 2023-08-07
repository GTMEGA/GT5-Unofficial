package gregtech.api.metatileentity.implementations.dev;


import com.google.common.io.ByteArrayDataInput;
import gregtech.api.enums.RSControlMode;
import gregtech.api.interfaces.IAdvancedGUIEntity;
import gregtech.api.interfaces.IRedstoneSensitive;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.GT_Container_DevEnergySource;
import gregtech.common.gui.GT_GUIContainer_DevEnergySource;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.Textures.BlockIcons.*;


@Getter
public class GT_MetaTileEntity_DevEnergySource extends GT_MetaTileEntity_TieredMachineBlock implements IAdvancedGUIEntity, IRedstoneSensitive {

    @NoArgsConstructor
    @AllArgsConstructor
    public static class GUIData implements ISerializableObject {

        private int tier;

        private long voltage;

        private int amps;

        private RSControlMode mode;

        private boolean enabled;

        /**
         * @return A copy
         */
        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new GUIData(tier, voltage, amps, mode, enabled);
        }

        /**
         * @return Unused here
         */
        @Deprecated
        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            return new NBTTagCompound();
        }

        /**
         * Write data to given ByteBuf
         * The data saved this way is intended to be stored for short amount of time over network.
         * DO NOT store it to disks.
         *
         * @param aBuf Buffer to write into
         */
        @Override
        public void writeToByteBuf(final ByteBuf aBuf) {
            aBuf.writeInt(tier);
            aBuf.writeLong(voltage);
            aBuf.writeInt(amps);
            aBuf.writeInt(mode.ordinal());
            aBuf.writeBoolean(enabled);
        }

        /**
         * @param aNBT Unused
         */
        @Deprecated
        @Override
        public void loadDataFromNBT(final NBTBase aNBT) {

        }

        /**
         * Read data from given parameter and return this.
         * The data read this way is intended to be stored for short amount of time over network.
         *
         * @param aBuf    Buffer
         * @param aPlayer Player, unused
         */
        @Nonnull
        @Override
        public ISerializableObject readFromPacket(final ByteArrayDataInput aBuf, final EntityPlayerMP aPlayer) {
            return new GUIData(aBuf.readInt(), aBuf.readLong(), aBuf.readInt(), RSControlMode.getMode(aBuf.readInt()), aBuf.readBoolean());
        }

    }


    private final byte[] rsValues = {0, 0, 0, 0, 0, 0};

    private int energyTier = 0;

    private long voltage = 0;

    private int amperage = 0;

    protected void setEnabled(final boolean enabled) {
        this.enabled = enabled;
        markDirty();
    }

    private boolean enabled = true;

    private boolean rsEnabled = true;

    private RSControlMode rsMode = RSControlMode.IGNORE;

    public GT_MetaTileEntity_DevEnergySource(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 15, 0, new String[]{
                "Draws unlimited energy from quirks of the quantum foam", "You ARE supposed to have this, aren't you?"
        });
    }

    public GT_MetaTileEntity_DevEnergySource(String aName, String[] aDescriptionArray, ITexture[][][] aTextures) {
        super(aName, 16, 0, aDescriptionArray, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DevEnergySource(this.mName, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        NBTTagCompound devNBT = new NBTTagCompound();
        devNBT.setBoolean("enabled", this.enabled);
        devNBT.setLong("voltage", this.voltage);
        devNBT.setInteger("amps", this.amperage);
        devNBT.setInteger("rsMode", this.rsMode.ordinal());
        devNBT.setInteger("tier", this.energyTier);
        aNBT.setTag("dev", devNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        NBTTagCompound devNBT = aNBT.getCompoundTag("dev");
        this.enabled = devNBT.getBoolean("enabled");
        this.voltage = devNBT.getLong("voltage");
        this.amperage = devNBT.getInteger("amps");
        this.rsMode = RSControlMode.getMode(devNBT.getInteger("rsMode"));
        this.energyTier = devNBT.getInteger("tier");
        processRS();
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone
                                ) {
        final int color = aColorIndex + 1;
        return mTextures[aSide == aFacing ? 0 : 1][color];
    }

    @Override
    public boolean onWrenchRightClick(
            byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ
                                     ) {
        if (!isAccessAllowed(aPlayer)) {
            return false;
        }
        return super.onWrenchRightClick(aSide, aWrenchingSide, aPlayer, aX, aY, aZ);
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            setEUVar(getMinimumStoredEU() * (1 << 9));
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide() || !isAccessAllowed(aPlayer)) {
            return true;
        }
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public long maxEUStore() {
        return 2147483647L * (1 << 10);
    }

    @Override
    public long maxEUOutput() {
        return canRun() ? voltage : 0L;
    }

    /**
     * How many Amperes this Block can suck at max. Surpassing this value won't blow it up.
     */
    @Override
    public long maxAmperesIn() {
        return 0;
    }

    @Override
    public long maxAmperesOut() {
        return canRun() ? amperage : 0;
    }

    @Override
    public boolean isOutputFacing(final byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return aPlayer.capabilities.isCreativeMode;
    }

    @Override
    public long getMinimumStoredEU() {
        return 1L;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        getBaseMetaTileEntity().markDirty();
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_DevEnergySource(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_DevEnergySource(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public String[] getDescription() {
        final String flavor = String.format(
                "Generating %d amps of %s power (%d Eu/t)%s", amperage, tierName(), maxEUOutput() * amperage, canRun() ? "" : (enabled ? "[Disabled by Redstone]" : "[Disabled by User]"));
        return ArrayUtils.addAll(mDescriptionArray, flavor);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[2][17][];
        ITexture out = TextureFactory.of(OVERLAYS_ENERGY_OUT[mTier]);
        for (int i = 0; i < rTextures[0].length; i++) {
            rTextures[0][i] = new ITexture[]{MACHINE_CASINGS[mTier][i], out};
            rTextures[1][i] = new ITexture[]{MACHINE_CASINGS[mTier][i], TextureFactory.of(OVERLAY_DEV_ENERGY_SOURCE)};
        }
        return rTextures;
    }

    public String tierName() {
        return VN[energyTier];
    }

    /**
     * Receive and accept the packet
     *
     * @param data data to read from
     */
    @Override
    public void receiveGuiData(final ISerializableObject data) {
        if (data instanceof GUIData) {
            setEnergyTier(((GUIData) data).tier);
            setAmperage(((GUIData) data).amps);
            setVoltage(((GUIData) data).voltage);
            setMode(((GUIData) data).mode);
            setEnabled(((GUIData) data).enabled);
            processRS();
            markDirty();
        }
    }

    public void setEnergyTier(final int energyTier) {
        this.energyTier = energyTier;
        if (this.energyTier < 0) {
            this.energyTier = 0;
        }
        markDirty();
    }

    public void setAmperage(final int amperage) {
        this.amperage = amperage;
        if (this.amperage < 0) {
            this.amperage = 0;
        }
        markDirty();
    }

    public void setVoltage(final long voltage) {
        this.voltage = voltage;
        if (this.voltage < 0) {
            this.voltage = 0;
        }
        if (this.voltage > V[getEnergyTier()]) {
            this.voltage = V[getEnergyTier()];
        }
        this.markDirty();
    }

    /**
     * Decodes the packet, machine type specific
     *
     * @param aData Packet to decipher
     */
    @Override
    public ISerializableObject decodePacket(final ByteArrayDataInput aData) {
        return new GUIData().readFromPacket(aData, null);
    }

    /**
     * @return
     */
    @Override
    public RSControlMode getMode() {
        return rsMode;
    }

    /**
     * @return
     */
    @Override
    public byte[] getRSValues() {
        return rsValues;
    }

    /**
     * @param newMode
     */
    @Override
    public void setMode(final RSControlMode newMode) {
        if (isValidMode(newMode)) {
            this.rsMode = newMode;
        }
        markDirty();
    }

    /**
     * @param side
     * @param rsSignal
     */
    @Override
    public void updateRSValues(final byte side, final byte rsSignal) {
        rsValues[side] = rsSignal;
    }

    /**
     *
     */
    @Override
    public void processRS() {
        rsEnabled = getMode().checkPredicate(getMaxRSValue());
    }

    public boolean canRun() {
        return this.isEnabled() && this.isRsEnabled();
    }

}
