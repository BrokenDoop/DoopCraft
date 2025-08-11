package brokendoop.doopmod.core;

import brokendoop.doopmod.client.renderer.MobRendererExoskeleton;
import brokendoop.doopmod.core.entity.MobExoskeleton;
import brokendoop.doopmod.core.entity.MobZombieCrawling;
import brokendoop.doopmod.core.entity.projectile.ProjectileLaser;
import brokendoop.doopmod.client.renderer.EntityRendererLaser;
import brokendoop.doopmod.client.renderer.MobRendererCrawlingZombie;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

import static brokendoop.doopmod.DoopMod.MOD_ID;

@Environment(EnvType.CLIENT)
public class DoopModModels implements ModelEntrypoint {


	@Override
	public void initBlockModels(BlockModelDispatcher blockModelDispatcher) {

	}

	@Override
	public void initItemModels(ItemModelDispatcher itemModelDispatcher) {
		ModelHelper.setItemModel(DoopModItems.CREATIVE_BLASTER, ()-> {
			ItemModelStandard model = new ItemModelStandard(DoopModItems.CREATIVE_BLASTER, MOD_ID);
			model.setIcon("doopmod:item/creative_blaster");
			model.setFull3D();
			return model;
		});

	}

	@Override
	public void initEntityModels(EntityRenderDispatcher entityRenderDispatcher) {
		//mobs
		ModelHelper.setEntityModel(MobZombieCrawling.class, MobRendererCrawlingZombie::new);
		ModelHelper.setEntityModel(MobExoskeleton.class, MobRendererExoskeleton::new);

		//projectiles
		ModelHelper.setEntityModel(ProjectileLaser.class, EntityRendererLaser::new);

	}

	@Override
	public void initTileEntityModels(TileEntityRenderDispatcher tileEntityRenderDispatcher) {

	}

	@Override
	public void initBlockColors(BlockColorDispatcher blockColorDispatcher) {

	}
}
