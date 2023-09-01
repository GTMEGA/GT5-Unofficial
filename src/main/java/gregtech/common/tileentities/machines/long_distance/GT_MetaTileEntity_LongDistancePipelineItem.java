/**
 * Inspired/ported from GregTech 6 under the LGPL license
 * <p>
 * Copyright (c) 2020 GregTech-6 Team
 * <p>
 * This file is part of GregTech.
 * <p>
 * GregTech is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * GregTech is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with GregTech. If not, see <http://www.gnu.org/licenses/>.
 */

package gregtech.common.tileentities.machines.long_distance;


import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import static gregtech.api.enums.Textures.BlockIcons.*;


public class GT_MetaTileEntity_LongDistancePipelineItem extends GT_MetaTileEntity_LongDistancePipelineBase {

    static final int[] emptyIntArray = new int[0];

    public GT_MetaTileEntity_LongDistancePipelineItem(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "Sends Items over long distances");
    }

    public GT_MetaTileEntity_LongDistancePipelineItem(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public int getPipeMeta() {
        return 1;
    }

    @Override
    public boolean isSameClass(GT_MetaTileEntity_LongDistancePipelineBase other) {
        return other instanceof GT_MetaTileEntity_LongDistancePipelineItem;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int aSide) {
        if (checkTarget()) {
            final IGregTechTileEntity tTile = mTarget.getBaseMetaTileEntity();
            IInventory iInventory = getInventory();
            if (iInventory instanceof ISidedInventory) {
                return ((ISidedInventory) iInventory).getAccessibleSlotsFromSide(tTile.getBackFacing());
            }
            if (iInventory != null) {
                int[] tReturn = new int[iInventory.getSizeInventory()];
                for (int i = 0; i < tReturn.length; i++) {
                    tReturn[i] = i;
                }
                return tReturn;
            }
        }

        return emptyIntArray;
    }

    @Override
    public int getSizeInventory() {
        if (checkTarget()) {
            IInventory iInventory = getInventory();
            if (iInventory != null) {
                return iInventory.getSizeInventory();
            }
        }
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int aSlot) {
        if (checkTarget()) {
            IInventory iInventory = getInventory();
            if (iInventory != null) {
                return iInventory.getStackInSlot(aSlot);
            }
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int aSlot, int aDecrement) {
        if (checkTarget()) {
            IInventory iInventory = getInventory();
            if (iInventory != null) {
                return iInventory.decrStackSize(aSlot, aDecrement);
            }
        }
        return null;
    }

    public IInventory getInventory() {
        final IGregTechTileEntity tTile = mTarget.getBaseMetaTileEntity();
        TileEntity invTile = tTile.getTileEntityAtSide(tTile.getBackFacing());
        if (invTile instanceof IInventory) {
            return (IInventory) invTile;
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int aSlot) {
        if (checkTarget()) {
            IInventory iInventory = getInventory();
            if (iInventory != null) {
                return iInventory.getStackInSlotOnClosing(aSlot);
            }
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int aSlot, ItemStack aStack) {
        if (checkTarget()) {
            IInventory iInventory = getInventory();
            if (iInventory != null) {
                iInventory.setInventorySlotContents(aSlot, aStack);
            }
        }
    }

    @Override
    public String getInventoryName() {
        if (checkTarget()) {
            IInventory iInventory = getInventory();
            if (iInventory != null) {
                return iInventory.getInventoryName();
            }
        }
        return super.getInventoryName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        if (checkTarget()) {
            IInventory iInventory = getInventory();
            if (iInventory != null) {
                return iInventory.hasCustomInventoryName();
            }
        }
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        if (checkTarget()) {
            IInventory iInventory = getInventory();
            if (iInventory != null) {
                return iInventory.getInventoryStackLimit();
            }
        }
        return 0;
    }

//    // Relay Sided Inventories
//

    @Override
    public boolean isItemValidForSlot(int aSlot, ItemStack aStack) {
        if (checkTarget()) {
            IInventory iInventory = getInventory();
            if (iInventory != null) {
                return iInventory.isItemValidForSlot(aSlot, aStack);
            }
        }
        return false;
    }

    @Override
    public boolean canInsertItem(int aSlot, ItemStack aStack, int aSide) {
        if (checkTarget()) {
            final IGregTechTileEntity tTile = mTarget.getBaseMetaTileEntity();
            IInventory iInventory = getInventory();
            if (iInventory instanceof ISidedInventory) {
                return ((ISidedInventory) iInventory).canInsertItem(aSlot, aStack, tTile.getBackFacing());
            }
            return iInventory != null;
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int aSlot, ItemStack aStack, int aSide) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_LongDistancePipelineItem(mName, mTier, getDescription()[0], mTextures);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{
                    MACHINE_CASINGS[mTier][aColorIndex + 1], TextureFactory.of(OVERLAY_PIPELINE_ITEM_FRONT)
            };
        } else if (aSide == GT_Utility.getOppositeSide(aFacing)) {
            return new ITexture[]{
                    MACHINE_CASINGS[mTier][aColorIndex + 1], TextureFactory.of(OVERLAY_PIPELINE_ITEM_BACK)
            };
        } else {
            return new ITexture[]{
                    MACHINE_CASINGS[mTier][aColorIndex + 1], TextureFactory.of(OVERLAY_PIPELINE_ITEM_SIDE), TextureFactory.builder().addIcon(
                    OVERLAY_PIPELINE_ITEM_SIDE_GLOW).glow().build()
            };
        }
    }

}
