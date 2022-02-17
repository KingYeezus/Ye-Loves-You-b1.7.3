package net.minecraft.src.MEDMEX.Modules.World;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Gui;
import net.minecraft.src.Item;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet102WindowClick;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet28EntityVelocity;
import net.minecraft.src.Packet3Chat;
import net.minecraft.src.Packet70Bed;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.de.Hero.settings.Setting;


public class BucketFix extends Module{

	public static BucketFix instance;

	public BucketFix() {
		super("BucketFix", Keyboard.KEY_NONE, Category.WORLD);

		instance = this;
	}
	
	public void onEnable() {
	System.out.println(mc.thePlayer.inventory.getStackInSlot(0));
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				
				
				if(mc.thePlayer.inventory.getCurrentItem() != null  && mc.thePlayer.inventory.getCurrentItem().getItem().equals(Item.bucketEmpty)) {
					mc.thePlayer.dropCurrentItem();
				}
				
				if(mc.thePlayer.inventory.getCurrentItem() == null) {
					int bestSlot = -1;
				for(int i = 0; i < 9; i++) {
					int prevItem = mc.thePlayer.inventory.currentItem;
					ItemStack stack =
							mc.thePlayer.inventory.getStackInSlot(i);
						if(stack == null || (!(stack.getItem() == Item.bucketLava) || (stack.stackSize == 1)))
							continue;
						bestSlot = i;
						if(stack.stackSize != -128) {
						if(mc.thePlayer.inventory.getStackInSlot(bestSlot + 1) == null) {
						mc.playerController.handleMouseClick(0, bestSlot + 36, 0, false, mc.thePlayer);
						mc.playerController.handleMouseClick(0, bestSlot+37, 1, false, mc.thePlayer);
						mc.playerController.handleMouseClick(0, bestSlot+36, 0, false, mc.thePlayer);
						}
						}
						
						
						
				}
				}
				
			}
		}
	}
}
