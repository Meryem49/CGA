package CGA.User.DataStructures.Geometry;

import CGA.User.DataStructures.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;

/**
 * Created by Fabian on 19.09.2017.
 */

/**
 * Renders Mesh objects
 */
public class Renderable extends Transformable implements IRenderable
{
    /**
     * List of meshes attached to this renderable object
     */
    public Vector3f position;
    public Matrix4f tMat;
    public ArrayList<Mesh> meshes;

    /**
     * creates an empty renderable object with an empty mesh list
     */
    public Renderable()
    {
        super();
        meshes = new ArrayList<>();
    }

    public Renderable(ArrayList<Mesh> meshes)
    {
        super();
        this.meshes = new ArrayList<>();
        this.meshes.addAll(meshes);
    }

    /**
     * Renders all meshes attached to this Renderable, applying the transformation matrix to
     * each of them
     */
    public void render()
    {
        for(Mesh m : meshes)
        {
            m.render();
        }
    }

    @Override
    public void render(ShaderProgram shaderProgram){
        shaderProgram.setUniform("model_matrix", getWorldModelMatrix(), false);
        for(Mesh m : meshes)
        {
            m.render(shaderProgram);
        }
    }
    public void computeMatrix()
    {
       tMat = new Matrix4f().translate(position);
    }
    public  void setPosition(Vector3f pos)
    {

    }
}
