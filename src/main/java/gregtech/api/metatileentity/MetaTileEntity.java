package gregtech.api.metatileentity;

import appeng.api.util.AECableType;
import appeng.me.helpers.AENetworkProxy;
import com.enderio.core.common.util.BlockCoord;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.metatileentity.IConfigurationCircuitSupport;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity.ClientEvents;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.*;
import gregtech.common.GT_Client;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.GT_Values.EU_PER_STEAM;
import static gregtech.api.enums.GT_Values.V;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * Extend this Class to add a new MetaMachine
 * Call the Constructor with the desired ID at the load-phase (not preload and also not postload!)
 * Implement the newMetaEntity-Method to return a new ready instance of your MetaTileEntity
 * <p/>
 * Call the Constructor like the following example inside the Load Phase, to register it.
 * "new GT_MetaTileEntity_E_Furnace(54, "GT_E_Furnace", "Automatic E-Furnace");"
 */
@SuppressWarnings("unused")
public abstract class MetaTileEntity implements IMetaTileEntity {
    /**
     * Only assigned for the MetaTileEntity in the List! Also only used to get the localized Name for the ItemStack and for getInvName.
     */
    public final String mName;
    /**
     * The Inventory of the MetaTileEntity. Amount of Slots can be larger than 256. HAYO!
     */
    public final ItemStack[] mInventory;
    public boolean doTickProfilingInThisTick = true;


    /**
     * accessibility to this Field is no longer given, see below
     */
    private IGregTechTileEntity mBaseMetaTileEntity;

    public long mSoundRequests=0;

    /**
     * This registers your Machine at the List.
     * Use only ID's larger than 2048, because i reserved these ones.
     * See also the List in the API, as it has a Description containing all the reservations.
     *
     * @param aID the ID
     * @example for Constructor overload.
     * <p/>
     * public GT_MetaTileEntity_EBench(int aID, String mName, String mNameRegional) {
     * super(aID, mName, mNameRegional);
     * }
     */
    public MetaTileEntity(int aID, String aBasicName, String aRegionalName, int aInvSlotCount) {
        if (GregTech_API.sPostloadStarted || !GregTech_API.sPreloadStarted)
            throw new IllegalAccessError("This Constructor has to be called in the load Phase");
        mName = aBasicName.replace(" ", "_").toLowerCase(Locale.ENGLISH);
        if (GregTech_API.METATILEENTITIES[aID] == null) {
            GregTech_API.METATILEENTITIES[aID] = this;
        } else {
            handleBadID(this, aID);
            throw new IllegalArgumentException("MetaMachine-Slot Nr. " + aID + " is already occupied!");
        }
        setBaseMetaTileEntity(GregTech_API.constructBaseMetaTileEntity());
        getBaseMetaTileEntity().setMetaTileID((short) aID);
        GT_LanguageManager.addStringLocalization("gt.blockmachines." + mName + ".name", aRegionalName);
        mInventory = new ItemStack[aInvSlotCount];
    }

    public static void handleBadID(MetaTileEntity entity, int aID) {
        final String otherName, thisName;
        otherName = GregTech_API.METATILEENTITIES[aID].getMetaName();
        thisName = entity.getMetaName();
        final String nameMsg = String.format("Existing: '%s' cannot coexist with '%s'", otherName, thisName);
        GT_FML_LOGGER.printf(Level.ERROR, "Conflict at ID: %d", aID);
        GT_FML_LOGGER.printf(Level.ERROR, nameMsg);
        if (GT_Values.dump_meta_entity_space || true) {
            dump_meta_spaces();
        } else {
            final String msg_0 = "Change the 'MTEDefrag' option in the main config to see free space. ";
            final String msg_1 = "The output from this will be in 'logs/MetaTileEntity.log'";
            final String msg = msg_0 + msg_1;
            GT_FML_LOGGER.printf(Level.ERROR, msg);
        }
    }

    public static void dump_meta_spaces() {
        if (!GT_Values.dump_meta_entity_space) {
            return;
        }
        if (GT_Log.mte == null) {
            GT_FML_LOGGER.printf(Level.ERROR, "MetaTileEntity Log File not created :/");
            return;
        }
        final PrintStream mte = GT_Log.mte;
        int rangeStart = 0;
        int rangeEnd = 0;
        boolean inRange = false;
        for (int i = 0; i < GregTech_API.METATILEENTITIES.length; i++) {
            final IMetaTileEntity atLoc = GregTech_API.METATILEENTITIES[i];
            if (atLoc == null) {
                inRange = true;
                rangeEnd = i;
            } else {
                if (inRange) {
                    final int range = rangeEnd - rangeStart;
                    if (range == 0) {
                        mte.printf("Free ID: %d%n", rangeStart);
                    } else {
                        mte.printf("Free Range of %d IDs: [%d, %d]%n", range, rangeStart, rangeEnd);
                    }
                }
                inRange = false;
                rangeStart = i + 1;
                rangeEnd = i + 1;
            }
        }
        if (inRange) {
            mte.printf("IDs from %d onward are free%n", rangeStart);
        }
    }

    /**
     * This is the normal Constructor.
     */
    public MetaTileEntity(String aName, int aInvSlotCount) {
        mInventory = new ItemStack[aInvSlotCount];
        mName = aName;
    }

    /**
     * This method will only be called on client side
     * @return whether the secondary description should be display. default is false
     */
    public boolean isDisplaySecondaryDescription() {
        return false;
    }

    @Override
    public IGregTechTileEntity getBaseMetaTileEntity() {
        return mBaseMetaTileEntity;
    }

    @Override
    public void setBaseMetaTileEntity(IGregTechTileEntity aBaseMetaTileEntity) {
        if (mBaseMetaTileEntity != null && aBaseMetaTileEntity == null) {
            mBaseMetaTileEntity.getMetaTileEntity().inValidate();
            mBaseMetaTileEntity.setMetaTileEntity(null);
        }
        mBaseMetaTileEntity = aBaseMetaTileEntity;
        if (mBaseMetaTileEntity != null) {
            mBaseMetaTileEntity.setMetaTileEntity(this);
        }
    }

    @Override
    public ItemStack getStackForm(long aAmount) {
        return new ItemStack(GregTech_API.sBlockMachines, (int) aAmount, getBaseMetaTileEntity().getMetaTileID());
    }

    public String getLocalName() {
        return GT_LanguageManager.getTranslation("gt.blockmachines." + mName + ".name");
    }

    @Override
    public void onServerStart() {/*Do nothing*/}

    @Override
    public void onWorldSave(File aSaveDirectory) {/*Do nothing*/}

    @Override
    public void onWorldLoad(File aSaveDirectory) {/*Do nothing*/}

    @Override
    public void onConfigLoad(GT_Config aConfig) {/*Do nothing*/}

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {/*Do nothing*/}

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {/*Do nothing*/}

    @Override
    public boolean allowCoverOnSide(byte aSide, GT_ItemStack aStack) {
        return true;
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {/*Do nothing*/}

    @Override
    public boolean onWrenchRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isValidFacing(aWrenchingSide)) {
            getBaseMetaTileEntity().setFrontFacing(aWrenchingSide);
            return true;
        }
        return false;
    }

    @Override
    public boolean onWireCutterRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if(!aPlayer.isSneaking()) return false;
        byte tSide = GT_Utility.getOppositeSide(aWrenchingSide);
        TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityAtSide(aWrenchingSide);
        if ((tTileEntity instanceof IGregTechTileEntity) && (((IGregTechTileEntity) tTileEntity).getMetaTileEntity() instanceof GT_MetaPipeEntity_Cable)) {
            // The tile entity we're facing is a cable, let's try to connect to it
            return ((IGregTechTileEntity) tTileEntity).getMetaTileEntity().onWireCutterRightClick(aWrenchingSide, tSide, aPlayer, aX, aY, aZ);
        }
        return false;
    }

    @Override
    public boolean onSolderingToolRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if(!aPlayer.isSneaking()) return false;
        byte tSide = GT_Utility.getOppositeSide(aWrenchingSide);
        TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityAtSide(aWrenchingSide);
        if ((tTileEntity instanceof IGregTechTileEntity) && (((IGregTechTileEntity) tTileEntity).getMetaTileEntity() instanceof GT_MetaPipeEntity_Cable)) {
            // The tile entity we're facing is a cable, let's try to connect to it
            return ((IGregTechTileEntity) tTileEntity).getMetaTileEntity().onSolderingToolRightClick(aWrenchingSide, tSide, aPlayer, aX, aY, aZ);
        }
        return false;
    }

    @Override
    public void onExplosion() {
        GT_Log.exp.println("Machine at "+this.getBaseMetaTileEntity().getXCoord()+" | "+this.getBaseMetaTileEntity().getYCoord()+" | "+this.getBaseMetaTileEntity().getZCoord()+" DIMID: "+ this.getBaseMetaTileEntity().getWorld().provider.dimensionId+ " exploded.");
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {/*Do nothing*/}

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {/*Do nothing*/}

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isClientSide() && GT_Client.changeDetected == 4) {
            /* Client tick counter that is set to 5 on hiding pipes and covers.
             * It triggers a texture update next client tick when reaching 4, with provision for 3 more update tasks,
             * spreading client change detection related work and network traffic on different ticks, until it reaches 0.
             */
            aBaseMetaTileEntity.issueTextureUpdate();
        }
    }

    @Override
    public void inValidate() {/*Do nothing*/}

    @Override
    public void onRemoval() {/*Do nothing*/}

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {/*Do nothing*/}

    /**
     * When a GUI is opened
     */
    public void onOpenGUI() {/*Do nothing*/}

    /**
     * When a GUI is closed
     */
    public void onCloseGUI() {/*Do nothing*/}

    /**
     * a Player rightclicks the Machine
     * Sneaky rightclicks are not getting passed to this!
     */
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        return false;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ) {
        return onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {/*Do nothing*/}

    @Override
    public void onValueUpdate(byte aValue) {/*Do nothing*/}

    @Override
    public byte getUpdateData() {
        return 0;
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {/*Do nothing*/}

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {/*Do nothing*/}

    @Override
    public void stopSoundLoop(byte aValue, double aX, double aY, double aZ) {/*Do nothing*/}

    @Override
    public final void sendSound(byte aIndex) {
        if (!getBaseMetaTileEntity().hasMufflerUpgrade())
            getBaseMetaTileEntity().sendBlockEvent(ClientEvents.DO_SOUND, aIndex);
    }

    @Override
    public final void sendLoopStart(byte aIndex) {
        if (!getBaseMetaTileEntity().hasMufflerUpgrade())
            getBaseMetaTileEntity().sendBlockEvent(ClientEvents.START_SOUND_LOOP, aIndex);
        mSoundRequests++;
    }

    @Override
    public final void sendLoopEnd(byte aIndex) {
        if (!getBaseMetaTileEntity().hasMufflerUpgrade())
            getBaseMetaTileEntity().sendBlockEvent(ClientEvents.STOP_SOUND_LOOP, aIndex);
    }

    /**
     * @return true if this Device emits Energy at all
     */
    public boolean isElectric() {
        return true;
    }

    /**
     * @return true if this Device emits Energy at all
     */
    public boolean isPneumatic() {
        return false;
    }

    /**
     * @return true if this Device emits Energy at all
     */
    public boolean isSteampowered() {
        return false;
    }

    /**
     * @return true if this Device emits Energy at all
     */
    public boolean isEnetOutput() {
        return false;
    }

    /**
     * @return true if this Device consumes Energy at all
     */
    public boolean isEnetInput() {
        return false;
    }

    /**
     * @return the amount of EU, which can be stored in this Device. Default is 0 EU.
     */
    public long maxEUStore() {
        return 0;
    }

    /**
     * @return the amount of EU/t, which can be accepted by this Device before it explodes.
     */
    public long maxEUInput() {
        return 0;
    }

    /**
     * @return the amount of EU/t, which can be outputted by this Device.
     */
    public long maxEUOutput() {
        return 0;
    }

    /**
     * @return the amount of E-net Impulses of the maxEUOutput size, which can be outputted by this Device.
     * Default is 1 Pulse, this shouldn't be set to smaller Values than 1, as it won't output anything in that Case!
     */
    public long maxAmperesOut() {
        return 1;
    }

    /**
     * How many Amperes this Block can suck at max. Surpassing this value won't blow it up.
     */
    public long maxAmperesIn() {
        return 1;
    }

    /**
     * @return true if that Side is an Output.
     */
    public boolean isOutputFacing(byte aSide) {
        return false;
    }

    /**
     * @return true if that Side is an Input.
     */
    public boolean isInputFacing(byte aSide) {
        return false;
    }

    /**
     * @return true if Transformer Upgrades increase Packet Amount.
     */
    public boolean isTransformingLowEnergy() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return false;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public boolean setStackToZeroInsteadOfNull(int aIndex) {
        return false;
    }

    /**
     * This is used to get the internal Energy. I use this for the IDSU.
     */
    public long getEUVar() {
        return ((BaseMetaTileEntity) mBaseMetaTileEntity).mStoredEnergy;
    }

    /**
     * This is used to set the internal Energy to the given Parameter. I use this for the IDSU.
     */
    public void setEUVar(long aEnergy) {
        if (aEnergy != ((BaseMetaTileEntity) mBaseMetaTileEntity).mStoredEnergy) {
            markDirty();
            ((BaseMetaTileEntity) mBaseMetaTileEntity).mStoredEnergy = aEnergy;
        }
    }

    /**
     * This is used to get the internal Steam Energy.
     */
    public long getSteamVar() {
        return ((BaseMetaTileEntity) mBaseMetaTileEntity).mStoredSteam;
    }

    /**
     * This is used to set the internal Steam Energy to the given Parameter.
     */
    public void setSteamVar(long aSteam) {
        if (aSteam != ((BaseMetaTileEntity) mBaseMetaTileEntity).mStoredSteam) {
            markDirty();
            ((BaseMetaTileEntity) mBaseMetaTileEntity).mStoredSteam = aSteam;
        }
    }

    /**
     * @return the amount of Steam, which can be stored in this Device. Default is 0 EU.
     */
    public long maxSteamStore() {
        return 0;
    }

    /**
     * @return the amount of EU, which this Device stores before starting to emit Energy.
     * useful if you don't want to emit stored Energy until a certain Level is reached.
     */
    public long getMinimumStoredEU() {
        return 512;
    }

    /**
     * Determines the Tier of the Machine, used for de-charging Tools.
     */
    public long getInputTier() {
        return GT_Utility.getTier(getBaseMetaTileEntity().getInputVoltage());
    }

    /**
     * Determines the Tier of the Machine, used for charging Tools.
     */
    public long getOutputTier() {
        return GT_Utility.getTier(getBaseMetaTileEntity().getOutputVoltage());
    }

    /**
     * gets the first RechargerSlot
     */
    public int rechargerSlotStartIndex() {
        return 0;
    }

    /**
     * gets the amount of RechargerSlots
     */
    public int rechargerSlotCount() {
        return 0;
    }

    /**
     * gets the first DechargerSlot
     */
    public int dechargerSlotStartIndex() {
        return 0;
    }

    /**
     * gets the amount of DechargerSlots
     */
    public int dechargerSlotCount() {
        return 0;
    }

    /**
     * gets if this is protected from other Players per default or not
     */
    public boolean ownerControl() {
        return false;
    }

    @Override
    public ArrayList<String> getSpecialDebugInfo(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, int aLogLevel, ArrayList<String> aList) {
        return aList;
    }

    @Override
    public boolean isLiquidInput(byte aSide) {
        return true;
    }

    @Override
    public boolean isLiquidOutput(byte aSide) {
        return true;
    }

    /**
     * gets the contained Liquid
     */
    @Override
    public FluidStack getFluid() {
        return null;
    }

    /**
     * tries to fill this Tank
     */
    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return 0;
    }

    /**
     * tries to empty this Tank
     */
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return null;
    }

    /**
     * Tank pressure
     */
    public int getTankPressure() {
        return 0;
    }

    /**
     * Liquid Capacity
     */
    @Override
    public int getCapacity() {
        return 0;
    }

    @Override
    public void onMachineBlockUpdate() {/*Do nothing*/}

    @Override
    public void receiveClientEvent(byte aEventID, byte aValue) {/*Do nothing*/}

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    /**
     * If this accepts up to 4 Overclockers
     */
    public boolean isOverclockerUpgradable() {
        return false;
    }

    /**
     * If this accepts Transformer Upgrades
     */
    public boolean isTransformerUpgradable() {
        return false;
    }

    /**
     * Progress this machine has already made
     */
    public int getProgresstime() {
        return 0;
    }

    /**
     * Progress this Machine has to do to produce something
     */
    public int maxProgresstime() {
        return 0;
    }

    /**
     * Increases the Progress, returns the overflown Progress.
     */
    public int increaseProgress(int aProgress) {
        return 0;
    }

    /**
     * If this TileEntity makes use of Sided Redstone behaviors.
     * Determines only, if the Output Redstone Array is getting filled with 0 for true, or 15 for false.
     */
    public boolean hasSidedRedstoneOutputBehavior() {
        return false;
    }

    /**
     * When the Facing gets changed.
     */
    public void onFacingChange() {/*Do nothing*/}

    /**
     * if the IC2 Teleporter can drain from this.
     */
    public boolean isTeleporterCompatible() {
        return isEnetOutput() && getBaseMetaTileEntity().getOutputVoltage() >= 128 && getBaseMetaTileEntity().getUniversalEnergyCapacity() >= 500000;
    }

    /**
     * Gets the Output for the comparator on the given Side
     */
    @Override
    public byte getComparatorValue(byte aSide) {
        return 0;
    }

    @Override
    public boolean acceptsRotationalEnergy(byte aSide) {
        return false;
    }

    @Override
    public boolean injectRotationalEnergy(byte aSide, long aSpeed, long aEnergy) {
        return false;
    }

    @Override
    public String getSpecialVoltageToolTip() {
        return null;
    }

    @Override
    public boolean isGivingInformation() {
        return false;
    }

    @Override
    public String[] getInfoData() {
        return new String[]{};
    }

    public boolean isDigitalChest() {
        return false;
    }

    public ItemStack[] getStoredItemData() {
        return null;
    }

    public void setItemCount(int aCount) {/*Do nothing*/}

    public int getMaxItemCount() {
        return 0;
    }

    @Override
    public int getSizeInventory() {
        return mInventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int aIndex) {
        if (aIndex >= 0 && aIndex < mInventory.length) return mInventory[aIndex];
        return null;
    }

    @Override
    public void setInventorySlotContents(int aIndex, ItemStack aStack) {
        markDirty();
        if (this instanceof IConfigurationCircuitSupport) {
            IConfigurationCircuitSupport ccs = (IConfigurationCircuitSupport)this;
            if (ccs.allowSelectCircuit() && aIndex == ccs.getCircuitSlot() && aStack != null) {
                mInventory[aIndex] = GT_Utility.copyAmount(0, aStack);
                return;
            }
        }
        if (aIndex >= 0 && aIndex < mInventory.length)
            mInventory[aIndex] = aStack;
    }

    @Override
    public String getInventoryName() {
        if (GregTech_API.METATILEENTITIES[getBaseMetaTileEntity().getMetaTileID()] != null)
            return GregTech_API.METATILEENTITIES[getBaseMetaTileEntity().getMetaTileID()].getMetaName();
        return "";
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {
        return getBaseMetaTileEntity().isValidSlot(aIndex);
    }

    @Override
    public ItemStack decrStackSize(int aIndex, int aAmount) {
        ItemStack tStack = getStackInSlot(aIndex), rStack = GT_Utility.copyOrNull(tStack);
        if (tStack != null) {
            if (tStack.stackSize <= aAmount) {
                if (setStackToZeroInsteadOfNull(aIndex)) {
                    tStack.stackSize = 0;
                    markDirty();
                }
                else setInventorySlotContents(aIndex, null);
            } else {
                rStack = tStack.splitStack(aAmount);
                markDirty();
                if (tStack.stackSize == 0 && !setStackToZeroInsteadOfNull(aIndex))
                    setInventorySlotContents(aIndex, null);
            }
        }
        return rStack;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int aSide) {
        ArrayList<Integer> tList = new ArrayList<Integer>();
        IGregTechTileEntity tTileEntity = getBaseMetaTileEntity();
        boolean tSkip = tTileEntity.getCoverBehaviorAtSideNew((byte) aSide).letsItemsIn((byte) aSide, tTileEntity.getCoverIDAtSide((byte) aSide), tTileEntity.getComplexCoverDataAtSide((byte) aSide), -2, tTileEntity) || tTileEntity.getCoverBehaviorAtSideNew((byte) aSide).letsItemsOut((byte) aSide, tTileEntity.getCoverIDAtSide((byte) aSide), tTileEntity.getComplexCoverDataAtSide((byte) aSide), -2, tTileEntity);
        for (int i = 0; i < getSizeInventory(); i++)
            if (isValidSlot(i) && (tSkip || tTileEntity.getCoverBehaviorAtSideNew((byte) aSide).letsItemsOut((byte) aSide, tTileEntity.getCoverIDAtSide((byte) aSide), tTileEntity.getComplexCoverDataAtSide((byte) aSide), i, tTileEntity) || tTileEntity.getCoverBehaviorAtSideNew((byte) aSide).letsItemsIn((byte) aSide, tTileEntity.getCoverIDAtSide((byte) aSide), tTileEntity.getComplexCoverDataAtSide((byte) aSide), i, tTileEntity)))
                tList.add(i);
        int[] rArray = new int[tList.size()];
        for (int i = 0; i < rArray.length; i++) rArray[i] = tList.get(i);
        return rArray;
    }

    @Override
    public boolean canInsertItem(int aIndex, ItemStack aStack, int aSide) {
        return isValidSlot(aIndex) && aStack != null && aIndex < mInventory.length && (mInventory[aIndex] == null || GT_Utility.areStacksEqual(aStack, mInventory[aIndex])) && allowPutStack(getBaseMetaTileEntity(), aIndex, (byte) aSide, aStack);
    }

    @Override
    public boolean canExtractItem(int aIndex, ItemStack aStack, int aSide) {
        return isValidSlot(aIndex) && aStack != null && aIndex < mInventory.length && allowPullStack(getBaseMetaTileEntity(), aIndex, (byte) aSide, aStack);
    }

    @Override
    public boolean canFill(ForgeDirection aSide, Fluid aFluid) {
        return fill(aSide, new FluidStack(aFluid, 1), false) == 1;
    }

    @Override
    public boolean canDrain(ForgeDirection aSide, Fluid aFluid) {
        return drain(aSide, new FluidStack(aFluid, 1), false) != null;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection aSide) {
        if (getCapacity() <= 0 && !getBaseMetaTileEntity().hasSteamEngineUpgrade()) return new FluidTankInfo[]{};
        return new FluidTankInfo[]{getInfo()};
    }

    public int fill_default(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
        markDirty();
        return fill(aFluid, doFill);
    }

    @Override
    public int fill(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
        if (getBaseMetaTileEntity().hasSteamEngineUpgrade() && GT_ModHandler.isSteam(aFluid) && aFluid.amount > 1) {
            long tSteam = Math.min(Integer.MAX_VALUE, Math.min(((long) aFluid.amount * EU_PER_STEAM), getBaseMetaTileEntity().getSteamCapacity() - getBaseMetaTileEntity().getStoredSteam()));
            if (tSteam >= EU_PER_STEAM) {
                markDirty();
                if (doFill) getBaseMetaTileEntity().increaseStoredSteam(tSteam, true);
                return (int) (tSteam / EU_PER_STEAM);
            }
        } else {
            return fill_default(aSide, aFluid, doFill);
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection aSide, FluidStack aFluid, boolean doDrain) {
        if (getFluid() != null && aFluid != null && getFluid().isFluidEqual(aFluid))
            return drain(aFluid.amount, doDrain);
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection aSide, int maxDrain, boolean doDrain) {
        return drain(maxDrain, doDrain);
    }

    @Override
    public int getFluidAmount() {
        return 0;
    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(this);
    }

    @Override
    public String getMetaName() {
        return mName;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public boolean doTickProfilingMessageDuringThisTick() {
        return doTickProfilingInThisTick;
    }

    @Override
    public void markDirty() {
        if (mBaseMetaTileEntity != null) {
            mBaseMetaTileEntity.markDirty();
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return false;
    }

    @Override
    public void openInventory() {
        //
    }

    @Override
    public void closeInventory() {
        //
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return null;
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return null;
    }

    @Override
    public boolean connectsToItemPipe(byte aSide) {
        return false;
    }

    @Override
    public float getExplosionResistance(byte aSide) {
        return 10.0F;
    }

    @Override
    public ItemStack[] getRealInventory() {
        return mInventory;
    }

    @Override
    public void onColorChangeServer(byte aColor) {
        final IGregTechTileEntity meta = getBaseMetaTileEntity();
        final int aX = meta.getXCoord(), aY = meta.getYCoord(), aZ = meta.getZCoord();
        for (byte aSide = 0; aSide < 6 ; aSide++ ) {
            // Flag surrounding pipes/cables to revaluate their connection with us if we got painted
            final TileEntity tTileEntity = meta.getTileEntityAtSide(aSide);
            if ((tTileEntity instanceof BaseMetaPipeEntity)) {
                ((BaseMetaPipeEntity) tTileEntity).onNeighborBlockChange(aX, aY, aZ);
            }
        }
    }

    @Override
    public void onColorChangeClient(byte aColor) {
        // Do nothing apparently
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderInInventory(Block aBlock, int aMeta, RenderBlocks aRenderer) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderInWorld(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, RenderBlocks aRenderer, boolean isTranslucentPass) {
        return false;
    }

    @Override
    public void doExplosion(long aExplosionPower) {
        World tWorld = getBaseMetaTileEntity().getWorld();
        GT_Utility.sendSoundToPlayers(tWorld, GregTech_API.sSoundList.get(209), 1.0F, -1, getBaseMetaTileEntity().getXCoord(), getBaseMetaTileEntity().getYCoord(), getBaseMetaTileEntity().getZCoord());
        tWorld.setBlock(getBaseMetaTileEntity().getXCoord(), getBaseMetaTileEntity().getYCoord(), getBaseMetaTileEntity().getZCoord(), Blocks.air);
        if (GregTech_API.sMachineExplosions)
            tWorld.createExplosion(null, getBaseMetaTileEntity().getXCoord() + 0.5, (int) getBaseMetaTileEntity().getYCoord() + 0.5, getBaseMetaTileEntity().getZCoord() + 0.5, GT_Values.MachineExplosionPower, true);
    }

    @Override
    public int getLightOpacity() {
        return ((BaseMetaTileEntity) getBaseMetaTileEntity()).getLightValue() > 0 ? 0 : 255;
    }

    @Override
    public void addCollisionBoxesToList(World aWorld, int aX, int aY, int aZ, AxisAlignedBB inputAABB, List<AxisAlignedBB> outputAABB, Entity collider) {
        AxisAlignedBB axisalignedbb1 = getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
        if (axisalignedbb1 != null && inputAABB.intersectsWith(axisalignedbb1)) outputAABB.add(axisalignedbb1);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        return AxisAlignedBB.getBoundingBox(aX, aY, aZ, aX + 1, aY + 1, aZ + 1);
    }

    @Override
    public void onEntityCollidedWithBlock(World aWorld, int aX, int aY, int aZ, Entity collider) {
        //
    }

    @Override
    public void onCreated(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        //
    }

    @Override
    public boolean allowGeneralRedstoneOutput(){
    	return false;
    }

    public String trans(String aKey, String aEnglish){
    	return GT_LanguageManager.addStringLocalization("Interaction_DESCRIPTION_Index_"+aKey, aEnglish, false);
    }

    @Override
    public boolean hasAlternativeModeText(){
    	return false;
    }

    @Override
    public String getAlternativeModeText() {
        return "";
    }

    @Override
    public boolean shouldJoinIc2Enet() {
        return false;
    }

    public boolean shouldTriggerBlockUpdate() {
        return false;
    }

    @Override
    public void getWailaBody(ItemStack stack, List<String> currentTip, MovingObjectPosition pos, NBTTagCompound tag, int side) {
        String facingStr = "Facing";
        final int facing = mBaseMetaTileEntity.getFrontFacing();
        currentTip.add(String.format("%s: %s", facingStr, ForgeDirection.getOrientation(facing).name()));
    }

    @Override
    public void getWailaNBT(NBTTagCompound tag, World world, BlockCoord pos) {
        IMetaTileEntity.super.getWailaNBT(tag, world, pos);
    }

    @Optional.Method(modid = "appliedenergistics2")
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return AECableType.NONE;
    }

    @Optional.Method(modid = "appliedenergistics2")
    public AENetworkProxy getProxy() {
        return null;
    }

    @Optional.Method(modid = "appliedenergistics2")
    public void gridChanged() {}

    protected void receiveMiscEvent(final byte aValue) {
        // Do nothing
    }

}
