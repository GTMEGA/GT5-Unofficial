package gregtech.common.tileentities.automation;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Buffer;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.GT_Container_Regulator;
import gregtech.common.gui.GT_GUIContainer_Regulator;
import lombok.val;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collections;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_REGULATOR;

public class GT_MetaTileEntity_Regulator
        extends GT_MetaTileEntity_Buffer {
    public int[] mTargetSlots = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private boolean charge = false, decharge = false;

    public GT_MetaTileEntity_Regulator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 20, new String[]{
                "Filters up to 9 different Items",
                "Allows Item-specific output stack size",
                "Allows Item-specific output slot",
                "Does not consume energy to move Item"});
    }

    public GT_MetaTileEntity_Regulator(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Regulator(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Regulator(this.mName, this.mTier, this.mInventory.length, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public ITexture getOverlayIcon() {
        return TextureFactory.of(AUTOMATION_REGULATOR);
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex < 9;
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_Regulator(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_Regulator(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mTargetSlot1", this.mTargetSlots[0]);
        aNBT.setInteger("mTargetSlot2", this.mTargetSlots[1]);
        aNBT.setInteger("mTargetSlot3", this.mTargetSlots[2]);
        aNBT.setInteger("mTargetSlot4", this.mTargetSlots[3]);
        aNBT.setInteger("mTargetSlot5", this.mTargetSlots[4]);
        aNBT.setInteger("mTargetSlot6", this.mTargetSlots[5]);
        aNBT.setInteger("mTargetSlot7", this.mTargetSlots[6]);
        aNBT.setInteger("mTargetSlot8", this.mTargetSlots[7]);
        aNBT.setInteger("mTargetSlot9", this.mTargetSlots[8]);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mTargetSlots[0] = aNBT.getInteger("mTargetSlot1");
        this.mTargetSlots[1] = aNBT.getInteger("mTargetSlot2");
        this.mTargetSlots[2] = aNBT.getInteger("mTargetSlot3");
        this.mTargetSlots[3] = aNBT.getInteger("mTargetSlot4");
        this.mTargetSlots[4] = aNBT.getInteger("mTargetSlot5");
        this.mTargetSlots[5] = aNBT.getInteger("mTargetSlot6");
        this.mTargetSlots[6] = aNBT.getInteger("mTargetSlot7");
        this.mTargetSlots[7] = aNBT.getInteger("mTargetSlot8");
        this.mTargetSlots[8] = aNBT.getInteger("mTargetSlot9");
    }

    @Override
     public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        //Regulation per Screwdriver is overridden by GUI regulation.
    }

    @Override
    public void moveItems(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        val baseMetaTileEntity = getBaseMetaTileEntity();
        val backFacing = baseMetaTileEntity.getBackFacing();
        val otherTE = baseMetaTileEntity.getTileEntityAtSide(backFacing);
        for (int i = 0, tCosts; i < 9; i++) {
            if (this.mInventory[(i + 9)] != null) {
                val filterList = Collections.singletonList(this.mInventory[(i + 9)]);
                val targetSize = (byte) this.mInventory[(i + 9)].stackSize;
                tCosts = GT_Utility.moveOneItemStackIntoSlot(baseMetaTileEntity, otherTE, backFacing, this.mTargetSlots[i], filterList, false, targetSize, targetSize, (byte) 64, (byte) 1) * 3;
                if (tCosts > 0) {
                    this.mSuccess = 50;
                    break;
                }
            }
        }
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return (super.allowPutStack(aBaseMetaTileEntity, aIndex, aSide, aStack)) && (GT_Utility.areStacksEqual(aStack, this.mInventory[(aIndex + 9)]));
    }

    @Override
    public int rechargerSlotStartIndex() {
        return 19;
    }

    @Override
    public int dechargerSlotStartIndex() {
        return 19;
    }

    @Override
    public int rechargerSlotCount() {
        return charge ? 1 : 0;
    }

    @Override
    public int dechargerSlotCount() {
        return decharge ? 1 : 0;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            charge = aBaseMetaTileEntity.getStoredEU() / 2 > aBaseMetaTileEntity.getEUCapacity() / 3;
            decharge = aBaseMetaTileEntity.getStoredEU() < aBaseMetaTileEntity.getEUCapacity() / 3;
        }
    }
}
