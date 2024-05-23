package gregtech.common.entities.explosives;


import gregtech.api.enums.Textures;
import gregtech.common.misc.explosions.GT_Explosion;
import gregtech.common.misc.explosions.GT_FlatBombExplosion;
import gregtech.common.misc.explosions.IGT_ExplosiveTier;
import lombok.NonNull;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;


public class GT_Entity_FlatBomb extends GT_Entity_Explosive<IGT_ExplosiveTier.GT_FlatBombTier>{

    public GT_Entity_FlatBomb(final World world) {
        super(world);
    }

    public GT_Entity_FlatBomb(final World world, final double x, final double y, final double z, final EntityLivingBase placedBy, final int metadata, final int timer, final IGT_ExplosiveTier.@NonNull GT_FlatBombTier tier) {
        super(world, x, y, z, placedBy, metadata, timer, tier);
    }

    /**
     * @return
     */
    @Override
    protected @NonNull GT_Explosion<IGT_ExplosiveTier.GT_FlatBombTier> createExplosion() {
        return new GT_FlatBombExplosion(worldObj, this, posX, posY, posZ, (float)tier.getPower());
    }

    /**
     * @return
     */
    @Override
    public Class<IGT_ExplosiveTier.GT_FlatBombTier> getTierClass() {
        return IGT_ExplosiveTier.GT_FlatBombTier.class;
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
    public float getBlockResistance(final Explosion explosion, final World world, final int x, final int y, final int z, final Block block) {
        return 0;
    }

    /**
     * @return
     */
    @Override
    public ResourceLocation getEntityTexture() {
        // TODO: Temporary texture
        return Textures.BlockIcons.TUNEX.getTextureFile();
    }

}
