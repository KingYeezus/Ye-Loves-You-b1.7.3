package net.minecraft.src.MEDMEX.Modules.Combat;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet28EntityVelocity;
import net.minecraft.src.Packet70Bed;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;


public class KillSults extends Module{
	public static KillSults instance;
	
	public KillSults() {
		super("KillSults", Keyboard.KEY_NONE, Category.COMBAT);
		instance = this;
	}
	
	public void PlayerUnload(EntityPlayer p) {
		if(this.isEnabled() && mc.thePlayer != null && mc.thePlayer.isEntityAlive() && p != mc.thePlayer && mc.thePlayer.getDistanceToEntity(p) < 6.0 && p.username != mc.thePlayer.username) {
			mc.thePlayer.sendChatMessage(p.username+" got obliterated by Ye Loves You");
		}
	}
}
