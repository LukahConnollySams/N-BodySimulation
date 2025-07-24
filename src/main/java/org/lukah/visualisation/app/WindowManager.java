package org.lukah.visualisation.app;

import org.lukah.visualisation.input.InputManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLDebugMessageCallback;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;
import static org.lwjgl.opengl.GL43.glDebugMessageCallback;

public class WindowManager {

    private long window;
    private int width, height;
    private final String title = "Simulation";

    private InputManager inputManager;
    private GLFWKeyCallback keyCallback;
    private GLDebugMessageCallback debugCallback;

    public WindowManager() {

    }

    public void init() {

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_DEPTH_BITS, 24);

        long monitor = GLFW.glfwGetPrimaryMonitor();

        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(monitor);

        if (videoMode == null) {

            width = 800;
            height = 600;

        } else {

            width = videoMode.width();
            height = videoMode.height();
        }

        window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);

        if (window == MemoryUtil.NULL) {

            throw new RuntimeException("Failed to create GLFW Window");
        }

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwMaximizeWindow(window);

        GL.createCapabilities();

        GLFW.glfwSwapInterval(1);

        GLFW.glfwShowWindow(window);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    public void initCallbacks() {

        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                inputManager.handleKeyEvent(key, mods, action);
            }};
        GLFW.glfwSetKeyCallback(window, keyCallback);

        glEnable(GL_DEBUG_OUTPUT);
        debugCallback = GLDebugMessageCallback.create((source, type, id, severity, length, message, userParam) -> {
            String msg = GLDebugMessageCallback.getMessage(length, message);
            System.err.println("[GL DEBUG] " + msg);
        });
        glDebugMessageCallback(debugCallback, 0);
    }

    public boolean shouldClose() {

        return GLFW.glfwWindowShouldClose(window);
    }

    public void swapBuffers() {
        GLFW.glfwSwapBuffers(window);
    }

    public void pollEvents() {
        GLFW.glfwPollEvents();
    }

    public void cleanup() {

        if (keyCallback != null) keyCallback.free();
        debugCallback.free();
    }

    public void close() {

        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }
}
