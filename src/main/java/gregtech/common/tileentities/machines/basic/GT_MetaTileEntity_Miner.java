package gregtech.common.tileentities.machines.basic;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Ore;


import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.common.util.FakePlayer;

import java.util.ArrayList;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.debugBlockMiner;

public class GT_MetaTileEntity_Miner extends GT_MetaTileEntity_BasicMachine {
    private static final ItemStack MINING_PIPE = new ItemStack(Blocks.iron_bars);
    private static final Block MINING_PIPE_BLOCK = GT_Utility.getBlockFromStack(MINING_PIPE);
    private static final Block MINING_PIPE_TIP_BLOCK = Blocks.iron_block;

    int drillY = 0;
    boolean isPickingPipes;
    boolean waitMiningPipe;
    static final int[] RADIUS = {8, 8, 16, 24, 32}; //Miner radius per tier
    static final int[] SPEED = {640, 640, 320, 160, 80}; //Miner cycle time per tier
    static final int[] ENERGY = {8, 8, 30, 120, 480}; //Miner energy consumption per tier

    // Fortune bonus per tier
    static final int[] FORTUNE = {1, 2, 5, 8};

    private int radiusConfig; //Miner configured radius
    private final ArrayList<ChunkPosition> oreBlockPositions = new ArrayList<>();

    public GT_MetaTileEntity_Miner(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1,
                new String[]{
                        "Digging ore instead of you",
                        "Use Screwdriver to regulate work area",
                        ENERGY[aTier] + " EU/t, " + SPEED[aTier] / 20 + " sec per block, no stuttering",
                        "Maximum work area " + (RADIUS[aTier] * 2 + 1) + "x" + (RADIUS[aTier] * 2 + 1),
                        "Collects " + (aTier + 2 * aTier) + " chunks of ore per block mined.",
                        "Mines small and normal ores."},
                2, 2, "Miner.png", "",
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/miner/OVERLAY_SIDE_ACTIVE")),
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/miner/OVERLAY_SIDE")),
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/miner/OVERLAY_FRONT_ACTIVE")),
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/miner/OVERLAY_FRONT")),
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/miner/OVERLAY_TOP_ACTIVE")),
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/miner/OVERLAY_TOP")),
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/miner/OVERLAY_BOTTOM_ACTIVE")),
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/miner/OVERLAY_BOTTOM")));
        radiusConfig = RADIUS[mTier];
}

    public GT_MetaTileEntity_Miner(String aName, int aTier, String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
        radiusConfig = RADIUS[mTier];
}

    public GT_MetaTileEntity_Miner(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 2, 2, aGUIName, aNEIName);
        radiusConfig = RADIUS[mTier];

    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Miner(mName, mTier, mDescriptionArray, mTextures, mGUIName, mNEIName);
    }

    @Override
    protected boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, aSide, aStack) && aStack.getItem() == MINING_PIPE.getItem();
    }

    public boolean hasFreeSpace() {
        for (int i = getOutputSlot(); i < getOutputSlot() + 2; i++) {
            if (mInventory[i] != null) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
        if (aSide != getBaseMetaTileEntity().getFrontFacing() && aSide != mMainFacing) {
            if (aPlayer.isSneaking()) {
                if (radiusConfig >= 0) {
                    radiusConfig--;
                }
                if (radiusConfig < 0)
                    radiusConfig = RADIUS[mTier];
            } else {
                if (radiusConfig <= RADIUS[mTier]) {
                    radiusConfig++;
                }
                if (radiusConfig > RADIUS[mTier])
                    radiusConfig = 0;
            }
            GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("GT5U.machines.workareaset") + " " + (radiusConfig * 2 + 1) + "x" + (radiusConfig * 2 + 1));//TODO Add translation support
            oreBlockPositions.clear();
            fillOreList(getBaseMetaTileEntity());
        }
    }
    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        fillOreList(aBaseMetaTileEntity);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aBaseMetaTileEntity.isAllowedToWork() && aBaseMetaTileEntity.isUniversalEnergyStored(ENERGY[mTier] * (SPEED[mTier] - mProgresstime)) && hasFreeSpace()) {
                miningPipe:
                if (waitMiningPipe) {
                    mMaxProgresstime = 0;
                    for (int i = 0; i < mInputSlotCount; i++) {
                        ItemStack s = getInputAt(i);
                        if (s != null && s.getItem() == MINING_PIPE.getItem() && s.stackSize > 0) {
                            waitMiningPipe = false;
                            break miningPipe;
                        }
                    }
                    if (debugBlockMiner) {
                        GT_Log.out.println("MINER: Pipe found in input");
                    }
                    return;
                }
                aBaseMetaTileEntity.decreaseStoredEnergyUnits(ENERGY[mTier], true);
                mMaxProgresstime = SPEED[mTier];
            } else {
                mMaxProgresstime = 0;
                 if (debugBlockMiner) {
                    if (!aBaseMetaTileEntity.isAllowedToWork()) {
                        GT_Log.out.println("MINER: Disabled");
                    }
                    if (!aBaseMetaTileEntity.isUniversalEnergyStored(ENERGY[mTier] * (SPEED[mTier] - mProgresstime))) {
                        GT_Log.out.println("MINER: Not enough energy yet, want " + (ENERGY[mTier] * SPEED[mTier]) + " have " + aBaseMetaTileEntity.getUniversalEnergyStored());
                    }
                    if (!hasFreeSpace()) {
                        GT_Log.out.println("MINER: No free space");
                    }
                }
                return;
            }
            if (mProgresstime == SPEED[mTier] - 1) {
                if (isPickingPipes) {
                    if (drillY == 0) {
                        aBaseMetaTileEntity.disableWorking();
                        isPickingPipes = false;
                        if (debugBlockMiner) {
                            GT_Log.out.println("MINER: Completed picking pipes");
                        }
                    } else if (aBaseMetaTileEntity.getBlockOffset(0, drillY, 0) == MINING_PIPE_TIP_BLOCK || aBaseMetaTileEntity.getBlockOffset(0, drillY, 0) == MINING_PIPE_BLOCK) {
                        mOutputItems[0] = MINING_PIPE.copy();
                        mOutputItems[0].stackSize = 1;
                        aBaseMetaTileEntity.getWorld().setBlockToAir(aBaseMetaTileEntity.getXCoord(), aBaseMetaTileEntity.getYCoord() + drillY, aBaseMetaTileEntity.getZCoord());
                        drillY++;
                    }
                    return;
                }
                if (drillY == 0 || oreBlockPositions.isEmpty()) {
                    moveOneDown(aBaseMetaTileEntity);
                } else {
                    ChunkPosition oreBlockPos;
                    int x = 0, y = 0, z = 0;
                    Block oreBlock;
                    int oreBlockMetadata = 0;
                    do {
                        oreBlockPos = oreBlockPositions.remove(0);
                        oreBlock = aBaseMetaTileEntity.getBlockOffset(oreBlockPos.chunkPosX, oreBlockPos.chunkPosY, oreBlockPos.chunkPosZ);
                        x = aBaseMetaTileEntity.getXCoord() + oreBlockPos.chunkPosX;
                        y = aBaseMetaTileEntity.getYCoord() + oreBlockPos.chunkPosY;
                        z = aBaseMetaTileEntity.getZCoord() + oreBlockPos.chunkPosZ;
                        oreBlockMetadata = getBaseMetaTileEntity().getWorld().getBlockMetadata(x, y, z);
                    } // someone else might have removed the block
                    while (!GT_Utility.isOre(oreBlock, oreBlockMetadata) && !oreBlockPositions.isEmpty());

                    if (GT_Utility.isOre(oreBlock, oreBlockMetadata)) {
                        mineBlock(aBaseMetaTileEntity, oreBlock, x, y, z);
                    }
                }
            }
        }
    }
    private void fillOreList(IGregTechTileEntity aBaseMetaTileEntity) {
        if (drillY == 0)
            return;
        for (int z = -radiusConfig; z <= radiusConfig; ++z) {
            for (int x = -radiusConfig; x <= radiusConfig; ++x) {
                Block block = aBaseMetaTileEntity.getBlockOffset(x, drillY, z);
                int blockMeta = aBaseMetaTileEntity.getMetaIDOffset(x, drillY, z);

                if (GT_Utility.isOre(block, blockMeta)) {
                    this.oreBlockPositions.add(new ChunkPosition(x, drillY, z));
                }
            }
        }
    }
    @Override
    public long maxEUStore() {
        return mTier == 1 ? 4096 : V[mTier] * 64;
    }

    public boolean moveOneDown(IGregTechTileEntity aBaseMetaTileEntity) {
        int xCoord = aBaseMetaTileEntity.getXCoord();
        int zCoord = aBaseMetaTileEntity.getZCoord();
        short yCoord = aBaseMetaTileEntity.getYCoord();
        if (yCoord + drillY - 1 < 0
                || GT_Utility.getBlockHardnessAt(aBaseMetaTileEntity.getWorld(), xCoord, yCoord + drillY - 1, zCoord) < 0
                || !GT_Utility.setBlockByFakePlayer(getFakePlayer(aBaseMetaTileEntity), xCoord, yCoord + drillY - 1, zCoord, MINING_PIPE_TIP_BLOCK, 0, true)) {
            isPickingPipes = true;
            if (debugBlockMiner) {
                GT_Log.out.println("MINER: Hit bottom? Hit block with -1 hardness? Unable to set mining pipe tip?");
            }
            return false;
        }
        if (aBaseMetaTileEntity.getBlockOffset(0, drillY, 0) == MINING_PIPE_TIP_BLOCK) {
            aBaseMetaTileEntity.getWorld().setBlock(xCoord, yCoord + drillY, zCoord, MINING_PIPE_BLOCK);
        }
        miningPipes:
        {
            for (int i = 0; i < mInputSlotCount; i++) {
                ItemStack s = getInputAt(i);
                if (s != null && s.getItem() == MINING_PIPE.getItem() && s.stackSize > 0) {
                    s.stackSize--;
                    if (s.stackSize == 0) {
                        mInventory[getInputSlot() + i] = null;
                    }
                    break miningPipes;
                }
            }
            waitMiningPipe = true;
            return false;
        }
        Block block = aBaseMetaTileEntity.getBlockOffset(0, drillY - 1, 0);
        if (block != Blocks.air) {
            mineBlock(aBaseMetaTileEntity, block, xCoord, yCoord + drillY - 1, zCoord);
        }
        aBaseMetaTileEntity.getWorld().setBlock(xCoord, yCoord + drillY - 1, zCoord, MINING_PIPE_TIP_BLOCK);
        drillY--;
        fillOreList(aBaseMetaTileEntity);
        return true;
    }

    public void mineBlock(IGregTechTileEntity aBaseMetaTileEntity, Block block, int x, int y, int z) {
        if (!GT_Utility.eraseBlockByFakePlayer(getFakePlayer(aBaseMetaTileEntity), x, y, z, true)) {
            if (debugBlockMiner)
                GT_Log.out.println("MINER: FakePlayer cannot mine block at " + x + ", " + y + ", " + z);
        } else {
            ArrayList<ItemStack> drops = getBlockDrops(block, x, y, z);
            if (drops.size() > 0)
                mOutputItems[0] = drops.get(0);
            if (drops.size() > 1)
                mOutputItems[1] = drops.get(1);

            //TODO nether ores should replace with netherrack
            aBaseMetaTileEntity.getWorld().setBlock(x, y, z, Blocks.cobblestone, 0, 3);
            if (debugBlockMiner)
                GT_Log.out.println("MINER: Mining GT ore block at " + x + " " + y + " " + z);
        }
    }

    private ArrayList<ItemStack> getBlockDrops(final Block oreBlock, int posX, int posY, int posZ) {
        final int blockMeta = getBaseMetaTileEntity().getMetaID(posX, posY, posZ);
        final int tIndex = mTier >= FORTUNE.length ? FORTUNE[FORTUNE.length - 1] : FORTUNE[mTier];
        return oreBlock.getDrops(getBaseMetaTileEntity().getWorld(), posX, posY, posZ, blockMeta, tIndex);
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        super.setItemNBT(aNBT);
        aNBT.setByte("mTier", mTier);
        aNBT.setInteger("radiusConfig", radiusConfig);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("isPickingPipe", isPickingPipes);
        aNBT.setInteger("drillY", drillY);
        aNBT.setInteger("radiusConfig", radiusConfig);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        isPickingPipes = aNBT.getBoolean("isPickingPipe");
        drillY = aNBT.getInteger("drillY");
        if (aNBT.hasKey("radiusConfig"))
            radiusConfig = aNBT.getInteger("radiusConfig");
    }

    private FakePlayer mFakePlayer = null;

    protected FakePlayer getFakePlayer(IGregTechTileEntity aBaseTile) {
        if (mFakePlayer == null)
            mFakePlayer = GT_Utility.getFakePlayer(aBaseTile);
        if (mFakePlayer != null) {
            mFakePlayer.setWorld(aBaseTile.getWorld());
            mFakePlayer.setPosition(aBaseTile.getXCoord(), aBaseTile.getYCoord(), aBaseTile.getZCoord());
        }
        return mFakePlayer;
    }
    
    @Override
    public String[] getInfoData() {
        return new String[]{
                EnumChatFormatting.BLUE+StatCollector.translateToLocal("GT5U.machines.miner")+EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.machines.workarea")+": " + EnumChatFormatting.GREEN + (radiusConfig * 2 + 1)+ 
                EnumChatFormatting.RESET+" " + StatCollector.translateToLocal("GT5U.machines.blocks")
        };
    }

}
