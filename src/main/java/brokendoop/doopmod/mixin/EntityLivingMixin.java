package brokendoop.doopmod.mixin;

import brokendoop.doopmod.util.IEntityHurtFramesDelay;
import com.mojang.nbt.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityLiving.class, remap = false)
public class EntityLivingMixin extends Entity implements IEntityHurtFramesDelay {
	@Shadow
	public int heartsHalvesLife;
	@Shadow
	public int health;
	@Shadow
	public int prevHealth;
	@Shadow
	public int hurtTime;
	@Shadow
	public int maxHurtTime;
	@Shadow
	public float attackedAtYaw;
	@Shadow
	public float limbYaw;
	@Shadow
	protected int lastDamage;
	@Shadow
	protected int entityAge;
	@Shadow
	protected void damageEntity(int i, DamageType damageType) {}
	@Shadow
	public void knockBack(Entity entity, int i, double d, double d1) {}
	@Shadow
	public void onDeath(Entity entity) {}
	@Shadow
	protected String getHurtSound() {
		return null;
	}
	@Shadow
	protected String getDeathSound() {
		return null;
	}
	@Shadow
	protected float getSoundVolume() {
        return 0;
    }
	@Unique
	public int ticksHFTDelay;
	@Unique
	public Boolean doHeartsFlashTime = false;

	public EntityLivingMixin(World world) { super(world); }

	@Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/Entity;baseTick()V", ordinal = 0, shift = At.Shift.AFTER))
	private void ticksHFTDelay(CallbackInfo ci) {
		if (this.doHeartsFlashTime && (this.ticksHFTDelay > 0)) {
			--this.ticksHFTDelay;
		}
		if (this.doHeartsFlashTime && (this.ticksHFTDelay <= 0)) {
			this.heartsFlashTime = this.heartsHalvesLife;
			this.hurtTime = this.maxHurtTime = 10;
			this.doHeartsFlashTime = false;
		}
	}
	//currently if you hit something before the delay ends it will reset the delay. I might actually want it to work this way.
	@Override
	public boolean hurtWithDelay(Entity entity, int damage1, DamageType type1, int damage2, DamageType type2, Boolean doHurtHeartsFlashTime, int isHFTDelay) {
		if (this.world.isClientSide) {
			return false;
		} else {
			this.entityAge = 0;
			if (this.health <= 0) {
				return false;
			} else {
				this.limbYaw = 1.5F;
				boolean flag = true;
				int combinedDamage = damage1 + damage2;
				if ((float)this.heartsFlashTime > (float)this.heartsHalvesLife / 2.0F) {
					if (combinedDamage <= this.lastDamage) {
						return false;
					}
					this.damageEntity(damage1 - this.lastDamage, type1);
					this.damageEntity(damage2 - this.lastDamage, type2);
					this.lastDamage = combinedDamage;
					flag = false;
				} else {
					this.lastDamage = combinedDamage;
					this.prevHealth = this.health;
					this.damageEntity(damage1, type1);
					this.damageEntity(damage2, type2);
					if (doHurtHeartsFlashTime) {
						if (isHFTDelay > 0) {
							this.doHeartsFlashTime = true;
							this.ticksHFTDelay = isHFTDelay;
						} else {
							this.heartsFlashTime = this.heartsHalvesLife;
							this.hurtTime = this.maxHurtTime = 10;
						}
					}
				}

				this.attackedAtYaw = 0.0F;
				if (flag) {
					this.markHurtDelay();
					if (entity == null) {
						this.attackedAtYaw = (float)((int)(Math.random() * 2.0) * 180);
					} else {
						double d = entity.x - this.x;

						double d1;
						for(d1 = entity.z - this.z; d * d + d1 * d1 < 1.0E-4; d1 = (Math.random() - Math.random()) * 0.01) {
							d = (Math.random() - Math.random()) * 0.01;
						}

						this.attackedAtYaw = (float)(Math.atan2(d1, d) * 180.0 / (float) Math.PI) - this.yRot;
						this.knockBack(entity, combinedDamage, d, d1);
					}

					this.world.sendTrackedEntityStatusUpdatePacket(this, (byte)2, this.attackedAtYaw);
				}

				if (this.health <= 0) {
					if (flag) {
						this.world
							.playSoundAtEntity(this, this.getDeathSound(), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
					}

					this.onDeath(entity);
				} else if (flag) {
					this.world
						.playSoundAtEntity(this, this.getHurtSound(), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
				}

				return true;
			}
		}
	}
	@Shadow
	protected void init() {
	}
	@Shadow
	public void readAdditionalSaveData(CompoundTag compoundTag) {
	}
	@Shadow
	public void addAdditionalSaveData(CompoundTag compoundTag) {
	}
}
