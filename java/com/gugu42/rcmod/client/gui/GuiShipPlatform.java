package com.gugu42.rcmod.client.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.gugu42.rcmod.RcMod;
import com.gugu42.rcmod.network.packets.PacketEditWaypoint;
import com.gugu42.rcmod.network.packets.PacketShipPlatform;
import com.gugu42.rcmod.shipsys.ShipSystem;
import com.gugu42.rcmod.tileentity.TileEntityShipPlatform;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiShipPlatform extends GuiScreen {

	private ResourceLocation       texture        = new ResourceLocation("rcmod", "textures/gui/gadgetron_helper.png");
	private TileEntityShipPlatform tileEntity;
	private EntityPlayer           player;
	public final int               xSizeOfTexture = 192;
	public final int               ySizeOfTexture = 192;

	private GuiTextField           textField;
	private GuiButton              privateBtn;
	private GuiButton              createBtn;

	private String                 ownerName;
	private int                    posX, posY, posZ;
	private String                 saveData       = "";

	private String                 warningMessage;
	private boolean                isEditing;                                                                          // Used when the waypoint was saved once.

	public Minecraft               mc;

	public GuiShipPlatform(EntityPlayer player, TileEntityShipPlatform te) {
		this.player = player;
		this.tileEntity = te;
		this.mc = Minecraft.getMinecraft();
		this.warningMessage = "NAME MUST NOT CONTAIN SPACES OR BE EMPTY";
	}

	@Override
	public void drawScreen(int x, int y, float f) {
		drawDefaultBackground();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(texture);

		int posX = (this.width - xSizeOfTexture) / 2;
		int posY = (this.height - ySizeOfTexture) / 2;

		drawTexturedModalRect(posX, posY, 0, 0, xSizeOfTexture, ySizeOfTexture);
		drawString(mc.fontRendererObj, "Waypoint info :", posX + 10, posY + 10, 0xFF5F1F);
		drawString(mc.fontRendererObj, "Name :", posX + 10, posY + 55, 0xFFFFFF);
		drawString(mc.fontRendererObj, "Position : " + this.posX + " " + this.posY + " " + this.posZ, posX + 10, posY + 80, 0xFFFFFF);
		drawString(mc.fontRendererObj, "Owner : " + this.ownerName, posX + 10, posY + 100, 0xFFFFFF);
		drawString(mc.fontRendererObj, "Private : ", posX + 10, posY + 125, 0xFFFFFF);
		drawString(mc.fontRendererObj, warningMessage, posX + 15, posY + 28, 0xFF0000);
		this.textField.drawTextBox();

		super.drawScreen(x, y, f);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		super.keyTyped(par1, par2);

		if (par2 == 1) {
			this.mc.player.closeScreen();
		}

		this.textField.textboxKeyTyped(par1, par2);
	}

	protected void mouseClicked(int x, int y, int btn) throws IOException {
		super.mouseClicked(x, y, btn);
		this.textField.mouseClicked(x, y, btn);
	}

	public void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0:
			this.saveData();
			if (!isEditing) {
				try {
					PacketShipPlatform packet = new PacketShipPlatform(saveData);
					RcMod.rcModPacketHandler.sendToServer(packet);
					RcMod.rcModPacketHandler.sendToAll(packet);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			} else {
				try {
					PacketEditWaypoint packet = new PacketEditWaypoint(this.tileEntity.wpName + " " + this.textField.getText() + " " + tileEntity.getPos().getX() + " " + tileEntity.getPos().getY() + " " + tileEntity.getPos().getZ());
					RcMod.rcModPacketHandler.sendToServer(packet);
					RcMod.rcModPacketHandler.sendToAll(packet);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
			break;
		case 1:
			if (this.privateBtn.displayString.equals("False")) {
				this.privateBtn.displayString = "True";
			} else {
				this.privateBtn.displayString = "False";
			}
			break;
		default:
			break;
		}
	}

	public void initGui() {
		this.buttonList.clear();

		int posX = (this.width - xSizeOfTexture) / 2;
		int posY = (this.height - ySizeOfTexture) / 2;

		this.createBtn = new GuiButton(0, posX + 50, posY + 150, 100, 20, "Create waypoint");
		this.privateBtn = new GuiButton(1, posX + 60, posY + 120, 90, 20, "False");

		this.textField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 50, this.height / 2 - 46, 137, 20);
		this.textField.setMaxStringLength(23);
		this.textField.setFocused(true);

		this.buttonList.add(createBtn);
		this.buttonList.add(privateBtn);

		loadData();
	}

	private void loadData() {
		if (this.tileEntity.wpName != null) {
			textField.setText(this.tileEntity.wpName);
		} else {
			textField.setText("Waypoint name");
		}
		
		if(ShipSystem.isNameTaken(this.tileEntity.wpName)){
			this.isEditing = true;
		} else {
			this.isEditing = false;
		}

		if (this.tileEntity.ownerName != null) {
			this.ownerName = this.tileEntity.ownerName;
		} else {
			this.ownerName = this.player.getDisplayName().getFormattedText();
		}

		this.posX = tileEntity.getPos().getX();
		this.posY = tileEntity.getPos().getY() + 1;
		this.posZ = tileEntity.getPos().getZ();

		if (this.tileEntity.isPrivate) {
			this.privateBtn.displayString = "True";
		} else {
			this.privateBtn.displayString = "False";
		}
	}

	private void saveData() {
		if (this.textField.getText() != null) {
			saveData += this.textField.getText() + ";";
		}

		if (this.ownerName != null) {
			saveData += this.ownerName + ";";
		}

		saveData += this.privateBtn.displayString + ";";
		saveData += this.tileEntity.getPos().getX() + ";" + this.tileEntity.getPos().getY() + ";" + this.tileEntity.getPos().getZ();
	}

	public void updateScreen() {
		super.updateScreen();
		this.textField.updateCursorCounter();

		if(this.player.capabilities.isCreativeMode || this.tileEntity.ownerName.equals(this.player.getDisplayName())){
			this.createBtn.enabled = true;
		} else {
			this.createBtn.enabled = false;
		}
		
		if (this.textField.getText() == "" || this.textField.getText().contains(" ")) {
			GuiButton btn = (GuiButton) this.buttonList.get(0);
			btn.enabled = false;
			this.warningMessage = "NAME MUST NOT CONTAIN SPACES";
		} else {
			GuiButton btn = (GuiButton) this.buttonList.get(0);
			btn.enabled = true;
			this.warningMessage = "";
		}

		if (isEditing)
			createBtn.displayString = "Edit waypoint";
		else
			createBtn.displayString = "Create waypoint";
	}
}
