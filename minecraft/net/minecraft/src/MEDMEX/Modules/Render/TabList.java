package net.minecraft.src.MEDMEX.Modules.Render;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Gui;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet28EntityVelocity;
import net.minecraft.src.Packet31RelEntityMove;
import net.minecraft.src.Packet33RelEntityMoveLook;
import net.minecraft.src.Packet34EntityTeleport;
import net.minecraft.src.Packet3Chat;
import net.minecraft.src.Packet70Bed;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.de.Hero.settings.Setting;


public class TabList extends Module{
	public static boolean down = false;
	public static CopyOnWriteArrayList<String> players = new CopyOnWriteArrayList<String>();
	public static String messages = "";
	public static TabList instance;

	public TabList() {
		super("TabList", Keyboard.KEY_NONE, Category.RENDER);

		instance = this;
	}
	
	public void getPacket(EventPacket e) {
		if(this.isEnabled()) {
			if(mc.thePlayer != null && mc.theWorld != null) {
				if(e.getPacket() instanceof Packet3Chat) {
					if(Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
					Packet3Chat p = (Packet3Chat)e.getPacket();
					if(p.message.contains(",")) {
						String message = p.message.replace(" ", "");
						messages += message;
						for(int i = 0; i < message.split(",").length; i++) {
							players.add(message.split(",")[i]);
						}
						
						e.setCancelled(true);
						
					}
					if(p.message.contains("Players online:")) {
						e.setCancelled(true);
					}
				}
				}
				}
			}
				
			}
	
	public void onRenderGUI() {
		if(this.isEnabled()) {
			if(Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
				 ScaledResolution var5 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
				for(int i = 0; i < players.size(); i++) {
					mc.ingameGUI.drawRect(var5.getScaledWidth() / 2 - 40, 4 + i*10, var5.getScaledWidth() / 2 + 40, 13 + i*10, 0xff333333);
					mc.ingameGUI.drawCenteredString(mc.fontRenderer, players.get(i), var5.getScaledWidth() / 2, 4 + i*10, -1);	
				}
			}else {
				players.clear();
			}
		}
	}
				
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				if(Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
					if(!down) {
					down = true;
					mc.thePlayer.sendChatMessage("/list");
					}
				}else {
					down = false;
				}

			}
		}
	}
}
