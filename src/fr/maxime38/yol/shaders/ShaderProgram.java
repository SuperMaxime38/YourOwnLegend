package fr.maxime38.yol.shaders;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import fr.maxime38.yol.utils.FileUtils;
import fr.maxime38.yol.utils.Matrix4f;
import fr.maxime38.yol.utils.Vector2f;
import fr.maxime38.yol.utils.Vector3f;

public abstract class ShaderProgram {
	
	int programID, vertexShaderID, fragmentShaderID;
	FileUtils util;
	
	FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public ShaderProgram(String vertexFile, String fragmentFile) {
		util = new FileUtils();
		
		programID = GL20.glCreateProgram();
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);

		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		
		bindAttributes();
		
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		
		getAllUniformLocations();
	}
	
	protected abstract void getAllUniformLocations();
	
	protected int getUniformLocation(String varName) {
		return GL20.glGetUniformLocation(programID, varName);
	}
	
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	protected void load2DVector(int location, Vector2f vec) {
		GL20.glUniform2f(location, vec.x, vec.y);
	}
	

	protected void load3DVector(int location, Vector3f vec) {
		GL20.glUniform3f(location, vec.x, vec.y, vec.z);
	}
	
	protected void loadMatrix(int location, Matrix4f mat) {
		mat.store(matrixBuffer);
		matrixBuffer.flip();
		
		GL20.glUniformMatrix4fv(location, false, matrixBuffer);
		
		
	}
	
	protected void loadBoolean(int location, boolean bool) {
		float value = bool ? 0: 1;
		GL20.glUniform1f(location, value);
	}
	
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(String variableName, int attribute) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	private int loadShader(String path, int type) {
		String src = util.loadAsString(path);
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, src);
		GL20.glCompileShader(shaderID);
		
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID));
			System.err.println("Couldn't compile the shader !");
			System.exit(1);
		}
		
		return shaderID;
	}
}
