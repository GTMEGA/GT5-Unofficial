package gregtech.loaders.preload.refactored;

public final class ExampleLoader {
    private static boolean LOADED = false;

    public static void load() {
        if (LOADED)
            throw new RuntimeException("Already loaded!");

        LOADED = true;
    }
}
