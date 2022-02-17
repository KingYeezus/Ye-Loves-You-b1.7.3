package net.minecraft.src.MEDMEX.Modules.World;



import org.lwjgl.input.Keyboard;

import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.EntityRenderer;

public class NoWeather extends Module {
	public static NoWeather instance;
	
	
	
	public NoWeather() {
		super("NoWeather", Keyboard.KEY_P, Category.WORLD);
		instance = this;
	}
	
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				if(mc.theWorld.thunderingStrength > 0) {
					this.attribute = " §7[§f"+"Thunder"+"§7]";
				}
				if(mc.theWorld.rainingStrength > 0) {
					this.attribute = " §7[§f"+"Rain"+"§7]";
				}
				if(mc.theWorld.rainingStrength == 0 && mc.theWorld.thunderingStrength == 0) {
					this.attribute = " §7[§f"+"Clear"+"§7]";
				}
			}
				
			}
				
			}
}
