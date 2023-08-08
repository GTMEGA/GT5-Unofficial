package gregtech.api.metatileentity.implementations.dev;


import gregtech.api.enums.RSControlMode;
import gregtech.api.interfaces.IRedstoneSensitive;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.GT_Container_DevItemSource;
import gregtech.common.gui.GT_GUIContainer_DevItemSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;


public class GT_MetaTileEntity_DevItemSource extends GT_MetaTileEntity_TieredMachineBlock implements IRedstoneSensitive {

    private final byte[] rsValues = {0, 0, 0, 0, 0, 0};

    private RSControlMode redstoneMode = RSControlMode.IGNORE;

    private int itemPerTick = 0;

    private int itemPerSecond = 0;

    private int tCount = 0;

    private int tPeriod = 1;

    private boolean perTick = true;

    private boolean active = true;

    private boolean rsActive = true;

    public GT_MetaTileEntity_DevItemSource(
            final int aID, final String aName, final String aNameRegional
                                          ) {
        super(aID, aName, aNameRegional, 15, 3, new String[]{
                "Synthesizes items from... You know, you don't really care do you?"
        });
    }

    public GT_MetaTileEntity_DevItemSource(
            final String aName, final String[] aDescription, final ITexture[][][] aTextures
                                          ) {
        super(aName, 15, 3, aDescription, aTextures);
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
        active = devNBT.getBoolean("active");
        itemPerTick = devNBT.getInteger("perTick");
        itemPerSecond = devNBT.getInteger("perSecond");
        perTick = devNBT.getBoolean("mode");
        tCount = devNBT.getInteger("tick");
        tPeriod = devNBT.getInteger("period");
        redstoneMode = RSControlMode.loadFromNBTData(devNBT);
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
        return canOutputItems() && aIndex == 1 && aSide == aBaseMetaTileEntity.getFrontFacing();
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
        // return aIndex == 0 && (!hasItem() || GT_Utility.areStacksEqual(getStored(), aStack)) && aSide != aBaseMetaTileEntity.getFrontFacing();
    }

    /**
     * Icon of the Texture. If this returns null then it falls back to getTextureIndex.
     *
     * @param aBaseMetaTileEntity base entity
     * @param aSide               is the Side of the Block
     * @param aFacing             is the direction the Block is facing (or a Bitmask of all Connections in case of Pipes)
     * @param aColorIndex         The Minecraft Color the Block is having
     * @param aActive             if the Machine is currently active (use this instead of calling mBaseMetaTileEntity.mActive!!!). Note: In case of Pipes
     *                            this means if this Side is connected to something or not.
     * @param aRedstone           if the Machine is currently outputting a RedstoneSignal (use this instead of calling mBaseMetaTileEntity.mRedstone!!!)
     */
    @Override
    public ITexture[] getTexture(
            final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive,
            final boolean aRedstone
                                ) {
        return mTextures[aSide == aFacing ? 0 : 1][aColorIndex + 1];
    }

    public boolean hasItem() {
        return getStored() != null;
    }

    public ItemStack getStored() {
        return mInventory[2];
    }

    public void setStored(final ItemStack newStored) {
        mInventory[2] = newStored;
        markDirty();
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
     * Used Client Side to get a Texture Set for this Block.
     * Called after setting the Tier and the Description so that those two are accessible.
     *
     * @param aTextures is the optional Array you can give to the Constructor.
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

    /**
     * @param aBaseMetaTileEntity
     * @param aTick
     */
    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        if (getBaseMetaTileEntity().isServerSide() && getBaseMetaTileEntity().isAllowedToWork()) {
            if (hasItem() && canRun()) {
                mInventory[1] = GT_Utility.copyAmount(getAmount(), getAmount());
            } else {
                mInventory[1] = null;
            }
            if (mInventory[0] != null && !hasItem()) {
                setStored(GT_Utility.copyAmount(getMaxItemCount(), mInventory[0]));
            }
            if (mInventory[0] != null && GT_Utility.areStacksEqual(getStored(), mInventory[0])) {
                mInventory[0] = null;
            }
            final IInventory atSide = aBaseMetaTileEntity.getIInventoryAtSide(aBaseMetaTileEntity.getFrontFacing());
            if (canOutputItems() && atSide != null) {
                moveItems(aBaseMetaTileEntity, atSide);
            }
            tickForward();
            markDirty();
        }
    }

    private void moveItems(final IGregTechTileEntity aBaseMetaTileEntity, final IInventory atSide) {
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
        int moved = 0;
        for (int targetSlot: targetSlots) {
            if (moved >= getAmount()) {
                break;
            }
            moved += GT_Utility.moveStackFromSlotAToSlotB(aBaseMetaTileEntity, atSide, 2, targetSlot, (byte)64, (byte)1, (byte) getStored().getMaxStackSize(), (byte)1);
        }
    }

    /**
     * @param aIndex
     * @return
     */
    @Override
    public ItemStack getStackInSlot(final int aIndex) {
        if (aIndex == 2) {
            return GT_Utility.copyAmount(getAmount(), getStored());
        }
        return super.getStackInSlot(aIndex);
    }

    /**
     * @return
     */
    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
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
     * @param aID
     * @param aPlayerInventory
     * @param aBaseMetaTileEntity
     * @return
     */
    @Override
    public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_DevItemSource(aPlayerInventory, aBaseMetaTileEntity);
    }

    /**
     * @param aID
     * @param aPlayerInventory
     * @param aBaseMetaTileEntity
     * @return
     */
    @Override
    public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_DevItemSource(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public int getMaxItemCount() {
        return 2147483647;
    }

    /**
     * @param aSide
     * @return
     */
    @Override
    public int[] getAccessibleSlotsFromSide(final int aSide) {
        if (aSide == getBaseMetaTileEntity().getFrontFacing()) {
            return new int[]{1};
        }
        return new int[]{};
    }

    public boolean canRun() {
        return active && rsActive;
    }

    private int getAmount() {
        if (perTick) {
            return itemPerTick;
        } else {
            return itemPerSecond;
        }
    }

    public boolean canOutputItems() {
        return canRun() && hasItem() && tCount == 0;
    }

    private void tickForward() {
        this.tCount += 1;
        adjustCount();
    }

    private void adjustCount() {
        this.tCount = this.tCount % this.tPeriod;
    }

}
