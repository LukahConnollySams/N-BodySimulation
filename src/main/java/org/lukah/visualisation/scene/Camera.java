package org.lukah.visualisation.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private Matrix4f projection;
    private Matrix4f view;
    private Matrix4f viewProjection;

    private Vector3f position;
    private Vector3f target;
    private Vector3f up;

    float speed;

    public Camera(float aspectRatio) {

        projection = new Matrix4f().perspective((float) Math.toRadians(60.0f), aspectRatio, 0.01f, 100f);
        view = new Matrix4f();
        viewProjection = new Matrix4f();

        position = new Vector3f(0, -3.5f, 0.075f);
        target = new Vector3f(0, 0, 0);
        up = new Vector3f(0, 1f, 0);

        speed = 0.1f;

        updateViewProjection();
    }

    public Matrix4f getViewProjection() {
        return viewProjection;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getTarget() {
        return target;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    private void addPosition(Vector3f position) {
        this.position.add(new Vector3f(position).mul(speed));
    }

    private void addTarget(Vector3f target) {
        this.target.add(new Vector3f(target).mul(speed));

    }

    public void move(Vector3f vector, float speed) {

        setSpeed(speed);
        addPosition(vector);
        addTarget(vector);
        updateViewProjection();
    }

    private void updateViewProjection() {
        view.identity().lookAt(position, target, up);
        viewProjection.set(projection).mul(view);
    }
}
