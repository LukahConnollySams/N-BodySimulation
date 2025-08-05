package org.lukah.physics.simulation.planetary;

import org.joml.Vector3f;
import java.util.ArrayList;

public class Body {

    final String name;
    final String colour;
    final float mass;
    final float radius;
    Vector3f[] pos;
    Vector3f vel;
    ArrayList<Vector3f> acc;
    float timeStep;


    Body(String name, String colour, float mass, float radius, Vector3f[] pos, Vector3f vel) {

        this.name = name;
        this.colour = colour;
        this.mass = mass;
        this.radius = radius;
        this.pos = pos;
        this.vel = vel;

        this.acc = new ArrayList<>();
        acc.add(new Vector3f(0f, 0f, 0f));
        acc.add(new Vector3f(0f, 0f, 0f));
        acc.add(new Vector3f(0f, 0f, 0f));
    }

    public String toString() {

        return String.format("%s: \nMass: %f\nRadius: %f\nPosition: (%f, %f, %f)", this.name, this.mass, this.radius, this.pos[1].x, this.pos[1].y, this.pos[1].z);
    }

    public String getColour() {
        return colour;
    }

    public Vector3f getCurrentPosition() {
        return pos[1];
    }

    public String getName() {
        return name;
    }

    public float getRadius() {
        return radius;
    }

    public void setTimeStep(float timeStep) {
        this.timeStep = timeStep;
    }

    public Vector3f updatePosition() {

        // calculate terms in a way that avoids incorrect mutations
        pos[0] = new Vector3f(pos[1]);
        Vector3f velTerm = new Vector3f(vel).mul(timeStep);
        Vector3f accTerm = new Vector3f(acc.get(1)).mul(4f).sub(acc.get(0)).mul(1f/6f);

        accTerm.mul(timeStep * timeStep);

        pos[1].add(velTerm).add(accTerm);

        return pos[1];
    }

    public Vector3f updateVelocity() {

        // calculate terms in a way that avoids incorrect mutations
        Vector3f a1Term = new Vector3f(acc.get(2)).mul(2);
        Vector3f a2Term = new Vector3f(acc.get(1)).mul(5);

        Vector3f accTerm = a1Term.add(a2Term).sub(acc.get(0)).mul(1f/6f * timeStep);

        vel = vel.add(accTerm);

        return vel;
    }

}
