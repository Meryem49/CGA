package CGA.User.DataStructures.Light;

import CGA.User.DataStructures.ShaderProgram;
import org.joml.Vector3f;

public interface IPointLight
{
    void bind(ShaderProgram shaderProgram, String name);

    public void setLightColor(Vector3f lightColor);
}