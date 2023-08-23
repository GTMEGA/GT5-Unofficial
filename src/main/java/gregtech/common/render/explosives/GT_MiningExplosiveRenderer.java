package gregtech.common.render.explosives;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.common.entities.explosives.GT_Entity_MiningExplosive;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;


@SideOnly(Side.CLIENT)
public class GT_MiningExplosiveRenderer extends GT_ExplosiveRenderer<GT_Entity_MiningExplosive> {

    public GT_MiningExplosiveRenderer() {
        super(GT_Entity_MiningExplosive.class);
    }

    protected Block getBlockToRenderAs() {
        return GregTech_API.sBlockMiningExplosive;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     *
     * @param p_110775_1_
     */
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return Textures.BlockIcons.MINING_EXPLOSIVE.getTextureFile();
    }

}
