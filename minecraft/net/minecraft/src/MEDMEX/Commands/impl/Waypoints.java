package net.minecraft.src.MEDMEX.Commands.impl;



import org.lwjgl.input.Keyboard;


import net.minecraft.client.Minecraft;
import net.minecraft.src.Vec3D;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Commands.Command;


public class Waypoints extends Command {
	
	public Waypoints() {
		super("Waypoints", "Adds/removes a waypoint", "waypoints <add/remove> <name> <x> <y> <z> ", "waypoints");
		
	}

	@Override
	public void onCommand(String[] args, String command) {
		try {
			String currentAddress = (Minecraft.theMinecraft.serverName != null) ? (Minecraft.theMinecraft.serverName) : "singleplayer";
		    Minecraft mc = Minecraft.theMinecraft;
		    if (args[0].equalsIgnoreCase("add") && args.length >= 2) {
		      double x = mc.thePlayer.posX;
		      double y = mc.thePlayer.posY + 2.0D;
		      double z = mc.thePlayer.posZ;
		      int dim = mc.thePlayer.dimension;
		      int argsup = 0;
		      if (args.length >= argsup + 3)
		        try {
		          int i = Integer.parseInt(args[argsup + 2]);
		        } catch (Exception e) {
		          String str;
		          switch ((str = args[argsup + 2]).hashCode()) {
		            case -1048926120:
		              if (!str.equals("nether"))
		                break; 
		              dim = -1;
		              break;
		            case -745159874:
		              if (!str.equals("overworld"))
		                break; 
		              dim = 0;
		              break;
		            case 3560:
		              if (!str.equals("ow"))
		                break; 
		              dim = 0;
		              break;
		            case 100571:
		              if (!str.equals("end"))
		                break; 
		              dim = 1;
		              break;
		          } 
		          if (dim == -1 && mc.thePlayer.dimension == 0) {
		            x /= 8.0D;
		            z /= 8.0D;
		          } else if (dim == 0 && mc.thePlayer.dimension == -1) {
		            x *= 8.0D;
		            z *= 8.0D;
		          } 
		          argsup++;
		        }  
		      if (args.length == argsup + 3) {
		        y = Integer.parseInt(args[argsup + 2]);
		      } else if (args.length == argsup + 4) {
		        x = Integer.parseInt(args[argsup + 2]);
		        y = 70.0D;
		        z = Integer.parseInt(args[argsup + 3]);
		      } else if (args.length == argsup + 5) {
		        x = Integer.parseInt(args[argsup + 2]);
		        y = Integer.parseInt(args[argsup + 3]);
		        z = Integer.parseInt(args[argsup + 4]);
		      } 
		      net.minecraft.src.MEDMEX.Modules.Render.Waypoints.instance.addPoint(new Vec3D(x, y, z), args[1], currentAddress, dim);
		    } 
		    if (args[0].equalsIgnoreCase("remove") && args.length == 2)
		    	 net.minecraft.src.MEDMEX.Modules.Render.Waypoints.instance.remove(args[1]); 
		
		}catch(Exception e) {
			Client.addChatMessage("Usage: waypoints <add/remove> <name> <x> <y> <z>");
		}
	}
		
	

}
