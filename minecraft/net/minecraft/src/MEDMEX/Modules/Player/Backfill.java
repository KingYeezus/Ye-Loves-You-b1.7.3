package net.minecraft.src.MEDMEX.Modules.Player;

import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiDownloadTerrain;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Packet0KeepAlive;
import net.minecraft.src.Packet101CloseWindow;
import net.minecraft.src.Packet103SetSlot;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet11PlayerPosition;
import net.minecraft.src.Packet12PlayerLook;
import net.minecraft.src.Packet13PlayerLookMove;
import net.minecraft.src.Packet15Place;
import net.minecraft.src.Packet16BlockItemSwitch;
import net.minecraft.src.Vec3D;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Utils.Timer;
import net.minecraft.src.de.Hero.settings.Setting;

public class Backfill extends Module{
	public static Backfill instance;
	int X, Y , Z, nZ, nX;
	public Backfill() {
		super("Backfill", Keyboard.KEY_NONE, Category.PLAYER);
		instance = this;
	}
	
	public void directions() {
		int direction = MathHelper.floor_double((double)(this.mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		X = (int)mc.thePlayer.posX;
		Y = (int)mc.thePlayer.posY;
		Z = (int)mc.thePlayer.posZ;
		//South
		if(direction == 0) {
			nZ = -1;
			nX =0;
		}
		//West
		if(direction == 1) {
			nX = 1;
			nZ = 0;
		}
		//North
		if(direction == 2) {
			nZ = 1;
			nX = 0;
		}
		//East
		if(direction == 3) {
			nX = -1;
			nZ = 0;
		}
	}
	
	public boolean canPlaceBlock(int x, int y, int z) {
        int id = mc.theWorld.getBlockId(x, y, z);
        return id == 0 || id == 10 || id == 11 || id == 8 || id == 9;
    }
	
	public void placeBlock(int x, int y, int z) {
        if(!canPlaceBlock(x-1, y, z)) {
            mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), x-1, y, z, 5);
            return;
        }
        if(!canPlaceBlock(x+1, y, z)) {
            mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), x+1, y, z, 4);
            return;
        }
        if(!canPlaceBlock(x, y, z-1)) {
            mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), x, y, z-1, 3);
            return;
        }
        if(!canPlaceBlock(x, y, z+1)) {
            mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), x, y, z+1, 2);
            return;
        }
        if(!canPlaceBlock(x, y-1, z)) {
            mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), x, y-1, z, 1);
            return;
        }
        if(!canPlaceBlock(x, y+1, z)) {
            mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), x, y-1, z, 0);
            return;
        }
    }
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
			   		directions();
			   		for(int i = -3; i < 3; i++) {
			   			for(int j = -3; j < 3 ; j++) {
			   				if(mc.thePlayer.ticksExisted % 6 == 0) {
			
			             	     placeBlock(X+(nX+(i*nZ)), Y+j, Z+(nZ+(i*nX)));
			   				
			   			}
			   		}
			   	}
			}
		}	
	}
}
