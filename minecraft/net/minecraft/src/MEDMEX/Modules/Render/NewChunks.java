package net.minecraft.src.MEDMEX.Modules.Render;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Utils.RenderUtils;
import net.minecraft.src.de.Hero.settings.Setting;
import net.minecraft.src.Chunk;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet51MapChunk;
import net.minecraft.src.Packet53BlockChange;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.RenderManager;

public class NewChunks extends Module {
	public static NewChunks instance;
	
	
	public NewChunks() {
		super("NewChunks", Keyboard.KEY_NONE, Category.RENDER);
		instance = this;
		
	}
	
	public void setup() {
		Client.settingsmanager.rSetting(new Setting("Newchunks Height", this, 0, 0, 130, true));
	}
	
	
	public void getPacket(EventPacket e) {
		if(this.isEnabled()) {
			if(mc.thePlayer != null && mc.theWorld != null) {
				if(e.getPacket() instanceof Packet53BlockChange) {
					Packet53BlockChange packet = (Packet53BlockChange)e.getPacket();
					if(packet.metadata == 8 || packet.metadata == 9 || packet.metadata == 10 || packet.metadata == 11) {
					int x = MathHelper.floor_double(packet.xPosition);
				    int z = MathHelper.floor_double(packet.zPosition);
				    int chunkx = Integer.valueOf(x & 15);
				    int chunkz = Integer.valueOf(z & 15);
		        	int newchunkx = chunkx*16-8;
		        	int newchunkz = chunkz*16-8;
		        	RenderGlobal.x.add(newchunkx);
		        	RenderGlobal.z.add(newchunkz);
					}
					
					
					
				}
			}
		}
			
		}
}
