package gregtech.api.interfaces;

import com.google.common.io.ByteArrayDataInput;
import gregtech.api.util.ISerializableObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IPacketReceivableItem {

    void onPacketReceived(final World world, final EntityPlayer player, final ItemStack stack, final ISerializableObject data);

    /**
     * Read a byte first, 0 means no data was passed, a non-zero number means there was.
     * In the former case, return null, in the latter, do further processing.
     * */
    @Nullable
    ISerializableObject readFromBytes(final ByteArrayDataInput aData);

}
