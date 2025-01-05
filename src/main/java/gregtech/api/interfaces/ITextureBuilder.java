package gregtech.api.interfaces;

import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import gregtech.api.render.TextureFactory;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * <p>This Interface defines operations to configure and build instances of the {@link ITexture} implementations</p>
 * <p>Use the {@link TextureFactory#builder()} method to get an instance of the {@link ITextureBuilder} implementation.</p>
 */
public interface ITextureBuilder {
    /**
     * Build the {@link ITexture}
     *
     * @return The built {@link ITexture}
     */
    ITexture build();

    /**
     * @param block The {@link Block}
     * @param meta The meta value for the Block
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder setFromBlock(final Block block, final int meta);

    /**
     * @param side <p>The {@link ForgeDirection} side providing the texture</p>
     *             <p>Default is {@link ForgeDirection#UNKNOWN} to use same side as rendered</p>
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder setFromSide(final ForgeDirection side);

    /**
     * @param iconContainers The {@link IIconContainer}s to add
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder addIcon(final IIconContainer... iconContainers);

    /**
     * @param rgba The RGBA tint for this {@link ITexture}
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder setRGBA(final short[] rgba);

    /**
     * @param iTextures The {@link ITexture} layers to add
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder addLayer(final ITexture... iTextures);

    /**
     * Texture will render with same orientation as with vanilla blocks
     *
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder stdOrient();

    /**
     * Texture will orientate from block's {@link ExtendedFacing}
     *
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder extFacing();

    /**
     * Should texture render as opaque or as translucent?
     *
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder setTranslucent(boolean isTranslucent);
}
