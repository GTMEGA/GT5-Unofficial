package gregtech.common.misc.explosions.definitions;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.common.blocks.GT_Block_Explosive;
import gregtech.common.blocks.GT_Block_Ore_Abstract;
import gregtech.common.misc.explosions.GT_Explosion;
import gregtech.common.misc.explosions.IGT_ExplosiveTier;
import gregtech.common.misc.explosions.explosion_logic.GT_MiningExplosion;
import lombok.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Getter
public
enum GT_MiningExplosiveTier implements IGT_ExplosiveTier<GT_MiningExplosiveTier> {
    MK1("Mining Explosive Mk 1", ExplosiveTextureInfo.getOld(), ExplosiveFlavorInfo.builder().build(), MiningExplosiveParameters.getDefaults(4.0f, 3).build()),
    MK2("Mining Explosive Mk 2", ExplosiveTextureInfo.getOld(), ExplosiveFlavorInfo.builder().build(), MiningExplosiveParameters.getDefaults(5.0f, 4).build()),
    MK3("Mining Explosive Mk 3", ExplosiveTextureInfo.getNew("mk3"), ExplosiveFlavorInfo.builder().build(), MiningExplosiveParameters.getDefaults(6.0f, 5).magic(true).build()),
    ;


    @Getter
    @Builder
    @Setter
    public static class MiningExplosiveParameters {

        public static MiningExplosiveParametersBuilder getDefaults(final float radius, final int fortune) {
            return MiningExplosiveParameters.builder().radius(radius).fortune(fortune);
        }

        private final float radius;

        private final int fortune;

        @Builder.Default
        private float oreChance = 0.9f;

        @Builder.Default
        private float clayChance = 1.0f;

        @Builder.Default
        private float soilChance = 0.05f;

        @Builder.Default
        private float rockChance = 0.05f;

        @Builder.Default
        private float otherChance = 0.05f;

        @Builder.Default
        private float featherRange = 2.5f;

        @Builder.Default
        private float featherOffset = 1.5f;

        @Builder.Default
        private boolean magic = false;


    }


    private @NonNull final String ELName;

    private @NonNull final ExplosiveTextureInfo textureInfo;

    private final @NonNull IGT_ExplosiveTier.ExplosiveFlavorInfo flavorInfo;

    private @NonNull final MiningExplosiveParameters parameters;

    private final @NonNull Class<GT_MiningExplosion> explosionClass = GT_MiningExplosion.class;

    private final @NonNull AtomicReference<GT_Block_Explosive<GT_MiningExplosiveTier>> blockReference = new AtomicReference<>();

    /**
     * @return The fortune level of the explosive
     */
    @Override
    public int getFortuneTier() {
        return getParameters().fortune;
    }


    @Override
    public boolean isMagic() {
        return parameters.isMagic();
    }

    @Override
    public float getBlockResistance(final GT_Explosion<?> explosion, final World world, final int x, final int y, final int z, final Block block) {
        final float base = explosion.getBlockResistance(x, y, z, block);
        if (block instanceof BlockOre || block instanceof GT_Block_Ore_Abstract) {
            return -GT_Values.MEOrePowerBoost;
        }
        final Material material = block.getMaterial();
        if (material == Material.rock) {
            return base * GT_Values.MERockResistanceDrop;
        } else if (isSoil(block, material)) {
            return -GT_Values.MESoilPowerBoost;
        } else {
            return base * GT_Values.MEOtherResistanceDrop;
        }
    }

    @Override
    public ResourceLocation getEntityTexture() {
        return Textures.BlockIcons.MINING_EXPLOSIVE.getTextureFile();
    }

    @Override
    public void addExplosiveFlavorText(final List<String> list) {
        val lines = new String[]{
                "An extraordinary explosive for extracting ore from the world.",
                String.format("Fortune bonus of %d", getFortuneTier()),
                String.format("Radius of %.1f blocks", getParameters().getRadius()),
                "Mainly effective on ore and clay, but will destroy most terrain.",
                };
        Collections.addAll(list, lines);
    }

    private static boolean isSoil(final Block block, final Material material) {
        return material == Material.ground || material == Material.sand || material == Material.clay || block instanceof BlockGrass;
    }

}
