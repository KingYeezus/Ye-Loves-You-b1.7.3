package net.minecraft.src.MEDMEX.Modules.Render;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.RenderManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.TileEntityFurnace;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Utils.RenderUtils;
import net.minecraft.src.de.Hero.settings.Setting;

public class ESP extends Module{
	public static boolean players;
	public static boolean items;
	public static boolean mobs;
	public static boolean animals;
	public static ESP instance;
	public ESP() {
		super("ESP", Keyboard.KEY_NONE, Category.RENDER);
		players = true;
		items = true;
		mobs = false;
		animals = false;
		instance = this;
	}
	
	public void setup() {
		Client.settingsmanager.rSetting(new Setting("Player ESP", this, true));
		Client.settingsmanager.rSetting(new Setting("Item ESP", this, true));
		Client.settingsmanager.rSetting(new Setting("Mob ESP", this, false));
		Client.settingsmanager.rSetting(new Setting("Animal ESP", this, false));
	}
	
	public void onRender() {
		if(this.isEnabled()) {
			for(Entity e : mc.theWorld.loadedEntityList) {
				double cX = e.posX;
				double cY = e.posY;
				double cZ = e.posZ;
				double renderX = cX - RenderManager.renderPosX;
		    	double renderY = cY - RenderManager.renderPosY;
		    	double renderZ = cZ - RenderManager.renderPosZ;
		    	if(e instanceof EntityPlayer && e != mc.thePlayer && Client.settingsmanager.getSettingByName("Player ESP").getValBoolean()) {
		    		EntityPlayer p = (EntityPlayer) e;
		    		if(Client.friends.contains(p.username)) {
		    		RenderUtils.drawOutlinedEntityESP(renderX, renderY, renderZ, e.width, e.height * 1.1, 0.1f, 0.8f, 0.1f, 1);
		    		}else {
		    		RenderUtils.drawOutlinedEntityESP(renderX, renderY, renderZ, e.width, e.height * 1.1, 0.709f, 0.576f, 0.858f, 1);
		    		}
		    	}
		    	if(e instanceof EntityItem && Client.settingsmanager.getSettingByName("Item ESP").getValBoolean())
		    		RenderUtils.drawOutlinedEntityESP(renderX, renderY, renderZ, e.width, e.height*1.8, 0.709f, 0.576f, 0.858f, 1);
		    	if(e instanceof EntityMob && Client.settingsmanager.getSettingByName("Mob ESP").getValBoolean())
		    		RenderUtils.drawOutlinedEntityESP(renderX, renderY, renderZ, e.width, e.height,0.980f, 0f, 0.274f, 1);
		    	if(e instanceof EntityAnimal && Client.settingsmanager.getSettingByName("Animal ESP").getValBoolean())
		    		RenderUtils.drawOutlinedEntityESP(renderX, renderY, renderZ, e.width, e.height * 1.2,0f, 0.980f, 0.176f, 1);
			}
		}
	}
}
