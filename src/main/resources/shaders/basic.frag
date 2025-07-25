#version 330 core

in vec3 mvVertexPosition;
in vec3 mvVertexNormal;

uniform vec3 lightDir;
uniform vec3 ambientColour;
uniform vec3 lightColour;
uniform vec3 objectColour;

out vec4 FragColor;

void main() {

    //Ambient
    vec3 ambient = ambientColour * objectColour;

    // Diffuse
    vec3 norm = normalize(mvVertexNormal);
    vec3 lightDirection = normalize(-lightDir);
    float diff = max(dot(norm, lightDirection), 0.0);
    vec3 diffuse = diff * lightColour * objectColour;

    vec3 finalColour = ambient + diffuse;

    FragColor = vec4(finalColour, 1.0); // orange color
}