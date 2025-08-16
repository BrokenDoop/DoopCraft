package brokendoop.doopmod.core.entity;

import brokendoop.doopmod.core.entity.projectile.ProjectileGasFlame;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

public class MobCremator extends MobExoskeleton{
	public MobCremator(World world) {
		super(world);
		this.textureIdentifier = NamespaceID.getPermanent("doopmod", "cremator");
	}





	protected void attackEntity(@NotNull Entity entity, float distance) {
		if (distance < 16.0F) {

			double dX = entity.x - this.x;
			double dZ = entity.z - this.z;

			double projectileSpeed = 0.2;

			double horizontalDistance = Math.sqrt(dX * dX + dZ * dZ);
			double travelTime = horizontalDistance / projectileSpeed;

			double futureX = entity.x + entity.xd * travelTime;
			double futureZ = entity.z + entity.zd * travelTime;

			double aimDX = futureX - this.x;
			double aimDZ = futureZ - this.z;




			if (this.attackTime == 0) {
				if (this.world != null && !this.world.isClientSide) {
					ProjectileGasFlame flameThrown = new ProjectileGasFlame(this.world, this);
					double d2 = entity.y + (double) entity.getHeadHeight() - 0.2 - flameThrown.y;
					flameThrown.setHeading(aimDX, d2, aimDZ, 0.45F, 8);
					this.world.entityJoinedWorld(flameThrown);
				}
			}
			this.yRot = (float)(Math.atan2(dZ, dX) * (double)180.0F / Math.PI) - 90.0F;
			if (distance < 8.0F) {
				this.hasAttacked = true;
			}
		}
	}
}
