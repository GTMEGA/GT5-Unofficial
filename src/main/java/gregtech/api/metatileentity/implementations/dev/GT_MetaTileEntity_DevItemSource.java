package gregtech.api.metatileentity.implementations.dev;


import gregtech.api.enums.RSControlMode;
import gregtech.api.interfaces.IRedstoneSensitive;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_DigitalChestBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;


public class GT_MetaTileEntity_DevItemSource extends GT_MetaTileEntity_TieredMachineBlock implements IRedstoneSensitive {

    private final byte[] rsValues = {0, 0, 0, 0, 0, 0};

    private RSControlMode redstoneMode = RSControlMode.IGNORE;

    private ItemStack stored = null;

    private int itemPerTick = 0;

    private boolean active = true;

    private boolean rsActive = true;

    public GT_MetaTileEntity_DevItemSource(
            final int aID, final String aName, final String aNameRegional) {
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
        return new GT_MetaTileEntity_DevItemSource(mName, mDescriptionArray, mTextures);
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
    public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
        if (!aPlayer.capabilities.isCreativeMode) {
            return false;
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    /**
     * Icon of the Texture. If this returns null then it falls back to getTextureIndex.
     *
     * @param aBaseMetaTileEntity
     * @param aSide               is the Side of the Block
     * @param aFacing             is the direction the Block is facing (or a Bitmask of all Connections in case of Pipes)
     * @param aColorIndex         The Minecraft Color the Block is having
     * @param aActive             if the Machine is currently active (use this instead of calling mBaseMetaTileEntity.mActive!!!). Note: In case of Pipes this means if this Side is connected to something or not.
     * @param aRedstone           if the Machine is currently outputting a RedstoneSignal (use this instead of calling mBaseMetaTileEntity.mRedstone!!!)
     */
    @Override
    public ITexture[] getTexture(
            final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive,
            final boolean aRedstone
                                ) {
        return new ITexture[0];
    }

    @Override
    public int getMaxItemCount() {
        return 2147483647;
    }

    public boolean isActive() {
        return active && rsActive;
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
        return new ITexture[0][][];
    }

    @Override
    public void saveNBTData(final NBTTagCompound aNBT) {
        if (stored != null) {
            aNBT.setTag("item", stored.writeToNBT(new NBTTagCompound()));
        }
        aNBT.setBoolean("active", active);
        aNBT.setInteger("perTick", itemPerTick);
        redstoneMode.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        if (aNBT.hasKey("item")) {
            this.stored = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("item"));
        }
        active = aNBT.getBoolean("active");
        itemPerTick = aNBT.getInteger("perTick");
        redstoneMode = RSControlMode.loadFromNBTData(aNBT);
    }

    /**
     * @param aBaseMetaTileEntity
     * @param aTick
     */
    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        if (getBaseMetaTileEntity().isServerSide() && getBaseMetaTileEntity().isAllowedToWork()) {
            if (stored != null && isActive()) {
                mInventory[0] = GT_Utility.copyAmount(itemPerTick, stored);
            } else {
                mInventory[1] = null;
            }
            if (mInventory[0] != null && GT_Utility.areStacksEqual(stored, mInventory[0])) {
                mInventory[0] = null;
            }
        }
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
        return isActive() && aIndex == 1;
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
        return aIndex == 0 && (stored == null || GT_Utility.areStacksEqual(stored, aStack));
    }

}
