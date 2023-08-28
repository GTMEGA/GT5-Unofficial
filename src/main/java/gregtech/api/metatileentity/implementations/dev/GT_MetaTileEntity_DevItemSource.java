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
import gregtech.api.util.GT_Utility;
import gregtech.api.util.INonNBTSerializable;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.dev.GT_Container_DevItemSource;
import gregtech.common.gui.dev.GT_GUIContainer_DevItemSource;
import io.netty.buffer.ByteBuf;
import lombok.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;


@Getter
public class GT_MetaTileEntity_DevItemSource extends GT_MetaTileEntity_TieredMachineBlock
        implements IAdvancedGUIEntity, IRedstoneSensitive {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GUIData implements ISerializableObject {

        @Builder.Default
        private RSControlMode redstoneMode = RSControlMode.IGNORE;

        @Builder.Default
        private int itemPerTick = 1;

        @Builder.Default
        private int itemPerSecond = 20;

        @Builder.Default
        private boolean perTick = true;

        @Builder.Default
        private boolean active = true;

        @Builder.Default
        private boolean rsActive = true;

        /**
         * @return
         */
        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new GUIData(redstoneMode, itemPerTick, itemPerSecond, perTick, active, rsActive);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound result = new NBTTagCompound();
            return result;
        }

        /**
         * Write data to given ByteBuf The data saved this way is intended to be stored for short amount of time over
         * network. DO NOT store it to disks.
         *
         * @param aBuf
         */
        @Override
        public void writeToByteBuf(final ByteBuf aBuf) {
            aBuf.writeInt(redstoneMode.ordinal());
            aBuf.writeInt(itemPerTick);
            aBuf.writeInt(itemPerSecond);
            aBuf.writeBoolean(perTick);
            aBuf.writeBoolean(active);
            aBuf.writeBoolean(rsActive);
        }

        @Override
        public void loadDataFromNBT(final NBTBase oNBT) {
            if (!(oNBT instanceof NBTTagCompound)) {
                return;
            }
            NBTTagCompound devNBT = (NBTTagCompound) oNBT;
            active = devNBT.getBoolean("active");
            itemPerTick = devNBT.getInteger("perTick");
            itemPerSecond = devNBT.getInteger("perSecond");
            perTick = devNBT.getBoolean("mode");
            redstoneMode = RSControlMode.loadFromNBTData(devNBT);
        }

        /**
         * Read data from given parameter and return this. The data read this way is intended to be stored for short
         * amount of time over network.
         *
         * @param aBuf
         * @param aPlayer
         */
        @Nonnull
        @Override
        public ISerializableObject readFromPacket(final ByteArrayDataInput aBuf, final EntityPlayerMP aPlayer) {
            return new GUIData(RSControlMode.getMode(aBuf.readInt()), aBuf.readInt(), aBuf.readInt(),
                               aBuf.readBoolean(), aBuf.readBoolean(), aBuf.readBoolean());
        }

    }

    public static final int numSlots = 3;

    public static final int slotManualOutput = 0, slotStorage = 1, slotOutput = 2;

    private final byte[] rsValues = {0, 0, 0, 0, 0, 0};

    private GUIData internalData = new GUIData();

    private RSControlMode redstoneMode = RSControlMode.IGNORE;

    private int itemPerTick = 1;

    private int itemPerSecond = 0;

    private int tCount = 0;

    private int tPeriod = 1;

    private boolean perTick = true;

    private boolean active = true;

    private boolean rsActive = true;

    public GT_MetaTileEntity_DevItemSource(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional, 15, numSlots, new String[]{
                "Synthesizes items from... You know, you don't really care, do you?"
        });
    }

    public GT_MetaTileEntity_DevItemSource(final String aName, final String[] aDescription,
                                           final ITexture[][][] aTextures) {
        super(aName, 15, numSlots, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DevItemSource(this.mName, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public void saveNBTData(final NBTTagCompound aNBT) {
        final NBTTagCompound devNBT = new NBTTagCompound();
        if (hasItem()) {
            devNBT.setTag("item", getStored().writeToNBT(new NBTTagCompound()));
        }
        devNBT.setBoolean("mode", perTick);
        devNBT.setBoolean("active", active);
        devNBT.setInteger("perTick", itemPerTick);
        devNBT.setInteger("perSecond", itemPerSecond);
        devNBT.setInteger("tick", tCount);
        devNBT.setInteger("period", tPeriod);
        redstoneMode.saveNBTData(devNBT);
        aNBT.setTag("dev", devNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        final NBTTagCompound devNBT = aNBT.getCompoundTag("dev");
        if (devNBT.hasKey("item")) {
            mInventory[2] = ItemStack.loadItemStackFromNBT(devNBT.getCompoundTag("item"));
        }
        /*active = devNBT.getBoolean("active");
        itemPerTick = devNBT.getInteger("perTick");
        itemPerSecond = devNBT.getInteger("perSecond");
        perTick = devNBT.getBoolean("mode");
        tCount = devNBT.getInteger("tick");
        tPeriod = devNBT.getInteger("period");
        redstoneMode = RSControlMode.loadFromNBTData(devNBT);*/
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
    public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide,
                                  final ItemStack aStack) {
        refreshOutput();
        return canOutputItems() && aIndex == slotOutput && aSide == aBaseMetaTileEntity.getFrontFacing();
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
    public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide,
                                 final ItemStack aStack) {
        return false;
        // return aIndex == 0 && (!hasItem() || GT_Utility.areStacksEqual(getStored(), aStack)) && aSide != aBaseMetaTileEntity.getFrontFacing();
    }

    /**
     * Icon of the Texture. If this returns null then it falls back to getTextureIndex.
     *
     * @param aBaseMetaTileEntity
     *         base entity
     * @param aSide
     *         is the Side of the Block
     * @param aFacing
     *         is the direction the Block is facing (or a Bitmask of all Connections in case of Pipes)
     * @param aColorIndex
     *         The Minecraft Color the Block is having
     * @param aActive
     *         if the Machine is currently active (use this instead of calling mBaseMetaTileEntity.mActive!!!). Note: In
     *         case of Pipes this means if this Side is connected to something or not.
     * @param aRedstone
     *         if the Machine is currently outputting a RedstoneSignal (use this instead of calling
     *         mBaseMetaTileEntity.mRedstone!!!)
     */
    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
                                 final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
        return mTextures[aSide == aFacing ? 0 : 1][aColorIndex + 1];
    }

    public boolean hasItem() {
        return getStored() != null;
    }

    public ItemStack getStored() {
        return mInventory[slotStorage];
    }

    public void setStored(ItemStack newStored) {
        if (newStored != null) {
            newStored = GT_Utility.copyAmount(1, newStored);
        }
        mInventory[slotStorage] = newStored;
        markDirty();
    }

    public boolean canOutputItems() {
        return canRun() && hasItem() && tCount == 0;
    }

    public boolean canRun() {
        return active && rsActive;
    }

    /**
     * @param aSide
     * @param aWrenchingSide
     * @param aPlayer
     * @param aX
     * @param aY
     * @param aZ
     *
     * @return
     */
    @Override
    public boolean onWrenchRightClick(final byte aSide, final byte aWrenchingSide, final EntityPlayer aPlayer,
                                      final float aX, final float aY, final float aZ) {
        if (!isAccessAllowed(aPlayer)) {
            return false;
        }
        return super.onWrenchRightClick(aSide, aWrenchingSide, aPlayer, aX, aY, aZ);
    }

    /**
     * @param aBaseMetaTileEntity
     * @param aTick
     */
    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (getBaseMetaTileEntity().isServerSide() && getBaseMetaTileEntity().isAllowedToWork()) {
            refreshOutput();
            final IInventory atSide = aBaseMetaTileEntity.getIInventoryAtSide(aBaseMetaTileEntity.getFrontFacing());
            if (canOutputItems() && atSide != null) {
                moveItems(aBaseMetaTileEntity, atSide);
            }
            tickForward();
            markDirty();
        }
    }

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
     *
     * @return
     */
    @Override
    public boolean isFacingValid(final byte aFacing) {
        return true;
    }

    /**
     * @param aPlayer
     *
     * @return
     */
    @Override
    public boolean isAccessAllowed(final EntityPlayer aPlayer) {
        return aPlayer.capabilities.isCreativeMode;
    }

    /**
     * @return
     */
    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public int getMaxItemCount() {
        return 2147483647;
    }

    /**
     * @param aIndex
     *
     * @return
     */
    @Override
    public ItemStack getStackInSlot(final int aIndex) {
        if (aIndex == slotStorage) {
            return GT_Utility.copyAmount(getAmount(), getStored());
        }
        return super.getStackInSlot(aIndex);
    }

    private int getAmount() {
        if (perTick) {
            return itemPerTick;
        } else {
            return itemPerSecond;
        }
    }

    /**
     * @param aIndex
     * @param aStack
     *
     * @return
     */
    @Override
    public boolean isItemValidForSlot(final int aIndex, final ItemStack aStack) {
        return getStored() == null || GT_Utility.areStacksEqual(getStored(), aStack);
    }

    /**
     * @param aSide
     *
     * @return
     */
    @Override
    public int[] getAccessibleSlotsFromSide(final int aSide) {
        if (aSide == getBaseMetaTileEntity().getFrontFacing()) {
            return new int[]{slotOutput};
        }
        return new int[]{};
    }

    /**
     *
     */
    @Override
    public void markDirty() {
        super.markDirty();
        getBaseMetaTileEntity().markDirty();
    }

    /**
     * @param aID
     * @param aPlayerInventory
     * @param aBaseMetaTileEntity
     *
     * @return
     */
    @Override
    public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory,
                               final IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_DevItemSource(aPlayerInventory, aBaseMetaTileEntity);
    }

    /**
     * @param aID
     * @param aPlayerInventory
     * @param aBaseMetaTileEntity
     *
     * @return
     */
    @Override
    public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory,
                               final IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_DevItemSource(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public RSControlMode getMode() {
        return redstoneMode;
    }

    @Override
    public byte[] getRSValues() {
        return rsValues;
    }

    @Override
    public void setMode(final RSControlMode newMode) {
        this.redstoneMode = newMode;
    }

    @Override
    public void updateRSValues(final byte side, final byte rsSignal) {
        rsValues[side] = rsSignal;
    }

    @Override
    public void processRS() {
        this.rsActive = redstoneMode.checkPredicate(getMaxRSValue());
    }

    /**
     * Used Client Side to get a Texture Set for this Block. Called after setting the Tier and the Description so that
     * those two are accessible.
     *
     * @param aTextures
     *         is the optional Array you can give to the Constructor.
     */
    @Override
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[2][17][];
        for (int i = 0; i < rTextures[0].length; i++) {
            rTextures[0][i] = new ITexture[]{MACHINE_CASINGS[mTier][i], TextureFactory.of(OVERLAY_PIPE_OUT)};
            rTextures[1][i] = new ITexture[]{MACHINE_CASINGS[mTier][i]};
        }
        return rTextures;
    }

    public void clearItem() {
        setStored(null);
    }

    /**
     * Receive and accept the packet
     *
     * @param data
     */
    @Override
    public void receiveGuiData(final ISerializableObject data) {
        if (data instanceof GUIData) {
            val gData = (GUIData) data;
            setMode(gData.redstoneMode);
            setPerTick(gData.perTick);
            setItemPerTick(gData.itemPerTick);
            setItemPerSecond(gData.itemPerSecond);
            adjustItemRate();
            setActive(gData.active);
            processRS();
            markDirty();
        }
    }

    public void setPerTick(final boolean perTick) {
        this.perTick = perTick;
        markDirty();
    }

    public void setItemPerTick(final int itemPerTick) {
        this.itemPerTick = itemPerTick;
        markDirty();
    }

    public void setItemPerSecond(final int itemPerSecond) {
        this.itemPerSecond = itemPerSecond;
        markDirty();
    }

    public void adjustItemRate() {
        if (perTick) {
            itemPerSecond = itemPerTick * 20;
            tPeriod = 1;
        } else {
            tPeriod = 20;
        }
        adjustCount();
        markDirty();
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    private void adjustCount() {
        if (this.tPeriod <= 0) {
            this.tPeriod = 1;
        }
        this.tCount = this.tCount % this.tPeriod;
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

    @Override
    public ISerializableObject getTEGUIData() {
        return new GUIData(redstoneMode, itemPerTick, itemPerSecond, perTick, active, rsActive);
    }

    public void refreshOutput() {
        if (hasItem()) {
            mInventory[slotManualOutput] = GT_Utility.copyAmount(64, getStored());
            mInventory[slotOutput] = GT_Utility.copyAmount(getAmount(), getStored());
        } else {
            mInventory[slotManualOutput] = null;
            mInventory[slotOutput] = null;
        }
    }

    private void moveItems(final IGregTechTileEntity aBaseMetaTileEntity, final IInventory atSide) {
        if (!hasItem()) {
            return;
        }
        final byte sideInputtedInto = aBaseMetaTileEntity.getBackFacing();
        int[] targetSlots = null;
        if (atSide instanceof ISidedInventory) {
            targetSlots = ((ISidedInventory) atSide).getAccessibleSlotsFromSide(sideInputtedInto);
        }
        if (targetSlots == null) {
            targetSlots = new int[atSide.getSizeInventory()];
            for (int i = 0; i < targetSlots.length; i++) {
                targetSlots[i] = i;
            }
        }
        int moved = 0, max = getAmount();
        for (int targetSlot : targetSlots) {
            if (moved >= max) {
                break;
            }
            moved += GT_Utility.moveStackFromSlotAToSlotB(aBaseMetaTileEntity, atSide, slotOutput, targetSlot,
                                                          (byte) 64, (byte) 1,
                                                          (byte) Math.min(max - moved, getStored().getMaxStackSize()),
                                                          (byte) 1);
            refreshOutput();
        }
    }

    private void tickForward() {
        this.tCount += 1;
        adjustCount();
    }

}
