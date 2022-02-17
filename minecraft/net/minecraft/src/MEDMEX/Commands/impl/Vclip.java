package net.minecraft.src.MEDMEX.Commands.impl;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Packet11PlayerPosition;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Commands.Command;
import net.minecraft.src.MEDMEX.Modules.Module;



public class Vclip extends Command {
	
	public Vclip() {
		super("Vclip", "Clip vertically", "Vclip <blocks>", "vclip");
		
	}

	@Override
	public void onCommand(String[] args, String command) {
		try {
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + Double.valueOf(args[0]), mc.thePlayer.posZ);
			
			Client.addChatMessage("Vclipped "+args[0]+ " blocks");
		}catch(Exception e) {
			
		}
		
	}

}
