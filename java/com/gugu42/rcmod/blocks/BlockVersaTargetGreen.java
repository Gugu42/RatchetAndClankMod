package com.gugu42.rcmod.blocks;

import com.gugu42.rcmod.RcMod;
import com.gugu42.rcmod.client.ClientProxy;
import com.gugu42.rcmod.tileentity.TileEntityVersaTargetG;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockVersaTargetGreen extends Block {

	public BlockVersaTargetGreen(Material p_i45394_1_) {
		super(p_i45394_1_);
		this.setCreativeTab(RcMod.rcGadgTab);
		this.setLightLevel(9f);
	}


	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityVersaTargetG();
	}

	public boolean hasTileEntity(int metadata) {
		return true;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return ClientProxy.renderInventoryTESRId;
	}
	
}
