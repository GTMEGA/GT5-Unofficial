package gregtech.api.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import com.falsepattern.falsetweaks.Compat;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gregtech.common.tools.GT_Tool_Drill_LV;
import lombok.val;
import lombok.var;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

@SideOnly(Side.CLIENT)
public class DrillDrawBlockHighlightEvent {
    private static final float LINE_WIDTH = 5F; // Same as MEGA default
    private static final float LINE_OFFSET = 0.002F; // Same as MEGA default

    private static final Color LINE_COLOR = new Color(26, 51, 230, 153); // Just here for the color picker
    private static final float LINE_COLOR_R = LINE_COLOR.getRed() / 255F;
    private static final float LINE_COLOR_G = LINE_COLOR.getGreen() / 255F;
    private static final float LINE_COLOR_B = LINE_COLOR.getBlue() / 255F;
    private static final float LINE_COLOR_A = LINE_COLOR.getAlpha() / 255F;

    @SubscribeEvent
    public void onBlockDrawHighlight(DrawBlockHighlightEvent event) {
        val player = event.player;
        if (player == null)
            return;

        val renderGlobal = event.context;
        if (renderGlobal == null)
            return;

        val rayHit = event.target;
        if (rayHit == null || rayHit.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK)
            return;

        val posX = rayHit.blockX;
        val posY = rayHit.blockY;
        val posZ = rayHit.blockZ;
        val side = rayHit.sideHit;

        val itemStack = event.currentItem;
        if (itemStack == null)
            return;
        val item = itemStack.getItem();
        if (item == null)
            return;

        if (!(item instanceof GT_MetaGenerated_Tool_01))
            return;
        val tool = (GT_MetaGenerated_Tool_01) item;
        val toolType = tool.getToolStats(event.currentItem);
        if (!(toolType instanceof GT_Tool_Drill_LV))
            return;

        val drillType = (GT_Tool_Drill_LV) toolType;
        val nbtTag = GT_MetaGenerated_Tool.getStatNbt(itemStack);
        if (nbtTag == null)
            return;
        val aoe = drillType.getAOE(nbtTag);

        val blockPositions = drillType.getBlocksInAOERange(side,
                                                           posX,
                                                           posY,
                                                           posZ,
                                                           player.worldObj,
                                                           GT_Tool_Drill_LV.xStart[aoe],
                                                           GT_Tool_Drill_LV.yStart[aoe],
                                                           GT_Tool_Drill_LV.xLen[aoe],
                                                           GT_Tool_Drill_LV.yLen[aoe],
                                                           player);

        val aabbList = new ArrayList<AxisAlignedBB>();

        for (val blockPosition : blockPositions) {
            val x = blockPosition.chunkPosX;
            val y = blockPosition.chunkPosY;
            val z = blockPosition.chunkPosZ;

            val block = player.worldObj.getBlock(x, y, z);

            if (block == null || block.isAir(player.worldObj, x, y, z)) {
                continue;
            }

            block.setBlockBoundsBasedOnState(player.worldObj, x, y, z);

            val aabb = block.getSelectedBoundingBoxFromPool(player.worldObj, x, y, z)
                            .expand(LINE_OFFSET, LINE_OFFSET, LINE_OFFSET);

            aabbList.add(aabb);
        }

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        {
            GL11.glLineWidth(LINE_WIDTH);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            // Basically this will look goofy-ah in dev, but it will be correct with MegaBlox 1.5.0+
            GL11.glColor4f(LINE_COLOR_R, LINE_COLOR_G, LINE_COLOR_B, LINE_COLOR_A);

            val partialTick = event.partialTicks;
            val offsetX = (float) (player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTick);
            val offsetY = (float) (player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTick);
            val offsetZ = (float) (player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTick);
            GL11.glTranslatef(-offsetX, -offsetY, -offsetZ);

            aabbList.forEach(aabb -> RenderGlobal.drawOutlinedBoundingBox(aabb, -1));
        }
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}
