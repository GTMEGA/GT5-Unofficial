package gregtech.common.render;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.ITextureBuilder;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class GT_TextureBuilder implements ITextureBuilder {
    private final List<IIconContainer> iconContainerList;
    private final List<ITexture> textureLayers;
    private Block fromBlock;
    private int fromMeta;
    private ForgeDirection fromSide;
    private short[] rgba;
    private boolean stdOrient;
    private boolean extFacing;
    private boolean isTranslucent;

    public GT_TextureBuilder() {
        textureLayers = new ArrayList<>();
        iconContainerList = new ArrayList<>();
        rgba = Dyes._NULL.mRGBa;
        stdOrient = false;
    }

    @Override
    public ITextureBuilder setFromBlock(final Block block, final int meta) {
        this.fromBlock = block;
        this.fromMeta = meta;
        this.fromSide = ForgeDirection.UNKNOWN;
        return this;
    }

    @Override
    public ITextureBuilder setFromSide(final ForgeDirection side) {
        this.fromSide = side;
        return this;
    }

    @Override
    public ITextureBuilder addIcon(final IIconContainer... iconContainers) {
        this.iconContainerList.addAll(Arrays.asList(iconContainers));
        return this;
    }

    @Override
    public ITextureBuilder setRGBA(final short[] rgba) {
        this.rgba = rgba;
        return this;
    }

    @Override
    public ITextureBuilder addLayer(final ITexture... iTextures) {
        this.textureLayers.addAll(Arrays.asList(iTextures));
        return this;
    }

    @Override
    public ITextureBuilder stdOrient() {
        this.stdOrient = true;
        return this;
    }

    @Override
    public ITextureBuilder extFacing() {
        this.extFacing = true;
        return this;
    }

    @Override
    public ITextureBuilder setTranslucent(boolean isTranslucent) {
        this.isTranslucent = isTranslucent;
        return this;
    }

    @Override
    public ITexture build() {
        if (fromBlock != null) return new GT_CopiedBlockTexture(fromBlock, fromSide.ordinal(), fromMeta, rgba);
        if (!textureLayers.isEmpty()) return new GT_MultiTexture(textureLayers.toArray(new ITexture[0]));
        return switch (iconContainerList.size()) {
            case 1 -> new GT_RenderedTexture(iconContainerList.get(0), rgba, stdOrient, extFacing, isTranslucent);
            case 6 -> new GT_SidedTexture(
                    iconContainerList.get(ForgeDirection.DOWN.ordinal()),
                    iconContainerList.get(ForgeDirection.UP.ordinal()),
                    iconContainerList.get(ForgeDirection.NORTH.ordinal()),
                    iconContainerList.get(ForgeDirection.SOUTH.ordinal()),
                    iconContainerList.get(ForgeDirection.WEST.ordinal()),
                    iconContainerList.get(ForgeDirection.EAST.ordinal()),
                    rgba,
                    isTranslucent);
            default -> throw new IllegalStateException("Invalid sideIconContainer count");
        };
    }
}
