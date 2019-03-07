package theskidster.lwjgl.graphics;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.STBI_rgb_alpha;
import static org.lwjgl.stb.STBImage.stbi_load;
import org.lwjgl.system.MemoryStack;

/**
 * @author J Hoffman
 * Created: Mar 7, 2019
 */

public class Texture {
    
    public int id;
    public int width;
    public int height;
    
    private ByteBuffer bb;
    
    /**
     * 
     * @param fileName
     * @param width
     * @param height 
     */
    public Texture(String fileName, int width, int height) {
        this.width = width;
        this.height = height;
        
        try(MemoryStack ms = MemoryStack.stackPush()) {
            IntBuffer imageWidth = ms.mallocInt(1);
            IntBuffer imageHeight = ms.mallocInt(1);
            IntBuffer imageChannels =  ms.mallocInt(1);
            
            bb = stbi_load(getClass().getResource("/theskidster/lwjgl/assets/").toString().substring(6) + fileName, imageWidth, imageHeight, imageChannels, STBI_rgb_alpha);
        }
        
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, bb);
    }
    
}