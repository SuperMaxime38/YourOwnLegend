package fr.maxime38.yol.render_engine;

import java.util.List;

import fr.maxime38.yol.entities.Entity;
import fr.maxime38.yol.utils.Vector3f;

public class Chunk {
	
	private List<Entity> blocks;
	private Vector3f origin;
	
	public Chunk(List<Entity> blocks, Vector3f origin) {
		this.blocks = blocks;
		this.origin = origin;
	}

	public List<Entity> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Entity> blocks) {
		this.blocks = blocks;
	}

	public Vector3f getOrigin() {
		return origin;
	}

	public void setOrigin(Vector3f origin) {
		this.origin = origin;
	}
	
	@Override
	public String toString() {
		return "Chunk(Origin: " + origin + "; blocks: " + blocks;
	}
	
}
