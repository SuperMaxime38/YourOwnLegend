package fr.maxime38.yol.chunks;

import java.util.ArrayList;
import java.util.List;

import fr.maxime38.yol.cubes.Block;
import fr.maxime38.yol.cubes.Vertex;
import fr.maxime38.yol.models.CubeModel;
import fr.maxime38.yol.utils.Vector3f;

public class ChunkMesh {
	
	private List<Vertex> vertices;
	private List<Float> positionsList;
	private List<Float> UVsList;
	private List<Float> normalsList;
	
	public float[] positions, UVs, normals;
	
	public Chunk chunk;
	
	
	
	public ChunkMesh(Chunk chunk) {
		this.chunk = chunk;
		
		vertices = new ArrayList<Vertex>();
		positionsList = new ArrayList<Float>();
		UVsList = new ArrayList<Float>();
		normalsList = new ArrayList<Float>();
		
		buildMesh();
		populateLists();
	}

	public void update(Chunk chunk) {
		this.chunk = chunk;
		
		buildMesh();
		populateLists();
	}
	
	private void buildMesh() {
		
		//Loop throught blocks in chunk to determine which faces are visible
		
		for(int i = 0; i < chunk.blocks.size(); i++) {
			Block block = chunk.blocks.get(i);
			boolean EAST = false, WEST = false, NORTH = false, SOUTH = false, UP = false, DOWN = false;
			
			for(int j = 0; j < chunk.blocks.size(); j++) {
				Block blockJ = chunk.blocks.get(j);
				
				if((block.x+1) == blockJ.x && block.y == blockJ.y && block.z == blockJ.z) { //There's a block on the EAST side (X positive)
					EAST = true;
				}
				if((block.x-1) == blockJ.x && block.y == blockJ.y && block.z == blockJ.z) {
					WEST = true;
				}
				if((block.x) == blockJ.x && block.y+1 == blockJ.y && block.z == blockJ.z) {
					UP = true;
				}
				if((block.x) == blockJ.x && block.y-1 == blockJ.y && block.z == blockJ.z) {
					DOWN = true;
				}
				if((block.x) == blockJ.x && block.y == blockJ.y && block.z+1 == blockJ.z) {
					NORTH = true;
				}
				if((block.x) == blockJ.x && block.y == blockJ.y && block.z-1 == blockJ.z) {
					SOUTH = true;
				}
			}
			
			//Add visible faces to Chunk Mesh
			
			//add vertices
			if(!EAST) {
				for(int k = 0; k < 6; k++) { //There's 6 vertices
					vertices.add(new Vertex(new Vector3f(CubeModel.EAST_POS[k].x + block.x, CubeModel.EAST_POS[k].y + block.y, CubeModel.EAST_POS[k].z + block.z), CubeModel.NORMALS[k], CubeModel.UVs[k]));
				}
			}
			if(!WEST) {
				for(int k = 0; k < 6; k++) { //There's 6 vertices
					vertices.add(new Vertex(new Vector3f(CubeModel.WEST_POS[k].x + block.x, CubeModel.WEST_POS[k].y + block.y, CubeModel.WEST_POS[k].z + block.z), CubeModel.NORMALS[k], CubeModel.UVs[k]));
				}
			}
			if(!UP) {
				for(int k = 0; k < 6; k++) { //There's 6 vertices
					vertices.add(new Vertex(new Vector3f(CubeModel.UP_POS[k].x + block.x, CubeModel.UP_POS[k].y + block.y, CubeModel.UP_POS[k].z + block.z), CubeModel.NORMALS[k], CubeModel.UVs[k]));
				}
			}
			if(!DOWN) {
				for(int k = 0; k < 6; k++) { //There's 6 vertices
					vertices.add(new Vertex(new Vector3f(CubeModel.DOWN_POS[k].x + block.x, CubeModel.DOWN_POS[k].y + block.y, CubeModel.DOWN_POS[k].z + block.z), CubeModel.NORMALS[k], CubeModel.UVs[k]));
				}
			}
			if(!NORTH) {
				for(int k = 0; k < 6; k++) { //There's 6 vertices
					vertices.add(new Vertex(new Vector3f(CubeModel.NORTH_POS[k].x + block.x, CubeModel.NORTH_POS[k].y + block.y, CubeModel.NORTH_POS[k].z + block.z), CubeModel.NORMALS[k], CubeModel.UVs[k]));
				}
			}
			if(!SOUTH) {
				for(int k = 0; k < 6; k++) { //There's 6 vertices
					vertices.add(new Vertex(new Vector3f(CubeModel.SOUTH_POS[k].x + block.x, CubeModel.SOUTH_POS[k].y + block.y, CubeModel.SOUTH_POS[k].z + block.z), CubeModel.NORMALS[k], CubeModel.UVs[k]));
				}
			}
			
			
		}
		
	}
	
	private void populateLists() {
		
		for(int i = 0; i < vertices.size(); i++) {
			positionsList.add(vertices.get(i).position.x);
			positionsList.add(vertices.get(i).position.y);
			positionsList.add(vertices.get(i).position.z);

			UVsList.add(vertices.get(i).UVs.x);
			UVsList.add(vertices.get(i).UVs.y);

			normalsList.add(vertices.get(i).normals.x);
			normalsList.add(vertices.get(i).normals.y);
			normalsList.add(vertices.get(i).normals.z);
		}

		positions = new float[positionsList.size()];
		UVs = new float[UVsList.size()];
		normals = new float[normalsList.size()];

		for (int i = 0; i < positionsList.size(); i++) {
			positions[i] = positionsList.get(i);
		}
		for (int i = 0; i < UVsList.size(); i++) {
			UVs[i] = UVsList.get(i);
		}
		for (int i = 0; i < normalsList.size(); i++) {
			normals[i] = normalsList.get(i);
		}
		
		
	}
	
}
