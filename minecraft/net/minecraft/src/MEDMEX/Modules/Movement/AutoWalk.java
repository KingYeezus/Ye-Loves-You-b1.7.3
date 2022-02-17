package net.minecraft.src.MEDMEX.Modules.Movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet70Bed;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;

public class AutoWalk extends Module{
	public AutoWalk() {
		super("AutoWalk", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	public void onDisable() {
		mc.thePlayer.movementInput.checkKeyForMovementInput(mc.gameSettings.keyBindForward.keyCode, false);
	}
	
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				mc.thePlayer.moveEntityWithHeading(0, 1f);
			}
		}
	}
}
