package fr.maxime38.yol.entities;

import fr.maxime38.yol.models.TexturedModel;
import fr.maxime38.yol.utils.Vector3f;

public class Entity {
	
	TexturedModel model;
	Vector3f position;
	float rotaX, rotaY, rotaZ;
	float scale;
	
	public Entity(TexturedModel model, Vector3f position, float rotaX, float rotaY, float rotaZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotaX = rotaX;
		this.rotaY = rotaY;
		this.rotaZ = rotaZ;
		this.scale = scale;
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x+=dx;
		this.position.y+=dy;
		this.position.z+=dz;
	}
	
	public void increaseRotation(float dx, float dy, float dz) {
		this.rotaX += dx;
		this.rotaY += dy;
		this.rotaZ += dz;
	}
	
	public void increaseScale(float scale) {
		this.scale += scale;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotaX() {
		return rotaX;
	}

	public void setRotaX(float rotaX) {
		this.rotaX = rotaX;
	}

	public float getRotaY() {
		return rotaY;
	}

	public void setRotaY(float rotaY) {
		this.rotaY = rotaY;
	}

	public float getRotaZ() {
		return rotaZ;
	}

	public void setRotaZ(float rotaZ) {
		this.rotaZ = rotaZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	
	
}
