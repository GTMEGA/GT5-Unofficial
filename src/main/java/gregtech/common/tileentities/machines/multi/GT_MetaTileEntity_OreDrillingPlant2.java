package gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class GT_MetaTileEntity_OreDrillingPlant2 extends GT_MetaTileEntity_OreDrillingPlantBase {
    public GT_MetaTileEntity_OreDrillingPlant2(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        mTier=2;
    }

    public GT_MetaTileEntity_OreDrillingPlant2(String aName) {
        super(aName);
        mTier=2;
    }

    @Override
    protected int fortune() {
        return 600;
    }

    @Override
    protected int perTickFluidStackMultiplier() {
        return 2;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        return createTooltip("II");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_OreDrillingPlant2(mName);
    }

    @Override
    protected ItemList getCasingBlockItem() {
        return ItemList.Casing_FrostProof;
    }

    @Override
    protected Materials getFrameMaterial() {
        return Materials.Aluminium;
    }

    @Override
    protected int getCasingTextureIndex() {
        return 17;
    }

    @Override
    protected int getRadiusInChunks() {
        return 1;
    }

    @Override
    protected int getMinTier() {
        return 3;
    }

    @Override
    protected int getBaseProgressTime() {
        return 800;
    }
}
