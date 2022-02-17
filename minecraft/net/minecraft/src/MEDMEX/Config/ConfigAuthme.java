package net.minecraft.src.MEDMEX.Config;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.src.Vec3D;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Modules.Render.Waypoints;
import net.minecraft.src.MEDMEX.Modules.Render.Waypoints.WayPoint;



public class ConfigAuthme {
	
	static String filedir = Client.name + "\\";
	static String configfiledir = Client.name+"\\authme";
	public static String input = "authme";
	
	public static void load(){
		String config = Client.name+"\\"+input;
		createFiles();
		//Dont forget to put it in Client.startup
		try {
			System.out.println(config);
			String authme = "";
			BufferedReader reader = new BufferedReader(new FileReader(new File(config)));
			while((authme = reader.readLine()) != null){
				String a = authme;
				Client.authme.add(a);
				
			}
				
				
			
			reader.close();
		}	catch(IOException e) {
					e.printStackTrace();
				}
	}
	
	public static void save(){
		String config = Client.name+"\\"+input;
		createFiles();
		//Dont forget to put it in Minecraft.shutdown
		
		try(FileWriter writer = new FileWriter(new File(config), false))
        {
			
			for(String a : Client.authme) {
				writer.write(a+"\n");
			}
			
            writer.flush();
            writer.close();
        }
        catch(IOException ex){
             
            System.out.println(ex.getMessage());
        } 
	}
	
	public static void createFiles(){
		if(!new File(filedir).exists()){
			new File(filedir).mkdir();
		}
		
		if(!new File(configfiledir).exists()){
			try {
			new File(configfiledir).createNewFile();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	

}
