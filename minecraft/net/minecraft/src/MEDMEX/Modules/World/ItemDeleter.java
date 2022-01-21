package net.minecraft.src.MEDMEX.Modules.World;



import org.lwjgl.input.Keyboard;

import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityRenderer;

public class ItemDeleter extends Module {
	public static ItemDeleter instance;
	
	
	
	public ItemDeleter() {
		super("ItemDeleter", Keyboard.KEY_P, Category.WORLD);
		instance = this;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				for(Entity ent : mc.theWorld.loadedEntityList) {
					if(ent instanceof EntityItem) {
						ent.setEntityDead();
					}
				}
			}
				
			}
				
			}
}
