package gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import net.minecraft.block.Block;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION3;

public class GT_MetaTileEntity_FusionComputer3 extends GT_MetaTileEntity_FusionComputer {

    private static final ITexture textureOverlay = TextureFactory.builder()
                                                                 .addIcon(OVERLAY_FUSION3)
                                                                 .extFacing()
                                                                 .build();

    public GT_MetaTileEntity_FusionComputer3(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 6);
    }

    public GT_MetaTileEntity_FusionComputer3(String aName) {
        super(aName);
    }

    @Override
    public int tier() {
        return 8;
    }

    @Override
    public long maxEUStore() {
        return 640010000L * (Math.min(16, this.mEnergyHatches.size())) / 16L;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_FusionComputer3(mName);
    }

    @Override
    public Block getCasing() {
        return GregTech_API.sBlockCasings4;
    }

    @Override
    public int getCasingMeta() {
        return 8;
    }

    @Override
    public Block getFusionCoil() {
        return GregTech_API.sBlockCasings4;
    }

    @Override
    public int getFusionCoilMeta() {
        return 7;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Fusion Reactor")
                .addInfo("A SUN DOWN ON EARTH")
                .addInfo("Controller block for the Fusion Reactor Mk III")
                .addInfo("8192EU/t and 40M EU capacity per Energy Hatch")
                .addInfo("If the recipe has a startup cost greater than the")
                .addInfo("number of energy hatches * cap, you can't do it")
                .addSeparator()
                .beginStructureBlock(15, 3, 15, false)
                .addController("See diagram when placed")
                .addCasingInfo("Fusion Machine Casing Mk II", 79)
                .addStructureInfo("Cover the coils with casing")
                .addOtherStructurePart("Fusion Coil Block", "Center part of the ring")
                .addEnergyHatch("1-16, Specified casings", 2)
                .addInputHatch("2-16, Specified casings", 1)
                .addOutputHatch("1-16, Specified casings", 3)
                .addStructureInfo("ALL Hatches must be UV or better")
                .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public ITexture getTextureOverlay() {
        return textureOverlay;
    }

    @Override
    public int tierOverclock() {
        return 4;
    }
}

