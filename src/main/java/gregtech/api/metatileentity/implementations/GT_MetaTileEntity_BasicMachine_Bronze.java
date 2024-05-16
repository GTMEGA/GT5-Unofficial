package gregtech.api.metatileentity.implementations;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.WorldSpawnedEventBuilder;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

import static gregtech.api.enums.GT_Values.*;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_BRONZE_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_BRONZE_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_BRONZE_TOP;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_STEAM_OUT;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the main construct for my Basic Machines such as the Automatic Extractor
 * Extend this class to make a simple Machine
 */
public abstract class GT_MetaTileEntity_BasicMachine_Bronze extends GT_MetaTileEntity_BasicMachine {
    /*
    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_BasicMachine_Bronze(mTier, mDescription, mTextures);
    }
    */
    private static final int NEEDS_STEAM_VENTING = 64;
    public boolean mNeedsSteamVenting = false;

    public GT_MetaTileEntity_BasicMachine_Bronze(int aID, String aName, String aNameRegional, String aDescription, int aInputSlotCount, int aOutputSlotCount, boolean aBricked) {
        super(aID, aName, aNameRegional, aBricked ? 1 : 0, 0, aDescription, aInputSlotCount, aOutputSlotCount, "", "");
    }

    public GT_MetaTileEntity_BasicMachine_Bronze(String aName, String aDescription, ITexture[][][] aTextures, int aInputSlotCount, int aOutputSlotCount, boolean aBricked) {
        super(aName, aBricked ? 1 : 0, 0, aDescription, aTextures, aInputSlotCount, aOutputSlotCount, "", "");
    }

    public GT_MetaTileEntity_BasicMachine_Bronze(String aName, String[] aDescription, ITexture[][][] aTextures, int aInputSlotCount, int aOutputSlotCount, boolean aBricked) {
        super(aName, aBricked ? 1 : 0, 0, aDescription, aTextures, aInputSlotCount, aOutputSlotCount, "", "");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mNeedsSteamVenting", mNeedsSteamVenting);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mNeedsSteamVenting = aNBT.getBoolean("mNeedsSteamVenting");
    }

    @Override
    public boolean isElectric() {
        return false;
    }

    @Override
    public boolean isEnetInput() {
        return false;
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return false;
    }

    @Override
    public long maxEUStore() {
        return 0;
    }

    @Override
    public long maxEUInput() {
        return 0;
    }

    @Override
    public int rechargerSlotCount() {
        return 0;
    }

    @Override
    public int dechargerSlotCount() {
        return 0;
    }

    @Override
    public boolean isSteampowered() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return super.isFacingValid(aFacing) && aFacing != mMainFacing;
    }

    @Override
    public long getMinimumStoredEU() {
        return 1000;
    }

    @Override
    public long maxSteamStore() {
        return 16000;
    }

    @Override
    public boolean isLiquidInput(byte aSide) {
        return aSide != mMainFacing;
    }

    @Override
    public boolean isLiquidOutput(byte aSide) {
        return aSide != mMainFacing;
    }

    @Override
    public boolean doesAutoOutput() {
        return false;
    }

    @Override
    public boolean allowToCheckRecipe() {
        if (mNeedsSteamVenting && getBaseMetaTileEntity().getCoverIDAtSide(getBaseMetaTileEntity().getFrontFacing()) == 0 && !GT_Utility.hasBlockHitBox(getBaseMetaTileEntity().getWorld(), getBaseMetaTileEntity().getOffsetX(getBaseMetaTileEntity().getFrontFacing(), 1), getBaseMetaTileEntity().getOffsetY(getBaseMetaTileEntity().getFrontFacing(), 1), getBaseMetaTileEntity().getOffsetZ(getBaseMetaTileEntity().getFrontFacing(), 1))) {
            sendSound((byte) 9);
            mNeedsSteamVenting = false;
            try {
                for (EntityLivingBase tLiving : (ArrayList<EntityLivingBase>) getBaseMetaTileEntity().getWorld().getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(getBaseMetaTileEntity().getOffsetX(getBaseMetaTileEntity().getFrontFacing(), 1), getBaseMetaTileEntity().getOffsetY(getBaseMetaTileEntity().getFrontFacing(), 1), getBaseMetaTileEntity().getOffsetZ(getBaseMetaTileEntity().getFrontFacing(), 1), getBaseMetaTileEntity().getOffsetX(getBaseMetaTileEntity().getFrontFacing(), 1) + 1, getBaseMetaTileEntity().getOffsetY(getBaseMetaTileEntity().getFrontFacing(), 1) + 1, getBaseMetaTileEntity().getOffsetZ(getBaseMetaTileEntity().getFrontFacing(), 1) + 1))) {
                    GT_Utility.applyHeatDamage(tLiving, getSteamDamage());
                }
            } catch (Throwable e) {
                if (D1) e.printStackTrace(GT_Log.err);
            }
        }
        return !mNeedsSteamVenting;
    }

    @Override
    public int checkRecipe(boolean skipOC) {
        GT_Recipe.GT_Recipe_Map tMap = getRecipeList();
        if (tMap == null) return DID_NOT_FIND_RECIPE;
        GT_Recipe tRecipe = tMap.findRecipe(getBaseMetaTileEntity(), mLastRecipe, false, V[1], new FluidStack[]{getFillableStack()}, getSpecialSlot(), getAllInputs());
        if (tRecipe == null) return DID_NOT_FIND_RECIPE;

        if (GT_Mod.gregtechproxy.mLowGravProcessing && (tRecipe.mSpecialValue == -100 || tRecipe.mSpecialValue == -300) &&
                !isValidForLowGravity(tRecipe,getBaseMetaTileEntity().getWorld().provider.dimensionId))
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        if (tRecipe.mCanBeBuffered) mLastRecipe = tRecipe;
        if (!canOutput(tRecipe)) {
            mOutputBlocked++;
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        }
        if (tRecipe.mSpecialValue == -200 && (getCallbackBase() == null || getCallbackBase().mEfficiency == 0))
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        if (!tRecipe.isRecipeInputEqual(true, new FluidStack[]{getFillableStack()}, getAllInputs()))
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        for (int i = 0; i < mOutputItems.length; i++)
            if (getBaseMetaTileEntity().getRandomNumber(10000) < tRecipe.getOutputChance(i))
                mOutputItems[i] = tRecipe.getOutput(i);
        if (tRecipe.mSpecialValue == -200 || tRecipe.mSpecialValue == -300)
            for (int i = 0; i < mOutputItems.length; i++)
                if (mOutputItems[i] != null && getBaseMetaTileEntity().getRandomNumber(10000) > getCallbackBase().mEfficiency)
                {
                    if (debugCleanroom) {
                        GT_Log.out.println(
                                "BasicMachine: Voiding output due to efficiency failure. mEfficiency = " +
                                        getCallbackBase().mEfficiency
                        );
                    }
                    mOutputItems[i] = null;
                }
        mOutputFluid = tRecipe.getFluidOutput(0);
        if (!skipOC) {
            calculateOverclockedNess(tRecipe);
            //In case recipe is too OP for that machine
            if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        } else {
            mEUt = tRecipe.mEUt;
            mMaxProgresstime = tRecipe.mDuration;
        }
        mEUt *= getEUTModifier();
        mMaxProgresstime *= getTimeModifier();
        return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
    }

    public float getTimeModifier() {
        return 1;
    }

    public float getEUTModifier() {
        return 1;
    }

    @Override
    protected boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, aSide, aStack) && getRecipeList().containsInput( aStack);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        // Super already zeroed out setErrorDisplayID, add additional error codes here.
        aBaseMetaTileEntity.setErrorDisplayID(aBaseMetaTileEntity.getErrorDisplayID() | (mNeedsSteamVenting ? NEEDS_STEAM_VENTING : 0));
    }

    @Override
    public void endProcess() {
        if (isSteampowered()) mNeedsSteamVenting = true;
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        super.doSound(aIndex, aX, aY, aZ);
        if (aIndex == 9) {
            GT_Utility.doSoundAtClient(GregTech_API.sSoundList.get(225), 5, 0.5F, aX, aY, aZ);

            new WorldSpawnedEventBuilder.ParticleEventBuilder()
                    .setIdentifier("largesmoke")
                    .setWorld(getBaseMetaTileEntity().getWorld())
                    .setMotion(
                            ForgeDirection.getOrientation(getBaseMetaTileEntity().getFrontFacing()).offsetX / 5.0,
                            ForgeDirection.getOrientation(getBaseMetaTileEntity().getFrontFacing()).offsetY / 5.0,
                            ForgeDirection.getOrientation(getBaseMetaTileEntity().getFrontFacing()).offsetZ / 5.0
                    )
                    .<WorldSpawnedEventBuilder.ParticleEventBuilder>times(8, x -> x
                            .setPosition(
                                    aX - 0.5 + XSTR_INSTANCE.nextFloat(),
                                    aY - 0.5 + XSTR_INSTANCE.nextFloat(),
                                    aZ - 0.5 + XSTR_INSTANCE.nextFloat()
                            ).run()
                    );
        }
    }

    @Override
    public boolean isGivingInformation() {
        return false;
    }

    @Override
    public boolean allowCoverOnSide(byte aSide, GT_ItemStack aCoverID) {
        return GregTech_API.getCoverBehaviorNew(aCoverID.toStack()).isSimpleCover() && super.allowCoverOnSide(aSide, aCoverID);
    }

    public float getSteamDamage() {
        return 2.0F;
    }

    @Override
    public ITexture[] getSideFacingActive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_BRONZEBRICKS_SIDE : MACHINE_BRONZE_SIDE, Dyes.getModulation(aColor, Dyes._NULL.mRGBa))};
    }

    @Override
    public ITexture[] getSideFacingInactive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_BRONZEBRICKS_SIDE : MACHINE_BRONZE_SIDE, Dyes.getModulation(aColor, Dyes._NULL.mRGBa))};
    }

    @Override
    public ITexture[] getFrontFacingActive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_BRONZEBRICKS_SIDE : MACHINE_BRONZE_SIDE, Dyes.getModulation(aColor, Dyes._NULL.mRGBa))};
    }

    @Override
    public ITexture[] getFrontFacingInactive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_BRONZEBRICKS_SIDE : MACHINE_BRONZE_SIDE, Dyes.getModulation(aColor, Dyes._NULL.mRGBa))};
    }

    @Override
    public ITexture[] getTopFacingActive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_BRONZEBRICKS_TOP : MACHINE_BRONZE_TOP, Dyes.getModulation(aColor, Dyes._NULL.mRGBa))};
    }

    @Override
    public ITexture[] getTopFacingInactive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_BRONZEBRICKS_TOP : MACHINE_BRONZE_TOP, Dyes.getModulation(aColor, Dyes._NULL.mRGBa))};
    }

    @Override
    public ITexture[] getBottomFacingActive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_BRONZEBRICKS_BOTTOM : MACHINE_BRONZE_BOTTOM, Dyes.getModulation(aColor, Dyes._NULL.mRGBa))};
    }

    @Override
    public ITexture[] getBottomFacingInactive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_BRONZEBRICKS_BOTTOM : MACHINE_BRONZE_BOTTOM, Dyes.getModulation(aColor, Dyes._NULL.mRGBa))};
    }

    @Override
    public ITexture[] getBottomFacingPipeActive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_BRONZEBRICKS_BOTTOM : MACHINE_BRONZE_BOTTOM, Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(OVERLAY_STEAM_OUT)};
    }

    @Override
    public ITexture[] getBottomFacingPipeInactive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_BRONZEBRICKS_BOTTOM : MACHINE_BRONZE_BOTTOM, Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(OVERLAY_STEAM_OUT)};
    }

    @Override
    public ITexture[] getTopFacingPipeActive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_BRONZEBRICKS_TOP : MACHINE_BRONZE_TOP, Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(OVERLAY_STEAM_OUT)};
    }

    @Override
    public ITexture[] getTopFacingPipeInactive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_BRONZEBRICKS_TOP : MACHINE_BRONZE_TOP, Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(OVERLAY_STEAM_OUT)};
    }

    @Override
    public ITexture[] getSideFacingPipeActive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_BRONZEBRICKS_SIDE : MACHINE_BRONZE_SIDE, Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(OVERLAY_STEAM_OUT)};
    }

    @Override
    public ITexture[] getSideFacingPipeInactive(byte aColor) {
        return new ITexture[]{TextureFactory.of(mTier == 1 ? MACHINE_BRONZEBRICKS_SIDE : MACHINE_BRONZE_SIDE, Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(OVERLAY_STEAM_OUT)};
    }
}
