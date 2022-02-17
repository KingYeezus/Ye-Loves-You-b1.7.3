package net.minecraft.src.MEDMEX.Utils;

import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemBucket;
import net.minecraft.src.ItemPickaxe;
import net.minecraft.src.ItemStack;

public class InventoryUtils {
	
	public static int getHotbarslotItem(int item) {
		Minecraft mc = Minecraft.theMinecraft;
		for(int i = 0; i < 9; i++) {
			ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
			if(stack != null && stack.itemID == item) 
				return i;
		}
		return -1;
	}	
	
	public static int getHotbarslotBlocks() {
		Minecraft mc = Minecraft.theMinecraft;
		for(int i = 0; i < 9; i++) {
			ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
			
			if(stack != null && stack.getItem() instanceof ItemBlock)
				return i;
		}
		return -1;
	}
	
	public static int getHotbarslotPickaxe() {
		Minecraft mc = Minecraft.theMinecraft;
		for(int i = 0; i < 9; i++) {
			ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
			
			if(stack != null && stack.getItem() instanceof ItemPickaxe)
				return i;
		}
		return -1;
	}
	
	public static int getAmountInInventory(int ItemID) {
		Minecraft mc = Minecraft.theMinecraft;
		int amount = 0;
		for(int i = 0; i < 36; i++) {
			if(mc.thePlayer.inventory.mainInventory[i] != null && mc.thePlayer.inventory.mainInventory[i].itemID == ItemID) {
				amount++;
			}
		}
		return amount;
	}
	
	public static int findItemInInventory(int ItemID) {
		Minecraft mc = Minecraft.theMinecraft;
		for(int i = 0; i < 36; i++) {
			if(mc.thePlayer.inventory.mainInventory[i] != null && mc.thePlayer.inventory.mainInventory[i].itemID == ItemID) {
				return i;
			}
		}
		return -1;
		
	}
	
	public static int findItemStackInInventory(ItemStack is) {
		Minecraft mc = Minecraft.theMinecraft;
		for(int i = 0; i < 36; i++) {
			if(mc.thePlayer.inventory.mainInventory[i] != null && mc.thePlayer.inventory.mainInventory[i] == is) {
				return i;
			}
		}
		return -1;
		
	}

}
