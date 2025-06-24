package gregtech.common.items;

import gregtech.api.items.GT_Generic_Item;

import net.minecraft.util.EnumChatFormatting;

public class GT_Item_Cell extends GT_Generic_Item {
    public GT_Item_Cell() {
        super("gtItemCell",
              "Cell",
              EnumChatFormatting.DARK_GREEN + "Created by Dr. Gero",
              true);
    }
}
