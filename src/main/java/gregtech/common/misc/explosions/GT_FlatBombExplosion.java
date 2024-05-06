package gregtech.common.misc.explosions;


import gregtech.common.entities.explosives.GT_Entity_Explosive;
import net.minecraft.block.Block;
import net.minecraft.world.World;


public class GT_FlatBombExplosion extends GT_Explosion<IGT_ExplosiveTier.GT_FlatBombTier>{

    public GT_FlatBombExplosion(final World world, final GT_Entity_Explosive<IGT_ExplosiveTier.GT_FlatBombTier> entity, final double x, final double y, final double z, final float power) {
        super(world, entity, x, y, z, power);
    }

    /**
     * @param block
     * @return
     */
    @Override
    protected float getDropChance(final Block block) {
        return 0;
    }

    /**
     * @param ray
     * @return
     */
    @Override
    protected boolean isRayValid(final GT_Explosion_PreCalculation.Ray ray) {
        return false;
    }

    /**
     * @param ray
     * @return
     */
    @Override
    protected double preCalculateRayMaximumLength(final GT_Explosion_PreCalculation.Ray ray) {
        return 0;
    }

}
