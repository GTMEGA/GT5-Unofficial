package gregtech.common.blocks;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.items.GT_Generic_Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;


public class GT_Block_Storage extends GT_Generic_Block {

    protected GT_Block_Storage(Class<? extends ItemBlock> aItemClass, String aName, Material aMaterial) {
        super(aItemClass, aName, aMaterial);
        setStepSound(soundTypeMetal);
        setCreativeTab(GregTech_API.TAB_GREGTECH_MATERIALS);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return true;
    }

    @Override
    public float getBlockHardness(World aWorld, int aX, int aY, int aZ) {
        return Blocks.iron_block.getBlockHardness(aWorld, aX, aY, aZ);
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 1;
    }

    @Override
    public Item getItemDropped(int par1, Random par2Random, int par3) {
        return Item.getItemFromBlock(this);
    }

    @Override
    public int damageDropped(int par1) {
        return par1;
    }

    @Override
    public float getExplosionResistance(Entity aTNT) {
        return Blocks.iron_block.getExplosionResistance(aTNT);
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal(this.mUnlocalizedName + ".name");
    }

    @Override
    public String getUnlocalizedName() {
        return this.mUnlocalizedName;
    }

    @Override
    public int getDamageValue(World par1World, int par2, int par3, int par4) {
        return par1World.getBlockMetadata(par2, par3, par4);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        for (int i = 0; i < 16; i++) {
            if (!(new ItemStack(aItem, 1, i).getDisplayName().contains(".name"))) {
                aList.add(new ItemStack(aItem, 1, i));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister aIconRegister) {
    }

    @Override
    public boolean isNormalCube(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return true;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return false;
    }

    @Override
    public String getHarvestTool(int aMeta) {
        return "pickaxe";
    }

    @Override
    public int getHarvestLevel(int aMeta) {
        return 1;
    }

}
