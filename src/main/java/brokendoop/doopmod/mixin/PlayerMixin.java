package brokendoop.doopmod.mixin;


import brokendoop.doopmod.interfaces.IAttackCooldown;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerMixin implements IAttackCooldown {

	@Shadow
	public abstract ItemStack getCurrentEquippedItem();

	@Unique
	private int attackCooldown;

	@Override
	public void doopmod$setAttackCooldown(int cooldown){
		this.attackCooldown = cooldown;
	}
	public int doopmod$getAttackCooldown() {
		return this.attackCooldown;
	}


	@Inject(method = "onLivingUpdate", at = @At("HEAD"))
	private void onLivingUpdate(CallbackInfo ci) {
		if (this.attackCooldown > 0) {
			this.attackCooldown--;
		}
		if (this.attackCooldown < 0) {
			this.attackCooldown = 0;
		}
	}


}
