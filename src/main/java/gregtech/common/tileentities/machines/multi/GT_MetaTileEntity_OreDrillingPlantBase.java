package gregtech.common.tileentities.machines.multi;

import gregtech.GT_Mod;
import gregtech.api.enums.ItemList;
import gregtech.api.events.GT_OreVeinLocations;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_ChunkManager;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import gregtech.common.blocks.GT_Block_Ore;
import gregtech.common.fluids.GT_OreSlurry;
import lombok.val;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static gregtech.api.enums.GT_Values.VN;

public abstract class GT_MetaTileEntity_OreDrillingPlantBase extends GT_MetaTileEntity_DrillerBase {
    protected static final String NBT_KEY_CURRENT_SLURRY = "currentSlurryType";
    protected static final String NBT_KEY_CURRENT_MINING = "currentSlurry";
    protected static final String NBT_KEY_CURRENT_MINING_AMOUNT = "currentSlurryAmount";

    protected static final int BASE_FLUID_PER_TICK = 100;//mb;

    private final ArrayList<ChunkPosition> oreBlockPositions = new ArrayList<>();
    private final Map<GT_Block_Ore, Integer> oreTypeFrequency = new HashMap<>();
    private GT_OreSlurry slurryType = null;


    protected int mTier = 1;
    private int chunkRadiusConfig = getRadiusInChunks();
    protected FluidStack currentOreSlurry = null;

    private static final int[] FORTUNE = {12, 15, 18, 21, 24};

    GT_MetaTileEntity_OreDrillingPlantBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    GT_MetaTileEntity_OreDrillingPlantBase(String aName) {
        super(aName);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("chunkRadiusConfig", chunkRadiusConfig);

        if (this.slurryType != null) {
            aNBT.setString(NBT_KEY_CURRENT_SLURRY, this.slurryType.getName());
        }

        if (this.currentOreSlurry != null) {
            val nbt = new NBTTagCompound();

            this.currentOreSlurry.writeToNBT(nbt);
            aNBT.setTag(NBT_KEY_CURRENT_MINING, nbt);

            aNBT.setInteger(NBT_KEY_CURRENT_MINING_AMOUNT, this.currentOreSlurry.amount);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        if (aNBT.hasKey("chunkRadiusConfig")) {
            chunkRadiusConfig = aNBT.getInteger("chunkRadiusConfig");
        }

        if (aNBT.hasKey(NBT_KEY_CURRENT_SLURRY)) {
            this.slurryType = (GT_OreSlurry) FluidRegistry.getFluid(aNBT.getString(NBT_KEY_CURRENT_SLURRY));
        }

        if (aNBT.hasKey(NBT_KEY_CURRENT_MINING)) {
            this.currentOreSlurry = GT_Utility.loadFluid(aNBT, NBT_KEY_CURRENT_MINING);
            this.currentOreSlurry.amount = aNBT.getInteger(NBT_KEY_CURRENT_MINING_AMOUNT);
        }
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "OreDrillingPlant.png");
    }

    abstract protected int fortune();

    abstract protected int perTickFluidStackMultiplier();

    protected final int perTickFluidStackSize() {
        return BASE_FLUID_PER_TICK * this.perTickFluidStackMultiplier();
    }

    @Override
    protected void updateCoordinates() {
        super.updateCoordinates();

        if (this.slurryType == null) {
            val chunkCoord = new ChunkCoordIntPair(this.getXDrill() >> 4, this.getZDrill() >> 4);
            val oreMix = GT_OreVeinLocations.getOreVeinInChunk(this.getBaseMetaTileEntity().getWorld().provider.dimensionId,
                                                               chunkCoord);

            this.slurryType = GT_OreSlurry.slurries.get(oreMix);

            if (this.slurryType == null) {
                this.slurryType = this.scanSlurry();
            }
        }
    }

    @Override
    protected boolean workingDownward(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe, int yHead, int oldYHead) {
        if (yHead != oldYHead) {
            this.oreBlockPositions.clear();
        }

        if (mWorkChunkNeedsReload && mChunkLoadingEnabled) { // ask to load machine itself
            GT_ChunkManager.requestChunkLoad((TileEntity) getBaseMetaTileEntity(), null);
            mWorkChunkNeedsReload = false;
        }

        this.fillMineListIfEmpty(xDrill, yDrill, zDrill, xPipe, zPipe, yHead);

        if (oreBlockPositions.isEmpty()) {
            switch (this.tryLowerPipeState()) {
                case 2: mMaxProgresstime = 0; return false;
                case 3: workState = STATE_UPWARD; return true;
                case 1: workState = STATE_AT_BOTTOM; return true;
            }

            //new layer - fill again
            this.fillMineListIfEmpty(xDrill, yDrill, zDrill, xPipe, zPipe, yHead);
        }

        return processOreList();
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        if (isOutputFull()) {
            addFluidOutputs(mOutputFluids);
        }
        return super.checkRecipe(aStack);
    }

    protected boolean isOutputFull() {
        if (mOutputFluids != null) {
            for (int i = 0; i < mOutputFluids.length; i++) {
                if (mOutputFluids[i] != null) return true;
            }
        }
        return false;
    }

    private boolean processOreList() {
        if (isOutputFull()) {
            mMaxProgresstime = 0;
            mEUt = 0;
            return false;
        }
        int amountTransferred = 0;

        if (this.currentOreSlurry != null && this.currentOreSlurry.amount != 0) {
            if (!this.tryConsumeDrillingFluid()) {
                return false;
            }

            amountTransferred = this.decreaseOutput();
            if (amountTransferred == this.perTickFluidStackSize()) {
                return true;
            }
        }

        ChunkPosition oreBlockPos = null;
        int x = 0, y = 0, z = 0;
        Block oreBlock = null;
        int oreBlockMetadata = 0;

        while ((oreBlock == null || !isBigOreBlock(oreBlock,oreBlockMetadata)) && !oreBlockPositions.isEmpty()) {
            oreBlockPos = oreBlockPositions.remove(0);
            x = oreBlockPos.chunkPosX;
            y = oreBlockPos.chunkPosY;
            z = oreBlockPos.chunkPosZ;

            if (GT_Utility.eraseBlockByFakePlayer(getFakePlayer(getBaseMetaTileEntity()), x, y, z, true)) {
                oreBlock = getBaseMetaTileEntity().getBlock(x, y, z);
            }

            oreBlockMetadata = getBaseMetaTileEntity().getWorld().getBlockMetadata(x, y, z);
        }

        if (!tryConsumeDrillingFluid()) {
            oreBlockPositions.add(0, oreBlockPos);
            return false;
        }

        if (oreBlock != null && isBigOreBlock(oreBlock,oreBlockMetadata)) {
            //TODO replace with proper rock variant
            this.currentOreSlurry = new FluidStack(this.slurryType, this.fortune() * BASE_FLUID_PER_TICK);
            getBaseMetaTileEntity().getWorld().setBlock(x, y, z, Blocks.cobblestone, 0, 3);

            if (amountTransferred > 0) {
                this.decreaseOutputSecondary(this.perTickFluidStackSize() - amountTransferred);
                return true;
            }
            decreaseOutput();
        }

        return true;
    }

    protected int decreaseOutput() {
        if (mOutputFluids == null || mOutputFluids.length != 2) {
            mOutputFluids = new FluidStack[2];
        }

        int transferAmount = Math.min(this.perTickFluidStackSize(), this.currentOreSlurry.amount);

        mOutputFluids[0] = this.currentOreSlurry.copy();
        mOutputFluids[0].amount = transferAmount;

        this.currentOreSlurry.amount -= transferAmount;
        if (this.currentOreSlurry.amount == 0) {
            this.currentOreSlurry = null;
        }

        return transferAmount;
    }

    protected void decreaseOutputSecondary(int max) {
        int transferAmount = Math.min(Math.min(this.perTickFluidStackSize(), max),
                                      this.currentOreSlurry.amount);

        mOutputFluids[1] = this.currentOreSlurry.copy();
        mOutputFluids[1].amount = transferAmount;

        this.currentOreSlurry.amount -= transferAmount;

        if (this.currentOreSlurry.amount == 0) {
            this.currentOreSlurry = null;
        }
    }

    protected void addFluidOutputs(FluidStack[] mOutputFluids2) {
        for (int i = 0; i < mOutputFluids2.length; i++) {
            if (mOutputFluids2[i] == null) continue;
            if (!addOutput(mOutputFluids2[i])) continue;
            mOutputFluids2[i] = null;
        }
    }

    protected static boolean isBigOreBlock(Block block, int meta) {
        if (block instanceof GT_Block_Ore) {
            return true;
        }
        return false;
    }


    @Override
    protected boolean workingAtBottom(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe, int yHead, int oldYHead) {
        if (!mChunkLoadingEnabled)
            return super.workingAtBottom(aStack, xDrill, yDrill, zDrill, xPipe, zPipe, yHead, oldYHead);

        if (mCurrentChunk == null) {
            createInitialWorkingChunk();
            return true;
        }

        if (mWorkChunkNeedsReload) {
            GT_ChunkManager.requestChunkLoad((TileEntity)getBaseMetaTileEntity(), mCurrentChunk);
            mWorkChunkNeedsReload = false;
            return true;
        }
        if (oreBlockPositions.isEmpty()){
            fillChunkMineList(yHead, yDrill);
            if (oreBlockPositions.isEmpty()) {
                GT_ChunkManager.releaseChunk((TileEntity)getBaseMetaTileEntity(), mCurrentChunk);
                if (!moveToNextChunk(xDrill >> 4, zDrill >> 4))
                    workState = STATE_UPWARD;
                return true;
            }
        }
        return processOreList();
    }

    private void createInitialWorkingChunk() {
        final int centerX = getXDrill() >> 4;
        final int centerZ = getZDrill() >> 4;
        // use corner closest to the drill as mining area center
        final int leftRight = (getXDrill() - (centerX << 4)) < 8 ? 0 : 1;
        final int topBottom = (getZDrill() - (centerZ << 4)) < 8 ? 0 : 1;

        mCurrentChunk = new ChunkCoordIntPair(centerX - chunkRadiusConfig + leftRight, centerZ - chunkRadiusConfig + topBottom);
        GT_ChunkManager.requestChunkLoad((TileEntity)getBaseMetaTileEntity(), mCurrentChunk);
        mWorkChunkNeedsReload = false;
    }

    @Override
    protected boolean workingUpward(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe, int yHead, int oldYHead) {
        if (!mChunkLoadingEnabled || oreBlockPositions.isEmpty())
            return super.workingUpward(aStack, xDrill, yDrill, zDrill, xPipe, zPipe, yHead, oldYHead);
        boolean result = processOreList();
        if (oreBlockPositions.isEmpty())
            GT_ChunkManager.releaseTicket((TileEntity)getBaseMetaTileEntity());
        return result;
    }

    private boolean moveToNextChunk(int centerX, int centerZ){
        if (mCurrentChunk == null)
            return false;
        // use corner closest to the drill as mining area center
        final int left = centerX - chunkRadiusConfig + ((getXDrill() - (centerX << 4)) < 8 ? 0 : 1);
        final int right = left + chunkRadiusConfig * 2;
        final int bottom = centerZ + chunkRadiusConfig + ((getZDrill() - (centerZ << 4)) < 8 ? 0 : 1);

        int nextChunkX = mCurrentChunk.chunkXPos + 1;
        int nextChunkZ = mCurrentChunk.chunkZPos;

        // step to the next chunk
        if (nextChunkX >= right) {
            nextChunkX = left;
            ++nextChunkZ;
        }

        // skip center chunk - dug in workingDownward()
        if (nextChunkX == centerX && nextChunkZ == centerZ) {
            ++nextChunkX;

            if (nextChunkX >= right) {
                nextChunkX = left;
                ++nextChunkZ;
            }
        }

        if (nextChunkZ >= bottom) {
            mCurrentChunk = null;
            return false;
        }
        mCurrentChunk = new ChunkCoordIntPair(nextChunkX, nextChunkZ);
        GT_ChunkManager.requestChunkLoad((TileEntity)getBaseMetaTileEntity(), new ChunkCoordIntPair(nextChunkX, nextChunkZ));
        return true;
    }
    
    @Override
    protected boolean checkHatches(){
        return !this.mMaintenanceHatches.isEmpty() &&
               !this.mInputHatches.isEmpty() &&
               !this.mOutputHatches.isEmpty() &&
               !this.mEnergyHatches.isEmpty();
    }

    @Override
    protected void setElectricityStats() {
        this.mEfficiency = getCurrentEfficiency(null);
        this.mEfficiencyIncrease = 10000;
        int tier = Math.max(1, GT_Utility.getTier(getMaxInputVoltage()));
        this.mEUt = -3 * (1 << (tier << 1));
        this.mMaxProgresstime = ((workState == STATE_DOWNWARD || workState == STATE_AT_BOTTOM) ? getBaseProgressTime() / 12 : 80) / (1 <<tier);
        this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
    }

    private ItemStack getBlockDrops(final GT_Block_Ore oreBlock, int posX, int posY, int posZ) {
        final int blockMeta = getBaseMetaTileEntity().getMetaID(posX, posY, posZ);
        return oreBlock.getDropsMining(getBaseMetaTileEntity().getWorld(), posX, posY, posZ, blockMeta, fortune());
    }

    private boolean tryConsumeDrillingFluid() {
        if (!depleteInput(new FluidStack(ItemList.sDrillingFluid, drillingFluidConsumption()))) {
            mMaxProgresstime = 0;
            return false;
        }
        return true;
    }

    protected static int drillingFluidConsumption() {
        return 100;
    }

    private void fillChunkMineList(int yHead, int yDrill) {
        if (mCurrentChunk == null || !oreBlockPositions.isEmpty())
            return;
        final int minX = mCurrentChunk.chunkXPos << 4;
        final int maxX = minX + 16;
        final int minZ = mCurrentChunk.chunkZPos << 4;
        final int maxZ = minZ + 16;
        for (int x = minX; x < maxX; ++x) {
            for (int z = minZ; z < maxZ; ++z) {
                for (int y = yHead; y < yDrill; ++y) {
                    this.tryAddOreBlockToMineList(x, y, z);
                }
            }
        }
    }

    private void fillMineListIfEmpty(int xDrill, int yDrill, int zDrill, int xPipe, int zPipe, int yHead) {
        if (!oreBlockPositions.isEmpty())
            return;

        tryAddOreBlockToMineList(xPipe, yHead - 1, zPipe);
        if (yHead == yDrill)
            return; //skip controller block layer

        if (mChunkLoadingEnabled) {
            int startX = (xDrill >> 4) << 4;
            int startZ = (zDrill >> 4) << 4;
            for (int x = startX; x < (startX + 16); ++x)
                for (int z = startZ; z < (startZ + 16); ++z)
                    tryAddOreBlockToMineList(x, yHead, z);
        } else {
            int radius = chunkRadiusConfig << 4;
            for (int xOff = -radius; xOff <= radius; xOff++)
                for (int zOff = -radius; zOff <= radius; zOff++)
                    tryAddOreBlockToMineList(xDrill + xOff, yHead, zDrill + zOff);
        }
    }

    private void tryAddOreBlockToMineList(int x, int y, int z) {
        val block = getBaseMetaTileEntity().getBlock(x, y, z);
        val blockMeta = getBaseMetaTileEntity().getMetaID(x, y, z);
        val blockPos = new ChunkPosition(x, y, z);

        if (!oreBlockPositions.contains(blockPos) && isBigOreBlock(block, blockMeta)) {
            oreBlockPositions.add(blockPos);
        }
    }

    private GT_OreSlurry scanSlurry() {
        val world = this.getBaseMetaTileEntity().getWorld();
        val oreVeinLikelihood = new HashMap<GT_Worldgen_GT_Ore_Layer, Integer>();

        val tileEntity = this.getBaseMetaTileEntity();
        val xPos = tileEntity.getXCoord();
        val yPos = tileEntity.getYCoord();
        val zPos = tileEntity.getZCoord();

        this.oreTypeFrequency.clear();

        val scanRange = 7;
        for (int y = yPos; y > 0; y--) {
            for (int x = xPos - scanRange; x < xPos + scanRange; x++) {
                for (int z = zPos - scanRange; z < zPos + scanRange; z++) {
                    val block = world.getBlock(x, y, z);

                    if (!(block instanceof GT_Block_Ore)) {
                        continue;
                    }

                    val ore = (GT_Block_Ore) block;

                    val frequency = this.oreTypeFrequency.computeIfAbsent(ore, key -> 0);
                    this.oreTypeFrequency.put(ore, frequency + 1);
                }
            }
        }

        for (val ore : this.oreTypeFrequency.keySet()) {
            val material = ore.material();

            for (val oreMix : GT_Worldgen_GT_Ore_Layer.sList) {
                if (oreMix.containsMaterial(material)) {
                    val frequency = oreVeinLikelihood.computeIfAbsent(oreMix, key -> 0);

                    oreVeinLikelihood.put(oreMix, frequency + 1);
                }
            }
        }

        val oreVeinEntry = oreVeinLikelihood.entrySet()
                                            .stream()
                                            .max(Map.Entry.comparingByValue())
                                            .orElse(null);

        if (oreVeinEntry != null) {
            return GT_OreSlurry.slurries.get(oreVeinEntry.getKey());
        }

        GT_Mod.GT_FML_LOGGER.warn("Null ore slurry selected");

        return null;
    }

    protected abstract int getRadiusInChunks();

    protected abstract int getBaseProgressTime();

    protected GT_Multiblock_Tooltip_Builder createTooltip(String tierSuffix) {
        String casings = getCasingBlockItem().get(0).getDisplayName();
        
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        //TODO: Non auto-generated tooltips...
		tt.addMachineType("Miner")
		.addInfo("Controller Block for the Ore Drilling Rig MK" + (tierSuffix != null ? tierSuffix : ""))
		.addInfo("Mines from the closest ore chunk")
        .addInfo("Harvests massive amounts of ore slurry over a long period of time.")
        .addInfo(EnumChatFormatting.YELLOW + "Slurry contents are determined by the nearest ore vein" + EnumChatFormatting.RESET)
        .addInfo("Consumes " + drillingFluidConsumption() + "L" + " of Drilling Fluid per operation")
        .addInfo("Ore to slurry yield: " + EnumChatFormatting.YELLOW + fortune() +"00%" + EnumChatFormatting.RESET)
		.addSeparator()
		.beginStructureBlock(3, 7, 3, false)
		.addController("Front bottom")
		.addStructureInfo(casings + " form the 3x1x3 Base")
		.addOtherStructurePart(casings, " 1x3x1 pillar above the center of the base (2 minimum total)")
		.addOtherStructurePart(getFrameMaterial().mName + " Frame Boxes", "Each pillar's side and 1x3x1 on top")
		.addEnergyHatch(VN[getMinTier()] + "+, Any base casing", 1)
		.addMaintenanceHatch("Any base casing", 1)
		.addInputBus("Mining Pipes, optional, any base casing", 1)
		.addInputHatch("Drilling Fluid, any base casing", 1)
		.addOutputBus("Any base casing", 1)
		.toolTipFinisher("Gregtech");
		return tt;
    }

    @Override
    public String[] getInfoData() {
        final int diameter = chunkRadiusConfig * 2;
        return new String[]{
                EnumChatFormatting.BLUE+StatCollector.translateToLocal("GT5U.machines.minermulti")+EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.machines.workarea")+": " + EnumChatFormatting.GREEN + diameter + "x" + diameter +
                EnumChatFormatting.RESET+" " + StatCollector.translateToLocal("GT5U.machines.chunks")
        };
    }
}
