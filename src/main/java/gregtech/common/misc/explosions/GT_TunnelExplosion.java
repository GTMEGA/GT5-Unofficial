package gregtech.common.misc.explosions;


import gregtech.common.entities.explosives.GT_Entity_TunnelExplosive;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static gregtech.common.misc.explosions.IGT_ExplosiveTier.GT_TunnelExplosiveTier;


public class GT_TunnelExplosion extends GT_Explosion<GT_TunnelExplosiveTier> {

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
        val params = getParams();
        val material = block.getMaterial();
        if (material == Material.clay) {
            return (float) params.getClayChance();
        }
        return (float) params.getOtherChance();
    }

    /**
     * @param ray
     * @return
     */
    @Override
    protected boolean isRayValid(final GT_Explosion_PreCalculation.Ray ray) {
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
    protected boolean isRayValid(final float power, final double rayLength, final double posX, final double posY, final double posZ, final double maxRadius) {
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
    protected double preCalculateRayMaximumLength(GT_Explosion_PreCalculation.Ray ray) {
        val params = getParams();
        val maxLength = params.getMaxLength();
        val maxRadius = params.getMaxRadius();
        val radVar = params.getRadiusVariation();
        float distance = (float) (facingCorrectly(ray.aX, ray.aY, ray.aZ) ? maxLength : maxRadius);
        distance += (float) (pubWorld.rand.nextDouble() * radVar - radVar);
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
        val params = getParams();
        val radV = params.getRadiusVariation();
        val radius = params.getMaxRadius() + (pubWorld.rand.nextDouble() * radV) - radV;
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

    private IGT_ExplosiveTier.GT_TunnelExplosiveTier.TunnelExplosiveParameters getParams() {
        return getTier().getParameters();
    }

}
