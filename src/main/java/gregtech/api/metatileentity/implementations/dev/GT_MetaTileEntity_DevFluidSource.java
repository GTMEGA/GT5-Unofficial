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
import gregtech.api.util.INonNBTSerializable;
import gregtech.api.util.ISerializableObject;
import io.netty.buffer.ByteBuf;
import lombok.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import javax.annotation.Nonnull;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;


@Getter
public class GT_MetaTileEntity_DevFluidSource extends GT_MetaTileEntity_BasicTank implements IAdvancedGUIEntity, IRedstoneSensitive {

    public static final int numSlots = 3;

    public static final int inputSlot = 0, outputSlot = 1, displaySlot = 2;

    private final byte[] rsInputs = new byte[6];

    private RSControlMode mode = RSControlMode.IGNORE;

    private boolean rsActive = true;

    private boolean active = true;

    private boolean perTick = true;

    private int ratePerTick = 1;

    private int ratePerSecond = 20;

    private String fluidName = null;

    private int tPeriod = 1;

    private int tCount = 0;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GUIData implements INonNBTSerializable {

        private RSControlMode mode;

        private boolean active, perTick;

        private int rPT, rPS;

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new GUIData(mode, active, perTick, rPT, rPS);
        }

        @Override
        public void writeToByteBuf(final ByteBuf aBuf) {
            aBuf.writeInt(mode.ordinal());
            aBuf.writeBoolean(active);
            aBuf.writeBoolean(perTick);
            aBuf.writeInt(rPT);
            aBuf.writeInt(rPS);
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(final ByteArrayDataInput aBuf, final EntityPlayerMP aPlayer) {
            return new GUIData(RSControlMode.getMode(aBuf.readInt()), aBuf.readBoolean(), aBuf.readBoolean(), aBuf.readInt(), aBuf.readInt());
        }

    }

    public GT_MetaTileEntity_DevFluidSource(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional, 15, numSlots, new String[]{
                "Someone spilled a bottle of coke and this happened"
        });
    }

    public GT_MetaTileEntity_DevFluidSource(final String aName, final String[] aDescription, final ITexture[][][] aTextures) {
        super(aName, 15, numSlots, aDescription, aTextures);
    }

    public boolean hasFluid() {
        return fluidName != null;
    }

    /**
     * Receive and accept the packet
     *
     * @param data
     */
    @Override
    public void receiveGuiData(final ISerializableObject data) {

    }

    /**
     * Decodes the packet, machine type specific
     *
     * @param aData
     */
    @Override
    public ISerializableObject decodePacket(final ByteArrayDataInput aData) {
        return null;
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
        mode = newMode;
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
        this.rsActive = mode.checkPredicate(getMaxRSValue());
    }

    /**
     * @param aTileEntity
     *         is just because the internal Variable "mBaseMetaTileEntity" is set after this Call.
     *
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
     * @param aSide
     *         is the Side of the Block
     * @param aFacing
     *         is the direction the Block is facing (or a Bitmask of all Connections in case of Pipes)
     * @param aColorIndex
     *         The Minecraft Color the Block is having
     * @param aActive
     *         if the Machine is currently active (use this instead of calling mBaseMetaTileEntity.mActive!!!). Note: In case of Pipes this means if this Side
     *         is connected to something or not.
     * @param aRedstone
     *         if the Machine is currently outputting a RedstoneSignal (use this instead of calling mBaseMetaTileEntity.mRedstone!!!)
     */
    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex,
                                 final boolean aActive, final boolean aRedstone) {
        return mTextures[aSide == aFacing ? 0 : 1][aColorIndex + 1];
    }

    @Override
    public boolean isValidSlot(final int aIndex) {
        return aIndex != getStackDisplaySlot() && aIndex != displaySlot;
    }

    /**
     * ^= writeToNBT
     *
     * @param aNBT
     */
    @Override
    public void saveNBTData(final NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        final NBTTagCompound devNBT = new NBTTagCompound();
        if (hasFluid()) {
            devNBT.setString("fName", fluidName);
        }
        devNBT.setBoolean("mode", perTick);
        devNBT.setInteger("perTick", ratePerTick);
        devNBT.setInteger("perSecond", ratePerSecond);
        devNBT.setInteger("tick", tCount);
        devNBT.setInteger("period", tPeriod);
        mode.saveNBTData(devNBT);
        aNBT.setTag("dev", devNBT);
    }

    /**
     * ^= readFromNBT
     *
     * @param aNBT
     */
    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        final NBTTagCompound devNBT = aNBT.getCompoundTag("dev");
        if (devNBT.hasKey("item")) {
            mInventory[2] = ItemStack.loadItemStackFromNBT(devNBT.getCompoundTag("item"));
        }
        active = devNBT.getBoolean("active");
        ratePerTick = devNBT.getInteger("perTick");
        ratePerSecond = devNBT.getInteger("perSecond");
        perTick = devNBT.getBoolean("mode");
        tCount = devNBT.getInteger("tick");
        tPeriod = devNBT.getInteger("period");
        mode = RSControlMode.loadFromNBTData(devNBT);
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
        return false;
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
        return displaySlot;
    }

    @Override
    public void updateFluidDisplayItem() {
        super.updateFluidDisplayItem();
        mInventory[displaySlot] = GT_Utility.getFluidDisplayStack(getFluid(), false, true);
    }

    @Override
    public FluidStack getFluid() {
        if (!hasFluid()) {
            return null;
        }
        return FluidRegistry.getFluidStack(fluidName, Integer.MAX_VALUE);
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

    private void doFluidPush(final IGregTechTileEntity aBaseMetaTileEntity) {
        IFluidHandler atSide = aBaseMetaTileEntity.getITankContainerAtSide(aBaseMetaTileEntity.getFrontFacing());
        if (atSide != null) {
            byte front = aBaseMetaTileEntity.getFrontFacing(), back = aBaseMetaTileEntity.getBackFacing();
            FluidStack simDrained = aBaseMetaTileEntity.drain(ForgeDirection.getOrientation(front), Integer.MAX_VALUE, false);
            if (simDrained != null) {
                int simFilledAmount = atSide.fill(ForgeDirection.getOrientation(back), simDrained, false);
                if (simFilledAmount > 0) {
                    FluidStack drained = aBaseMetaTileEntity.drain(ForgeDirection.getOrientation(front), simFilledAmount, true);
                    atSide.fill(ForgeDirection.getOrientation(back), drained, true);
                }
            }
        }
    }

    /**
     * @param aFacing
     *
     * @return
     */
    @Override
    public boolean isFacingValid(final byte aFacing) {
        return true;
    }

    @Override
    public int getCapacity() {
        return Integer.MAX_VALUE;
    }

    /**
     * @param aSide
     * @param aFluid
     *
     * @return
     */
    @Override
    public boolean canDrain(final ForgeDirection aSide, final Fluid aFluid) {
        return aSide.ordinal() == getBaseMetaTileEntity().getFrontFacing() && isExecutionTick();
    }

    /**
     * @param aSide
     *         Orientation the fluid is drained to.
     * @param maxDrain
     *         Maximum amount of fluid to drain.
     * @param doDrain
     *         If false, drain will only be simulated.
     *
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
     * @param aTextures
     *         is the optional Array you can give to the Constructor.
     */
    @Override
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[2][17][];
        ITexture out = TextureFactory.of(OVERLAY_PIPE_OUT);
        for (int i = 0; i < rTextures[0].length; i++) {
            rTextures[0][i] = new ITexture[]{MACHINE_CASINGS[mTier][i], out};
            rTextures[1][i] = new ITexture[]{MACHINE_CASINGS[mTier][i]};
        }
        return new ITexture[0][][];
    }

    public boolean isExecutionTick() {
        return tCount == 0;
    }

    public boolean canRun() {
        return isActive() && isRsActive();
    }

    public void syncRates() {
        if (perTick) {
            ratePerSecond = ratePerTick * 20;
            tPeriod = 1;
        } else {
            tPeriod = 20;
        }
        adjustCount();
        markDirty();
    }

    protected void setPerTick(final boolean perTick) {
        this.perTick = perTick;
        markDirty();
    }

    protected void setRatePerTick(final int ratePerTick) {
        this.ratePerTick = ratePerTick;
        markDirty();
    }

    protected void setRatePerSecond(final int ratePerSecond) {
        this.ratePerSecond = ratePerSecond;
        markDirty();
    }

    protected void setRsActive(final boolean rsActive) {
        this.rsActive = rsActive;
        markDirty();
    }

    protected void setActive(final boolean active) {
        this.active = active;
        markDirty();
    }

    protected void setFluidName(final String fluidName) {
        this.fluidName = fluidName;
        markDirty();
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

}
