package fr.maxime38.yol.toolbox;

import fr.maxime38.yol.entities.Camera;
import fr.maxime38.yol.utils.Matrix4f;
import fr.maxime38.yol.utils.Vector3f;

public class Maths {

	static Vector3f xVec = new Vector3f(1, 0, 0);
	static Vector3f yVec = new Vector3f(0, 1, 0);
	static Vector3f zVec = new Vector3f(0, 0, 1);
	static Vector3f scaleVec = new Vector3f(1,1,1);
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rotaX, float rotaY, float rotaZ, float scale) {
		Matrix4f matrix = new Matrix4f();
		
		Matrix4f.translate(translation, matrix, matrix);
		
		Matrix4f.rotate((float) Math.toRadians(rotaX), xVec, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotaY), yVec, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotaZ), zVec, matrix, matrix);
		
		scaleVec.set(scale, scale, scale);
		Matrix4f.scale(scaleVec, matrix, matrix);
		
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f matrix = new Matrix4f();
		
		Matrix4f.rotate((float) Math.toRadians(camera.getRotaX()), xVec, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRotaY()), yVec, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRotaZ()), zVec, matrix, matrix);
		Matrix4f.translate(new Vector3f(-camera.getPosition().x,-camera.getPosition().y,-camera.getPosition().z), matrix, matrix);
		
		
		return matrix;
	}
	
	
	
}
