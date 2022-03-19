package net.minecraft.src.MEDMEX.Commands.impl;

import net.minecraft.src.Packet19EntityAction;
import net.minecraft.src.Vec3D;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Commands.Command;
import net.minecraft.src.MEDMEX.Modules.Module;
public class Wake extends Command {
	
	public Wake() {
		super("Wake", "Wake serverside", "Wake", "Wake");
	
	}

	@Override
	public void onCommand(String[] args, String command) {
		try {
			Client.sendPacket(new Packet19EntityAction(mc.thePlayer, 3));
		} catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {
			Client.addChatMessage("Usage: Wake");
		}
		
	}

}
