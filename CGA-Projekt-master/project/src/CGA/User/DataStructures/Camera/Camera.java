package CGA.User.DataStructures.Camera;

import CGA.User.DataStructures.Geometry.Transformable;
import CGA.User.DataStructures.ShaderProgram;
import org.joml.Matrix4f;

public abstract class Camera extends Transformable {

    protected Matrix4f viewMat;
    protected Matrix4f projMat;

    protected Camera() {
        super();
        viewMat = new Matrix4f();
        projMat = new Matrix4f();
    }

    public Matrix4f getViewMatrix() {
        return viewMat;
    }

    public Matrix4f getProjectionMatrix() {
        return projMat;
    }

    public abstract Matrix4f calculateViewMatrix();
    public abstract Matrix4f calculateProjectionMatrix();
    public abstract void bind(ShaderProgram shaderProgram);


}
