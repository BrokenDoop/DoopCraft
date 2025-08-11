package brokendoop.doopmod.core;

import brokendoop.doopmod.DoopMod;
import brokendoop.doopmod.core.entity.MobExoskeleton;
import brokendoop.doopmod.core.entity.MobZombieCrawling;
import brokendoop.doopmod.core.entity.particle.ParticleLaserDust;
import brokendoop.doopmod.core.entity.particle.ParticleExhaust;
import brokendoop.doopmod.core.entity.projectile.ProjectileLaser;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.helper.ParticleHelper;

public class DoopModEntities {

	public static void initEntities() {
		//mobs
		EntityHelper.createEntity(MobExoskeleton.class, NamespaceID.getPermanent(DoopMod.MOD_ID, "exoskeleton"), "doopmod.entity.exoskeleton");
		EntityHelper.createEntity(MobZombieCrawling.class, NamespaceID.getPermanent(DoopMod.MOD_ID, "zombie_crawling"), "doopmod.entity.zombie_crawling");

		//projectiles
		EntityHelper.createEntity(ProjectileLaser.class, NamespaceID.getPermanent(DoopMod.MOD_ID, "laser"), "doopmod.entity.projectile.laser");

		//particles
		ParticleHelper.createParticle("laserdust", ParticleLaserDust::new);
		ParticleHelper.createParticle("exhaust", ParticleExhaust::new);
	}
}
