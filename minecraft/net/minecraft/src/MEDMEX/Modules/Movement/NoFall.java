package net.minecraft.src.MEDMEX.Modules.Movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Timer;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;

public class NoFall extends Module{
	public NoFall() {
		super("NoFall", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				if(mc.thePlayer.fallDistance > 2)
					mc.getSendQueue().addToSendQueue(new Packet10Flying(true));
			}
		}
	}
}
