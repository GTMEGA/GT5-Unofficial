package gregtech.loaders.misc;

import gregtech.api.GregTech_API;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.common.covers.GT_Cover_Vent;


import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import static gregtech.api.enums.Textures.BlockIcons.VENT_ADVANCED;
import static gregtech.api.enums.Textures.BlockIcons.VENT_NORMAL;

public class GT_CoverLoader
        implements Runnable {
    @Override
    public void run() {
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            GregTech_API.registerCover(new ItemStack(Blocks.carpet, 1, i), TextureFactory.of(Blocks.wool, i), null);
        }
    }
}
