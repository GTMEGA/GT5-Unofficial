package gregtech.common.misc.explosions;


import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.common.blocks.GT_Block_Explosive;
import gregtech.common.entities.GT_Entity_Explosive;
import lombok.*;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static gregtech.GT_Mod.GT_FML_LOGGER;


public interface IGT_ExplosiveTier<MyType extends Enum<MyType> & IGT_ExplosiveTier<MyType>> {


    @RequiredArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    class ExplosiveFlavorInfo {

        public static ExplosiveFlavorInfoBuilder getDefault() {
            return ExplosiveFlavorInfo.builder();
        }

        @Builder.Default
        private int explosionSoundID = 213;

        @Builder.Default
        private int fuseSoundID = 214;

        @Builder.Default
        private @NonNull String particleName = "hugeexplosion";

    }

    @NoArgsConstructor
    class ExplosiveTextureInfo {

        public static final @NonNull String SIDE_SUFFIX = "SIDES", BOTTOM_SUFFIX = "BOTTOM", TOP_SUFFIX = "TOP", TOP_ACTIVE_SUFFIX = "TOP_ACTIVE";

        public static final @NonNull String EXP_BASE_PATH = "iconsets/EXPLOSIVES";

        /**
         * Gets the texture using the old style where they were in a flat directory <br/> tunnel explosives would be .../TUNEX_....png <br/>
         * <p>
         * This exists PURELY so that Houston does not have to reorganize the HTX files until he is ready, do not add new shit with this please.
         *
         * @return Old texture path
         */
        public static ExplosiveTextureInfo getOld() {
            return new ExplosiveTextureInfo();
        }

        /**
         * Gets the texture using the new, more organized method <br/> tunnel explosives would be .../tunex/[subpath]/TUNEX_....png
         *
         * @param subPath
         *         The sub path of the icon
         *
         * @return New texture path
         */
        public static ExplosiveTextureInfo getNew(final @NonNull String subPath) {
            return new ExplosiveTextureInfo(subPath);
        }

        private IIconContainer sideTex, bottomTex, topTex, topActiveTex;

        private String textureSubPath = null;

        private ExplosiveTextureInfo(final @NonNull String subPath) {
            textureSubPath = subPath;
        }

        public void loadIcons(final IGT_ExplosiveTier<?> tier) {
            val    tierMeta     = tier.getMetaInfo();
            val    texName      = tierMeta.getTextureHandle();
            val    texNameUpper = texName.toUpperCase();
            String pathStr;
            if (textureSubPath != null) {
                pathStr      = String.join("/", new String[]{
                        texName,
                        textureSubPath
                });
                sideTex      = wrapCustomIcon(pathStr, texNameUpper + "_" + SIDE_SUFFIX);
                bottomTex    = wrapCustomIcon(pathStr, texNameUpper + "_" + BOTTOM_SUFFIX);
                topTex       = wrapCustomIcon(pathStr, texNameUpper + "_" + TOP_SUFFIX);
                topActiveTex = wrapCustomIcon(pathStr, texNameUpper + "_" + TOP_ACTIVE_SUFFIX);
            } else {
                sideTex      = wrapCustomIcon(texNameUpper + "_" + SIDE_SUFFIX);
                bottomTex    = wrapCustomIcon(texNameUpper + "_" + BOTTOM_SUFFIX);
                topTex       = wrapCustomIcon(texNameUpper + "_" + TOP_SUFFIX);
                topActiveTex = wrapCustomIcon(texNameUpper + "_" + TOP_ACTIVE_SUFFIX);
            }
        }

        /**
         * Allows nesting of folders in the icon directory
         *
         * @param subPath
         *         The sub path of the icon
         * @param pathSuffix
         *         The suffix of the icon
         *
         * @return The icon container
         */
        public static IIconContainer wrapCustomIcon(final @NonNull String subPath, final @NonNull String pathSuffix) {
            return new Textures.BlockIcons.CustomIcon(EXP_BASE_PATH + "/" + subPath + "/" + pathSuffix);
        }

        /**
         * Wraps a custom icon with the explosive path
         *
         * @param pathSuffix
         *         The suffix of the icon
         *
         * @return The icon container
         */
        public static IIconContainer wrapCustomIcon(final @NonNull String pathSuffix) {
            return new Textures.BlockIcons.CustomIcon(EXP_BASE_PATH + "/" + pathSuffix);
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
     * @return The fortune level of the explosive
     */
    default int getFortuneTier() {
        return 0;
    }

    /**
     * @return The unlocalized suffix with a period
     */
    default @NonNull String getULName(final @NonNull String uName) {
        return "gt." + getULSuffix(uName);
    }

    default @NonNull String getULSuffix(final String uName) {
        if (!shouldUseSuffix()) {
            return uName;
        }
        return uName + "." + getEnumName().toLowerCase();
    }

    default boolean shouldUseSuffix() {
        return getTier() > 0;
    }

    default String getEnumName() {
        return asEnum().name();
    }

    /**
     * @return The tier of the explosive
     */
    default int getTier() {
        return asEnum().ordinal();
    }

    @SuppressWarnings("unchecked")
    default Enum<MyType> asEnum() {
        return (Enum<MyType>) this;
    }

    default @NonNull IIconContainer[] getIcons() {
        return getTextureInfo().getIcons();
    }

    @NonNull
    ExplosiveTextureInfo getTextureInfo();

    @NonNull
    default GT_Block_Explosive<MyType> getBlock() {
        return getBlockReference().get();
    }

    @NonNull
    AtomicReference<GT_Block_Explosive<MyType>> getBlockReference();

    default int getTierTrackIndex() {
        return GT_Explosion_Info.getIndexFromTier(this);
    }

    default GT_Entity_Explosive createExplosive(final World world, final double x, final double y, final double z, final EntityLivingBase placedBy, final int metadata, final int timer) {
        val klass = GT_Entity_Explosive.class;
        try {
            val constructor = klass.getConstructor(World.class, double.class, double.class, double.class, EntityLivingBase.class, int.class, int.class, IGT_ExplosiveTier.class);
            return constructor.newInstance(world, x, y, z, placedBy, metadata, timer, asInterface());
        } catch (final Exception e) {
            val msg = String.format("Failed to create explosive entity for %s", getELName());
            GT_FML_LOGGER.error(msg, e);
        }
        return null;
    }

    @NonNull
    default IGT_ExplosiveTier<MyType> asInterface() {
        return this;
    }

    /**
     * @return The localized name.
     */
    @NonNull
    String getELName();

    default String getFuseSound() {
        return GregTech_API.sSoundList.get(getFlavorInfo().getFuseSoundID());
    }

    @NonNull
    IGT_ExplosiveTier.ExplosiveFlavorInfo getFlavorInfo();

    default GT_Explosion<MyType> createExplosion(final GT_Entity_Explosive entity) {
        val explosionClass = getExplosionClass();
        val entityClass    = GT_Entity_Explosive.class;
        try {
            val isDirectional = GT_Directional_Explosion.class.isAssignableFrom(explosionClass);
            val power         = getPower();
            if (isDirectional) {
                val side        = GT_Block_Explosive.getFacing(entity.getMetadata());
                val constructor = explosionClass.getConstructor(World.class, entityClass, double.class, double.class, double.class, float.class, ForgeDirection.class);
                val x           = entity.posX + side.offsetX;
                val y           = entity.posY + side.offsetY;
                val z           = entity.posZ + side.offsetZ;
                return constructor.newInstance(entity.worldObj, entity, x, y, z, power, side);
            }
            val constructor = explosionClass.getConstructor(World.class, entityClass, double.class, double.class, double.class, float.class);
            return constructor.newInstance(entity.worldObj, entity, entity.posX, entity.posY, entity.posZ, power);
        } catch (final Exception e) {
            GT_FML_LOGGER.error(String.format("Failed to create explosive entity for %s", getELName()), e);
        }
        return null;
    }

    Class<? extends GT_Explosion<MyType>> getExplosionClass();

    /**
     * @return The power of the explosion, (used primarily for entity interactions)
     */
    default float getPower() {
        return 9.0f + 4.0f * getTier();
    }

    float getBlockResistance(final GT_Explosion<?> explosion, final World world, final int x, final int y, final int z, final Block block);

    ResourceLocation getEntityTexture();

    default String getBlockName() {
        return getMetaInfo().getBlockItemHandle();
    }

    @NonNull
    default GT_Explosion_Info getMetaInfo() {
        return GT_Explosion_Info.getTierTrack(this);
    }

    default void addInformation(final List<String> list) {
        addExplosiveFlavorText(list);
        addCommonFlavorText(list);
    }

    void addExplosiveFlavorText(final List<String> list);

    default void addCommonFlavorText(final List<String> list) {
        list.add("Prime with a Remote Detonator to explode!");
        list.add("Packs a pretty mean punch, so take");
        list.add(String.format("cover or you'll be mist in %d seconds!", GT_Values.MEFuse / 20));
    }

    boolean isMagic();

    default void createBlockAndItem() {
        val newBlock = new GT_Block_Explosive<>(asMyType());
        getBlockReference().set(newBlock);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    default MyType asMyType() {
        return (MyType) this;
    }

}
