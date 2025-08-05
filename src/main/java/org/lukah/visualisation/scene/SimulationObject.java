package org.lukah.visualisation.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lukah.visualisation.graphics.Texture;
import org.lukah.visualisation.graphics.shapes.Mesh;
import org.lukah.visualisation.graphics.shapes.TrailLine;

public class SimulationObject {

    private final Mesh mesh;
    private Texture texture;
    private boolean useTexture;
    private final Vector3f colour;
    private final float radius;
    private TrailLine trail;
    private Matrix4f transform;

    public SimulationObject(Mesh mesh, Vector3f position, float radius, Vector3f colour, String texturePath) {

        this.mesh = mesh;
        this.radius = radius;
        this.colour = colour;

        try {

            this.texture = new Texture(texturePath);
            this.useTexture = true;

        } catch (RuntimeException e) {

            System.out.println("Could not find texture in path: " + e.getMessage());
            System.out.println("Loading default texture");
            this.texture = new Texture((Texture.getDefaultPath()));
            this.useTexture = false;
        }

        if (this.texture == null) {
            throw new RuntimeException("Failed to create texture or load default");
        }

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

    public Texture getTexture() {
        return texture;
    }

    public TrailLine getTrail() {
        return trail;
    }

    public Matrix4f getTransform() {
        return transform;
    }

    public boolean isUseTexture() {
        return useTexture;
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
