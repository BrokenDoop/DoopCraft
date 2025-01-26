package brokendoop.doopmod.core.entity;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.HitResult;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockTransparent;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.monster.EntityMonster;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.World;

public class EntityGeist extends EntityMonster {

	protected float initialMoveSpeed;
	public float prevLimbDrag;
	public float limbDrag;
	public float setLimbDrag;
	public float angerBuffer = 0;
	public float seenBuffer = 0;
	public static class Limb {
		public float rotationTargetX = (float) Math.PI / 8;
		public float rotationTargetY;
		public float rotationTargetZ;
		public float rotationCurrentX = (float) Math.PI / 8;
		public float rotationCurrentY;
		public float rotationCurrentZ;
	}
	public Limb rightArm = new Limb();
	public Limb leftArm = new Limb();
	public Limb rightLeg = new Limb();
	public Limb leftLeg = new Limb();

	public EntityGeist(World world) {
		super(world);
		this.moveSpeed = 0.5F;
		this.initialMoveSpeed = this.moveSpeed;
		this.attackStrength = 5;

	}
	protected void init() {
		super.init();
		this.entityData.define(16, (byte) 0);
	}

	protected boolean makeStepSound() {
		return false;
	}

	public boolean canRide() {
		return false;
	}

	public String getEntityTexture() { //skin variants don't work rn
		if (this.isGeistAngry()) {
			return "/assets/doopmod/textures/entity/geist_angry/" + this.getSkinVariant() + ".png";
		} else if (this.isGeistShy()) {
			return "/assets/doopmod/textures/entity/geist_shy/" + this.getSkinVariant() + ".png";
		} else
			return "/assets/doopmod/textures/entity/geist/" + this.getSkinVariant() + ".png";
	}
	public String getDefaultEntityTexture() {
		if (this.isGeistAngry()) {
			return "/assets/doopmod/textures/entity/geist_angry/0.png";
		} else if (this.isGeistShy()) {
			return "/assets/doopmod/textures/entity/geist_shy/0.png";
		} else
			return "/assets/doopmod/textures/entity/geist/0.png";
	}

	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("Angry", this.isGeistAngry());
		tag.putBoolean("Shy", this.isGeistShy());
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setGeistAngry(tag.getBoolean("Angry"));
		this.setGeistShy(tag.getBoolean("Shy"));
	}

	public void moveEntityWithHeading(float moveStrafing, float moveForward) {
		super.moveEntityWithHeading(moveStrafing, moveForward);



		//logic for limbs dragging
		this.prevLimbDrag = this.limbDrag;
		double deltaX = this.x - this.xo;
		double deltaZ = this.z - this.zo;
		float distanceMoved = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ) * 4.0F;
		if (distanceMoved > 1.0F) {
			distanceMoved = 1.0F;
		}

		double forwardX = -Math.sin(Math.toRadians(this.yRot));
		double forwardZ = Math.cos(Math.toRadians(this.yRot));
		double forwardMovement = this.xd * forwardX + this.zd * forwardZ;
		if(forwardMovement >= 0) {
			this.limbDrag += (distanceMoved - this.limbDrag) * 0.4F;
		} else if (forwardMovement < 0){
			this.limbDrag += (-distanceMoved - this.limbDrag) * 0.4F;
		}

	}

	public void baseTick () {
		super.baseTick();
		if (this.angerBuffer > 0) {
			--this.angerBuffer;
		}
		if (this.seenBuffer > 0) {
			--this.seenBuffer;
		}
	}
	protected void updatePlayerActionState() {
		super.updatePlayerActionState();
		// It tries to check if the nearest player looking at it is looking at it, otherwise it would only try to check the nearest player and ignore if a further player is looking at it.
		// It does this so anyone who is looking at it can put it in the shy state as long as it has aggro on somebody, without needing to check every player.
		boolean isSeen = this.isSeenByPlayer(getClosestPlayerToEntityLooking());

		//handle state switch and behaviour
		if(this.entityToAttack != null && this.distanceTo(this.entityToAttack) < 16) {
			if (isSeen && !(this.angerBuffer > 0) || this.seenBuffer > 0) { //shy
				this.setGeistAngry(false);
				this.setGeistShy(true);
				if (isSeen) {
					this.seenBuffer = 10; //set the buffer, so it stays "shy" for a moment after looking away
				}
				this.moveSpeed = 0;
				if (this.isGeistTooClose()) { //too close and shy
					this.moveSpeed = -0.1F;
				}
			} else { // angry
				this.setGeistAngry(true);
				this.setGeistShy(false);
				this.moveSpeed = this.initialMoveSpeed * 2.6F;
				if (this.isGeistTooClose()) { //too close and angry
				this.moveSpeed = this.initialMoveSpeed * 2.2F;
				}
			}
		} else {
			if (this.entityToAttack != null) {
				this.pathToEntity = null;
			}
			this.moveSpeed = this.initialMoveSpeed;
			this.setGeistAngry(false);
			this.setGeistShy(false);
		}

	}


	protected Entity findPlayerToAttack() {
		EntityPlayer entityplayer = this.world.getClosestPlayerToEntity(this, 6.0);
		return entityplayer != null && this.canEntityBeSeen(entityplayer) && entityplayer.getGamemode().areMobsHostile() ? entityplayer : null;
	}

	public boolean hurt(Entity attacker, int i, DamageType type) {
		if (super.hurt(attacker, i, type)) {
			if (this.passenger != attacker && this.vehicle != attacker) {
				if (attacker != this) {
					this.seenBuffer = 0;
					this.angerBuffer = 80;
				}
            }
            return true;
        } else {
			return false;
		}
	}

	protected void attackEntity(Entity entity, float distance) {
		if (!(this.isGeistShy()) && this.attackTime <= 0 && distance < 2.0F && entity.bb.maxY > this.bb.minY && entity.bb.minY < this.bb.maxY) {
			this.attackTime = 20;
			entity.hurt(this, this.attackStrength, DamageType.COMBAT);
		}

	}
	public void setLimbDrag(float limbDrag){
		this.setLimbDrag = limbDrag;
	}
	public float getLimbDrag(){
		return this.setLimbDrag;
	}

	public EntityPlayer getClosestPlayerToEntityLooking(){
		EntityPlayer closestPlayerLooking = this.world.getClosestPlayerToEntity(this, 24);
		if (closestPlayerLooking != null) {
			ItemStack pumpkinHead = closestPlayerLooking.inventory.armorItemInSlot(3);

			if (pumpkinHead != null && pumpkinHead.getItem() == Block.pumpkinCarvedIdle.asItem()) {
				return null;
			} else if (isSeenByPlayer(closestPlayerLooking)) {
				return closestPlayerLooking;
			}
		}
		return null;
	}

	public boolean isGeistTooClose() {
		if (this.entityToAttack != null) {
			return this.entityToAttack.distanceTo(this) < 5;
		} else
			return false;
	}

	public boolean isSeenByPlayer(EntityPlayer player){
		if (player != null) {

			Vec3d playerLook = player.getLookAngle().normalize();
			Vec3d toEntity = Vec3d.createVector(this.x - player.x,(this.y + (double) this.getHeadHeight()) - (player.y + (double) player.getHeadHeight()),this.z - player.z);
			double distance = toEntity.lengthVector();
			toEntity = toEntity.normalize();
			double dotProduct = (playerLook.xCoord * toEntity.xCoord) + (playerLook.yCoord * toEntity.yCoord) + (playerLook.zCoord * toEntity.zCoord);


			Vec3d playerPos = Vec3d.createVector(player.x, player.y + (double) player.getHeadHeight(), player.z);
			Vec3d thisPos = Vec3d.createVector(this.x, this.y + (double) this.getHeadHeight(), this.z);
			HitResult hitResult = this.world.checkBlockCollisionBetweenPoints(playerPos, thisPos, false, true);

			if (hitResult == null) {
				return dotProduct > 0.85D - (0.005D * distance);
			} else
				return false;
		}
        return false;
    }

	public String getLivingSound() {
		if (isGeistShy()) {
			return "doopmod.geist.shy";
		} else if (isGeistAngry()) {
			return "doopmod.geist.angry";
		}
		return "doopmod.geist.idle";
	}
	protected String getDeathSound() {
		return "doopmod.geist.death";
	}

	protected String getHurtSound() {
		return "doopmod.geist.hurt";
	}


	public boolean isGeistShy() {
		return (this.entityData.getByte(16) & 1) != 0;
	}

	public void setGeistShy(boolean flag) {
		byte byte0 = this.entityData.getByte(16);
		if (flag) {
			this.entityData.set(16, (byte)(byte0 | 1));
		} else {
			this.entityData.set(16, (byte)(byte0 & -2));
		}
	}

	public boolean isGeistAngry() {
		return (this.entityData.getByte(16) & 2) != 0;
	}

	public void setGeistAngry(boolean flag) {
		byte byte0 = this.entityData.getByte(16);
		if (flag) {
			this.entityData.set(16, (byte)(byte0 | 2));
		} else {
			this.entityData.set(16, (byte)(byte0 & -3));
		}
	}



}
