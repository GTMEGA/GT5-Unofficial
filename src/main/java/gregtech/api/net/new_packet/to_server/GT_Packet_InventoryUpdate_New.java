package gregtech.api.net.new_packet.to_server;

import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import eu.usrv.yamcore.network.server.AbstractServerMessageHandler;
import gregtech.api.interfaces.IPacketReceivableItem;
import gregtech.api.util.ISerializableObject;
import gregtech.api.util.interop.BaublesInterop;
import io.netty.buffer.ByteBuf;
import lombok.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GT_Packet_InventoryUpdate_New implements IMessage {

    public static class Handler extends AbstractServerMessageHandler<GT_Packet_InventoryUpdate_New> {

        @Override
        public IMessage handleServerMessage(final EntityPlayer player, final GT_Packet_InventoryUpdate_New message, final MessageContext ctx) {
            if (player == null) {
                return null;
            }
            final ItemStack stack;
            if (message.bauble) {
                stack = BaublesInterop.INSTANCE.getBauble(player, message.slot);
            } else {
                stack = player.inventory.getStackInSlot(message.slot);
            }
            if (stack != null && stack.getItem() == message.item && message.item instanceof IPacketReceivableItem) {
                val receivable = (IPacketReceivableItem) message.item;
                receivable.onPacketReceived(player.worldObj, player, stack, message.data);
            }
            return null;
        }

    }

    private boolean bauble;

    private Item item;

    private int slot;

    private ISerializableObject data;

    @Override
    public void fromBytes(final ByteBuf byteBuf) {
        bauble = byteBuf.readBoolean();
        item = Item.getItemById(byteBuf.readInt());
        slot = byteBuf.readInt();
        if (byteBuf.readBoolean() && item instanceof IPacketReceivableItem) {
            val receivable = (IPacketReceivableItem) item;
            data = receivable.readFromBytes((ByteArrayDataInput) byteBuf);
        } else {
            data = null;
        }
    }

    @Override
    public void toBytes(final ByteBuf byteBuf) {
        byteBuf.writeBoolean(bauble);
        byteBuf.writeInt(Item.getIdFromItem(item));
        byteBuf.writeInt(slot);
        if (data != null) {
            byteBuf.writeBoolean(true);
            data.writeToByteBuf(byteBuf);
        } else {
            byteBuf.writeBoolean(false);
        }
    }

}
