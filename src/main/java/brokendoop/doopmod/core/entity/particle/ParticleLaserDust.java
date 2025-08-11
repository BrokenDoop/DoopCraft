package brokendoop.doopmod.core.entity.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.entity.particle.Particle;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.world.World;

@Environment(EnvType.CLIENT)
public class ParticleLaserDust extends Particle {

	private final float oSize;



	public ParticleLaserDust(World world, double x, double y, double z, double red, double green, double blue, float scale) {
		this(world, x, y, z, 0, 0, 0, red, green, blue, scale);
	}

	public ParticleLaserDust(World world, double x, double y, double z, double motionX, double motionY, double motionZ, double red, double green, double blue, float scale) {
		super(world, x, y, z, 0.0, 0.0, 0.0);

		double speed = (Math.random() + Math.random() + 1.0) * 0.15;
		this.xd = (motionX + (Math.random() * 2.0 - 1.0) * 0.4) * speed * 0.4;
		this.yd = (motionY + (Math.random() * 2.0 - 1.0) * 0.4) * speed * 0.4 + 0.1;
		this.zd = (motionZ + (Math.random() * 2.0 - 1.0) * 0.4) * speed * 0.4;
		// this is for fixing the particle class messing shi up with normalization (im assuming that's the problem)

		this.xd *= 0.1;
		this.yd *= 0.1;
		this.zd *= 0.1;
		this.xd += motionX;
		this.yd += motionY;
		this.zd += motionZ;
		float f4 = (float)Math.random() * 0.4F + 0.6F;
		float f5 = (float)(Math.random() * 0.2) + 0.8F;
		this.rCol = (float) (f5 * red * f4);
		this.gCol = (float) (f5 * green * f4);
		this.bCol = (float) (f5 * blue * f4);
		this.size *= 0.75F;
		this.size *= scale;
		this.oSize = this.size;
		this.lifetime = (int)(8.0 / (Math.random() * 0.8 + 0.2));
		this.lifetime = (int)((float)this.lifetime * scale);
		this.noPhysics = false;
	}

	public void render(Tessellator t, float partialTick, double xOff, double yOff, double zOff, float xa, float ya, float za, float xa2, float za2) {
		float l = ((float)this.age + partialTick) / (float)this.lifetime * 32.0F;
		if (l < 0.0F) {
			l = 0.0F;
		}

		if (l > 1.0F) {
			l = 1.0F;
		}

		this.size = this.oSize * l;
		if (this.tex != null) {
			float u0 = (float) this.tex.getIconUMin();
			float u2 = (float) this.tex.getSubIconU(1.0F);
			float v0 = (float) this.tex.getIconVMin();
			float v2 = (float) this.tex.getSubIconV(1.0F);
			float r = 0.1F * this.size;
			float x = (float) (this.xo + (this.x - this.xo) * partialTick - xOff);
			float y = (float) (this.yo + (this.y - this.yo) * partialTick - yOff);
			float z = (float) (this.zo + (this.z - this.zo) * partialTick - zOff);
			float br = 1.0F;
			if (LightmapHelper.isLightmapEnabled()) {
				t.setLightmapCoord(LightmapHelper.getLightmapCoord(15, 15));
			}
			t.setColorOpaque_F(this.rCol * br, this.gCol * br, this.bCol * br);
			t.addVertexWithUV((x - xa * r - xa2 * r), (y - ya * r), (z - za * r - za2 * r), u2, v2);
			t.addVertexWithUV((x - xa * r + xa2 * r), (y + ya * r), (z - za * r + za2 * r), u2, v0);
			t.addVertexWithUV((x + xa * r + xa2 * r), (y + ya * r), (z + za * r + za2 * r), u0, v0);
			t.addVertexWithUV((x + xa * r - xa2 * r), (y - ya * r), (z + za * r - za2 * r), u0, v2);
		}
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
		}

		int val = 7 - this.age * 8 / this.lifetime;
		if (val >= 0) {
			this.tex = TextureRegistry.getTexture("minecraft:particle/puff_" + val);
		} else {
			this.tex = null;
		}

		this.move(this.xd, this.yd, this.zd);
		if (this.y == this.yo) {
			this.xd *= 1.1;
			this.zd *= 1.1;
		}

		this.xd *= 0.96;
		this.yd *= 0.96;
		this.zd *= 0.96;
		if (this.onGround) {
			this.xd *= 0.7;
			this.zd *= 0.7;
		}
	}
}
