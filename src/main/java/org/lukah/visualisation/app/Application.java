package org.lukah.visualisation.app;

import org.lukah.physics.engine.Engine;
import org.lukah.visualisation.graphics.Shader;
import org.lukah.visualisation.input.InputManager;
import org.lukah.visualisation.scene.Camera;
import org.lukah.visualisation.scene.Scene;

import java.io.IOException;

import static org.lukah.visualisation.util.Conversions.metersToAU;

public class Application {

    private WindowManager windowManager;
    private Renderer renderer;
    private Scene scene;
    private Engine engine;
    private Camera camera;
    private int frameRate = 60;

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public void run(){

        this.engine = new Engine();
        this.scene = new Scene();

        this.windowManager = new WindowManager();
        windowManager.init();

        this.camera = new Camera(currentAspectRatio());
        windowManager.setInputManager(new InputManager(engine, camera));
        windowManager.initCallbacks();


        try {

            this.renderer = new Renderer(
                    new Shader("/shaders/basic.vert","/shaders/basic.frag"),
                    camera);

        } catch (IOException e) {

            System.out.println("Couldn't load the renderer: \n" + e.getMessage());
            e.printStackTrace();
            return;
        }

        scene.addObjects(engine.getSimObjects(32));

        // values below used to throttle rendering to match frame rate
        double secsPerUpdate = 1.0 / (frameRate * 0.6);
        double previous = getTime();
        double steps = 0.0;

        while (!windowManager.shouldClose()) {

            // values below used to throttle rendering to match frame rate
            double loopStartTime = getTime();
            double elapsed = loopStartTime - previous;
            previous = loopStartTime;
            steps += elapsed;

            // update engine until time for a frame has passed, then render
            while (steps >= secsPerUpdate) {
                engine.update();
                steps -= secsPerUpdate;
            }

            scene.updateObjects(metersToAU(engine.getUpdatedPositions()));

            renderer.renderScene(scene);

            // sleep until enough time for a frame has passed
            sync(loopStartTime);

            windowManager.swapBuffers();
            windowManager.pollEvents();
        }

        cleanup();
        windowManager.cleanup();
        windowManager.close();
    }

    public float currentAspectRatio() {

        return (float) windowManager.getWidth() / windowManager.getHeight();
    }

    private void sync(double loopStartTime) {

        double endTime = loopStartTime + (1d / frameRate);

        while (getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {}
        }
    }

    private void cleanup() {
        scene.cleanup();
        renderer.cleanup();
    }

    public double getTime() {

        return System.currentTimeMillis() / 1000d;
    }

    public static void main(String[] args) {

        Application app = new Application();
        app.setFrameRate(60);
        app.run();
    }
}
