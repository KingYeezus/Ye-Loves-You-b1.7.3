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
import net.minecraft.src.MEDMEX.Utils.Timer;
import net.minecraft.src.de.Hero.settings.Setting;


public class AutoAuthme extends Module{

	public static AutoAuthme instance;

	public AutoAuthme() {
		super("AutoAuthme", Keyboard.KEY_NONE, Category.CLIENT);
		instance = this;
	}
	
	Timer timer = new Timer();
	
	public void getPacket(EventPacket e) {
		if(this.isEnabled()) {
			if(mc.thePlayer != null && mc.theWorld != null) {
				if(e.getPacket() instanceof Packet3Chat) {
					Packet3Chat p = (Packet3Chat)e.getPacket();
					if(p.message.equals("§cPlease login with \"/login password\"")) {
						for(String a : Client.authme) {
							if(a.contains(mc.thePlayer.username)) {
								mc.thePlayer.sendChatMessage("/login "+a.split(":")[1]);
								Client.addChatMessage("Logged in automatically with AutoAuthme");
								
							}
						}
					}
					if(p.message.startsWith("/login")) {
						String pass = p.message.split(" ")[1];
						if(!Client.authme.contains(mc.thePlayer.username+":"+pass)) {
							Client.authme.add(mc.thePlayer.username+":"+pass);
							Client.addChatMessage("Added new entry to AutoAuthme");
						}
					}
				}	
			}
		}				
	}
}
