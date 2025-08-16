package brokendoop.doopmod.core;

import brokendoop.doopmod.DoopMod;
import brokendoop.doopmod.core.entity.MobCremator;
import brokendoop.doopmod.core.entity.MobExoskeleton;
import brokendoop.doopmod.core.entity.MobZombieCrawling;
import brokendoop.doopmod.core.entity.particle.ParticleFixedFlame;
import brokendoop.doopmod.core.entity.particle.ParticleLaserDust;
import brokendoop.doopmod.core.entity.particle.ParticleExhaust;
import brokendoop.doopmod.core.entity.projectile.ProjectileGasFlame;
import brokendoop.doopmod.core.entity.projectile.ProjectileLaser;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.helper.ParticleHelper;

public class DoopModEntities {

	public static void initEntities() {
		//mobs
		EntityHelper.createEntity(MobZombieCrawling.class, NamespaceID.getPermanent(DoopMod.MOD_ID, "zombie_crawling"), "doopmod.entity.zombie_crawling");
		EntityHelper.createEntity(MobExoskeleton.class, NamespaceID.getPermanent(DoopMod.MOD_ID, "exoskeleton"), "doopmod.entity.exoskeleton");
		EntityHelper.createEntity(MobCremator.class, NamespaceID.getPermanent(DoopMod.MOD_ID, "cremator"), "doopmod.entity.cremator");

		//projectiles
		EntityHelper.createEntity(ProjectileLaser.class, NamespaceID.getPermanent(DoopMod.MOD_ID, "laser"), "doopmod.entity.projectile.laser");
		EntityHelper.createEntity(ProjectileGasFlame.class, NamespaceID.getPermanent(DoopMod.MOD_ID, "gasflame"), "doopmod.entity.projectile.gasflame");

		//particles
		ParticleHelper.createParticle("laserdust", ParticleLaserDust::new);
		ParticleHelper.createParticle("exhaust", ParticleExhaust::new);
		ParticleHelper.createParticle("fixed_flame", ParticleFixedFlame::new);
	}
}
