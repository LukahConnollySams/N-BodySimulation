package org.lukah.visualisation.input;

import org.joml.Vector3f;
import org.lukah.config.Settings;
import org.lukah.physics.engine.EngineController;
import org.lukah.visualisation.scene.Camera;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class KeyInputManager {

    private final Map<MultiKey, Runnable> keyBinds = new HashMap<>();
    private final Settings.KeyBindings keyBindings;

    public KeyInputManager(EngineController controller, Camera camera, Settings.KeyBindings keyBindings, Settings.CameraSettings cameraSettings) {

        this.keyBindings = keyBindings;

        registerEngineBindings(controller);

        registerMovementBindings(camera, 1.0f, 0);
        registerMovementBindings(camera, cameraSettings.fastMoveSpeed, keyBindings.moveFastMod);
    }

    public void bindKey(int key, int mods, Runnable action) {

        this.keyBinds.put(new MultiKey(key, mods), action);
    }

    public void registerEngineBindings(EngineController controller) {

        bindKey(keyBindings.pause, 0, controller::togglePause);
        bindKey(keyBindings.slowEngine, 0, controller::engineSlowDown);
        bindKey(keyBindings.hasteEngine, 0, controller::engineSpeedUp);
    }

    public void registerMovementBindings(Camera camera, float speed, int mods) {

        bindKey(keyBindings.moveForward, mods, () -> camera.move(new Vector3f(0, 1f, 0), speed));
        bindKey(keyBindings.moveLeft, mods, () -> camera.move(new Vector3f(-1f, 0, 0), speed));
        bindKey(keyBindings.moveBack, mods, () -> camera.move(new Vector3f(0, -1f, 0), speed));
        bindKey(keyBindings.moveRight, mods, () -> camera.move(new Vector3f(1, 0, 0), speed));
        bindKey(keyBindings.moveUp, mods, () -> camera.move(new Vector3f(0, 0, 1f), speed));
        bindKey(keyBindings.moveDown, mods, () -> camera.move(new Vector3f(0, 0, -1f), speed));
    }

    public void handleKeyEvent(int key, int mods, int action) {

        if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {

            Runnable command = this.keyBinds.get(new MultiKey(key, mods));
            if (command != null) command.run();
        }
    }
}