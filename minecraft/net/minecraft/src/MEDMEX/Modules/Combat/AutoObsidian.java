package net.minecraft.src.MEDMEX.Modules.Combat;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet70Bed;
import net.minecraft.src.Vec3D;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Utils.Timer;
import net.minecraft.src.de.Hero.settings.Setting;

public class AutoObsidian extends Module{
	public static Timer timer = new Timer();
	public static int X;
	public static int Z;
	public static int offsetX;
	public static int offsetZ;
	public AutoObsidian() {
		super("AutoObsidian", Keyboard.KEY_NONE, Category.COMBAT);
	}
	
	public void setup() {
		Client.settingsmanager.rSetting(new Setting("Full Surround", this, false));
		Client.settingsmanager.rSetting(new Setting("Center Player", this, false));
		
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				X = (int)mc.thePlayer.posX;
				Z = (int)mc.thePlayer.posZ;
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
				int bestSlot = -1;
				for(int i = 0; i < 9; i++) {
					int prevItem = mc.thePlayer.inventory.currentItem;
					ItemStack stack =
							mc.thePlayer.inventory.getStackInSlot(i);
						if(stack == null || !(stack.itemID == 49))
							continue;
						bestSlot = i;
						if(timer.hasTimeElapsed(10, false)) {
						mc.thePlayer.inventory.currentItem = bestSlot;
						if(mc.theWorld.isAirBlock(X+1+offsetX, (int)mc.thePlayer.posY - 1, Z+offsetZ)) {
							int[] values = getDir(new Vec3D(X+1+offsetX, mc.thePlayer.posY - 1, Z+offsetZ));
							mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), values[0], values[1], values[2], values[3]);
						}
						if(mc.theWorld.isAirBlock(X-1+offsetX, (int)mc.thePlayer.posY - 1, Z+offsetZ)) {
							int[] values = getDir(new Vec3D(X-1+offsetX, mc.thePlayer.posY - 1, Z+offsetZ));
							mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), values[0], values[1], values[2], values[3]);
						}
						if(mc.theWorld.isAirBlock(X+offsetX, (int)mc.thePlayer.posY - 1, Z+1+offsetZ)) {
							int[] values = getDir(new Vec3D(X+offsetX, mc.thePlayer.posY - 1, Z+1+offsetZ));
							mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), values[0], values[1], values[2], values[3]);
						}
						if(mc.theWorld.isAirBlock(X+offsetX, (int)mc.thePlayer.posY - 1, Z-1+offsetZ)) {
							int[] values = getDir(new Vec3D(X+offsetX, mc.thePlayer.posY - 1, Z-1+offsetZ));
							mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), values[0], values[1], values[2], values[3]);
						}
						if(mc.theWorld.isAirBlock(X+offsetX, (int)mc.thePlayer.posY - 2, Z+offsetZ)) {
							int[] values = getDir(new Vec3D(X+offsetX, mc.thePlayer.posY - 2, Z+offsetZ));
							mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), values[0], values[1], values[2], values[3]);
						}
						if(Client.settingsmanager.getSettingByName("Full Surround").getValBoolean()) {
							if(mc.theWorld.isAirBlock(X+1+offsetX, (int)mc.thePlayer.posY, Z+offsetZ)) {
								int[] values = getDir(new Vec3D(X+1+offsetX, mc.thePlayer.posY, Z+offsetZ));
								mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), values[0], values[1], values[2], values[3]);
							}
							if(mc.theWorld.isAirBlock(X-1+offsetX, (int)mc.thePlayer.posY, Z+offsetZ)) {
								int[] values = getDir(new Vec3D(X-1+offsetX, mc.thePlayer.posY, Z+offsetZ));
								mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), values[0], values[1], values[2], values[3]);
							}
							if(mc.theWorld.isAirBlock(X+offsetX, (int)mc.thePlayer.posY, Z+1+offsetZ)) {
								int[] values = getDir(new Vec3D(X+offsetX, mc.thePlayer.posY, Z+1+offsetZ));
								mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), values[0], values[1], values[2], values[3]);
							}
							if(mc.theWorld.isAirBlock(X+offsetX, (int)mc.thePlayer.posY, Z-1+offsetZ)) {
								int[] values = getDir(new Vec3D(X+offsetX, mc.thePlayer.posY, Z-1+offsetZ));
								mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), values[0], values[1], values[2], values[3]);
							}
							
						}
						
						
						
						mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getStackInSlot(bestSlot));
						mc.thePlayer.inventory.currentItem = prevItem;
						
						timer.reset();
						}
					}
				}
			
			
			
			
			if(Client.settingsmanager.getSettingByName("Center Player").getValBoolean()) {
				 Vec3D pos = GetPlayerPosHighFloored((Entity)this.mc.thePlayer);
			      AxisAlignedBB bb = this.mc.thePlayer.getBoundingBox();
			        Vec3D Center = new Vec3D(pos.xCoord + 0.5D, pos.yCoord, pos.zCoord + 0.5D);
			        double l_XDiff = Math.abs(Center.xCoord - this.mc.thePlayer.posX);
			        double l_ZDiff = Math.abs(Center.zCoord - this.mc.thePlayer.posZ);
			        if (l_XDiff <= 0.1D && l_ZDiff <= 0.1D) {
			          Center = Vec3D.ZERO;
			        } else {
			          double l_MotionX = Center.xCoord - this.mc.thePlayer.posX;
			          double l_MotionZ = Center.zCoord - this.mc.thePlayer.posZ;
			          this.mc.thePlayer.motionX = l_MotionX / 3.0D;
			          this.mc.thePlayer.motionZ = l_MotionZ / 3.0D;
			        } 
			}
				
				
						
					
				
		}
		}
				

			
			
		
		
		public static Vec3D GetPlayerPosHighFloored(Entity p_Player) {
		    return new Vec3D(Math.floor(p_Player.posX), Math.floor(p_Player.posY + 0.2D), Math.floor(p_Player.posZ));
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

