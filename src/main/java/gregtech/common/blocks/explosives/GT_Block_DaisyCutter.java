package gregtech.common.blocks.explosives;


import gregtech.common.entities.explosives.GT_Entity_DaisyCutterExplosive;
import gregtech.common.entities.explosives.GT_Entity_Explosive;
import gregtech.common.items.explosives.GT_Item_DaisyCutter;
import lombok.NonNull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import static gregtech.common.misc.explosions.IGT_ExplosiveTier.GT_DaisyCutterTier;


public class GT_Block_DaisyCutter extends GT_Block_Explosive<GT_DaisyCutterTier> {

    public GT_Block_DaisyCutter(final @NonNull GT_DaisyCutterTier tier) {
        super(GT_Item_DaisyCutter.class, "daisy_cutter", tier);
    }

    /**
     * @param world
     * @param x
     * @param y
     * @param z
     * @param player
     * @param fuse
     */
//    @Override
//    protected void goBoom(final World world, final int x, final int y, final int z, final EntityPlayer player, final int fuse) {
//        final int metadata = world.getBlockMetadata(x, y, z);
//        final GT_Entity_DaisyCutterExplosive explosive = new GT_Entity_DaisyCutterExplosive(world, x, y, z, player, metadata, fuse);
//        world.spawnEntityInWorld(explosive);
//        world.playSoundAtEntity(explosive, GregTech_API.sSoundList.get(214), 1.0f, 1.0f);
//        world.setBlockToAir(x, y, z);
//    }

    /**
     * @param world
     * @param x
     * @param y
     * @param z
     * @param placedBy
     * @param metadata
     * @param timer
     * @return
     */
    @Override
    protected GT_Entity_Explosive<GT_DaisyCutterTier> createExplosive(final World world, final double x, final double y, final double z, final EntityPlayer placedBy, final int metadata, final int timer) {
        return new GT_Entity_DaisyCutterExplosive(world, x, y, z, placedBy, metadata, timer, this.tier);
    }

}
