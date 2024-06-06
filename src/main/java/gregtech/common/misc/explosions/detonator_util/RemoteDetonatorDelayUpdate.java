package gregtech.common.misc.explosions.detonator_util;

import com.google.common.io.ByteArrayDataInput;
import gregtech.api.util.ISerializableObject;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RemoteDetonatorDelayUpdate implements ISerializableObject {

    private int delay;

    @Nonnull
    @Override
    public ISerializableObject copy() {
        return new RemoteDetonatorDelayUpdate(delay);
    }

    @Deprecated
    @Nonnull
    @Override
    public NBTBase saveDataToNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void writeToByteBuf(final ByteBuf aBuf) {
        aBuf.writeByte(1);
        aBuf.writeInt(delay);
    }

    @Deprecated
    @Override
    public void loadDataFromNBT(final NBTBase aNBT) {

    }

    @Nonnull
    @Override
    public ISerializableObject readFromPacket(final ByteArrayDataInput aBuf, final EntityPlayerMP aPlayer) {
        return new RemoteDetonatorDelayUpdate(aBuf.readInt());
    }

}
