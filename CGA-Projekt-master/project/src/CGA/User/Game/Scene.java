package CGA.User.Game;

import CGA.Framework.GameWindow;
import CGA.Framework.ModelLoader;
import CGA.Framework.OBJLoader;
import CGA.User.DataStructures.Camera.TronCam;
import CGA.User.DataStructures.Geometry.*;
import CGA.User.DataStructures.Light.PointLight;
import CGA.User.DataStructures.Light.SpotLight;
import CGA.User.DataStructures.ShaderProgram;
import CGA.User.DataStructures.Texture2D;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static java.lang.Thread.sleep;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Fabian on 16.09.2017.
 */
public class Scene {
    private ShaderProgram simpleShader;
    private GameWindow window;
    //private Matrix4f groundmatrix, kugelmatrix;
    private float[] vertexData;
    private int[] indexData;
    private VertexAttribute[] attributes;
    private Renderable ground,sphere;
    private Renderable cycle;
    private Renderable baum;
    private Renderable baum2;
    private Renderable baum3;
    private Renderable ball2;
    private TronCam tronCam;
    private double oldmousex;
    private double oldmousey;
    //private Mesh meshhaus;
    private Material groundmat;
    private PointLight cycleLight;
    private SpotLight cycleLightspot;

    public Scene(CGA.User.Game.Game window) {
        this.window = window;

    }

    //scene setup
    public boolean init() {
        try {
            //Load staticShader
            simpleShader = new ShaderProgram("project/assets/shaders/tron_vert.glsl", "project/assets/shaders/tron_frag.glsl");

            //TODO: Place your code here. Specify the geometry data and create the needed mesh object.

            //Objekt laden und Mesh erzeugen
            OBJLoader.OBJResult resgr = OBJLoader.loadOBJ("project/assets/models/ground.obj", false, false);
            OBJLoader.OBJMesh objMeshgr;

            OBJLoader.OBJResult resgr2 = OBJLoader.loadOBJ("project/assets/models/Ball_Game/Player_Ball.obj", false, false);
            OBJLoader.OBJMesh objMeshgr2;

            cycle = ModelLoader.loadModel("project/assets/Light Cycle/Light Cycle/HQ_Movie cycle.obj", (float) Math.toRadians(-90), (float) Math.toRadians(90.0f), 0.0f);
            cycle.scaleLocal(new Vector3f(0.8f));
            baum = ModelLoader.loadModel("project/assets/models/Tree/Tree.obj", (float) Math.toRadians(0), (float) Math.toRadians(90.0f), 0.0f);
            baum.scaleLocal(new Vector3f(2.0f));
            baum.translateLocal(new Vector3f(2.0f,0.0f,0.0f));
            baum2 = ModelLoader.loadModel("project/assets/models/Tree/Tree.obj", (float) Math.toRadians(0), (float) Math.toRadians(90.0f), 0.0f);
            baum2.scaleLocal(new Vector3f(2.0f));
            baum2.translateLocal(new Vector3f(-2.0f,0.0f,0.0f));
            baum3 = ModelLoader.loadModel("project/assets/models/Tree/Tree.obj", (float) Math.toRadians(0), (float) Math.toRadians(90.0f), 0.0f);
            baum3.scaleLocal(new Vector3f(2.0f));
            baum3.translateLocal(new Vector3f(0.0f,0.0f,2.0f));
            ball2 = ModelLoader.loadModel("project/assets/models/moon/Moon.obj", (float) Math.toRadians(0), (float) Math.toRadians(90.0f), 0.0f);
            ball2.scaleLocal(new Vector3f(2.0f));
            ball2.translateLocal(new Vector3f(-5.0f,1.0f,0.0f));


            //Erstes Mesh aus der OBJ Datei
            objMeshgr = resgr.objects.get(0).meshes.get(0);
            objMeshgr2 = resgr2.objects.get(0).meshes.get(0);
            attributes = new VertexAttribute[3]; //Vertexattribute für Kreis und Ground
            attributes[0] = new VertexAttribute(3, GL_FLOAT, 32, 0);
            attributes[1] = new VertexAttribute(2, GL_FLOAT, 32, 12);
            attributes[2] = new VertexAttribute(3, GL_FLOAT, 32, 20);

            //Ground Modelmatrix
            //groundmatrix = new Matrix4f()
            //groundmatrix.rotate((float)Math.toRadians(90.0f), 1.0f, 0.0f, 0.0f);
            //groundmatrix.scale(0.03f);
            //kugelmatrix = new Matrix4f()
            // kugelmatrix.scale(0.5f);

            Texture2D grounddiff = new Texture2D("project/assets/textures/ground_diff.png", true);
            grounddiff.setTexParams(GL_CLAMP, GL_REPEAT, GL_LINEAR, GL_LINEAR);
            Texture2D groundemit = new Texture2D("project/assets/textures/ground_emit.png", true);
            groundemit.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR);
            Texture2D groundspec = new Texture2D("project/assets/textures/ground_spec.png", true);
            groundspec.setTexParams(GL_CLAMP, GL_REPEAT, GL_LINEAR, GL_LINEAR);

            groundmat = new Material(grounddiff, groundemit, groundspec, 60.0f, new Vector2f(64.0f, 64.0f));


            ground = new Renderable();
            ground.meshes.add(new Mesh(objMeshgr.getVertexData(), objMeshgr.getIndexData(), attributes, groundmat));

            sphere = new Renderable();
            sphere.meshes.add(new Mesh(objMeshgr.getVertexData(), objMeshgr.getIndexData(), attributes, groundmat));

            tronCam = new TronCam();
            tronCam.translateLocal(new Vector3f(0.0f, 2.0f, 4.0f));
            tronCam.setParent(cycle);

            cycleLightspot = new SpotLight(0.0f,0.0f,0.0f,1.0f,1.0f,1.0f, 0.5f, 0.05f, 0.01f, 10.0f, 30.0f);
            cycleLightspot.translateLocal(new Vector3f(0.0f,1.0f,-2.0f));
            cycleLightspot.rotateLocal((float)Math.toRadians(-10.0f),0,0);
            cycleLightspot.setParent(cycle);

            cycleLight = new PointLight(0.0f,0.0f,0.0f, 1.0f,0.0f,0.0f, 1.0f, 0.5f, 0.1f);
            cycleLight.translateLocal(new Vector3f(0.0f,0.3f,0.0f));
            cycleLight.setParent(cycle);

            oldmousex = window.getMousePos().xpos;
            oldmousey = window.getMousePos().ypos;


            glEnable(GL_CULL_FACE);
            glFrontFace(GL_CCW);
            glCullFace(GL_BACK);
            //glDisable(GL_CULL_FACE);

            glEnable(GL_DEPTH_TEST);
            glDepthFunc(GL_LESS);


            return true;
        } catch (Exception ex) {
            System.err.println("Scene initialization failed:\n" + ex.getMessage() + "\n");
            return false;
        }
    }

    public void render(float dt, float t) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        simpleShader.use();
        //simpleShader.setUniform("model_matrix", groundmatrix, false);
        simpleShader.setUniform("bodenfarbe", new Vector3f(0.0f,1.0f,0.0f));

        cycleLight.bind(simpleShader, "pointlight");
        cycleLightspot.bind(simpleShader, "spotlight", tronCam.getViewMatrix());

        tronCam.bind(simpleShader);

        ground.render(simpleShader);

        sphere.render(simpleShader);

        cycle.render(simpleShader);

        baum.render(simpleShader);
        baum2.render(simpleShader);
        baum3.render(simpleShader);


        ball2.render(simpleShader);

    }

    public void update(float dt, float t) {

        if (window.getKeyState(GLFW_KEY_Q)) {
            cycle.scaleLocal(new Vector3f(0.99f));
        }

        if (window.getKeyState(GLFW_KEY_E)) {
            cycle.scaleLocal(new Vector3f(1.01f));
        }

        if (window.getKeyState(GLFW_KEY_D)) {
            cycle.rotateLocal(0.0f, -0.01f, 0.0f);
        }


        if (window.getKeyState(GLFW_KEY_W)) {
            cycle.translateLocal(new Vector3f(0.0f, 0.0f, -0.1f));

        }

        if (window.getKeyState(GLFW_KEY_A)) {
            cycle.rotateLocal(0.0f, 0.01f, 0.0f);

        }

        if (window.getKeyState(GLFW_KEY_S)) {
            cycle.translateLocal(new Vector3f(0.0f, 0.0f, 0.1f));

        }

        if (oldmousex > window.getMousePos().xpos) {
            tronCam.rotateAroundPoint(0f, -(float) (oldmousex - window.getMousePos().xpos) / 1000, 0f,cycle.getPosition().add(0.0f,0.0f,4.0f).negate());
            //tronCam.translateLocal(new Vector3f(-(float) (oldmousex - window.getMousePos().xpos) / 200,0.0f,0.0f));
            oldmousex = window.getMousePos().xpos;
        }
        if (oldmousex < window.getMousePos().xpos) {
            tronCam.rotateAroundPoint(0f, (float) (window.getMousePos().xpos - oldmousex) / 1000, 0f,cycle.getPosition().add(0.0f,0.0f,4.0f).negate());
            //tronCam.translateLocal(new Vector3f(-(float) (oldmousex - window.getMousePos().xpos) / 200,0.0f,0.0f));
            oldmousex = window.getMousePos().xpos;
        }

        /*if (oldmousey > window.getMousePos().ypos) {
            tronCam.rotateAroundPoint((float) (oldmousey - window.getMousePos().ypos) / 1000, 0f, 0f, cycle.getPosition().add(0.0f,0.0f,4.0f).negate());
            oldmousey = window.getMousePos().ypos;
        }
        if (oldmousey < window.getMousePos().ypos) {
            tronCam.rotateAroundPoint(-(float) (window.getMousePos().ypos - oldmousey) / 1000, 0f, 0f, cycle.getPosition().add(0.0f,0.0f,4.0f).negate());
            oldmousey = window.getMousePos().ypos;
        }*/


    }

    public void onKey(int key, int scancode, int action, int mode) {

    }

    public void onMouseMove(double xpos, double ypos) {

    }


    public void cleanup() {
    }
}