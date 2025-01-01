package fr.maxime38.yol.entities;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;

import fr.maxime38.yol.render_engine.DisplayManager;
import fr.maxime38.yol.toolbox.KeyHandler;
import fr.maxime38.yol.utils.Pair;
import fr.maxime38.yol.utils.Vector3f;

public class Camera {

	Vector3f position;
	float rotaX, rotaY, rotaZ;

	float speed = 10f;
	float speedMult = (float) 1.25;
	float turn_speed = 0.05f;
	float moveAt = 0;
	float strafe = 0;// Déplacement latéral initialisé à zéro
	float up = 0;


	DisplayManager manager;
	
	KeyHandler handler;
	
	long window;
	
	double newX, newY, oldX, oldY;
    double deltaX, deltaY;
    Pair rotation;
	
	public Camera(DisplayManager manager, long window, KeyHandler handler, Vector3f position, float rotaX, float rotaY, float rotaZ) {
		super();
		this.manager = manager;
		this.handler = handler;
		this.position = position;
		this.rotaX = rotaX;
		this.rotaY = rotaY;
		this.rotaZ = rotaZ;
		this.window = window;

        newX = 600;
        newY = 350;
        oldX = newX;
        oldY = newY;
        rotation = new Pair(0,0);
	}
	
	public void move() {
		if(handler.isKeyPressed(GLFW_KEY_W)) {
			moveAt = (float) (-(speed / manager.FPS)*1.25);
		} else if(handler.isKeyPressed(GLFW_KEY_S)) {
			moveAt = (float) ((speed / manager.FPS)*1.25);
		} else {
			moveAt = 0;
		}
		
		// Déplacement latéral
	    if (handler.isKeyPressed(GLFW_KEY_A)) {
	        strafe = -speed / manager.FPS; // Aller à gauche
	    } else if (handler.isKeyPressed(GLFW_KEY_D)) {
	        strafe = speed / manager.FPS; // Aller à droite
	    } else {
	    	strafe = 0;
	    }
	    
	    if(handler.isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
	    	moveAt*=speedMult;
	    	strafe*=speedMult;
	    }
	    
	    //Déplacement vertical
	    if (handler.isKeyPressed(GLFW_KEY_SPACE)) {
	        up = -speed/manager.FPS; // Aller en haut
	    } else if (handler.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
	        up = speed/manager.FPS; // Aller en bas
	    } else {
	    	up = 0;
	    }
		getMouseRotation(); //Updates mouse rotation
		rotaX += -((float) rotation.getB() * turn_speed);
		rotaY += (float) rotation.getA() * turn_speed;
		
		
		float dx = (float) -(Math.sin(Math.toRadians(rotaY)) * moveAt - Math.cos(Math.toRadians(rotaY)) * strafe);
		float dy = (float) (Math.sin(Math.toRadians(rotaX)) * moveAt) - up;
		float dz = (float) (Math.cos(Math.toRadians(rotaY)) * moveAt + Math.sin(Math.toRadians(rotaY)) * strafe);

		position.x += dx;
		position.y += dy;
		position.z += dz;
	}
	
	private void getMouseRotation() {
		DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(window, x, y);
        x.rewind();
        y.rewind();

        newX = x.get();
        newY = y.get();

        deltaX = newX - oldX;
        deltaY = newY - oldY;

        if(deltaX > 360) deltaX -= 360;
        if(deltaY > 360) deltaY -= 360;
        
        float rotX = (float) deltaX;
        float rotY = (float) -deltaY;

        rotation.setA(rotX);
        rotation.setB(rotY);
        
        oldX = newX;
        oldY = newY;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	public float getRotaX() {
		return rotaX;
	}
	public float getRotaY() {
		return rotaY;
	}
	public float getRotaZ() {
		return rotaZ;
	}
	
	
}
