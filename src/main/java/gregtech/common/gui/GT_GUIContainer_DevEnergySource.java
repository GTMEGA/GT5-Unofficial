package gregtech.common.gui;


import appeng.integration.modules.GT;
import gregtech.api.gui.GT_GUIContainer;
import gregtech.api.gui.GT_GUIContainer_Machine_Plus;
import gregtech.api.gui.widgets.*;
import gregtech.api.interfaces.IGuiScreen;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VN;


public class GT_GUIContainer_DevEnergySource extends GT_GUIContainer_Machine_Plus {

    public GT_GUIContainer_DevEnergySource(
            final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity
                                          ) {
        super(new GT_Container_DevEnergySource(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/DevEnergySource.png", 256, 256);
        addGUIElements();
    }

    /**
     * @param button Handler for a button click
     */
    @Override
    public void buttonClicked(final GuiButton button) {
        super.buttonClicked(button);
        if (button.id == 0) {
            GT_GuiIconCheckButton checkButton = (GT_GuiIconCheckButton) button;
            getSource().toggleEnabled();
            checkButton.toggle();
        } else if (button.id == 1) {
            getSource().zeroOut();
        }
        getSource().sendPacket();
        updateButtons();
        clearSelectedButton();

    }

    public GT_Container_DevEnergySource getSource() {
        return (GT_Container_DevEnergySource) mContainer;
    }

    private void updateButtons() {
        GuiButton button;
        for (Object o : buttonList) {
            button = (GuiButton) o;
            button.enabled = true;
        }
    }

    /**
     * @param mouseX   Mouse X
     * @param mouseY   Mouse Y
     * @param parTicks Still dunno lol
     */
    @Override
    public void drawExtras(final int mouseX, final int mouseY, final float parTicks) {
        this.fontRendererObj.drawString(String.format("Tier: %s", VN[getSource().getEnergyTier()]), 32, 8, 0xFF555555);
        this.fontRendererObj.drawString(String.format("Voltage: %d", getSource().getVoltage()), 32, 24, 0xFF555555);
        this.fontRendererObj.drawString(String.format("Amperage: %d", getSource().getAmperage()), 32, 40, 0xFF555555);
        if (!getSource().isEnabled()) {
            this.fontRendererObj.drawString("Disabled", 32, 56, rgbaToInt(0xFF, 0x55, 0x55, 0xFF));
        }
    }

    private void addGUIElements() {
        new GT_GuiIconCheckButton(this, 0, 8, 8, GT_GuiIcon.CHECKMARK, GT_GuiIcon.CROSS, "Enable Activity", "Disable Activity").setChecked(
                !getSource().isEnabled());
        new GT_GuiIconButton(this, 1, 8, 16, GT_GuiIcon.MATH_ZERO).setTooltipText("Sets amperage and voltage to 0");
        addVoltTierSlider();
        addVoltageSlider();
        addAmperageSlider();
    }

    private void addAmperageSlider() {
        final GT_GuiSlider ampSlider = new GT_GuiSlider(2, this, 32, 96, 48, 8, 0.0, 256.0, getSource().getAmperage());
        ampSlider.setTextHandler(slider -> String.format("%dA", (int) slider.getValue()));
        ampSlider.setOnChange(slider -> {
            final IGuiScreen gui = slider.getGui();
            if (gui instanceof GT_GUIContainer_DevEnergySource) {
                slider.setValue(((GT_GUIContainer_DevEnergySource) gui).getSource().getAmperage());
            }
        });
        ampSlider.setOnClickBehavior((screen, button, mouseX, mouseY) -> {
            if (screen instanceof GT_GUIContainer_DevEnergySource && button instanceof GT_GuiSlider) {
                ((GT_GUIContainer_DevEnergySource) screen).getSource().setAmperage((int) ((GT_GuiSlider) button).getValue());
                getSource().sendPacket();
            }
        });
    }

    private void addVoltTierSlider() {
        final GT_GuiSlider voltTierSlider = new GT_GuiSlider(0, this, 32, 64, 48, 8, 0.0, 15.0, getSource().getEnergyTier());
        voltTierSlider.setTextHandler(slider -> VN[(int) slider.getValue()]);
        voltTierSlider.setOnChange(slider -> {
            final IGuiScreen gui = slider.getGui();
            if (gui instanceof GT_GUIContainer_DevEnergySource) {
                slider.setValue(((GT_GUIContainer_DevEnergySource) gui).getSource().getEnergyTier());
            }
        });
        voltTierSlider.setOnClickBehavior((screen, button, mouseX, mouseY) -> {
            if (screen instanceof GT_GUIContainer_DevEnergySource && button instanceof GT_GuiSlider) {
                ((GT_GUIContainer_DevEnergySource) screen).getSource().setEnergyTier((int) ((GT_GuiSlider) button).getValue());
                final GT_GuiSlider slider = sliders.get(1);
                if (slider != null) {
                    final double[] bounds = ((GT_GUIContainer_DevEnergySource) screen).getNewVoltageBounds();
                    slider.updateBounds(bounds, false);
                    getSource().sendPacket();
                }
            }
        });
    }

    private double[] getNewVoltageBounds() {
        final double[] bounds = {0.0, 0.0};
        final int tierLow, tierHigh;
        tierLow = Math.max(0, getSource().getEnergyTier() - 1);
        tierHigh = Math.min(V.length - 1, getSource().getEnergyTier());
        bounds[0] = V[tierLow];
        bounds[1] = V[tierHigh];
        return bounds;
    }

    private void addVoltageSlider() {
        final GT_GuiSlider voltageSlider = new GT_GuiSlider(1, this, 32, 80, 48, 8, 0.0, 8.0, 4.0);
        voltageSlider.setTextHandler(slider -> String.format("%.0f", slider.getValue()));
        voltageSlider.setOnChange(slider -> {
            final IGuiScreen gui = slider.getGui();
            if (gui instanceof GT_GUIContainer_DevEnergySource) {
                slider.setValue(((GT_GUIContainer_DevEnergySource) gui).getSource().getVoltage());
                final double[] bounds = ((GT_GUIContainer_DevEnergySource) gui).getNewVoltageBounds();
                slider.updateBounds(bounds, false);
            }
        });
        voltageSlider.setOnClickBehavior((screen, button, mouseX, mouseY) -> {
            if (screen instanceof GT_GUIContainer_DevEnergySource && button instanceof GT_GuiSlider) {
                ((GT_GUIContainer_DevEnergySource) screen).getSource().setVoltage((long) ((GT_GuiSlider) button).getValue());
                getSource().sendPacket();
            }
        });
    }

}
