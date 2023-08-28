package gregtech.api.metatileentity.implementations.dev;


import com.google.common.io.ByteArrayDataInput;
import gregtech.api.enums.RSControlMode;
import gregtech.api.enums.Textures.BlockIcons.*;
import gregtech.api.interfaces.IAdvancedGUIEntity;
import gregtech.api.interfaces.IRedstoneSensitive;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.ISerializableObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;


public class GT_MetaTileEntity_DevFluidSource extends GT_MetaTileEntity_TieredMachineBlock implements IAdvancedGUIEntity, IRedstoneSensitive {

    public GT_MetaTileEntity_DevFluidSource(
            final int aID, final String aName, final String aNameRegional
                                           ) {
        super(aID, aName, aNameRegional, 15, 1, new String[]{
                "Someone spilled a bottle of coke and this happened"
        });
    }

    public GT_MetaTileEntity_DevFluidSource(
            final String aName, final String[] aDescription, final ITexture[][][] aTextures
                                           ) {
        super(aName, 15, 1, aDescription, aTextures);
    }

    /**
     * @param aSide
     * @param aFluid
     * @return
     */
    @Override
    public boolean canDrain(final ForgeDirection aSide, final Fluid aFluid) {
        return aSide.ordinal() == getBaseMetaTileEntity().getFrontFacing();
    }

    /**
     * @param aSide    Orientation the fluid is drained to.
     * @param maxDrain Maximum amount of fluid to drain.
     * @param doDrain  If false, drain will only be simulated.
     * @return
     */
    @Override
    public FluidStack drain(final ForgeDirection aSide, final int maxDrain, final boolean doDrain) {
        return super.drain(aSide, maxDrain, doDrain);
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
    public RSControlMode getMode() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public byte[] getRSValues() {
        return new byte[0];
    }

    /**
     * @param newMode
     */
    @Override
    public void setMode(final RSControlMode newMode) {

    }

    /**
     * @param side
     * @param rsSignal
     */
    @Override
    public void updateRSValues(final byte side, final byte rsSignal) {

    }

    /**
     *
     */
    @Override
    public void processRS() {

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
     * ^= writeToNBT
     *
     * @param aNBT
     */
    @Override
    public void saveNBTData(final NBTTagCompound aNBT) {

    }

    /**
     * ^= readFromNBT
     *
     * @param aNBT
     */
    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {

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

    /**
     * Icon of the Texture. If this returns null then it falls back to getTextureIndex.
     *
     * @param aBaseMetaTileEntity
     * @param aSide               is the Side of the Block
     * @param aFacing             is the direction the Block is facing (or a Bitmask of all Connections in case of Pipes)
     * @param aColorIndex         The Minecraft Color the Block is having
     * @param aActive             if the Machine is currently active (use this instead of calling mBaseMetaTileEntity.mActive!!!). Note: In case of Pipes
     *                            this means if this Side is connected to something or not.
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
        return mTextures[aSide == aFacing ? 0 : 1][aColorIndex + 1];
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
        ITexture out = TextureFactory.of(OVERLAY_PIPE_OUT);
        for (int i = 0; i < rTextures[0].length; i++) {
            rTextures[0][i] = new ITexture[]{MACHINE_CASINGS[mTier][i], out};
            rTextures[1][i] = new ITexture[]{MACHINE_CASINGS[mTier][i]};
        }
        return new ITexture[0][][];
    }

}
