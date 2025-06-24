package gregtech.common.blocks;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;

import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class GT_Block_Rubber_Leaves extends BlockLeaves {
    public GT_Block_Rubber_Leaves() {
        super();

        this.setCreativeTab(GregTech_API.TAB_GREGTECH);
        this.setBlockName("blockRubberLeaves");

        GameRegistry.registerBlock(this, this.getUnlocalizedName());
    }

    public void dropBlockAsItemWithChance(World world, int par2, int par3, int par4, int meta, float par6, int fortune) {
        if (!world.isRemote && world.rand.nextInt(35) == 0) {
            Item item = this.getItemDropped(meta, world.rand, fortune);
            this.dropBlockAsItem(world, par2, par3, par4, new ItemStack(item, 1, this.damageDropped(meta)));
        }
    }

    public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<>();
        ret.add(new ItemStack(this, 1, world.getBlockMetadata(x, y, z) & 3));
        return ret;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(GT_Values.RES_PATH_BLOCK + "misc/blockRubberLeaves");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.blockIcon;
    }

    public String[] func_150125_e() {
        return null;
    }

    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 30;
    }

    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 20;
    }
}
