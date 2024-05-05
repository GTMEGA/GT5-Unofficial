package gregtech.common;

import com.falsepattern.falsetweaks.api.ThreadedChunkUpdates;

import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.common.Loader;

public class GT_Compat {
    private static boolean isFalseTweaksPresent;

    public static void init() {
        if (Loader.isModLoaded("falsetweaks")) {
            isFalseTweaksPresent = true;
        }
    }

    public static Tessellator tessellator() {
        if (isFalseTweaksPresent) {
            return FalseTweaksCompat.getThreadTessellator();
        } else {
            return Tessellator.instance;
        }
    }

    //This extra bit of indirection is needed to avoid accidentally trying to load ThreadedChunkUpdates when FalseTweaks
    // is not installed.
    private static class FalseTweaksCompat {
        public static Tessellator getThreadTessellator() {
            if (ThreadedChunkUpdates.isEnabled()) {
                return ThreadedChunkUpdates.getThreadTessellator();
            } else {
                return Tessellator.instance;
            }
        }
    }
}
