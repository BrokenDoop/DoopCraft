package brokendoop.doopmod.core.entity;

import net.minecraft.core.entity.Entity;

import net.minecraft.core.entity.monster.MobZombie;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

public class MobZombieCrawling extends MobZombie {
	public MobZombieCrawling(World world) {
		super(world);
		this.textureIdentifier = NamespaceID.getPermanent("minecraft", "zombie");
		this.setSize(0.95F, 0.5F);
		this.moveSpeed = 0.9F;
		this.scoreValue = 500;
	}
	public boolean canClimb(){
		return false;
	}

	protected void attackEntity(@NotNull Entity entity, float distance) {
		if (distance > 2.0F && distance < 6.0F && this.random.nextInt(10) == 0) {
			if (this.onGround) {
				double d = entity.x - this.x;
				double d1 = entity.z - this.z;
				float f2 = MathHelper.sqrt(d * d + d1 * d1);
				this.xd = d / (double)f2 * 0.5 * 0.800000011920929 + this.xd * 0.20000000298023224;
				this.zd = d1 / (double)f2 * 0.5 * 0.800000011920929 + this.zd * 0.20000000298023224;
				this.yd = 0.4000000059604645;
			}
		} else {
			super.attackEntity(entity, distance);
		}
	}
}
