package gregtech.api.metatileentity.implementations.dev;


import com.google.common.io.ByteArrayDataInput;
import gregtech.api.interfaces.IAdvancedGUIEntity;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.net.GT_Packet_TileEntityGUI;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.GT_Container_DevEnergySource;
import gregtech.common.gui.GT_GUIContainer_DevEnergySource;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.minecraft.client.entity.EntityClientPlayerMP;
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
public class GT_MetaTileEntity_DevEnergySource extends GT_MetaTileEntity_TieredMachineBlock implements IAdvancedGUIEntity {

    public static class GUIData implements ISerializableObject {

        private int tier;

        private int amps;

        private boolean enabled;

        public GUIData() {

        }

        public GUIData(final GT_MetaTileEntity_DevEnergySource source) {
            this(source.energyTier, source.amperage, source.enabled);
        }

        public GUIData(final int tier, final int amps, final boolean enabled) {
            this.tier = tier;
            this.amps = amps;
            this.enabled = enabled;
        }

        /**
         * @return
         */
        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new GUIData(tier, amps, enabled);
        }

        /**
         * @return
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
         * @param aBuf
         */
        @Override
        public void writeToByteBuf(final ByteBuf aBuf) {
            aBuf.writeInt(tier);
            aBuf.writeInt(amps);
            aBuf.writeBoolean(enabled);
        }

        /**
         * @param aNBT
         */
        @Deprecated
        @Override
        public void loadDataFromNBT(final NBTBase aNBT) {

        }

        /**
         * Read data from given parameter and return this.
         * The data read this way is intended to be stored for short amount of time over network.
         *
         * @param aBuf
         * @param aPlayer
         */
        @Nonnull
        @Override
        public ISerializableObject readFromPacket(final ByteArrayDataInput aBuf, final EntityPlayerMP aPlayer) {
            return new GUIData(aBuf.readInt(), aBuf.readInt(), aBuf.readBoolean());
        }

    }


    private int energyTier = 0;

    private int amperage = 0;

    private boolean enabled = true;

    public GT_MetaTileEntity_DevEnergySource(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 15, 0, new String[]{
                "Draws unlimited energy from quirks of the quantum foam", "You ARE supposed to have this, aren't you?"
        });
    }

    public GT_MetaTileEntity_DevEnergySource(String aName, String[] aDescriptionArray, ITexture[][][] aTextures) {
        super(aName, 16, 0, aDescriptionArray, aTextures);
    }

    public void zeroOut() {
        setEnergyTier(0);
        setAmperage(0);
        markDirty();
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

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DevEnergySource(this.mName, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        NBTTagCompound devNBT = new NBTTagCompound();
        devNBT.setBoolean("enabled", this.enabled);
        devNBT.setInteger("amps", this.amperage);
        devNBT.setInteger("tier", this.energyTier);
        aNBT.setTag("dev", devNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        NBTTagCompound devNBT = aNBT.getCompoundTag("dev");
        this.enabled = devNBT.getBoolean("enabled");
        this.amperage = devNBT.getInteger("amps");
        this.energyTier = devNBT.getInteger("tier");
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
        return enabled ? maxEUTheoretical() : 0L;
    }

    @Override
    public long maxAmperesOut() {
        return enabled ? amperage : 0;
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

    public long maxEUTheoretical() {
        return V[energyTier];
    }

    @Override
    public String[] getDescription() {
        final String flavor =
                String.format("Generating %d amps of %s power (%d Eu/t)%s", amperage, tierName(), maxEUTheoretical() * amperage, enabled ? "" : " [Disabled]");
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

    public void bumpAmperage(int amount) {
        this.amperage += amount;
        if (this.amperage < 0) {
            this.amperage = 0;
        }
        markDirty();
    }

    public boolean toggleEnabled() {
        this.enabled = !this.enabled;
        markDirty();
        return this.enabled;
    }

    public void scaleAmperage(final float factor) {
        this.amperage = (int) (this.amperage * factor);
        if (this.amperage < 0) {
            this.amperage = 0;
        }
        markDirty();
    }


    /**
     * Get the packet representing the current state of the TE
     */
    @Override
    public ISerializableObject getData() {
        return new GUIData(this);
    }

    /**
     * Receive and accept the packet
     *
     * @param data
     * @param player
     */
    @Override
    public void receiveGuiData(final ISerializableObject data, final EntityClientPlayerMP player) {
        if (data instanceof GUIData) {
            setEnergyTier(((GUIData) data).tier);
            setAmperage(((GUIData) data).amps);
            this.enabled = ((GUIData) data).enabled;
            markDirty();
        }
    }

    /**
     * Decodes the packet, machine type specific
     *
     * @param aData
     */
    @Override
    public ISerializableObject decodePacket(final ByteArrayDataInput aData) {
        return new GUIData().readFromPacket(aData, null);
    }

}
