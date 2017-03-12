package com.gugu42.rcmod.network.packets;

import com.gugu42.rcmod.capabilities.bolt.BoltProvider;
import com.gugu42.rcmod.capabilities.bolt.IBolt;
import com.gugu42.rcmod.items.EnumRcWeapons;
import com.gugu42.rcmod.utils.ffmtutils.AbstractPacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class PacketVend extends AbstractPacket {

	private int id;

	//	private int page;

	public PacketVend() {

	}

	public PacketVend(int id) {
		this.id = id;
		//		this.page = page;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(id);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.id = buffer.readInt();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		//ExtendedPlayerBolt props = ExtendedPlayerBolt.get(player);
		IBolt props = player.getCapability(BoltProvider.BOLT_CAP, null);
		props.setPlayer((EntityPlayerMP)player);

		if (!player.inventory.hasItemStack(new ItemStack(EnumRcWeapons.getItemFromID(this.id)
				.getWeapon()))) {
			if (player.inventory.getFirstEmptyStack() >= 0 &&
					props.getCurrentBolt() >= EnumRcWeapons.getItemFromID(this.id).getPrice() &&
					player.inventory.addItemStackToInventory(new ItemStack(EnumRcWeapons.getItemFromID(this.id).getWeapon(), 1,
					(EnumRcWeapons.getItemFromID(this.id).getWeapon()
							.getMaxDamage() / 2))) && props.consumeBolts(EnumRcWeapons.getItemFromID(this.id)
					.getPrice())) {
				//TODO - Fix chat and sound
				//player.addChatMessage(new ChatComponentText(I18n.format("gui.vendor.buy.success")));
				//player.world.playSoundAtEntity(player, "rcmod:vendor.buy", 1.0f, 1.0f);
			} else {
				//player.addChatMessage(new ChatComponentText(I18n.format("gui.vendor.buy.error")));
				//player.world.playSoundAtEntity(player, "rcmod:vendor.maxAmmo", 1.0f, 1.0f);
			}
		} else {
			//player.addChatMessage(new ChatComponentText(I18n.format("gui.vendor.buy.alreadyhave")));
		}

	}

}
