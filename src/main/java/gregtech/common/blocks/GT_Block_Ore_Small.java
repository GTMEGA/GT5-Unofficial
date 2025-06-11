package gregtech.common.blocks;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_OreDictUnificator;

import net.minecraft.item.ItemStack;

public class GT_Block_Ore_Small extends GT_Block_Ore_Abstract {
    protected GT_Block_Ore_Small(Materials oreType) {
        super(oreType, String.join(".", "gt.blockore.small", oreType.mName));

        GT_OreDictUnificator.add(OrePrefixes.oreSmall, oreType, new ItemStack(this));
    }

    @Override
    protected ITexture getOreTexture(Materials oreType) {
        return TextureFactory.builder()
                .addIcon(oreType.mIconSet.mTextures[OrePrefixes.oreSmall.mTextureIndex])
                .setRGBA(oreType.mRGBa)
                .stdOrient()
                .build();
    }
}
