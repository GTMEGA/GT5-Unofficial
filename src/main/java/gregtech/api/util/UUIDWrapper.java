package gregtech.api.util;


import com.google.common.io.ByteArrayDataInput;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UUIDWrapper implements ISerializableObject {

    private UUID uuid;

    /**
     * @return
     */
    @Nonnull
    @Override
    public ISerializableObject copy() {
        return new UUIDWrapper(uuid);
    }

    /**
     * @return
     */
    @Nonnull
    @Deprecated
    @Override
    public NBTBase saveDataToNBT() {
        return new NBTTagCompound();
    }

    /**
     * Write data to given ByteBuf
     * The data saved this way is intended to be stored for short amount of time over network.
     * DO NOT store it to disks.
     *
     * @param aBuf
     */
    @Override
    public void writeToByteBuf(final ByteBuf aBuf) {
        aBuf.writeLong(uuid.getMostSignificantBits());
        aBuf.writeLong(uuid.getLeastSignificantBits());
    }

    /**
     * @param aNBT
     */
    @Deprecated
    @Override
    public void loadDataFromNBT(final NBTBase aNBT) {

    }

    /**
     * Read data from given parameter and return this.
     * The data read this way is intended to be stored for short amount of time over network.
     *
     * @param aBuf
     * @param aPlayer
     */
    @Nonnull
    @Override
    public ISerializableObject readFromPacket(final ByteArrayDataInput aBuf, final EntityPlayerMP aPlayer) {
        uuid = new UUID(aBuf.readLong(), aBuf.readLong());
        return this;
    }

}
