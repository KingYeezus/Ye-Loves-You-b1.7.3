package net.minecraft.src.MEDMEX.Modules.Movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;

public class InventoryMove extends Module{
	public static InventoryMove instance;
	public InventoryMove() {
		super("InventoryMove", Keyboard.KEY_NONE, Category.MOVEMENT);
		instance = this;
	}
}
