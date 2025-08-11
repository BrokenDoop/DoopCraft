package brokendoop.doopmod.core.gui;

import brokendoop.doopmod.DoopModConfig;
import brokendoop.doopmod.interfaces.IAttackCooldown;
import brokendoop.doopmod.interfaces.IMaxCooldown;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.gui.hud.component.HudComponent;
import net.minecraft.client.gui.hud.component.layout.Layout;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.phys.HitResult;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class HudComponentCooldownBar extends HudComponent {

	private IAttackCooldown cooldownPlayer;

	private int currentMaxCooldown;

	private int barLingerTime;

	public HudComponentCooldownBar(String key, Layout layout) {
		super(key, 16, 8, layout);
	}


	public boolean isVisible(Minecraft mc) {
		return mc.gameSettings.immersiveMode.drawCrosshair() && mc.gameSettings.thirdPersonView.value == 0;
	}

	public void renderAttackHud(Minecraft mc, HudIngame hud, int x, int y, int maxCooldown){
		boolean doBar = false;
		switch (DoopModConfig.cfg.getString("Gui.attackGuiStyle")){
			case "both":
				doBar = true;

				mc.textureManager.loadTexture("/assets/doopmod/textures/gui/sprites/attack_indicator.png").bind();
				if (this.cooldownPlayer.doopmod$getAttackCooldown() == 0 && (mc.objectMouseOver != null && mc.objectMouseOver.hitType == HitResult.HitType.ENTITY)) {
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					hud.drawTexturedModalRect(x + 4, y + 4, 0, 0, 8, 4, 1F / 8, 1F / 4);
				}
				break;
			case "bar":
				doBar = true;
				break;
			case "arrow":
				mc.textureManager.loadTexture("/assets/doopmod/textures/gui/sprites/attack_indicator.png").bind();
				int arrowHeight = 4;
				int heightAdjustment = (int) ((((IAttackCooldown) mc.thePlayer).doopmod$getAttackCooldown() / (float) maxCooldown * arrowHeight));
				heightAdjustment = Math.min(heightAdjustment, arrowHeight);
				if (this.cooldownPlayer.doopmod$getAttackCooldown() > 0 || (mc.objectMouseOver != null && mc.objectMouseOver.hitType == HitResult.HitType.ENTITY)) {
					hud.drawTexturedModalRect(x + 4, y - (arrowHeight - heightAdjustment) + arrowHeight, 0, arrowHeight + heightAdjustment, 8, arrowHeight - heightAdjustment, 1F / 8, 1F / arrowHeight);
					GL11.glColor4f(0.25F, 0.25F, 0.25F, 0.5F);// second bar here is for the background, it's made darker and fills the empty area in reverse
					hud.drawTexturedModalRect(x + 4, y, 0, arrowHeight, 8, arrowHeight - ( arrowHeight - heightAdjustment), 1F / 8, 1F / arrowHeight);
				}
				break;
			case "none":
		}

		if (doBar) { //handles bar rendering since there are two configs that do it the same way
			mc.textureManager.loadTexture("/assets/doopmod/textures/gui/sprites/cooldown_bar.png").bind();
			int barWidth = 14;
			int widthAdjustment = (int) ((((IAttackCooldown) mc.thePlayer).doopmod$getAttackCooldown() / (float) maxCooldown * barWidth));
			widthAdjustment = Math.min(widthAdjustment, barWidth);
			if (this.cooldownPlayer.doopmod$getAttackCooldown() > 0) {
				this.barLingerTime = 15;
				hud.drawTexturedModalRect(x, y - 2, 0, 0, barWidth - widthAdjustment, 8, 1F / 16, 1F / 16);
				GL11.glColor4f(0.25F, 0.25F, 0.25F, 0.5F);// second bar here is for the background, it's made darker and fills the empty area in reverse
				hud.drawTexturedModalRect(x + (barWidth - widthAdjustment), y - 2, barWidth - widthAdjustment, 0, widthAdjustment, 8, 1F / 16, 1F / 16);
				hud.drawTexturedModalRect(x + 14, y - 2, 14, 0, 2, 8, 1F / 16, 1F / 16);

			} else if (this.cooldownPlayer.doopmod$getAttackCooldown() == 0 && this.barLingerTime > 0) {
				--this.barLingerTime;
				barWidth = 16;
				mc.textureManager.loadTexture("/assets/doopmod/textures/gui/sprites/cooldown_bar.png").bind();
				hud.drawTexturedModalRect(x, y - 2, 0, 8, barWidth, 8, 1F / barWidth, 1F / 16);
			}
		}

	}

	public void render(Minecraft mc, HudIngame hud, int xSizeScreen, int ySizeScreen, float partialTick) {
		int x = this.getLayout().getComponentX(mc, this, xSizeScreen);
		int y = this.getLayout().getComponentY(mc, this, ySizeScreen);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(3042);
		GL11.glBlendFunc(775, 769);
		this.cooldownPlayer = (IAttackCooldown) mc.thePlayer;

		ItemStack heldStack = mc.thePlayer.getCurrentEquippedItem();
		Item heldItem = (heldStack != null) ? heldStack.getItem() : null;
		IMaxCooldown cooldownItem = (IMaxCooldown) heldItem;
		if (heldStack != null) {
			int lastMaxCooldown = cooldownItem.doopmod$getMaxAttackCooldown(); // used to keep the active cooldown properly represented on screen after switching weapons
			if (this.cooldownPlayer.doopmod$getAttackCooldown() == 0) {
				this.currentMaxCooldown = lastMaxCooldown;
			}
			renderAttackHud(mc, hud, x ,y, this.currentMaxCooldown);
		} else {
			if (this.cooldownPlayer.doopmod$getAttackCooldown() == 0) { // used for setting the hand cooldown to the default (5) since it is null and not an item
				this.currentMaxCooldown = 5;
			}
			renderAttackHud(mc, hud, x ,y, this.currentMaxCooldown);
		}
		GL11.glDisable(3042);
	}


	public void renderPreview(Minecraft mc, Gui gui, Layout layout, int xSizeScreen, int ySizeScreen) {
		int x = layout.getComponentX(mc, this, xSizeScreen);
		int y = layout.getComponentY(mc, this, ySizeScreen);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		switch (DoopModConfig.cfg.getString("Gui.attackGuiStyle")){
			case "both":
				gui.drawGuiIcon(x, y - 2, 16, 16, TextureRegistry.getTexture("doopmod:gui/cooldown_bar"));
				gui.drawGuiIcon(x + 4, y + 3, 8, 4, TextureRegistry.getTexture("doopmod:gui/attack_indicator"));
				break;
			case "bar":
				gui.drawGuiIcon(x, y - 2, 16, 16, TextureRegistry.getTexture("doopmod:gui/cooldown_bar"));
				break;
			case "arrow":
				gui.drawGuiIcon(x + 4, y, 8, 4, TextureRegistry.getTexture("doopmod:gui/attack_indicator"));
				break;
			case "none":
		}
	}
}
