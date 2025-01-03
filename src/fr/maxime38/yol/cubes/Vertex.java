package fr.maxime38.yol.cubes;

import fr.maxime38.yol.utils.Vector2f;
import fr.maxime38.yol.utils.Vector3f;

public class Vertex {
	
	public Vector3f position, normals;
	public Vector2f UVs;
	public Vertex(Vector3f position, Vector3f normals, Vector2f UVs) {
		
		this.position = position;
		this.normals = normals;
		this.UVs = UVs;
	}
	
	
}
