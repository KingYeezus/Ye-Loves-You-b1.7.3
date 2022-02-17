package net.minecraft.src.MEDMEX.Modules.Movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet70Bed;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;

public class Step extends Module{
	public Step() {
		super("Step", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	public void onDisable() {
		mc.thePlayer.stepHeight = 0.5f;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				mc.thePlayer.stepHeight = 1;
			}
		}
	}
}
