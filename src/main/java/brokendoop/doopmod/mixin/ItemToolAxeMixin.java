package brokendoop.doopmod.mixin;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolAxe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemToolAxe.class, remap = false)
public class ItemToolAxeMixin extends ItemMixin {

	@Unique
	private int weaponDamage;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void ItemToolAxe(String name, String namespaceId, int id, ToolMaterial enumtoolmaterial, CallbackInfo ci) {
		if (enumtoolmaterial == ToolMaterial.gold || enumtoolmaterial == ToolMaterial.diamond) {
			this.doopmod$setMaxAttackCooldown(32);
		} else {
			this.doopmod$setMaxAttackCooldown(40);
		}
		this.weaponDamage = 7 + enumtoolmaterial.getDamage() * 2;
	}

	@Unique
	public int getDamageVsEntity(Entity entity, ItemStack is) {
		return this.weaponDamage;
	}
}
