package brokendoop.doopmod;

import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import static brokendoop.doopmod.DoopMod.MOD_ID;

public class DoopModConfig {
	private static final Toml properties = new Toml("DoopCraft Toml Config");
	public static TomlConfigHandler cfg;

	static {
		properties.addCategory("IDs")
				.addEntry("startingItemID", 21700);


		properties.addCategory("Gui")
			.addEntry("attackGuiStyle", "both, bar, arrow, none", "both");

		cfg = new TomlConfigHandler(MOD_ID, properties);
	}
}
