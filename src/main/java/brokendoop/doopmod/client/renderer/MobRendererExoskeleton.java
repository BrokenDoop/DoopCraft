package brokendoop.doopmod.client.renderer;

import brokendoop.doopmod.client.model.ModelExoskeleton;
import brokendoop.doopmod.core.entity.MobExoskeleton;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.entity.MobRenderer;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class MobRendererExoskeleton extends MobRenderer<MobExoskeleton> {

	public MobRendererExoskeleton() {
		super(new ModelExoskeleton(0), 0.6f);
		this.setArmorModel(new ModelExoskeleton(0.01F));
	}

	protected boolean setSkeletronGlowBrightness(MobExoskeleton skeletron, int renderPass, float partialTick) {
		if (renderPass == 0) {
			this.bindTexture("/assets/doopmod/textures/entity/skeletron/glow/" + skeletron.getTextureReference() + ".png");
			if (LightmapHelper.isLightmapEnabled()) {
				LightmapHelper.setLightmapCoord(LightmapHelper.getLightmapCoord(15, 15));
			}

			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			return true;
		} else {
			return false;
		}
	}

	protected boolean prepareArmor(MobExoskeleton entity, int renderPass, float partialTick) {
		return this.setSkeletronGlowBrightness(entity, renderPass, partialTick);
	}

}
