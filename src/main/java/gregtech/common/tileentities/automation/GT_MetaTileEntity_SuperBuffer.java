package gregtech.common.tileentities.automation;


import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.GT_Container_SuperBuffer;
import gregtech.common.gui.GT_GUIContainer_SuperBuffer;
import net.minecraft.entity.player.InventoryPlayer;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_SUPERBUFFER;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_SUPERBUFFER_GLOW;


public class GT_MetaTileEntity_SuperBuffer extends GT_MetaTileEntity_ChestBuffer {

    public GT_MetaTileEntity_SuperBuffer(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 257, new String[]{
                "Buffers up to 256 Item Stacks", "Use Screwdriver to regulate output stack size", "Does not consume energy to move Item", getTickRateDesc(aTier)
        });
    }

    public GT_MetaTileEntity_SuperBuffer(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_SuperBuffer(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_SuperBuffer(this.mName, this.mTier, this.mInventory.length, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public ITexture getOverlayIcon() {
        return TextureFactory.of(TextureFactory.of(AUTOMATION_SUPERBUFFER), TextureFactory.builder().addIcon(AUTOMATION_SUPERBUFFER_GLOW).glow().build());
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_SuperBuffer(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_SuperBuffer(aPlayerInventory, aBaseMetaTileEntity);
    }

}
