package gregtech.api.items;


import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.util.GT_LanguageManager;
import lombok.val;
import lombok.var;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import java.util.List;
import java.util.Map;


public class GT_FluidCell extends Item implements IFluidContainerItem {

    public final IIcon textures[] = new IIcon[2];

    public GT_FluidCell() {
        setUnlocalizedName("gt.fluidcell");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".name", "Fluid Cell");
        setCreativeTab(GregTech_API.TAB_GREGTECH_MATERIALS);
        GameRegistry.registerItem(this, "gt.fluidcell");
    }

    /**
     * @return
     */
    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     *
     * @param item
     * @param tab
     * @param itemList
     */
    @SuppressWarnings("unchecked")
    @Override
    public void getSubItems(final Item item, final CreativeTabs tab, final List itemList) {
        itemList.add(new ItemStack(item, 1, 0));
        for (Map.Entry<String, Fluid> entry : FluidRegistry.getRegisteredFluids().entrySet()) {
            val stack = new ItemStack(item, 1, 0);
            fill(stack, new FluidStack(entry.getValue(), Integer.MAX_VALUE), true);
            itemList.add(stack);
        }
    }

    /**
     * @param register
     */
    @Override
    public void registerIcons(final IIconRegister register) {
        textures[0] = register.registerIcon("gregtech:fluidcell");
        textures[1] = register.registerIcon("gregtech:fluidcell_overlay");
    }

    /**
     * @param container ItemStack which is the fluid container.
     * @return FluidStack representing the fluid in the container, null if the container is empty.
     */
    @Override
    public FluidStack getFluid(final ItemStack container) {
        return FluidStack.loadFluidStackFromNBT(getOrCreateNBT(container).getCompoundTag("Fluid"));
    }

    public NBTTagCompound getOrCreateNBT(final ItemStack stack) {
        if (!stack.hasTagCompound()) {
            val result = new NBTTagCompound();
            stack.setTagCompound(result);
            return result;
        }
        return stack.getTagCompound();
    }

    /**
     * @param container ItemStack which is the fluid container.
     * @return Capacity of this fluid container.
     */
    @Override
    public int getCapacity(final ItemStack container) {
        return 1000;
    }

    /**
     * @param container ItemStack which is the fluid container.
     * @param resource  FluidStack attempting to fill the container.
     * @param doFill    If false, the fill will only be simulated.
     * @return Amount of fluid that was (or would have been, if simulated) filled into the
     * container.
     */
    @Override
    public int fill(final ItemStack stack, final FluidStack resource, final boolean doFill) {
        if (stack.stackSize != 0) {
            return 0;
        } else if (resource == null) {
            return 0;
        } else {
            val nbt = getOrCreateNBT(stack);
            val fluid = nbt.getCompoundTag("Fluid");
            var fluidStored = FluidStack.loadFluidStackFromNBT(fluid);
            if (fluidStored == null) {
                fluidStored = new FluidStack(resource, 0);
            }
            if (!fluidStored.isFluidEqual(resource)) {
                return 0;
            } else {
                val amount = Math.min(1000 - fluidStored.amount, resource.amount);
                if (doFill && amount > 0) {
                    fluidStored.amount += amount;
                    nbt.setTag("Fluid", fluidStored.writeToNBT(fluid));
                }
            }
            return 0;
        }
    }

    /**
     * @param container ItemStack which is the fluid container.
     * @param maxDrain  Maximum amount of fluid to be removed from the container.
     * @param doDrain
     * @return Amount of fluid that was (or would have been, if simulated) drained from the
     * container.
     */
    @Override
    public FluidStack drain(final ItemStack stack, int maxDrain, final boolean doDrain) {
        if (stack.stackSize != 0) {
            return null;
        } else {
            val nbt = getOrCreateNBT(stack);
            val fluid = nbt.getCompoundTag("Fluid");
            val fluidStored = FluidStack.loadFluidStackFromNBT(fluid);
            if (fluidStored == null) {
                return null;
            } else {
                maxDrain = Math.min(fluidStored.amount, maxDrain);
                if (doDrain) {
                    fluidStored.amount -= maxDrain;
                    if (fluidStored.amount <= 0) {
                        nbt.removeTag("Fluid");
                    } else {
                        nbt.setTag("Fluid", fluidStored.writeToNBT(fluid));
                    }
                }
                return new FluidStack(fluidStored, maxDrain);
            }
        }
    }

}
