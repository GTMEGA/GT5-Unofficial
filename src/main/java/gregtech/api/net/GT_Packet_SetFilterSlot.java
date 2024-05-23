package gregtech.api.net;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.primitives.Bytes;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.val;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.common.network.ByteBufUtils;

import java.util.ArrayList;

public class GT_Packet_SetFilterSlot extends GT_Packet_New {
    private EntityPlayer player = null;
    private int x, y, z;
    private int slotNumber;
    private ItemStack ghostStack;

    public GT_Packet_SetFilterSlot() {
        super(true);
    }

    public GT_Packet_SetFilterSlot(int x, int y, int z, int slotNumber, ItemStack ghostStack) {
        super(true);
        this.x = x;
        this.y = y;
        this.z = z;
        this.slotNumber = slotNumber;
        this.ghostStack = ghostStack;
    }

    @Override
    public void process(IBlockAccess aWorld) {
        val container = this.player.openContainer;

        if (!(container instanceof GT_ContainerMetaTile_Machine)) {
            return;
        }

        if (this.slotNumber < 0 || slotNumber >= container.inventorySlots.size()) {
            return;
        }

        val slot = container.getSlot(this.slotNumber);

        if (!(slot instanceof GT_Slot_Holo)) {
            return;
        }

        this.ghostStack.stackSize = 1;
        slot.putStack(this.ghostStack.copy());
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(this.x);
        aOut.writeInt(this.y);
        aOut.writeInt(this.z);
        aOut.writeInt(this.slotNumber);
        ByteBufUtils.writeItemStack(aOut, this.ghostStack);
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        val x = aData.readInt();
        val y = aData.readInt();
        val z = aData.readInt();

        val slotNumber = aData.readInt();

        val bytes = new ArrayList<Byte>();
        try {
            while (true) {
                bytes.add(aData.readByte());
            }
        } catch (Exception e) {
            //lmao it's finished reading or something idk
        }

        val byteArray = Bytes.toArray(bytes);

        val byteBuf = Unpooled.wrappedBuffer(byteArray);
        val itemStack = ByteBufUtils.readItemStack(byteBuf);

        val packet = new GT_Packet_SetFilterSlot(x, y, z, slotNumber, itemStack);

        return packet;
    }

    @Override
    public void setINetHandler(INetHandler aHandler) {
        if (aHandler instanceof NetHandlerPlayServer) {
            this.player = ((NetHandlerPlayServer) aHandler).playerEntity;
        }
    }
}
