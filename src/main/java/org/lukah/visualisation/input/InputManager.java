package org.lukah.visualisation.input;

import org.joml.Vector3f;
import org.lukah.config.Settings;
import org.lukah.physics.engine.EngineController;
import org.lukah.visualisation.scene.Camera;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class InputManager {

    private final Map<MultiKey, Runnable> keyBinds = new HashMap<>();
    private Settings.KeyBindings keyBindings;

    public InputManager(EngineController controller, Camera camera, Settings.KeyBindings keyBindings, Settings.CameraSettings cameraSettings) {

        this.keyBindings = keyBindings;

        registerEngineBindings(controller);

        registerMovementBindings(camera, 1.0f, 0);
        registerMovementBindings(camera, cameraSettings.fastMoveSpeed, keyBindings.moveFastMod);
    }

    public void bind(int key, int mods, Runnable action) {

        this.keyBinds.put(new MultiKey(key, mods), action);
    }

    public void registerEngineBindings(EngineController controller) {

        bind(keyBindings.pause, 0, controller::togglePause);
        bind(keyBindings.slowEngine, 0, controller::engineSlowDown);
        bind(keyBindings.hasteEngine, 0, controller::engineSpeedUp);
    }

    public void registerMovementBindings(Camera camera, float speed, int mods) {

        bind(keyBindings.moveForward, mods, () -> camera.move(new Vector3f(0, 1f, 0), speed));
        bind(keyBindings.moveLeft, mods, () -> camera.move(new Vector3f(-1f, 0, 0), speed));
        bind(keyBindings.moveBack, mods, () -> camera.move(new Vector3f(0, -1f, 0), speed));
        bind(keyBindings.moveRight, mods, () -> camera.move(new Vector3f(1, 0, 0), speed));
        bind(keyBindings.moveUp, mods, () -> camera.move(new Vector3f(0, 0, 1f), speed));
        bind(keyBindings.moveDown, mods, () -> camera.move(new Vector3f(0, 0, -1f), speed));
    }

    public void handleKeyEvent(int key, int mods, int action) {

        if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {

            Runnable command = this.keyBinds.get(new MultiKey(key, mods));
            if (command != null) command.run();
        }
    }
}
