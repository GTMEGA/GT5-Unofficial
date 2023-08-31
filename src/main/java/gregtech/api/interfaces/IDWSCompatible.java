package gregtech.api.interfaces;


import gregtech.api.GregTech_API;
import lombok.val;


public interface IDWSCompatible {

    /**
     * Offsets a position for doublewide.
     *
     * @param x Initial position
     * @return Shifted position
     */
    default int applyDWSBump(final int x) {
        return x + (GregTech_API.mDWS ? getDWSWidthBump() : 0);
    }

    /**
     * Returns the x position, shifted so that its position relative to the full gui is the same
     *
     * @param x Initial X position
     * @return New X position
     */
    default int applyDWSBumpKeepRelative(final int x) {
        final int oldWidth = baseWidth();
        final int newWidth = applyDWSBump(oldWidth);
        if (!GregTech_API.mDWS || oldWidth == newWidth) {
            return x;
        }
        val ratio = (float) x / oldWidth;
        return (int) (newWidth * ratio);
    }

    int getDWSWidthBump();

    int baseWidth();

}
