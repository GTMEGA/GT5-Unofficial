package gregtech.common.tileentities.generators;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicGenerator;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;

import static gregtech.api.enums.Textures.BlockIcons.*;

public class GT_MetaTileEntity_DieselGenerator extends GT_MetaTileEntity_BasicGenerator {

    public static final int BASE_POLLUTION = 2;

    public int mEfficiency;

    public GT_MetaTileEntity_DieselGenerator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, new String[]{
                "Requires liquid Fuel",
                "Causes " + (int) (20 * BASE_POLLUTION * Math.pow(2, aTier - 1)) + " Pollution per second"});
        onConfigLoad();
    }

    public GT_MetaTileEntity_DieselGenerator(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        onConfigLoad();
    }

    public GT_MetaTileEntity_DieselGenerator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        onConfigLoad();
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DieselGenerator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipes() {
        return GT_Recipe.GT_Recipe_Map.sDieselFuels;
    }

    @Override
    public int getCapacity() {
        return 16000;
    }

    public void onConfigLoad() {
        this.mEfficiency = GregTech_API.sMachineFile.get(ConfigCategories.machineconfig, "DieselGenerator.efficiency.tier." + this.mTier, (95));
    }

    @Override
    public int getEfficiency() {
        return this.mEfficiency;
    }

    @Override
    public int getFuelValue(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack) || getRecipes() == null) return 0;
        long rValue = Math.max(GT_ModHandler.getFuelCanValue(aStack) * 6 / 5, super.getFuelValue(aStack));
        if (ItemList.Fuel_Can_Plastic_Filled.isStackEqual(aStack, false, true)) {
            rValue = Math.max(rValue, GameRegistry.getFuelValue(aStack) * 3L);
        }
        if (rValue > Integer.MAX_VALUE) {
            throw new ArithmeticException("Integer LOOPBACK!");
        }
        return (int) rValue;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aTick % 100 == 0 && mFluid != null && mFluid.amount > this.getCapacity()) {
            GT_Log.err.println("Dupe Abuse: " + aBaseMetaTileEntity.getOwnerName() + " Coords: " + aBaseMetaTileEntity.getXCoord() + " " + aBaseMetaTileEntity.getYCoord() + " " + aBaseMetaTileEntity.getZCoord());
            aBaseMetaTileEntity.setToFire();
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public ITexture[] getFront(byte aColor) {
        return new ITexture[]{super.getFront(aColor)[0],
                              TextureFactory.of(DIESEL_GENERATOR_FRONT),
                              OVERLAYS_ENERGY_OUT[this.mTier]};
    }

    @Override
    public ITexture[] getBack(byte aColor) {
        return new ITexture[]{super.getBack(aColor)[0], TextureFactory.of(DIESEL_GENERATOR_BACK)};
    }

    @Override
    public ITexture[] getBottom(byte aColor) {
        return new ITexture[]{super.getBottom(aColor)[0], TextureFactory.of(DIESEL_GENERATOR_BOTTOM)};
    }

    @Override
    public ITexture[] getTop(byte aColor) {
        return new ITexture[]{super.getTop(aColor)[0], TextureFactory.of(DIESEL_GENERATOR_TOP)};
    }

    @Override
    public ITexture[] getSides(byte aColor) {
        return new ITexture[]{super.getSides(aColor)[0], TextureFactory.of(DIESEL_GENERATOR_SIDE)};
    }

    @Override
    public ITexture[] getFrontActive(byte aColor) {
        return new ITexture[]{super.getFrontActive(aColor)[0],
                              TextureFactory.of(DIESEL_GENERATOR_FRONT_ACTIVE),
                              OVERLAYS_ENERGY_OUT[this.mTier]};
    }

    @Override
    public ITexture[] getBackActive(byte aColor) {
        return new ITexture[]{super.getBackActive(aColor)[0], TextureFactory.of(DIESEL_GENERATOR_BACK_ACTIVE)};
    }

    @Override
    public ITexture[] getBottomActive(byte aColor) {
        return new ITexture[]{super.getBottomActive(aColor)[0], TextureFactory.of(DIESEL_GENERATOR_BOTTOM_ACTIVE)};
    }

    @Override
    public ITexture[] getTopActive(byte aColor) {
        return new ITexture[]{super.getTopActive(aColor)[0], TextureFactory.of(DIESEL_GENERATOR_TOP_ACTIVE)};
    }

    @Override
    public ITexture[] getSidesActive(byte aColor) {
        return new ITexture[]{super.getSidesActive(aColor)[0], TextureFactory.of(DIESEL_GENERATOR_SIDE_ACTIVE)};
    }

    @Override
    public int getPollution() {
        return (int) (BASE_POLLUTION * Math.pow(2, mTier - 1));
    }
}
