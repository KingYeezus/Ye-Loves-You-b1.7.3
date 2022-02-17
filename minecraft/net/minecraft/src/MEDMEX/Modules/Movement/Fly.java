package net.minecraft.src.MEDMEX.Modules.Movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet13PlayerLookMove;
import net.minecraft.src.Packet17Sleep;
import net.minecraft.src.Packet19EntityAction;
import net.minecraft.src.Packet70Bed;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Utils.Timer;


public class Fly extends Module{
	public Fly() {
		super("Fly", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	Timer timer = new Timer();
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				mc.thePlayer.motionY = 0;
				
			}
		}
	}
}
