package gregtech.common.tileentities.automation;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Buffer;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_NBT_Util;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.GT_Container_ItemDistributor;
import gregtech.common.gui.GT_GUIContainer_ItemDistributor;
import lombok.val;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Arrays;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_ITEMDISTRIBUTOR;

public class GT_MetaTileEntity_ItemDistributor extends GT_MetaTileEntity_Buffer {

    public static final int[][] INVERSE_ROTATION_MATRIX = new int[][]{{0, 1, 5, 4, 2, 3, 6}, {0, 1, 4, 5, 3, 2, 6}, {4, 5, 2, 3, 1, 0, 6}, {5, 4, 2, 3, 0, 1, 6}, {3, 2, 0, 1, 4, 5, 6}, {2, 3, 1, 0, 4, 5, 6}, {0, 1, 2, 3, 4, 5, 6}};

    private static final byte TEX_PAGE_INPUT = 0, TEX_PAGE_OUTPUT_ENABLED = 1, TEX_PAGE_OUTPUT_DISABLED = 2;

    public static void main(String[] args) {
        // Display all cross products
        int rowIndex = 0;
        for (ForgeDirection a : ForgeDirection.VALID_DIRECTIONS) {
            int colIndex = 0;
            for (ForgeDirection b : ForgeDirection.VALID_DIRECTIONS) {
                val cross = crossProduct(a, b);
                if (cross == ForgeDirection.UNKNOWN) {
                    continue;
                }
                val actualCross = traditionalCrossProduct(a, b);
                System.out.println("Row: " + rowIndex + ", Col: " + colIndex);
                System.out.println(a.name() + " x " + b.name() + " = " + cross.name());
                System.out.println(getVecRep(a) + " x " + getVecRep(b) + " = " + getVecRep(cross) + " (expected: " + Arrays.toString(actualCross) + ")");
                System.out.println();
                colIndex += 1;
            }
            rowIndex += 1;
        }
    }

    private static ForgeDirection crossProduct(ForgeDirection a, ForgeDirection b) {
        val tbl = new int[][]{{6, 6, 5, 4, 2, 3, 6}, {6, 6, 4, 5, 3, 2, 6}, {4, 5, 6, 6, 1, 0, 6}, {5, 4, 6, 6, 0, 1, 6}, {3, 2, 0, 1, 6, 6, 6}, {2, 3, 1, 0, 6, 6, 6}, {6, 6, 6, 6, 6, 6, 6}};
        return ForgeDirection.getOrientation(tbl[a.ordinal()][b.ordinal()]);
    }

    static int[] traditionalCrossProduct(ForgeDirection a, ForgeDirection b) {
        return new int[]{a.offsetY * b.offsetZ - a.offsetZ * b.offsetY, a.offsetZ * b.offsetX - a.offsetX * b.offsetZ, a.offsetX * b.offsetY - a.offsetY * b.offsetX};
    }

    static String getVecRep(ForgeDirection dir) {
        return String.format("(%d, %d, %d)", dir.offsetX, dir.offsetY, dir.offsetZ);
    }

    private byte[] itemsPerSide = new byte[6];

    private boolean[] sidesEnabled = new boolean[6];

    private boolean useNewDistributionBehavior = false;

    private byte currentSide = 0, currentSideItemCount = 0;

    private byte perpFacingNow = -1;

    public GT_MetaTileEntity_ItemDistributor(int aID, String aName, String aNameRegional, int aTier) {
        super(
                aID,
                aName,
                aNameRegional,
                aTier,
                28,
                new String[]{"Distributes specified quantities of items between output sides.",
                             "RMB or Shift+RMB using a Screwdriver to increase or decrease items pushed per side.",
                             "Right-click with a hammer to configure forced sorting",
                             "Does not require energy to move items, but CAN accept energy to pass to a connected machine."}
             );
    }

    @SuppressWarnings("unused")
    public GT_MetaTileEntity_ItemDistributor(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String aDescription) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
    }

    @SuppressWarnings("unused")
    public GT_MetaTileEntity_ItemDistributor(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_ItemDistributor(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_ItemDistributor(this.mName, this.mTier, this.mInventory.length, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean onHammerToolRightClick(final byte aSide, final byte aWrenchingSide, final EntityPlayer aPlayer, final float aX, final float aY, final float aZ) {
        useNewDistributionBehavior = !useNewDistributionBehavior;
        GT_Utility.sendChatToPlayer(aPlayer, useNewDistributionBehavior ? "Do not force sorting" : "Force sorting");
        return true;
    }

    @Override
    public boolean canReceiveImmediateEvents() {
        return true;
    }

    @Override
    public void acceptPerpendicular(final int perpendicularFacing) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.perpFacingNow = (byte) perpendicularFacing;
        }
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] returnTextures  = new ITexture[3][17][];
        ITexture       baseIcon        = getOverlayIcon();
        ITexture       inIcon          = TextureFactory.of(Textures.BlockIcons.OVERLAY_ITEM_IN);
        ITexture       outIcon         = TextureFactory.of(Textures.BlockIcons.OVERLAY_ITEM_OUT);
        ITexture       outDisabledIcon = TextureFactory.of(Textures.BlockIcons.OVERLAY_OUT_DISABLED);
        for (int i = 0; i < 17; i++) {
            val casing = Textures.BlockIcons.MACHINE_CASINGS[mTier][i];
            returnTextures[TEX_PAGE_INPUT][i]           = new ITexture[]{casing, baseIcon, inIcon};
            returnTextures[TEX_PAGE_OUTPUT_ENABLED][i]  = new ITexture[]{casing, baseIcon, outIcon};
            returnTextures[TEX_PAGE_OUTPUT_DISABLED][i] = new ITexture[]{casing, baseIcon,outDisabledIcon,};
        }
        return returnTextures;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return mTextures[TEX_PAGE_INPUT][aColorIndex + 1];
        } else {
            if (!sidesEnabled[aSide]) {
                return mTextures[TEX_PAGE_OUTPUT_DISABLED][aColorIndex + 1];
            }
            return mTextures[TEX_PAGE_OUTPUT_ENABLED][aColorIndex + 1];
        }
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return getBaseMetaTileEntity().getFrontFacing() == aSide || itemsPerSide[aSide] == 0;
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return getBaseMetaTileEntity().getFrontFacing() != aSide && itemsPerSide[aSide] > 0;
    }

    @Override
    public ITexture getOverlayIcon() {
        return TextureFactory.of(AUTOMATION_ITEMDISTRIBUTOR);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByteArray("mItemsPerSide", itemsPerSide);
        aNBT.setBoolean("mUseNewDistributionBehavior", useNewDistributionBehavior);
        GT_NBT_Util.setBooleanArray(aNBT, "mSidesEnabled", sidesEnabled);
        aNBT.setByte("mCurrentSide", currentSide);
        aNBT.setByte("mCurrentSideItemCount", currentSideItemCount);
        //
        aNBT.setByte("mCurrentFacing", getBaseMetaTileEntity().getFrontFacing());
        aNBT.setByte("mCurrentPerp", perpFacingNow);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        grabNBT(aNBT);
        currentSide          = aNBT.getByte("mCurrentSide");
        currentSideItemCount = aNBT.getByte("mCurrentSideItemCount");
        perpFacingNow        = aNBT.getByte("mCurrentPerp");
    }

    private void grabNBT(final NBTTagCompound aNBT) {
        useNewDistributionBehavior = aNBT.getBoolean("mUseNewDistributionBehavior");
        itemsPerSide               = aNBT.getByteArray("mItemsPerSide");
        sidesEnabled               = GT_NBT_Util.getBooleanArray(aNBT, "mSidesEnabled");
        if (itemsPerSide.length != 6) {
            itemsPerSide = new byte[6];
        }
        if (sidesEnabled.length != 6) {
            sidesEnabled = new boolean[6];
        }
        setSideEnabledFromItemPerSide();
    }

    private void setSideEnabledFromItemPerSide() {
        for (int i = 0; i < 6; i++) {
            sidesEnabled[i] = itemsPerSide[i] != 0;
        }
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        val newPerSideValue = (byte) ((itemsPerSide[aSide] + (aPlayer.isSneaking() ? -1 : 1) + 128) % 128);
        updateSideToValue(aSide, newPerSideValue);
        sendSideUpdateToClient(aSide);
        GT_Utility.sendChatToPlayer(aPlayer, trans("211", "Items per side: ") + newPerSideValue);
    }

    protected void updateSideToValue(byte aSide, byte newVal) {
        itemsPerSide[aSide] = newVal;
        sidesEnabled[aSide] = newVal != 0;
    }

    private void sendSideUpdateToClient(final byte aSide) {
        if (getBaseMetaTileEntity().isServerSide()) {
            val toSend = (aSide & 0x7) | (sidesEnabled[aSide] ? 0x8 : 0);
            getBaseMetaTileEntity().sendBlockEvent(BaseMetaTileEntity.ClientEvents.MISC_EVENT, (byte) toSend);
        }
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTimer) {
        super.onPostTick(aBaseMetaTileEntity, aTimer);
        if (getBaseMetaTileEntity().isServerSide() && aTimer % 40 == 0) {
            sendAllSidesToClient();
        }
    }

    @Override
    protected void moveItems(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        fillStacksIntoFirstSlots();
        int        movedItems;
        TileEntity adjacentTileEntity = aBaseMetaTileEntity.getTileEntityAtSide(currentSide);
        int        inspectedSides     = 0;
        while (itemsPerSide[currentSide] == 0) {
            iterateSide();
            currentSideItemCount = 0;
            adjacentTileEntity   = aBaseMetaTileEntity.getTileEntityAtSide(currentSide);
            inspectedSides += 1;
            if (inspectedSides == 6) {
                return;
            }
        }
        val oppositeSide = GT_Utility.getOppositeSide(currentSide);
        val maxAmount    = (byte) (itemsPerSide[currentSide] - currentSideItemCount);
        movedItems = GT_Utility.moveOneItemStack(aBaseMetaTileEntity, adjacentTileEntity, currentSide, oppositeSide, null, false, (byte) 64, (byte) 1, maxAmount, (byte) 1);
        currentSideItemCount += (byte) movedItems;
        if (currentSideItemCount >= itemsPerSide[currentSide] || useNewDistributionBehavior) {
            iterateSide();
            currentSideItemCount = 0;
        }
        if (movedItems > 0 || aBaseMetaTileEntity.hasInventoryBeenModified() || useNewDistributionBehavior) {
            mSuccess = 50;
        }
        fillStacksIntoFirstSlots();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return aSide == aBaseMetaTileEntity.getFrontFacing();
    }

    private void iterateSide() {
        currentSide = (byte) ((currentSide + 1) % 6);
    }

    private ForgeDirection getPerpFacing(final byte perpFacing) {
        switch (perpFacing) {
            case 0: {
                return ForgeDirection.DOWN;
            }
            case 1: {
                return ForgeDirection.UP;
            }
            case 2: {
                return ForgeDirection.NORTH;
            }
            case 3: {
                return ForgeDirection.EAST;
            }
            case 4: {
                return ForgeDirection.SOUTH;
            }
            case 5: {
                return ForgeDirection.WEST;
            }
            default: {
                return ForgeDirection.UNKNOWN;
            }
        }
    }

    private void sendAllSidesToClient() {
        for (byte i = 0; i < 6; i++) {
            sendSideUpdateToClient(i);
        }
    }

    private void processAxialRotation(final ForgeDirection axis, final ForgeDirection primary, final ForgeDirection primaryMapped, final byte[] newItemsPerSide) {
        if (primary.equals(primaryMapped.getOpposite())) {
            // 180 about axis
            for (byte i = 0; i < 6; i++) {
                final int j = rotationSquared(axis, ForgeDirection.getOrientation(i)).ordinal();
                newItemsPerSide[j] = itemsPerSide[i];
            }
        } else {
            val transform = getInverseRotationMatrix(primary, primaryMapped);
            for (byte i = 0; i < 6; i++) {
                val j = ForgeDirection.getOrientation(i).getRotation(transform).ordinal();
                newItemsPerSide[i] = itemsPerSide[j];
            }
        }
    }

    private static ForgeDirection getInverseRotationMatrix(final ForgeDirection x, final ForgeDirection y) {
        return ForgeDirection.getOrientation(INVERSE_ROTATION_MATRIX[x.ordinal()][y.ordinal()]);
    }

    private static ForgeDirection rotationSquared(final ForgeDirection axis, final ForgeDirection initial) {
        return initial.getRotation(axis).getRotation(axis);
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_ItemDistributor(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_ItemDistributor(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    protected void receiveMiscEvent(byte eventData) {
        if (getBaseMetaTileEntity().isClientSide()) {
            val aSide       = eventData & 0x7;
            val sideEnabled = (eventData & 0x8) != 0;
            sidesEnabled[aSide] = sideEnabled;
        }
    }

}
