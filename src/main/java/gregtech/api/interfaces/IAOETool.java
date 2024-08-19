package gregtech.api.interfaces;

import lombok.val;
import lombok.var;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.BlockEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public interface IAOETool {

    int getMaxAOESize();

    void onRightClick(ItemStack stack, EntityPlayer player);

    float getDamageMultiplyer();

    float getSpeedReduction();

    float getDigSpeed(float digSpeed, IToolStats stats, ItemStack stack);

    float onBlockDestroyed(ItemStack stack, IToolStats stats, float damagePerBlock, float timeToTakeCenter, float digSpeed, World world, Block block, int x, int y, int z, EntityLivingBase player);

    default void setAOE(NBTTagCompound statNbt, int value) {
        statNbt.setInteger("AOE", value);
    }

    default int getAOE(NBTTagCompound statNbt) {
        return statNbt.getInteger("AOE");
    }

    default List<ChunkPosition> getBlocksInAOERange(int side,
                                                    int x,
                                                    int y,
                                                    int z,
                                                    IBlockAccess world,
                                                    int xOffset,
                                                    int yOffset,
                                                    int xRange,
                                                    int yRange,
                                                    EntityPlayer player) {
        val blocksToMine = new ArrayList<ChunkPosition>();

        if (world == null) {
            return blocksToMine;
        }

        val f3Facing = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        var playerLookDirection = ForgeDirection.UNKNOWN;
        switch (f3Facing) {
            case 0:
                playerLookDirection = ForgeDirection.SOUTH;
                break;
            case 1:
                playerLookDirection = ForgeDirection.WEST;
                break;
            case 2:
                playerLookDirection = ForgeDirection.NORTH;
                break;
            case 3:
                playerLookDirection = ForgeDirection.EAST;
                break;
        }

        val blockFace = ForgeDirection.getOrientation(side);

        var yAxis = ForgeDirection.UNKNOWN;
        switch (blockFace) {
            case UP:
                yAxis = playerLookDirection.getOpposite();
                break;
            case DOWN:
                yAxis = playerLookDirection;
                break;
            case SOUTH:
            case WEST:
            case NORTH:
            case EAST:
                yAxis = ForgeDirection.DOWN;
                break;
        }

        val xAxis = blockFace.getRotation(yAxis);

        var startPosition = new ChunkPosition(x + xAxis.offsetX * xOffset + yAxis.offsetX * yOffset,
                                              y + xAxis.offsetY * xOffset + yAxis.offsetY * yOffset,
                                              z + xAxis.offsetZ * xOffset + yAxis.offsetZ * yOffset);

        for (var dX = 0; dX < xRange; dX++) {
            for (var dY = 0; dY < yRange; dY++) {
                val position = new ChunkPosition(startPosition.chunkPosX + xAxis.offsetX * dX + yAxis.offsetX * dY,
                                                 startPosition.chunkPosY + xAxis.offsetY * dX + yAxis.offsetY * dY,
                                                 startPosition.chunkPosZ + xAxis.offsetZ * dX + yAxis.offsetZ * dY);

                if (world.isAirBlock(position.chunkPosX, position.chunkPosY, position.chunkPosZ)) {
                    continue;
                }

                blocksToMine.add(position);
            }
        }

        return blocksToMine;
    }

    default float breakBlockAround(int side,
                                   int xStartPos,
                                   int yStartPos,
                                   int xLength,
                                   int yLenghth,
                                   IToolStats stats,
                                   ItemStack stack,
                                   World world,
                                   int x,
                                   int y,
                                   int z,
                                   EntityPlayerMP player,
                                   float damgePerBlock,
                                   float timeToTakeCenter,
                                   float digSpeed) {
        int harvestLevel = stack.getItem().getHarvestLevel(stack, "");

        val blocksToMine = this.getBlocksInAOERange(side, x, y, z, world, xStartPos, yStartPos, xLength, yLenghth, player);

        var tooldamage = 0F;

        for (val blockPosition : blocksToMine) {
            val block = world.getBlock(blockPosition.chunkPosX, blockPosition.chunkPosY, blockPosition.chunkPosZ);
            val hardness = block.getBlockHardness(world, blockPosition.chunkPosX, blockPosition.chunkPosY, blockPosition.chunkPosZ);

            val brokeBlock = this.breakBlock(stack,
                                             stats,
                                             harvestLevel,
                                             hardness,
                                             timeToTakeCenter,
                                             digSpeed,
                                             world,
                                             blockPosition.chunkPosX,
                                             blockPosition.chunkPosY,
                                             blockPosition.chunkPosZ,
                                             player,
                                             block);

            if (brokeBlock) {
                tooldamage += hardness * damgePerBlock;
            }
        }

        return tooldamage;
    }

    default float getTimeToBreak(float digSpeed, float blockHardness) {
        return digSpeed / (blockHardness * 30f);
    }

    default boolean breakBlock(ItemStack aStack, IToolStats stats, int harvestLevel, float blockHardness, float timeToTakeCenter, float digSpeed, World world, int x, int y, int z,
                               EntityPlayerMP player, Block block) {
        // most of this code is stolen from TiC
        if (world.isAirBlock(x, y, z))
            return false;
        int blockMeta = world.getBlockMetadata(x, y, z);
        int blockHarvestLevel = block.getHarvestLevel(blockMeta);
        if (!canHarvest(harvestLevel, blockHarvestLevel, blockHardness, timeToTakeCenter, digSpeed, block, blockMeta, stats))
            return false;
        BlockEvent.BreakEvent event = ForgeHooks.onBlockBreakEvent(world, player.theItemInWorldManager.getGameType(), player, x, y, z);
        if (event.isCanceled())
            return false;
        block.onBlockHarvested(world, x, y, z, blockMeta, player);
        block.harvestBlock(world, player, x, y, z, blockMeta);
        block.onBlockDestroyedByPlayer(world, x, y, z, blockMeta);
        if (block.removedByPlayer(world, player, x, y, z, true)) {
            block.dropXpOnBlockBreak(world, x, y, z, event.getExpToDrop());
        }

        player.playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, world));
        return true;
    }

    //a fast version of get digSpeed
    //and block stats dont matter for the get digSpeed mult or even matter for this function
    static boolean canHarvest(int toolHarvestLevel, int blockHarvestLevel, float blockHardness, float timeToTakeCenter, float digSpeed, Block block, int meta, IToolStats stats) {
        if (toolHarvestLevel < blockHarvestLevel || blockHardness < 0) return false;
        return stats.isMinableBlock(block, (byte) meta) &&
                (((IAOETool) stats).getTimeToBreak(digSpeed, blockHardness) * 12 > timeToTakeCenter);
    }
}
