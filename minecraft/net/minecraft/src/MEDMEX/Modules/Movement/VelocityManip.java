package net.minecraft.src.MEDMEX.Modules.Movement;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet11PlayerPosition;
import net.minecraft.src.Packet28EntityVelocity;
import net.minecraft.src.Packet70Bed;
import net.minecraft.src.Packet8UpdateHealth;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;

public class VelocityManip extends Module{
	public VelocityManip() {
		super("VelocityManip", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	public void getPacket(EventPacket e) {
		if(this.isEnabled()) {
			if(mc.thePlayer != null && mc.theWorld != null) {
				if(e.getPacket() instanceof Packet8UpdateHealth) {
					Packet8UpdateHealth packet = (Packet8UpdateHealth)e.getPacket();
						
						//packet.healthMP = -999999999;
						//e.setCancelled(true);
					}
				}
			}
				
			}
				
			
	
	public void gettarget() {
		for(EntityPlayer p : mc.theWorld.playerEntities) {
			if(mc.thePlayer.getDistanceToEntity(p) < 4) {
				if(p != mc.thePlayer) {
				double x = p.posX;
				double y = p.posY;
				double z = p.posZ;
				
			}
			}
		}
	}
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				
				//mc.getSendQueue().addToSendQueue(new Packet28EntityVelocity(mc.thePlayer.entityId, mc.thePlayer.posX+100, mc.thePlayer.posY, mc.thePlayer.posZ));
				
			}
		}
	}
}
