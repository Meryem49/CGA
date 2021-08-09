package CGA.User.DataStructures;

import org.lwjgl.BufferUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDeleteShader;

public class BasicShader {
    public int programID;
    public int posLoc,mvpLoc,colorLoc;
    public BasicShader(String vertexShaderPath,String fragmentShaderPath) throws Exception
    {

        programID = 0;



        Path vPath = Paths.get(vertexShaderPath);
        Path fPath = Paths.get(fragmentShaderPath);

        String vSource = new String(Files.readAllBytes(vPath));
        String fSource = new String(Files.readAllBytes(fPath));

        int vShader = glCreateShader(GL_VERTEX_SHADER);
        if(vShader == 0)
            throw new Exception("Vertex shader object couldn't be created.");
        int fShader = glCreateShader(GL_FRAGMENT_SHADER);
        if(fShader == 0)
        {
            glDeleteShader(vShader);
            throw new Exception("Fragment shader object couldn't be created.");
        }

        glShaderSource(vShader, vSource);
        glShaderSource(fShader, fSource);

        glCompileShader(vShader);
        if(glGetShaderi(vShader, GL_COMPILE_STATUS) == GL_FALSE)
        {
            String log = glGetShaderInfoLog(vShader);
            glDeleteShader(fShader);
            glDeleteShader(vShader);
            throw new Exception("Vertex shader compilation failed:\n" + log);
        }

        glCompileShader(fShader);
        if(glGetShaderi(fShader, GL_COMPILE_STATUS) == GL_FALSE)
        {
            String log = glGetShaderInfoLog(fShader);
            glDeleteShader(fShader);
            glDeleteShader(vShader);
            throw new Exception("Fragment shader compilation failed:\n" + log);
        }

        programID = glCreateProgram();
        if(programID == 0)
        {
            glDeleteShader(vShader);
            glDeleteShader(fShader);
            throw new Exception("Program object creation failed.");
        }

        glAttachShader(programID, vShader);
        glAttachShader(programID, fShader);

        glLinkProgram(programID);

        if(glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE)
        {
            String log = glGetProgramInfoLog(programID);
            glDetachShader(programID, vShader);
            glDetachShader(programID, fShader);
            glDeleteShader(vShader);
            glDeleteShader(fShader);
            throw new Exception("Program linking failed:\n" + log);
        }

        glDetachShader(programID, vShader);
        glDetachShader(programID, fShader);
        glDeleteShader(vShader);
        glDeleteShader(fShader);

        getAttribAndUniformLocations();
    }
    public void getAttribAndUniformLocations()
    {
        posLoc =glGetAttribLocation(programID,"position");
        mvpLoc =glGetUniformLocation(programID,"mvp");
        colorLoc =glGetUniformLocation(programID,"u_color");
    }


}
