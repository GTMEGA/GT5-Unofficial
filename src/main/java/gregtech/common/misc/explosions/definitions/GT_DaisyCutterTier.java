package gregtech.common.misc.explosions.definitions;

import gregtech.api.enums.Textures;
import gregtech.common.blocks.GT_Block_Explosive;
import gregtech.common.misc.explosions.GT_Explosion;
import gregtech.common.misc.explosions.IGT_ExplosiveTier;
import gregtech.common.misc.explosions.explosion_logic.GT_DaisyCutterExplosion;
import lombok.*;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Getter
public
enum GT_DaisyCutterTier implements IGT_ExplosiveTier<GT_DaisyCutterTier> {

    MK1("§7Daisy Cutter MKI§r", ExplosiveTextureInfo.getNew("mk1"), ExplosiveFlavorInfo.builder().build(), DaisyCutterParameters.get(20.0).build()),
    MK2("§eDaisy Cutter MKII§r", ExplosiveTextureInfo.getNew("mk2"), ExplosiveFlavorInfo.builder().build(), DaisyCutterParameters.get(48.0).build()),
    MK3("§cDaisy Cutter MKIII§r", ExplosiveTextureInfo.getNew("mk3"), ExplosiveFlavorInfo.builder().build(), DaisyCutterParameters.get(96.0).magic(true).build()),
    ;

    @Getter
    @Setter
    @Builder
    public static class DaisyCutterParameters {

        public static DaisyCutterParametersBuilder get(final double radius) {
            return DaisyCutterParameters.builder().radius(radius);
        }

        private final double radius;

        @Builder.Default
        private double dropChance = 0.001;

        @Builder.Default
        private boolean magic = false;

    }


    private @NonNull final String ELName;

    private @NonNull final ExplosiveTextureInfo textureInfo;

    private final @NonNull IGT_ExplosiveTier.ExplosiveFlavorInfo flavorInfo;

    private final @NonNull DaisyCutterParameters parameters;

    private final @NonNull Class<GT_DaisyCutterExplosion> explosionClass = GT_DaisyCutterExplosion.class;

    private final @NonNull AtomicReference<GT_Block_Explosive<GT_DaisyCutterTier>> blockReference = new AtomicReference<>();

    @Override
    public float getBlockResistance(final GT_Explosion<?> explosion, final World world, final int x, final int y, final int z, final Block block) {
        return 0.0f;
    }

    @Override
    public ResourceLocation getEntityTexture() {
        return Textures.BlockIcons.DAISY_CUTTER.getTextureFile();
    }

    @Override
    public void addExplosiveFlavorText(final List<String> list) {
        val params = getParameters();
        val lines = new String[]{
                "An explosive, developed by a bygone civilization, which blows away only foliage",
                "to make room for industrial bigger-ing. Destroys most items.",
                String.format("Has a radius of %.1f blocks", params.getRadius() / 2),
                };
        Collections.addAll(list, lines);
    }

    @Override
    public boolean isMagic() {
        return parameters.isMagic();
    }
}
