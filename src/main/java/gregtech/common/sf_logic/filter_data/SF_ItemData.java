package gregtech.common.sf_logic.filter_data;


import com.google.common.io.ByteArrayDataInput;
import gregtech.api.util.ISerializableObject;
import gregtech.common.sf_logic.ISF_Data;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;


@AllArgsConstructor
@Getter
public class SF_ItemData implements ISF_Data<SF_ItemData> {

    private int itemID;
    private int itemMeta;

}
