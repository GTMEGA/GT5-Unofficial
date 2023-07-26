package gregtech.common.tileentities.storage;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.render.TextureFactory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import static gregtech.api.enums.Textures.BlockIcons.*;

public class GT_MetaTileEntity_DevTank extends GT_MetaTileEntity_BasicTank {

    public GT_MetaTileEntity_DevTank(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 3, "Stores ∞ L of fluid. You ARE supposed to have this, right?");
    }

    public GT_MetaTileEntity_DevTank(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    public GT_MetaTileEntity_DevTank(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean doesFillContainers() {
        return true;
    }

    @Override
    public boolean doesEmptyContainers() {
        return true;
    }

    @Override
    public boolean canTankBeFilled() {
        return true;
    }

    @Override
    public boolean canTankBeEmptied() {
        return true;
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
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DevTank(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide != ForgeDirection.UP.ordinal()) return new ITexture[]{MACHINE_CASINGS[mTier][aColorIndex + 1]};
        return new ITexture[]{
                MACHINE_CASINGS[mTier][aColorIndex + 1],
                TextureFactory.of(OVERLAY_DTANK),
                TextureFactory.builder().addIcon(OVERLAY_DTANK_GLOW).glow().build()
        };
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!aBaseMetaTileEntity.isClientSide()) {
            aBaseMetaTileEntity.openGUI(aPlayer);
        }
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public int getTankPressure() {
        return 100;
    }

    @Override
    public int getCapacity() {
        return 2147483647;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        if (mFluid == null) {
            return new String[]{
                    EnumChatFormatting.LIGHT_PURPLE + "Dev Tank" + EnumChatFormatting.RESET,
                    EnumChatFormatting.GOLD + "No Fluid Set" + EnumChatFormatting.RESET
            };
        } else {
            return new String[]{
                    EnumChatFormatting.LIGHT_PURPLE + "Dev Tank" + EnumChatFormatting.RESET,
                    "Stored Fluid:",
                    EnumChatFormatting.GOLD + mFluid.getLocalizedName() + EnumChatFormatting.RESET,
                    EnumChatFormatting.GREEN + "∞ L" + EnumChatFormatting.RESET
            };
        }
    }

    @Override
    public FluidStack getDrainableStack() {
        if (mFluid != null) {
            mFluid.amount = getCapacity();
            return mFluid.copy();
        }
        return null;
    }

    @Override
    public FluidStack setDrainableStack(FluidStack aFluid) {
        if (aFluid != null) {
            mFluid = aFluid.copy();
            if (mFluid != null) {
                mFluid.amount = getCapacity();
            }
        } else {
            mFluid = null;
        }
        return mFluid;
    }

    @Override
    public boolean isDrainableStackSeparate() {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return aPlayer.capabilities.isCreativeMode;
    }
}
