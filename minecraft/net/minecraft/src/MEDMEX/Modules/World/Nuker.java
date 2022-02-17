package net.minecraft.src.MEDMEX.Modules.World;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Block;
import net.minecraft.src.Packet130UpdateSign;
import net.minecraft.src.Packet14BlockDig;
import net.minecraft.src.Packet31RelEntityMove;
import net.minecraft.src.Timer;
import net.minecraft.src.Vec3D;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.de.Hero.settings.Setting;

public class Nuker extends Module{
	public Nuker() {
		super("Nuker", Keyboard.KEY_NONE, Category.WORLD);
	}

	
	public void setup() {
		ArrayList<String> options = new ArrayList<>();
		options.add("BedPvP");
		options.add("Above");
		Client.settingsmanager.rSetting(new Setting("Nuker Mode", this, "BedPvP", options));
	}

	CopyOnWriteArrayList<Vec3D> blockVecs = new CopyOnWriteArrayList<Vec3D>();
	long t = 0l;
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				if(Client.settingsmanager.getSettingByName("Nuker Mode").getValString().equalsIgnoreCase("BedPvP")) {
					byte b0 = 3;
		    		for(int A = b0; A>-b0; A--) {
		    			for(int B = b0; B > -b0; B--) {
		    				for(int C = b0; C > -b0; C--) {
		    					
		    					int A1 = ((int)(mc.thePlayer.posX + (double)A)); 
		    					int B1 = ((int)(mc.thePlayer.posY + (double)B)); 
		    					int C1 = ((int)(mc.thePlayer.posZ + (double)C)); 
		    					
		    					int BId = mc.theWorld.getBlockId(A1, B1, C1);
		    					Block block = Block.blocksList[BId];
			    					if(block != null) {
			    						if(block.blockID == 51) {
			    							mc.playerController.clickBlock(A1, B1-1, C1, 1);
			    						}
			    						if(block.blockID == 40 || block.blockID == 39)
			    						{
			    							mc.playerController.clickBlock(A1, B1, C1, 0);
			    						}
			    					}
		    					}
		    				}
		    			}
	    			}
				if(Client.settingsmanager.getSettingByName("Nuker Mode").getValString().equalsIgnoreCase("Above")) {
					byte b0 = 3;
		    		for(int A = b0; A>-b0; A--) {
		    			for(int B = 0; B < b0; B++) {
		    				for(int C = b0; C > -b0; C--) {
		    					
		    					int A1 = ((int)(mc.thePlayer.posX + (double)A)); 
		    					int B1 = ((int)(mc.thePlayer.posY + (double)B)); 
		    					int C1 = ((int)(mc.thePlayer.posZ + (double)C)); 
		    					
		    					int BId = mc.theWorld.getBlockId(A1, B1, C1);
		    					Block block = Block.blocksList[BId];
		    					if(block != null) {
		    						blockVecs.add(new Vec3D(A1, B1, C1));
		    						if(blockVecs.size() > 30) {
		    							blockVecs.remove(30);
		    						}
		    						if(!blockVecs.isEmpty()) {
		    							Vec3D tobreak = blockVecs.get(0);
		    							if(!mc.theWorld.isAirBlock((int)tobreak.xCoord, (int)tobreak.yCoord, (int)tobreak.zCoord)) {
		    								t++;
		    								if(t > 5) {
				    							Client.sendPacket(new Packet14BlockDig(0, (int)tobreak.xCoord, (int)tobreak.yCoord, (int)tobreak.zCoord, 0));
				    							Client.sendPacket(new Packet14BlockDig(2, (int)tobreak.xCoord, (int)tobreak.yCoord, (int)tobreak.zCoord, 0));
				    							t = 0l;
		    								}
		    							}else {
		    								blockVecs.remove(tobreak);
		    							}
		    						}
		    						
		    					}
			    					
		    					}
		    				}
		    			}
				}
	    		}
			}
		}
}

