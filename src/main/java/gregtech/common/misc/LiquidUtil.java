package gregtech.common.misc;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidContainerItem;

public class LiquidUtil {

    public static int fillContainerStack(ItemStack stack, EntityPlayer player, FluidStack fluid, boolean simulate) {
        Item item = stack.getItem();
        if (!(item instanceof IFluidContainerItem)) {
            return 0;
        } else {
            IFluidContainerItem container = (IFluidContainerItem)item;
            if (stack.stackSize == 1) {
                return container.fill(stack, fluid, !simulate);
            } else {
                ItemStack testStack = stack.copy();
                testStack.stackSize = 1;

                int amount = container.fill(testStack, fluid, true);
                if (amount <= 0) {
                    return 0;
                } else if (StackUtil.storeInventoryItem(testStack, player, simulate)) {
                    if (!simulate) {
                        stack.stackSize--;
                    }

                    return amount;
                } else {
                    return 0;
                }
            }
        }
    }

    public static FluidStack drainContainerStack(ItemStack stack, EntityPlayer player, int maxAmount, boolean simulate) {
        Item item = stack.getItem();
        if (!(item instanceof IFluidContainerItem)) {
            return null;
        } else {
            IFluidContainerItem container = (IFluidContainerItem)item;
            if (stack.stackSize == 1) {
                return container.drain(stack, maxAmount, !simulate);
            } else {
                ItemStack testStack = stack.copy();
                testStack.stackSize = 1;

                FluidStack ret = container.drain(testStack, maxAmount, true);
                if (ret == null || ret.amount <= 0) {
                    return null;
                } else if (StackUtil.storeInventoryItem(testStack, player, simulate)) {
                    if (!simulate) {
                        stack.stackSize--;
                    }

                    return ret;
                } else {
                    return null;
                }
            }
        }
    }

    public static boolean check(FluidStack fs) {
        return fs.getFluid() != null;
    }

    public static boolean placeFluid(FluidStack fs, World world, int x, int y, int z) {
        if (fs != null && fs.amount >= 1000) {
            Fluid fluid = fs.getFluid();
            Block block = world.getBlock(x, y, z);
            if ((block.isAir(world, x, y, z) || !block.getMaterial().isSolid())
                && fluid.canBePlacedInWorld()
                && (block != fluid.getBlock() || !isFullFluidBlock(world, x, y, z, block))) {
                if (world.provider.isHellWorld && fluid == FluidRegistry.WATER) {
                    world.playSoundEffect(
                            (double)x + 0.5, (double)y + 0.5, (double)z + 0.5, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F
                                         );

                    for (int i = 0; i < 8; i++) {
                        world.spawnParticle("largesmoke", (double)x + Math.random(), (double)y + Math.random(), (double)z + Math.random(), 0.0, 0.0, 0.0);
                    }
                } else {
                    if (!world.isRemote && !block.getMaterial().isSolid() && !block.getMaterial().isLiquid()) {
                        world.func_147480_a(x, y, z, true);
                    }

                    if (fluid == FluidRegistry.WATER) {
                        block = Blocks.flowing_water;
                    } else if (fluid == FluidRegistry.LAVA) {
                        block = Blocks.flowing_lava;
                    } else {
                        block = fluid.getBlock();
                    }

                    int meta = block instanceof BlockFluidBase ? ((BlockFluidBase)block).getMaxRenderHeightMeta() : 0;
                    if (!world.setBlock(x, y, z, block, meta, 3)) {
                        return false;
                    }
                }

                fs.amount -= 1000;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean isFullFluidBlock(World world, int x, int y, int z, Block block) {
        if (!(block instanceof IFluidBlock)) {
            return block != Blocks.water && block != Blocks.flowing_water && block != Blocks.lava && block != Blocks.flowing_lava
                   ? false
                   : world.getBlockMetadata(x, y, z) == 0;
        } else {
            IFluidBlock fBlock = (IFluidBlock)block;
            FluidStack drained = fBlock.drain(world, x, y, z, false);
            return drained != null && drained.amount >= 1000;
        }
    }

}
