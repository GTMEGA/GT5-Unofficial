package gregtech.api.util.keybind;


import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;


public class GT_KeyHandler extends KeyBinding {

    public interface GT_KeyCallback {

        void onPress(final EntityPlayer player);

    }


    public static final String category = "key.categories.gregtech";

    static {
        GT_LanguageManager.addStringLocalization(category, "GregTech");
    }

    private final GT_KeyCallback callback;

    public GT_KeyHandler(final String unlocalizedName, final String english, final GT_KeyCallback callback) {
        this(unlocalizedName, english, 0, callback);
    }

    public GT_KeyHandler(final String unlocalizedName, final String english, final int keyCode, final GT_KeyCallback callback) {
        super(unlocalizedName, keyCode, category);
        this.callback = callback;
        linkToKB(unlocalizedName);
        registerClientKeybind();
        localize(unlocalizedName, english);
        registerAsEvent();
    }

    protected void linkToKB(final String unlocalizedName) {
        GT_Values.KB.getKeyBindings().put(unlocalizedName, this);
    }

    protected void registerClientKeybind() {
        ClientRegistry.registerKeyBinding(this);
    }

    private static void localize(final String unlocalizedName, final String english) {
        GT_LanguageManager.addStringLocalization(unlocalizedName, english);
    }

    protected void registerAsEvent() {
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (this.isPressed()) {
            this.callback.onPress(GT_Mod.gregtechproxy.getThePlayer());
        }
    }

}
