package org.lukah.visualisation.app;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lukah.config.Settings;
import org.lukah.visualisation.graphics.Shader;
import org.lukah.visualisation.graphics.Texture;
import org.lukah.visualisation.graphics.shapes.TrailLine;
import org.lukah.visualisation.graphics.shapes.Mesh;
import org.lukah.visualisation.scene.Camera;
import org.lukah.visualisation.scene.Scene;
import org.lukah.visualisation.scene.SimulationObject;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20C.glGetUniformLocation;

public class Renderer {

    private Shader objectShader;
    private Shader trailShader;
    public Camera camera;
    private Settings.RenderSettings settings;

    public Renderer(Shader objectShader, Shader trailShader, Camera camera, Settings.RenderSettings settings) {

        this.objectShader = objectShader;
        this.trailShader = trailShader;
        this.camera = camera;
        this.settings = settings;
    }

    public void renderScene(Scene scene) {

        glClearColor(settings.clearColour[0], settings.clearColour[1], settings.clearColour[2], settings.clearColour[3]);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);

        objectShader.bind();

        int objectLocation = glGetUniformLocation(objectShader.getProgramID(), "objectColour");
        if (objectLocation == -1) {
            System.err.println("Uniform 'objectColour' not found!");
        } else {
            glUniform3f(objectLocation, settings.defaultObjectColour[0], settings.defaultObjectColour[1], settings.defaultObjectColour[2]);
        }

        objectShader.setUniform("viewProj", camera.getViewProjection());
        objectShader.setUniform("lightDir", scene.getAmbientLight().getDirection());
        objectShader.setUniform("ambientColour", scene.getAmbientLight().getAmbientColour());
        objectShader.setUniform("lightColour", scene.getAmbientLight().getColour());

        for (SimulationObject object : scene.getObjects()) {

            render(object.getMesh(), object.getTexture(), object.getTransform(), object.getColour(), object.isUseTexture());
        }
        objectShader.unbind();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        trailShader.bind();

        int trailLocation = glGetUniformLocation(trailShader.getProgramID(), "trailColour");
        if (trailLocation == -1) {
            System.err.println("Uniform 'trailColour' not found!");
        } else {
            glUniform3f(trailLocation, settings.trailColour[0], settings.trailColour[1], settings.trailColour[2]);
        }

        trailShader.setUniform("viewProj", camera.getViewProjection());
        trailShader.setUniform("model", new Matrix4f());

        for (SimulationObject object : scene.getObjects()) {

            render(object.getTrail());
        }

        trailShader.unbind();

        glDisable(GL_BLEND);
    }

    public void render(Mesh mesh, Texture texture, Matrix4f model, Vector3f colour, boolean useTexture) {

        objectShader.setUniform("model", model);
        objectShader.setUniform("objectColour", colour);

        glActiveTexture(GL_TEXTURE0);
        texture.bind();
        objectShader.setUniform("textureSampler", 0);
        objectShader.setUniform("useTexture", useTexture);

        mesh.bind();
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
        mesh.unbind();

        texture.unbind();
    }

    public void render(TrailLine line) {

        line.bind();
        glDrawArrays(GL_LINE_STRIP, 0, line.getVertexCount());
        line.unbind();
    }

    public void cleanup() {
        objectShader.delete();
        trailShader.delete();
    }
}
