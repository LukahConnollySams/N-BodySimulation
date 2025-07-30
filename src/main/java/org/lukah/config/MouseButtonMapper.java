package org.lukah.config;

import com.google.gson.*;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MouseButtonMapper implements JsonDeserializer<Settings.MouseButtonBindings>, JsonSerializer<Settings.MouseButtonBindings> {

    public static final Map<String, Integer> mouseButtonMap = new HashMap<>();
    public static final Map<Integer, String> reverseMouseButtonMap = new HashMap<>();

    @Override
    public Settings.MouseButtonBindings deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject object = json.getAsJsonObject();
        Settings.MouseButtonBindings mouseButtonBindings = new Settings.MouseButtonBindings();

        mouseButtonBindings.toggleCamera = MouseButtonMapper.getButtonCode(object.get("toggleCamera").getAsString());

        return mouseButtonBindings;
    }

    @Override
    public JsonElement serialize(Settings.MouseButtonBindings src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject object = new JsonObject();

        object.addProperty("toggleCamera", getButtonName(src.toggleCamera));

        return object;
    }

    static {

        mouseButtonMap.put("LEFT_BUTTON", GLFW.GLFW_MOUSE_BUTTON_1);

        for (Map.Entry<String, Integer> entry : mouseButtonMap.entrySet()) {
            reverseMouseButtonMap.put(entry.getValue(), entry.getKey());
        }
    }

    public static int getButtonCode(String button) {
        return mouseButtonMap.getOrDefault(button.toUpperCase(), -1);
    }

    public static String getButtonName(int code) {
        return reverseMouseButtonMap.get(code);
    }
}
