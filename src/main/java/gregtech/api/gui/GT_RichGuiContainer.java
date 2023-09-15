package gregtech.api.gui;


import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.gui.widgets.*;
import gregtech.api.gui.widgets.slider.GT_GuiSlider_Horizontal;
import gregtech.api.interfaces.IDWSCompatibleGUI;
import gregtech.api.interfaces.IGuiScreen;
import gregtech.api.util.interop.NEIInterop;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public abstract class GT_RichGuiContainer extends GT_GUIContainer implements GT_GuiTooltipManager.GT_IToolTipRenderer, IGuiScreen, IDWSCompatibleGUI {

    protected static final boolean debug = false;

    protected final List<IGuiElement> elements = new ArrayList<>();

    protected final List<GT_GuiIntegerTextBox> textBoxes = new ArrayList<>();

    protected final List<GT_GuiSlider_Horizontal> sliders = new ArrayList<>();

    protected final List<IGT_GuiSubWindow> subWindows = new ArrayList<>();

    protected final RenderItem itemRenderer = new RenderItem();

    protected final GT_GuiTooltipManager tooltipManager = new GT_GuiTooltipManager();

    @Getter
    private final int guiWidth;

    @Getter
    private final int guiHeight;

    private final ResourceLocation dwsGuiBackground;

    private final int baseGuiWidth;

    protected GuiButton selectedButton = null;

    public GT_RichGuiContainer(final Container aContainer, final String aGuiBackground, final int width, final int height) {
        super(aContainer, aGuiBackground);
        this.dwsGuiBackground = new ResourceLocation(getDWSGuiBackgroundPath(aGuiBackground));
        this.baseGuiWidth = width;
        this.guiWidth = this.xSize = applyDWSBump(baseWidth());
        this.guiHeight = this.ySize = height;
    }

    public int getDWSWidthBump() {
        return 0;
    }

    /**
     * @return
     */
    @Override
    public int baseWidth() {
        return baseGuiWidth;
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
        return guiHeight;
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
        return guiWidth;
    }

    /**
     * @return Font renderer
     */
    @Override
    public FontRenderer getFontRenderer() {
        return fontRendererObj;
    }

    @SuppressWarnings("rawtypes")
    public void drawHoveringText(final List text, final int x, final int y) {
        this.drawHoveringText(text, x, y, this.fontRendererObj);
    }

    /**
     * @param par1List List of tooltips
     * @param par2     x?
     * @param par3     y?
     * @param font     font
     */
    @Override
    public void drawHoveringText(final List par1List, final int par2, final int par3, final FontRenderer font) {
        super.drawHoveringText(par1List, par2, par3, font);
    }

    /**
     * Causes the screen to lay out its subcomponents again. This is the equivalent of the Java call
     * Container.validate()
     *
     * @param minecraft
     * @param width
     * @param height
     */
    @Override
    public void setWorldAndResolution(final Minecraft minecraft, final int width, final int height) {
        super.setWorldAndResolution(minecraft, width, height);
        for (IGuiScreen subWindow : subWindows) {
            ((GuiScreen) subWindow).setWorldAndResolution(minecraft, width, height);
        }
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
            if (element instanceof GT_GuiSlider_Horizontal) {
                sliders.add((GT_GuiSlider_Horizontal) element);
            }
            if (element instanceof IGT_GuiSubWindow) {
                subWindows.add((IGT_GuiSubWindow) element);
            }
        }
    }

    protected void onInitGui(final int guiLeft, final int guiTop, final int width, final int height) {

    }

    /**
     * Called when the mouse is clicked.
     *
     * @param rawMX
     * @param rawMY
     * @param clickType
     */
    @Override
    protected void mouseClicked(final int rawMX, final int rawMY, final int clickType) {
        super.mouseClicked(rawMX, rawMY, clickType);
        final int mouseX = getMouseX(rawMX);
        final int mouseY = getMouseY(rawMY);
        sliders.stream().filter(element -> element.inBounds(mouseX, mouseY, clickType)).forEach(slider -> slider.onMousePressed(mouseX, mouseY, clickType));
        textBoxes.stream().filter(element -> element.inBounds(mouseX, mouseY, clickType)).forEach(this::setFocusedTextBox);
        subWindows.stream().filter(element -> element.inBounds(mouseX, mouseY, clickType)).forEach(element -> element.receiveClick(mouseX, mouseY, clickType));
    }

    /**
     * @param stack
     * @param x
     * @param y
     * @param displayString
     */
    @Override
    public void drawItemStack(final ItemStack stack, final int x, final int y, final String displayString) {
        super.drawItemStack(stack, x, y, displayString);
    }

    /**
     * @param mouseX Raw mouse X
     * @return mouseX within Gui
     */
    public int getMouseX(final int mouseX) {
        return mouseX - guiLeft;
    }

    /**
     * @param mouseY Raw mouse Y
     * @return mouseY within Gui
     */
    public int getMouseY(final int mouseY) {
        return mouseY - guiTop;
    }

    /**
     * TextBoxes
     */
    protected void setFocusedTextBox(GT_GuiIntegerTextBox boxToFocus) {
        for (GT_GuiIntegerTextBox textBox : textBoxes) {
            textBox.setFocused(textBox.equals(boxToFocus) && textBox.isEnabled());
        }
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     *
     * @param mouseX
     * @param mouseY
     * @param clickState
     */
    @Override
    protected void mouseMovedOrUp(final int mouseX, final int mouseY, final int clickState) {
        super.mouseMovedOrUp(mouseX, mouseY, clickState);
        sliders.forEach(slider -> slider.onMouseReleased(mouseX, mouseY, clickState));
    }

    /**
     * @param slot
     * @param slotIndex
     * @param controlDown
     * @param mouseButton
     */
    @Override
    protected void handleMouseClick(final Slot slot, int slotIndex, final int controlDown, final int mouseButton) {
        super.handleMouseClick(slot, slotIndex, controlDown, mouseButton);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     *
     * @param c
     * @param key
     */
    @Override
    protected void keyTyped(final char c, final int key) {
        GT_GuiIntegerTextBox focusedTextBox = null;
        for (GT_GuiIntegerTextBox textBox : textBoxes) {
            if (textBox.isFocused()) {
                focusedTextBox = textBox;
            }
        }

        if (key == 1) { //esc
            if (focusedTextBox != null) {
                resetTextBox(focusedTextBox);
                setFocusedTextBox(null);
                return;
            } else {
                closeScreen();
            }
        }

        if (c == '\t') { //tab
            for (int i = 0; i < textBoxes.size(); i++) {
                GT_GuiIntegerTextBox box = textBoxes.get(i);
                if (box.isFocused()) {
                    applyTextBox(box);
                    setFocusedTextBox(((i + 1) < textBoxes.size()) ? textBoxes.get(i + 1) : null);
                    return;
                }
            }
            if (!textBoxes.isEmpty()) {
                setFocusedTextBox(textBoxes.get(0));
            }
            return;
        }

        if (focusedTextBox != null && focusedTextBox.textboxKeyTyped(c, key)) {
            return;
        }

        if (key == 28 && focusedTextBox != null) { // enter
            applyTextBox(focusedTextBox);
            setFocusedTextBox(null);
            return;
        }

        if (key == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            if (focusedTextBox != null) {
                applyTextBox(focusedTextBox);
                setFocusedTextBox(null);
                return;
            }
            closeScreen();
            return;
        }
        super.keyTyped(c, key);
    }

    /**
     * Reset the given textbox to the last valid value, <b>NOT</b> 0.
     */
    public void resetTextBox(GT_GuiIntegerTextBox box) {

    }

    public void closeScreen() {
        this.mc.displayGuiScreen(null);
        this.mc.setIngameFocus();
    }

    /**
     * Given textbox's value might have changed.
     */
    public void applyTextBox(GT_GuiIntegerTextBox box) {

    }

    public boolean isDWSEnabled() {
        return GregTech_API.mDWS;
    }

    public int getDimension() {
        return this.mc.thePlayer.dimension;
    }

    public int drawString(final String text, final int x, final int y, final Color color) {
        return drawString(text, x, y, colorToARGB(color));
    }

    private int drawString(final String text, final int x, final int y, final int color) {
        getFontRenderer().drawString(text, x, y, color);
        return getFontRenderer().getStringWidth(text);
    }

    public static int colorToARGB(final Color color) {
        return color.getAlpha() << 24 | color.getRed() << 16 | color.getGreen() << 8 | color.getBlue();
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     *
     * @param x
     * @param y
     * @param u
     * @param v
     * @param width
     * @param height
     */
    @Override
    public void drawTexturedModalRect(
            final int x, final int y, final int u, final int v, final int width, final int height
                                     ) {
        super.drawTexturedModalRect(x, y, u, v, width, height);
    }

    protected void uncheckButtons() {
        GuiButton button;
        for (Object o : buttonList) {
            button = (GuiButton) o;
            button.enabled = true;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {

    }

    /**
     * @param mouseX   X of mouse
     * @param mouseY   Y of mouse
     * @param parTicks ?
     */
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float parTicks) {
        preDrawHook(mouseX, mouseY, parTicks);

        neiPreDraw();
        drawDefaultBackground();
        drawBackground();

        magicGLPre();
        drawElements(mouseX, mouseY, parTicks);
        magicGLMid1();
        drawSlots(mouseX, mouseY);
        drawForegroundLayer(mouseX, mouseY, parTicks);
        neiRenderObjects(mouseX, mouseY);
        magicGLMid1dot5();
        drawItems(mouseX, mouseY);
        magicGLMid2();
        renderTooltips(mouseX, mouseY);
        magicGLPost();

        postDrawHook(mouseX, mouseY, parTicks);
    }

    /**
     * Do not use this for GL calls, this is for handling per-frame logic
     * */
    protected void postDrawHook(final int mouseX, final int mouseY, final float parTicks) {

    }

    /**
     * Do not use this for GL calls, this is for handling per-frame logic
     * */
    protected void preDrawHook(final int mouseX, final int mouseY, final float parTicks) {

    }

    /**
     * NEI injects code into GUIContainer to draw itself, and in overriding the drawScreen method, we lose out on that.
     * Thus, this is my solution.
     */
    private void neiPreDraw() {
        if (isNEIEnabled() && isNEILoaded()) {
            NEIInterop.INSTANCE.preDraw(this);
        }
    }

    public void drawBackground() {
        short[] color = Dyes.MACHINE_METAL.getRGBA();
        GL11.glColor3ub((byte) color[0], (byte) color[1], (byte) color[2]);
        final ResourceLocation bg = getGUIBackground();
        this.mc.renderEngine.bindTexture(bg);
        final int x, y;
        x = (width - xSize) / 2;
        y = (height - ySize) / 2;
        drawBigTexturedModalRect(x, y, 0, 0, guiWidth, guiHeight, 512, 512);
    }


    /**
     * Allows you to draw textures bigger than 256x256
     *
     * @param x
     * @param y
     * @param u
     * @param v
     * @param width
     * @param height
     * @param texMaxWidth
     * @param textMaxHeight
     */
    public void drawBigTexturedModalRect(
            final int x, final int y, final int u, final int v, final int width, final int height, final int texMaxWidth, final int textMaxHeight
                                        ) {
        func_146110_a(x, y, u, v, width, height, texMaxWidth, textMaxHeight);
    }

    /**
     * @return
     */
    @Override
    public @NonNull ResourceLocation getDWSGuiBackground() {
        return dwsGuiBackground;
    }

    /**
     * @return
     */
    @Override
    public ResourceLocation getGUIBackground() {
        return GregTech_API.mDWS && hasDWSAlternativeBackground() ? getDWSGuiBackground() : super.getGUIBackground();
    }

    protected void magicGLPre() {
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

    protected void magicGLMid1() {
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(guiLeft, guiTop, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    public void drawSlots(final int mouseX, final int mouseY) {
        for (int i = 0; i < this.inventorySlots.inventorySlots.size(); i++) {
            handleDrawSlot((Slot) inventorySlots.inventorySlots.get(i), mouseX, mouseY);
        }
    }

    public void drawForegroundLayer(final int mouseX, final int mouseY, final float parTicks) {
        drawExtras(mouseX, mouseY, parTicks);
        tooltipManager.onTick(this, mouseX, mouseY);
    }

    protected void neiRenderObjects(final int mouseX, final int mouseY) {
        if (isNEIEnabled() && isNEILoaded()) {
            NEIInterop.INSTANCE.renderObjects(this, mouseX, mouseY);
        }
    }

    protected void magicGLMid1dot5() {
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    protected void drawItems(final int mouseX, final int mouseY) {
        final InventoryPlayer player = this.mc.thePlayer.inventory;
        final ItemStack inDragged = draggedStack;
        ItemStack dragged = inDragged == null ? player.getItemStack() : inDragged;
        if (dragged != null) {
            final int dragOffset = inDragged == null ? 8 : 16;
            String s = null;
            if (inDragged != null && isRightMouseClick) {
                dragged = dragged.copy();
                dragged.stackSize = MathHelper.ceiling_float_int((float) dragged.stackSize / 2.0F);
            } else if (this.field_147007_t && this.field_147008_s.size() > 1) {
                dragged = dragged.copy();
                dragged.stackSize = getDragStackSize();
                if (dragged.stackSize == 0) {
                    s = EnumChatFormatting.YELLOW + "0";
                }
            }
            drawStack(dragged, mouseX - this.guiLeft - 8, mouseY - this.guiTop - dragOffset, s);
        }
    }

    private static void magicGLMid2() {
        GL11.glPopMatrix();
    }

    private void renderTooltips(final int mouseX, final int mouseY) {
        if (isNEIEnabled() && isNEILoaded()) {
            NEIInterop.INSTANCE.renderTooltips(this, mouseX, mouseY);
        } else {
            final InventoryPlayer player = this.mc.thePlayer.inventory;
            if (player.getItemStack() == null && theSlot != null && theSlot.getHasStack()) {
                final ItemStack inSlot = getTheSlot().getStack();
                renderToolTip(inSlot, mouseX, mouseY);
            }
        }
    }

    private void magicGLPost() {
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    /**
     * Says whether NEI should be shown in the GUI
     */
    protected boolean isNEIEnabled() {
        return true;
    }

    public final boolean isNEILoaded() {
        return GregTech_API.mNEI;
    }

    protected void handleDrawSlot(final Slot slot, final int mouseX, final int mouseY) {
        if (autoDrawSlots()) {
            drawSlot(slot);
        }
        partialDrawSlot(slot);
        if (isMouseOverSlot(slot, mouseX, mouseY) && isSlotEnabled(slot, mouseX, mouseY)) {
            theSlot = slot;
            renderSlotHighlight(slot);
        }
    }

    protected void partialDrawSlot(final Slot slot) {
        func_146977_a(slot);
    }

    public abstract void drawExtras(final int mouseX, final int mouseY, final float parTicks);


    public int getDragStackSize() {
        return field_146996_I;
    }

    protected void drawStack(final ItemStack stack, final int x, final int y, final String s) {
        drawItemStack(stack, x, y, s);
    }

    public Slot getTheSlot() {
        return theSlot;
    }

    /**
     * @return whether to automatically generate slots
     */
    protected boolean autoDrawSlots() {
        return false;
    }

    protected void drawSlot(final Slot slot) {
        final int x, y;
        x = slot.xDisplayPosition;
        y = slot.yDisplayPosition;
        GL11.glColorMask(true, true, true, false);
        val backgroundGradientStart = colorToARGB(new Color(0x00, 0x00, 0x00, 0x3F));
        val backgroundGradientEnd = colorToARGB(new Color(0x00, 0x00, 0x00, 0x0F));
        drawGradientRect(x - 1, y - 1, x + 17, y + 17, backgroundGradientStart, backgroundGradientEnd);
        val foregroundGradientStart = colorToARGB(new Color(0x0F, 0x0F, 0xFF, 0xAF));
        val foregroundGradientEnd = colorToARGB(new Color(0x00, 0x00, 0x40, 0x1F));
        drawGradientRect(x, y, x + 16, y + 16, foregroundGradientStart, foregroundGradientEnd);
        GL11.glColorMask(true, true, true, true);
    }

    protected boolean isSlotEnabled(final Slot slot, final int mouseX, final int mouseY) {
        if (isNEIEnabled() && isNEILoaded()) {
            return NEIInterop.INSTANCE.checkSlotUnderMouse(this, mouseX, mouseY) || slot.func_111238_b();
        } else {
            return slot.func_111238_b();
        }
    }

    protected void renderSlotHighlight(final Slot slot) {
        final int x = slot.xDisplayPosition;
        final int y = slot.yDisplayPosition;
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glColorMask(true, true, true, false);
        val highlightGradientStart = colorToARGB(new Color(0xFF, 0xFF, 0xFF, 0x80));
        val highlightGradientEnd = colorToARGB(new Color(0xAF, 0xAF, 0xFF, 0x80));
        drawGradientRect(x, y, x + 16, y + 16, highlightGradientStart, highlightGradientEnd);
        GL11.glColorMask(true, true, true, true);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }

}
