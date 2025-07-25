package org.lukah.visualisation.scene;

import org.joml.Vector3f;
import org.lukah.visualisation.graphics.Light;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scene {

    List<SimulationObject> objects = new ArrayList<>();
    Light ambientLight = new Light(
            new Vector3f(-1f, -1f, -1f),
            new Vector3f(1f, 1f, 1f),
            new Vector3f(0.2f, 0.2f, 0.2f)
    );


    public void addObject(SimulationObject object) {
        objects.add(object);
    }

    public void addObjects(SimulationObject[] objects) {

        this.objects.addAll(Arrays.asList(objects));
    }

    public List<SimulationObject> getObjects() {
        return objects;
    }

    public Light getAmbientLight() {
        return ambientLight;
    }

    public void updateObjects(List<Vector3f> updatedPos) {

        for (int i = 0; i < objects.size(); i++) {

            if (i < updatedPos.size()){

                objects.get(i).update(updatedPos.get(i));
            }
        }
    }

    public void cleanup() {

        for (SimulationObject object : objects) {

            object.cleanup();
        }
    }
}
