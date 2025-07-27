#version 330 core

in float vAlpha;

out vec4 FragColour;

uniform vec3 trailColour;

void main() {

    FragColour = vec4(trailColour, vAlpha);
}