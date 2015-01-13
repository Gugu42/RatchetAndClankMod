package com.gugu42.rcmod.items;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.gugu42.rcmod.entity.projectiles.EntityPyrocitorAmmo;

public class ItemPyrocitor extends ItemRcGun
{

	public ItemPyrocitor()
	{
		super();
		this.setFull3D();
		this.ammoPrice = 1;
		this.weaponName = "pyrocitor";
		this.maxAmmo = 240;
		this.heldType = 1;
		this.setMaxDamage(maxAmmo);
		this.hideCrosshair = true;
		this.hasAmmoImage = true;
		this.hasEquipSound = true;
//		this.ammoTexturePath = "textures/gui/ammoImage_pyrocitor.png";
	}

	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
	{
		if (maxAmmo - par1ItemStack.getItemDamage() > 0)
		{
			if (!par2World.isRemote) 
			{
				int nbr = par3EntityPlayer.getRNG().nextInt(100)+100;
				float f = 4.5f;
				for(int i = 0;i<nbr;i++)
				{
					EntityPyrocitorAmmo flame = new EntityPyrocitorAmmo(par3EntityPlayer.worldObj,
							par3EntityPlayer,par3EntityPlayer.getRNG().nextFloat()*f*2-f);
					par3EntityPlayer.worldObj.spawnEntityInWorld(flame);
//					EntityPyrocitorAmmo flame2 = new EntityPyrocitorAmmo(par3EntityPlayer.worldObj,
//							par3EntityPlayer,par3EntityPlayer.getRNG().nextFloat()*f*2-f);
//					par3EntityPlayer.worldObj.spawnEntityInWorld(flame2);
				}
					par1ItemStack.damageItem(1, par3EntityPlayer);
			}
		}
		return par1ItemStack;
	}

	public boolean canHarvestBlock(Block par1Block)
	{
		return false;
	}

	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block) 
	{
		return 0.0f;
	}

	public void onPlayerStoppedUsing(ItemStack stack, World w, EntityPlayer player, int i) 
	{
	}
	
	@Override
	public void onUpdate(ItemStack stack, World w, Entity ent, int i,
			boolean flag) {
		super.onUpdate(stack, w, ent, i, flag);
	}
}
