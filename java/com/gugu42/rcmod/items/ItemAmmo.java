package com.gugu42.rcmod.items;

import net.minecraft.item.Item;

public class ItemAmmo extends Item{
	
	public Item gun;
	
	public Item getGun()
	{
		return gun;
	}
	public void setGun(Item type)
	{
		gun = type;
	}
	public ItemAmmo(Item type) {
		super();
		setGun(type);
		this.setMaxDamage(10000);
		this.maxStackSize = 1;
	}
	
	
	
	
}
