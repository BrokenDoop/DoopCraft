package brokendoop.doopmod.mixin;

import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolSword;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemToolSword.class, remap = false)
public class ItemToolSwordMixin extends ItemMixin {


	@Inject(method = "<init>", at = @At("TAIL"))
	private void ItemToolSword(String name, String namespaceId, int id, ToolMaterial enumtoolmaterial, CallbackInfo ci) {
		if (enumtoolmaterial == ToolMaterial.gold || enumtoolmaterial == ToolMaterial.diamond) {
			this.doopmod$setMaxAttackCooldown(17);
		} else {
			this.doopmod$setMaxAttackCooldown(25);
		}
	}

}
