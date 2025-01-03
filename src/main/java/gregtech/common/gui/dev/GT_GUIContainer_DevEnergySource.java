package gregtech.common.gui.dev;


import gregtech.api.enums.RSControlMode;
import gregtech.api.gui.GT_RichGuiContainer_Machine;
import gregtech.api.gui.widgets.GT_GuiCycleButton;
import gregtech.api.gui.widgets.GT_GuiIconButton;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.gui.widgets.icon.GT_GuiIcon;
import gregtech.api.gui.widgets.slider.GT_GuiSlider;
import gregtech.api.gui.widgets.slider.GT_GuiSlider_Horizontal;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import lombok.val;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.input.Mouse;

import java.awt.*;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VN;


public class GT_GUIContainer_DevEnergySource extends GT_RichGuiContainer_Machine {

    public static final int COOLDOWN = 1;

    public static final Color TEXT_COLOR = new Color(0x30, 0x30, 0xFF, 0xFF);

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

    private static void boxOnClick(final GT_GuiIntegerTextBox vBox) {
        vBox.setFocused(true);
        vBox.setUpdateCooldown(COOLDOWN);
    }

    private static int getRSButtonX() {
        return elementLeft() + 2 * buttonSize();
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
     *         Textbox to apply
     */
    @Override
    public void applyTextBox(final GT_GuiIntegerTextBox box) {
        long   i;
        String s = box.getText().trim();
        try {
            i = Long.parseLong(s);
        } catch (NumberFormatException ignored) {
            resetTextBox(box);
            return;
        }
        if (box.id == 3) {
            val curTier = getSource().getData().getTier();
            if (i > V[curTier] && curTier < 15) {
                getSource().setEnergyTier(getTierForVoltage(i));
            } else {
                i = Math.max(0, Math.min(i, V[curTier]));
            }
            getSource().setVoltage(i);
        }
        if (box.id == 4) {
            i = (int) Math.max(0, i);
            getSource().setAmperage((int) i);
        }
        box.setUpdateCooldown(COOLDOWN);
        boxOnUpdate(box, String.valueOf(i));
        sendUpdateToServer();
    }

    protected int getTierForVoltage(final long voltage) {
        for (int i = 0; i < V.length; i++) {
            if (V[i] >= voltage) {
                return i;
            }
        }
        return 15;
    }

    private static void boxOnUpdate(final GT_GuiIntegerTextBox aBox, final String source) {
        aBox.setText(source);
    }

    /**
     *
     */
    @Override
    public void sendUpdateToServer() {
//        sendPacketToServer();
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
    }

    private void sendPacketToServer() {
        getSource().sendPacket();
    }

    public GT_Container_DevEnergySource getSource() {
        return (GT_Container_DevEnergySource) mContainer;
    }

    public String getTierString() {
        return VN[getSource().getData().getTier()];
    }

    @Override
    public boolean hasDWSAlternativeBackground() {
        return false;
    }


    private void updateButtonBackground(final GT_GuiIconButton button, final boolean error) {
        val highlightIcon = error ? GT_GuiIcon.BUTTON_HIGHLIGHT : GT_GuiIcon.BUTTON_ERROR_HIGHLIGHT;
        val normalIcon    = error ? GT_GuiIcon.BUTTON_NORMAL : GT_GuiIcon.BUTTON_ERROR_NORMAL;
        button.setBackgroundIconHighlight(highlightIcon);
        button.setBackgroundIconNormal(normalIcon);
    }

    private void addGUIElements() {
        addButtons();
        addVoltTierSlider();
        addVoltageTextBox();
        addAmperageTextBox();
    }

    private void addButtons() {
        val activityCheck = new GT_GuiIconCheckButton(this, 0, elementLeft(), topButtonRowY(), GT_GuiIcon.CHECKMARK, GT_GuiIcon.CROSS, "Disable Activity", "Enable Activity");
        activityCheck.setChecked(!getSource().getData().isEnabled());
        activityCheck.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> activityCheckboxOnUpdate(activityCheck));
        activityCheck.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> activityCheckboxOnClick(activityCheck));
        //
        val zeroButton = new GT_GuiIconButton(this, 1, elementLeft() + buttonSize(), topButtonRowY(), GT_GuiIcon.MATH_ZERO);
        zeroButton.setTooltipText("Sets amperage and voltage to 0");
        zeroButton.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> zeroButtonOnClick());
        //
        addRedstoneButton();
    }

    private void zeroButtonOnClick() {
        getSource().zeroOut();
        sendUpdateToServer();
    }

    private void activityCheckboxOnUpdate(final GT_GuiIconCheckButton activityCheck) {
        val source = getSource();
        activityCheck.setChecked(source.getData().isEnabled());
        updateButtonBackground(activityCheck, source.getData().isEnabled());
    }

    private void activityCheckboxOnClick(final GT_GuiIconCheckButton activityCheck) {
        val source = getSource();
        activityCheck.setChecked(source.toggleEnabled());
        updateButtonBackground(activityCheck, source.getData().isEnabled());
        activityCheck.setUpdateCooldown(COOLDOWN);
        sendUpdateToServer();
    }

    private void addRedstoneButton() {
        val source = getSource();
        val rsButton = new GT_GuiCycleButton(this, 2, getRSButtonX(), topButtonRowY(), 0, new GT_GuiCycleButton.IconToolTipPair[]{
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.CROSS, "Ignores redstone"),
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.REDSTONE_OFF, "Requires low signal"),
                new GT_GuiCycleButton.IconToolTipPair(GT_GuiIcon.REDSTONE_ON, "Requires signal"),
                });
        rsButton.setState(source.getData().getMode().ordinal());
        rsButton.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> rsButtonOnUpdate(rsButton));
        rsButton.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> rsButtonOnClick(rsButton));
        rsButton.setDoCycle(false);
    }

    private void rsButtonOnClick(final GT_GuiCycleButton rsButton) {
        val source = getSource();
//        rsButton.cycle();
        rsButton.setUpdateCooldown(COOLDOWN);
        source.setMode(RSControlMode.getMode(rsButton.cycle().getState()));
        updateButtonBackground(rsButton, source.getData().isRsActive());
        sendUpdateToServer();
    }

    private void rsButtonOnUpdate(final GT_GuiCycleButton rsButton) {
        val source = getSource();
        rsButton.setState(source.getData().getMode().ordinal());
        updateButtonBackground(rsButton, source.getData().isRsActive());
    }

    private void addVoltTierSlider() {
        val voltTierSlider = new GT_GuiSlider_Horizontal(this, 0, elementLeft(), vSliderY(), 128, 8, 0.0, 15.0, getSource().getData().getTier(), 16);
        voltTierSlider.setMidColor(voltTierSlider.getEdgeColor());
        voltTierSlider.setIgnoreYForCheck(true);
        voltTierSlider.setTextHandler(slider -> String.format("Tier: %s", VN[(int) slider.getValue()]));
        voltTierSlider.setOnChange(this::voltSliderOnChange);
        voltTierSlider.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> voltTierSliderOnClick(voltTierSlider));
        voltTierSlider.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> voltSliderScroll(voltTierSlider));
        voltTierSlider.setLiveUpdate(true);
    }

    private void voltSliderOnChange(final GT_GuiSlider slider) {
        if (!slider.isDragged() && slider.isLastInteracted(this)) {
            slider.setValue(getSource().getData().getTier());
        }
    }

    private void voltSliderScroll(final GT_GuiSlider slider) {
        val minMotion = 1;
        val dWheel = Mouse.getDWheel();
        val absDWheel = Math.abs(dWheel);
        if (absDWheel < minMotion) {
            return;
        }
        val current = slider.getValue();
        if (dWheel > 0) {
            slider.setValue(current + 1);
        } else {
            slider.setValue(current - 1);
        }
        setTierFromSlider(slider);
    }

    private void voltTierSliderOnClick(final GT_GuiSlider voltTierSlider) {
        setTierFromSlider(voltTierSlider);
    }

    private void setTierFromSlider(final GT_GuiSlider slider) {
        getSource().setEnergyTier(slider.getIntValue());
        slider.setUpdateCooldown(COOLDOWN);
        sendUpdateToServer();
    }

    private void addVoltageTextBox() {
        val vBox = new GT_GuiIntegerTextBox(this, 3, elementLeft(), vTBoxY(), 128, 10);
        vBox.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> boxOnUpdate(vBox, getVoltString()));
        vBox.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> boxOnClick(vBox));
    }

    private String getVoltString() {
        return String.valueOf(getSource().getData().getVoltage());
    }

    private void addAmperageTextBox() {
        val aBox = new GT_GuiIntegerTextBox(this, 4, elementLeft(), aTBoxY(), 128, 10);
        aBox.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> boxOnUpdate(aBox, getAmpString()));
        aBox.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> boxOnClick(aBox));
    }

    private String getAmpString() {
        return String.valueOf(getSource().getData().getAmps());
    }

}
