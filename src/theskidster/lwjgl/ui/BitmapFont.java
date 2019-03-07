package theskidster.lwjgl.ui;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL33.*;
import theskidster.lwjgl.graphics.Instance;
import theskidster.lwjgl.main.MainContainer;
import theskidster.lwjgl.shader.Shader;

/**
 * @author J Hoffman
 * Created: Mar 7, 2019
 */

public class BitmapFont {
    
    private int vboPos;
    private int vboTex;
    private int lastCount;
    
    private float charXPos = 0.0f;
    private float charYPos = 0.0f;
    
    public Instance data;
    
    private String charset =    " !\"#$%&\'()*+,-./" + "\r" +
                                "0123456789:;<=>?" + "\r" +
                                "@abcdefghijklmno" + "\r" +
                                "pqrstuvwxyz[\\]^_";
    
    public Map<Character, Vector2f> texOffsets = new HashMap();
    
    /**
     * In a nutshell- this object takes the data that will be shared between 
     * each instance, and "builds" on top of it. This allows instances to have 
     * unique attributes such as different positions on the screen.
     * 
     * @param data  The data to be shared between each instance on the screen.
     */
    public BitmapFont(Instance data) {
        this.data = data;
        
        /*
            using the char data type seemed approprate here, since it let me 
            assocate the letters with the ones in the bmf_system.png file.  
        */
        for(char glyph : charset.toCharArray()) {
            if(glyph != '\r') {
                texOffsets.put(glyph, new Vector2f(charXPos, charYPos));
                charXPos += data.sprite.cellWidth;
            } else {
                charXPos = 0.0f;
                charYPos += data.sprite.cellHeight;
            }
        }
    }
    
    /**
     * Draws text at some desired position.
     * <br><br>
     * Real talk, some documentation would've just left you with that, but since
     * there's a lot going on here I'm obligated to explain. besides this is for 
     * study anyway right?
     * <br><br>
     * 
     * 1. Altering the model matrix will effect all instances, this lets us apply 
     *    transformations in a "global" manner. Such as drawing a string to a 
     *    certain position.
     * <br><br>
     * 
     * 2. Next, we bind the texture and corresponding vertex array, this makes 
     *    sure that we're using the right data before we draw, omitting this can 
     *    have strange effects because the GPU retains data. Drawing while the 
     *    wrong buffer/texture is bound often results in all objects sharing 
     *    the same texture or shape/mesh.
     * <br><br>
     * 
     * 3. As a general rule of thumb avoid executing "gl" commands every frame 
     *    if you can help it. Though its effects are less apparent on modern 
     *    hardware, such calls are still somewhat expensive. Here I leverage the
     *    amount of instances we're drawing to my advantage, so now it will only 
     *    execute when that number has changed.
     * <br><br>
     * 4. Here's where things start to get complicated. Because shaders must be 
     *    compiled before they can be used, we cant conveniently interact with 
     *    variables quite the same way we would in a Java program. Instead, we 
     *    can only interact with the shader by passing data to it, and having it 
     *    work on that data. For this reason we use a FloatBuffer object to 
     *    store the values that are subject to change at runtime, then send it 
     *    to the GPU with a glBindBuffer call.
     * <br><br>
     * 
     * 5. This is a special draw call reserved for instanced objects, notice how 
     *    we pass the length of the string as the final parameter? throw some 
     *    random int in there and see what happens.
     * 
     * @param text
     * @param position 
     */
    public void draw(String text, Vector2f position) {
        data.model.translation(position.x, position.y, 0); //1.
        
        glBindTexture(GL_TEXTURE_2D, data.tex.id);  //2.
        glBindVertexArray(data.vao);                //2.
        
        if(lastCount != text.length()) {    //3.
            offsetPosition(text.length());  //4.
            offsetTexture(text);            //4.
            lastCount = text.length();
        }
        
        glUniformMatrix4fv(Shader.uniforms.get("model"), false, data.model.get(MainContainer.fbModel));
        glDrawElementsInstanced(GL_TRIANGLES, data.indices.length, GL_UNSIGNED_INT, 0, text.length()); //5.
    }
    
    /**
     * Stores the positions which will be used to offset each individual 
     * instances position inside a FloatBuffer that is then sent to the GPU to
     * be processed by the vertex shader.
     * 
     * @param length    The number of instances to offset.
     */
    private void offsetPosition(int length) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(length * Float.BYTES);
        
        for(int i = 0; i < length; i++) {
            fb.put((i * 6.0f)).put(0.0f);
        }
        
        fb.flip(); //this is kinda important.

        vboPos = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboPos);
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, (2 * Float.BYTES), 0);
        glEnableVertexAttribArray(2);
        glVertexAttribDivisor(2, 1);
    }
    
    /**
     * Stores values used to offset each individual instances texture 
     * coordinates, this is where that map made from the sprite sheet comes in 
     * handy.
     * 
     * @param text  The text thats being rendered.
     */
    private void offsetTexture(String text) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(text.length() * Float.BYTES);
        
        for(char glyph : text.toCharArray()) {
            fb.put(texOffsets.get(glyph).x).put(texOffsets.get(glyph).y);
        }
        
        fb.flip();
        
        vboTex = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboTex);
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);

        glVertexAttribPointer(3, 2, GL_FLOAT, false, (2 * Float.BYTES), 0);
        glEnableVertexAttribArray(3);
        glVertexAttribDivisor(3, 1);
    }
    
}