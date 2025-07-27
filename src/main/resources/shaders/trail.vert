#version 330 core

layout(location = 0) in vec3 pos;
layout(location = 1) in float alpha;

uniform mat4 viewProj;
uniform mat4 model;

out float vAlpha;

void main() {

    gl_Position = viewProj * model * vec4(pos, 1.0);
    vAlpha = alpha;
}