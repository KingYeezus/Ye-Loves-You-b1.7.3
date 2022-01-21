package net.minecraft.src.MEDMEX.Commands.impl;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Packet;
import net.minecraft.src.Packet11PlayerPosition;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Commands.Command;
import net.minecraft.src.MEDMEX.Modules.Module;



public class PacketLogger extends Command {
	
	public PacketLogger() {
		super("PacketLogger", "Log specified packets", "PacketLogger <packetID>", "pl");
		
	}

	@Override
	public void onCommand(String[] args, String command) {
		try {			
			if(net.minecraft.src.MEDMEX.Modules.World.PacketLogger.lPackets.contains(Integer.valueOf(args[0]))) {
				net.minecraft.src.MEDMEX.Modules.World.PacketLogger.lPackets.remove((Object)Integer.valueOf(args[0]));
				String format = ""+Packet.packetIdToClassMap.get(Integer.valueOf(args[0]));
				Client.addChatMessage("Stopped logging: "+format.replace("class net.minecraft.src.", ""));;
			}else {
				net.minecraft.src.MEDMEX.Modules.World.PacketLogger.lPackets.add(Integer.valueOf(args[0]));
				String format = ""+Packet.packetIdToClassMap.get(Integer.valueOf(args[0]));
				Client.addChatMessage("Started logging: "+format.replace("class net.minecraft.src.", ""));
			}
		}catch(Exception e) {
			System.out.println(e);
			Client.addChatMessage("Usage: PacketLogger <packetID>");
		}
		
	}

}
