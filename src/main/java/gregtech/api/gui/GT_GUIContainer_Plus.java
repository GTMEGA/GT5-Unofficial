package gregtech.api.gui;


import gregtech.api.enums.Dyes;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.gui.widgets.GT_GuiTooltip;
import gregtech.api.gui.widgets.GT_GuiTooltipManager;
import gregtech.api.interfaces.IGuiScreen;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.List;


public abstract class GT_GUIContainer_Plus extends GT_GUIContainer implements GT_GuiTooltipManager.GT_IToolTipRenderer,
                                                                              IGuiScreen {


    protected final List<IGuiElement> elements = new ArrayList<>();

    protected final List<GT_GuiIntegerTextBox> textBoxes = new ArrayList<>();

    protected final RenderItem itemRenderer = new RenderItem();

    protected final GT_GuiTooltipManager tooltipManager = new GT_GuiTooltipManager();

    protected GuiButton selectedButton = null;


    public GT_GUIContainer_Plus(final Container aContainer, final String aGUIbackground) {
        super(aContainer, aGUIbackground);
    }

    /**
     * @param toolTip Tooltip to add
     */
    @Override
    public void addToolTip(final GT_GuiTooltip toolTip) {
        tooltipManager.addToolTip(toolTip);
    }

    /**
     * @param toolTip Tooltip to remove
     * @return Sucess or failure
     */
    @Override
    public boolean removeToolTip(final GT_GuiTooltip toolTip) {
        return tooltipManager.removeToolTip(toolTip);
    }

    /**
     * @return Button selected
     */
    @Override
    public GuiButton getSelectedButton() {
        return selectedButton;
    }

    /**
     *
     */
    @Override
    public void clearSelectedButton() {
        selectedButton = null;
    }

    /**
     * @param button Handler for a button click
     */
    @Override
    public void buttonClicked(final GuiButton button) {
        selectedButton = button;
    }

    /**
     * @return Get the height
     */
    @Override
    public int getYSize() {
        return height;
    }

    /**
     * @param element Add element to the GUI
     */
    @Override
    public void addElement(final IGuiElement element) {
        elements.add(element);
    }

    /**
     * @param element Remove the element
     * @return Success and failure
     */
    @Override
    public boolean removeElement(final IGuiElement element) {
        return elements.remove(element);
    }

    /**
     * @return Renderer
     */
    @Override
    public RenderItem getItemRenderer() {
        return itemRenderer;
    }

    /**
     * @return Get left
     */
    @Override
    public int getGuiLeft() {
        return guiLeft;
    }

    /**
     * @return Get top
     */
    @Override
    public int getGuiTop() {
        return guiTop;
    }

    /**
     * @return Get the width
     */
    @Override
    public int getXSize() {
        return width;
    }

    /**
     * @return Font renderer
     */
    @Override
    public FontRenderer getFontRenderer() {
        return fontRendererObj;
    }

    /**
     * @param par1List List of tooltips
     * @param par2     x?
     * @param par3     y?
     * @param font     font
     */
    @Override
    public void drawHoveringText(final List par1List, final int par2, final int par3, final FontRenderer font) {
        super.drawHoveringText(par1List, par2, par3, fontRendererObj);
    }

    @Override
    public void initGui() {
        super.initGui();
        addElements();
        onInitGui(guiLeft, guiTop, width, height);
        for (IGuiElement element : elements) {
            element.onInit();
        }
    }

    @SuppressWarnings("unchecked")
    private void addElements() {
        for (IGuiElement element : elements) {
            if (element instanceof GuiButton) {
                buttonList.add(element);
            }
            if (element instanceof GT_GuiIntegerTextBox) {
                textBoxes.add((GT_GuiIntegerTextBox) element);
            }
        }
    }

    protected void onInitGui(final int guiLeft, final int guiTop, final int width, final int height) {

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

    /**
     * @param mouseX   X of mouse
     * @param mouseY   Y of mouse
     * @param parTicks ?
     */
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float parTicks) {
        drawDefaultBackground();
        drawBackground();

        magicGLPre();
        drawElements(mouseX, mouseY, parTicks);
        magicGLPost();
    }

    private void magicGLPost() {
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        GL11.glPushMatrix();
        GL11.glTranslatef(guiLeft, guiTop, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    private static void magicGLPre() {
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
    }

    private void drawElements(final int mouseX, final int mouseY, final float parTicks) {
        for (IGuiElement e : elements) {
            e.draw(mouseX, mouseY, parTicks);
        }
    }

    public void drawBackground() {
        short[] color = Dyes.MACHINE_METAL.getRGBA();
        GL11.glColor3ub((byte) color[0], (byte) color[1], (byte) color[2]);
        this.mc.renderEngine.bindTexture(new ResourceLocation(mGUIbackgroundPath));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, width, height);
    }

}
