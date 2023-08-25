package gregtech.common.entities.explosives;


import gregtech.api.enums.GT_Values;
import gregtech.common.misc.explosions.GT_DaisyCutterExplosion;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;


public class GT_Entity_DaisyCutterExplosive extends GT_Entity_Explosive{

    @SuppressWarnings("unused")
    public GT_Entity_DaisyCutterExplosive(final World world) {
        super(world);
    }

    public GT_Entity_DaisyCutterExplosive(
            final World world, final double x, final double y, final double z, final EntityLivingBase placedBy, final int metadata
                                         ) {
        super(world, x, y, z, placedBy, metadata);
    }

    /**
     * @param explosion
     * @param world
     * @param x
     * @param y
     * @param z
     * @param block
     * @return
     */
    @Override
    public float func_145772_a(
            final Explosion explosion, final World world, final int x, final int y, final int z, final Block block
                              ) {
        return 0.0f;
    }

    /**
     *
     */
    @Override
    protected void doExplode() {
        new GT_DaisyCutterExplosion(worldObj, this, posX, posY, posZ, GT_Values.MEExplosionPower).perform();
    }

}
