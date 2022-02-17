package net.minecraft.src.MEDMEX.Commands.impl;

import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Commands.Command;
import net.minecraft.src.MEDMEX.Commands.CommandManager;

public class Help extends Command {
	
	public Help() {
		super("Help", "Help command", "help", "help");
		
	}

	@Override
	public void onCommand(String[] args, String command) {
		for(Command c : CommandManager.commands) {
			Client.addChatMessage(c.name+" - "+c.syntax + " - "+c.description);
		}
	}
	}
		
