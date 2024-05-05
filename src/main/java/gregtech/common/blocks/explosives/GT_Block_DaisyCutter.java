package gregtech.common.blocks.explosives;


import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.common.entities.explosives.GT_Entity_DaisyCutterExplosive;
import gregtech.common.items.explosives.GT_Item_DaisyCutter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;


public class GT_Block_DaisyCutter extends GT_Block_Explosive {

    public GT_Block_DaisyCutter() {
        super(GT_Item_DaisyCutter.class, "daisy_cutter", Textures.BlockIcons.DAISY_CUTTERS);
    }

    /**
     * @param world
     * @param x
     * @param y
     * @param z
     * @param player
     * @param fuse
     */
    @Override
    protected void goBoom(final World world, final int x, final int y, final int z, final EntityPlayer player, final int fuse) {
        final int metadata = world.getBlockMetadata(x, y, z);
        final GT_Entity_DaisyCutterExplosive explosive = new GT_Entity_DaisyCutterExplosive(world, x, y, z, player, metadata, fuse);
        world.spawnEntityInWorld(explosive);
        world.playSoundAtEntity(explosive, GregTech_API.sSoundList.get(214), 1.0f, 1.0f);
        world.setBlockToAir(x, y, z);
    }

}
