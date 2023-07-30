package gregtech.common.gui;


import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GT_GUIContainer_DevEnergySource extends GT_GUIContainerMetaTile_Machine {

    public enum Icons {
        // Voltage Buttons
        ICON_ULV(IconFamily.VOLTAGE, 0),
        ICON_LV(IconFamily.VOLTAGE, 1),
        ICON_MV(IconFamily.VOLTAGE, 2),
        ICON_HV(IconFamily.VOLTAGE, 3),
        ICON_EV(IconFamily.VOLTAGE, 4),
        ICON_IV(IconFamily.VOLTAGE, 5),
        ICON_LuV(IconFamily.VOLTAGE, 6),
        ICON_ZPM(IconFamily.VOLTAGE, 7),
        ICON_UV(IconFamily.VOLTAGE, 8),
        ICON_UHV(IconFamily.VOLTAGE, 9),
        ICON_UEV(IconFamily.VOLTAGE, 10),
        ICON_UIV(IconFamily.VOLTAGE, 11),
        ICON_UMV(IconFamily.VOLTAGE, 12),
        ICON_UXV(IconFamily.VOLTAGE, 13),
        ICON_OpV(IconFamily.VOLTAGE, 14),
        ICON_MAX(IconFamily.VOLTAGE, 15),
        // Amperage Bump Buttons
        ICON_DEC_AMP(IconFamily.AMPERAGE_BUMP, 0),
        ICON_INC_AMP(IconFamily.AMPERAGE_BUMP, 1),
        // Amperage Scale Buttons
        ICON_SCALE_AMP_DOWN_2x(IconFamily.AMPERAGE_SCALE, 0),
        ICON_SCALE_AMP_DOWN_3x(IconFamily.AMPERAGE_SCALE, 1),
        ICON_SCALE_AMP_DOWN_4x(IconFamily.AMPERAGE_SCALE, 2),
        ICON_SCALE_AMP_UP_2x(IconFamily.AMPERAGE_SCALE, 3),
        ICON_SCALE_AMP_UP_3x(IconFamily.AMPERAGE_SCALE, 4),
        ICON_SCALE_AMP_UP_4x(IconFamily.AMPERAGE_SCALE, 5),
        // Misc
        ICON_ZERO(IconFamily.ZERO_OUT),
        ICON_TOGGLE(IconFamily.TOGGLE);


        public enum IconFamily {

            VOLTAGE(128, 28, 8, (final Icons icon) -> String.format("%s Power", GT_Values.VN[icon.famIndex]), renderVoltage()),
            AMPERAGE_BUMP(8, 28, 1, (final Icons icon) -> String.format("%s Amperage by 1", icon.famIndex == 1 ? "Increases" : "Decreases"), renderAmpBump()),
            AMPERAGE_SCALE(8, 44, 3, (final Icons icon) -> {
                boolean inc = icon.famIndex >= 3;
                int amt = (icon.famIndex % 3) + 2;
                String str = inc ? String.format("%d", amt) : String.format("1/%d", amt);
                return String.format("Scales amperage by %s", str);
            }, renderAmpScale()),
            ZERO_OUT(8, 104, 1, (final Icons icon) -> "Zeros out Values", (final GT_GUIContainer_DevEnergySource gui, final Icons icon) -> gui.drawIcon(GT_GuiIcon.DISABLE, icon.posX, icon.posY, false)),
            TOGGLE(24, 104, 1, (final Icons icon) -> "Toggles Activity", (final GT_GUIContainer_DevEnergySource gui, final Icons icon) -> {
                final boolean isEnabled = gui.getSource().isEnabled();
                final GT_GuiIcon status = isEnabled ? GT_GuiIcon.REDSTONE_ON : GT_GuiIcon.REDSTONE_OFF;
                gui.drawIcon(status, icon.posX, icon.posY, isEnabled);
            });


            public interface IconRenderer {

                void process(final GT_GUIContainer_DevEnergySource gui, final Icons icon);

            }


            public interface ITooltipGetter {

                String getTooltip(final Icons icon);

            }


            private final int xOffset;

            private final int yOffset;

            private final int perColumn;

            private final ITooltipGetter tooltip;

            private final IconRenderer renderer;

            private static IconRenderer renderVoltage() {
                return (final GT_GUIContainer_DevEnergySource gui, final Icons icon) -> {
                    final GT_GuiIcon render = GT_GuiIcon.getTierIcon(icon.famIndex);
                    gui.drawIcon(render, icon.posX, icon.posY, gui.getSource().getTier() == icon.famIndex);
                };
            }

            private static IconRenderer renderAmpBump() {
                return (final GT_GUIContainer_DevEnergySource gui, final Icons icon) -> {
                    final int num = 2 * icon.famIndex - 1;
                    final boolean neg = num < 0;
                    final int temp = neg ? -num : num;
                    final int posX = icon.posX;
                    final int posY = icon.posY;
                    final GT_GuiIcon sign = neg ? GT_GuiIcon.MATH_MINUS : GT_GuiIcon.MATH_PLUS;
                    GT_GuiIcon.render(GT_GuiIcon.BUTTON_NORMAL, posX, posY, 16, 16, 0, true);
                    GT_GuiIcon.render(sign, posX + 1, posY + 6, 6, 6, 0, true);
                    GT_GuiIcon.renderInteger(temp, posX + 5, posY + 1, 11, 14, 0, true, false);
                };
            }

            private static IconRenderer renderAmpScale() {
                return (final GT_GUIContainer_DevEnergySource gui, final Icons icon) -> {
                    boolean inc = icon.famIndex >= 3;
                    int temp = icon.famIndex % 3 + 2;
                    drawMult(icon.posX, icon.posY);
                    if (inc) {
                        GT_GuiIcon.renderInteger(temp, icon.posX + 5, icon.posY + 1, 11, 14, 0, true, false);
                    } else {
                        GT_GuiIcon.renderFraction(1, temp, icon.posX + 5, icon.posY + 1, 11, 14, 0, true, false);
                    }
                };
            }

            IconFamily(final int xOffset, final int yOffset, final int perColumn, final ITooltipGetter tooltipGetter,
                       final IconRenderer renderer
                      ) {
                this.xOffset = xOffset;
                this.yOffset = yOffset;
                this.perColumn = perColumn;
                this.tooltip = tooltipGetter;
                this.renderer = renderer;
            }

            public int getX(final int index) {
                return GT_Container_DevEnergySource.slotX(index, xOffset, perColumn);
            }

            public int getY(final int index) {
                return GT_Container_DevEnergySource.slotY(index, yOffset, perColumn);
            }

        }

        private static final int xSize = 16, ySize = 16;

        private final IconFamily family;

        private final int famIndex;

        private final int posX;

        private final int posY;

        Icons(final IconFamily family) {
            this(family, 0);
        }

        Icons(final IconFamily family, final int famIndex) {
            this.family = family;
            this.famIndex = famIndex;
            this.posX = family.getX(famIndex);
            this.posY = family.getY(famIndex);
        }

        public String getTooltip(final int x, final int y) {
            if (y >= posY && y <= posY + ySize) {
                if (x >= posX && x <= posX + xSize) {
                    return family.tooltip.getTooltip(this);
                }
            }
            return null;
        }

    }


    public static final GT_GuiIcon[] voltageIcons = new GT_GuiIcon[GT_Values.V.length];

    static {
        for (int i = 0; i < GT_Values.V.length; i++) {
            voltageIcons[i] = GT_GuiIcon.getTierIcon(i);
        }
    }

    public GT_GUIContainer_DevEnergySource(
            final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity
                                          ) {
        super(new GT_Container_DevEnergySource(aInventoryPlayer, aTileEntity),
              "gregtech:textures/gui/DevEnergySource.png");

    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
        super.drawGuiContainerForegroundLayer(par1, par2);
        drawInfo();
        magicGLPre();
        renderIcons();
        magicGLPost();
        drawTooltips(par1, par2);
    }

    private void renderIcons() {
        for (final Icons icon: Icons.values()) {
            icon.family.renderer.process(this, icon);
        }
    }

    private void drawTooltips(final int mX, final int mY) {
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        int x = mX - xStart;
        int y = mY - yStart + 5;
        List<String> toolTips = new ArrayList<>();
        for (final Icons icon: Icons.values()) {
            final String tTip = icon.getTooltip(x, y);
            if (tTip != null) {
                toolTips.add(tTip);
            }
        }
        if (!toolTips.isEmpty()) {
            drawHoveringText(toolTips, x, y, this.fontRendererObj);
        }
    }

    private static void magicGLPost() {
        GL11.glPopAttrib();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private static void magicGLPre() {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1, 1, 1, 1);
    }

    private void drawInfo() {
        this.fontRendererObj.drawString("Developer Energy Source", 8, 8, 0xFF555555);
        this.fontRendererObj.drawString("Voltages", 125, 18, 0xFF555555);
        final String tierName = String.format("Tier: %s", GT_Values.VN[getSource().getTier()]);
        this.fontRendererObj.drawString(tierName, 48, 33, 0xFF555555);
        final String ampInfo = String.format("Amps: %dA", getSource().getAmperage());
        this.fontRendererObj.drawString(ampInfo, 48, 43, 0xFF555555);
        if (!getSource().isEnabled()) {
            this.fontRendererObj.drawString("Disabled", 48, 53, 0xFF555555);
        }
        this.fontRendererObj.drawString("Amperage Modifiers", 8, 18, 0xFF555555);
        this.fontRendererObj.drawString("Misc.", 8, 94, 0xFF555555);
    }

    public GT_Container_DevEnergySource getSource() {
        return (GT_Container_DevEnergySource) mContainer;
    }

    private static void drawMult(final int x, final int y) {
        GT_GuiIcon.render(GT_GuiIcon.BUTTON_NORMAL, x, y, 16, 16, 0, true);
        GT_GuiIcon.render(GT_GuiIcon.MATH_TIMES, x + 1, y + 5, 6, 6, 0, true);
    }

    private void drawIcon(final GT_GuiIcon icon, final int x, final int y, final boolean active) {
        final GT_GuiIcon background = active ? GT_GuiIcon.BUTTON_HIGHLIGHT : GT_GuiIcon.BUTTON_NORMAL;
        GT_GuiIcon.render(background, x, y, 16, 16, 0, true);
        if (icon != null) {
            GT_GuiIcon.render(icon, x, y, 16, 16, 0, true);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

}
