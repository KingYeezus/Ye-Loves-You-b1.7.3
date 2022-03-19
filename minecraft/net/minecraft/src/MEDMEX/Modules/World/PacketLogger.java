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
import net.minecraft.src.Packet102WindowClick;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet28EntityVelocity;
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

public class PacketLogger extends Module {
	public static CopyOnWriteArrayList<Integer> lPackets = new CopyOnWriteArrayList<Integer>();
	public static PacketLogger instance;

	public PacketLogger() {
		super("PacketLogger", Keyboard.KEY_NONE, Category.WORLD);
		instance = this;
	}

	public void getPacket(EventPacket e) {
		if (this.isEnabled()) {
			if (mc.thePlayer != null && mc.theWorld != null) {
				if (lPackets.contains(e.getPacket().getPacketId())) {
					Client.addChatMessage("" + e.getPacket());
				}

			}

		}

	}

}
