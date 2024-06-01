package gregtech.common.misc.explosions;


import gregtech.common.entities.explosives.GT_Entity_TunnelExplosive;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static gregtech.common.misc.explosions.IGT_ExplosiveTier.GT_TunnelExplosiveTier;


public class GT_TunnelExplosion extends GT_Directional_Explosion<GT_TunnelExplosiveTier> {

//    private final ForgeDirection direction;

    public GT_TunnelExplosion(
            final World world, final GT_Entity_TunnelExplosive entity, final double x, final double y, final double z, final float power, final ForgeDirection side
                             ) {
        super(world, entity, x, y, z, power, side);
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
     * @param block
     *
     * @return
     */
    @Override
    protected float getDropChance(final Block block) {
        val params   = getParams();
        val material = block.getMaterial();
        if (material == Material.clay) {
            return (float) params.getClayChance();
        }
        return (float) params.getOtherChance();
    }

    /**
     * @param ray
     *
     * @return
     */
    @Override
    protected boolean isRayValid(final GT_Explosion_PreCalculation.Ray ray) {
        return ray.myLength < ray.maxLength && ray.doubleFields[1] < ray.doubleFields[0];
    }

    /**
     * @param ray
     */
    @Override
    protected void preprocessRay(final GT_Explosion_PreCalculation.Ray ray) {
        ray.flagFields[0]   = facingCorrectly(ray.aX, ray.aY, ray.aZ);
        ray.doubleFields[0] = getTunnelRadius();
        processRay(ray);
    }

    @Override
    protected double preCalculateRayMaximumLength(GT_Explosion_PreCalculation.Ray ray) {
        val   params    = getParams();
        val   maxLength = params.getMaxLength();
        val   maxRadius = params.getMaxRadius();
        val   radVar    = params.getRadiusVariation();
        float distance  = (float) (facingCorrectly(ray.aX, ray.aY, ray.aZ) ? maxLength : maxRadius);
        distance += (float) (pubWorld.rand.nextDouble() * radVar - radVar);
        return distance;
    }

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

    @Override
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

    private double getTunnelRadius() {
        val params = getParams();
        val radV   = params.getRadiusVariation();
        val radius = params.getMaxRadius() + (pubWorld.rand.nextDouble() * radV) - radV;
        return radius * radius;
    }

    private IGT_ExplosiveTier.GT_TunnelExplosiveTier.TunnelExplosiveParameters getParams() {
        return getTier().getParameters();
    }

}
