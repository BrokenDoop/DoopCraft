package brokendoop.doopmod.core;

import brokendoop.doopmod.core.entity.MobZombieCrawling;
import net.minecraft.client.gui.guidebook.mobs.MobInfoRegistry;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;

public class DoopModMobInfo {

	public static void initMobInfo() {
		MobInfoRegistry.register(MobZombieCrawling.class, "doopmod.guidebook.section.mob.crawling_zombie.name", "doopmod.guidebook.section.mob.crawling_zombie.desc",20, 500, new MobInfoRegistry.MobDrop[]{new MobInfoRegistry.MobDrop(new ItemStack(Items.CLOTH), 0.66F, 1, 2)});
	}
}
