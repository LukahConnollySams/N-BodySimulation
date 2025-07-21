package org.lukah.Visualisation.app;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lukah.Visualisation.graphics.Shader;
import org.lukah.Visualisation.graphics.shapes.Mesh;
import org.lukah.Visualisation.scene.Camera;
import org.lukah.Visualisation.scene.Scene;
import org.lukah.Visualisation.scene.SimulationObject;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20C.glGetUniformLocation;

public class Renderer {

    private Shader shader;
    public Camera camera;

    public Renderer(Shader shader, Camera camera) {
        this.shader = shader;
        this.camera = camera;

    }

    public void renderScene(Scene scene) {

        glClearColor(0f, 0f, 0f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);

        shader.bind();

        int location = glGetUniformLocation(shader.getProgramID(), "objectColour");
        if (location == -1) {
            System.err.println("Uniform 'objectColour' not found!");
        } else {
            glUniform3f(location, 0.6f, 0.3f, 0.2f);
        }

        shader.setUniform("viewProj", camera.getViewProjection());

        for (SimulationObject object : scene.getObjects()) {

            this.render(object.getMesh(), object.getTransform(), object.getColour());
        }
        shader.unbind();
    }

    public void render(Mesh mesh, Matrix4f model, Vector3f colour) {

        shader.setUniform("model", model);
        shader.setUniform("objectColour", colour);

        mesh.bind();
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
        mesh.unbind();
    }

    public void cleanup() {
        shader.delete();
    }
}
