package org.lukah.visualisation.input;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lukah.config.Settings;
import org.lukah.visualisation.scene.Camera;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class MouseInputManager {

    private final Vector2d prevPos;
    private final Vector2d currentPos;
    private final Vector2f posDiff;

    private boolean inWindow = false;

    private final Camera camera;

    private final Settings.MouseButtonBindings mouseButtonBindings;
    private final Map<MouseButtonEvent, Runnable> mouseBinds = new HashMap<>();

    public MouseInputManager(Camera camera, Settings.MouseButtonBindings mouseButtonBindings) {

        this.mouseButtonBindings = mouseButtonBindings;
        this.camera = camera;

        prevPos = new Vector2d();
        currentPos = new Vector2d();
        posDiff = new Vector2f();
    }

    public void bindButton(int button, int mods, Runnable action) {

        this.mouseBinds.put(new MouseButtonEvent(button, mods), action);
    }

    public void handleMouseMove(double xpos, double ypos) {

        if (inWindow && camera.isRotationEnabled()) {

            currentPos.x = xpos;
            currentPos.y = ypos;

            camera.rotate(calcPosDiff());

            prevPos.set(currentPos);

        } else {

            prevPos.set(xpos, ypos);
        }
    }

    public void handleMouseButtonEvent(int button, int mods, int action) {

        if (button == mouseButtonBindings.toggleCamera) {
            camera.setCanRotate(action == GLFW.GLFW_PRESS);
        }

        if (action == GLFW.GLFW_PRESS && inWindow) {
            Runnable command = this.mouseBinds.get(new MouseButtonEvent(button, mods));
            if (command != null) command.run();
        }
    }

    public void setEntered(Boolean isInWindow) {
        this.inWindow = isInWindow;
    }

    public Vector2f calcPosDiff() {

        this.posDiff.x = (float) (currentPos.x - prevPos.x);
        this.posDiff.y = (float) (currentPos.y - prevPos.y);

        return posDiff;
    }
}
