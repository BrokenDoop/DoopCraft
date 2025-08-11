package brokendoop.doopmod.mixin;

import brokendoop.doopmod.interfaces.IAttackCooldown;
import brokendoop.doopmod.interfaces.IMaxCooldown;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.player.controller.PlayerController;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.phys.HitResult;
import org.lwjgl.Sys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class, remap = false)
public class MinecraftMixin {

	@Shadow
	public PlayerLocal thePlayer;

	@Shadow
	public HitResult objectMouseOver;

	@Shadow
	public PlayerController playerController;

	@Unique
	boolean isMining;

	@Unique
	boolean setCooldown;

	@Unique
	public HitResult.HitType lastHit;

	@Inject(method = "clickMouse", at = @At("HEAD"), cancellable = true)
	public void preventClickOnCooldown(int clickType, boolean attack, boolean repeat, CallbackInfo ci) {




		if (this.objectMouseOver == null || this.objectMouseOver.hitType == HitResult.HitType.TILE || this.objectMouseOver.hitType == HitResult.HitType.ENTITY) {
			//I need to add a proper interaction with blocks later, right now it just resets the cooldown while you mine (terrible)
			if (clickType == 0 && attack) { //checks if you are left-clicking and attacking
				if (this.thePlayer instanceof IAttackCooldown) {
					IAttackCooldown cooldownPlayer = (IAttackCooldown) this.thePlayer;
					ItemStack heldStack = this.thePlayer.inventory.getCurrentItem();
					Item heldItem = (heldStack != null) ? heldStack.getItem() : null; //null check because I don't fw another mixin for itemStack (lmao)
					IMaxCooldown cooldownItem = (IMaxCooldown) heldItem;

					if (cooldownPlayer.doopmod$getAttackCooldown() > 0) {
						ci.cancel();
					} else {
						if (heldStack != null) {
							cooldownPlayer.doopmod$setAttackCooldown(cooldownItem.doopmod$getMaxAttackCooldown());
						} else {
							cooldownPlayer.doopmod$setAttackCooldown(5);
						}
					}
				}
			}
		}



	}
}
