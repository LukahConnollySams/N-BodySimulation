package org.lukah.visualisation.graphics;

import org.joml.Vector3f;

public class Light {

    private Vector3f direction, colour, ambientColour;

    public Light(Vector3f direction, Vector3f colour, Vector3f ambientColour) {

        this.direction = direction.normalize();
        this.colour = colour;
        this.ambientColour = ambientColour;
    }

    public Vector3f getAmbientColour() {
        return ambientColour;
    }

    public Vector3f getColour() {
        return colour;
    }

    public Vector3f getDirection() {
        return direction;
    }

}
