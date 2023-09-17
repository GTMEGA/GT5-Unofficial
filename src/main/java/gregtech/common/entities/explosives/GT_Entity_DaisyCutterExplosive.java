package gregtech.common.entities.explosives;


import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.common.misc.explosions.GT_DaisyCutterExplosion;
import gregtech.common.misc.explosions.GT_Explosion;
import lombok.NonNull;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;


public class GT_Entity_DaisyCutterExplosive extends GT_Entity_Explosive {

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
     * @return
     */
    @NonNull
    @Override
    protected GT_Explosion createExplosion() {
        return new GT_DaisyCutterExplosion(worldObj, this, posX, posY, posZ, GT_Values.MEExplosionPower);
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
    public float getBlockResistance(
            final Explosion explosion, final World world, final int x, final int y, final int z, final Block block
                                   ) {
        return 0.0f;
    }

    /**
     * @return
     */
    @Override
    public Block getBlockToRenderAs() {
        return GregTech_API.sBlockDaisyCutter;
    }

    /**
     * @return
     */
    @Override
    public ResourceLocation getEntityTexture() {
        return Textures.BlockIcons.TUNEX.getTextureFile();
    }

}
