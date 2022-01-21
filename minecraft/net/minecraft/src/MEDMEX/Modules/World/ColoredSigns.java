package net.minecraft.src.MEDMEX.Modules.World;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Packet130UpdateSign;
import net.minecraft.src.Packet31RelEntityMove;
import net.minecraft.src.Timer;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;

public class ColoredSigns extends Module{
	public static String[] newlines = {"","","",""};
	String newline0, newline1, newline2, newline3;
	public ColoredSigns() {
		super("ColoredSigns", Keyboard.KEY_NONE, Category.WORLD);
	}
	public void getPacket(EventPacket e) {
		if(this.isEnabled()) {
			if(mc.thePlayer != null && mc.theWorld != null) {
			if(e.getPacket() instanceof Packet130UpdateSign) {
				Packet130UpdateSign packet = (Packet130UpdateSign)e.getPacket();
				packet.signLines[0] = newlines[0];
				packet.signLines[1] = newlines[1];
				packet.signLines[2] = newlines[2];
				packet.signLines[3] = newlines[3];
				
				
			}
			}
		}
	}
}
