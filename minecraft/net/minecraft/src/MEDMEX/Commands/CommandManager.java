package net.minecraft.src.MEDMEX.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Commands.impl.*;
import net.minecraft.src.MEDMEX.Event.listeners.EventChat;

public class CommandManager {
	
	
	public static boolean chatencryption = false;
	public static List<Command> commands = new ArrayList<Command>();
	public String prefix = ".";
	
	public CommandManager() {
		setup();
	}
	
	public void setup() {
		commands.add(new Toggle());
		commands.add(new Bind());
		commands.add(new ColoredSigns());
		commands.add(new Waypoints());
		commands.add(new Vclip());
		commands.add(new Help());
		commands.add(new Search());
		commands.add(new Friends());
		commands.add(new Yaw());
		commands.add(new Drawn());
		commands.add(new PacketCancel());
		commands.add(new PacketLogger());
		
	}
	
	public void handleChat(EventChat event) {
		String message = event.getMessage();
		
		
		if(!message.startsWith(prefix))
			return;
		
		
		event.setCancelled(true);
		
		message = message.substring(prefix.length());
		
		boolean foundCommand = false;
		
		if(message.split(" ").length > 0);
		String commandName = message.split(" ")[0];
		
		for(Command c : commands) {
			if(c.aliases.contains(commandName) || c.name.equalsIgnoreCase(commandName)) {
				c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
				foundCommand = true;
				break;
			}
			
		}
		if(!foundCommand) {
			Client.addChatMessage("Could not find command.");
		}
		
	}
	
	
}