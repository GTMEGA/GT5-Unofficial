package gregtech.common.misc.explosions;


import gregtech.api.enums.GT_Values;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;


public class GT_MiningExplosion extends GT_Explosion {

    public GT_MiningExplosion(
            final World world, final Entity entity, final double x, final double y, final double z, final float power
                             ) {
        super(world, entity, x, y, z, power);
    }

    @Override
    protected boolean rayValid(final float power, final double rayLength, final double posX, final double posY, final double posZ) {
        return power > 0.0f && rayLength < GT_Values.MEMaxRange;
    }

}
