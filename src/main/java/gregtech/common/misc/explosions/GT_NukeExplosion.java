package gregtech.common.misc.explosions;

import gregtech.api.enums.GT_Values;
import gregtech.common.entities.explosives.GT_Entity_Explosive;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class GT_NukeExplosion extends GT_Explosion {

    public GT_NukeExplosion(World world, GT_Entity_Explosive entity, double x, double y, double z, float power) {
        super(world, entity, x, y, z, power);
    }

    @Override
    protected float getDropChance(Block block) {
        return 0.0000001f;
    }

    @Override
    protected boolean rayValid(GT_Explosion_PreCalculation.Ray ray) {
        return ray.myLength < ray.maxLength && ray.power > 0.0;
    }

    @Override
    protected double precalcRayMaxLength(GT_Explosion_PreCalculation.Ray ray) {
        return GT_Values.NUKERadius + pubWorld.rand.nextDouble() * 10 - 5;
    }

    @Override
    protected int getMaxRays() {
        return 200;
    }

    @Override
    protected float getRayPower() {
        return super.getRayPower();
    }

    @Override
    public int getFuse() {
        return GT_Values.NUKEFuse;
    }

    @Override
    protected float getRayPowerDropRatio() {
        return super.getRayPowerDropRatio() * 0.25f;
    }

    @Override
    public void destroyBlock(Block block, int i, int j, int k) {

    }

    @Override
    protected void processDrops() {

    }

}
