package gregtech.api.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public interface INonNBTSerializable extends ISerializableObject {

    /**
     * Not necessary for TE guis, don't bother with this unless you're doing something goofy.
     * @return Empty Tag
     */
    @Nonnull
    @Override
    @Deprecated
    default NBTBase saveDataToNBT() {
        return new NBTTagCompound();
    }

    /**
     * Not necessary for TE guis, don't bother with this unless you're doing something goofy.
     * @param aNBT Ignored
     */
    @Override
    @Deprecated
    default void loadDataFromNBT(NBTBase aNBT) {

    }

}
