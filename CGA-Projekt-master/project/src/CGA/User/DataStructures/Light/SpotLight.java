package CGA.User.DataStructures.Light;

import CGA.User.DataStructures.ShaderProgram;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class SpotLight extends PointLight implements ISpotLight {

    private float winkelinnen;
    private float winkelaussen;


    public SpotLight(float x, float y, float z, float r, float g, float b, float c, float l, float q, float winkelinnen , float winkelaussen){
        super(x,y,z,r,g,b ,c,l,q);
        this.winkelinnen = (float)Math.toRadians(winkelinnen);
        this.winkelaussen = (float)Math.toRadians(winkelaussen);

    }

    public void bind(ShaderProgram shaderProgram, String name, Matrix4f viewMatrix) {
        super.bind(shaderProgram,name);
        shaderProgram.setUniform(name + "Dir", new Vector3f(getWorldZAxis()).negate().mul(new Matrix3f(viewMatrix)));
        shaderProgram.setUniform("winkelInnen", winkelinnen);
        shaderProgram.setUniform("winkelAussen", winkelaussen);

    }
}