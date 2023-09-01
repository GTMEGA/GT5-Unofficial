package gregtech.api.util.keybind;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


@Getter
public class GT_KeyBindings {

    public static final GT_KeyBindings INSTANCE = new GT_KeyBindings();

    private final Map<String, GT_KeyHandler> keyBindings = new HashMap<>();

    private GT_KeyBindings() {
    }

}
