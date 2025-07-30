package org.lukah.visualisation.scene;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lukah.config.Settings;

public class Camera implements CameraController{

    private final Matrix4f projection;
    private Matrix4f view;
    private Matrix4f viewProjection;


    private Vector3f direction;
    private Vector3f position;
    private Vector3f target;
    private Vector3f up;
    private final Vector3f worldUp = new Vector3f(0, 0, 1);
    private final Vector3f worldRight = new Vector3f(1, 0, 0);

    private Quaternionf rotation;

    private float yawAngle;
    private float pitchAngle;
    private final float horizontalSensitivity;
    private final float verticalSensitivity;
    private Boolean canRotate = false;

    float moveSpeed;

    public Camera(float aspectRatio, Settings.CameraSettings settings) {

        projection = new Matrix4f().perspective((float) Math.toRadians(settings.fov), aspectRatio, settings.zNear, settings.zFar);
        view = new Matrix4f();
        viewProjection = new Matrix4f();

        position = new Vector3f(settings.initPosition);
        target = new Vector3f(settings.initTarget);
        up = new Vector3f(0, 1f, 0);

        this.horizontalSensitivity = settings.horizontalSensitivity;
        this.verticalSensitivity = settings.verticalSensitivity;

        Matrix4f lookAtMat = new Matrix4f().lookAt(position, target, up);
        rotation = new Quaternionf().setFromNormalized(lookAtMat.invert());

        Vector3f forwardDir = rotation.transform(new Vector3f(0, 1, 0), new Vector3f());
        yawAngle = (float) Math.toDegrees(Math.atan2(forwardDir.x, forwardDir.y));
        pitchAngle = (float) Math.toDegrees(Math.asin(forwardDir.z));

        moveSpeed = settings.moveSpeed;

        updateCameraVectors();
        updateViewProjection();
    }

    public Matrix4f getViewProjection() {
        return viewProjection;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    @Override
    public void setCanRotate(boolean canRotate) {
        this.canRotate = canRotate;
    }

    public Boolean isRotationEnabled() {
        return canRotate;
    }

    private void addPosition(Vector3f position) {
        this.position.add(new Vector3f(position).mul(moveSpeed));
    }

    private void addTarget(Vector3f target) {
        this.target.add(new Vector3f(target).mul(moveSpeed));
    }

    public void move(Vector3f vector, float speed) {

        setMoveSpeed(speed);
        addPosition(vector);
        addTarget(vector);
        updateViewProjection();
    }

    public void rotate(Vector2f cursorDiff) {

        if (!canRotate) return;

        yawAngle += cursorDiff.x * horizontalSensitivity;
        pitchAngle += cursorDiff.y * verticalSensitivity;

        updateCameraVectors();
        updateViewProjection();
    }

    public void updateCameraVectors() {

        float yawRad = (float) Math.toRadians(yawAngle);
        float pitchRad = (float) Math.toRadians(pitchAngle);

        Quaternionf yawQuat = new Quaternionf().rotateAxis(yawRad, worldUp);

        Vector3f localRight = yawQuat.transform(new Vector3f(worldRight));

        Quaternionf pitchQuat = new Quaternionf().rotateAxis(pitchRad, localRight);

        rotation = new Quaternionf(pitchQuat).mul(yawQuat).normalize();

        direction = rotation.transform(new Vector3f(0, 1, 0), new Vector3f());
        up = rotation.transform(new Vector3f(worldUp), new Vector3f());
    }

    private void updateViewProjection() {

        target.set(position).add(direction);

        Quaternionf invRot = new Quaternionf(rotation).conjugate();
        view.identity()
                .rotate(invRot)
                .translate(position.negate(new Vector3f()));

        viewProjection.set(projection).mul(view);
    }
}
