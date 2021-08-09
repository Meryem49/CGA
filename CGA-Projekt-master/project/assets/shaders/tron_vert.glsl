#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texcoords;
layout(location = 2) in vec3 normals;

//uniforms
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;
uniform vec2 tcMultiplier;
uniform vec3 pointlightPosition;
uniform vec3 spotlightPosition;

out struct VertexData
{
    vec3 normal;
    vec2 tc;
    vec3 toPointlight;
    vec3 toSpotlight;
    vec3 toCamera;
} vertexData;

void main(){
    mat4 model_view = view_matrix * model_matrix;
    vec4 viewPos = model_view * vec4(position, 1.0f);
    vec4 pos = projection_matrix * viewPos;

    //die Inverse der transponierten aggregierten Model-View-Matrix
    vec4 normal = inverse(transpose(model_view)) * vec4(normals, 0.0f);
    vec2 tc = tcMultiplier * texcoords;


    // compute light direction in view space //
    vec4 lp = view_matrix * vec4(pointlightPosition, 1.0f);
    vertexData.toPointlight = (lp - viewPos).xyz;

    vec4 lps = view_matrix * vec4(spotlightPosition, 1.0f);
    vertexData.toSpotlight = (lps - viewPos).xyz;

    // specular term //
    vertexData.toCamera = -viewPos.xyz;

    gl_Position = pos;
    vertexData.normal = normal.xyz;
    vertexData.tc = tc;

}