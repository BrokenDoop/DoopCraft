package brokendoop.doopmod.core.item;

import brokendoop.doopmod.core.entity.projectile.ProjectileLaser;
import brokendoop.doopmod.core.entity.projectile.ProjectileLaserBlue;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.world.World;

public class ItemCreativeBlaster extends Item {
	public ItemCreativeBlaster(String name, String namespaceId, int id) {
		super(name, namespaceId, id);
		this.setMaxDamage(899);
		this.maxStackSize = 1;
	}

	public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
		ProjectileLaser laserToFire = null;
		if (entityplayer.inventory.consumeInventoryItem(Items.DUST_REDSTONE.id)) {
			laserToFire = new ProjectileLaser(world, entityplayer, true, 0);
		} else if (entityplayer.inventory.consumeInventoryItem(Items.DYE.id)) {
			laserToFire = new ProjectileLaserBlue(world, entityplayer, true);
		}


		if (laserToFire != null){
			itemstack.damageItem(1, entityplayer);
			world.playSoundAtEntity( entityplayer, entityplayer, "doopmod:projectile.laser.shot", 0.3F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if (!world.isClientSide) {
				world.entityJoinedWorld(laserToFire);
			}
		}
		return itemstack;
	}
}
