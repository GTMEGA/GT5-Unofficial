package gregtech.common.blocks;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.GT_OreVeinStats;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GT_Block_Ore extends GT_Block_Ore_Abstract {
    protected GT_Block_Ore(Materials oreType) {
        super(oreType, String.join(".", "gt.blockore", oreType.mName));
        this.setStepSound(soundTypeStone);

        GT_OreDictUnificator.add(OrePrefixes.oreNormal, oreType, new ItemStack(this));
    }

    @Override
    protected ITexture getOreTexture(Materials oreType) {
        return TextureFactory.builder()
                .addIcon(oreType.mIconSet.mTextures[OrePrefixes.ore.mTextureIndex])
                .setRGBA(oreType.mRGBa)
                .stdOrient()
                .build();
    }

    @Override
    int fortune(int fortune) {
        return (int) (Math.ceil(fortune * 1.66));//ceil for float mods
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        return 30.0F;
    }

    @Override
    public int getHarvestLevel(int metadata) {
        return 3;
    }

    @Override
    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
        if (worldIn.isRemote) {
            return;
        }

        GT_OreVeinStats.decrementOreVeinCount(worldIn, x >> 4, z >> 4);
    }
}
