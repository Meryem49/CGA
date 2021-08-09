package CGA.User.DataStructures.Camera;

import CGA.User.DataStructures.ShaderProgram;
import org.joml.Matrix4f;

public class TronCam extends Camera {

    private float fov, aspect, nearPlane, farPlane;

    public TronCam() {
        this.fov = (float) Math.toRadians(90.0f);
        this.aspect = 16.0f / 9.0f;
        this.nearPlane = 0.1f;
        this.farPlane = 1000.0f;
    }

    public TronCam(float fov, float aspect, float nearPlane, float farPlane) {
        this.fov = fov;
        this.aspect = aspect;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
    }

    public Matrix4f calculateViewMatrix() {
        viewMat.identity();
        viewMat = new Matrix4f().lookAt(getWorldPosition(), getWorldPosition().sub(getWorldZAxis()), getWorldYAxis());
        return viewMat;
    }

    public Matrix4f calculateProjectionMatrix() {
        projMat.identity();
        projMat.perspective(fov, aspect, nearPlane, farPlane);
        return projMat;

    }

    public void bind(ShaderProgram shaderProgram) {
        shaderProgram.setUniform("view_matrix", this.calculateViewMatrix(), false);
        shaderProgram.setUniform("projection_matrix", this.calculateProjectionMatrix(), false);
    }
}
