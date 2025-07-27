package org.lukah.physics.engine;

import org.joml.Vector3f;
import org.lukah.config.Settings;
import org.lukah.physics.simulation.planetary.Body;
import org.lukah.physics.simulation.planetary.BodySim;
import org.lukah.visualisation.graphics.shapes.Sphere;
import org.lukah.visualisation.scene.SimulationObject;

import java.util.List;

import static org.lukah.visualisation.util.Conversions.*;

public class Engine implements EngineController{

    private boolean paused;
    private BodySim simulation;

    public Engine() {

        this.paused = false;
        this.simulation = new BodySim();
    }

    public Engine(Settings.EngineSettings settings) {

        this.paused = settings.startPaused;
        this.simulation = new BodySim(settings.setupFile, settings.timeStep, settings.length);
    }

    public SimulationObject[] getSimObjects(int resolution) {

        Body[] bodies = simulation.getBodies();

        SimulationObject[] simObjects = new SimulationObject[bodies.length];

        Sphere sphere = new Sphere(resolution, resolution);

        for (int i = 0; i < bodies.length; i++) {

            simObjects[i] = new SimulationObject(
                    sphere,
                    metersToAU(bodies[i].getCurrentPosition()), // AU conversion for better vertices precision
                    logScale(metersToAU(bodies[i].getRadius())), // log scaling need as relative sizes can be problematic
                    hexToNormRGB(bodies[i].getColour())
            );
        }

        return simObjects;
    }

    public List<Vector3f> getUpdatedPositions() {

        return simulation.getBodiesPositions();
    }

    public void update() {

            if (!paused && simulation.getTime() < simulation.getLength()) {
                simulation.update();
            }
    }

    public boolean isPaused() {

        return paused;
    }

    @Override
    public void togglePause(){
        this.paused = !this.paused;
    }

    @Override
    public void engineSlowDown() {

        if (!paused) {
            this.simulation.setTimeStep((float) (simulation.getTimeStep() * 0.9));
        }
    }

    @Override
    public void engineSpeedUp() {

        if (!paused) {
            this.simulation.setTimeStep((float) (simulation.getTimeStep() * 1.1));
        }
    }
}
