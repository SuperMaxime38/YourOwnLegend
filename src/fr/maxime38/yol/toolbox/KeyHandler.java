package fr.maxime38.yol.toolbox;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

import java.util.HashMap;

import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyHandler {
	
	protected static final int GLHW_KEY_Z = 0;
	private HashMap<Integer, Boolean> keysPressed;
	private GLFWKeyCallback callback;
	
	public KeyHandler() {
		keysPressed = new HashMap<Integer, Boolean>();
		
		callback = new GLFWKeyCallback() {

		    @Override
		    public void invoke(long window, int key, int scancode, int action, int mods) {
		    	
		    	if(action == GLFW_PRESS) {

	    			keysPressed.put(key, true);
		    		switch(key) {
		    		case GLFW_KEY_ESCAPE:
		    			glfwSetWindowShouldClose(window, true);
		    			System.out.println("Game closed by user (ESCAPE_KEY)");
		    			break;
		    		case GLFW_KEY_W:
		    			break;
		    		case GLFW_KEY_E:
		    			break;
		    		case GLFW_KEY_SPACE:
		    			break;
		    		}
		    	} else if(action == GLFW_RELEASE) {
    				keysPressed.put(key, false);
		    		switch(key) {
		    			case GLFW_KEY_W:
		    				//System.out.println("STOP AVANCER");
		    				break;
		    		}
		    	}
		    	
		    }
		    
		    
		    
		};
	}
	
	public GLFWKeyCallback getCallback() {
		return callback;
	}
	
	public boolean isKeyPressed(int key) {
		if(keysPressed.containsKey(key)) {
			return keysPressed.get(key);
		}
		return false;
	}
}
