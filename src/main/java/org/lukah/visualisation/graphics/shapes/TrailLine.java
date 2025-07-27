package org.lukah.visualisation.graphics.shapes;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class TrailLine extends Mesh {

    private List<TrailPoint> points = new ArrayList<>();
    float lifeTime;
    int length;


    public TrailLine(float lifeTime, int length) {

        this.lifeTime = lifeTime;
        this.length = length;

        build();
    }

    public void build() {

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        int maxFloats = length * 4;

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, maxFloats * Float.BYTES, GL_DYNAMIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 1, GL_FLOAT, false, 4 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);
    }

    public void addPoint(Vector3f position) {

        points.add(new TrailPoint(position, lifeTime));
        if (points.size() > length) {
            points.removeFirst();
        }
    }

    public void update() {
        this.update(20);
    }

    public void update(float time) {
        points.removeIf(point -> (point.life -= time) <=0);
    }

    public void upload() {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(points.size() * 4);

        for (TrailPoint point : points) {

            float alpha = point.life / lifeTime;
            buffer.put(point.position.x)
                    .put(point.position.y)
                    .put(point.position.z)
                    .put(alpha);
        }

        buffer.flip();

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
        vertexCount = points.size();
    }


    private static class TrailPoint {

        Vector3f position;
        float life;

        TrailPoint(Vector3f position, float life) {

            this.position = new Vector3f(position);
            this.life = life;
        }
    }
}
