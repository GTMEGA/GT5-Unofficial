package gregtech.api.util;

import lombok.NonNull;
import lombok.val;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public class GT_NBT_Util {

    public static boolean[] getBooleanArray(NBTTagCompound nbt, String key) {
        if (!nbt.hasKey(key)) {
            return new boolean[0];
        }
        val byteArray = nbt.getByteArray(key);
        val boolArray = new boolean[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            boolArray[i] = byteArray[i] != 0;
        }
        return boolArray;
    }

    public static void setBooleanArray(NBTTagCompound nbt, String key, boolean[] boolArray) {
        val byteArray = new byte[boolArray.length];
        for (int i = 0; i < boolArray.length; i++) {
            byteArray[i] = (byte) (boolArray[i] ? 1 : 0);
        }
        nbt.setByteArray(key, byteArray);
    }

}
