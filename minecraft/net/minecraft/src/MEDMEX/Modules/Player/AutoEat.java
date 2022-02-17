package net.minecraft.src.MEDMEX.Modules.Player;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiDownloadTerrain;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet0KeepAlive;
import net.minecraft.src.Packet101CloseWindow;
import net.minecraft.src.Packet103SetSlot;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet11PlayerPosition;
import net.minecraft.src.Packet12PlayerLook;
import net.minecraft.src.Packet13PlayerLookMove;
import net.minecraft.src.Packet15Place;
import net.minecraft.src.Packet16BlockItemSwitch;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Utils.Timer;

public class AutoEat extends Module{
	public static AutoEat instance;
	public static double x, y, z;
	public static Timer timer = new Timer();
	public AutoEat() {
		super("AutoEat", Keyboard.KEY_NONE, Category.PLAYER);
		instance = this;
	}
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				if(mc.thePlayer.health < 20) {
					int bestSlot = -1;
					for(int i = 0; i < 9; i++) {
						int prevItem = mc.thePlayer.inventory.currentItem;
						ItemStack stack =
								mc.thePlayer.inventory.getStackInSlot(i);
							if(stack == null || !(stack.getItem() instanceof ItemFood))
								continue;
							bestSlot = i;
							if(timer.hasTimeElapsed(10, false)) {
							//mc.thePlayer.inventory.currentItem = bestSlot;
							mc.thePlayer.inventory.currentItem = bestSlot;
							mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getStackInSlot(bestSlot));
							mc.thePlayer.inventory.currentItem = prevItem;
							
							timer.reset();
							}
					}
						}
				}
			}
		}
	
	
	
	
	
	}
