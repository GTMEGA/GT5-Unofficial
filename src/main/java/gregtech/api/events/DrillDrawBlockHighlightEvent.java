package gregtech.api.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gregtech.common.tools.GT_Tool_Drill_LV;
import lombok.val;
import lombok.var;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
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

        val aabbList = getBlockBoundsInRange(side,
                                             GT_Tool_Drill_LV.xStart[aoe],
                                             GT_Tool_Drill_LV.yStart[aoe],
                                             GT_Tool_Drill_LV.xLen[aoe],
                                             GT_Tool_Drill_LV.yLen[aoe],
                                             posX,
                                             posY,
                                             posZ,
                                             player);

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

    // copy pasted from IAOETool.breakBlockAround
    //
    // edited to be cooling
    //
    private static List<AxisAlignedBB> getBlockBoundsInRange(int side,
                                                             int negWidth,
                                                             int negHeight,
                                                             int posWidth,
                                                             int posHeight,
                                                             int centerX,
                                                             int centerY,
                                                             int centerZ,
                                                             EntityPlayer player) {
        val world = player.worldObj;
        if (world == null)
            return Collections.emptyList();

        val aabbList = new ArrayList<AxisAlignedBB>();

        val downUp = side < 2;
        val northSouth = side < 4;
        val rotation = player.rotationYawHead < 0 ? 360F + player.rotationYawHead : player.rotationYawHead;

        BiFunction<Integer, Integer, Boolean> xCheck = (value, len) -> value < len;
        BiFunction<Integer, Integer, Boolean> yCheck = (value, len) -> value > len;

        var xIterate = 1;
        var yIterate = -1;

        if (downUp) {
            val north = rotation > 135 && rotation < 225;// 180
            //boolean east = rotation >= 225 && rotation <= 315;//270
            val west = rotation >= 45 && rotation <= 135;// 90
            val south = rotation < 45 || rotation > 315;//0

            //x = north-south
            //y = east-west

            if (south) {
                int tempX = posWidth;
                posWidth = posHeight;
                posHeight = tempX;
                //invert Y direction
                posHeight *= -1;
                posHeight -= negHeight;
                negHeight *= -1;
                //invert X direction when looking down
                if (side == 0) {
                    posWidth += negWidth;
                } else {
                    xCheck = (value, len) -> value > len;
                    posWidth *= -1;
                    posWidth -= negWidth;
                    xIterate = -1;
                    negWidth *= -1;
                }
            } else if (west) {
                //invert X direction
                xCheck = (value, len) -> value > len;
                posWidth *= -1;
                posWidth -= negWidth;
                xIterate = -1;
                negWidth *= -1;
                //invert Y direction when looking up
                if (side == 0) {
                    posHeight *= -1;
                    posHeight -= negHeight;
                    negHeight *= -1;
                } else {
                    yCheck = (value, len) -> value < len;
                    yIterate = 1;
                    posHeight += negHeight;
                }
            } else if (north) {
                int tempX = posWidth;
                posWidth = posHeight;
                posHeight = tempX;
                //dont invert Y direction
                yCheck = (value, len) -> value < len;
                yIterate = 1;
                posHeight += negHeight;

                //invert X direction when looking UP
                if (side == 0) {
                    xCheck = (value, len) -> value > len;
                    posWidth *= -1;
                    posWidth -= negWidth;
                    xIterate = -1;
                    negWidth *= -1;
                } else {
                    posWidth += negWidth;
                }
            } else {
                //dont invert X direction
                posWidth += negWidth;

                //invert Y direction when looking DOWN
                if (side == 0) {
                    yCheck = (value, len) -> value < len;
                    yIterate = 1;
                    posHeight += negHeight;
                } else {
                    posHeight *= -1;
                    posHeight -= negHeight;
                    negHeight *= -1;
                }
            }
        } else {
            posHeight *= -1;
            posHeight -= negHeight;
            negHeight *= -1;
            if (northSouth) {
                if (side == 2) {
                    posWidth *= -1;
                    posWidth -= negWidth;
                    xIterate = -1;
                    negWidth *= -1;
                    xCheck = (value, len) -> value > len;
                } else {
                    posWidth += negWidth;
                }
            } else {
                if (side == 5) {
                    posWidth *= -1;
                    posWidth -= negWidth;
                    xIterate = -1;
                    negWidth *= -1;
                    xCheck = (value, len) -> value > len;
                } else {
                    posWidth += negWidth;
                }
            }
        }

        for (var offsetY = negHeight; yCheck.apply(offsetY, posHeight); offsetY += yIterate) {
            for (var offsetX = negWidth; xCheck.apply(offsetX, posWidth); offsetX += xIterate) {
                if (offsetY == 0 && offsetX == 0)
                    continue;

                final int posX;
                final int posY;
                final int posZ;
                if (downUp) {
                    //im to lazy to fix this
                    posX = centerX + offsetY;
                    posY = centerY;
                    posZ = centerZ + offsetX;
                } else if (northSouth) {
                    posX = centerX + offsetX;
                    posY = centerY + offsetY;
                    posZ = centerZ;
                } else {
                    posX = centerX;
                    posY = centerY + offsetY;
                    posZ = centerZ + offsetX;
                }

                val block = world.getBlock(posX, posY, posZ);
                if (block == null || block.isAir(world, posX, posY, posZ))
                    continue;

                block.setBlockBoundsBasedOnState(world, posX, posY, posZ);
                val aabb = block.getSelectedBoundingBoxFromPool(world, posX, posY, posZ)
                                .expand(LINE_OFFSET, LINE_OFFSET, LINE_OFFSET);
                aabbList.add(aabb);
            }
        }

        return aabbList;
    }
}
