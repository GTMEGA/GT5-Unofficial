package gregtech.common.entities.explosives;


import gregtech.api.enums.Textures;
import gregtech.common.blocks.explosives.GT_Block_Explosive;
import gregtech.common.misc.explosions.GT_Explosion;
import gregtech.common.misc.explosions.GT_FlatBombExplosion;
import gregtech.common.misc.explosions.IGT_ExplosiveTier;
import lombok.NonNull;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;


public class GT_Entity_FlatBomb extends GT_Entity_Explosive<IGT_ExplosiveTier.GT_FlatBombTier> {

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
        val side = GT_Block_Explosive.getFacing(metadata);
        return new GT_FlatBombExplosion(worldObj, this, posX + side.offsetX, posY + side.offsetY, posZ + side.offsetZ, (float) tier.getPower(), side);
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
     *
     * @return
     */
    @Override
    public float getBlockResistance(final Explosion explosion, final World world, final int x, final int y, final int z, final Block block) {
        final float defaultResistance = defaultBlockResistance(explosion, world, x, y, z, block);
        if (defaultResistance > 10) {
            return defaultResistance;
        }
        return 0.0f;
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
