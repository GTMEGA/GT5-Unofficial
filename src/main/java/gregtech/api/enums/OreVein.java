package gregtech.api.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Current purpose for mapping an ordinal to an enum for better normalization in the ore_vein_stats table
 *
 * Would be nice if we migrated all ore vein definitions into this later
 */
@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public enum OreVein {
    // Houston if you rearrange these, I will kill you
    empty_vein("empty_vein"),
    coal("ore.mix.coal"),
    magnetite("ore.mix.magnetite"),
    zinc("ore.mix.zinc"),
    tin("ore.mix.tin"),
    copper("ore.mix.copper"),
    owbauxite("ore.mix.owbauxite"),
    salts("ore.mix.salts"),
    redstone("ore.mix.redstone"),
    nickel("ore.mix.nickel"),
    manganese("ore.mix.manganese"),
    diamond("ore.mix.diamond"),
    galena("ore.mix.galena"),
    calcite("ore.mix.calcite"),
    oilsand("ore.mix.oilsand"),
    alunite("ore.mix.alunite"),
    garnet("ore.mix.garnet"),
    mica("ore.mix.mica"),
    quartz("ore.mix.quartz"),
    olivine("ore.mix.olivine"),
    aquaignis("ore.mix.aquaignis"),
    terraaer("ore.mix.terraaer"),
    perditioordo("ore.mix.perditioordo"),
    titanium("ore.mix.titanium"),
    nuclear("ore.mix.nuclear"),
    firestone("ore.mix.firestone"),
    infusedgold("ore.mix.infusedgold"),
    netherstar("ore.mix.netherstar"),
    ;

    private final String unlocalizedName;

    public static final Map<String, OreVein> LOOKUP;

    static {
        LOOKUP = Arrays.stream(OreVein.values())
                       .collect(Collectors.toMap(OreVein::unlocalizedName, Function.identity()));
    }
}
