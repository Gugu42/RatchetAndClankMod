package com.gugu42.rcmod.blocks;

import com.gugu42.rcmod.ClientProxy;
import com.gugu42.rcmod.RcMod;
import com.gugu42.rcmod.tileentity.TileEntityShip;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockShip extends Block {

	public BlockShip(Material p_i45394_1_) {
		super(p_i45394_1_);
		this.setCreativeTab(RcMod.rcTab);
		//this.setBlockBounds(-3f, 0, -3f, 3f, 3f, 3f);
	}

	public boolean onBlockActivated(World par1World, int x, int y, int z,
			EntityPlayer par5EntityPlayer, int par6, float par7, float par8,
			float par9) {

		TileEntity tileEntity = par1World.getTileEntity(new BlockPos(x, y, z));
		if (tileEntity == null || par5EntityPlayer.isSneaking()) {
			return false;
		}

		par5EntityPlayer.openGui(RcMod.instance, 0, par1World, x, y, z);

		return true;
	}

	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityShip();
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

	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase living, ItemStack stack) {
		int direction = MathHelper
				.floor((double) (living.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		//world.setBlockMetadataWithNotify(x, y, z, direction, 2);
	}

	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return ClientProxy.renderInventoryTESRId;
	}

}
