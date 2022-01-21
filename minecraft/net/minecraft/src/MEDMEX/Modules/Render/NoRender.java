package net.minecraft.src.MEDMEX.Modules.Render;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityBoat;
import net.minecraft.src.EntityItem;
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

public class NoRender extends Module{
	public static NoRender instance;
	public NoRender() {
		super("NoRender", Keyboard.KEY_NONE, Category.RENDER);
		instance = this;
	}
	
	public void setup() {
		Client.settingsmanager.rSetting(new Setting("NoRender Players", this, true));
		Client.settingsmanager.rSetting(new Setting("NoRender Items", this, true));
		Client.settingsmanager.rSetting(new Setting("NoRender Mobs", this, false));
		Client.settingsmanager.rSetting(new Setting("NoRender Animals", this, false));
		Client.settingsmanager.rSetting(new Setting("NoRender Vehicles", this, false));
	}
	
}
