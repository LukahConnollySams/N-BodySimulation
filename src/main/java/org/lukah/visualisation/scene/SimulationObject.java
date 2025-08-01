package org.lukah.visualisation.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lukah.visualisation.graphics.shapes.Mesh;
import org.lukah.visualisation.graphics.shapes.TrailLine;

public class SimulationObject {

    private final Mesh mesh;
    // Texture texture; not implemented yet
    private final Vector3f colour;
    private final float radius;
    private TrailLine trail;
    private Matrix4f transform;

    public SimulationObject(Mesh mesh, Vector3f position, float radius, Vector3f colour) {

        this.mesh = mesh;
        this.radius = radius;
        this.colour = colour;
        update(position);
    }

    public void bindTrail(TrailLine trail) {
        this.trail = trail;
    }

    public Vector3f getColour() {
        return colour;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public TrailLine getTrail() {
        return trail;
    }

    public Matrix4f getTransform() {
        return transform;
    }

    public void update(Vector3f updatedPos) {

        transform = new Matrix4f().translate(updatedPos).scale(radius);
        if (trail != null) {
            trail.addPoint(updatedPos);
            trail.update();
            trail.upload();
        }
    }

    public void cleanup() {
        mesh.cleanup();
    }
}
