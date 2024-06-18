package gregtech.common.misc.explosions.definitions;

import gregtech.api.enums.Textures;
import gregtech.common.blocks.GT_Block_Explosive;
import gregtech.common.misc.explosions.GT_Explosion;
import gregtech.common.misc.explosions.IGT_ExplosiveTier;
import gregtech.common.misc.explosions.explosion_logic.GT_FlatBombExplosion;
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
enum GT_FlatBombTier implements IGT_ExplosiveTier<GT_FlatBombTier> {

    MK1("Flat Bomb Mk 1", ExplosiveTextureInfo.getNew("mk1"), ExplosiveFlavorInfo.builder().build(), FlatBombParameters.get(4, 10).build()),
    MK2("Flat Bomb Mk 2", ExplosiveTextureInfo.getNew("mk2"), ExplosiveFlavorInfo.builder().build(), FlatBombParameters.get(6, 16).build()),
    MK3("Flat Bomb Mk 3", ExplosiveTextureInfo.getNew("mk3"), ExplosiveFlavorInfo.builder().build(), FlatBombParameters.get(6, 20).magic(true).build()),
    ;

    @Getter
    @Setter
    @Builder
    public static class FlatBombParameters {

        public static FlatBombParametersBuilder get(final double maxDepth, final double maxRadius) {
            return FlatBombParameters.builder().maxDepth(maxDepth).maxRadius(maxRadius);
        }

        private final double maxDepth, maxRadius;

        @Builder.Default
        private double depthVariation = 1.5;

        @Builder.Default
        private double clayChance = 1.0;

        @Builder.Default
        private double otherChance = 0.001;

        @Builder.Default
        private boolean magic = false;

    }

    private final @NonNull String ELName;

    private final @NonNull ExplosiveTextureInfo textureInfo;

    private final @NonNull ExplosiveFlavorInfo flavorInfo;

    private final FlatBombParameters parameters;

    private final @NonNull Class<GT_FlatBombExplosion> explosionClass = GT_FlatBombExplosion.class;

    private final @NonNull AtomicReference<GT_Block_Explosive<GT_FlatBombTier>> blockReference = new AtomicReference<>();

    @Override
    //TODO: Make this not mine any blocks beneath itself.
    public float getBlockResistance(final GT_Explosion<?> explosion, final World world, final int x, final int y, final int z, final Block block) {
        val base = explosion.getBlockResistance(x, y, z, block);
        if (base > 10) {
            return base;
        }
        return 0;
    }

    @Override
    public ResourceLocation getEntityTexture() {
        return Textures.BlockIcons.TUNEX.getTextureFile();
    }

    @Override
    public void addExplosiveFlavorText(final List<String> list) {
        val params = getParameters();
        val lines = new String[]{
                "Flattens an area along the placed direction",
                String.format("Has a maximum radius of %.1f blocks and a depth of %.1f blocks", params.getMaxRadius(), params.getMaxDepth()),
                "creating a flat area. Destroys most items, with clay being an exception.",
                };
        Collections.addAll(list, lines);
    }

    @Override
    public boolean isMagic() {
        return parameters.isMagic();
    }

}
