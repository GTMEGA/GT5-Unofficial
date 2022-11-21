package gregtech.common.tileentities.boilers;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.GT_Container_Boiler;
import gregtech.common.gui.GT_GUIContainer_Boiler;
import lombok.val;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import static gregtech.api.enums.Textures.BlockIcons.*;

public class GT_MetaTileEntity_Boiler_Semi extends GT_MetaTileEntity_Boiler {
    public static final int COOLDOWN_INTERVAL = 20;
    public static final int ENERGY_PER_LAVA = 1;
    public static final int CONSUMPTION_PER_HEATUP = 16;
    public static final int PRODUCTION_PER_SECOND = 640;
    public static final int POLLUTION_PER_SECOND = 20;
    public static final float EFFICIENCY = 0.8f;
    protected static Fluid creosent;
    protected static Fluid oil;

    public GT_MetaTileEntity_Boiler_Semi(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, new String[]{
                "Burns Ceosent or Oil at "+EFFICIENCY*100+"% efficiency",
                "Produces " + PRODUCTION_PER_SECOND + "L of Steam per second",
                "Causes " + POLLUTION_PER_SECOND + " Pollution per second"});
    }

    public GT_MetaTileEntity_Boiler_Semi(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Boiler_Semi(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[5][17][];
        final ITexture[]
                texBottom = {TextureFactory.of(MACHINE_STEELBRICKS_BOTTOM)},
                texTop = {TextureFactory.of(MACHINE_STEELBRICKS_TOP), TextureFactory.of(OVERLAY_PIPE)},
                texSide = {TextureFactory.of(MACHINE_STEELBRICKS_SIDE), TextureFactory.of(OVERLAY_PIPE)},
                texFront = {
                        TextureFactory.of(MACHINE_STEELBRICKS_SIDE),
                        TextureFactory.of(BOILER_LAVA_FRONT),
                        TextureFactory.of(BOILER_LAVA_FRONT_GLOW)},
                texFrontActive = {
                        TextureFactory.of(MACHINE_STEELBRICKS_SIDE),
                        TextureFactory.of(BOILER_LAVA_FRONT_ACTIVE),
                        TextureFactory.builder().addIcon(BOILER_LAVA_FRONT_ACTIVE_GLOW).glow().build()};
        for (byte i = 0; i < 17; i++) {
            rTextures[0][i] = texBottom;
            rTextures[1][i] = texTop;
            rTextures[2][i] = texSide;
            rTextures[3][i] = texFront;
            rTextures[4][i] = texFrontActive;
        }
        return rTextures;
    }

    @Override
    public int maxProgresstime() {
        return 1000;
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_Boiler(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_Boiler(aPlayerInventory, aBaseMetaTileEntity, "SteelBoiler.png");
    }

    @Override
    public int getCapacity() {
        return 32000;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Boiler_Semi(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    protected int getPollution() {
        return POLLUTION_PER_SECOND;
    }

    @Override
    protected int getProductionPerSecond() {
        return PRODUCTION_PER_SECOND;
    }

    @Override
    protected int getMaxTemperature() {
        return 1000;
    }

    @Override
    protected int getEnergyConsumption() {
        return CONSUMPTION_PER_HEATUP;
    }

    @Override
    protected int getCooldownInterval() {
        return COOLDOWN_INTERVAL;
    }

    @Override
    protected int getHeatUpRate() {
        return 20;
    }

    @Override
    protected int getHeatUpAmount() {
        return 2;
    }

    @Override
    protected void updateFuel(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        val inputStack = this.mInventory[2];
        val fluid = GT_Utility.getFluidForFilledItem(inputStack,true);
        if (isValidFluid(fluid)) {
            val recipe = GT_Recipe.GT_Recipe_Map.sDenseLiquidFuels.findFuel(fluid);
            if (recipe == null) return;
            this.mProcessingEnergy += recipe.mSpecialValue * fluid.amount * EFFICIENCY * 0.05;
            if (inputStack.stackSize > 1) {
                inputStack.stackSize--;
            } else {
                mInventory[2] = null;
            }
            val container = GT_Utility.getContainerItem(inputStack,true);
            val out = mInventory[3];
            if (out == null) {
                mInventory[3] = container;
            } else if (GT_Utility.areStacksEqual(out,container,false)) {
                int stackSize = out.stackSize + 1;
                if (stackSize < out.getMaxStackSize()) {
                    out.stackSize = stackSize;
                }
            }
        }
    }

    protected void loadFuelCache() {
        if (creosent == null) {
            creosent = Materials.Creosote.getFluid(1).getFluid();
            oil = Materials.Oil.getFluid(1).getFluid();
        }
    }

    protected boolean isValidFluid(FluidStack fluid) {
        loadFuelCache();
        return fluid.getFluid().equals(creosent) || fluid.getFluid().equals(oil);
    }

    @Override
    public final int fill(FluidStack aFluid, boolean doFill) {
        if (isValidFluid(aFluid) && (this.mProcessingEnergy < 50)) {
            val recipe = GT_Recipe.GT_Recipe_Map.sDenseLiquidFuels.findFuel(aFluid);
            if (recipe == null) return 0;
            int tFilledAmount = Math.min(100, aFluid.amount);
            if (doFill) {
                this.mProcessingEnergy += tFilledAmount * recipe.mSpecialValue * EFFICIENCY * 0.05;
            }
            return tFilledAmount;
        }
        return super.fill(aFluid, doFill);
    }
}
