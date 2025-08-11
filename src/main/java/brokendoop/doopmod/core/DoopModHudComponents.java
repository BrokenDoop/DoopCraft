package brokendoop.doopmod.core;


import brokendoop.doopmod.core.gui.HudComponentCooldownBar;
import net.minecraft.client.gui.hud.component.ComponentAnchor;
import net.minecraft.client.gui.hud.component.HudComponent;
import net.minecraft.client.gui.hud.component.HudComponents;
import net.minecraft.client.gui.hud.component.layout.LayoutSnap;

public class DoopModHudComponents {

	private static final HudComponent cooldown_bar = HudComponents.register(new HudComponentCooldownBar("cooldown_bar", new LayoutSnap(HudComponents.CROSSHAIR, ComponentAnchor.BOTTOM_LEFT, ComponentAnchor.TOP_LEFT)));

	public static void init(){
	}
}
