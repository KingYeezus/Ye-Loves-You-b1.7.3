package net.minecraft.src.MEDMEX.Modules.Render;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.de.Hero.settings.Setting;

public class Xray extends Module{
	public static Xray instance;
	public Xray() {
		super("Xray", Keyboard.KEY_NONE, Category.RENDER);
		instance = this;
	}
	
	public void onDisable() {
		mc.renderGlobal.loadRenderers();
	}
	public void onEnable() {
		mc.renderGlobal.loadRenderers();
	}
}
