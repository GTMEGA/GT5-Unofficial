package gregtech.common.misc.explosions;


import gregtech.api.enums.GT_Values;
import gregtech.common.entities.explosives.GT_Entity_TunnelExplosive;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


public class GT_TunnelExplosion extends GT_Explosion {

    private static final double A = 1.0411961/* (1.0 + Math.sqrt(4 - 2 * Math.sqrt(2))) / 2 */, B = 0.70710678;

    private static double magnitude(double a, double b) {
        // https://majewsky.wordpress.com/2011/04/10/optimization-tricks-fast-norm/
        // return Math.sqrt(a * a + b * b);
        a = a > 0 ? a : -a;
        b = b > 0 ? b : -b;
        val c = a > b ? a : b;
        val d = B * (a + b);
        val e = c > d ? c : d;
        return A * e;
    }

    private final ForgeDirection direction;

    private final int axisIndex;

    public GT_TunnelExplosion(
            final World world, final GT_Entity_TunnelExplosive entity, final double x, final double y, final double z, final float power, final ForgeDirection side
                             ) {
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

    /**
     * @return
     */
    @Override
    public int getMaxX() {
        final int result = super.getMaxX();
        return axisIndex == 0 ? result : result * 3;
    }

    /**
     * @return
     */
    @Override
    public int getMaxY() {
        final int result = super.getMaxY();
        return axisIndex == 1 ? result : result * 3;
    }

    /**
     * @return
     */
    @Override
    public int getMaxZ() {
        final int result = super.getMaxZ();
        return axisIndex == 2 ? result : result * 3;
    }

    /**
     * @return
     */
    @Override
    protected int getMaxRays() {
        return super.getMaxRays();
    }

    /**
     * @param block
     * @return
     */
    @Override
    protected float getDropChance(final Block block) {
        return 0.001f;
    }

    /**
     * @param ray
     * @return
     */
    @Override
    protected boolean rayValid(final GT_Explosion_PreCalculation.Ray ray) {
        final double rayX, rayY, rayZ;
        rayX = ray.posX - explosionX;
        rayY = ray.posY - explosionY;
        rayZ = ray.posZ - explosionZ;
        val radiusSquared = ray.doubleFields[1];
        return ray.myLength < ray.maxLength && radiusSquared < ray.doubleFields[0];
    }

    /**
     * @param power
     * @param rayLength
     * @param posX
     * @param posY
     * @param posZ
     * @param maxRadius
     * @return
     */
    /*@Override
    protected boolean rayValid(final float power, final double rayLength, final double posX, final double posY, final double posZ, final double maxRadius) {
        final double rayX, rayY, rayZ;
        rayX = posX - explosionX;
        rayY = posY - explosionY;
        rayZ = posZ - explosionZ;
        final double radius = getRadiusSquared(rayX, rayY, rayZ);
        *//* double range = maxRadius;
        if (facingCorrectly(rayX, rayY, rayZ)) {
            range *= 6;
        } *//*
        return power > 0.0 && rayLength < getRangeForRay(posX, posY, posZ, maxRadius) && radius < getTunnelRadius();
    }*/

    /**
     * @param ray
     */
    @Override
    protected void preprocessRay(final GT_Explosion_PreCalculation.Ray ray) {
        ray.flagFields[0] = facingCorrectly(ray.aX, ray.aY, ray.aZ);
        ray.doubleFields[0] = getTunnelRadius();
        processRay(ray);
    }

    @Override
    protected double precalcRayMaxLength(GT_Explosion_PreCalculation.Ray ray) {
        float distance = facingCorrectly(ray.aX, ray.aY, ray.aZ) ? GT_Values.TEMaxRange : GT_Values.TERadius;
        distance += (float) (pubWorld.rand.nextDouble() * GT_Values.TERadiusVariation - GT_Values.TERadiusVariation);
        return distance;
    }

    /**
     * @param posX
     * @param posY
     * @param posZ
     * @param maxRadius
     * @return
     */
    /*@Override
    protected double getRangeForRay(final double posX, final double posY, final double posZ, final double maxRadius) {
        final double rayX, rayY, rayZ;
        rayX = posX - explosionX;
        rayY = posY - explosionY;
        rayZ = posZ - explosionZ;
        return facingCorrectly(rayX, rayY, rayZ) ? GT_Values.TEMaxRange : GT_Values.TERadius;
    }*/

    /**
     * @return
     */
    @Override
    protected float getRayPowerDropRatio() {
        return super.getRayPowerDropRatio() * 0.1f;
    }

    /**
     * @param ray
     */
    @Override
    protected void processRay(final GT_Explosion_PreCalculation.Ray ray) {
        ray.doubleFields[1] = getRadiusSquared(ray.posX - explosionX, ray.posY - explosionY, ray.posZ - explosionZ);
    }

    private boolean facingCorrectly(final double rayX, final double rayY, final double rayZ) {
        final double check, checkAgainst;
        switch (axisIndex) {
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
        val radius = GT_Values.TERadius + (pubWorld.rand.nextDouble() * GT_Values.TERadiusVariation) - GT_Values.TERadiusVariation;
        return radius * radius;
    }

    private double getRadiusSquared(final double rayX, final double rayY, final double rayZ) {
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

}
