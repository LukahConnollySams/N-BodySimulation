#version 330 core

in vec3 mvVertexPosition;
in vec3 mvVertexNormal;
in vec2 fragTexCoord;

uniform vec3 lightDir;
uniform vec3 ambientColour;
uniform vec3 lightColour;
uniform vec3 objectColour;
uniform sampler2D textureSampler;
uniform bool useTexture;

out vec4 FragColour;

void main() {

    vec3 baseColour = useTexture
        ? texture(textureSampler, fragTexCoord).rgb
        : objectColour;

    vec3 materialColour = useTexture ? vec3(1.0) : objectColour;

    //Ambient
    vec3 ambient = ambientColour * materialColour;

    // Diffuse
    vec3 norm = normalize(mvVertexNormal);
    vec3 lightDirection = normalize(-lightDir);
    float diff = max(dot(norm, lightDirection), 0.0);
    vec3 diffuse = diff * lightColour * materialColour;

    vec3 finalColour = ambient + diffuse;

    FragColour = vec4(finalColour * baseColour, 1.0);
}