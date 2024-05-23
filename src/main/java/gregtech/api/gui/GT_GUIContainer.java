package gregtech.api.gui;

import gregtech.common.GT_Compat;
import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;


/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * Main GUI-Container-Class which basically contains the Code needed to prevent crashes from improperly Coded Items.
 */
public class GT_GUIContainer extends GuiContainer {

    public boolean mCrashed = false;

    public ResourceLocation mGUIbackground;

    public String mGUIbackgroundPath;

    public GT_GUIContainer(Container aContainer, String aGUIbackground) {
        super(aContainer);
        mGUIbackground = new ResourceLocation(mGUIbackgroundPath = aGUIbackground);
    }

    public int getLeft() {
        return guiLeft;
    }

    public int getTop() {
        return guiTop;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        //
    }

    public ResourceLocation getGUIBackground() {
        return mGUIbackground;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        mc.renderEngine.bindTexture(getGUIBackground());
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        try {
            super.drawScreen(par1, par2, par3);
        } catch (Throwable e) {
            try {
                GT_Compat.tessellator().draw();
            } catch (Throwable f) {
                //
            }
        }
    }

    @Override
    public void handleMouseInput() {
        int delta = Mouse.getEventDWheel();
        if (delta != 0) {
            int i = Mouse.getEventX() * this.width / this.mc.displayWidth;
            int j = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
            onMouseWheel(i, j, delta);
        }
        super.handleMouseInput();
    }

    protected void onMouseWheel(int mx, int my, int delta) {
    }


    /*
    @Override
    protected void drawSlotInventory(Slot par1Slot) {
        try {
        	super.drawSlotInventory(par1Slot);
        } catch(Throwable e) {
            try {
            	Tessellator.instance.draw();
            } catch(Throwable f) {}
        	if (!mCrashed) {
        		GT_Log.out.println("Clientside Slot drawing Crash prevented. Seems one Itemstack causes Problems with negative Damage Values or the Wildcard Damage Value. This is absolutely NOT a Bug of the GregTech-Addon, so don't even think about reporting it to me, it's a Bug of the Mod, which belongs to the almost-crash-causing Item, so bug that Mods Author and not me! Did you hear it? NOT ME!!!");
        		e.printStackTrace();
            	mCrashed = true;
        	}
        }
    }*/
}
