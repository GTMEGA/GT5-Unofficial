package gregtech.common.misc.explosions;

import gregtech.common.entities.explosives.GT_Entity_Explosive;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class GT_Directional_Explosion<TierType extends Enum<TierType> & IGT_ExplosiveTier<TierType>> extends GT_Explosion<TierType> {

    protected final ForgeDirection direction;

    protected final int axisIndex;

    public GT_Directional_Explosion(final World world, final GT_Entity_Explosive<TierType> entity, final double x, final double y, final double z, final float power, final ForgeDirection side) {
        super(world, entity, x, y, z, power);
        this.direction = side;
        this.axisIndex = getAxisIndex();
    }

    protected int getAxisIndex() {
        switch (direction) {
            default:
            case EAST:
            case WEST:
                return 0;
            case DOWN:
            case UP:
                return 1;
            case NORTH:
            case SOUTH:
                return 2;
        }
    }

    protected double getRadiusSquared(final double rayX, final double rayY, final double rayZ) {
        switch (axisIndex) {
            case 0: {
                return rayY * rayY + rayZ * rayZ;
            }
            case 1: {
                return rayX * rayX + rayZ * rayZ;
            }
            case 2: {
                return rayX * rayX + rayY * rayY;
            }
        }
        return 0;
    }

    protected abstract boolean facingCorrectly(double rayX, double rayY, double rayZ);

}
