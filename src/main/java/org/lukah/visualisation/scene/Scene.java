package org.lukah.visualisation.scene;

import org.joml.Vector3f;
import org.lukah.config.Settings;
import org.lukah.visualisation.graphics.Light;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scene {

    List<SimulationObject> objects = new ArrayList<>();
    Light ambientLight;
    Settings.SceneSettings settings;

    public Scene(Settings.SceneSettings settings) {

        this.settings = settings;
        this.ambientLight = new Light(
            new Vector3f(settings.lightDirection),
            new Vector3f(settings.lightColour),
            new Vector3f(settings.ambientColour)
    );
    }

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
