package gregtech.common.misc.explosions;

import cpw.mods.fml.common.registry.EntityRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.common.entities.GT_Entity_Explosive;
import gregtech.common.misc.explosions.definitions.GT_DaisyCutterTier;
import gregtech.common.misc.explosions.definitions.GT_FlatBombTier;
import gregtech.common.misc.explosions.definitions.GT_MiningExplosiveTier;
import gregtech.common.misc.explosions.definitions.GT_TunnelExplosiveTier;
import gregtech.common.render.GT_ExplosiveRenderer;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public
enum GT_Explosion_Info {
    DAISY_CUTTER(GT_DaisyCutterTier.class, "daisy_cutter"),
    MINING_EXPLOSIVE(GT_MiningExplosiveTier.class, "mining_explosives", "mining_explosive"),
    TUNNEL_EXPLOSIVE(GT_TunnelExplosiveTier.class, "tunex"),
    FLAT_BOMB(GT_FlatBombTier.class, "flat_bomb");

    private static final Map<Integer, Map<Integer, ? extends IGT_ExplosiveTier<?>>> TIER_INT_MAP = new HashMap<>();

    private static final Map<Class<? extends IGT_ExplosiveTier<?>>, GT_Explosion_Info> TIER_CLASS_MAP = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends Enum<T> & IGT_ExplosiveTier<T>> T getTier(final Class<? extends IGT_ExplosiveTier<T>> tierClass, final int tier) {
        return (T) getTierTrack(tierClass).getTier(tier);
    }

    public IGT_ExplosiveTier<?> getTier(final int tier) {
        return getTierType(ordinal(), tier);
    }

    @SuppressWarnings("rawtypes")
    public static GT_Explosion_Info getTierTrack(final @NonNull Class<? extends IGT_ExplosiveTier> tierClass) {
        return TIER_CLASS_MAP.get(tierClass);
    }

    public static IGT_ExplosiveTier<?> getTierType(final int typeIndex, final int tier) {
        return TIER_INT_MAP.getOrDefault(typeIndex, new HashMap<>()).getOrDefault(tier, null);
    }

    public static void registerBlocksAndItems() {
        for (val type : values()) {
            for (val tier : type.getTierClass().getEnumConstants()) {
                tier.createBlockAndItem();
            }
        }
    }

    public static int getIndexFromTier(final @NonNull IGT_ExplosiveTier<?> tier) {
        return getTierTrack(tier).ordinal();
    }

    public static GT_Explosion_Info getTierTrack(final @NonNull IGT_ExplosiveTier<?> tier) {
        return getTierTrack(tier.getClass());
    }

    public static void registerEntities() {
        EntityRegistry.registerModEntity(GT_Entity_Explosive.class, "GT_Entity_Explosive", 3, GT_Values.GT, 160, 1, false);
    }

    public static void registerEntityRendering() {
        new GT_ExplosiveRenderer().addExplosive(GT_Entity_Explosive.class);
    }

    static {
        for (val tier : values()) {
            val tierClass = tier.getTierClass();
            val eConsts   = tierClass.getEnumConstants();
            val tierMap   = new HashMap<Integer, IGT_ExplosiveTier<?>>();
            for (final IGT_ExplosiveTier<? extends Enum<?>> eConst : eConsts) {
                tierMap.put(eConst.getTier(), eConst);
            }
            TIER_INT_MAP.put(tier.ordinal(), tierMap);
            TIER_CLASS_MAP.put(tierClass, tier);
        }
    }

    private final @NonNull Class<? extends IGT_ExplosiveTier<?>> tierClass;

    private final @NonNull String blockItemHandle, textureHandle;

    GT_Explosion_Info(@NonNull final Class<? extends IGT_ExplosiveTier<?>> tierClass, @NonNull final String blockItemTextureHandle) {
        this(tierClass, blockItemTextureHandle, blockItemTextureHandle);
    }

    public @NonNull String getUpperCaseTextureHandle() {
        return textureHandle.toUpperCase();
    }
}
