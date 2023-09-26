package gregtech.common.items.explosives;

import gregtech.api.enums.GT_Values;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class GT_Item_NukeExplosive extends GT_Item_Explosive {

    public GT_Item_NukeExplosive(Block block) {
        super(block, "mega_nuke", "MEGA Nuke");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lore, boolean b0) {
        final String[] lines = {
                "A forbidden piece of technology",
                "Created by scientists who asked themselves if they could, but not if they should.",
                "Has almost no practical use, but is fun to watch.",
                "Prime with a Remote Detonator",
                "Packs a pretty mean punch, so take",
                String.format("cover or you'll be mist in %d seconds!", GT_Values.NUKEFuse / 20)
        };
        lore.addAll(Arrays.asList(lines));
    }

}
