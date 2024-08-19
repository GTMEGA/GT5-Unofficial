package gregtech.common.blocks;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;

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
}
