package CGA.User.Game;

/**
 * Created by Fabian on 16.09.2017.
 */
import imgui.*;
import imgui.flag.ImGuiFreeTypeBuilderFlags;
import imgui.flag.ImGuiWindowFlags;
import CGA.Framework.*;
import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

import static org.lwjgl.glfw.GLFW.*;

public class Game extends GameWindow
{
    private Scene scene;
    private Scene_Two scene_two;
    GameWindow windowContext;
    public Game(int width,
                int height,
                boolean fullscreen,
                boolean vsync,
                String title,
                int GLVersionMajor,
                int GLVersionMinor)
    {
        super(width, height, fullscreen, vsync, GLVersionMajor, GLVersionMinor, title, 4, 120.0f);
        //scene = new Scene(this);
        scene_two= new Scene_Two(this);
        scene_two.setScreenWidthAndHeight(width,height);
    }

    @Override
    protected void init(GameWindow gameWindow)
    {
   //     setCursorVisible(false);
       // if(!scene.init())
         //   quit();
        windowContext=gameWindow;
initImGui();
        if(!scene_two.init())
            quit();
    }

    @Override
    protected void shutdown() {
        //scene.cleanup();
        scene_two.cleanup();
    }

    @Override
    protected void update(float dt, float t)
    {

       //scene.update(dt, t);
        scene_two.update(dt,t);
    }

    @Override
    protected void render(float dt, float t)
    {
       // scene.render(dt, t);

        scene_two.render(dt,t);
        renderImGuiContent();
    }

    @Override
    protected void onMouseMove(double xpos, double ypos) {

       // scene.onMouseMove(xpos, ypos);
    }

    @Override
    protected void onKey(int key, int scancode, int action, int mode) {
       //scene.onKey(key, scancode, action, mode);
    }

    private void initImGui() //user interface
    {
        //imgui code
        ImGui.createContext();
        var  glslVersion = "#version 130";
        ImGui.styleColorsDark();
        windowContext.imGuiGlfw.init(windowContext.m_window, true);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 0);


        ImGuiIO io = ImGui.getIO();
        ImFontAtlas atlas=io.getFonts();
        ImFontConfig config=new ImFontConfig();
        config.setGlyphRanges(atlas.getGlyphRangesDefault());
        config.setPixelSnapH(true);
        atlas.addFontFromFileTTF("./project/assets/Fonts/Scream_Real.ttf", 100); //schriftart

        config.destroy();

        imGuiGl3.init(glslVersion);



        //font= io.getFonts().addFontFromFileTTF("./project/assets/Fonts/Scream_Real.ttf", 48);
        //  io.getFonts().build();
        //ends hereaaa
    }
    public void renderImGuiContent()
    {
        imGuiGlfw.newFrame(); //hier wird ein neues fenster erstellt
        ImGui.newFrame();
        int window_flags = ImGuiWindowFlags.NoBackground;
        window_flags += ImGuiWindowFlags.NoTitleBar;
        window_flags += ImGuiWindowFlags.NoScrollbar;
        window_flags += ImGuiWindowFlags.AlwaysAutoResize;
        window_flags += ImGuiWindowFlags.NoMove;
        if(!scene_two.levelSuccess) {
            ImGui.begin("new window", window_flags);
            ImGui.setWindowPos("new window", 0, 0); //steht oben links
            ImGui.text("Score :");
            ImGui.end();

            ImGui.begin("scoreText", window_flags);
            ImGui.setWindowPos("scoreText", 250, 0);
            ImGui.text(String.valueOf(Math.round(scene_two.score))); //die Scores werden aufgerundet(keine klmmerzahl)
            ImGui.end();
        }
if(scene_two.levelSuccess) {
    ImGui.begin("LevelCompleted", window_flags);
    ImGui.setWindowPos("LevelCompleted", getWindowWidth() / 3, getWindowHeight() / 5);
    ImGui.text("Level Completed");
    ImGui.text("Score:");
    ImGui.end();

    ImGui.begin("LevelCompletedScore", window_flags);
    ImGui.setWindowPos("LevelCompletedScore", getWindowWidth() / 2.2f, getWindowHeight() / 3.35f);
    ImGui.text(String.valueOf(Math.round(scene_two.score)));
    ImGui.end();

    ImGui.begin("RetryBtn", window_flags);
    ImGui.setWindowPos("RetryBtn", getWindowWidth() / 2.5f, getWindowHeight() / 2.35f);
    boolean res = ImGui.button("Play Again");
    if (res) {
        scene_two.reset();
    }
    ImGui.end();
}
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
        ImGui.endFrame();
    }
}
