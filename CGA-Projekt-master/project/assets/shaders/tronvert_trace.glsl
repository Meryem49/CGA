#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textureCoordinate;
layout(location = 2) in vec3 normal;
layout(location = 3) in float time;

//uniforms
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 proj_matrix;

uniform vec2 tcMultiplier;

out struct VertexData
{
    vec2 textureCoordinate;
    vec3 normal;
    float time;
} vertexData;

void main(){
    mat4 modelview = view_matrix * model_matrix;
    vec4 viewpos = modelview * vec4(position, 1.0f);
    gl_Position = proj_matrix * viewpos;

    vertexData.normal = (inverse(transpose(modelview)) * vec4(normal, 0.0f)).xyz;
    vertexData.textureCoordinate = textureCoordinate * tcMultiplier;

    vertexData.time = time;
}