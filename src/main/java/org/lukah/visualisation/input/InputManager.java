package org.lukah.visualisation.input;

import org.joml.Vector3f;
import org.lukah.physics.engine.EngineController;
import org.lukah.visualisation.scene.Camera;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class InputManager {

    private final Map<MultiKey, Runnable> keyBinds = new HashMap<>();

    public InputManager(EngineController controller, Camera camera) {

        registerEngineBindings(controller);

        registerMovementBindings(camera, 1.0f, 0);
        registerMovementBindings(camera, 0.01f, GLFW.GLFW_MOD_SHIFT);
    }

    public void bind(int key, int mods, Runnable action) {

        this.keyBinds.put(new MultiKey(key, mods), action);
    }

    public void registerEngineBindings(EngineController controller) {

        bind(GLFW.GLFW_KEY_ESCAPE, 0, controller::togglePause);
        bind(GLFW.GLFW_KEY_LEFT, 0, controller::engineSlowDown);
        bind(GLFW.GLFW_KEY_RIGHT, 0, controller::engineSpeedUp);
    }

    public void registerMovementBindings(Camera camera, float speed, int mods) {

        bind(GLFW.GLFW_KEY_W, mods, () -> camera.move(new Vector3f(0, 1f, 0), speed));
        bind(GLFW.GLFW_KEY_A, mods, () -> camera.move(new Vector3f(-1f, 0, 0), speed));
        bind(GLFW.GLFW_KEY_S, mods, () -> camera.move(new Vector3f(0, -1f, 0), speed));
        bind(GLFW.GLFW_KEY_D, mods, () -> camera.move(new Vector3f(1, 0, 0), speed));
        bind(GLFW.GLFW_KEY_SPACE, mods, () -> camera.move(new Vector3f(0, 0, 1f), speed));
        bind(GLFW.GLFW_KEY_C, mods, () -> camera.move(new Vector3f(0, 0, -1f), speed));
    }

    public void handleKeyEvent(int key, int mods, int action) {

        if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {

            Runnable command = this.keyBinds.get(new MultiKey(key, mods));
            if (command != null) command.run();
        }
    }
}
