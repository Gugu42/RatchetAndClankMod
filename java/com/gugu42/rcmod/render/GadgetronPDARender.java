package com.gugu42.rcmod.render;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.jglrxavpok.glutils.obj.mc.TessellatorModel;
import org.lwjgl.opengl.GL11;

public class GadgetronPDARender implements IItemRenderer {
	private TessellatorModel model1;

	public GadgetronPDARender() {
		model1 = new TessellatorModel("/assets/rcmod/models/GPDA.obj");
		model1.regenerateNormals();
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch (type) {
		case EQUIPPED:
			return true;
		case EQUIPPED_FIRST_PERSON:
			return true;
		case ENTITY:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		switch (type) {
		case EQUIPPED: {
			GL11.glPushMatrix();
			GL11.glTranslatef(0.81f, 0.29f, 0.00f);
			GL11.glRotatef(-90, 0.0f, 1.0f, 0.0f);
			GL11.glRotatef(0, 0.0f, 0.0f, 1.0f);
			GL11.glRotatef(190, 1.0f, 0.0f, 0.0f);
			GL11.glScalef(-0.031f, -0.031f, -0.031f);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			model1.render();
			GL11.glTranslatef(-0.5F, 0.0F, 0.09F);
			GL11.glPopMatrix();
			break;
		}
		case EQUIPPED_FIRST_PERSON: {
			GL11.glPushMatrix();
			GL11.glTranslatef(0.3f, 0.3f, -0.4f);
			GL11.glRotatef(90, 0.0f, 1.0f, 0.0f);
			GL11.glRotatef(10, 1.0f, 0.0f, 0.0f);
			GL11.glRotatef(180, 0.0f, 1.0f, 0.0f);
			GL11.glRotatef(-30, 0.0f, 0.0f, 1.0f);
			GL11.glScalef(0.046f, 0.046f, 0.046f);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			model1.render();
			GL11.glPopMatrix();
			break;
		}
		case ENTITY: {
			GL11.glPushMatrix();
			GL11.glScalef(0.05f, 0.05f, 0.05f);
			GL11.glTranslatef(-8.5f, 5.5f, 0.0f);
			GL11.glRotatef(90, 1.0f, 0.0f, 0.0f);
			GL11.glRotatef(180, 0.0f, 1.0f, 0.0f);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			model1.render();
			GL11.glPopMatrix();
			break;
		}
		default:
			break;
		}

	}
}
