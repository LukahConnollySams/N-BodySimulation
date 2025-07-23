package org.lukah.visualisation.scene;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scene {

    List<SimulationObject> objects = new ArrayList<>();


    public void addObject(SimulationObject object) {
        objects.add(object);
    }

    public void addObjects(SimulationObject[] objects) {

        this.objects.addAll(Arrays.asList(objects));
    }

    public List<SimulationObject> getObjects() {
        return objects;
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
