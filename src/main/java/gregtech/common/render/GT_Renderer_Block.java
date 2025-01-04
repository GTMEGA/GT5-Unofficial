package gregtech.common.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IPipeRenderedTileEntity;
import gregtech.api.interfaces.tileentity.ITexturedTileEntity;
import gregtech.common.GT_Compat;
import gregtech.common.blocks.GT_Block_Machines;
import gregtech.common.blocks.GT_Block_Ore_Abstract;
import gregtech.common.blocks.GT_Block_Potentiometer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import lombok.val;
import org.lwjgl.opengl.GL11;

import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_DOWN;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_EAST;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_NORTH;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_SOUTH;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_UP;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_WEST;
import static gregtech.api.interfaces.metatileentity.IConnectable.HAS_FRESHFOAM;
import static gregtech.api.interfaces.metatileentity.IConnectable.HAS_HARDENEDFOAM;
import static gregtech.api.interfaces.metatileentity.IConnectable.NO_CONNECTION;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.VALID_DIRECTIONS;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

public class GT_Renderer_Block implements ISimpleBlockRenderingHandler {
    public static GT_Renderer_Block INSTANCE;
    public final int mRenderID;

    public static final float blockMin = 0.0F;
    public static final float blockMax = 1.0F;
    private static final float coverThickness = blockMax / 8.0F;
    private static final float coverInnerMin = blockMin + coverThickness;
    private static final float coverInnerMax = blockMax - coverThickness;

    public GT_Renderer_Block() {
        this.mRenderID = RenderingRegistry.getNextAvailableRenderId();
        INSTANCE = this;
        RenderingRegistry.registerBlockHandler(this);
    }

    @Override
    public void renderInventoryBlock(Block block, int blockMeta, int modelID, RenderBlocks renderBlocks) {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();

        val prevEnableAO = renderBlocks.enableAO;
        val prevUseInventoryTint = renderBlocks.useInventoryTint;
        renderBlocks.enableAO = false;
        renderBlocks.useInventoryTint = true;

        try {
            GL11.glRotatef(180F, 0F, 1F, 0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

            render:
            {
                // Special case for ores
                if (block instanceof GT_Block_Ore_Abstract ore) {
                    block.setBlockBoundsForItemRender();
                    renderBlocks.setRenderBoundsFromBlock(block);
                    val textures = ore.getTextures();
                    renderAllFaces(null, renderBlocks, block, 0, 0, 0, textures, true);
                    break render;
                }
                // Special case for potentiometer
                if (block instanceof GT_Block_Potentiometer potentiometer) {
                    block.setBlockBoundsForItemRender();
                    renderBlocks.setRenderBoundsFromBlock(block);
                    val textures = potentiometer.getInventoryTexture(blockMeta);
                    renderAllFaces(null, renderBlocks, block, 0, 0, 0, textures, true);
                    break render;
                }
                // Everything else
                if (blockMeta > 0
                    && (blockMeta < GregTech_API.METATILEENTITIES.length)
                    && block instanceof GT_Block_Machines
                    && (GregTech_API.METATILEENTITIES[blockMeta] != null)
                    && (!GregTech_API.METATILEENTITIES[blockMeta].renderInInventory(block, blockMeta, renderBlocks))) {
                    renderNormalInventoryMetaTileEntity(block, blockMeta, renderBlocks);
                }
            }
        } finally {
            // Reset bounds
            block.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
            renderBlocks.setRenderBoundsFromBlock(block);

            // Reset tint/ao
            renderBlocks.useInventoryTint = prevUseInventoryTint;
            renderBlocks.enableAO = prevEnableAO;

            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world,
                                    int posX,
                                    int posY,
                                    int posZ,
                                    Block block,
                                    int modelId,
                                    RenderBlocks render) {
        val prevEnableAO = render.enableAO;
        val prevUseInventoryTint = render.useInventoryTint;

        render.enableAO = Minecraft.isAmbientOcclusionEnabled() && GT_Mod.gregtechproxy.mRenderTileAmbientOcclusion;
        render.useInventoryTint = false;

        try {
            // Special case for ores
            if (block instanceof GT_Block_Ore_Abstract ore) {
                val textures = ore.getTextures();
                return renderStandardBlock(world, posX, posY, posZ, block, render, textures);
            }
            // Special case for potentiometer
            if (block instanceof GT_Block_Potentiometer potentiometer) {
                val textures = potentiometer.getPotentiometerTextures(world, posX, posY, posZ);
                return renderStandardBlock(world, posX, posY, posZ, block, render, textures);
            }

            val te = world.getTileEntity(posX, posY, posZ);
            if (te != null) {
                if (te instanceof IGregTechTileEntity gte) {
                    // Set the MTE handle it's own rendering, or keep going
                    val metaTileEntity = gte.getMetaTileEntity();
                    if (metaTileEntity != null && metaTileEntity.renderInWorld(world, posX, posY, posZ, block, render))
                        return true;
                }
                if (te instanceof IPipeRenderedTileEntity pipeTile)
                    return renderPipeBlock(world, posX, posY, posZ, block, pipeTile, render);
                if (te instanceof ITexturedTileEntity texturedTile)
                    return renderStandardBlock(world, posX, posY, posZ, block, render, new ITexture[][]{
                            texturedTile.getTexture(block, DOWN),
                            texturedTile.getTexture(block, UP),
                            texturedTile.getTexture(block, NORTH),
                            texturedTile.getTexture(block, SOUTH),
                            texturedTile.getTexture(block, WEST),
                            texturedTile.getTexture(block, EAST)});
            }
        } finally {
            // Reset tint/ao
            render.useInventoryTint = prevUseInventoryTint;
            render.enableAO = prevEnableAO;
        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int aModel) {
        return true;
    }

    @Override
    public int getRenderId() {
        return this.mRenderID;
    }

    public static boolean renderStandardBlock(IBlockAccess world, int posX, int posY, int posZ, Block block, RenderBlocks renderBlocks) {
        val te = world.getTileEntity(posX, posY, posZ);
        if ((te instanceof IPipeRenderedTileEntity pipeTile)) {
            return renderStandardBlock(world, posX, posY, posZ, block, renderBlocks, new ITexture[][]{
                    pipeTile.getTextureCovered(DOWN),
                    pipeTile.getTextureCovered(UP),
                    pipeTile.getTextureCovered(NORTH),
                    pipeTile.getTextureCovered(SOUTH),
                    pipeTile.getTextureCovered(WEST),
                    pipeTile.getTextureCovered(EAST)});
        }
        if (te instanceof ITexturedTileEntity texturedTile) {
            return renderStandardBlock(world, posX, posY, posZ, block, renderBlocks, new ITexture[][]{
                    texturedTile.getTexture(block, DOWN),
                    texturedTile.getTexture(block, UP),
                    texturedTile.getTexture(block, NORTH),
                    texturedTile.getTexture(block, SOUTH),
                    texturedTile.getTexture(block, WEST),
                    texturedTile.getTexture(block, EAST)});
        }
        return false;
    }

    public static boolean renderStandardBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, RenderBlocks aRenderer, ITexture[][] aTextures) {
        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        aRenderer.setRenderBoundsFromBlock(aBlock);

        renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[DOWN.ordinal()], true);
        renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[UP.ordinal()], true);
        renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[NORTH.ordinal()], true);
        renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[SOUTH.ordinal()], true);
        renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[WEST.ordinal()], true);
        renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[EAST.ordinal()], true);
        return true;
    }

    public static boolean renderStandardBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, RenderBlocks aRenderer, ITexture[] aTextures) {
        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        aRenderer.setRenderBoundsFromBlock(aBlock);
        renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures, true);
        renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures, true);
        renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures, true);
        renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures, true);
        renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures, true);
        renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures, true);
        return true;
    }

    public static boolean renderPipeBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, IPipeRenderedTileEntity aTileEntity, RenderBlocks aRenderer) {
        final byte aConnections = aTileEntity.getConnections();
        if ((aConnections & (HAS_FRESHFOAM | HAS_HARDENEDFOAM)) != 0) {
            return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer);
        }
        final float thickness = aTileEntity.getThickNess();
        if (thickness >= 0.99F) {
            return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer);
        }
        // Range of block occupied by pipe
        final float pipeMin = (blockMax - thickness) / 2.0F;
        final float pipeMax = blockMax - pipeMin;
        final boolean[] tIsCovered = new boolean[VALID_DIRECTIONS.length];
        for (int i = 0; i < VALID_DIRECTIONS.length; i++) {
            tIsCovered[i] = (aTileEntity.getCoverIDAtSide((byte) i) != 0);
        }

        final ITexture[][] tIcons = new ITexture[][]{
                aTileEntity.getTextureUncovered(DOWN),
                aTileEntity.getTextureUncovered(UP),
                aTileEntity.getTextureUncovered(NORTH),
                aTileEntity.getTextureUncovered(SOUTH),
                aTileEntity.getTextureUncovered(WEST),
                aTileEntity.getTextureUncovered(EAST)};
        final ITexture[][] tCovers = new ITexture[][]{
                aTileEntity.getTexture(aBlock, DOWN),
                aTileEntity.getTexture(aBlock, UP),
                aTileEntity.getTexture(aBlock, NORTH),
                aTileEntity.getTexture(aBlock, SOUTH),
                aTileEntity.getTexture(aBlock, WEST),
                aTileEntity.getTexture(aBlock, EAST)};

        switch (aConnections) {
            case NO_CONNECTION:
                aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                aRenderer.setRenderBoundsFromBlock(aBlock);
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false);
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false);
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false);
                break;
            case CONNECTED_EAST | CONNECTED_WEST:
                // EAST - WEST Pipe Sides
                aBlock.setBlockBounds(blockMin, pipeMin, pipeMin, blockMax, pipeMax, pipeMax);
                aRenderer.setRenderBoundsFromBlock(aBlock);
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false);
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false);

                // EAST - WEST Pipe Ends
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false);
                break;
            case CONNECTED_DOWN | CONNECTED_UP:
                // UP - DOWN Pipe Sides
                aBlock.setBlockBounds(pipeMin, blockMin, pipeMin, pipeMax, blockMax, pipeMax);
                aRenderer.setRenderBoundsFromBlock(aBlock);
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false);
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false);

                // UP - DOWN Pipe Ends
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false);
                break;
            case CONNECTED_NORTH | CONNECTED_SOUTH:
                // NORTH - SOUTH Pipe Sides
                aBlock.setBlockBounds(pipeMin, pipeMin, blockMin, pipeMax, pipeMax, blockMax);
                aRenderer.setRenderBoundsFromBlock(aBlock);
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false);
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false);

                // NORTH - SOUTH Pipe Ends
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false);
                break;
            default:
                if ((aConnections & CONNECTED_WEST) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(blockMin, pipeMin, pipeMin, pipeMin, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false);
                    renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false);
                    renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false);
                    renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false);
                }
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false);

                if ((aConnections & CONNECTED_EAST) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMax, pipeMin, pipeMin, blockMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false);
                    renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false);
                    renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false);
                    renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false);
                }
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false);

                if ((aConnections & CONNECTED_DOWN) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMin, blockMin, pipeMin, pipeMax, pipeMin, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false);
                    renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false);
                    renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false);
                    renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false);
                }
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false);

                if ((aConnections & CONNECTED_UP) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMin, pipeMax, pipeMin, pipeMax, blockMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false);
                    renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false);
                    renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false);
                    renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false);
                }
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false);

                if ((aConnections & CONNECTED_NORTH) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMin, pipeMin, blockMin, pipeMax, pipeMax, pipeMin);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false);
                    renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false);
                    renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false);
                    renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false);
                }
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false);

                if ((aConnections & CONNECTED_SOUTH) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMax, pipeMax, pipeMax, blockMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false);
                    renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false);
                    renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false);
                    renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false);
                }
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false);
                break;
        }

        // Render covers on pipes
        if (tIsCovered[DOWN.ordinal()]) {
            aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, coverInnerMin, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[NORTH.ordinal()]) {
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false);
            }
            if (!tIsCovered[SOUTH.ordinal()]) {
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false);
            }
            if (!tIsCovered[WEST.ordinal()]) {
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false);
            }
            if (!tIsCovered[EAST.ordinal()]) {
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false);
            }
            renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false);
            if ((aConnections & CONNECTED_DOWN) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMin, blockMin, blockMax, blockMin, pipeMin);
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, blockMin, pipeMax, blockMax, blockMin, blockMax);
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, blockMin, pipeMin, pipeMin, blockMin, pipeMax);
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false);
                // Middle right panel
                aRenderer.setRenderBounds(pipeMax, blockMin, pipeMin, blockMax, blockMin, pipeMax);
            }
            renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false);
        }

        if (tIsCovered[UP.ordinal()]) {
            aBlock.setBlockBounds(blockMin, coverInnerMax, blockMin, blockMax, blockMax, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[NORTH.ordinal()]) {
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false);
            }
            if (!tIsCovered[SOUTH.ordinal()]) {
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false);
            }
            if (!tIsCovered[WEST.ordinal()]) {
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false);
            }
            if (!tIsCovered[EAST.ordinal()]) {
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false);
            }
            renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false);
            if ((aConnections & CONNECTED_UP) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMax, blockMin, blockMax, blockMax, pipeMin);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, blockMax, pipeMax, blockMax, blockMax, blockMax);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, blockMax, pipeMin, pipeMin, blockMax, pipeMax);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false);
                // Middle right panel
                aRenderer.setRenderBounds(pipeMax, blockMax, pipeMin, blockMax, blockMax, pipeMax);
            }
            renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false);
        }

        if (tIsCovered[NORTH.ordinal()]) {
            aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, coverInnerMin);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[DOWN.ordinal()]) {
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false);
            }
            if (!tIsCovered[UP.ordinal()]) {
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false);
            }
            if (!tIsCovered[WEST.ordinal()]) {
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false);
            }
            if (!tIsCovered[EAST.ordinal()]) {
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false);
            }
            renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false);
            if ((aConnections & CONNECTED_NORTH) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMin, blockMin, blockMax, pipeMin, blockMin);
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, pipeMax, blockMin, blockMax, blockMax, blockMin);
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, pipeMin, blockMin, pipeMin, pipeMax, blockMin);
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false);
                // Middle right panel
                aRenderer.setRenderBounds(pipeMax, pipeMin, blockMin, blockMax, pipeMax, blockMin);
            }
            renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false);
        }

        if (tIsCovered[SOUTH.ordinal()]) {
            aBlock.setBlockBounds(blockMin, blockMin, coverInnerMax, blockMax, blockMax, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[DOWN.ordinal()]) {
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false);
            }
            if (!tIsCovered[UP.ordinal()]) {
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false);
            }
            if (!tIsCovered[WEST.ordinal()]) {
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false);
            }
            if (!tIsCovered[EAST.ordinal()]) {
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false);
            }
            renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false);
            if ((aConnections & CONNECTED_SOUTH) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMin, blockMax, blockMax, pipeMin, blockMax);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, pipeMax, blockMax, blockMax, blockMax, blockMax);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, pipeMin, blockMax, pipeMin, pipeMax, blockMax);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false);
                // Middle right panel
                aRenderer.setRenderBounds(pipeMax, pipeMin, blockMax, blockMax, pipeMax, blockMax);
            }
            renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false);
        }

        if (tIsCovered[WEST.ordinal()]) {
            aBlock.setBlockBounds(blockMin, blockMin, blockMin, coverInnerMin, blockMax, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[DOWN.ordinal()]) {
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false);
            }
            if (!tIsCovered[UP.ordinal()]) {
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false);
            }
            if (!tIsCovered[NORTH.ordinal()]) {
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false);
            }
            if (!tIsCovered[SOUTH.ordinal()]) {
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false);
            }
            renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false);
            if ((aConnections & CONNECTED_WEST) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMin, blockMin, blockMin, pipeMin, blockMax);
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, pipeMax, blockMin, blockMin, blockMax, blockMax);
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, pipeMin, blockMin, blockMin, pipeMax, pipeMin);
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false);
                // Middle right panel
                aRenderer.setRenderBounds(blockMin, pipeMin, pipeMax, blockMin, pipeMax, blockMax);
            }
            renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false);
        }

        if (tIsCovered[EAST.ordinal()]) {
            aBlock.setBlockBounds(coverInnerMax, blockMin, blockMin, blockMax, blockMax, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[DOWN.ordinal()]) {
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false);
            }
            if (!tIsCovered[UP.ordinal()]) {
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false);
            }
            if (!tIsCovered[NORTH.ordinal()]) {
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false);
            }
            if (!tIsCovered[SOUTH.ordinal()]) {
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false);
            }
            renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false);

            if ((aConnections & CONNECTED_EAST) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMax, blockMin, blockMin, blockMax, pipeMin, blockMax);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false);
                // Upper panel
                aRenderer.setRenderBounds(blockMax, pipeMax, blockMin, blockMax, blockMax, blockMax);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false);
                // Middle left panel
                aRenderer.setRenderBounds(blockMax, pipeMin, blockMin, blockMax, pipeMax, pipeMin);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false);
                // Middle right panel
                aRenderer.setRenderBounds(blockMax, pipeMin, pipeMax, blockMax, pipeMax, blockMax);
            }
            renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false);
        }
        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        aRenderer.setRenderBoundsFromBlock(aBlock);

        return true;
    }

    private static void renderNormalInventoryMetaTileEntity(Block aBlock, int aMeta, RenderBlocks aRenderer) {
        if ((aMeta <= 0) || (aMeta >= GregTech_API.METATILEENTITIES.length)) {
            return;
        }
        IMetaTileEntity tMetaTileEntity = GregTech_API.METATILEENTITIES[aMeta];
        if (tMetaTileEntity == null) {
            return;
        }
        aBlock.setBlockBoundsForItemRender();
        aRenderer.setRenderBoundsFromBlock(aBlock);

        final IGregTechTileEntity iGregTechTileEntity = tMetaTileEntity.getBaseMetaTileEntity();

        val tess = GT_Compat.tessellator();
        if ((iGregTechTileEntity instanceof IPipeRenderedTileEntity)) {
            final float tThickness = ((IPipeRenderedTileEntity) iGregTechTileEntity).getThickNess();
            final float pipeMin = (blockMax - tThickness) / 2.0F;
            final float pipeMax = blockMax - pipeMin;

            aBlock.setBlockBounds(blockMin, pipeMin, pipeMin, blockMax, pipeMax, pipeMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            renderNegativeYFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) DOWN.ordinal(), (byte) (CONNECTED_WEST | CONNECTED_EAST), (byte) -1, false, false), true);
            renderPositiveYFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) UP.ordinal(), (byte) (CONNECTED_WEST | CONNECTED_EAST), (byte) -1, false, false), true);
            renderNegativeZFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) NORTH.ordinal(), (byte) (CONNECTED_WEST | CONNECTED_EAST), (byte) -1, false, false), true);
            renderPositiveZFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) SOUTH.ordinal(), (byte) (CONNECTED_WEST | CONNECTED_EAST), (byte) -1, false, false), true);
            renderNegativeXFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) WEST.ordinal(), (byte) (CONNECTED_WEST | CONNECTED_EAST), (byte) -1, true, false), true);
            renderPositiveXFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) EAST.ordinal(), (byte) (CONNECTED_WEST | CONNECTED_EAST), (byte) -1, true, false), true);
        } else {
            renderNegativeYFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) DOWN.ordinal(), (byte) WEST.ordinal(), (byte) -1, true, false), true);
            renderPositiveYFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) UP.ordinal(), (byte) WEST.ordinal(), (byte) -1, true, false), true);
            renderNegativeZFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) NORTH.ordinal(), (byte) WEST.ordinal(), (byte) -1, true, false), true);
            renderPositiveZFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) SOUTH.ordinal(), (byte) WEST.ordinal(), (byte) -1, true, false), true);
            renderNegativeXFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) WEST.ordinal(), (byte) WEST.ordinal(), (byte) -1, true, false), true);
            renderPositiveXFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) EAST.ordinal(), (byte) WEST.ordinal(), (byte) -1, true, false), true);
        }
    }

    public static void renderFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock, byte side) {
        switch (side) {
            case 0: {
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aIcon, aFullBlock);
                break;
            }
            case 1: {
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aIcon, aFullBlock);
                break;
            }
            case 2: {
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aIcon, aFullBlock);
                break;
            }
            case 3: {
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aIcon, aFullBlock);
                break;
            }
            case 4: {
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aIcon, aFullBlock);
                break;
            }
            case 5: {
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aIcon, aFullBlock);
                break;
            }
        }
    }

    public static void renderAllFaces(IBlockAccess world,
                                      RenderBlocks renderBlocks,
                                      Block block,
                                      int posX,
                                      int posY,
                                      int posZ,
                                      ITexture[] textures,
                                      boolean isFullBlock) {
        renderNegativeYFacing(world, renderBlocks, block, posX, posY, posZ, textures, isFullBlock);
        renderPositiveYFacing(world, renderBlocks, block, posX, posY, posZ, textures, isFullBlock);
        renderNegativeZFacing(world, renderBlocks, block, posX, posY, posZ, textures, isFullBlock);
        renderPositiveZFacing(world, renderBlocks, block, posX, posY, posZ, textures, isFullBlock);
        renderNegativeXFacing(world, renderBlocks, block, posX, posY, posZ, textures, isFullBlock);
        renderPositiveXFacing(world, renderBlocks, block, posX, posY, posZ, textures, isFullBlock);
    }

    public static boolean renderNegativeYFacing(IBlockAccess world,
                                             RenderBlocks render,
                                             Block block,
                                             int posX,
                                             int posY,
                                             int posZ,
                                             ITexture[] textures,
                                             boolean isFullBlock) {
        if (textures == null)
            return false;
        if (world != null) {
            if ((isFullBlock) && (!block.shouldSideBeRendered(world, posX, posY - 1, posZ, 0)))
                return false;
            val brightness = block.getMixedBrightnessForBlock(world, posX, isFullBlock ? posY - 1 : posY, posZ);
            GT_Compat.tessellator().setBrightness(brightness);
        }

        var didWork = false;
        for (val texture : textures)
            if (texture != null)
                didWork |= texture.renderYNeg(render, block, posX, posY, posZ);
        return didWork;
    }

    public static boolean renderPositiveYFacing(IBlockAccess world,
                                             RenderBlocks render,
                                             Block block,
                                             int posX,
                                             int posY,
                                             int posZ,
                                             ITexture[] textures,
                                             boolean isFullBlock) {
        if (textures == null)
            return false;
        if (world != null) {
            if ((isFullBlock) && (!block.shouldSideBeRendered(world, posX, posY + 1, posZ, 1)))
                return false;
            val brightness = block.getMixedBrightnessForBlock(world, posX, isFullBlock ? posY + 1 : posY, posZ);
            GT_Compat.tessellator().setBrightness(brightness);
        }

        var didWork = false;
        for (val texture : textures)
            if (texture != null)
                didWork |= texture.renderYPos(render, block, posX, posY, posZ);
        return didWork;
    }

    public static boolean renderNegativeZFacing(IBlockAccess world,
                                             RenderBlocks render,
                                             Block block,
                                             int posX,
                                             int posY,
                                             int posZ,
                                             ITexture[] textures,
                                             boolean isFullBlock) {
        if (textures == null)
            return false;
        if (world != null) {
            if ((isFullBlock) && (!block.shouldSideBeRendered(world, posX, posY, posZ - 1, 2)))
                return false;
            val brightness = block.getMixedBrightnessForBlock(world, posX, posY, isFullBlock ? posZ - 1 : posZ);
            GT_Compat.tessellator().setBrightness(brightness);
        }

        var didWork = false;
        for (val texture : textures)
            if (texture != null)
                didWork |= texture.renderZNeg(render, block, posX, posY, posZ);
        return didWork;
    }

    public static boolean renderPositiveZFacing(IBlockAccess world,
                                             RenderBlocks render,
                                             Block block,
                                             int posX,
                                             int posY,
                                             int posZ,
                                             ITexture[] textures,
                                             boolean isFullBlock) {
        if (textures == null)
            return false;
        if (world != null) {
            if ((isFullBlock) && (!block.shouldSideBeRendered(world, posX, posY, posZ + 1, 3)))
                return false;
            val brightness = block.getMixedBrightnessForBlock(world, posX, posY, isFullBlock ? posZ + 1 : posZ);
            GT_Compat.tessellator().setBrightness(brightness);
        }

        var didWork = false;
        for (val texture : textures)
            if (texture != null)
                didWork |= texture.renderZPos(render, block, posX, posY, posZ);
        return didWork;
    }

    public static boolean renderNegativeXFacing(IBlockAccess world,
                                             RenderBlocks render,
                                             Block block,
                                             int posX,
                                             int posY,
                                             int posZ,
                                             ITexture[] textures,
                                             boolean isFullBlock) {
        if (textures == null)
            return false;
        if (world != null) {
            if ((isFullBlock) && (!block.shouldSideBeRendered(world, posX - 1, posY, posZ, 4)))
                return false;
            val brightness = block.getMixedBrightnessForBlock(world, isFullBlock ? posX - 1 : posX, posY, posZ);
            GT_Compat.tessellator().setBrightness(brightness);
        }

        var didWork = false;
        for (val texture : textures)
            if (texture != null)
                didWork |= texture.renderXNeg(render, block, posX, posY, posZ);
        return didWork;
    }

    public static boolean renderPositiveXFacing(IBlockAccess world,
                                             RenderBlocks render,
                                             Block block,
                                             int posX,
                                             int posY,
                                             int posZ,
                                             ITexture[] textures,
                                             boolean isFullBlock) {
        if (textures == null)
            return false;
        if (world != null) {
            if ((isFullBlock) && (!block.shouldSideBeRendered(world, posX + 1, posY, posZ, 5)))
                return false;
            val brightness = block.getMixedBrightnessForBlock(world, isFullBlock ? posX + 1 : posX, posY, posZ);
            GT_Compat.tessellator().setBrightness(brightness);
        }

        var didWork = false;
        for (val texture : textures)
            if (texture != null)
                didWork |= texture.renderXPos(render, block, posX, posY, posZ);
        return didWork;
    }
}
