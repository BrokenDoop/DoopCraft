package brokendoop.doopmod.client.renderer;

import brokendoop.doopmod.core.entity.projectile.ProjectileGasFlame;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.util.helper.MathHelper;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class EntityRendererGasFlame extends EntityRenderer<ProjectileGasFlame> {

	protected float oSize = 0.5F;
	protected float oBBscale;

	protected IconCoordinate tex;

	public EntityRendererGasFlame() {
	}

	public void render(Tessellator tess, ProjectileGasFlame entity, double x, double y, double z, float yaw, float partialTicks) {
		TextureRegistry.particleAtlas.bind();

		int frame;

		float size = entity.scale;
		this.oSize = size;


		float maxSize = 0.5F;

		if (entity.tickCount < entity.smallTime) {
			// first frame 0
			frame = 0;
			entity.scale = 0.5F;
		}
		else if (entity.tickCount < entity.growthTime) {
			// frames 1-9
			int ticksInSection = entity.tickCount - entity.smallTime;
			int sectionLength = entity.growthTime - entity.smallTime;

			frame = 1 + ticksInSection * 9 / sectionLength;
			float progress = (ticksInSection + partialTicks) / (float)sectionLength;
			entity.scale = 0.5f + progress * (0.75F - 0.5F); // Scale from 0.5 to 1.5 for example
		}
		else if (entity.tickCount < entity.deathTime){
			// frames 10-13
			int ticksInSection = entity.tickCount - entity.growthTime;
			int sectionLength = entity.deathTime - entity.growthTime;
			frame = 10 + ticksInSection * 4 / sectionLength;

			entity.scale = this.oSize;
		} else {
			frame = 0;
			entity.scale = 0.5F;
		}



		// actually assign the texture based on the frame
		if (frame <= 13) {
			this.tex = TextureRegistry.getTexture("doopmod:particle/gas_flame/gas_flame_" + frame);
		} else {
			this.tex = null;
		}

		if (this.tex != null) {
			float u0 = (float) this.tex.getIconUMin();
			float u2 = (float) this.tex.getIconUMax();
			float v0 = (float) this.tex.getIconVMin();
			float v2 = (float) this.tex.getIconVMax();

			// we rotate the sprite after the first frame to lazily make it look more varied. lol
			double[][] uvCoords = new double[4][2];

			if (frame == 0) {
				// Force no rotation on frame 0
				uvCoords[0] = new double[]{u2, v2};
				uvCoords[1] = new double[]{u2, v0};
				uvCoords[2] = new double[]{u0, v0};
				uvCoords[3] = new double[]{u0, v2};
			} else {
				switch (entity.uvRotation) {
					case 0: // 0 degrees
						uvCoords[0] = new double[]{u2, v2};
						uvCoords[1] = new double[]{u2, v0};
						uvCoords[2] = new double[]{u0, v0};
						uvCoords[3] = new double[]{u0, v2};
						break;
					case 1: // 90 degrees
						uvCoords[0] = new double[]{u0, v2};
						uvCoords[1] = new double[]{u2, v2};
						uvCoords[2] = new double[]{u2, v0};
						uvCoords[3] = new double[]{u0, v0};
						break;
					case 2: // 180 degrees
						uvCoords[0] = new double[]{u0, v0};
						uvCoords[1] = new double[]{u0, v2};
						uvCoords[2] = new double[]{u2, v2};
						uvCoords[3] = new double[]{u2, v0};
						break;
					case 3: // 270 degrees
						uvCoords[0] = new double[]{u2, v0};
						uvCoords[1] = new double[]{u0, v0};
						uvCoords[2] = new double[]{u0, v2};
						uvCoords[3] = new double[]{u2, v2};
						break;
				}
			}



			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0f);
			GL11.glDisable(GL11.GL_LIGHTING);

			// we need to adjust this so it gets darker until it has no fullbright on the last 4 frames
			if (LightmapHelper.isLightmapEnabled()) {
				LightmapHelper.setLightmapCoord(LightmapHelper.getLightmapCoord(15, 15));
			}


			GL11.glPushMatrix();
			// bunch of code stolen from Particle and ParticleEngine to make it billboard.
			float yRot = this.renderDispatcher.viewLerpYaw;
			float xRot = this.renderDispatcher.viewLerpPitch;
			float xa = MathHelper.cos((float) (yRot * Math.PI) / 180.0F);
			float za = MathHelper.sin((float) (yRot * Math.PI) / 180.0F);
			float xa2 = -za * MathHelper.sin((float) (xRot * Math.PI / (double) 180.0F));
			float za2 = xa * MathHelper.sin((float) (xRot * Math.PI / (double) 180.0F));
			float ya = MathHelper.cos((float) (xRot * Math.PI / (double) 180.0F));
			double xOff = this.renderDispatcher.viewLerpPosX;
			double yOff = this.renderDispatcher.viewLerpPosY;
			double zOff = this.renderDispatcher.viewLerpPosZ;

			float xp = (float) (entity.xo + (entity.x - entity.xo) * (double) partialTicks - xOff);
			float yp = (float) (entity.yo + (entity.y - entity.yo) * (double) partialTicks - yOff + entity.bbHeight / 2);
			float zp = (float) (entity.zo + (entity.z - entity.zo) * (double) partialTicks - zOff);

			tess.startDrawingQuads();
			tess.addVertexWithUV((xp - xa * size - xa2 * size), (yp - ya * size), (zp - za * size - za2 * size), uvCoords[0][0], uvCoords[0][1]);
			tess.addVertexWithUV((xp - xa * size + xa2 * size), (yp + ya * size), (zp - za * size + za2 * size), uvCoords[1][0], uvCoords[1][1]);
			tess.addVertexWithUV((xp + xa * size + xa2 * size), (yp + ya * size), (zp + za * size + za2 * size), uvCoords[2][0], uvCoords[2][1]);
			tess.addVertexWithUV((xp + xa * size - xa2 * size), (yp - ya * size), (zp + za * size - za2 * size), uvCoords[3][0], uvCoords[3][1]);
			tess.draw();

			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_LIGHTING);
		}
	}
}
