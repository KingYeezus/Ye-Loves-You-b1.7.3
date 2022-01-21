package net.minecraft.src.MEDMEX.Modules.World;

import java.awt.Color;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Packet14BlockDig;
import net.minecraft.src.Packet15Place;
import net.minecraft.src.RenderManager;
import net.minecraft.src.Vec3D;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Utils.RenderUtils;
import net.minecraft.src.MEDMEX.Utils.Timer;
import net.minecraft.src.de.Hero.settings.Setting;

public class AutoTunnel extends Module{
	public static AutoTunnel instance;
	int offsetX = 0, offsetZ = 0;
	Vec3D target;
	public AutoTunnel() {
		super("AutoTunnel", Keyboard.KEY_NONE, Category.WORLD);
		instance = this;
	}
	
	public static CopyOnWriteArrayList<Integer> sentx = new CopyOnWriteArrayList<Integer>();
	public static CopyOnWriteArrayList<Integer> senty = new CopyOnWriteArrayList<Integer>();
	public static CopyOnWriteArrayList<Integer> sentz = new CopyOnWriteArrayList<Integer>();
	
	public void setup() {
		Client.settingsmanager.rSetting(new Setting("Backfill", this, false));
	}
	
	Timer timer = new Timer();
	Timer t1 = new Timer();
	Timer t2 = new Timer();
	Timer t3 = new Timer();
	
	public boolean isExposed(int x, int y, int z) {
		if(mc.theWorld.isAirBlock(x+1, y, z))
			return true;
		if(mc.theWorld.isAirBlock(x-1, y, z))
			return true;
		if(mc.theWorld.isAirBlock(x, y, z+1))
			return true;
		if(mc.theWorld.isAirBlock(x, y, z-1))
			return true;
		if(mc.theWorld.isAirBlock(x, y+1, z))
			return true;
		if(mc.theWorld.isAirBlock(x, y-1, z))
			return true;
		return false;
	}
	
	int X, Y, Z , oX, oZ, nX, nZ;
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {			
				offsets();
				directions();	
				if(Client.settingsmanager.getSettingByName("Backfill").getValBoolean())
					silentplace();
				

				if(sentx.size() > 100) {
					sentx.remove(0);
					senty.remove(0);
					sentz.remove(0);
				}
				
				for(int i = 1; i < 4; i++) {
				if(!mc.theWorld.isAirBlock(X+(oX*i)+offsetX, Y, Z+(oZ*i)+offsetZ) || !mc.theWorld.isAirBlock(X+(oX*i)+offsetX, Y-1, Z+(oZ*i)+offsetZ)) {
				if(!(mc.theWorld.isAirBlock(X+(oX*i)+offsetX, Y, Z+(oZ*i)+offsetZ)) && !(mc.theWorld.isAirBlock(X+(oX*i)+offsetX, Y-1, Z+(oZ*i)+offsetZ))) {

					target = new Vec3D(X+(oX*i)+offsetX, Y, Z+(oZ*i)+offsetZ);
					mc.getSendQueue().addToSendQueue(new Packet14BlockDig(0, X+(oX*i)+offsetX, Y, Z+(oZ*i)+offsetZ, 1));
					mc.getSendQueue().addToSendQueue(new Packet14BlockDig(2,  X+(oX*i)+offsetX, Y, Z+(oZ*i)+offsetZ, 1));
					
				}
				else if(mc.theWorld.isAirBlock(X+(oX*i)+offsetX, Y, Z+(oZ*i)+offsetZ)) {
					target = new Vec3D(X+(oX*i)+offsetX, Y-1, Z+(oZ*i)+offsetZ);
						mc.getSendQueue().addToSendQueue(new Packet14BlockDig(0, X+(oX*i)+offsetX, Y-1, Z+(oZ*i)+offsetZ, 1));
						mc.getSendQueue().addToSendQueue(new Packet14BlockDig(2,  X+(oX*i)+offsetX, Y-1, Z+(oZ*i)+offsetZ, 1));
					
					}
				
				
				else if(mc.theWorld.isAirBlock(X+(oX*i)+offsetX, Y-1, Z+(oZ*i)+offsetZ)) {
					target = new Vec3D(X+(oX*i)+offsetX, Y, Z+(oZ*i)+offsetZ);
						mc.getSendQueue().addToSendQueue(new Packet14BlockDig(0, X+(oX*i)+offsetX, Y, Z+(oZ*i)+offsetZ, 1));
						mc.getSendQueue().addToSendQueue(new Packet14BlockDig(2,  X+(oX*i)+offsetX, Y, Z+(oZ*i)+offsetZ, 1));
					
				}
					
					
				}
				
				if(mc.theWorld.isAirBlock(X+offsetX, Y-2, Z+offsetZ)) {
					int bestSlot = -1;
					for(int j = 0; i < 9; i++) {
						int prevItem = mc.thePlayer.inventory.currentItem;
						ItemStack stack =
								mc.thePlayer.inventory.getStackInSlot(i);
							if(stack == null || !(stack.getItem() instanceof ItemBlock))
								continue;
							bestSlot = i;
							mc.thePlayer.inventory.currentItem = bestSlot;
							int[] values = getDir(new Vec3D(X+offsetX, Y-2, Z+offsetZ));
							mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), values[0], values[1], values[2], values[3]);
							mc.thePlayer.inventory.currentItem = prevItem;
						}
				}
				if(mc.theWorld.isAirBlock(X+offsetX+nX, Y-2, Z+offsetZ+nZ)) {
					int bestSlot = -1;
					for(int j = 0; i < 9; i++) {
						int prevItem = mc.thePlayer.inventory.currentItem;
						ItemStack stack =
								mc.thePlayer.inventory.getStackInSlot(i);
							if(stack == null || !(stack.getItem() instanceof ItemBlock))
								continue;
							bestSlot = i;
							mc.thePlayer.inventory.currentItem = bestSlot;
							int[] values = getDir(new Vec3D(X+offsetX+nX, Y-2, Z+offsetZ+nZ));
							mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), values[0], values[1], values[2], values[3]);
							mc.thePlayer.inventory.currentItem = prevItem;
						}
				}
				
				}
				
			}			
		}			
	}
	
	public void onRender() {
		if(this.isEnabled()) {
			if(target != null) {
				AxisAlignedBB bb = new AxisAlignedBB(target.xCoord, target.yCoord, target.zCoord, target.xCoord + 1.0D, target.yCoord + 1.0D, target.zCoord + 1.0D);
		    	RenderUtils.boundingESPBox(bb, new Color(255, 255, 255));
		    	if(mc.theWorld.isAirBlock((int)target.xCoord, (int)target.yCoord, (int)target.zCoord))
					target = null;
			}
		}
	}
	
	public void offsets() {
		if(mc.thePlayer.posX < 0 && mc.thePlayer.posZ < 0) {
			offsetX = -1;
			offsetZ = -1;	
		}
		if(mc.thePlayer.posX > 0 && mc.thePlayer.posZ > 0) {
			offsetX = 0;
			offsetZ = 0;
		}
		if(mc.thePlayer.posX > 0 && mc.thePlayer.posZ < 0) {
			offsetX = 0;
			offsetZ = -1;
		}
		if(mc.thePlayer.posX < 0 && mc.thePlayer.posZ > 0) {
			offsetX = -1;
			offsetZ =  0;
		}
	}
	
	public void directions() {
		int direction = MathHelper.floor_double((double)(this.mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		X = (int)mc.thePlayer.posX;
		Y = (int)mc.thePlayer.posY;
		Z = (int)mc.thePlayer.posZ;
		//South
		if(direction == 0) {
			oZ = 1;
			oX = 0;
			nZ = -1;
			nX =0;
		}
		//West
		if(direction == 1) {
			oX = -1;
			oZ = 0;
			nX = 1;
			nZ = 0;
		}
		//North
		if(direction == 2) {
			oZ = -1;
			oX = 0;
			nZ = 1;
			nX = 0;
		}
		//East
		if(direction == 3) {
			oX = 1;
			oZ = 0;
			nX = -1;
			nZ = 0;
		}
	}
	
	public void silentplace() {
		if(mc.theWorld.isAirBlock(X+nX+offsetX, Y, Z+nZ+offsetZ)) {
			int bestSlot = -1;
			for(int i = 0; i < 9; i++) {
				int prevItem = mc.thePlayer.inventory.currentItem;
				ItemStack stack =
						mc.thePlayer.inventory.getStackInSlot(i);
					if(stack == null || !(stack.getItem() instanceof ItemBlock))
						continue;
					bestSlot = i;
					mc.thePlayer.inventory.currentItem = bestSlot;
					int[] values = getDir(new Vec3D(X+nX+offsetX, Y, Z+nZ+offsetZ));
					if(mc.thePlayer.posX - Math.floor(mc.thePlayer.posX) > 0.3 && mc.thePlayer.posX - Math.floor(mc.thePlayer.posX) < 0.7 && mc.thePlayer.posZ - Math.floor(mc.thePlayer.posZ) > 0.3 && mc.thePlayer.posZ - Math.floor(mc.thePlayer.posZ) < 0.7)
					mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), values[0], values[1], values[2], values[3]);
					mc.thePlayer.inventory.currentItem = prevItem;
					
	
			}
			}
		
		if(mc.theWorld.isAirBlock(X+nX+offsetX, Y-1, Z+nZ+offsetZ)) {
			int bestSlot = -1;
			for(int i = 0; i < 9; i++) {
				int prevItem = mc.thePlayer.inventory.currentItem;
				ItemStack stack =
						mc.thePlayer.inventory.getStackInSlot(i);
					if(stack == null || !(stack.getItem() instanceof ItemBlock))
						continue;
					bestSlot = i;
					mc.thePlayer.inventory.currentItem = bestSlot;
					int[] values = getDir(new Vec3D(X+nX+offsetX, Y-1, Z+nZ+offsetZ));
					if(mc.thePlayer.posX - Math.floor(mc.thePlayer.posX) > 0.3 && mc.thePlayer.posX - Math.floor(mc.thePlayer.posX) < 0.7 && mc.thePlayer.posZ - Math.floor(mc.thePlayer.posZ) > 0.3 && mc.thePlayer.posZ - Math.floor(mc.thePlayer.posZ) < 0.7)
					mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), values[0], values[1], values[2], values[3]);
					mc.thePlayer.inventory.currentItem = prevItem;
					}
				
		
		}
			
			
		}
	
	
	
	
	
	public int[] getDir(Vec3D blockpos) {
		if(!mc.theWorld.isAirBlock((int)blockpos.xCoord, (int)blockpos.yCoord-1, (int)blockpos.zCoord)) {
			int[] values = {(int)blockpos.xCoord+0,(int)blockpos.yCoord+-1,(int)blockpos.zCoord+0,1};
			return values;
 		}
		if(!mc.theWorld.isAirBlock((int)blockpos.xCoord+1, (int)blockpos.yCoord, (int)blockpos.zCoord)) {
			int[] values = {(int)blockpos.xCoord+1,(int)blockpos.yCoord+0,(int)blockpos.zCoord+0,4};
			return values;
 		}
		if(!mc.theWorld.isAirBlock((int)blockpos.xCoord-1, (int)blockpos.yCoord, (int)blockpos.zCoord)) {
			int[] values = {(int)blockpos.xCoord-1,(int)blockpos.yCoord+0,(int)blockpos.zCoord+0,5};
			return values;
 		}
		if(!mc.theWorld.isAirBlock((int)blockpos.xCoord, (int)blockpos.yCoord, (int)blockpos.zCoord+1)) {
			int[] values = {(int)blockpos.xCoord+0,(int)blockpos.yCoord+0,(int)blockpos.zCoord+1,2};
			return values;
 		}
		if(!mc.theWorld.isAirBlock((int)blockpos.xCoord, (int)blockpos.yCoord, (int)blockpos.zCoord-1)) {
			int[] values = {(int)blockpos.xCoord+0,(int)blockpos.yCoord+0,(int)blockpos.zCoord-1,3};
			return values;
 		}
		if(!mc.theWorld.isAirBlock((int)blockpos.xCoord, (int)blockpos.yCoord - 1, (int)blockpos.zCoord)) {
			
			int[] values = {(int)blockpos.xCoord+0,(int)blockpos.yCoord-1,(int)blockpos.zCoord, 0};
			return values;
 		}
		
		int[] values = {0,0,0,0};
		return values;
		
	}
	
	

	
	
	
}
