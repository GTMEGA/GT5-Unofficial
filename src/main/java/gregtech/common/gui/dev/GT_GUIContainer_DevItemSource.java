package gregtech.common.gui.dev;


import gregtech.api.GregTech_API;
import gregtech.api.enums.RSControlMode;
import gregtech.api.gui.GT_GUIContainer_Machine_Plus;
import gregtech.api.gui.widgets.*;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import lombok.val;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;

import java.awt.*;


public class GT_GUIContainer_DevItemSource extends GT_GUIContainer_Machine_Plus {

    public GT_GUIContainer_DevItemSource(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
        super(new GT_Container_DevItemSource(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/DevItemSource.png", 256, 166);
        addGuiElements();
    }

    private void addGuiElements() {
        new GT_GuiIconCheckButton(this, 0, buttonX(), 24, GT_GuiIcon.CHECKMARK, GT_GuiIcon.CROSS, "Enable Activity", "Disable Activity").setChecked(
                !getSource().getData().isActive()).setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> {
            if (screen instanceof GT_GUIContainer_DevItemSource && button instanceof GT_GuiIconCheckButton) {
                ((GT_GuiIconCheckButton) button).setChecked(((GT_GUIContainer_DevItemSource) screen).getSource().getData().isActive());
            }
        }).setOnClickHook((screen, button, mouseX, mouseY, clickType) -> button.setUpdateCooldown(20));
        new GT_GuiIconButton(this, 1, buttonX(), 42, GT_GuiIcon.MATH_ZERO).setTooltipText("Set output rate to zero");
        addRedstoneButton();
        addTextBoxes();
    }

    public int buttonX() {
        return getGuiWidth() - 40;
    }

    public GT_Container_DevItemSource getSource() {
        return (GT_Container_DevItemSource) mContainer;
    }

    private void addRedstoneButton() {
        final GT_GuiCycleButton rsButton = new GT_GuiCycleButton(this, 2, buttonX(), 60, 0, new GT_GuiCycleButton.IconToolTipPair[]{
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.CROSS, "Ignores redstone"),
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.REDSTONE_OFF, "Requires low signal"),
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.REDSTONE_ON, "Requires signal"),
                });
        rsButton.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> {
            if (screen instanceof GT_GUIContainer_DevItemSource && button instanceof GT_GuiCycleButton) {
                ((GT_GuiCycleButton) button).setState(((GT_GUIContainer_DevItemSource) screen).getSource().getData().getRedstoneMode().ordinal());
            }
        }).setOnClickHook((screen, button, mouseX, mouseY, clickType) -> {
            if (screen instanceof GT_GUIContainer_DevItemSource && button instanceof GT_GuiCycleButton) {
                ((GT_GuiCycleButton) button).cycle();
                ((GT_GUIContainer_DevItemSource) screen).getSource().setRedstoneMode(RSControlMode.getMode(((GT_GuiCycleButton) button).getState()));
                button.setUpdateCooldown(20);
                sendUpdateToServer();
            }
        });
        rsButton.setDoCycle(false);
    }

    private void addTextBoxes() {
        val perTickBox = new GT_GuiIntegerTextBox(this, 3, boxX(), 24, boxWidth(), 10);
        perTickBox.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> {
            if (screen instanceof GT_GUIContainer_DevItemSource && button instanceof GT_GuiIntegerTextBox && !((GT_GuiIntegerTextBox) button).isFocused()) {
                ((GT_GuiIntegerTextBox) button).setText(((GT_GUIContainer_DevItemSource) screen).getIPTString());
            }
        }).setOnClickHook((screen, button, mouseX, mouseY, clickType) -> button.setUpdateCooldown(20));
        val perSecondBox = new GT_GuiIntegerTextBox(this, 4, boxX(), 34, boxWidth(), 10);
        perSecondBox.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> {
            if (screen instanceof GT_GUIContainer_DevItemSource && button instanceof GT_GuiIntegerTextBox && !((GT_GuiIntegerTextBox) button).isFocused()) {
                ((GT_GuiIntegerTextBox) button).setText(((GT_GUIContainer_DevItemSource) screen).getIPSString());
            }
        }).setOnClickHook((screen, button, mouseX, mouseY, clickType) -> button.setUpdateCooldown(20));
    }

    /**
     *
     */
    @Override
    public void sendUpdateToServer() {
        getSource().sendPacket();
    }

    private int boxX() {
        return getGuiWidth() / 2 - boxWidth();
    }

    private int boxWidth() {
        return GregTech_API.mDWS ? 96 : 64;
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
     * @param button Handler for a button click
     */
    @Override
    public void buttonClicked(final GuiButton button) {
        super.buttonClicked(button);
        if (button.id == 0 && button instanceof GT_GuiIconCheckButton) {
            ((GT_GuiIconCheckButton) button).setChecked(getSource().toggleActive());
        } else if (button.id == 1) {
            getSource().zeroOut();
        } else if (button.id == 2 && button instanceof GT_GuiCycleButton) {
            getSource().setRedstoneMode(RSControlMode.getMode(((GT_GuiCycleButton) button).getState()));
        }
        sendUpdateToServer();
        updateButtons();
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
        if (!getSource().canRun()) {
            drawString(getSource().getDisabledStatus(), left, 44, errorColor);
        }
    }

    /**
     * @return Whether to draw slots
     */
    @Override
    protected boolean autoDrawSlots() {
        return true;
    }

    private void updateButtons() {
        for (Object o : buttonList) {
            ((GuiButton) o).enabled = true;
        }
    }

    /**
     * @return
     */
    @Override
    public boolean hasDWSAlternativeBackground() {
        return true;
    }

}
