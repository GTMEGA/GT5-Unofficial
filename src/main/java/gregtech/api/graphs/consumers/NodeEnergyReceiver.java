package gregtech.api.graphs.consumers;

import cofh.api.energy.IEnergyReceiver;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.WorldSpawnedEventBuilder;
import gregtech.common.GT_Pollution;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

import static gregtech.api.enums.GT_Values.V;

//consumer for RF machines
public class NodeEnergyReceiver extends ConsumerNode {
    int mRestRF = 0;
    public NodeEnergyReceiver(int aNodeValue, IEnergyReceiver aTileEntity, byte aSide, ArrayList<ConsumerNode> aConsumers) {
        super(aNodeValue, (TileEntity) aTileEntity, aSide, aConsumers);
    }

    @Override
    public int injectEnergy(long aVoltage, long aMaxAmps) {
        ForgeDirection tDirection = ForgeDirection.getOrientation(mSide);
        int rfOut = GT_Utility.safeInt(aVoltage * GregTech_API.mEUtoRF / 100);
        int ampsUsed = 0;
        if (mRestRF < rfOut) {
            mRestRF += rfOut;
            ampsUsed = 1;
        }
        if (((IEnergyReceiver) mTileEntity).receiveEnergy(tDirection, mRestRF, true) > 0) {
            int consumed = ((IEnergyReceiver) mTileEntity).receiveEnergy(tDirection, mRestRF, false);
            mRestRF -= consumed;
            return ampsUsed;
        }
        if (GregTech_API.mRFExplosions && GregTech_API.sMachineExplosions &&
                ((IEnergyReceiver) mTileEntity).getMaxEnergyStored(tDirection) < rfOut * 600L) {
            explode(rfOut);
        }
        return 0;
    }

    //copied from IEnergyConnected
    private void explode(int aRfOut) {
        if (aRfOut > 32L * GregTech_API.mEUtoRF / 100L) {
            int tX = mTileEntity.xCoord, tY = mTileEntity.yCoord, tZ = mTileEntity.zCoord;
            World tWorld = mTileEntity.getWorldObj();
            GT_Utility.sendSoundToPlayers(tWorld, GregTech_API.sSoundList.get(209), 1.0F, -1, tX, tY, tZ);
            tWorld.setBlock(tX, tY, tZ, Blocks.air);
            if (GregTech_API.sMachineExplosions)
                if (GT_Mod.gregtechproxy.mPollution)
                    GT_Pollution.addPollution(tWorld.getChunkFromBlockCoords(tX, tZ), 100000);

            new WorldSpawnedEventBuilder.ExplosionEffectEventBuilder()
                    .setStrength(GT_Values.MachineExplosionPower)
                    .setSmoking(true)
                    .setPosition(tX + 0.5, tY + 0.5, tZ + 0.5)
                    .setWorld(tWorld)
                    .run();
        }
    }
}
