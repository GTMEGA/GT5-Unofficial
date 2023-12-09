package gregtech.common.covers;


import com.google.common.io.ByteArrayDataInput;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_CanvasPanel;
import gregtech.api.gui.widgets.GT_GuiTooltip;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.INewCoverData;
import gregtech.api.util.ISerializableObject;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import javax.annotation.Nonnull;
import java.awt.*;


@Getter
public class GT_Cover_AdvancedFluidRegulator extends GT_CoverBehaviorBase<GT_Cover_AdvancedFluidRegulator.InternalData> {

    @AllArgsConstructor
    public class InternalData implements INewCoverData {

        @Getter
        @Setter
        @AllArgsConstructor
        public class FluidSetting {


            private int flowRate;

            private boolean outputting;

            private boolean enabled;

            public FluidSetting(final int flowRate) {
                this(flowRate, true, true);
            }

            public int getFlags() {
                return (outputting ? 1 : 0) | (enabled ? 2 : 0);
            }

            public void setFlags(final int flags) {
                setOutputting((flags & 1) != 0);
                setEnabled((flags & 2) != 0);
            }

            public FluidSetting copy() {
                return new FluidSetting(flowRate, outputting, enabled);
            }

            public int getEffectiveSpeed() {
                return enabled ? getEffectiveFlowRate() : 0;
            }

            public int getEffectiveFlowRate() {
                return outputting ? flowRate : -flowRate;
            }

        }


        private final @Nonnull FluidSetting[] fluidSettings = new FluidSetting[15];

        private boolean enabled;

        private boolean inverted;

        public InternalData() {
            setDefaultFluidSettings();
        }

        public void setDefaultFluidSettings() {
            for (int i = 0; i < fluidSettings.length; i++) {
                fluidSettings[i] = new FluidSetting(maxTransferRate / 2);
            }
        }

        /**
         * @param aLegacyData
         */
        @Override
        public void setFromInt(final int aLegacyData) {

        }

        /**
         *
         */
        @Override
        public void screwDriverClick() {

        }

        /**
         *
         */
        @Override
        public void screwDriverClickSneak() {

        }

        public int getSpeedFromRS(byte redstone) {
            redstone = (byte) (inverted ? 15 - redstone : redstone);
            if (!enabled || redstone <= 0 || redstone > 15) {
                return 0;
            }
            return fluidSettings[redstone % 16].getEffectiveSpeed();
        }

        public int getMinFlowRate() {
            int min = Integer.MAX_VALUE;
            for (val setting : fluidSettings) {
                min = Math.min(min, setting.getFlowRate());
            }
            return min;
        }

        public int getMaxFlowRate() {
            int max = Integer.MIN_VALUE;
            for (val setting : fluidSettings) {
                max = Math.max(max, setting.getFlowRate());
            }
            return max;
        }

        /**
         * @return
         */
        @Nonnull
        @Override
        public ISerializableObject copy() {
            val result = new InternalData(enabled, inverted);
            for (int i = 0; i < fluidSettings.length; i++) {
                result.fluidSettings[i] = fluidSettings[i].copy();
            }
            return result;
        }

        /**
         * @return
         */
        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            val compound = new NBTTagCompound();
            compound.setBoolean("enabled", enabled);
            compound.setBoolean("inverted", inverted);
            val fluidSettings = new NBTTagList();
            for (final FluidSetting setting : this.fluidSettings) {
                val fluidSetting = new NBTTagCompound();
                fluidSetting.setInteger("flowRate", setting.flowRate);
                fluidSetting.setInteger("flags", setting.getFlags());
                fluidSettings.appendTag(fluidSetting);
            }
            compound.setTag("fluidSettings", fluidSettings);
            return compound;
        }

        /**
         * Write data to given ByteBuf
         * The data saved this way is intended to be stored for short amount of time over network.
         * DO NOT store it to disks.
         *
         * @param aBuf
         */
        @Override
        public void writeToByteBuf(final ByteBuf aBuf) {
            aBuf.writeBoolean(enabled);
            aBuf.writeBoolean(inverted);
            for (final FluidSetting setting : fluidSettings) {
                aBuf.writeInt(setting.flowRate);
                aBuf.writeByte(setting.getFlags());
            }
        }

        /**
         * @param aNBT
         */
        @Override
        public void loadDataFromNBT(final NBTBase aNBT) {
            if (aNBT instanceof NBTTagCompound) {
                val compound = (NBTTagCompound) aNBT;
                enabled = compound.getBoolean("enabled");
                inverted = compound.getBoolean("inverted");
                val fluidSettings = compound.getTagList("fluidSettings", 10);
                for (int i = 0; i < fluidSettings.tagCount(); i++) {
                    val fluidSetting = fluidSettings.getCompoundTagAt(i);
                    val flowRate = fluidSetting.getInteger("flowRate");
                    val flags = fluidSetting.getInteger("flags");
                    this.fluidSettings[i] = new FluidSetting(flowRate);
                    this.fluidSettings[i].setFlags(flags);
                }
            }
        }

        /**
         * Read data from given parameter and return this.
         * The data read this way is intended to be stored for short amount of time over network.
         *
         * @param aBuf
         * @param aPlayer
         */
        @Nonnull
        @Override
        public ISerializableObject readFromPacket(final ByteArrayDataInput aBuf, final EntityPlayerMP aPlayer) {
            val result = new InternalData();
            result.enabled = aBuf.readBoolean();
            result.inverted = aBuf.readBoolean();
            for (int i = 0; i < result.fluidSettings.length; i++) {
                result.fluidSettings[i] = new FluidSetting(aBuf.readInt());
                result.fluidSettings[i].setFlags(aBuf.readByte());
            }
            return result;
        }

    }


    @Setter
    @Getter
    private class GUI extends GT_GUICover {

        @Getter
        @Setter
        private class RegulatorPoint implements GT_CanvasPanel.IDraggableElement {

            @Nonnull
            private final GT_CanvasPanel<GUI> currentCanvas;

            @Nonnull
            private final InternalData.FluidSetting fluidSetting;

            private final int index;

            private boolean canClip;

            private int renderX, renderY;

            private int elementWidth, elementHeight;

            private GT_GuiTooltip tooltip;

            private String[] tooltipText;

            @Getter
            @Accessors(chain = true)
            @Setter
            private IGT_GuiHook onPositionChangeBehavior, onInitBehavior, onUpdateBehavior, onClickBehavior;

            private int updateCooldown;

            public RegulatorPoint(final GT_CanvasPanel<GUI> currentCanvas, @Nonnull final InternalData.FluidSetting fluidSetting, final int index) {
                this.currentCanvas = currentCanvas;
                this.fluidSetting = fluidSetting;
                this.index = index;
                currentCanvas.addDraggableElement(this);
                setElementWidth(8);
                setElementHeight(8);
                setPosition();
            }

            private void setPosition() {
                setRenderX();
                setRenderY();
            }

            public void setRenderX() {
                val canvas = currentCanvas;
                val index = this.index;
                val slice = canvas.getContentWidth() / 17;
                val x = canvas.getContentLeft() + slice * index - elementWidth / 2;
                setRenderX(x);
            }

            public void setRenderY() {
                // TODO: logarithmic
                val maxFlowRate = data.getMaxFlowRate();
                val minFlowRate = data.getMinFlowRate();
                val range = maxFlowRate - minFlowRate;
                val pixelValue = Math.max(1, (range / (double) (currentCanvas.getContentHeight() - elementHeight)));
                val flowRate = fluidSetting.getFlowRate();
                val y = (int)(flowRate / pixelValue);
                setRenderY(y + elementHeight / 2);
            }

            /**
             *
             */
            @Override
            public void onInit() {

            }

            /**
             * @param mouseX
             * @param mouseY
             * @param parTicks
             */
            @Override
            public void draw(final int mouseX, final int mouseY, final float parTicks) {
                GUI.drawRect(getRenderX(), getRenderY(), getRenderX() + elementWidth, getRenderY() + elementHeight, 0xFF000000);
            }

            /**
             * @param text
             * @return
             */
            @Override
            public IGuiElement setTooltipText(final String... text) {
                if (tooltip == null) {
                    tooltip = new GT_GuiTooltip(getBounds(), text);
                } else {
                    tooltip.setToolTipText(text);
                }
                this.tooltipText = text;
                return this;
            }

            /**
             * @param mouseX
             * @param mouseY
             * @param clickType
             * @return
             */
            @Override
            public boolean inBounds(final int mouseX, final int mouseY, final int clickType) {
                return false;
            }

            /**
             * @return
             */
            @Override
            public Rectangle getBounds() {
                return null;
            }

            public GUI getGui() {
                return currentCanvas.getParent();
            }

            /**
             * @return
             */
            @Override
            public boolean canClip() {
                return false;
            }

            /**
             * @return
             */
            @Override
            public boolean isXUnlocked() {
                return false;
            }

            /**
             * @param unlocked
             */
            @Override
            public void setXUnlocked(final boolean unlocked) {

            }

            /**
             * @return
             */
            @Override
            public boolean isYUnlocked() {
                return true;
            }

            /**
             * @param unlocked
             */
            @Override
            public void setYUnlocked(final boolean unlocked) {

            }

            /**
             * @param mouseX
             * @param mouseY
             * @param lastClick
             */
            @Override
            public void receiveDrag(final int mouseX, final int mouseY, final int lastClick) {

            }

            /**
             * @return
             */
            @Override
            public Rectangle getMouseBounds() {
                val base = getBounds();
                if (base == null) {
                    return null;
                }
                val fuzzX = elementWidth / 3;
                val fuzzY = elementHeight / 3;
                return new Rectangle(base.x - fuzzX, base.y - fuzzY, base.width + fuzzX, base.height + fuzzY);
            }

            /**
             * @param mouseX
             * @param mouseY
             * @param mouseButton
             */
            @Override
            public void receiveClick(final int mouseX, final int mouseY, final int mouseButton) {

            }

            /**
             * @param mouseX
             * @param mouseY
             * @param clickState
             */
            @Override
            public void receiveMouseMovement(final int mouseX, final int mouseY, final int clickState) {

            }

        }


        private final byte side;

        private final int coverID;

        private final InternalData data;

        private final GT_CanvasPanel<GUI> graphPanel;

        private boolean logarithmic = false;

        public GUI(final byte aSide, final int aCoverID, final InternalData aData, final ICoverable tile) {
            super(tile, 338, 166, GT_Utility.intToStack(aCoverID));
            side = aSide;
            coverID = aCoverID;
            data = aData;
            graphPanel = new GT_CanvasPanel<>(this, 0, 8, 8, 250, 160, 248, 158);
        }

        /**
         * @param guiLeft
         * @param guiTop
         * @param gui_width
         * @param gui_height
         */
        @Override
        protected void onInitGui(final int guiLeft, final int guiTop, final int gui_width, final int gui_height) {
            updateButtons();
        }

        /**
         * @param button
         */
        @Override
        public void buttonClicked(final GuiButton button) {
            int id;
            if (getClickable((id = button.id))) {
                updateButtons();
            }
        }

        private void updateButtons() {
            GuiButton button;
            for (val o : buttonList) {
                button = (GuiButton) o;
                button.enabled = getClickable(button.id);
            }
        }

        private boolean getClickable(final int id) {
            return true;
        }

    }


    private final int maxTransferRate;

    private boolean allowFluid = true;

    public GT_Cover_AdvancedFluidRegulator(final int tier) {
        super(InternalData.class);
        this.maxTransferRate = 8 * (1 << (2 * tier));
    }

    /**
     * @param aLegacyData
     * @return
     */
    @Override
    public InternalData createDataObject(final int aLegacyData) {
        return new InternalData();
    }

    /**
     * @return
     */
    @Override
    public InternalData createDataObject() {
        return new InternalData();
    }

    /**
     * Called by updateEntity inside the covered TileEntity. aCoverVariable is the Value you returned last time.
     *
     * @param aSide
     * @param aInputRedstone
     * @param aCoverID
     * @param aCoverVariable
     * @param aTileEntity
     * @param aTimer
     */
    @Override
    protected InternalData doCoverThingsImpl(final byte aSide, final byte aInputRedstone, final int aCoverID, final InternalData aCoverVariable, final ICoverable aTileEntity, final long aTimer) {
        if (aCoverVariable == null || !aCoverVariable.enabled) {
            return aCoverVariable;
        }
        val speed = aCoverVariable.getSpeedFromRS(aInputRedstone);
        if (speed == 0) {
            return aCoverVariable;
        }
        val isOutputting = speed > 0;
        if (aTileEntity instanceof IFluidHandler) {
            IFluidHandler sender, receiver;
            ForgeDirection from, to;
            if (isOutputting) {
                sender = (IFluidHandler) aTileEntity;
                receiver = aTileEntity.getITankContainerAtSide(aSide);
                from = ForgeDirection.getOrientation(aSide);
            } else {
                sender = aTileEntity.getITankContainerAtSide(aSide);
                receiver = (IFluidHandler) aTileEntity;
                from = ForgeDirection.getOrientation(aSide).getOpposite();
            }
            to = from.getOpposite();
            if (sender != null && receiver != null) {
                allowFluid = true;
                FluidStack targetFluid = sender.drain(from, Math.abs(speed), false);
                if (targetFluid != null) {
                    targetFluid = targetFluid.copy();
                    targetFluid.amount = receiver.fill(to, targetFluid, false);
                    if (targetFluid.amount > 0) {
                        sender.fill(to, receiver.drain(from, targetFluid.amount, true), true);
                    }
                }
                allowFluid = false;
            }
        }
        return aCoverVariable;
    }

    /**
     * @param aSide
     * @param aCoverID
     * @param aCoverVariable
     * @param aTileEntity
     * @param aPlayer
     * @param aWorld
     * @return
     */
    @Override
    protected Object getClientGUIImpl(final byte aSide, final int aCoverID, final InternalData aCoverVariable, final ICoverable aTileEntity, final EntityPlayer aPlayer, final World aWorld) {
        return new GUI(aSide, aCoverID, aCoverVariable, aTileEntity);
    }

    /**
     * If it lets RS-Signals into the Block
     * <p/>
     * This is just Informative so that Machines know if their Redstone Input is blocked or not
     *
     * @param aSide
     * @param aCoverID
     * @param aCoverVariable
     * @param aTileEntity
     */
    @Override
    protected boolean letsRedstoneGoInImpl(final byte aSide, final int aCoverID, final InternalData aCoverVariable, final ICoverable aTileEntity) {
        return true;
    }

    /**
     * If it lets RS-Signals out of the Block
     *
     * @param aSide
     * @param aCoverID
     * @param aCoverVariable
     * @param aTileEntity
     */
    @Override
    protected boolean letsRedstoneGoOutImpl(final byte aSide, final int aCoverID, final InternalData aCoverVariable, final ICoverable aTileEntity) {
        return true;
    }

    /**
     * If it lets Energy into the Block
     *
     * @param aSide
     * @param aCoverID
     * @param aCoverVariable
     * @param aTileEntity
     */
    @Override
    protected boolean letsEnergyInImpl(final byte aSide, final int aCoverID, final InternalData aCoverVariable, final ICoverable aTileEntity) {
        return true;
    }

    /**
     * If it lets Energy out of the Block
     *
     * @param aSide
     * @param aCoverID
     * @param aCoverVariable
     * @param aTileEntity
     */
    @Override
    protected boolean letsEnergyOutImpl(final byte aSide, final int aCoverID, final InternalData aCoverVariable, final ICoverable aTileEntity) {
        return true;
    }

    /**
     * If it lets Liquids into the Block, aFluid can be null meaning if this is generally allowing Fluids or not.
     *
     * @param aSide
     * @param aCoverID
     * @param aCoverVariable
     * @param aFluid
     * @param aTileEntity
     */
    @Override
    protected boolean letsFluidInImpl(final byte aSide, final int aCoverID, final InternalData aCoverVariable, final Fluid aFluid, final ICoverable aTileEntity) {
        return allowFluid;
    }

    /**
     * If it lets Liquids out of the Block, aFluid can be null meaning if this is generally allowing Fluids or not.
     *
     * @param aSide
     * @param aCoverID
     * @param aCoverVariable
     * @param aFluid
     * @param aTileEntity
     */
    @Override
    protected boolean letsFluidOutImpl(final byte aSide, final int aCoverID, final InternalData aCoverVariable, final Fluid aFluid, final ICoverable aTileEntity) {
        return allowFluid;
    }

    /**
     * If it lets Items into the Block, aSlot = -1 means if it is generally accepting Items (return false for no Interaction at all), aSlot = -2 means if it would accept for all Slots (return true to skip the Checks for each Slot).
     *
     * @param aSide
     * @param aCoverID
     * @param aCoverVariable
     * @param aSlot
     * @param aTileEntity
     */
    @Override
    protected boolean letsItemsInImpl(final byte aSide, final int aCoverID, final InternalData aCoverVariable, final int aSlot, final ICoverable aTileEntity) {
        return true;
    }

    /**
     * If it lets Items out of the Block, aSlot = -1 means if it is generally accepting Items (return false for no Interaction at all), aSlot = -2 means if it would accept for all Slots (return true to skip the Checks for each Slot).
     *
     * @param aSide
     * @param aCoverID
     * @param aCoverVariable
     * @param aSlot
     * @param aTileEntity
     */
    @Override
    protected boolean letsItemsOutImpl(final byte aSide, final int aCoverID, final InternalData aCoverVariable, final int aSlot, final ICoverable aTileEntity) {
        return true;
    }

    /**
     * if this Cover should let Pipe Connections look connected even if it is not the case.
     *
     * @param aSide
     * @param aCoverID
     * @param aCoverVariable
     * @param aTileEntity
     */
    @Override
    protected boolean alwaysLookConnectedImpl(final byte aSide, final int aCoverID, final InternalData aCoverVariable, final ICoverable aTileEntity) {
        return true;
    }

    /**
     * Gets the Tick Rate for doCoverThings of the Cover
     * <p/>
     * 0 = No Ticks! Yes, 0 is Default, you have to override this
     *
     * @param aSide
     * @param aCoverID
     * @param aCoverVariable
     * @param aTileEntity
     */
    @Override
    protected int getTickRateImpl(final byte aSide, final int aCoverID, final InternalData aCoverVariable, final ICoverable aTileEntity) {
        // TODO: Wtf is this
        return 1;
    }

    /**
     * @return
     */
    @Override
    public boolean hasCoverGUI() {
        return true;
    }

}
