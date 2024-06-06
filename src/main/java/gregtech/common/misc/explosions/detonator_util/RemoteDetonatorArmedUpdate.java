package gregtech.common.misc.explosions.detonator_util;

import com.google.common.io.ByteArrayDataInput;
import gregtech.api.util.ISerializableObject;
import gregtech.common.misc.explosions.GT_Explosion_Info;
import io.netty.buffer.ByteBuf;
import lombok.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RemoteDetonatorArmedUpdate implements ISerializableObject {

    private RemoteDetonationTargetList.Target target;

    private boolean armed;

    @Nonnull
    @Override
    public ISerializableObject copy() {
        return new RemoteDetonatorArmedUpdate(target, armed);
    }

    @Deprecated
    @Nonnull
    @Override
    public NBTBase saveDataToNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void writeToByteBuf(final ByteBuf aBuf) {
        aBuf.writeByte(2);
        aBuf.writeInt(target.getExplosiveType().ordinal());
        aBuf.writeInt(target.getX());
        aBuf.writeInt(target.getY());
        aBuf.writeInt(target.getZ());
        aBuf.writeBoolean(armed);
    }

    @Override
    public void loadDataFromNBT(final NBTBase aNBT) {

    }

    @Nonnull
    @Override
    public ISerializableObject readFromPacket(final ByteArrayDataInput aBuf, final EntityPlayerMP aPlayer) {
        val type    = RemoteDetonationTargetList.ExplosiveType.values()[aBuf.readInt()];
        val expTier = GT_Explosion_Info.getTierType(aBuf.readInt(), aBuf.readInt());
        return new RemoteDetonatorArmedUpdate(new RemoteDetonationTargetList.Target(-1, type, expTier, aBuf.readInt(), aBuf.readInt(), aBuf.readInt()), aBuf.readBoolean());
    }

}
