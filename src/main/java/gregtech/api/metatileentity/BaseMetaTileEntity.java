package gregtech.api.metatileentity;

import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.IAlignmentProvider;
import com.gtnewhorizon.structurelib.alignment.constructable.ICallbackable;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructableProvider;
import com.gtnewhorizon.structurelib.alignment.constructable.ICallbackableProvider;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import cpw.mods.fml.common.Optional;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.graphs.GenerateNodeMap;
import gregtech.api.graphs.GenerateNodeMapPower;
import gregtech.api.graphs.Node;
import gregtech.api.interfaces.IRedstoneSensitive;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.net.GT_Packet_TileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.*;
import gregtech.common.GT_Client;
import gregtech.common.GT_Pollution;
import ic2.api.Direction;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.GT_Values.NW;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.graphs.GenerateNodeMapPower.POWER_GENERATION;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the main TileEntity for EVERYTHING.
 */
@Optional.InterfaceList(value = {
    @Optional.Interface(iface = "appeng.api.networking.security.IActionHost", modid = "appliedenergistics2", striprefs = true),
    @Optional.Interface(iface = "appeng.me.helpers.IGridProxyable", modid = "appliedenergistics2", striprefs = true)})
public class BaseMetaTileEntity extends BaseTileEntity implements IGregTechTileEntity, IActionHost, IGridProxyable, IAlignmentProvider, IConstructableProvider, ICallbackableProvider {
    static final String[] COVER_DATA_NBT_KEYS = Arrays.stream(ForgeDirection.VALID_DIRECTIONS).mapToInt(Enum::ordinal).mapToObj(i -> "mCoverData" + i).toArray(String[]::new);
    private final GT_CoverBehaviorBase<?>[] mCoverBehaviors = new GT_CoverBehaviorBase<?>[]{GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior};
    protected MetaTileEntity mMetaTileEntity;
    protected long mStoredEnergy = 0, mStoredSteam = 0;
    protected int mAverageEUInputIndex = 0, mAverageEUOutputIndex = 0;
    protected boolean mReleaseEnergy = false;
    protected long[] mAverageEUInput = new long[]{0, 0, 0, 0, 0}, mAverageEUOutput = new long[]{0, 0, 0, 0, 0};
    private boolean[] mActiveEUInputs = new boolean[]{false, false, false, false, false, false}, mActiveEUOutputs = new boolean[]{false, false, false, false, false, false};
    private byte[] mSidedRedstone = new byte[]{15, 15, 15, 15, 15, 15};
    private int[] mCoverSides = new int[]{0, 0, 0, 0, 0, 0}, mTimeStatistics = new int[GregTech_API.TICKS_FOR_LAG_AVERAGING];
    private ISerializableObject[] mCoverData = new ISerializableObject[6];
    private boolean mHasEnoughEnergy = true, mRunningThroughTick = false, mInputDisabled = false, mOutputDisabled = false, mMuffler = false, mLockUpgrade = false, mActive = false, mRedstone = false, mWorkUpdate = false, mSteamConverter = false, mInventoryChanged = false, mWorks = true, mNeedsUpdate = true, mNeedsBlockUpdate = true, mSendClientData = false, oRedstone = false;
    private byte mColor = 0, oColor = 0, oStrongRedstone = 0, mStrongRedstone = 0, oRedstoneData = 63, oTextureData = 0, oUpdateData = 0, oTexturePage=0, oLightValueClient = -1, oLightValue = -1, mLightValue = 0, mOtherUpgrades = 0, mFacing = 0, oFacing = 0, mWorkData = 0;
    private int mDisplayErrorCode = 0, oX = 0, oY = 0, oZ = 0, mTimeStatisticsIndex = 0, mLagWarningCount = 0;
    private short mID = 0;
    public long mTickTimer = 0;
    private long oOutput = 0, mAcceptedAmperes = Long.MAX_VALUE;
    public long mLastSoundTick = 0;
    private long mLastCheckTick = 0;
    private String mOwnerName = "";
    private UUID mOwnerUuid = GT_Utility.defaultUuid;
    private NBTTagCompound mRecipeStuff = new NBTTagCompound();
    private int cableUpdateDelay = 10;

    public boolean mWasShutdown = false;

    public boolean mWasNotified = false;

    private static final Field ENTITY_ITEM_HEALTH_FIELD;
    static
    {
        Field f = null;

        try {
            f = EntityItem.class.getDeclaredField("field_70291_e");
            f.setAccessible(true);
        } catch (Exception e1) {
            try {
                f = EntityItem.class.getDeclaredField("health");
                f.setAccessible(true);
            } catch (Exception e2) {
                e1.printStackTrace();
                e2.printStackTrace();
            }
        }
        ENTITY_ITEM_HEALTH_FIELD = f;
    }

    public BaseMetaTileEntity() {
    }

    @Override
    public void writeToNBT(NBTTagCompound aNBT) {
        super.writeToNBT(aNBT);
        aNBT.setInteger("mID", mID);
        aNBT.setLong("mStoredSteam", mStoredSteam);
        aNBT.setLong("mStoredEnergy", mStoredEnergy);
        for (int i = 0; i < mCoverData.length; i++) {
            if (mCoverSides[i] != 0 && mCoverData[i] != null)
                aNBT.setTag(COVER_DATA_NBT_KEYS[i], mCoverData[i].saveDataToNBT());
        }
        aNBT.setIntArray("mCoverSides", mCoverSides);
        aNBT.setByteArray("mRedstoneSided", mSidedRedstone);
        aNBT.setByte("mColor", mColor);
        aNBT.setByte("mLightValue", mLightValue);
        aNBT.setByte("mOtherUpgrades", mOtherUpgrades);
        aNBT.setByte("mWorkData", mWorkData);
        aNBT.setByte("mStrongRedstone", mStrongRedstone);
        aNBT.setShort("mFacing", mFacing);
        aNBT.setString("mOwnerName", mOwnerName);
        aNBT.setString("mOwnerUuid", mOwnerUuid == null ? "" : mOwnerUuid.toString());
        aNBT.setBoolean("mLockUpgrade", mLockUpgrade);
        aNBT.setBoolean("mMuffler", mMuffler);
        aNBT.setBoolean("mSteamConverter", mSteamConverter);
        aNBT.setBoolean("mActive", mActive);
        aNBT.setBoolean("mRedstone", mRedstone);
        aNBT.setBoolean("mWorks", !mWorks);
        aNBT.setBoolean("mInputDisabled", mInputDisabled);
        aNBT.setBoolean("mOutputDisabled", mOutputDisabled);
        aNBT.setTag("GT.CraftingComponents", mRecipeStuff);
        aNBT.setInteger("nbtVersion", GT_Mod.TOTAL_VERSION);
        if (hasValidMetaTileEntity()) {
            NBTTagList tItemList = new NBTTagList();
            for (int i = 0; i < mMetaTileEntity.getRealInventory().length; i++) {
                ItemStack tStack = mMetaTileEntity.getRealInventory()[i];
                if (tStack != null) {
                    NBTTagCompound tTag = new NBTTagCompound();
                    tTag.setInteger("IntSlot", i);
                    tStack.writeToNBT(tTag);
                    tItemList.appendTag(tTag);
                }
            }
            aNBT.setTag("Inventory", tItemList);
            mMetaTileEntity.saveNBTData(aNBT);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound aNBT) {
        super.readFromNBT(aNBT);
        setInitialValuesAsNBT(aNBT, (short) 0);
    }

    @Override
    public void setInitialValuesAsNBT(NBTTagCompound aNBT, short aID) {
        if (aNBT == null) {
            if (aID > 0) mID = aID;
            else mID = mID > 0 ? mID : 0;
            if (mID != 0) createNewMetatileEntity(mID);
            mSidedRedstone = (hasValidMetaTileEntity() && mMetaTileEntity.hasSidedRedstoneOutputBehavior() ? new byte[]{0, 0, 0, 0, 0, 0} : new byte[]{15, 15, 15, 15, 15, 15});
        } else {
            if (aID <= 0) mID = (short) aNBT.getInteger("mID");
            else mID = aID;
            mStoredSteam = aNBT.getLong("mStoredSteam");
            mStoredEnergy = aNBT.getLong("mStoredEnergy");
            mColor = aNBT.getByte("mColor");
            mLightValue = aNBT.getByte("mLightValue");
            mWorkData = aNBT.getByte("mWorkData");
            mStrongRedstone = aNBT.getByte("mStrongRedstone");
            mFacing = oFacing = (byte) aNBT.getShort("mFacing");
            mOwnerName = aNBT.getString("mOwnerName");
            try {
                mOwnerUuid = UUID.fromString(aNBT.getString("mOwnerUuid"));
            } catch (IllegalArgumentException e){
                mOwnerUuid = null;
            }
            mLockUpgrade = aNBT.getBoolean("mLockUpgrade");
            mMuffler = aNBT.getBoolean("mMuffler");
            mSteamConverter = aNBT.getBoolean("mSteamConverter");
            mActive = aNBT.getBoolean("mActive");
            mRedstone = aNBT.getBoolean("mRedstone");
            mWorks = !aNBT.getBoolean("mWorks");
            mInputDisabled = aNBT.getBoolean("mInputDisabled");
            mOutputDisabled = aNBT.getBoolean("mOutputDisabled");
            mOtherUpgrades = (byte) (aNBT.getByte("mOtherUpgrades") + aNBT.getByte("mBatteries") + aNBT.getByte("mLiBatteries"));
            mCoverSides = aNBT.getIntArray("mCoverSides");
            mSidedRedstone = aNBT.getByteArray("mRedstoneSided");
            mRecipeStuff = aNBT.getCompoundTag("GT.CraftingComponents");
            int nbtVersion = aNBT.getInteger("nbtVersion");

            if (mCoverSides.length != 6) mCoverSides = new int[]{0, 0, 0, 0, 0, 0};
            if (mSidedRedstone.length != 6)
                if (hasValidMetaTileEntity() && mMetaTileEntity.hasSidedRedstoneOutputBehavior())
                    mSidedRedstone = new byte[]{0, 0, 0, 0, 0, 0};
                else mSidedRedstone = new byte[]{15, 15, 15, 15, 15, 15};

            for (byte i = 0; i < 6; i++) mCoverBehaviors[i] = GregTech_API.getCoverBehaviorNew(mCoverSides[i]);

            // check legacy data
            mCoverData = new ISerializableObject[6];
            int[] tOldData = aNBT.getIntArray("mCoverData");
            if (aNBT.hasKey("mCoverData", 11) && tOldData.length == 6) {
                for (int i = 0; i < tOldData.length; i++) {
                    if (mCoverBehaviors[i] != null)
                        mCoverData[i] = mCoverBehaviors[i].createDataObject(tOldData[i]);
                }
            } else {
                // no old data
                for (byte i = 0; i<6; i++) {
                    if (mCoverBehaviors[i] == null)
                        continue;
                    if (aNBT.hasKey(COVER_DATA_NBT_KEYS[i]))
                        mCoverData[i] = mCoverBehaviors[i].createDataObject(aNBT.getTag(COVER_DATA_NBT_KEYS[i]));
                    else
                        mCoverData[i] = mCoverBehaviors[i].createDataObject();
                }
            }

            if (mID != 0 && createNewMetatileEntity(mID)) {
                NBTTagList tItemList = aNBT.getTagList("Inventory", 10);
                for (int i = 0; i < tItemList.tagCount(); i++) {
                    NBTTagCompound tTag = tItemList.getCompoundTagAt(i);
                    int tSlot = tTag.getInteger("IntSlot");
                    tSlot = migrateInventoryIndex(tSlot, nbtVersion);
                    if (tSlot >= 0 && tSlot < mMetaTileEntity.getRealInventory().length) {
                        mMetaTileEntity.getRealInventory()[tSlot] = GT_Utility.loadItem(tTag);
                    }
                }

                mMetaTileEntity.loadNBTData(aNBT);
            }
        }

        if (mCoverData == null || mCoverData.length != 6) mCoverData = new ISerializableObject[6];
        if (mCoverSides.length != 6) mCoverSides = new int[]{0, 0, 0, 0, 0, 0};
        if (mSidedRedstone.length != 6)
            if (hasValidMetaTileEntity() && mMetaTileEntity.hasSidedRedstoneOutputBehavior())
                mSidedRedstone = new byte[]{0, 0, 0, 0, 0, 0};
            else mSidedRedstone = new byte[]{15, 15, 15, 15, 15, 15};

        for (byte i = 0; i < 6; i++) mCoverBehaviors[i] = GregTech_API.getCoverBehaviorNew(mCoverSides[i]);
    }

    private boolean createNewMetatileEntity(short aID) {
        if (aID <= 0 || aID >= GregTech_API.METATILEENTITIES.length || GregTech_API.METATILEENTITIES[aID] == null) {
            GT_Log.err.println("MetaID " + aID + " not loadable => locking TileEntity!");
        } else {
            if (hasValidMetaTileEntity()) mMetaTileEntity.setBaseMetaTileEntity(null);
            GregTech_API.METATILEENTITIES[aID].newMetaEntity(this).setBaseMetaTileEntity(this);
            mTickTimer = 0;
            mID = aID;
            return true;
        }
        return false;
    }

    /**
     * Used for ticking special BaseMetaTileEntities, which need that for Energy Conversion
     * It's called right before onPostTick()
     */
    public void updateStatus() {
        //
    }

    /**
     * Called when trying to charge Items
     */
    public void chargeItem(ItemStack aStack) {
        decreaseStoredEU(GT_ModHandler.chargeElectricItem(aStack, (int) Math.min(Integer.MAX_VALUE, getStoredEU()), (int) Math.min(Integer.MAX_VALUE, mMetaTileEntity.getOutputTier()), false, false), true);
    }

    /**
     * Called when trying to discharge Items
     */
    public void dischargeItem(ItemStack aStack) {
        increaseStoredEnergyUnits(GT_ModHandler.dischargeElectricItem(aStack, (int) Math.min(Integer.MAX_VALUE, getEUCapacity() - getStoredEU()), (int) Math.min(Integer.MAX_VALUE, mMetaTileEntity.getInputTier()), false, false, false), true);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (!hasValidMetaTileEntity()) {
            if (mMetaTileEntity == null) return;
            mMetaTileEntity.setBaseMetaTileEntity(this);
        }

        mRunningThroughTick = true;
        long tTime = System.nanoTime();
        int tCode = 0;
        boolean aSideServer = isServerSide();
        boolean aSideClient = isClientSide();


        if (getMetaTileEntity() instanceof IRedstoneSensitive) {
            final IRedstoneSensitive rs = (IRedstoneSensitive) getMetaTileEntity();
            if (mTickTimer % rs.rsTickRate() == 0 && (aSideServer || (aSideClient && rs.receiveRSClientUpdates()))) {
                for (byte i = 0; i < 6; i++) {
                    rs.updateRSValues(i, getInputRedstoneSignal(i));
                }
                rs.processRS();
            }
        }

        try { for (tCode = 0; hasValidMetaTileEntity() && tCode >= 0;) {
            switch (tCode) {
                case 0:
                    tCode++;
                    if (mTickTimer++ == 0) {
                        oX = xCoord;
                        oY = yCoord;
                        oZ = zCoord;
                        if (aSideServer) {
                            for (byte i = 0; i < 6; i++) {
                                if (getCoverIDAtSide(i) != 0) {
                                    if (!mMetaTileEntity.allowCoverOnSide(i, new GT_ItemStack(getCoverIDAtSide(i)))) {
                                        dropCover(i, i, true);
                                    }
                                }
                            }
                        }
                        worldObj.markTileEntityChunkModified(xCoord, yCoord, zCoord, this);
                        mMetaTileEntity.onFirstTick(this);
                        if (!hasValidMetaTileEntity()) {
                            mRunningThroughTick = false;
                            return;
                        }
                    }
                case 1:
                    tCode++;
                    if (aSideClient) {
                        if (mColor != oColor) {
                            mMetaTileEntity.onColorChangeClient(oColor = mColor);
                            issueTextureUpdate();
                        }

                        if (mLightValue != oLightValueClient) {
                            worldObj.setLightValue(EnumSkyBlock.Block, xCoord, yCoord, zCoord, mLightValue);
                            worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
                            worldObj.updateLightByType(EnumSkyBlock.Block, xCoord + 1, yCoord, zCoord);
                            worldObj.updateLightByType(EnumSkyBlock.Block, xCoord - 1, yCoord, zCoord);
                            worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord + 1, zCoord);
                            worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord - 1, zCoord);
                            worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord + 1);
                            worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord - 1);
                            oLightValueClient = mLightValue;
                            issueTextureUpdate();
                        }

                        if (mNeedsUpdate) {
                            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                            //worldObj.func_147479_m(xCoord, yCoord, zCoord);
                            mNeedsUpdate = false;
                        }
                    }
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    if (aSideServer && mTickTimer > 10) {
                        for (byte i = (byte) (tCode - 2); i < 6; i++)
                            if (getCoverIDAtSide(i) != 0) {
                                tCode++;
                                GT_CoverBehaviorBase<?> tCover = getCoverBehaviorAtSideNew(i);
                                int tCoverTickRate = tCover.getTickRate(i, getCoverIDAtSide(i), mCoverData[i], this);
                                if (tCoverTickRate > 0 && mTickTimer % tCoverTickRate == 0) {
                                    byte tRedstone = tCover.isRedstoneSensitive(i, getCoverIDAtSide(i), mCoverData[i], this, mTickTimer) ? getInputRedstoneSignal(i) : 0;
                                    mCoverData[i] = tCover.doCoverThings(i, tRedstone, getCoverIDAtSide(i), mCoverData[i], this, mTickTimer);
                                    if (!hasValidMetaTileEntity()) {
                                        mRunningThroughTick = false;
                                        return;
                                    }
                                }
                            }

                    }
                case 8:
                    tCode = 9;
                    if (aSideServer) {
                        if (++mAverageEUInputIndex >= mAverageEUInput.length) mAverageEUInputIndex = 0;
                        if (++mAverageEUOutputIndex >= mAverageEUOutput.length) mAverageEUOutputIndex = 0;

                        mAverageEUInput[mAverageEUInputIndex] = 0;
                        mAverageEUOutput[mAverageEUOutputIndex] = 0;
                    }
                case 9:
                    tCode++;
                    mMetaTileEntity.onPreTick(this, mTickTimer);
                    if (!hasValidMetaTileEntity()) {
                        mRunningThroughTick = false;
                        return;
                    }
                case 10:
                    tCode++;
                    if (aSideServer) {
                        if (mRedstone != oRedstone || mTickTimer == 10) {
                            for (byte i = 0; i < 6; i++)
                                mCoverBehaviors[i] = GregTech_API.getCoverBehaviorNew(mCoverSides[i]);
                            oRedstone = mRedstone;
                            issueBlockUpdate();
                        }
                        if(mTickTimer == 10) joinEnet();

                        if (xCoord != oX || yCoord != oY || zCoord != oZ) {
                            oX = xCoord;
                            oY = yCoord;
                            oZ = zCoord;
                            issueClientUpdate();
                            clearTileEntityBuffer();
                        }

                        if (mFacing != oFacing) {
                            oFacing = mFacing;
                            for (byte i = 0; i < 6; i++)
                                if (getCoverIDAtSide(i) != 0)
                                    if (!mMetaTileEntity.allowCoverOnSide(i, new GT_ItemStack(getCoverIDAtSide(i))))
                                        dropCover(i, i, true);
                            issueBlockUpdate();
                        }

                        if (mTickTimer > 20 && mMetaTileEntity.isElectric()) {
                            mAcceptedAmperes = 0;

                            if (getOutputVoltage() != oOutput) {
                                oOutput = getOutputVoltage();
                            }

                            if (mMetaTileEntity.isEnetOutput() || mMetaTileEntity.isEnetInput()) {
                                for (byte i = 0; i < 6; i++) {
                                    boolean temp = isEnergyInputSide(i);
                                    if (temp != mActiveEUInputs[i]) {
                                        mActiveEUInputs[i] = temp;
                                    }
                                    temp = isEnergyOutputSide(i);
                                    if (temp != mActiveEUOutputs[i]) {
                                        mActiveEUOutputs[i] = temp;
                                    }
                                }
                            }


                            if (mMetaTileEntity.isEnetOutput() && oOutput > 0) {
                                long tOutputVoltage = Math.max(oOutput, oOutput + (1 << GT_Utility.getTier(oOutput))), tUsableAmperage = Math.min(getOutputAmperage(), (getStoredEU() - mMetaTileEntity.getMinimumStoredEU()) / tOutputVoltage);
                                if (tUsableAmperage > 0) {
                                    long tEU = tOutputVoltage * IEnergyConnected.Util.emitEnergyToNetwork(oOutput, tUsableAmperage, this);
                                    mAverageEUOutput[mAverageEUOutputIndex] += tEU;
                                    decreaseStoredEU(tEU, true);
                                }
                            }
                            if (getEUCapacity() > 0) {
                                if (GregTech_API.sMachineFireExplosions && getRandomNumber(1000) == 0) {
                                    Block tBlock = getBlockAtSide((byte) getRandomNumber(6));
                                    if (tBlock instanceof BlockFire) doEnergyExplosion();
                                }

                                if (!hasValidMetaTileEntity()) {
                                    mRunningThroughTick = false;
                                    return;
                                }

                                if (getRandomNumber(1000) == 0) {
				    int precipitationHeightAtSide2 = worldObj.getPrecipitationHeight(xCoord, zCoord - 1);
                                    int precipitationHeightAtSide3 = worldObj.getPrecipitationHeight(xCoord, zCoord + 1);
                                    int precipitationHeightAtSide4 = worldObj.getPrecipitationHeight(xCoord - 1, zCoord);
                                    int precipitationHeightAtSide5 = worldObj.getPrecipitationHeight(xCoord + 1, zCoord);

                                    if ((getCoverIDAtSide((byte) 1) == 0 && worldObj.getPrecipitationHeight(xCoord, zCoord) - 2 < yCoord)
                                            || (getCoverIDAtSide((byte) 2) == 0 && precipitationHeightAtSide2 - 1 < yCoord && precipitationHeightAtSide2 > -1)
                                            || (getCoverIDAtSide((byte) 3) == 0 && precipitationHeightAtSide3 - 1 < yCoord && precipitationHeightAtSide3 > -1)
                                            || (getCoverIDAtSide((byte) 4) == 0 && precipitationHeightAtSide4 - 1 < yCoord && precipitationHeightAtSide4 > -1)
                                            || (getCoverIDAtSide((byte) 5) == 0 && precipitationHeightAtSide5 - 1 < yCoord && precipitationHeightAtSide5 > -1)) {
                                        if (GregTech_API.sMachineRainExplosions && worldObj.isRaining() && getBiome().rainfall > 0) {
                                            if (getRandomNumber(10) == 0) {
                                                try{
                                                    GT_Mod.instance.achievements.issueAchievement(this.getWorldObj().getPlayerEntityByName(mOwnerName), "badweather");
                                                }catch(Exception e){

                                                }
                                                GT_Log.exp.println("Machine at: "+ this.getXCoord()+" | "+ this.getYCoord()+" | "+ this.getZCoord()+" DIMID: " +this.worldObj.provider.dimensionId +" explosion due to rain!");
                                                doEnergyExplosion();
                                            } else {
                                                GT_Log.exp.println("Machine at: "+ this.getXCoord()+" | "+ this.getYCoord()+" | "+ this.getZCoord()+" DIMID: " +this.worldObj.provider.dimensionId +"  set to Fire due to rain!");
                                                setOnFire();
                                            }
                                        }
                                        if (!hasValidMetaTileEntity()) {
                                            mRunningThroughTick = false;
                                            return;
                                        }
                                    }
                                }
                            }
                        }

                        if (!hasValidMetaTileEntity()) {
                            mRunningThroughTick = false;
                            return;
                        }
                    }
                case 11:
                    tCode++;
                    if (aSideServer) {
                        if (mMetaTileEntity.dechargerSlotCount() > 0 && getStoredEU() < getEUCapacity()) {
                            for (int i = mMetaTileEntity.dechargerSlotStartIndex(), k = mMetaTileEntity.dechargerSlotCount() + i; i < k; i++) {
                                if (mMetaTileEntity.mInventory[i] != null && getStoredEU() < getEUCapacity()) {
                                    dischargeItem(mMetaTileEntity.mInventory[i]);
				                    if(ic2.api.info.Info.itemEnergy.getEnergyValue(mMetaTileEntity.mInventory[i])>0){
                                       if((getStoredEU() + ic2.api.info.Info.itemEnergy.getEnergyValue(mMetaTileEntity.mInventory[i]))<getEUCapacity()){
                                           increaseStoredEnergyUnits((long)ic2.api.info.Info.itemEnergy.getEnergyValue(mMetaTileEntity.mInventory[i]),false);
                                           mMetaTileEntity.mInventory[i].stackSize--;
                                           mInventoryChanged = true;
                                       }
                                    }
                                    if (mMetaTileEntity.mInventory[i].stackSize <= 0) {
                                        mMetaTileEntity.mInventory[i] = null;
                                        mInventoryChanged = true;
                                    }
                                }
                            }
                        }
                    }
                case 12:
                    tCode++;
                    if (aSideServer) {
                        if (mMetaTileEntity.rechargerSlotCount() > 0 && getStoredEU() > 0) {
                            for (int i = mMetaTileEntity.rechargerSlotStartIndex(), k = mMetaTileEntity.rechargerSlotCount() + i; i < k; i++) {
                                if (getStoredEU() > 0 && mMetaTileEntity.mInventory[i] != null) {
                                    chargeItem(mMetaTileEntity.mInventory[i]);
                                    if (mMetaTileEntity.mInventory[i].stackSize <= 0) {
                                        mMetaTileEntity.mInventory[i] = null;
                                        mInventoryChanged = true;
                                    }
                                }
                            }
                        }
                    }
                case 13:
                    tCode++;
                    updateStatus();
                    if (!hasValidMetaTileEntity()) {
                        mRunningThroughTick = false;
                        return;
                    }
                case 14:
                    tCode++;
                    mMetaTileEntity.onPostTick(this, mTickTimer);
                    if (!hasValidMetaTileEntity()) {
                        mRunningThroughTick = false;
                        return;
                    }
                case 15:
                    tCode++;
                    if (aSideServer) {
                        if (mTickTimer > 5 && cableUpdateDelay == 0) {
                            generatePowerNodes();
                            cableUpdateDelay--;
                        } else {
                            cableUpdateDelay--;
                        }
                        if (mTickTimer % 10 == 0) {
                            if (mSendClientData) {
                                NW.sendPacketToAllPlayersInRange(worldObj,
                                        new GT_Packet_TileEntity(xCoord, (short) yCoord, zCoord, mID,
                                                mCoverSides[0], mCoverSides[1], mCoverSides[2], mCoverSides[3], mCoverSides[4], mCoverSides[5],
                                                oTextureData = (byte) ((mFacing & 7) | (mActive ? 8 : 0) | (mRedstone ? 16 : 0) | (mLockUpgrade ? 32 : 0) | (mWorks ? 64 : 0) ),
                                                oTexturePage = (hasValidMetaTileEntity() && mMetaTileEntity instanceof GT_MetaTileEntity_Hatch) ? ((GT_MetaTileEntity_Hatch) mMetaTileEntity).getTexturePage() : 0,
                                                oUpdateData = hasValidMetaTileEntity() ? mMetaTileEntity.getUpdateData() : 0,
                                                oRedstoneData = (byte) (((mSidedRedstone[0] > 0) ? 1 : 0) | ((mSidedRedstone[1] > 0) ? 2 : 0) | ((mSidedRedstone[2] > 0) ? 4 : 0) | ((mSidedRedstone[3] > 0) ? 8 : 0) | ((mSidedRedstone[4] > 0) ? 16 : 0) | ((mSidedRedstone[5] > 0) ? 32 : 0)),
                                                oColor = mColor),
                                        xCoord, zCoord);
                                mSendClientData = false;
                            }
                        }

                        if (mTickTimer > 10) {
                            byte tData = (byte) ((mFacing & 7) | (mActive ? 8 : 0) | (mRedstone ? 16 : 0) | (mLockUpgrade ? 32 : 0)| (mWorks ? 64 : 0));
                            if (tData != oTextureData)
                                sendBlockEvent(ClientEvents.CHANGE_COMMON_DATA, oTextureData = tData);

                            tData = mMetaTileEntity.getUpdateData();
                            if (tData != oUpdateData)
                                sendBlockEvent(ClientEvents.CHANGE_CUSTOM_DATA, oUpdateData = tData);
                            if (mMetaTileEntity instanceof GT_MetaTileEntity_Hatch) {
                                tData = ((GT_MetaTileEntity_Hatch) mMetaTileEntity).getTexturePage();
                                if (tData != oTexturePage)
                                    sendBlockEvent(ClientEvents.CHANGE_CUSTOM_DATA, (byte) ((oTexturePage = tData) | 0x80));//set last bit as a flag for page
                            }
                            if (mColor != oColor) sendBlockEvent(ClientEvents.CHANGE_COLOR, oColor = mColor);
                            tData = (byte) (((mSidedRedstone[0] > 0) ? 1 : 0) | ((mSidedRedstone[1] > 0) ? 2 : 0) | ((mSidedRedstone[2] > 0) ? 4 : 0) | ((mSidedRedstone[3] > 0) ? 8 : 0) | ((mSidedRedstone[4] > 0) ? 16 : 0) | ((mSidedRedstone[5] > 0) ? 32 : 0));
                            if (tData != oRedstoneData)
                                sendBlockEvent(ClientEvents.CHANGE_REDSTONE_OUTPUT, oRedstoneData = tData);
                            if (mLightValue != oLightValue) {
                                worldObj.setLightValue(EnumSkyBlock.Block, xCoord, yCoord, zCoord, mLightValue);
                                worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
                                worldObj.updateLightByType(EnumSkyBlock.Block, xCoord + 1, yCoord, zCoord);
                                worldObj.updateLightByType(EnumSkyBlock.Block, xCoord - 1, yCoord, zCoord);
                                worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord + 1, zCoord);
                                worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord - 1, zCoord);
                                worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord + 1);
                                worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord - 1);
                                issueTextureUpdate();
                                sendBlockEvent(ClientEvents.CHANGE_LIGHT, oLightValue = mLightValue);
                            }
                        }

                        if (mNeedsBlockUpdate) {
                            updateNeighbours(mStrongRedstone, oStrongRedstone);
                            oStrongRedstone = mStrongRedstone;
                            mNeedsBlockUpdate = false;
                        }
                    }
                default:
                    tCode = -1;
            }
        }
        } catch (Throwable e) {
            //gregtech.api.util.GT_Log.err.println("Encountered Exception while ticking MetaTileEntity in Step " + (tCode - 1) + ". The Game should've crashed now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
            e.printStackTrace(GT_Log.err);
        }

        if (aSideServer && hasValidMetaTileEntity()) {
            tTime = System.nanoTime() - tTime;
            if (mTimeStatistics.length > 0)
                mTimeStatistics[mTimeStatisticsIndex = (mTimeStatisticsIndex + 1) % mTimeStatistics.length] = (int) tTime;
            if (tTime > 0 && tTime > (GregTech_API.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING*1000000) && mTickTimer > 1000 && getMetaTileEntity().doTickProfilingMessageDuringThisTick() && mLagWarningCount++ < 10)
                GT_FML_LOGGER.warn("WARNING: Possible Lag Source at [" + xCoord + ", " + yCoord + ", " + zCoord + "] in Dimension " + worldObj.provider.dimensionId + " with " + tTime + "ns caused by an instance of " + getMetaTileEntity().getClass());
        }

        mWorkUpdate = mInventoryChanged = mRunningThroughTick = false;
    }

    @Override
    public Packet getDescriptionPacket() {
        issueClientUpdate();
        return null;
    }

    public final void receiveMetaTileEntityData(short aID, int aCover0, int aCover1, int aCover2, int aCover3, int aCover4, int aCover5, byte aTextureData, byte aTexturePage, byte aUpdateData, byte aRedstoneData, byte aColorData) {
        issueTextureUpdate();
        if (mID != aID && aID > 0) {
            mID = aID;
            createNewMetatileEntity(mID);
        }

        setCoverIDAtSide((byte) 0, aCover0);
        setCoverIDAtSide((byte) 1, aCover1);
        setCoverIDAtSide((byte) 2, aCover2);
        setCoverIDAtSide((byte) 3, aCover3);
        setCoverIDAtSide((byte) 4, aCover4);
        setCoverIDAtSide((byte) 5, aCover5);

        receiveClientEvent(ClientEvents.CHANGE_COMMON_DATA, aTextureData);
        receiveClientEvent(ClientEvents.CHANGE_CUSTOM_DATA, aUpdateData & 0x7F);
        receiveClientEvent(ClientEvents.CHANGE_CUSTOM_DATA, aTexturePage | 0x80);
        receiveClientEvent(ClientEvents.CHANGE_COLOR, aColorData);
        receiveClientEvent(ClientEvents.CHANGE_REDSTONE_OUTPUT, aRedstoneData);
    }

    @Deprecated
    public final void receiveMetaTileEntityData(short aID, int aCover0, int aCover1, int aCover2, int aCover3, int aCover4, int aCover5, byte aTextureData, byte aUpdateData, byte aRedstoneData, byte aColorData) {
        issueTextureUpdate();
        if (mID != aID && aID > 0) {
            mID = aID;
            createNewMetatileEntity(mID);
        }

        mCoverSides[0] = aCover0;
        mCoverSides[1] = aCover1;
        mCoverSides[2] = aCover2;
        mCoverSides[3] = aCover3;
        mCoverSides[4] = aCover4;
        mCoverSides[5] = aCover5;

        for (byte i = 0; i < 6; i++) mCoverBehaviors[i] = GregTech_API.getCoverBehaviorNew(mCoverSides[i]);

        receiveClientEvent(ClientEvents.CHANGE_COMMON_DATA, aTextureData);
        receiveClientEvent(ClientEvents.CHANGE_CUSTOM_DATA, aUpdateData & 0x7F);
        receiveClientEvent(ClientEvents.CHANGE_CUSTOM_DATA, 0x80);
        receiveClientEvent(ClientEvents.CHANGE_COLOR, aColorData);
        receiveClientEvent(ClientEvents.CHANGE_REDSTONE_OUTPUT, aRedstoneData);
    }

    public static class ClientEvents {
        public static final byte CHANGE_COMMON_DATA = 0;
        public static final byte CHANGE_CUSTOM_DATA = 1;
        public static final byte CHANGE_COLOR = 2;
        public static final byte CHANGE_REDSTONE_OUTPUT = 3;
        public static final byte DO_SOUND = 4;
        public static final byte START_SOUND_LOOP = 5;
        public static final byte STOP_SOUND_LOOP = 6;
        public static final byte CHANGE_LIGHT = 7;
        public static final byte MISC_EVENT = 8;
    }

    @Override
    public boolean receiveClientEvent(int aEventID, int aValue) {
        super.receiveClientEvent(aEventID, aValue);

        if (hasValidMetaTileEntity()) {
            try {
                mMetaTileEntity.receiveClientEvent((byte) aEventID, (byte) aValue);
            } catch (Throwable e) {
                GT_Log.err.println("Encountered Exception while receiving Data from the Server, the Client should've been crashed by now, but I prevented that. Please report immediately to GregTech Intergalactical!!!");
                e.printStackTrace(GT_Log.err);
            }
        }

        if (isClientSide()) {
            issueTextureUpdate();
            switch (aEventID) {
                case ClientEvents.CHANGE_COMMON_DATA:
                    mFacing = (byte) (aValue & 7);
                    mActive = ((aValue & 8) != 0);
                    mRedstone = ((aValue & 16) != 0);
				    //mLockUpgrade	= ((aValue&32) != 0);
                    mWorks =  ((aValue & 64) != 0);
                    break;
                case ClientEvents.CHANGE_CUSTOM_DATA:
                    if (hasValidMetaTileEntity()) {
                        if ((aValue & 0x80) == 0) //Is texture index
                            mMetaTileEntity.onValueUpdate((byte) (aValue & 0x7F));
                        else if (mMetaTileEntity instanceof GT_MetaTileEntity_Hatch)//is texture page and hatch
                                ((GT_MetaTileEntity_Hatch) mMetaTileEntity).onTexturePageUpdate((byte) (aValue & 0x7F));
                    }
                    break;
                case ClientEvents.CHANGE_COLOR:
                    if (aValue > 16 || aValue < 0) aValue = 0;
                    mColor = (byte) aValue;
                    break;
                case ClientEvents.CHANGE_REDSTONE_OUTPUT:
                    mSidedRedstone[0] = (byte) ((aValue & 1) == 1 ? 15 : 0);
                    mSidedRedstone[1] = (byte) ((aValue & 2) == 2 ? 15 : 0);
                    mSidedRedstone[2] = (byte) ((aValue & 4) == 4 ? 15 : 0);
                    mSidedRedstone[3] = (byte) ((aValue & 8) == 8 ? 15 : 0);
                    mSidedRedstone[4] = (byte) ((aValue & 16) == 16 ? 15 : 0);
                    mSidedRedstone[5] = (byte) ((aValue & 32) == 32 ? 15 : 0);
                    break;
                case ClientEvents.DO_SOUND:
                    if (hasValidMetaTileEntity() && mTickTimer > 20)
                        mMetaTileEntity.doSound((byte) aValue, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                    break;
                case ClientEvents.START_SOUND_LOOP:
                    if (hasValidMetaTileEntity() && mTickTimer > 20)
                        mMetaTileEntity.startSoundLoop((byte) aValue, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                    break;
                case ClientEvents.STOP_SOUND_LOOP:
                    if (hasValidMetaTileEntity() && mTickTimer > 20)
                        mMetaTileEntity.stopSoundLoop((byte) aValue, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                    break;
                case ClientEvents.CHANGE_LIGHT:
                    mLightValue = (byte) aValue;
                    break;
                case ClientEvents.MISC_EVENT:
                    if (hasValidMetaTileEntity() && (mTickTimer > 20 || mMetaTileEntity.canReceiveImmediateEvents())) {
                        mMetaTileEntity.receiveMiscEvent((byte) aValue);
                    }
                    break;
            }
        }
        return true;
    }

    public ArrayList<String> getDebugInfo(EntityPlayer aPlayer, int aLogLevel) {
        ArrayList<String> tList = new ArrayList<String>();
        if (aLogLevel > 2) {
            tList.add("Meta-ID: " +EnumChatFormatting.BLUE+ mID +EnumChatFormatting.RESET + (canAccessData() ? EnumChatFormatting.GREEN+" valid"+EnumChatFormatting.RESET : EnumChatFormatting.RED+" invalid"+EnumChatFormatting.RESET) + (mMetaTileEntity == null ? EnumChatFormatting.RED+" MetaTileEntity == null!"+EnumChatFormatting.RESET : " "));
        }
        if (aLogLevel > 1) {
            if (mTimeStatistics.length > 0) {
                double tAverageTime = 0;
                double tWorstTime = 0;
                for (int tTime : mTimeStatistics) {
                    tAverageTime += tTime;
                    if (tTime > tWorstTime) {
                        tWorstTime = tTime;
                    }
                    // Uncomment this line to print out tick-by-tick times.
                    //tList.add("tTime " + tTime);
                }
                tList.add("Average CPU load of ~" + GT_Utility.formatNumbers(tAverageTime / mTimeStatistics.length) + "ns over " + GT_Utility.formatNumbers(mTimeStatistics.length) + " ticks with worst time of " + GT_Utility.formatNumbers(tWorstTime) + "ns.");
                tList.add("Recorded " + GT_Utility.formatNumbers(mMetaTileEntity.mSoundRequests) + " sound requests in " + GT_Utility.formatNumbers(mTickTimer - mLastCheckTick) + " ticks." );
                mLastCheckTick = mTickTimer;
                mMetaTileEntity.mSoundRequests = 0;
            }
            if (mLagWarningCount > 0) {
                tList.add("Caused " + (mLagWarningCount >= 10 ? "more than 10" : mLagWarningCount) + " Lag Spike Warnings (anything taking longer than " + GregTech_API.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING + "ms) on the Server.");
            }
            tList.add("Is" + (mMetaTileEntity.isAccessAllowed(aPlayer) ? " " : EnumChatFormatting.RED+" not "+EnumChatFormatting.RESET) + "accessible for you");
        }
        if (aLogLevel > 0) {
            if (getSteamCapacity() > 0 && hasSteamEngineUpgrade())
                tList.add(GT_Utility.formatNumbers(getStoredSteam()) + " of " + GT_Utility.formatNumbers(getSteamCapacity()) + " Steam");
            tList.add("Machine is " + (mActive ? EnumChatFormatting.GREEN+"active"+EnumChatFormatting.RESET : EnumChatFormatting.RED+"inactive"+EnumChatFormatting.RESET));
            if (!mHasEnoughEnergy)
                tList.add(EnumChatFormatting.RED+"ATTENTION: This Device needs more power."+EnumChatFormatting.RESET);
        }
        if(joinedIc2Enet)
            tList.add("Joined IC2 ENet");
        return mMetaTileEntity.getSpecialDebugInfo(this, aPlayer, aLogLevel, tList);
    }

    @Override
    public void issueTextureUpdate() {
        mNeedsUpdate = true;
    }

    @Override
    public void issueBlockUpdate() {
        mNeedsBlockUpdate = true;
    }

    @Override
    public void issueClientUpdate() {
        mSendClientData = true;
    }

    @Override
    public void issueCoverUpdate(byte aSide) {
        issueClientUpdate();
    }

    @Override
    public void receiveCoverData(byte coverSide, int coverID, int coverData) {
        if ((coverSide >= 0 && coverSide < 6) && (mCoverSides[coverSide] == coverID))
            setCoverDataAtSide(coverSide, coverData);
    }

    @Override
    public void receiveCoverData(byte aCoverSide, int aCoverID, ISerializableObject aCoverData, EntityPlayerMP aPlayer) {
        if ((aCoverSide >= 0 && aCoverSide < 6) && (mCoverSides[aCoverSide] == aCoverID))
            setCoverDataAtSide(aCoverSide, aCoverData);
    }

    @Override
    public byte getStrongestRedstone() {
        return (byte) Math.max(getInternalInputRedstoneSignal((byte) 0), Math.max(getInternalInputRedstoneSignal((byte) 1), Math.max(getInternalInputRedstoneSignal((byte) 2), Math.max(getInternalInputRedstoneSignal((byte) 3), Math.max(getInternalInputRedstoneSignal((byte) 4), getInternalInputRedstoneSignal((byte) 5))))));
    }

    @Override
    public boolean getRedstone() {
        return getRedstone((byte) 0) || getRedstone((byte) 1) || getRedstone((byte) 2) || getRedstone((byte) 3) || getRedstone((byte) 4) || getRedstone((byte) 5);
    }

    @Override
    public boolean getRedstone(byte aSide) {
        return getInternalInputRedstoneSignal(aSide) > 0;
    }

    public ITexture getCoverTexture(byte aSide) {
        if (getCoverIDAtSide(aSide) == 0) return null;
        if (GT_Mod.instance.isClientSide() && (GT_Client.hideValue & 0x1) != 0) {
            return BlockIcons.HIDDEN_TEXTURE[0]; // See through
        }
        return GregTech_API.sCovers.get(new GT_ItemStack(getCoverIDAtSide(aSide)));
    }

    @Override
    public boolean isGivingInformation() {
        if (canAccessData()) return mMetaTileEntity.isGivingInformation();
        return false;
    }

    @Override
    public boolean isValidFacing(byte aSide) {
        if (canAccessData()) return mMetaTileEntity.isFacingValid(aSide);
        return false;
    }

    @Override
    public byte getBackFacing() {
        return GT_Utility.getOppositeSide(mFacing);
    }

    @Override
    public byte getFrontFacing() {
        return mFacing;
    }

    @Override
    public void setFrontFacing(byte aFacing) {
        if (isValidFacing(aFacing)) {
            mFacing = aFacing;
            mMetaTileEntity.onFacingChange();

            doEnetUpdate();
            cableUpdateDelay = 10;

            if (mMetaTileEntity.shouldTriggerBlockUpdate()) {
                // If we're triggering a block update this will call onMachineBlockUpdate()
                GregTech_API.causeMachineUpdate(worldObj, xCoord, yCoord, zCoord);
            } else {
                // If we're not trigger a cascading one, call the update here.
                onMachineBlockUpdate();
            }
        }
    }

    @Override
    public int getSizeInventory() {
        if (canAccessData()) return mMetaTileEntity.getSizeInventory();
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int aIndex) {
        if (canAccessData()) return mMetaTileEntity.getStackInSlot(aIndex);
        return null;
    }

    @Override
    public void setInventorySlotContents(int aIndex, ItemStack aStack) {
        mInventoryChanged = true;
        if (canAccessData()) {
            markDirty();
            mMetaTileEntity.setInventorySlotContents(aIndex, worldObj.isRemote ? aStack : GT_OreDictUnificator.setStack(true, aStack));
        }
    }

    @Override
    public String getInventoryName() {
        if (canAccessData()) return mMetaTileEntity.getInventoryName();
        if (GregTech_API.METATILEENTITIES[mID] != null) return GregTech_API.METATILEENTITIES[mID].getInventoryName();
        return "";
    }

    @Override
    public int getInventoryStackLimit() {
        if (canAccessData()) return mMetaTileEntity.getInventoryStackLimit();
        return 64;
    }

    @Override
    public void openInventory() {
        if (canAccessData()) mMetaTileEntity.onOpenGUI();
    }

    @Override
    public void closeInventory() {
        if (canAccessData()) mMetaTileEntity.onCloseGUI();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer aPlayer) {
        return canAccessData() && playerOwnsThis(aPlayer, false) && mTickTimer > 4 && getTileEntityOffset(0, 0, 0) == this && aPlayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64 && mMetaTileEntity.isAccessAllowed(aPlayer);
    }

    @Override
    public void validate() {
        super.validate();
        mTickTimer = 0;
    }

    @Override
    public void invalidate() {
        tileEntityInvalid = false;
        leaveEnet();
        if (canAccessData()) {
            if (GregTech_API.mAE2)
                invalidateAE();
            mMetaTileEntity.onRemoval();
            mMetaTileEntity.setBaseMetaTileEntity(null);
        }
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        if (GregTech_API.mAE2)
            onChunkUnloadAE();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) setInventorySlotContents(slot, null);
        return stack;
    }

    /**
     * Checks validity of meta tile and delegates to it
     */
    @Override
    public void onMachineBlockUpdate() {
        if (canAccessData()) mMetaTileEntity.onMachineBlockUpdate();
        cableUpdateDelay = 10;
    }

    /**
     * Checks validity of meta tile and delegates to it
     */
    @Override
    public boolean isMachineBlockUpdateRecursive() {
        return canAccessData() && mMetaTileEntity.isMachineBlockUpdateRecursive();
    }

    @Override
    public int getProgress() {
        return canAccessData() ? mMetaTileEntity.getProgresstime() : 0;
    }

    @Override
    public int getMaxProgress() {
        return canAccessData() ? mMetaTileEntity.maxProgresstime() : 0;
    }

    @Override
    public boolean increaseProgress(int aProgressAmountInTicks) {
        return canAccessData() ? mMetaTileEntity.increaseProgress(aProgressAmountInTicks) != aProgressAmountInTicks : false;
    }

    @Override
    public boolean hasThingsToDo() {
        return getMaxProgress() > 0;
    }

    @Override
    public void enableWorking() {
        if (!mWorks) mWorkUpdate = true;
        mWorks = true;
        mWasShutdown = false;
    }

    @Override
    public void disableWorking() {
        mWorks = false;
    }

    @Override
    public boolean isAllowedToWork() {
        return mWorks;
    }

    @Override
    public boolean hasWorkJustBeenEnabled() {
        return mWorkUpdate;
    }

    @Override
    public byte getWorkDataValue() {
        return mWorkData;
    }

    @Override
    public void setWorkDataValue(byte aValue) {
        mWorkData = aValue;
    }

    @Override
    public int getMetaTileID() {
        return mID;
    }

    @Override
    public int setMetaTileID(short aID) {
        return mID = aID;
    }

    @Override
    public boolean isActive() {
        return mActive;
    }

    @Override
    public void setActive(boolean aActive) {
        mActive = aActive;
    }

    @Override
    public long getTimer() {
        return mTickTimer;
    }

    @Override
    public boolean decreaseStoredEnergyUnits(long aEnergy, boolean aIgnoreTooLessEnergy) {
        if (!canAccessData()) return false;
        return mHasEnoughEnergy = decreaseStoredEU(aEnergy, aIgnoreTooLessEnergy) || decreaseStoredSteam(aEnergy, false) || (aIgnoreTooLessEnergy && (decreaseStoredSteam(aEnergy, true)));
    }

    @Override
    public boolean increaseStoredEnergyUnits(long aEnergy, boolean aIgnoreTooMuchEnergy) {
        if (!canAccessData()) return false;
        if (getStoredEU() < getEUCapacity() || aIgnoreTooMuchEnergy) {
            setStoredEU(mMetaTileEntity.getEUVar() + aEnergy);
            return true;
        }
        return false;
    }

    @Override
    public boolean inputEnergyFrom(byte aSide) {
        return inputEnergyFrom(aSide, true);
    }

    @Override
    public boolean inputEnergyFrom(byte aSide, boolean waitForActive) {
        if (aSide == 6) return true;
        if (isServerSide() && waitForActive) return ((aSide >= 0 && aSide < 6) && mActiveEUInputs[aSide]) && !mReleaseEnergy;
        return isEnergyInputSide(aSide);
    }

    @Override
    public boolean outputsEnergyTo(byte aSide) {
        return outputsEnergyTo(aSide, true);
    }

    @Override
    public boolean outputsEnergyTo(byte aSide, boolean waitForActive) {
        if (aSide == 6) return true;
        if (isServerSide() && waitForActive) return ((aSide >= 0 && aSide < 6) && mActiveEUOutputs[aSide]) || mReleaseEnergy;
        return isEnergyOutputSide(aSide);
    }

    public void generatePowerNodes() {
        if (isServerSide() && mMetaTileEntity != null && (mMetaTileEntity.isEnetInput() || mMetaTileEntity.isEnetOutput())) {
            int time = MinecraftServer.getServer().getTickCounter();
            for (byte i = 0;i<6;i++) {
                if (outputsEnergyTo(i,false) || inputEnergyFrom(i,false)) {
                    IGregTechTileEntity TE = getIGregTechTileEntityAtSide(i);
                    if (TE instanceof BaseMetaPipeEntity) {
                        Node node = ((BaseMetaPipeEntity) TE).getNode();
                        if (node == null) {
                            POWER_GENERATION.accept((BaseMetaPipeEntity) TE);
                        } else if (node.mCreationTime != time) {
                            GenerateNodeMap.clearNodeMap(node,-1);
                            POWER_GENERATION.accept((BaseMetaPipeEntity) TE);
                        }
                    }
                }
            }
        }
    }

    @Override
    public long getOutputAmperage() {
        if (canAccessData() && mMetaTileEntity.isElectric()) return mMetaTileEntity.maxAmperesOut();
        return 0;
    }

    @Override
    public long getOutputVoltage() {
        if (canAccessData() && mMetaTileEntity.isElectric() && mMetaTileEntity.isEnetOutput())
            return mMetaTileEntity.maxEUOutput();
        return 0;
    }

    @Override
    public long getInputAmperage() {
        if (canAccessData() && mMetaTileEntity.isElectric()) return mMetaTileEntity.maxAmperesIn();
        return 0;
    }

    @Override
    public long getInputVoltage() {
        if (canAccessData() && mMetaTileEntity.isElectric()) return mMetaTileEntity.maxEUInput();
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean increaseStoredSteam(long aEnergy, boolean aIgnoreTooMuchEnergy) {
        if (!canAccessData()) return false;
        if (mMetaTileEntity.getSteamVar() < getSteamCapacity() || aIgnoreTooMuchEnergy) {
            setStoredSteam(mMetaTileEntity.getSteamVar() + aEnergy);
            return true;
        }
        return false;
    }

    @Override
    public String[] getDescription() {
        if (canAccessData()) return mMetaTileEntity.getDescription();
        return new String[0];
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        if (canAccessData()) return mMetaTileEntity.isValidSlot(aIndex);
        return false;
    }

    @Override
    public long getUniversalEnergyStored() {
        return Math.max(getStoredEU(), getStoredSteam());
    }

    @Override
    public long getUniversalEnergyCapacity() {
        return Math.max(getEUCapacity(), getSteamCapacity());
    }

    @Override
    public long getStoredEU() {
        if (canAccessData()) return Math.min(mMetaTileEntity.getEUVar(), getEUCapacity());
        return 0;
    }

    @Override
    public long getEUCapacity() {
        if (canAccessData()) return mMetaTileEntity.maxEUStore();
        return 0;
    }

    @Override
    public long getStoredSteam() {
        if (canAccessData()) return Math.min(mMetaTileEntity.getSteamVar(), getSteamCapacity());
        return 0;
    }

    @Override
    public long getSteamCapacity() {
        if (canAccessData()) return mMetaTileEntity.maxSteamStore();
        return 0;
    }

    @Override
    public ITexture[] getTexture(Block block, ForgeDirection side) {
        ITexture coverTexture = getCoverTexture((byte) side.ordinal());
        ITexture[] textureUncovered = hasValidMetaTileEntity() ?
                mMetaTileEntity.getTexture(this, (byte)side.ordinal(), mFacing, (byte) (mColor - 1), mActive, getOutputRedstoneSignal((byte) side.ordinal()) > 0) :
                Textures.BlockIcons.ERROR_RENDERING;
        ITexture[] textureCovered;
        if (coverTexture != null) {
            textureCovered = Arrays.copyOf(textureUncovered, textureUncovered.length + 1);
            textureCovered[textureUncovered.length] = coverTexture;
            return textureCovered;
        } else {
            return textureUncovered;
        }
    }

    private boolean isEnergyInputSide(byte aSide) {
        if (aSide >= 0 && aSide < 6) {
            if (!getCoverBehaviorAtSideNew(aSide).letsEnergyIn(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this))
                return false;
            if (isInvalid() || mReleaseEnergy) return false;
            if (canAccessData() && mMetaTileEntity.isElectric() && mMetaTileEntity.isEnetInput())
                return mMetaTileEntity.isInputFacing(aSide);
        }
        return false;
    }

    private boolean isEnergyOutputSide(byte aSide) {
        if (aSide >= 0 && aSide < 6) {
            if (!getCoverBehaviorAtSideNew(aSide).letsEnergyOut(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this))
                return false;
            if (isInvalid() || mReleaseEnergy) return mReleaseEnergy;
            if (canAccessData() && mMetaTileEntity.isElectric() && mMetaTileEntity.isEnetOutput())
                return mMetaTileEntity.isOutputFacing(aSide);
        }
        return false;
    }

    protected boolean hasValidMetaTileEntity() {
        return mMetaTileEntity != null && mMetaTileEntity.getBaseMetaTileEntity() == this;
    }

    protected boolean canAccessData() {
        return !isDead && hasValidMetaTileEntity();
    }

    public boolean setStoredEU(long aEnergy) {
        if (!canAccessData()) return false;
        if (aEnergy < 0) aEnergy = 0;
        mMetaTileEntity.setEUVar(aEnergy);
        return true;
    }

    public boolean setStoredSteam(long aEnergy) {
        if (!canAccessData()) return false;
        if (aEnergy < 0) aEnergy = 0;
        mMetaTileEntity.setSteamVar(aEnergy);
        return true;
    }

    public boolean decreaseStoredEU(long aEnergy, boolean aIgnoreTooLessEnergy) {
        if (!canAccessData()) {
            return false;
        }
        if (mMetaTileEntity.getEUVar() - aEnergy >= 0 || aIgnoreTooLessEnergy) {
            setStoredEU(mMetaTileEntity.getEUVar() - aEnergy);
            if (mMetaTileEntity.getEUVar() < 0) {
                setStoredEU(0);
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean decreaseStoredSteam(long aEnergy, boolean aIgnoreTooLessEnergy) {
        if (!canAccessData()) return false;
        if (mMetaTileEntity.getSteamVar() - aEnergy >= 0 || aIgnoreTooLessEnergy) {
            setStoredSteam(mMetaTileEntity.getSteamVar() - aEnergy);
            if (mMetaTileEntity.getSteamVar() < 0) {
                setStoredSteam(0);
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean playerOwnsThis(EntityPlayer aPlayer, boolean aCheckPrecicely) {
        if (!canAccessData()) return false;
        if (aCheckPrecicely || privateAccess() || (mOwnerName.length() == 0))
            if ((mOwnerName.length() == 0) && isServerSide()) {
                setOwnerName(aPlayer.getDisplayName());
                setOwnerUuid(aPlayer.getUniqueID());
            } else
                return !privateAccess() || aPlayer.getDisplayName().equals("Player") || mOwnerName.equals("Player") || mOwnerName.equals(aPlayer.getDisplayName());
        return true;
    }

    public boolean privateAccess() {
        if (!canAccessData()) return mLockUpgrade;
        return mLockUpgrade || mMetaTileEntity.ownerControl();
    }

    public void doEnergyExplosion() {
        if (getUniversalEnergyCapacity() > 0 && getUniversalEnergyStored() >= getUniversalEnergyCapacity() / 5) {
            GT_Log.exp.println("Energy Explosion, injected " + getUniversalEnergyStored() + "EU >= " + getUniversalEnergyCapacity() / 5D + "Capacity of the Machine!");

            doExplosion(oOutput * (getUniversalEnergyStored() >= getUniversalEnergyCapacity() ? 4 : getUniversalEnergyStored() >= getUniversalEnergyCapacity() / 2 ? 2 : 1));
            GT_Mod.achievements.issueAchievement(this.getWorldObj().getPlayerEntityByName(mOwnerName), "electricproblems");
        }
    }

    @Override
    public void doExplosion(long aAmount) {
        if (canAccessData()) {
            // This is only for Electric Machines
            if (GregTech_API.sMachineWireFire && mMetaTileEntity.isElectric()) {
                try {
                    mReleaseEnergy = true;
                    IEnergyConnected.Util.emitEnergyToNetwork(V[5], Math.max(1, getStoredEU() / V[5]), this);
                } catch (Exception e) {/* Fun Fact: all these "do nothing" Comments you see in my Code, are just there to let Eclipse shut up about the intended empty Brackets, but I need eclipse to yell at me in some of the regular Cases where I forget to add Code */}
            }
            mReleaseEnergy = false;
            // Normal Explosion Code
            mMetaTileEntity.onExplosion();
            if(GT_Mod.gregtechproxy.mExplosionItemDrop){
                for (int i = 0; i < this.getSizeInventory(); i++) {
                    ItemStack tItem = this.getStackInSlot(i);
                    if ((tItem != null) && (tItem.stackSize > 0) && (this.isValidSlot(i))) {
                    	dropItems(tItem);
                    	this.setInventorySlotContents(i, null); }
                }
            }
            if (mRecipeStuff != null) {
                for (int i = 0; i < 9; i++) {
                    if (this.getRandomNumber(100) < 50) {
                        dropItems(GT_Utility.loadItem(mRecipeStuff, "Ingredient." + i));
                    }
                }
            }

            GT_Pollution.addPollution(this, 100000);
            mMetaTileEntity.doExplosion(aAmount);
        }
    }

    public void dropItems(ItemStack tItem){
    	if(tItem==null)return;
        EntityItem tItemEntity = new EntityItem(this.worldObj, this.xCoord + XSTR_INSTANCE.nextFloat() * 0.8F + 0.1F, this.yCoord + XSTR_INSTANCE.nextFloat() * 0.8F + 0.1F, this.zCoord + XSTR_INSTANCE.nextFloat() * 0.8F + 0.1F, new ItemStack(tItem.getItem(), tItem.stackSize, tItem.getItemDamage()));
        if (tItem.hasTagCompound()) {
            tItemEntity.getEntityItem().setTagCompound((NBTTagCompound) tItem.getTagCompound().copy());
        }
        tItemEntity.motionX = (XSTR_INSTANCE.nextGaussian() * 0.0500000007450581D);
        tItemEntity.motionY = (XSTR_INSTANCE.nextGaussian() * 0.0500000007450581D + 0.2000000029802322D);
        tItemEntity.motionZ = (XSTR_INSTANCE.nextGaussian() * 0.0500000007450581D);
        tItemEntity.hurtResistantTime = 999999;
        tItemEntity.lifespan = 60000;
        try {
            if(ENTITY_ITEM_HEALTH_FIELD != null)
                ENTITY_ITEM_HEALTH_FIELD.setInt(tItemEntity, 99999999);
		} catch (Exception ignored) {}
        this.worldObj.spawnEntityInWorld(tItemEntity);
        tItem.stackSize = 0;
    }

    @Override
    public ArrayList<ItemStack> getDrops() {
        if (mMetaTileEntity == null || !mMetaTileEntity.canDrop()) {
            return new ArrayList<>();
        }
        ItemStack rStack = new ItemStack(GregTech_API.sBlockMachines, 1, mID);
        NBTTagCompound tNBT = new NBTTagCompound();
        if (mRecipeStuff != null && !mRecipeStuff.hasNoTags()) tNBT.setTag("GT.CraftingComponents", mRecipeStuff);
        if (mMuffler) tNBT.setBoolean("mMuffler", mMuffler);
        if (mLockUpgrade) tNBT.setBoolean("mLockUpgrade", mLockUpgrade);
        if (mSteamConverter) tNBT.setBoolean("mSteamConverter", mSteamConverter);
        if (mColor > 0) tNBT.setByte("mColor", mColor);
        if (mOtherUpgrades > 0) tNBT.setByte("mOtherUpgrades", mOtherUpgrades);
        if (mStrongRedstone > 0) tNBT.setByte("mStrongRedstone", mStrongRedstone);
        boolean hasCover = false;
        for (byte i = 0; i < mCoverSides.length; i++) {
            if (mCoverSides[i] != 0) {
                if (mCoverData[i] != null) // this really shouldn't be null if a cover is there already, but whatever
                    tNBT.setTag(COVER_DATA_NBT_KEYS[i], mCoverData[i].saveDataToNBT());
                hasCover = true;
            }
        }
        if (hasCover)
            tNBT.setIntArray("mCoverSides", mCoverSides);
        if (hasValidMetaTileEntity()) mMetaTileEntity.setItemNBT(tNBT);
        if (!tNBT.hasNoTags()) rStack.setTagCompound(tNBT);
        return new ArrayList<ItemStack>(Arrays.asList(rStack));
    }

    public int getUpgradeCount() {
        return (mMuffler ? 1 : 0) + (mLockUpgrade ? 1 : 0) + (mSteamConverter ? 1 : 0) + mOtherUpgrades;
    }

    @Override
    public boolean onRightclick(EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ) {
        if (isClientSide()) {
            //Configure Cover, sneak can also be: screwdriver, wrench, side cutter, soldering iron
            if (aPlayer.isSneaking()) {
                byte tSide = (getCoverIDAtSide(aSide) == 0) ? GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ) : aSide;
                return (getCoverBehaviorAtSideNew(tSide).hasCoverGUI());
            } else if (getCoverBehaviorAtSideNew(aSide).onCoverRightclickClient(aSide, this, aPlayer, aX, aY, aZ)) {
                return true;
            }

            if (!getCoverBehaviorAtSideNew(aSide).isGUIClickable(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this))
                return false;
        }
        if (isServerSide()) {
            if (!privateAccess() || aPlayer.getDisplayName().equalsIgnoreCase(getOwnerName())) {
                ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
                if (tCurrentItem != null) {
                    if (getColorization() >= 0 && GT_Utility.areStacksEqual(new ItemStack(Items.water_bucket, 1), tCurrentItem)) {
                        tCurrentItem.func_150996_a(Items.bucket);
                        setColorization((byte) (getColorization() >= 16 ? -2 : -1));
                        return true;
                    }
                    if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sWrenchList)) {
                    	if(aPlayer.isSneaking() && mMetaTileEntity instanceof GT_MetaTileEntity_BasicMachine && ((GT_MetaTileEntity_BasicMachine)mMetaTileEntity).setMainFacing(GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ))){
                            GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer);
                            GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(100), 1.0F, -1, xCoord, yCoord, zCoord);
                            cableUpdateDelay = 10;
                    	}else if (mMetaTileEntity.onWrenchRightClick(aSide, GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ), aPlayer, aX, aY, aZ)) {
                            GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer);
                            GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(100), 1.0F, -1, xCoord, yCoord, zCoord);
                            cableUpdateDelay = 10;
                        }
                        return true;
                    }

                    if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sScrewdriverList)) {
                        if (GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 200, aPlayer)) {
                            setCoverDataAtSide(aSide, getCoverBehaviorAtSideNew(aSide).onCoverScrewdriverClick(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this, aPlayer, aX, aY, aZ));
                            mMetaTileEntity.onScrewdriverRightClick(aSide, GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ), aPlayer, aX, aY, aZ);
                            GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(100), 1.0F, -1, xCoord, yCoord, zCoord);
                        }
                        return true;
                    }

                    if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sHardHammerList)) {
                        byte tSide = GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ);
                        if (mMetaTileEntity.onHammerToolRightClick(aSide, tSide, aPlayer, aX, aY, aZ)) {
                            // Handled internally
                            GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(1), 1.0F, -1, xCoord, yCoord, zCoord);
                        } else if (GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
                            mInputDisabled = !mInputDisabled;
                            if (mInputDisabled) mOutputDisabled = !mOutputDisabled;
                            GT_Utility.sendChatToPlayer(aPlayer, trans("086","Auto-Input: ") + (mInputDisabled ? trans("087","Disabled") : trans("088","Enabled") + trans("089","  Auto-Output: ") + (mOutputDisabled ? trans("087","Disabled") : trans("088","Enabled"))));
                            GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(1), 1.0F, -1, xCoord, yCoord, zCoord);
                        }
                        return true;
                    }

                    if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sSoftHammerList)) {
                        if (GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
                            if (mWorks) disableWorking();
                            else enableWorking();
                            {
                                String tChat = trans("090", "Machine Processing: ") + (isAllowedToWork() ? trans("088", "Enabled") : trans("087", "Disabled"));
                                if (getMetaTileEntity() != null && getMetaTileEntity().hasAlternativeModeText())
                                    tChat = getMetaTileEntity().getAlternativeModeText();
                                GT_Utility.sendChatToPlayer(aPlayer, tChat);
                            }
                            GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(101), 1.0F, -1, xCoord, yCoord, zCoord);
                        }
                        return true;
                    }

                    if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sSolderingToolList)) {
                        byte tSide = GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ);
                        if (mMetaTileEntity.onSolderingToolRightClick(aSide, tSide, aPlayer, aX, aY, aZ)) {
                            //logic handled internally
                            GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(103), 1.0F, -1, xCoord, yCoord, zCoord);
                        } else if (GT_ModHandler.useSolderingIron(tCurrentItem, aPlayer)) {
                            mStrongRedstone ^= (1 << tSide);
                            GT_Utility.sendChatToPlayer(aPlayer, trans("091","Redstone Output at Side ") + tSide + trans("092"," set to: ") + ((mStrongRedstone & (1 << tSide)) != 0 ? trans("093","Strong") : trans("094","Weak")));
                            GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(103), 3.0F, -1, xCoord, yCoord, zCoord);
                            issueBlockUpdate();
                        }
                        doEnetUpdate();
                        cableUpdateDelay = 10;
                        return true;
                    }

                    if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sWireCutterList)) {
                    	byte tSide = GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ);
                        if (mMetaTileEntity.onWireCutterRightClick(aSide, tSide, aPlayer, aX, aY, aZ)) {
                            //logic handled internally
                            GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(100), 1.0F, -1, xCoord, yCoord, zCoord);
                        }
                        doEnetUpdate();
                        cableUpdateDelay = 10;
                        return true;
                    }

                    byte coverSide = aSide;
                    if (getCoverIDAtSide(aSide) == 0) coverSide = GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ);

                    if (getCoverIDAtSide(coverSide) == 0) {
                        if (GregTech_API.sCovers.containsKey(new GT_ItemStack(tCurrentItem))) {
                            if (GregTech_API.getCoverBehaviorNew(tCurrentItem).isCoverPlaceable(coverSide, new GT_ItemStack(tCurrentItem), this) &&
                                mMetaTileEntity.allowCoverOnSide(coverSide, new GT_ItemStack(tCurrentItem)))
                            {
                                setCoverItemAtSide(coverSide, tCurrentItem);
                                if (!aPlayer.capabilities.isCreativeMode) tCurrentItem.stackSize--;
                                GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(100), 1.0F, -1, xCoord, yCoord, zCoord);
                            }
                            return true;
                        }
                    } else {
                        if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sCrowbarList)) {
                            if (GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
                                GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(0), 1.0F, -1, xCoord, yCoord, zCoord);
                                dropCover(coverSide, aSide, false);
                            }
                            return true;
                        }
                    }
                }
                else if (aPlayer.isSneaking()) { //Sneak click, no tool -> open cover config if possible.
                    aSide = (getCoverIDAtSide(aSide) == 0) ? GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ) : aSide;
                    return getCoverIDAtSide(aSide) > 0 && getCoverBehaviorAtSideNew(aSide).onCoverShiftRightClick(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this, aPlayer);
                }

                if (getCoverBehaviorAtSideNew(aSide).onCoverRightClick(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this, aPlayer, aX, aY, aZ))
                    return true;

                if (!getCoverBehaviorAtSideNew(aSide).isGUIClickable(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this))
                    return false;

                if (isUpgradable() && tCurrentItem != null) {/*
                    if (ItemList.Upgrade_SteamEngine.isStackEqual(aPlayer.inventory.getCurrentItem())) {
						if (addSteamEngineUpgrade()) {
							GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(3), 1.0F, -1, xCoord, yCoord, zCoord);
							if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
						}
						return true;
					}*/
                    if (ItemList.Upgrade_Muffler.isStackEqual(aPlayer.inventory.getCurrentItem())) {
                        if (addMufflerUpgrade()) {
                            GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(3), 1.0F, -1, xCoord, yCoord, zCoord);
                            if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
                        }
                        return true;
                    }
                    if (ItemList.Upgrade_Lock.isStackEqual(aPlayer.inventory.getCurrentItem())) {
                        if (isUpgradable() && !mLockUpgrade) {
                            mLockUpgrade = true;
                            setOwnerName(aPlayer.getDisplayName());
                            setOwnerUuid(aPlayer.getUniqueID());
                            GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(3), 1.0F, -1, xCoord, yCoord, zCoord);
                            if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
                        }
                        return true;
                    }
                }
            }
        }

        try {
            if (!aPlayer.isSneaking() && hasValidMetaTileEntity()) {
                return mMetaTileEntity.onRightclick(this, aPlayer, aSide, aX, aY, aZ);
            }
        } catch (Throwable e) {
            GT_Log.err.println("Encountered Exception while rightclicking TileEntity, the Game should've crashed now, but I prevented that. Please report immediately to GregTech Intergalactical!!!");
            e.printStackTrace(GT_Log.err);
        }

        return false;
    }

    @Override
    public void onLeftclick(EntityPlayer aPlayer) {
        try {
            if (aPlayer != null && hasValidMetaTileEntity()) mMetaTileEntity.onLeftclick(this, aPlayer);
        } catch (Throwable e) {
            GT_Log.err.println("Encountered Exception while leftclicking TileEntity, the Game should've crashed now, but I prevented that. Please report immediately to GregTech Intergalactical!!!");
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public boolean isDigitalChest() {
        if (canAccessData()) return mMetaTileEntity.isDigitalChest();
        return false;
    }

    @Override
    public ItemStack[] getStoredItemData() {
        if (canAccessData()) return mMetaTileEntity.getStoredItemData();
        return null;
    }

    @Override
    public void setItemCount(int aCount) {
        if (canAccessData()) mMetaTileEntity.setItemCount(aCount);
    }

    @Override
    public int getMaxItemCount() {
        if (canAccessData()) return mMetaTileEntity.getMaxItemCount();
        return 0;
    }

    /**
     * Can put aStack into Slot
     */
    @Override
    public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {
        return canAccessData() && mMetaTileEntity.isItemValidForSlot(aIndex, aStack);
    }

    /**
     * returns all valid Inventory Slots, no matter which Side (Unless it's covered).
     * The Side Stuff is done in the following two Functions.
     */
    @Override
    public int[] getAccessibleSlotsFromSide(int aSide) {
        if (canAccessData() && (getCoverBehaviorAtSideNew((byte) aSide).letsItemsOut((byte) aSide, getCoverIDAtSide((byte) aSide), getComplexCoverDataAtSide((byte) aSide), -1, this) || getCoverBehaviorAtSideNew((byte) aSide).letsItemsIn((byte) aSide, getCoverIDAtSide((byte) aSide), getComplexCoverDataAtSide((byte) aSide), -1, this)))
            return mMetaTileEntity.getAccessibleSlotsFromSide(aSide);
        return new int[0];
    }

    /**
     * Can put aStack into Slot at Side
     */
    @Override
    public boolean canInsertItem(int aIndex, ItemStack aStack, int aSide) {
        return canAccessData() && (mRunningThroughTick || !mInputDisabled) && getCoverBehaviorAtSideNew((byte) aSide).letsItemsIn((byte) aSide, getCoverIDAtSide((byte) aSide), getComplexCoverDataAtSide((byte) aSide), aIndex, this) && mMetaTileEntity.canInsertItem(aIndex, aStack, aSide);
    }

    /**
     * Can pull aStack out of Slot from Side
     */
    @Override
    public boolean canExtractItem(int aIndex, ItemStack aStack, int aSide) {
        return canAccessData() && (mRunningThroughTick || !mOutputDisabled) && getCoverBehaviorAtSideNew((byte) aSide).letsItemsOut((byte) aSide, getCoverIDAtSide((byte) aSide), getComplexCoverDataAtSide((byte) aSide), aIndex, this) && mMetaTileEntity.canExtractItem(aIndex, aStack, aSide);
    }

    @Override
    public boolean isUpgradable() {
        return canAccessData() && getUpgradeCount() < 8;
    }

    @Override
    public byte getInternalInputRedstoneSignal(byte aSide) {
        return (byte) (getCoverBehaviorAtSideNew(aSide).getRedstoneInput(aSide, getInputRedstoneSignal(aSide), getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this) & 15);
    }

    @Override
    public byte getInputRedstoneSignal(byte aSide) {
        return (byte) (worldObj.getIndirectPowerLevelTo(getOffsetX(aSide, 1), getOffsetY(aSide, 1), getOffsetZ(aSide, 1), aSide) & 15);
    }

    @Override
    public byte getOutputRedstoneSignal(byte aSide) {
        return getCoverBehaviorAtSideNew(aSide).manipulatesSidedRedstoneOutput(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this) ? mSidedRedstone[aSide] : getGeneralRS(aSide);
    }

    public byte getGeneralRS(byte aSide){
    	if(mMetaTileEntity==null)return 0;
    	return mMetaTileEntity.allowGeneralRedstoneOutput() ? mSidedRedstone[aSide] : 0;
    }

    @Override
    public void setInternalOutputRedstoneSignal(byte aSide, byte aStrength) {
        if (!getCoverBehaviorAtSideNew(aSide).manipulatesSidedRedstoneOutput(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this))
            setOutputRedstoneSignal(aSide, aStrength);
    }

    @Override
    public void setOutputRedstoneSignal(byte aSide, byte aStrength) {
        aStrength = (byte) Math.min(Math.max(0, aStrength), 15);
        if (aSide >= 0 && aSide < 6 && mSidedRedstone[aSide] != aStrength) {
            mSidedRedstone[aSide] = aStrength;
            issueBlockUpdate();
        }
    }

    @Override
    public boolean isSteamEngineUpgradable() {
        return isUpgradable() && !hasSteamEngineUpgrade() && getSteamCapacity() > 0;
    }

    @Override
    public boolean addSteamEngineUpgrade() {
        if (isSteamEngineUpgradable()) {
            issueBlockUpdate();
            mSteamConverter = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean hasSteamEngineUpgrade() {
        if (canAccessData() && mMetaTileEntity.isSteampowered()) return true;
        return mSteamConverter;
    }

    @Override
    public boolean hasMufflerUpgrade() {
        return mMuffler;
    }

    @Override
    public boolean isMufflerUpgradable() {
        return isUpgradable() && !hasMufflerUpgrade();
    }

    @Override
    public boolean addMufflerUpgrade() {
        if (isMufflerUpgradable()) return mMuffler = true;
        return false;
    }

    @Override
    public boolean hasInventoryBeenModified() {
        return mInventoryChanged;
    }

    @Override
    public void markInventoryBeenModified() {
        mInventoryChanged = true;
    }

    @Override
    public void setGenericRedstoneOutput(boolean aOnOff) {
        mRedstone = aOnOff;
    }

    @Override
    public int getErrorDisplayID() {
        return mDisplayErrorCode;
    }

    @Override
    public void setErrorDisplayID(int aErrorID) {
        mDisplayErrorCode = aErrorID;
    }

    @Override
    public IMetaTileEntity getMetaTileEntity() {
        return hasValidMetaTileEntity() ? mMetaTileEntity : null;
    }

    @Override
    public void setMetaTileEntity(IMetaTileEntity aMetaTileEntity) {
        mMetaTileEntity = (MetaTileEntity) aMetaTileEntity;
    }

    @Override
    @Deprecated
    public GT_CoverBehavior getCoverBehaviorAtSide(byte aSide) {
        if (aSide >= 0 && aSide < mCoverBehaviors.length && mCoverBehaviors[aSide] instanceof GT_CoverBehavior)
            return (GT_CoverBehavior) mCoverBehaviors[aSide];
        return GregTech_API.sNoBehavior;
    }

    @Override
    public void setCoverIDAtSide(byte aSide, int aID) {
        if (aSide >= 0 && aSide < 6) {
            mCoverSides[aSide] = aID;
            mCoverBehaviors[aSide] = GregTech_API.getCoverBehaviorNew(aID);
            mCoverData[aSide] = mCoverBehaviors[aSide].createDataObject();
            issueCoverUpdate(aSide);
            issueBlockUpdate();
        }
    }

    @Override
    public void setCoverItemAtSide(byte aSide, ItemStack aCover) {
        GregTech_API.getCoverBehaviorNew(aCover).placeCover(aSide, aCover, this);
    }

    @Override
    public int getCoverIDAtSide(byte aSide) {
        if (aSide >= 0 && aSide < 6) return mCoverSides[aSide];
        return 0;
    }

    @Override
    public ItemStack getCoverItemAtSide(byte aSide) {
        return GT_Utility.intToStack(getCoverIDAtSide(aSide));
    }

    @Override
    public boolean canPlaceCoverIDAtSide(byte aSide, int aID) {
        return getCoverIDAtSide(aSide) == 0;
    }

    @Override
    public boolean canPlaceCoverItemAtSide(byte aSide, ItemStack aCover) {
        return getCoverIDAtSide(aSide) == 0;
    }

    @Override
    @Deprecated
    public void setCoverDataAtSide(byte aSide, int aData) {
        if (aSide >= 0 && aSide < 6) mCoverData[aSide] = new ISerializableObject.LegacyCoverData(aData);
    }

    @Override
    @Deprecated
    public int getCoverDataAtSide(byte aSide) {
        if (aSide >= 0 && aSide < 6 && mCoverData[aSide] instanceof ISerializableObject.LegacyCoverData)
            return ((ISerializableObject.LegacyCoverData) mCoverData[aSide]).get();
        return 0;
    }
    @Override
    public void setCoverDataAtSide(byte aSide, ISerializableObject aData) {
        if (aSide >= 0 && aSide < 6 && getCoverBehaviorAtSideNew(aSide) != null && getCoverBehaviorAtSideNew(aSide).cast(aData) != null)
            mCoverData[aSide] = aData;
    }

    @Override
    public ISerializableObject getComplexCoverDataAtSide(byte aSide) {
        if (aSide >= 0 && aSide < 6 && getCoverBehaviorAtSideNew(aSide) != null)
            return mCoverData[aSide];
        return GregTech_API.sNoBehavior.createDataObject();
    }

    @Override
    public GT_CoverBehaviorBase<?> getCoverBehaviorAtSideNew(byte aSide) {
        if (aSide >= 0 && aSide < 6)
            return mCoverBehaviors[aSide];
        return GregTech_API.sNoBehavior;
    }

    public byte getLightValue() {
        return mLightValue;
    }

    @Override
    public void setLightValue(byte aLightValue) {
        mLightValue = (byte) (aLightValue & 15);
    }

    @Override
    public long getAverageElectricInput() {
        long rEU = 0;
        for (int i = 0; i < mAverageEUInput.length; ++i)
            if (i != mAverageEUInputIndex)
                rEU += mAverageEUInput[i];
        return rEU / (mAverageEUInput.length - 1);
    }

    @Override
    public long getAverageElectricOutput() {
        long rEU = 0;
        for (int i = 0; i < mAverageEUOutput.length; ++i)
            if (i != mAverageEUOutputIndex)
                rEU += mAverageEUOutput[i];
        return rEU / (mAverageEUOutput.length - 1);
    }

    @Override
    public boolean dropCover(byte aSide, byte aDroppedSide, boolean aForced) {
        if (getCoverBehaviorAtSideNew(aSide).onCoverRemoval(aSide, getCoverIDAtSide(aSide), mCoverData[aSide], this, aForced) || aForced) {
            ItemStack tStack = getCoverBehaviorAtSideNew(aSide).getDrop(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this);
            if (tStack != null) {
                tStack.setTagCompound(null);
                EntityItem tEntity = new EntityItem(worldObj, getOffsetX(aDroppedSide, 1) + 0.5, getOffsetY(aDroppedSide, 1) + 0.5, getOffsetZ(aDroppedSide, 1) + 0.5, tStack);
                tEntity.motionX = 0;
                tEntity.motionY = 0;
                tEntity.motionZ = 0;
                worldObj.spawnEntityInWorld(tEntity);
            }
            setCoverIDAtSide(aSide, 0);
            if (mMetaTileEntity.hasSidedRedstoneOutputBehavior()) {
                setOutputRedstoneSignal(aSide, (byte) 0);
            } else {
                setOutputRedstoneSignal(aSide, (byte) 15);
            }
            return true;
        }
        return false;
    }

    @Override
    public String getOwnerName() {
        if (GT_Utility.isStringInvalid(mOwnerName)) return "Player";
        return mOwnerName;
    }

    @Override
    public String setOwnerName(String aName) {
        if (GT_Utility.isStringInvalid(aName)) return mOwnerName = "Player";
        return mOwnerName = aName;
    }

    @Override
    public UUID getOwnerUuid() {
        return mOwnerUuid;
    }

    @Override
    public void setOwnerUuid(UUID uuid) {
        mOwnerUuid = uuid;
    }

    @Override
    public byte getComparatorValue(byte aSide) {
        return canAccessData() ? mMetaTileEntity.getComparatorValue(aSide) : 0;
    }

    @Override
    public byte getStrongOutputRedstoneSignal(byte aSide) {
        return aSide >= 0 && aSide < 6 && (mStrongRedstone & (1 << aSide)) != 0 ? (byte) (mSidedRedstone[aSide] & 15) : 0;
    }

    @Override
    public void setStrongOutputRedstoneSignal(byte aSide, byte aStrength) {
        mStrongRedstone |= (1 << aSide);
        setOutputRedstoneSignal(aSide, aStrength);
    }

    @Override
    public ItemStack decrStackSize(int aIndex, int aAmount) {
        if (canAccessData()) {
            mInventoryChanged = true;
            return mMetaTileEntity.decrStackSize(aIndex, aAmount);
        }
        return null;
    }

    @Override
    public long injectEnergyUnits(byte aSide, long aVoltage, long aAmperage) {
        if (!canAccessData() || !mMetaTileEntity.isElectric() || !inputEnergyFrom(aSide) || aAmperage <= 0 || aVoltage <= 0 || getStoredEU() >= getEUCapacity() || mMetaTileEntity.maxAmperesIn() <= mAcceptedAmperes)
            return 0;
        if (aVoltage > getInputVoltage()) {
            GT_Log.exp.println("Energy Explosion, injected "+aVoltage+"EU/t in a "+getInputVoltage()+"EU/t Machine!");
            doExplosion(aVoltage);
            return 0;
        }
        if (increaseStoredEnergyUnits(aVoltage * (aAmperage = Math.min(aAmperage, Math.min(mMetaTileEntity.maxAmperesIn() - mAcceptedAmperes, 1 + ((getEUCapacity() - getStoredEU()) / aVoltage)))), true)) {
            mAverageEUInput[mAverageEUInputIndex] += aVoltage * aAmperage;
            mAcceptedAmperes += aAmperage;
            return aAmperage;
        }
        return 0;
    }

    @Override
    public boolean drainEnergyUnits(byte aSide, long aVoltage, long aAmperage) {
        if (!canAccessData() || !mMetaTileEntity.isElectric() || !outputsEnergyTo(aSide) || getStoredEU() - (aVoltage * aAmperage) < mMetaTileEntity.getMinimumStoredEU())
            return false;
        if (decreaseStoredEU(aVoltage * aAmperage, false)) {
            mAverageEUOutput[mAverageEUOutputIndex] += aVoltage * aAmperage;
            return true;
        }
        return false;
    }

    @Override
    public boolean acceptsRotationalEnergy(byte aSide) {
        if (!canAccessData() || getCoverIDAtSide(aSide) != 0) return false;
        return mMetaTileEntity.acceptsRotationalEnergy(aSide);
    }

    @Override
    public boolean injectRotationalEnergy(byte aSide, long aSpeed, long aEnergy) {
        if (!canAccessData() || getCoverIDAtSide(aSide) != 0) return false;
        return mMetaTileEntity.injectRotationalEnergy(aSide, aSpeed, aEnergy);
    }

    @Override
    public int fill(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
        if (mTickTimer > 5 && canAccessData() &&
            (mRunningThroughTick || !mInputDisabled) &&
            (
                aSide == ForgeDirection.UNKNOWN ||
                (
                    mMetaTileEntity.isLiquidInput((byte) aSide.ordinal()) &&
                    getCoverBehaviorAtSideNew((byte) aSide.ordinal()).letsFluidIn((byte) aSide.ordinal(), getCoverIDAtSide((byte) aSide.ordinal()), getComplexCoverDataAtSide((byte) aSide.ordinal()), aFluid == null ? null : aFluid.getFluid(), this)
                )
            )
        )
            return mMetaTileEntity.fill(aSide, aFluid, doFill);
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection aSide, int maxDrain, boolean doDrain) {
        if (mTickTimer > 5 && canAccessData() &&
            (mRunningThroughTick || !mOutputDisabled) &&
            (
                aSide == ForgeDirection.UNKNOWN ||
                (
                    mMetaTileEntity.isLiquidOutput((byte) aSide.ordinal()) &&
                    getCoverBehaviorAtSideNew((byte) aSide.ordinal()).letsFluidOut((byte) aSide.ordinal(), getCoverIDAtSide((byte) aSide.ordinal()), getComplexCoverDataAtSide((byte) aSide.ordinal()), mMetaTileEntity.getFluid() == null ? null : mMetaTileEntity.getFluid().getFluid(), this)
                )
            )
        ) {
            return mMetaTileEntity.drain(aSide, maxDrain, doDrain);
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection aSide, FluidStack aFluid, boolean doDrain) {
        if (mTickTimer > 5 && canAccessData() &&
            (mRunningThroughTick || !mOutputDisabled) &&
            (
                aSide == ForgeDirection.UNKNOWN ||
                (
                    mMetaTileEntity.isLiquidOutput((byte) aSide.ordinal()) &&
                    getCoverBehaviorAtSideNew((byte) aSide.ordinal()).letsFluidOut((byte) aSide.ordinal(), getCoverIDAtSide((byte) aSide.ordinal()), getComplexCoverDataAtSide((byte) aSide.ordinal()), aFluid == null ? null : aFluid.getFluid(), this)
                )
            )
        )
            return mMetaTileEntity.drain(aSide, aFluid, doDrain);
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection aSide, Fluid aFluid) {
        if (mTickTimer > 5 && canAccessData() &&
            (mRunningThroughTick || !mInputDisabled) &&
            (
                aSide == ForgeDirection.UNKNOWN ||
                (
                    mMetaTileEntity.isLiquidInput((byte) aSide.ordinal()) &&
                    getCoverBehaviorAtSideNew((byte) aSide.ordinal()).letsFluidIn((byte) aSide.ordinal(), getCoverIDAtSide((byte) aSide.ordinal()), getComplexCoverDataAtSide((byte) aSide.ordinal()), aFluid, this)
                )
            )
        )
            return mMetaTileEntity.canFill(aSide, aFluid);
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection aSide, Fluid aFluid) {
        if (mTickTimer > 5 && canAccessData() &&
            (mRunningThroughTick || !mOutputDisabled) &&
            (
                aSide == ForgeDirection.UNKNOWN ||
                (
                    mMetaTileEntity.isLiquidOutput((byte) aSide.ordinal()) &&
                        getCoverBehaviorAtSideNew((byte) aSide.ordinal()).letsFluidOut((byte) aSide.ordinal(), getCoverIDAtSide((byte) aSide.ordinal()), getComplexCoverDataAtSide((byte) aSide.ordinal()), aFluid, this)
                )
            )
        )
            return mMetaTileEntity.canDrain(aSide, aFluid);
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection aSide) {
        if (canAccessData() &&
            (
                aSide == ForgeDirection.UNKNOWN ||
                (
                    mMetaTileEntity.isLiquidInput((byte) aSide.ordinal()) &&
                    getCoverBehaviorAtSideNew((byte) aSide.ordinal()).letsFluidIn((byte) aSide.ordinal(), getCoverIDAtSide((byte) aSide.ordinal()), getComplexCoverDataAtSide((byte) aSide.ordinal()), null, this)) || (mMetaTileEntity.isLiquidOutput((byte) aSide.ordinal()) &&
                    getCoverBehaviorAtSideNew((byte) aSide.ordinal()).letsFluidOut((byte) aSide.ordinal(), getCoverIDAtSide((byte) aSide.ordinal()), getComplexCoverDataAtSide((byte) aSide.ordinal()), null, this)
                )
            )
        )
            return mMetaTileEntity.getTankInfo(aSide);
        return new FluidTankInfo[]{};
    }

    public double getOutputEnergyUnitsPerTick() {
        return oOutput;
    }

    public boolean isTeleporterCompatible(ForgeDirection aSide) {
        return canAccessData() && mMetaTileEntity.isTeleporterCompatible();
    }

    public double demandedEnergyUnits() {
        if (mReleaseEnergy || !canAccessData() || !mMetaTileEntity.isEnetInput()) return 0;
        return getEUCapacity() - getStoredEU();
    }

    public double injectEnergyUnits(ForgeDirection aDirection, double aAmount) {
        return injectEnergyUnits((byte) aDirection.ordinal(), (int) aAmount, 1) > 0 ? 0 : aAmount;
    }

    public boolean acceptsEnergyFrom(TileEntity aEmitter, ForgeDirection aDirection) {
        return inputEnergyFrom((byte) aDirection.ordinal());
    }

    public boolean emitsEnergyTo(TileEntity aReceiver, ForgeDirection aDirection) {
        return outputsEnergyTo((byte) aDirection.ordinal());
    }

    public double getOfferedEnergy() {
        return (canAccessData() && getStoredEU() - mMetaTileEntity.getMinimumStoredEU() >= oOutput) ? Math.max(0, oOutput) : 0;
    }

    public void drawEnergy(double amount) {
        mAverageEUOutput[mAverageEUOutputIndex] += amount;
        decreaseStoredEU((int) amount, true);
    }

    public int injectEnergy(ForgeDirection aForgeDirection, int aAmount) {
        return injectEnergyUnits((byte) aForgeDirection.ordinal(), aAmount, 1) > 0 ? 0 : aAmount;
    }

    public int addEnergy(int aEnergy) {
        if (!canAccessData()) return 0;
        if (aEnergy > 0)
            increaseStoredEnergyUnits(aEnergy, true);
        else
            decreaseStoredEU(-aEnergy, true);
        return (int) Math.min(Integer.MAX_VALUE, mMetaTileEntity.getEUVar());
    }

    public boolean isAddedToEnergyNet() {
        return false;
    }

    public int demandsEnergy() {
        if (mReleaseEnergy || !canAccessData() || !mMetaTileEntity.isEnetInput()) return 0;
        return getCapacity() - getStored();
    }

    public int getCapacity() {
        return (int) Math.min(Integer.MAX_VALUE, getEUCapacity());
    }

    public int getStored() {
        return (int) Math.min(Integer.MAX_VALUE, Math.min(getStoredEU(), getCapacity()));
    }

    public void setStored(int aEU) {
        if (canAccessData()) setStoredEU(aEU);
    }

    public int getMaxSafeInput() {
        return (int) Math.min(Integer.MAX_VALUE, getInputVoltage());
    }

    public int getMaxEnergyOutput() {
        if (mReleaseEnergy) return Integer.MAX_VALUE;
        return getOutput();
    }

    public int getOutput() {
        return (int) Math.min(Integer.MAX_VALUE, oOutput);
    }

    public int injectEnergy(Direction aDirection, int aAmount) {
        return injectEnergyUnits((byte) aDirection.toSideValue(), aAmount, 1) > 0 ? 0 : aAmount;
    }

    public boolean isTeleporterCompatible(Direction aSide) {
        return canAccessData() && mMetaTileEntity.isTeleporterCompatible();
    }

    public boolean acceptsEnergyFrom(TileEntity aReceiver, Direction aDirection) {
        return inputEnergyFrom((byte) aDirection.toSideValue());
    }

    public boolean emitsEnergyTo(TileEntity aReceiver, Direction aDirection) {
        return outputsEnergyTo((byte) aDirection.toSideValue());
    }

    @Override
    public boolean isInvalidTileEntity() {
        return isInvalid();
    }

    @Override
    public boolean addStackToSlot(int aIndex, ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return true;
        if (aIndex < 0 || aIndex >= getSizeInventory()) return false;
        ItemStack tStack = getStackInSlot(aIndex);
        if (GT_Utility.isStackInvalid(tStack)) {
            setInventorySlotContents(aIndex, aStack);
            return true;
        }
        aStack = GT_OreDictUnificator.get(aStack);
        if (GT_Utility.areStacksEqual(tStack, aStack) && tStack.stackSize + aStack.stackSize <= Math.min(aStack.getMaxStackSize(), getInventoryStackLimit())) {
            tStack.stackSize += aStack.stackSize;
            markDirty();
            return true;
        }
        return false;
    }

    @Override
    public boolean addStackToSlot(int aIndex, ItemStack aStack, int aAmount) {
        return addStackToSlot(aIndex, GT_Utility.copyAmount(aAmount, aStack));
    }

    @Override
    public byte getColorization() {
        return (byte) (mColor - 1);
    }

    @Override
    public byte setColorization(byte aColor) {
        if (aColor > 15 || aColor < -1) aColor = -1;
        mColor = (byte) (aColor + 1);
        if (canAccessData()) mMetaTileEntity.onColorChangeServer(aColor);
        return mColor;
    }

    @Override
    public float getBlastResistance(byte aSide) {
        return canAccessData() ? Math.max(0, getMetaTileEntity().getExplosionResistance(aSide)) : 10.0F;
    }

    @Override
    public boolean isUniversalEnergyStored(long aEnergyAmount) {
        if (getUniversalEnergyStored() >= aEnergyAmount) return true;
        mHasEnoughEnergy = false;
        return false;
    }

    @Override
    public String[] getInfoData() {
        {
            if (canAccessData()) return getMetaTileEntity().getInfoData();
            return new String[]{};
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        mInventoryChanged = true;
    }

    @Override
    public int getLightOpacity() {
        return mMetaTileEntity == null ? getLightValue() > 0 ? 0 : 255 : mMetaTileEntity.getLightOpacity();
    }

    @Override
    public void addCollisionBoxesToList(World aWorld, int aX, int aY, int aZ, AxisAlignedBB inputAABB, List<AxisAlignedBB> outputAABB, Entity collider) {
        mMetaTileEntity.addCollisionBoxesToList(aWorld, aX, aY, aZ, inputAABB, outputAABB, collider);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        return mMetaTileEntity.getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
    }

    @Override
    public void onEntityCollidedWithBlock(World aWorld, int aX, int aY, int aZ, Entity collider) {
        mMetaTileEntity.onEntityCollidedWithBlock(aWorld, aX, aY, aZ, collider);
    }

     /**
      * Shifts the machine Inventory index according to the change in Input/Output Slots.
      * This is NOT done automatically. If you want to change slot count for a machine this method needs to be adapted.
      * Currently this method only works for GT_MetaTileEntity_BasicMachine
      * @param slotIndex The original Inventory index
      * @param nbtVersion The GregTech version in which the original Inventory Index was saved.
      * @return The corrected Inventory index
      */
     private int migrateInventoryIndex(int slotIndex, int nbtVersion){
         int oldInputSize, newInputSize, oldOutputSize, newOutputSize;
         int chemistryUpdateVersion = GT_Mod.calculateTotalGTVersion(509, 31);
         int configCircuitAdditionVersion = GT_Mod.calculateTotalGTVersion(509, 41);
         // 4 is old GT_MetaTileEntity_BasicMachine.OTHER_SLOT_COUNT
         if (nbtVersion < configCircuitAdditionVersion && getMetaTileEntity() instanceof GT_MetaTileEntity_BasicMachine && slotIndex >= 4)
             slotIndex += 1;
         if (mID >= 211 && mID <= 218) {//Assembler
             if (nbtVersion < chemistryUpdateVersion) {
                 oldInputSize = 2;
                 oldOutputSize = 1;
             } else {
                 return slotIndex;
             }
             newInputSize = 6;
             newOutputSize = 1;


         } else if (mID >= 421 && mID <= 428){//Chemical Reactor
             if (nbtVersion < chemistryUpdateVersion) {
                 oldInputSize = 2;
                 oldOutputSize = 1;
             } else {
                 return slotIndex;
             }
             newInputSize = 2;
             newOutputSize = 2;

         } else if (mID >= 531 && mID <= 538) {//Distillery
             if (nbtVersion < chemistryUpdateVersion) {
                 oldInputSize = 1;
                 oldOutputSize = 0;
             } else {
                 return slotIndex;
             }
             newInputSize = 1;
             newOutputSize = 1;
         } else if (mID >= 581 && mID <=588){//Mixer
             if (nbtVersion < chemistryUpdateVersion) {
                 oldInputSize = 4;
                 oldOutputSize = 1;
             }else{
                 return slotIndex;
             }
             newInputSize = 6;
             newOutputSize = 1;

         } else {
             return slotIndex;
         }

         int indexShift = 0;
         if (slotIndex >= GT_MetaTileEntity_BasicMachine.OTHER_SLOT_COUNT + oldInputSize) {indexShift += newInputSize - oldInputSize;
                	}
         if (slotIndex >= GT_MetaTileEntity_BasicMachine.OTHER_SLOT_COUNT + oldInputSize + oldOutputSize) {
             indexShift += newOutputSize - oldOutputSize;
         }
         return slotIndex + indexShift;
     }
    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public IGridNode getGridNode(ForgeDirection forgeDirection) {
        if (mFacing != forgeDirection.ordinal())
            return null;
        AENetworkProxy gp = getProxy();
        return gp != null ? gp.getNode() : null;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return mMetaTileEntity == null ? AECableType.NONE : mMetaTileEntity.getCableConnectionType(forgeDirection);
     }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public void securityBreak() {}

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public IGridNode getActionableNode() {
        AENetworkProxy gp = getProxy();
        return gp != null ? gp.getNode() : null;
     }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public AENetworkProxy getProxy() {
        return mMetaTileEntity == null ? null : mMetaTileEntity.getProxy();
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public DimensionalCoord getLocation() { return new DimensionalCoord( this ); }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public void gridChanged() {
        if (mMetaTileEntity != null)
            mMetaTileEntity.gridChanged();
    }

    @TileEvent( TileEventType.WORLD_NBT_READ )
    @Optional.Method(modid = "appliedenergistics2")
    public void readFromNBT_AENetwork( final NBTTagCompound data )
    {
        AENetworkProxy gp = getProxy();
        if (gp != null)
            getProxy().readFromNBT( data );
    }

    @TileEvent( TileEventType.WORLD_NBT_WRITE )
    @Optional.Method(modid = "appliedenergistics2")
    public void writeToNBT_AENetwork( final NBTTagCompound data )
    {
        AENetworkProxy gp = getProxy();
        if (gp != null)
            gp.writeToNBT( data );
    }

    @Optional.Method(modid = "appliedenergistics2")
    void onChunkUnloadAE() {
        AENetworkProxy gp = getProxy();
        if (gp != null)
            gp.onChunkUnload();
    }

    @Optional.Method(modid = "appliedenergistics2")
    void invalidateAE() {
        AENetworkProxy gp = getProxy();
        if (gp != null)
            gp.invalidate();
    }

    @Override
    public boolean wasShutdown() {
        return mWasShutdown;
    }

    @Override
    public boolean wasNotified() {
        return mWasNotified;
    }

    @Override
    public void setShutdownStatus(boolean newStatus) {
        mWasShutdown = newStatus;
    }

    @Override
    public void setNotificationStatus(boolean newStatus) {
        mWasNotified = newStatus;
    }

    @Override
    public void resetShutdownInformation() {
        setShutdownStatus(false);
        setNotificationStatus(false);
    }

    @Override
    public boolean hasTranslucency() {
        return hasValidMetaTileEntity() && mMetaTileEntity.hasTranslucency();
    }

    @Override
    public IAlignment getAlignment() {
        return getMetaTileEntity() instanceof IAlignmentProvider ? ((IAlignmentProvider) getMetaTileEntity()).getAlignment() : new BasicAlignment();
    }

    @Nullable
    @Override
    public IConstructable getConstructable() {
        return getMetaTileEntity() instanceof IConstructable ? (IConstructable) getMetaTileEntity() : null;
    }

    @Nullable
    @Override
    public ICallbackable getCallbackable() {
        val mte = this.getMetaTileEntity();

        if (mte instanceof ICallbackable<?>) {
            return (ICallbackable) mte;
        }

        return null;
    }

    private class BasicAlignment implements IAlignment {

        @Override
        public ExtendedFacing getExtendedFacing() {
            return ExtendedFacing.of(ForgeDirection.getOrientation(getFrontFacing()));
        }

        @Override
        public void setExtendedFacing(ExtendedFacing alignment) {
            setFrontFacing((byte) Math.min(alignment.getDirection().ordinal(), ForgeDirection.UNKNOWN.ordinal() - 1));
        }

        @Override
        public IAlignmentLimits getAlignmentLimits() {
            return (direction, rotation, flip) -> rotation.isNotRotated() && flip.isNotFlipped();
        }
    }
}
