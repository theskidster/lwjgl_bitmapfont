package theskidster.lwjgl.shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import static org.lwjgl.opengl.GL20.*;
import theskidster.lwjgl.main.MainContainer;

/**
 * @author J Hoffman
 * Created: Mar 7, 2019
 */

public class Shader {
    
    public int id;
    
    public static Map<String, Integer> uniforms = new HashMap();
    
    /**
     * Loads and compiles a shader program.
     * 
     * @param fileName  Name of the file to be loaded.
     * @param type      The type of shader we want to load.
     */
    public Shader(String fileName, int type) {
        StringBuilder sb = new StringBuilder();
        
        try(InputStream in = Shader.class.getResourceAsStream(fileName); BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while((line = br.readLine()) != null) sb.append(line).append("\n");
        } catch(IOException e) {
            e.printStackTrace();
            throw new RuntimeException("failed to parse GLSL file, TODO: window based error messages.");
        }
        
        CharSequence src = sb.toString();
        
        id = glCreateShader(type);
        glShaderSource(id, src);
        glCompileShader(id);
        
        if(glGetShaderi(id, GL_COMPILE_STATUS) != GL_TRUE) throw new RuntimeException(glGetShaderInfoLog(id));
    }
    
    /**
     * Locates a uniform variable inside the shader, uniforms are a good way to 
     * interact with shader programs beyond just passing data to it, but they 
     * come with some restrictions.
     * 
     * @param name      Desired name we want to refer to later when using the uniform variable.
     * @param uniform   The name of the uniform as specified in the shader.
     */
    public static void addUniform(String name, String uniform) {
        if(glGetUniformLocation(MainContainer.prog, uniform) == -1) {
            throw new RuntimeException("ERROR: uniform returned -1, check the variable \"" + name + "\" \nTODO: window based error messages.");
        } else {
            uniforms.put(name, glGetUniformLocation(MainContainer.prog, uniform));
            System.out.println("NEW UNIFORM ADDED: \"" + name + "\" (" + uniforms.get(name) + ")");
        }
    }
    
}