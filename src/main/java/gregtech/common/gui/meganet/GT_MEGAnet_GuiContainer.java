package gregtech.common.gui.meganet;


import gregtech.api.gui.GT_RichGuiContainer;
import gregtech.api.gui.widgets.icon.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.gui.widgets.slider.GT_GuiSlider_Horizontal;
import gregtech.common.items.GT_MEGAnet;
import lombok.Getter;
import lombok.val;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;


@Getter
public class GT_MEGAnet_GuiContainer extends GT_RichGuiContainer {

    private final Color TEXT_COLOR = new Color(69, 69, 198, 0xFF);

    private final GT_MEGAnet_Container meganetContainer;

    public GT_MEGAnet_GuiContainer(final GT_MEGAnet_Container aContainer) {
        super(aContainer, "gregtech:textures/gui/MEGAnet_GUI.png", 338, 166);
        this.meganetContainer = aContainer;
        addGUIElements();
    }

    /**
     * True by default specifically so that anyone implementing this wrong gets a nice fat purple and black checkerboard
     */
    @Override
    public boolean hasDWSAlternativeBackground() {
        return false;
    }

    @Override
    public void buttonClicked(final GuiButton button) {
        super.buttonClicked(button);
        clearSelectedButton();
        uncheckButtons();
    }

    @Override
    public void closeScreen() {
        meganetContainer.sendSettingUpdate();
        meganetContainer.sendFilterUpdate();
        super.closeScreen();
    }

    @Override
    public void drawExtras(final int mouseX, final int mouseY, final float parTicks) {
        val range = GT_MEGAnet_Container.MEGANET.getRange(meganetContainer.getStack());
        drawString(String.format("Range: %d (%d) blocks", range, GT_MEGAnet_Container.MEGANET.heldRange(range)), rangeSlideX(), rangeSlideY() + 15, TEXT_COLOR);
        if (!isDWSEnabled()) {
            drawString("Looks weird? Try using Double Wide Surprise!", rangeSlideX(), rangeSlideY() + 25, new Color(0xFF, 0x00, 0x00, 0xFF));
        }
    }

    private int rangeSlideX() {
        return filterSlotRight();
    }

    private int rangeSlideY() {
        return buttonY() + buttonWidth();
    }

    public int filterSlotRight() {
        return 224;
    }

    private int buttonY() {
        return 9;
    }

    public int buttonWidth() {
        return 18;
    }

    @Override
    protected boolean autoDrawSlots() {
        return true;
    }

    private void addGUIElements() {
        val meganetEnabledButton = new GT_GuiIconCheckButton(this, 0, enabledButtonX(), buttonY(), GT_GuiIcon.CHECKMARK, GT_GuiIcon.CROSS, "Disable MEGAnet", "Enable MEGAnet");
        meganetEnabledButton.setChecked(meganetContainer.getMEGAnetActive()); // <--- This seems redundant, but it helps ensure the tooltips are initialized
        meganetEnabledButton.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> meganetEnabledButton.setChecked(meganetContainer.getMEGAnetActive()));
        meganetEnabledButton.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> {
            meganetEnabledButton.setChecked(meganetContainer.toggleMeganet());
            meganetEnabledButton.setUpdateCooldown(20);
        });
        addToolTip(meganetEnabledButton.getTooltip());
        //
        val meganetFilterActiveButton = new GT_GuiIconCheckButton(this, 1, filterEnabledButtonX(), buttonY(), GT_GuiIcon.CHECKMARK, GT_GuiIcon.CROSS, "Disable Filter", "Enable Filter");
        meganetFilterActiveButton.setChecked(meganetContainer.getFilterActive()); // <--- This seems redundant, but it helps ensure the tooltips are initialized
        meganetFilterActiveButton.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> meganetFilterActiveButton.setChecked(meganetContainer.getFilterActive()));
        meganetFilterActiveButton.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> {
            meganetFilterActiveButton.setChecked(meganetContainer.toggleFilter());
            meganetFilterActiveButton.setUpdateCooldown(20);
        });
        addToolTip(meganetFilterActiveButton.getTooltip());
        //
        val meganetFilterWhitelistButton = new GT_GuiIconCheckButton(this, 2, filterWhiteListButtonX(), buttonY(), GT_GuiIcon.WHITELIST, GT_GuiIcon.BLACKLIST, "Blacklist", "Whitelist");
        meganetFilterWhitelistButton.setChecked(meganetContainer.getFilterActive()); // <--- This seems redundant, but it helps ensure the tooltips are initialized
        meganetFilterWhitelistButton.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> meganetFilterWhitelistButton.setChecked(meganetContainer.getFilterWhitelist()));
        meganetFilterWhitelistButton.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> {
            meganetFilterWhitelistButton.setChecked(meganetContainer.toggleWhitelist());
            meganetFilterWhitelistButton.setUpdateCooldown(20);
        });
        addToolTip(meganetFilterWhitelistButton.getTooltip());
        //
        val meganetRangeSlider = new GT_GuiSlider_Horizontal(this, 3, rangeSlideX(), rangeSlideY(), 96, 10, 0.0, GT_MEGAnet.MAX_RANGE, meganetContainer.getRange(), -1);
        meganetRangeSlider.setOnChange((slider) -> {
            meganetContainer.setRange((int) meganetRangeSlider.getValue());
            meganetRangeSlider.setUpdateCooldown(20);
        });
        meganetRangeSlider.setShowNumbers(false);
        meganetRangeSlider.setOnUpdateBehavior((screen, slider, mouseX, mouseY, clickType) -> meganetUpdateBehavior(meganetRangeSlider));
    }

    private void meganetUpdateBehavior(final GT_GuiSlider_Horizontal megaNetSlider) {
        if (!(megaNetSlider.isDragged())) {
            megaNetSlider.setValue(meganetContainer.getRange());
        }
    }

    private int filterWhiteListButtonX() {
        return filterEnabledButtonX() + buttonWidth();
    }

    private int filterEnabledButtonX() {
        return enabledButtonX() + buttonWidth();
    }

    private int enabledButtonX() {
        return filterSlotRight();
    }

}
