package net.minecraft.src.MEDMEX.Modules.World;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet130UpdateSign;
import net.minecraft.src.Packet31RelEntityMove;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Utils.Timer;
import net.minecraft.src.de.Hero.settings.Setting;

public class FastDrop extends Module{
	public static Timer timer = new Timer();
	public FastDrop() {
		super("FastDrop", Keyboard.KEY_NONE, Category.WORLD);
	}
	
	public void setup() {
		Client.settingsmanager.rSetting(new Setting("Drop Speed", this, 1, 1, 1000, true));
		Client.settingsmanager.rSetting(new Setting("Insta 130", this, false));
	}
	
	
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				if(Client.settingsmanager.getSettingByName("Insta 130").getValBoolean()) {
						if(Mouse.isButtonDown(1)) {
							if(mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiContainer) {
								if(timer.hasTimeElapsed(1000, true)) {
								for(int i = 0; i < 129; i++) {
									mc.currentScreen.handleMouseInput();
									
								}
								}
							}
							
					}
						
						
					
					
				}else {
				
				
				if(Mouse.isButtonDown(1)) {
					if(mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiContainer) {
					for(int i = 0; i < Client.settingsmanager.getSettingByName("Drop Speed").getValDouble(); i++) {
				mc.currentScreen.handleMouseInput();
					}
					}
				}
				}
				
				
			}
		}
	}
}
