package gregtech.api.graphs.consumers;


import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;


// consumer for IC2 machines
public class NodeEnergySink extends ConsumerNode {

    public NodeEnergySink(int nodeValue, TileEntity tileEntity, byte side, ArrayList<ConsumerNode> consumers) {
        super(nodeValue, tileEntity, side, consumers);
    }

    @Override
    public boolean needsEnergy() {
        return super.needsEnergy();
    }

    @Override
    public int injectEnergy(long aVoltage, long aMaxAmps) {
        return 0;
    }

}
