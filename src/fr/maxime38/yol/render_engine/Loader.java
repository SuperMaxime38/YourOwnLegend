package fr.maxime38.yol.render_engine;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import fr.maxime38.yol.models.RawModel;
import fr.maxime38.yol.textures.Texture;

public class Loader {

	static List<Integer> vaos = new ArrayList<Integer>();
	static List<Integer> vbos = new ArrayList<Integer>();
	static List<Integer> textures = new ArrayList<Integer>();

	public RawModel loadToVao(float[] vertices, int[] indices, float[] UVs) {
		int vaoID = createVAO();

		vaos.add(vaoID);

		storeDataInAttributeList(vertices, 0, 3);
		storeDataInAttributeList(UVs, 1, 2);
		bindIndicesBuffer(indices);
		GL30.glBindVertexArray(0);
		
		return new RawModel(vaoID, indices.length);
	}
	
	public RawModel loadToVao(float[] vertices, float[] UVs) {
		int vaoID = createVAO();

		vaos.add(vaoID);

		storeDataInAttributeList(vertices, 0, 3);
		storeDataInAttributeList(UVs, 1, 2);
		GL30.glBindVertexArray(0);
		
		return new RawModel(vaoID, vertices.length);
	}
	
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void storeDataInAttributeList(float[] data, int atrributeNumber, int dimentions) {
		int vboID= GL15.glGenBuffers();
		
		vbos.add(vboID);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
		GL20.nglVertexAttribPointer(atrributeNumber, dimentions, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
		
		
	}
	
	public int loadTexture(String fileName) {
		Texture texture = new Texture(fileName);
		int ID = texture.getID();
		textures.add(ID);
		return ID;
	}
	
	public void cleanUp() {
		for(int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		for(int texture : textures) {
			GL11.glDeleteTextures(texture);
		}
	}
}
