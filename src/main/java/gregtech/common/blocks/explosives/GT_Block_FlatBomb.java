package gregtech.common.blocks.explosives;


import gregtech.common.entities.explosives.GT_Entity_Explosive;
import gregtech.common.entities.explosives.GT_Entity_FlatBomb;
import gregtech.common.items.explosives.GT_Item_FlatBomb;
import lombok.NonNull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;

import static gregtech.common.misc.explosions.IGT_ExplosiveTier.GT_FlatBombTier;

public class GT_Block_FlatBomb extends GT_Block_Explosive<GT_FlatBombTier> {

    public GT_Block_FlatBomb(final @NonNull GT_FlatBombTier tier) {
        super(GT_Item_FlatBomb.class, "flat_bomb", tier);
    }

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
    protected GT_Entity_Explosive<GT_FlatBombTier> createExplosive(final World world, final double x, final double y, final double z, final EntityPlayer placedBy, final int metadata, final int timer) {
        return new GT_Entity_FlatBomb(world, x, y, z, placedBy, metadata, timer, this.tier.asType());
    }

}
