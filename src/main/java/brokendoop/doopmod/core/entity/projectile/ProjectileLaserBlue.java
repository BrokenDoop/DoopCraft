package brokendoop.doopmod.core.entity.projectile;

import net.minecraft.core.entity.Mob;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;

public class ProjectileLaserBlue extends ProjectileLaser {

	public ProjectileLaserBlue(World world) {
		super(world, 1);
	}

	public ProjectileLaserBlue(World world, double d, double d1, double d2) {
		super(world, d, d1, d2, 1);
	}

	public ProjectileLaserBlue(World world, Mob entityLiving, boolean doesLaserBelongToPlayer) {
		super(world, entityLiving, doesLaserBelongToPlayer, 1);
	}

	protected void initProjectile() {
		super.initProjectile();
		this.fireImmune = true;
		this.laserPierce = 3; //3
		this.laserSpread = 3; //3
		this.laserSpeed = 0.7; //0.7
		this.laserBounce = 200; //6
		this.laserGravity = 0F; //0
		this.laserDamage = 2; //2
		this.laserFireDamage = 3; //3
	}

	public void tick() {
		super.tick();
		double pOffsetX = this.x - this.xd;
		double pOffsetY = this.y - this.yd;
		double pOffsetZ = this.z - this.zd;
		if (this.world != null) {
			this.world.spawnParticle("laserdust", pOffsetX, pOffsetY, pOffsetZ, 0.1, 0.1, 1, 1);
		}
		if (this.removed) {
			createParticleSphere(0.25, 8, 0.1, 0.1, 1, 1);
		}
	}
}
