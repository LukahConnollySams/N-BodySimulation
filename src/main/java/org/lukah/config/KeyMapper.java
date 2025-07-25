package org.lukah.config;

import com.google.gson.*;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class KeyMapper implements JsonDeserializer<Settings.KeyBindings>, JsonSerializer<Settings.KeyBindings> {

    public static final Map<String, Integer> keyMap = new HashMap<>();
    public static final Map<Integer, String> reverseKeyMap = new HashMap<>();

    @Override
    public Settings.KeyBindings deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject object = json.getAsJsonObject();
        Settings.KeyBindings keyBindings = new Settings.KeyBindings();

        keyBindings.pause = KeyMapper.getKeyCode(object.get("pause").getAsString());
        keyBindings.moveForward = KeyMapper.getKeyCode(object.get("moveForward").getAsString());
        keyBindings.moveLeft = KeyMapper.getKeyCode(object.get("moveLeft").getAsString());
        keyBindings.moveBack = KeyMapper.getKeyCode(object.get("moveBack").getAsString());
        keyBindings.moveRight = KeyMapper.getKeyCode(object.get("moveRight").getAsString());
        keyBindings.moveUp = KeyMapper.getKeyCode(object.get("moveUp").getAsString());
        keyBindings.moveDown = KeyMapper.getKeyCode(object.get("moveDown").getAsString());
        keyBindings.moveFastMod = KeyMapper.getKeyCode(object.get("moveFastMod").getAsString());
        keyBindings.slowEngine = KeyMapper.getKeyCode(object.get("slowEngine").getAsString());
        keyBindings.hasteEngine = KeyMapper.getKeyCode(object.get("hasteEngine").getAsString());

        return keyBindings;
    }

    @Override
    public JsonElement serialize(Settings.KeyBindings src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject object = new JsonObject();

        object.addProperty("pause", getKeyName(src.pause));
        object.addProperty("moveForward", getKeyName(src.moveForward));
        object.addProperty("moveLeft", getKeyName(src.moveLeft));
        object.addProperty("moveBack", getKeyName(src.moveBack));
        object.addProperty("moveRight", getKeyName(src.moveRight));
        object.addProperty("moveUp", getKeyName(src.moveUp));
        object.addProperty("moveDown", getKeyName(src.moveDown));
        object.addProperty("moveFastMod", getKeyName(src.moveFastMod));
        object.addProperty("slowEngine", getKeyName(src.slowEngine));
        object.addProperty("hasteEngine", getKeyName(src.hasteEngine));

        return object;
    }

    static {

        for (char c = 'A'; c <= 'Z'; c++) {
            keyMap.put(String.valueOf(c), GLFW.GLFW_KEY_A + (c - 'A'));
        }

        for (char c = '0'; c <= '9'; c++) {
            keyMap.put(String.valueOf(c), GLFW.GLFW_KEY_0 + (c - '0'));
        }

        for (int i = 1; i <= 12; i++) {
            keyMap.put("F" + i, GLFW.GLFW_KEY_F1 + (i - 1));
        }

        keyMap.put("SPACE", GLFW.GLFW_KEY_SPACE);
        keyMap.put("LEFT_SHIFT", GLFW.GLFW_KEY_LEFT_SHIFT);
        keyMap.put("SHIFT_MOD", GLFW.GLFW_MOD_SHIFT);
        keyMap.put("LEFT_CTRL", GLFW.GLFW_KEY_LEFT_CONTROL);
        keyMap.put("CTRL_MOD", GLFW.GLFW_MOD_CONTROL);
        keyMap.put("LEFT_ALT", GLFW.GLFW_KEY_LEFT_ALT);
        keyMap.put("ALT_MOD", GLFW.GLFW_MOD_ALT);
        keyMap.put("ENTER", GLFW.GLFW_KEY_ENTER);
        keyMap.put("ESCAPE", GLFW.GLFW_KEY_ESCAPE);
        keyMap.put("LEFT", GLFW.GLFW_KEY_LEFT);
        keyMap.put("RIGHT", GLFW.GLFW_KEY_RIGHT);
        keyMap.put("UP", GLFW.GLFW_KEY_UP);
        keyMap.put("DOWN", GLFW.GLFW_KEY_DOWN);
        keyMap.put("TAB", GLFW.GLFW_KEY_TAB);
        keyMap.put("BACKSPACE", GLFW.GLFW_KEY_BACKSPACE);
        keyMap.put("DELETE", GLFW.GLFW_KEY_DELETE);
        keyMap.put("HOME", GLFW.GLFW_KEY_HOME);
        keyMap.put("END", GLFW.GLFW_KEY_END);
        keyMap.put("PAGE_UP", GLFW.GLFW_KEY_PAGE_UP);
        keyMap.put("PAGE_DOWN", GLFW.GLFW_KEY_PAGE_DOWN);
        keyMap.put("INSERT", GLFW.GLFW_KEY_INSERT);

        for (Map.Entry<String, Integer> entry : keyMap.entrySet()) {
            reverseKeyMap.put(entry.getValue(), entry.getKey());
        }
    }

    public static int getKeyCode(String key) {

        return keyMap.getOrDefault(key.toUpperCase(), -1);
    }

    public static String getKeyName(int code) {

        return reverseKeyMap.get(code);
    }
}
