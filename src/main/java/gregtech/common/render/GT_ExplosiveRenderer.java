package gregtech.common.render;


import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import gregtech.common.entities.explosives.GT_Entity_Explosive;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;


@SideOnly(Side.CLIENT)
public class GT_ExplosiveRenderer extends Render {

    public GT_ExplosiveRenderer() {
    }

    public GT_ExplosiveRenderer addExplosive(final Class<? extends GT_Entity_Explosive> explosiveClass) {
        RenderingRegistry.registerEntityRenderingHandler(explosiveClass, this);
        return this;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     *
     * @param entity
     * @param x
     * @param y
     * @param z
     * @param f0
     * @param someTimer
     */
    @Override
    public void doRender(
            final Entity entity, final double x, final double y, final double z, final float f0, final float someTimer
                        ) {
        if (entity instanceof GT_Entity_Explosive) {
            this.doRender((GT_Entity_Explosive) entity, x, y, z, f0, someTimer);
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     *
     * @param entity
     */
    @Override
    protected ResourceLocation getEntityTexture(final Entity entity) {
        return ((GT_Entity_Explosive) entity).getEntityTexture();
    }

    public void doRender(final GT_Entity_Explosive entity, final double x, final double y, final double z, final float f0, final float someTimer) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);
        float f2 = GT_Values.MEMaxEntitySize - (entity.fuse - someTimer + 1.0f) / ((float) entity.getMaxFuse() / 2);
        f2 = Math.max(f2, GT_Values.MEMinEntitySize);
        float f3 = Math.min(f2, 1.0f);
        GL11.glScalef(f2, f2, f2);
        this.bindEntityTexture(entity);
        renderTheBlock(entity, Math.max(0.5f, Math.min(1.0f, entity.getBrightness(someTimer) * 2.0f)));

        if (entity.fuse / 5 % 2 == 0) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
            GL11.glColor4f(1.0F, 1.0f - f3, 1.0f - f3, f3);
            renderTheBlock(entity, 1.0f);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        GL11.glPopMatrix();
    }

    protected void renderTheBlock(final GT_Entity_Explosive entityExplosive, final float brightness) {
        final RenderBlocks renderer = RenderBlocks.getInstance();
        GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
        renderer.renderBlockAsItem(entityExplosive.getBlockToRenderAs(), entityExplosive.getMetadata(), brightness);
    }

}
