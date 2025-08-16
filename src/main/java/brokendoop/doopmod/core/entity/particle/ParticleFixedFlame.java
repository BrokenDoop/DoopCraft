package brokendoop.doopmod.core.entity.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.entity.particle.ParticleFlame;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;

@Environment(EnvType. CLIENT)
public class ParticleFixedFlame extends ParticleFlame {

	public ParticleFixedFlame(World world, double x, double y, double z, double motionX, double motionY, double motionZ, float scale) {
		this(world, x, y, z, motionX, motionY, motionZ, scale, Type.ORANGE);
	}

	public ParticleFixedFlame(World world, double x, double y, double z, double xd, double yd, double zd, float scale, Type type) {
		super(world, x, y, z, xd, yd, zd, type);
		this.noPhysics = false;
		double speed = (Math.random() + Math.random() + 1.0) * 0.15;
		this.xd = (xd + (Math.random() * 2.0 - 1.0) * 0.4) * speed * 0.4;
		this.yd = (yd + (Math.random() * 2.0 - 1.0) * 0.4) * speed * 0.4 + 0.1;
		this.zd = (zd + (Math.random() * 2.0 - 1.0) * 0.4) * speed * 0.4;
		this.xd = this.xd * 0.01 + xd;
		this.yd = this.yd * 0.01 + yd;
		this.zd = this.zd * 0.01 + zd;
	}

	public float getBrightness(float partialTick) {
		return 15;
	}

	public void tick() {
		super.tick();

		float friction = 0.98F;
		if (this.collision) {
			friction = 0.88F;
		}
		this.xd *= friction;
		this.yd *= friction;
		this.zd *= friction;
		if (this.world != null  && this.world.getBlockMaterial(MathHelper.floor(this.x), MathHelper.floor(this.y), MathHelper.floor(this.z)) == Material.water) {
			this.remove();
		}
	}
}
