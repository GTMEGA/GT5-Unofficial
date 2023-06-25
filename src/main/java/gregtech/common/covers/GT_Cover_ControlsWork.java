package gregtech.common.covers;

import com.google.common.io.ByteArrayDataInput;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconButton;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.net.GT_Packet_TileEntityCoverNew;
import gregtech.api.util.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;

public class GT_Cover_ControlsWork extends GT_CoverBehaviorBase<GT_Cover_ControlsWork.InternalData> {

    public static class InternalData implements INewCoverData {

        private static final String STATE_FIELD = "state", SAFE_FIELD = "safe";

        private byte state;
        private boolean safe;

        public InternalData() {
            this((byte) 0, false);
        }

        public InternalData(int aLegacyData) {
            setFromInt(aLegacyData);
        }

        public InternalData(byte state, boolean safe) {
            setState(state);
            setSafe(safe);
        }

        public static InternalData getSafeDisabled() {
            return new InternalData((byte) 2, true);
        }

        public void setFromInt(int aLegacyData) {
            boolean safe = aLegacyData > 2;
            setState(safe ? aLegacyData - 3 : aLegacyData);
            setSafe(safe);
        }

        @Override
        public void screwDriverClick() {
            if (getState() < 2) {
                setState(getState() + 1);
            } else {
                setState(0);
                toggleSafe();
            }
        }

        @Override
        public void screwDriverClickSneak() {
            if (getState() > 0) {
                setState(getState() - 1);
            } else {
                setState(2);
                toggleSafe();
            }
        }

        public boolean toggleSafe() {
            safe = !safe;
            return safe;
        }

        public boolean isRSHigh() {
            return getState() == 0;
        }

        public boolean isDisabled() {
            return getState() == 2;
        }

        public byte getState() {
            return state;
        }

        public void setState(byte state) {
            if (state > 2) {
                this.state = 0;
            } else {
                this.state = state;
            }
        }

        public void setState(int state) {
            setState((byte) state);
        }

        public boolean isSafe() {
            return safe;
        }

        public void setSafe(boolean safe) {
            this.safe = safe;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new InternalData(state, safe);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound result = new NBTTagCompound();
            result.setByte(InternalData.STATE_FIELD, getState());
            result.setBoolean(InternalData.SAFE_FIELD, isSafe());
            return result;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeByte(state);
            aBuf.writeBoolean(safe);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            if (aNBT instanceof NBTBase.NBTPrimitive) {
                setFromInt(((NBTBase.NBTPrimitive) aNBT).func_150287_d());
            } else {
                NBTTagCompound compound = (NBTTagCompound) aNBT;
                if (compound.hasKey(STATE_FIELD, 1)) {
                    setState(compound.getByte(STATE_FIELD));
                } else {
                    setState(0);
                }
                if (compound.hasKey(SAFE_FIELD, 1)) {
                    setSafe(compound.getBoolean(SAFE_FIELD));
                } else {
                    setSafe(false);
                }
            }
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            setState(aBuf.readByte());
            setSafe(aBuf.readBoolean());
            return this;
        }
    }

    private class GUI extends GT_GUICover {
        private final byte side;
        private final int coverID;
        private final InternalData coverVariable;

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public GUI(byte aSide, int aCoverID, InternalData aCoverVariable, ICoverable aTileEntity) {
            super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
            this.side = aSide;
            this.coverID = aCoverID;
            this.coverVariable = aCoverVariable;

            new GT_GuiIconButton(this, 0, startX + spaceX * 0, startY + spaceY * 0, GT_GuiIcon.REDSTONE_ON);
            new GT_GuiIconButton(this, 1, startX + spaceX * 0, startY + spaceY * 1, GT_GuiIcon.REDSTONE_OFF);
            new GT_GuiIconButton(this, 2, startX + spaceX * 0, startY + spaceY * 2, GT_GuiIcon.CROSS);

            new GT_GuiIconCheckButton(this, 3, startX + spaceX * 0, startY + spaceY * 3, GT_GuiIcon.CHECKMARK, GT_GuiIcon.CROSS).setChecked(aCoverVariable.isSafe());
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            this.fontRendererObj.drawString(trans("243", "Enable with Redstone"), 3 + startX + spaceX * 1, 4 + startY + spaceY * 0, 0xFF555555);
            this.fontRendererObj.drawString(trans("244", "Disable with Redstone"), 3 + startX + spaceX * 1, 4 + startY + spaceY * 1, 0xFF555555);
            this.fontRendererObj.drawString(trans("245", "Disable machine"), 3 + startX + spaceX * 1, 4 + startY + spaceY * 2, 0xFF555555);
            this.fontRendererObj.drawString(trans("507", "Safe Mode"), 3 + startX + spaceX * 1, 4 + startY + spaceY * 3, 0xFF555555);
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            updateButtons();
        }

        @Override
        public void buttonClicked(GuiButton btn) {
            if (getClickable(btn.id)) {
                int bID = btn.id;
                if (bID == 3) {
                    ((GT_GuiIconCheckButton) btn).toggle();
                } else {
                    coverVariable.setState((byte) bID);
                }
                adjustCoverVariable();
                GT_Values.NW.sendToServer(new GT_Packet_TileEntityCoverNew(side, coverID, coverVariable, tile));
            }
            updateButtons();
        }

        private void updateButtons() {
            GuiButton b;
            for (Object o : buttonList) {
                b = (GuiButton) o;
                b.enabled = getClickable(b.id);
            }
        }

        private boolean getClickable(int id) {
            return (id != coverVariable.state || id == 3);
        }

        private void adjustCoverVariable() {
            boolean safeMode = ((GT_GuiIconCheckButton) buttonList.get(3)).isChecked();
            if (safeMode && !coverVariable.isSafe()) {
                coverVariable.setSafe(true);
            }
            if (!safeMode && coverVariable.isSafe()) {
                coverVariable.setSafe(false);
            }
            if (tile instanceof IGregTechTileEntity && !coverVariable.isDisabled()) {
                IGregTechTileEntity base = (IGregTechTileEntity) tile;
                base.enableWorking();
            }
            setLastPlayer(mc.thePlayer);
        }

    }

    public GT_Cover_ControlsWork() {
        super(InternalData.class);
    }

    @Override
    public InternalData createDataObject(int aLegacyData) {
        return new InternalData(aLegacyData);
    }

    @Override
    public InternalData createDataObject() {
        return new InternalData();
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(byte aSide, int aCoverID, InternalData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return super.isRedstoneSensitiveImpl(aSide, aCoverID, aCoverVariable, aTileEntity, aTimer);
    }

    @Override
    protected InternalData doCoverThingsImpl(byte aSide, byte aInputRedstone, int aCoverID, InternalData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        if (aTileEntity instanceof IMachineProgress) {
            boolean redstoneActive;
            if (aInputRedstone > 0) {
                redstoneActive = aCoverVariable.isRSHigh();
            } else {
                redstoneActive = !aCoverVariable.isRSHigh();
            }
            IMachineProgress machine = (IMachineProgress) aTileEntity;
            if (!aCoverVariable.isSafe()) {
                if (redstoneActive) {
                    if (!machine.isAllowedToWork()) {
                        machine.enableWorking();
                    }
                } else if (machine.isAllowedToWork()) {
                    machine.disableWorking();
                }
                machine.setWorkDataValue(aInputRedstone);
            } else if (aCoverVariable.isDisabled()) {
                if (machine.isAllowedToWork()) {
                    machine.disableWorking();
                }
            } else {
                if (redstoneActive && machine.wasShutdown()) {
                    if (!machine.wasNotified()) {
                        String machineName = "gt.blockmachines." + aTileEntity.getInventoryName() + ".name";
                        machineName = GT_LanguageManager.getTranslation(machineName);
                        GT_Utility.sendChatToPlayer(getLastPlayer(), machineName + " at " + String.format("(%d, %d, %d)", aTileEntity.getXCoord(), aTileEntity.getYCoord(), aTileEntity.getZCoord()) + " shut down.");
                        if (aTileEntity instanceof GT_MetaTileEntity_MultiBlockBase) {
                            GT_MetaTileEntity_MultiBlockBase base = (GT_MetaTileEntity_MultiBlockBase) aTileEntity;
                            base.getBaseMetaTileEntity().setNotificationStatus(true);
                            base.getBaseMetaTileEntity().setShutdownStatus(false);
                        }
                        return InternalData.getSafeDisabled();
                    }
                } else {
                    InternalData modified = (InternalData) aCoverVariable.copy();
                    modified.setSafe(false);
                    modified = doCoverThingsImpl(aSide, aInputRedstone, aCoverID, modified, aTileEntity, aTimer);
                    modified.setSafe(true);
                    return modified;
                }
            }
        }
        return aCoverVariable;
    }

    @Override
    protected InternalData onCoverScrewdriverClickImpl(byte aSide, int aCoverID, InternalData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        setLastPlayer(aPlayer);
        if (!aPlayer.isSneaking()) {
            aCoverVariable.screwDriverClick();
        } else {
            aCoverVariable.screwDriverClickSneak();
        }
        if (!aCoverVariable.isSafe()) {
            switch (aCoverVariable.getState()) {
                case 0: {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("003", "Enable with Signal"));
                    break;
                }
                case 1: {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("004", "Disable with Signal"));
                    break;
                }
                case 2: {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("005", "Disabled"));
                } default: {
                    break;
                }
            }
        } else {
            switch (aCoverVariable.getState()) {
                case 0: {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("505", "Enable with Signal (Safe)"));
                    break;
                }
                case 1: {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("506", "Disable with Signal (Safe)"));
                    break;
                }
                case 2: {
                    GT_Utility.sendChatToPlayer(aPlayer, trans("507", "Disabled (Safe)"));
                } default: {
                    break;
                }
            }
        }
        if (aTileEntity instanceof IGregTechTileEntity && !aCoverVariable.isDisabled()) {
            IGregTechTileEntity base = (IGregTechTileEntity) aTileEntity;
            base.enableWorking();
        }
        return aCoverVariable;
    }

    @Override
    protected Object getClientGUIImpl(byte aSide, int aCoverID, InternalData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, World aWorld) {
        return new GT_Cover_ControlsWork.GUI(aSide, aCoverID, aCoverVariable, aTileEntity);
    }

    @Override
    protected boolean onCoverRemovalImpl(byte aSide, int aCoverID, InternalData aCoverVariable, ICoverable aTileEntity, boolean aForced) {
        if ((aTileEntity) instanceof IMachineProgress) {
            IMachineProgress bTileEntity = (IMachineProgress) aTileEntity;
            bTileEntity.enableWorking();
            bTileEntity.setWorkDataValue((byte) 0);
        }
        return true;
    }

    @Override
    protected boolean letsEnergyInImpl(byte aSide, int aCoverID, InternalData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyOutImpl(byte aSide, int aCoverID, InternalData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidInImpl(byte aSide, int aCoverID, InternalData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidOutImpl(byte aSide, int aCoverID, InternalData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsInImpl(byte aSide, int aCoverID, InternalData aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsOutImpl(byte aSide, int aCoverID, InternalData aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    protected int getTickRateImpl(byte aSide, int aCoverID, InternalData aCoverVariable, ICoverable aTileEntity) {
        return 1;
    }
}
