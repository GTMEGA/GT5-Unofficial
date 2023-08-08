package gregtech.common.blocks;

import gregtech.GT_Mod;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import lombok.AllArgsConstructor;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

import java.util.function.Supplier;

@AllArgsConstructor
public enum GT_Block_Ore_StoneType {
    STONE         (0,
                   true,
                   () -> Blocks.stone.getIcon(0, 0),
                   TextureFactory.of(Blocks.stone)),
    NETHERRACK    (0,
                   true,
                   () -> Blocks.netherrack.getIcon(0, 0),
                   TextureFactory.of(Blocks.netherrack)),
    ENDSTONE      (0,
                   false,
                   () -> Blocks.end_stone.getIcon(0, 0),
                   TextureFactory.of(Blocks.end_stone)),
    BLACK_GRANITE (3,
                   GT_Mod.gregtechproxy.enableBlackGraniteOres && false,
                   Textures.BlockIcons.GRANITE_BLACK_STONE::getIcon,
                   TextureFactory.builder()
                                  .addIcon(Textures.BlockIcons.GRANITE_BLACK_STONE)
                                  .stdOrient()
                                  .build()),
    RED_GRANITE   (3,
                   GT_Mod.gregtechproxy.enableRedGraniteOres && false,
                   Textures.BlockIcons.GRANITE_RED_STONE::getIcon,
                   TextureFactory.builder()
                                 .addIcon(Textures.BlockIcons.GRANITE_RED_STONE)
                                 .stdOrient()
                                 .build()),
    MARBLE        (0,
                   GT_Mod.gregtechproxy.enableMarbleOres && false,
                   Textures.BlockIcons.MARBLE_STONE::getIcon,
                   TextureFactory.builder()
                                 .addIcon(Textures.BlockIcons.MARBLE_STONE)
                                 .stdOrient()
                                 .build()),
    BASALT        (0,
                   GT_Mod.gregtechproxy.enableBasaltOres && false,
                   Textures.BlockIcons.BASALT_STONE::getIcon,
                   TextureFactory.builder()
                                 .addIcon(Textures.BlockIcons.BASALT_STONE)
                                 .stdOrient()
                                 .build()),
    //yeah idk what this next one is for, but I copied it over anyways...Maybe it's the magical 8th entry that makes
    //the preexisting system work?
    NULL          (0,
                   false,
                   () -> Blocks.stone.getIcon(0, 0),
                   TextureFactory.of(Blocks.stone)),
    ;

    final int baseBlockHarvestLevel;
    final boolean isEnabled;
    final Supplier<IIcon> icon;
    final ITexture texture;
}
