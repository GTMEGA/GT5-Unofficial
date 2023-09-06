package gregtech.api.net;

import com.google.common.io.ByteArrayDataInput;
import gregtech.api.interfaces.IPacketReceivableItem;
import gregtech.api.util.ISerializableObject;
import gregtech.api.util.interop.BaublesInterop;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.IBlockAccess;

import java.util.UUID;

@Setter
@Getter
public class GT_Packet_InventoryUpdate extends GT_Packet_New {

    private boolean bauble;

    private Item item;

    private int slot;

    private ISerializableObject data;

    private EntityPlayer player;

    public GT_Packet_InventoryUpdate() {
        super(true);
    }

    public GT_Packet_InventoryUpdate(final EntityPlayer player, final Item item, final boolean bauble,  final int aSlot, final ISerializableObject data) {
        super(false);
        this.player = player;
        this.item = item;
        this.bauble = bauble;
        this.slot = aSlot;
        this.data = data;
    }

    @Override
    public void process(final IBlockAccess aWorld) {
        if (player == null) {
            return;
        }
        final ItemStack stack;
        if (bauble) {
            stack = BaublesInterop.INSTANCE.getBauble(player, slot);
        } else {
            stack = player.inventory.getStackInSlot(slot);
        }
        if (stack != null && stack.getItem() == item && item instanceof IPacketReceivableItem) {
            ((IPacketReceivableItem) item).onPacketReceived(player.worldObj, player, stack, data);
        }
    }

    @Override
    public void encode(final ByteBuf aOut) {
        val id = player.getUniqueID();
        aOut.writeLong(id.getMostSignificantBits());
        aOut.writeLong(id.getLeastSignificantBits());
        aOut.writeInt(Item.getIdFromItem(item));
        aOut.writeBoolean(bauble);
        aOut.writeInt(slot);
        if (data == null) {
            aOut.writeBoolean(false);
        } else {
            aOut.writeBoolean(true);
            data.writeToByteBuf(aOut);
        }
    }

    @Override
    public GT_Packet_New decode(final ByteArrayDataInput aData) {
        val msb = aData.readLong();
        val lsb = aData.readLong();
        val id = new UUID(msb, lsb);
        val item = Item.getItemById(aData.readInt());
        val bauble = aData.readBoolean();
        val slot = aData.readInt();
        return new GT_Packet_InventoryUpdate(MinecraftServer.getServer().getEntityWorld().func_152378_a(id), item, bauble, slot, item instanceof IPacketReceivableItem && aData.readBoolean() ? ((IPacketReceivableItem) item).readFromBytes(aData) : null);
    }

}
