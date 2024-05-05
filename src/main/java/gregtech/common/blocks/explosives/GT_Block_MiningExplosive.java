package gregtech.common.blocks.explosives;


import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.common.entities.explosives.GT_Entity_MiningExplosive;
import gregtech.common.items.explosives.GT_Item_MiningExplosive;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;


public class GT_Block_MiningExplosive extends GT_Block_Explosive {

    public GT_Block_MiningExplosive() {
        super(GT_Item_MiningExplosive.class, "mining_explosives", Textures.BlockIcons.MINING_EXPLOSIVES);
    }

    @Override
    protected void goBoom(final World world, final int x, final int y, final int z, final EntityPlayer player, final int fuse) {
        final int metadata = world.getBlockMetadata(x, y, z);
        final GT_Entity_MiningExplosive explosive = new GT_Entity_MiningExplosive(world, x, y, z, player, metadata, fuse);
        world.spawnEntityInWorld(explosive);
        world.playSoundAtEntity(explosive, GregTech_API.sSoundList.get(214), 1.0F, 1.0F);
        world.setBlockToAir(x, y, z);
    }

}
