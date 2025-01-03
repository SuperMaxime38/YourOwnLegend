package fr.maxime38.yol.render_engine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import fr.maxime38.yol.entities.Entity;
import fr.maxime38.yol.models.TexturedModel;
import fr.maxime38.yol.shaders.StaticShader;
import fr.maxime38.yol.toolbox.Maths;
import fr.maxime38.yol.utils.Matrix4f;

public class EntityRenderer {
	
	static StaticShader shader = new StaticShader();
	
//	public static void render(Entity entity, StaticShader shader) {
//		GL30.glBindVertexArray(entity.getModel().getModel().getVaoID());
//		GL20.glEnableVertexAttribArray(0);
//		GL20.glEnableVertexAttribArray(1);
//		
//		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotaX(), entity.getRotaY(), entity.getRotaZ(), entity.getScale());
//		shader.loadTransformationMatrix(transformationMatrix);
//		
//		GL13.glActiveTexture(GL13.GL_TEXTURE0);
//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getTextureID());
//		
//		GL11.nglDrawElements(GL11.GL_TRIANGLES, entity.getModel().getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
//		
//		GL20.glDisableVertexAttribArray(0);
//		GL20.glDisableVertexAttribArray(1);
//		GL30.glBindVertexArray(0);
//	}
	
	public static void render(Map<TexturedModel, List<Entity>> entities) {
		
		for(TexturedModel model : entities.keySet()) {
			
			GL30.glBindVertexArray(model.getModel().getVaoID());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
			
			List<Entity> batch = entities.get(model);
			for(Entity entity : batch) {
				Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotaX(), entity.getRotaY(), entity.getRotaZ(), entity.getScale());
				shader.loadTransformationMatrix(transformationMatrix);
				

				GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getModel().getVertexCount());
				
			}
			
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL30.glBindVertexArray(0);
		}
		
	}
}
