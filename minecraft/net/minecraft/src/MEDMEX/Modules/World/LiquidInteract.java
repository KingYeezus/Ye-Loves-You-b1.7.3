package net.minecraft.src.MEDMEX.Modules.World;



import org.lwjgl.input.Keyboard;

import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.EntityRenderer;

public class LiquidInteract extends Module {
	public static LiquidInteract instance;
	
	
	
	public LiquidInteract() {
		super("LiquidInteract", Keyboard.KEY_NONE, Category.WORLD);
		instance = this;
	}
}
