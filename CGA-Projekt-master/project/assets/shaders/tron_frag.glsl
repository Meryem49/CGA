#version 330 core

//input from vertex shader
in struct VertexData
{
    vec3 normal;
    vec2 tc;
    vec3 toPointlight;
    vec3 toSpotlight;
    vec3 toCamera;
} vertexData;

//uniforms
uniform sampler2D diff;
uniform sampler2D emit;
uniform sampler2D specular;
uniform float shininess;

uniform vec3 pointlightColor;
uniform vec3 spotlightColor;
uniform vec3 spotlightDir;
uniform float winkelInnen;
uniform float winkelAussen;
uniform float constant;
uniform float linear;
uniform float quadratic;
uniform vec3 bodenfarbe;


//fragment shader output
out vec4 color;

vec4 shade (vec3 diff, vec3 toLight, vec3 normal, vec3 spec, vec3 toCamera, float shininess) {
    vec3 r = normalize(reflect(-toLight, normal));
    float cosa = max(dot(toLight, normal), 0.0f);
    float cosbk = pow(max(dot(toCamera, r), 0.0f), shininess);
    return vec4(diff * cosa + spec * cosbk, 1.0f);
}
//Attenuation= Lichtdämpfung verliert seine intensität, wenn es weiter von der Quelle entfernt ist
float calcAtt(vec3 L) {
    float distance  = length(L);
    return 1.0 / (constant + linear * distance + quadratic * (distance * distance));
}


float calcIntensity(vec3 L, vec3 S, float winkelaussen, float winkelinnen){
    float winkel = dot(L, normalize(-S));
    return clamp((winkel - cos(winkelaussen)) / (cos(winkelinnen) - cos(winkelaussen)), 0.0f, 1.0f);
}


void main(){

    // normalize everything necessary //

    vec3 N = normalize(vertexData.normal);
    vec3 L = normalize(vertexData.toPointlight);
    vec3 LS = normalize(vertexData.toSpotlight);
    vec3 V = normalize(vertexData.toCamera);


    vec3 diff = texture(diff, vertexData.tc).rgb;
    vec3 spec = texture(specular, vertexData.tc).rgb;
    vec4 emit = texture(emit, vertexData.tc) * vec4(bodenfarbe, 1.0f);


    vec4 pointlight = shade(diff, L, N, spec, V, shininess) * vec4(pointlightColor, 1.0f) * calcAtt(L);
    //vec4 pointlight = vec4(pointlightColor, 1.0f);
    //vec4 spotlight = shade(diff, LS, N, spec, V, shininess) * vec4(spotlightColor, 1.0f) * vec4(calcIntensity(LS, spotlightDir, winkelAussen, winkelInnen), 0, 0, 1.0f);
    vec4 spotlight = shade(diff, LS, N, spec, V, shininess) * vec4(spotlightColor, 1.0f) * calcIntensity(LS, spotlightDir, winkelAussen, winkelInnen) * calcAtt(LS);
    color = emit +  pointlight + spotlight;
}