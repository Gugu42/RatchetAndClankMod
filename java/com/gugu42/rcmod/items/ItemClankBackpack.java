package com.gugu42.rcmod.items;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gugu42.rcmod.RcMod;
import com.gugu42.rcmod.render.armor.ClankBackpackRender;

public class ItemClankBackpack extends ItemArmor {

	@SideOnly(Side.CLIENT)
	public ClankBackpackRender model;
	
	private String[] type = new String[]{"helipack", "helipack-deployed"};
//	private IIcon[] iconArray;
	
	public ItemClankBackpack(ArmorMaterial par2EnumArmorMaterial,
			int par3, int par4) {
		super(par2EnumArmorMaterial, par3, par4);
		this.setHasSubtypes(true);
		this.setCreativeTab(RcMod.rcGadgTab);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot,
			String type) {
		return "rcmod:models/ClankBackpack.png";
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel(){
		if(model == null){
			model = new ClankBackpackRender();
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving,
			ItemStack itemStack, int armorSlot) {
		initModel();
		return model;
	}

	public int getMetadata(int metadata)
	{
		return metadata;
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		int metadata = stack.getItemDamage();
		if(metadata > type.length || metadata < 0)
		{
			metadata = 0;
		}
		return super.getUnlocalizedName() + "." + type[metadata];
	}
//
//	public void registerIcons(IIconRegister iconregister)
//	{
//		iconArray = new IIcon[type.length];
//		for(int i = 0; i < type.length; i++)
//		{
//			iconArray[i] = iconregister.registerIcon("rcmod:" + type[i]);
//		}
//	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(int id, CreativeTabs creativeTabs, List list)
	{
		for(int metadata = 0; metadata < type.length; metadata++)
		{
			list.add(new ItemStack(this, 1, metadata));
		}
	}

//	@SideOnly(Side.CLIENT)
//	public IIcon getIconFromDamage(int metadata)
//	{
//		return metadata < type.length && metadata >= 0 ? iconArray[metadata] : iconArray[0];
//	}
	
}
