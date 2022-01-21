package net.minecraft.src.MEDMEX.Modules.Combat;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet70Bed;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.de.Hero.settings.Setting;

public class ForceField extends Module{
	public static Entity target;
	public ForceField() {
		super("ForceField", Keyboard.KEY_NONE, Category.COMBAT);
	}
	
	public void setup() {
		Client.settingsmanager.rSetting(new Setting("Attack Players", this, true));
		Client.settingsmanager.rSetting(new Setting("Attack Mobs", this, true));
		Client.settingsmanager.rSetting(new Setting("Attack Animals", this, false));
		}
	
	
	public void onDisable() {
		target = null;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				List entities = mc.theWorld.getLoadedEntityList();
				for (int i = 0; i < entities.size(); i++) {
					if(entities.get(i) instanceof EntityPlayer && entities.get(i) != mc.thePlayer && Client.settingsmanager.getSettingByName("Attack Players").getValBoolean()) {
						if(mc.thePlayer.getDistanceToEntity((Entity)entities.get(i)) <= 6) {
							EntityPlayer p = (EntityPlayer) entities.get(i);
							if(!Client.friends.contains(p.username))
								target =  (Entity)entities.get(i);
								mc.playerController.attackEntity(mc.thePlayer, (Entity)entities.get(i));
					}
					}
					if(entities.get(i) instanceof EntityMob && entities.get(i) != mc.thePlayer && Client.settingsmanager.getSettingByName("Attack Mobs").getValBoolean()) {
						if(mc.thePlayer.getDistanceToEntity((Entity)entities.get(i)) <= 6) {
						mc.playerController.attackEntity(mc.thePlayer, (Entity)entities.get(i));
					}	
					}
					if(entities.get(i) instanceof EntityAnimal && entities.get(i) != mc.thePlayer && Client.settingsmanager.getSettingByName("Attack Animals").getValBoolean()) {
						if(mc.thePlayer.getDistanceToEntity((Entity)entities.get(i)) <= 6) {
						mc.playerController.attackEntity(mc.thePlayer, (Entity)entities.get(i));
					}
					}
					
					
						
						
					
				}
				if(target != null && mc.thePlayer.getDistanceToEntity(target) > 6) {
					target = null;
				}

			}
		}
	}
}
