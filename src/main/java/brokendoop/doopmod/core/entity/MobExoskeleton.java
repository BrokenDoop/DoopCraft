package brokendoop.doopmod.core.entity;

import brokendoop.doopmod.core.entity.projectile.ProjectileLaser;
import brokendoop.doopmod.interfaces.IWorldAdditions;
import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.monster.MobMonster;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

public class MobExoskeleton extends MobMonster{

	private int lasersFired = 1;

	public int maxLaserBurst = 2;

	public float armAimYaw;
	public float armAimPitch;

	public int exhaustTime;


	public MobExoskeleton(World world) {
		super(world);
		this.textureIdentifier = NamespaceID.getPermanent("doopmod", "skeletron");
		this.exhaustTime = 0;
		this.scoreValue = 800;
		this.mobDrops.add(new WeightedRandomLootObject(Items.DUST_REDSTONE.getDefaultStack(), 0, 2));
	}

	public int getMaxHealth() {
		return 40;
	}

	public boolean canBreatheUnderwater() {
		return true;
	}

	public String getLivingSound() {
		return "mob.skeleton";
	}

	protected String getHurtSound() {
		return "mob.skeletonhurt";
	}

	protected String getDeathSound() {
		return "mob.skeletonhurt";
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(15, this.attackTime, Integer.class);
	}

	public void onLivingUpdate() {
		if (this.world != null) {
			if (this.world.isClientSide) {
				this.attackTime = this.entityData.getInt(15);
			}
			if (this.exhaustTime > 0) {
				--this.exhaustTime;
			}
		}

		super.onLivingUpdate();
		this.doExhaust();
	}

	public void doExhaust(){
		double yawRad = Math.toRadians(this.yBodyRot);
		double offsetDistance = 0.1875;

		double offsetX = Math.sin(yawRad) * offsetDistance;
		double offsetZ = -Math.cos(yawRad) * offsetDistance;

		double particleX = this.x + offsetX;
		double particleY = this.y + 1.3125;
		double particleZ = this.z + offsetZ;

		double motionX = Math.sin(yawRad) * 0.08;
		double motionZ = -Math.cos(yawRad) * 0.08;

		if (this.world != null && this.exhaustTime == 0) {
			this.world.spawnParticle("exhaust", particleX, particleY, particleZ, motionX, 0.05, motionZ, 1);
			this.exhaustTime = 3 + (int)(Math.random() * 3);
		}
	}

	protected void attackEntity(@NotNull Entity entity, float distance) {
		if (distance < 16.0F) {

			double dX = entity.x - this.x;
			double dZ = entity.z - this.z;

			// 0.8 seems to be accurate for the projectile speed of 1.2 the red laser has
			double projectileSpeed = 0.8;

			double horizontalDistance = Math.sqrt(dX * dX + dZ * dZ);
			double travelTime = horizontalDistance / projectileSpeed;

			double futureX = entity.x + entity.xd * travelTime;
			double futureZ = entity.z + entity.zd * travelTime;

			double aimDX = futureX - this.x;
			double aimDZ = futureZ - this.z;




			if (this.attackTime == 0) {
				if (this.world != null && !this.world.isClientSide) {
					ProjectileLaser laser = new ProjectileLaser(this.world, this, false, 0);
					double d2 = entity.y + (double) entity.getHeadHeight() - 0.2 - laser.y;
					world.playSoundAtEntity(null, this, "doopmod:projectile.laser.shot", 0.3F, (float) (0.5 + this.lasersFired * 0.2 / (random.nextFloat() * 0.4F + 0.8F)));
					laser.setHeading(aimDX, d2, aimDZ, 0, 0);
					this.world.entityJoinedWorld(laser);
				}

				if (this.lasersFired < this.maxLaserBurst) {
					this.attackTime = 3; // delay before second shot
					this.lasersFired++;
				} else {
					this.attackTime = 40; // complete cooldown
					this.lasersFired = 1;
				}
			}
			this.yRot = (float)(Math.atan2(dZ, dX) * (double)180.0F / Math.PI) - 90.0F;
			if (distance < 10.0F) {
				this.hasAttacked = true;
			}
		}
	}

	protected void damageEntity(int damage, DamageType type) {

		// takes 50% of fire and fall damage
		if (type == DamageType.FIRE || type == DamageType.FALL || type == DamageType.BLAST) {
			float halfDamage = damage * 0.5F;

			// takes 75% of blast damage
			if (type == DamageType.BLAST) {
				halfDamage = damage * 0.75F;
			}

			damage = (this.random.nextFloat() > 0.5F) ? (int)Math.ceil(halfDamage) : (int)Math.floor(halfDamage);
		}


		super.damageEntity(damage, type);
	}

	public boolean hurt(Entity attacker, int damage, DamageType type) {
		Entity lastTarget = this.target;

		if (super.hurt(attacker, damage, type)) {
			if (this.target instanceof MobExoskeleton) {
				this.target = lastTarget;
			}
			return true;
		} else {
			return false;
		}
	}

	public void knockBack(Entity entity, int i, double d, double d1) {
		float f = MathHelper.sqrt(d * d + d1 * d1);
		float power = 0.25F;
		this.xd /= 2.0F;
		this.yd /= 2.0F;
		this.zd /= 2.0F;
		this.xd -= d / f * power;
		this.yd += power;
		this.zd -= d1 / f * power;
		if (this.yd > 0.4F) {
			this.yd = 0.4F;
		}
	}

	protected Entity findPlayerToAttack() {
		Player entityplayer = this.world != null ? this.world.getClosestPlayerToEntity(this, 16.0F) : null;
		Mob entityMob = this.world != null ? ((IWorldAdditions) this.world).doopmod$getClosestMobToEntity(this, 16.0F) : null;
		Entity targetMob = null;

		if (entityMob instanceof MobExoskeleton) entityMob = null;

		if (entityplayer != null && this.canEntityBeSeen(entityplayer) && entityplayer.getGamemode().areMobsHostile()) {
			targetMob = entityplayer;
		}

		if (entityMob != null && this.canEntityBeSeen(entityMob)) {
			targetMob = entityMob;
		}

		return targetMob;
	}


	public void addAdditionalSaveData(@NotNull CompoundTag tag) {
		super.addAdditionalSaveData(tag);
	}

	public void readAdditionalSaveData(@NotNull CompoundTag tag) {
		super.readAdditionalSaveData(tag);
	}


}
