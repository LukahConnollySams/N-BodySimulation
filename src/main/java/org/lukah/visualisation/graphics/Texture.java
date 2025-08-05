package org.lukah.visualisation.graphics;

import org.lukah.config.ConfigLoader;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {

    private int textureID;

    public Texture(String path) {
        this.textureID = load(path);
    }

    public int load(String path) {

        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        try (MemoryStack stack = MemoryStack.stackPush()) {

            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            ByteBuffer image = STBImage.stbi_load(path, width, height, comp, 4);

            if (image == null) {
                throw new RuntimeException("Failed to load image" + path + "\n" + STBImage.stbi_failure_reason());
            }

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

            glGenerateMipmap(GL_TEXTURE_2D);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);


            STBImage.stbi_image_free(image);
            return id;
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private static Path getProjectDir() {

        try {

            Path path = Paths.get(ConfigLoader.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI());

            if (path.toString().endsWith(".jar")) {

                return path.getParent();

            } else {

                // used for development environment
                return Paths.get(System.getProperty("user.dir")).getParent();
            }

        } catch (Exception e) {

            throw new RuntimeException("Could not get Project Directory: " + e);
        }
    }

    public static String getDefaultPath() {

        Path projectPath = getProjectDir();

        Path configPath = projectPath
                .resolve("N-Body Simulation Configs")
                .resolve("white-pixel.jpg");

        return configPath.toString();
    }

    public static String getDefaultsPath() {
        Path projectPath = getProjectDir();

        Path configPath = projectPath
                .resolve("N-Body Simulation Configs");

        return configPath.toString() + "\\";
    }
}
