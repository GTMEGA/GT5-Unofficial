package gregtech.common.sf_logic;


import com.google.common.io.ByteArrayDataInput;
import gregtech.api.util.ISerializableObject;
import io.netty.buffer.ByteBuf;
import lombok.NonNull;
import lombok.val;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;


public interface ISF_Data<MyType extends ISF_Data<MyType>> extends ISerializableObject {

    class FilterRegistry {

        private static final Map<Byte, ISF_Data<?>> FILTER_REGISTRY = new HashMap<>();

        public static void registerFilter(byte id, ISF_Data<?> filter) {
            FILTER_REGISTRY.put(filter.getID(), filter);
        }

        public static ISF_Data<?> getFilter(byte id) {
            return FILTER_REGISTRY.get(id);
        }

    }

    byte getID();

    @NonNull
    @Override
    default NBTBase saveDataToNBT() {
        val result = new NBTTagCompound();
        val filterData = filterSaveDataToNBT();
        result.setTag("filterData", filterData);
        return result;
    }

    NBTTagCompound filterSaveDataToNBT();

    @Override
    default void writeToByteBuf(ByteBuf aBuf) {
        filterWriteToByteBuf(aBuf);
    }

    @Override
    default void loadDataFromNBT(NBTBase aNBT) {
        if (aNBT instanceof NBTTagCompound) {
            val compound = (NBTTagCompound) aNBT;
            val filterData = compound.getCompoundTag("filterData");
            filterLoadDataFromNBT(filterData);
        }
    }

    void filterLoadDataFromNBT(NBTTagCompound aNBT);

    @NonNull
    @Override
    default ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
        return filterReadFromPacket(aBuf, aPlayer);
    }

    MyType filterReadFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer);

    void filterWriteToByteBuf(ByteBuf aBuf);

}
