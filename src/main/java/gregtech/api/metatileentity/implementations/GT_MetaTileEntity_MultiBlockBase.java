package gregtech.api.metatileentity.implementations;

import com.enderio.core.common.util.BlockCoord;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.gui.GT_Container_MultiMachine;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Pollution;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.enums.GT_Values.*;
import static net.minecraft.util.EnumChatFormatting.*;

public abstract class GT_MetaTileEntity_MultiBlockBase extends MetaTileEntity {

    public static boolean disableMaintenance;
    public boolean mMachine = false, mWrench = false, mScrewdriver = false, mSoftHammer = false, mHardHammer = false, mSolderingTool = false, mCrowbar = false, mRunningOnLoad = false;
    public int mPollution = 0, mProgresstime = 0, mMaxProgresstime = 0, mEUt = 0, mEfficiencyIncrease = 0, mStartUpCheck = 100, mRuntime = 0, mEfficiency = 0;
    public volatile int mUpdate = 0; //TODO: Replace with AtomicInteger
    public ItemStack[] mOutputItems = null;
    public FluidStack[] mOutputFluids = null;
    public String mNEI;
    public int damageFactorLow = 5;
    public float damageFactorHigh = 0.6f;

    protected long numEnergyInputAmps = 0;

    protected long numEnergyOutputAmps = 0;

    public ArrayList<GT_MetaTileEntity_Hatch_Input> mInputHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Output> mOutputHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_InputBus> mInputBusses = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_OutputBus> mOutputBusses = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Dynamo> mDynamoHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Muffler> mMufflerHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Energy> mEnergyHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Maintenance> mMaintenanceHatches = new ArrayList<>();

    public GT_MetaTileEntity_MultiBlockBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 2);
        GT_MetaTileEntity_MultiBlockBase.disableMaintenance = GregTech_API.sMachineFile.get(ConfigCategories.machineconfig, "MultiBlockMachines.disableMaintenance", false);
        this.damageFactorLow = GregTech_API.sMachineFile.get(ConfigCategories.machineconfig, "MultiBlockMachines.damageFactorLow", 5);
        this.damageFactorHigh = (float) GregTech_API.sMachineFile.get(ConfigCategories.machineconfig, "MultiBlockMachines.damageFactorHigh", 0.6f);
        this.mNEI = "";
    }

    public GT_MetaTileEntity_MultiBlockBase(String aName) {
        super(aName, 2);
        GT_MetaTileEntity_MultiBlockBase.disableMaintenance = GregTech_API.sMachineFile.get(ConfigCategories.machineconfig, "MultiBlockMachines.disableMaintenance", false);
        this.damageFactorLow = GregTech_API.sMachineFile.get(ConfigCategories.machineconfig, "MultiBlockMachines.damageFactorLow", 5);
        this.damageFactorHigh = (float) GregTech_API.sMachineFile.get(ConfigCategories.machineconfig, "MultiBlockMachines.damageFactorHigh", 0.6f);
    }

    public static boolean isValidMetaTileEntity(MetaTileEntity aMetaTileEntity) {
        return aMetaTileEntity.getBaseMetaTileEntity() != null && aMetaTileEntity.getBaseMetaTileEntity().getMetaTileEntity() == aMetaTileEntity && !aMetaTileEntity.getBaseMetaTileEntity().isDead();
    }

    /**
     * Caps the amount of energy that can be inputted into this machine. Set to -1 to make limitless
     * WIP
     * */
    public int maxTotalAmperageInput() {
        return 4;
    }

    /**
     * Caps the amount of energy that can be outputted from this machine. Set to -1 to make limitless
     * WIP
     * */
    public int maxTotalAmperageOutput() {
        return 1;
    }

    @Override
    public boolean isDisplaySecondaryDescription() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }

    @Override
    public boolean allowCoverOnSide(byte aSide, GT_ItemStack aCoverID) {
        return aSide != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex > 0;
    }

    @Override
    public int getProgresstime() {
        return mProgresstime;
    }

    @Override
    public int maxProgresstime() {
        return mMaxProgresstime;
    }

    @Override
    public int increaseProgress(int aProgress) {
        return aProgress;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mEUt", mEUt);
        aNBT.setInteger("mProgresstime", mProgresstime);
        aNBT.setInteger("mMaxProgresstime", mMaxProgresstime);
        aNBT.setInteger("mEfficiencyIncrease", mEfficiencyIncrease);
        aNBT.setInteger("mEfficiency", mEfficiency);
        aNBT.setInteger("mPollution", mPollution);
        aNBT.setInteger("mRuntime", mRuntime);

        /*aNBT.setLong("numEnergyInputAmps", numEnergyInputAmps);
        aNBT.setLong("numEnergyOutputAmps", numEnergyOutputAmps);*/

        if (mOutputItems != null) {
            aNBT.setInteger("mOutputItemsLength", mOutputItems.length);
            for (int i = 0; i < mOutputItems.length; i++)
                if (mOutputItems[i] != null) {
                    NBTTagCompound tNBT = new NBTTagCompound();
                    mOutputItems[i].writeToNBT(tNBT);
                    aNBT.setTag("mOutputItem" + i, tNBT);
                }
        }
        if (mOutputFluids != null) {
            aNBT.setInteger("mOutputFluidsLength", mOutputFluids.length);
            for (int i = 0; i < mOutputFluids.length; i++)
                if (mOutputFluids[i] != null) {
                    NBTTagCompound tNBT = new NBTTagCompound();
                    mOutputFluids[i].writeToNBT(tNBT);
                    aNBT.setTag("mOutputFluids" + i, tNBT);
                }
        }
        aNBT.setBoolean("mWrench", mWrench);
        aNBT.setBoolean("mScrewdriver", mScrewdriver);
        aNBT.setBoolean("mSoftHammer", mSoftHammer);
        aNBT.setBoolean("mHardHammer", mHardHammer);
        aNBT.setBoolean("mSolderingTool", mSolderingTool);
        aNBT.setBoolean("mCrowbar", mCrowbar);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mEUt = aNBT.getInteger("mEUt");
        mProgresstime = aNBT.getInteger("mProgresstime");
        mMaxProgresstime = aNBT.getInteger("mMaxProgresstime");
        if (mMaxProgresstime > 0) mRunningOnLoad = true;
        mEfficiencyIncrease = aNBT.getInteger("mEfficiencyIncrease");
        mEfficiency = aNBT.getInteger("mEfficiency");
        mPollution = aNBT.getInteger("mPollution");
        mRuntime = aNBT.getInteger("mRuntime");

        /*if (aNBT.hasKey("numEnergyInputAmps")) {
            numEnergyInputAmps = aNBT.getLong("numEnergyInputAmps");
        } else {
            numEnergyInputAmps = 0;
        }

        if (aNBT.hasKey("numEnergyOutputAmps")) {
            numEnergyOutputAmps = aNBT.getLong("numEnergyOutputAmps");
        } else {
            numEnergyOutputAmps = 0;
        }*/

        int aOutputItemsLength = aNBT.getInteger("mOutputItemsLength");
        if (aOutputItemsLength > 0) {
            mOutputItems = new ItemStack[aOutputItemsLength];
            for (int i = 0; i < mOutputItems.length; i++)
                mOutputItems[i] = GT_Utility.loadItem(aNBT, "mOutputItem" + i);
        }

        int aOutputFluidsLength = aNBT.getInteger("mOutputFluidsLength");
        if (aOutputFluidsLength > 0) {
            mOutputFluids = new FluidStack[aOutputFluidsLength];
            for (int i = 0; i < mOutputFluids.length; i++)
                mOutputFluids[i] = GT_Utility.loadFluid(aNBT, "mOutputFluids" + i);
        }

        mWrench = aNBT.getBoolean("mWrench");
        mScrewdriver = aNBT.getBoolean("mScrewdriver");
        mSoftHammer = aNBT.getBoolean("mSoftHammer");
        mHardHammer = aNBT.getBoolean("mHardHammer");
        mSolderingTool = aNBT.getBoolean("mSolderingTool");
        mCrowbar = aNBT.getBoolean("mCrowbar");
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_MultiMachine(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "MultiblockDisplay.png");
    }

    @Override
    public byte getTileEntityBaseType() {
        return 2;
    }

    @Override
    public void onMachineBlockUpdate() {
        mUpdate = 50;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (mEfficiency < 0) mEfficiency = 0;
            if (--mUpdate == 0 || --mStartUpCheck == 0) {
                resetCachedHatchInfo();
                mMachine = checkMachine(aBaseMetaTileEntity, mInventory[1]);
            }
            if (mStartUpCheck < 0) {
                if (mMachine) {
                    for (GT_MetaTileEntity_Hatch_Maintenance tHatch : mMaintenanceHatches) {
                        if (isValidMetaTileEntity(tHatch)) {
                            if (disableMaintenance){
                                mWrench = true;
                                mScrewdriver = true;
                                mSoftHammer = true;
                                mHardHammer = true;
                                mSolderingTool = true;
                                mCrowbar = true;
                            } else {
                                if (tHatch.mAuto && !(mWrench&&mScrewdriver&&mSoftHammer&&mHardHammer&&mSolderingTool&&mCrowbar))tHatch.autoMaintainance();
                                if (tHatch.mWrench) mWrench = true;
                                if (tHatch.mScrewdriver) mScrewdriver = true;
                                if (tHatch.mSoftHammer) mSoftHammer = true;
                                if (tHatch.mHardHammer) mHardHammer = true;
                                if (tHatch.mSolderingTool) mSolderingTool = true;
                                if (tHatch.mCrowbar) mCrowbar = true;

                                tHatch.mWrench = false;
                                tHatch.mScrewdriver = false;
                                tHatch.mSoftHammer = false;
                                tHatch.mHardHammer = false;
                                tHatch.mSolderingTool = false;
                                tHatch.mCrowbar = false;
                            }
                        }
                    }
                    if (getRepairStatus() > 0) {
                        if (mMaxProgresstime > 0 && doRandomMaintenanceDamage()) {
                            if (onRunningTick(mInventory[1])) {
                                markDirty();
                                if (!polluteEnvironment(getPollutionPerTick(mInventory[1]))) {
                                    stopMachine();
                                }
                                if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime) {
                                    if (mOutputItems != null) for (ItemStack tStack : mOutputItems)
                                        if (tStack != null) {
                                            try {
                                                GT_Mod.achievements.issueAchivementHatch(aBaseMetaTileEntity.getWorld().getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()), tStack);
                                            } catch (Exception ignored) {
                                            }
                                            addOutput(tStack);
                                        }
                                    if (mOutputFluids != null) {
                                    	addFluidOutputs(mOutputFluids);
                                    }
                                    mEfficiency = Math.max(0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
                                    mOutputItems = null;
                                    mProgresstime = 0;
                                    mMaxProgresstime = 0;
                                    mEfficiencyIncrease = 0;
                                    if (aBaseMetaTileEntity.isAllowedToWork()) checkRecipe(mInventory[1]);
                                    if (mOutputFluids != null && mOutputFluids.length > 0) {
//                                        if (mOutputFluids.length > 1) {
//                                            try {
//                                                GT_Mod.achievements.issueAchievement(aBaseMetaTileEntity.getWorld().getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()), "oilplant");
//                                            } catch (Exception ignored) {
//                                            }
//                                        }
                                    }
                                }
                            }
                        } else {
                            if (aTick % 100 == 0 || aBaseMetaTileEntity.hasWorkJustBeenEnabled() || aBaseMetaTileEntity.hasInventoryBeenModified()) {

                                if (aBaseMetaTileEntity.isAllowedToWork()) {
                                    if (checkRecipe(mInventory[1])) {
                                        markDirty();
                                    }
                                }
                                if (mMaxProgresstime <= 0) mEfficiency = Math.max(0, mEfficiency - 1000);
                            }
                        }
                    } else {
                        stopMachine();
                    }
                } else {
                    stopMachine();
                }
            }
            aBaseMetaTileEntity.setErrorDisplayID((aBaseMetaTileEntity.getErrorDisplayID() & ~127) | (mWrench ? 0 : 1) | (mScrewdriver ? 0 : 2) | (mSoftHammer ? 0 : 4) | (mHardHammer ? 0 : 8) | (mSolderingTool ? 0 : 16) | (mCrowbar ? 0 : 32) | (mMachine ? 0 : 64));
            aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
            boolean active=aBaseMetaTileEntity.isActive() && mPollution>0;
            for (GT_MetaTileEntity_Hatch_Muffler aMuffler : mMufflerHatches) {
                IGregTechTileEntity iGTTileEntity = aMuffler.getBaseMetaTileEntity();
                if (iGTTileEntity != null && !iGTTileEntity.isDead()) {
                    iGTTileEntity.setActive(active);
                }
            }
        }
    }

    protected void resetCachedHatchInfo() {
        mInputHatches.clear();
        mInputBusses.clear();
        mOutputHatches.clear();
        mOutputBusses.clear();
        mDynamoHatches.clear();
        mEnergyHatches.clear();
        mMufflerHatches.clear();
        mMaintenanceHatches.clear();
        numEnergyInputAmps = 0;
        numEnergyOutputAmps = 0;
    }

    public boolean polluteEnvironment(int aPollutionLevel) {
        mPollution += aPollutionLevel;
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                if (mPollution >= 10000) {
                    if (tHatch.polluteEnvironment(this)) {
                        mPollution -= 10000;
                    }
                } else {
                    break;
                }
            }
        }
        return mPollution < 10000;
    }

    /**
     * Called every tick the Machine runs
     */
    public boolean onRunningTick(ItemStack aStack) {
        if (mEUt > 0) {
            addEnergyOutput(((long) mEUt * mEfficiency) / 10000);
            return true;
        }
        if (mEUt < 0) {
            if (!drainEnergyInput(((long) -mEUt * 10000) / Math.max(1000, mEfficiency))) {
                if (!getBaseMetaTileEntity().wasShutdown()) {
                    criticalStopMachine();
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if this is a Correct Machine Part for this kind of Machine (Turbine Rotor for example)
     */
    public abstract boolean isCorrectMachinePart(ItemStack aStack);

    /**
     * Checks the Recipe
     */
    public abstract boolean checkRecipe(ItemStack aStack);

    /**
     * Checks the Machine. You have to assign the MetaTileEntities for the Hatches here.
     */
    public abstract boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack);

    /**
     * Gets the maximum Efficiency that spare Part can get (0 - 10000)
     */
    public abstract int getMaxEfficiency(ItemStack aStack);

    /**
     * Gets the pollution this Device outputs to a Muffler per tick (10000 = one Pullution Block)
     */
    public abstract int getPollutionPerTick(ItemStack aStack);

    /**
     * Gets the damage to the ItemStack, usually 0 or 1.
     */
    public abstract int getDamageToComponent(ItemStack aStack);

    /**
     * If it explodes when the Component has to be replaced.
     */
    public abstract boolean explodesOnComponentBreak(ItemStack aStack);

    public void stopMachine() {
        mOutputItems = null;
        mEUt = 0;
        mEfficiency = 0;
        mProgresstime = 0;
        mMaxProgresstime = 0;
        mEfficiencyIncrease = 0;
        getBaseMetaTileEntity().disableWorking();
    }

    public void criticalStopMachine() {
        stopMachine();
        getBaseMetaTileEntity().setShutdownStatus(true);
        getBaseMetaTileEntity().setNotificationStatus(false);
    }

    public int getRepairStatus() {
        return (mWrench ? 1 : 0) + (mScrewdriver ? 1 : 0) + (mSoftHammer ? 1 : 0) + (mHardHammer ? 1 : 0) + (mSolderingTool ? 1 : 0) + (mCrowbar ? 1 : 0);
    }

    public int getIdealStatus() {
        return 6;
    }


    public int getCurrentEfficiency(ItemStack itemStack) {
            int maxEff = getMaxEfficiency(itemStack);
            return maxEff - (getIdealStatus() - getRepairStatus()) * maxEff / 10;
        }

    public boolean doRandomMaintenanceDamage() {
        if (!isCorrectMachinePart(mInventory[1]) || getRepairStatus() == 0) {
            stopMachine();
            return false;
        }
        if (mRuntime++ > 1000) {
            mRuntime = 0;
            if (getBaseMetaTileEntity().getRandomNumber(6000) == 0) {
                applyMaintenanceDamage();
            }
            if (mInventory[1] != null && getBaseMetaTileEntity().getRandomNumber(2) == 0 && !mInventory[1].getUnlocalizedName().startsWith("gt.blockmachines.basicmachine.")) {
                if (mInventory[1].getItem() instanceof GT_MetaGenerated_Tool_01) {
                    NBTTagCompound tNBT = mInventory[1].getTagCompound();
                    if (tNBT != null) {
                        NBTTagCompound tNBT2 = tNBT.getCompoundTag("GT.CraftingComponents");//tNBT2 dont use out if
                        /*if (!tNBT.getBoolean("mDis")) {
                            tNBT2 = new NBTTagCompound();
                            Materials tMaterial = GT_MetaGenerated_Tool.getPrimaryMaterial(mInventory[1]);
                            ItemStack tTurbine = GT_OreDictUnificator.get(OrePrefixes.turbineBlade, tMaterial, 1);
                            int i = mInventory[1].getItemDamage();
                            if (i == 170) {
                                ItemStack tStack = GT_Utility.copyAmount(1, tTurbine);
                                tNBT2.setTag("Ingredient.0", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.1", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.2", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.3", tStack.writeToNBT(new NBTTagCompound()));
                                tStack = GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Magnalium, 1);
                                tNBT2.setTag("Ingredient.4", tStack.writeToNBT(new NBTTagCompound()));
                            } else if (i == 172) {
                                ItemStack tStack = GT_Utility.copyAmount(1, tTurbine);
                                tNBT2.setTag("Ingredient.0", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.1", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.2", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.3", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.5", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.6", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.7", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.8", tStack.writeToNBT(new NBTTagCompound()));
                                tStack = GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Titanium, 1);
                                tNBT2.setTag("Ingredient.4", tStack.writeToNBT(new NBTTagCompound()));
                            } else if (i == 174) {
                                ItemStack tStack = GT_Utility.copyAmount(2, tTurbine);
                                tNBT2.setTag("Ingredient.0", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.1", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.2", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.3", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.5", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.6", tStack.writeToNBT(new NBTTagCompound()));
                                tStack = GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TungstenSteel, 1);
                                tNBT2.setTag("Ingredient.4", tStack.writeToNBT(new NBTTagCompound()));
                            } else if (i == 176) {
                                ItemStack tStack = GT_Utility.copyAmount(2, tTurbine);
                                tNBT2.setTag("Ingredient.0", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.1", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.2", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.3", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.5", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.6", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.7", tStack.writeToNBT(new NBTTagCompound()));
                                tNBT2.setTag("Ingredient.8", tStack.writeToNBT(new NBTTagCompound()));
                                tStack = GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Americium, 1);
                                tNBT2.setTag("Ingredient.4", tStack.writeToNBT(new NBTTagCompound()));
                            }
                            tNBT.setTag("GT.CraftingComponents", tNBT2);
                            tNBT.setBoolean("mDis", true);
                            mInventory[1].setTagCompound(tNBT);

                        }*/
                    }
                    ((GT_MetaGenerated_Tool) mInventory[1].getItem()).doDamage(mInventory[1], (long)getDamageToComponent(mInventory[1]) * (long) Math.min(mEUt / this.damageFactorLow, Math.pow(mEUt, this.damageFactorHigh)));
                    if (mInventory[1].stackSize == 0) mInventory[1] = null;
                }
            }
        }
        return true;
    }

    public void applyMaintenanceDamage() {
        switch (getBaseMetaTileEntity().getRandomNumber(6)) {
            case 0:
                mWrench = false;
                break;
            case 1:
                mScrewdriver = false;
                break;
            case 2:
                mSoftHammer = false;
                break;
            case 3:
                mHardHammer = false;
                break;
            case 4:
                mSolderingTool = false;
                break;
            case 5:
                mCrowbar = false;
                break;
        }
    }

    public void explodeMultiblock() {

        GT_Log.exp.println("MultiBlockExplosion at: " +this.getBaseMetaTileEntity().getXCoord()+" | "+this.getBaseMetaTileEntity().getYCoord()+" | "+this.getBaseMetaTileEntity().getZCoord()+" DIMID: "+ this.getBaseMetaTileEntity().getWorld().provider.dimensionId+".");

        GT_Pollution.addPollution(getBaseMetaTileEntity(), 300000);
        mInventory[1] = null;
        for (MetaTileEntity tTileEntity : mInputBusses) tTileEntity.getBaseMetaTileEntity().doExplosion(V[8]);
        for (MetaTileEntity tTileEntity : mOutputBusses) tTileEntity.getBaseMetaTileEntity().doExplosion(V[8]);
        for (MetaTileEntity tTileEntity : mInputHatches) tTileEntity.getBaseMetaTileEntity().doExplosion(V[8]);
        for (MetaTileEntity tTileEntity : mOutputHatches) tTileEntity.getBaseMetaTileEntity().doExplosion(V[8]);
        for (MetaTileEntity tTileEntity : mDynamoHatches) tTileEntity.getBaseMetaTileEntity().doExplosion(V[8]);
        for (MetaTileEntity tTileEntity : mMufflerHatches) tTileEntity.getBaseMetaTileEntity().doExplosion(V[8]);
        for (MetaTileEntity tTileEntity : mEnergyHatches) tTileEntity.getBaseMetaTileEntity().doExplosion(V[8]);
        for (MetaTileEntity tTileEntity : mMaintenanceHatches) tTileEntity.getBaseMetaTileEntity().doExplosion(V[8]);
        getBaseMetaTileEntity().doExplosion(V[8]);
    }

    public boolean addEnergyOutput(long aEU) {
        if (aEU <= 0) {
            return true;
        }
        if (mDynamoHatches.size() > 0) {
            return addEnergyOutputMultipleDynamos(aEU, true);
        }
        return false;
    }
    public boolean addEnergyOutputMultipleDynamos(long aEU, boolean aAllowMixedVoltageDynamos) {
        int injected = 0;
        long totalOutput = 0;
        long aFirstVoltageFound = -1;
        boolean aFoundMixedDynamos = false;
        for (GT_MetaTileEntity_Hatch_Dynamo aDynamo : mDynamoHatches) {
            if( aDynamo == null ) {
                return false;
            }
            if (isValidMetaTileEntity(aDynamo)) {
                long aVoltage = aDynamo.maxEUOutput();
                long aTotal = aDynamo.maxAmperesOut() * aVoltage;
                // Check against voltage to check when hatch mixing
                if (aFirstVoltageFound == -1) {
                    aFirstVoltageFound = aVoltage;
                }
                else {
                    if (aFirstVoltageFound != aVoltage) {
                        aFoundMixedDynamos = true;
                    }
                }
                totalOutput += aTotal;
            }
        }

        if (totalOutput < aEU || (aFoundMixedDynamos && !aAllowMixedVoltageDynamos)) {
            //explodeMultiblock();
            return false;
        }

        long leftToInject;
        long aVoltage;
        int aAmpsToInject;
        int aRemainder;
        int ampsOnCurrentHatch;
        for (GT_MetaTileEntity_Hatch_Dynamo aDynamo : mDynamoHatches) {
            if (isValidMetaTileEntity(aDynamo)) {
                leftToInject = aEU - injected;
                aVoltage = aDynamo.maxEUOutput();
                aAmpsToInject = (int) (leftToInject / aVoltage);
                aRemainder = (int) (leftToInject - (aAmpsToInject * aVoltage));
                ampsOnCurrentHatch= (int) Math.min(aDynamo.maxAmperesOut(), aAmpsToInject);
                for (int i = 0; i < ampsOnCurrentHatch; i++) {
                    aDynamo.getBaseMetaTileEntity().increaseStoredEnergyUnits(aVoltage, false);
                }
                injected += aVoltage * ampsOnCurrentHatch;
                if(aRemainder>0 && ampsOnCurrentHatch<aDynamo.maxAmperesOut()){
                    aDynamo.getBaseMetaTileEntity().increaseStoredEnergyUnits(aRemainder, false);
                    injected+=aRemainder;
                }
            }
        }
        return injected > 0;
    }

    public long getMaxInputVoltage() {
        long rVoltage = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches)
            if (isValidMetaTileEntity(tHatch)) {
                var amps = tHatch.maxAmperesIn();
                // Fixes 2A inputs overclocking the multi improperly, kinda janky
                // TODO: Figure out a better approach
                amps = amps > 2 ? amps : 1;
                rVoltage += tHatch.getBaseMetaTileEntity().getInputVoltage() * amps;
            }
        return rVoltage;
    }

    /**
     * Calcualtes the overclockedness using long integers
     * @param aEUt              - recipe EUt
     * @param aDuration         - recipe Duration
     * @param mAmperage         - should be 1 ?
     * @param maxInputVoltage   - Multiblock Max input voltage
     * @param perfectOC         - If the Multiblock OCs perfectly, i.e. the large Chemical Reactor
     */
    protected void calculateOverclockedNessMultiInternal(int aEUt, int aDuration, int mAmperage, long maxInputVoltage, boolean perfectOC) {
        byte mTier = (byte) Math.max(0, GT_Utility.getTier(maxInputVoltage));
        if(mTier == 0){
            //Long time calculation
            long xMaxProgresstime = ((long)aDuration)<<1;
            if(xMaxProgresstime > Integer.MAX_VALUE - 1){
                //make impossible if too long
                mEUt = Integer.MAX_VALUE - 1;
                mMaxProgresstime = Integer.MAX_VALUE - 1;
            }else{
                mEUt = aEUt >> 2;
                mMaxProgresstime= (int) xMaxProgresstime;
            }
        }else{
            //Long EUt calculation
            long xEUt = aEUt;
            //Isnt too low EUt check?
            long tempEUt = Math.max(xEUt, V[1]);

            mMaxProgresstime = aDuration;

            final int ocTimeShift = perfectOC ? 2 : 1;

            while (tempEUt <= V[mTier - 1] * mAmperage) {
                tempEUt <<= 2;//this actually controls overclocking
                //xEUt *= 4;//this is effect of everclocking
                int oldTime = mMaxProgresstime;
                mMaxProgresstime >>= ocTimeShift;//this is effect of overclocking
                if (mMaxProgresstime <1)
                {
                    if(oldTime == 1)
                        break;
                    xEUt *= oldTime * (perfectOC ? 1:2);
                    break;
                }
                else
                {
                    xEUt <<= 2;
                }
            }
            if(xEUt > Integer.MAX_VALUE - 1) {
                mEUt = Integer.MAX_VALUE - 1;
                mMaxProgresstime = Integer.MAX_VALUE - 1;
            } else {
                mEUt = (int) xEUt;
                if(mEUt == 0)
                    mEUt = 1;
                if(mMaxProgresstime == 0)
                    mMaxProgresstime = 1;//set time to 1 tick
            }
        }
    }

    protected void calculateOverclockedNessMulti(int aEUt, int aDuration, int mAmperage, long maxInputVoltage) {
        calculateOverclockedNessMultiInternal(aEUt,aDuration,mAmperage,maxInputVoltage,false);
    }

    protected void calculatePerfectOverclockedNessMulti(int aEUt, int aDuration, int mAmperage, long maxInputVoltage) {
        calculateOverclockedNessMultiInternal(aEUt,aDuration,mAmperage,maxInputVoltage,true);
    }

    public boolean drainEnergyInput(long aEU) {
        if (aEU <= 0) return true;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches)
            if (isValidMetaTileEntity(tHatch)) {
                if (tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(aEU, false)) return true;
            }
        return false;
    }

    protected static boolean dumpFluid(List<GT_MetaTileEntity_Hatch_Output> aOutputHatches, FluidStack fluidStack, boolean restrictiveHatchesOnly){
        for (GT_MetaTileEntity_Hatch_Output tHatch : aOutputHatches) {
        	if (!isValidMetaTileEntity(tHatch) || (restrictiveHatchesOnly && tHatch.mMode == 0)) {
        		continue;
        	}
        	if (GT_ModHandler.isSteam(fluidStack)) {
        		if (!tHatch.outputsSteam()) {
        			continue;
        		}
        	} else {
        		if (!tHatch.outputsLiquids()) {
        			continue;
        		}
        		if (tHatch.isFluidLocked() && tHatch.getLockedFluidName() != null && !tHatch.getLockedFluidName().equals(fluidStack.getFluid().getName())) {
        			continue;
        		}
        	}
            int tAmount = tHatch.fill(fluidStack, false);
            if (tAmount >= fluidStack.amount) {
            	boolean filled = tHatch.fill(fluidStack, true) >= fluidStack.amount;
            	tHatch.onEmptyingContainerWhenEmpty();
                return filled;
            } else if (tAmount > 0) {
                fluidStack.amount = fluidStack.amount - tHatch.fill(fluidStack, true);
                tHatch.onEmptyingContainerWhenEmpty();
            }
        }
        return false;
    }
    
    public boolean addOutput(FluidStack aLiquid) {
        if (aLiquid == null) return false;
        if (!dumpFluid(mOutputHatches, aLiquid, true)){
            return dumpFluid(mOutputHatches, aLiquid, false);
        }
        return true;
    }

    protected void addFluidOutputs(FluidStack[] mOutputFluids2) {
        for (FluidStack outputFluidStack : mOutputFluids2) {
        	addOutput(outputFluidStack);
        }
    }

    public boolean depleteInput(FluidStack aLiquid) {
        if (aLiquid == null) return false;
        for (GT_MetaTileEntity_Hatch_Input tHatch : mInputHatches) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch)) {
                FluidStack tLiquid = tHatch.getFluid();
                if (tLiquid != null && tLiquid.isFluidEqual(aLiquid)) {
                    tLiquid = tHatch.drain(aLiquid.amount, false);
                    if (tLiquid != null && tLiquid.amount >= aLiquid.amount) {
                        tLiquid = tHatch.drain(aLiquid.amount, true);
                        return tLiquid != null && tLiquid.amount >= aLiquid.amount;
                    }
                }
            }
        }
        return false;
    }

    public boolean addOutput(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return false;
        aStack = GT_Utility.copyOrNull(aStack);
        for (GT_MetaTileEntity_Hatch_OutputBus tHatch : mOutputBusses) {
            if (isValidMetaTileEntity(tHatch) && tHatch.storeAll(aStack)) {
                return true;
            }
        }
        boolean outputSuccess = true;
        while (outputSuccess && aStack.stackSize > 0) {
            outputSuccess = false;
            ItemStack single = aStack.splitStack(1);
            for (GT_MetaTileEntity_Hatch_Output tHatch : mOutputHatches) {
                if (!outputSuccess && isValidMetaTileEntity(tHatch) && tHatch.outputsItems()) {
                    if (tHatch.getBaseMetaTileEntity().addStackToSlot(1, single)) outputSuccess = true;
                }
            }
        }
        return outputSuccess;
    }

    public boolean depleteInput(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return false;
        FluidStack aLiquid = GT_Utility.getFluidForFilledItem(aStack, true);
        if (aLiquid != null) return depleteInput(aLiquid);
        for (GT_MetaTileEntity_Hatch_Input tHatch : mInputHatches) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch)) {
                if (GT_Utility.areStacksEqual(aStack, tHatch.getBaseMetaTileEntity().getStackInSlot(0))) {
                    if (tHatch.getBaseMetaTileEntity().getStackInSlot(0).stackSize >= aStack.stackSize) {
                        tHatch.getBaseMetaTileEntity().decrStackSize(0, aStack.stackSize);
                        return true;
                    }
                }
            }
        }
        for (GT_MetaTileEntity_Hatch_InputBus tHatch : mInputBusses) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch)) {
                for (int i = tHatch.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
                    if (GT_Utility.areStacksEqual(aStack, tHatch.getBaseMetaTileEntity().getStackInSlot(i))) {
                        if (tHatch.getBaseMetaTileEntity().getStackInSlot(0).stackSize >= aStack.stackSize) {
                            tHatch.getBaseMetaTileEntity().decrStackSize(0, aStack.stackSize);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<ItemStack> getStoredOutputs() {
        ArrayList<ItemStack> rList = new ArrayList<>();
//        for (GT_MetaTileEntity_Hatch_Output tHatch : mOutputHatches) {
//            if (isValidMetaTileEntity(tHatch)) {
//                rList.add(tHatch.getBaseMetaTileEntity().getStackInSlot(1));
//            }
//        }
        for (GT_MetaTileEntity_Hatch_OutputBus tHatch : mOutputBusses) {
            if (isValidMetaTileEntity(tHatch)) {
                for (int i = tHatch.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
                    rList.add(tHatch.getBaseMetaTileEntity().getStackInSlot(i));
                }
            }
        }
        return rList;
    }

    public ArrayList<FluidStack> getStoredFluids() {
        ArrayList<FluidStack> rList = new ArrayList<>();
        for (GT_MetaTileEntity_Hatch_Input tHatch : mInputHatches) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch) && tHatch.getFillableStack() != null) {
                rList.add(tHatch.getFillableStack());
            }
        }
        return rList;
    }

    public ArrayList<ItemStack> getStoredInputs() {
        ArrayList<ItemStack> rList = new ArrayList<>();
//        for (GT_MetaTileEntity_Hatch_Input tHatch : mInputHatches) {
//            tHatch.mRecipeMap = getRecipeMap();
//            if (isValidMetaTileEntity(tHatch) && tHatch.getBaseMetaTileEntity().getStackInSlot(0) != null) {
//                rList.add(tHatch.getBaseMetaTileEntity().getStackInSlot(0));
//            }
//        }
        for (GT_MetaTileEntity_Hatch_InputBus tHatch : mInputBusses) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch)) {
                for (int i = tHatch.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
                    if (tHatch.getBaseMetaTileEntity().getStackInSlot(i) != null)
                        rList.add(tHatch.getBaseMetaTileEntity().getStackInSlot(i));
                }
            }
        }
        return rList;
    }

    public GT_Recipe_Map getRecipeMap() {
        return null;
    }

    public void updateSlots() {
        for (GT_MetaTileEntity_Hatch_Input tHatch : mInputHatches)
            if (isValidMetaTileEntity(tHatch)) tHatch.updateSlots();
        for (GT_MetaTileEntity_Hatch_InputBus tHatch : mInputBusses)
            if (isValidMetaTileEntity(tHatch)) tHatch.updateSlots();
    }

    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
            ((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output)
            return mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus)
            return mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy)
            return mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo)
            return mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance)
            return mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler)
            return mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
        return false;
    }

    public boolean addMaintenanceToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        }
        return false;
    }

    public boolean addEnergyInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
            val energyInputHatch = (GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity;
            val inputAmperage = energyInputHatch.getBaseMetaTileEntity().getInputAmperage();
            val maxAmps = maxTotalAmperageInput();
            if (maxAmps >= 0 && numEnergyInputAmps + inputAmperage > maxAmps) {
                /*System.out.printf("Too many input amps: %d > %d%n", numEnergyInputAmps + inputAmperage, maxTotalAmperageInput());
                for (val hatch : mEnergyHatches) {
                    System.out.printf("Hatch: %s, amps: %d%n", hatch.getClass().getName(), hatch.getBaseMetaTileEntity().getInputAmperage());
                }*/
                return false;
            }
            numEnergyInputAmps += inputAmperage;
            energyInputHatch.updateTexture(aBaseCasingIndex);
            return mEnergyHatches.add(energyInputHatch);
        }
        return false;
    }

    public boolean addDynamoToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
            val energyOutputHatch = (GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity;
            val outputAmperage = energyOutputHatch.getBaseMetaTileEntity().getOutputAmperage();
            val maxAmps = maxTotalAmperageOutput();
            if (maxAmps >= 0 && numEnergyOutputAmps + outputAmperage > maxAmps) {
                /*System.out.printf("Too many output amps: %d > %d%n", numEnergyOutputAmps + outputAmperage, maxAmps);
                for (val hatch : mDynamoHatches) {
                    System.out.printf("Hatch: %s, amps: %d%n", hatch.getClass().getName(), hatch.getBaseMetaTileEntity().getOutputAmperage());
                }*/
                return false;
            }
            numEnergyOutputAmps += outputAmperage;
            energyOutputHatch.updateTexture(aBaseCasingIndex);
            return mDynamoHatches.add(energyOutputHatch);
        }
        return false;
    }

    public boolean addMufflerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
        }
        return false;
    }

    public boolean addInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
        }
        return false;
    }

    public boolean addOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public String[] getInfoData() {
        int mPollutionReduction=0;
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                mPollutionReduction=Math.max(tHatch.calculatePollutionReduction(100),mPollutionReduction);
            }
        }

        long storedEnergy=0;
        long maxEnergy=0;
        for(GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy+=tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy+=tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }

        return new String[]{
                /* 1*/        StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": " +
                EnumChatFormatting.GREEN + GT_Utility.formatNumbers(mProgresstime / 20) + EnumChatFormatting.RESET + " s / " +
                EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(mMaxProgresstime / 20) + EnumChatFormatting.RESET + " s",
                /* 2*/         StatCollector.translateToLocal("GT5U.multiblock.energy") + ": " +
                EnumChatFormatting.GREEN + GT_Utility.formatNumbers(storedEnergy) + EnumChatFormatting.RESET + " EU / " +
                EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(maxEnergy) + EnumChatFormatting.RESET + " EU",
                /* 3*/         StatCollector.translateToLocal("GT5U.multiblock.usage") + ": " +
                RED + GT_Utility.formatNumbers(-mEUt) + EnumChatFormatting.RESET + " EU/t",
                /* 4*/         StatCollector.translateToLocal("GT5U.multiblock.mei") + ": " +
                EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(getMaxInputVoltage()) + EnumChatFormatting.RESET + " EU/t(*2A) " +
                StatCollector.translateToLocal("GT5U.machines.tier") + ": " +
                EnumChatFormatting.YELLOW + VN[GT_Utility.getTier(getMaxInputVoltage())] + EnumChatFormatting.RESET,
                /* 5*/        StatCollector.translateToLocal("GT5U.multiblock.problems") + ": " +
                RED + (getIdealStatus() - getRepairStatus()) + EnumChatFormatting.RESET + " " +
                StatCollector.translateToLocal("GT5U.multiblock.efficiency") + ": " +
                EnumChatFormatting.YELLOW + Float.toString(mEfficiency / 100.0F) + EnumChatFormatting.RESET + " %",
                /* 6*/      StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": " +
                EnumChatFormatting.GREEN + mPollutionReduction + EnumChatFormatting.RESET + " %"
        };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    protected ItemStack[] getCompactedInputs(){
        //TODO: repalce method with a cleaner one
        ArrayList<ItemStack> tInputList = getStoredInputs();
        int tInputList_sS = tInputList.size();
        for (int i = 0; i < tInputList_sS - 1; i++) {
            for (int j = i + 1; j < tInputList_sS; j++) {
                if (!GT_Utility.areStacksEqual(tInputList.get(i), tInputList.get(j)))
                    continue;
                if (tInputList.get(i).stackSize >= tInputList.get(j).stackSize) {
                    tInputList.remove(j--);
                    tInputList_sS = tInputList.size();
                } else {
                    tInputList.remove(i--);
                    tInputList_sS = tInputList.size();
                    break;
                }
            }
        }
        return tInputList.toArray(new ItemStack[0]);
    }

    protected FluidStack[] getCompactedFluids(){
        //TODO: repalce method with a cleaner one
        ArrayList<FluidStack> tFluidList = getStoredFluids();
        int tFluidList_sS = tFluidList.size();
        for (int i = 0; i < tFluidList_sS - 1; i++) {
            for (int j = i + 1; j < tFluidList_sS; j++) {
                if (!GT_Utility.areFluidsEqual(tFluidList.get(i), tFluidList.get(j)))
                    continue;

                if (tFluidList.get(i).amount >= tFluidList.get(j).amount) {
                    tFluidList.remove(j--);
                    tFluidList_sS = tFluidList.size();
                } else {
                    tFluidList.remove(i--);
                    tFluidList_sS = tFluidList.size();
                    break;
                }
            }
        }
        return tFluidList.toArray(new FluidStack[0]);
    }

    @Override
    public void getWailaBody(ItemStack stack, List<String> currentTip, MovingObjectPosition pos, NBTTagCompound tag, int side) {
        super.getWailaBody(stack, currentTip, pos, tag, side);
        if (tag.getBoolean("incompleteStructure")) {
            currentTip.add(RED + "** INCOMPLETE STRUCTURE **" + RESET);
        }
        currentTip.add((tag.getBoolean("hasProblems") ? RED + "** HAS PROBLEMS **" : GREEN + "No Problems") + RESET + " | "
                + (tag.getBoolean("enabled") ? GREEN + "Enabled" : RED + "Disabled"));

        if (!GT_MetaTileEntity_BasicMachine.fancyWailaProgress(currentTip, tag)) {
            currentTip.add(String.format("Progress: %d s / %d s", tag.getInteger("progress"), tag.getInteger("maxProgress")));
        }
    }

    @Override
    public void getWailaNBT(NBTTagCompound tag, World world, BlockCoord pos) {
        super.getWailaNBT(tag, world, pos);
        final int problems = getIdealStatus() - getRepairStatus();

        tag.setBoolean("hasProblems", problems > 0);
        tag.setBoolean("enabled", this.getBaseMetaTileEntity().isAllowedToWork());
        tag.setInteger("progress", mProgresstime / 20);
        tag.setInteger("progressTicks", mProgresstime);
        tag.setInteger("maxProgress", mMaxProgresstime / 20);
        tag.setInteger("maxProgressTicks", mMaxProgresstime);
        tag.setBoolean("incompleteStructure", (getBaseMetaTileEntity().getErrorDisplayID() & 64) != 0);
    }
}
