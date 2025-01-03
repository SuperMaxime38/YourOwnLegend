package fr.maxime38.yol.chunks;

import java.util.List;

import fr.maxime38.yol.cubes.Block;
import fr.maxime38.yol.utils.Vector3f;

public class Chunk {
	
	public List<Block> blocks;
	private Vector3f origin;
	
	public Chunk(List<Block> blocks, Vector3f origin) {
		this.blocks = blocks;
		this.origin = origin;
	}
	
}
