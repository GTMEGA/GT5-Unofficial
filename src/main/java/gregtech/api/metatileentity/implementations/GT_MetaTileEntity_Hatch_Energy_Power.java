package gregtech.api.metatileentity.implementations;


import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
public class GT_MetaTileEntity_Hatch_Energy_Power extends GT_MetaTileEntity_Hatch_Energy {

    protected final long maxAmpThroughPut;

    protected final ITexture powerTextureOverlay;

    public GT_MetaTileEntity_Hatch_Energy_Power(int aID, String aName, String aNameRegional, int aTier, long amps) {
        super(aID, aName, aNameRegional, aTier, new String[]{"Energy Injector for Multiblocks", "Accepts up to " + amps + " Amps"});
        maxAmpThroughPut = amps;
        powerTextureOverlay = Textures.BlockIcons.POWER_OVERLAYS.getOrDefault(amps, TextureFactory.of(Textures.BlockIcons.POWER_16));
    }

    public GT_MetaTileEntity_Hatch_Energy_Power(String aName, int aTier, String aDescription, ITexture[][][] aTextures, long amps) {
        super(aName, aTier, aDescription, aTextures);
        maxAmpThroughPut = amps;
        powerTextureOverlay = Textures.BlockIcons.POWER_OVERLAYS.getOrDefault(amps, TextureFactory.of(Textures.BlockIcons.POWER_16));
    }

    public GT_MetaTileEntity_Hatch_Energy_Power(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, long amps) {
        super(aName, aTier, aDescription, aTextures);
        maxAmpThroughPut = amps;
        powerTextureOverlay = Textures.BlockIcons.POWER_OVERLAYS.getOrDefault(amps, TextureFactory.of(Textures.BlockIcons.POWER_16));
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Energy_Power(mName, mTier, mDescriptionArray, mTextures, maxAmpThroughPut);
    }

    @Override
    public long maxAmperesIn() {
        return getMaxAmpThroughPut();
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return ArrayUtils.add(super.getTexturesActive(aBaseTexture), getPowerTextureOverlay());
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return ArrayUtils.add(super.getTexturesActive(aBaseTexture), getPowerTextureOverlay());
    }

}
