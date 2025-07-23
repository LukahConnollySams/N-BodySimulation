package org.lukah.visualisation.graphics.shapes;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public abstract class Mesh {

    protected int vao, vbo, ebo;
    protected int vertexCount;

    public int getVertexCount() {
        return vertexCount;
    }

    public void bind() {
        glBindVertexArray(vao);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void cleanup() {

        if (vbo != 0) glDeleteBuffers(vbo);
        if (ebo != 0) glDeleteBuffers(ebo);
        if (vao != 0) glDeleteVertexArrays(vao);
    }
}
