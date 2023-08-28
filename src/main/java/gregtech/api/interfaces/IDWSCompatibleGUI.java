package gregtech.api.interfaces;


import lombok.NonNull;
import net.minecraft.util.ResourceLocation;


public interface IDWSCompatibleGUI extends IDWSCompatible {

    @NonNull ResourceLocation getDWSGuiBackground();

    @NonNull default String getDWSGuiBackgroundPath(final @NonNull String path) {
        return path.replace(".png", "_dws.png");
    }

    default boolean hasDWSAlternativeBackground() {
        return false;
    }

}
