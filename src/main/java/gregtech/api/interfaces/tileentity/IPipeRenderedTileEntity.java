package gregtech.api.interfaces.tileentity;


import gregtech.api.interfaces.ITexture;


public interface IPipeRenderedTileEntity extends ICoverable, ITexturedTileEntity {

    float getThickNess();

    byte getConnections();

    default ITexture[] getTextureCovered(byte aSide) {
        return getTextureUncovered(aSide);
    }

    ITexture[] getTextureUncovered(byte aSide);

}
