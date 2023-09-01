package gregtech.api.util.interop.ic2;


import com.google.common.base.Stopwatch;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;


public class IC2InteropBase {

    public TileEntity getIC2EnergyTile(TileEntity tile) {
        return null;
    }

    public boolean isValidIC2Tile(TileEntity base, TileEntity target, ForgeDirection side) {
        return false;
    }

    public boolean isEmitter(TileEntity tile) {
        return false;
    }

    public boolean isReceiver(TileEntity tile) {
        return false;
    }

    public boolean isEnergyTile(TileEntity tile) {
        return false;
    }

    public boolean isEnetNull() {
        return true;
    }

    public boolean shouldJoinIC2Enet(final TileEntity target) {
        return false;
    }

    public void parseIC2Recipes(final Stopwatch stopwatch) {

    }

}
