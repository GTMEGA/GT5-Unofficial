package gregtech.api.util.interop.ic2;


import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.common.Optional;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.reactor.IReactorChamber;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;


public class IC2Interop extends IC2InteropBase {

    public static final IC2Interop INSTANCE = new IC2Interop();

    private IC2Interop() {
    }

    /**
     * @param tile
     * @return
     */
    @Optional.Method(modid = "IC2")
    @Override
    public TileEntity getIC2EnergyTile(final TileEntity tile) {
        TileEntity ic2Energy;
        if (tile instanceof IReactorChamber) {
            ic2Energy = (TileEntity) ((IReactorChamber)tile).getReactor();
        } else {
            if (tile instanceof IEnergyTile || EnergyNet.instance == null) {
                ic2Energy = tile;
            } else {
                ic2Energy = EnergyNet.instance.getTileEntity(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
            }
        }
        return ic2Energy;
    }

    /**
     * @param base
     * @param target
     * @param side
     * @return
     */
    @Optional.Method(modid = "IC2")
    @Override
    public boolean isValidIC2Tile(final TileEntity base, final TileEntity target, final ForgeDirection side) {
        if (target instanceof IEnergySink && ((IEnergySink) target).acceptsEnergyFrom(base, side)) {
            return true;
        }
        if (GT_Mod.gregtechproxy.ic2EnergySourceCompat && target instanceof IEnergySource) {
            return true;
        }
        return GregTech_API.mInputRF && target instanceof IEnergyEmitter && ((IEnergyEmitter) target).emitsEnergyTo(base, side);
    }

    /**
     * @param tile
     * @return
     */
    @Optional.Method(modid = "IC2")
    @Override
    public boolean isEmitter(final TileEntity tile) {
       return tile instanceof IEnergyEmitter;
    }

    /**
     * @param tile
     * @return
     */
    @Optional.Method(modid = "IC2")
    @Override
    public boolean isReceiver(final TileEntity tile) {
        return tile instanceof IEnergyReceiver;
    }

    /**
     * @param tile
     * @return
     */
    @Optional.Method(modid = "IC2")
    @Override
    public boolean isEnergyTile(final TileEntity tile) {
        return tile instanceof IEnergyTile;
    }

    /**
     * @return
     */
    @Optional.Method(modid = "IC2")
    @Override
    public boolean isEnetNull() {
        return EnergyNet.instance == null;
    }

    /**
     * @return
     */
    @Optional.Method(modid = "IC2")
    @Override
    public boolean shouldJoinIC2Enet(final TileEntity target) {
        TileEntity temp = null;
        if (target == null || target instanceof IEnergyTile || isEnetNull()) {
            temp = target;
        } else {
            temp = EnergyNet.instance.getTileEntity(target.getWorldObj(), target.xCoord, target.yCoord, target.zCoord);
        }
        return temp instanceof IEnergyEmitter;
    }

}
