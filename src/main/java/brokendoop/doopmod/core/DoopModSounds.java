package brokendoop.doopmod.core;

import turniplabs.halplibe.helper.SoundHelper;

import static brokendoop.doopmod.DoopMod.MOD_ID;

public class DoopModSounds {
	public static void initializeSounds() {
		SoundHelper.addSound(MOD_ID, "geist/death.ogg");
		SoundHelper.addSound(MOD_ID, "geist/idle1.ogg");
		SoundHelper.addSound(MOD_ID, "geist/idle2.ogg");
		SoundHelper.addSound(MOD_ID, "geist/idle3.ogg");
		SoundHelper.addSound(MOD_ID, "geist/angry1.ogg");
		SoundHelper.addSound(MOD_ID, "geist/angry2.ogg");
		SoundHelper.addSound(MOD_ID, "geist/angry3.ogg");
		SoundHelper.addSound(MOD_ID, "geist/shy1.ogg");
		SoundHelper.addSound(MOD_ID, "geist/shy2.ogg");
		SoundHelper.addSound(MOD_ID, "geist/shy3.ogg");
		SoundHelper.addSound(MOD_ID, "geist/hurt1.ogg");
		SoundHelper.addSound(MOD_ID, "geist/hurt2.ogg");
	}

}
