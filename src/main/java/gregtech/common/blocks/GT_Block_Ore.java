package gregtech.common.blocks;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.events.GT_OreVeinLocations;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import lombok.val;

import net.minecraft.block.Block;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

public class GT_Block_Ore extends GT_Block_Ore_Abstract {
    protected GT_Block_Ore(Materials oreType) {
        super(oreType, String.join(".", "gt.blockore", oreType.mName));
        this.setStepSound(soundTypeStone);
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
    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
        if (worldIn.isRemote) {
            return;
        }

        val chunkCoord = new ChunkCoordIntPair(x >> 4, z >> 4);
        val dimId = worldIn.provider.dimensionId;

        val data = GT_OreVeinLocations.RecordedOreVeinInChunk.get().get(dimId, chunkCoord);

        if (data != null && data.oresCurrent > 0) {
            data.oresCurrent--;
        }

        GT_OreVeinLocations.updateClients(dimId, chunkCoord, data);
    }
}
