package gregtech.api.interfaces.tileentity;

import gregtech.api.interfaces.ITexture;
import net.minecraftforge.common.util.ForgeDirection;

public interface IPipeRenderedTileEntity extends ICoverable, ITexturedTileEntity {
    float getThickNess();

    byte getConnections();

    ITexture[] getTextureUncovered(ForgeDirection side);

    default ITexture[] getTextureCovered(ForgeDirection side) {
        return getTextureUncovered(side);
    }
}
