package net.minecraft.src.MEDMEX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.src.MEDMEX.UI.HUD;
import net.minecraft.src.de.Hero.settings.SettingsManager;
import net.minecraft.src.Packet;
import net.minecraft.src.MEDMEX.Commands.CommandManager;
import net.minecraft.src.MEDMEX.Config.Config;
import net.minecraft.src.MEDMEX.Config.ConfigAuthme;
import net.minecraft.src.MEDMEX.Config.ConfigDrawn;
import net.minecraft.src.MEDMEX.Config.ConfigFriends;
import net.minecraft.src.MEDMEX.Config.ConfigWaypoints;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventChat;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Modules.Client.*;
import net.minecraft.src.MEDMEX.Modules.Module.Category;
import net.minecraft.src.MEDMEX.Modules.Combat.*;
import net.minecraft.src.MEDMEX.Modules.Movement.*;
import net.minecraft.src.MEDMEX.Modules.Player.*;
import net.minecraft.src.MEDMEX.Modules.Render.*;
import net.minecraft.src.MEDMEX.Modules.World.*;

public class Client {
	public static int protocolver = 14;
	public static String name = "Ye Loves You", version = "7";
	public static CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<Module>();
	public static CopyOnWriteArrayList<String> friends = new CopyOnWriteArrayList<String>();
	public static CopyOnWriteArrayList<String> authme = new CopyOnWriteArrayList<String>();
	public static CopyOnWriteArrayList<Module> drawn = new CopyOnWriteArrayList<Module>();
	public static HUD hud = new HUD();
	public static CommandManager commandManager = new CommandManager();
	public static SettingsManager settingsmanager;
	public static net.minecraft.src.de.Hero.clickgui.ClickGUI clickgui;

	public static void startup(){
		settingsmanager = new SettingsManager();
		
		modules.add(new Fly());
		modules.add(new Fullbright());
		modules.add(new Strafe());
		modules.add(new Packetmine());
		modules.add(new Freecam());
		modules.add(new Coords());
		modules.add(new Xray());
		modules.add(new ColoredSigns());
		modules.add(new Jesus());
		modules.add(new Scaffold());
		modules.add(new NoFall());
		modules.add(new ClickGUI());
		modules.add(new ArmorStatus());
		modules.add(new Waypoints());
		modules.add(new AutoEat());
		modules.add(new ForceField());
		modules.add(new NoClip());
		modules.add(new ViewClip());
		modules.add(new ChestESP());
		modules.add(new VisualRange());
		modules.add(new Nametags());
		modules.add(new NewChunks());
		modules.add(new ESP());
		modules.add(new InventoryMove());
		modules.add(new AntiKB());
		modules.add(new FastPlace());
		modules.add(new NoWeather());
		modules.add(new Step());
		modules.add(new FastDrop());
		modules.add(new NoRender());
		modules.add(new Timer());
		modules.add(new VelocityManip());
		modules.add(new Tracers());
		modules.add(new Search());
		modules.add(new SecretChat());
		modules.add(new TabList());
		modules.add(new ItemDeleter());
		modules.add(new InstaMine());
		modules.add(new Speed());
		modules.add(new MCF());
		modules.add(new NoCollide());
		modules.add(new BedAura());
		modules.add(new AutoObsidian());
		modules.add(new KillSults());
		modules.add(new Nuker());
		modules.add(new LiquidInteract());
		modules.add(new TargetStrafe());
		modules.add(new Chams());
		modules.add(new Tower());
		modules.add(new AutoTunnel());
		modules.add(new AutoWalk());
		modules.add(new Yaw());
		modules.add(new AutoAuthme());
		modules.add(new AutoReconnect());
		modules.add(new CombatLog());
		modules.add(new BreakProgress());
		modules.add(new AntiDesync());
		modules.add(new NoHurtCam());
		modules.add(new ChatTime());
		modules.add(new Backfill());
		modules.add(new PacketCancel());
		modules.add(new PacketLogger());
		modules.add(new BucketFix());
		modules.add(new AutoHighway());
		modules.add(new AutoTNT());
		modules.add(new AutoTool());
		modules.add(new Fat());
		try {
		Config.load();
		}catch(Exception e) {
			
		}
		ConfigWaypoints.load();
		ConfigFriends.load();
		ConfigAuthme.load();
		ConfigDrawn.load();
		clickgui = new net.minecraft.src.de.Hero.clickgui.ClickGUI();
		
		
		
		System.out.println("Loading "+ name +" "+ version);
	}
	
public static void onEvent(Event e) {
		
		if(e instanceof EventChat) {
			commandManager.handleChat((EventChat)e);
			
		}
		
		for(Module m: modules) {
			if(!m.toggled)
				continue;
			
			m.onEvent(e);
		}
	}

public static void sendPacket(Packet p) {
	Minecraft.theMinecraft.getSendQueue().addToSendQueue(p);
}

public static void keyPress(int key) {
	for(Module m : modules) {
		if(key == m.getKey()) {
			m.toggle();
		}
	}
	
}

public static String onMessage(String s) {
	for(Module m : modules) {
		m.onMessage(s);
	}
	return s;
}

public static void onRenderGUI() {
	  for(Module m : modules) {
		  if(!m.toggled)
			  continue;
		  m.onRenderGUI();
	  }
	  
}
public static void onRenderEntities() {
	  for(Module m : modules) {
		  if(!m.toggled)
			  continue;
		  m.onRender();
}
}

public static void getPacket(EventPacket e) {
	  for(Module m : modules) {
		  if(!m.toggled)
			  continue;
		  m.getPacket(e);
	  }
}

public static List<Module> getModuleByCategory(Category c){
	List<Module> modules = new ArrayList<Module>();
	for(Module m : Client.modules) {
		if(m.category == c)
			modules.add(m);
	}
	return modules;
	
}

public static List<Module> getModules(){
	List<Module> modules = new ArrayList<Module>();
	for(Module m : Client.modules) {
			modules.add(m);
	}
	
	return modules;
	
}

public static void addChatMessage(String message) {
	message = "\2474>\u00A77" + message;
	Minecraft.theMinecraft.thePlayer.addChatMessage(new String(message));
}
}
