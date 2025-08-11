package brokendoop.doopmod.interfaces;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;

public interface IWorldAdditions {
	Mob doopmod$getClosestMobToEntity(Entity entity, double radius);
}
