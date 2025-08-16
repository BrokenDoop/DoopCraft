package brokendoop.doopmod.core;

import brokendoop.doopmod.DoopModConfig;
import brokendoop.doopmod.core.item.ItemCreativeBlaster;
import brokendoop.doopmod.core.item.ItemCreativeFlamethrower;
import net.minecraft.core.item.Item;
import turniplabs.halplibe.helper.ItemBuilder;

import static brokendoop.doopmod.DoopMod.MOD_ID;

public class DoopModItems {
	private static int itemID = DoopModConfig.cfg.getInt("IDs.startingItemID");
	private static int nextID() {
		return ++itemID;
	}

	public static final Item CREATIVE_BLASTER = new ItemBuilder(MOD_ID)
		.build(new ItemCreativeBlaster("creative.blaster", MOD_ID + ":item/creative_blaster", nextID()));

	public static final Item CREATIVE_FLAMETHROWER = new ItemBuilder(MOD_ID)
		.build(new ItemCreativeFlamethrower("creative.flamethrower", MOD_ID + ":item/creative_flamethrower", nextID()));


	public static void initItems(){}



}
