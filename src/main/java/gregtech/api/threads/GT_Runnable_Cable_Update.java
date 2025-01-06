package gregtech.api.threads;

import gregtech.GT_Mod;
import gregtech.api.interfaces.metatileentity.IConnectable;
import gregtech.api.interfaces.tileentity.IMachineBlockUpdateable;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Item;
import gregtech.common.GT_Proxy;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class GT_Runnable_Cable_Update extends GT_Runnable_MachineBlockUpdate {
    protected final BlockChecker blockChecker;
    protected GT_Runnable_Cable_Update(World aWorld, ChunkCoordinates aCoords, BlockChecker blockChecker) {
        super(aWorld, aCoords);
        this.blockChecker = blockChecker;
    }

    public static void setCableUpdateValues(World aWorld, ChunkCoordinates aCoords) {
        if (isEnabled) {
            EXECUTOR_SERVICE.submit(new GT_Runnable_Cable_Update(aWorld, aCoords,(t,r) -> {
                if (t instanceof IMachineBlockUpdateable && !r) return true;
                if ((t instanceof BaseMetaPipeEntity)) {
                    return ((BaseMetaPipeEntity) t).getMetaTileEntity() instanceof GT_MetaPipeEntity_Cable;
                }
                return false;
            }));
        }
    }

    public static void setPipeUpdateValues(World aWorld, ChunkCoordinates aCoords) {
        if (isEnabled) {
            EXECUTOR_SERVICE.submit(new GT_Runnable_Cable_Update(aWorld, aCoords,(t,r) -> {
                if ((t instanceof BaseMetaPipeEntity)) {
                    return ((BaseMetaPipeEntity) t).getMetaTileEntity() instanceof GT_MetaPipeEntity_Item;
                }
                return false;
            }));
        }
    }

    public interface BlockChecker {
        boolean check(TileEntity entity,boolean shouldRecurse);
    }

    @Override
    public void run() {
        try {
            while (!tQueue.isEmpty()) {
                final ChunkCoordinates aCoords = tQueue.poll();
                final TileEntity tTileEntity;

                GT_Proxy.TICK_LOCK.lock();
                try {
                    //we dont want to go over cables that are in unloaded chunks
                    //keeping the lock just to make sure no CME happens
                    if (world.blockExists(aCoords.posX, aCoords.posY, aCoords.posZ)) {
                        tTileEntity = world.getTileEntity(aCoords.posX, aCoords.posY, aCoords.posZ);
                    } else {
                        tTileEntity = null;
                    }
                } finally {
                    GT_Proxy.TICK_LOCK.unlock();
                }

                // See if the block itself needs an update
                if (tTileEntity instanceof IMachineBlockUpdateable && blockChecker.check(tTileEntity,false))
                    ((IMachineBlockUpdateable) tTileEntity).onMachineBlockUpdate();

                // Now see if we should add the nearby blocks to the queue:
                // only add blocks the cable is connected to
                if (tTileEntity instanceof BaseMetaPipeEntity &&
                        ((BaseMetaPipeEntity) tTileEntity).getMetaTileEntity() instanceof IConnectable &&
                        blockChecker.check(tTileEntity,true))
                {
                    ChunkCoordinates tCoords;
                    for (byte i = 0;i<6;i++) {
                        if (((IConnectable) ((BaseMetaPipeEntity) tTileEntity).getMetaTileEntity()).isConnectedAtSide(i)) {
                            ForgeDirection offset = ForgeDirection.getOrientation(i);
                            if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX + offset.offsetX,
                                    aCoords.posY + offset.offsetY, aCoords.posZ + offset.offsetZ)))
                                tQueue.add(tCoords);
                        }
                    }
                }
            }
        } catch (Exception e) {
            GT_Mod.GT_FML_LOGGER.error(
                    "Well this update was broken... " + mCoords + ", mWorld={" + world.getProviderName() + " @dimId " + world.provider.dimensionId + "}", e);
        }
    }
}
