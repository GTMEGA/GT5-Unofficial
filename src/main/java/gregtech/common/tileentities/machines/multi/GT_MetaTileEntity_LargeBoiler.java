package gregtech.common.tileentities.machines.multi;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.GT_Mod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_Values.STEAM_PER_WATER;
import static gregtech.api.enums.GT_Values.EU_PER_STEAM;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public abstract class GT_MetaTileEntity_LargeBoiler extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_LargeBoiler> {
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final ClassValue<IStructureDefinition<GT_MetaTileEntity_LargeBoiler>> STRUCTURE_DEFINITION =new ClassValue<IStructureDefinition<GT_MetaTileEntity_LargeBoiler>>() {
        @Override
        protected IStructureDefinition<GT_MetaTileEntity_LargeBoiler> computeValue(Class<?> type) {
            return  StructureDefinition.<GT_MetaTileEntity_LargeBoiler>builder()
                    .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][]{
                            {"ccc", "ccc", "ccc"},
                            {"ccc", "cPc", "ccc"},
                            {"ccc", "cPc", "ccc"},
                            {"ccc", "cPc", "ccc"},
                            {"f~f", "fff", "fff"},
                    }))
                    .addElement('P', lazy(t -> ofBlock(t.getPipeBlock(), t.getPipeMeta())))
                    .addElement('c', lazy(t -> ofChain(
                            ofHatchAdder(GT_MetaTileEntity_LargeBoiler::addOutputToMachineList, t.getCasingTextureIndex(), 2),
                            onElementPass(GT_MetaTileEntity_LargeBoiler::onCasingAdded, ofBlock(t.getCasingBlock(), t.getCasingMeta()))
                    )))
                    .addElement('f', lazy(t -> ofChain(
                            ofHatchAdder(GT_MetaTileEntity_LargeBoiler::addMaintenanceToMachineList, t.getFireboxTextureIndex(), 1),
                            ofHatchAdder(GT_MetaTileEntity_LargeBoiler::addInputToMachineList, t.getFireboxTextureIndex(), 1),
                            ofHatchAdder(GT_MetaTileEntity_LargeBoiler::addMufflerToMachineList, t.getFireboxTextureIndex(), 1),
                            onElementPass(GT_MetaTileEntity_LargeBoiler::onFireboxAdded, ofBlock(t.getFireboxBlock(), t.getFireboxMeta()))
                    )))
                    .build();
        }
    };
    private boolean firstRun = true;
    private int mSuperEfficencyIncrease = 0;
    private int integratedCircuitConfig = 0; //Steam output is reduced by 1000L per config
    private int excessWater = 0; //Eliminate rounding errors for water
    private int excessFuel = 0; //Eliminate rounding errors for fuels that burn half items
    private int excessProjectedEU = 0; //Eliminate rounding errors from throttling the boiler
    private int mCasingAmount;
    private int mFireboxAmount;
    protected int throttleTimer = 0;
    protected FluidStack leftOverSteam;

    public static final double DIESEL_FACTOR = 0.85;

    public GT_MetaTileEntity_LargeBoiler(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_LargeBoiler(String aName) {
        super(aName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt          = new GT_Multiblock_Tooltip_Builder();
        val                                 euPerSecond = getEUt() * 20;
        val steamPerSecond = euPerSecond / EU_PER_STEAM;
        tt.addMachineType("Boiler")
          .addInfo("Produces " + (steamPerSecond * (runtimeBoost(20) / 20f) + "L of Steam with 1 Coal at " + steamPerSecond) + "L/s")
          .addInfo("Consumes " + steamPerSecond / STEAM_PER_WATER + "L of water per second.")
          .addInfo("Throttles fuel consumption to 10% if there is nowhere for steam to go.")
          .addInfo("A programmed circuit in the controller can throttle the boiler (-250L/s per config)")
          .addInfo(String.format("Diesel fuels have %.1f%% efficiency - Takes %.2f seconds to heat up", 100 * DIESEL_FACTOR, 500.0 / getEfficiencyIncrease()))//? check semifluid again
          .addSeparator()
          .beginStructureBlock(3, 5, 3, false)
          .addController("Front bottom")
          .addCasingInfo(getCasingMaterial() + " " + getCasingBlockType() + " Casing", 24)//?
          .addOtherStructurePart(getCasingMaterial() + " Fire Boxes", "Bottom layer, 3 minimum")
          .addOtherStructurePart(getCasingMaterial() + " Pipe Casing Blocks", "Inner 3 blocks")
          .addMaintenanceHatch("Any firebox", 1)
          .addMufflerHatch("Any firebox", 1)
          .addInputBus("Solid fuel, Any firebox", 1)
          .addInputHatch("Liquid fuel, Any firebox", 1)
          .addStructureInfo("You can use either, or both")
          .addInputHatch("Water, Any firebox", 1)
          .addOutputHatch("Steam, any casing", 2);
        return tt;
    }

    public abstract String getCasingMaterial();

    public abstract Block getCasingBlock();

    public abstract String getCasingBlockType();

    public abstract byte getCasingMeta();

    public abstract byte getCasingTextureIndex();

    public abstract Block getPipeBlock();

    public abstract byte getPipeMeta();

    public abstract Block getFireboxBlock();

    public abstract byte getFireboxMeta();

    public abstract byte getFireboxTextureIndex();

    public abstract int getEUt();

    public abstract int getEfficiencyIncrease();

    public int getThrottleMax() {
        return 10;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive) return new ITexture[]{
                    BlockIcons.getCasingTextureForId(getCasingTextureIndex()),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_LARGE_BOILER_ACTIVE).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_LARGE_BOILER_ACTIVE_GLOW).extFacing().glow().build()};
            return new ITexture[]{
                    BlockIcons.getCasingTextureForId(getCasingTextureIndex()),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_LARGE_BOILER).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_LARGE_BOILER_GLOW).extFacing().glow().build()};
        }
        return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(getCasingTextureIndex())};
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "LargeBoiler.png");
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        //Do we have an integrated circuit with a valid configuration?
        if (mInventory[1] != null && mInventory[1].getUnlocalizedName().startsWith("gt.integrated_circuit")) {
            int circuit_config = mInventory[1].getItemDamage();
            if (circuit_config >= 1 && circuit_config <= 25) {
                // If so, overwrite the current config
                this.integratedCircuitConfig = circuit_config;
            }
        } else {
            //If not, set the config to zero
            this.integratedCircuitConfig = 0;
        }

        this.mSuperEfficencyIncrease = 0;
        for (GT_Recipe tRecipe : GT_Recipe.GT_Recipe_Map.sDieselFuels.mRecipeList) {
            FluidStack tFluid = GT_Utility.getFluidForFilledItem(tRecipe.getRepresentativeInput(0), true);
            if (tFluid != null && tRecipe.mSpecialValue > 1) {
                tFluid.amount = 1000;
                if (depleteInput(tFluid)) {
                    this.mMaxProgresstime = adjustBurnTimeForConfig(runtimeBoost((int) (tRecipe.mSpecialValue * DIESEL_FACTOR)));
                    this.mEUt = adjustEUtForConfig(getEUt());
                    this.mEfficiencyIncrease = this.mMaxProgresstime * getEfficiencyIncrease() * 4;
                    return true;
                }
            }
        }
        for (GT_Recipe tRecipe : GT_Recipe.GT_Recipe_Map.sDenseLiquidFuels.mRecipeList) {
            FluidStack tFluid = GT_Utility.getFluidForFilledItem(tRecipe.getRepresentativeInput(0), true);
            if (tFluid != null) {
                tFluid.amount = 1000;
                if (depleteInput(tFluid)) {
                    this.mMaxProgresstime = adjustBurnTimeForConfig(Math.max(1, runtimeBoost(tRecipe.mSpecialValue * 2)));
                    this.mEUt = adjustEUtForConfig(getEUt());
                    this.mEfficiencyIncrease = this.mMaxProgresstime * getEfficiencyIncrease();
                    return true;
                }
            }
        }
        ArrayList<ItemStack> tInputList = getStoredInputs();
        if (!tInputList.isEmpty()) {
            for (ItemStack tInput : tInputList) {
                if (tInput != GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Lava, 1)){
                    if (GT_Utility.getFluidForFilledItem(tInput, true) == null && (this.mMaxProgresstime = GT_ModHandler.getFuelValue(tInput) / 80) > 0) {
                        this.excessFuel += GT_ModHandler.getFuelValue(tInput) % 80;
                        this.mMaxProgresstime += this.excessFuel / 80;
                        this.excessFuel %= 80;
                        this.mMaxProgresstime = adjustBurnTimeForConfig(runtimeBoost(this.mMaxProgresstime));
                        this.mEUt = adjustEUtForConfig(getEUt());
                        this.mEfficiencyIncrease = this.mMaxProgresstime * getEfficiencyIncrease();
                        this.mOutputItems = new ItemStack[]{GT_Utility.getContainerItem(tInput, true)};
                        tInput.stackSize -= 1;
                        updateSlots();
                        if (this.mEfficiencyIncrease > 5000) {
                            this.mEfficiencyIncrease = 0;
                            this.mSuperEfficencyIncrease = 20;
                        }
                        return true;
                    }
                }
            }
        }
        this.mMaxProgresstime = 0;
        this.mEUt = 0;
        this.throttleTimer = 0;
        return false;
    }

    abstract int runtimeBoost(int mTime);

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (this.mEUt > 0) {
            int maxEff = getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000);
            if (++throttleTimer < getThrottleMax()
                    && mEfficiency >= maxEff
                    && leftOverSteam != null
                    && leftOverSteam.amount > 0) {
                if (addOutput(leftOverSteam) || leftOverSteam.amount <= 0)
                    leftOverSteam = null;
                --mProgresstime;
                return true;
            } else {
                leftOverSteam = null;
                if (this.mSuperEfficencyIncrease > 0)
                    mEfficiency = Math.max(0, Math.min(mEfficiency + mSuperEfficencyIncrease, maxEff));
                long steamGenerated = (this.mEUt * (long) this.mEfficiency / 10000L);
                steamGenerated = (steamGenerated/ EU_PER_STEAM);
                if (steamGenerated > 0) {
                    long amount = (steamGenerated + STEAM_PER_WATER) / STEAM_PER_WATER;
                    excessWater += (int) (amount * STEAM_PER_WATER - steamGenerated);
                    amount -= excessWater / STEAM_PER_WATER;
                    excessWater %= STEAM_PER_WATER;
                    if (depleteInput(Materials.Water.getFluid(amount)) || depleteInput(GT_ModHandler.getDistilledWater(amount))) {
                        val steam = GT_ModHandler.getSteam(steamGenerated);
                        if (addOutput(steam) || mEfficiency < maxEff) return true;
                        leftOverSteam = steam;
                        if (++throttleTimer > getThrottleMax()) {
                            throttleTimer = 0;
                        }
                    } else {
                        GT_Log.exp.println("Boiler "+this.mName+" had no Water!");
                        explodeMultiblock();
                    }
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean addOutput(FluidStack aLiquid) {
        if (aLiquid == null) return false;
        return dumpFluidBoiler(mOutputHatches, aLiquid, false);
    }

    protected static boolean dumpFluidBoiler(List<GT_MetaTileEntity_Hatch_Output> aOutputHatches, FluidStack copiedFluidStack, boolean restrictiveHatchesOnly){
        for (GT_MetaTileEntity_Hatch_Output tHatch : aOutputHatches) {
            if (!isValidMetaTileEntity(tHatch) || (restrictiveHatchesOnly && tHatch.mMode == 0)) {
                continue;
            }
            if (GT_ModHandler.isSteam(copiedFluidStack)) {
                if (!tHatch.outputsSteam()) {
                    continue;
                }
            } else {
                if (!tHatch.outputsLiquids()) {
                    continue;
                }
                if (tHatch.isFluidLocked() && tHatch.getLockedFluidName() != null && !tHatch.getLockedFluidName().equals(copiedFluidStack.getFluid().getName())) {
                    continue;
                }
            }
            int tAmount = tHatch.fill(copiedFluidStack, false);
            if (tAmount >= copiedFluidStack.amount) {
                tHatch.fill(copiedFluidStack, true);
                return true;
            } else if (tAmount > 0) {
                copiedFluidStack.amount = copiedFluidStack.amount - tHatch.fill(copiedFluidStack, true);
            }
        }
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("excessFuel", excessFuel);
        aNBT.setInteger("excessWater", excessWater);
        aNBT.setInteger("excessProjectedEU", excessProjectedEU);
        aNBT.setInteger("throttleTimer", throttleTimer);
        if (leftOverSteam != null && leftOverSteam.amount > 0)
            aNBT.setInteger("leftOverSteam", leftOverSteam.amount);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        excessFuel = aNBT.getInteger("excessFuel");
        excessWater = aNBT.getInteger("excessWater");
        excessProjectedEU = aNBT.getInteger("excessProjectedEU");
        throttleTimer = aNBT.getInteger("throttleTimer");
        val steamAmount = aNBT.getInteger("leftOverSteam");
        if (steamAmount > 0) {
            leftOverSteam = GT_ModHandler.getSteam(steamAmount);
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (mProgresstime > 0 && firstRun) {
            firstRun = false;
            GT_Mod.achievements.issueAchievement(aBaseMetaTileEntity.getWorld().getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()), "extremepressure");
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_LargeBoiler> getStructureDefinition() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    private void onCasingAdded() {
        mCasingAmount++;
    }

    private void onFireboxAdded() {
        mFireboxAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mFireboxAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 4, 0) && mCasingAmount >= 24 && mFireboxAmount >= 3 &&
                mMaintenanceHatches.size() == 1 && !mMufflerHatches.isEmpty();
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        int adjustedEUOutput = Math.max(25, getEUt() - 25 * integratedCircuitConfig);
        return Math.max(1, 12 * adjustedEUOutput / getEUt());
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    private int adjustEUtForConfig(int rawEUt) {
        int adjustedSteamOutput = rawEUt - 25 * integratedCircuitConfig;
        return Math.max(adjustedSteamOutput, 25);
    }

    private int adjustBurnTimeForConfig(int rawBurnTime) {
        if (mEfficiency < getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)) {
            return rawBurnTime;
        }
        int adjustedEUt = Math.max(25, getEUt() - 25 * integratedCircuitConfig);
        int adjustedBurnTime = rawBurnTime * getEUt() / adjustedEUt;
        this.excessProjectedEU += getEUt() * rawBurnTime - adjustedEUt * adjustedBurnTime;
        adjustedBurnTime += this.excessProjectedEU / adjustedEUt;
        this.excessProjectedEU %= adjustedEUt;
        return adjustedBurnTime;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 4, 0);
    }
}
