package gregtech.common.misc.explosions.definitions;

import gregtech.api.enums.Textures;
import gregtech.common.blocks.GT_Block_Explosive;
import gregtech.common.misc.explosions.GT_Explosion;
import gregtech.common.misc.explosions.IGT_ExplosiveTier;
import gregtech.common.misc.explosions.explosion_logic.GT_TunnelExplosion;
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
enum GT_TunnelExplosiveTier implements IGT_ExplosiveTier<GT_TunnelExplosiveTier> {

    MK1("§7Tunnel Explosive MKI§r", ExplosiveTextureInfo.getOld(), ExplosiveFlavorInfo.builder().build(), TunnelExplosiveParameters.getDefaults(30, 4).build()),
    MK2("§eTunnel Explosive MKII§r", ExplosiveTextureInfo.getOld(), ExplosiveFlavorInfo.builder().build(), TunnelExplosiveParameters.getDefaults(45, 5).build()),
    MK3("§cTunnel Explosive MKIII§r", ExplosiveTextureInfo.getNew("mk3"), ExplosiveFlavorInfo.builder().build(), TunnelExplosiveParameters.getDefaults(55, 6).magic(true).build()),
    ;

    @Getter
    @Setter
    @Builder
    public static class TunnelExplosiveParameters {

        public static TunnelExplosiveParametersBuilder getDefaults(final double maxLength, final double maxRadius) {
            return TunnelExplosiveParameters.builder().maxLength(maxLength).maxRadius(maxRadius);
        }

        private final double maxLength, maxRadius;

        @Builder.Default
        private float radiusVariation = 1.5f;

        @Builder.Default
        private double clayChance = 1.0;

        @Builder.Default
        private double otherChance = 0.001;

        @Builder.Default
        private boolean magic = false;

    }


    private @NonNull final String ELName;

    private @NonNull final ExplosiveTextureInfo textureInfo;

    private final @NonNull IGT_ExplosiveTier.ExplosiveFlavorInfo flavorInfo;


    private final @NonNull TunnelExplosiveParameters parameters;

    private final @NonNull Class<GT_TunnelExplosion> explosionClass = GT_TunnelExplosion.class;

    private final @NonNull AtomicReference<GT_Block_Explosive<GT_TunnelExplosiveTier>> blockReference = new AtomicReference<>();

    @Override
    public float getBlockResistance(final GT_Explosion<?> explosion, final World world, final int x, final int y, final int z, final Block block) {
        val base = explosion.getGtExplosive().defaultBlockResistance(explosion, world, x, y, z, block);
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
                "Produces a powerful blast in the placed direction,",
                String.format("Has a maximum length of %.1f blocks and a radius of %.1f blocks", params.getMaxLength(), params.getMaxRadius()),
                "creating a small tunnel. Destroys most items, with clay being an exception.",
                };
        Collections.addAll(list, lines);
    }

    @Override
    public boolean isMagic() {
        return parameters.isMagic();
    }

}
