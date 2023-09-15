package gregtech.api.metatileentity.implementations.dev;


import com.google.common.io.ByteArrayDataInput;
import gregtech.api.enums.RSControlMode;
import gregtech.api.interfaces.IAdvancedGUIEntity;
import gregtech.api.interfaces.IRedstoneSensitive;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.IAdvancedTEData;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.dev.GT_Container_DevFluidSource;
import gregtech.common.gui.dev.GT_GUIContainer_DevFluidSource;
import io.netty.buffer.ByteBuf;
import lombok.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import javax.annotation.Nonnull;

import static gregtech.api.enums.Textures.BlockIcons.*;


@Getter
public class GT_MetaTileEntity_DevFluidSource extends GT_MetaTileEntity_BasicTank implements IAdvancedGUIEntity, IRedstoneSensitive {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GUIData implements IAdvancedTEData {

        @Builder.Default
        private RSControlMode mode = RSControlMode.IGNORE;

        @Builder.Default
        private boolean active = true;

        @Builder.Default
        private boolean perTick = true;

        @Builder.Default
        private boolean rsActive = true;

        @Builder.Default
        private int rPT = 1;

        @Builder.Default
        private int rPS = 20;

        /**
         * @return
         */
        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            final NBTTagCompound result = new NBTTagCompound();
            mode.saveNBTData(result);
            result.setBoolean("active", active);
            result.setBoolean("perTick", perTick);
            result.setInteger("rPT", rPT);
            result.setInteger("rPS", rPS);
            return result;
        }

        @Override
        public void writeToByteBuf(final ByteBuf aBuf) {
            aBuf.writeInt(mode.ordinal());
            aBuf.writeBoolean(active);
            aBuf.writeBoolean(perTick);
            aBuf.writeInt(rPT);
            aBuf.writeInt(rPS);
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
            setMode(RSControlMode.loadFromNBTData(aNBT));
            setActive(aNBT.getBoolean("active"));
            setPerTick(aNBT.getBoolean("perTick"));
            setRPT(aNBT.getInteger("rPT"));
            setRPS(aNBT.getInteger("rPS"));
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(final ByteArrayDataInput aBuf, final EntityPlayerMP aPlayer) {
            return new GUIData(
                    RSControlMode.getMode(aBuf.readInt()), aBuf.readBoolean(), aBuf.readBoolean(), aBuf.readBoolean(), aBuf.readInt(), aBuf.readInt());
        }

        /**
         * @param other
         */
        @Override
        public void readFrom(final ISerializableObject other) {
            GUIData toReadFrom = (GUIData) other;
            setActive(toReadFrom.active);
            setMode(toReadFrom.getMode());
            setPerTick(toReadFrom.perTick);
            setRPT(toReadFrom.rPT);
            setRPS(toReadFrom.rPS);
        }

        /**
         * Actually sends the changes
         *
         * @param container
         * @param crafter
         */
        @Override
        public void sendChange(final Container container, final ICrafting crafter) {
            crafter.sendProgressBarUpdate(container, 200, active ? 1 : 0);
            crafter.sendProgressBarUpdate(container, 201, mode.ordinal());
            crafter.sendProgressBarUpdate(container, 202, perTick ? 1 : 0);
            crafter.sendProgressBarUpdate(container, 203, rPT);
            crafter.sendProgressBarUpdate(container, 204, rPS);
        }

        /**
         * Receives changed data
         *
         * @param changeID
         * @param data
         */
        @Override
        public void receiveChange(final int changeID, final int data) {
            switch (changeID) {
                case 200: {
                    setActive(data != 0);
                    break;
                }
                case 201: {
                    setMode(RSControlMode.getMode(data));
                    break;
                }
                case 202: {
                    setPerTick(data != 0);
                    break;
                }
                case 203: {
                    setRPT(data);
                    break;
                }
                case 204: {
                    setRPS(data);
                    break;
                }
            }
        }

    }


    public static final int numSlots = 1;

    public static final int inputSlot = 0, outputSlot = 1;

    private final byte[] rsInputs = new byte[6];

    private final GUIData internalData = new GUIData();

    private int tPeriod = 1;

    private int tCount = 0;

    public GT_MetaTileEntity_DevFluidSource(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional, 15, numSlots, new String[]{
                "Someone spilled a bottle of coke and this happened"
        });
    }

    public GT_MetaTileEntity_DevFluidSource(final String aName, final String[] aDescription, final ITexture[][][] aTextures) {
        super(aName, 15, numSlots, aDescription, aTextures);
    }

    /**
     * Receive and accept the packet
     *
     * @param data
     */
    @Override
    public void receiveGuiData(final ISerializableObject data) {
        if (data instanceof GUIData) {
            internalData.readFrom(data);
            adjustFluidRate();
            processRS();
            markDirty();
        }
    }

    private void adjustFluidRate() {
        if (internalData.isPerTick()) {
            internalData.setRPS(internalData.getRPT() * 20);
            tPeriod = 1;
        } else {
            tPeriod = 20;
        }
    }

    @Override
    public boolean receiveRSClientUpdates() {
        return true;
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

    /**
     * @return
     */
    @Override
    public RSControlMode getRedstoneMode() {
        return internalData.mode;
    }

    /**
     * @return
     */
    @Override
    public byte[] getRSValues() {
        return rsInputs;
    }

    /**
     * @param newMode
     */
    @Override
    public void setMode(final RSControlMode newMode) {
        internalData.setMode(newMode);
        markDirty();
    }

    /**
     * @param side
     * @param rsSignal
     */
    @Override
    public void updateRSValues(final byte side, final byte rsSignal) {
        rsInputs[side] = rsSignal;
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

    /**
     * @param aTileEntity is just because the internal Variable "mBaseMetaTileEntity" is set after this Call.
     * @return a newly created and ready MetaTileEntity
     */
    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DevFluidSource(mName, mDescriptionArray, mTextures);
    }

    /**
     * Icon of the Texture. If this returns null then it falls back to getTextureIndex.
     *
     * @param aBaseMetaTileEntity
     * @param aSide               is the Side of the Block
     * @param aFacing             is the direction the Block is facing (or a Bitmask of all Connections in case of Pipes)
     * @param aColorIndex         The Minecraft Color the Block is having
     * @param aActive             if the Machine is currently active (use this instead of calling mBaseMetaTileEntity.mActive!!!). Note: In case of Pipes
     *                            this means if this Side
     *                            is connected to something or not.
     * @param aRedstone           if the Machine is currently outputting a RedstoneSignal (use this instead of calling mBaseMetaTileEntity.mRedstone!!!)
     */
    @Override
    public ITexture[] getTexture(
            final IGregTechTileEntity aBaseMetaTileEntity,
            final byte aSide,
            final byte aFacing,
            final byte aColorIndex,
            final boolean aActive,
            final boolean aRedstone
                                ) {
        boolean caresAboutRS = internalData.mode != RSControlMode.IGNORE;
        int rsBump = 2 * (caresAboutRS ? (internalData.rsActive ? 2 : 1) : 0);
        int facingBump = aSide == aFacing ? 0 : 1;
        return mTextures[facingBump + rsBump][aColorIndex + 1];
    }

    @Override
    public boolean isValidSlot(final int aIndex) {
        return aIndex != getStackDisplaySlot() && aIndex != inputSlot;
    }

    /**
     * ^= writeToNBT
     *
     * @param aNBT
     */
    @Override
    public void saveNBTData(final NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        final NBTTagCompound devNBT = (NBTTagCompound) internalData.saveDataToNBT();
        devNBT.setInteger("tick", tCount);
        devNBT.setInteger("period", tPeriod);
        aNBT.setTag("dev", devNBT);
    }

    public boolean hasFluid() {
        return mFluid != null;
    }

    /**
     * ^= readFromNBT
     *
     * @param aNBT
     */
    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        final NBTTagCompound devNBT = aNBT.getCompoundTag("dev");
        internalData.loadDataFromNBT(devNBT);
        tCount = devNBT.getInteger("tick");
        tPeriod = devNBT.getInteger("period");
    }

    @Override
    public boolean doesFillContainers() {
        return true;
    }

    @Override
    public boolean doesEmptyContainers() {
        return false;
    }

    @Override
    public boolean canTankBeFilled() {
        return false;
    }

    @Override
    public boolean canTankBeEmptied() {
        return getDrainableStack() != null;
    }

    @Override
    public boolean displaysItemStack() {
        return true;
    }

    @Override
    public boolean displaysStackSize() {
        return false;
    }

    @Override
    public int getStackDisplaySlot() {
        return inputSlot;
    }

    /**
     * @param aID
     * @param aPlayerInventory
     * @param aBaseMetaTileEntity
     * @return
     */
    @Override
    public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_DevFluidSource(aPlayerInventory, aBaseMetaTileEntity);
    }

    /**
     * @param aID
     * @param aPlayerInventory
     * @param aBaseMetaTileEntity
     * @return
     */
    @Override
    public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_DevFluidSource(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public void updateFluidDisplayItem() {
        super.updateFluidDisplayItem();
        mInventory[inputSlot] = GT_Utility.getFluidDisplayStack(getFluid(), false, true);
    }

    @Override
    public FluidStack getFluid() {
        if (!hasFluid()) {
            return null;
        }
        return mFluid;
    }

    /**
     * From new ISidedInventory
     *
     * @param aBaseMetaTileEntity
     * @param aIndex
     * @param aSide
     * @param aStack
     */
    @Override
    public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
        return false;
    }

    /**
     * From new ISidedInventory
     *
     * @param aBaseMetaTileEntity
     * @param aIndex
     * @param aSide
     * @param aStack
     */
    @Override
    public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
        return false;
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork()) {
            processSlots();
            if (hasFluid() && isExecutionTick()) {
                doFluidPush(aBaseMetaTileEntity);
            }
            tickForward();
            markDirty();
        }
    }

    private void processSlots() {

    }

    public boolean isExecutionTick() {
        return tCount == 0;
    }

    /**
     * @param aFluid
     * @return
     */
    @Override
    public FluidStack setDrainableStack(final FluidStack aFluid) {
        val result =  super.setDrainableStack(aFluid);
        markDirty();
        return result;
    }

    private void doFluidPush(final IGregTechTileEntity aBaseMetaTileEntity) {
        IFluidHandler atSide = aBaseMetaTileEntity.getITankContainerAtSide(aBaseMetaTileEntity.getFrontFacing());
        if (atSide != null) {
            byte front = aBaseMetaTileEntity.getFrontFacing(), back = aBaseMetaTileEntity.getBackFacing();
            FluidStack simDrained = aBaseMetaTileEntity.drain(ForgeDirection.getOrientation(front), getRate(), false);
            if (simDrained != null) {
                int simFilledAmount = atSide.fill(ForgeDirection.getOrientation(back), simDrained, false);
                if (simFilledAmount > 0) {
                    FluidStack drained = aBaseMetaTileEntity.drain(ForgeDirection.getOrientation(front), simFilledAmount, true);
                    atSide.fill(ForgeDirection.getOrientation(back), drained, true);
                }
            }
            mFluid.amount = Integer.MAX_VALUE;
        }
    }

    public int getRate() {
        if (internalData.isPerTick()) {
            return internalData.rPT;
        } else {
            return internalData.rPS;
        }
    }

    private void tickForward() {
        this.tCount += 1;
        adjustCount();
    }

    private void adjustCount() {
        if (this.tPeriod <= 0) {
            this.tPeriod = 1;
        }
        this.tCount = this.tCount % this.tPeriod;
    }

    /**
     * a Player rightclicks the Machine
     * Sneaky rightclicks are not getting passed to this!
     *
     * @param aBaseMetaTileEntity
     * @param aPlayer
     */
    @Override
    public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide() || !isAccessAllowed(aPlayer)) {
            return true;
        }
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    /**
     * @param aFacing
     * @return
     */
    @Override
    public boolean isFacingValid(final byte aFacing) {
        return true;
    }

    /**
     * @param aPlayer
     * @return
     */
    @Override
    public boolean isAccessAllowed(final EntityPlayer aPlayer) {
        return GT_Utility.validatePlayerCanDoFancyStuff(aPlayer, true);
    }

    /**
     * @return
     */
    @Override
    public boolean canDrop() {
        return false;
    }

    @Override
    public int getCapacity() {
        return Integer.MAX_VALUE;
    }

    /**
     * @param aSide
     * @param aFluid
     * @return
     */
    @Override
    public boolean canDrain(final ForgeDirection aSide, final Fluid aFluid) {
        return aSide.ordinal() == getBaseMetaTileEntity().getFrontFacing() && isExecutionTick();
    }

    /**
     * @param aSide    Orientation the fluid is drained to.
     * @param maxDrain Maximum amount of fluid to drain.
     * @param doDrain  If false, drain will only be simulated.
     * @return
     */
    @Override
    public FluidStack drain(final ForgeDirection aSide, final int maxDrain, final boolean doDrain) {
        if (aSide != ForgeDirection.getOrientation(getBaseMetaTileEntity().getFrontFacing())) {
            return null;
        }
        return super.drain(aSide, maxDrain, doDrain);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        getBaseMetaTileEntity().markDirty();
    }

    /**
     * Used Client Side to get a Texture Set for this Block. Called after setting the Tier and the Description so that those two are accessible.
     *
     * @param aTextures is the optional Array you can give to the Constructor.
     */
    @Override
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[6][17][];
        val pipe = TextureFactory.of(OVERLAY_PIPE_OUT);
        val fluidSource = TextureFactory.of(OVERLAY_DEV_FLUID_SOURCE);
        val rsInactive = TextureFactory.of(OVERLAY_RS_INACTIVE);
        val rsActive = TextureFactory.of(OVERLAY_RS_ACTIVE);
        for (int i = 0; i < rTextures[0].length; i++) {
            rTextures[0][i] = new ITexture[]{MACHINE_CASINGS[mTier][i], fluidSource, pipe};
            rTextures[1][i] = new ITexture[]{MACHINE_CASINGS[mTier][i], fluidSource};
            rTextures[2][i] = new ITexture[]{MACHINE_CASINGS[mTier][i], rsInactive, fluidSource, pipe};
            rTextures[3][i] = new ITexture[]{MACHINE_CASINGS[mTier][i], rsInactive, fluidSource};
            rTextures[4][i] = new ITexture[]{MACHINE_CASINGS[mTier][i], rsActive, fluidSource, pipe};
            rTextures[5][i] = new ITexture[]{MACHINE_CASINGS[mTier][i], rsActive, fluidSource};
        }
        return rTextures;
    }

}
