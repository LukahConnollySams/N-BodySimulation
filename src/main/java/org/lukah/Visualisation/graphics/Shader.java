package org.lukah.Visualisation.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private final int programID;

    public Shader(String vertexPath, String fragmentPath) throws IOException {

        String vertexCode = Files.readString(Paths.get("src/main/resources"  + vertexPath));
        String fragmentCode = Files.readString(Paths.get("src/main/resources"  + fragmentPath));

        int vertexID = compileShader(vertexCode, GL_VERTEX_SHADER);
        int fragmentID = compileShader(fragmentCode, GL_FRAGMENT_SHADER);

        programID = glCreateProgram();
        glAttachShader(programID, vertexID);
        glAttachShader(programID, fragmentID);

        glLinkProgram(programID);

        if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Shader link error:\n" + glGetProgramInfoLog(programID));
        }

        glDeleteShader(vertexID);
        glDeleteShader(fragmentID);
    }

    public int getProgramID() {
        return programID;
    }

    public void setUniform(String name, Matrix4f matrix) {

        int loc = glGetUniformLocation(programID, name);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);
            matrix.get(buffer);
            glUniformMatrix4fv(loc, false, buffer);
        }
    }

    public void setUniform(String name, Vector3f vector) {

        int location = glGetUniformLocation(programID, name);
        glUniform3f(location, vector.x, vector.y, vector.z);
    }

    private int compileShader(String code, int type) {
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, code);
        glCompileShader(shaderID);

        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Shader compile error:\n" + glGetShaderInfoLog(shaderID));
        }

        return shaderID;
    }

    public void bind() {
        glUseProgram(programID);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void delete() {
        glDeleteProgram(programID);
    }
}
