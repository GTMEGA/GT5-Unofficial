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


public class GT_GUIContainer_DevItemSource extends GT_RichGuiContainer_Machine {

    public static int COOLDOWN = 30;

    private static void boxOnUpdate(final GT_GuiIntegerTextBox perTickBox, final String string) {
        perTickBox.setText(string);
    }

    public GT_GUIContainer_DevItemSource(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
        super(new GT_Container_DevItemSource(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/DevItemSource.png", 256, 166);
        addGuiElements();
    }

    public int buttonX() {
        return getGuiWidth() - 40;
    }

    public String getIPTString() {
        final int pT = getSource().getData().getItemPerTick(), pS = getSource().getData().getItemPerSecond();
        if (getSource().getData().isPerTick()) {
            return String.valueOf(pT);
        } else {
            if (pS == 0) {
                return "0";
            }
            if (pS % 20 == 0) {
                return String.valueOf(pS / 20);
            }
            return String.valueOf((float) pS / 20);
        }
    }

    public String getIPSString() {
        if (!getSource().getData().isPerTick()) {
            return String.valueOf(getSource().getData().getItemPerSecond());
        } else {
            return String.valueOf(getSource().getData().getItemPerTick() * 20);
        }
    }

    /**
     * @return
     */
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
     * Given textbox's value might have changed.
     *
     * @param box
     */
    @Override
    public void applyTextBox(final GT_GuiIntegerTextBox box) {
        val s = box.getText().trim();
        int value;
        if (s.contains(".")) {
            double v;
            try {
                v = Double.parseDouble(s);
            } catch (NumberFormatException ignored) {
                resetTextBox(box);
                return;
            }
            value = (int) v;
        } else {
            try {
                value = Integer.parseInt(s);
            } catch (NumberFormatException ignored) {
                resetTextBox(box);
                return;
            }
        }
        if (value < 0) {
            resetTextBox(box);
            return;
        }
        switch (box.id) {
            case 3: {
                getSource().setItemPerTick(value);
                getSource().setPerTick(true);
                break;
            }
            case 4: {
                getSource().setItemPerSecond(value);
                getSource().setPerTick(false);
                break;
            }
        }
        getSource().syncRates();
        box.setUpdateCooldown(20);
        box.setText(String.valueOf(value));
        sendUpdateToServer();
    }

    public GT_Container_DevItemSource getSource() {
        return (GT_Container_DevItemSource) mContainer;
    }

    /**
     *
     */
    @Override
    public void sendUpdateToServer() {
        getSource().sendPacket();
    }

    /**
     * @param mouseX
     * @param mouseY
     * @param parTicks
     */
    @Override
    public void drawExtras(final int mouseX, final int mouseY, final float parTicks) {
        val inactiveColor = new Color(0x30, 0x30, 0x4F, 0xFF);
        val activeColor = new Color(0x30, 0x30, 0xFF, 0xFF);
        val errorColor = new Color(0xFF, 0x55, 0x55, 0xFF);
        val left = boxX() + 3 + boxWidth();
        val pT = getSource().getData().isPerTick();
        drawString("Items / tick", left, 24, pT ? activeColor : inactiveColor);
        drawString("Items / second", left, 34, pT ? inactiveColor : activeColor);
        /*if (!getSource().canRun()) {
            drawString(getSource().getDisabledStatus(), left, 44, errorColor);
        }*/
    }

    private int boxX() {
        return getGuiWidth() / 2 - boxWidth();
    }

    private int boxWidth() {
        return GregTech_API.mDWS ? 96 : 64;
    }

    /**
     * @return Whether to draw slots
     */
    @Override
    protected boolean autoDrawSlots() {
        return true;
    }

    /**
     * @return
     */
    @Override
    public boolean hasDWSAlternativeBackground() {
        return true;
    }

    private void addGuiElements() {
        val source = getSource();
        //
        val check = new GT_GuiIconCheckButton(this, 0, buttonX(), 24, GT_GuiIcon.CHECKMARK, GT_GuiIcon.CROSS, "Enable Activity", "Disable Activity");
        check.setChecked(!getSource().getData().isActive());
        check.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> checkButtonOnUpdate(check));
        check.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> checkButtonOnClick(check, source));
        //
        val zero = new GT_GuiIconButton(this, 1, buttonX(), 42, GT_GuiIcon.MATH_ZERO);
        zero.setTooltipText("Set output rate to zero");
        zero.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> zeroOnClick(source));
        //
        addRedstoneButton();
        addTextBoxes();
    }

    private void zeroOnClick(final GT_Container_DevItemSource source) {
        source.zeroOut();
        sendUpdateToServer();
    }

    private void checkButtonOnClick(final GT_GuiIconCheckButton check, final GT_Container_DevItemSource source) {
        check.setChecked(source.toggleActive());
        check.setUpdateCooldown(COOLDOWN);
        updateButton(check, source.getData().isActive());
        sendUpdateToServer();
    }

    private void checkButtonOnUpdate(final GT_GuiIconCheckButton check) {
        val source = getSource();
        check.setChecked(source.getData().isActive());
        updateButton(check, source.getData().isActive());
    }

    private void addRedstoneButton() {
        val source = getSource();
        val rsIconTooltipPairs = new GT_GuiCycleButton.IconToolTipPair[]{
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.CROSS, "Ignores redstone"),
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.REDSTONE_OFF, "Requires low signal"),
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.REDSTONE_ON, "Requires signal"),
                };
        val rsButton = new GT_GuiCycleButton(this, 2, buttonX(), 60, 0, rsIconTooltipPairs);
        rsButton.setState(source.getData().getRedstoneMode().ordinal());
        rsButton.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> rsButtonOnUpdate(rsButton));
        rsButton.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> rsButtonOnClick(rsButton));
        rsButton.setDoCycle(false);
    }

    private void addTextBoxes() {
        val perTickBox = new GT_GuiIntegerTextBox(this, 3, boxX(), 24, boxWidth(), 10);
        perTickBox.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> boxOnUpdate(perTickBox, getIPTString()));
        perTickBox.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> perTickBox.setUpdateCooldown(COOLDOWN));
        //
        val perSecondBox = new GT_GuiIntegerTextBox(this, 4, boxX(), 34, boxWidth(), 10);
        perSecondBox.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> boxOnUpdate(perSecondBox, getIPSString()));
        perSecondBox.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> perSecondBox.setUpdateCooldown(COOLDOWN));
    }

    private void rsButtonOnUpdate(final GT_GuiCycleButton rsButton) {
        val source = getSource();
        rsButton.setState(source.getData().getRedstoneMode().ordinal());
        updateButton(rsButton, source.getData().isRsActive());
    }

    private void updateButton(final GT_GuiIconButton button, final boolean error) {
        val highlightIcon = error ? GT_GuiIcon.BUTTON_HIGHLIGHT : GT_GuiIcon.BUTTON_ERROR_HIGHLIGHT;
        val normalIcon = error ? GT_GuiIcon.BUTTON_NORMAL : GT_GuiIcon.BUTTON_ERROR_NORMAL;
        button.setBackgroundIconHighlight(highlightIcon);
        button.setBackgroundIconNormal(normalIcon);
    }

    private void rsButtonOnClick(final GT_GuiCycleButton rsButton) {
        rsButton.cycle();
        getSource().setRedstoneMode(RSControlMode.getMode(rsButton.getState()));
        updateButton(rsButton, getSource().getData().isRsActive());
        rsButton.setUpdateCooldown(COOLDOWN);
        sendUpdateToServer();
    }

}
