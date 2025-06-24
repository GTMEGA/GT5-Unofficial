package gregtech.common.blocks;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.common.GT_Worldgen_Rubber_Tree;

import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class GT_Block_Rubber_Sapling extends BlockSapling {
    public GT_Block_Rubber_Sapling() {
        this.setHardness(0.0F);
        this.setStepSound(soundTypeGrass);
        this.setBlockName("blockRubberSapling");
        this.setCreativeTab(GregTech_API.TAB_GREGTECH);
        GameRegistry.registerBlock(this, this.getUnlocalizedName());
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(GT_Values.RES_PATH_BLOCK + "misc/blockRubberSapling");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.blockIcon;
    }

    public boolean canBeReplacedByLeaves(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return true;
    }

    public void updateTick(World world, int i, int j, int k, Random random) {
        if (!world.isRemote) {
            if (!this.canBlockStay(world, i, j, k)) {
                this.dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
                world.setBlockToAir(i, j, k);
            } else {
                if (world.getBlockLightValue(i, j + 1, k) >= 9 && random.nextInt(30) == 0) {
                    this.func_149878_d(world, i, j, k, random);
                }
            }
        }
    }

    public void func_149878_d(World world, int i, int j, int k, Random random) {
        GT_Worldgen_Rubber_Tree.grow(world, i, j, k, random);
    }

    public int damageDropped(int i) {
        return 0;
    }

    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float a, float b, float c) {
        if (world.isRemote) {
            return false;
        }

        ItemStack equipped = entityplayer.getCurrentEquippedItem();
        if (equipped == null) {
            return false;
        } else {
            if (equipped.getItem() == Items.dye && equipped.getItemDamage() == 15) {
                this.func_149878_d(world, i, j, k, world.rand);
                if (!entityplayer.capabilities.isCreativeMode) {
                    equipped.stackSize--;
                }

                entityplayer.swingItem();
            }

            return false;
        }
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(this));
    }

    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
        return EnumPlantType.Plains;
    }
}
