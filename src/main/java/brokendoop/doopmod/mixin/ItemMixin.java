package brokendoop.doopmod.mixin;

import brokendoop.doopmod.interfaces.IMaxCooldown;
import net.minecraft.core.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = Item.class, remap = false)
public class ItemMixin implements IMaxCooldown {

	@Unique
	private int maxCooldown = 5;

	@Override
	public void doopmod$setMaxAttackCooldown(int cooldown){
		this.maxCooldown = cooldown;
	}
	public int doopmod$getMaxAttackCooldown() {
		return this.maxCooldown;
	}

}
