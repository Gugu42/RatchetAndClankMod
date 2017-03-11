package com.gugu42.rcmod;

import java.util.HashMap;
import java.util.Map;

import com.gugu42.rcmod.items.RcItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {
	/**
	 * Used to store IExtendedEntityProperties data temporarily between player
	 * death and respawn
	 */
	private static final Map<String, NBTTagCompound> extendedEntityData = new HashMap<String, NBTTagCompound>();

	public void preInit()
	{

	}
	
	public void registerRenderers() {
	}

	@Override
	public Object getServerGuiElement(int guiId, EntityPlayer player,
			World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int guiId, EntityPlayer player,
			World world, int x, int y, int z) {
		return null;
	}

	public static void storeEntityData(String name, NBTTagCompound compound) {
		extendedEntityData.put(name, compound);
	}

	public static NBTTagCompound getEntityData(String name) {
		return extendedEntityData.remove(name);
	}
	
	
	
	public void registerRender() {
	}

	public void registerRenderInformation() {
	}
	
	public void registerTileEntityRender()
	{
		
	}

}
