package gregtech.api.items;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_ModHandler;
import net.minecraft.item.ItemStack;

public class CachedForgeItem {
    ItemStack cache;
    final String modId;
    final String name;
    final int meta;
    final int amount;

    public CachedForgeItem(String modId, String name, int meta, int amount) {
        this.modId = modId;
        this.name = name;
        this.meta = meta;
        this.amount = amount;
    }

    public ItemStack getItem() {
        if (cache == null) {
            cache = new ItemStack(GameRegistry.findItem(modId,name),amount,meta);
        }
        return cache.copy();
    }

    public ItemStack getItemNoCopy() {
        if (cache == null) {
            cache = new ItemStack(GameRegistry.findItem(modId,name),amount,meta);
        }
        return cache;
    }
}
