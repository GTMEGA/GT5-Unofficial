package gregtech.common.gui.dev;


import gregtech.api.GregTech_API;
import gregtech.api.enums.RSControlMode;
import gregtech.api.gui.GT_RichGuiContainer_Machine;
import gregtech.api.gui.widgets.GT_GuiCycleButton;
import gregtech.api.gui.widgets.GT_GuiIconButton;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.gui.widgets.icon.GT_GuiIcon;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import lombok.val;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;

import java.awt.*;


public class GT_GUIContainer_DevFluidSource extends GT_RichGuiContainer_Machine {

    public static int COOLDOWN = 1;

    public GT_GUIContainer_DevFluidSource(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
        super(new GT_Container_DevFluidSource(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/DevFluidSource.png", 256, 166);
        addGUIElements();
    }

    private void addGUIElements() {
        val source = getSource();
        //
        val check = new GT_GuiIconCheckButton(this, 0, buttonX(), 24, GT_GuiIcon.CHECKMARK, GT_GuiIcon.CROSS, "Enable Activity", "Disable Activity");
        check.setChecked(!source.getData().isActive());
        check.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> checkOnUpdate(check));
        check.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> checkOnClick(check));
        //
        val zero = new GT_GuiIconButton(this, 1, buttonX(), 42, GT_GuiIcon.MATH_ZERO);
        zero.setTooltipText("Set output rate to zero");
        zero.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> zeroOnClick());
        //
        addRedstoneButton();
        addTextBoxes();
    }

    public GT_Container_DevFluidSource getSource() {
        return (GT_Container_DevFluidSource) mContainer;
    }

    private int buttonX() {
        return getGuiWidth() - 40;
    }

    private void checkOnUpdate(final GT_GuiIconCheckButton check) {
        val source = getSource();
        check.setChecked(source.getData().isActive());
        updateButtonBackground(check, source.getData().isActive());
    }

    private void checkOnClick(final GT_GuiIconCheckButton check) {
        check.setChecked(getSource().toggleActive());
        check.setUpdateCooldown(COOLDOWN);
        sendUpdateToServer();
    }

    private void zeroOnClick() {
        getSource().zeroOut();
        sendUpdateToServer();
    }

    private void addRedstoneButton() {
        val redstoneToolTipPairs = new GT_GuiCycleButton.IconToolTipPair[]{
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.CROSS, "Ignores redstone"),
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.REDSTONE_OFF, "Requires low signal"),
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.REDSTONE_ON, "Requires signal"),
                };
        val rsButton = new GT_GuiCycleButton(this, 2, buttonX(), 60, 0, redstoneToolTipPairs);
        rsButton.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> rsButtonOnUpdate(rsButton));
        rsButton.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> rsButtonOnClick(rsButton));
        rsButton.setDoCycle(false);
    }

    private void addTextBoxes() {
        val boxHeight  = 10;
        val firstBoxY  = 24;
        val secondBoxY = firstBoxY + boxHeight;
        val rateBox    = new GT_GuiIntegerTextBox(this, 3, boxX(), firstBoxY, boxWidth(), boxHeight);
        rateBox.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> boxOnUpdate(rateBox, String.valueOf(getSource().getData().getRate())));
        rateBox.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> rateBox.setUpdateCooldown(COOLDOWN));
        //
        val frequencyBox = new GT_GuiIntegerTextBox(this, 4, boxX(), secondBoxY, boxWidth(), boxHeight);
        frequencyBox.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> boxOnUpdate(frequencyBox, String.valueOf(getSource().getData().getFrequency())));
        frequencyBox.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> frequencyBox.setUpdateCooldown(COOLDOWN));
    }

    private void updateButtonBackground(final GT_GuiIconButton rsButton, final boolean error) {
        val highlightIcon = error ? GT_GuiIcon.BUTTON_HIGHLIGHT : GT_GuiIcon.BUTTON_ERROR_HIGHLIGHT;
        val normalIcon    = error ? GT_GuiIcon.BUTTON_NORMAL : GT_GuiIcon.BUTTON_ERROR_NORMAL;
        rsButton.setBackgroundIconHighlight(highlightIcon);
        rsButton.setBackgroundIconNormal(normalIcon);
    }

    @Override
    public void sendUpdateToServer() {
//        sendPacketToServer();
    }

    private void rsButtonOnUpdate(final GT_GuiCycleButton rsButton) {
        val source = getSource();
        rsButton.setState(source.getData().getMode().ordinal());
        updateButtonBackground(rsButton, source.getData().isRsActive());
    }

    private void rsButtonOnClick(final GT_GuiCycleButton rsButton) {
        val source = getSource();
        rsButton.cycle();
        source.setMode(RSControlMode.getMode(rsButton.getState()));
        updateButtonBackground(rsButton, source.getData().isRsActive());
        rsButton.setUpdateCooldown(COOLDOWN);
        sendUpdateToServer();
    }

    private int boxX() {
        return getGuiWidth() / 2 - boxWidth();
    }

    private int boxWidth() {
        return GregTech_API.mDWS ? 96 : 64;
    }

    private void boxOnUpdate(final GT_GuiIntegerTextBox box, final String text) {
        if (!box.isFocused()) {
            box.setText(text);
        }
    }

    @Override
    public int getDWSWidthBump() {
        return 82;
    }

    /**
     * @param button
     *         Handler for a button click
     */
    @Override
    public void buttonClicked(final GuiButton button) {
        super.buttonClicked(button);
        uncheckButtons();
        clearSelectedButton();
    }

    /**
     * @param mouseX
     * @param mouseY
     * @param lastClick
     * @param timeSinceLastClick
     */
    @Override
    protected void handleClickOutsideUI(final int mouseX, final int mouseY, final int lastClick, final long timeSinceLastClick) {
//        sendPacketToServer();
    }

    /**
     *
     */
    @Override
    public void onScreenClosed() {
        sendPacketToServer();
    }

    /**
     * Given textbox's value might have changed.
     *
     * @param box
     */
    @Override
    public void applyTextBox(final GT_GuiIntegerTextBox box) {
        val source = getSource();
        val text   = box.getText().trim();
        if (box.id == 3) {
            source.setRate(Integer.parseInt(text));
            box.setText(String.valueOf(source.getData().getRate()));
        } else if (box.id == 4) {
            source.setFrequency(Integer.parseInt(text));
            box.setText(String.valueOf(source.getData().getFrequency()));
        }
        sendUpdateToServer();
    }

    @Override
    public void drawExtras(final int mouseX, final int mouseY, final float parTicks) {
        val textColor = new Color(0, 0, 0, 255);
        val left      = boxX() + 3 + boxWidth();
        val top       = 24;
        drawString("Rate", left, top, textColor);
        drawString("Frequency (t)", left, top + 10, textColor);
        val rate      = getSource().getData().getRate();
        val frequency = (double) getSource().getData().getFrequency();
        val ratio     = rate / frequency;
        drawString(String.format("%.2f /t", ratio), left, top + 20, textColor);
        drawString(String.format("%.2f /s", ratio * 20), left, top + 30, textColor);
    }

    /**
     * @return whether to automatically generate slots
     */
    @Override
    protected boolean autoDrawSlots() {
        return true;
    }

    private void sendPacketToServer() {
        getSource().sendPacket();
    }

    /**
     * @return
     */
    @Override
    public boolean hasDWSAlternativeBackground() {
        return true;
    }

}
