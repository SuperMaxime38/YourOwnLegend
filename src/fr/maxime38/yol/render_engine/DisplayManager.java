package fr.maxime38.yol.render_engine;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;
import java.util.Calendar;
import java.util.TimeZone;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import fr.maxime38.yol.entities.Camera;
import fr.maxime38.yol.entities.Entity;
import fr.maxime38.yol.models.RawModel;
import fr.maxime38.yol.models.TexturedModel;
import fr.maxime38.yol.shaders.StaticShader;
import fr.maxime38.yol.textures.ModelTexture;
import fr.maxime38.yol.toolbox.KeyHandler;
import fr.maxime38.yol.utils.Matrix4f;
import fr.maxime38.yol.utils.Vector3f;

public class DisplayManager {
	private static Loader loader;
	private static Entity entity;
	private static StaticShader shader;
	
	//Handles keys
	public static KeyHandler keyHandler;
	
	//Camera
	static Camera camera;
	
	//Projection Matrix data
	static Matrix4f projectionMatrix;
	private static final float FOV = 70f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 10000f;
	private static int width = 1200;
	private static int height = 700;
	
	
	// The window handle
		private static long window;
		@SuppressWarnings("unused")
		private static GLFWKeyCallback keyCallback;

		public static void run() {
			System.out.println("["+Calendar.getInstance(TimeZone.getDefault()).getTime() + "]: Hello LWJGL " + Version.getVersion() + "!");

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
			
			System.out.println("["+Calendar.getInstance(TimeZone.getDefault()).getTime() + "]: Closed the game");
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
			
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

			// Setup a key callback. It will be called every time a key is pressed, repeated or released.
			// ------------ HANDLE key event ------------ //
			
			keyHandler = new KeyHandler();
			
			camera = new Camera(window, keyHandler, new Vector3f(0, 0, 0), 0, 0, 0);
			
			
			glfwSetKeyCallback(window, keyHandler.getCallback());

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
			
			
			
			//Load the shader
			shader = new StaticShader();
			
			
			//Create the projection Matrix (for the 3D)
			createProjectionMatrix();
			shader.start();
			shader.loadViewMatrix(camera);
			shader.loadProjectionMatrix(projectionMatrix);
			shader.stop();
			
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
			
			float[] UVs = {
					0,0,
					0,1,
					1,1,
					1,0
			};
			
			RawModel raw_model = loader.loadToVao(vertices, indices, UVs);
			ModelTexture texture = new ModelTexture(loader.loadTexture("dirt.png"));
			TexturedModel model = new TexturedModel(raw_model, texture);
			entity = new Entity(model, new Vector3f(0,0,-1), 0, 0, 0, 1);

			
		}
		
		public static void displayStuff() {
			entity.increasePosition(0, 0, 0f);
			entity.increaseScale(0f);
			entity.increaseRotation(0f, 0f, 0f);
			render(entity, shader);
		}
		
		
		public static void render(Entity entity, StaticShader shader) {
			EntityRenderer.render(entity, shader);
		}
		
		public static void createProjectionMatrix() {
			projectionMatrix = new Matrix4f();
			
			float aspect_ratio = (float) width/ (float) height;
			float yScale = (float) (1f / Math.tan(Math.toRadians(FOV/2f)));
			float xScale = yScale / aspect_ratio;
			float zp = FAR_PLANE + NEAR_PLANE;
			float zm = FAR_PLANE - NEAR_PLANE;

			projectionMatrix.m00 = xScale;
			projectionMatrix.m11 = yScale;
			projectionMatrix.m22 = -zp/zm;
			projectionMatrix.m23 = -1;
			projectionMatrix.m32 = -(2*FAR_PLANE*NEAR_PLANE)/zm;
			projectionMatrix.m33 = 0; //just to be safe
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
				
				//Move camera
				camera.move();
				
				//Shaders
				shader.start();
				
				//Load camera view
				shader.loadViewMatrix(camera);
				
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
