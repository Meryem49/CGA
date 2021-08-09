package CGA.User.DataStructures.Geometry;

        import CGA.User.DataStructures.ShaderProgram;

        import static org.lwjgl.opengl.GL11.*;
        import static org.lwjgl.opengl.GL15.*;
        import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
        import static org.lwjgl.opengl.GL15.glBindBuffer;
        import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
        import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
        import static org.lwjgl.opengl.GL30.glBindVertexArray;
        import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
        import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Created by Fabian on 16.09.2017.
 */
public class Mesh {

    //private data
    private int vao = 0;
    private int vbo = 0;
    private int ibo = 0;

    private int indexcount= 0;

    private Material material;

    /**
     * Creates a Mesh object from vertexdata, intexdata and a given set of vertex attributes
     *
     * @param vertexdata plain float array of vertex data
     * @param indexdata  index data
     * @param attributes vertex attributes contained in vertex data
     * @throws Exception If the creation of the required OpenGL objects fails, an exception is thrown
     */
    public Mesh(float[] vertexdata, int[] indexdata, VertexAttribute[] attributes) throws Exception {

        indexcount = indexdata.length;

        //1.Schritt Generiere ID
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ibo = glGenBuffers();

        //2.Schritt Aktiviere ID
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);

        // Daten in Buffer hochladen
        glBufferData(GL_ARRAY_BUFFER, vertexdata, GL_STATIC_DRAW);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexdata, GL_STATIC_DRAW);

        for(int i= 0; i<attributes.length; i++) {


            glEnableVertexAttribArray(i);
            glVertexAttribPointer(i, attributes[i].n, attributes[i].type, false, attributes[i].stride, attributes[i].offset);

        }
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public Mesh(float[] vertexdata, int[] indexdata, VertexAttribute[] attributes, Material material) throws Exception {

        this(vertexdata, indexdata, attributes);

        this.material = material;
    }


    //2.2.2 Render
    public void render() {

        //binden Vertex Array damit gerendert werden kann
        glBindVertexArray(vao);
        //Element Zeichnen
      // glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
        glDrawElements(GL_TRIANGLES, indexcount, GL_UNSIGNED_INT,0);
        glBindVertexArray(0);
    }

    public void render(ShaderProgram shaderProgram) {

        material.bind(shaderProgram);
        render();
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    public void cleanup() {
        if (ibo != 0)
            glDeleteBuffers(ibo);
        if (vbo != 0)
            glDeleteBuffers(vbo);
        if (vao != 0)
            glDeleteVertexArrays(vao);
    }
}