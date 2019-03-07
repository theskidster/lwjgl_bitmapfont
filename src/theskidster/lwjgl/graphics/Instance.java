package theskidster.lwjgl.graphics;

import org.joml.Matrix4f;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author J Hoffman
 * Created: Mar 7, 2019
 */

public class Instance {
    
    public int vao; //vertex array object
    public int vbo; //vertex buffer object
    public int ibo; //index buffer object (element buffer object)
    
    public float vertices[];
    public int indices[];
    
    public Texture tex;
    public Quad quad;
    public SpriteSheet sprite;
    public Matrix4f model;
    
    /**
     * Creates a new instance, all data specified here will be shared between 
     * each instance on the screen.
     * 
     * @param tex   The texture to be used.
     * @param quad  Geometry to render onto.
     */
    public Instance(Texture tex, Quad quad) {
        this.quad = quad;
        this.tex = tex;
        this.sprite = new SpriteSheet(quad, tex);
        this.model = new Matrix4f();
        
        /*
            because OpenGLs normalized device coordinates can make things a 
            bit tricky, I chose to pass some data from the structures above.
        */
        this.vertices = new float[] {
             //position                                 //texCoords
            -(quad.width / 2), -(quad.height / 2),      0.0f, 0.0f,                         //top left
            -(quad.width / 2),  (quad.height / 2),      0.0f, sprite.cellHeight,            //bot left
             (quad.width / 2),  (quad.height / 2),      sprite.cellWidth, sprite.cellHeight,//bot right
             (quad.width / 2), -(quad.height / 2),      sprite.cellWidth, 0.0f              //top right
        };
        
        //not essential, though I can sleep easy knowing my GPU isn't duplicating data! 
        this.indices = new int[] {
            0, 1, 2,
            2, 3, 0
        };
        
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        
        glVertexAttribPointer(0, 2, GL_FLOAT, false, (4 * Float.BYTES), 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, (4 * Float.BYTES), (2 * Float.BYTES));
        
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }
    
}