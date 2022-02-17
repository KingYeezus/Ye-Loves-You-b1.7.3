package net.minecraft.src.MEDMEX.Modules.World;


import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;

public class VisualRange extends Module{
	public static VisualRange instance;
	public VisualRange() {
		super("VisualRange", Keyboard.KEY_NONE, Category.WORLD);
		instance = this;
	} 
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				int players = mc.theWorld.playerEntities.size() - 1;
				this.attribute = " §7[§f"+String.valueOf(players)+"§7]";
			}
		}
	}
}
