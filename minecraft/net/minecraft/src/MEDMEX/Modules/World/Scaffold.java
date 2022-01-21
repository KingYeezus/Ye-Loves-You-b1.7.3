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

public class Scaffold extends Module{
	public static Scaffold instance;
	public Scaffold() {
		super("Scaffold", Keyboard.KEY_NONE, Category.WORLD);
		instance = this;
	}
	
	public void setup() {
		Client.settingsmanager.rSetting(new Setting("Scaffold Reach", this, 3, 1, 5, true));
	}
	
	
	Timer timer = new Timer();


	public boolean canPlaceBlock(int x, int y, int z) {
        int id = mc.theWorld.getBlockId(x, y, z);
        return id == 0 || id == 10 || id == 11 || id == 8 || id == 9;
    }

	public void placeBlock(int x, int y, int z) {
        if(!canPlaceBlock(x-1, y, z)) {
            mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), x-1, y, z, 5);
            return;
        }
        if(!canPlaceBlock(x+1, y, z)) {
            mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), x+1, y, z, 4);
            return;
        }
        if(!canPlaceBlock(x, y, z-1)) {
            mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), x, y, z-1, 3);
            return;
        }
        if(!canPlaceBlock(x, y, z+1)) {
            mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), x, y, z+1, 2);
            return;
        }
        if(!canPlaceBlock(x, y-1, z)) {
            mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), x, y-1, z, 1);
            return;
        }
        if(!canPlaceBlock(x, y+1, z)) {
            mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), x, y-1, z, 0);
            return;
        }
    }

	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				 	int plrX = MathHelper.floor_double(mc.thePlayer.posX);
			   		int plrY = MathHelper.floor_double(mc.thePlayer.posY);
			   		int plrZ = MathHelper.floor_double(mc.thePlayer.posZ);
			   		int radius = (int) (Client.settingsmanager.getSettingByName("Scaffold Reach").getValDouble() - 2);
			   		for(int x = (int) (plrX-radius); x <= plrX+radius; x++) {
			               for(int z = (int) (plrZ-radius); z <= plrZ+radius; z++) {
			             	  //if(scaffoldTimer.delay(500)) {
			             	  //ClientUtils.mc().getSendQueue().addToSendQueue(new Packet15Place(x, plrY -3, z, 1, inventory.getCurrentItem()));
			            	   if(mc.thePlayer.ticksExisted % 8 == 0) {
			             	     placeBlock(x, plrY -2, z);
			             			//if(this.ClientUtils.mc().thePlayer.ticksExisted % 5 == 0) {
			             	  //scaffoldTimer.reset();
			    }
			               }
			}
}
	}
}
}
