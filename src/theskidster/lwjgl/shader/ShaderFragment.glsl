#version 330 core

in vec2 ioTexCoords;

uniform sampler2D tex;

out vec4 ioResult;

void main() {
    if(texture(tex, ioTexCoords).a == 0) discard; //if our texture has transparency keep it that way.
    ioResult = texture(tex, ioTexCoords);
}