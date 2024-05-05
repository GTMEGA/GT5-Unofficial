package gregtech.common.entities.explosives;


import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.common.blocks.explosives.GT_Block_Explosive;
import gregtech.common.misc.explosions.GT_TunnelExplosion;
import lombok.NonNull;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static gregtech.common.misc.explosions.IGT_ExplosiveTier.GT_TunnelExplosiveTier;


public class GT_Entity_TunnelExplosive extends GT_Entity_Explosive<GT_TunnelExplosiveTier> {

    @SuppressWarnings("unused")
    public GT_Entity_TunnelExplosive(final World world) {
        super(world);
    }

    public GT_Entity_TunnelExplosive(
            final World world, final double x, final double y, final double z, final EntityLivingBase placedBy, final int metadata, final int fuse, final @NonNull GT_TunnelExplosiveTier tier
                                    ) {
        super(world, x, y, z, placedBy, metadata, fuse, tier);
    }

    /**
     * @return
     */
    @Override
    protected @NonNull GT_TunnelExplosion createExplosion() {
        final ForgeDirection side = GT_Block_Explosive.getFacing(metadata);
        final double xOff, yOff, zOff;
        xOff = side.offsetX;
        yOff = side.offsetY;
        zOff = side.offsetZ;
        return new GT_TunnelExplosion(worldObj, this, posX + xOff, posY + yOff, posZ + zOff, GT_Values.MEExplosionPower, side);
    }

    /**
     * @return
     */
    @Override
    public Class<GT_TunnelExplosiveTier> getTierClass() {
        return GT_TunnelExplosiveTier.class;
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
        return Textures.BlockIcons.TUNEX.getTextureFile();
    }

}
