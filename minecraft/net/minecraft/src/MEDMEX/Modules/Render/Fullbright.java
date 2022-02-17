package net.minecraft.src.MEDMEX.Modules.Render;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;

public class Fullbright extends Module{
	public static Fullbright instance;
	public Fullbright() {
		super("Fullbright", Keyboard.KEY_NONE, Category.RENDER);
		instance = this;
	}
	
	public void onDisable() {
		mc.gameSettings.updateWorldLightLevels();
		mc.renderGlobal.updateAllRenderers();
	}
	public void onEnable() {
		mc.gameSettings.updateWorldLightLevels();
		mc.renderGlobal.updateAllRenderers();
	}
}
