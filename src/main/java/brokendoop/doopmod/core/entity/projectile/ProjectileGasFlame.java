package brokendoop.doopmod.core.entity.projectile;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityPainting;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.projectile.Projectile;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;

import java.util.List;

public class ProjectileGasFlame extends Projectile {

	public int smallTime = 4; // how long it stays on frame one looking like a small flame, 4 looks just right
	public int growthTime = this.smallTime + 18; // changing this will change how long it takes to get to the dissipation animation, effectively changes range
	public int deathTime = this.growthTime + 6; // essentially the tick length of the dissipation animation

	protected int scaleTicks = 0;

	public float scale;

	private float bbScale = 0.25F;

	public int uvRotation;


	public ProjectileGasFlame(World world) {
		super(world);
	}


	public ProjectileGasFlame(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public ProjectileGasFlame(World world, Mob owner) {
		super(world, owner);
		// redo the positioning so it comes out from the center of the bounding box.
		this.moveTo(owner.x, owner.y + owner.getHeadHeight() - this.bbHeight / 2, owner.z, owner.yRot, owner.xRot);
		this.x -= (MathHelper.cos(this.yRot / 180.0F * (float)Math.PI) * 0.16F);
		this.y -= 0.1;
		this.z -= (MathHelper.sin(this.yRot / 180.0F * (float)Math.PI) * 0.16F);
		this.setPos(this.x, this.y, this.z);

		Vec3 lookDir = owner.getLookAngle();
		this.setHeading(lookDir.x, lookDir.y, lookDir.z, 0.45f, 6);
	}

	public void initProjectile() {
		super.initProjectile();
		this.uvRotation = this.random.nextInt(4);
		this.fireImmune = true;
		this.damage = 8;
		this.defaultGravity = 0.004f;
		this.muteStepSounds = true;
	}

	protected boolean makeStepSound() {
		return false;
	}

	public void setHeading(double newMotionX, double newMotionY, double newMotionZ, float speed, float spread) {
		float velocity = MathHelper.sqrt(newMotionX * newMotionX + newMotionY * newMotionY + newMotionZ * newMotionZ);
		newMotionX /= velocity;
		newMotionY /= velocity;
		newMotionZ /= velocity;
		double i = 0.0075 * spread;
		newMotionX += this.random.nextGaussian() * i * 1.0f;
		newMotionY += this.random.nextGaussian() * i * 1.0f;
		newMotionZ += this.random.nextGaussian() * i * 1.0f;
		newMotionX *= speed;
		newMotionY *= speed;
		newMotionZ *= speed;
		this.xd = newMotionX;
		this.yd = newMotionY;
		this.zd = newMotionZ;
		float f3 = MathHelper.sqrt(newMotionX * newMotionX + newMotionZ * newMotionZ);
		this.yRotO = this.yRot = (float)(Math.atan2(newMotionX, newMotionZ) * (double)180.0F / Math.PI);
		this.xRotO = this.xRot = (float)(Math.atan2(newMotionY, f3) * (double)180.0F / Math.PI);
	}



	public void tick() {
		this.gravity = this.defaultGravity;
		this.stuckInCobweb = false;
		this.stuckInSpikes = false;
		super.baseTick();
		++this.ticksInAir;

		if (this.fireImmune || (this.world != null && this.world.isClientSide)) {
			this.remainingFireTicks = 0;
		}

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
				if (entity1.isPickable() && (entity1 != this.owner)) {
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
	}

	public void onHit(HitResult hitResult) {
		if (hitResult.entity != null && !(hitResult.entity instanceof EntityPainting)&& !this.isInWater()) {
			if (this.tickCount > this.growthTime) {
				this.damage = 4;
			}
			if (hitResult.entity.hurt(this.owner, this.damage, DamageType.FIRE)) {
				hitResult.entity.remainingFireTicks = 200;
				if (this.world != null) {
					this.world.playSoundAtEntity(null, this, "random.fizz", 0.4F, 1.4F + this.random.nextFloat() * 0.4F);
				}
			}

		}
	}

	public void afterTick() {



		this.updateBoundingBoxWithScaling();



		if (this.world != null) {
			double radius = this.bbScale * 1.1;

			double u = this.world.rand.nextDouble();
			double v = this.world.rand.nextDouble();
			double theta = u * 2.0 * Math.PI;
			double phi = Math.acos(2.0 * v - 1.0);
			double r = Math.cbrt(this.world.rand.nextDouble()) * radius;

			double offsetX = r * Math.sin(phi) * Math.cos(theta);
			double offsetY = r * Math.sin(phi) * Math.sin(theta);
			double offsetZ = r * Math.cos(phi);

			double centerY = this.y + this.bbHeight / 2;

			if (this.tickCount % 3 == 0 && !(this.tickCount > this.growthTime)) {
				this.world.spawnParticle("fixed_flame", this.x + offsetX, centerY + offsetY, this.z + offsetZ, this.xd, this.yd, this.zd, 0);
			} else if (this.tickCount > this.growthTime) {
				double smokeY = this.yd + this.gravity;
				this.world.spawnParticle("exhaust", this.x - offsetX, centerY - offsetY, this.z - offsetZ, this.xd, smokeY, this.zd, 0);
			}
		}




		if (this.isInWater()) {
			this.waterTick();
		}

		if (this.tickCount >= this.deathTime) {
			this.remove();
		}

		// deceleration and gravity
		float friction = 0.99F;

		if (this.collision || (this.tickCount > this.growthTime)) {
			if (this.collision){
				this.gravity = this.defaultGravity * 9;
				friction = 0.88F;
			} else {
				friction = 0.86F;
			}
		}
		this.xd *= friction;
		this.yd *= friction;
		this.zd *= friction;
		this.yd -= this.gravity;

		this.setPos(this.x, this.y, this.z); // this is needed for updating the bounding box size
		this.move(this.xd, this.yd, this.zd);
	}

	public void updateBoundingBoxWithScaling() {
		float oldScale = this.bbScale;

		// update bbScale based on current tick progress
		// -6 makes it reach its peak 3 frames before the deathTime animation
		if (this.tickCount > this.smallTime && this.tickCount < (this.growthTime - 6)) {
			++this.scaleTicks;
			if (this.scaleTicks > 1) {
				int ticksInSection = this.tickCount - this.smallTime;
				int sectionLength = (this.growthTime - 6) - this.smallTime;

				float progress = ticksInSection / (float) sectionLength;

				// scale from 0.25 to 1.1
				this.bbScale = 0.25F + progress * (1.125F - 0.25F);
				this.scaleTicks = 0;
			}
		} else if (this.tickCount < this.deathTime && this.tickCount > this.smallTime) {
			this.bbScale = oldScale;
		}

		float halfScale = this.bbScale / 2.0F;
		AABB newBB = AABB.getTemporaryBB(
			this.x - halfScale, this.y, this.z - halfScale,
			this.x + halfScale, this.y + this.bbScale, this.z + halfScale
		);

		List<AABB> collisions = this.world != null ? this.world.getCubes(this, newBB) : null;

		if (collisions != null && !collisions.isEmpty() && this.collision) {

			Vec3 pushVec = calculatePushOutVector(newBB, collisions);

			this.setPos(this.x + pushVec.x, this.y + pushVec.y, this.z + pushVec.z);
		}

		if (this.bbScale > 1.125F){ this.bbScale = 1.125F;}

		this.setSize(this.bbScale, this.bbScale);
	}

	private Vec3 calculatePushOutVector(AABB bb, List<AABB> collisions) {
		double pushX = 0;
		double pushY = 0;
		double pushZ = 0;

		for (AABB collisionBB : collisions) {

			double overlapX1 = collisionBB.maxX - bb.minX;
			double overlapX2 = bb.maxX - collisionBB.minX;
			double overlapZ1 = collisionBB.maxZ - bb.minZ;
			double overlapZ2 = bb.maxZ - collisionBB.minZ;
			double overlapY1 = collisionBB.maxY - bb.minY;
			double overlapY2 = bb.maxY - collisionBB.minY;

			double minOverlap = Math.min(
				Math.min(Math.abs(overlapX1), Math.abs(overlapX2)),
				Math.min(Math.min(Math.abs(overlapZ1), Math.abs(overlapZ2)),
					Math.min(Math.abs(overlapY1), Math.abs(overlapY2)))
			);

			if (minOverlap == Math.abs(overlapX1)) {
				pushX += overlapX1;
			} else if (minOverlap == Math.abs(overlapX2)) {
				pushX -= overlapX2;
			} else if (minOverlap == Math.abs(overlapZ1)) {
				pushZ += overlapZ1;
			} else if (minOverlap == Math.abs(overlapZ2)) {
				pushZ -= overlapZ2;
			} else if (minOverlap == Math.abs(overlapY1)) {
				pushY += overlapY1;
			} else if (minOverlap == Math.abs(overlapY2)) {
				pushY -= overlapY2;
			}
		}

		return Vec3.getTempVec3(pushX, pushY, pushZ);
	}

	public void waterTick() {
		if (this.world != null) {
			this.world.playSoundAtEntity(null, this, "random.fizz", 0.4F, 1.4F + this.random.nextFloat() * 0.4F);
			if (this.tickCount % 2 == 0) {
				this.world.spawnParticle("largesmoke", this.x, this.y, this.z, 0, 0, 0, 1);
			}
		}
		if (this.tickCount < this.growthTime) {
			this.tickCount = this.growthTime;
		}
		this.gravity = 0;
		this.xd = 0;
		this.yd = 0;
		this.zd = 0;
	}
	protected void checkOnWater(boolean addVelocity) {
		if (this.world != null) {
			this.wasInWater = this.checkAndHandleWater(addVelocity);
		}
	}

}
