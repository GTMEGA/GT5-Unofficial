package gregtech.common.misc.explosions;

import gregtech.common.entities.GT_Entity_Explosive;
import lombok.Setter;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

@Setter
public abstract class GT_Directional_Explosion<TierType extends Enum<TierType> & IGT_ExplosiveTier<TierType>> extends GT_Explosion<TierType> {

    protected ForgeDirection direction;

    protected int axisIndex;

    public GT_Directional_Explosion(final World world, final GT_Entity_Explosive entity, final double x, final double y, final double z, final float power, final ForgeDirection side) {
        super(world, entity, x, y, z, power);
        initAxes(side);
    }

    public void initAxes(final ForgeDirection side) {
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

    protected boolean facingCorrectly(final double rayX, final double rayY, final double rayZ) {
        final double check, checkAgainst;
        switch (axisIndex) {
            case 0: {
                check        = direction.offsetX;
                checkAgainst = rayX;
                break;
            }
            case 1: {
                check        = direction.offsetY;
                checkAgainst = rayY;
                break;
            }
            case 2: {
                check        = direction.offsetZ;
                checkAgainst = rayZ;
                break;
            }
            default: {
                return false;
            }
        }
        return check * checkAgainst > 0;
    }

}
