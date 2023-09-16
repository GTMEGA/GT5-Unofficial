package gregtech.common.gui.remotedetonator;


import gregtech.api.gui.GT_RichGuiContainer;
import gregtech.api.gui.widgets.GT_GuiIconButton;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.gui.widgets.GT_GuiScrollPanel;
import gregtech.api.gui.widgets.icon.GT_GuiIcon;
import gregtech.api.gui.widgets.slider.GT_GuiSlider_Vertical;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.explosives.GT_Block_Explosive;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import java.util.stream.Collectors;


@Getter
public class GT_RemoteDetonator_GuiContainer extends GT_RichGuiContainer {

    private final GT_RemoteDetonator_Container remoteDetonatorContainer;

    private GT_GuiScrollPanel<GT_RemoteDetonator_GuiContainer> scrollPanel;

    private GT_GuiIconButton detonateButton;

    private GT_GuiSlider_Vertical scrollBar;

    private GT_GuiIntegerTextBox detonationDelay;

    @Setter
    private RemoteDetonator_GuiEntry selectedEntry = null, hoveredEntry = null;

    private GT_GuiIconCheckButton toggleExplosiveArmedButton;

    public GT_RemoteDetonator_GuiContainer(final GT_RemoteDetonator_Container aContainer) {
        super(aContainer, "gregtech:textures/gui/remote_detonator_gui.png", 338, 166);
        this.remoteDetonatorContainer = aContainer;
        addGUIElements();
    }

    private void addGUIElements() {
        val scrollPanelX = 13;
        val yOffset = 13;
        val height = 140;
        val barWidth = 10;
        val width = 180;
        val scrollHeight = height - 4;
        scrollPanel = new GT_GuiScrollPanel<>(this, 0, scrollPanelX, yOffset, width, height, 0.0, width - 4, scrollHeight);
        val scrollBarX = width + scrollPanelX + 2;
        scrollBar = new GT_GuiSlider_Vertical(this, 1, scrollBarX, yOffset, barWidth, height, 0.0, 1.0, 0.0, -1);
        //
        val keys = remoteDetonatorContainer.getTargetList().getTargets().keySet().stream().sorted().collect(Collectors.toList());
        for (val key : keys) {
            val target = remoteDetonatorContainer.getTargetList().getTargets().get(key);
            val entry = new RemoteDetonator_GuiEntry(scrollPanel, remoteDetonatorContainer.getTargetList(), target);
            entry.setOnClickBehavior((screen, element, mouseX, mouseY, mouseButton) -> onEntryClick(entry));
            entry.setOnUpdateBehavior((screen, element, mouseX, mouseY, clickType) -> onEntryUpdate(entry));
        }
        //
        scrollBar.setOnChange((slider) -> scrollPanel.setCurrentScroll(scrollBar.getCurrent()));
        scrollBar.setShowNumbers(false);
        scrollBar.setOnUpdateBehavior((screen, element, mouseX, mouseY, clickType) -> scrollBar.setBarDiameter(1 - scrollPanel.getMaxScrollFactor()));
        scrollBar.setLiveUpdate(true);
        //
        val detonationDelayX = scrollBarX + barWidth + 5;
        detonationDelay = new GT_GuiIntegerTextBox(this, 2, detonationDelayX, yOffset, 75, 10);
        detonationDelay.setText(String.valueOf(remoteDetonatorContainer.getTargetList().getDelay()));
        //
        val buttonY = yOffset + 15;
        detonateButton = new GT_GuiIconButton(this, 3, detonationDelayX, buttonY, GT_GuiIcon.MEGA_EXPLOSION);
        detonateButton.setBackgroundIconNormal(GT_GuiIcon.BUTTON_NORMAL);
        detonateButton.setTooltipText("Detonate Explosives");
        //
        toggleExplosiveArmedButton = new GT_GuiIconCheckButton(this, 4, detonationDelayX + 20, buttonY, GT_GuiIcon.CHECKMARK, GT_GuiIcon.CROSS, "Arm Explosives", "Disarm Explosives");
        toggleExplosiveArmedButton.setOnUpdateBehavior((screen, button, mouseX, mouseY, clickType) -> toggleExplosiveArmedButtonOnUpdate(toggleExplosiveArmedButton));
        toggleExplosiveArmedButton.setOnClickBehavior((screen, button, mouseX, mouseY, clickType) -> toggleExplosiveArmedButtonOnClick(toggleExplosiveArmedButton));
    }

    private void toggleExplosiveArmedButtonOnClick(final GT_GuiIconCheckButton toggleExplosiveArmedButton) {
        if (selectedEntry != null && selectedEntry.isValidBlock()) {
            val target = selectedEntry.getTarget();
            val x = target.getX();
            val y = target.getY();
            val z = target.getZ();
            val world = remoteDetonatorContainer.getWorld();
            val block = world.getBlock(x, y, z);
            val metadata = world.getBlockMetadata(x, y, z);
            if (block instanceof GT_Block_Explosive) {
                val explosive = (GT_Block_Explosive) block;
                val isPrimed = explosive.isPrimed(metadata);
                explosive.setPrimed(world, x, y, z, !isPrimed);
                toggleExplosiveArmedButton.setChecked(!isPrimed);
            }
        }
    }

    private void toggleExplosiveArmedButtonOnUpdate(final GT_GuiIconCheckButton toggleExplosiveArmedButton) {
        if (selectedEntry != null && selectedEntry.isValidBlock()) {
            toggleExplosiveArmedButton.enabled = true;
            toggleExplosiveArmedButton.setChecked(selectedEntry.isBlockPrimed());
        } else {
            toggleExplosiveArmedButton.setChecked(false);
            toggleExplosiveArmedButton.enabled = false;
        }
    }

    private void onEntryClick(final RemoteDetonator_GuiEntry entry) {
        if (selectedEntry != entry) {
            selectedEntry = entry;
        } else {
            selectedEntry = null;
        }
    }

    private void onEntryUpdate(final RemoteDetonator_GuiEntry entry) {
        val target = entry.getTarget();
        val x = target.getX();
        val y = target.getY();
        val z = target.getZ();
        val world = remoteDetonatorContainer.getWorld();
        val block = world.getBlock(x, y, z);
        val metadata = world.getBlockMetadata(x, y, z);
        entry.setValidBlock((block instanceof GT_Block_Explosive));
        if (entry.isValidBlock() && block instanceof GT_Block_Explosive) {
            entry.setBlockPrimed(((GT_Block_Explosive) block).isPrimed(metadata));
        } else {
            entry.setBlockPrimed(false);
        }
    }

    /**
     * @param button Handler for a button click
     */
    @Override
    public void buttonClicked(final GuiButton button) {
        super.buttonClicked(button);
        uncheckButtons();
        clearSelectedButton();
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
    }

    /**
     * Given textbox's value might have changed.
     *
     * @param box
     */
    @Override
    public void applyTextBox(final GT_GuiIntegerTextBox box) {
        val s = box.getText().trim();
        if (s.contains(".")) {
            resetTextBox(box);
            return;
        }
        int value = -1;
        if (box.id == 2) {
            try {
                value = Integer.parseInt(s);
            } catch (NumberFormatException ignored) {
                resetTextBox(box);
                return;
            }
            remoteDetonatorContainer.getTargetList().setDelay(value);
        }
        if (value > 0) {
            box.setUpdateCooldown(20);
            box.setText(String.valueOf(value));
            sendUpdateToServer();
        } else {
            resetTextBox(box);
        }
    }

    private void sendUpdateToServer() {

    }

    @Override
    protected void preDrawHook(final int mouseX, final int mouseY, final float parTicks) {
        for (val element : scrollPanel.getScrollableElements().values()) {
            val entry = (RemoteDetonator_GuiEntry) element;
            if (entry.inBounds(mouseX, mouseY, 0)) {
                hoveredEntry = entry;
                break;
            } else if (hoveredEntry == entry) {
                hoveredEntry = null;
            }
        }
    }

    /**
     * @param mouseX
     * @param mouseY
     * @param parTicks
     */
    @Override
    public void drawForegroundLayer(final int mouseX, final int mouseY, final float parTicks) {
        super.drawForegroundLayer(mouseX, mouseY, parTicks);
        /* val c1 = new Color(0xFF, 0xFF, 0xFF, 0xFF);
        val c2 = new Color(0x00, 0x00, 0x00, 0xFF);
        scrollBar.drawGradientRect(250, 8, 300, 28, c1.getRGB(), c2.getRGB(), true);
        scrollBar.drawGradientRect(250, 28, 300, 48, c1.getRGB(), c2.getRGB(), false); */
    }

    @Override
    protected boolean isNEIEnabled() {
        return false;
    }

    @Override
    public void drawExtras(final int mouseX, final int mouseY, final float parTicks) {
        if (selectedEntry != null && selectedEntry.isValidBlock()) {
            val target = selectedEntry.getTarget();
            val x = target.getX();
            val y = target.getY();
            val z = target.getZ();
            val world = remoteDetonatorContainer.getWorld();
            val block = world.getBlock(x, y, z);
            val metadata = world.getBlockMetadata(x, y, z);
            if (block instanceof GT_Block_Explosive) {
                val explosive = (GT_Block_Explosive) block;
                val isPrimed = explosive.isPrimed(metadata);
                val explosiveName = GT_LanguageManager.getTranslation(explosive.getItem().getUnlocalizedName());
                val armedText = isPrimed ? "Armed" : "Disarmed";
                val color = isPrimed ? 0xFF0000 : 0x000000;
                drawString(getFontRenderer(), explosiveName, 210, 50, target.getExplosiveType().getBackgroundColor().getRGB());
                drawString(getFontRenderer(), armedText, 210, 62, color);
            }
        }
    }

    @Override
    public boolean hasDWSAlternativeBackground() {
        return false;
    }

}
