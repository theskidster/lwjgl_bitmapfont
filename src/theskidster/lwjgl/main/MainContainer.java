package theskidster.lwjgl.main;

import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import theskidster.lwjgl.graphics.Instance;
import theskidster.lwjgl.graphics.Quad;
import theskidster.lwjgl.graphics.Texture;
import theskidster.lwjgl.shader.Shader;
import theskidster.lwjgl.ui.BitmapFont;

/**
 * @author J Hoffman
 * Created: Mar 7, 2019
 */

public class MainContainer implements Runnable {

    private int scale = 3;
    public static final int WIDTH = 320;
    public static final int HEIGHT = 224;
    public static int prog;
    
    private long context;
    
    private Vector3f bgColor = new Vector3f(0.0f, 0.0f, 200.0f);
    
    public static FloatBuffer fbModel = BufferUtils.createFloatBuffer(16);
    public static FloatBuffer fbView = BufferUtils.createFloatBuffer(16);
    public static FloatBuffer fbProjection = BufferUtils.createFloatBuffer(16);
    
    private BitmapFont bmf;
    private String text = "press 1, 2, 3, or 4 to change this text.";
    
    private Vector2f textPos = new Vector2f();
    
    /**
     * Used to organize all the GLFW stuff, probably could write a whole window 
     * management system, but thats outside of this programs scope.
     */
    public MainContainer() {
        if(!glfwInit()) throw new RuntimeException("glfw done goofed.");
        
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        
        context = glfwCreateWindow(WIDTH * scale, HEIGHT * scale, "LWJGL: instanced rendering. V2", NULL, NULL);
        GLFWVidMode vm = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(context, (vm.width() - WIDTH * scale) / 2, (vm.height() - HEIGHT * scale) / 2);
        
        glfwSetKeyCallback(context, (long window, int key, int scancode, int action, int mods) -> {
            if(key == GLFW_KEY_ESCAPE) glfwSetWindowShouldClose(context, true);
            
            if(key == GLFW_KEY_1) text = "i'm the man in the box.";
            if(key == GLFW_KEY_2) text = "welcome to our fortress tall";
            if(key == GLFW_KEY_3) text = "all your base are belong to us!";
            if(key == GLFW_KEY_4) text = "!@#$%^&*()0123456789\\+-/'";
        });
        
        glfwMakeContextCurrent(context);
        glfwSwapInterval(1);
        glfwShowWindow(context);
        GL.createCapabilities();
    }
    
    /**
     * Sets up our shaders, I prefer keeping my uniforms in a map so they're 
     * easier to find later. Typically this would be placed near whatever code 
     * you're using to load in assets.
     */
    public void init() {
        Shader vs = new Shader("ShaderVertex.glsl", GL_VERTEX_SHADER);
        Shader fs = new Shader("ShaderFragment.glsl", GL_FRAGMENT_SHADER);
        
        prog = glCreateProgram();
        glAttachShader(prog, vs.id);
        glAttachShader(prog, fs.id);
        glLinkProgram(prog);
        glUseProgram(prog);
        
        Shader.addUniform("model", "uModel");
        Shader.addUniform("view", "uView");
        Shader.addUniform("projection", "uProjection");
        
        //squishes the screen into an orthographic plane
        glUniformMatrix4fv(Shader.uniforms.get("projection"), false, new Matrix4f()
                .ortho(0, WIDTH, HEIGHT, 0, -1, 10)
                .get(fbProjection)
        );
        
        //redementary camera
        glUniformMatrix4fv(Shader.uniforms.get("view"), false, new Matrix4f()
                .get(fbView)
        );
        
        Instance data = new Instance(new Texture("bmf_system.png", 96, 24), new Quad(6, 6));
        bmf = new BitmapFont(data);
    }
    
    @Override
    public void run() {
        int glErrCode;
        
        init();
        
        while(!glfwWindowShouldClose(context)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glClearColor((bgColor.x / 255.0f), (bgColor.y / 255.0f), (bgColor.z / 255.0f), 0);
            
            bmf.draw("this isn't your typical blue screen =.)", textPos.set(9.0f, 9.0f));
            bmf.draw(text, textPos.set(WIDTH / 7, HEIGHT / 3));
            
            glfwSwapBuffers(context);
            glfwPollEvents();
            
            glErrCode = glGetError();
            if(glErrCode != GL_NO_ERROR) throw new RuntimeException("OpenGL error: " + glErrCode);
        }
        
        glfwTerminate();
    }
    
}