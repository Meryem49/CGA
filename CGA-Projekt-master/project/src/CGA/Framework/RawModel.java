package CGA.Framework;

import java.util.ArrayList;

public class RawModel
{
    public ArrayList<RawMesh> meshes;
    public ArrayList<RawMaterial> materials;
    public ArrayList<String> textures;

    public RawModel()
    {
        this.meshes = new ArrayList<>();
        this.materials = new ArrayList<>();
        this.textures = new ArrayList<>();
    }
}
