package net.minecraft.src.MEDMEX.Modules.Combat;

import java.awt.Color;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet12PlayerLook;
import net.minecraft.src.Packet53BlockChange;
import net.minecraft.src.Packet70Bed;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3D;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Utils.RenderUtils;
import net.minecraft.src.MEDMEX.Utils.Timer;
import net.minecraft.src.de.Hero.settings.Setting;

public class BedAura extends Module{
	float yaw;
	Vec3D target;
	Vec3D toplace;
	public static Timer timer = new Timer();
	public BedAura() {
		super("BedAura", Keyboard.KEY_NONE, Category.COMBAT);
	}
	
	public void setup() {
		Client.settingsmanager.rSetting(new Setting("Auto Place", this, true));
	}
	
	
	public void onRender() {
	    if (this.isEnabled()) {
	    	if(target != null) {
	    		if(mc.theWorld.getBlockId((int)target.xCoord, (int)target.yCoord, (int)target.zCoord) == 26) {
	    			if(mc.theWorld.getBlockId((int)target.xCoord + 1, (int)target.yCoord, (int)target.zCoord) == 26) {
	    				Vec3D bed = new Vec3D(target.xCoord+1, target.yCoord, target.zCoord);
	    				RenderUtils.bedESPBoxFilled(bed, new Color(200, 25, 25, (int)((120.0F + 0.5 / 2.0F))));
	    			}
	    			if(mc.theWorld.getBlockId((int)target.xCoord - 1, (int)target.yCoord, (int)target.zCoord) == 26) {
	    				Vec3D bed = new Vec3D(target.xCoord-1, target.yCoord, target.zCoord);
	    				RenderUtils.bedESPBoxFilled(bed, new Color(200, 25, 25, (int)((120.0F + 0.5 / 2.0F))));
	    			}
	    			if(mc.theWorld.getBlockId((int)target.xCoord, (int)target.yCoord, (int)target.zCoord+1) == 26) {
	    				Vec3D bed = new Vec3D(target.xCoord, target.yCoord, target.zCoord+1);
	    				RenderUtils.bedESPBoxFilled(bed, new Color(200, 25, 25, (int)((120.0F + 0.5 / 2.0F))));
	    			}
	    			if(mc.theWorld.getBlockId((int)target.xCoord, (int)target.yCoord, (int)target.zCoord-1) == 26) {
	    				Vec3D bed = new Vec3D(target.xCoord, target.yCoord, target.zCoord-1);
	    				RenderUtils.bedESPBoxFilled(bed, new Color(200, 25, 25, (int)((120.0F + 0.5 / 2.0F))));
	    			}
	    			RenderUtils.bedESPBoxFilled(target, new Color(200, 25, 25, (int)((120.0F + 0.5 / 2.0F))));
	    		}else {
	    			target = null;
	    		}
	    		
	    	}
	    	
	    	if(toplace != null) {
	    		RenderUtils.blockESPBoxFilled(toplace, new Color(25, 200, 25,  (int)((120.0F + 0.5 / 2.0F))));
	    	}
	    }
	     
	  }
	

	
	public void getPacket(EventPacket e) {
		if(this.isEnabled()) {
			if(mc.thePlayer != null && mc.theWorld != null) {
				if(e.getPacket() instanceof Packet53BlockChange) {
					Packet53BlockChange packet = (Packet53BlockChange)e.getPacket();
						if(packet.type == 26) {
							if(mc.theWorld.getBlockId(packet.xPosition, packet.yPosition, packet.zPosition) == 26) {
								target = new Vec3D(packet.xPosition, packet.yPosition, packet.zPosition);
							}
						}
					}
				}
					
					
				}
			}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				if(target != null) {
					if(mc.thePlayer.getDistance(target.xCoord, target.yCoord, target.zCoord) <= 6 && mc.thePlayer.dimension != 0) {
						mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), (int)target.xCoord, (int)target.yCoord, (int)target.zCoord, 1);
					}
				}
				if(Client.settingsmanager.getSettingByName("Auto Place").getValBoolean()) {
				if(mc.thePlayer.dimension != 0) {
				for(EntityPlayer p : mc.theWorld.playerEntities) {
					if(mc.thePlayer.getDistanceToEntity(p) <= 6) {
						if(p != mc.thePlayer && !Client.friends.contains(p.username)) {
						int plrX = MathHelper.floor_double(p.posX);
				   		int plrY = MathHelper.floor_double(p.posY);
				   		int plrZ = MathHelper.floor_double(p.posZ);
				   		int radius = (int) 4 - 2;
				   		for(int x = (int) (plrX-radius); x <= plrX+radius; x++) {
				               for(int z = (int) (plrZ-radius); z <= plrZ+radius; z++) {
				            	   for(int y = (int) (plrY-radius); y <= plrY+radius; y++) {				               
					            	   if(!mc.theWorld.getBlockMaterial(x, y, z).isSolid()){
					            		   if(mc.theWorld.getBlockMaterial(x, y-1, z).isSolid()){
					            			   if(mc.thePlayer.getDistance(x, y-1, z) <= 4) {
					            		   if(mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().itemID == 355) {
					            			   if(isBedPlaceable(new Vec3D(x, y-1, z))) {
					            			   if(timer.hasTimeElapsed(100, true)) {
					            			   toplace = new Vec3D(x, y-1, z);
					            			   mc.getSendQueue().addToSendQueue(new Packet12PlayerLook(yaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround));
					            			   mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), x, y - 1, z, 1);
					            			   
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
						}
					}
				}
						
						
					
				

			}
		}
	}
	
	public boolean isBedPlaceable(Vec3D bedpos) {
		if(!mc.theWorld.isAirBlock((int)bedpos.xCoord, (int)bedpos.yCoord, (int)bedpos.zCoord)) {
			if(mc.theWorld.isAirBlock((int)bedpos.xCoord+1, (int)bedpos.yCoord+1, (int)bedpos.zCoord) && !mc.theWorld.isAirBlock((int)bedpos.xCoord+1, (int)bedpos.yCoord, (int)bedpos.zCoord)) {
				yaw = -90;
				return true;
			}
			if(mc.theWorld.isAirBlock((int)bedpos.xCoord-1, (int)bedpos.yCoord+1, (int)bedpos.zCoord) && !mc.theWorld.isAirBlock((int)bedpos.xCoord-1, (int)bedpos.yCoord, (int)bedpos.zCoord)) {
				yaw = 90;
				return true;
			}
			if(mc.theWorld.isAirBlock((int)bedpos.xCoord, (int)bedpos.yCoord+1, (int)bedpos.zCoord+1) && !mc.theWorld.isAirBlock((int)bedpos.xCoord, (int)bedpos.yCoord, (int)bedpos.zCoord+1)) {
				yaw = 0;
				return true;
			}
			if(mc.theWorld.isAirBlock((int)bedpos.xCoord, (int)bedpos.yCoord+1, (int)bedpos.zCoord-1) && !mc.theWorld.isAirBlock((int)bedpos.xCoord, (int)bedpos.yCoord, (int)bedpos.zCoord-1)) {
				yaw = 180;		
				return true;
			}
		}
		return false;
		
	}
	
}
