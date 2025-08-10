package org.lukah.visualisation.app;

import org.lukah.config.ConfigLoader;
import org.lukah.config.Settings;
import org.lukah.physics.engine.Engine;
import org.lukah.visualisation.graphics.Shader;
import org.lukah.visualisation.graphics.shapes.TrailLine;
import org.lukah.visualisation.input.KeyInputManager;
import org.lukah.visualisation.input.MouseInputManager;
import org.lukah.visualisation.scene.Camera;
import org.lukah.visualisation.scene.Scene;
import org.lukah.visualisation.scene.SimulationObject;

import java.io.IOException;

import static org.lukah.visualisation.util.Conversions.metersToAU;

public class Application {

    private WindowManager windowManager;
    private Renderer renderer;
    private Scene scene;
    private Engine engine;
    private Camera camera;
    private KeyInputManager keyInputManager;
    private int frameRate;

    Settings settings;

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public void run(){

        try {

            this.settings = ConfigLoader.load();

        } catch (IOException e) {

            System.err.println(e.getMessage());
            return;
        }

        this.frameRate = settings.appSettings.frameRate;

        this.engine = new Engine(settings.engineSettings);
        this.scene = new Scene(settings.sceneSettings);

        this.windowManager = new WindowManager(settings.windowSettings);
        windowManager.init();

        this.camera = new Camera(currentAspectRatio(), settings.cameraSettings);
        this.keyInputManager = new KeyInputManager(engine, camera, settings.keyBindings, settings.cameraSettings);

        windowManager.setInputManager(keyInputManager);
        windowManager.setMouseInputManager(new MouseInputManager(camera, settings.mouseButtonBindings));
        windowManager.initCallbacks();


        try {

            this.renderer = new Renderer(
                    new Shader("/shaders/basic.vert","/shaders/basic.frag"),
                    new Shader("/shaders/trail.vert", "/shaders/trail.frag"),
                    camera,
                    settings.renderSettings);

        } catch (IOException e) {

            System.out.println("Couldn't load the renderer: \n" + e.getMessage());
            e.printStackTrace();
            return;
        }

        SimulationObject[] simObjects = engine.getSimObjects(settings.shapeSettings.sphereSettings.resolution);

        for (SimulationObject object : simObjects) {

            object.bindTrail(new TrailLine(settings.trailSettings.lifeTime, settings.trailSettings.length));
            scene.addObject(object);
        }

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

            keyInputManager.executeKeys();
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
        app.run();
    }
}
