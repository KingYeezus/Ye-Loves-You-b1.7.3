package net.minecraft.src.MEDMEX.Modules.Render;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;

public class Coords extends Module{
	public static Coords instance;
	public Coords() {
		super("Coords", Keyboard.KEY_NONE, Category.RENDER);
		instance = this;
	}
}
