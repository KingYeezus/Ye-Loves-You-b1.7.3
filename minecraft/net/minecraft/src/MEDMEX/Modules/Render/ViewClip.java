package net.minecraft.src.MEDMEX.Modules.Render;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;

public class ViewClip extends Module{
	public static ViewClip instance;
	public ViewClip() {
		super("ViewClip", Keyboard.KEY_NONE, Category.RENDER);
		instance = this;
	}
}
