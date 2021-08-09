package CGA.User.DataStructures.Light;

import CGA.User.DataStructures.Geometry.Transformable;
import CGA.User.DataStructures.ShaderProgram;
import org.joml.Vector3f;

public class PointLight extends Transformable implements IPointLight {

    Vector3f lightColor;
    Vector3f lightPos;

    float constant;
    float linear;
    float quadratic;

    public PointLight(float x, float y, float z, float r, float g, float b, float c, float l, float q) {
        this.lightPos = new Vector3f(x,y,z);
        this.lightColor = new Vector3f(r,g,b);
        this.constant = c;
        this.linear = l;
        this.quadratic = q;
        translateLocal(lightPos);
    }

    @Override
    public void bind(ShaderProgram shaderProgram, String name) {
        shaderProgram.setUniform(name + "Color", lightColor);
        shaderProgram.setUniform(name + "Position", getWorldPosition());
        shaderProgram.setUniform("constant", constant);
        shaderProgram.setUniform("linear", linear);
        shaderProgram.setUniform("quadratic", quadratic);

    }

    @Override
    public void setLightColor(Vector3f lightColor) {
        this.lightColor = lightColor;
    }
}