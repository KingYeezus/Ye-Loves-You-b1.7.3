package net.minecraft.src.MEDMEX.Modules.Render;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityBoat;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMinecart;
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
		Client.settingsmanager.rSetting(new Setting("Vehicle ESP", this, false));
		Client.settingsmanager.rSetting(new Setting("Misc ESP", this, false));
		ArrayList<String> options = new ArrayList<>();
		options.add("Box");
		options.add("Sphere");
		options.add("Outline");
		options.add("Filled");
		Client.settingsmanager.rSetting(new Setting("ESP Mode", this, "Outline", options));
		
	}
	
	public Color getColor(Entity e) {
		if(e instanceof EntityPlayer) {
			if(Client.friends.contains(((EntityPlayer)e).username)) {
				return new Color(0.1f, 0.8f, 0.1f, 1);
			}else {
				return new Color(0.709f, 0.576f, 0.858f, 1);
			}
		}
		if(e instanceof EntityItem) {
			return new Color(0.709f, 0.576f, 0.858f, 1);
		}
		if(e instanceof EntityMob) {
			return new Color(0.980f, 0f, 0.274f, 1);
		}
		if(e instanceof EntityAnimal) {
			return new Color(0f, 0.980f, 0.176f, 1);
		}
		if(e instanceof EntityBoat || e instanceof EntityMinecart) {
			return new Color(0.874f, 1f, 0.078f, 1);
		}
		return null;
	}
	
	public boolean shouldRenderEntity(Entity e) {
		if(e instanceof EntityPlayer && e != mc.thePlayer && Client.settingsmanager.getSettingByName("Player ESP").getValBoolean()) {
			return true;
		}
		if(e instanceof EntityItem && Client.settingsmanager.getSettingByName("Item ESP").getValBoolean()) {
			return true;
		}
		if(e instanceof EntityMob && Client.settingsmanager.getSettingByName("Mob ESP").getValBoolean()) {
			return true;
		}
		if(e instanceof EntityAnimal && Client.settingsmanager.getSettingByName("Animal ESP").getValBoolean()) {
			return true;
		}
		return false;
	}
	
	public void onRender() {
		if(this.isEnabled()) {
			this.attribute = " §7[§f"+Client.settingsmanager.getSettingByName("ESP Mode").getValString()+"§7]";
			if(Client.settingsmanager.getSettingByName("ESP Mode").getValString().equalsIgnoreCase("Box")) {
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
			if(Client.settingsmanager.getSettingByName("ESP Mode").getValString().equalsIgnoreCase("Sphere")) {
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
			    			RenderUtils.renderSphere(renderX, renderY, renderZ, 1.2f, 0.1f, 0.8f, 0.1f, 1);
			    		}else {
			    			RenderUtils.renderSphere(renderX, renderY, renderZ, 1.2f, 0.709f, 0.576f, 0.858f, 1);
			    		}
			    	}
			    	if(e instanceof EntityItem && Client.settingsmanager.getSettingByName("Item ESP").getValBoolean())
			    	{
			    		RenderUtils.renderSphere(renderX, renderY - 0.9, renderZ, 0.3f, 0.709f, 0.576f, 0.858f, 1);
			    	}
			    	if(e instanceof EntityMob && Client.settingsmanager.getSettingByName("Mob ESP").getValBoolean())
			    		RenderUtils.renderSphere(renderX, renderY, renderZ, 1.2f, 0.980f, 0f, 0.274f, 1);
			    	if(e instanceof EntityAnimal && Client.settingsmanager.getSettingByName("Animal ESP").getValBoolean())
			    		RenderUtils.renderSphere(renderX, renderY, renderZ, 1.2f, 0f, 0.980f, 0.176f, 1);
			    		
			    	
				}
			}
		}
	}
}
