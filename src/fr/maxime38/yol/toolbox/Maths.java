package fr.maxime38.yol.toolbox;

import fr.maxime38.yol.entities.Camera;
import fr.maxime38.yol.utils.Matrix4f;
import fr.maxime38.yol.utils.Vector3f;

public class Maths {
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rotaX, float rotaY, float rotaZ, float scale) {
		Matrix4f matrix = new Matrix4f();
		
		Matrix4f.translate(translation, matrix, matrix);
		
		Matrix4f.rotate((float) Math.toRadians(rotaX), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotaY), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotaZ), new Vector3f(0, 0, 1), matrix, matrix);
		
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f matrix = new Matrix4f();
		
		Matrix4f.rotate((float) Math.toRadians(camera.getRotaX()), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRotaY()), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRotaZ()), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.translate(new Vector3f(-camera.getPosition().x,-camera.getPosition().y,-camera.getPosition().z), matrix, matrix);
		
		
		return matrix;
	}
	
	
	
}
