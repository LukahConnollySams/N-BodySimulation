package org.lukah.physics.simulation.planetary;

import org.joml.Vector3f;
import org.lukah.physics.math.Constants;
import org.lukah.physics.utils.MathParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;



public class BodySim {

    Body[] bodies;
    int length;
    float timeStep;
    int time;
    Vector3f[][] displacements;

    public BodySim(){
       this("/simulation/defaultSetup.txt", 18000, 40000);
    }

    public BodySim(String filePath, float timeStep, int length) {

        this.length = length;
        this.time = 0;
        this.timeStep = timeStep;

        //read from file or database
        try (InputStream stream = getClass().getResourceAsStream(filePath)) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            List<String> file = reader.lines().toList();

            List<Body> tempBodies = new ArrayList<>();
            for (String line : file) {
                if (!line.startsWith("#")) {
                    Body tempBody = bodyFromLine(line);
                    tempBody.setTimeStep(this.timeStep);
                    tempBodies.add(tempBody);
                }
            }

            bodies =  tempBodies.toArray(new Body[0]);

        } catch (IOException e) {
            System.out.println("Could not finds file named: " + e.getMessage());
        }

        this.acceleration(1);
    }

    public BodySim(Body[] bodies, float timeStep, int length) throws Exception {

        if (bodies.length < 2) {

            throw new Exception("Not enough bodies for simulation");
        }

        this.bodies = bodies;
        this.timeStep = timeStep;
        this.length = length;
        this.time = 0;

        this.acceleration(1);
    }

    public Body[] getBodies() {
        return bodies;
    }

    public double getLength() {
        return length;
    }

    public double getTime() {
        return time;
    }

    public float getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(float timeStep) {

        for (Body body : bodies) {
            body.setTimeStep(timeStep);
        }
        this.timeStep = timeStep;
    }

    public List<Vector3f> getBodiesPositions() {

        List<Vector3f> positions = new ArrayList<>();

        for (Body body : this.bodies) {

            positions.add(body.pos[1]);
        }

        return positions;
    }

    private void acceleration(int x) {

        this.displacements = new Vector3f[bodies.length][bodies.length];

        for (int i = 0; i < displacements.length; i++) {

            for (int j = 0; j < displacements.length; j++) {

                displacements[i][j] = new Vector3f(bodies[j].pos[1]).sub(bodies[i].pos[1]);
            }
        }

        for (int i = 0; i < bodies.length; i ++) {
            for (int j = 0; j < bodies.length; j ++) {

                if (i != j && displacements[i][j].length() > 1e-5) {

                    bodies[i].acc.get(x).add(new Vector3f(displacements[j][i])
                            .mul((float) (bodies[j].mass / Math.pow(displacements[j][i].length(), 3))));

                } else {

                    bodies[i].acc.get(x).add(new Vector3f(0f, 0f, 0f));
                }
            }
            bodies[i].acc.get(x).mul((float) (-1 * Constants.G));
        }
    }

    private void stepForward() {

        for (Body body: bodies) {

            body.updatePosition();
        }

        acceleration(2);

        for (Body body: bodies) {

            body.updateVelocity();
        }
    }

    private void cut() {

        // sliding window for accelerations
        for (Body body : bodies) {

            body.acc.addLast(new Vector3f(0f, 0f, 0f));
            body.acc.removeFirst();
        }
    }

    public void update() {

        this.stepForward();
        this.cut();

        time++;
    }

    private static Body bodyFromLine(String line) {

        String[] params = line.split(",");

        String name = params[0];
        String colour = params[1];

        float mass = MathParser.parseFloatOrExpr(params[2]);
        float radius = MathParser.parseFloatOrExpr(params[3]);

        Vector3f[] pos = new Vector3f[2];
        pos[0] = new Vector3f(0f, 0f, 0f);
        pos[1] = new Vector3f(
                MathParser.parseFloatOrExpr(params[4].replaceFirst("[(]", "")),
                MathParser.parseFloatOrExpr(params[5]),
                MathParser.parseFloatOrExpr(params[6].replaceFirst("\\)(?!.*\\))", ""))); // regex replaces final ')'

        Vector3f vel = new Vector3f(
                MathParser.parseFloatOrExpr(params[7].replaceFirst("[(]", "")),
                MathParser.parseFloatOrExpr(params[8]),
                MathParser.parseFloatOrExpr(params[9].replaceFirst("\\)(?!.*\\))", "")));

        return new Body(name, colour, mass, radius, pos, vel);
    }
}
