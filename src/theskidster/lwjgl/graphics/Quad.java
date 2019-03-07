package theskidster.lwjgl.graphics;

/**
 * @author J Hoffman
 * Created: Mar 7, 2019
 */

public class Quad {
    
    public int width;
    public int height;
    
    /**
     * A simple data structure that stores an objects width and height. You 
     * could probably just use a Rectangle object to do the same thing, but I 
     * wanted to keep things simple.
     * 
     * @param width     Desired width of the object.
     * @param height    Desired height of the object.
     */
    public Quad(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
}