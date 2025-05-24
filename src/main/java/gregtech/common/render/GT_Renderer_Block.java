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
import net.minecraft.world.IBlockAccess;

import lombok.val;
import net.minecraftforge.client.ForgeHooksClient;
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
        val isTranslucentPass = false; //TODO: Rendering item blocks in inventory with translucency

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
                    renderAllFaces(null, renderBlocks, block, 0, 0, 0, textures, true, isTranslucentPass);
                    break render;
                }
                // Special case for potentiometer
                if (block instanceof GT_Block_Potentiometer potentiometer) {
                    block.setBlockBoundsForItemRender();
                    renderBlocks.setRenderBoundsFromBlock(block);
                    val textures = potentiometer.getInventoryTexture(blockMeta);
                    renderAllFaces(null, renderBlocks, block, 0, 0, 0, textures, true, isTranslucentPass);
                    break render;
                }
                // Everything else
                if (blockMeta > 0
                    && (blockMeta < GregTech_API.METATILEENTITIES.length)
                    && block instanceof GT_Block_Machines
                    && (GregTech_API.METATILEENTITIES[blockMeta] != null)
                    && (!GregTech_API.METATILEENTITIES[blockMeta].renderInInventory(block, blockMeta, renderBlocks))) {
                    renderNormalInventoryMetaTileEntity(block, blockMeta, renderBlocks, isTranslucentPass);
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
        final boolean isTranslucentPass;
        {
            val renderPass = ForgeHooksClient.getWorldRenderPass();
            if (renderPass == 1) {
                if (!GT_Mod.gregtechproxy.mRenderTileTranslucentPass)
                    return false;
                isTranslucentPass = true;
            } else {
                // This will happen for either pass -1 (block breaking overlay usually)
                // Or pass 0, which is the opaque render into the actual chunk mesh
                isTranslucentPass = false;
            }
        }

        val prevEnableAO = render.enableAO;
        val prevUseInventoryTint = render.useInventoryTint;

        render.enableAO = Minecraft.isAmbientOcclusionEnabled() && GT_Mod.gregtechproxy.mRenderTileAmbientOcclusion;
        render.useInventoryTint = false;

        try {
            // Special case for ores
            if (block instanceof GT_Block_Ore_Abstract ore) {
                val textures = ore.getTextures();
                return renderStandardBlock(world, posX, posY, posZ, block, render, textures, isTranslucentPass);
            }
            // Special case for potentiometer
            if (block instanceof GT_Block_Potentiometer potentiometer) {
                val textures = potentiometer.getPotentiometerTextures(world, posX, posY, posZ);
                return renderStandardBlock(world, posX, posY, posZ, block, render, textures, isTranslucentPass);
            }

            val te = world.getTileEntity(posX, posY, posZ);
            if (!(te instanceof ITexturedTileEntity texturedTile))
                return false;
            if (isTranslucentPass && !texturedTile.hasTranslucency())
                return false;

            if (te instanceof IGregTechTileEntity gte) {
                // Set the MTE handle it's own rendering, or keep going
                val metaTileEntity = gte.getMetaTileEntity();
                if (metaTileEntity != null && metaTileEntity.renderInWorld(world, posX, posY, posZ, block, render, isTranslucentPass))
                    return true;
            }
            if (te instanceof IPipeRenderedTileEntity pipeTile)
                return renderPipeBlock(world, posX, posY, posZ, block, pipeTile, render, isTranslucentPass);
            return renderStandardBlock(world, posX, posY, posZ, block, render, new ITexture[][]{
                                               texturedTile.getTexture(block, DOWN),
                                               texturedTile.getTexture(block, UP),
                                               texturedTile.getTexture(block, NORTH),
                                               texturedTile.getTexture(block, SOUTH),
                                               texturedTile.getTexture(block, WEST),
                                               texturedTile.getTexture(block, EAST)},
                                       isTranslucentPass);
        } finally {
            // Reset bounds
            block.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
            render.setRenderBoundsFromBlock(block);

            // Reset tint/ao
            render.useInventoryTint = prevUseInventoryTint;
            render.enableAO = prevEnableAO;
        }
    }

    @Override
    public boolean shouldRender3DInInventory(int aModel) {
        return true;
    }

    @Override
    public int getRenderId() {
        return this.mRenderID;
    }

    public static boolean renderStandardBlock(IBlockAccess world, int posX, int posY, int posZ, Block block, RenderBlocks renderBlocks, boolean isTranslucentPass) {
        val te = world.getTileEntity(posX, posY, posZ);
        if ((te instanceof IPipeRenderedTileEntity pipeTile)) {
            return renderStandardBlock(world, posX, posY, posZ, block, renderBlocks, new ITexture[][]{
                                               pipeTile.getTextureCovered(DOWN),
                                               pipeTile.getTextureCovered(UP),
                                               pipeTile.getTextureCovered(NORTH),
                                               pipeTile.getTextureCovered(SOUTH),
                                               pipeTile.getTextureCovered(WEST),
                                               pipeTile.getTextureCovered(EAST)},
                                       isTranslucentPass);
        }
        if (te instanceof ITexturedTileEntity texturedTile) {
            return renderStandardBlock(world, posX, posY, posZ, block, renderBlocks, new ITexture[][]{
                                               texturedTile.getTexture(block, DOWN),
                                               texturedTile.getTexture(block, UP),
                                               texturedTile.getTexture(block, NORTH),
                                               texturedTile.getTexture(block, SOUTH),
                                               texturedTile.getTexture(block, WEST),
                                               texturedTile.getTexture(block, EAST)},
                                       isTranslucentPass);
        }
        return false;
    }

    public static boolean renderStandardBlock(IBlockAccess world, int posX, int posY, int posZ, Block block, RenderBlocks render, ITexture[][] textures, boolean isTranslucentPass) {
        block.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        render.setRenderBoundsFromBlock(block);
        var didWork = false;
        didWork |= renderNegativeYFacing(world, render, block, posX, posY, posZ, textures[DOWN.ordinal()], true, isTranslucentPass);
        didWork |= renderPositiveYFacing(world, render, block, posX, posY, posZ, textures[UP.ordinal()], true, isTranslucentPass);
        didWork |= renderNegativeZFacing(world, render, block, posX, posY, posZ, textures[NORTH.ordinal()], true, isTranslucentPass);
        didWork |= renderPositiveZFacing(world, render, block, posX, posY, posZ, textures[SOUTH.ordinal()], true, isTranslucentPass);
        didWork |= renderNegativeXFacing(world, render, block, posX, posY, posZ, textures[WEST.ordinal()], true, isTranslucentPass);
        didWork |= renderPositiveXFacing(world, render, block, posX, posY, posZ, textures[EAST.ordinal()], true, isTranslucentPass);
        return didWork;
    }

    public static boolean renderStandardBlock(IBlockAccess world, int posX, int posY, int posZ, Block block, RenderBlocks render, ITexture[] textures, boolean isTranslucentPass) {
        block.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        render.setRenderBoundsFromBlock(block);
        return renderAllFaces(world, render, block, posX, posY, posZ, textures, true, isTranslucentPass);
    }

    public static boolean renderPipeBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, IPipeRenderedTileEntity aTileEntity, RenderBlocks aRenderer, boolean isTranslucentPass) {
        final byte aConnections = aTileEntity.getConnections();
        if ((aConnections & (HAS_FRESHFOAM | HAS_HARDENEDFOAM)) != 0) {
            return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer, isTranslucentPass);
        }
        final float thickness = aTileEntity.getThickNess();
        if (thickness >= 0.99F) {
            return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer, isTranslucentPass);
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

        var didWork = false;
        switch (aConnections) {
            case NO_CONNECTION:
                aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                aRenderer.setRenderBoundsFromBlock(aBlock);
                didWork |= renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false, isTranslucentPass);
                didWork |= renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false, isTranslucentPass);
                didWork |= renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false, isTranslucentPass);
                didWork |= renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false, isTranslucentPass);
                didWork |= renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false, isTranslucentPass);
                didWork |= renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false, isTranslucentPass);
                break;
            case CONNECTED_EAST | CONNECTED_WEST:
                // EAST - WEST Pipe Sides
                aBlock.setBlockBounds(blockMin, pipeMin, pipeMin, blockMax, pipeMax, pipeMax);
                aRenderer.setRenderBoundsFromBlock(aBlock);
                didWork |= renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false, isTranslucentPass);
                didWork |= renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false, isTranslucentPass);
                didWork |= renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false, isTranslucentPass);
                didWork |= renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false, isTranslucentPass);

                // EAST - WEST Pipe Ends
                didWork |= renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false, isTranslucentPass);
                didWork |= renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false, isTranslucentPass);
                break;
            case CONNECTED_DOWN | CONNECTED_UP:
                // UP - DOWN Pipe Sides
                aBlock.setBlockBounds(pipeMin, blockMin, pipeMin, pipeMax, blockMax, pipeMax);
                aRenderer.setRenderBoundsFromBlock(aBlock);
                didWork |= renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false, isTranslucentPass);
                didWork |= renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false, isTranslucentPass);
                didWork |= renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false, isTranslucentPass);
                didWork |= renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false, isTranslucentPass);

                // UP - DOWN Pipe Ends
                didWork |= renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false, isTranslucentPass);
                didWork |= renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false, isTranslucentPass);
                break;
            case CONNECTED_NORTH | CONNECTED_SOUTH:
                // NORTH - SOUTH Pipe Sides
                aBlock.setBlockBounds(pipeMin, pipeMin, blockMin, pipeMax, pipeMax, blockMax);
                aRenderer.setRenderBoundsFromBlock(aBlock);
                didWork |= renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false, isTranslucentPass);
                didWork |= renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false, isTranslucentPass);
                didWork |= renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false, isTranslucentPass);
                didWork |= renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false, isTranslucentPass);

                // NORTH - SOUTH Pipe Ends
                didWork |= renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false, isTranslucentPass);
                didWork |= renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false, isTranslucentPass);
                break;
            default:
                if ((aConnections & CONNECTED_WEST) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(blockMin, pipeMin, pipeMin, pipeMin, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    didWork |= renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false, isTranslucentPass);
                    didWork |= renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false, isTranslucentPass);
                    didWork |= renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false, isTranslucentPass);
                    didWork |= renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false, isTranslucentPass);
                }
                didWork |= renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false, isTranslucentPass);

                if ((aConnections & CONNECTED_EAST) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMax, pipeMin, pipeMin, blockMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    didWork |= renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false, isTranslucentPass);
                    didWork |= renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false, isTranslucentPass);
                    didWork |= renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false, isTranslucentPass);
                    didWork |= renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false, isTranslucentPass);
                }
                didWork |= renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false, isTranslucentPass);

                if ((aConnections & CONNECTED_DOWN) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMin, blockMin, pipeMin, pipeMax, pipeMin, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    didWork |= renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false, isTranslucentPass);
                    didWork |= renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false, isTranslucentPass);
                    didWork |= renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false, isTranslucentPass);
                    didWork |= renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false, isTranslucentPass);
                }
                didWork |= renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false, isTranslucentPass);

                if ((aConnections & CONNECTED_UP) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMin, pipeMax, pipeMin, pipeMax, blockMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    didWork |= renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false, isTranslucentPass);
                    didWork |= renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false, isTranslucentPass);
                    didWork |= renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false, isTranslucentPass);
                    didWork |= renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false, isTranslucentPass);
                }
                didWork |= renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false, isTranslucentPass);

                if ((aConnections & CONNECTED_NORTH) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMin, pipeMin, blockMin, pipeMax, pipeMax, pipeMin);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    didWork |= renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false, isTranslucentPass);
                    didWork |= renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false, isTranslucentPass);
                    didWork |= renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false, isTranslucentPass);
                    didWork |= renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false, isTranslucentPass);
                }
                didWork |= renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false, isTranslucentPass);

                if ((aConnections & CONNECTED_SOUTH) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMax, pipeMax, pipeMax, blockMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    didWork |= renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false, isTranslucentPass);
                    didWork |= renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false, isTranslucentPass);
                    didWork |= renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false, isTranslucentPass);
                    didWork |= renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false, isTranslucentPass);
                }
                didWork |= renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false, isTranslucentPass);
                break;
        }

        // Render covers on pipes
        if (tIsCovered[DOWN.ordinal()]) {
            aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, coverInnerMin, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[NORTH.ordinal()]) {
                didWork |= renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false, isTranslucentPass);
            }
            if (!tIsCovered[SOUTH.ordinal()]) {
                didWork |= renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false, isTranslucentPass);
            }
            if (!tIsCovered[WEST.ordinal()]) {
                didWork |= renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false, isTranslucentPass);
            }
            if (!tIsCovered[EAST.ordinal()]) {
                didWork |= renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false, isTranslucentPass);
            }
            didWork |= renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false, isTranslucentPass);
            if ((aConnections & CONNECTED_DOWN) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMin, blockMin, blockMax, blockMin, pipeMin);
                didWork |= renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false, isTranslucentPass);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, blockMin, pipeMax, blockMax, blockMin, blockMax);
                didWork |= renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false, isTranslucentPass);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, blockMin, pipeMin, pipeMin, blockMin, pipeMax);
                didWork |= renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false, isTranslucentPass);
                // Middle right panel
                aRenderer.setRenderBounds(pipeMax, blockMin, pipeMin, blockMax, blockMin, pipeMax);
            }
            didWork |= renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false, isTranslucentPass);
        }

        if (tIsCovered[UP.ordinal()]) {
            aBlock.setBlockBounds(blockMin, coverInnerMax, blockMin, blockMax, blockMax, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[NORTH.ordinal()]) {
                didWork |= renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false, isTranslucentPass);
            }
            if (!tIsCovered[SOUTH.ordinal()]) {
                didWork |= renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false, isTranslucentPass);
            }
            if (!tIsCovered[WEST.ordinal()]) {
                didWork |= renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false, isTranslucentPass);
            }
            if (!tIsCovered[EAST.ordinal()]) {
                didWork |= renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false, isTranslucentPass);
            }
            didWork |= renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false, isTranslucentPass);
            if ((aConnections & CONNECTED_UP) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMax, blockMin, blockMax, blockMax, pipeMin);
                didWork |= renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false, isTranslucentPass);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, blockMax, pipeMax, blockMax, blockMax, blockMax);
                didWork |= renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false, isTranslucentPass);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, blockMax, pipeMin, pipeMin, blockMax, pipeMax);
                didWork |= renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false, isTranslucentPass);
                // Middle right panel
                aRenderer.setRenderBounds(pipeMax, blockMax, pipeMin, blockMax, blockMax, pipeMax);
            }
            didWork |= renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false, isTranslucentPass);
        }

        if (tIsCovered[NORTH.ordinal()]) {
            aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, coverInnerMin);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[DOWN.ordinal()]) {
                didWork |= renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false, isTranslucentPass);
            }
            if (!tIsCovered[UP.ordinal()]) {
                didWork |= renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false, isTranslucentPass);
            }
            if (!tIsCovered[WEST.ordinal()]) {
                didWork |= renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false, isTranslucentPass);
            }
            if (!tIsCovered[EAST.ordinal()]) {
                didWork |= renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false, isTranslucentPass);
            }
            didWork |= renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false, isTranslucentPass);
            if ((aConnections & CONNECTED_NORTH) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMin, blockMin, blockMax, pipeMin, blockMin);
                didWork |= renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false, isTranslucentPass);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, pipeMax, blockMin, blockMax, blockMax, blockMin);
                didWork |= renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false, isTranslucentPass);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, pipeMin, blockMin, pipeMin, pipeMax, blockMin);
                didWork |= renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false, isTranslucentPass);
                // Middle right panel
                aRenderer.setRenderBounds(pipeMax, pipeMin, blockMin, blockMax, pipeMax, blockMin);
            }
            didWork |= renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false, isTranslucentPass);
        }

        if (tIsCovered[SOUTH.ordinal()]) {
            aBlock.setBlockBounds(blockMin, blockMin, coverInnerMax, blockMax, blockMax, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[DOWN.ordinal()]) {
                didWork |= renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false, isTranslucentPass);
            }
            if (!tIsCovered[UP.ordinal()]) {
                didWork |= renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false, isTranslucentPass);
            }
            if (!tIsCovered[WEST.ordinal()]) {
                didWork |= renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false, isTranslucentPass);
            }
            if (!tIsCovered[EAST.ordinal()]) {
                didWork |= renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false, isTranslucentPass);
            }
            didWork |= renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false, isTranslucentPass);
            if ((aConnections & CONNECTED_SOUTH) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMin, blockMax, blockMax, pipeMin, blockMax);
                didWork |= renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false, isTranslucentPass);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, pipeMax, blockMax, blockMax, blockMax, blockMax);
                didWork |= renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false, isTranslucentPass);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, pipeMin, blockMax, pipeMin, pipeMax, blockMax);
                didWork |= renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false, isTranslucentPass);
                // Middle right panel
                aRenderer.setRenderBounds(pipeMax, pipeMin, blockMax, blockMax, pipeMax, blockMax);
            }
            didWork |= renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false, isTranslucentPass);
        }

        if (tIsCovered[WEST.ordinal()]) {
            aBlock.setBlockBounds(blockMin, blockMin, blockMin, coverInnerMin, blockMax, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[DOWN.ordinal()]) {
                didWork |= renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false, isTranslucentPass);
            }
            if (!tIsCovered[UP.ordinal()]) {
                didWork |= renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false, isTranslucentPass);
            }
            if (!tIsCovered[NORTH.ordinal()]) {
                didWork |= renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false, isTranslucentPass);
            }
            if (!tIsCovered[SOUTH.ordinal()]) {
                didWork |= renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false, isTranslucentPass);
            }
            didWork |= renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false, isTranslucentPass);
            if ((aConnections & CONNECTED_WEST) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMin, blockMin, blockMin, pipeMin, blockMax);
                didWork |= renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false, isTranslucentPass);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, pipeMax, blockMin, blockMin, blockMax, blockMax);
                didWork |= renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false, isTranslucentPass);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, pipeMin, blockMin, blockMin, pipeMax, pipeMin);
                didWork |= renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false, isTranslucentPass);
                // Middle right panel
                aRenderer.setRenderBounds(blockMin, pipeMin, pipeMax, blockMin, pipeMax, blockMax);
            }
            didWork |= renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false, isTranslucentPass);
        }

        if (tIsCovered[EAST.ordinal()]) {
            aBlock.setBlockBounds(coverInnerMax, blockMin, blockMin, blockMax, blockMax, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[DOWN.ordinal()]) {
                didWork |= renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false, isTranslucentPass);
            }
            if (!tIsCovered[UP.ordinal()]) {
                didWork |= renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false, isTranslucentPass);
            }
            if (!tIsCovered[NORTH.ordinal()]) {
                didWork |= renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false, isTranslucentPass);
            }
            if (!tIsCovered[SOUTH.ordinal()]) {
                didWork |= renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false, isTranslucentPass);
            }
            didWork |= renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false, isTranslucentPass);

            if ((aConnections & CONNECTED_EAST) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMax, blockMin, blockMin, blockMax, pipeMin, blockMax);
                didWork |= renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false, isTranslucentPass);
                // Upper panel
                aRenderer.setRenderBounds(blockMax, pipeMax, blockMin, blockMax, blockMax, blockMax);
                didWork |= renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false, isTranslucentPass);
                // Middle left panel
                aRenderer.setRenderBounds(blockMax, pipeMin, blockMin, blockMax, pipeMax, pipeMin);
                didWork |= renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false, isTranslucentPass);
                // Middle right panel
                aRenderer.setRenderBounds(blockMax, pipeMin, pipeMax, blockMax, pipeMax, blockMax);
            }
            didWork |= renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false, isTranslucentPass);
        }
        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        aRenderer.setRenderBoundsFromBlock(aBlock);

        return didWork;
    }

    private static void renderNormalInventoryMetaTileEntity(Block aBlock, int aMeta, RenderBlocks aRenderer, boolean isTranslucentPass) {
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

        if ((iGregTechTileEntity instanceof IPipeRenderedTileEntity)) {
            final float tThickness = ((IPipeRenderedTileEntity) iGregTechTileEntity).getThickNess();
            final float pipeMin = (blockMax - tThickness) / 2.0F;
            final float pipeMax = blockMax - pipeMin;

            aBlock.setBlockBounds(blockMin, pipeMin, pipeMin, blockMax, pipeMax, pipeMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            renderNegativeYFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) DOWN.ordinal(), (byte) (CONNECTED_WEST | CONNECTED_EAST), (byte) -1, false, false), true, isTranslucentPass);
            renderPositiveYFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) UP.ordinal(), (byte) (CONNECTED_WEST | CONNECTED_EAST), (byte) -1, false, false), true, isTranslucentPass);
            renderNegativeZFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) NORTH.ordinal(), (byte) (CONNECTED_WEST | CONNECTED_EAST), (byte) -1, false, false), true, isTranslucentPass);
            renderPositiveZFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) SOUTH.ordinal(), (byte) (CONNECTED_WEST | CONNECTED_EAST), (byte) -1, false, false), true, isTranslucentPass);
            renderNegativeXFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) WEST.ordinal(), (byte) (CONNECTED_WEST | CONNECTED_EAST), (byte) -1, true, false), true, isTranslucentPass);
            renderPositiveXFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) EAST.ordinal(), (byte) (CONNECTED_WEST | CONNECTED_EAST), (byte) -1, true, false), true, isTranslucentPass);
        } else {
            renderNegativeYFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) DOWN.ordinal(), (byte) WEST.ordinal(), (byte) -1, true, false), true, isTranslucentPass);
            renderPositiveYFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) UP.ordinal(), (byte) WEST.ordinal(), (byte) -1, true, false), true, isTranslucentPass);
            renderNegativeZFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) NORTH.ordinal(), (byte) WEST.ordinal(), (byte) -1, true, false), true, isTranslucentPass);
            renderPositiveZFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) SOUTH.ordinal(), (byte) WEST.ordinal(), (byte) -1, true, false), true, isTranslucentPass);
            renderNegativeXFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) WEST.ordinal(), (byte) WEST.ordinal(), (byte) -1, true, false), true, isTranslucentPass);
            renderPositiveXFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) EAST.ordinal(), (byte) WEST.ordinal(), (byte) -1, true, false), true, isTranslucentPass);
        }
    }

    public static boolean renderFacing(IBlockAccess world,
                                       RenderBlocks render,
                                       Block block,
                                       int posX,
                                       int posY,
                                       int posZ,
                                       ITexture[] textures,
                                       boolean isFullBlock,
                                       byte side,
                                       boolean isTranslucentPass) {
        return switch (side) {
            case 0 ->
                    renderNegativeYFacing(world, render, block, posX, posY, posZ, textures, isFullBlock, isTranslucentPass);
            case 1 ->
                    renderPositiveYFacing(world, render, block, posX, posY, posZ, textures, isFullBlock, isTranslucentPass);
            case 2 ->
                    renderNegativeZFacing(world, render, block, posX, posY, posZ, textures, isFullBlock, isTranslucentPass);
            case 3 ->
                    renderPositiveZFacing(world, render, block, posX, posY, posZ, textures, isFullBlock, isTranslucentPass);
            case 4 ->
                    renderNegativeXFacing(world, render, block, posX, posY, posZ, textures, isFullBlock, isTranslucentPass);
            case 5 ->
                    renderPositiveXFacing(world, render, block, posX, posY, posZ, textures, isFullBlock, isTranslucentPass);
            default -> false;
        };
    }

    public static boolean renderAllFaces(IBlockAccess world,
                                         RenderBlocks renderBlocks,
                                         Block block,
                                         int posX,
                                         int posY,
                                         int posZ,
                                         ITexture[] textures,
                                         boolean isFullBlock,
                                         boolean isTranslucentPass) {
        var didWork = false;
        didWork |= renderNegativeYFacing(world, renderBlocks, block, posX, posY, posZ, textures, isFullBlock, isTranslucentPass);
        didWork |= renderPositiveYFacing(world, renderBlocks, block, posX, posY, posZ, textures, isFullBlock, isTranslucentPass);
        didWork |= renderNegativeZFacing(world, renderBlocks, block, posX, posY, posZ, textures, isFullBlock, isTranslucentPass);
        didWork |= renderPositiveZFacing(world, renderBlocks, block, posX, posY, posZ, textures, isFullBlock, isTranslucentPass);
        didWork |= renderNegativeXFacing(world, renderBlocks, block, posX, posY, posZ, textures, isFullBlock, isTranslucentPass);
        didWork |= renderPositiveXFacing(world, renderBlocks, block, posX, posY, posZ, textures, isFullBlock, isTranslucentPass);
        return didWork;
    }

    public static boolean renderNegativeYFacing(IBlockAccess world,
                                                RenderBlocks render,
                                                Block block,
                                                int posX,
                                                int posY,
                                                int posZ,
                                                ITexture[] textures,
                                                boolean isFullBlock,
                                                boolean isTranslucentPass) {
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
                didWork |= texture.renderYNeg(render, block, posX, posY, posZ, isTranslucentPass);
        return didWork;
    }

    public static boolean renderPositiveYFacing(IBlockAccess world,
                                                RenderBlocks render,
                                                Block block,
                                                int posX,
                                                int posY,
                                                int posZ,
                                                ITexture[] textures,
                                                boolean isFullBlock,
                                                boolean isTranslucentPass) {
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
                didWork |= texture.renderYPos(render, block, posX, posY, posZ, isTranslucentPass);
        return didWork;
    }

    public static boolean renderNegativeZFacing(IBlockAccess world,
                                                RenderBlocks render,
                                                Block block,
                                                int posX,
                                                int posY,
                                                int posZ,
                                                ITexture[] textures,
                                                boolean isFullBlock,
                                                boolean isTranslucentPass) {
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
                didWork |= texture.renderZNeg(render, block, posX, posY, posZ, isTranslucentPass);
        return didWork;
    }

    public static boolean renderPositiveZFacing(IBlockAccess world,
                                                RenderBlocks render,
                                                Block block,
                                                int posX,
                                                int posY,
                                                int posZ,
                                                ITexture[] textures,
                                                boolean isFullBlock,
                                                boolean isTranslucentPass) {
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
                didWork |= texture.renderZPos(render, block, posX, posY, posZ, isTranslucentPass);
        return didWork;
    }

    public static boolean renderNegativeXFacing(IBlockAccess world,
                                                RenderBlocks render,
                                                Block block,
                                                int posX,
                                                int posY,
                                                int posZ,
                                                ITexture[] textures,
                                                boolean isFullBlock,
                                                boolean isTranslucentPass) {
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
                didWork |= texture.renderXNeg(render, block, posX, posY, posZ, isTranslucentPass);
        return didWork;
    }

    public static boolean renderPositiveXFacing(IBlockAccess world,
                                                RenderBlocks render,
                                                Block block,
                                                int posX,
                                                int posY,
                                                int posZ,
                                                ITexture[] textures,
                                                boolean isFullBlock,
                                                boolean isTranslucentPass) {
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
                didWork |= texture.renderXPos(render, block, posX, posY, posZ, isTranslucentPass);
        return didWork;
    }
}
