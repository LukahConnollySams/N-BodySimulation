package org.lukah.visualisation.app;

import org.lukah.config.Settings;
import org.lukah.visualisation.input.KeyInputManager;
import org.lukah.visualisation.input.MouseInputManager;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLDebugMessageCallback;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;
import static org.lwjgl.opengl.GL43.glDebugMessageCallback;

public class WindowManager {

    private long window;
    private int width, height;
    private final String title = "Simulation";

    Settings.WindowSettings settings;

    private KeyInputManager keyInputManager;
    private MouseInputManager mouseInputManager;
    private GLFWKeyCallback keyCallback;
    private GLFWFramebufferSizeCallback frameBufferCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    private GLFWCursorEnterCallback cursorEnterCallback;
    private GLFWCursorPosCallback cursorPosCallback;
    private GLDebugMessageCallback debugCallback;

    public WindowManager(Settings.WindowSettings settings) {
        this.settings = settings;
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

            width = settings.defaultWidth;
            height = settings.defaultHeight;

        } else {

            width = videoMode.width();
            height = videoMode.height();
        }

        window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);

        if (window == MemoryUtil.NULL) {

            throw new RuntimeException("Failed to create GLFW Window");
        }

        GLFW.glfwMakeContextCurrent(window);

        if (settings.launchFullscreen) {

            GLFW.glfwMaximizeWindow(window);
        }

        GL.createCapabilities();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer fbWidth = stack.mallocInt(1);
            IntBuffer fbHeight = stack.mallocInt(1);
            GLFW.glfwGetFramebufferSize(window, fbWidth, fbHeight);
            viewport(fbWidth.get(0), fbHeight.get(0));
        }

        GLFW.glfwSwapInterval(1);

        GLFW.glfwShowWindow(window);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setInputManager(KeyInputManager keyInputManager) {
        this.keyInputManager = keyInputManager;
    }

    public void setMouseInputManager(MouseInputManager mouseManager) {
        this.mouseInputManager = mouseManager;
    }

    public void initCallbacks() {

        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                keyInputManager.handleKeyEvent(key, mods, action);
            }};
        GLFW.glfwSetKeyCallback(window, keyCallback);

        glEnable(GL_DEBUG_OUTPUT);
        debugCallback = GLDebugMessageCallback.create((source, type, id, severity, length, message, userParam) -> {
            String msg = GLDebugMessageCallback.getMessage(length, message);
            System.err.println("[GL DEBUG] " + msg);
        });

        frameBufferCallback = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int fbWidth, int fbHeight) {
                viewport(fbWidth, fbHeight);
            }
        };

        GLFW.glfwSetFramebufferSizeCallback(window, frameBufferCallback);

        mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                mouseInputManager.handleMouseButtonEvent(button, mods, action);
            }
        };

        GLFW.glfwSetMouseButtonCallback(window, mouseButtonCallback);

        cursorPosCallback = new GLFWCursorPosCallback() {

            @Override
            public void invoke(long window, double xpos, double ypos) {
                mouseInputManager.handleMouseMove(xpos, ypos);
            }
        };

        GLFW.glfwSetCursorPosCallback(window, cursorPosCallback);

        cursorEnterCallback = new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long window, boolean entered) {
                mouseInputManager.setEntered(entered);
            }
        };

        GLFW.glfwSetCursorEnterCallback(window, cursorEnterCallback);

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

        keyCallback.free();
        frameBufferCallback.free();
        mouseButtonCallback.free();
        cursorEnterCallback.free();
        cursorPosCallback.free();
        debugCallback.free();
    }

    public void viewport(int fbWidth, int fbHeight) {

        glViewport(0, 0, fbWidth, fbHeight);

        this.width = fbWidth;
        this.height = fbHeight;
    }

    public void close() {

        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }
}
