package net.minecraft.src.MEDMEX.Modules.Movement;

import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.GuiDownloadTerrain;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet0KeepAlive;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet11PlayerPosition;
import net.minecraft.src.Packet13PlayerLookMove;
import net.minecraft.src.Packet70Bed;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.de.Hero.settings.Setting;

public class Blink extends Module{
	public static Blink instance;
	public Blink() {
		super("Blink", Keyboard.KEY_NONE, Category.MOVEMENT);
		instance = this;
	}
	
	public void setup() {
		Client.settingsmanager.rSetting(new Setting("Blink Delay", this, 1, 20, 100, true));
	}
	
	public boolean shouldCancel = true;
	
	public CopyOnWriteArrayList<Packet10Flying> backedPackets = new CopyOnWriteArrayList<Packet10Flying>();
	
	public void getPacket(EventPacket e) {
		if(this.isEnabled()) {
			if(mc.thePlayer != null && mc.theWorld != null) {
				if(!(mc.currentScreen instanceof GuiDownloadTerrain)) {
					if(shouldCancel) {
						if(e.getPacket() instanceof Packet10Flying) {
							Packet10Flying p = (Packet10Flying)e.getPacket();
							backedPackets.add(p);
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				if(mc.thePlayer.ticksExisted % 15 == 0)
					Client.sendPacket(new Packet0KeepAlive());
				if(!backedPackets.isEmpty())
					this.attribute = " §7[§f"+backedPackets.size()+"§7]";
				if(backedPackets.size() > Client.settingsmanager.getSettingByName("Blink Delay").getValDouble()) {
					shouldCancel = false;
					for(Packet10Flying p : backedPackets) {
						Client.sendPacket(p);
					}
					backedPackets.clear();
					shouldCancel = true;
				}
			}
		}
	}
	
	public void onEnable() {
		shouldCancel = true;
	}
	
	public void onDisable() {
		shouldCancel = false;
	}
}
