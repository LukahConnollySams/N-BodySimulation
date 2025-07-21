package org.lukah.Physics.engine;

import org.joml.Vector3f;
import org.lukah.Physics.simulation.planetary.Body;
import org.lukah.Physics.simulation.planetary.BodySim;
import org.lukah.Visualisation.graphics.shapes.Sphere;
import org.lukah.Visualisation.scene.SimulationObject;

import java.util.List;

import static org.lukah.Visualisation.util.Conversions.*;

public class Engine implements EngineController{

    private boolean paused = false;
    private BodySim simulation;

    public Engine() {
        this.simulation = new BodySim();
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

        if (paused) {
            this.simulation.setTimeStep(simulation.getTimeStep() * 0.9);
        }
    }

    @Override
    public void engineSpeedUp() {

        if (paused) {
            this.simulation.setTimeStep(simulation.getTimeStep() * 1.1);
        }
    }
}
