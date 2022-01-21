package net.minecraft.src.MEDMEX.Modules.World;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Gui;
import net.minecraft.src.Packet;
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


public class PacketCancel extends Module{
	public static CopyOnWriteArrayList<Integer> cPackets = new CopyOnWriteArrayList<Integer>();
	public static PacketCancel instance;
	public PacketCancel() {
		super("PacketCancel", Keyboard.KEY_NONE, Category.WORLD);
		instance = this;
	}
	
	public void getPacket(EventPacket e) {
		if(this.isEnabled()) {
			if(mc.thePlayer != null && mc.theWorld != null) {
				if(cPackets.contains(e.getPacket().getPacketId())) {
						e.setCancelled(true);
					}
			
				
				
			}
				
			}
				
			}
	
}
