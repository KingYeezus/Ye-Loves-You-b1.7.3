package net.minecraft.src.MEDMEX.Modules.Render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.src.GuiIngame;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;

public class ArmorStatus extends Module{
	public static int hotbarwidth;
	public static ArmorStatus instance;
	public ArmorStatus() {
		super("ArmorStatus", Keyboard.KEY_NONE, Category.RENDER);
		instance = this;
	}
	public void onRenderGUI() {
		if(this.isEnabled()) {
			ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
			 GL11.glPushMatrix();
	         GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
	         RenderHelper.enableStandardItemLighting();
	         GL11.glPopMatrix();
	         GL11.glDisable(2896 /*GL_LIGHTING*/);
	         GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
	         GL11.glEnable(2903 /*GL_COLOR_MATERIAL*/);
	         GL11.glEnable(2896 /*GL_LIGHTING*/);
	         ItemStack boots = mc.thePlayer.inventory.armorInventory[0];
	         ItemStack legs = mc.thePlayer.inventory.armorInventory[1];
	         ItemStack chest = mc.thePlayer.inventory.armorInventory[2];
	         ItemStack helmet = mc.thePlayer.inventory.armorInventory[3];
	         ItemStack held = mc.thePlayer.inventory.getCurrentItem();
			 GuiIngame.itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, boots, hotbarwidth - 20, sr.getScaledHeight() - 16);
			 GuiIngame.itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, boots, hotbarwidth - 20, sr.getScaledHeight() - 16);
			 GuiIngame.itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, legs, hotbarwidth - 20, sr.getScaledHeight() - 30);
			 GuiIngame.itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, legs, hotbarwidth - 20, sr.getScaledHeight() - 30);
			 GuiIngame.itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, chest, hotbarwidth - 20, sr.getScaledHeight() - 44);
			 GuiIngame.itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, chest, hotbarwidth - 20, sr.getScaledHeight() - 44);
			 GuiIngame.itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, helmet, hotbarwidth - 20, sr.getScaledHeight() - 58);
			 GuiIngame.itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, helmet, hotbarwidth - 20, sr.getScaledHeight() - 58);
			 GuiIngame.itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, held, hotbarwidth - 20, sr.getScaledHeight() - 72);
			 GuiIngame.itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, held, hotbarwidth - 20, sr.getScaledHeight() - 72);
	         GL11.glDisable(2896 /*GL_LIGHTING*/);
	         GL11.glDepthMask(true);
	         GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
		}
	}
	
	}
