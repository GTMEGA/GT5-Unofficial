package gregtech.common.gui.remotedetonator;


import gregtech.api.gui.GT_RichGuiContainer;
import gregtech.api.gui.widgets.GT_GuiScrollPanel;
import gregtech.api.gui.widgets.GT_GuiSlider;
import gregtech.common.blocks.explosives.GT_Block_Explosive;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.minecraft.client.Minecraft;

import java.util.stream.Collectors;


@Getter
public class GT_RemoteDetonator_GuiContainer extends GT_RichGuiContainer {

    private final GT_RemoteDetonator_Container remoteDetonatorContainer;

    private GT_GuiScrollPanel<GT_RemoteDetonator_GuiContainer> scrollPanel;

    private GT_GuiSlider scrollBar;

    @Setter
    private RemoteDetonator_GuiEntry selectedEntry = null, hoveredEntry = null;

    public GT_RemoteDetonator_GuiContainer(final GT_RemoteDetonator_Container aContainer) {
        super(aContainer, "gregtech:textures/gui/remote_detonator_gui.png", 338, 166);
        this.remoteDetonatorContainer = aContainer;
        addGUIElements();
    }

    @Override
    protected void preDrawHook(final int mouseX, final int mouseY, final float parTicks) {
        for (val element: scrollPanel.getScrollableElements().values()) {
            val entry = (RemoteDetonator_GuiEntry) element;
            if (entry.inBounds(mouseX, mouseY, 0)) {
                hoveredEntry = entry;
                break;
            } else if (hoveredEntry == entry) {
                hoveredEntry = null;
            }
        }
    }

    private void addGUIElements() {
        val x = 13;
        val yOffset = 13;
        val height = 140;
        val barHeight = 10;
        val width = 180;
        val scrollHeight = height - 4;
        scrollPanel = new GT_GuiScrollPanel<>(this, 0, x, yOffset, width, height, 0.0, width - 4, scrollHeight);
        scrollBar = new GT_GuiSlider(this, 1, width + 20, 13, 80, barHeight, 0.0, 1.0, 0.0, -1);
        //
        val keys = remoteDetonatorContainer.getTargetList().getTargets().keySet().stream().sorted().collect(Collectors.toList());
        for (val key: keys) {
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
    }

    private void onEntryUpdate(final RemoteDetonator_GuiEntry entry) {
        val target = entry.getTarget();
        val x = target.getX();
        val y = target.getY();
        val z = target.getZ();
        val world = remoteDetonatorContainer.getWorld();
        val block = world.getBlock(x, y, z);
        val metadata = world.getBlockMetadata(x, y, z);
        entry.setValid((block instanceof GT_Block_Explosive) && ((GT_Block_Explosive) block).isPrimed(metadata));
    }

    private void onEntryClick(final RemoteDetonator_GuiEntry entry) {
        if (selectedEntry != entry) {
            selectedEntry = entry;
        } else {
            selectedEntry = null;
        }
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

    @Override
    protected boolean isNEIEnabled() {
        return false;
    }

    @Override
    public void drawExtras(final int mouseX, final int mouseY, final float parTicks) {

    }

    @Override
    public boolean hasDWSAlternativeBackground() {
        return false;
    }

}
