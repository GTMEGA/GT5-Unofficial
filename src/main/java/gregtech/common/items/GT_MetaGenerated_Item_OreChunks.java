package gregtech.common.items;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.GT_MetaGenerated_Item_X32;
import lombok.val;

import net.minecraft.item.ItemStack;

public class GT_MetaGenerated_Item_OreChunks extends GT_MetaGenerated_Item_X32 {
    public GT_MetaGenerated_Item_OreChunks() {
        super("metaitem.ores", OrePrefixes.oreChunk);
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        val tMaterial = Materials.getMetaItemMaterial(aStack);

        return OrePrefixes.oreChunk.getDefaultLocalNameForItem(tMaterial);
    }
}
