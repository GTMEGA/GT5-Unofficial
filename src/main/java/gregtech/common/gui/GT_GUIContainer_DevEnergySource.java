package gregtech.common.gui;


import gregtech.api.enums.RSControlMode;
import gregtech.api.gui.GT_GUIContainer_Machine_Plus;
import gregtech.api.gui.widgets.*;
import gregtech.api.interfaces.IGuiScreen;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VN;


public class GT_GUIContainer_DevEnergySource extends GT_GUIContainer_Machine_Plus {

    public GT_GUIContainer_DevEnergySource(
            final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity
                                          ) {
        super(new GT_Container_DevEnergySource(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/DevEnergySource.png", 256, 166);
        addGUIElements();
    }

    private void addGUIElements() {
        new GT_GuiIconCheckButton(this, 0, 8, 8, GT_GuiIcon.CHECKMARK, GT_GuiIcon.CROSS, "Enable Activity", "Disable Activity").setChecked(
                !getSource().isEnabled()).setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> {
            if (screen instanceof GT_GUIContainer_DevEnergySource && button instanceof GT_GuiIconCheckButton) {
                ((GT_GuiIconCheckButton) button).setChecked(((GT_GUIContainer_DevEnergySource) screen).getSource().isEnabled());
            }
        }).setOnClickHook((screen, button, mouseX, mouseY, clickType) -> button.setUpdateCooldown(20));
        new GT_GuiIconButton(this, 1, 24, 8, GT_GuiIcon.MATH_ZERO).setTooltipText("Sets amperage and voltage to 0");
        addRedstoneButton();
        addVoltTierSlider();
        addVoltageTextBox();
        addAmperageTextBox();
    }

    public GT_Container_DevEnergySource getSource() {
        return (GT_Container_DevEnergySource) mContainer;
    }

    private void addRedstoneButton() {
        final GT_GuiCycleButton rsButton = new GT_GuiCycleButton(this, 2, 40, 8, 0, new GT_GuiCycleButton.IconToolTipPair[]{
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.CROSS, "Ignores redstone"),
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.REDSTONE_OFF, "Requires low signal"),
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.REDSTONE_ON, "Requires signal"),
                });
        rsButton.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> {
            if (screen instanceof GT_GUIContainer_DevEnergySource && button instanceof GT_GuiCycleButton) {
                ((GT_GuiCycleButton) button).setState(((GT_GUIContainer_DevEnergySource) screen).getSource().getMode().ordinal());
            }
        }).setOnClickHook((screen, button, mouseX, mouseY, clickType) -> {
            if (screen instanceof GT_GUIContainer_DevEnergySource && button instanceof GT_GuiCycleButton) {
                ((GT_GuiCycleButton) button).cycle();
                ((GT_GUIContainer_DevEnergySource) screen).getSource().setMode(RSControlMode.getMode(((GT_GuiCycleButton) button).getState()));
                button.setUpdateCooldown(20);
                ((GT_GUIContainer_DevEnergySource) screen).getSource().sendPacket();
            }
        });
        rsButton.setDoCycle(false);
    }

    private void addVoltTierSlider() {
        final GT_GuiSlider voltTierSlider = new GT_GuiSlider(0, this, 8, 40, 128, 8, 0.0, 15.0, getSource().getEnergyTier());
        voltTierSlider.setTextHandler(slider -> String.format("Tier: %s", VN[(int) slider.getValue()]));
        voltTierSlider.setOnChange(slider -> {
            final IGuiScreen gui = slider.getGui();
            if (gui instanceof GT_GUIContainer_DevEnergySource) {
                slider.setValue(((GT_GUIContainer_DevEnergySource) gui).getSource().getEnergyTier());
            }
        });
        voltTierSlider.setOnClickHook((screen, button, mouseX, mouseY, clickType) -> {
            if (screen instanceof GT_GUIContainer_DevEnergySource && button instanceof GT_GuiSlider) {
                ((GT_GUIContainer_DevEnergySource) screen).getSource().setEnergyTier((int) ((GT_GuiSlider) button).getValue());
                ((GT_GUIContainer_DevEnergySource) screen).getSource().sendPacket();
            }
        });
    }

    private void addVoltageTextBox() {
        final GT_GuiIntegerTextBox vBox = new GT_GuiIntegerTextBox(this, 3, 8, 60, 128, 10);
        vBox.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> {
            if (screen instanceof GT_GUIContainer_DevEnergySource && button instanceof GT_GuiIntegerTextBox && !((GT_GuiIntegerTextBox) button).isFocused()) {
                ((GT_GuiIntegerTextBox) button).setText(String.valueOf(((GT_GUIContainer_DevEnergySource) screen).getSource().getVoltage()));
            }
        }).setOnClickHook((screen, button, mouseX, mouseY, clickType) -> {
            if (screen instanceof GT_GUIContainer_DevEnergySource && button instanceof GT_GuiIntegerTextBox) {
                button.setUpdateCooldown(20);
            }
        });
    }

    private void addAmperageTextBox() {
        final GT_GuiIntegerTextBox aBox = new GT_GuiIntegerTextBox(this, 4, 8, 80, 128, 10);
        aBox.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> {
            if (screen instanceof GT_GUIContainer_DevEnergySource && button instanceof GT_GuiIntegerTextBox && !((GT_GuiIntegerTextBox) button).isFocused()) {
                ((GT_GuiIntegerTextBox) button).setText(String.valueOf(((GT_GUIContainer_DevEnergySource) screen).getSource().getAmperage()));
            }
        }).setOnClickHook((screen, button, mouseX, mouseY, clickType) -> {
            if (screen instanceof GT_GUIContainer_DevEnergySource && button instanceof GT_GuiIntegerTextBox) {
                button.setUpdateCooldown(20);
            }
        });
    }

    /**
     * @param button Handler for a button click
     */
    @Override
    public void buttonClicked(final GuiButton button) {
        super.buttonClicked(button);
        if (button.id == 0 && button instanceof GT_GuiIconCheckButton) {
            ((GT_GuiIconCheckButton) button).setChecked(getSource().toggleEnabled());
        } else if (button.id == 1) {
            getSource().zeroOut();
        } else if (button.id == 2 && button instanceof GT_GuiCycleButton) {
            getSource().setMode(RSControlMode.getMode(((GT_GuiCycleButton) button).getState()));
        }
        getSource().sendPacket();
        updateButtons();
        clearSelectedButton();

    }

    private void updateButtons() {
        GuiButton button;
        for (Object o : buttonList) {
            button = (GuiButton) o;
            button.enabled = true;
        }
    }

    /**
     * Given textbox's value might have changed.
     *
     * @param box Textbox to apply
     */
    @Override
    public void applyTextBox(final GT_GuiIntegerTextBox box) {
        long i;
        String s = box.getText().trim();
        try {
            i = Long.parseLong(s);
        } catch (NumberFormatException ignored) {
            resetTextBox(box);
            return;
        }
        if (box.id == 3) {
            i = Math.max(0, Math.min(i, V[getSource().getEnergyTier()]));
            getSource().setVoltage(i);
        }
        if (box.id == 4) {
            final int l = (int) Math.max(0, i);
            getSource().setAmperage(l);
        }
        box.setUpdateCooldown(20);
        box.setText(String.valueOf(i));
        getSource().sendPacket();
    }

    /**
     * Says whether NEI should be shown in the GUI
     */
    @Override
    protected boolean isNEIEnabled() {
        return false;
    }

    /**
     * @param mouseX   Mouse X
     * @param mouseY   Mouse Y
     * @param parTicks Still dunno lol
     */
    @Override
    public void drawExtras(final int mouseX, final int mouseY, final float parTicks) {
        final int tColor = 0xFF3030FF;
        final int left = 140;
        drawString(String.format("%s(%d Eu/t)", getTierString(), V[getSource().getEnergyTier()]), left, 40, tColor);
        drawString(String.format("%d Eu/t(%s)", getSource().getVoltage(), VN[GT_Utility.getTier(getSource().getVoltage())]), left, 61, tColor);
        drawString("Amperage", left, 81, tColor);
        if (!getSource().isActive()) {
            drawString(getSource().getDisabledStatus(), 8, 100, rgbaToInt(0xFF, 0x55, 0x55, 0xFF));
        }
    }

    public String getTierString() {
        return VN[getSource().getEnergyTier()];
    }

}
