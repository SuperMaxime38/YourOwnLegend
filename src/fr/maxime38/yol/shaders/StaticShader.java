package fr.maxime38.yol.shaders;

public class StaticShader extends ShaderProgram{

	private static final String vertexFile = "shaders/mainVertex.glsl";
	private static final String fragmentFile = "shaders/mainFragment.glsl";
	
	public StaticShader() {
		super(vertexFile, fragmentFile);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute("position", 0);
	}

}
