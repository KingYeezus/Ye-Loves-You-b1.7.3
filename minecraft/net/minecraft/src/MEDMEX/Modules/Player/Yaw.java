package net.minecraft.src.MEDMEX.Modules.Player;

import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiDownloadTerrain;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet0KeepAlive;
import net.minecraft.src.Packet101CloseWindow;
import net.minecraft.src.Packet103SetSlot;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet11PlayerPosition;
import net.minecraft.src.Packet12PlayerLook;
import net.minecraft.src.Packet13PlayerLookMove;
import net.minecraft.src.Packet15Place;
import net.minecraft.src.Packet16BlockItemSwitch;
import net.minecraft.src.Vec3D;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Utils.Timer;
import net.minecraft.src.de.Hero.settings.Setting;

public class Yaw extends Module{
	public static Yaw instance;
	public static Vec3D Destination;
	public Yaw() {
		super("Yaw", Keyboard.KEY_NONE, Category.PLAYER);
		instance = this;
	}
	
	public void setup() {
		Client.settingsmanager.rSetting(new Setting("Direction", this, 0, 0, 3, true));
		Client.settingsmanager.rSetting(new Setting("Custom Pos", this, false));
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				if(Client.settingsmanager.getSettingByName("Custom Pos").getValBoolean()) {
					if(Destination != null) {
						int dX = (int)Destination.xCoord;
						int dY = (int)Destination.yCoord;
						int dZ = (int)Destination.zCoord;
						float yaw = (float) -(Math.atan2((dX-mc.thePlayer.posX),(dZ-mc.thePlayer.posZ))*(180.0/Math.PI));
						mc.thePlayer.rotationYaw = yaw;
					}
					
				}else {
				mc.thePlayer.rotationYaw = getYawFromDir((int) Client.settingsmanager.getSettingByName("Direction").getValDouble());
			}
			}
		}
	}
	
	
	public int getYawFromDir(int dir) {
		if(dir == 0) 
			return 0;
		if(dir == 1) 
			return 90;
		if(dir == 2) 
			return 180;
		return 270;
		
	}
}
