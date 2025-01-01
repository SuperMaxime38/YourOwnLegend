package fr.maxime38.yol.render_engine;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
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
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import fr.maxime38.yol.entities.Camera;
import fr.maxime38.yol.entities.Entity;
import fr.maxime38.yol.models.CubeModel;
import fr.maxime38.yol.models.RawModel;
import fr.maxime38.yol.models.TexturedModel;
import fr.maxime38.yol.shaders.StaticShader;
import fr.maxime38.yol.textures.ModelTexture;
import fr.maxime38.yol.toolbox.KeyHandler;
import fr.maxime38.yol.utils.Matrix4f;
import fr.maxime38.yol.utils.Vector3f;

public class DisplayManager {
	private static Loader loader;
	private static StaticShader shader;
	private static Map<TexturedModel, List<Entity>> entities2;
	
	//Thread
	private static int fps = 0;
	public int FPS = Integer.MAX_VALUE; // This one is used to unify speed (max value to not have speed 1000 for the 1st sec)
	private static final int THREAD_REFRESH_RATE = 300;
	
	
	//World
	private static TexturedModel model;
	private static List<Chunk> chunks;
	private static List<Vector3f> usedCoords;
	private static final int RENDER_DISTANCE = 32; //32 blocks dude (PS: c'est un rayon)
	private static final int CHUNK_SIZE = 16;
	
	//Handles keys
	public static KeyHandler keyHandler;
	
	//Camera
	static Camera camera;
	static Vector3f camPos;
	
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

		public void run() {
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

		private void init() {
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
			
			chunks = new ArrayList<Chunk>();
			
			entities2 = new HashMap<TexturedModel, List<Entity>>();
			
			keyHandler = new KeyHandler();
			
			camera = new Camera(this, window, keyHandler, new Vector3f(0, 0, 0), 0, 0, 0);
			camPos = camera.getPosition();
			
			usedCoords = new ArrayList<Vector3f>();
			
			
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
			glfwSwapInterval(0);

			// Make the window visible
			glfwShowWindow(window);
			

			// This line is critical for LWJGL's interoperation with GLFW's
			// OpenGL context, or any context that is managed externally.
			// LWJGL detects the context that is current in the current thread,
			// creates the GLCapabilities instance and makes the OpenGL
			// bindings available for use.
			GL.createCapabilities();
			

			GL11.glEnable(GL11.GL_DEPTH_TEST);
			
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
			
			
			RawModel raw_model = loader.loadToVao(CubeModel.vertices, CubeModel.indices, CubeModel.UVs);
			ModelTexture texture = new ModelTexture(loader.loadTexture("dirt.png"));
			model = new TexturedModel(raw_model, texture);

			//Initalize 2 new Thread for rendering
			new Thread(new Runnable() {
				
				Vector3f coord = new Vector3f(0, 0, 0);

				@Override
				public void run() {
					double drawInterval = 1000000000/THREAD_REFRESH_RATE;
					double delta = 0;
					long lastTime = System.nanoTime();
					long currentTime;
					
					long timer = 0;
					
					while (!glfwWindowShouldClose(window) ) {
						
						currentTime = System.nanoTime();
						
						delta += (currentTime - lastTime) / drawInterval;
						timer+= (currentTime - lastTime);
						lastTime = currentTime;
						
						
						if(delta >= 1) {
							updateTerrain();
							delta--;
						}
						
						
						
						
						if(timer >= 1000000000) {
							timer=0;
						}
					}
					
				}
				
				private void updateTerrain() {
					for(int x = (int) (camPos.x - RENDER_DISTANCE)/CHUNK_SIZE; x < (camPos.x + RENDER_DISTANCE)/CHUNK_SIZE; x++) {
						for(int z = (int) (camPos.z - RENDER_DISTANCE)/CHUNK_SIZE; z < (camPos.z + RENDER_DISTANCE)/CHUNK_SIZE ; z++) {
							
							coord = new Vector3f(x * CHUNK_SIZE, 0 , z * CHUNK_SIZE);
							
							if(!usedCoords.contains(coord)) {
								List<Entity> blocks = new ArrayList<Entity>();
								
								
								for(int i = 0; i < CHUNK_SIZE; i++) {
									for(int j = 0; j < CHUNK_SIZE; j++) {
										blocks.add(new Entity(model, new Vector3f((x*16) + i, 0, (z*16)+j), 0, 0, 0, 1));
									}
								}
								
								chunks.add(new Chunk(blocks, coord));
								
								usedCoords.add(coord);
							}
							
							
						}
					}
				}
				
			}).start();
			
		}
		
		public static void addEntity(Entity entity) {
			TexturedModel model = entity.getModel();
			entities2.computeIfAbsent(model, k -> new ArrayList<>()).add(entity);
		}
		
		public static void render() {
			//Shaders
			shader.start();
			
			//Load camera view
			shader.loadViewMatrix(camera);
			
			EntityRenderer.render(entities2);

			//Stop shader
			shader.stop();
			entities2.clear();
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

		private void loop() {
			timepassed=System.nanoTime();

			// Set the clear color
			glClearColor(0.4f, 0.7f, 1.0f, 1f);
			
			
			

			// Run the rendering loop until the user has attempted to close
			// the window or has pressed the ESCAPE key.
			while ( !glfwWindowShouldClose(window) ) {
				
				if(System.nanoTime() - timepassed >= 1000000000) {
					timepassed = System.nanoTime();
					System.out.println("FPS > "+fps);
					FPS = fps;
					fps=0;
				}
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
				
				//Move camera
				camera.move();
				camPos = camera.getPosition();
				
				//Update terrain --> in another thread (look above)
				
				//display 2D/3D i guess stuff onto the screen
				for(int i = 0; i < chunks.size(); i++) {
					Chunk c = chunks.get(i);
					
					Vector3f origin = c.getOrigin();
					
					int distX = (int) (camPos.x - origin.x);
					int distZ = (int) (camPos.z - origin.z);
					distX = Math.abs(distX);
					distZ = Math.abs(distZ);
					
					if(distX <= RENDER_DISTANCE && distZ <= RENDER_DISTANCE) {
						for(int j = 0; j < c.getBlocks().size(); j++) {
							addEntity(c.getBlocks().get(j));
						}
					}
					
				}
				render();
				

				glfwSwapBuffers(window); // swap the color buffers

				// Poll for window events. The key callback above will only be
				// invoked during this call.
				glfwPollEvents();
				
				
				//update fps
				fps++;
			}
		}
}
