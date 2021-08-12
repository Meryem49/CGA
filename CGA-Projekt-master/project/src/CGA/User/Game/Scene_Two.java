package CGA.User.Game;

import CGA.Framework.GameWindow;
import CGA.Framework.ModelLoader;
import CGA.Framework.OBJLoader;
import CGA.User.DataStructures.BasicShader;
import CGA.User.DataStructures.Geometry.Mesh;
import CGA.User.DataStructures.Geometry.Renderable;
import CGA.User.DataStructures.Geometry.VertexAttribute;
import imgui.app.Application;
import org.joml.*;
import org.joml.Math;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import imgui.ImGui;
import imgui.app.Application;
public class Scene_Two extends Application
{
    private GameWindow window;
    private Renderable Player;
    private BasicShader basicShader;
    private float[] vertexArr,indexArr;
    private float screenWidth=0,screenHeight=0;
    private VertexAttribute[] attributes;
    private Matrix4f viewMat,projectionMat;

    private FloatBuffer m3x3buf;
    private FloatBuffer m4x4buf;

    private  Renderable player,ground,obstacleOne,obstacleTwo,obstacleThree,obstacleFour,obstacleFive,obstacleSix,obstacleSeven;
    private  Renderable successMark;
    ArrayList<Renderable>obstacles;
    private Boolean startJump=false;
    float amt=0,jumpFactor =1;
    float ang=0,cnt=0;
    Matrix4f mvp;

    public float score=0;
    float movementSpeed=0.5f;
    boolean stopGame=false;
    public boolean levelSuccess=false;
        //private

    public Scene_Two(CGA.User.Game.Game window) {
        this.window = window;

    }
    public  void setScreenWidthAndHeight(float w,float h)
    {
        screenWidth =w;
        screenHeight =h;
    }

    @Override
    public void process() {
        ImGui.text("Hello, World!");
    }
    public boolean init()
    {
        glClearColor(0.1f, 0.2f, 0.3f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

       // ImGui.text("Hello, World!");
        //loading our player here
        try {
            attributes = new VertexAttribute[3]; //Vertexattribute für Kreis und Ground
            attributes[0] = new VertexAttribute(3, GL_FLOAT, 32, 0);
            attributes[1] = new VertexAttribute(2, GL_FLOAT, 32, 12);
            attributes[2] = new VertexAttribute(3, GL_FLOAT, 32, 20);


            ground = ModelLoader.loadModel("project/assets/models/Ball_Game/Ground.obj", (float) Math.toRadians(0), (float) Math.toRadians(90.0f), 0.0f);
            ground.position=new Vector3f(0,-10,-300);  // setzen die Position für den Boden
            ground.computeMatrix();

            player = ModelLoader.loadModel("project/assets/models/Ball_Game/Player_Ball.obj", (float) Math.toRadians(0), (float) Math.toRadians(90.0f), 0.0f);
            player.position=new Vector3f(0,5,150);
            player.computeMatrix();

            obstacles= new ArrayList<Renderable>(); //eine arrayliste für die Balken
            obstacleOne = ModelLoader.loadModel("project/assets/models/Ball_Game/ObstacleOne.obj", (float) Math.toRadians(0), (float) Math.toRadians(90.0f), 0.0f);
            obstacleOne.position=new Vector3f(25,5,100);
            obstacleOne.computeMatrix();

            obstacles.add(obstacleOne);

            obstacleTwo = ModelLoader.loadModel("project/assets/models/Ball_Game/ObstacleOne.obj", (float) Math.toRadians(0), (float) Math.toRadians(90.0f), 0.0f);
            obstacleTwo.position=new Vector3f(-25,5,0);
            obstacleTwo.computeMatrix();

            obstacles.add(obstacleTwo);

            obstacleThree = ModelLoader.loadModel("project/assets/models/Ball_Game/ObstacleOne.obj", (float) Math.toRadians(0), (float) Math.toRadians(90.0f), 0.0f);
            obstacleThree.position=new Vector3f(25,5,-100);
            obstacleThree.computeMatrix();

            obstacles.add(obstacleThree);

            obstacleFour = ModelLoader.loadModel("project/assets/models/Ball_Game/ObstacleOne.obj", (float) Math.toRadians(0), (float) Math.toRadians(90.0f), 0.0f);
            obstacleFour.position=new Vector3f(-25,5,-200);
            obstacleFour.computeMatrix();

            obstacles.add(obstacleFour);

            obstacleFive = ModelLoader.loadModel("project/assets/models/Ball_Game/ObstacleOne.obj", (float) Math.toRadians(0), (float) Math.toRadians(90.0f), 0.0f);
            obstacleFive.position=new Vector3f(25,5,-300);
            obstacleFive.computeMatrix();

            obstacles.add(obstacleFive);

            obstacleSix = ModelLoader.loadModel("project/assets/models/Ball_Game/ObstacleOne.obj", (float) Math.toRadians(0), (float) Math.toRadians(90.0f), 0.0f);
            obstacleSix.position=new Vector3f(-25,5,-400);
            obstacleSix.computeMatrix();

            obstacles.add(obstacleSix);

            obstacleSeven = ModelLoader.loadModel("project/assets/models/Ball_Game/ObstacleOne.obj", (float) Math.toRadians(0), (float) Math.toRadians(90.0f), 0.0f);
            obstacleSeven.position=new Vector3f(25,5,-500);
            obstacleSeven.computeMatrix();

            obstacles.add(obstacleSeven);
         //   obstacles.add(obstacleFive);

            successMark = ModelLoader.loadModel("project/assets/models/Ball_Game/ObstacleFull.obj", (float) Math.toRadians(0), (float) Math.toRadians(90.0f), 0.0f);
            successMark.position=new Vector3f(0,5,-700);
            successMark.computeMatrix();  //ist die Ziellinie


            basicShader =new BasicShader("project/assets/shaders/Basic_Vert.glsl", "project/assets/shaders/Basic_Frag.glsl");

            Vector3fc eye = new Vector3f(0.0f,25,200f); //kameraposition, wie hoch, wo das Zentrum ist
            Vector3fc center = new Vector3f(0.0f,0.0f,0.0f);
            Vector3fc up = new Vector3f(0.0f,1.0f,0.0f);
            viewMat = new Matrix4f(); //wie die kamera auf das objekt guckt
            projectionMat = new Matrix4f();
            viewMat=new Matrix4f().lookAt(eye,center,up);
            var fov = (float) Math.toRadians(60.0f);
            var aspect = screenWidth / screenHeight;
            var nearPlane = 0.1f;
            var farPlane = 10000.0f;
            projectionMat=new Matrix4f().perspective(fov,aspect,nearPlane,farPlane);
          //  projectionMat=new Matrix4f().ortho(-screenWidth/2, screenWidth/2, -screenHeight/2, screenHeight/2, -1.0f, 100000.0f);
            m3x3buf = BufferUtils.createFloatBuffer(9);
            m4x4buf = BufferUtils.createFloatBuffer(16);






            return true;
        }
        catch (Exception ex)
        {
            System.err.println("Scene initialization failed:\n" + ex.getMessage() + "\n");
            return false;
        }

    }

    public  void render(float dt, float t) //modelle werden auf das frame eingesetzt
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
       // glClearColor(0.1f,0.0f,0.3f,1);
        glClearColor(135/255.0f,206/255.0f,235/255.0f,1);
       // 135, 206, 235
        glUseProgram(basicShader.programID);

        glEnableVertexAttribArray(basicShader.posLoc);
        {
            Matrix4f modelmat = new Matrix4f();
            mvp = new Matrix4f();
            modelmat.identity();
            Matrix4f mv = new Matrix4f();

            //in die modelmatrix werden die objekte hinzugefügt
            modelmat.mul(ground.tMat, modelmat);
            viewMat.mul(modelmat, mv);
            projectionMat.mul(mv, mvp); //
            mvp.get(m4x4buf);

            glUniformMatrix4fv(basicShader.mvpLoc, false, m4x4buf);
            glUniform3f(basicShader.colorLoc, 1, 1, 1);
            ground.render();
        }
        {
            var scaleMat = new Matrix4f().scale(new Vector3f(5,5,5));
            Matrix4f modelmat = new Matrix4f();
            mvp = new Matrix4f();
            modelmat.identity();
            Matrix4f mv = new Matrix4f();
            mvp = new Matrix4f();
            modelmat.identity();
            mv = new Matrix4f();


            modelmat.mul(player.tMat, modelmat);
            modelmat.mul(scaleMat, modelmat);
            viewMat.mul(modelmat, mv);
            projectionMat.mul(mv, mvp);
            mvp.get(m4x4buf);

            glUniformMatrix4fv(basicShader.mvpLoc, false, m4x4buf);
            glUniform3f(basicShader.colorLoc, 0.5f, 1, 0.5f);
            player.render();

        }

        {
           for(int i=0;i <obstacles.size();i++)
           {
               var ob= obstacles.get(i); //weil mehrere obstacles vorhanden sind wird
               Matrix4f modelmat = new Matrix4f(); //modelmatrix beschreibt größe position von den objekten
               mvp = new Matrix4f();
               modelmat.identity();
               Matrix4f mv = new Matrix4f();
               mvp = new Matrix4f();
               modelmat.identity();
               mv = new Matrix4f();
               ob.computeMatrix();
               modelmat.mul(ob.tMat, modelmat);

               viewMat.mul(modelmat, mv);
               projectionMat.mul(mv, mvp);
               mvp.get(m4x4buf);

               glUniformMatrix4fv(basicShader.mvpLoc, false, m4x4buf);
               glUniform3f(basicShader.colorLoc, 1, 0, 0);
               ob.render(); // wird anschließend gerendert
           }

            Matrix4f modelmat = new Matrix4f();
            mvp = new Matrix4f();
            modelmat.identity();
            Matrix4f mv = new Matrix4f();
            mvp = new Matrix4f();
            modelmat.identity();
            mv = new Matrix4f();
            successMark.computeMatrix();
            modelmat.mul(successMark.tMat, modelmat);

            viewMat.mul(modelmat, mv);
            projectionMat.mul(mv, mvp);
            mvp.get(m4x4buf);

            glUniformMatrix4fv(basicShader.mvpLoc, false, m4x4buf);
            glUniform3f(basicShader.colorLoc, 0, 1, 0);
            successMark.render();

        }

        glDisableVertexAttribArray(basicShader.posLoc);
    }
    public  void update(float dt, float t) // ist die methode damit sich jedes mal das bild ändert.("wir gehen nicht mit ball, hindernisse kommen auf uns zu ")
    {
        if(!stopGame) {  //die geschwindigkeit wird erhöht
            if(!levelSuccess)
               score += 0.1;
            movementSpeed += 0.0005f;

            for (int i = 0; i < obstacles.size(); i++) {  //update der position und movementspeed
                var ob = obstacles.get(i);

                  ob.position.z += movementSpeed;

                  if (ob.position.z > 170 && score<250) {//falls gegenstand hinter dem spieler ist und 250 punkte noch nicht erreicht wurden
                    Random rn = new Random();
                    int n = -650 - -450 + 1;
                    int x = rn.nextInt() % n;
                    int randomNum =  -450 + x; //zufällige zahl zwischen -450 und -650
                    ob.position.z = randomNum;  // poisiton von gegenstand zurücksetzen

                }
                var res = SphereRectCollision(player, ob);
                if (res) {
                    System.out.println(res);
                    reset();
                }

            }
            if(score>250) //position von ball, wenn das level erreicht  wird
                successMark.position.z += movementSpeed;
            var res = SphereRectCollision(player, successMark); //überprüfung ob berürhung besteht
            if (res) {
                System.out.println(res);
                levelSuccess=true;
                stopGame=true;
            }
        }

        if(startJump) //leertaste.
        {
            amt= (float)Math.sin(Math.toRadians(ang));
            ang+=2.5;
            player.position.y+=(amt*jumpFactor);
            if(amt>=1)
            {
                cnt++;
                amt=0;
                ang=0;
               jumpFactor *=-1;
                // startJump=false;
                if(cnt==2)
                {
                    cnt=0;
                    startJump=false;
                }
            }
            player.computeMatrix();
        }
        if (window.getKeyState(GLFW_KEY_A)) { //-0.25 (links) so weit kann der ball nach links
           //move left
            player.position.x-=0.25;
            if(player.position.x<-25)
                player.position.x=-25;
            player.computeMatrix();
        }

        if (window.getKeyState(GLFW_KEY_D)) {
            //move Right
            player.position.x+=0.25;
            if(player.position.x>25)
                player.position.x=25;

            player.computeMatrix();
        }
        if (window.getKeyState(GLFW_KEY_SPACE)) {
            //move Right
          startJump=true;
        }
    }
    public void cleanup() {
    }
    boolean SphereRectCollision( Renderable sphere, Renderable rect) //prüfung ob ball gegen hindernis kommt
    {
        float Width=50f/2,Height=8f/2,Depth=7.3f/2,Radius=2; //werte vom Ball und balken
        float sphereXDistance = Math.abs(sphere.position.x - rect.position.x); //guckt wie weit von der x achse enternt ist, ist die distanz
        float sphereYDistance = Math.abs(sphere.position.y - rect.position.y);
        float sphereZDistance = Math.abs(sphere.position.z - rect.position.z);

        if (sphereXDistance >= (Width + Radius)) { return false; }
        if (sphereYDistance >= (Height + Radius)) { return false; }
        if (sphereZDistance >= (Depth + Radius)) { return false; }

        if (sphereXDistance < (Width)) { return true; }
        if (sphereYDistance < (Height)) { return true; }
        if (sphereZDistance < (Depth)) { return true; }

        float cornerDistance_sq = ((sphereXDistance - Width) * (sphereXDistance - Width)) +
                ((sphereYDistance - Height) * (sphereYDistance - Height) +
                        ((sphereYDistance - Depth) * (sphereYDistance - Depth)));

        return (cornerDistance_sq < (Radius * Radius));
    }
    public void reset() //stellt alles wieder auf null. spiel beginnt von neu
    {
        obstacleOne.position=new Vector3f(25,5,100);
        obstacleTwo.position=new Vector3f(-25,5,0);
        obstacleThree.position=new Vector3f(25,5,-100);
        obstacleFour.position=new Vector3f(-25,5,-200);
        obstacleFive.position=new Vector3f(25,5,-300);
        obstacleSix.position=new Vector3f(-25,5,-400);
        obstacleSeven.position=new Vector3f(25,5,-500);

        successMark.position=new Vector3f(0,5,-500);
        score =0;
        movementSpeed=0.5f;
        stopGame=false;
        levelSuccess=false;
    }
}
