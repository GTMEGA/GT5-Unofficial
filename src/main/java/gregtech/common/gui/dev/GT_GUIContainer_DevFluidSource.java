package gregtech.common.gui.dev;


import gregtech.api.GregTech_API;
import gregtech.api.enums.RSControlMode;
import gregtech.api.gui.GT_RichGuiContainer_Machine;
import gregtech.api.gui.widgets.*;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import lombok.val;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;

import java.awt.*;


public class GT_GUIContainer_DevFluidSource extends GT_RichGuiContainer_Machine {

    public GT_GUIContainer_DevFluidSource(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
        super(new GT_Container_DevFluidSource(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/DevFluidSource.png", 256, 166);
        addGUIElements();
    }

    private void addGUIElements() {
        val source = getSource();
        //
        val check = new GT_GuiIconCheckButton(this, 0, buttonX(), 24, GT_GuiIcon.CHECKMARK, GT_GuiIcon.CROSS, "Enable Activity", "Disable Activity");
        check.setChecked(!source.getData().isActive());
        check.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> check.setChecked(source.getData().isActive()));
        check.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> {
            check.setChecked(source.toggleActive());
            button.setUpdateCooldown(20);
            sendUpdateToServer();
        });
        //
        val zero = new GT_GuiIconButton(this, 1, buttonX(), 42, GT_GuiIcon.MATH_ZERO);
        zero.setTooltipText("Set output rate to zero");
        zero.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> {
            source.zeroOut();
            sendUpdateToServer();
        });
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

    @Override
    public void sendUpdateToServer() {
        getSource().sendPacket();
    }

    private void addRedstoneButton() {
        val source = getSource();
        final GT_GuiCycleButton.IconToolTipPair[] redstoneToolTipPairs = {
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.CROSS, "Ignores redstone"),
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.REDSTONE_OFF, "Requires low signal"),
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.REDSTONE_ON, "Requires signal"),
                };
        val rsButton = new GT_GuiCycleButton(this, 2, buttonX(), 60, 0, redstoneToolTipPairs);
        rsButton.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> rsButton.setState(source.getData().getMode().ordinal()));
        rsButton.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> {
            rsButton.cycle();
            source.setMode(RSControlMode.getMode(((GT_GuiCycleButton) button).getState()));
            button.setUpdateCooldown(20);
            sendUpdateToServer();
        });
        rsButton.setDoCycle(false);
    }

    private void addTextBoxes() {
        val gui = this;
        //
        val perTickBox = new GT_GuiIntegerTextBox(this, 3, boxX(), 24, boxWidth(), 10);
        perTickBox.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> {
            if (!perTickBox.isFocused()) {
                perTickBox.setText(gui.getIPTString());
            }
        }).setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> button.setUpdateCooldown(20));
        //
        val perSecondBox = new GT_GuiIntegerTextBox(this, 4, boxX(), 34, boxWidth(), 10);
        perSecondBox.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> {
            if (!perSecondBox.isFocused()) {
                perSecondBox.setText(gui.getIPSString());
            }
        }).setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> button.setUpdateCooldown(20));
    }

    private int boxX() {
        return getGuiWidth() / 2 - boxWidth();
    }

    private int boxWidth() {
        return GregTech_API.mDWS ? 96 : 64;
    }

    public String getIPTString() {
        final int pT = getSource().getData().getRPT(), pS = getSource().getData().getRPS();
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
            return String.valueOf(getSource().getData().getRPS());
        } else {
            return String.valueOf(getSource().getData().getRPS() * 20);
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
     * Given textbox's value might have changed.
     *
     * @param box
     */
    @Override
    public void applyTextBox(final GT_GuiIntegerTextBox box) {
        String s = box.getText().trim();
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
                getSource().setRPT(value);
                getSource().setPerTick(true);
                break;
            }
            case 4: {
                getSource().setRPS(value);
                getSource().setPerTick(false);
                break;
            }
        }
        getSource().syncRates();
        box.setUpdateCooldown(20);
        box.setText(String.valueOf(value));
        sendUpdateToServer();
    }

    @Override
    public void drawExtras(final int mouseX, final int mouseY, final float parTicks) {
        val inactiveColor = new Color(0x30, 0x30, 0x4F, 0xFF);
        val activeColor = new Color(0x30, 0x30, 0xFF, 0xFF);
        val errorColor = new Color(0xFF, 0x55, 0x55, 0xFF);
        val left = boxX() + 3 + boxWidth();
        val isPerTick = getSource().getData().isPerTick();
        drawString("/ tick", left, 24, isPerTick ? activeColor : inactiveColor);
        drawString("/ second", left, 34, isPerTick ? inactiveColor : activeColor);
        if (!getSource().canRun()) {
            drawString(getSource().getDisabledStatus(), left, 44, errorColor);
        }
    }

    /**
     * @return whether to automatically generate slots
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

}
