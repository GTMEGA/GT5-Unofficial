package gregtech.common.gui.dev;


import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.RSControlMode;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.gui.GT_Slot_Output;
import gregtech.api.interfaces.IDWSCompatible;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.dev.GT_MetaTileEntity_DevItemSource;
import gregtech.api.net.GT_Packet_TileEntityGUI;
import gregtech.api.util.GT_Utility;
import lombok.Getter;
import lombok.val;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


@Getter
public class GT_Container_DevItemSource extends GT_ContainerMetaTile_Machine implements IDWSCompatible {

    private final boolean serverSide;

    private GT_MetaTileEntity_DevItemSource.GUIData data;

    private RSControlMode redstoneMode;

    private int itemPerTick, itemPerSecond;

    private boolean perTick, active, rsActive;

    public GT_Container_DevItemSource(
            final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity
                                     ) {
        super(aInventoryPlayer, aTileEntity);
        if (aTileEntity instanceof GT_MetaTileEntity_DevItemSource) {
            data = (GT_MetaTileEntity_DevItemSource.GUIData) getSource().getTEGUIData();
            /*redstoneMode = getSource().getMode();
            itemPerTick = getSource().getItemPerTick();
            itemPerSecond = getSource().getItemPerSecond();
            perTick = getSource().isPerTick();
            active = getSource().isActive();
            rsActive = getSource().isRsActive();*/
        }
        serverSide = aTileEntity.isServerSide();
        detectAndSendChanges();
    }

    public void sendPacket() {
        final int dimension = mPlayerInventory.player.dimension;
        /*final GT_MetaTileEntity_DevItemSource.GUIData data = new GT_MetaTileEntity_DevItemSource.GUIData(
                redstoneMode, itemPerTick, itemPerSecond, perTick, active);*/
        GT_Values.NW.sendToServer(GT_Packet_TileEntityGUI.createFromMachine(getSource(), data, dimension));
    }

    /**
     * @return
     */
    @Override
    public int getDWSWidthBump() {
        return 82;
    }

    /**
     * @return
     */
    @Override
    public int baseWidth() {
        return 256;
    }

    /**
     * To add the Slots to your GUI
     *
     * @param aPlayerInventory
     */
    @Override
    public void addSlots(final InventoryPlayer aPlayerInventory) {
        addSlotToContainer(new GT_Slot_Output(this.mTileEntity, 0, slotX(), 54));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 1, slotX(), 24, false, false, 1));
    }

    public int slotX() {
        return 40;
    }

    /**
     * Amount of regular Slots in the GUI (so, non-HoloSlots)
     */
    @Override
    public int getSlotCount() {
        return 2;
    }

    public boolean toggleActive() {
        active = !active;
        detectAndSendChanges();
        return active;
    }

    /**
     * @return
     */
    @Override
    public int getShiftClickStartIndex() {
        return 0;
    }

    /**
     * Amount of Slots in the GUI the player can Shift-Click into. Uses also getSlotStartIndex
     */
    @Override
    public int getShiftClickSlotCount() {
        return 2;
    }

    /**
     * @return
     */
    @Override
    protected int getGuiWidth() {
        return applyDWSBump(baseWidth());
    }

    /**
     * @param aSlotIndex
     * @param aMouseclick
     * @param aShifthold
     * @param aPlayer
     * @return
     */
    @Override
    public ItemStack slotClick(final int aSlotIndex, final int aMouseclick, final int aShifthold, final EntityPlayer aPlayer) {
        if (aSlotIndex < 1) {
            return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        }
        val slotClicked = (Slot) inventorySlots.get(aSlotIndex);
        if (slotClicked != null) {
            if (!(mTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_DevItemSource)) {
                return null;
            }
            if (aSlotIndex == 1) {
                val currentStack = aPlayer.inventory.getItemStack();
                ((GT_MetaTileEntity_DevItemSource) mTileEntity.getMetaTileEntity()).clearItem();
                if (currentStack != null) {
                    ((GT_MetaTileEntity_DevItemSource) mTileEntity.getMetaTileEntity()).setStored(currentStack);
                    slotClicked.putStack(GT_Utility.copyAmount(1, currentStack.copy()));
                } else {
                    slotClicked.putStack(null);
                }
            }
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    protected void setRedstoneMode(final RSControlMode redstoneMode) {
        this.redstoneMode = redstoneMode;
        detectAndSendChanges();
    }

    /**
     *
     */
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) {
            return;
        }
        for (Object o : crafters) {
            ICrafting crafter = (ICrafting) o;
            crafter.sendProgressBarUpdate(this, 200, getSource().getRedstoneMode().ordinal());
            crafter.sendProgressBarUpdate(this, 201, getSource().getItemPerTick());
            crafter.sendProgressBarUpdate(this, 202, getSource().getItemPerSecond());
            crafter.sendProgressBarUpdate(this, 203, getSource().isPerTick() ? 1 : 0);
            crafter.sendProgressBarUpdate(this, 204, getSource().isActive() ? 1 : 0);
            crafter.sendProgressBarUpdate(this, 205, getSource().isRsActive() ? 1 : 0);

        }
    }

    public GT_MetaTileEntity_DevItemSource getSource() {
        return (GT_MetaTileEntity_DevItemSource) mTileEntity.getMetaTileEntity();
    }

    /**
     * @param par1
     * @param par2
     */
    @Override
    public void updateProgressBar(final int par1, final int par2) {
        super.updateProgressBar(par1, par2);
        if (mTileEntity.isServerSide()) {
            return;
        }
        switch (par1) {
            case 200: {
                data.setRedstoneMode(RSControlMode.getMode(par2));
                // redstoneMode = RSControlMode.getMode(par2);
                break;
            }
            case 201: {
                data.setItemPerTick(par2);
                // itemPerTick = par2;
                break;
            }
            case 202: {
                data.setItemPerSecond(par2);
                // itemPerSecond = par2;
                break;
            }
            case 203: {
                data.setPerTick(par2 != 0);
                // perTick = par2 != 0;
                break;
            }
            case 204: {
                data.setActive(par2 != 0);
                // active = par2 != 0;
                break;
            }
            case 205: {
                data.setRsActive(par2 != 0);
                // rsActive = par2 != 0;
                break;
            }
        }
    }

    protected void setItemPerTick(final int itemPerTick) {
        this.itemPerTick = itemPerTick;
        detectAndSendChanges();
    }

    protected void setItemPerSecond(final int itemPerSecond) {
        this.itemPerSecond = itemPerSecond;
        detectAndSendChanges();
    }

    protected void setPerTick(final boolean perTick) {
        this.perTick = perTick;
        detectAndSendChanges();
    }

    protected void syncRates() {
        if (perTick) {
            setItemPerSecond(itemPerTick * 20);
        } else {
            setItemPerTick(itemPerSecond / 20);
        }
        detectAndSendChanges();
    }

    protected void setActive(final boolean active) {
        this.active = active;
        detectAndSendChanges();
    }

    protected void zeroOut() {
        setItemPerTick(0);
        setItemPerSecond(0);
        setPerTick(true);
        detectAndSendChanges();
    }

    protected boolean canRun() {
        return active && rsActive;
    }

    protected String getDisabledStatus() {
        if (!isActive()) {
            return "Disabled by User";
        }
        if (!isRsActive()) {
            return "Disabled by Redstone";
        }
        return "";
    }

}
