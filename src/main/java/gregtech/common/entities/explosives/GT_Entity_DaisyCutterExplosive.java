package gregtech.common.entities.explosives;


import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.common.misc.explosions.GT_DaisyCutterExplosion;
import lombok.NonNull;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import static gregtech.common.misc.explosions.IGT_ExplosiveTier.GT_DaisyCutterTier;


public class GT_Entity_DaisyCutterExplosive extends GT_Entity_Explosive<GT_DaisyCutterTier> {

    @SuppressWarnings("unused")
    public GT_Entity_DaisyCutterExplosive(final World world) {
        super(world);
    }

    public GT_Entity_DaisyCutterExplosive(
            final World world, final double x, final double y, final double z, final EntityLivingBase placedBy, final int metadata, final int fuse, final @NonNull GT_DaisyCutterTier tier
                                         ) {
        super(world, x, y, z, placedBy, metadata, fuse, tier);
    }

    /**
     * @return
     */
    @NonNull
    @Override
    protected GT_DaisyCutterExplosion createExplosion() {
        return new GT_DaisyCutterExplosion(worldObj, this, posX, posY, posZ, GT_Values.MEExplosionPower);
    }

    /**
     * @return
     */
    @Override
    public Class<GT_DaisyCutterTier> getTierClass() {
        return GT_DaisyCutterTier.class;
    }

    /**
     * @param explosion
     * @param world
     * @param x
     * @param y
     * @param z
     * @param block
     *
     * @return
     */
    @Override
    public float getBlockResistance(
            final Explosion explosion, final World world, final int x, final int y, final int z, final Block block
                                   ) {
        return 0.0f;
    }

    /**
     * @return
     */
    @Override
    public ResourceLocation getEntityTexture() {
        return Textures.BlockIcons.TUNEX.getTextureFile();
    }

}
