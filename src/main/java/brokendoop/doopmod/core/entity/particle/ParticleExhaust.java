package brokendoop.doopmod.core.entity.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.entity.particle.Particle;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;

@Environment(EnvType.CLIENT)
public class ParticleExhaust extends Particle{

	private final float oSize;

	private final boolean canBubble;

	public double bubbleXd;
	public double bubbleYd;
	public double bubbleZd;

	public ParticleExhaust(World world, double x, double y, double z, double motionX, double motionY, double motionZ, float scale) {
		this(world, x, y, z, motionX, motionY, motionZ, scale, true);
	}

	public ParticleExhaust(World world, double x, double y, double z, double motionX, double motionY, double motionZ, float scale, boolean canBubble) {
		super(world, x, y, z, 0.0F, 0.0F, 0.0F);

		double speed = (Math.random() + Math.random() + 1.0) * 0.15;
		this.xd = (motionX + (Math.random() * 2.0 - 1.0) * 0.4) * speed * 0.4;
		this.yd = (motionY + (Math.random() * 2.0 - 1.0) * 0.4) * speed * 0.4 + 0.1;
		this.zd = (motionZ + (Math.random() * 2.0 - 1.0) * 0.4) * speed * 0.4;
		// this is for fixing the particle class messing shi up with normalization (im assuming that's the problem)

		if (scale == 0) {
			scale = 1.0f;
		}

		this.bubbleXd = motionX;
		this.bubbleYd = motionY;
		this.bubbleZd = motionZ;

		this.canBubble = canBubble;
		this.xd *= 0.1;
		this.yd *= 0.1;
		this.zd *= 0.1;
		this.xd += motionX;
		this.yd += motionY;
		this.zd += motionZ;
		this.rCol = this.gCol = this.bCol = (float)(Math.random() * 0.3);
		this.size *= 0.75F;
		this.size *= scale;
		this.oSize = this.size;
		this.lifetime = (int)((double)8.0F / (Math.random() * 0.8 + 0.2));
		this.lifetime *= (int)scale;
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
		super.render(t, partialTick, xOff, yOff, zOff, xa, ya, za, xa2, za2);
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;


		if (this.world != null && this.canBubble && this.world.getBlockMaterial(MathHelper.floor(this.x), MathHelper.floor(this.y), MathHelper.floor(this.z)) == Material.water) {
			this.world.spawnParticle("bubble", this.x, this.y, this.z, this.bubbleXd, this.bubbleYd, this.bubbleZd, 0);
			this.remove();
		}



		if (this.age++ >= this.lifetime) {
			this.remove();
		}

		int val = 7 - this.age * 8 / this.lifetime;
		if (val >= 0) {
			this.tex = TextureRegistry.getTexture("minecraft:particle/puff_" + val);
		} else {
			this.tex = null;
		}

		this.yd += 0.004;
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
