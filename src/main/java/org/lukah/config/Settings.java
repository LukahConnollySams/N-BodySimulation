package org.lukah.config;

public class Settings {

    public static class AppSettings {

        public int frameRate;
    }

    public static class CameraSettings {

        public float fov;
        public float zNear;
        public float zFar;
        public float[] initPosition;
        public float[] initTarget;
        public float moveSpeed;
        public float fastMoveSpeed;
        public float horizontalSensitivity;
        public float verticalSensitivity;
    }

    public static class EngineSettings {

        public boolean startPaused;
        public float timeStep;
        public int length;
        public String setupFile;
    }

    public static class KeyBindings {

        public int pause;
        public int moveForward;
        public int moveLeft;
        public int moveBack;
        public int moveRight;
        public int moveUp;
        public int moveDown;
        public int moveFastMod;
        public int slowEngine;
        public int hasteEngine;

        public KeyBindings() {}
    }

    public static class MouseButtonBindings {
        public int toggleCamera;
    }

    public static class RenderSettings {

        public float[] clearColour;
        public float[] defaultObjectColour;
        public float[] trailColour;
    }

    public static class SceneSettings {

        public float[] lightDirection;
        public float[] lightColour;
        public float[] ambientColour;
    }

    public static class TrailSettings {

        public float lifeTime;
        public int length;
    }

    public static class ShapeSettings {

        public SphereSettings sphereSettings;

        public static class SphereSettings {
            public int resolution;
        }
    }

    public static class WindowSettings {

        public int defaultWidth;
        public int defaultHeight;
        public boolean launchFullscreen;
    }

    public AppSettings appSettings;
    public CameraSettings cameraSettings;
    public EngineSettings engineSettings;
    public KeyBindings keyBindings;
    public MouseButtonBindings mouseButtonBindings;
    public RenderSettings renderSettings;
    public SceneSettings sceneSettings;
    public ShapeSettings shapeSettings;
    public TrailSettings trailSettings;
    public WindowSettings windowSettings;
}
