package CGA.Framework;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.*;

import java.util.ArrayList;
import java.util.Collection;

public class Vertex
{
    public Vector3f position;
    public Vector2f texCoord;
    public Vector3f normal;

    public Vertex(Vector3f position, Vector2f texCoord, Vector3f normal)
    {
        this.position = position;
        this.texCoord = texCoord;
        this.normal = normal;
    }
}
