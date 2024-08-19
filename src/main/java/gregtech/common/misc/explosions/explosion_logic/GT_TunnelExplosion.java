package gregtech.common.misc.explosions.explosion_logic;


import gregtech.common.entities.GT_Entity_Explosive;
import gregtech.common.misc.explosions.GT_Directional_Explosion;
import gregtech.common.misc.explosions.GT_Explosion_PreCalculation;
import gregtech.common.misc.explosions.definitions.GT_TunnelExplosiveTier;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


public class GT_TunnelExplosion extends GT_Directional_Explosion<GT_TunnelExplosiveTier> {

//    private final ForgeDirection direction;

    public GT_TunnelExplosion(
            final World world, final GT_Entity_Explosive entity, final double x, final double y, final double z, final float power, final ForgeDirection side
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

    private double getTunnelRadius() {
        val params = getParams();
        val radV   = params.getRadiusVariation();
        val radius = params.getMaxRadius() + (pubWorld.rand.nextDouble() * radV) - radV;
        return radius * radius;
    }

    private GT_TunnelExplosiveTier.TunnelExplosiveParameters getParams() {
        return getTier().getParameters();
    }

}
