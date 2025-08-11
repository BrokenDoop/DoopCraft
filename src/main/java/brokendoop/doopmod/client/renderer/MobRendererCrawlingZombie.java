package brokendoop.doopmod.client.renderer;

import brokendoop.doopmod.core.entity.MobZombieCrawling;
import brokendoop.doopmod.client.model.ModelZombieCrawling;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.MobRenderer;

@Environment(EnvType.CLIENT)
public class MobRendererCrawlingZombie extends MobRenderer<MobZombieCrawling> {
	public MobRendererCrawlingZombie() {
		super(new ModelZombieCrawling(), 0.6F);
	}
}
