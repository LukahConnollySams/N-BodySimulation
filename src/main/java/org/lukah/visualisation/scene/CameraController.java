package org.lukah.visualisation.scene;

import org.joml.Vector2f;
import org.joml.Vector3f;

public interface CameraController {

    void move(Vector3f vector, float speed);

    void rotate(Vector2f cursorDiff);

    void setCanRotate(boolean b);
}
