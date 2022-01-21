package net.minecraft.src.MEDMEX.Commands.impl;

import net.minecraft.src.Vec3D;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Commands.Command;
import net.minecraft.src.MEDMEX.Modules.Module;
public class Yaw extends Command {
	
	public Yaw() {
		super("Yaw", "Sets yaw destination", "<Yaw> <x> <y> <z>", "Yaw");
	
	}

	@Override
	public void onCommand(String[] args, String command) {
		try {
			Vec3D dest = new Vec3D(Integer.valueOf(args[0]), Integer.valueOf(args[1]) , Integer.valueOf(args[2]));
			net.minecraft.src.MEDMEX.Modules.Player.Yaw.Destination = dest;
		} catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {
			Client.addChatMessage("Usage: Toggle/t <module>");
		}
		
	}

}
