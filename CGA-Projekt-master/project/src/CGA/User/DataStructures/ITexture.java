package CGA.User.DataStructures;

import java.nio.ByteBuffer;

public interface ITexture {

    public void processTexture(ByteBuffer imageData, int width, int height, boolean genMipMaps) throws Exception;

    public void setTexParams(int wrapS, int wrapT, int minFilter, int magFilter) throws Exception;

    public void bind(int textureUnit);

    public void unbind();

    public int getTexID();

    public void cleanup();

}
