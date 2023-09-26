package gregtech.api.interfaces;


import lombok.NonNull;
import net.minecraft.util.ResourceLocation;


public interface IDWSCompatibleGUI extends IDWSCompatible {

    @NonNull ResourceLocation getDWSGuiBackground();

    @NonNull
    default String getDWSGuiBackgroundPath(final @NonNull String path) {
        return path.replace(".png", "_dws.png");
    }

    /**
     * True by default specifically so that anyone implementing this wrong gets a nice fat purple and black checkerboard
     */
    default boolean hasDWSAlternativeBackground() {
        return true;
    }

}
