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
import gregtech.api.util.IAdvancedTEData;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.dev.GT_Container_DevEnergySource;
import gregtech.common.gui.dev.GT_GUIContainer_DevEnergySource;
import gregtech.common.render.GT_Renderer_Block;
import io.netty.buffer.ByteBuf;
import lombok.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.Textures.BlockIcons.*;


@Getter
public class GT_MetaTileEntity_DevEnergySource extends GT_MetaTileEntity_TieredMachineBlock implements IAdvancedGUIEntity, IRedstoneSensitive {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class GUIData implements IAdvancedTEData {

        @Builder.Default
        private int tier = 0;

        @Builder.Default
        private long voltage = 0;

        @Builder.Default
        private int amps = 0;

        @Builder.Default
        private RSControlMode mode = RSControlMode.IGNORE;

        @Builder.Default
        private boolean enabled = true;

        @Builder.Default
        private boolean rsActive = true;

        public boolean canRun() {
            return enabled && rsActive;
        }

        /**
         * @return
         */
        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            final NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger("tier", getTier());
            compound.setLong("voltage", getVoltage());
            compound.setInteger("amps", getAmps());
            compound.setBoolean("enabled", isEnabled());
            mode.saveNBTData(compound);
            return compound;
        }

        /**
         * Write data to given ByteBuf The data saved this way is intended to be stored for short amount of time over
         * network. DO NOT store it to disks.
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
         * @param oNBT
         */
        @Override
        public void loadDataFromNBT(final NBTBase oNBT) {
            if (!(oNBT instanceof NBTTagCompound)) {
                return;
            }
            NBTTagCompound aNBT = (NBTTagCompound) oNBT;
            setTier(aNBT.getInteger("tier"));
            setVoltage(aNBT.getLong("voltage"));
            setAmps(aNBT.getInteger("amps"));
            setEnabled(aNBT.getBoolean("enabled"));
            setMode(RSControlMode.loadFromNBTData(aNBT));
        }

        public void setTier(final int tier) {
            this.tier = tier;
            if (this.tier < 0) {
                this.tier = 0;
            }
        }

        public void setVoltage(final long voltage) {
            this.voltage = voltage;
            if (this.voltage < 0) {
                this.voltage = 0;
            }
            if (this.voltage > V[getTier()]) {
                this.voltage = V[getTier()];
            }
        }

        public void setAmps(final int amps) {
            this.amps = amps;
            if (this.amps < 0) {
                this.amps = 0;
            }
        }

        /**
         * Read data from given parameter and return this. The data read this way is intended to be stored for short
         * amount of time over network.
         *
         * @param aBuf    Buffer
         * @param aPlayer Player, unused
         */
        @Nonnull
        @Override
        public ISerializableObject readFromPacket(final ByteArrayDataInput aBuf, final EntityPlayerMP aPlayer) {
            return new GUIData(aBuf.readInt(), aBuf.readLong(), aBuf.readInt(), RSControlMode.getMode(aBuf.readInt()), aBuf.readBoolean(), aBuf.readBoolean());
        }

        /**
         * @param other
         */
        @Override
        public void readFrom(final ISerializableObject other) {
            GUIData toReadFrom = (GUIData) other;
            setEnabled(toReadFrom.enabled);
            setAmps(toReadFrom.amps);
            setTier(toReadFrom.tier);
            setVoltage(toReadFrom.voltage);
            setMode(toReadFrom.mode);
        }

        @Override
        public void sendChange(final Container source, final ICrafting crafter) {
            crafter.sendProgressBarUpdate(source, 200, getTier());
            crafter.sendProgressBarUpdate(source, 201, getAmps());
            crafter.sendProgressBarUpdate(source, 202, isEnabled() ? 1 : 0);
            crafter.sendProgressBarUpdate(source, 203, (int) (getVoltage()));
            crafter.sendProgressBarUpdate(source, 204, (int) ((getVoltage() & 0xFFFFFFFF00000000L) >> 32));
            crafter.sendProgressBarUpdate(source, 205, getMode().ordinal());
            crafter.sendProgressBarUpdate(source, 206, isRsActive() ? 1 : 0);
        }

        @Override
        public void receiveChange(final int changeID, final int data) {
            switch (changeID) {
                case 200: {
                    setTier(data);
                    break;
                }
                case 201: {
                    setAmps(data);
                    break;
                }
                case 202: {
                    setEnabled(data != 0);
                    break;
                }
                case 203: {
                    setVoltage(((getVoltage() & 0xFFFFFFFF00000000L) | data));
                    break;
                }
                case 204: {
                    setVoltage((getVoltage() & 0xFFFFFFFFL) | (long) data << 32);
                    break;
                }
                case 205: {
                    setMode(RSControlMode.getMode(data));
                    break;
                }
                case 206: {
                    setRsActive(data != 0);
                    break;
                }
            }
        }

    }


    private final byte[] rsValues = {0, 0, 0, 0, 0, 0};

    private GUIData internalData = new GUIData();

    /* private int energyTier = 0;

    private long voltage = 0;

    private int amperage = 0;

    private boolean enabled = true;

    private RSControlMode rsMode = RSControlMode.IGNORE; */


    public GT_MetaTileEntity_DevEnergySource(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 15, 0, new String[]{
                "Draws unlimited energy from quirks of the quantum foam", "You ARE supposed to have this, aren't you?"
        });
    }

    public GT_MetaTileEntity_DevEnergySource(String aName, String[] aDescriptionArray, ITexture[][][] aTextures) {
        super(aName, 15, 0, aDescriptionArray, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DevEnergySource(this.mName, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        /*
        NBTTagCompound devNBT = new NBTTagCompound();
        devNBT.setBoolean("enabled", isEnabled());
        devNBT.setLong("voltage", getVoltage());
        devNBT.setInteger("amps", getAmperage());
        getRedstoneMode().saveNBTData(devNBT);
        devNBT.setInteger("tier", getEnergyTier());
        aNBT.setTag("dev", devNBT);
        */
        aNBT.setTag("dev", internalData.saveDataToNBT());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        internalData = new GUIData();
        internalData.loadDataFromNBT(aNBT.getCompoundTag("dev"));
        /*
        NBTTagCompound devNBT = aNBT.getCompoundTag("dev");
        this.enabled = devNBT.getBoolean("enabled");
        this.voltage = devNBT.getLong("voltage");
        this.amperage = devNBT.getInteger("amps");
        this.rsMode = RSControlMode.loadFromNBTData(devNBT);
        this.energyTier = devNBT.getInteger("tier");
        */
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
        boolean caresAboutRS = internalData.mode != RSControlMode.IGNORE;
        int rsBump = 2 * (caresAboutRS ? (internalData.rsActive ? 2 : 1) : 0);
        int facingBump = aSide == aFacing ? 0 : 1;
        return mTextures[facingBump + rsBump][aColorIndex + 1];
    }

    /**
     * @return
     */
    @Override
    public RSControlMode getRedstoneMode() {
        return internalData.getMode();
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
            internalData.setMode(newMode);
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
        val te = getBaseMetaTileEntity();
        internalData.setRsActive(getRedstoneMode().checkPredicate(getMaxRSValue()));
        te.getWorld().scheduleBlockUpdate(te.getXCoord(), te.getYCoord(), te.getZCoord(), te.getBlockOffset(0, 0, 0), 3);
        getBaseMetaTileEntity().issueClientUpdate();
    }

    @Override
    public boolean receiveRSClientUpdates() {
        return true;
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
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            setEUVar(internalData.voltage * Math.max(2, internalData.amps) * (1 << 4));
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
        return canRun() ? internalData.getVoltage() : 0L;
    }

    @Override
    public long maxAmperesOut() {
        return canRun() ? internalData.getAmps() : 0;
    }

    /**
     * How many Amperes this Block can suck at max. Surpassing this value won't blow it up.
     */
    @Override
    public long maxAmperesIn() {
        return 0;
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
        return GT_Utility.validatePlayerCanDoFancyStuff(aPlayer, true);
    }

    @Override
    public long getMinimumStoredEU() {
        return 1L;
    }

    /**
     * @return
     */
    @Override
    public boolean canDrop() {
        return false;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        getBaseMetaTileEntity().markDirty();
    }

    @Override
    public boolean renderInWorld(final IBlockAccess aWorld, final int aX, final int aY, final int aZ, final Block aBlock, final RenderBlocks aRenderer) {
        val te = getBaseMetaTileEntity();
        byte facing = te.getFrontFacing();
        for (byte side = 0; side < 6; side++) {
            val tex = getTexture(te, side, facing, te.getColorization(), te.isActive(), true);
            GT_Renderer_Block.renderFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tex, true, side);
        }
        return true;
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_DevEnergySource(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_DevEnergySource(aPlayerInventory, aBaseMetaTileEntity);
    }

    public boolean canRun() {
        return internalData.canRun();
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[6][17][];
        val pipe = TextureFactory.of(OVERLAYS_ENERGY_OUT);
        val energySource = TextureFactory.of(OVERLAY_DEV_ENERGY_SOURCE);
        val rsInactive = TextureFactory.of(OVERLAY_RS_INACTIVE);
        val rsActive = TextureFactory.of(OVERLAY_RS_ACTIVE);
        for (int i = 0; i < rTextures[0].length; i++) {
            rTextures[0][i] = new ITexture[]{MACHINE_CASINGS[mTier][i], energySource, pipe};
            rTextures[1][i] = new ITexture[]{MACHINE_CASINGS[mTier][i], energySource};
            rTextures[2][i] = new ITexture[]{MACHINE_CASINGS[mTier][i], rsInactive, energySource, pipe};
            rTextures[3][i] = new ITexture[]{MACHINE_CASINGS[mTier][i], rsInactive, energySource};
            rTextures[4][i] = new ITexture[]{MACHINE_CASINGS[mTier][i], rsActive, energySource, pipe};
            rTextures[5][i] = new ITexture[]{MACHINE_CASINGS[mTier][i], rsActive, energySource};
        }
        return rTextures;
    }

    /**
     * Receive and accept the packet
     *
     * @param data data to read from
     */
    @Override
    public void receiveGuiData(final ISerializableObject data) {
        if (data instanceof GUIData) {
            internalData.readFrom(data);
            processRS();
            markDirty();
        }
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
     * Dumps the relevant data from the TE
     */
    @Override
    public ISerializableObject getTEGUIData() {
        return internalData;
    }

    @Override
    public void onNeighborChange(final int x, final int y, final int z) {
        processRS();
    }

}
