package org.lukah.Visualisation.graphics.shapes;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Sphere extends Mesh {

    private final int latSegments;
    private final int lonSegments;

    public Sphere(int latSegments, int lonSegments) {

        this.latSegments = latSegments;
        this.lonSegments = lonSegments;

        build();
    }

    public void build() {

        List<Float> vertices = genVertices();
        List<Integer> indices = genIndices();

        float[] vertexArray = genVertexArray(vertices);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        int[] indexArray = indices.stream().mapToInt(Integer::intValue).toArray();
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indexArray.length);
        indexBuffer.put(indexArray).flip();

        vertexCount = indexArray.length;
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        glBindVertexArray(0);
    }

    private List<Float> genVertices() {

        List<Float> vertices = new ArrayList<>();

        for (int i = 0; i <= latSegments; i++) {

            float theta = (float) (i * Math.PI / latSegments);
            float sinTheta = (float) Math.sin(theta);
            float cosTheta = (float) Math.cos(theta);

            for (int j = 0; j <= lonSegments; j++) {

                float phi = (float) (j * 2 * Math.PI / lonSegments);
                float sinPhi = (float) Math.sin(phi);
                float cosPhi = (float) Math.cos(phi);

                float x = sinTheta * cosPhi;
                // float y = cosTheta;
                float z = sinTheta * sinPhi;

                vertices.add(x);
                vertices.add(cosTheta);
                vertices.add(z);
            }
        }
        return vertices;
    }

    private List<Integer> genIndices() {

        List<Integer> indices = new ArrayList<>();

        // creates triangles out of a 2x2 of points
        for (int i = 0; i <= latSegments; i++) {
            for (int j = 0; j <= lonSegments; j++) {

                int first = i * (lonSegments + 1) + j;
                int second = first + lonSegments + 1;

                indices.add(first);
                indices.add(second);
                indices.add(first + 1);

                indices.add(second);
                indices.add(second + 1);
                indices.add(first + 1);
            }
        }

        return indices;
    }

    private float[] genVertexArray(List<Float> vertices) {

        float[] vertexArray = new float[vertices.size()];

        for (int i = 0; i < vertexArray.length; i++) {

            vertexArray[i] = vertices.get(i);
        }

        return vertexArray;
    }
}
