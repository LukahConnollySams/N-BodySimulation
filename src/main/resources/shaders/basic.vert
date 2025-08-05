#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 vertexNormal;
layout(location = 2) in vec2 texCoord;

out vec3 mvVertexPosition;
out vec3 mvVertexNormal;
out vec2 fragTexCoord;

uniform mat4 model;
uniform mat4 viewProj;

void main() {

    fragTexCoord = texCoord;

    mvVertexPosition = vec3(model * vec4(position, 1.0));
    mvVertexNormal = mat3(transpose(inverse(model))) * vertexNormal;

    gl_Position = viewProj * model * vec4(position, 1.0);
}