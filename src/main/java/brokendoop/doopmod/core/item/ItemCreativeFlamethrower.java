package brokendoop.doopmod.core.item;

import brokendoop.doopmod.core.entity.projectile.ProjectileGasFlame;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.world.World;

public class ItemCreativeFlamethrower extends Item
{
	public ItemCreativeFlamethrower(String name, String namespaceId, int id) {
		super(name, namespaceId, id);
		this.setMaxDamage(899);
		this.maxStackSize = 1;
	}

	public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
		ProjectileGasFlame gasFlameToFire = null;
		if (entityplayer.inventory.consumeInventoryItem(Items.COAL.id)) {

			gasFlameToFire = new ProjectileGasFlame(world, entityplayer);
		}


		if (gasFlameToFire != null){
			itemstack.damageItem(1, entityplayer);
			if (!world.isClientSide) {
				world.entityJoinedWorld(gasFlameToFire);
			}
		}
		return itemstack;
	}
}
