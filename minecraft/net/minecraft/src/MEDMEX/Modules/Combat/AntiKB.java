package net.minecraft.src.MEDMEX.Modules.Combat;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet28EntityVelocity;
import net.minecraft.src.Packet70Bed;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;


public class AntiKB extends Module{
	public static AntiKB instance;
	
	public AntiKB() {
		super("AntiKB", Keyboard.KEY_NONE, Category.COMBAT);
		instance = this;
	}
	
	public void getPacket(EventPacket e) {
		if(this.isEnabled()) {
			if(mc.thePlayer != null && mc.theWorld != null) {
				if(e.getPacket() instanceof Packet28EntityVelocity) {
					Packet28EntityVelocity packet = (Packet28EntityVelocity)e.getPacket();
					if(packet.entityId == mc.thePlayer.entityId) {
						e.setCancelled(true);
					}
				}
			}
				
			}
				
			}

}
