package gregtech.api.interfaces.tileentity;

import gregtech.api.interfaces.ITexture;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

public interface ITexturedTileEntity {
    /**
     * @return the Textures rendered by the GT IconRenderer
     */
    ITexture[] getTexture(Block block, ForgeDirection side);

    default boolean hasTranslucency() {
        return false;
    }
}
