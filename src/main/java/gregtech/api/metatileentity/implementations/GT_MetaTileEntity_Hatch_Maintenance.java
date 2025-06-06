package gregtech.api.metatileentity.implementations;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.GT_Container_1by1;
import gregtech.api.gui.GT_Container_2by2;
import gregtech.api.gui.GT_Container_MaintenanceHatch;
import gregtech.api.gui.GT_GUIContainer_1by1;
import gregtech.api.gui.GT_GUIContainer_2by2;
import gregtech.api.gui.GT_GUIContainer_MaintenanceHatch;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.CachedForgeItem;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_AUTOMAINTENANCE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_AUTOMAINTENANCE_IDLE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DUCTTAPE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_MAINTENANCE;

public class GT_MetaTileEntity_Hatch_Maintenance extends GT_MetaTileEntity_Hatch {
    CachedForgeItem maintanceKit = new CachedForgeItem("htx","item.maintenance_kit",0,1);
    int baseTier = 0;
    public boolean mWrench = false, mScrewdriver = false, mSoftHammer = false, mHardHammer = false, mSolderingTool = false, mCrowbar = false, mAuto;

    public GT_MetaTileEntity_Hatch_Maintenance(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, "For maintaining Multiblocks manually.");
        mAuto = false;
    }

    public GT_MetaTileEntity_Hatch_Maintenance(int aID, String aName, String aNameRegional, int aTier, boolean aAuto) {
        super(aID, aName, aNameRegional, aTier, 4, "For automatically maintaining Multiblocks.");
        mAuto = aAuto;
    }

    public GT_MetaTileEntity_Hatch_Maintenance(String aName, int aTier, String aDescription, ITexture[][][] aTextures, boolean aAuto) {
        super(aName, aTier, aAuto ? 4 : 1, aDescription, aTextures);
        mAuto = aAuto;
    }

    public GT_MetaTileEntity_Hatch_Maintenance(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, boolean aAuto) {
        super(aName, aTier, aAuto ? 4 : 1, aDescription, aTextures);
        mAuto = aAuto;
    }

    @Override
    public String[] getDescription() {
        if (mAuto) {
            String[] desc = new String[mDescriptionArray.length + 3];
            System.arraycopy(mDescriptionArray, 0, desc, 0, mDescriptionArray.length);
            desc[mDescriptionArray.length] = "Consumes 1 Maintenance Kit per repair.";
            return desc;
        } else {
            String[] desc = new String[mDescriptionArray.length + 1];
            System.arraycopy(mDescriptionArray, 0, desc, 0, mDescriptionArray.length);
            desc[mDescriptionArray.length] = "Cannot be shared between machines.";
            return desc;
        }
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        if (mAuto) return new ITexture[]{
                aBaseTexture,
                TextureFactory.of(OVERLAY_AUTOMAINTENANCE_IDLE)};
        return new ITexture[]{
                aBaseTexture,
                TextureFactory.of(OVERLAY_MAINTENANCE)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        if (mAuto) return new ITexture[]{
                aBaseTexture,
                TextureFactory.of(OVERLAY_AUTOMAINTENANCE)};
        return new ITexture[]{
                aBaseTexture,
                TextureFactory.of(OVERLAY_MAINTENANCE),
                TextureFactory.of(OVERLAY_DUCTTAPE)};
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        getBaseMetaTileEntity().setActive(true);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return mAuto && GT_Mod.gregtechproxy.mAMHInteraction;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        if (aTileEntity.getMetaTileID() == 111)
            return new GT_MetaTileEntity_Hatch_Maintenance(mName, mTier, mDescriptionArray, mTextures, true);
        return new GT_MetaTileEntity_Hatch_Maintenance(mName, mTier, mDescriptionArray, mTextures, false);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        if (aSide == aBaseMetaTileEntity.getFrontFacing()) aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        if (mAuto) return new GT_Container_1by1(aPlayerInventory, aBaseMetaTileEntity);
        return new GT_Container_MaintenanceHatch(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        if (mAuto) return new GT_GUIContainer_1by1(aPlayerInventory, aBaseMetaTileEntity, getLocalName());
        return new GT_GUIContainer_MaintenanceHatch(aPlayerInventory, aBaseMetaTileEntity);
    }

    public void updateSlots() {
        for (int i = 0; i < mInventory.length; i++)
            if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
    }


    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if(aBaseMetaTileEntity.isServerSide() && mAuto && aTick % 100L ==0L){
            aBaseMetaTileEntity.setActive(!isRecipeInputEqual(false));
        }
    }

    public boolean autoMaintainance(){
        return isRecipeInputEqual(true);
    }

    public boolean isRecipeInputEqual(boolean aDecreaseStacksizeBySuccess) {
        ItemStack[] mInputs= {maintanceKit.getItemNoCopy()};

        int amt;

        for (ItemStack tStack : mInputs) {
            if (tStack != null) {
                amt = tStack.stackSize;
                boolean temp = true;
                for (ItemStack aStack : mInventory) {
                    if ((GT_Utility.areUnificationsEqual(aStack, tStack, true) || GT_Utility.areUnificationsEqual(GT_OreDictUnificator.get(false, aStack), tStack, true))) {
                        amt -= aStack.stackSize;
                        if (amt < 1) {
                            temp = false;
                            break;
                        }
                    }
                }
                if (temp) return false;
            }
        }

        if (aDecreaseStacksizeBySuccess) {
            for (ItemStack tStack : mInputs) {
                if (tStack != null) {
                    amt = tStack.stackSize;
                    for (ItemStack aStack : mInventory) {
                        if ((GT_Utility.areUnificationsEqual(aStack, tStack, true) || GT_Utility.areUnificationsEqual(GT_OreDictUnificator.get(false, aStack), tStack, true))) {
                            if (aStack.stackSize < amt) {
                                amt -= aStack.stackSize;
                                aStack.stackSize = 0;
                            } else {
                                aStack.stackSize -= amt;
                                amt = 0;
                                break;
                            }
                        }
                    }
                }
            }
            mCrowbar = true;
            mHardHammer = true;
            mScrewdriver = true;
            mSoftHammer = true;
            mSolderingTool = true;
            mWrench = true;
            updateSlots();
        }
        return true;
    }

    public void onToolClick(ItemStack aStack, EntityLivingBase aPlayer) {
        if (aStack == null || aPlayer == null) return;
        if (GT_Utility.isStackInList(aStack, GregTech_API.sWrenchList) && GT_ModHandler.damageOrDechargeItem(aStack, 100, 1000, aPlayer))
            mWrench = true;
        if (GT_Utility.isStackInList(aStack, GregTech_API.sScrewdriverList) && GT_ModHandler.damageOrDechargeItem(aStack, 100, 1000, aPlayer))
            mScrewdriver = true;
        if (GT_Utility.isStackInList(aStack, GregTech_API.sSoftHammerList) && GT_ModHandler.damageOrDechargeItem(aStack, 100, 1000, aPlayer))
            mSoftHammer = true;
        if (GT_Utility.isStackInList(aStack, GregTech_API.sHardHammerList) && GT_ModHandler.damageOrDechargeItem(aStack, 50, 1000, aPlayer))
            mHardHammer = true;
        if (GT_Utility.isStackInList(aStack, GregTech_API.sCrowbarList) && GT_ModHandler.damageOrDechargeItem(aStack, 50, 1000, aPlayer))
            mCrowbar = true;
        if (GT_ModHandler.useSolderingIron(aStack, aPlayer)) mSolderingTool = true;

        if (GT_OreDictUnificator.isItemStackInstanceOf(aStack, "item.maintenance_kit")) {
            mWrench = mScrewdriver = mSoftHammer = mHardHammer = mCrowbar = mSolderingTool = true;
            getBaseMetaTileEntity().setActive(false);
            aStack.stackSize--;
        }
        if (mSolderingTool && aPlayer instanceof EntityPlayerMP) {
            EntityPlayerMP tPlayer = (EntityPlayerMP) aPlayer;
            try {
                GT_Mod.achievements.issueAchievement(tPlayer, "maintainance");
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return mAuto && GT_Mod.gregtechproxy.mAMHInteraction;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return mAuto && GT_Mod.gregtechproxy.mAMHInteraction;
    }
}
