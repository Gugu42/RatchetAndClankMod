package com.gugu42.rcmod.gui;

import java.util.Random;

import com.gugu42.rcmod.ContainerVendor;
import com.gugu42.rcmod.RcMod;
import com.gugu42.rcmod.handler.ExtendedPlayerBolt;
import com.gugu42.rcmod.items.EnumRcWeapons;
import com.gugu42.rcmod.items.ItemRcWeap;
import com.gugu42.rcmod.network.packets.PacketRefill;
import com.gugu42.rcmod.network.packets.PacketVend;
import com.gugu42.rcmod.tileentity.TileEntityVendor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiVendor extends GuiContainer {

	private static final ResourceLocation texturepath = new ResourceLocation(
			"rcmod", "textures/gui/vendor.png");

	private GuiButton ammoBtn;
	private GuiButton buyBtn;
	private GuiButton cancelBtn;
	private GuiButton pageNextBtn;
	private GuiButton pagePrevBtn;

	private EnumRcWeapons weaps;

	private TileEntityVendor tileEntity;
	private EntityPlayer player;
	private Minecraft mc;
	private ContainerVendor container;

	private int weapPage;

	private boolean isOverSlot1 = false;
	private boolean hasSlot1BeenClicked = false;

	private boolean isOverSlot2 = false;
	private boolean hasSlot2BeenClicked = false;

	private boolean isOverSlot3 = false;
	private boolean hasSlot3BeenClicked = false;

	private boolean isOverSlot4 = false;
	private boolean hasSlot4BeenClicked = false;
	
	public int soundCooldown;

	public GuiVendor(InventoryPlayer inventoryPlayer,
			TileEntityVendor tileEntity, EntityPlayer player,
			ContainerVendor container) {
		super(new ContainerVendor(inventoryPlayer, tileEntity));
		this.tileEntity = tileEntity;
		this.player = player;
		this.xSize = 176;
		this.ySize = 222;
		this.mc = Minecraft.getMinecraft();
		this.container = container;
		this.weapPage = 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		int posX = (this.width - xSize) / 2;
		int posY = (this.height - ySize) / 2;
		this.buttonList.add(this.ammoBtn = new GuiButton(0, posX + 7,
				posY + 40, 33, 20, "Refill"));
		this.buttonList.add(this.buyBtn = new GuiButton(1, posX + 79,
				posY + 75, 33, 20, "Buy"));
		this.buttonList.add(this.cancelBtn = new GuiButton(2, posX + 112,
				posY + 75, 43, 20, "Cancel"));
		this.buttonList.add(this.pageNextBtn = new GuiButton(3, posX + 152,
				posY + 98, 16, 20, ">"));
		this.buttonList.add(this.pagePrevBtn = new GuiButton(4, posX + 62,
				posY + 98, 16, 20, "<"));
	}



	public void updateScreen() {

		if (weapPage > 0) {
			if (EnumRcWeapons.getItemForPageAndSlot(weapPage, 1) != null) {
				container.putStackInSlot(1, new ItemStack(EnumRcWeapons
						.getItemForPageAndSlot(weapPage, 1).getWeapon()));
			} else {
				container.putStackInSlot(1, null);
			}
			if (EnumRcWeapons.getItemForPageAndSlot(weapPage, 2) != null) {
				container.putStackInSlot(3, new ItemStack(EnumRcWeapons
						.getItemForPageAndSlot(weapPage, 2).getWeapon()));
			} else {
				container.putStackInSlot(3, null);
			}
			if (EnumRcWeapons.getItemForPageAndSlot(weapPage, 3) != null) {
				container.putStackInSlot(4, new ItemStack(EnumRcWeapons
						.getItemForPageAndSlot(weapPage, 3).getWeapon()));
			} else {
				container.putStackInSlot(4, null);
			}
			if (EnumRcWeapons.getItemForPageAndSlot(weapPage, 4) != null) {
				container.putStackInSlot(5, new ItemStack(EnumRcWeapons
						.getItemForPageAndSlot(weapPage, 4).getWeapon()));
			} else {
				container.putStackInSlot(5, null);
			}
		}

		if (hasNextPage(weapPage)) {
			this.pageNextBtn.enabled = true;
		} else {
			this.pageNextBtn.enabled = false;
		}

		if (hasPrevPage(weapPage)) {
			this.pagePrevBtn.enabled = true;
		} else {
			this.pagePrevBtn.enabled = false;
		}

		if (this.tileEntity.getStackInSlot(1) != null) {
			this.ammoBtn.enabled = this.tileEntity.getStackInSlot(1).getItem() instanceof ItemRcWeap;
			if (this.tileEntity.getStackInSlot(1).getItem() instanceof ItemRcWeap) {
				ItemRcWeap itemInSlot1 = (ItemRcWeap) this.tileEntity
						.getStackInSlot(1).getItem();
				int price = this.tileEntity.getStackInSlot(1).getItemDamage()
						* itemInSlot1.getPrice();
			}
		} else {
			this.ammoBtn.enabled = false;
		}

		if (hasSlot1BeenClicked
				&& EnumRcWeapons.getItemForPageAndSlot(weapPage, 1) != null) {
			this.buyBtn.enabled = true;
			this.cancelBtn.enabled = true;
			this.container.putStackInSlot(2, new ItemStack(EnumRcWeapons
					.getItemForPageAndSlot(weapPage, 1).getWeapon()));
			hasSlot2BeenClicked = false;
			hasSlot3BeenClicked = false;
			hasSlot4BeenClicked = false;
		} else if (hasSlot2BeenClicked
				&& EnumRcWeapons.getItemForPageAndSlot(weapPage, 2) != null) {
			this.buyBtn.enabled = true;
			this.cancelBtn.enabled = true;
			this.container.putStackInSlot(2, new ItemStack(EnumRcWeapons
					.getItemForPageAndSlot(weapPage, 2).getWeapon()));
			hasSlot1BeenClicked = false;
			hasSlot3BeenClicked = false;
			hasSlot4BeenClicked = false;
		} else if (hasSlot3BeenClicked
				&& EnumRcWeapons.getItemForPageAndSlot(weapPage, 3) != null) {
			this.buyBtn.enabled = true;
			this.cancelBtn.enabled = true;
			this.container.putStackInSlot(2, new ItemStack(EnumRcWeapons
					.getItemForPageAndSlot(weapPage, 3).getWeapon()));
			hasSlot1BeenClicked = false;
			hasSlot2BeenClicked = false;
			hasSlot4BeenClicked = false;
		} else if (hasSlot4BeenClicked
				&& EnumRcWeapons.getItemForPageAndSlot(weapPage, 4) != null) {
			this.buyBtn.enabled = true;
			this.cancelBtn.enabled = true;
			this.container.putStackInSlot(2, new ItemStack(EnumRcWeapons
					.getItemForPageAndSlot(weapPage, 4).getWeapon()));
			hasSlot1BeenClicked = false;
			hasSlot2BeenClicked = false;
			hasSlot3BeenClicked = false;
		} else {
			this.buyBtn.enabled = false;
			this.cancelBtn.enabled = false;
			this.container.putStackInSlot(2, null);
		}

		if (soundCooldown <= 30 * 20) {
			soundCooldown++;
		}

		if (soundCooldown >= 30 * 20) {
			soundCooldown = 0;
//			mc.sndManager.playSoundFX(
//					"rcmod:VendorSalesman.vendor_speech_wait", 1.0f, 1.0f);
			mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("rcmod:vendor.speech.wait"), 1.0F));
		}

	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	public boolean hasNextPage(int currentPage) {
		if (EnumRcWeapons.getItemForPageAndSlot(currentPage + 1, 1) != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean hasPrevPage(int currentPage) {
		if (currentPage > 1) {
			return true;
		} else {
			return false;
		}
	}

	public void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0:
			this.soundCooldown = 0;
			if (tileEntity.getStackInSlot(1) != null
					&& tileEntity.getStackInSlot(1).getItem() instanceof ItemRcWeap) {
				int damage = tileEntity.getStackInSlot(1).getItemDamage();
				ExtendedPlayerBolt props = ExtendedPlayerBolt.get(player);
				if (damage > 0) {
					if (props.getCurrentBolt() > damage) {
						try {

							PacketRefill packet = new PacketRefill(tileEntity.getStackInSlot(1));
							RcMod.rcModPacketHandler.sendToServer(packet);
//							mc.sndManager
//							.playSoundFX("rcmod:MenuVendorBuy", 1.0f, 1.0f);
							mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("rcmod:vendor.buy"), 1.0F));
						} catch (Exception exception) {
							exception.printStackTrace();
						}
					} else {
//						mc.sndManager
//						.playSoundFX("rcmod:MenuVendorAmmoMaxedOut", 1.0f, 1.0f);
						mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("rcmod:vendor.maxAmmo"), 1.0F));
					}
				} else {
//					mc.sndManager
//					.playSoundFX("rcmod:MenuVendorAmmoMaxedOut", 1.0f, 1.0f);
					mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("rcmod:vendor.maxAmmo"), 1.0F));
				}
			}
			break;
		case 1:
			this.soundCooldown = 0;
			ExtendedPlayerBolt props = ExtendedPlayerBolt.get(player);
			if (props.getCurrentBolt() > 0) {

				try {
					PacketVend packetVend = new PacketVend(getSlotClicked(), weapPage);
					RcMod.rcModPacketHandler.sendToServer(packetVend); 
					//Send a packet telling the server wich page and slot is the item in.
					
//					mc.sndManager
//							.playSoundFX("rcmod:MenuVendorBuy", 1.0f, 1.0f);
					mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("rcmod:vendor.buy"), 1.0F));
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
			break;
		case 2:
			this.soundCooldown = 0;
			this.hasSlot1BeenClicked = false;
			this.hasSlot2BeenClicked = false;
			this.hasSlot3BeenClicked = false;
			this.hasSlot4BeenClicked = false;
			break;
		case 3:
			this.soundCooldown = 0;
			this.weapPage += 1;
			break;
		case 4:
			this.soundCooldown = 0;
			this.weapPage -= 1;
			break;
		default:
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		mc.fontRenderer.drawString("Vendor", 8, 6, 4210752);

		if (isMouseOverSlot(container.getSlot(1), par1, par2)) {
			isOverSlot1 = true;
			isOverSlot2 = false;
			isOverSlot3 = false;
			isOverSlot4 = false;
		} else if (isMouseOverSlot(container.getSlot(3), par1, par2)) {
			isOverSlot1 = false;
			isOverSlot2 = true;
			isOverSlot3 = false;
			isOverSlot4 = false;
		} else if (isMouseOverSlot(container.getSlot(4), par1, par2)) {
			isOverSlot1 = false;
			isOverSlot2 = false;
			isOverSlot3 = true;
			isOverSlot4 = false;
		} else if (isMouseOverSlot(container.getSlot(5), par1, par2)) {
			isOverSlot1 = false;
			isOverSlot2 = false;
			isOverSlot3 = false;
			isOverSlot4 = true;
		} else {
			isOverSlot1 = false;
			isOverSlot2 = false;
			isOverSlot3 = false;
			isOverSlot4 = false;
		}

		if (this.tileEntity.getStackInSlot(1) != null
				&& this.tileEntity.getStackInSlot(1).getItem() instanceof ItemRcWeap) {

			ItemRcWeap itemInSlot1 = (ItemRcWeap) this.tileEntity
					.getStackInSlot(1).getItem();
			int price = this.tileEntity.getStackInSlot(1).getItemDamage()
					* itemInSlot1.getPrice();
			mc.fontRenderer.drawString("Buy "
					+ this.tileEntity.getStackInSlot(1).getItemDamage()
					+ " ammo", 7, 64, 0);
			mc.fontRenderer.drawString("for " + price, 7, 74, 0);
			mc.fontRenderer.drawString("bolts", 7, 84, 0);
		}

		if (hasSlot1BeenClicked
				&& EnumRcWeapons.getItemForPageAndSlot(weapPage, 1) != null) {
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 1).getDesc()[0],
							100, 10, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 1).getDesc()[1],
							100, 20, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 1).getDesc()[2],
							80, 30, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 1).getDesc()[3],
							80, 40, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 1).getDesc()[4],
							80, 50, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 1).getDesc()[5],
							80, 60, 0);
			mc.fontRenderer.drawString("Price : "
					+ EnumRcWeapons.getItemForPageAndSlot(weapPage, 1)
							.getPrice(), 160, 80, 0);
		} else if (hasSlot2BeenClicked
				&& EnumRcWeapons.getItemForPageAndSlot(weapPage, 2) != null) {
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 2).getDesc()[0],
							100, 10, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 2).getDesc()[1],
							100, 20, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 2).getDesc()[2],
							80, 30, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 2).getDesc()[3],
							80, 40, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 2).getDesc()[4],
							80, 50, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 2).getDesc()[5],
							80, 60, 0);
			mc.fontRenderer.drawString("Price : "
					+ EnumRcWeapons.getItemForPageAndSlot(weapPage, 2)
							.getPrice(), 160, 80, 0);
		} else if (hasSlot3BeenClicked
				&& EnumRcWeapons.getItemForPageAndSlot(weapPage, 3) != null) {
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 3).getDesc()[0],
							100, 10, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 3).getDesc()[1],
							100, 20, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 3).getDesc()[2],
							80, 30, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 3).getDesc()[3],
							80, 40, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 3).getDesc()[4],
							80, 50, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 3).getDesc()[5],
							80, 60, 0);
			mc.fontRenderer.drawString("Price : "
					+ EnumRcWeapons.getItemForPageAndSlot(weapPage, 3)
							.getPrice(), 160, 80, 0);
		} else if (hasSlot4BeenClicked
				&& EnumRcWeapons.getItemForPageAndSlot(weapPage, 4) != null) {
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 4).getDesc()[0],
							100, 10, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 4).getDesc()[1],
							100, 20, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 4).getDesc()[2],
							80, 30, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 4).getDesc()[3],
							80, 40, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 4).getDesc()[4],
							80, 50, 0);
			mc.fontRenderer
					.drawString(EnumRcWeapons
							.getItemForPageAndSlot(weapPage, 4).getDesc()[5],
							80, 60, 0);
			mc.fontRenderer.drawString("Price : "
					+ EnumRcWeapons.getItemForPageAndSlot(weapPage, 4)
							.getPrice(), 160, 80, 0);
		}

		mc.fontRenderer.drawString(
				StatCollector.translateToLocal("container.inventory"), 8,
				ySize - 96 + 2, 4210752);
	}

	@Override
	public void mouseClicked(int par1, int par2, int par3) {
		if (isOverSlot1) {
			hasSlot1BeenClicked = true;
			hasSlot2BeenClicked = false;
			hasSlot3BeenClicked = false;
			hasSlot4BeenClicked = false;
//			mc.sndManager
//			.playSoundFX("rcmod:MenuSelect", 1.0f, 1.0f);
			mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("rcmod:MenuSelect"), 1.0F));
			Random random = new Random();
			int randomInt = random.nextInt(9);
			if(randomInt == 5){
				mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("rcmod:vendor.speech.weapClicked"), 1.0F));
			}
		} else if (isOverSlot2) {
			hasSlot1BeenClicked = false;
			hasSlot2BeenClicked = true;
			hasSlot3BeenClicked = false;
			hasSlot4BeenClicked = false;
			mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("rcmod:MenuSelect"), 1.0F));
			Random random = new Random();
			int randomInt = random.nextInt(9);
			if(randomInt == 5){
				mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("rcmod:vendor.speech.weapClicked"), 1.0F));
			}
		} else if (isOverSlot3) {
			hasSlot1BeenClicked = false;
			hasSlot2BeenClicked = false;
			hasSlot3BeenClicked = true;
			hasSlot4BeenClicked = false;
			mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("rcmod:MenuSelect"), 1.0F));
			Random random = new Random();
			int randomInt = random.nextInt(9);
			if(randomInt == 5){
				mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("rcmod:vendor.speech.weapClicked"), 1.0F));
			}
		} else if (isOverSlot4) {
			hasSlot1BeenClicked = false;
			hasSlot2BeenClicked = false;
			hasSlot3BeenClicked = false;
			hasSlot4BeenClicked = true;
			mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("rcmod:MenuSelect"), 1.0F));
			Random random = new Random();
			int randomInt = random.nextInt(9);
			if(randomInt == 5){
				mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("rcmod:vendor.speech.weapClicked"), 1.0F));
			}
		} else {
			super.mouseClicked(par1, par2, par3);
		}
	}

	private boolean isMouseOverSlot(Slot par1Slot, int par2, int par3) {
		return this.func_146978_c(par1Slot.xDisplayPosition,
				par1Slot.yDisplayPosition, 16, 16, par2, par3);
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
			int par3) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.renderEngine.bindTexture(texturepath);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedQuadFit(x, y, 256, 256, 0);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public int getSlotClicked() {

		if (hasSlot1BeenClicked) {
			return 1;
		} else if (hasSlot2BeenClicked) {
			return 2;
		} else if (hasSlot3BeenClicked) {
			return 3;
		} else if (hasSlot4BeenClicked) {
			return 4;
		} else {
			return 0;
		}
	}
	
	public void onGuiClosed() {
		super.onGuiClosed();
		mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("rcmod:vendor.exit"), 1.0F));
	}

	@SideOnly(Side.CLIENT)
	public static void drawTexturedQuadFit(double x, double y, double width,
			double height, double zLevel) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x + 0, y + height, zLevel, 0, 1);
		tessellator.addVertexWithUV(x + width, y + height, zLevel, 1, 1);
		tessellator.addVertexWithUV(x + width, y + 0, zLevel, 1, 0);
		tessellator.addVertexWithUV(x + 0, y + 0, zLevel, 0, 0);
		tessellator.draw();
	}

}
