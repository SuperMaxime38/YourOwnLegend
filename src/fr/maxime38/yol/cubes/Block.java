package fr.maxime38.yol.cubes;

public class Block {
	
	public int x, y, z;
	public static enum BlockType {
		DIRT,
		GRASS
	}
	
	public BlockType type;
	
	public Block(int x, int y, int z, BlockType type) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
	}
	
	
}
