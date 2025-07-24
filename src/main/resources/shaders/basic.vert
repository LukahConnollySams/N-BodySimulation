#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 vertexNormal;

out vec3 mvVertexPosition;
out vec3 mvVertexNormal;

uniform mat4 model;
uniform mat4 viewProj;

void main() {

    mvVertexPosition = vec3(model * vec4(position, 1.0));
    mvVertexNormal = mat3(transpose(inverse(model))) * vertexNormal;

    gl_Position = viewProj * vec4(mvVertexPosition, 1.0);
}