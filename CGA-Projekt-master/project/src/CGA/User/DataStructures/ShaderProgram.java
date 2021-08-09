package CGA.User.DataStructures;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by Fabian on 16.09.2017.
 */
public class ShaderProgram
{
    private int programID;

    //Matrix buffers for setting matrix uniforms. Prevents allocation for each uniform
    private FloatBuffer m3x3buf;
    private FloatBuffer m4x4buf;
    private int currentTextureUnit;
    private int tuSave;

    /**
     * Creates a shader object from vertex and fragment shader paths
     * @param vertexShaderPath      vertex shader path
     * @param fragmentShaderPath    fragment shader path
     * @throws Exception if shader compilation failed, an exception is thrown
     */
    public ShaderProgram(String vertexShaderPath, String fragmentShaderPath) throws Exception
    {
        m3x3buf = BufferUtils.createFloatBuffer(9);
        m4x4buf = BufferUtils.createFloatBuffer(16);

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

        tuSave = 0;
    }

    /**
     * Sets the active shader program of the OpenGL render pipeline to this shader
     * if this isn't already the currently active shader
     */
    public void use()
    {
        int curprog = glGetInteger(GL_CURRENT_PROGRAM);
        if(curprog != programID)
            glUseProgram(programID);
        tuSave = 0;
    }

    public void saveTU()
    {
        tuSave = currentTextureUnit;
    }

    public void resetTU()
    {
        currentTextureUnit = tuSave;
    }

    /**
     * Frees the allocated OpenGL objects
     */
    public void cleanup()
    {
        glDeleteProgram(programID);
    }

    //setUniform() functions are added later during the course
    //float vector uniforms

    /**
     * Sets a single float uniform
     * @param name  Name of the uniform variable in the shader
     * @param value Value
     * @return returns false if the uniform was not found in the shader
     */
    public boolean setUniform(String name, float value)
    {
        if(programID == 0)
            return false;
        int loc = glGetUniformLocation(programID, name);
        if(loc != -1)
        {
            glUniform1f(loc, value);
            return true;
        }
        return false;
    }

    public boolean setUniform(String name, Matrix4f matrix, boolean transpose)
    {
        if(programID == 0)
            return false;
        int loc = glGetUniformLocation(programID, name);
        if(loc != -1)
        {
            matrix.get(m4x4buf);
            glUniformMatrix4fv(loc, transpose, m4x4buf);
            return true;
        }
        return false;
    }

    public boolean setUniform(String name, Vector2f vector)
    {
        if(programID == 0)
            return false;
        int loc = glGetUniformLocation(programID, name);
        if(loc != -1)
        {
            glUniform2f(loc, vector.x, vector.y);
            return true;
        }
        return false;
    }

    public boolean setUniform(String name, Vector3f vector)
    {
        if(programID == 0)
            return false;
        int loc = glGetUniformLocation(programID, name);
        if(loc != -1)
        {
            glUniform3f(loc, vector.x, vector.y, vector.z);
            return true;
        }
        return false;
    }

    public boolean setUniform(String name, int value){
        if(programID == 0)
            return false;
        int loc = glGetUniformLocation(programID, name);
        if(loc != -1)
        {
            glUniform1i(loc, value);
            return true;
        }
        return false;
    }

}
