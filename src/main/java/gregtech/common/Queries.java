package gregtech.common;

public final class Queries {
    public static final String CREATE_ORE_VEIN_STATS_TABLE = """
        CREATE TABLE IF NOT EXISTS ore_vein_stats (
            location int8 primary key not null,
            ore_mix int2,
            ores_placed int2,
            ores_current int2
        );
        """;

    public static final String QUERY_ORE_VEIN_STATS_COUNT = """
        SELECT
            count(*)
        FROM
            ore_vein_stats ovs
        WHERE
            (ovs.location >> 54) = ?
        """;

    public static final String QUERY_ORE_VEIN_STATS_BY_DIMENSION_PAGED = """
        SELECT
            ovs.location,
            ovs.ore_mix,
            ovs.ores_placed,
            ovs.ores_current
        FROM
            ore_vein_stats ovs
        WHERE
            (ovs.location >> 54) = ?
        LIMIT
            ?
        OFFSET
            ?
        """;

    public static final String QUERY_ORE_VEIN_STATS_BY_LOCATION = """
        SELECT
            ovs.location,
            ovs.ore_mix,
            ovs.ores_placed,
            ovs.ores_current
        FROM
            ore_vein_stats ovs
        WHERE
            ovs.location in (%s)
        """;

    public static final String UPSERT_ORE_VEIN_STATS_VALUES_TEMPLATE = "(%d, %d, %d, %d)";

    public static final String UPSERT_ORE_VEIN_STATS = """
        INSERT INTO
            ore_vein_stats (location, ore_mix, ores_placed, ores_current)
        VALUES
            %s
        ON CONFLICT
            (location)
        DO UPDATE SET
            ore_mix = excluded.ore_mix,
            ores_placed = excluded.ores_placed,
            ores_current = excluded.ores_current
        """;
}
