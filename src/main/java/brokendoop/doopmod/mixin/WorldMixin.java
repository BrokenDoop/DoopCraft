package brokendoop.doopmod.mixin;

import brokendoop.doopmod.interfaces.IWorldAdditions;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.animal.MobFireflyCluster;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(value = World.class, remap = false)
public class WorldMixin implements IWorldAdditions {


	@Shadow
	public List<Entity> loadedEntityList;

	@Unique
	public Mob doopmod$getClosestMobToEntity(Entity entity, double radius) {
		return this.getClosestMob(entity ,entity.x, entity.y, entity.z, radius);
	}

	@Unique
	public Mob getClosestMob(Entity self,double x, double y, double z, double radius) {
		double closestDistance = Double.POSITIVE_INFINITY;
		Mob entityMob = null;
		double rSquared = (radius < 0.0F) ? Double.POSITIVE_INFINITY : radius * radius;

		for(Entity entityMob1 : this.loadedEntityList) {
			if (entityMob1 instanceof Mob) {
				if (entityMob1.removed || entityMob1 == self || entityMob1 instanceof Player || entityMob1 instanceof MobFireflyCluster) continue;

				double currentDistance = entityMob1.distanceToSqr(x, y, z);
				if (currentDistance < rSquared && currentDistance < closestDistance) {
					closestDistance = currentDistance;
					entityMob = (Mob) entityMob1;
				}
			}
		}

		return entityMob;
	}



}
