package gregtech.common.render.explosives;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.common.entities.explosives.GT_Entity_DaisyCutterExplosive;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;


@SideOnly(Side.CLIENT)
public class GT_DaisyCutterRenderer extends GT_ExplosiveRenderer<GT_Entity_DaisyCutterExplosive> {

    public GT_DaisyCutterRenderer() {
        super(GT_Entity_DaisyCutterExplosive.class);
    }

    /**
     * @return
     */
    @Override
    protected Block getBlockToRenderAs() {
        return GregTech_API.sBlockDaisyCutter;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     *
     * @param entity
     */
    @Override
    protected ResourceLocation getEntityTexture(final Entity entity) {
        return Textures.BlockIcons.DAISY_CUTTER.getTextureFile();
    }

}
