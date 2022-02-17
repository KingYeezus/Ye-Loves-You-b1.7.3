package net.minecraft.src.MEDMEX.Modules.World;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.MathHelper;
import net.minecraft.src.Packet15Place;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Utils.Timer;
import net.minecraft.src.de.Hero.settings.Setting;

public class Tower extends Module{
	public static Tower instance;
	public Tower() {
		super("Tower", Keyboard.KEY_NONE, Category.WORLD);
		instance = this;
	}	

	public boolean canPlaceBlock(int x, int y, int z) {
        int id = mc.theWorld.getBlockId(x, y, z);
        return id == 0 || id == 10 || id == 11 || id == 8 || id == 9;
    }
	
	public static int X;
	public static int Z;
	public static int offsetX;
	public static int offsetZ;
	Timer timer = new Timer();
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				X = (int)mc.thePlayer.posX;
				Z = (int)mc.thePlayer.posZ;
				
				if(mc.thePlayer.posX < 0 && mc.thePlayer.posZ < 0) {
					offsetX = -1;
					offsetZ = -1;
					
				}
				if(mc.thePlayer.posX > 0 && mc.thePlayer.posZ > 0) {
					offsetX = 0;
					offsetZ = 0;
				}
				if(mc.thePlayer.posX > 0 && mc.thePlayer.posZ < 0) {
					offsetX = 0;
					offsetZ = -1;
				}
				if(mc.thePlayer.posX < 0 && mc.thePlayer.posZ > 0) {
					offsetX = -1;
					offsetZ =  0;
				}
				
				if(Keyboard.isKeyDown(mc.gameSettings.keyBindJump.keyCode)) {
				if(canPlaceBlock(X+offsetX, (int)mc.thePlayer.posY - 2, Z+offsetZ))
				{
					if(timer.hasTimeElapsed(200, true))
					mc.thePlayer.jump();
					mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), X+offsetX, (int)mc.thePlayer.posY - 3,Z+offsetZ, 1);
				}
				}
}
	}
}
}
