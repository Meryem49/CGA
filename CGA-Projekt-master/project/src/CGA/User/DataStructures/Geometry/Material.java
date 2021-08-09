package CGA.User.DataStructures.Geometry;

import CGA.User.DataStructures.ShaderProgram;
import CGA.User.DataStructures.Texture2D;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengles.GLES20.GL_CLAMP_TO_EDGE;

public class Material
{
    public Texture2D diff;
    public Texture2D emit;
    public Texture2D specular;

    public float shininess;
    public Vector2f tcMultiplier;

    public Material(Texture2D diff, Texture2D emit, Texture2D specular, float shininess, Vector2f tcmul)
    {
        this.diff = diff;
        this.emit = emit;
        this.specular = specular;
        this.shininess = shininess;
        this.tcMultiplier = tcmul;
    }

    public Material()
    {
        this.diff = null;
        this.emit = null;
        this.specular = null;
        this.shininess = 50.0f;
        this.tcMultiplier = new Vector2f(1.0f);
    }

    public void bind(ShaderProgram shaderProgram)
    {
        //TODO: Place your code here
        shaderProgram.setUniform("diff",0);
        diff.bind(0);
        shaderProgram.setUniform("emit",1);
        emit.bind(1);
        shaderProgram.setUniform("specular",2);
        specular.bind(2);
        shaderProgram.setUniform("shininess", shininess);
        shaderProgram.setUniform("tcMultiplier", tcMultiplier);
    }
}