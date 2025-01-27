package brokendoop.doopmod.core;

import brokendoop.doopmod.DoopModConfig;
import turniplabs.halplibe.helper.EntityHelper;

public class DoopModEntities {
	private static int startingID = DoopModConfig.cfg.getInt("IDs.startingEntityID");
	private static int nextID() {
		return ++startingID;
	}

	public static void initEntities() {
	}
}
