package net.minecraft.src.MEDMEX.Modules.Movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Material;
import net.minecraft.src.Timer;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;


public class Jesus extends Module{
	public static Jesus instance;
	public Jesus() {
		super("Jesus", Keyboard.KEY_NONE, Category.MOVEMENT);
		instance = this;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				if(mc.thePlayer.isInWater() || mc.thePlayer.isInsideOfMaterial(Material.lava)) {
					mc.thePlayer.motionY = 0.6f;
				}
				
			}
		}
	}
}
