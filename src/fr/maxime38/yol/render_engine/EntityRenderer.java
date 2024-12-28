package fr.maxime38.yol.render_engine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import fr.maxime38.yol.entities.Entity;
import fr.maxime38.yol.shaders.StaticShader;
import fr.maxime38.yol.toolbox.Maths;
import fr.maxime38.yol.utils.Matrix4f;

public class EntityRenderer {
	
	public static void render(Entity entity, StaticShader shader) {
		GL30.glBindVertexArray(entity.getModel().getModel().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotaX(), entity.getRotaY(), entity.getRotaZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getTextureID());
		
		GL11.nglDrawElements(GL11.GL_TRIANGLES, entity.getModel().getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
}
