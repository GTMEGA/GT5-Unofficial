package gregtech.api.objects;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.LightingHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * This ITexture implementation extends the GT_RenderedTexture class
 * to render with bottom side flipped as with dumb blocks rendering.
 * It is used in Ore blocks rendering so they better blends with dumb block ores
 * from vanilla or other mods, when seen from bottom.
 * @deprecated Replaced by the {@link gregtech.api.render.TextureFactory} API.
 */
@Deprecated
public class GT_StdRenderedTexture extends GT_RenderedTexture{

    @SuppressWarnings("unused")
    public GT_StdRenderedTexture(IIconContainer aIcon, short[] aRGBa, boolean aAllowAlpha) {
        super(aIcon, aRGBa, aAllowAlpha);
    }

    public GT_StdRenderedTexture(IIconContainer aIcon, short[] aRGBa) {
        super(aIcon, aRGBa, true);
    }

    @SuppressWarnings("unused")
    public GT_StdRenderedTexture(IIconContainer aIcon) {
        super(aIcon, Dyes._NULL.mRGBa);
    }
}
