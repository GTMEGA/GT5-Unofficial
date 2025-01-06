package gregtech.api.graphs.api;

import gregtech.api.graphs.Node;
import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;

public interface PushItemGraph {
    boolean push(Node currentNode, ItemStack[] items, int invalid);
    void generate(BaseMetaPipeEntity aTileEntity);
}
