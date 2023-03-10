package gregtech.api.interfaces;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent;

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

    default float breakBlockAround(int side, int xStartPos, int yStartPos, int xLength, int yLenghth, IToolStats stats, ItemStack stack, World world, int x, int y, int z,
                                   EntityPlayerMP player, float damgePerBlock, float timeToTakeCenter, float digSpeed) {
        int harvestLevel = stack.getItem().getHarvestLevel(stack, "");

        int DX;
        int DY;
        int DZ;
        boolean downUp = side < 2;
        boolean northSouth = side < 4;
        float rotation = player.rotationYawHead < 0 ? 360f + player.rotationYawHead : player.rotationYawHead;

        BiFunction<Integer, Integer, Boolean> xCheck = (value, len) -> value < len;
        BiFunction<Integer, Integer, Boolean> yCheck = (value, len) -> value > len;

        int xIterate = 1;
        int yIterate = -1;

        if (downUp) {
            boolean north = rotation > 135 && rotation < 225;// 180
            //boolean east = rotation >= 225 && rotation <= 315;//270
            boolean west = rotation >= 45 && rotation <= 135;// 90
            boolean south = rotation < 45 || rotation > 315;//0

            //x = north-south
            //y = east-west

            if (south) {
                int tempX = xLength;
                xLength = yLenghth;
                yLenghth = tempX;
                //invert Y direction
                yLenghth *= -1;
                yLenghth -= yStartPos;
                yStartPos *= -1;
                //invert X direction when looking down
                if (side == 0) {
                    xLength += xStartPos;
                } else {
                    xCheck = (value, len) -> value > len;
                    xLength *= -1;
                    xLength -= xStartPos;
                    xIterate = -1;
                    xStartPos *= -1;
                }
            } else if (west) {
                //invert X direction
                xCheck = (value, len) -> value > len;
                xLength *= -1;
                xLength -= xStartPos;
                xIterate = -1;
                xStartPos *= -1;
                //invert Y direction when looking up
                if (side == 0) {
                    yLenghth *= -1;
                    yLenghth -= yStartPos;
                    yStartPos *= -1;
                } else {
                    yCheck = (value, len) -> value < len;
                    yIterate = 1;
                    yLenghth += yStartPos;
                }
            } else if (north) {
                int tempX = xLength;
                xLength = yLenghth;
                yLenghth = tempX;
                //dont invert Y direction
                yCheck = (value, len) -> value < len;
                yIterate = 1;
                yLenghth += yStartPos;

                //invert X direction when looking UP
                if (side == 0) {
                    xCheck = (value, len) -> value > len;
                    xLength *= -1;
                    xLength -= xStartPos;
                    xIterate = -1;
                    xStartPos *= -1;
                } else {
                    xLength += xStartPos;
                }
            } else {
                //dont invert X direction
                xLength += xStartPos;

                //invert Y direction when looking DOWN
                if (side == 0) {
                    yCheck = (value, len) -> value < len;
                    yIterate = 1;
                    yLenghth += yStartPos;
                } else {
                    yLenghth *= -1;
                    yLenghth -= yStartPos;
                    yStartPos *= -1;
                }
            }
        } else {
            yLenghth *= -1;
            yLenghth -= yStartPos;
            yStartPos *= -1;
            if (northSouth) {
                if (side == 2) {
                    xLength *= -1;
                    xLength -= xStartPos;
                    xIterate = -1;
                    xStartPos *= -1;
                    xCheck = (value, len) -> value > len;
                } else {
                    xLength += xStartPos;
                }
            } else {
                if (side == 5) {
                    xLength *= -1;
                    xLength -= xStartPos;
                    xIterate = -1;
                    xStartPos *= -1;
                    xCheck = (value, len) -> value > len;
                } else {
                    xLength += xStartPos;
                }
            }
        }
        float tooldamage = 0;
        for (int Y = yStartPos; yCheck.apply(Y, yLenghth); Y += yIterate) {
            for (int X = xStartPos; xCheck.apply(X, xLength); X += xIterate) {
                if (Y != 0 || X != 0) {
                    if (downUp) {
                        //im to lazy to fix this
                        DX = x + Y;
                        DY = y;
                        DZ = z + X;
                    } else if (northSouth) {
                        DX = x + X;
                        DY = y + Y;
                        DZ = z;
                    } else {
                        DX = x;
                        DY = y + Y;
                        DZ = z + X;
                    }
                    Block block = world.getBlock(DX, DY, DZ);
                    float blockHardness = block.getBlockHardness(world, DX, DY, DZ);
                    if (breakBlock(stack, stats, harvestLevel, blockHardness, timeToTakeCenter, digSpeed, world, DX, DY, DZ, player, block))
                        tooldamage += blockHardness * damgePerBlock;
                }
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
