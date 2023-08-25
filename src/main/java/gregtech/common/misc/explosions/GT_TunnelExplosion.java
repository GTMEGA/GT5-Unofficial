package gregtech.common.misc.explosions;


import gregtech.api.enums.GT_Values;
import gregtech.common.entities.explosives.GT_Entity_TunnelExplosive;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


public class GT_TunnelExplosion extends GT_Explosion {

    private final ForgeDirection direction;

    public GT_TunnelExplosion(
            final World world, final GT_Entity_TunnelExplosive entity, final double x, final double y, final double z, final float power,
            final ForgeDirection side
                             ) {
        super(world, entity, x, y, z, power);
        this.direction = side;
    }

    /**
     * @return
     */
    @Override
    protected int getMaxRays() {
        return super.getMaxRays() * 2;
    }

    /**
     * @return
     */
    @Override
    protected int getMaxX() {
        final int result = super.getMaxX();
        return getAxisIndex() == 0 ? result : result * 4;
    }

    /**
     * @return
     */
    @Override
    protected int getMaxY() {
        final int result = super.getMaxY();
        return getAxisIndex() == 1 ? result : result * 4;
    }

    /**
     * @return
     */
    @Override
    protected int getMaxZ() {
        final int result = super.getMaxZ();
        return getAxisIndex() == 2 ? result : result * 4;
    }

    /**
     * @param power
     * @param rayLength
     * @param posX
     * @param posY
     * @param posZ
     * @return
     */
    @Override
    protected boolean rayValid(final float power, final double rayLength, final double posX, final double posY, final double posZ) {
        final double rayX, rayY, rayZ;
        rayX = posX - explosionX;
        rayY = posY - explosionY;
        rayZ = posZ - explosionZ;
        final double radius = getRadius(rayX, rayY, rayZ);
        double range = GT_Values.MEMaxRange;
        if (facingCorrectly(rayX, rayY, rayZ)) {
            range *= 16;
        }
        return power > 0.0 && rayLength < range && radius < getTunnelRadius();
    }

    private double getRadius(final double rayX, final double rayY, final double rayZ) {
        switch (getAxisIndex()) {
            case 0: {
                return Math.sqrt(rayY * rayY + rayZ * rayZ);
            }
            case 1: {
                return Math.sqrt(rayX * rayX + rayZ * rayZ);
            }
            case 2: {
                return Math.sqrt(rayX * rayX + rayY * rayY);
            }
        }
        return 0;
    }

    private boolean facingCorrectly(final double rayX, final double rayY, final double rayZ) {
        final double check, checkAgainst;
        switch (getAxisIndex()) {
            case 0: {
                check = direction.offsetX;
                checkAgainst = rayX;
                break;
            }
            case 1: {
                check = direction.offsetY;
                checkAgainst = rayY;
                break;
            }
            case 2: {
                check = direction.offsetZ;
                checkAgainst = rayZ;
                break;
            }
            default: {
                return false;
            }
        }
        return check * checkAgainst > 0;
    }

    private double getTunnelRadius() {
        return GT_Values.MEMaxRange * 0.8 + (pubWorld.rand.nextDouble() * 1.5) - 0.5;
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

    /**
     * @param block
     * @return
     */
    @Override
    protected float getDropChance(final Block block) {
        return 0.001f;
    }

}
