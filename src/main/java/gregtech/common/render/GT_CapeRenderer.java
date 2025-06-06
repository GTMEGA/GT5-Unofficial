package gregtech.common.render;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import lombok.val;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.lwjgl.opengl.GL11;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GT_CapeRenderer extends RenderPlayer {
    private final ResourceLocation[] mCapes = {
            new ResourceLocation("gregtech:textures/BrainTechCape.png"),
            new ResourceLocation("gregtech:textures/MrBrainCape.png"),
            new ResourceLocation("gregtech:textures/GregoriusCape.png"),
            new ResourceLocation("gregtech:textures/GregTechCape.png"),
            new ResourceLocation("gregtech:textures/DonorCape.png"),
            new ResourceLocation("gregtech:textures/DevCape.png"),
            new ResourceLocation("gregtech:textures/TecCape.png"),
            new ResourceLocation("gregtech:textures/MEGACAPE.png")};
    private final Collection<String> mCapeList;
    private final Map<String, ResourceLocation> nameCapeMap = new HashMap<>();

    public GT_CapeRenderer(Collection<String> aCapeList) {
        this.mCapeList = aCapeList;
        setRenderManager(RenderManager.instance);
    }

    public void receiveRenderSpecialsEvent(RenderPlayerEvent.Specials.Pre aEvent) {
        AbstractClientPlayer aPlayer = (AbstractClientPlayer) aEvent.entityPlayer;
        if (GT_Utility.getFullInvisibility(aPlayer)) {
            aEvent.setCanceled(true);
            return;
        }
        if (aPlayer.getHideCape())
            return;
        if (aPlayer.isInvisible())
            return;
        if (GT_Utility.getPotion(aPlayer, Potion.invisibility.id))
            return;

        val tResource = getCapeLocation(aPlayer);
        if (tResource == null)
            return;

        try {
            float aPartialTicks = aEvent.partialRenderTick;

            bindTexture(tResource);
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double d0 = aPlayer.field_71091_bM + (aPlayer.field_71094_bP - aPlayer.field_71091_bM) * aPartialTicks - (aPlayer.prevPosX + (aPlayer.posX - aPlayer.prevPosX) * aPartialTicks);
            double d1 = aPlayer.field_71096_bN + (aPlayer.field_71095_bQ - aPlayer.field_71096_bN) * aPartialTicks - (aPlayer.prevPosY + (aPlayer.posY - aPlayer.prevPosY) * aPartialTicks);
            double d2 = aPlayer.field_71097_bO + (aPlayer.field_71085_bR - aPlayer.field_71097_bO) * aPartialTicks - (aPlayer.prevPosZ + (aPlayer.posZ - aPlayer.prevPosZ) * aPartialTicks);
            float f6 = aPlayer.prevRenderYawOffset + (aPlayer.renderYawOffset - aPlayer.prevRenderYawOffset) * aPartialTicks;
            double d3 = MathHelper.sin(f6 * 3.141593F / 180.0F);
            double d4 = -MathHelper.cos(f6 * 3.141593F / 180.0F);
            float f7 = (float) d1 * 10.0F;
            float f8 = (float) (d0 * d3 + d2 * d4) * 100.0F;
            float f9 = (float) (d0 * d4 - d2 * d3) * 100.0F;
            if (f7 < -6.0F) {
                f7 = -6.0F;
            }
            if (f7 > 32.0F) {
                f7 = 32.0F;
            }
            if (f8 < 0.0F) {
                f8 = 0.0F;
            }
            float f10 = aPlayer.prevCameraYaw + (aPlayer.cameraYaw - aPlayer.prevCameraYaw) * aPartialTicks;
            f7 += MathHelper.sin((aPlayer.prevDistanceWalkedModified + (aPlayer.distanceWalkedModified - aPlayer.prevDistanceWalkedModified) * aPartialTicks) * 6.0F) * 32.0F * f10;
            if (aPlayer.isSneaking()) {
                f7 += 25.0F;
            }
            GL11.glRotatef(6.0F + f8 / 2.0F + f7, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f9 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f9 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            ((ModelBiped) this.mainModel).renderCloak(0.0625F);
            GL11.glPopMatrix();
        } catch (Throwable e) {
            if (GT_Values.D1) {
                e.printStackTrace(GT_Log.err);
            }
        }
    }

    private ResourceLocation getCapeLocation(AbstractClientPlayer aPlayer) {
        val playerName = aPlayer.getDisplayName();
        if (playerName == null || playerName.isEmpty())
            return null;

        if (nameCapeMap.containsKey(playerName))
            return nameCapeMap.get(playerName);

        var tResource = findCapeByName(playerName);
        if (tResource == null)
            tResource = aPlayer.getLocationCape();

        nameCapeMap.put(playerName, tResource);
        return tResource;
    }

    private ResourceLocation findCapeByName(String playerName) {
        playerName = playerName.toLowerCase();

        if ("friedi4321".equals(playerName))
            return mCapes[0];
        if ("mr_brain".equals(playerName))
            return mCapes[1];
        if ("gregoriust".equals(playerName))
            return mCapes[2];

        if (mCapeList.contains(playerName))
            return mCapes[3];
        if (mCapeList.contains(playerName + ":capedonor"))
            return mCapes[4];
        if (mCapeList.contains(playerName + ":capedev"))
            return mCapes[5];
        if (mCapeList.contains(playerName + ":capetec"))
            return mCapes[6];
        if (mCapeList.contains(playerName + ":capemega"))
            return mCapes[7];
        return null;
    }
}
