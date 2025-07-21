#version 330 core

uniform vec3 objectColour;

out vec4 FragColor;

void main() {
    FragColor = vec4(objectColour, 0.8); // orange color
}