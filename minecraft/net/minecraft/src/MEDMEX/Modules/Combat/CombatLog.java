package net.minecraft.src.MEDMEX.Modules.Combat;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet28EntityVelocity;
import net.minecraft.src.Packet70Bed;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Modules.Client.AutoReconnect;
import net.minecraft.src.de.Hero.settings.Setting;


public class CombatLog extends Module{
	public static CombatLog instance;
	
	public CombatLog() {
		super("CombatLog", Keyboard.KEY_NONE, Category.COMBAT);
		instance = this;
	}
	
	public void setup() {
		Client.settingsmanager.rSetting(new Setting("Log Health", this, 6, 1, 20, true));
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {	
				this.attribute = " §7[§f"+(float) Client.settingsmanager.getSettingByName("Log Health").getValDouble() / 2+"§7]";
				if(mc.thePlayer.health <= Client.settingsmanager.getSettingByName("Log Health").getValDouble()) {
					if(AutoReconnect.instance.isEnabled())
						AutoReconnect.instance.toggle();
					mc.theWorld.sendQuittingDisconnectingPacket();
					if(this.isEnabled()) 
						this.toggle();
					
				}
			}
				
			}
				
			}


}
