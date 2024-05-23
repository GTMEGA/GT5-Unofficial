package gregtech.common.misc.explosions;


import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.common.blocks.explosives.GT_Block_Explosive;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.concurrent.atomic.AtomicReference;


public interface IGT_ExplosiveTier<MyType extends Enum<MyType> & IGT_ExplosiveTier<MyType>> {

    @RequiredArgsConstructor
    @Getter
    enum GT_DaisyCutterTier implements IGT_ExplosiveTier<GT_DaisyCutterTier> {

        MK1("Daisy Cutter Mk 1", new ExplosiveTextureInfo("DAISY_CUTTER"), new ExplosiveFlavorInfo(213, 214), 64.0, 9.0, 0, new DaisyCutterParameters(0.001)),
        MK2("Daisy Cutter Mk 2", new ExplosiveTextureInfo("DAISY_CUTTER"), new ExplosiveFlavorInfo(213, 214), 96.0, 15.0, 1, new DaisyCutterParameters(0.002));


        @RequiredArgsConstructor
        @Getter
        public static class DaisyCutterParameters {

            private final double dropChance;

        }


        private @NonNull
        final String ELName;

        private @NonNull
        final ExplosiveTextureInfo textureInfo;

        private final @NonNull IGT_ExplosiveTier.ExplosiveFlavorInfo flavorInfo;

        private final double radius, power;

        private final int fortuneTier;

        private final @NonNull DaisyCutterParameters parameters;

        private final @NonNull AtomicReference<GT_Block_Explosive<GT_DaisyCutterTier>> blockReference = new AtomicReference<>();

        public boolean shouldUseSuffix() {
            return ordinal() > 0;
        }

        /**
         * @return
         */
        @Override
        public @NonNull GT_DaisyCutterTier asType() {
            return this;
        }
    }


    @RequiredArgsConstructor
    @Getter
    enum GT_MiningExplosiveTier implements IGT_ExplosiveTier<GT_MiningExplosiveTier> {

        MK1("Mining Explosive Mk 1", new ExplosiveTextureInfo("MINING_EXPLOSIVE"), new ExplosiveFlavorInfo(213, 214), 4.0, 9.0, 3, new MiningExplosiveParameters(0.9f, 1.0f, 0.05f, 0.05f, 0.05f, 2.5f, 1.5f)),
        MK2("Mining Explosive Mk 2", new ExplosiveTextureInfo("MINING_EXPLOSIVE"), new ExplosiveFlavorInfo(213, 214), 5.0, 12.0, 4, new MiningExplosiveParameters(0.9f, 1.0f, 0.05f, 0.05f, 0.05f, 2.5f, 1.5f)),
        ;


        @Getter
        @RequiredArgsConstructor
        public static class MiningExplosiveParameters {

            private final float oreChance, clayChance, soilChance, rockChance, otherChance, featherRange, featherOffset;

        }


        private @NonNull
        final String ELName;

        private @NonNull
        final ExplosiveTextureInfo textureInfo;

        private final @NonNull IGT_ExplosiveTier.ExplosiveFlavorInfo flavorInfo;


        private final double radius, power;

        private final int fortuneTier;

        private @NonNull
        final MiningExplosiveParameters parameters;

        private final @NonNull AtomicReference<GT_Block_Explosive<GT_MiningExplosiveTier>> blockReference = new AtomicReference<>();

        public boolean shouldUseSuffix() {
            return ordinal() > 0;
        }

        /**
         * @return
         */
        @Override
        public @NonNull GT_MiningExplosiveTier asType() {
            return this;
        }
    }


    @RequiredArgsConstructor
    @Getter
    enum GT_TunnelExplosiveTier implements IGT_ExplosiveTier<GT_TunnelExplosiveTier> {

        MK1("Tunnel Explosive Mk 1", new ExplosiveTextureInfo("TUNEX"), new ExplosiveFlavorInfo(213, 214), new TunnelExplosiveParameters(30, 4, 1.5, 1.0, 0.001), 9.0, 0),
        MK2("Tunnel Explosive Mk 2", new ExplosiveTextureInfo("TUNEX"), new ExplosiveFlavorInfo(213, 214), new TunnelExplosiveParameters(45, 6, 1.5, 1.0, 0.001), 12.0, 1);


        @Getter
        @RequiredArgsConstructor
        public static class TunnelExplosiveParameters {

            private final double maxLength, maxRadius;

            private final double radiusVariation;

            private final double clayChance, otherChance;

        }


        private @NonNull
        final String ELName;

        private @NonNull
        final ExplosiveTextureInfo textureInfo;

        private final @NonNull IGT_ExplosiveTier.ExplosiveFlavorInfo flavorInfo;


        private final @NonNull TunnelExplosiveParameters parameters;

        private final double power;

        private final int fortuneTier;

        private final @NonNull AtomicReference<GT_Block_Explosive<GT_TunnelExplosiveTier>> blockReference = new AtomicReference<>();

        public boolean shouldUseSuffix() {
            return ordinal() > 0;
        }

        @Override
        public double getRadius() {
            return 0.0;
        }

        /**
         * @return
         */
        @Override
        public @NonNull GT_TunnelExplosiveTier asType() {
            return this;
        }
    }


    @RequiredArgsConstructor
    @Getter
    enum GT_FlatBombTier implements IGT_ExplosiveTier<GT_FlatBombTier> {

        MK1("Flat Bomb Mk 1", new ExplosiveTextureInfo("flatbomb/mk1", "FLAT_BOMB"), new ExplosiveFlavorInfo(213, 214), 24, 9.0, 0),
        MK2("Flat Bomb Mk 2", new ExplosiveTextureInfo("flatbomb/mk2", "FLAT_BOMB"), new ExplosiveFlavorInfo(213, 214), 36, 12.0, 0);

        private final @NonNull String ELName;

        private final @NonNull ExplosiveTextureInfo textureInfo;

        private final @NonNull ExplosiveFlavorInfo flavorInfo;

        private final double radius, power;

        private final int fortuneTier;

        private final @NonNull AtomicReference<GT_Block_Explosive<GT_FlatBombTier>> blockReference = new AtomicReference<>();

        public boolean shouldUseSuffix() {
            return ordinal() > 0;
        }

        /**
         * @return
         */
        @Override
        public @NonNull GT_FlatBombTier asType() {
            return this;
        }
    }


    class ExplosiveTierUtil {

        public static final @NonNull String EXP_BASE_PATH = "iconsets/EXPLOSIVES";

        public static int getIndexFromTier(final @NonNull IGT_ExplosiveTier<?> tier) {
            if (tier instanceof GT_DaisyCutterTier) {
                return 0;
            } else if (tier instanceof GT_MiningExplosiveTier) {
                return 1;
            } else if (tier instanceof GT_TunnelExplosiveTier) {
                return 2;
            } else if (tier instanceof GT_FlatBombTier) {
                return 3;
            } else {
                // TODO: No exceptions in production
                throw new IllegalArgumentException("Invalid tier " + tier);
            }
        }

        public static IGT_ExplosiveTier<?> getTier(final int typeIndex, final int tier) {
            switch (typeIndex) {
                case 0: {
                    return getTier(GT_DaisyCutterTier.class, tier);
                }
                case 1: {
                    return getTier(GT_MiningExplosiveTier.class, tier);
                }
                case 2: {
                    return getTier(GT_TunnelExplosiveTier.class, tier);
                }
                case 3: {
                    return getTier(GT_FlatBombTier.class, tier);
                }
                default: {
                    // TODO: No exceptions in production
                    throw new IllegalArgumentException("Invalid type index " + typeIndex);
                }
            }
        }

        public static <T extends Enum<T> & IGT_ExplosiveTier<T>> T getTier(final Class<T> tierClass, final int tier) {
            val values = tierClass.getEnumConstants();
            if (tier < 0 || tier >= values.length) {
                // TODO: No exceptions in production
                throw new IllegalArgumentException("Invalid tier " + tier + " for " + tierClass.getSimpleName());
            }
            return values[tier];
        }

        /**
         * Allows nesting of folders in the icon directory
         *
         * @param subPath    The sub path of the icon
         * @param pathSuffix The suffix of the icon
         * @return The icon container
         */
        public static IIconContainer wrapCustomIcon(final @NonNull String subPath, final @NonNull String pathSuffix) {
            return new Textures.BlockIcons.CustomIcon(EXP_BASE_PATH + "/" + subPath + "/" + pathSuffix);
        }

        /**
         * Wraps a custom icon with the explosive path
         *
         * @param pathSuffix The suffix of the icon
         * @return The icon container
         */
        public static IIconContainer wrapCustomIcon(final @NonNull String pathSuffix) {
            return new Textures.BlockIcons.CustomIcon(EXP_BASE_PATH + "/" + pathSuffix);
        }

    }


    @RequiredArgsConstructor
    @Getter
    class ExplosiveFlavorInfo {

        private final int explosionSoundID, fuseSoundID;

        private final @NonNull String particleName;

        public ExplosiveFlavorInfo(final int expId, final int fuseID) {
            this(expId, fuseID, "hugeexplosion");
        }

    }


    @RequiredArgsConstructor
    @Getter
    class ExplosiveTextureInfo {

        public static final @NonNull String SIDE_SUFFIX = "SIDES", BOTTOM_SUFFIX = "BOTTOM", TOP_SUFFIX = "TOP", TOP_ACTIVE_SUFFIX = "TOP_ACTIVE";

        private final IIconContainer sideTex, bottomTex, topTex, topActiveTex;

        public ExplosiveTextureInfo(final @NonNull String subPath, final @NonNull String expTexHandle) {
            this(
                    ExplosiveTierUtil.wrapCustomIcon(subPath, expTexHandle + "_" + SIDE_SUFFIX),
                    ExplosiveTierUtil.wrapCustomIcon(subPath, expTexHandle + "_" + BOTTOM_SUFFIX),
                    ExplosiveTierUtil.wrapCustomIcon(subPath, expTexHandle + "_" + TOP_SUFFIX),
                    ExplosiveTierUtil.wrapCustomIcon(subPath, expTexHandle + "_" + TOP_ACTIVE_SUFFIX)
                );
        }

        public ExplosiveTextureInfo(final @NonNull String expTexHandle) {
            this(
                    ExplosiveTierUtil.wrapCustomIcon(expTexHandle + "_" + SIDE_SUFFIX),
                    ExplosiveTierUtil.wrapCustomIcon(expTexHandle + "_" + BOTTOM_SUFFIX),
                    ExplosiveTierUtil.wrapCustomIcon(expTexHandle + "_" + TOP_SUFFIX),
                    ExplosiveTierUtil.wrapCustomIcon(expTexHandle + "_" + TOP_ACTIVE_SUFFIX)
                );
        }

        @NonNull
        public IIconContainer[] getIcons() {
            return new IIconContainer[]{
                    sideTex,
                    bottomTex,
                    topTex,
                    topActiveTex
            };
        }

    }

    /**
     * @return The tier of the explosive
     */
    @SuppressWarnings("unchecked")
    default int getTier() {
        return ((Enum<MyType>) this).ordinal();
    }

    /**
     * @return The radius of the explosion
     */
    double getRadius();

    /**
     * @return The power of the explosion, (used primarily for entity interactions)
     */
    double getPower();

    @NonNull
    IGT_ExplosiveTier.ExplosiveFlavorInfo getFlavorInfo();

    /**
     * @return The fortune level of the explosive
     */
    int getFortuneTier();

    /**
     * @return The unlocalized suffix with a period
     */
    default @NonNull String getULName(final @NonNull String uName) {
        return "gt." + getULSuffix(uName);
    }

    @SuppressWarnings("unchecked")
    default @NonNull String getULSuffix(final String uName) {
        if (!shouldUseSuffix()) {
            return uName;
        }
        return uName + "." + ((Enum<MyType>) this).name().toLowerCase();
    }

    /**
     * @return The localized name.
     */
    @NonNull
    String getELName();

    default @NonNull IIconContainer[] getIcons() {
        return getTextureInfo().getIcons();
    }

    boolean shouldUseSuffix();

    @NonNull
    ExplosiveTextureInfo getTextureInfo();

    @NonNull
    default GT_Block_Explosive<MyType> getBlock() {
        return getBlockReference().get();
    }

    @NonNull
    AtomicReference<GT_Block_Explosive<MyType>> getBlockReference();

    @NonNull
    MyType asType();

}
