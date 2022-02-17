package net.minecraft.src.MEDMEX.Commands.impl;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Packet;
import net.minecraft.src.Packet11PlayerPosition;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Commands.Command;
import net.minecraft.src.MEDMEX.Modules.Module;



public class PacketCancel extends Command {
	
	public PacketCancel() {
		super("PacketCancel", "Cancel specified packets", "PacketCancel <packetID>", "pc");
		
	}

	@Override
	public void onCommand(String[] args, String command) {
		try {			
			if(net.minecraft.src.MEDMEX.Modules.World.PacketCancel.cPackets.contains(Integer.valueOf(args[0]))) {
				net.minecraft.src.MEDMEX.Modules.World.PacketCancel.cPackets.remove((Object)Integer.valueOf(args[0]));
				String format = ""+Packet.packetIdToClassMap.get(Integer.valueOf(args[0]));
				Client.addChatMessage("Stopped cancelling: "+format.replace("class net.minecraft.src.", ""));;
			}else {
				net.minecraft.src.MEDMEX.Modules.World.PacketCancel.cPackets.add(Integer.valueOf(args[0]));
				String format = ""+Packet.packetIdToClassMap.get(Integer.valueOf(args[0]));
				Client.addChatMessage("Started cancelling: "+format.replace("class net.minecraft.src.", ""));
			}
		}catch(Exception e) {
			System.out.println(e);
			Client.addChatMessage("Usage: PacketCancel <packetID>");
		}
		
	}

}
