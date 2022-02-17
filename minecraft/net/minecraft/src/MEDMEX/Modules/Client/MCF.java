package net.minecraft.src.MEDMEX.Modules.Client;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Gui;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet28EntityVelocity;
import net.minecraft.src.Packet3Chat;
import net.minecraft.src.Packet70Bed;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.de.Hero.settings.Setting;


public class MCF extends Module{
	boolean preventloop = false;

	public static MCF instance;

	public MCF() {
		super("MCF", Keyboard.KEY_NONE, Category.CLIENT);

		instance = this;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				if(Mouse.isButtonDown(2)) {
					if(!preventloop) {
					if(mc.objectMouseOver != null) {
						if(mc.objectMouseOver.entityHit instanceof EntityPlayer){
							EntityPlayer playerhit = (EntityPlayer) mc.objectMouseOver.entityHit;
							if(Client.friends.contains(playerhit.username)) {
								Client.friends.remove(playerhit.username);
								Client.addChatMessage("Removed "+playerhit.username+" from friendslist");
							}else {
							Client.friends.add(playerhit.username);
							Client.addChatMessage("Added "+playerhit.username+" to friendslist");
							}
							preventloop = true;
						}
					}
					}
				}else {
					preventloop = false;
				}

			}
		}
	}
}
