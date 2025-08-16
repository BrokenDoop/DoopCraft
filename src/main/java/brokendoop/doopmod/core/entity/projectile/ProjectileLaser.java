package brokendoop.doopmod.core.entity.projectile;

import brokendoop.doopmod.core.entity.particle.ParticleExhaust;
import brokendoop.doopmod.core.entity.particle.ParticleLaserDust;
import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.particle.Particle;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityPainting;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.projectile.Projectile;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProjectileLaser extends Projectile {

	protected int xTile;
	protected int yTile;
	protected int zTile;
	protected int xTileOld;
	protected int yTileOld;
	protected int zTileOld;

	protected int ticksSoundDelay = 0;

	protected float laserScale = 0.05625F;

	protected double laserSpeed;
	protected int laserPierce;
	protected int laserSpread;
	protected int laserBounce;
	protected float laserGravity;
	protected int laserDamage;
	protected int laserFireDamage;
	protected int laserType;
	protected boolean doesLaserBelongToPlayer; // pretty sure we can get rid of every instance of this

	public ProjectileLaser(World world) {
		this(world, 0);
	}

	public ProjectileLaser(World world, int laserType) {
		super(world);
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.xTileOld = this.xTile;
		this.yTileOld = this.yTile;
		this.zTileOld = this.zTile;
		this.doesLaserBelongToPlayer = false;
		this.laserType = laserType;
	}

	public ProjectileLaser(World world, double d, double d1, double d2, int laserType) {
		super(world, d, d1, d2);
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.xTileOld = this.xTile;
		this.yTileOld = this.yTile;
		this.zTileOld = this.zTile;
		this.doesLaserBelongToPlayer = false;
		this.laserType = laserType;
	}

	public ProjectileLaser(World world, Mob owner, boolean doesLaserBelongToPlayer, int laserType) {
		super(world, owner);
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.xTileOld = this.xTile;
		this.yTileOld = this.yTile;
		this.zTileOld = this.zTile;
		this.doesLaserBelongToPlayer = false;
		this.setDoesLaserBelongToPlayer(doesLaserBelongToPlayer);
		this.laserType = laserType;
		Vec3 lookDir = owner.getLookAngle();
		this.setHeading(lookDir.x, lookDir.y, lookDir.z,0, 0);
	}

	protected void initProjectile() {
		super.initProjectile();
		this.fireImmune = true;
		this.laserPierce = 3; //3
		this.laserSpread = 3; //3
		this.laserSpeed = 1.2; //1.2
		this.laserBounce = 0; //0
		this.laserGravity = 0F; //0
		this.laserDamage = 4; //3
		this.laserFireDamage = 8; //4
	}

	public void setHeading(double newMotionX, double newMotionY, double newMotionZ, float speed, float randomness) {
		float velocity = MathHelper.sqrt(newMotionX * newMotionX + newMotionY * newMotionY + newMotionZ * newMotionZ);
		newMotionX /= velocity;
		newMotionY /= velocity;
		newMotionZ /= velocity;

		double i = 0.0075 * this.laserSpread;

		newMotionX += this.random.nextGaussian() * i * 1.0f;
		newMotionY += this.random.nextGaussian() * i * 1.0f;
		newMotionZ += this.random.nextGaussian() * i * 1.0f;

		newMotionX *= this.laserSpeed;
		newMotionY *= this.laserSpeed;
		newMotionZ *= this.laserSpeed;

		this.xd = newMotionX;
		this.yd = newMotionY;
		this.zd = newMotionZ;

		float f3 = MathHelper.sqrt(newMotionX * newMotionX + newMotionZ * newMotionZ);
		this.yRotO = this.yRot = (float)(Math.atan2(newMotionX, newMotionZ) * (double)180.0F / Math.PI);
		this.xRotO = this.xRot = (float)(Math.atan2(newMotionY, f3) * (double)180.0F / Math.PI);
	}

	public void setDoesLaserBelongToPlayer(boolean flag) {
		this.doesLaserBelongToPlayer = flag;
	}

	public boolean laserBelongsToPlayer() {
		return this.doesLaserBelongToPlayer; // this might also be useless
	}


	public void lerpMotion(double xd, double yd, double zd) {
		this.xd = xd;
		this.yd = yd;
		this.zd = zd;
		if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
			float f = MathHelper.sqrt(xd * xd + zd * zd);
			this.yRot = (float)(Math.atan2(xd, zd) * (double)180.0F / Math.PI);
			this.xRot = (float)(Math.atan2(yd, f) * (double)180.0F / Math.PI);
			this.xRotO = this.xRot;
			this.yRotO = this.yRot;
			this.moveTo(this.x, this.y, this.z, this.yRot, this.xRot);
		}
	}


	public void tick() {
		this.xTileOld = this.xTile;
		this.yTileOld = this.yTile;
		this.zTileOld = this.zTile;
		this.baseTick();

		++this.ticksSoundDelay;
		++this.ticksInAir;

		HitResult movingobjectposition = this.getHitResult();
		Vec3 oldPosition = Vec3.getTempVec3(this.x, this.y, this.z);
		Vec3 newPosition = Vec3.getTempVec3(this.x + this.xd, this.y + this.yd, this.z + this.zd);
		if (movingobjectposition != null) {
			newPosition = Vec3.getTempVec3(movingobjectposition.location.x, movingobjectposition.location.y, movingobjectposition.location.z);
		}

		if (this.world != null && !this.world.isClientSide) {
			Entity entity = null;
			List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.bb.expand(this.xd, this.yd, this.zd).grow( 1.0F, 1.0F, 1.0F));
			double d = 0.0F;

			for (Entity entity1 : list) {
				if (entity1.isPickable() && (entity1 != this.owner || this.ticksInAir >= 5)) {
					float f4 = 0.3F;
					AABB axisalignedbb = entity1.bb.grow(f4, f4, f4);
					HitResult movingobjectposition1 = axisalignedbb.clip(oldPosition, newPosition);
					if (movingobjectposition1 != null) {
						double d1 = oldPosition.distanceTo(movingobjectposition1.location);
						if (d1 < d || d == (double) 0.0F) {
							entity = entity1;
							d = d1;
						}
					}
				}
			}
			if (entity != null) {
				movingobjectposition = new HitResult(entity);
			}
		}

		if (movingobjectposition != null) {
			this.onHit(movingobjectposition);
		}

		this.afterTick();
		if (laserType == 0){
			double pOffsetX = this.x - this.xd;
			double pOffsetY = this.y - this.yd;
			double pOffsetZ = this.z - this.zd;
			if (this.world != null) {
				this.world.spawnParticle("laserdust", pOffsetX, pOffsetY, pOffsetZ, 0.9, 0, 0, 1);
			}
			if (this.removed) {
				createParticleSphere(0.25, 8, 0.9, 0, 0, 1);
			}
		}
	}


	public void createParticleSphere(double radius, int numParticles, double red, double green, double blue, float scale) {
		createParticleSphere(radius, numParticles, red, green, blue, scale, 0);
	}
	// these methods are for creating particles around the impact/death point of the projectile
	// method needs to be fixed because it doesn't create the particles at the proper impact point (probably cause it stupidly factors in velocity)
	public void createParticleSphere(double radius, int numParticles, double red, double green, double blue, float scale, int particleChoice) {
		for (int i = 0; i < numParticles; i++) {
			double theta = 2 * Math.PI * Math.random();
			double phi = Math.acos(2 * Math.random() - 1);
			double velocityX = Math.sin(phi) * Math.cos(theta);
			double velocityY = Math.sin(phi) * Math.sin(theta);
			double velocityZ = Math.cos(phi);
			double d1 = MathHelper.sqrt(velocityX * velocityX + velocityY * velocityY + velocityZ * velocityZ);
			velocityX /= d1;
			velocityY /= d1;
			velocityZ /= d1;
			double d2 = 0.5 / (d1 / radius + 0.1);
			d2 *= (0.2 + random.nextFloat() * (0.3 - 0.2));
			velocityX *= d2;
			velocityY *= d2;
			velocityZ *= d2;

			double pOffsetX = this.x - this.xd * 1;
			double pOffsetY = this.y - this.yd * 1;
			double pOffsetZ = this.z - this.zd * 1;

			if (particleChoice == 0 || particleChoice == 1 || particleChoice == 2) { //this is so I can create a smoke sphere without an entirely new method, ik this is stupid
				if (particleChoice == 0 || particleChoice == 2) {
					spawnSphereParticle(new ParticleLaserDust(world, pOffsetX, pOffsetY, pOffsetZ, velocityX, velocityY, velocityZ, red, green, blue, scale));
				}
				if (particleChoice == 1 || particleChoice == 2) {
					spawnSphereParticle(new ParticleExhaust(world, pOffsetX, pOffsetY, pOffsetZ, velocityX, velocityY, velocityZ, scale, false));
				}
			}
		}
	}

	public static void spawnSphereParticle(Particle particle){ // we need this for spawning particles while accessing the color for laserDust, as well ass motion and offset - im sure there's a better way?
		if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().particleEngine == null)
			return;
		double d6 = Minecraft.getMinecraft().thePlayer.x - particle.x;
		double d7 = Minecraft.getMinecraft().thePlayer.y - particle.y;
		double d8 = Minecraft.getMinecraft().thePlayer.z - particle.z;
		double d9 = 16.0D;
		if (d6 * d6 + d7 * d7 + d8 * d8 > d9 * d9)
			return;
		Minecraft.getMinecraft().particleEngine.add(particle);
	}

	private void calculateBounces(){ //this is still broken, goes into blocks if shot into an inner corner or a wall at harsh angles
		if (xTile == xTileOld && yTile == yTileOld && zTile == zTileOld) return; // Don't bounce if block hit is the same as the previous block
		if (getHitResult() == null) return; // Don't bounce if ray-cast result is null
		Side sideHit = getHitResult().side;

//		if (sideHit !=null && getHitResult().hitType == HitResult.HitType.TILE && laserBounce > 0) {
//			if (sideHit == Side.TOP || sideHit == Side.BOTTOM) {
//				this.yd *= -1;
//			} else if (sideHit == Side.NORTH || sideHit == Side.SOUTH) {
//				this.zd *= -1;
//			} else if (sideHit == Side.EAST || sideHit == Side.WEST) {
//				this.xd *= -1;
//			}
//		}



		double hitX = getHitResult().location.x;
		double hitY = getHitResult().location.y;
		double hitZ = getHitResult().location.z;
		double relX = hitX - xTile;
		double relY = hitY - yTile;
		double relZ = hitZ - zTile;
		double deltaX = xd;
		double deltaY = yd;
		double deltaZ = zd;
		double normalX = 0.0;
		double normalY = 0.0;
		double normalZ = 0.0;
		switch (sideHit) {
			case EAST:
				normalX = -1.0;
				break;
			case WEST:
				normalX = 1.0;
				break;
			case TOP:
				normalY = -1.0;
				break;
			case BOTTOM:
				normalY = 1.0;
				break;
			case NORTH:
				normalZ = 1.0;
				break;
			case SOUTH:
				normalZ = -1.0;
				break;
		}
		double dotProduct = deltaX * normalX + deltaY * normalY + deltaZ * normalZ;
		deltaX -= 2 * dotProduct * normalX;
		deltaY -= 2 * dotProduct * normalY;
		deltaZ -= 2 * dotProduct * normalZ;

//		this.x = xTile + relX + deltaX * 0.05; // doing this just made the 'clip through corners' bug worse
//		this.y = yTile + relY + deltaY * 0.05;
//		this.z = zTile + relZ + deltaZ * 0.05;




		if (this.laserBounce > 0) { // If bounces available
			this.setHeading(deltaX, deltaY, deltaZ, 1.5f, 1f);
			if (this.world != null) {
				this.world.playSoundAtEntity(null, this, "doopmod:projectile.laser.bounce", 1F, 1F / (this.random.nextFloat() * 0.2F + 0.9F));
			}
			this.laserBounce--;
		} else {
			if (this.world != null) {
				this.world.playSoundAtEntity(null, this, "doopmod:projectile.laser.hit", 1.0F, 1F / (this.random.nextFloat() * 0.2F + 0.9F));
			}
			this.remove();
		}
	}

	public HitResult getHitResult() {
		Vec3 oldPosition = Vec3.getTempVec3(this.x, this.y, this.z);
		Vec3 newPosition = Vec3.getTempVec3(this.x + this.xd, this.y + this.yd, this.z + this.zd);
		return this.world != null ? this.world.checkBlockCollisionBetweenPoints(oldPosition, newPosition, false, true, false) : null;
	}

	public void onHit(HitResult hitResult) {
		if (hitResult.entity != null && !(hitResult.entity instanceof EntityPainting)) {
			if (hitResult.entity.hurt(this.owner, this.laserDamage, DamageType.COMBAT)) {
				// we reset the 'heartsHalvesLife' which represents the actual effective Iframes before applying the next damage instance to set it normally.
				Mob mob = (Mob) hitResult.entity;
				if (hitResult.entity instanceof Mob && !(mob.heartsHalvesLife > 0)) {
					mob.hurtTime = 0; // probably don't actually need this here but eh.
					mob.heartsHalvesLife = 0;
				}
				hitResult.entity.hurt(this.owner, this.laserFireDamage, DamageType.FIRE);

				if (this.world != null) {
					if (laserPierce > 0) {
						this.world.playSoundAtEntity(null, this, "doopmod:projectile.laser.pierce", 0.9F, 1F / (this.random.nextFloat() * 0.2F + 0.9F));
						laserPierce--;
					} else {
						this.remove();
						this.world.playSoundAtEntity(null, this, "doopmod:projectile.laser.hit", 1.0F, 1F / (this.random.nextFloat() * 0.2F + 0.9F));
					}
				}
			}
		} else {
			this.xTile = hitResult.x;
			this.yTile = hitResult.y;
			this.zTile = hitResult.z;

			// bounce method calculates normal hits too
			this.calculateBounces();
		}
	}

	public void afterTick() {
		this.x += this.xd;
		this.y += this.yd;
		this.z += this.zd;

		this.yRot = (float)(Math.atan2(this.xd, this.zd) * (double)180.0F / Math.PI);


		while (this.xRot - this.xRotO < -180.0F) {
			this.xRotO -= 360.0F;
		}

		while(this.xRot - this.xRotO >= 180.0F) {
			this.xRotO += 360.0F;
		}

		while(this.yRot - this.yRotO < -180.0F) {
			this.yRotO -= 360.0F;
		}

		while(this.yRot - this.yRotO >= 180.0F) {
			this.yRotO += 360.0F;
		}



		this.xRot = this.xRotO + (this.xRot - this.xRotO) * 0.2F;
		this.yRot = this.yRotO + (this.yRot - this.yRotO) * 0.2F;

		if (this.isInWater()) {
			this.waterTick();
		}

		//this is so it can't fly off forever
		if (this.ticksInAir == 400) {
			if (this.world != null) {
				this.world.playSoundAtEntity(null, this, "doopmod:projectile.laser.hit", 1.0F, 1F / (this.random.nextFloat() * 0.2F + 0.9F));
			}
			this.remove();
		}


		this.yd -= this.laserGravity;
		this.setPos(this.x, this.y, this.z);
	}



	public void waterTick() {
		if (this.world != null) {
			for (int k = 0; k < 4; ++k) {
				double particleDistance = 0.25F;
				this.world.spawnParticle("bubble", this.x - this.xd * particleDistance, this.y - this.yd * particleDistance, this.z - this.zd * particleDistance, this.xd, this.yd, this.zd, 0);
			}
			if (this.ticksSoundDelay >= 3) {
				this.world.playSoundAtEntity(null, this, "doopmod:projectile.laser.sizzle", 0.2F, 1.6F + (random.nextFloat() - random.nextFloat()) * 0.8F);
				this.ticksSoundDelay = 0;
			}
		}
	}


	public int getLaserType() {
		return this.laserType;
	}

	public float getLaserScale(float partialTick){
		return this.laserScale;
	}

	public void addAdditionalSaveData(@NotNull CompoundTag tag) {
		tag.putShort("xTile", (short)this.xTile);
		tag.putShort("yTile", (short)this.yTile);
		tag.putShort("zTile", (short)this.zTile);
		tag.putBoolean("player", this.doesLaserBelongToPlayer);
	}

	public void readAdditionalSaveData(@NotNull CompoundTag tag) {
		this.xTile = tag.getShort("xTile");
		this.yTile = tag.getShort("yTile");
		this.zTile = tag.getShort("zTile");
		this.doesLaserBelongToPlayer = tag.getBoolean("player");
	}
}
