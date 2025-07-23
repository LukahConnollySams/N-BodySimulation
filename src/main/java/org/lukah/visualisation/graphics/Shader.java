package org.lukah.visualisation.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private final int programID;

    public Shader(String vertexPath, String fragmentPath) throws IOException {

        String vertexCode = readShaders(vertexPath);
        String fragmentCode = readShaders(fragmentPath);

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

    private String readShaders(String path) {

        try (InputStream stream = getClass().getResourceAsStream(path)) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            return reader.lines().collect(Collectors.joining("\n"));

        } catch (IOException e) {

            System.err.println("Could not find file named: " + e.getMessage());
            return "";
        }
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
