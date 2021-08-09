package CGA.Framework;
import CGA.User.DataStructures.Geometry.Material;
import CGA.User.DataStructures.Geometry.Mesh;
import CGA.User.DataStructures.Geometry.Renderable;
import CGA.User.DataStructures.Geometry.VertexAttribute;
import CGA.User.DataStructures.Texture2D;
import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

public class ModelLoader
{
    private static RawModel load(String objPath) throws Exception
    {
        RawModel rm = new RawModel();
        try
        {
            AIScene aiScene = Assimp.aiImportFile(objPath, Assimp.aiProcess_Triangulate | Assimp.aiProcess_GenNormals);

            // read materials
            for(int m = 0; m < aiScene.mNumMaterials(); ++m)
            {
                RawMaterial rmat = new RawMaterial();
                AIString tpath = AIString.calloc();
                AIMaterial mat = AIMaterial.create(aiScene.mMaterials().get(m));
                Assimp.aiGetMaterialTexture(mat, Assimp.aiTextureType_DIFFUSE, 0, tpath, (IntBuffer) null, null, null, null, null, null);

                // diffuse texture
                String tpathj = tpath.dataString();
                if(rm.textures.contains(tpathj))
                    rmat.diffTexIndex = rm.textures.indexOf(tpathj);
                else
                {
                    rm.textures.add(tpathj);
                    rmat.diffTexIndex = rm.textures.size() - 1;
                }

                // specular texture
                Assimp.aiGetMaterialTexture(mat, Assimp.aiTextureType_SPECULAR, 0, tpath, (IntBuffer) null, null, null, null, null, null);
                tpathj = tpath.dataString();
                if(rm.textures.contains(tpathj))
                    rmat.specTexIndex = rm.textures.indexOf(tpathj);
                else
                {
                    rm.textures.add(tpathj);
                    rmat.specTexIndex = rm.textures.size() - 1;
                }

                // emissive texture
                Assimp.aiGetMaterialTexture(mat, Assimp.aiTextureType_EMISSIVE, 0, tpath, (IntBuffer) null, null, null, null, null, null);
                tpathj = tpath.dataString();
                if(rm.textures.contains(tpathj))
                    rmat.emitTexIndex = rm.textures.indexOf(tpathj);
                else
                {
                    rm.textures.add(tpathj);
                    rmat.emitTexIndex = rm.textures.size() - 1;
                }

                // shininess
                PointerBuffer sptr = PointerBuffer.allocateDirect(1);
                Assimp.aiGetMaterialProperty(mat, Assimp.AI_MATKEY_SHININESS, sptr);
                AIMaterialProperty sprop = AIMaterialProperty.create(sptr.get(0));
                rmat.shininess = sprop.mData().getFloat(0);
                rm.materials.add(rmat);
            }

            // read meshes
            ArrayList<RawMesh> meshes = new ArrayList<>();
            for(int m = 0; m < aiScene.mNumMeshes(); ++m)
            {
                AIMesh aiMesh = AIMesh.create(aiScene.mMeshes().get(m));
                RawMesh mesh = new RawMesh();

                // read vertices
                for(int v = 0; v < aiMesh.mNumVertices(); ++v)
                {
                    AIVector3D aiVert = aiMesh.mVertices().get(v);
                    AIVector3D aiNormal = aiMesh.mNormals().get(v);
                    AIVector3D aiTexCoord = aiMesh.mNumUVComponents(0) > 0 ? aiMesh.mTextureCoords(0).get(v) : null;
                    Vertex vert = new Vertex(
                            new Vector3f(aiVert.x(), aiVert.y(), aiVert.z()),
                            aiTexCoord != null ? new Vector2f(aiTexCoord.x(), aiTexCoord.y()) : new Vector2f(0.0f, 0.0f),
                            new Vector3f(aiNormal.x(), aiNormal.y(), aiNormal.z())
                    );
                    mesh.vertices.add(vert);
                }

                // read indices
                for(int f = 0; f < aiMesh.mNumFaces(); ++f)
                {
                    AIFace face = aiMesh.mFaces().get(f);
                    for(int i = 0; i < face.mNumIndices(); ++i)
                    {
                        mesh.indices.add(face.mIndices().get(i));
                    }
                }

                // material index
                mesh.materialIndex = aiMesh.mMaterialIndex();
                meshes.add(mesh);
            }

            // traverse assimp scene graph
            Queue<AINode> nodeQueue = new LinkedList<AINode>();
            nodeQueue.offer(aiScene.mRootNode());

            while(!nodeQueue.isEmpty())
            {
                AINode node = nodeQueue.poll();
                for(int m = 0; m < node.mNumMeshes(); m++)
                {
                    rm.meshes.add(meshes.get(node.mMeshes().get(m)));
                }
                for(int c = 0; c < node.mNumChildren(); ++c)
                {
                    AINode cnode = AINode.create(node.mChildren().get(c));
                    nodeQueue.offer(cnode);
                }
            }
        }
        catch(Exception ex)
        {
            throw new Exception("Something went terribly wrong. Thanks java.\n" + ex.getMessage());
        }
        return rm;
    }

    private static float[] flattenVertexData(ArrayList<Vertex> vertices, Matrix3f rot)
    {
        float[] data = new float[8 * vertices.size()];
        int di = 0;
        for(Vertex v : vertices)
        {
            v.position.mul(rot);
            v.normal.mul(new Matrix3f(rot).transpose().invert());
            data[di++] = v.position.x; data[di++] = v.position.y; data[di++] = v.position.z;
            data[di++] = v.texCoord.x; data[di++] = v.texCoord.y;
            data[di++] = v.normal.x; data[di++] = v.normal.y; data[di++] = v.normal.z;
        }
        return data;
    }

    private static int[] flattenIndexData(ArrayList<Integer> indices)
    {
        int[] data = new int[indices.size()];
        int di = 0;
        for(int i : indices)
        {
            data[di++] = i;
        }
        return data;
    }

    public static Renderable loadModel(String objpath, float pitch, float yaw, float roll) throws Exception
    {
        RawModel model = load(objpath);
        ArrayList<Texture2D> textures = new ArrayList<>();
        ArrayList<Material> materials = new ArrayList<>();
        ArrayList<Mesh> meshes = new ArrayList<>();

        VertexAttribute[] vertexAttributes = new VertexAttribute[3];
        int stride = 8 * 4;
        vertexAttributes[0] = new VertexAttribute(3, GL_FLOAT, stride, 0);
        vertexAttributes[1] = new VertexAttribute(2, GL_FLOAT, stride, 3 * 4);
        vertexAttributes[2] = new VertexAttribute(3, GL_FLOAT, stride, 5 * 4);

        // preprocessing rotation
        Matrix3f rot = new Matrix3f().rotateZ(roll).rotateY(yaw).rotateX(pitch);

        if(model != null)
        {
            // create textures
            //default textures
            ByteBuffer ddata = BufferUtils.createByteBuffer(4);
            ddata.put((byte)0).put((byte)0).put((byte)0).put((byte)0);
            ddata.flip();

            for(int i = 0; i < model.textures.size(); ++i)
            {
                if(model.textures.get(i).isEmpty())
                {
                    textures.add(new Texture2D(ddata, 1, 1, true));
                }
                else
                {
                    textures.add(new Texture2D(objpath.substring(0, objpath.lastIndexOf('/') + 1) + model.textures.get(i), true));
                }
            }

            // materials
            for(int i = 0; i < model.materials.size(); ++i)
            {
                materials.add(new Material(textures.get(model.materials.get(i).diffTexIndex),
                        textures.get(model.materials.get(i).emitTexIndex),
                        textures.get(model.materials.get(i).specTexIndex),
                        model.materials.get(i).shininess,
                        new Vector2f(1.0f, 1.0f)));
            }

            // meshes
            for(int i = 0; i < model.meshes.size(); ++i)
            {
                meshes.add(new Mesh(flattenVertexData(model.meshes.get(i).vertices, rot),
                        flattenIndexData(model.meshes.get(i).indices),
                        vertexAttributes,
                        materials.get(model.meshes.get(i).materialIndex)));
            }

            // assemble the renderable
            return new Renderable(meshes);
        }
        throw new Exception("Model loading failed.");
    }
}