package brokendoop.doopmod;

import brokendoop.doopmod.core.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.sound.SoundRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import static net.minecraft.client.render.texture.stitcher.TextureRegistry.guiSpriteAtlas;
import static net.minecraft.client.render.texture.stitcher.TextureRegistry.particleAtlas;


public class DoopMod implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "doopmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static boolean modPadding = false;


    @Override
    public void onInitialize() {
        LOGGER.info("DoopMod initialized.");
    }

	@Override
	public void beforeGameStart() {
		DoopModEntities.initEntities();
		DoopModItems.initItems();
		SoundRepository.registerNamespace(MOD_ID);
		try {
			TextureRegistry.initializeAllFiles(MOD_ID, guiSpriteAtlas, true);
			TextureRegistry.initializeAllFiles(MOD_ID, particleAtlas, true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void afterGameStart() {
		DoopModHudComponents.init();
		DoopModMobInfo.initMobInfo();
	}


	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}
}
