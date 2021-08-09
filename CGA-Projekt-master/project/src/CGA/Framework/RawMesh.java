package CGA.Framework;

import java.util.ArrayList;

public class RawMesh
{
    public ArrayList<Vertex> vertices;
    public ArrayList<Integer> indices;
    public int materialIndex;

    public RawMesh(ArrayList<Vertex> vertices, ArrayList<Integer> indices, int materialIndex)
    {
        this.vertices = vertices;
        this.indices = indices;
        this.materialIndex = materialIndex;
    }

    public RawMesh()
    {
        this.vertices = new ArrayList<>();
        this.indices = new ArrayList<>();
        this.materialIndex = 0;
    }
}