#version 330 core

//input from vertex shader
in struct VertexData
{
    vec2 textureCoordinate;
    vec3 normal;
    float time;
} vertexData;

uniform sampler2D materialDiff;
uniform sampler2D materialSpec;
uniform sampler2D materialEmit;
uniform float materialShininess;

uniform vec3 tronColor;
uniform float minTimeFactor;
uniform float timeFalloff;
uniform float currentTime;

//fragment shader output
out vec4 color;


float calcTimeAttenuation(float ts, float ct, float min, float falloff)
{
    return exp(-falloff * (ct - ts)) * (1.0 - min) + min;
}

void main(){

    vec3 emitColor = texture(materialEmit, vertexData.textureCoordinate).rgb;

    vec3 emit_term = emitColor * tronColor * calcTimeAttenuation(vertexData.time, currentTime, minTimeFactor, timeFalloff);

    color = vec4(emit_term , 1.0f);
}
