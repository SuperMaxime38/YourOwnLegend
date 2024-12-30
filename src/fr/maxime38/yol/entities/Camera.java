package fr.maxime38.yol.entities;

import fr.maxime38.yol.toolbox.KeyHandler;
import fr.maxime38.yol.utils.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

public class Camera {

	Vector3f position;
	float rotaX, rotaY, rotaZ;
	
	float speed = 0.1f;
	
	KeyHandler handler;
	
	public Camera(KeyHandler handler, Vector3f position, float rotaX, float rotaY, float rotaZ) {
		super();
		this.handler = handler;
		this.position = position;
		this.rotaX = rotaX;
		this.rotaY = rotaY;
		this.rotaZ = rotaZ;
	}
	
	public void move() {
		if(handler.isKeyPressed(GLFW_KEY_W)) {
			position.z += -speed;
		}
		if(handler.isKeyPressed(GLFW_KEY_A)) {
			position.z += speed;
		}
		if(handler.isKeyPressed(GLFW_KEY_S)) {
			position.z -= speed;
		}
		if(handler.isKeyPressed(GLFW_KEY_D)) {
			position.z += -speed;
		}
		
		
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
