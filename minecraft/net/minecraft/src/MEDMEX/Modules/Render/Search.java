package net.minecraft.src.MEDMEX.Modules.Render;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderManager;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Utils.RenderUtils;

public class Search extends Module{
	public static List<Integer> blocks = new ArrayList<>();
	public static List<Integer> searchx= new ArrayList<>();
	public static List<Integer> searchy= new ArrayList<>();
	public static List<Integer> searchz= new ArrayList<>();
	public static Search instance;
	public Search() {
		super("Search", Keyboard.KEY_NONE, Category.RENDER);
		instance = this;
	}
	
	public void onDisable() {
		searchx.clear();
		searchy.clear();
		searchz.clear();
		mc.renderGlobal.loadRenderers();
	}
	public void onEnable() {
		mc.renderGlobal.loadRenderers();
	}
	
	public void onRender() {
		if(this.isEnabled()) {
			for(int i = 0; i < searchx.size(); i++) {
		    	double renderX = searchx.get(i) - RenderManager.renderPosX;
		    	Double RenderY = searchy.get(i) - RenderManager.renderPosY;
		    	double renderZ = searchz.get(i) - RenderManager.renderPosZ;
		    	if(blocks.contains(mc.theWorld.getBlockId(searchx.get(i), searchy.get(i), searchz.get(i)))) {
		    		RenderUtils.drawOutlinedBlockESP(renderX, RenderY, renderZ, 0.709f, 0.576f, 0.858f, 1, 1);
		    	}
		    	}
		}
	}
	
}