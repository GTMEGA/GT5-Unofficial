package gregtech.common.blocks.explosives;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.common.entities.explosives.GT_Entity_NukeExplosive;
import gregtech.common.items.explosives.GT_Item_NukeExplosive;
import lombok.val;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;

public class GT_Block_NukeExplosive extends GT_Block_Explosive {

    public GT_Block_NukeExplosive() {
        super(GT_Item_NukeExplosive.class, "mega_nuke", Textures.BlockIcons.NUKE_EXPLOSIVES);
    }

    @Override
    protected void goBoom(World world, int x, int y, int z, EntityPlayer player) {
        val metadata = world.getBlockMetadata(x, y, z);
        val entity = new GT_Entity_NukeExplosive(world, x, y, z, player, metadata);
        world.spawnEntityInWorld(entity);
        world.playSoundAtEntity(entity, GregTech_API.sSoundList.get(223), 1.0F, 1.0F);
        world.setBlockToAir(x, y, z);
    }

}
