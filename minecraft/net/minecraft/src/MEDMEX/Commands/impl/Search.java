package net.minecraft.src.MEDMEX.Commands.impl;



import java.util.Collections;

import net.minecraft.client.Minecraft;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Commands.Command;

public class Search extends Command {
	
	public Search() {
		super("Search", "Changes Search", "Search <add/del> <blockid>", "search");
	}

	@Override
	public void onCommand(String[] args, String command) {
		try {
			if(args[0].equals("add")) {
				int blockid = Integer.valueOf(args[1]);
				net.minecraft.src.MEDMEX.Modules.Render.Search.blocks.add(blockid);
				mc.renderGlobal.loadRenderers();
				Client.addChatMessage("Added: "+blockid+" To Search.");
			}
			if(args[0].contains("del")) {
				int blockid = Integer.valueOf(args[1]);
				net.minecraft.src.MEDMEX.Modules.Render.Search.blocks.removeAll(Collections.singleton(blockid));
				mc.renderGlobal.loadRenderers();
				Client.addChatMessage("Removed: "+blockid+" From Search.");
			}
			
			
			
		}catch(Exception e) {
			Client.addChatMessage("Usage: Search <add/del> <blockid>");
		}
			
		}
	}

		
