#version 330 core

//vertex attributes specified explicitly.
layout (location = 0) in vec2 aPosition;
layout (location = 1) in vec2 aTexCoords;

//vertex attributes specified at runtime.
layout (location = 2) in vec2 aPosOffset;
layout (location = 3) in vec2 aTexOffset;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;

out vec2 ioTexCoords;

void main() {
    gl_Position = uProjection * uView * uModel * vec4(aPosition + aPosOffset, 0.0, 1.0);
    ioTexCoords = aTexCoords + aTexOffset;
}