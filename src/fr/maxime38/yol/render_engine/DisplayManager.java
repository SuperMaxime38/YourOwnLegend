package fr.maxime38.yol.render_engine;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SOFT_FULLSCREEN;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import fr.maxime38.yol.models.RawModel;
import fr.maxime38.yol.shaders.StaticShader;

public class DisplayManager {
	private static Loader loader;
	private static RawModel model;
	private static StaticShader shader;
	
	// The window handle
		private static long window;
		@SuppressWarnings("unused")
		private static GLFWKeyCallback keyCallback;

		public static void run() {
			System.out.println("Hello LWJGL " + Version.getVersion() + "!");

			init();
			loop();
			close();
		}
		
		private static void close() {
			
			loader.cleanUp();
			shader.cleanUp();

			// Free the window callbacks and destroy the window
			glfwFreeCallbacks(window);
			glfwDestroyWindow(window);

			// Terminate GLFW and free the error callback
			glfwTerminate();
			glfwSetErrorCallback(null).free();
			System.exit(0);
		}

		private static void init() {
			// Setup an error callback. The default implementation
			// will print the error message in System.err.
			GLFWErrorCallback.createPrint(System.err).set();

			// Initialize GLFW. Most GLFW functions will not work before doing this.
			if ( !glfwInit() )
				throw new IllegalStateException("Unable to initialize GLFW");

			// Configure GLFW
			glfwDefaultWindowHints(); // optional, the current window hints are already the default
			glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
			glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
			glfwWindowHint(GLFW_SOFT_FULLSCREEN, GLFW_TRUE); // full screen

			// Create the window
			window = glfwCreateWindow(1200, 700, "DevMode | Your Own Legend", NULL, NULL);
			if ( window == NULL )
				throw new RuntimeException("Failed to create the GLFW window");

			// Setup a key callback. It will be called every time a key is pressed, repeated or released.
			// ------------ HANDLE key event ------------ //
			glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {

			    @Override
			    public void invoke(long window, int key, int scancode, int action, int mods) {
			    	
			    	if(action == GLFW_PRESS) {
			    		switch(key) {
			    		case GLFW_KEY_ESCAPE:
			    			glfwSetWindowShouldClose(window, true);
			    			System.out.println("Game closed by user (ESCAPE_KEY)");
			    			break;
			    		case GLFW_KEY_E:
			    			System.out.println("Opened inv");
			    			break;
			    		case GLFW_KEY_SPACE:
			    			System.out.println("jumped ! yay");
			    			break;
			    		}
			    	}
			    }
			    
			    
			    
			});

			// Get the thread stack and push a new frame
			try ( MemoryStack stack = stackPush() ) {
				IntBuffer pWidth = stack.mallocInt(1); // int*
				IntBuffer pHeight = stack.mallocInt(1); // int*

				// Get the window size passed to glfwCreateWindow
				glfwGetWindowSize(window, pWidth, pHeight);

				// Get the resolution of the primary monitor
				GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

				// Center the window
				glfwSetWindowPos(
					window,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
				);
			} // the stack frame is popped automatically

			// Make the OpenGL context current
			glfwMakeContextCurrent(window);
			// Enable v-sync
			glfwSwapInterval(1);

			// Make the window visible
			glfwShowWindow(window);
			

			// This line is critical for LWJGL's interoperation with GLFW's
			// OpenGL context, or any context that is managed externally.
			// LWJGL detects the context that is current in the current thread,
			// creates the GLCapabilities instance and makes the OpenGL
			// bindings available for use.
			GL.createCapabilities();
			
			
			//Init rendering of stuff
			loader = new Loader();
			float[] vertices = {
					
					-0.5f, 0.5f, 0,
					-0.5f, -0.5f, 0,
					0.5f, -0.5f, 0,
					0.5f, 0.5f, 0,
			};
			
			int[] indices = {
				0,1,2,
				2,3,0
			};
			
			model = loader.loadToVao(vertices, indices);
			

			shader = new StaticShader();
			
		}
		
		public static void displayStuff() {
			DisplayManager.render(model);
		}
		
		
		public static void render(RawModel model) {
			EntityRenderer.render(model);
		}
		
		
		//to see fps
		private static long timepassed;
		private static long fps;

		private static void loop() {
			timepassed=System.nanoTime();;
			fps=0;

			// Set the clear color
			glClearColor(0.4f, 0.7f, 1.0f, 1f);
			
			
			

			// Run the rendering loop until the user has attempted to close
			// the window or has pressed the ESCAPE key.
			while ( !glfwWindowShouldClose(window) ) {
				if(System.nanoTime() - timepassed >= 1000000000) {
					timepassed = System.nanoTime();
					System.out.println("FPS > "+fps);
					fps=0;
				}
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
				
				//Shaders
				shader.start();
				
				//display 2D/3D i guess stuff onto the screen
				displayStuff();
				
				//Stop shader
				shader.stop();
				

				glfwSwapBuffers(window); // swap the color buffers

				// Poll for window events. The key callback above will only be
				// invoked during this call.
				glfwPollEvents();
				
				
				//update fps
				fps++;
			}
		}
}
