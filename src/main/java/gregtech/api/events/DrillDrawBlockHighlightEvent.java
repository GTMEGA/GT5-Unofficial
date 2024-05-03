package gregtech.api.events;

import com.falsepattern.falsetweaks.Compat;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gregtech.common.tools.GT_Tool_Drill_LV;
import lombok.val;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@SideOnly(Side.CLIENT)
public class DrillDrawBlockHighlightEvent {
    @SubscribeEvent
    public void onBlockDrawHighlight(DrawBlockHighlightEvent event) {
        if (event.currentItem == null) {
            return;
        }

        val heldItem = event.currentItem.getItem();

        if (!(heldItem instanceof GT_MetaGenerated_Tool_01)) {
            return;
        }

        val heldTool = (GT_MetaGenerated_Tool_01) heldItem;
        val heldToolType = heldTool.getToolStats(event.currentItem);

        if (!(heldToolType instanceof GT_Tool_Drill_LV)) {
            return;
        }

        val aoe = ((GT_Tool_Drill_LV) heldToolType).getAOE(GT_MetaGenerated_Tool.getStatNbt(event.currentItem));

        if (event.target.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return;
        }

        if (event.player == null) {
            return;
        }

        val list = this.getBlocksToBreak(event.target.sideHit,
                                         GT_Tool_Drill_LV.xStart[aoe], GT_Tool_Drill_LV.yStart[aoe],
                                         GT_Tool_Drill_LV.xLen[aoe], GT_Tool_Drill_LV.yLen[aoe],
                                         event.target.blockX, event.target.blockY, event.target.blockZ, event.player);

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        val player = event.player;
        val partialTick = event.partialTicks;

        val offsetX = (float) (player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTick);
        val offsetY = (float) (player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTick);
        val offsetZ = (float) (player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTick);

        GL11.glTranslatef(-offsetX, -offsetY, -offsetZ);
        GL11.glLineWidth(2F);
        GL11.glColor4f(1F, 1F, 1F, 1F);

        val color = Color.CYAN;
        val tessellator = Tessellator.instance;

        tessellator.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        tessellator.startDrawing(GL11.GL_LINES);

        for (val position : list) {
            val minX = position.chunkPosX - 0.01F;
            val minY = position.chunkPosY - 0.01F;
            val minZ = position.chunkPosZ - 0.01F;

            val maxX = minX + 1 + 0.01F;
            val maxY = minY + 1 + 0.01F;
            val maxZ = minZ + 1 + 0.01F;

            tessellator.addVertex(maxX, minY, maxZ); tessellator.addVertex(maxX, maxY, maxZ);
            tessellator.addVertex(maxX, minY, minZ); tessellator.addVertex(maxX, minY, maxZ);
            tessellator.addVertex(maxX, minY, minZ); tessellator.addVertex(maxX, maxY, minZ);
            tessellator.addVertex(maxX, maxY, minZ); tessellator.addVertex(maxX, maxY, maxZ);

            tessellator.addVertex(minX, minY, minZ); tessellator.addVertex(minX, maxY, minZ);
            tessellator.addVertex(minX, minY, maxZ); tessellator.addVertex(minX, maxY, maxZ);
            tessellator.addVertex(minX, minY, minZ); tessellator.addVertex(minX, minY, maxZ);
            tessellator.addVertex(minX, maxY, minZ); tessellator.addVertex(minX, maxY, maxZ);

            tessellator.addVertex(minX, maxY, minZ); tessellator.addVertex(maxX, maxY, minZ);
            tessellator.addVertex(minX, maxY, minZ); tessellator.addVertex(minX, maxY, maxZ);
            tessellator.addVertex(minX, maxY, maxZ); tessellator.addVertex(maxX, maxY, maxZ);
            tessellator.addVertex(maxX, maxY, minZ); tessellator.addVertex(maxX, maxY, maxZ);

            tessellator.addVertex(minX, minY, minZ); tessellator.addVertex(maxX, minY, minZ);
            tessellator.addVertex(minX, minY, minZ); tessellator.addVertex(minX, minY, maxZ);
            tessellator.addVertex(minX, minY, maxZ); tessellator.addVertex(maxX, minY, maxZ);
            tessellator.addVertex(maxX, minY, minZ); tessellator.addVertex(maxX, minY, maxZ);

            tessellator.addVertex(minX, minY, maxZ); tessellator.addVertex(maxX, minY, maxZ);
            tessellator.addVertex(minX, maxY, maxZ); tessellator.addVertex(maxX, maxY, maxZ);
            tessellator.addVertex(minX, minY, maxZ); tessellator.addVertex(minX, maxY, maxZ);
            tessellator.addVertex(maxX, minY, maxZ); tessellator.addVertex(maxX, maxY, maxZ);

            tessellator.addVertex(minX, minY, minZ); tessellator.addVertex(maxX, minY, minZ);
            tessellator.addVertex(minX, maxY, minZ); tessellator.addVertex(maxX, maxY, minZ);
            tessellator.addVertex(minX, minY, minZ); tessellator.addVertex(minX, maxY, minZ);
            tessellator.addVertex(maxX, minY, minZ); tessellator.addVertex(maxX, maxY, minZ);
        }

        tessellator.draw();

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    // copy pasted from IAOETool.breakBlockAround
    private List<ChunkPosition> getBlocksToBreak(int side, int xStartPos, int yStartPos, int xLength, int yLength, int x, int y, int z, EntityPlayer player) {
        val list = new ArrayList<ChunkPosition>();

        int DX;
        int DY;
        int DZ;
        boolean downUp = side < 2;
        boolean northSouth = side < 4;
        float rotation = player.rotationYawHead < 0 ? 360f + player.rotationYawHead : player.rotationYawHead;

        BiFunction<Integer, Integer, Boolean> xCheck = (value, len) -> value < len;
        BiFunction<Integer, Integer, Boolean> yCheck = (value, len) -> value > len;

        int xIterate = 1;
        int yIterate = -1;

        if (downUp) {
            boolean north = rotation > 135 && rotation < 225;// 180
            //boolean east = rotation >= 225 && rotation <= 315;//270
            boolean west = rotation >= 45 && rotation <= 135;// 90
            boolean south = rotation < 45 || rotation > 315;//0

            //x = north-south
            //y = east-west

            if (south) {
                int tempX = xLength;
                xLength = yLength;
                yLength = tempX;
                //invert Y direction
                yLength *= -1;
                yLength -= yStartPos;
                yStartPos *= -1;
                //invert X direction when looking down
                if (side == 0) {
                    xLength += xStartPos;
                } else {
                    xCheck = (value, len) -> value > len;
                    xLength *= -1;
                    xLength -= xStartPos;
                    xIterate = -1;
                    xStartPos *= -1;
                }
            } else if (west) {
                //invert X direction
                xCheck = (value, len) -> value > len;
                xLength *= -1;
                xLength -= xStartPos;
                xIterate = -1;
                xStartPos *= -1;
                //invert Y direction when looking up
                if (side == 0) {
                    yLength *= -1;
                    yLength -= yStartPos;
                    yStartPos *= -1;
                } else {
                    yCheck = (value, len) -> value < len;
                    yIterate = 1;
                    yLength += yStartPos;
                }
            } else if (north) {
                int tempX = xLength;
                xLength = yLength;
                yLength = tempX;
                //dont invert Y direction
                yCheck = (value, len) -> value < len;
                yIterate = 1;
                yLength += yStartPos;

                //invert X direction when looking UP
                if (side == 0) {
                    xCheck = (value, len) -> value > len;
                    xLength *= -1;
                    xLength -= xStartPos;
                    xIterate = -1;
                    xStartPos *= -1;
                } else {
                    xLength += xStartPos;
                }
            } else {
                //dont invert X direction
                xLength += xStartPos;

                //invert Y direction when looking DOWN
                if (side == 0) {
                    yCheck = (value, len) -> value < len;
                    yIterate = 1;
                    yLength += yStartPos;
                } else {
                    yLength *= -1;
                    yLength -= yStartPos;
                    yStartPos *= -1;
                }
            }
        } else {
            yLength *= -1;
            yLength -= yStartPos;
            yStartPos *= -1;
            if (northSouth) {
                if (side == 2) {
                    xLength *= -1;
                    xLength -= xStartPos;
                    xIterate = -1;
                    xStartPos *= -1;
                    xCheck = (value, len) -> value > len;
                } else {
                    xLength += xStartPos;
                }
            } else {
                if (side == 5) {
                    xLength *= -1;
                    xLength -= xStartPos;
                    xIterate = -1;
                    xStartPos *= -1;
                    xCheck = (value, len) -> value > len;
                } else {
                    xLength += xStartPos;
                }
            }
        }

        for (int Y = yStartPos; yCheck.apply(Y, yLength); Y += yIterate) {
            for (int X = xStartPos; xCheck.apply(X, xLength); X += xIterate) {
                if (Y != 0 || X != 0) {
                    if (downUp) {
                        //im to lazy to fix this
                        DX = x + Y;
                        DY = y;
                        DZ = z + X;
                    } else if (northSouth) {
                        DX = x + X;
                        DY = y + Y;
                        DZ = z;
                    } else {
                        DX = x;
                        DY = y + Y;
                        DZ = z + X;
                    }

                    list.add(new ChunkPosition(DX, DY, DZ));
                }
            }
        }

        return list;
    }
}
