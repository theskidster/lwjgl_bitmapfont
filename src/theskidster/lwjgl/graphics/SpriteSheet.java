package theskidster.lwjgl.graphics;

/**
 * @author J Hoffman
 * Created: Mar 7, 2019
 */

public class SpriteSheet {
    
    public float cellWidth;
    public float cellHeight;
    
    public Texture tex;
    public Quad quad;
    
    /**
     * This class takes an objects texture and cuts it up into segments in a way 
     * OpenGL likes.
     * 
     * @param quad  The objects quad.
     * @param tex   The texture atlas to be converted.
     */
    public SpriteSheet(Quad quad, Texture tex) {
        this.quad = quad;
        this.tex = tex;
        this.cellWidth = (float) quad.width / tex.width;
        this.cellHeight = (float) quad.height / tex.height;
    }
    
}