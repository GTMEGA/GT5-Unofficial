package gregtech.common.misc.explosions.explosion_logic;


import gregtech.common.entities.GT_Entity_Explosive;
import gregtech.common.misc.explosions.GT_Directional_Explosion;
import gregtech.common.misc.explosions.GT_Explosion_PreCalculation;
import gregtech.common.misc.explosions.definitions.GT_FlatBombTier;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


public class GT_FlatBombExplosion extends GT_Directional_Explosion<GT_FlatBombTier> {

    public GT_FlatBombExplosion(final World world, final GT_Entity_Explosive entity, final double x, final double y, final double z, final float power, final ForgeDirection side) {
        super(world, entity, x, y, z, power, side);
    }

    @Override
    public int getMaxX() {
        val base = super.getMaxX();
        return axisIndex != 0 ? base : base * 3;
    }

    @Override
    public int getMaxY() {
        val base = super.getMaxY();
        return axisIndex != 1 ? base : base * 3;
    }

    @Override
    public int getMaxZ() {
        val base = super.getMaxZ();
        return axisIndex != 2 ? base : base * 3;
    }

    @Override
    protected float getDropChance(final Block block) {
        return 0;
    }

    @Override
    protected boolean isRayValid(final GT_Explosion_PreCalculation.Ray ray) {
        return !ray.flagFields[0] && ray.myLength < ray.maxLength && ray.doubleFields[1] < ray.doubleFields[0];
    }

    @Override
    protected void preprocessRay(final GT_Explosion_PreCalculation.Ray ray) {
        val fc = facingCorrectly(ray.aX, ray.aY, ray.aZ);
        ray.flagFields[0]   = fc;
        ray.doubleFields[0] = getDepth();
        processRay(ray);
    }

    private double getDepth() {
        val params = getParams();
        val radV   = params.getDepthVariation();
        val radius = params.getMaxRadius() + (pubWorld.rand.nextDouble() * radV) - radV;
        return radius * radius;
    }

    private GT_FlatBombTier.FlatBombParameters getParams() {
        return getTier().getParameters();
    }

    @Override
    protected double preCalculateRayMaximumLength(final GT_Explosion_PreCalculation.Ray ray) {
        val   params    = getParams();
        val   maxDepth  = params.getMaxDepth();
        val   maxRadius = params.getMaxRadius();
        val   depthVar  = params.getDepthVariation();
        float distance  = (float) (facingCorrectly(ray.aX, ray.aY, ray.aZ) ? maxDepth : maxRadius);
        distance += (float) (pubWorld.rand.nextDouble() * depthVar - depthVar);
        return distance;
    }

    @Override
    protected void processRay(final GT_Explosion_PreCalculation.Ray ray) {
        ray.doubleFields[1] = getRadiusSquared(ray.posX - explosionX, ray.posY - explosionY, ray.posZ - explosionZ);
    }

}
