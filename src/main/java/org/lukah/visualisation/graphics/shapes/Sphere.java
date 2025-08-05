package org.lukah.visualisation.graphics.shapes;

import org.joml.Vector3f;
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

        int[] indexArray = indices.stream().mapToInt(Integer::intValue).toArray();
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indexArray.length);
        indexBuffer.put(indexArray).flip();

        vertexCount = indexArray.length;

        List<Float> normals = genNormals(vertices);

        float[] vertexArray = genVertexArray(vertices, normals);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // vertices
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // normals
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // texture coordinates
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);

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
                float y = sinTheta * sinPhi;
                // float z = cosTheta;

                vertices.add(x);
                vertices.add(y);
                vertices.add(cosTheta);

                float u = (float) j / lonSegments;
                float v = (float) i / latSegments;

                vertices.add(u);
                vertices.add(v);
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

    private float[] genVertexArray(List<Float> vertices, List<Float> normals) {

        int verticesSize = vertices.size() / 5;

        float[] vertexArray = new float[verticesSize * 8];

        for (int i = 0; i < verticesSize; i++) {

            // assigns first three floats to positions, second set to normals, and last 2 to uv
            vertexArray[i * 8] = vertices.get(i * 5);
            vertexArray[i * 8 + 1] = vertices.get(i * 5 + 1);
            vertexArray[i * 8 + 2] = vertices.get(i * 5 + 2);

            vertexArray[i * 8 + 3] = normals.get(i * 3);
            vertexArray[i * 8 + 4] = normals.get(i * 3 + 1);
            vertexArray[i * 8 + 5] = normals.get(i * 3 + 2);

            vertexArray[i * 8 + 6] = vertices.get(i * 5 + 3);
            vertexArray[i * 8 + 7] = vertices.get(i * 5 + 4);
        }

        return vertexArray;
    }

    private List<Float> genNormals(List<Float> vertices) {

        List<Float> normals = new ArrayList<>();

        for (int i = 0; i < vertices.size(); i += 5) {

            Vector3f vector = new Vector3f(
                    vertices.get(i),
                    vertices.get(i + 1),
                    vertices.get(i + 2)
            ).normalize();

            normals.add(vector.x);
            normals.add(vector.y);
            normals.add(vector.z);
        }

        return normals;
    }
}
