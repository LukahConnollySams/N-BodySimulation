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
        public float turnSpeed;
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

    public static class WindowSettings {

        public int defaultWidth;
        public int defaultHeight;
        public boolean launchFullscreen;
    }

    public AppSettings appSettings;
    public CameraSettings cameraSettings;
    public EngineSettings engineSettings;
    public KeyBindings keyBindings;
    public WindowSettings windowSettings;
}
