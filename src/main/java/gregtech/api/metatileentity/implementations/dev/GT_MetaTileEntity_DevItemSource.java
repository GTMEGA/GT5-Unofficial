package gregtech.api.metatileentity.implementations.dev;


import gregtech.api.enums.RSControlMode;
import gregtech.api.interfaces.IRedstoneSensitive;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_DigitalChestBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;


public class GT_MetaTileEntity_DevItemSource extends GT_MetaTileEntity_DigitalChestBase implements IRedstoneSensitive {

    private final byte[] rsValues = {0, 0, 0, 0, 0, 0};

    private RSControlMode redstoneMode = RSControlMode.IGNORE;

    private int itemPerTick = 0;

    private boolean active = true;

    public GT_MetaTileEntity_DevItemSource(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional, 15);
    }

    public GT_MetaTileEntity_DevItemSource(
            final String aName, final String[] aDescription, final ITexture[][][] aTextures
                                          ) {
        super(aName, 15, aDescription, aTextures);
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

    @Override
    public int getMaxItemCount() {
        return 2147483647;
    }

    @Override
    public int getItemCount() {
        return getItemStack() == null ? 0 : getMaxItemCount();
    }

    @Override
    public void setItemCount(final int mItemCount) {
        if (getItemStack() != null) {
            super.setItemCount(getMaxItemCount());
        }
    }

    @Override
    public void processRS() {
        this.active = redstoneMode.checkPredicate(getMaxRSValue());
    }

    @Override
    protected String chestName() {
        return "Developer Item Source";
    }

    @Override
    public void saveNBTData(final NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("active", active);
        aNBT.setInteger("perTick", itemPerTick);
        redstoneMode.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        active = aNBT.getBoolean("active");
        itemPerTick = aNBT.getInteger("perTick");
        redstoneMode = RSControlMode.loadFromNBTData(aNBT);
    }

    @Override
    public boolean allowPullStack(
            final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack
                                 ) {
        return super.allowPullStack(aBaseMetaTileEntity, aIndex, aSide, aStack) && active;
    }

}
