package gregtech.common.gui.dev;


import gregtech.api.enums.RSControlMode;
import gregtech.api.gui.GT_GUIContainer_Machine_Plus;
import gregtech.api.gui.widgets.*;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import lombok.val;
import lombok.var;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;

import java.awt.*;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VN;


public class GT_GUIContainer_DevEnergySource extends GT_GUIContainer_Machine_Plus {

    public static final Color TEXT_COLOR = new Color(0x30, 0x30, 0xFF, 0xFF);

    public static final Color ERROR_COLOR = new Color(144, 8, 8, 0xFF);

    private static int vTBoxY() {
        return vSliderY() + 20;
    }

    private static int aTBoxY() {
        return vTBoxY() + 20;
    }

    private static int vSliderY() {
        return topButtonRowY() + buttonSize() + 24;
    }

    private static int buttonSize() {
        return 16;
    }

    private static int elementLeft() {
        return 8;
    }

    private static int topButtonRowY() {
        return 8;
    }

    public GT_GUIContainer_DevEnergySource(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
        super(new GT_Container_DevEnergySource(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/DevEnergySource.png", 256, 166);
        addGUIElements();
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

    public GT_Container_DevEnergySource getSource() {
        return (GT_Container_DevEnergySource) mContainer;
    }

    /**
     *
     */
    @Override
    public void sendUpdateToServer() {
        getSource().sendPacket();
    }

    /**
     * Given textbox's value might have changed.
     *
     * @param box
     *         Textbox to apply
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
            i = Math.max(0, Math.min(i, V[getSource().getData().getTier()]));
            getSource().setVoltage(i);
        }
        if (box.id == 4) {
            final int l = (int) Math.max(0, i);
            getSource().setAmperage(l);
        }
        box.setUpdateCooldown(20);
        box.setText(String.valueOf(i));
        sendUpdateToServer();
    }

    /**
     * Says whether NEI should be shown in the GUI
     */
    @Override
    protected boolean isNEIEnabled() {
        return false;
    }

    /**
     * @param mouseX
     *         Mouse X
     * @param mouseY
     *         Mouse Y
     * @param parTicks
     *         Still dunno lol
     */
    @Override
    public void drawExtras(final int mouseX, final int mouseY, final float parTicks) {
        val left = 140;
        drawString(String.format("%s(%d Eu/t)", getTierString(), V[getSource().getData().getTier()]), left, vSliderY(), TEXT_COLOR);
        var volt = getSource().getData().getVoltage();
        drawString(String.format("%d Eu/t(%s)", volt, VN[GT_Utility.getTier(volt)]), left, vTBoxY(), TEXT_COLOR);
        drawString("Amperage", left, aTBoxY(), TEXT_COLOR);
        if (!getSource().getData().canRun()) {
            drawString(getSource().getDisabledStatus(), elementLeft(), (int) (topButtonRowY() + buttonSize() * 1.5f), ERROR_COLOR);
        } else {
            drawString("Running", elementLeft(), (int) (topButtonRowY() + buttonSize() * 1.5f), TEXT_COLOR);
        }
    }

    public String getTierString() {
        return VN[getSource().getData().getTier()];
    }

    @Override
    public boolean hasDWSAlternativeBackground() {
        return false;
    }

    private void addGUIElements() {
        addButtons();
        addVoltTierSlider();
        addVoltageTextBox();
        addAmperageTextBox();
    }

    private void addButtons() {
        val source = getSource();
        val activityCheck = new GT_GuiIconCheckButton(this, 0, elementLeft(), topButtonRowY(), GT_GuiIcon.CHECKMARK, GT_GuiIcon.CROSS, "Enable Activity",
                                                      "Disable Activity");
        activityCheck.setChecked(!getSource().getData().isEnabled()).setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> {
            activityCheck.setChecked(source.getData().isEnabled());
            sendUpdateToServer();
        }).setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> button.setUpdateCooldown(20));
        //
        val zeroButton = new GT_GuiIconButton(this, 1, elementLeft() + buttonSize(), topButtonRowY(), GT_GuiIcon.MATH_ZERO);
        zeroButton.setTooltipText("Sets amperage and voltage to 0");
        zeroButton.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> {
            source.zeroOut();
            sendUpdateToServer();
        });
        addRedstoneButton();
    }

    private void addRedstoneButton() {
        val source = getSource();
        val rsButton = new GT_GuiCycleButton(this, 2, elementLeft() + 2 * buttonSize(), topButtonRowY(), 0, new GT_GuiCycleButton.IconToolTipPair[]{
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.CROSS, "Ignores redstone"),
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.REDSTONE_OFF, "Requires low signal"),
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.REDSTONE_ON, "Requires signal"),
                });
        rsButton.setState(source.getData().getMode().ordinal());
        rsButton.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> rsButton.setState(source.getData().getMode().ordinal()));
        rsButton.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> {
            rsButton.cycle().setUpdateCooldown(20);
            source.setMode(RSControlMode.getMode(((GT_GuiCycleButton) button).getState()));
            sendUpdateToServer();
        });
        rsButton.setDoCycle(false);
    }

    private void addVoltTierSlider() {
        val source = getSource();
        val voltTierSlider = new GT_GuiSlider(0, this, elementLeft(), vSliderY(), 128, 8, 0.0, 15.0, getSource().getData().getTier(), 16);
        voltTierSlider.setTextHandler(slider -> String.format("Tier: %s", VN[(int) slider.getValue()]));
        voltTierSlider.setOnChange(slider -> slider.setValue(source.getData().getTier()));
        voltTierSlider.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> {
            source.setEnergyTier((int) voltTierSlider.getValue());
            sendUpdateToServer();
        });
    }

    private void addVoltageTextBox() {
        val source = getSource();
        val vBox = new GT_GuiIntegerTextBox(this, 3, elementLeft(), vTBoxY(), 128, 10);
        vBox.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> {
            if (!vBox.isFocused()) {
                vBox.setText(String.valueOf(source.getData().getVoltage()));
            }
        }).setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> vBox.setUpdateCooldown(20));
    }

    private void addAmperageTextBox() {
        val source = getSource();
        val aBox = new GT_GuiIntegerTextBox(this, 4, elementLeft(), aTBoxY(), 128, 10);
        aBox.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> {
            if (!aBox.isFocused()) {
                aBox.setText(String.valueOf(source.getData().getAmps()));
            }
        }).setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> button.setUpdateCooldown(20));
    }

}
